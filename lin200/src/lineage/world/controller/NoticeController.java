package lineage.world.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lineage.bean.lineage.Kingdom;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.World;

public class NoticeController {

	static private long real_notice_time;
	static private long kingdom_war_notice_time;
	static private int real_notice_idx;
	static private List<String> real_notice;

	static public void init() {
		TimeLine.start("NoticeController..");
		real_notice = new ArrayList<String>();
		try {
			BufferedReader lnrr = new BufferedReader(new FileReader("notice.txt"));
			String line;
			while ((line = lnrr.readLine()) != null) {
				line = line.trim();
				if (line.contains("#") || line.length() <= 0)
					continue;

				real_notice.add(line);
			}
			lnrr.close();
		} catch (Exception e) {
			lineage.share.System.printf("%s : init()\r\n", NoticeController.class.toString());
			lineage.share.System.println(e);
		}

		TimeLine.end();
	}
	
	static public void reload() {
		TimeLine.start("notice.txt 파일 리로드 완료 - ");
		
		real_notice.clear();
		
		try {
			BufferedReader lnrr = new BufferedReader(new FileReader("notice.txt"));
			String line;
			while ((line = lnrr.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("#") || line.length() <= 0)
					continue;

				real_notice.add(line);
			}
			lnrr.close();
		} catch (Exception e) {
			lineage.share.System.printf("%s : init()\r\n", NoticeController.class.toString());
			lineage.share.System.println(e);
		}

		TimeLine.end();
	}

	static public void toTimer(long time) {
		if (time - real_notice_time >= Lineage.notice_delay) {		
			real_notice_time = time;
			
			if (real_notice.size() == 0)
				return;
			if (real_notice.size() <= real_notice_idx)
				real_notice_idx = 0;

		
			World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class),"\\fW"+ real_notice.get(real_notice_idx++)));

			if (Lineage.open_wait){
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("안녕하세요 한양 서버 입니다.")));
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("서버를 찾아주셔서 감사합니다.")));
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("지금은 오픈대기 상태이며.")));
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("서버오픈 기준으로부터 10분마다")));
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("오픈대기 상자가 지급됩니다")));
			}
				
		}
		
		if ((time - kingdom_war_notice_time >= Lineage.kingdom_war_notice_delay) && Lineage.is_kingdom_war && Lineage.is_kingdom_war_notice) {
			kingdom_war_notice_time = time;
			
			// 공성전 시간 알림
			kingdomWarNoticeNew(time);
		}
	}

	@SuppressWarnings("deprecation")
	public static void kingdomWarNoticeOld(long time) {
		try {		
			Kingdom k = KingdomController.find(4);

			if (k == null)
				return;
		
			Calendar nowCalendar = Calendar.getInstance();
			Calendar kingdomWarCalendar = Calendar.getInstance();

			nowCalendar.setTimeInMillis(time);
			kingdomWarCalendar.setTimeInMillis(k.getWarDay());

			Date nowDate = nowCalendar.getTime();
			Date kingdomWarDate = kingdomWarCalendar.getTime();

			nowDate.setTime(time);
			kingdomWarDate.setTime(k.getWarDay());

			if (nowDate.getTime() < kingdomWarDate.getTime()) {

				if (nowDate.getMonth() >= kingdomWarDate.getMonth() && nowDate.getDate() > kingdomWarDate.getDate())
					return;

				if (nowDate.getMonth() == kingdomWarDate.getMonth() && nowDate.getDate() == kingdomWarDate.getDate()
						&& (nowDate.getHours() > kingdomWarDate.getHours() || kingdomWarDate.getTime() - nowDate.getTime() < Lineage.kingdom_war_notice_delay))
					return;

				String day = "";

				switch (kingdomWarDate.getDay()) {
				case 0:
					day = "일";
					break;
				case 1:
					day = "월";
					break;
				case 2:
					day = "화";
					break;
				case 3:
					day = "수";
					break;
				case 4:
					day = "목";
					break;
				case 5:
					day = "금";
					break;
				case 6:
					day = "토";
					break;
				}
				
				if (nowDate.getMonth() == kingdomWarDate.getMonth() && nowDate.getDate() == kingdomWarDate.getDate()) {
					if (kingdomWarDate.getHours() < 12)
						World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("기란 공성전이 오늘 오전 %d시 %d분에 진행됩니다.", 
								kingdomWarDate.getHours() == 0 ? 12 : kingdomWarDate.getHours(), kingdomWarDate.getMinutes())));
					else
						World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("기란 공성전이 오늘 오후 %d시 %d분에 진행됩니다.", 
								kingdomWarDate.getHours() - 12 == 0 ? 12 : kingdomWarDate.getHours() - 12, kingdomWarDate.getMinutes())));
				} else {
					if (kingdomWarDate.getHours() < 12)
						World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("기란 공성전이 %d월 %d일(%s) 오전 %d시 %d분에 진행됩니다.", 
								kingdomWarDate.getMonth() + 1, kingdomWarDate.getDate(), day,
								kingdomWarDate.getHours() == 0 ? 12 : kingdomWarDate.getHours(), kingdomWarDate.getMinutes())));
					else
						World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("기란 공성전이 %d월 %d일(%s) 오후 %d시 %d분에 진행됩니다.", 
								kingdomWarDate.getMonth() + 1, kingdomWarDate.getDate(), day,
								kingdomWarDate.getHours() == 0 ? 12 : kingdomWarDate.getHours(), kingdomWarDate.getMinutes())));
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : 기란 공성 시간 공지 오류\r\n", NoticeController.class.toString());
			lineage.share.System.println(e);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void kingdomWarNoticeNew(long time) {
		try {
			Kingdom k = KingdomController.find(4);

			if (k == null)
				return;
		
			Calendar nowCalendar = Calendar.getInstance();
			nowCalendar.setTimeInMillis(time);
			Date nowDate = nowCalendar.getTime();
			nowDate.setTime(time);
			
			StringBuffer sb = new StringBuffer();
			int idx = 0;
			boolean isWar = false;
			boolean toDay = false;
			for (Integer d : Lineage.getGiranKingdomWarDayList()) {
				int day = (int) d;
				
				if (nowDate.getDay() == day)
					toDay = true;
				
				if (idx > 0)
					sb.append(", ");
				
				switch (day) {
				case 0:
					sb.append("일");
					break;
				case 1:
					sb.append("월");
					break;
				case 2:
					sb.append("화");
					break;
				case 3:
					sb.append("수");
					break;
				case 4:
					sb.append("목");
					break;
				case 5:
					sb.append("금");
					break;
				case 6:
					sb.append("토");
					break;
				}		
				idx++;
				isWar = true;
			}
			
			if (isWar) {
				if (toDay) {
					if (Lineage.giran_kingdom_war_hour < 12)
						World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("기란 공성전이 오늘 오전 %d시 %s에 진행됩니다.", 
								Lineage.giran_kingdom_war_hour == 0 ? 12 : Lineage.giran_kingdom_war_hour, Lineage.giran_kingdom_war_min == 0 ? "정각" : String.format("%d분", Lineage.giran_kingdom_war_min))));
					else
						World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("기란 공성전이 오늘 오후 %d시 %s에 진행됩니다.",
								Lineage.giran_kingdom_war_hour - 12 == 0 ? 12 : Lineage.giran_kingdom_war_hour - 12, Lineage.giran_kingdom_war_min == 0 ? "정각" : String.format("%d분", Lineage.giran_kingdom_war_min))));
				} else {
					if (Lineage.giran_kingdom_war_hour < 12)
						World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("기란 공성전은 (%s요일) 오전 %d시 %s에 진행됩니다.", sb.toString(), 
								Lineage.giran_kingdom_war_hour == 0 ? 12 : Lineage.giran_kingdom_war_hour, Lineage.giran_kingdom_war_min == 0 ? "정각" : String.format("%d분", Lineage.giran_kingdom_war_min))));
					else
						World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("기란 공성전은 (%s요일) 오후 %d시 %s에 진행됩니다.", sb.toString(), 
								Lineage.giran_kingdom_war_hour - 12 == 0 ? 12 : Lineage.giran_kingdom_war_hour - 12, Lineage.giran_kingdom_war_min == 0 ? "정각" : String.format("%d분", Lineage.giran_kingdom_war_min))));
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : 기란 공성 시간 공지 오류\r\n", NoticeController.class.toString());
			lineage.share.System.println(e);
		}
	}
}
