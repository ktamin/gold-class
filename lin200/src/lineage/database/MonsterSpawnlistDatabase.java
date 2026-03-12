package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI.ToastType;
import all_night.Lineage_Balance;
import lineage.bean.database.Monster;
import lineage.bean.database.MonsterSpawnlist;
import lineage.bean.lineage.Map;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.thread.AiThread;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.BossController;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.NpcInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.monster.ArachnevilElder;
import lineage.world.object.monster.Baphomet;
import lineage.world.object.monster.Blob;
import lineage.world.object.monster.BombFlower;
import lineage.world.object.monster.Doppelganger;
import lineage.world.object.monster.Faust_Ghost;
import lineage.world.object.monster.FloatingEye;
import lineage.world.object.monster.Gremlin;
import lineage.world.object.monster.Grimreaper;
import lineage.world.object.monster.Oman_Monster;
import lineage.world.object.monster.Slime;
import lineage.world.object.monster.Spartoi;
import lineage.world.object.monster.StoneGolem;
import lineage.world.object.monster.Unicorn;
import lineage.world.object.monster.Wolf;
import lineage.world.object.monster.event.JackLantern;
import lineage.world.object.monster.quest.AtubaOrc;
import lineage.world.object.monster.quest.BetrayedOrcChief;
import lineage.world.object.monster.quest.BetrayerOfUndead;
import lineage.world.object.monster.quest.Bugbear;
import lineage.world.object.monster.quest.DarkElf;
import lineage.world.object.monster.quest.Darkmar;
import lineage.world.object.monster.quest.DudaMaraOrc;
import lineage.world.object.monster.quest.GandiOrc;
import lineage.world.object.monster.quest.MutantGiantQueenAnt;
import lineage.world.object.monster.quest.NerugaOrc;
import lineage.world.object.monster.quest.OrcZombie;
import lineage.world.object.monster.quest.Ramia;
import lineage.world.object.monster.quest.RovaOrc;
import lineage.world.object.monster.quest.Skeleton;
import lineage.world.object.monster.quest.Zombie;

public final class MonsterSpawnlistDatabase {

	static private List<MonsterInstance> pool;
	static private List<Monster> temp;
	
	static public void init(Connection con){
		TimeLine.start("MonsterSpawnlistDatabase..");
		
		// 몬스터 스폰 처리.
		pool = new ArrayList<MonsterInstance>();
		temp = new ArrayList<Monster>();
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM monster_spawnlist");
			rs = st.executeQuery();
			while(rs.next()){
				Monster m = MonsterDatabase.find(rs.getString("monster"));
				
				if(m != null){		
					if (temp.size() < 1) {
						temp.add(m);
					} else {
						boolean result = true;
						
						for (int i = 0; i < temp.size(); i++) {
							if (temp.get(i).getName().equalsIgnoreCase(m.getName())) {
								result = false;
								break;
							}
						}
						
						if (result)
							temp.add(m);
					}
					
					MonsterSpawnlist ms = new MonsterSpawnlist();
					ms.setUid(rs.getInt("uid"));
					ms.setName(rs.getString("name"));
					ms.setMonster(m);
					ms.setRandom(rs.getString("random").equalsIgnoreCase("true"));
					ms.setCount(rs.getInt("count"));
					ms.setLocSize(rs.getInt("loc_size"));
					ms.setX(rs.getInt("spawn_x"));
					ms.setY(rs.getInt("spawn_y"));
					StringTokenizer stt = new StringTokenizer(rs.getString("spawn_map"), "|");
					while(stt.hasMoreTokens()){
						String map = stt.nextToken();
						if(map.length() > 0)
							ms.getMap().add(Integer.valueOf(map));
					}
					
					ms.setReSpawn(rs.getInt("re_spawn_min") * 1000);
					
					if (rs.getInt("re_spawn_max") < rs.getInt("re_spawn_min"))
						ms.setReSpawnMax(rs.getInt("re_spawn_min") * 1000);
					else
						ms.setReSpawnMax(rs.getInt("re_spawn_max") * 1000);
				
					toSpawnMonster(ms, null);
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", MonsterSpawnlistDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		
		TimeLine.end();
	}
	
	/**
	 * 중복코드 방지용
	 * 	: gui 기능에서 사용중 lineage.gui.dialog.MonsterSpawn.step4()
	 */
	static public void toSpawnMonster(MonsterSpawnlist ms, Map map) {
		// 버그 방지
		if (ms == null || ms.getMap().size() <= 0)
			return;
		// 맵 확인.
		if (map == null) {
			if (ms.getMap().size() > 1)
				map = World.get_map(ms.getMap().get(Util.random(0, ms.getMap().size() - 1)));
			else
				map = World.get_map(ms.getMap().get(0));
		}
		if (map == null)
			return;
		// 스폰처리.
		for (int i = 0; i < ms.getCount(); ++i) {
			MonsterInstance mi = newInstance(ms.getMonster());
			if (mi == null)
				return;

			if (i == 0)
				mi.setMonsterSpawnlist(ms);
			mi.setDatabaseKey(Integer.valueOf(ms.getUid()));

			toSpawnMonster(mi, map, ms.isRandom(), ms.getX(), ms.getY(), map.mapid, ms.getLocSize(), ms.getReSpawn(), ms.getReSpawnMax(), true, true);
		}
	}
	
	/**
	 * 파우스트의 악령 및 기타 몬스터 이벤트성 소환시 사용
	 * 2017-10-07
	 * by all-night
	 */
	public static boolean toSpawnMonster(
	        Monster monster,
	        int x, int y,
	        int map,
	        int heading,
	        boolean boss,
	        MonsterInstance mon) {

	    // 버그 방지
	    if (monster == null || map < 0)
	        return false;

	    // 스폰 처리
	    MonsterInstance mi = newInstance(monster);
	    if (mi == null)
	        return false;

	    // 기존 몬스터 제거 후 스폰 대기
	    mon.toAiThreadRespawn();

	    // 헤이스트/브레이브 적용
	    if (mi.getMonster().isHaste() || mi.getMonster().isBravery()) {
	        if (mi.getMonster().isHaste())
	            mi.setSpeed(1);
	        if (mi.getMonster().isBravery())
	            mi.setBrave(true);
	    }

	    // 위치 및 방향 세팅
	    mi.setHeading(heading);
	    mi.setReSpawnTime(0);
	    mi.setHomeX(x);
	    mi.setHomeY(y);
	    mi.setHomeMap(map);
	    mi.toTeleport(x, y, map, false);

	    // 드롭 정보 로드는 필요 시 주석 해제
	    // mi.readDrop(map);

	    // AI 스케줄에 추가
	    AiThread.append(mi);

	    if (boss) {
	        mi.setBoss(true);
	        BossController.appendBossList(mi);

	        String mapName     = Util.getMapName(mi);
	        String monsterName = mi.getMonster().getName();

	        // 파우스트 고스트 출현 메시지
	        if (mon instanceof Faust_Ghost && Lineage_Balance.faust_spawn_msg) {
	            for (PcInstance onlinePc : World.getPcList()) {
	                SC_TOAST_NOTI.newInstance()
	                    .setMessage( String.format("\\g1* 보스 출현 [ %s ] *", monsterName) )
	                    .setMessage2(String.format("\\fH%s에서 [%s]이(가) 출현하였습니다.", mapName, monsterName))
	                    .setToastType(ToastType.HeavyText)
	                    .send(onlinePc);
	            }
	        }
	        
	        // 오만 몬스터 출현 메시지
	        else if (mon instanceof Oman_Monster && Lineage_Balance.grimreaper_spawn_msg) {
	            for (PcInstance onlinePc : World.getPcList()) {
	                SC_TOAST_NOTI.newInstance()
	                    .setMessage( String.format("\\g1* 보스 출현 [ %s ] *", monsterName) )
	                    .setMessage2(String.format("\\fH%s에서 [%s]이(가) 출현하였습니다.", mapName, monsterName))
	                    .setToastType(ToastType.HeavyText)
	                    .send(onlinePc);
	            }
	        }
	        // 림리퍼 출현 메시지
	        else if (mon instanceof Grimreaper && Lineage_Balance.oman_spawn_msg) {
	            for (PcInstance onlinePc : World.getPcList()) {
	                SC_TOAST_NOTI.newInstance()
	                    .setMessage( String.format("\\g1* 보스 출현 [ %s ] *", monsterName) )
	                    .setMessage2(String.format("\\fH%s에서 [%s]이(가) 출현하였습니다.", mapName, monsterName))
	                    .setToastType(ToastType.HeavyText)
	                    .send(onlinePc);
	            }
	        }
	    }

	    return true;
	}
/*	
	static public boolean toSpawnMonster(Monster monster, int x, int y, int map, int heading, boolean boss, MonsterInstance mon) {
		// 버그 방지
		if (monster == null || map < 0)
			return false;

		// 스폰처리.
		MonsterInstance mi = newInstance(monster);

		if (mi == null)
			return false;
		

		// 기존 몬스터 제거후 스폰대기
		mon.toAiThreadRespawn();
		
		if (mi.getMonster().isHaste() || mi.getMonster().isBravery()) {
			if (mi.getMonster().isHaste())
				mi.setSpeed(1);
			if (mi.getMonster().isBravery())
				mi.setBrave(true);
		}
		
		mi.setHeading(heading);
		mi.setReSpawnTime(0);
		mi.setHomeX(x);
		mi.setHomeY(y);
		mi.setHomeMap(map);
		mi.toTeleport(x, y, map, false);
//		mi.readDrop(map);
		AiThread.append(mi);
	
		if (boss) {
			mi.setBoss(true);
			BossController.appendBossList(mi);
			
			if (mon instanceof Faust_Ghost && Lineage_Balance.faust_spawn_msg) {
				World.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 780, "\\fY" + Util.getMapName(mi) + " " + mi.getMonster().getName()));
			} else if (mon instanceof Oman_Monster && Lineage_Balance.grimreaper_spawn_msg) {
				World.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 780, "\\fY" + Util.getMapName(mi) + " " + mi.getMonster().getName()));
			} else if (mon instanceof Grimreaper && Lineage_Balance.oman_spawn_msg) {
				World.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 780, "\\fY" + Util.getMapName(mi) + " " + mi.getMonster().getName()));
			}		
		}
		
		return true;
	}
*/	
	static public boolean toSpawnMonster2(Monster monster, int x, int y, int map, int heading, boolean boss, MonsterInstance mon) {
		// 버그 방지
		if (monster == null || map < 0)
			return false;

		// 스폰처리.
		MonsterInstance mi = newInstance(monster);

		if (mi == null)
			return false;
		

;
		
		if (mi.getMonster().isHaste() || mi.getMonster().isBravery()) {
			if (mi.getMonster().isHaste())
				mi.setSpeed(1);
			if (mi.getMonster().isBravery())
				mi.setBrave(true);
		}
		
		mi.setHeading(heading);
		mi.setReSpawnTime(0);
		mi.setHomeX(x);
		mi.setHomeY(y);
		mi.setHomeMap(map);
		mi.toTeleport(x, y, map, false);
//		mi.readDrop(map);
		AiThread.append(mi);
		
		if (boss) {
			mi.setBoss(true);
			BossController.appendBossList(mi);
			
			if (mon instanceof Faust_Ghost && Lineage_Balance.faust_spawn_msg) {
				World.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 780, "\\fY" + Util.getMapName(mi) + " " + mi.getMonster().getName()));
			} else if (mon instanceof Oman_Monster && Lineage_Balance.grimreaper_spawn_msg) {
				World.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 780, "\\fY" + Util.getMapName(mi) + " " + mi.getMonster().getName()));
			} else if (mon instanceof Grimreaper && Lineage_Balance.oman_spawn_msg) {
				World.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 780, "\\fY" + Util.getMapName(mi) + " " + mi.getMonster().getName()));
			}		
		}
		
		return true;
	}
	/**
	 * 중복 코드를 막기위해 함수로 따로 뺌.
	 */
	static public void toSpawnMonster(MonsterInstance mi, Map m, boolean random, int x, int y, int map, int loc, int respawn, int respawnMax, boolean drop, boolean ai){
		if(mi == null)
			return;
		
		int roop_cnt = 0;
		int lx = x;
		int ly = y;
		if(random){
			// 랜덤 좌표 스폰
			do{
				if(x>0){
					lx = Util.random(x-loc, x+loc);
					ly = Util.random(y-loc, y+loc);
				}else{
					lx = Util.random(m.locX1, m.locX2);
					ly = Util.random(m.locY1, m.locY2);
				}
				if(roop_cnt++>500){
					lx = x;
					ly = y;
					break;
				}
			}while(
					!World.isThroughObject(lx, ly+1, map, 0) || 
					!World.isThroughObject(lx, ly-1, map, 4) || 
					!World.isThroughObject(lx-1, ly, map, 2) || 
					!World.isThroughObject(lx+1, ly, map, 6) ||
					!World.isThroughObject(lx-1, ly+1, map, 1) ||
					!World.isThroughObject(lx+1, ly-1, map, 5) || 
					!World.isThroughObject(lx+1, ly+1, map, 7) || 
					!World.isThroughObject(lx-1, ly-1, map, 3) ||
					World.isNotMovingTile(lx, ly, map)
				);
		}else{
			// 좌표 스폰
			lx = x;
			ly = y;
			if(loc>1 && x>0){
				while(
						!World.isThroughObject(lx, ly+1, map, 0) || 
						!World.isThroughObject(lx, ly-1, map, 4) || 
						!World.isThroughObject(lx-1, ly, map, 2) || 
						!World.isThroughObject(lx+1, ly, map, 6) ||
						!World.isThroughObject(lx-1, ly+1, map, 1) || 
						!World.isThroughObject(lx+1, ly-1, map, 5) || 
						!World.isThroughObject(lx+1, ly+1, map, 7) || 
						!World.isThroughObject(lx-1, ly-1, map, 3) ||
						World.isNotMovingTile(lx, ly, map)
					){
					lx = Util.random(x-loc, x+loc);
					ly = Util.random(y-loc, y+loc);
					if(roop_cnt++>500){
						lx = x;
						ly = y;
						break;
					}
				}
			}
		}
		
		if (mi.getMonster().isHaste() || mi.getMonster().isBravery()) {
			if (mi.getMonster().isHaste())
				mi.setSpeed(1);
			if (mi.getMonster().isBravery())
				mi.setBrave(true);
		}
		
		mi.setReSpawnTime(Util.random(respawn, respawnMax));
		mi.setHomeX(lx);
		mi.setHomeY(ly);
		mi.setHomeLoc(loc);
		mi.setHomeMap(map);
		mi.toTeleport(lx, ly, map, false);

		if (mi.getInventory() != null) {
			for (ItemInstance ii : mi.getInventory().getList()) {
				ItemDatabase.setPool(ii);
			}
			mi.getInventory().clearList();
		}
		
		if(ai)
			AiThread.append(mi);
		
		if (respawn > 0)
			World.appendMonster(mi);
	}
	
	static public MonsterInstance newInstance(Monster m){
		MonsterInstance mi = null;
		
		if(m != null){
			
			switch(m.getNameIdNumber()){
				case 6:		// 괴물 눈
					mi = FloatingEye.clone(getPool(FloatingEye.class), m);
					break;
				case 7:		// 해골
					mi = Skeleton.clone(getPool(Skeleton.class), m);
					break;
				case 8:		// 슬라임
					mi = Slime.clone(getPool(Slime.class), m);
					break;
				case 56:	// 돌골렘
					mi = StoneGolem.clone(getPool(StoneGolem.class), m);
					break;
				case 57:	// 좀비
					mi = Zombie.clone(getPool(Zombie.class), m);
					break;
				case 268:	// 늑대
				case 904:	// 세인트버나드
				case 905:	// 도베르만
				case 4072:  // 아기진돗개
				case 4073:  // 진돗개
				case 4079:  // 아기 캥거루
				case 4078:  // 공포의판다곰
				case 4080:  // 불꽃의 캥거루
				case 4077:  // 아기 판다곰
				case 906:	// 콜리
				case 907:	// 세퍼드
				case 908:	// 비글
				case 1397:	// 여우
				case 1495:	// 곰
				case 1788:	// 허스키
				case 2563:	// 열혈토끼
				case 2701:	// 고양이
				case 3041:	// 호랑이
				case 3508:	// 라쿤
					mi = Wolf.clone(getPool(Wolf.class), m);
					break;
				case 306:	// 바포메트
					mi = Baphomet.clone(getPool(Baphomet.class), m);
					break;
				case 318:	// 스파토이
					mi = Spartoi.clone(getPool(Spartoi.class), m);
					break;
				case 319:	// 웅골리언트
					mi = ArachnevilElder.clone(getPool(ArachnevilElder.class), m);
					break;
				case 325:	// 버그베어
					mi = Bugbear.clone(getPool(Bugbear.class), m);
					break;
				case 494:	// 아투바 오크
					mi = AtubaOrc.clone(getPool(AtubaOrc.class), m);
					break;
				case 495:	// 네루가 오크
					mi = NerugaOrc.clone(getPool(NerugaOrc.class), m);
					break;
				case 496:	// 간디 오크
					mi = GandiOrc.clone(getPool(GandiOrc.class), m);
					break;
				case 497:	// 로바 오크
					mi = RovaOrc.clone(getPool(RovaOrc.class), m);
					break;
				case 498:	// 두다-마라 오크
					mi = DudaMaraOrc.clone(getPool(DudaMaraOrc.class), m);
					break;
				case 758:	// 브롭
					mi = Blob.clone(getPool(Blob.class), m);
					break;
				case 1041:	// 오크좀비
					mi = OrcZombie.clone(getPool(OrcZombie.class), m);
					break;
				case 1042:	// 다크엘프
					mi = DarkElf.clone(getPool(DarkElf.class), m);
					break;
				case 1046:	// 그렘린
					mi = Gremlin.clone(getPool(Gremlin.class), m);
					break;
				case 1428:	// 라미아
					mi = Ramia.clone(getPool(Ramia.class), m);
					break;
				case 1554:	// 도펠갱어
					mi = Doppelganger.clone(getPool(Doppelganger.class), m);
					break;
				case 1571:	// 폭탄꽃
					mi = BombFlower.clone(getPool(BombFlower.class), m);
					break;
				case 2017:	// 다크마르
					mi = Darkmar.clone(getPool(Darkmar.class), m);
					break;
				case 2020:	// 언데드의 배신자
					mi = BetrayerOfUndead.clone(getPool(BetrayerOfUndead.class), m);
					break;
				case 2063:	// 잭-O-랜턴
				case 2064:	// 잭-0-랜턴
					mi = JackLantern.clone(getPool(JackLantern.class), m);
					break;
				case 2073:	// 변종 거대 여왕 개미
					mi = MutantGiantQueenAnt.clone(getPool(MutantGiantQueenAnt.class), m);
					break;
				case 2219:	// 배신당한 오크대장
					mi = BetrayedOrcChief.clone(getPool(BetrayedOrcChief.class), m);
					break;
				case 2488:
					mi = Unicorn.clone(getPool(Unicorn.class), m);
					break;
				case 7444: // 파우스트의 악령
					mi = Faust_Ghost.clone(getPool(Faust_Ghost.class), m);
					break;
				case 19883: // 오만의 탑 몬스터
				case 19884:
				case 19885:
				case 19886:
				case 19887:
				case 19889:
				case 19890:
				case 19891:
				case 19892:
				case 19893:
				case 19895:
				case 19896:
				case 19897:
				case 19898:
				case 19899:
				case 19900:
				case 19902:
				case 19903:
				case 19904:
				case 19905:
				case 19906:
				case 19908:
				case 19909:
				case 19910:
				case 19911:
				case 19912:
				case 19913:
				case 19915:
				case 19916:
				case 19917:
				case 19918:
				case 19920:
				case 19921:
				case 19922:
				case 19923:
				case 19925:
				case 19926:
				case 19927:
				case 19928:
				case 19930:
				case 19931:
				case 19932:
				case 19933:
				case 19935:
				case 19936:
				case 19937:
				case 19938:
				case 19940:
				case 19941:
				case 19942:
				case 19943:
				case 19944:
				case 19945:
				case 19946:
				case 19947:
				case 19948:
					mi = Oman_Monster.clone(getPool(Oman_Monster.class), m);
					break;
				case 12410: // 감시자 리퍼
					mi = Grimreaper.clone(getPool(Grimreaper.class), m);
					break;

				default:
					
					
					mi = MonsterInstance.clone(getPool(MonsterInstance.class), m);
					break;
			}
			mi.setObjectId(ServerDatabase.nextEtcObjId());
			mi.setGfx(m.getGfx());
			mi.setGfxMode(m.getGfxMode());
			mi.setClassGfx(m.getGfx());
			mi.setClassGfxMode(m.getGfxMode());
			mi.setName(m.getNameId());
			mi.setLevel(m.getLevel());
			mi.setExp(m.getExp());
			mi.setMaxHp(m.getHp());
			mi.setMaxMp(m.getMp());
			mi.setNowHp(m.getHp());
			mi.setNowMp(m.getMp());
			mi.setLawful(m.getLawful());
			mi.setStr(m.getStr());
			mi.setDex(m.getDex());
			mi.setCon(m.getCon());
			mi.setInt(m.getInt());
			mi.setWis(m.getWis());
			mi.setCha(m.getCha());
			mi.setEarthress(m.getResistanceEarth());
			mi.setFireress(m.getResistanceFire());
			mi.setWindress(m.getResistanceWind());
			mi.setWaterress(m.getResistanceWater());
			mi.setAiStatus(Lineage.AI_STATUS_WALK);
		}
		
		return mi;
	}
	
	static private MonsterInstance getPool(Class<?> c){
		synchronized (pool) {
			MonsterInstance mon = null;
			for(MonsterInstance mi : pool){
				if(mi.getClass().equals(c)){
					mon = mi;
					break;
				}
			}
			if(mon != null)
				pool.remove(mon);
			return mon;
		}
	}
	
	static public void setPool(MonsterInstance mi){
		mi.close();
		synchronized (pool) {
			if(!pool.contains(mi))
				pool.add(mi);
		}
		
//		lineage.share.System.println(MonsterSpawnlistDatabase.class.toString()+" : pool.add("+pool.size()+")");
	}
	
	static public int getPoolSize(){
		return pool.size();
	}
	
	static public void insert(Connection con, String name, String monster, boolean random, int count, int loc_size, int x, int y, int map, int re_spawn) {
		PreparedStatement st = null;
		int uid = getUid(con);
		
		try {
			st = con.prepareStatement("INSERT INTO monster_spawnlist SET uid=?, name=?, monster=?, random=?, count=?, loc_size=?, spawn_x=?, spawn_y=?, spawn_map=?, re_spawn_min=?, re_spawn_max=?");
			st.setInt(1, uid);
			st.setString(2, name);
			st.setString(3, monster);
			st.setString(4, String.valueOf(random));
			st.setInt(5, count);
			st.setInt(6, loc_size);
			st.setInt(7, x);
			st.setInt(8, y);
			st.setInt(9, map);
			st.setInt(10, re_spawn);
			st.setInt(11, re_spawn);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : insert()\r\n", MonsterSpawnlistDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st);
		}
	}
	
	static public int getUid(Connection con) {
		PreparedStatement st = null;
		ResultSet rs = null;
		int uid = 0;

		try {
			st = con.prepareStatement("SELECT * FROM monster_spawnlist");
			rs = st.executeQuery();
			while (rs.next()) {
				int temp = rs.getInt("uid");

				if (uid < temp)
					uid = temp;
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : getUid(Connection con)\r\n", MonsterSpawnlistDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}

		return uid + 1;
	}
	
	public static void reload(int mapId) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		for (MonsterInstance mon : World.getMonsterList()) {
			if (mon.getMap() == mapId && !mon.isBoss()) {
				World.removeMonster(mon);
				mon.toAiThreadDelete();
				mon.close();
			}
		}

		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM monster_spawnlist WHERE spawn_map=?");
			st.setInt(1, mapId);
			rs = st.executeQuery();
			
			while (rs.next()) {
				Monster m = MonsterDatabase.find(rs.getString("monster"));
				
				if (m != null) {
					MonsterSpawnlist ms = new MonsterSpawnlist();
					ms.setUid(rs.getInt("uid"));
					ms.setName(rs.getString("name"));
					ms.setMonster(m);
					ms.setRandom(rs.getString("random").equalsIgnoreCase("true"));
					ms.setCount(rs.getInt("count"));
					ms.setLocSize(rs.getInt("loc_size"));
					ms.setX(rs.getInt("spawn_x"));
					ms.setY(rs.getInt("spawn_y"));
					StringTokenizer stt = new StringTokenizer(rs.getString("spawn_map"), "|");
					while(stt.hasMoreTokens()){
						String map = stt.nextToken();
						if(map.length() > 0)
							ms.getMap().add(Integer.valueOf(map));
					}
					
					ms.setReSpawn(rs.getInt("re_spawn_min") * 1000);
					
					if (rs.getInt("re_spawn_max") < rs.getInt("re_spawn_min"))
						ms.setReSpawnMax(rs.getInt("re_spawn_min") * 1000);
					else
						ms.setReSpawnMax(rs.getInt("re_spawn_max") * 1000);
					
					toSpawnMonster(ms, null);
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : reload(int map)\r\n", MonsterSpawnlistDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}
	
	public static void reload() {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		for (MonsterInstance mon : World.getMonsterList()) {
			if (!mon.isBoss()) {
				World.removeMonster(mon);
				mon.toAiThreadDelete();
				mon.close();
			}
		}

		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM monster_spawnlist");
			rs = st.executeQuery();
			
			while (rs.next()) {
				Monster m = MonsterDatabase.find(rs.getString("monster"));
				
				if (m != null) {
					MonsterSpawnlist ms = new MonsterSpawnlist();
					ms.setUid(rs.getInt("uid"));
					ms.setName(rs.getString("name"));
					ms.setMonster(m);
					ms.setRandom(rs.getString("random").equalsIgnoreCase("true"));
					ms.setCount(rs.getInt("count"));
					ms.setLocSize(rs.getInt("loc_size"));
					ms.setX(rs.getInt("spawn_x"));
					ms.setY(rs.getInt("spawn_y"));
					StringTokenizer stt = new StringTokenizer(rs.getString("spawn_map"), "|");
					while(stt.hasMoreTokens()){
						String map = stt.nextToken();
						if(map.length() > 0)
							ms.getMap().add(Integer.valueOf(map));
					}
					
					ms.setReSpawn(rs.getInt("re_spawn_min") * 1000);
					
					if (rs.getInt("re_spawn_max") < rs.getInt("re_spawn_min"))
						ms.setReSpawnMax(rs.getInt("re_spawn_min") * 1000);
					else
						ms.setReSpawnMax(rs.getInt("re_spawn_max") * 1000);
					
					toSpawnMonster(ms, null);
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : reload(int map)\r\n", MonsterSpawnlistDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}
}
