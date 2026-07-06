package lineage.world.object.npc;

import all_night.Lineage_Balance;
import java.util.ArrayList;
import java.util.List;
import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI.ToastType;

public class 마안합성사 extends object {
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "maanNpc"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
		if (pc.isWorldDelete() || pc.isDead() || pc.isLock() || pc.getInventory() == null) return;

		// ==========================================
		// 1. 인벤토리에서 마안 재료 찾기
		// ==========================================
		ItemInstance 수룡 = null, 풍룡 = null, 지룡 = null, 화룡 = null, 탄생 = null, 형상 = null;
		for (ItemInstance item : pc.getInventory().getList()) {
			String name = item.getItem().getName();
			if (수룡 == null && name.equalsIgnoreCase("수룡의 마안")) 수룡 = item;
			else if (풍룡 == null && name.equalsIgnoreCase("풍룡의 마안")) 풍룡 = item;
			else if (지룡 == null && name.equalsIgnoreCase("지룡의 마안")) 지룡 = item;
			else if (화룡 == null && name.equalsIgnoreCase("화룡의 마안")) 화룡 = item;
			else if (탄생 == null && name.equalsIgnoreCase("탄생의 마안")) 탄생 = item;
			else if (형상 == null && name.equalsIgnoreCase("형상의 마안")) 형상 = item;
		}

		String itemName = null;      // 지급될 아이템 (성공 마안 또는 실패 페이백)
		ItemInstance mat1 = null;    // 차감할 재료 1
		ItemInstance mat2 = null;    // 차감할 재료 2
		int reqAdena = 0;            // 소모 아데나 수량
		String reqAdenaName = "";    // 소모 아데나 이름

		// (마안 합성에 공통으로 사용할 시간 및 유저 정보 미리 정의)
				final String timeString = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
				final String charName = pc.getName();

				// ==========================================
				// 2. 탄생의 마안 합성
				// ==========================================
				if (action.equalsIgnoreCase("탄생의 마안")) {
					if (수룡 == null || 지룡 == null) {
						ChattingController.toChatting(pc, "수룡 또는 지룡의 마안이 부족합니다.", Lineage.CHATTING_MODE_MESSAGE); 
						return;
					}
					
					reqAdena = Lineage_Balance.maan_birth_aden_count;
					reqAdenaName = Lineage_Balance.maan_birth_aden_name;
					mat1 = 수룡;
					mat2 = 지룡;

					// 아데나 사전 검사
					if (reqAdena > 0 && !pc.getInventory().isAden(reqAdenaName, reqAdena, true)) {
						ChattingController.toChatting(pc, reqAdenaName + "이(가) 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}

					// 확률 계산 및 안내
					double currentChance = Lineage_Balance.maan_birth_percent + pc.maanBonusBirth;
					ChattingController.toChatting(pc, String.format("현재 성공 확률: %.1f%%", currentChance), Lineage.CHATTING_MODE_MESSAGE);

					int maxPity = Lineage_Balance.maan_pity_count_birth;
					boolean isPity = (maxPity > 0 && pc.maanCountBirth >= maxPity);
					boolean success = isPity || ((Math.random() * 100.0) < currentChance);

					// 지급할 아이템 결정 (성공: 탄생 / 실패: 지룡 페이백)
					itemName = success ? Lineage.magicDoll[7][1] : "지룡의 마안";

					if (success) {
						String msg = isPity ? "\\fV[시스템] 천장 달성! 합성에 성공하였습니다!" : "마안 합성 성공!";
						ChattingController.toChatting(pc, msg, Lineage.CHATTING_MODE_MESSAGE);
						
						pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, 2048), true);
						broadcastToast(pc, itemName, "마안 합성");
						
						// 💡 [GUI 로그] 성공 (천장 성공과 일반 성공 구분 출력)
						final String logMessage = isPity ? 
							String.format("[%s] [탄생 천장성공]\t [캐릭터: %s]\t [스택: %d] (확정 천장)", timeString, charName, pc.maanCountBirth) : 
							String.format("[%s] [탄생 성공]\t [캐릭터: %s]\t [스택 리셋]", timeString, charName);
						lineage.gui.GuiMain.display.asyncExec(new Runnable() {
							public void run() { lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(logMessage); }
						});
						
						pc.maanCountBirth = 0; 
						pc.maanBonusBirth = 0.0;
					} else {
						pc.maanCountBirth += 1; 
						if (Util.random(1, 100) <= 10) {
							pc.maanBonusBirth += Lineage_Balance.maan_birth_bonus_val;
							ChattingController.toChatting(pc, "운이 따르기 시작합니다! (탄생 성공 확률 상승)", Lineage.CHATTING_MODE_MESSAGE);
						}
						String pityStatus = (maxPity > 0) ? " (누적: " + pc.maanCountBirth + " / " + maxPity + ")" : "";
						ChattingController.toChatting(pc, "마안 합성에 실패하였습니다." + pityStatus, Lineage.CHATTING_MODE_MESSAGE);
						
						// 💡 [GUI 로그] 실패 및 누적 스택 기록
						final String logMessage = String.format("[%s] [탄생 실패]\t [캐릭터: %s]\t [스택: %d / %d]", timeString, charName, pc.maanCountBirth, maxPity);
						lineage.gui.GuiMain.display.asyncExec(new Runnable() {
							public void run() { lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(logMessage); }
						});
					}
				}

				// ==========================================
				// 3. 형상의 마안 합성
				// ==========================================
				else if (action.equalsIgnoreCase("형상의 마안")) {
					if (!Lineage.oman) {
						ChattingController.toChatting(pc, "현재는 형상의 마안을 제작할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE); 
						return;
					}
					if (풍룡 == null || 탄생 == null) {
						ChattingController.toChatting(pc, "풍룡 또는 탄생의 마안이 부족합니다.", Lineage.CHATTING_MODE_MESSAGE); 
						return;
					}
					
					reqAdena = Lineage_Balance.maan_shape_aden_count;
					reqAdenaName = Lineage_Balance.maan_shape_aden_name;
					mat1 = 풍룡;
					mat2 = 탄생;

					if (reqAdena > 0 && !pc.getInventory().isAden(reqAdenaName, reqAdena, true)) {
						ChattingController.toChatting(pc, reqAdenaName + "이(가) 부족합니다.", Lineage.CHATTING_MODE_MESSAGE); 
						return;
					}

					double currentChance = Lineage_Balance.maan_shape_percent + pc.maanBonusShape;
					ChattingController.toChatting(pc, String.format("현재 성공 확률: %.1f%%", currentChance), Lineage.CHATTING_MODE_MESSAGE);

					int maxPity = Lineage_Balance.maan_pity_count_shape;
					boolean isPity = (maxPity > 0 && pc.maanCountShape >= maxPity);
					boolean success = isPity || ((Math.random() * 100.0) < currentChance);

					itemName = success ? Lineage.magicDoll[7][2] : "탄생의 마안";

					if (success) {
						String msg = isPity ? "\\fV[시스템] 천장 달성! 100% 확률로 형상의 마안 합성에 성공하였습니다!" : "마안 합성 성공!";
						ChattingController.toChatting(pc, msg, Lineage.CHATTING_MODE_MESSAGE);
						pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, 2048), true);
						broadcastToast(pc, itemName, "마안 합성");
						
						// 💡 [GUI 로그] 성공
						final String logMessage = isPity ? 
							String.format("[%s] [형상 천장성공]\t [캐릭터: %s]\t [스택: %d] (확정 천장)", timeString, charName, pc.maanCountShape) : 
							String.format("[%s] [형상 성공]\t [캐릭터: %s]\t [스택 리셋]", timeString, charName);
						lineage.gui.GuiMain.display.asyncExec(new Runnable() {
							public void run() { lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(logMessage); }
						});
						
						pc.maanCountShape = 0;
						pc.maanBonusShape = 0.0;
					} else {
						pc.maanCountShape += 1;
						if (Util.random(1, 100) <= 10) {
							pc.maanBonusShape += Lineage_Balance.maan_shape_bonus_val;
							ChattingController.toChatting(pc, "운이 따르기 시작합니다! (형상 성공 확률 상승)", Lineage.CHATTING_MODE_MESSAGE);
						}
						String pityStatus = (maxPity > 0) ? " (누적: " + pc.maanCountShape + " / " + maxPity + ")" : "";
						ChattingController.toChatting(pc, "마안 합성에 실패하였습니다." + pityStatus, Lineage.CHATTING_MODE_MESSAGE);
						
						// 💡 [GUI 로그] 실패
						final String logMessage = String.format("[%s] [형상 실패]\t [캐릭터: %s]\t [스택: %d / %d]", timeString, charName, pc.maanCountShape, maxPity);
						lineage.gui.GuiMain.display.asyncExec(new Runnable() {
							public void run() { lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(logMessage); }
						});
					}
				}

				// ==========================================
				// 4. 생명의 마안 합성
				// ==========================================
				else if (action.equalsIgnoreCase("생명의 마안")) {
					if (!Lineage.oman4) {
						ChattingController.toChatting(pc, "현재는 생명의 마안을 제작할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE); 
						return;
					}
					if (형상 == null || 화룡 == null) {
						ChattingController.toChatting(pc, "형상 또는 화룡의 마안이 부족합니다.", Lineage.CHATTING_MODE_MESSAGE); 
						return;
					}
					
					reqAdena = Lineage_Balance.maan_life_aden_count;
					reqAdenaName = Lineage_Balance.maan_life_aden_name;
					mat1 = 형상;
					mat2 = 화룡;

					if (reqAdena > 0 && !pc.getInventory().isAden(reqAdenaName, reqAdena, true)) {
						ChattingController.toChatting(pc, reqAdenaName + "이(가) 부족합니다.", Lineage.CHATTING_MODE_MESSAGE); 
						return;
					}

					double currentChance = Lineage_Balance.maan_life_percent + pc.maanBonusLife;
					ChattingController.toChatting(pc, String.format("현재 성공 확률: %.1f%%", currentChance), Lineage.CHATTING_MODE_MESSAGE);

					int maxPity = Lineage_Balance.maan_pity_count_life;
					boolean isPity = (maxPity > 0 && pc.maanCountLife >= maxPity);
					boolean success = isPity || ((Math.random() * 100.0) < currentChance);

					itemName = success ? Lineage.magicDoll[7][0] : "형상의 마안";			

					if (success) {
						String msg = isPity ? "\\fV[시스템] 천장 달성! 100% 확률로 생명의 마안 합성에 성공하였습니다!" : "마안 합성 성공!";
						ChattingController.toChatting(pc, msg, Lineage.CHATTING_MODE_MESSAGE);
						pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, 2048), true);
						broadcastToast(pc, itemName, "마안 합성");
						
						// 💡 [GUI 로그] 성공
						final String logMessage = isPity ? 
							String.format("[%s] [생명 천장성공]\t [캐릭터: %s]\t [스택: %d] (확정 천장)", timeString, charName, pc.maanCountLife) : 
							String.format("[%s] [생명 성공]\t [캐릭터: %s]\t [스택 리셋]", timeString, charName);
						lineage.gui.GuiMain.display.asyncExec(new Runnable() {
							public void run() { lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(logMessage); }
						});
						
						pc.maanCountLife = 0;
						pc.maanBonusLife = 0.0;
					} else {
						pc.maanCountLife += 1;
						if (Util.random(1, 100) <= 10) {
							pc.maanBonusLife += Lineage_Balance.maan_life_bonus_val;
							ChattingController.toChatting(pc, "운이 따르기 시작합니다! (생명 성공 확률 상승)", Lineage.CHATTING_MODE_MESSAGE);
						}
						String pityStatus = (maxPity > 0) ? " (누적: " + pc.maanCountLife + " / " + maxPity + ")" : "";
						ChattingController.toChatting(pc, "마안 합성에 실패하였습니다." + pityStatus, Lineage.CHATTING_MODE_MESSAGE);
						
						// 💡 [GUI 로그] 실패
						final String logMessage = String.format("[%s] [생명 실패]\t [캐릭터: %s]\t [스택: %d / %d]", timeString, charName, pc.maanCountLife, maxPity);
						lineage.gui.GuiMain.display.asyncExec(new Runnable() {
							public void run() { lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(logMessage); }
						});
					}
				}

		// ==========================================
		// 5. 공통 처리 (재료 차감 및 아이템 지급, DB 저장)
		// ==========================================
		if (mat1 != null && mat2 != null && itemName != null) {
			
			// ★ 성공/실패 여부와 관계없이 재료 1개씩 무조건 차감
			pc.getInventory().count(mat1, mat1.getCount() - 1, true);
			pc.getInventory().count(mat2, mat2.getCount() - 1, true);
			
			// 아데나 실제 차감
			if (reqAdena > 0) {
				pc.getInventory().isAden(reqAdenaName, reqAdena, false);
			}

			// 결과 아이템 지급 (성공 시 고급 마안 / 실패 시 하위 마안 페이백)
			Item item = ItemDatabase.find(itemName);
			if (item != null) {
				ItemInstance temp = ItemDatabase.newInstance(item);
				temp.setObjectId(ServerDatabase.nextItemObjId());
				temp.setBless(1);
				temp.setEnLevel(0);
				temp.setDefinite(true);
				pc.getInventory().append(temp, true);
			}
			
			// 변경된 천장 카운트 및 보너스 확률 DB 저장
			pc.toCharacterSave2(); 
		}
	}

	// ==========================================
	// 6. 원본 토스트 메시지 완벽 복원
	// ==========================================
	private void broadcastToast(PcInstance pc, String itemName, String title) {
		String line1 = String.format("\\g1* %s [ %s ] *", title, itemName);
		String line2 = String.format("\\fH어느 아덴 용사가 %s [%s]을(를) 획득하였습니다.", title, itemName);
		
		SC_TOAST_NOTI.newInstance()
			.setMessage(line1)
			.setMessage2(line2)
			.setToastType(ToastType.HeavyText)
			.send(pc); 
	}
}