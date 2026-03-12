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
import lineage.world.controller.테베라스컨트롤러;
import lineage.world.controller.WantedController;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class 테베텔레포터 extends object {
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		List<String> list = new ArrayList<String>();
		int nowday = getDayOfWeek ();
		
		list.add(String.format("입장 레벨: %d이상 입장 가능", Lineage.tebe_level));
		list.add(String.format("수배 조건: %s", Lineage.tebe_wanted ? "수배자만 입장 가능" : "수배 필요없음"));
		list.add(String.format("혈맹 조건: %s", Lineage.tebe_clan ? "혈맹 필요" : "혈맹 필요없음"));
		if(nowday == 1 || nowday == 7){
		list.add(String.format("입장 시간: %s", Lineage.tebe_dungeon_time2));	
		}else{
			list.add(String.format("입장 시간: %s", Lineage.tebe_dungeon_time));		
		}
		list.add(String.format("진행 시간: %s", Lineage.tebe_play_time < 60 ? Lineage.tebe_play_time + "초" : (Lineage.tebe_play_time / 60) + "분"));
		list.add(String.format("입장 가능 여부: %s", 테베라스컨트롤러.isOpen ? "현재 입장 가능" : "입장 불가"));
		 if (Lineage.tebe_join_item_count > 0)
		 list.add(String.format("입장료: %s(%d개)", Lineage.tebe_join_item, Lineage.tebe_join_item_count));
		
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "tebetel", null, list));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
	    if (!action.equalsIgnoreCase("tebe_teleport"))
	        return;
	    
    	// 혈맹 제한: GM 제외, 혈맹이 없거나 신규 혈맹이면 입장 불가
    	if (pc.getGm() == 0 && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
    	    ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
    	    return;
    	}

	    // 던전 오픈 체크 및 GM
	    if (pc.getGm() == 0 && !테베라스컨트롤러.isOpen) {
	        ChattingController.toChatting(pc, "테베라스 사막으로 가는길이 닫혀있습니다.", Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }
	    // 레벨 체크
	    if (pc.getGm() == 0 && pc.getLevel() < Lineage.tebe_level) {
	        ChattingController.toChatting(pc, String.format("테베라스 사막은 %d레벨 이상 입장 가능합니다.", Lineage.tebe_level), Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }
	    // 수배자 체크
	    if (pc.getGm() == 0 && Lineage.tebe_wanted && !WantedController.checkWantedPc(pc)) {
	        ChattingController.toChatting(pc, "테베라스 사막은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }
	    // 혈맹 체크
	    if (pc.getGm() == 0 && Lineage.tebe_clan && pc.getClanId() == 0) {
	        ChattingController.toChatting(pc, "테베라스 사막은 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }
	    // [입장료 체크 - 단일 아이템/수량]
	    if (pc.getGm() == 0 && Lineage.tebe_join_item_count > 0) {
	        if (!pc.getInventory().isAden(Lineage.tebe_join_item, Lineage.tebe_join_item_count, true)) {
	            ChattingController.toChatting(pc,
	                String.format("입장에는 %s %d개가 필요합니다.", Lineage.tebe_join_item, Lineage.tebe_join_item_count),
	                Lineage.CHATTING_MODE_MESSAGE);
	            return;
	        }
	    }

	    // 실제 이동
	    pc.toPotal(Util.random(32742, 32742), Util.random(32803, 32797), 781);
	}

	public static int getDayOfWeek() {
		Calendar rightNow = Calendar.getInstance();
		int day_of_week = rightNow.get(Calendar.DAY_OF_WEEK);
		return day_of_week;
	}
}
