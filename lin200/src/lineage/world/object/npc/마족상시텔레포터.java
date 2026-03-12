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
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class 마족상시텔레포터 extends object {

    @Override
    public void toTalk(PcInstance pc, ClientBasePacket cbp) {
        List<String> list = new ArrayList<String>();

        list.add(String.format("입장 레벨: %d이상 입장 가능", Lineage.dete_level));
        list.add(String.format("수배 조건: %s", Lineage.dete_wanted ? "수배자만 입장 가능" : "수배 필요없음"));
        list.add(String.format("혈맹 조건: %s", Lineage.dete_clan ? "혈맹 필요" : "혈맹 필요없음"));
        list.add(String.format("입장재료 : 아데나 %,d 개", Lineage.go_dete));

        pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "dete24tel", null, list));
    }

    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
        if (!action.equalsIgnoreCase("dete24_teleport"))
            return;

        // GM이면 모든 제한 무시하고 바로 입장
        if (pc.getGm() > 0) {
            // 입장
        	pc.toPotal(Util.random(32908, 32917), Util.random(32990, 32992), 410);
            return;
        }

        // 1) 레벨 체크
        if (pc.getLevel() < Lineage.dete_level) {
            ChattingController.toChatting(pc,
                String.format("마족 신전은 %d레벨 이상 입장 가능합니다.", Lineage.dete_level),
                Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        // 2) 수배 조건
        // wh_wanted = true  → 수배자만 입장
        // wh_wanted = false → 수배 필요 없음(= 수배자는 입장 가능/불가 정책이 없으니 통과)
        if (Lineage.dete_wanted && !WantedController.checkWantedPc(pc)) {
            ChattingController.toChatting(pc, "마족 신전은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        // 3) 혈맹 조건
        if (Lineage.dete_clan && pc.getClanId() <= 0) {
            ChattingController.toChatting(pc, "마족 신전은 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        // 4) 입장료(아데나) 체크 — 0이면 패스
        if (Lineage.go_dete > 0) {
            // 서버 공용: 인벤에 아데나 차감 API가 있으면 사용 (이름은 "아데나"로 고정)
            // true: 차감 성공 / false: 부족
            if (pc.getInventory() == null ||
                !pc.getInventory().isAden("아데나", Lineage.go_dete, true)) {
                ChattingController.toChatting(pc,
                    String.format("입장재료가 부족합니다. 아데나 %,d만 필요합니다.", Lineage.go_dete),
                    Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
        }

        // 5) 입장
        pc.toPotal(Util.random(32908, 32917), Util.random(32990, 32992), 410);
    }
}
