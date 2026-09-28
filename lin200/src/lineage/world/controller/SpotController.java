package lineage.world.controller;

import java.util.Calendar;
import java.util.Date;

import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.World;
import lineage.world.object.npc.SpotTower;

public final class SpotController {
	static private Calendar calendar;
	static public SpotTower spot;
	static public long stopItemTime;
	static private int count;
	static private int mentCount;

	static public void init() {
		TimeLine.start("SpotController..");

		calendar = Calendar.getInstance();
		count = mentCount = 60;

		TimeLine.end();
	}

	// 💡 [추가됨] 서버 렉을 방지하는 초경량 요일 판별 함수
	static private boolean isSpotDay(int todayDayOfWeek) {
		String conf = Lineage.spot_open_days;
		// 콘프에 값이 비어있으면 기본으로 매일 진행되도록 처리
		if (conf == null || conf.isEmpty()) return true;
		
		// 무겁게 쪼개지(split) 않고, 설정값 안에 오늘 요일 숫자(문자열)가 포함되어 있는지 확인
		return conf.contains(String.valueOf(todayDayOfWeek));
	}

	@SuppressWarnings("deprecation")
	static public void toTimer(long time) {
		if (calendar == null)
			calendar = Calendar.getInstance();
		
		// 현재 시간.
		calendar.setTimeInMillis(time);
		Date date = calendar.getTime();
		int hour = date.getHours();
		int min = date.getMinutes();
		
		// 💡 [추가됨] 오늘 요일 구하기 (0: 일요일 ~ 6: 토요일)
		int today = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		
		try {
			if (spot == null)
				setSpotTower();
		} catch (Exception e) {
			lineage.share.System.printf("스팟 쟁탈전 초기화 에러\r\n : %s\r\n", e.toString());
		}
		
		try {
			// 💡 [수정됨] isSpotDay(today) 조건을 추가하여 개방 요일일 때만 스팟 쟁탈전이 시작되도록 제한
			if (Lineage.is_spot && spot != null && !spot.isStart && isSpotDay(today) && hour == Lineage.spot_tower_start_hour && min == Lineage.spot_tower_start_min)
				spot.start(time);
		} catch (Exception e) {
			lineage.share.System.printf("스팟 쟁탈전 시작\r\n : %s\r\n", e.toString());
		}
		
		try {
			if (spot != null && spot.isStart && (spot.getMonsterListSize() < 1 || spot.monKillCount < 1) && !spot.isDead() && spot.endTime > 0 && spot.endTime <= time)
				spot.end(true);
		} catch (Exception e) {
			lineage.share.System.printf("스팟 쟁탈전 종료 에러\r\n : %s\r\n", e.toString());
		}
		
		try {
			if (spot != null && spot.isStart && --mentCount < 1) {
				mentCount = count;
				World.toSender( S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "\\fY      *** 현재 스팟 쟁탈전이 진행 중입니다. ***") );
			}
		} catch (Exception e) {
			lineage.share.System.printf("스팟 쟁탈전 멘트 에러\r\n : %s\r\n", e.toString());
		}
		
		try {
			spot.toTimer();
		} catch (Exception e) {
			lineage.share.System.printf("스팟 쟁탈전 타이머 에러\r\n : %s\r\n", e.toString());
		}
	}
	
	static public void setSpotTower() {
		spot = new SpotTower();
		spot.setObjectId(ServerDatabase.nextEtcObjId());
		//spot.setClassGfx(2671);
		//spot.setGfx(2671);
		spot.setClassGfx(1672);
		spot.setGfx(1672);
		spot.setGfxMode(32);
		spot.setName("스팟 타워");
		spot.setMaxHp(Lineage.spot_tower_hp);
		spot.setNowHp(Lineage.spot_tower_hp);
	}
}