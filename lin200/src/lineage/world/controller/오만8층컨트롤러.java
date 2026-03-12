package lineage.world.controller;

import java.util.Calendar;
import java.util.Date;

import Fx.server.MJTemplate.MJProto.Models.SC_TIMER_UI_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_TIMER_UI_NOTI.TimerType;
import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI.ToastType;
import lineage.bean.database.TeamBattleTime;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_BlueMessage;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.World;
import lineage.world.object.instance.PcInstance;

public class 오만8층컨트롤러 {
    // 상태
    private static Calendar calendar;
    public  static boolean isOpen;
    public  static long    oman8EndTime;

    // 타이머 브로드캐스트 제어
    private static int  lastRemainSecSent    = -1; // 마지막 전송한 remainSec (중복 방지)
    private static long nextTimerBroadcastAt = 0L; // 다음 갱신 전송 시각(ms)
    
    // 항상 열려있는 사냥터로 운용할 때 true
    private static final boolean ALWAYS_OPEN = true;

    public static void init() {
        TimeLine.start("오만8층 컨트롤러..");
        calendar = Calendar.getInstance();
        isOpen = false;
        oman8EndTime = 0L;
        lastRemainSecSent = -1;
        nextTimerBroadcastAt = 0L;
        TimeLine.end();
    }

    @SuppressWarnings("deprecation")
    public static void toTimer(long nowMs) {
    	
    	/*상시개방*/
        if (ALWAYS_OPEN) {
            // 상시 모드: 스케줄 체크/타이머 브로드캐스트 전부 비활성
            if (!isOpen) {
                isOpen = true;
                sendMessage(); // 혹시 모를 재시작 시 1회만
            }
            // 타이머 UI는 항상 숨김 유지
            return;
        }
        
        
        // 현재 시/분
        calendar.setTimeInMillis(nowMs);
        Date date = calendar.getTime();
        int hour = date.getHours();
        int min  = date.getMinutes();

        // 요일(1=일, 7=토)
        int day = getDayOfWeek();

        // 스케줄 체크 (열릴 때만 트리거)
        if (!isOpen) {
            if (day == 1 || day == 7) {
                // 주말 스케줄
                for (TeamBattleTime t : Lineage.oman8_dungeon_time_list2) {
                    if (t.getHour() == hour && t.getMin() == min) {
                        open(nowMs);
                        break;
                    }
                }
            } else {
                // 평일 스케줄
                for (TeamBattleTime t : Lineage.oman8_dungeon_time_list) {
                    if (t.getHour() == hour && t.getMin() == min) {
                        open(nowMs);
                        break;
                    }
                }
            }
        }

        // 열려 있으면 주기 갱신/종료 처리
        if (isOpen) {
            long diffMs   = oman8EndTime - nowMs;
            int  remainSec = (int)Math.max(0, diffMs / 1000L);

            // 종료 시점
            if (diffMs <= 0) {
                close();               // 상태/메시지
                sendTimerUI(false, nowMs); // 0초 내려서 타이머 끄기
                return;
            }

            // 1초 간격으로만 브로드캐스트 (원하면 5000L로 줄여 부하 감소)
            if (nowMs >= nextTimerBroadcastAt && remainSec != lastRemainSecSent) {
                sendTimerUI(true, nowMs);         // 남은 시간(초) 전송
                lastRemainSecSent = remainSec;
                nextTimerBroadcastAt = nowMs + 1000L;
            }
        }
    }

    // 열기
    private static void open(long nowMs) {
        isOpen = true;
        oman8EndTime = nowMs + (1000L * Lineage.oman8_play_time); // 설정된 '초'를 ms로
        lastRemainSecSent = -1;
        nextTimerBroadcastAt = 0L;

        sendMessage();            // 오픈 안내
        sendTimerUI(true, nowMs); // 즉시 1회(남은 시간 전송)
        nextTimerBroadcastAt = nowMs + 1000L;
    }

    // 닫기
    private static void close() {
        isOpen = false;
        oman8EndTime = 0L;
        lastRemainSecSent = -1;
        nextTimerBroadcastAt = 0L;
        sendMessage(); // 종료 안내
    }

    // 공지/토스트
    public static void sendMessage() {
        String chatMsg;
        String toastTitle, toastDesc;

        if (isOpen) {
            chatMsg   = "\\fY      ***** 오만8층으로 가는길이 열렸습니다. *****";
            toastTitle = "★오만 8층 입장 가능 ★";
            toastDesc  = "던전이 열렸습니다. 지금 바로 입장하세요!";
        } else {
            chatMsg   = "\\fY      ***** 오만8층으로 가는길이 닫혔습니다. *****";
            toastTitle = "■ 오만 8층 닫힘 안내";
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

    /**
     * 타이머 UI 전송
     * @param show  true=남은 시간 표시, false=숨김(0초)
     * @param nowMs 현재 서버 ms
     */
    private static void sendTimerUI(boolean show, long nowMs) {
        int remainSec = 0;
        if (show) {
            long diff = oman8EndTime - nowMs;
            if (diff < 0) diff = 0;
            remainSec = (int)(diff / 1000L);   // ★ 반드시 '초' 단위
        }
        for (PcInstance pc : World.getPcList()) {
            SC_TIMER_UI_NOTI.newInstance()
                .setTimerType(TimerType.Normal) // 필요 시 Boss/Event 등
                .setRemainTime(remainSec)       // 0이면 클라가 숨김
                .send(pc);
        }
    }

    /**
     * 특정 유저에게 현재 타이머 상태 푸시 (입장/텔레포트 시 호출 추천)
     */
    public static void pushTimerTo(PcInstance pc) {
        int remainSec = 0;
        
        /*상시개방*/
        if (ALWAYS_OPEN) {
            // 상시 오픈: 타이머 숨김1
            SC_TIMER_UI_NOTI.newInstance()
                .setTimerType(TimerType.Normal)
                .setRemainTime(0)
                .send(pc);
            return;
        }
        
        if (isOpen) {
            long diff = oman8EndTime - System.currentTimeMillis();
            if (diff < 0) diff = 0;
            remainSec = (int)(diff / 1000L);
        }
        SC_TIMER_UI_NOTI.newInstance()
            .setTimerType(TimerType.Normal)
            .setRemainTime(remainSec)
            .send(pc);
    }

    public static int getDayOfWeek() {
        Calendar rightNow = Calendar.getInstance();
        return rightNow.get(Calendar.DAY_OF_WEEK);
    }
}
