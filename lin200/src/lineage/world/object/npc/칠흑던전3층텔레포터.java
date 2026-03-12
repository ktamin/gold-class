package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.WantedController;
import lineage.world.controller.칠흑던전3층컨트롤러;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class 칠흑던전3층텔레포터 extends object {
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		List<String> list = new ArrayList<String>();
		int nowday = getDayOfWeek ();
		
		list.add(String.format("입장 레벨: %d이상 입장 가능", Lineage.dark3_level));
		list.add(String.format("수배 조건: %s", Lineage.dark3_wanted ? "수배자만 입장 가능" : "수배 필요없음"));
		list.add(String.format("혈맹 조건: %s", Lineage.dark3_clan ? "혈맹 필요" : "혈맹 필요없음"));
		if(nowday == 1 || nowday == 7){
		    list.add(String.format("입장 시간: %s", Lineage.dark3_dungeon_time2));	
		}else{
			list.add(String.format("입장 시간: %s", Lineage.dark3_dungeon_time));	
		}
		list.add(String.format("진행 시간: %s", Lineage.dark3_play_time < 60 ? Lineage.dark3_play_time + "초" : (Lineage.dark3_play_time / 60) + "분"));
		list.add(String.format("입장 가능 여부: %s", 칠흑던전3층컨트롤러.isOpen ? "현재 입장 가능" : "입장 불가"));
		
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "darktel3", null, list));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
		if (action.equalsIgnoreCase("dark3_teleport")) {
			if (pc.getGm() > 0 || 칠흑던전3층컨트롤러.isOpen) {
				if (pc.getGm() > 0 || (Lineage.dark3_level <= pc.getLevel())) {
					if (pc.getGm() > 0 || !Lineage.dark3_wanted || (Lineage.dark3_wanted && WantedController.checkWantedPc(pc))) {
						if (pc.getGm() > 0 || !Lineage.dark3_clan || (Lineage.dark3_clan && pc.getClanId() > 0)) {
							pc.toPotal(Util.random(32798, 32795), Util.random(32754, 32751), 809);
						} else {
							ChattingController.toChatting(pc, "칠흑던전 3층은 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
						}
					} else {
						ChattingController.toChatting(pc, "칠흑던전 3층은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					}
				} else {
					ChattingController.toChatting(pc, String.format("칠흑던전 3층은 %d레벨 이상 입장 가능합니다.", Lineage.wg_level), Lineage.CHATTING_MODE_MESSAGE);
				}
			} else {
				ChattingController.toChatting(pc, "칠흑던전으로 가는길이 닫혀있습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}
	public static int getDayOfWeek() {
		Calendar rightNow = Calendar.getInstance();
		int day_of_week = rightNow.get(Calendar.DAY_OF_WEEK);
		return day_of_week;
	}
}
