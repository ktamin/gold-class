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
import lineage.world.controller.악마왕의영토컨트롤러;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class 악영텔레포터 extends object {
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		List<String> list = new ArrayList<String>();
		int nowday = getDayOfWeek ();
		
		list.add(String.format("입장 레벨: %d이상 입장 가능", Lineage.devil_level));
		list.add(String.format("수배 조건: %s", Lineage.devil_wanted ? "수배자만 입장 가능" : "수배 필요없음"));
		list.add(String.format("혈맹 조건: %s", Lineage.devil_clan ? "혈맹 필요" : "혈맹 필요없음"));
		if(nowday == 1 || nowday == 7){
			list.add(String.format("입장 시간: %s", Lineage.devil_dungeon_time2));	
		}else{
			list.add(String.format("입장 시간: %s", Lineage.devil_dungeon_time));
		}
		list.add(String.format("진행 시간: %s", Lineage.devil_play_time < 60 ? Lineage.devil_play_time + "초" : (Lineage.devil_play_time / 60) + "분"));
		list.add(String.format("입장 가능 여부: %s", 악마왕의영토컨트롤러.isOpen ? "현재 입장 가능" : "입장 불가"));
		
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "deviltel", null, list));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
	    if (!action.equalsIgnoreCase("devil_teleport"))
	        return;

	    // 혈맹 제한: GM 제외, 혈맹이 없거나 신규 혈맹이면 입장 불가
	    if (pc.getGm() == 0 && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
	        ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 던전 오픈 여부
	    if (pc.getGm() == 0 && !악마왕의영토컨트롤러.isOpen) {
	        ChattingController.toChatting(pc, "악마왕의 영토로 가는길이 닫혀있습니다.", Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 레벨 조건
	    if (pc.getGm() == 0 && pc.getLevel() < Lineage.devil_level) {
	        ChattingController.toChatting(pc, String.format("악마왕의 영토는 %d레벨 이상 입장 가능합니다.", Lineage.devil_level), Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 수배 조건
	    if (pc.getGm() == 0 && Lineage.devil_wanted && !WantedController.checkWantedPc(pc)) {
	        ChattingController.toChatting(pc, "악마왕의 영토는 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 혈맹 조건 (실제 입장)
	    if (pc.getGm() == 0 && Lineage.devil_clan && pc.getClanId() == 0) {
	        ChattingController.toChatting(pc, "악마왕의 영토는 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 아데나 소모 및 텔레포트
	    if (pc.getInventory().isAden("아데나", Lineage.go_devil, true)) {
	        pc.toPotal(Util.random(32721, 32725), Util.random(32796, 32803), 5167);
	    } else {
	        ChattingController.toChatting(pc, "입장에 필요한 아데나가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
	    }
	}


	
	public static int getDayOfWeek() {
		Calendar rightNow = Calendar.getInstance();
		int day_of_week = rightNow.get(Calendar.DAY_OF_WEEK);
		return day_of_week;
	}
}
