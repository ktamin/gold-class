package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.AbandonedController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.WantedController;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class AbandonTelepoter extends object {
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		List<String> list = new ArrayList<String>();
		
		int nowday = getDayOfWeek ();
//		list.add(String.format("입장 레벨: %d이상 입장 가능", Lineage.abandon_level));
		if (Lineage.abandon_max_level > 0) {
			list.add(String.format("입장 레벨: %d이상 ~ %d미만 입장 가능", Lineage.abandon_level, Lineage.abandon_max_level));
		} else {
			list.add(String.format("입장 레벨: %d이상 입장 가능", Lineage.abandon_level));
		}		
		list.add(String.format("수배 조건: %s", Lineage.abandon_wanted ? "수배자만 입장 가능" : "수배 필요없음"));
		list.add(String.format("혈맹 조건: %s", Lineage.abandon_clan ? "혈맹 필요" : "혈맹 필요없음"));
		

		// 💡 [수정] 플레이 시간이 0 이하(상시 개방)일 때의 예외 처리
		if (Lineage.abandon_play_time <= 0) {
			list.add("입장 시간: 24시간 상시 오픈");
			list.add("진행 시간: 무제한");
		} else {		
		if(nowday == 1 || nowday == 7){
			list.add(String.format("입장 시간: %s", Lineage.abandon_dungeon_time2));	
		}else{
			list.add(String.format("입장 시간: %s", Lineage.abandon_dungeon_time));
		}
		list.add(String.format("진행 시간: %s", Lineage.abandon_play_time < 60 ? Lineage.abandon_play_time + "초" : (Lineage.abandon_play_time / 60) + "분"));
		}
		list.add(String.format("입장 가능 여부: %s", AbandonedController.isOpen ? "현재 입장 가능" : "입장 불가"));
		
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "abandontel", null, list));
	}
		
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
	    if (!"abandon_teleport".equalsIgnoreCase(action)) return;
	    
	    // ==========================================
	    // ✅ [추가] 고정 멤버 제한
	    // ==========================================
	    if (pc.getGm() == 0 && !pc.isMember()) {
	        ChattingController.toChatting(pc, "고정 멤버만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 1. 혈맹 제한
	    if (pc.getGm() == 0 && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
	        ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 2. 레벨 제한
	    if (pc.getGm() == 0 && pc.getLevel() < Lineage.abandon_level) {
	        ChattingController.toChatting(pc, String.format("버땅은 %d레벨 이상 입장 가능합니다.", Lineage.abandon_level), Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }
	    
	 // 💡 [추가] 레벨 제한 (최대 - 너무 레벨이 높은 유저 차단)
	 		if (pc.getGm() == 0 && Lineage.abandon_max_level > 0 && pc.getLevel() >= Lineage.abandon_max_level) {
	 			ChattingController.toChatting(pc, String.format("버땅은 %d레벨 이상은 입장하실 수 없습니다.", Lineage.abandon_max_level), Lineage.CHATTING_MODE_MESSAGE);
	 			return;
	 		}

	    // 3. 사냥터 오픈 여부
	    if (pc.getGm() == 0 && !AbandonedController.isOpen) {
	        ChattingController.toChatting(pc, "버땅으로 가는길이 닫혀있습니다.", Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 4. 입장료 체크
	    if (pc.getGm() == 0 && !pc.getInventory().isAden("아데나", Lineage.go_abandon, true)) {
	        ChattingController.toChatting(pc, String.format("입장료 아데나: %,d 부족합니다.", Lineage.go_abandon), Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 5. 수배자 제한 (옵션)
	    if (pc.getGm() == 0 && Lineage.abandon_wanted && !WantedController.checkWantedPc(pc)) {
	        ChattingController.toChatting(pc, "버땅은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 모두 통과
	    pc.toPotal(Util.random(32559, 32559), Util.random(32862, 32873), 777);
	}

	public static int getDayOfWeek() {
		Calendar rightNow = Calendar.getInstance();
		int day_of_week = rightNow.get(Calendar.DAY_OF_WEEK);
		return day_of_week;
	}
}
