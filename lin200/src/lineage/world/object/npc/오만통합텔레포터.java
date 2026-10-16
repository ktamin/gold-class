package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.WantedController;
import lineage.world.controller.오만1층컨트롤러;
import lineage.world.controller.오만2층컨트롤러; // 필요한 층 컨트롤러 import
import lineage.world.controller.오만3층컨트롤러;
import lineage.world.controller.오만4층컨트롤러;
import lineage.world.controller.오만5층컨트롤러;
import lineage.world.controller.오만6층컨트롤러;
import lineage.world.controller.오만7층컨트롤러;
import lineage.world.controller.오만8층컨트롤러;
import lineage.world.controller.오만9층컨트롤러;
import lineage.world.controller.오만10층컨트롤러;
import lineage.world.controller.오만정상컨트롤러;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class 오만통합텔레포터 extends object {

    @Override
    public void toTalk(PcInstance pc, ClientBasePacket cbp) {
        showHtml(pc);
    }
    
    public void showHtml(PcInstance pc){
        List<String> ynlist = new ArrayList<String>();
        
        // HTML에 %s 순서대로 [열림] / [닫힘] 상태를 전달합니다.
        ynlist.add(String.format("%s", 오만1층컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
        ynlist.add(String.format("%s", 오만2층컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
        ynlist.add(String.format("%s", 오만3층컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
        ynlist.add(String.format("%s", 오만4층컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
        ynlist.add(String.format("%s", 오만5층컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
        ynlist.add(String.format("%s", 오만6층컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
        ynlist.add(String.format("%s", 오만7층컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
        ynlist.add(String.format("%s", 오만8층컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
        ynlist.add(String.format("%s", 오만9층컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
        ynlist.add(String.format("%s", 오만10층컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
        ynlist.add(String.format("%s", 오만정상컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
        
        pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "omantotaltel", null, ynlist));
    }    
    
    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp){
        if (Lineage.open_wait) {
            ChattingController.toChatting(pc, "[오픈대기] 오픈대기에는 이동 하실수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }        
        
        if (pc.isDead() || pc.isLock() || pc.isFishing()){
            ChattingController.toChatting(pc, "현재 상태에선 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }
        
        // ----------------------------------------------------------------------
        // action 분기: 각 층별 버튼 클릭 시 '공통 입장 함수(tryEnterOman)'를 호출합니다.
        // ----------------------------------------------------------------------
        if (action.equalsIgnoreCase("oman_1")) {
            // 💡 [수정] 오만의 탑 1층 안전 좌표 3곳 입력
            int[][] locs = { {32735, 32799}, {32855, 32800}, {32796, 32798} };
            int rnd = Util.random(0, 2);

            tryEnterOman(pc, "오만의 탑 1층", 오만1층컨트롤러.isOpen, Lineage.oman1_level, Lineage.oman1_wanted, Lineage.oman1_clan, Lineage.go_oman1, 
                         locs[rnd][0], locs[rnd][1], 101, // 1층 좌표 및 맵ID
                         Lineage.oman1_open_day_list, Lineage.oman1_dungeon_time, Lineage.oman1_dungeon_time2);
        }
        else if (action.equalsIgnoreCase("oman_2")) {
            // 💡 [수정] 오만의 탑 2층 안전 좌표 3곳 입력
            int[][] locs = { {32796, 32799}, {32869, 32784}, {32732, 32852} };
            int rnd = Util.random(0, 2);

            tryEnterOman(pc, "오만의 탑 2층", 오만2층컨트롤러.isOpen, Lineage.oman2_level, Lineage.oman2_wanted, Lineage.oman2_clan, Lineage.go_oman2, 
                         locs[rnd][0], locs[rnd][1], 102, // 2층 좌표 및 맵ID
                         Lineage.oman2_open_day_list, Lineage.oman2_dungeon_time, Lineage.oman2_dungeon_time2);
        }
        else if (action.equalsIgnoreCase("oman_3")) {
            // 💡 [수정] 오만의 탑 3층 안전 좌표 3곳 입력
            int[][] locs = { {32795, 32799}, {32729, 32798}, {32792, 32746} };
            int rnd = Util.random(0, 2);

            tryEnterOman(pc, "오만의 탑 3층", 오만3층컨트롤러.isOpen, Lineage.oman3_level, Lineage.oman3_wanted, Lineage.oman3_clan, Lineage.go_oman3, 
                         locs[rnd][0], locs[rnd][1], 103, // 3층 좌표 및 맵ID
                         Lineage.oman3_open_day_list, Lineage.oman3_dungeon_time, Lineage.oman3_dungeon_time2);
        }
        else if (action.equalsIgnoreCase("oman_4")) {
            // 💡 [수정] 오만의 탑 4층 안전 좌표 3곳 입력
            int[][] locs = { {32667, 32866}, {32674, 32814}, {32602, 32866} };
            int rnd = Util.random(0, 2);

            tryEnterOman(pc, "오만의 탑 4층", 오만4층컨트롤러.isOpen, Lineage.oman4_level, Lineage.oman4_wanted, Lineage.oman4_clan, Lineage.go_oman4, 
                         locs[rnd][0], locs[rnd][1], 104, // 4층 좌표 및 맵ID
                         Lineage.oman4_open_day_list, Lineage.oman4_dungeon_time, Lineage.oman4_dungeon_time2);
        }
        else if (action.equalsIgnoreCase("oman_5")) {
            // 💡 [수정] 오만의 탑 5층 안전 좌표 3곳 입력
            int[][] locs = { {32602, 32866}, {32669, 32863}, {32741, 32926}};
            int rnd = Util.random(0, 2);

            // 주의: 기존 코드에 있던 oman4 관련 오타를 oman5로 모두 수정했습니다.
            tryEnterOman(pc, "오만의 탑 5층", 오만5층컨트롤러.isOpen, Lineage.oman5_level, Lineage.oman5_wanted, Lineage.oman5_clan, Lineage.go_oman5, 
                         locs[rnd][0], locs[rnd][1], 105, // 5층 좌표 및 맵ID
                         Lineage.oman5_open_day_list, Lineage.oman5_dungeon_time, Lineage.oman5_dungeon_time2);
        }
        else if (action.equalsIgnoreCase("oman_6")) {
            // 💡 [수정] 오만의 탑 6층 안전 좌표 3곳 입력
            int[][] locs = { {32691, 32866}, {32733, 32861}, {32651, 32938} };
            int rnd = Util.random(0, 2);

            tryEnterOman(pc, "오만의 탑 6층", 오만6층컨트롤러.isOpen, Lineage.oman6_level, Lineage.oman6_wanted, Lineage.oman6_clan, Lineage.go_oman6, 
                         locs[rnd][0], locs[rnd][1], 106, // 6층 좌표 및 맵ID
                         Lineage.oman6_open_day_list, Lineage.oman6_dungeon_time, Lineage.oman6_dungeon_time2);
        }
        else if (action.equalsIgnoreCase("oman_7")) {
            // 💡 [수정] 오만의 탑 7층 안전 좌표 3곳 입력
            int[][] locs = { {32601, 32858}, {32668, 32862}, {32726, 32915} };
            int rnd = Util.random(0, 2);

            tryEnterOman(pc, "오만의 탑 7층", 오만7층컨트롤러.isOpen, Lineage.oman7_level, Lineage.oman7_wanted, Lineage.oman7_clan, Lineage.go_oman7, 
                         locs[rnd][0], locs[rnd][1], 107, // 7층 좌표 및 맵ID
                         Lineage.oman7_open_day_list, Lineage.oman7_dungeon_time, Lineage.oman7_dungeon_time2);
        }
        else if (action.equalsIgnoreCase("oman_8")) {
            // 💡 [수정] 오만의 탑 8층 안전 좌표 3곳 입력
            int[][] 유locs = { {32727, 32809}, {32668, 32863}, {32672, 32932} };
            int rnd = Util.random(0, 2);

            tryEnterOman(pc, "오만의 탑 8층", 오만8층컨트롤러.isOpen, Lineage.oman8_level, Lineage.oman8_wanted, Lineage.oman8_clan, Lineage.go_oman8, 
                         유locs[rnd][0], 유locs[rnd][1], 108, // 8층 좌표 및 맵ID
                         Lineage.oman8_open_day_list, Lineage.oman8_dungeon_time, Lineage.oman8_dungeon_time2);
        }
        else if (action.equalsIgnoreCase("oman_9")) {
            // 💡 [수정] 오만의 탑 9층 안전 좌표 3곳 입력
            int[][] locs = { {32724, 32811}, {32667, 32862}, {32622, 32816} };
            int rnd = Util.random(0, 2);

            tryEnterOman(pc, "오만의 탑 9층", 오만9층컨트롤러.isOpen, Lineage.oman9_level, Lineage.oman9_wanted, Lineage.oman9_clan, Lineage.go_oman9, 
                         locs[rnd][0], locs[rnd][1], 109, // 9층 좌표 및 맵ID
                         Lineage.oman9_open_day_list, Lineage.oman9_dungeon_time, Lineage.oman9_dungeon_time2);
        }
        else if (action.equalsIgnoreCase("oman_10")) {
            // 💡 [수정] 오만의 탑 10층 안전 좌표 3곳 입력
            int[][] locs = { {32753, 32747}, {32740, 32848}, {32796, 32797} };
            int rnd = Util.random(0, 2);

            tryEnterOman(pc, "오만의 탑 10층", 오만10층컨트롤러.isOpen, Lineage.oman10_level, Lineage.oman10_wanted, Lineage.oman10_clan, Lineage.go_oman10, 
                         locs[rnd][0], locs[rnd][1], 110, // 10층 좌표 및 맵ID
                         Lineage.oman10_open_day_list, Lineage.oman10_dungeon_time, Lineage.oman10_dungeon_time2);
        }
        else if (action.equalsIgnoreCase("oman_top")) {
            // 💡 [수정] 오만의 탑 정상 안전 좌표 3곳 입력
            int[][] locs = { {32612, 32796}, {32775, 32986}, {32808, 32819} };
            int rnd = Util.random(0, 2);

            tryEnterOman(pc, "오만의 탑 정상", 오만정상컨트롤러.isOpen, Lineage.oman0_level, Lineage.oman0_wanted, Lineage.oman0_clan, Lineage.go_oman0, 
                         locs[rnd][0], locs[rnd][1], 200, // 정상 좌표 및 맵ID
                         Lineage.oman0_open_day_list, Lineage.oman0_dungeon_time, Lineage.oman0_dungeon_time2);
        }
    }

    /**
     * ✅ 공통 입장 검사 함수 (수백 줄의 중복 코드를 방지합니다)
     */
    private void tryEnterOman(PcInstance pc, String floorName, boolean isOpen, int reqLevel, boolean reqWanted, boolean reqClan, int reqAdena, 
                              int locX, int locY, int mapId, List<Integer> openDaysList, String weekTime, String weekendTime) {
    	
        // * 오픈대기
		if (Lineage.open_wait) {
			ChattingController.toChatting(pc, "[오픈대기] 오픈대기에는 이동 하실수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}	
		
 		// ✅ [추가] 고정 멤버 제한 (가장 먼저 체크)
 		// ==========================================
 		if (pc.getGm() == 0 && !pc.isMember()) {
			ChattingController.toChatting(pc, "고정 멤버만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
 			return;
		}
        
        // 1. GM이 아니면서 사냥터가 닫혀있을 때 상세 안내
        if (pc.getGm() == 0 && !isOpen) {
            String daysStr = convertDaysToKorean(openDaysList);
            String closeMsg = String.format("[%s] 닫힘 (오픈: %s / 평일 %s, 주말 %s)", floorName, daysStr, weekTime, weekendTime);
            ChattingController.toChatting(pc, closeMsg, Lineage.CHATTING_MODE_MESSAGE);
            return;
        }
        
        // 2. 레벨 검사
        if (pc.getGm() == 0 && pc.getLevel() < reqLevel) {
            ChattingController.toChatting(pc, String.format("%s은(는) %d레벨 이상 입장 가능합니다.", floorName, reqLevel), Lineage.CHATTING_MODE_MESSAGE);
            return;
        }
        
        // 3. 수배자 검사
        if (pc.getGm() == 0 && reqWanted && !WantedController.checkWantedPc(pc)) {
            ChattingController.toChatting(pc, String.format("%s은(는) 수배자만 입장 가능합니다.", floorName), Lineage.CHATTING_MODE_MESSAGE);
            return;
        }
        
        // 4. 혈맹 검사 (무혈 및 신규혈맹)
        if (pc.getGm() == 0 && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
            ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }
        
        // 5. 혈맹 전용 검사
        if (pc.getGm() == 0 && reqClan && pc.getClanId() == 0) {
            ChattingController.toChatting(pc, String.format("%s은(는) 혈맹 가입자만 입장 가능합니다.", floorName), Lineage.CHATTING_MODE_MESSAGE);
            return;
        }
        
        // 6. 아데나 검사 (지불 포함)
        if (pc.getGm() == 0 && !pc.getInventory().isAden("아데나", reqAdena, true)) {
            ChattingController.toChatting(pc, String.format("%s 입장 재료(아데나)가 부족합니다.", floorName), Lineage.CHATTING_MODE_MESSAGE);
            return;
        }
        
        // 7. 모든 조건을 만족하면 텔레포트 실행
        pc.toPotal(locX, locY, mapId);
    }

    /**
     * ✅ 숫자(0~6) 리스트를 한글 요일 문자열(일~토)로 예쁘게 바꿔주는 헬퍼 함수
     */
    private String convertDaysToKorean(List<Integer> daysList) {
        String[] dayNames = {"일", "월", "화", "수", "목", "금", "토"};
        String result = "";
        for (int d : daysList) {
            if (d >= 0 && d <= 6) {
                result += dayNames[d] + " ";
            }
        }
        result = result.trim().replace(" ", ", ");
        return result.isEmpty() ? "설정 없음" : result;
    }
}