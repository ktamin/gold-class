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
        int nowday = getDayOfWeek();
        
        list.add(String.format("입장 레벨: %d이상 입장 가능", Lineage.dark3_level));
        list.add(String.format("수배 조건: %s", Lineage.dark3_wanted ? "수배자만 입장 가능" : "수배 필요없음"));
        list.add(String.format("혈맹 조건: %s", Lineage.dark3_clan ? "혈맹 필요" : "혈맹 필요없음"));
        
        if (nowday == 1 || nowday == 7) {
            list.add(String.format("입장 시간: %s", Lineage.dark3_dungeon_time2));    
        } else {
            list.add(String.format("입장 시간: %s", Lineage.dark3_dungeon_time));    
        }
        
        list.add(String.format("진행 시간: %s", Lineage.dark3_play_time < 60 ? Lineage.dark3_play_time + "초" : (Lineage.dark3_play_time / 60) + "분"));
        list.add(String.format("입장 가능 여부: %s", 칠흑던전3층컨트롤러.isOpen ? "현재 입장 가능" : "입장 불가"));
        
        pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "darktel3", null, list));
    }

    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
        if (action.equalsIgnoreCase("dark3_teleport")) {
            
            // 1. 운영자거나 던전이 열려있는지 확인
            if (pc.getGm() <= 0 && !칠흑던전3층컨트롤러.isOpen) {
                ChattingController.toChatting(pc, "칠흑던전으로 가는길이 닫혀있습니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }

            // 2. 고정멤버 확인
           if (pc.getGm() <= 0 && !pc.isMember()) {
                ChattingController.toChatting(pc, "고정 멤버만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
           }

            // 3. 파티 상태 확인
            if (pc.getGm() <= 0 && pc.getPartyId() > 0) {
                ChattingController.toChatting(pc, "파티를 해제한 후 입장해주세요.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }

            // 4. 레벨 확인
            if (pc.getGm() <= 0 && pc.getLevel() < Lineage.dark3_level) {
                ChattingController.toChatting(pc, String.format("칠흑던전은 %d레벨 이상 입장 가능합니다.", Lineage.dark3_level), Lineage.CHATTING_MODE_MESSAGE);
                return;
            }

            // 5. 수배자 조건 확인
            if (pc.getGm() <= 0 && Lineage.dark3_wanted && !WantedController.checkWantedPc(pc)) {
                ChattingController.toChatting(pc, "칠흑던전은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }

            // 6. 혈맹 조건 확인
            if (pc.getGm() <= 0 && Lineage.dark3_clan && pc.getClanId() <= 0) {
                ChattingController.toChatting(pc, "칠흑던전은 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }


            // ==========================================
            // 바다나 건물에 끼지 않도록, 100% 안전한 평지 좌표들을 미리 여러 개 세팅해 둡니다.
            int[][] safeLocs = {
                {32780, 32769}, {32800, 32797}, {32762, 32798}
            };

            // 배열에 있는 좌표들 중 랜덤으로 하나를 선택합니다.
            int rndIndex = Util.random(0, safeLocs.length - 1);
            int targetX = safeLocs[rndIndex][0];
            int targetY = safeLocs[rndIndex][1];
               lineage.world.controller.칠흑던전3층컨트롤러.enterAnonymous(pc, targetX, targetY, 809);
            // ==========================================
            
            // 9. 타이머 UI 전송
               칠흑던전3층컨트롤러.pushTimerTo(pc);;
        }
    }

    public static int getDayOfWeek() {
        Calendar rightNow = Calendar.getInstance();
        return rightNow.get(Calendar.DAY_OF_WEEK);
    }
}