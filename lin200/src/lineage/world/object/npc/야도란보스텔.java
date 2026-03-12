package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class 야도란보스텔 extends object {

    /** 사냥터 이동 시 소모되는 아데나 비용 */
    private static final int TELEPORT_COST = 30000;

    @Override
    public void toTalk(PcInstance pc, ClientBasePacket cbp) {
        // HTML 창 띄우기
        List<String> ynlist = new ArrayList<>();
        pc.toSender(S_Html.clone(
            BasePacketPooling.getPool(S_Html.class),
            this,
            "yadolantelbs",
            null,
            ynlist
        ));
    }

    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
        // 버튼 액션이 아닐 경우 종료
        if (!action.startsWith("yadolantelboss-bs")) {
            return;
        }

        // 오픈 대기 체크
        if (Lineage.open_wait) {
            ChattingController.toChatting(pc,
                "[오픈대기] 오픈대기에는 이동 하실수 없습니다.",
                Lineage.CHATTING_MODE_MESSAGE
            );
            return;
        }

        // 상태 체크
        if (pc.isDead() || pc.isLock() || pc.isFishing()) {
            ChattingController.toChatting(pc,
                "현재 상태에선 사용할 수 없습니다.",
                Lineage.CHATTING_MODE_MESSAGE
            );
            return;
        }

        // 아데나 비용 차감
        if (!pc.getInventory().isAden(TELEPORT_COST, true)) {
            pc.toSender(S_Message.clone(
                BasePacketPooling.getPool(S_Message.class),
                189  // 아데나 부족 메시지
            ));
            return;
        }

        // action에서 bs번호 추출
        String numStr = action.substring("yadolantelboss-bs".length());
        int idx;
        try {
            idx = Integer.parseInt(numStr);
        } catch (NumberFormatException e) {
            return;
        }

        // 좌표 분기
        switch (idx) {
            case 1:
                switch (Util.random(1, 3)) {
                    case 1: pc.toPotal(32757, 32760, 10); break;
                    case 2: pc.toPotal(32780, 32767, 10); break;
                    case 3: pc.toPotal(32764, 32766, 10); break;
                }
                break;
            case 2:
                switch (Util.random(1, 3)) {
                    case 1: pc.toPotal(32729, 32788, 12); break;
                    case 2: pc.toPotal(32740, 32783, 12); break;
                    case 3: pc.toPotal(32740, 32774, 12); break;
                }
                break;
            case 3:
                switch (Util.random(1, 3)) {
                    case 1: pc.toPotal(33411, 32413, 4); break;
                    case 2: pc.toPotal(33404, 32403, 4); break;
                    case 3: pc.toPotal(33387, 32419, 4); break;
                }
                break;
            case 4:
                switch (Util.random(1, 3)) {
                    case 1: pc.toPotal(32707, 32846, 2); break;
                    case 2: pc.toPotal(32707, 32835, 2); break;
                    case 3: pc.toPotal(32707, 32859, 2); break;
                }
                break;
            case 5:
                switch (Util.random(1, 3)) {
                    case 1: pc.toPotal(32699, 32831, 82); break;
                    case 2: pc.toPotal(32706, 32809, 82); break;
                    case 3: pc.toPotal(32686, 32812, 82); break;
                }
                break;
            case 6:
                switch (Util.random(1, 3)) {
                    case 1: pc.toPotal(32786, 32786, 13); break;
                    case 2: pc.toPotal(32763, 32793, 13); break;
                    case 3: pc.toPotal(32765, 32776, 13); break;
                }
                break;
            case 7:
                switch (Util.random(1, 3)) {
                    case 1: pc.toPotal(32695, 32851, 0); break;
                    case 2: pc.toPotal(32672, 32867, 0); break;
                    case 3: pc.toPotal(32671, 32840, 0); break;
                }
                break;
            case 8:
                switch (Util.random(1, 2)) {
                    case 1: pc.toPotal(33726, 32250, 4); break;
                    case 2: pc.toPotal(33733, 32268, 4); break;
                }
                break;
            case 9:
                switch (Util.random(1, 3)) {
                    case 1: pc.toPotal(33387, 32338, 4); break;
                    case 2: pc.toPotal(33402, 32345, 4); break;
                    case 3: pc.toPotal(33370, 32347, 4); break;
                }
                break;
            case 10:
                switch (Util.random(1, 3)) {
                    case 1: pc.toPotal(32747, 32805, 51); break;
                    case 2: pc.toPotal(32717, 32813, 51); break;
                    case 3: pc.toPotal(32730, 32797, 51); break;
                }
                break;
            case 11:
                switch (Util.random(1, 3)) {
                    case 1: pc.toPotal(34260, 33385, 4); break;
                    case 2: pc.toPotal(34235, 33388, 4); break;
                    case 3: pc.toPotal(34233, 33418, 4); break;
                }
                break;
            case 12:
                switch (Util.random(1, 3)) {
                    case 1: pc.toPotal(33384, 32354, 4); break;
                    case 2: pc.toPotal(33387, 32333, 4); break;
                    case 3: pc.toPotal(33398, 32344, 4); break;
                }
                break;
            case 13:
                switch (Util.random(1, 3)) {
                    case 1: pc.toPotal(34253, 32278, 4); break;
                    case 2: pc.toPotal(34248, 32305, 4); break;
                    case 3: pc.toPotal(34264, 32306, 4); break;
                }
                break;
            case 14:
                switch (Util.random(1, 3)) {
                    case 1: pc.toPotal(32692, 32894, 200); break;
                    case 2: pc.toPotal(32682, 32903, 200); break;
                    case 3: pc.toPotal(32701, 32903, 200); break;
                }
                break;
            case 15:
                switch (Util.random(1, 3)) {
                    case 1: pc.toPotal(32700, 32820, 67); break;
                    case 2: pc.toPotal(32708, 32833, 67); break;
                    case 3: pc.toPotal(32684, 32828, 67); break;
                }
                break;
            case 16:
                switch (Util.random(1, 3)) {
                    case 1: pc.toPotal(32741, 32883, 524); break;
                    case 2: pc.toPotal(32741, 32906, 524); break;
                    case 3: pc.toPotal(32720, 32895, 524); break;
                }
                break;
            default:
                break;
        }
    }
}
