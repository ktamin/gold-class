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
import lineage.world.controller.Lostilandcontroller;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class LostIslandTeleporter extends object {

    @Override
    public void toTalk(PcInstance pc, ClientBasePacket cbp) {
        List<String> list = new ArrayList<String>();
        int nowday = getDayOfWeek();
        
        list.add(String.format("입장 레벨: %d이상 입장 가능", Lineage.lost_level));
        list.add(String.format("수배 조건: %s", Lineage.lost_wanted ? "수배자만 입장 가능" : "수배 필요없음"));
        list.add(String.format("혈맹 조건: %s", Lineage.lost_clan ? "혈맹 필요" : "혈맹 필요없음"));
        
        if (nowday == 1 || nowday == 7) {
            list.add(String.format("입장 시간: %s", Lineage.lost_dungeon_time2));    
        } else {
            list.add(String.format("입장 시간: %s", Lineage.lost_dungeon_time));    
        }
        
        list.add(String.format("진행 시간: %s", Lineage.lost_play_time < 60 ? Lineage.lost_play_time + "초" : (Lineage.lost_play_time / 60) + "분"));
        list.add(String.format("입장 가능 여부: %s", Lostilandcontroller.isOpen ? "현재 입장 가능" : "입장 불가"));
        
        pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "losttel", null, list));
    }

    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
        if (action.equalsIgnoreCase("lostIsland_teleport")) {
            
            // 1. 운영자거나 던전이 열려있는지 확인
            if (pc.getGm() <= 0 && !Lostilandcontroller.isOpen) {
                ChattingController.toChatting(pc, "잊혀진 섬으로 가는길이 닫혀있습니다.", Lineage.CHATTING_MODE_MESSAGE);
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
            if (pc.getGm() <= 0 && pc.getLevel() < Lineage.lost_level) {
                ChattingController.toChatting(pc, String.format("잊혀진 섬은 %d레벨 이상 입장 가능합니다.", Lineage.lost_level), Lineage.CHATTING_MODE_MESSAGE);
                return;
            }

            // 5. 수배자 조건 확인
            if (pc.getGm() <= 0 && Lineage.lost_wanted && !WantedController.checkWantedPc(pc)) {
                ChattingController.toChatting(pc, "잊혀진 섬은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }

            // 6. 혈맹 조건 확인
            if (pc.getGm() <= 0 && Lineage.lost_clan && pc.getClanId() <= 0) {
                ChattingController.toChatting(pc, "잊혀진 섬은 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }

            // ==========================================
            // ✅ [수정] 잊섬 입장 시 배틀그라운드식 무작위 랜덤 드랍
            // ==========================================
            // 유저들이 입구에 뭉쳐서 티밍 페널티를 받는 것을 방지하기 위해 맵 전체로 흩뿌립니다.
            //   int targetX = Util.random(32677, 32819);
            //   int targetY = Util.random(32893, 32999);
            //   lineage.world.controller.Lostilandcontroller.enterAnonymous(pc, targetX, targetY, 70);
            // ==========================================
            
            // 지정한 위치로 이동
            int[][] safeLocations = {
                {32775, 32911}, // 1번 낙하산 위치 (임시 좌표 - 수정 필요)
                {32709, 32867}, // 2번 낙하산 위치
                {32646, 32929}, // 3번 낙하산 위치
                {32648, 32917}, // 4번 낙하산 위치
                {32652, 33001}, // 5번 낙하산 위치
                {32650, 33009}, // 6번 낙하산 위치
                {32830, 32704}, // 7번 낙하산 위치
                {32808, 32777}, // 8번 낙하산 위치
                {32833, 32840}, // 9번 낙하산 위치
                {32956, 32834}  // 10번 낙하산 위치
            };

            // 0번부터 배열의 마지막 번호까지 중 무작위로 하나를 뽑습니다.
            int randomIndex = Util.random(0, safeLocations.length - 1);
            
            // 뽑힌 번호의 X, Y 좌표를 꺼냅니다.
            int targetX = safeLocations[randomIndex][0];
            int targetY = safeLocations[randomIndex][1];

            // 해당 좌표로 입장!
            lineage.world.controller.Lostilandcontroller.enterAnonymous(pc, targetX, targetY, 70);
            // ==========================================
            
            // 9. 타이머 UI 전송
            Lostilandcontroller.pushTimerTo(pc);
        }
    }

    public static int getDayOfWeek() {
        Calendar rightNow = Calendar.getInstance();
        return rightNow.get(Calendar.DAY_OF_WEEK);
    }
}