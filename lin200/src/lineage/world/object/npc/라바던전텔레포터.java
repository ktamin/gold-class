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
import lineage.world.controller.라바던전컨트롤러;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class 라바던전텔레포터 extends object {
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		List<String> list = new ArrayList<String>();
		
		int nowday = getDayOfWeek ();
		list.add(String.format("입장 레벨: %d이상 입장 가능", Lineage.lasta_level));
		list.add(String.format("수배 조건: %s", Lineage.lasta_wanted ? "수배자만 입장 가능" : "수배 필요없음"));
		list.add(String.format("혈맹 조건: %s", Lineage.lasta_clan ? "혈맹 필요" : "혈맹 필요없음"));
		
		if(nowday == 1 || nowday == 7){
			list.add(String.format("입장 시간: %s", Lineage.lasta_dungeon_time2));	
		}else{
			list.add(String.format("입장 시간: %s", Lineage.lasta_dungeon_time));
		}
		list.add(String.format("진행 시간: %s", Lineage.lasta_play_time < 60 ? Lineage.lasta_play_time + "초" : (Lineage.lasta_play_time / 60) + "분"));
		list.add(String.format("입장 가능 여부: %s", 라바던전컨트롤러.isOpen ? "현재 입장 가능" : "입장 불가"));
		
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "lastatel", null, list));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
	    if (!"lasta_teleport".equalsIgnoreCase(action)) return;

	    // 1. 혈맹 제한
	    if (pc.getGm() == 0 && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
	        ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 2. 레벨 제한
	    if (pc.getGm() == 0 && pc.getLevel() < Lineage.lasta_level) {
	        ChattingController.toChatting(pc, String.format("라스타바드던전은 %d레벨 이상 입장 가능합니다.", Lineage.lasta_level), Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 3. 사냥터 오픈 여부
	    if (pc.getGm() == 0 && !라바던전컨트롤러.isOpen) {
	        ChattingController.toChatting(pc, "라스타바드 던전으로 가는길이 닫혀있습니다.", Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 4. 입장료 체크
	    if (pc.getGm() == 0 && !pc.getInventory().isAden("아데나", Lineage.go_lasta, true)) {
	        ChattingController.toChatting(pc, String.format("입장료 아데나: %,d 부족합니다.", Lineage.go_lasta), Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 5. 수배자 제한 (옵션)
	    if (pc.getGm() == 0 && Lineage.lasta_wanted && !WantedController.checkWantedPc(pc)) {
	        ChattingController.toChatting(pc, "라스타바드 던전은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 모두 통과
	    pc.toPotal(Util.random(32733, 32737), Util.random(32848, 32865), 451);
	}

	public static int getDayOfWeek() {
		Calendar rightNow = Calendar.getInstance();
		int day_of_week = rightNow.get(Calendar.DAY_OF_WEEK);
		return day_of_week;
	}
}
