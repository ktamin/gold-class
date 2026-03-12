// 월드보스텔레포터: 월드보스 입장 요일 설정 외부화
package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.WantedController;
import lineage.world.controller.월드보스컨트롤러;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class 월드보스텔레포터 extends object {
    // 외부화된 개방 요일 (0=일,1=월,...,6=토)
    static private List<Integer> OPEN_DAYS;
    static {
        String conf = Lineage.world_boss_open_days;  // ex: "0,2,4,6"
        if (conf != null && !conf.isEmpty()) {
            OPEN_DAYS = Arrays.stream(conf.split(","))
                              .map(String::trim)
                              .map(Integer::parseInt)
                              .collect(Collectors.toList());
        } else {
            OPEN_DAYS = Arrays.asList(0,1,2,3,4,5,6);
        }
    }

    private String getDayName(int day) {
        switch (day) {
            case 0: return "일요일";
            case 1: return "월요일";
            case 2: return "화요일";
            case 3: return "수요일";
            case 4: return "목요일";
            case 5: return "금요일";
            case 6: return "토요일";
            default: return "";
        }
    }

    private String getOpenDaysString() {
        return OPEN_DAYS.stream()
                .map(this::getDayName)
                .collect(Collectors.joining(", "));
    }

    @Override
    public void toTalk(PcInstance pc, ClientBasePacket cbp) {
        List<String> list = new ArrayList<>();
        list.add(String.format("입장 레벨: %d이상 입장 가능", Lineage.world_level));
        list.add(String.format("수배 조건: %s", Lineage.world_wanted ? "수배자만 입장 가능" : "수배 없음"));
        list.add(String.format("혈맹 조건: %s", Lineage.world_clan ? "혈맹 필요" : "혈맹 없음"));
        list.add(String.format("입장 시간: %s", Lineage.world_dungeon_time));
        list.add(String.format("진행 시간: %s", Lineage.world_play_time < 60 ? Lineage.world_play_time + "초" : (Lineage.world_play_time/60) + "분"));
        list.add(String.format("개방 요일: %s", getOpenDaysString()));
        list.add(String.format("입장 가능: %s", 월드보스컨트롤러.isOpen ? "가능" : "불가"));
        pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "worldtel", null, list));
    }

    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
        if (!action.equalsIgnoreCase("world_teleport")) return;

        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        if (pc.getGm() == 0 && !OPEN_DAYS.contains(today)) {
            ChattingController.toChatting(pc,
                String.format("월드보스 토벌은 %s에만 입장 가능합니다.", getOpenDaysString()),
                Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        if (pc.getGm()>0 || 월드보스컨트롤러.isOpen) {
            if (pc.getGm()>0 || pc.getLevel()>=Lineage.world_level) {
                if (pc.getGm()>0 || !Lineage.world_wanted || (Lineage.world_wanted && WantedController.checkWantedPc(pc))) {
                    if (pc.getGm()>0 || !Lineage.world_clan || (Lineage.world_clan && pc.getClanId()>0)) {
                        pc.toPotal(Util.random(32867,32870), Util.random(32815,32818), 1400);
                    } else {
                        ChattingController.toChatting(pc, "혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                    }
                } else {
                    ChattingController.toChatting(pc, "수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                }
            } else {
                ChattingController.toChatting(pc,
                    String.format("%d레벨 이상만 입장 가능합니다.", Lineage.world_level),
                    Lineage.CHATTING_MODE_MESSAGE);
            }
        } else {
            ChattingController.toChatting(pc, "현재 입장 불가능한 시간입니다.", Lineage.CHATTING_MODE_MESSAGE);
        }
    }
}