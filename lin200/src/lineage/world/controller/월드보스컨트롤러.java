package lineage.world.controller;

import java.util.Calendar;
import java.util.Date;

import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI.ToastType;
import lineage.bean.database.TeamBattleTime;
import lineage.database.MonsterDatabase;
import lineage.database.MonsterSpawnlistDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_BlueMessage;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.thread.AiThread;
import lineage.world.World;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public class 월드보스컨트롤러 {
	static private Calendar calendar;
	public static boolean isOpen;
	public static boolean isWait;
	public static long worldEndTime;
	
	static public void init() {
		TimeLine.start("월드보스컨트롤러..");
		
		calendar = Calendar.getInstance();
		isOpen = false;
		isWait = false;
		worldEndTime = 0L;
		
		TimeLine.end();
	}
	
	@SuppressWarnings("deprecation")
	static public void toTimer(long time) {
		calendar.setTimeInMillis(time);
		Date date = calendar.getTime();
		int hour = date.getHours();
		int min = date.getMinutes();
		int sec = date.getSeconds();
		
		for (TeamBattleTime tebeTime : Lineage.world_dungeon_time_list) {
			
			int test = tebeTime.getMin() - 1;
			
			if (!isOpen && tebeTime.getHour() == hour && test == min && sec == 0) {
				
				for (MonsterInstance boss: BossController.getBossList()){
					// 💡 [수정] equalsIgnoreCase("월드보스") 대신 contains를 사용!
					// 이렇게 해야 [1차]월드보스, [2차]월드보스 상관없이 이름에 "월드보스"만 들어가면 싹 청소합니다.
					if(boss.getMonster().getName().contains("월드보스")){
						boss.toAiThreadDelete();
						World.removeMonster(boss);
						World.remove(boss);
						BossController.toWorldOut(boss);
					}
				}
				
				isWait = true;
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class),  String.format("\\fU월드보스 레이드가 1분뒤 시작 합니다 마을npc를 통하여 입장 해주세요")));
			}
			
			if (!isOpen && tebeTime.getHour() == hour && test == min && sec == 30) {
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("\\fU 월드보스 레이드가 30초뒤 시작 합니다  마을npc를 통하여 입장 해주세요")));
			}
			
			if (!isOpen && tebeTime.getHour() == hour && tebeTime.getMin() == min && sec == 0) {
				isOpen = true;
				worldEndTime = time + (1000 * Lineage.world_play_time);
				
				// =========================================================
				// 💡 [핵심 수정] 콘프에 설정된 숫자를 가져와서 보스 이름을 동적으로 완성합니다!
				// =========================================================
				String bossName = "[" + Lineage.world_boss_step + "차]월드보스";
				
				MonsterInstance mi = MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find(bossName));
				
				// 혹시라도 나비캣 DB에 해당 n차 보스가 없어서 null이 뜨는 에러를 방지하는 안전장치
				if (mi != null) {
					mi.setHomeX( 32877);
					mi.setHomeY( 32817 );
					mi.setHomeMap(1400);
					mi.setBoss(true);
				
					AiThread.append(mi);
					BossController.appendBossList(mi);
					mi.toTeleport(mi.getHomeX(), mi.getHomeY(), mi.getHomeMap(), false);
				} else {
					System.out.println("[오류] DB에 '" + bossName + "' 몬스터가 존재하지 않습니다!");
				}
				
				sendMessage();
			}
		}
		
		if (isOpen && worldEndTime > 0 && worldEndTime < time) {
			isOpen = false;
			isWait = false;
			sendMessage();
			
			for (MonsterInstance boss: BossController.getBossList()){
				// 💡 [수정] 종료 시에도 깔끔하게 포함(contains) 단어로 청소
				if(boss.getMonster().getName().contains("월드보스")){
					boss.toAiThreadDelete();
					World.removeMonster(boss);
					World.remove(boss);
					BossController.toWorldOut(boss);
				}
			}
		}
	}
	
	static public void sendMessage() {
		String chatMsg;
		String toastTitle, toastDesc;

		if (isOpen) {
			chatMsg = "\\fY      ***** 월드보스 토벌이 시작 되었습니다. *****";
			toastTitle = "★ 월드보스 출현 ★";
			toastDesc = "토벌이 시작되었습니다. 지금 바로 참여하세요!";
		} else {
			chatMsg = "\\fY      ***** 월드보스가 종료 되었습니다.*****";
			toastTitle = "■ 월드보스 종료 안내";
			toastDesc = "토벌이 종료되었습니다. 다음 시간을 기다려 주세요.";
		}

		// 1. 전체 채팅 메세지 전송
		World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), chatMsg));

		// 2. 화면 중앙 블루 메세지 알리기 (설정값이 true일 때만)
		if (Lineage.is_blue_message) {
			World.toSender(S_BlueMessage.clone(BasePacketPooling.getPool(S_BlueMessage.class), 556, chatMsg));
		}

		// 3. 토스트 전체 메세지 알리기
		for (PcInstance pc : World.getPcList()) {
		    if (pc != null && pc.getClient() != null) { 
		        SC_TOAST_NOTI.newInstance()
		            .setMessage(toastTitle)
		            .setMessage2(toastDesc)
		            .setToastType(ToastType.HeavyText)
		            .send(pc);
		    }
		}
	}
}