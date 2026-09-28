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
// 💡 [삭제] 기존의 static 변수와 static { ... } 블록은 지워주세요.
    
    // 💡 [추가] 리로드 시 즉각 반영되도록, 호출될 때마다 콘프를 파싱하는 메서드 생성
    private List<Integer> getOpenDaysList() {
        String conf = Lineage.world_boss_open_days;  // ex: "0,2,4,6"
        if (conf != null && !conf.isEmpty()) {
            return Arrays.stream(conf.split(","))
                         .map(String::trim)
                         .map(Integer::parseInt)
                         .collect(Collectors.toList());
        } else {
            // 값이 비어있으면 매일(일~토) 개방
            return Arrays.asList(0, 1, 2, 3, 4, 5, 6);
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

    // 💡 [수정] 고정된 OPEN_DAYS 변수 대신, 새로 만든 getOpenDaysList()를 호출하도록 변경
    private String getOpenDaysString() {
        return getOpenDaysList().stream()
                .map(this::getDayName)
                .collect(Collectors.joining(", "));
    }

    @Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		List<String> list = new ArrayList<>();
		list.add(String.format("입장 레벨: %d이상 입장 가능", Lineage.world_level));
		list.add(String.format("수배 조건: %s", Lineage.world_wanted ? "수배자만 입장 가능" : "수배 없음"));
		list.add(String.format("혈맹 조건: %s", Lineage.world_clan ? "혈맹 필요" : "혈맹 없음"));
		
		// 💡 [수정] 평일 시간과 주말 시간을 나누어 표시하도록 변경
		list.add(String.format("평일 시간: %s", Lineage.world_dungeon_time));
		if (Lineage.world_dungeon_time2 != null && !Lineage.world_dungeon_time2.isEmpty()) {
			list.add(String.format("주말 시간: %s", Lineage.world_dungeon_time2));
		}
		
		list.add(String.format("진행 시간: %s", Lineage.world_play_time < 60 ? Lineage.world_play_time + "초" : (Lineage.world_play_time/60) + "분"));
		list.add(String.format("개방 요일: %s", getOpenDaysString())); // 실시간 계산됨
		list.add(String.format("입장 가능: %s", 월드보스컨트롤러.isOpen ? "가능" : "불가"));
		
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "worldtel", null, list));
	}
    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
        if (!action.equalsIgnoreCase("world_teleport")) return;
        
            // ==========================================
     		// ✅ [추가] 고정 멤버 제한 (가장 먼저 체크)
     		// ==========================================
     		if (pc.getGm() == 0 && !pc.isMember()) {
    			ChattingController.toChatting(pc, "고정 멤버만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
     			return;
    		}

     		int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
            
            // 💡 [수정] OPEN_DAYS 변수 대신 getOpenDaysList() 함수를 호출하여 확인하도록 변경
            if (pc.getGm() == 0 && !getOpenDaysList().contains(today)) {
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