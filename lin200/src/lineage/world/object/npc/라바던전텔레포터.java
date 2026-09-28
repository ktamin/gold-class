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
		
		// 1. Config에 설정된 오픈 요일 숫자(0~6)를 한글(일~토)로 변환
		String[] dayNames = {"일", "월", "화", "수", "목", "금", "토"};
		String openDays = "";
		
		for (int d : Lineage.lasta_open_day_list) {
			if(d >= 0 && d <= 6) {
				openDays += dayNames[d] + " ";
			}
		}
		// 끝에 남는 공백을 없애고 쉼표(,)로 예쁘게 이어줍니다. (예: "화, 목, 토")
		openDays = openDays.trim().replace(" ", ", ");
		if(openDays.isEmpty()) {
			openDays = "설정 없음";
		}
		
		// 2. 정보 출력 (안내 문구 추가)
		list.add(String.format("오픈 요일: %s요일", openDays)); // ★ 요일 안내 추가
		list.add(String.format("입장 레벨: %d이상 입장 가능", Lineage.lasta_level));
		list.add(String.format("수배 조건: %s", Lineage.lasta_wanted ? "수배자만 입장 가능" : "수배 필요없음"));
		list.add(String.format("혈맹 조건: %s", Lineage.lasta_clan ? "혈맹 필요" : "혈맹 필요없음"));
		
		// 3. 평일/주말 시간을 유저가 모두 확인할 수 있도록 각각 출력
		list.add(String.format("평일 시간: %s", Lineage.lasta_dungeon_time));
		list.add(String.format("주말 시간: %s", Lineage.lasta_dungeon_time2));
		
		list.add(String.format("진행 시간: %s", Lineage.lasta_play_time < 60 ? Lineage.lasta_play_time + "초" : (Lineage.lasta_play_time / 60) + "분"));
		list.add(String.format("입장 가능 여부: %s", 라바던전컨트롤러.isOpen ? "현재 입장 가능" : "입장 불가"));
		
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "lastatel", null, list));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
	    if (!"lasta_teleport".equalsIgnoreCase(action)) return;
	    
        // * 오픈대기
		if (Lineage.open_wait) {
			ChattingController.toChatting(pc, "[오픈대기] 오픈대기에는 이동 하실수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}	
	    
		// ✅ [추가] 고정 멤버 제한 (가장 먼저 체크)
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
	    if (pc.getGm() == 0 && pc.getLevel() < Lineage.lasta_level) {
	        ChattingController.toChatting(pc, String.format("라스타바드던전은 %d레벨 이상 입장 가능합니다.", Lineage.lasta_level), Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 3. 사냥터 오픈 여부
	    if (pc.getGm() == 0 && !라바던전컨트롤러.isOpen) {
	        ChattingController.toChatting(pc, "라스타바드 던전으로 가는길이 닫혀있습니다.", Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 5. 수배자 제한 (옵션)
	    if (pc.getGm() == 0 && Lineage.lasta_wanted && !WantedController.checkWantedPc(pc)) {
	        ChattingController.toChatting(pc, "라스타바드 던전은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }
	    
	    // 4. 입장료 체크
	    if (pc.getGm() == 0 && !pc.getInventory().isAden("아데나", Lineage.go_lasta, true)) {
	        ChattingController.toChatting(pc, String.format("입장료 아데나: %,d 부족합니다.", Lineage.go_lasta), Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 모두 통과
//	    pc.toPotal(Util.random(32733, 32737), Util.random(32848, 32865), 451);
	       // 7. 모든 조건 만족 → 입장 처리 (3개의 지점 중 랜덤)
	 		int rnd = Util.random(1, 3); // 💡 1, 2, 3 중 랜덤으로 숫자 하나를 뽑습니다.
	 		
	 		if (rnd == 1) {
	 			// 📍 첫 번째 위치 (기존 좌표)
	 			pc.toPotal(Util.random(32742, 32739), Util.random(32839, 32837), 451);
	 		} 
	 		else if (rnd == 2) {
	 			// 📍 두 번째 위치 (좌표 숫자를 원하시는 곳으로 수정하세요)
	 			pc.toPotal(Util.random(32737, 32743), Util.random(32858, 32857), 451); 
	 		} 
	 		else {
	 			// 📍 세 번째 위치 (좌표 숫자를 원하시는 곳으로 수정하세요)
	 			pc.toPotal(Util.random(32759, 32753), Util.random(32845, 32845), 451);
	 		}
	}

	public static int getDayOfWeek() {
		Calendar rightNow = Calendar.getInstance();
		int day_of_week = rightNow.get(Calendar.DAY_OF_WEEK);
		return day_of_week;
	}
}
