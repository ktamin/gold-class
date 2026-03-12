package lineage.thread;

import lineage.share.Common;
import lineage.share.Lineage;
import lineage.share.System;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.PcRobotInstance;

public class AutoAttackThread implements Runnable {

	static private AutoAttackThread thread;
	// 쓰레드동작 여부
	static private boolean running;

	static public void init() {
		TimeLine.start("AutoAttackThread..");

		thread = new AutoAttackThread();
		start();

		TimeLine.end();
	}

	static private void start() {
		running = true;
		Thread t = new Thread(thread);
		t.setName(AutoAttackThread.class.toString());
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
					int idx = 0;
					
					// 자동 칼질
					try {

						
						if (!pc.isAutoAttack || pc instanceof PcRobotInstance || pc.autoAttackTime > time)
							continue;
						
						idx = 1;
						
						if (pc.autoAttackTarget == null) {
							pc.resetAutoAttack();
							continue;
						}
						
						idx = 2;
						
						if (pc.autoAttackTarget.isDead() || pc.autoAttackTarget.isInvis() || pc.autoAttackTarget.isTransparent()) {
							pc.resetAutoAttack();
							continue;
						}

						idx = 3;
						
						if (pc.isLock()) {
							continue;
						}
						
						idx = 4;

						if (!Util.isAreaAttack(pc, pc.autoAttackTarget) || !Util.isAreaAttack(pc, pc.autoAttackTarget)) {
							continue;
						}
						
						idx = 5;

						if (pc.getMap() != pc.autoAttackTarget.getMap()) {
							pc.resetAutoAttack();
							continue;
						}
						
						idx = 6;

						boolean bow = false;
						int range = getRange(pc);

						ItemInstance weapon = pc.getInventory().getSlot(Lineage.SLOT_WEAPON);
						if (weapon != null && weapon.getItem() != null) {				
							if (weapon.getItem().getType2().equalsIgnoreCase("bow")) {
								bow = true;
								range = 11;
							} else if (weapon.getItem().getType2().equalsIgnoreCase("spear")) {
								range = 2;
							}
						}

						idx = 7;
						
						if (!bow && (pc.targetX != pc.autoAttackTarget.getX() || pc.targetY != pc.autoAttackTarget.getY())) {
							pc.resetAutoAttack();
							continue;
						}
						
						idx = 8;

						if (!Util.isDistance(pc, pc.autoAttackTarget, range)) {
							continue;
						}
						
						idx = 9;

						if (pc.autoAttackTime < time) {
							if (bow)
								pc.toAttack(pc.autoAttackTarget, pc.autoAttackTarget.getX(), pc.autoAttackTarget.getY(), true, 0, 0, false);
							else
								pc.toAttack(pc.autoAttackTarget, pc.autoAttackTarget.getX(), pc.autoAttackTarget.getY(), false, 0, 0, false);
						}
						
						idx = 10;
					} catch (Exception e) {
                                                e.printStackTrace();
						//lineage.share.System.printf("자동칼질 시스템 에러\r\n : %s\r\n", e.toString());
						//lineage.share.System.printf("[idx: %d] 캐릭터: %s\n", idx, pc.getName());
					}
				}
			Thread.sleep(Common.THREAD_SLEEP_30);
			} catch (Exception e) {
				lineage.share.System.printf("lineage.thread.AutoAttackThread.run()\r\n : %s\r\n", e.toString());
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
	
	/**
	 * 특정 몹은 사거리 증가
	 * 2021-01-23
	 * by connector12@nate.com
	 */
	static public int getRange(PcInstance pc) {
		int range = 1;
		
		if (pc != null && pc.autoAttackTarget != null && pc.autoAttackTarget instanceof MonsterInstance) {
			MonsterInstance mi = (MonsterInstance) pc.autoAttackTarget;
			
			if (mi.getMonster() != null && mi.getMonster().getName() != null) {
				switch (mi.getMonster().getName()) {
				case "안타라스":
				case "발라카스":
				case "린드비오르":
				case "파푸리온":
				case "사신 그림 리퍼":
				case "분노한 발록":	
					
					range = 2;
					break;
				}
			}
		}
		
		return range;
	}
}
