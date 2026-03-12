package lineage.thread;

import lineage.share.Common;
import lineage.share.System;
import lineage.share.TimeLine;
import lineage.world.World;
import lineage.world.object.instance.PcInstance;

public class AutoHuntThread implements Runnable {
	
	static private AutoHuntThread thread;
	// 쓰레드동작 여부
	static private boolean running;
	
	static public void init() {
		TimeLine.start("AutoHuntThread..");

		thread = new AutoHuntThread();
		start();

		TimeLine.end();
	}

	static private void start() {
		running = true;
		Thread t = new Thread(thread);
		t.setName(AutoHuntThread.class.toString());
		t.start();
	}
	
	@Override
	public void run() {
		for (; running;) {
			try {
				if (World.getPcList().size() < 1) {
					Thread.sleep(Common.TIMER_SLEEP);
					continue;
				}
				
				long time = System.currentTimeMillis();
				
				for (PcInstance pc : World.getPcList()) {
					pc.toAutoHunt(time);
				}
				
				Thread.sleep(Common.THREAD_SLEEP);
			} catch (Exception e) {
				lineage.share.System.printf("lineage.thread.AutoHuntThread.run()\r\n : %s\r\n", e.toString());
			}
		}
	}
	
	/**
	 * 쓰레드 종료처리 함수.
	 */
	static public void close() {
		running = false;
		thread = null;
	}
}
