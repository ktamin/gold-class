package lineage.world.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import lineage.bean.database.Item;
import lineage.network.LineageServer;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Disconnect;
import lineage.share.Lineage;
import lineage.world.object.instance.AfkInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class AfkController {
	public static void init() {}
	static private Map<Integer, AfkInstance> afk_list = new HashMap<Integer, AfkInstance>();
	
	// 무인잠수 로봇들의 시간 타이머
	static private Timer afkTimer = new Timer("AfkTimer", true);
	static {
		afkTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					long currentTime = System.currentTimeMillis();
					for (AfkInstance ai : afk_list.values().toArray(new AfkInstance[0])) {
						if (ai != null) {
							ai.toTimer(currentTime);
						}
					}
				} catch (Exception e) {}
			}
		}, 1000, 1000);
	}

	public static void appendAfkRobot(int uid, AfkInstance ai) { afk_list.put(uid, ai); }
	public static void removeAfkRobot(int uid) { afk_list.remove(uid); }
	public static AfkInstance getAfkRobot(int uid) { return afk_list.get(uid); }

	public static void startAutoAfk(PcInstance pc) {
		if (getAfkRobot(pc.getAccountUid()) != null || FishingController.getFishRobot(pc.getAccountUid()) != null) {
			ChattingController.toChatting(pc, "이미 계정에 자동낚시 또는 무인잠수 중인 캐릭터가 존재합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if (!pc.isLock() && !pc.isWorldDelete() && !pc.isDead()) {
			ChattingController.toChatting(pc, "무인 잠수 모드로 전환되며 접속이 종료됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			try {
				new AfkInstance(pc);
			} catch (Exception e) {}

			try {
				pc.toSender(S_Disconnect.clone(BasePacketPooling.getPool(S_Disconnect.class), 0x0A));
				LineageServer.close(pc.getClient());
			} catch (Exception e) {}
		}
	}

	/**
	 * 게임 접속 시 무인잠수 로봇 확인 및 지연 보상 지급 (3초 대기)
	 */
	static public void toWorldJoin(Object o) {
		if (o instanceof PcInstance) {
			final PcInstance pc = (PcInstance) o;
			final AfkInstance ai = AfkController.getAfkRobot(pc.getAccountUid());
			
			if (ai != null && ai.getPc_objectId() == pc.getObjectId()) {
				
				// 1. 주인이 접속했으므로 다른 사람이 못 들어오게 로봇 명단에서 즉시 삭제
				AfkController.removeAfkRobot(pc.getAccountUid());
				
				// 💡 [중요 추가] DB(character_afk)에서도 해당 로봇의 기록을 완전히 삭제하여 로그(찌꺼기)를 남기지 않음!
				lineage.database.AfkDatabase.delete(pc.getAccountUid());
				
				// 💡 [추가된 마법의 계산식]
				// 현재 시간 - 잠수 시작 시간 = 총 잠수 시간(밀리초)
				long elapsed = System.currentTimeMillis() - ai.getStartTime(); 
				
				long totalSeconds = elapsed / 1000;      // 초로 변환
				long totalMinutes = totalSeconds / 60;   // 분으로 변환
				long hours = totalMinutes / 60;          // 시간으로 변환
				long remainMinutes = totalMinutes % 60;  // 남은 분 계산
				
				lineage.world.controller.ChattingController.toChatting(pc, 
					"무인잠수를 무사히 마치고 복귀하셨습니다. (총 잠수시간: " + hours + "시간 " + remainMinutes + "분)", 
					lineage.share.Lineage.CHATTING_MODE_MESSAGE);
				
				// 2. 💡 [핵심 해결] 본캐릭터 가방(Inventory)이 완벽히 DB에서 로딩되기를 3초(3000ms) 기다립니다.
				new java.util.Timer().schedule(new java.util.TimerTask() {
					@Override
					public void run() {
						try {
							// 3초 뒤, 혹시라도 가방 로딩이 안 됐거나 로봇 가방이 비어있으면 안전하게 중단
							if (pc.getInventory() == null || ai.getInventory() == null) return;
							
							boolean getItem = false;
							java.util.List<ItemInstance> botList = ai.getInventory().getList();
							
							if (botList != null) {
								for (ItemInstance botItem : botList) {
									if (botItem != null && botItem.getItem() != null && botItem.getCount() > 0) {
										
										if (botItem.getItem().isPiles()) {
											ItemInstance pcItem = pc.getInventory().find(botItem.getItem().getName(), botItem.getBless(), true);
											if (pcItem == null) {
												ItemInstance newItem = lineage.database.ItemDatabase.newInstance(botItem.getItem());
												newItem.setObjectId(lineage.database.ServerDatabase.nextItemObjId());
												newItem.setCount(botItem.getCount());
												newItem.setBless(botItem.getBless());
												newItem.setEnLevel(botItem.getEnLevel());
												newItem.setDefinite(true);
												pc.getInventory().append(newItem, true); // 완벽히 로딩된 가방에 지급!
											} else {
												pc.getInventory().count(pcItem, pcItem.getCount() + botItem.getCount(), true);
											}
										} else {
											for (int i = 0; i < botItem.getCount(); i++) {
												ItemInstance newItem = lineage.database.ItemDatabase.newInstance(botItem.getItem());
												newItem.setObjectId(lineage.database.ServerDatabase.nextItemObjId());
												newItem.setCount(1);
												newItem.setBless(botItem.getBless());
												newItem.setEnLevel(botItem.getEnLevel());
												newItem.setDefinite(true);
												pc.getInventory().append(newItem, true);
											}
										}
										getItem = true;
									}
								}
							}
							
							if (getItem) {
//								ChattingController.toChatting(pc, "무인잠수 중 획득한 보상이 인벤토리로 지급되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
							}
							
							// 임무를 완수한 로봇 데이터 폭파 및 메모리 정리
							ai.getInventory().close();
							ai.clearList(true);
							lineage.world.World.remove(ai);
							ai.close();
							
						} catch (Exception e) {
							lineage.share.System.println("AfkController 3초 지연 지급 오류: " + e.getMessage());
						}
					}
				}, 2000); // 3000ms = 3초 대기
			}
		}
	}

	static public boolean toWorldOut(Object o) {
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			AfkInstance ai = AfkController.getAfkRobot(pc.getAccountUid());
			if (ai != null && ai.getPc_objectId() == pc.getObjectId()) {
				return true;
			}
		}
		return false;
	}
}