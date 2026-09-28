package lineage.world.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI.ToastType;
import lineage.world.object.instance.PcInstance;

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

public class 월드보스컨트롤러 {
	static private Calendar calendar;
	public static boolean isOpen;
	public static boolean isWait;
	public static long worldEndTime;
	
	// 소환된 보스의 상태 추적
	public static MonsterInstance spawnedBoss = null;
	
	static public void init() {
		TimeLine.start("월드보스컨트롤러..");
		
		calendar = Calendar.getInstance();
		isOpen = false;
		isWait = false;
		worldEndTime = 0L;
		spawnedBoss = null;
		
		TimeLine.end();
	}
	
	@SuppressWarnings("deprecation")
	static public void toTimer(long time) {
		calendar.setTimeInMillis(time);
		Date date = calendar.getTime();
		int hour = date.getHours();
		int min = date.getMinutes();
		int sec = date.getSeconds();
		
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int today = dayOfWeek - 1; // 0=일요일 ~ 6=토요일
		boolean isWeekend = (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY);
		
		// ==========================================
		// 💡 [추가] 오늘이 월드보스 오픈 요일인지 확인
		// ==========================================
		boolean isTodayOpen = true; // 기본적으로 열림으로 설정
		if (Lineage.world_boss_open_days != null && !Lineage.world_boss_open_days.isEmpty()) {
			isTodayOpen = false; // 콘프 값이 있으면 일단 닫음
			String[] days = Lineage.world_boss_open_days.split(",");
			for (String d : days) {
				if (Integer.parseInt(d.trim()) == today) {
					isTodayOpen = true; // 오늘 요일이 포함되어 있으면 열림
					break;
				}
			}
		}

		// 주말 여부에 따라 시간표 선택
		List<TeamBattleTime> activeTimeList;
		if (isWeekend && Lineage.world_dungeon_time_list2 != null && !Lineage.world_dungeon_time_list2.isEmpty()) {
			activeTimeList = Lineage.world_dungeon_time_list2;
		} else {
			activeTimeList = Lineage.world_dungeon_time_list;
		}
		
		// 💡 지정된 요일일 때만 스케줄(오픈) 검사를 진행합니다.
		if (isTodayOpen) {
			for (TeamBattleTime tebeTime : activeTimeList) {
				int test = tebeTime.getMin() - 1;
				
				if (!isOpen && tebeTime.getHour() == hour && test == min && sec == 0) {
					for (MonsterInstance boss: BossController.getBossList()){
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
					String bossName = "[" + Lineage.world_boss_step + "차]월드보스";
					MonsterInstance mi = MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find(bossName));
					
					if (mi != null) {
						isOpen = true;
						worldEndTime = time + (1000 * Lineage.world_play_time);
						
						mi.setHomeX(32877);
						mi.setHomeY(32817);
						mi.setHomeMap(1400);
						mi.setBoss(true);
					
						AiThread.append(mi);
						BossController.appendBossList(mi);
						mi.toTeleport(mi.getHomeX(), mi.getHomeY(), mi.getHomeMap(), false);
						
						spawnedBoss = mi;
						sendMessage(); 
					} else {
						System.out.println("[오류] DB에 '" + bossName + "' 몬스터가 존재하지 않습니다!");
					}
				}
			}
		}
		
		// ==========================================
		// 타임오버 및 보스 사망 처리 (요일 상관없이 열려있으면 감시)
		// ==========================================
		if (isOpen) {
			boolean isTimeOver = (worldEndTime > 0 && worldEndTime < time);
			// 소환 직후 3초 이내는 사망 판정 무시 (즉시 증발 방지)
			boolean isGracePeriod = (worldEndTime - time) > (1000 * Lineage.world_play_time - 3000); 
			boolean isBossDead = (!isGracePeriod && spawnedBoss != null && spawnedBoss.isDead());
			
			if (isTimeOver || isBossDead) {
				isOpen = false;
				isWait = false;
				worldEndTime = 0;
				
				sendMessage();
				
				for (MonsterInstance boss: BossController.getBossList()){
					if(boss.getMonster().getName().contains("월드보스")){
						boss.toAiThreadDelete();
						World.removeMonster(boss);
						World.remove(boss);
						BossController.toWorldOut(boss);
					}
				}
				spawnedBoss = null;
			}
		}
	}
	
	static public void sendMessage() {
		String chatMsg;
		String toastTitle, toastDesc;

		if (isOpen) {
			chatMsg    = "\\fY      ***** 월드보스 토벌이 시작 되었습니다. *****";
			toastTitle = "★ 월드보스 출현 ★";
			toastDesc  = "토벌이 시작되었습니다. 지금 바로 참여하세요!";
		} else {
			chatMsg    = "\\fY      ***** 월드보스가 종료 되었습니다.*****";
			toastTitle = "■ 월드보스 종료 안내";
			toastDesc  = "토벌이 종료되었습니다. 다음 시간을 기다려 주세요.";
		}

		World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), chatMsg));
		if (Lineage.is_blue_message)
			World.toSender(S_BlueMessage.clone(BasePacketPooling.getPool(S_BlueMessage.class), 556, chatMsg));

		for (PcInstance pc : World.getPcList()) {
			SC_TOAST_NOTI.newInstance()
				.setMessage(toastTitle)
				.setMessage2(toastDesc)
				.setToastType(ToastType.HeavyText)
				.send(pc);
		}
	}
}