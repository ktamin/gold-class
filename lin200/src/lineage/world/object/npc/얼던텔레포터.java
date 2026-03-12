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
import lineage.world.controller.얼던컨트롤러;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class 얼던텔레포터 extends object {
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		List<String> list = new ArrayList<String>();
		int nowday = getDayOfWeek ();
		
		list.add(String.format("입장 레벨: %d이상 입장 가능", Lineage.ice_level));
		list.add(String.format("수배 조건: %s", Lineage.ice_wanted ? "수배자만 입장 가능" : "수배 필요없음"));
		list.add(String.format("혈맹 조건: %s", Lineage.ice_clan ? "혈맹 필요" : "혈맹 필요없음"));
		if(nowday == 1 || nowday == 7){
			list.add(String.format("입장 시간: %s", Lineage.ice_dungeon_time2));	
		}else{
			list.add(String.format("입장 시간: %s", Lineage.ice_dungeon_time));	
		}
		list.add(String.format("진행 시간: %s", Lineage.ice_play_time < 60 ? Lineage.ice_play_time + "초" : (Lineage.ice_play_time / 60) + "분"));
		list.add(String.format("입장 가능 여부: %s", 얼던컨트롤러.isOpen ? "현재 입장 가능" : "입장 불가"));

		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "icetel", null, list));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
		if (action.equalsIgnoreCase("ice_teleport")) {			
			
			// 혈맹 제한: GM 제외, 혈맹이 없거나 신규 혈맹이면 입장 불가
			if (pc.getGm() == 0 && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
			    ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			    return;
			}
			
//			if (pc.getGm() > 0 || 얼던컨트롤러.isOpen) {
			if (true) {
				if (pc.getGm() > 0 || (Lineage.ice_level <= pc.getLevel())) {
					  if (pc.getGm() > 0 || !Lineage.ice_wanted || (Lineage.ice_wanted && WantedController.checkWantedPc(pc))) {
						if (pc.getGm() > 0 || !Lineage.ice_clan || (Lineage.ice_clan && pc.getClanId() > 0)) {
							if(pc.getLevel() >= 58){
							   //pc.toPotal(Util.random(32783, 32785), Util.random(32898, 32901), 74);
                               //pc.toPotal(Util.random(32812, 32814), Util.random(32723, 32725), 811);
					           //else{
							   //pc.toPotal(Util.random(32797, 32800), Util.random(32860, 32863), 73);
                               //pc.toPotal(Util.random(32812, 32814), Util.random(32723, 32725), 811);							
							}

						} else {
							ChattingController.toChatting(pc, "얼음 던전은 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
						}
					} else {
						ChattingController.toChatting(pc, "얼음 던전은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					}
				} else {
					ChattingController.toChatting(pc, String.format("얼음 던전은  %d레벨 이상 입장 가능합니다.", Lineage.ice_level), Lineage.CHATTING_MODE_MESSAGE);
				}
			
			} else {
				ChattingController.toChatting(pc, "얼음 던전으로 던전으로 가는길이 닫혀있습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}

	public static int getDayOfWeek() {
		Calendar rightNow = Calendar.getInstance();
		int day_of_week = rightNow.get(Calendar.DAY_OF_WEEK);
		return day_of_week;
	}
}
