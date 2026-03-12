package Fx.server.MJTemplate.MJProto;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class GeneralThreadPool {
    private static final int CORE_POOL_SIZE = 256; // 유휴 상태이더라도 pool에서 최소 유지되는 쓰레드 수

    private static final GeneralThreadPool _instance = new GeneralThreadPool();

    public static GeneralThreadPool getInstance() {
        return _instance;
    }

    private GeneralThreadPool() {
        _pool = Executors.newCachedThreadPool();
        _scheduler = Executors.newScheduledThreadPool(CORE_POOL_SIZE, new ScheduleThreadFactory("GeneralScheduler"));
        _pcScheduler = Executors.newScheduledThreadPool(CORE_POOL_SIZE, new ScheduleThreadFactory("PcScheduler"));
    }

    private ExecutorService _pool; // 범용 ExecutorService ThreadPool
    private ScheduledExecutorService _scheduler; // 범용 ScheduledExecutorService ThreadPool
    private ScheduledExecutorService _pcScheduler; // PC의 모니터용 ScheduledExecutorService ThreadPool

    /** 해당 쓰레드 실행(ThreadPool 사용)
     *  @param r Runnable 구현체 **/
    public void execute(Runnable r) {
        _pool.execute(r);
    }

    /** 해당 쓰레드 실행(ThreadPool 사용 안함)
     *  @param t Thread를 상속받은 실행 Class **/
    public void execute(Thread t) {
        t.start();
    }

    /** 해당 범용 쓰레드의 스케쥴 실행(일회용 스케쥴)
     *  @param r Runnable 구현체
     *  @param delay 해당 밀리초 후 실행
     *  @return ScheduledFuture **/
    public ScheduledFuture<?> schedule(Runnable r, long delay) {
        try {
            if (delay <= 0) {
                _pool.execute(r);
                return null;
            }
            return _scheduler.schedule(r, delay, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException e) {
        	e.printStackTrace();
            return null;
        }
    }

    /** 해당 범용 쓰레드의 스케쥴 실행(반복 스케쥴) <br>
     *  종료 명령이 없을시 무한 반복 되므로 사용시 주의 필요
     *  @param r Runnable 구현체
     *  @param initialDelay 해당 밀리초 후 실행
     *  @param period 해당 밀리초 후 재실행(반복 딜레이)
     *  @return ScheduledFuture **/
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long initialDelay, long period) {
        return _scheduler.scheduleAtFixedRate(r, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    /** 해당 PC 쓰레드의 스케쥴 실행(일회용 스케쥴)
     *  @param r Runnable 구현체
     *  @param delay 해당 밀리초 후 실행
     *  @return ScheduledFuture **/
    public ScheduledFuture<?> pcSchedule(L1PcMonitor r, long delay) {
        try {
            if (delay <= 0) {
                _pool.execute(r);
                return null;
            }
            return _pcScheduler.schedule(r, delay, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException e) {
        	e.printStackTrace();
            return null;
        }
    }

    /** 해당 PC 쓰레드의 스케쥴 실행(반복 스케쥴) <br>
     *  종료 명령이 없을시 무한 반복 되므로 사용시 주의 필요
     *  @param r Runnable 구현체
     *  @param initialDelay 해당 밀리초 후 실행
     *  @param period 해당 밀리초 후 재실행(반복 딜레이)
     *  @return ScheduledFuture **/
    public ScheduledFuture<?> pcScheduleAtFixedRate(L1PcMonitor r, long initialDelay, long period) {
        return _pcScheduler.scheduleAtFixedRate(r, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    // ScheduleThreadFactory
    private class ScheduleThreadFactory implements ThreadFactory {
        private final ThreadGroup _group;
        private int _currentNum;

        public ScheduleThreadFactory(String name) {
            _group = new ThreadGroup(name);
        }

        private int nextNum() {
            _currentNum++;
            if (_currentNum >= Integer.MAX_VALUE) {
                _currentNum = 0;
            }
            return _currentNum;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread th = new Thread(_group, r);
            th.setName(_group.getName() + "-" + nextNum());
            return th;
        }
    }
}
