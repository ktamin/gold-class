package lineage.world.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI.ToastType;
import lineage.bean.database.Boss;
import lineage.bean.database.Monster;
import lineage.bean.lineage.Map;
import lineage.database.MonsterBossSpawnlistDatabase;
import lineage.database.MonsterDatabase;
import lineage.database.MonsterSpawnlistDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_BlueMessage;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.thread.AiThread;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public final class BossController {
	// 현재 스폰된 보스 리스트
	static public List<MonsterInstance> list;
	static private Calendar calendar;
	
	static public void init(){
		TimeLine.start("BossController..");
		
		list = new ArrayList<MonsterInstance>();
		calendar = Calendar.getInstance();
		
		TimeLine.end();
	}
	
	@SuppressWarnings("deprecation")
	static public void toTimer(long time){
		for (MonsterInstance boss : getBossList()) {
			if (boss != null && boss.getMonster() != null) {
				if (Lineage.boss_live_time > 0) {
					if (--boss.bossLiveTime < 1) {
						
				
						World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("\\fY%s 소멸되었습니다.",  Util.getStringWord(boss.getMonster().getName(), "이", "가"))));
					
			
						toWorldOut(boss);
						boss.toAiThreadDelete();
						World.removeMonster(boss);
						World.remove(boss);
					}
				}
			}
		}
		
		List<Boss> spawnList = MonsterBossSpawnlistDatabase.getList();
		
		if(spawnList.size() > 0){
			// 버그 방지
			for (MonsterInstance boss : getBossList()) {
				if (boss == null || boss.getX() == 0 || boss.getY() == 0) {
					toWorldOut(boss);
					World.removeMonster(boss);
					World.remove(boss);
				}
			}
					
			// 현재 시간.
			calendar.setTimeInMillis(time);
			Date date = calendar.getTime();
			int day = date.getDay();
			int hour = date.getHours();
			int min = date.getMinutes();
			//
			PluginController.init(BossController.class, "toTimer", time, spawnList, hour, min);
			// 스폰할 보스 루프.
			for(Boss b : spawnList){
				// 버그 방지
				if(b.getSpawn().size()<=0)
					continue;
				
				if(b.isSpawnTime(day, hour, min) && !isSpawn(b)){
					// 객체 생성.
					MonsterInstance mi = MonsterSpawnlistDatabase.newInstance(b.getMon());
					
					if(mi != null){
						appendBossList(mi);

						// 정보 갱신.
						mi.setBoss(true);
						
						// 좌표 구분 추출.
						StringTokenizer stt = new StringTokenizer(b.getSpawn().get(Util.random(0, b.getSpawn().size() - 1)), ",");

						if (stt.hasMoreTokens()) {
							mi.setHomeX(Integer.valueOf(stt.nextToken().trim()));
							mi.setHomeY(Integer.valueOf(stt.nextToken().trim()));
							mi.setHomeMap(Integer.valueOf(stt.nextToken().trim()));
						}
						
						// 랜덤 좌표 스폰
						if (mi.getHomeX() == 0 || mi.getHomeY() == 0) {
							Map m = World.get_map(mi.getHomeMap());
							boolean stop = false;
							
							if (m != null) {
								int lx = Util.random(m.locX1, m.locX2);
								int ly = Util.random(m.locY1, m.locY2);
								int count = 0;
								// 랜덤 좌표 스폰
								do {
									if (count++ > 300) {
										stop = true;
										break;
									}
									
									lx = Util.random(m.locX1, m.locX2);
									ly = Util.random(m.locY1, m.locY2);

								} while (!World.isThroughObject(lx, ly + 1, mi.getHomeMap(), 0)
										|| !World.isThroughObject(lx, ly - 1, mi.getHomeMap(), 4)
										|| !World.isThroughObject(lx - 1, ly, mi.getHomeMap(), 2)
										|| !World.isThroughObject(lx + 1, ly, mi.getHomeMap(), 6)
										|| !World.isThroughObject(lx - 1, ly + 1, mi.getHomeMap(), 1)
										|| !World.isThroughObject(lx + 1, ly - 1, mi.getHomeMap(), 5)
										|| !World.isThroughObject(lx + 1, ly + 1, mi.getHomeMap(), 7)
										|| !World.isThroughObject(lx - 1, ly - 1, mi.getHomeMap(), 3));
								
								mi.setHomeX(lx);
								mi.setHomeY(ly);
							} else {
								stop = true;
							}
							
							if (stop) {
								toWorldOut(mi);
								mi = null;
								continue;
							}
						}
						
						for (String group_monster : b.getGroup_monster()) {
							Monster monster = MonsterDatabase.find(group_monster.substring(0, group_monster.indexOf("(")));
							
							if (monster != null) {
								int count = Integer.valueOf(group_monster.substring(group_monster.indexOf("(") + 1, group_monster.indexOf(")")));
								
								for (int i = 0; i < count; i ++) {
									MonsterInstance mon = MonsterSpawnlistDatabase.newInstance(monster);
														
									if (mon.getMonster().isBoss()) {
										mon.setBoss(true);
										
										appendBossList(mon);
									}
									
									if (mon.getMonster().isHaste() || mon.getMonster().isBravery()) {
										if (mon.getMonster().isHaste())
											mon.setSpeed(1);
										if (mon.getMonster().isBravery())
											mon.setBrave(true);	
									}
									
									mon.setGroupMaster(mi);
									mon.setHomeX(mi.getHomeX());
									mon.setHomeY(mi.getHomeY());
									mon.setHomeMap(mi.getHomeMap());
									mon.toTeleport(mon.getHomeX(), mon.getHomeY(), mon.getHomeMap(), false);							
//									mon.readDrop(mon.getHomeMap());
									AiThread.append(mon);
								}
							}
						}
						
						if (mi.getMonster().isHaste() || mi.getMonster().isBravery()) {
							if (mi.getMonster().isHaste())
								mi.setSpeed(1);
							if (mi.getMonster().isBravery())
								mi.setBrave(true);
						}

						mi.toTeleport(mi.getHomeX(), mi.getHomeY(), mi.getHomeMap(), false);
//						mi.readDrop(mi.getHomeMap());
						b.setLastTime(System.currentTimeMillis());
						// 인공지능쓰레드에 등록.
						AiThread.append(mi);
/*
						if (b.is스폰알림여부()) {
							// 메세지 알리기.
							if (Lineage.monster_boss_spawn_message && PluginController.init(BossController.class, "toTimer.Spawn", mi, b) == null)
								World.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 780, "\\fY"+ Util.getMapName(mi) + " " + b.getName()));

							// 화면 중앙에 메세지 알리기.
							if (Lineage.monster_boss_spawn_blue_message && PluginController.init(BossController.class, "toTimer.Spawn", mi, b) == null)
								World.toSender(S_BlueMessage.clone(BasePacketPooling.getPool(S_BlueMessage.class), 556, String.format("\\fY%s %s 나타났습니다.", Util.getMapName(mi), Util.getStringWord(b.getName(), "이", "가"))));
						}
*/
						if (b.is스폰알림여부()) {
						    String mapName = Util.getMapName(mi);
						    String monsterName = b.getName();
						    String monsterNameWithParticle = Util.getStringWord(monsterName, "이", "가");
						    
							// 메세지 알리기.
							if (Lineage.monster_boss_spawn_message && PluginController.init(BossController.class, "toTimer.Spawn", mi, b) == null)
								World.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 780, "\\fY"+ Util.getMapName(mi) + " " + b.getName()));

						    // 중복 호출 방지
						    if (PluginController.init(BossController.class, "toTimer.Spawn", mi, b) == null) {
						        if (Lineage.monster_boss_spawn_message || Lineage.monster_boss_spawn_blue_message) {
						            // 라인1, 라인2 구성
						            String toastLine1 = "\\fY보스 몬스터 등장 알림";
						            String toastLine2 = String.format("%s %s 나타났습니다.", mapName, monsterNameWithParticle);

						            for (PcInstance pc : World.getPcList()) {
						                SC_TOAST_NOTI.newInstance()
						                    .setMessage(toastLine1)
						                    .setMessage2(toastLine2)
						                    .setToastType(ToastType.HeavyText) // 또는 원하는 다른 타입
						                    .send(pc);
						            }
						        }
						    }
						}

					}
				}
			}
		}
	}
	
	/**
	 * 현재 스폰된 상태인지 확인해주는 함수.
	 * @param b
	 * @return
	 */
	static public boolean isSpawn(Boss b){
		synchronized (list) {
			for(MonsterInstance mi : list){
				if(mi.getMonster().getName().equalsIgnoreCase(b.getMon().getName()))
					return true;
			}
			return false;
		}
	}
	
	/**
	 * 보스몬스터 이름으로 스폰된 상태인지 확인하는 함수.
	 * 2017-10-07
	 * by all-night
	 */
	static public boolean isSpawn(String boss, int map){
		synchronized (list) {
			for(MonsterInstance mi : list){
				if(mi.getMonster().getName().equalsIgnoreCase(boss) && mi.getMap() == map)
					return true;
			}
			return false;
		}
	}
	
	/**
	 * boss변수가 true인 객체들은 월드에서 사라질때 이 함수가 호출됨.
	 * @param mi
	 */
	static public void toWorldOut(MonsterInstance mi){
		synchronized (list) {
			list.remove(mi);
		}
	}
	
	static public List<MonsterInstance> getBossList(){
		synchronized (list) {
			return new ArrayList<MonsterInstance>(list);
		}
	}
	
	static public void appendBossList(MonsterInstance mi){
		synchronized (list) {
			if (!list.contains(mi)) {
				mi.bossLiveTime = Lineage.boss_live_time;
				list.add(mi);
			}
		}
	}
	
	static public void appendBossList(MonsterInstance mi, int time){
		synchronized (list) {
			if (!list.contains(mi)) {
				mi.bossLiveTime = Lineage.boss_live_time;
				list.add(mi);
			}
		}
	}
}
