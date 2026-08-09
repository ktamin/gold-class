package lineage.world.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Fx.server.MJTemplate.MJProto.Models.SC_TIMER_UI_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_TIMER_UI_NOTI.TimerType;
import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI.ToastType;
import lineage.bean.database.TeamBattleTime;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_BlueMessage;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectName;
import lineage.network.packet.server.S_ObjectTitle;
import lineage.network.packet.server.S_ObjectPoly;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.ShapeChange;

public class Lostilandcontroller {
    // 상태 변수
    private static Calendar calendar;
    public  static boolean isOpen;
    public  static long    lostEndTime;
    private static long nextTeamingCheckAt = 0L; 

    private static int    lastRemainSecSent    = -1; 
    private static long nextTimerBroadcastAt = 0L; 
    
    private static final boolean ALWAYS_OPEN = false;
    public static List<PcInstance> anonymousList = new ArrayList<>();

    public static void init() {
        TimeLine.start("잊혀진섬 컨트롤러..");
        calendar = Calendar.getInstance();
        isOpen = false;
        lostEndTime = 0L;
        lastRemainSecSent = -1;
        nextTimerBroadcastAt = 0L;
        TimeLine.end();
    }

    @SuppressWarnings("deprecation")
    public static void toTimer(long nowMs) {
        if (ALWAYS_OPEN) {
            if (!isOpen) {
                isOpen = true;
                sendMessage(); 
            }
            return;
        }
        
        calendar.setTimeInMillis(nowMs);
        Date date = calendar.getTime();
        int hour = date.getHours();
        int min  = date.getMinutes();
        int day = getDayOfWeek();

        // 1. 오픈 스케줄 체크 (기존 로직 유지)
        if (!isOpen) {
            if (day == 1 || day == 7) {
                for (TeamBattleTime t : Lineage.lost_dungeon_time_list2) {
                    if (t.getHour() == hour && t.getMin() == min) {
                        open(nowMs);
                        break;
                    }
                }
            } else {
                for (TeamBattleTime t : Lineage.lost_dungeon_time_list) {
                    if (t.getHour() == hour && t.getMin() == min) {
                        open(nowMs);
                        break;
                    }
                }
            }
        }

        // 2. 진행 중 처리 (타이머 갱신 및 종료)
        if (isOpen) {
            long diffMs   = lostEndTime - nowMs;
            int  remainSec = (int)Math.max(0, diffMs / 1000L);

            if (diffMs <= 0) {
                close();
                sendTimerUI(false, nowMs); 
                return;
            }

            if (nowMs >= nextTimerBroadcastAt && remainSec != lastRemainSecSent) {
                sendTimerUI(true, nowMs);
                lastRemainSecSent = remainSec;
                nextTimerBroadcastAt = nowMs + 1000L;
            }
            
            // 티밍 방지 체크
            checkTeaming(nowMs);
        }
    }

    private static void open(long nowMs) {
        isOpen = true;
        lostEndTime = nowMs + (1000L * Lineage.lost_play_time);
        lastRemainSecSent = -1;
        nextTimerBroadcastAt = 0L;

        sendMessage();
        sendTimerUI(true, nowMs);
        nextTimerBroadcastAt = nowMs + 1000L;
    }

    private static void close() {
        isOpen = false;
        lostEndTime = 0L;
        lastRemainSecSent = -1;
        nextTimerBroadcastAt = 0L;
        sendMessage();
    }

    /**
     * [입장] 잊혀진 섬으로 보낼 때 NPC에서 호출
     */
    public static void enterAnonymous(PcInstance pc, int locX, int locY, int mapId) {
        // 이미 명단에 있는 유저라면 중복 실행 방지
        if (anonymousList.contains(pc)) return;

        // 1. 원본 데이터 백업
        pc.setTempName(pc.getName());
        pc.setTempTitle(pc.getTitle());
        pc.setTempClanName(pc.getClanName());
        pc.setTempClanId(pc.getClanId());
        pc.setTempClanGrade(pc.getClanGrade());

        // 2. 익명화 변조
        pc.setName("미지인");
        pc.setTitle("");
        pc.setClanName("");
        pc.setClanId(0);
        pc.setClanGrade(0);

        // 3. 잊섬 입장자 명단에 추가
        anonymousList.add(pc);

        // 4. 본인에게 정보 갱신 패킷 전송 (이름, 타이틀 지우기)
        pc.toSender(S_ObjectName.clone(BasePacketPooling.getPool(S_ObjectName.class), pc));
        pc.toSender(S_ObjectTitle.clone(BasePacketPooling.getPool(S_ObjectTitle.class), pc), true);

        // 5. 익명화 세팅이 끝난 후, 텔레포트 실행
        pc.toPotal(locX, locY, mapId);
    }

    /**
     * [퇴장] 귀환, 사망, 리스타트 시 PcInstance에서 호출
     */
    public static void exitAnonymous(PcInstance pc) {
        // 명단에 없는 유저라면 무시
        if (!anonymousList.contains(pc)) return;

        // 1. 원본 데이터 복구
        if (pc.getTempName() != null) pc.setName(pc.getTempName());
        if (pc.getTempTitle() != null) pc.setTitle(pc.getTempTitle());
        if (pc.getTempClanName() != null) pc.setClanName(pc.getTempClanName());
        pc.setClanId(pc.getTempClanId());
        pc.setClanGrade(pc.getTempClanGrade());

        // 2. 백업 데이터 초기화 (다음을 위해 비워둠)
        pc.setTempName(null);
        pc.setTempTitle(null);
        pc.setTempClanName(null);

        // 3. 잊섬 명단에서 제거
        anonymousList.remove(pc);

        // 4. 이름과 타이틀 갱신 패킷 전송
        pc.toSender(S_ObjectName.clone(BasePacketPooling.getPool(S_ObjectName.class), pc));
        pc.toSender(S_ObjectTitle.clone(BasePacketPooling.getPool(S_ObjectTitle.class), pc), true);
    }

    // 공지/토스트
    public static void sendMessage() {
        String chatMsg;
        String toastTitle, toastDesc;

        if (isOpen) {
            chatMsg   = "\\fY      ***** 잊혀진 섬으로 가는길이 열렸습니다. *****";
            toastTitle = "★잊혀진 섬 입장 가능 ★";
            toastDesc  = "던전이 열렸습니다. 지금 바로 입장하세요!";
        } else {
            chatMsg   = "\\fY      ***** 잊혀진 섬으로 가는길이 닫혔습니다. *****";
            toastTitle = "■ 잊혀진 섬 닫힘 안내";
            toastDesc  = "던전이 닫혔습니다. 다음 오픈을 기다려 주세요.";
        }

        // 채팅
        World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), chatMsg));
        // 화면 중앙
        if (Lineage.is_blue_message)
            World.toSender(S_BlueMessage.clone(BasePacketPooling.getPool(S_BlueMessage.class), 556, chatMsg));
        // 토스트
        for (PcInstance pc : World.getPcList()) {
            SC_TOAST_NOTI.newInstance()
                .setMessage(toastTitle)
                .setMessage2(toastDesc)
                .setToastType(ToastType.HeavyText)
                .send(pc);
        }
    }

 // ==========================================
    // 1. 타이머 UI (전체 전송 or 잊섬 유저 전송)
    // ==========================================
    private static void sendTimerUI(boolean show, long nowMs) {
        int remainSec = show ? (int)((lostEndTime - nowMs) / 1000L) : 0;
        for (PcInstance pc : World.getPcList()) {
            if (pc == null) continue;
            
            // 💡 잊혀진 섬(70) 내부 유저에게만 띄우고 싶다면 아래 주석(//) 해제
            // if (pc.getMap() != 70 && pc.getMap() != 809) continue;

            // ✅ ToastType이 아닌 TimerType.Normal 로 수정 완료
            SC_TIMER_UI_NOTI.newInstance()
                .setTimerType(TimerType.Normal) 
                .setRemainTime(remainSec)
                .send(pc);
        }
    }

    public static void pushTimerTo(PcInstance pc) {
        if (pc == null) return;
        int remainSec = isOpen ? (int)((lostEndTime - System.currentTimeMillis()) / 1000L) : 0;
        
        // ✅ ToastType이 아닌 TimerType.Normal 로 수정 완료
        SC_TIMER_UI_NOTI.newInstance()
            .setTimerType(TimerType.Normal) 
            .setRemainTime(remainSec)
            .send(pc);
    }
    
 // ==========================================
 	// 💡 [추가] 지정된 안전 텔레포트 구역 (낙하산 위치 10곳)
 	// ==========================================
 	private static final int[][] DROP_ZONES = {
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

 	// ==========================================
 	// 2. 티밍(밀집) 방지 로직 (성능 최적화 버전)
 	// ==========================================
 	public static void checkTeaming(long nowMs) {
 		// 던전이 열려있지 않거나, 아직 체크할 시간이 안 되었다면 통과
 		if (!isOpen || nowMs < nextTeamingCheckAt) return;
 		
 		nextTeamingCheckAt = nowMs + 10000L; // 다음 체크는 10초 뒤
 		
 		// 1단계: 전체 인원을 다 돌지 말고, "잊섬에 살아있는 일반 유저"만 명단으로 추려냅니다.
 		// 이렇게 하면 서버 렉을 엄청나게 줄일 수 있습니다.
 		java.util.List<PcInstance> map70Users = new java.util.ArrayList<>();
 		for (PcInstance pc : World.getPcList()) {
 			if (pc != null && pc.getMap() == 70 && pc.getGm() == 0 && !pc.isDead()) {
 				map70Users.add(pc);
 			}
 		}

 		// 2단계: 추려낸 잊섬 유저들끼리만 거리를 비교합니다.
 		for (PcInstance pc : map70Users) {
 			int count = 0;
 			for (PcInstance other : map70Users) {
 				if (pc == other) continue; // 자기 자신은 제외
 				
 				// 10칸 이내에 있으면 카운트 증가
 				if (Util.isDistance(pc, other, 10)) {
 					count++;
 				}
 			}

 			// 3단계: 나를 제외하고 주변에 4명 이상(즉 5명 이상 뭉쳐있다면) 강제 텔레포트
 			if (count >= 4) {
 				ChattingController.toChatting(pc, "\\fR[시스템] 과도한 밀집이 감지되어 강제 이동됩니다.", Lineage.CHATTING_MODE_MESSAGE);
 				
 				// 💡 [수정됨] 지정된 DROP_ZONES 배열에서 랜덤으로 하나의 인덱스(0~9)를 뽑습니다.
 				int randomIndex = Util.random(0, DROP_ZONES.length - 1);
 				
 				// 뽑힌 인덱스의 X, Y 좌표를 가져옵니다.
 				int targetX = DROP_ZONES[randomIndex][0];
 				int targetY = DROP_ZONES[randomIndex][1];
 				
 				// 안전하게 추출된 좌표로 텔레포트 (맵 번호: 70)
 				pc.toPotal(targetX, targetY, 70);
 			}
 		}
 	}
 	
/*
    // ==========================================
    // 2. 티밍(밀집) 방지 로직 (성능 최적화 버전)
    // ==========================================
    public static void checkTeaming(long nowMs) {
        // 던전이 열려있지 않거나, 아직 체크할 시간이 안 되었다면 통과
        if (!isOpen || nowMs < nextTeamingCheckAt) return;
        
        nextTeamingCheckAt = nowMs + 10000L; // 다음 체크는 10초 뒤
        
        // 1단계: 전체 인원을 다 돌지 말고, "잊섬에 살아있는 일반 유저"만 명단으로 추려냅니다.
        // 이렇게 하면 서버 렉을 엄청나게 줄일 수 있습니다.
        java.util.List<PcInstance> map70Users = new java.util.ArrayList<>();
        for (PcInstance pc : World.getPcList()) {
            if (pc != null && pc.getMap() == 70 && pc.getGm() == 0 && !pc.isDead()) {
                map70Users.add(pc);
            }
        }

        // 2단계: 추려낸 잊섬 유저들끼리만 거리를 비교합니다.
        for (PcInstance pc : map70Users) {
            int count = 0;
            for (PcInstance other : map70Users) {
                if (pc == other) continue; // 자기 자신은 제외
                
                // 10칸 이내에 있으면 카운트 증가
                if (Util.isDistance(pc, other, 10)) {
                    count++;
                }
            }

            // 3단계: 나를 제외하고 주변에 4명 이상(즉 5명 이상 뭉쳐있다면) 강제 텔레포트
            if (count >= 4) {
                ChattingController.toChatting(pc, "\\fR[시스템] 과도한 밀집이 감지되어 강제 이동됩니다.", Lineage.CHATTING_MODE_MESSAGE);
                
                // ⚠️ 주의: 이 좌표 범위(32650~32850)에 벽이나 바다가 없는지 꼭 확인하세요!
                pc.toPotal(Util.random(32677, 32819), Util.random(32893, 32999), 70);
            }
        }
    }
*/    
    public static int getDayOfWeek() {
        Calendar rightNow = Calendar.getInstance();
        return rightNow.get(Calendar.DAY_OF_WEEK);
    }
}