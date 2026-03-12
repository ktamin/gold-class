package lineage.world.object.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArraySet;

import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI;
import all_night.Lineage_Balance;
import lineage.bean.database.Drop;
import lineage.bean.database.Exp;
import lineage.bean.database.Item;
import lineage.bean.database.Monster;
import lineage.bean.database.MonsterSkill;
import lineage.bean.database.MonsterSpawnlist;
import lineage.bean.lineage.Inventory;
import lineage.bean.lineage.Party;
import lineage.database.ExpDatabase;
import lineage.database.ItemDatabase;
import lineage.database.ItemDropMessageDatabase;
import lineage.database.MonsterDatabase;
import lineage.database.MonsterSpawnlistDatabase;
import lineage.database.ServerDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAttack;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectRevival;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.System;
import lineage.util.Util;
import lineage.world.AStar;
import lineage.world.Node;
import lineage.world.World;
import lineage.world.controller.AutoHuntCheckController;
import lineage.world.controller.BossController;
import lineage.world.controller.CharacterController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.DamageController;
import lineage.world.controller.InventoryController;
import lineage.world.controller.KingdomController;
import lineage.world.controller.PartyController;
import lineage.world.controller.월드보스컨트롤러;
import lineage.world.controller.타임이벤트컨트롤러;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.SummonInstance.SUMMON_MODE;
import lineage.world.object.item.Meat;
import lineage.world.object.item.MonsterEyeMeat;
import lineage.world.object.item.potion.CurePoisonPotion;
import lineage.world.object.item.potion.HastePotion;
import lineage.world.object.item.potion.HealingPotion;
import lineage.world.object.magic.monster.ShareMagic;
import lineage.world.object.monster.Faust_Ghost;
import lineage.world.object.instance.PcInstance;

public class MonsterInstance extends Character {

	protected Monster mon; //
	private MonsterSpawnlist monSpawn; //
	private AStar aStar; // 길찾기 변수
	private Node tail; // 길찾기 변수
	private int[] iPath; // 길찾기 변수
	private double add_exp; // hp1당 지급될 경험치 값.
	private double dangerous_exp; // 공격자 우선처리를위한 변수로 경험치의 70% 값을 저장.
	private List<object> attackList; // 전투 목록
	private List<object> astarList; // astar 무시할 객체 목록.
	private List<Exp> expList; // 경험치 지급해야할 목록
	private int ai_walk_stay_count; // 랜덤워킹 중 잠시 휴식을 취하기위한 카운팅 값
	private int reSpawnTime; // 재스폰 하기위한 대기 시간값.
	protected boolean boss; // 보스 몬스터인지 여부. monster_spawnlist_boss 를 거쳐서 스폰도니것만
							// true가 됨.
	private final Set<PcInstance> bossAttackers = new CopyOnWriteArraySet<>();

	// 경험치 2배 물약 아데나 드랍량
	private static final double EXP_POTION_ADEN_RATE = 1.20;

	// 펫/소환 타겟 전환 조건
	private static final int PET_AGGRO_PERCENT = 130; // 펫/소환 누적딜이 PC의 130% 이상이면
														// 전환 허용
	private static final long PET_HOLD_MS = 2000L; // 2초 이상 계속 우세해야 전환(즉시 튐 방지)
	private static final long SWITCH_COOLDOWN_MS = 3000L; // 타겟 변경 쿨타임

	private long petDominatingSince = 0L; // 펫/소환 우세 시작 시각
	private long lastSwitchAt = 0L; // 마지막 전환 시각

	// 시체유지(toAiCorpse) 구간에서 사용중.
	// 재스폰대기(toAiSpawn) 구간에서 사용중.
	private long ai_time_temp_1;
	// 인벤토리
	protected Inventory inv;
	// 그룹몬스터에 사용되는 변수.
	private List<MonsterInstance> group_list; // 현재 관리되고있는 몬스터 목록.
	private MonsterInstance group_master; // 나를 관리하고있는 마스터객체 임시 저장용.
	// 동적 변환 값
	protected int dynamic_attack_area; // 공격 범위 값.
	// 스킬 딜레이를 주기위한 변수
	public long lastSkillTime;
	// 보스 생존 시간.
	public int bossLiveTime;
	// 보스 텔레포트 시간
	public long lastTeleportTime;

	// 스팟 몬스터
	private boolean isSpotMonster;

	static synchronized public MonsterInstance clone(MonsterInstance mi, Monster m) {
		if (mi == null)
			mi = new MonsterInstance();
		mi.setMonster(m);
		mi.setAddExp((double) m.getExp() / (double) m.getHp());
		mi.setDangerousExp(m.getExp() * 0.7);
		return mi;
	}

	public MonsterInstance() {
		aStar = new AStar();
		astarList = new ArrayList<object>();
		attackList = new ArrayList<object>();
		expList = new ArrayList<Exp>();
		group_list = new ArrayList<MonsterInstance>();
		iPath = new int[2];
		// 인벤토리 활성화를 위해.
		InventoryController.toWorldJoin(this);
	}

	@Override
	public void close() {
		super.close();

		classType = Lineage.LINEAGE_CLASS_MONSTER;
		ai_time_temp_1 = reSpawnTime = dynamic_attack_area = 0;
		lastSkillTime = 0L;
		lastTeleportTime = 0L;
		bossLiveTime = 0;
		boss = false;
		monSpawn = null;
		group_master = null;
		if (attackList != null)
			clearAttackList();
		if (astarList != null)
			clearAstarList();
		if (expList != null) {
			synchronized (expList) {
				for (Exp e : expList)
					ExpDatabase.setPool(e);
				expList.clear();
			}
		}
		if (inv != null) {
			for (ItemInstance ii : inv.getList()) {
				ItemDatabase.setPool(ii);
			}
			inv.clearList();
		}
		if (group_list != null)
			group_list.clear();

		isSpotMonster = false;
		//
		CharacterController.toWorldOut(this);
	}

	public boolean isSpotMonster() {
		return isSpotMonster;
	}

	public void setSpotMonster(boolean isSpotMonster) {
		this.isSpotMonster = isSpotMonster;
	}

	public void setMonsterSpawnlist(MonsterSpawnlist monSpawn) {
		this.monSpawn = monSpawn;
	}

	public MonsterSpawnlist getMonsterSpawnlist() {
		return monSpawn;
	}

	public void setAiTimeTemp1(long ai_time_temp_1) {
		this.ai_time_temp_1 = ai_time_temp_1;
	}

	public long getAiTimeTemp1() {
		return ai_time_temp_1;
	}

	public MonsterInstance getGroupMaster() {
		return group_master;
	}

	public void setGroupMaster(MonsterInstance group_master) {
		this.group_master = group_master;
	}

	/**
	 * 그룹에 속한 몬스터중 마스터를 찾아 리턴한다. : 실제 마스터가 죽엇을경우 그룹에 속한 목록에서 살아있는 놈을 마스터로 잡는다. :
	 * 모두 죽거나 하면 null을 리턴.
	 * 
	 * @return
	 */
	public MonsterInstance getGroupMasterDynamic() {
		if (group_master != null) {
			if (group_master.isDead()) {
				for (MonsterInstance mi : group_master.getGroupList()) {
					if (!mi.isDead())
						return mi;
				}
			} else {
				return group_master;
			}
		}
		return null;
	}

	public List<MonsterInstance> getGroupList() {
		return group_list;
	}

	@Override
	public Inventory getInventory() {
		return inv;
	}

	@Override
	public void setInventory(Inventory inv) {
		this.inv = inv;
	}

	public int getReSpawnTime() {
		return reSpawnTime;
	}

	@Override
	public void setReSpawnTime(int reSpawnTime) {
		this.reSpawnTime = reSpawnTime;
	}

	@Override
	public int getHpTime() {
		return Lineage.ai_monster_tic_time;
	}

	@Override
	public int hpTic() {
		return mon.getTicHp();
	}

	@Override
	public int mpTic() {
		return mon.getTicMp();
	}

	public void setAddExp(double add_exp) {
		this.add_exp = add_exp;
	}

	public void setDangerousExp(double dangerous_exp) {
		this.dangerous_exp = dangerous_exp;
	}

	public void setMonster(Monster m) {
		this.mon = m;
		// 시전되는 스킬목록에서 거리범위가 세팅된게 잇을경우 그것으로 거리변경함.
		// 이렇게 해야 몬스터 인공지능 발동시 공격거리가 최상위로 잡혀서 리얼해짐.
		for (MonsterSkill ms : m.getSkillList()) {
			if (ms.getDistance() > 0 && m.getAtkRange() < ms.getDistance() && dynamic_attack_area < ms.getDistance())
				dynamic_attack_area = ms.getDistance();
		}
		// 틱값이 존재한다면 관리를위해 등록.
		if (m.getTicHp() > 0 || m.getTicMp() > 0)
			CharacterController.toWorldJoin(this);
	}

	public Monster getMonster() {
		return mon;
	}

	protected int getAtkRange() {
		return dynamic_attack_area > 0 ? dynamic_attack_area : mon.getAtkRange();
	}

	public boolean isBoss() {
		return boss;
	}

	public void setBoss(boolean boss) {
		this.boss = boss;
	}

	private void appendAttackList(object o) {
		synchronized (attackList) {
			if (!attackList.contains(o))
				attackList.add(o);
		}
	}

	private void removeAttackList(object o) {
		synchronized (attackList) {
			attackList.remove(o);
		}
	}

	public List<object> getAttackList() {
		synchronized (attackList) {
			return new ArrayList<object>(attackList);
		}
	}

	private boolean containsAttackList(object o) {
		synchronized (attackList) {
			return attackList.contains(o);
		}
	}

	protected void clearAttackList() {
		synchronized (attackList) {
			attackList.clear();
		}
	}

	private boolean containsAstarList(object o) {
		synchronized (astarList) {
			return astarList.contains(o);
		}
	}

	private void appendAstarList(object o) {
		synchronized (astarList) {
			if (!astarList.contains(o))
				astarList.add(o);
		}
	}

	private void removeAstarList(object o) {
		synchronized (astarList) {
			astarList.remove(o);
		}
	}

	protected void clearAstarList() {
		synchronized (astarList) {
			astarList.clear();
		}
	}

	protected List<Exp> getExpList() {
		synchronized (expList) {
			return new ArrayList<Exp>(expList);
		}
	}

	private void appendExpList(Exp e) {
		synchronized (expList) {
			if (!expList.contains(e))
				expList.add(e);
		}
	}

	private void removeExpList(Exp e) {
		synchronized (expList) {
			expList.remove(e);
		}
	}

	private Exp getExpList(int index) {
		if (expList.size() > index)
			synchronized (expList) {
				return expList.get(index);
			}
		else
			return null;
	}

	protected void clearExpList() {
		synchronized (expList) {
			expList.clear();
		}
	}

	/**
	 * 전투목록에서 원하는 위치에있는 객체 찾아서 리턴.
	 * 
	 * @param index
	 * @return
	 */
	protected object getAttackList(int index) {
		if (getAttackListSize() > index) {
			synchronized (attackList) {
				return attackList.get(index);
			}
		} else {
			return null;
		}
	}

	protected int getAttackListSize() {
		return attackList.size();
	}

	/*
	 * private PcInstance getOwnerPcFromAttacker(Character cha) {
	 * if (cha instanceof SummonInstance) {
	 * SummonInstance si = (SummonInstance) cha;
	 * try {
	 * if (si.summon != null) {
	 * object master = si.summon.getMaster(); // SummonInstance에서
	 * // 실제 쓰는 구조
	 * if (master instanceof PcInstance)
	 * return (PcInstance) master;
	 * }
	 * } catch (Throwable ignore) {
	 * }
	 * }
	 * return null;
	 * }
	 */
	private PcInstance getOwnerPcFromAttacker(Character cha) {
		if (cha == null)
			return null;

		if (cha instanceof SummonInstance) {
			SummonInstance si = (SummonInstance) cha;
			try {
				if (si.summon != null && si.summon.getMaster() instanceof PcInstance)
					return (PcInstance) si.summon.getMaster();
			} catch (Throwable t) {
			}
		}
		return null;
	}

	/**
	 * 해당 객체와 연결된 경험치 객체 찾기.
	 * 
	 * @param o
	 * @return
	 */
	private Exp findExp(object o) {
		for (Exp e : getExpList()) {
			if (e.getObject() != null && e.getObject().getObjectId() == o.getObjectId())
				return e;
		}
		return null;
	}

	/**
	 * 전투중 타겟 선택 (PC 우선 + 펫/소환은 누적딜 우세 시에만 전환) 70% dangerous_exp 로직은 사용하지 않음.
	 */
	protected object findDangerousObject() {
		object bestPc = null;
		double bestPcExp = -1;

		object bestPetOrSummon = null;
		double bestPetExp = -1;

		// 1) expList에서 PC / 펫/소환 각각 최고 누적값 찾기
		for (Exp e : getExpList()) {
			if (e == null)
				continue;

			object obj = e.getObject();
			if (obj == null || containsAstarList(obj))
				continue;

			// ★ 타입 mismatch 회피: getExp()가 long이든 double이든 double로 받으면 안전
			double exp = e.getExp();

			if (obj instanceof PcInstance) {
				if (exp > bestPcExp) {
					bestPcExp = exp;
					bestPc = obj;
				}
			} else if (obj instanceof SummonInstance || obj instanceof PetInstance) {
				if (exp > bestPetExp) {
					bestPetExp = exp;
					bestPetOrSummon = obj;
				}
			}
		}

		// 2) PC가 없으면(PC가 아예 expList에 없으면) 펫/소환 또는 가장 가까운 공격자로
		if (bestPc == null) {
			petDominatingSince = 0L;

			if (bestPetOrSummon != null)
				return bestPetOrSummon;

			// expList에도 없다면 attackList에서 가까운 대상
			object nearest = null;
			for (object oo : getAttackList()) {
				if (oo == null || containsAstarList(oo))
					continue;
				if (nearest == null)
					nearest = oo;
				else if (Util.getDistance(this, oo) < Util.getDistance(this, nearest))
					nearest = oo;
			}
			return nearest;
		}

		// 3) 기본은 무조건 PC 우선 (가까운 펫이 있어도 PC 반환)
		// 단, 펫/소환 누적딜이 PC보다 충분히 높고, 일정 시간 유지되면 전환
		if (bestPetOrSummon != null && bestPcExp > 0) {
			long now = System.currentTimeMillis();

			boolean petDominates = (bestPetExp * 100.0) >= (bestPcExp * (double) PET_AGGRO_PERCENT);

			if (petDominates) {
				if (petDominatingSince == 0L)
					petDominatingSince = now;

				boolean holdOk = (now - petDominatingSince) >= PET_HOLD_MS;
				boolean cooldownOk = (now - lastSwitchAt) >= SWITCH_COOLDOWN_MS;

				if (holdOk && cooldownOk) {
					lastSwitchAt = now;
					return bestPetOrSummon;
				}
			} else {
				// 우세가 깨지면 초기화
				petDominatingSince = 0L;
			}
		} else {
			petDominatingSince = 0L;
		}

		return bestPc;
	}

	@Override
	public void toRevival(object o) {
		// 사용자가 부활을 시키는거라면 디비상에 부활가능 할때만 처리.
		if (o instanceof PcInstance && !mon.isRevival() && !(this instanceof SummonInstance)
				&& !(this instanceof PetInstance))
			return;

		if (isDead()) {
			super.toReset(false);
			// 다이상태 풀기.
			setDead(false);
			// 체력 채우기.
			setNowHp(level);
			// 패킷 처리.
			toSender(S_ObjectRevival.clone(BasePacketPooling.getPool(S_ObjectRevival.class), o, this), false);
			toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this, 230), true);
			// 상태 변경.
			ai_time_temp_1 = 0;
			setAiStatus(Lineage.AI_STATUS_WALK);
		}
	}

	@Override
	public void setNowHp(int nowHp) {
		super.setNowHp(nowHp);
	}

	/**
	 * 인공지능에 사용된 모든 변수 초기화. 객체를 스폰되기전 상태로 전환하는 함수. : 펫을 길들인후 뒷처리 함수로 사용함. : 펫
	 * 맡길때 제거용으로도 사용함. : 테이밍몬스터후 뒷처리함수로 사용함.
	 * 
	 * @param packet
	 *               : 패킷 처리 할지 여부.
	 */
	public void toAiClean(boolean packet) {
		// 인벤토리 제거.
		for (ItemInstance ii : inv.getList())
			ItemDatabase.setPool(ii);
		inv.clearList();
		// 경험치 지급 제거.
		synchronized (expList) {
			for (Exp e : expList)
				ExpDatabase.setPool(e);
			expList.clear();
		}
		// 전투 관련 변수 초기화.
		clearAttackList();
		// 버프제거
		toReset(true);
		// 객체관리목록 제거.
		clearList(packet);
		World.remove(this);
		// 스폰 준비상태로 변환.
		setAiStatus(Lineage.AI_STATUS_DELETE);
		ai_time_temp_1 = 0;
	}

	@Override
	public void setDead(boolean dead) {
		super.setDead(dead);
		if (dead) {
			ai_time = 0;
			setAiStatus(Lineage.AI_STATUS_DEAD);
		}
	}

	@Override
	public void toDamage(Character cha, int dmg, int type, Object... opt) {
		if (cha == null)
			return;

		if (ai_status != Lineage.AI_STATUS_ATTACK && summon == null) {
			if (getSpeed() == 0 && Util.random(0, 10) == 0) {
				ItemInstance ii = getInventory().findDbNameId(234);
				if (ii != null)
					ii.toClick(this, null);
			}
		}

		// 1) 누적딜/exp는 "실제 공격자(펫/소환 포함)" 기준 유지
		if (dmg > 0)
			appendExp(cha, dmg);

		// 2) 어그로용 공격자는 "주인 PC"로 치환 (펫/소환이면)
		Character aggro = cha;
		PcInstance owner = getOwnerPcFromAttacker(cha);
		if (owner != null)
			aggro = owner;

		// 3) 공격목록/동족/그룹 모두 aggro 기준으로만 등록
		addAttackList(aggro);

		if (mon.getFamily().length() > 0 && group_master == null) {
			for (object inside_o : getInsideList()) {
				if (inside_o instanceof MonsterInstance && !(inside_o instanceof SummonInstance)) {
					MonsterInstance inside_mon = (MonsterInstance) inside_o;
					if (isFamily(inside_mon, mon.getFamily()) && inside_mon.getGroupMaster() == null)
						inside_mon.addAttackList(aggro);
				}
			}
		}

		for (MonsterInstance mi : group_list)
			mi.addAttackList(aggro);

		if (group_master != null && group_master.getObjectId() != getObjectId())
			group_master.toDamage(aggro, 0, type);

		// 손상처리는 "실제 공격자" 기준이 자연스러움(무기 손상 등)
		if (mon.isToughskin() && type == Lineage.ATTACK_TYPE_WEAPON && !cha.isBuffSoulOfFlame()) {
			if (PluginController.init(MonsterInstance.class, "toDamage.손상처리", cha) == null) {
				ItemInstance weapon = cha.getInventory().getSlot(Lineage.SLOT_WEAPON);
				if (weapon != null && weapon.getItem().isCanbedmg() && Util.random(0, 100) < 10) {
					weapon.setDurability(weapon.getDurability() + 1);
					if (Lineage.server_version >= 160)
						cha.toSender(
								S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), weapon));
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 268, weapon.toString()));
				}
			}
		}

		// 길찾기 무시목록 제거도 aggro 기준으로
		removeAstarList(aggro);
	}

	/**
	 * 경험치 지급목록 처리 함수.
	 * 
	 * @param cha
	 * @param dmg
	 */
	protected void appendExp(Character cha, int dmg) {
		if (dmg == 0)
			return;
		Exp e = findExp(cha);
		if (e == null) {
			e = ExpDatabase.getPool();
			e.setObject(cha);
			appendExpList(e);
		}

		// 연산
		double exp = add_exp;
		if (getNowHp() < dmg)
			exp *= getNowHp();
		else
			exp *= dmg;
		// 축적.
		e.setExp(e.getExp() + exp);
		e.setDmg(e.getDmg() + dmg);
	}

	@Override
	public void toGiveItem(object o, ItemInstance item, long count) {
		if (getInventory() != null) {
			if (o.getGm() != 0) {
				super.toGiveItem(o, item, count);
				return;
			}

			if (item != null && item.getItem() != null && item.getItem().getName().equalsIgnoreCase("아데나")) {
				ChattingController.toChatting(o, "아데나는 줄 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			if (!getMonster().getName().equalsIgnoreCase("브롭") && !getInventory().isAppendItem(item, count)) {
				ChattingController.toChatting(o, "아이템을 더 이상 줄 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if (!(item instanceof HastePotion || item instanceof HealingPotion || item instanceof CurePoisonPotion
					|| item instanceof Meat || item instanceof MonsterEyeMeat)) {
				ChattingController.toChatting(o, "이 아이템은 몬스터에게 줄 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			super.toGiveItem(o, item, count);
			if (count == 1) {
				ItemInstance temp = getInventory().find(item.getItem().getName(), item.getBless(),
						item.getItem().isPiles());
				if (temp instanceof HastePotion || temp instanceof HealingPotion || temp instanceof CurePoisonPotion)
					temp.toClick(this, null);
			}
		}
	}

	/**
	 * 공격자 목록에 등록처리 함수.
	 * 
	 * @param o
	 */
	public void addAttackList(object o) {
		if (!isDead() && !o.isDead() && o.getObjectId() != getObjectId() && !group_list.contains(o)) {
			// 공격목록에 추가.
			appendAttackList(o);
		}
	}

	/**
	 * 몬스터별 drop_list 정보를 참고해서 확률 연산후 인벤토리에 갱신.
	 */
	public void readDrop(int spawnMap, PcInstance pc) {
		if (inv == null)
			return;

		// 1) 상태/버프 체크 (pc 없을 수도 있음)
		final boolean hasPc = (pc != null && pc.getInventory() != null);
		final boolean 불멸의가호 = hasPc && pc.getInventory().find(Lineage.immortality_item_name) != null;
		final boolean 행운의인장 = hasPc && pc.getInventory().find("행운의인장(20%)") != null;
		final boolean 대박의인장 = hasPc && pc.getInventory().find("대박의인장(50%)") != null;

		// 2) 일반 아이템 드랍
		for (Drop d : mon.getDropList()) {
			Item item = ItemDatabase.find(d.getItemName());
			if (item == null || d.getChance() <= 0)
				continue;

			double chance = d.getChance(); // 기본 확률

			// 2-1) PC 보정 (있을 때만)
			if (hasPc) {
				// 추가 아이템 드랍률
				if (pc.getAddDropItemRate() > 0)
					chance += (chance * pc.getAddDropItemRate());

				// 불멸의 가호 아이템 드랍률
				if (Lineage.is_immortality && Lineage.immortality_item_percent > 0 && 불멸의가호)
					chance += (chance * Lineage.immortality_item_percent);

				// 인장 보정
				if (대박의인장)
					chance *= 1.5; // +50%
				else if (행운의인장)
					chance *= 1.2; // +20%

				// 타임 이벤트 보정
				if (타임이벤트컨트롤러.isOpen) {
					if (타임이벤트컨트롤러.num == 2)
						chance *= 1.2;
					if (타임이벤트컨트롤러.num == 5)
						chance *= 1.4;
				}

				// 자동사냥 보정(설정값 곱)
				chance *= Lineage.is_auto_hunt_item_drop_percent;
			}

			// 2-2) 서버 전역 드랍 배율 (원본 로직 유지)
			if (d.getChance() < 1) {
				chance *= Lineage.rate_drop;
			}

			// 2-3) 최종 추첨
			if (Math.random() < chance) {
				ItemInstance ii = ItemDatabase.newInstance(item);
				if (ii != null) {
					ii.setBless(d.getItemBress());
					ii.setEnLevel(d.getItemEn());
					ii.setCount(Util.random(d.getCountMin() == 0 ? 1 : d.getCountMin(),
							d.getCountMax() == 0 ? 1 : d.getCountMax()));
					inv.append(ii, true);
				}
			}
		}

		// 3) 아데나 드랍 (중복 방지)
		if (mon.isAdenDrop()) {
			for (ItemInstance i : getInventory().getList()) {
				if ("아데나".equalsIgnoreCase(i.getItem().getName()))
					return; // 이미 들어갔으면 종료(원본 유지)
			}

			// 3-1) 기본 계산
			long count = Math.round(Util.random(level * 3, level * 6) * Lineage.rate_aden);

			// 보스면 보스 범위로 덮어쓰기 (원본 유지)
			if (mon.isBoss()) {
				if (getMonster().getBossClass().equalsIgnoreCase("하급 보스"))
					count = Util.random(Lineage.class_1_boss_aden_min, Lineage.class_1_boss_aden_max);
				else if (getMonster().getBossClass().equalsIgnoreCase("중급 보스"))
					count = Util.random(Lineage.class_2_boss_aden_min, Lineage.class_2_boss_aden_max);
				else if (getMonster().getBossClass().equalsIgnoreCase("상급 보스"))
					count = Util.random(Lineage.class_3_boss_aden_min, Lineage.class_3_boss_aden_max);
				else if (getMonster().getBossClass().equalsIgnoreCase("최상급 보스"))
					count = Util.random(Lineage.class_4_boss_aden_min, Lineage.class_4_boss_aden_max);
			}

			// 3-2) PC 보정 (있을 때만)
			if (hasPc) {
				// 추가 아데나
				if (pc.getAddDropAdenRate() > 0)
					count += Math.round(count * pc.getAddDropAdenRate());

				// 불멸/프리미엄
				if (Lineage.is_immortality && Lineage.immortality_aden_percent > 0 && 불멸의가호)
					count += Math.round(count * Lineage.immortality_aden_percent);

				ItemInstance prem = pc.getInventory().find(Lineage.immortality_premium_item_name);
				if (prem != null && prem.getCount() > 0 && Lineage.immortality_premium_aden_percent > 0)
					count += Math.round(count * Lineage.immortality_premium_aden_percent);

				// 이후 기존처럼 아데나 아이템 생성/append
				// 3-3) 지급

				if (count > 0) {
					ItemInstance aden = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
					if (aden != null) {
						aden.setObjectId(ServerDatabase.nextItemObjId());
						aden.setCount(count);
						inv.append(aden, true);
					}
				}
			}
		}
	}

	// 기존 시그니처 유지용(호환)
	public void readDrop(int spawnMap) {
		readDrop(spawnMap, null);
	}

	/**
	 * 해당객체를 공격해도 되는지 분석하는 함수.
	 * 
	 * @param o
	 * @param walk
	 *             : 랜덤워킹 상태 체크인지 구분용
	 * @return
	 */
	public boolean isAttack(object o, boolean walk) {
		if (o == null)
			return false;
		if (o.isDead())
			return false;
		if (o.isWorldDelete())
			return false;
		// if(o.getGm()>0)
		// return false;
		if (o instanceof PcInstance) {
			if (((PcInstance) o).isFishing() || ((PcInstance) o).isFishingZone())
				return false;
		}
		if (o.isTransparent())
			return false;
		if (!Util.isDistance(this, o, Lineage.SEARCH_MONSTER_TARGET_LOCATION))
			return false;
		if (walk) {
			if (o.getGfx() != o.getClassGfx() && !mon.isAtkPoly())
				return false;
		}
		// 특정 몬스터들은 굳은상태를 확인해서 무시하기.
		switch (mon.getNameIdNumber()) {
			case 962: // 바실리스크
			case 969: // 코카트리스
				if (o.isLockHigh())
					return false;
		}
		if (mon.getName().equalsIgnoreCase("오크 창고 근무자a")) {
			if (o.isLockHigh())
				return false;
		}
		// -- 몬스터 인공지능 설정에서 변경하도록 하는게 좋을듯. 지금은 투망일때 무조건 무시하도록 설정.
		if (!mon.isAtkInvis() && o.isInvis())
			return false;
		// // 투망상태일경우 공격목록에 없으면 무시.
		// if(!mon.isAtkInvis() && o.isInvis())
		// // 동기화 할필요 없음. 동기화 한 상태로 들어옴.
		// return containsAttackList(o);

		if (this instanceof SummonInstance) {
			// 소환및 펫은 거의다..
			return true;
		} else {
			// 몬스터는 pc, sum, pet
			// 랜덤워킹중에 선인식 체크함. 랜덤워킹이라면 서먼몬스터는 제외.
			if (walk)
				return o instanceof PcInstance;
			else
				return o instanceof PcInstance || o instanceof SummonInstance;
		}
	}

	/**
	 * 매개변수 좌표로 A스타를 발동시켜 이동시키기. 객체가 존재하는 지역은 패스하도록 함. 이동할때마다 aStar가 새로 그려지기때문에
	 * 과부하가 심함.
	 */
	public void toMoving(object o, final int x, final int y, final int h, final boolean astar) {
		if (astar) {
			aStar.cleanTail();
			tail = aStar.searchTail(this, x, y, true);
			if (tail != null) {
				while (tail != null) {
					// 현재위치 라면 종료
					if (tail.x == getX() && tail.y == getY())
						break;
					//
					iPath[0] = tail.x;
					iPath[1] = tail.y;
					tail = tail.prev;
				}
				toMoving(iPath[0], iPath[1], Util.calcheading(this.x, this.y, iPath[0], iPath[1]));
			} else {
				if (o != null) {
					// o 객체가 마스터일경우 휴식모드로 전환.
					if (this instanceof SummonInstance) {
						SummonInstance si = (SummonInstance) this;
						if (si.getSummon() != null && si.getSummon().getMasterObjectId() == o.getObjectId()) {
							si.setSummonMode(SUMMON_MODE.Rest);
							return;
						}
					}
					// 그외엔 에이스타 무시목록에 등록.
					appendAstarList(o);
				}
			}
		} else {
			toMoving(x, y, h);
		}
	}

	@Override
	public void toAttack(object o, int x, int y, boolean bow, int gfxMode, int alpha_dmg, boolean isTriple) {
		// 일반적인 공격mode와 다를경우 프레임값 재 설정.
		if (this.gfxMode + Lineage.GFX_MODE_ATTACK != gfxMode)
			ai_time = SpriteFrameDatabase.getGfxFrameTime(this, gfx, gfxMode);

		int effect = bow ? mon.getArrowGfx() != 0 ? mon.getArrowGfx() : 66 : 0;
		int dmg = DamageController.getDamage(this, o, bow, null, null, alpha_dmg);
		DamageController.toDamage(this, o, dmg, Lineage.ATTACK_TYPE_WEAPON);

		// 칼렉
		if (!Lineage.is_sword_lack_check
				|| (Lineage.is_sword_lack_check && lastDamageActionTime < System.currentTimeMillis()))
			toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), this, o, gfxMode, dmg,
					effect, bow, true, 0, 0), false);
	}

	@Override
	public void toAi(long time) {
		switch (ai_status) {
			// 공격목록이 발생하면 공격모드로 변경
			case Lineage.AI_STATUS_WALK:
			case Lineage.AI_STATUS_PICKUP:
				if (getAttackListSize() > 0)
					setAiStatus(Lineage.AI_STATUS_ATTACK);
				break;

			// 전투 처리부분은 항상 타켓들이 공격가능한지 확인할 필요가 있음.
			case Lineage.AI_STATUS_ATTACK:
			case Lineage.AI_STATUS_ESCAPE:
				// 보스일경우 스폰위치에서 너무 벗어나면 스폰위치로 강제 텔레포트.
				if (isBoss()
						&& !Util.isDistance(x, y, map, homeX, homeY, homeMap, Lineage.SEARCH_MONSTER_TARGET_LOCATION)) {
					// setNowHp(getTotalHp());
					// setNowMp(getTotalMp());
					toTeleport(homeX, homeY, homeMap, true);
				}
				if (map == 807 && !Util.isDistance(x, y, map, homeX, homeY, homeMap, 10)) {
					toTeleport(homeX, homeY, homeMap, true);
				}
				//
				for (object o : getAttackList()) {
					if (!isAttack(o, false) || containsAstarList(o)) {
						removeAttackList(o);
						Exp e = findExp(o);
						if (e != null) {
							removeExpList(e);
							ExpDatabase.setPool(e);
						}
					}
				}

				// 전투목록이 없을경우 랜덤워킹으로 변경.
				if (getAttackListSize() < 1) {
					setAiStatus(Lineage.AI_STATUS_WALK);
				}
				break;
		}

		super.toAi(time);
	}

	@Override
	protected void toAiWalk(long time) {
		if (!SpriteFrameDatabase.findGfxMode(getGfx(), getGfxMode() + Lineage.GFX_MODE_WALK))
			return;

		super.toAiWalk(time);

		List<object> insideList = getInsideList();

		if (getMonster() != null && !getMonster().getBossClass().contains("보스")) {
			boolean isWalk = false;
			// 주변에 유저가 있는지 검색.
			for (object o : insideList) {
				if (o instanceof PcInstance) {
					isWalk = true;
					break;
				}
			}
			// 주변에 유저가 없을경우 움직이지 않음.
			if (!isWalk)
				return;
		}

		// 선인식 체크
		if (mon.getAtkType() == 1) {
			for (object o : insideList) {
				if (isAttack(o, true)) {
					// 공격목록에 등록.
					if (!containsAstarList(o)) {
						addAttackList(o);
						clearAstarList();
						setAiStatus(Lineage.AI_STATUS_ATTACK);
						toAiAttack(time);
						return;
					}
					// 그룹 알림.
					if (group_list != null) {
						for (MonsterInstance mi : group_list) {
							if (!mi.containsAstarList(o))
								mi.addAttackList(o);
						}
						if (group_master != null && group_master.getObjectId() != getObjectId()
								&& o instanceof Character)
							group_master.toDamage((Character) o, 0, Lineage.ATTACK_TYPE_DIRECT);
					}
				}
			}
		}

		// 아이템 줍기 체크
		if (getAttackListSize() == 0 && mon.isPickup() && group_master == null
				&& inv.getNowWeight() < (getLevel() * 25) * 0.82) {
			for (object o : insideList) {
				if (o instanceof ItemInstance && !containsAstarList(o)) {
					// 아이템이 발견되면 아이템줍기 모드로 전환.
					setAiStatus(Lineage.AI_STATUS_PICKUP);
					return;
				}
			}
		}
		// 멘트
		// toMent(time);

		// 아직 휴식카운팅값이 남앗을경우 리턴.
		if (ai_walk_stay_count-- > 0)
			return;

		// Astar 발동처리하다가 길이막혀서 이동못하던 객체를 모아놓은 변수를 일정주기마다 클린하기.
		if (Util.random(0, 5) == 0)
			clearAstarList();

		do {
			switch (Util.random(0, 7)) {
				case 0:
				case 1:
					ai_walk_stay_count = Util.random(3, 6);
					break;
				case 2:
				case 3:
					setHeading(getHeading() + 1);
					break;
				case 4:
				case 5:
					setHeading(getHeading() - 1);
					break;
				case 6:
				case 7:
					setHeading(Util.random(0, 7));
					break;
			}
			// 이동 좌표 추출.
			int x = Util.getXY(heading, true) + this.x;
			int y = Util.getXY(heading, false) + this.y;
			// 그룹 마스터로 지정된놈 추출.
			MonsterInstance master = getGroupMasterDynamic();
			// 스폰된 위치에서 너무 벗어낫을경우 스폰쪽으로 유도하기.
			if (master == null || master.getObjectId() == getObjectId() || group_master == null) {
				if (!Util.isDistance(x, y, map, homeX, homeY, homeMap, Lineage.SEARCH_LOCATIONRANGE)) {
					heading = Util.calcheading(this, homeX, homeY);
					x = Util.getXY(heading, true) + this.x;
					y = Util.getXY(heading, false) + this.y;
				}
				// 마스터가 존재하는 그룹몬스터일경우 마스터가 있는 좌표 내에서 왓다갓다 하기.
			} else {
				// 이동 범위는 마스터 목록에 등록된 순번+3으로 범위지정. 등록순번이 뒤에잇을수록 이동반경은 넓어짐.
				if (!Util.isDistance(x, y, map, master.getX(), master.getY(), master.getMap(),
						group_master.getGroupList().indexOf(this) + 3)) {
					heading = Util.calcheading(this, master.getX(), master.getY());
					x = Util.getXY(heading, true) + this.x;
					y = Util.getXY(heading, false) + this.y;
				}
			}
			// 해당 좌표 이동가능한지 체크.
			boolean tail = World.isThroughObject(this.x, this.y, this.map, heading)
					&& (World.isMapdynamic(x, y, map) == false) && !World.isNotAttackTile(x, y, map);
			if (tail)
				// 타일이 이동가능하고 객체가 방해안하면 이동처리.
				toMoving(null, x, y, heading, false);
		} while (false);

	}

	@Override
	public void toAiAttack(long time) {
		super.toAiAttack(time);
		// 몹몰이하는 애들때문에 넣을 수 없음.
		// 스폰된 위치에서 멀리 떠러졋을경우 스폰된 위치로 텔레포트.
		if ((isBoss() || isSpotMonster()) && !Util.isDistance(getX(), getY(), getMap(), getHomeX(), getHomeY(),
				getHomeMap(), Lineage.SEARCH_WORLD_LOCATION)) {
			toTeleport(homeX, homeY, homeMap, true);
			return;
		}

		// 멘트
		// toMent(time);
		// 공격자 확인.
		object o = findDangerousObject();

		// 객체를 찾지못했다면 무시.
		if (o == null)
			return;

		boolean blind = isBuffCurseBlind() && !Util.isDistance(this, o, 2);
		// 객체 거리 확인
		if (Util.isDistance(this, o, getAtkRange()) && Util.isAreaAttack(this, o) && Util.isAreaAttack(o, this)
				&& !blind) {
			// 객체 공격
			// 공격 시전했는지 확인용.
			boolean is_attack = false;
			// 스킬 확인하기.
			if (lastSkillTime < System.currentTimeMillis()) {
				for (MonsterSkill ms : mon.getSkillList()) {
					// 마법시전 시도.
					if (ShareMagic.init(this, o, ms, 0, 0, ms.getDistance() > 2)) {
						// 시전 성공시 루프종료.
						is_attack = true;
						break;
					}
				}
			}

			// 마법 시전이 실패됫을때.
			if (!is_attack) {
				if (Util.isDistance(this, o, mon.getAtkRange())) {
					// 물리공격 범위내로 잇을경우 처리.
					toAttack(o, 0, 0, mon.getAtkRange() > 2, gfxMode + Lineage.GFX_MODE_ATTACK, 0, false);
				} else {
					// 객체에게 접근.
					ai_time = SpriteFrameDatabase.getGfxFrameTime(this, gfx, gfxMode + Lineage.GFX_MODE_WALK);
					toMoving(o, o.getX(), o.getY(), 0, true);
				}
			}
		} else {
			ai_time = SpriteFrameDatabase.getGfxFrameTime(this, gfx, gfxMode + Lineage.GFX_MODE_WALK);
			// 객체 이동
			if (blind) {
				if (Util.random(0, 2) == 0)
					toMoving(null, o.getX() + Util.random(0, 1), o.getY() + Util.random(0, 1), 0, true);
			} else {
				if (!World.isNotAttackTile(o.getX(), o.getY(), o.getMap()))
					toMoving(o, o.getX(), o.getY(), 0, true);

				// 벽넘어서 공격시 타켓으로 텔레포트
				/*
				 * if (lastTeleportTime < time &&
				 * (getMonster().getBossClass().contains("보스")) &&
				 * !Util.isAreaAttack(this, o)) { lastTeleportTime = time +
				 * (1000 * 5); Util.rangeTeleport(this, o, 3); } else {
				 * toMoving(o, o.getX(), o.getY(), 0, true); }
				 */
			}
		}
		// 일정확률로 인벤에 있는 체력회복물약 복용하기.
		if (Util.random(0, 5) == 0) {
			// 현재 hp의 백분율값 추출.
			int now_percent = (int) (((double) getNowHp() / (double) getTotalHp()) * 100);
			// 복용해야한다면
			if (Lineage.ai_auto_healingpotion_percent >= now_percent) {
				// 찾고
				ItemInstance ii = getInventory().find(HealingPotion.class);
				// 복용
				if (ii != null)
					ii.toClick(this, null);
			}
		}
	}

	@Override
	protected void toAiDead(long time) {

		Random random = new Random();
		random.setSeed(System.currentTimeMillis());

		// ---------------------------------------------------------
		// [1] 몬스터 스폰 및 확률 설정
		// ---------------------------------------------------------
		double chance1 = Lineage_Balance.event_b_spawn_probability;
		double chance2 = Lineage_Balance.event_b2_spawn_probability;
		double chance3 = Lineage_Balance.event_a_spawn_probability;
		double chance4 = Lineage_Balance.event_a2_spawn_probability;
		double chance5 = Lineage_Balance.event_s_spawn_probability;
		double chance6 = Lineage_Balance.event_s2_spawn_probability;

		if (타임이벤트컨트롤러.isOpen && 타임이벤트컨트롤러.num == 3) {
			chance1 += 0.05;
			chance2 += 0.05;
			chance3 += 0.05;
			chance4 += 0.05;
			chance5 += 0.05;
			chance6 += 0.05;
		}

		if (!getMonster().getName().contains("파우스트") && !mon.isBoss() && summon == null && getMap() == 53) {
			Monster monster = MonsterDatabase.find("파우스트의 악령(잔인)");
			if (monster != null) {
				MonsterSpawnlistDatabase.toSpawnMonster2(monster, x, y, map, heading, false, this);
			}
		}

		if (Math.random() < chance1 && !getMonster().getName().contains("드래곤") && !mon.isBoss() && summon == null
				&& (getMap() == 430 || getMap() == 400)) {
			if (Math.random() < chance2) {
				Monster monster = MonsterDatabase.find("하급 드래곤");
				this.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this, 4784), true);
				if (monster != null
						&& MonsterSpawnlistDatabase.toSpawnMonster(monster, x, y, map, heading, false, this)) {
					return;
				}
			} else {
				Monster monster = MonsterDatabase.find("그린 드래곤");
				this.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this, 6082), true);
				if (monster != null
						&& MonsterSpawnlistDatabase.toSpawnMonster(monster, x, y, map, heading, false, this)) {
					return;
				}
			}
		}

		if (Math.random() < chance3 && !getMonster().getName().contains("드래곤") && !mon.isBoss() && summon == null
				&& (getMap() == 110 || getMap() == 99)) {
			if (Math.random() < chance4) {
				Monster monster = MonsterDatabase.find("중급 드래곤");
				this.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this, 4784), true);
				if (monster != null
						&& MonsterSpawnlistDatabase.toSpawnMonster(monster, x, y, map, heading, false, this)) {
					return;
				}
			} else {
				Monster monster = MonsterDatabase.find("블루 드래곤");
				this.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this, 6082), true);
				if (monster != null
						&& MonsterSpawnlistDatabase.toSpawnMonster(monster, x, y, map, heading, false, this)) {
					return;
				}
			}
		}

		if (Math.random() < chance5 && !getMonster().getName().contains("드래곤") && !mon.isBoss() && summon == null
				&& (getMap() == 811 || getMap() == 99)) {
			if (Math.random() < chance6) {
				Monster monster = MonsterDatabase.find("상급 드래곤");
				this.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this, 4784), true);
				if (monster != null
						&& MonsterSpawnlistDatabase.toSpawnMonster(monster, x, y, map, heading, false, this)) {
					return;
				}
			} else {
				Monster monster = MonsterDatabase.find("레드 드래곤");
				this.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this, 6082), true);
				if (monster != null
						&& MonsterSpawnlistDatabase.toSpawnMonster(monster, x, y, map, heading, false, this)) {
					return;
				}
			}
		}

		giveBossRewardToAttackers();
		super.toAiDead(time);

		object o = getAttackList(0);
		PcInstance tempPc = null;
		for (object oo : getAttackList()) {
			if (oo instanceof PcInstance) {
				tempPc = (PcInstance) oo;
				break;
			}
		}

		if (!(this instanceof SummonInstance)) {
			if (tempPc != null)
				readDrop(getHomeMap(), tempPc);
			else
				readDrop(getHomeMap());
		}

		// ---------------------------------------------------------
		// [2] 아이템 오토루팅 (데미지 비례 확률 분배)
		// ---------------------------------------------------------
		int tempCount = 0;
		boolean isDrop = true;

		for (Exp e : getExpList()) {
			if (e != null && e.getObject() != null) {
				Character cha = (Character) e.getObject();
				if (cha instanceof PcRobotInstance)
					tempCount++;
			}
		}
		if (tempCount == getExpList().size())
			isDrop = false;

		// 아이템 하나하나마다 누구에게 줄지 주사위를 굴립니다.
		for (ItemInstance ii : inv.getList()) {
			boolean add = false;

			if (isAutoPickup(ii)
					&& (!Lineage.monster_item_drop || (Lineage.monster_item_drop && getAttackListSize() <= 1))) {

				if (Lineage.is_damage_item_drop) {
					// ★ [수정됨] 데미지 비례 확률 분배 로직 시작 ★

					// 1. 전체 데미지 합계 계산
					double totalExpDmg = 0;
					for (Exp e : getExpList()) {
						if (e != null && e.getObject() != null) {
							totalExpDmg += e.getDmg();
						}
					}

					Character target = null;

					// 2. 전체 데미지가 0보다 클 때만 확률 계산 (안전장치)
					if (totalExpDmg > 0) {
						// 0 ~ 전체 데미지 사이의 랜덤 숫자 뽑기
						double randomPoint = Util.random(0, (int) totalExpDmg);
						double currentSum = 0;

						// 3. 룰렛 돌리기 (누적 데미지가 랜덤 숫자를 넘는 순간 그 사람이 당첨)
						for (Exp e : getExpList()) {
							if (e != null && e.getObject() != null && isAutoPickupItem(e.getObject())) {
								currentSum += e.getDmg();
								if (randomPoint <= currentSum) {
									target = (Character) e.getObject();
									break; // 당첨자 선정 완료
								}
							}
						}
					}

					// 4. 당첨자에게 지급 시도
					if (target != null) {
						if (toAutoPickupItem(target, ii)) {
							add = true;
						}
					}

					// 5. 만약 당첨자가 인벤이 꽉 찼거나 해서 못 받았다면? -> 차순위(랜덤) 지급
					if (!add) {
						for (object oo : getAttackList()) {
							Exp e = findExp(oo);
							if (e != null && e.getObject() != null && isAutoPickupItem(e.getObject())) {
								Character cha = (Character) oo;
								if (toAutoPickupItem(cha, ii)) {
									add = true;
									break;
								}
							}
						}
					}
				} // damage_item_drop end

				// 옵션이 꺼져있거나, 위에서 아무도 못 먹은 경우 (완전 랜덤)
				if (!add) {
					for (int i = 0; i < 100; ++i) {
						if (expList.size() == 0)
							break;
						Exp e = getExpList(Util.random(0, expList.size() - 1));

						if (e != null && e.getObject() != null && isAutoPickupItem(e.getObject())) {
							Character cha = (Character) e.getObject();
							if (cha instanceof SummonInstance) {
								SummonInstance summon = (SummonInstance) cha;
								if (summon.getSummon().getMaster() != null)
									cha = (Character) summon.getSummon().getMaster();
							}
							if (toAutoPickupItem(cha, ii)) {
								add = true;
								break;
							}
						}
					}
				}
			}

			// 바닥 드랍
			if (isDrop && !add && ii.getItem().isDrop()) {
				if (ii.getObjectId() == 0)
					ii.setObjectId(ServerDatabase.nextItemObjId());
				int x = Util.random(getX() - 1, getX() + 1);
				int y = Util.random(getY() - 1, getY() + 1);

				if (World.isThroughObject(x, y + 1, map, 0))
					ii.toTeleport(x, y, map, false);
				else
					ii.toTeleport(this.x, this.y, map, false);
				ii.toDrop(this);
			}
		}

		// 잭오랜턴
		if (mon.getName().equalsIgnoreCase("잭 오랜 턴")) {
			for (int i = 0; i < 100; i++) {
				ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("호박 바구니"));
				if (ii != null) {
					ii.setObjectId(ServerDatabase.nextItemObjId());
					ii.setCount(1);
					if (ii.getObjectId() == 0)
						ii.setObjectId(ServerDatabase.nextItemObjId());
					int lx = Util.random(getX() - 20, getX() + 20);
					int ly = Util.random(getY() - 20, getY() + 20);
					if (World.isThroughObject(lx, ly + 1, map, 0))
						ii.toTeleport(lx, ly, map, false);
					else
						ii.toTeleport(this.x, this.y, map, false);
					ii.toTeleport(lx, ly, 4, false);
					ii.toDrop(this);
				}
			}
		}

		inv.clearList();

		// ---------------------------------------------------------
		// [3] 경험치 지급 (펫 포함)
		// ---------------------------------------------------------
		double total_dmg = 0;
		for (Exp e : getExpList())
			total_dmg += e.getDmg();

		for (Exp e : getExpList()) {
			if (e == null)
				continue;
			object oo = e.getObject();
			if (oo == null)
				continue;

			double percent = e.getDmg() / total_dmg;
			e.setExp(getMonster().getExp() * percent);

			if (oo instanceof Character) {
				Character cha = (Character) oo;

				if (Util.isDistance(this, cha, Lineage.SEARCH_LOCATIONRANGE)) {
					if (!PartyController.toExp(cha, this, e.getExp()) && !cha.isDead()) {
						if (cha instanceof PcInstance || cha instanceof PetInstance) {
							cha.toExp(this, e.getExp());

							double lawful = Math.round(((getLevel() * 3) / 2) * Lineage.rate_lawful);
							if (getMonster().getLawful() - Lineage.NEUTRAL > 0) {
								int tempLawful = (getMonster().getLawful() - Lineage.NEUTRAL) * -1;
								lawful = Util.random(tempLawful * 0.8, tempLawful);
							}
							cha.setLawful(cha.getLawful() + (int) lawful);
						}
					}
					if (Lineage.is_auto_hunt_check && oo.getGm() == 0 && oo instanceof PcInstance)
						AutoHuntCheckController.addCount((PcInstance) oo);
				}
			}
			ExpDatabase.setPool(e);
		}

		if (Lineage.event_christmas && Util.random(0, 100) < 10 && getAttackListSize() > 0) {
			if (o != null && o instanceof PcInstance && !(o instanceof RobotInstance)) {
				ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("빨간 양말"));
				if (!toAutoPickupItem((PcInstance) o, ii))
					ItemDatabase.setPool(ii);
			}
		}

		if (getMonster().getName().equalsIgnoreCase("월드보스")) {
			for (PcInstance pc : World.getPcList()) {
				if (pc.getMap() == 1400) {
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("월드보스 보상"));
					ii.setCount(Lineage.world_result);
					ii.setBless(1);
					ii.setDefinite(true);
					월드보스컨트롤러.isOpen = false;
					월드보스컨트롤러.isWait = false;
					pc.toGiveItem(null, ii, ii.getCount());
				}
			}
		}

		if (Lineage.monster_boss_dead_message && isBoss()) {
			String msg = null;
			if (getAttackList().size() > 0) {
				if (o != null && o instanceof PcInstance) {
					if (getAttackList().size() > 1)
						msg = String.format("%s %s", Util.getMapName(this), getMonster().getName());
					else
						msg = String.format("%s %s", Util.getMapName(this), getMonster().getName());
				}
			} else {
				msg = Util.getMapName(this) + " " + getMonster().getName();
			}
			BossController.toWorldOut(this);
			World.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 781, msg));
		}

		ai_time_temp_1 = 0;
		clearExpList();
		clearAttackList();
		clearAstarList();
		setAiStatus(Lineage.AI_STATUS_CORPSE);
	}

	private void giveBossRewardToAttackers() {
		// 보스가 아니라면 리턴
		if (!isBoss() || getAttackListSize() == 0)
			return;

		// 2. 중급 이상 보스만 한입상자 지급
		String bossClass = getMonster().getBossClass();
		if (bossClass == null)
			return;
		// 등급이 "중급", "상급", "최상급" 만 허용 (원하는 값으로 수정)
		if (!bossClass.equals("중급 보스") && !bossClass.equals("상급 보스") && !bossClass.equals("최상급 보스"))
			return;

		// 설정 값 읽기
		String boxName = Lineage.boss_reward_box_name; // 예: "보스한입상자"
		int boxCount = Lineage.boss_reward_box_count; // 예: 1
		int rewardRange = Lineage.boss_reward_range; // 예: 10 (격자 단위)

		Item boxItem = ItemDatabase.find(boxName);
		if (boxItem == null)
			return;

		for (object o : getAttackList()) {
			if (!(o instanceof PcInstance))
				continue;
			PcInstance pc = (PcInstance) o;

			// 보스 근처 일정 거리 내에만 지급
			if (!Util.isDistance(this, pc, rewardRange))
				continue;

			// 조금이라도 데미지를 입힌 사람만 지급
			Exp e = findExp(pc);
			if (e == null || e.getDmg() <= 0)
				continue;

			ItemInstance reward = ItemDatabase.newInstance(boxItem);
			reward.setCount(boxCount);

			// 인벤이 꽉찼으면 드랍
			if (!toAutoPickupItem(pc, reward)) {
				// 땅에 드랍 (혹시 인벤이 가득찬 경우)
				reward.setObjectId(ServerDatabase.nextItemObjId());
				reward.toTeleport(pc.getX(), pc.getY(), pc.getMap(), false);
				reward.toDrop(this);
			}
			// 메시지
			ChattingController.toChatting(pc, String.format("\\fY[ %s ] %d개를 획득했습니다.", boxName, boxCount),
					Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	private static void sleep() {
		try {
			// 스레드 1초 대기
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void toAiCorpse(long time) {
		super.toAiCorpse(time);

		if (ai_time_temp_1 == 0)
			ai_time_temp_1 = time;

		// 시체 유지
		if (summon != null) {
			if (this instanceof PetInstance && ai_time_temp_1 + Lineage.ai_pet_corpse_time > time)
				return;
			if (this instanceof SummonInstance) {
				SummonInstance si = (SummonInstance) this;
				// 요정이 소환한 정령이라면 바로 소멸.
				// 그외엔 시체 유지.
				if (si.isElemental() == false && ai_time_temp_1 + Lineage.ai_summon_corpse_time > time)
					return;
			}
		} else {
			if (this instanceof MonsterInstance && ai_time_temp_1 + Lineage.ai_corpse_time > time
					&& !(this instanceof Faust_Ghost))
				return;
		}

		ai_time_temp_1 = 0;
		// 버프제거
		toReset(true);
		// 시체 제거
		clearList(true);
		World.remove(this);
		// 상태 변환.
		setAiStatus(Lineage.AI_STATUS_SPAWN);
	}

	public void toAiThreadRespawn() {
		// 전투 관련 변수 초기화.
		clearExpList();
		clearAttackList();
		clearAstarList();
		// 상태 변환
		setAiStatus(Lineage.AI_STATUS_CORPSE);
		World.removeMonster(this);
		// 버프제거
		toReset(true);
		// 시체 제거
		clearList(true);
		World.remove(this);
	}

	protected void toEventMonsterDead(long time) {
		// 멘트
		// toMent(time);
		// 아이템 드랍
		for (ItemInstance ii : inv.getList()) {
			boolean add = false;

			// ============================================
			// ✅ [고정 루터] 공격자가 1명(혼자 사냥)이면
			// 확률/랜덤 없이 무조건 그 1명에게 오토루팅 시도
			// (원본 흐름 유지 + 랜덤 바닥드랍 방지)
			// ============================================
			if (getAttackListSize() == 1) {
				object only = getAttackList(0);
				if (only instanceof Character) {
					Character onlyCha = (Character) only;

					// 범위 안에 있고 오토루팅 가능한 상태면 무조건 지급 시도
					if (isAutoPickup(ii) && isAutoPickupItem(onlyCha)) {
						if (toAutoPickupItem(onlyCha, ii)) {
							add = true;
						}
					}
				}
			}

			// 0) 소환몹 드랍 허용 여부 (config 반영)
			if ((this instanceof SummonInstance) && !Lineage.monster_summon_item_drop) {
				add = false; // 무조건 바닥 드랍
			} else {

				// 1) old 방식: 오토루팅 대상이면 오토루팅 시도
				if (isAutoPickup(ii)) {

					if (Lineage.is_damage_item_drop) {
						// (A) 기여도 기반 확률 지급 (원본 주석 살림)
						for (object oo : getAttackList()) {
							Exp e = findExp(oo);
							if (e != null && e.getObject() != null && (mon.getExp() * 0.3) <= e.getExp()) {

								// ✅ 지급 대상 캐릭터 결정 (펫/소환이면 주인PC로 치환)
								Character cha = null;
								if (e.getObject() instanceof Character) {
									cha = normalizeAttacker((Character) e.getObject());
								} else if (oo instanceof Character) {
									cha = normalizeAttacker((Character) oo);
								}

								// cha가 없거나(이상 케이스), 지급 가능 대상 아니면 스킵
								if (cha == null || !isAutoPickupItem(cha))
									continue;

								if (Util.random(0, Lineage.auto_pickup_percent) <= (e.getExp() / mon.getExp())
										* Lineage.auto_pickup_percent) {
									if (toAutoPickupItem(cha, ii)) {
										add = true;
										break;
									}
								}
							}
						}

						// (B) 실패 시 랜덤 fallback (원본 유지)
						if (!add) {
							for (int k = 0; k < 100; ++k) {
								if (expList == null || expList.size() <= 0)
									break;

								Exp e = getExpList(Util.random(0, expList.size() - 1));
								if (e != null && e.getObject() != null) {

									PcInstance pc = getLootReceiver(e.getObject()); // ★ 항상 주인 PC로 받기

									if (pc != null && isAutoPickupItem(pc)) { // 거리/생존 체크도 PC 기준
										if (toAutoPickupItem(pc, ii)) { // ★ toAutoPickupItem에 PC만 넣기
											add = true;
											break;
										}
									}
								}
							}
						}

					} else {
						// is_damage_item_drop=false면 랜덤 분배
						for (int k = 0; k < 100; ++k) {
							if (expList == null || expList.size() <= 0)
								break;

							Exp e = getExpList(Util.random(0, expList.size() - 1));
							if (e != null && e.getObject() instanceof Character) {

								// ✅ 펫/소환이면 주인PC로 치환
								Character cha = normalizeAttacker((Character) e.getObject());

								if (cha != null && isAutoPickupItem(cha)) {
									if (toAutoPickupItem(cha, ii)) {
										add = true;
										break;
									}
								}
							}
						}
					}
				}
			}

			// 2) 오토루팅 실패 or 오토루팅 대상 아님 -> 바닥 드랍
			if (!add) {
				if (ii.getObjectId() == 0)
					ii.setObjectId(ServerDatabase.nextItemObjId());

				int x = Util.random(getX() - 1, getX() + 1);
				int y = Util.random(getY() - 1, getY() + 1);

				if (World.isThroughObject(x, y + 1, map, 0))
					ii.toTeleport(x, y, map, false);
				else
					ii.toTeleport(this.x, this.y, map, false);

				ii.toDrop(this);
			}
		}

		// 경험치 지급
		double total_dmg = 0;
		for (Exp e : getExpList())
			total_dmg += e.getDmg();
		for (object o : getAttackList()) {
			Exp e = findExp(o);
			if (e != null) {
				// 데미지를 준만큼 경험치 설정.
				double percent = e.getDmg() / total_dmg;
				e.setExp(getMonster().getExp() * percent);
				//
				if (o instanceof Character) {
					Character cha = (Character) o;
					// 화면안에 존재할 경우에만 경험치 지급.
					if (Util.isDistance(this, cha, Lineage.SEARCH_LOCATIONRANGE)) {
						// 파티 경험치 처리. 실패하면 혼자 독식.
						if (!PartyController.toExp(cha, this, e.getExp()) && !cha.isDead()) {
							// 유저와 펫,로봇 만 처리.
							if (cha instanceof PcInstance || cha instanceof PetInstance) {
								// 경험치 지급.
								cha.toExp(this, e.getExp());
								// 라우풀 지급.
								double lawful = Math.round(((getLevel() * 3) / 2) * Lineage.rate_lawful);
								if (getMonster().getLawful() < 0)
									lawful = ~(int) lawful + 1;
								cha.setLawful(cha.getLawful() + (int) lawful);
							}
						}
					}
				}
				ExpDatabase.setPool(e);
			}
		}
		// 크리스마스 이벤트 양말 지급.
		if (Lineage.event_christmas && Util.random(0, 100) < 10 && getAttackListSize() > 0) {
			object o = getAttackList(0);

			if (o != null) {
				if (o instanceof PcInstance && !(o instanceof RobotInstance)) {
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("빨간 양말"));
					if (!toAutoPickupItem((PcInstance) o, ii))
						ItemDatabase.setPool(ii);
				}
			}
		}
		//
		if (Lineage.monster_boss_dead_message && isBoss()) {
			List<object> list = getAttackList();
			object o = list.get(0);

			if (o != null) {
				if (o instanceof SummonInstance)
					name = ((SummonInstance) o).getOwnName();
			}

			World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class),
					String.format("사망: %s", getMonster().getName())));
		}

		ai_time_temp_1 = 0;
		// 전투 관련 변수 초기화.
		clearExpList();
		clearAttackList();
		clearAstarList();

		if (ai_time_temp_1 == 0)
			ai_time_temp_1 = time;

		// 시체 유지
		if (summon != null) {
			if (this instanceof PetInstance && ai_time_temp_1 + Lineage.ai_pet_corpse_time > time)
				return;
			if (this instanceof SummonInstance) {
				SummonInstance si = (SummonInstance) this;
				// 요정이 소환한 정령이라면 바로 소멸.
				// 그외엔 시체 유지.
				if (si.isElemental() == false && ai_time_temp_1 + Lineage.ai_summon_corpse_time > time)
					return;
			}
		} else {
			if (this instanceof MonsterInstance && ai_time_temp_1 + Lineage.ai_corpse_time > time
					&& !(this instanceof Faust_Ghost))
				return;
		}

		ai_time_temp_1 = 0;
		// 버프제거
		toReset(true);
		// 시체 제거
		clearList(true);
		World.remove(this);
		// 상태 변환.
		setAiStatus(Lineage.AI_STATUS_SPAWN);
	}

	@Override
	protected void toAiSpawn(long time) {
		super.toAiSpawn(time);

		// 스폰 유지딜레이 값 초기화.
		if (ai_time_temp_1 == 0)
			ai_time_temp_1 = time;
		// 그룹몬스터쪽에 그룹원들이 스폰할 상태인지 확인. 아닐경우 딜레이 시키기.
		if (group_master != null) {
			if (group_master.getObjectId() != getObjectId()) {
				if (ai_time_temp_1 != 1)
					ai_time_temp_1 = time;
			} else {
				if (getGroupMasterDynamic() != null)
					ai_time_temp_1 = time;
			}
		}
		// 스폰 대기.
		if (ai_time_temp_1 + reSpawnTime > time) {

		} else {
			// 리스폰값이 정의되어 있지않다면 재스폰 할 필요 없음.
			// 서먼몬스터에서도 이걸 호출함.
			if (reSpawnTime == 0) {
				toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this,
						Lineage.object_teleport_effect), true);
				toAiThreadDelete();
			} else {
				// 그룹몬스터 마스터가 스폰한다면 해당 그룹원들도 스폰시키기.
				if (group_master != null && group_master.getObjectId() == getObjectId()) {
					for (MonsterInstance mi : group_master.getGroupList())
						mi.setAiTimeTemp1(1);
				}

				// 상태 변환
				setDead(false);
				setNowHp(getMaxHp());
				setNowMp(getMaxMp());
				// gfx 복구
				setGfx(getClassGfx());
				setGfxMode(getClassGfxMode());
				x = y = 0;
				// 스폰
				if (monSpawn != null) {
					MonsterSpawnlistDatabase.toSpawnMonster(this, World.get_map(homeMap), monSpawn.isRandom(),
							monSpawn.getX(), monSpawn.getY(), map, monSpawn.getLocSize(), monSpawn.getReSpawn(),
							monSpawn.getReSpawnMax(), false, false);
				} else {
					if (group_master == null)
						toTeleport(homeX, homeY, homeMap, false);
					else
						toTeleport(group_master.getX(), group_master.getY(), group_master.getMap(), false);
				}
				// 멘트
				// toMent(time);
				// 상태 변환.
				setAiStatus(Lineage.AI_STATUS_WALK);
			}
		}

	}

	@Override
	public void toAiEscape(long time) {
		super.toAiEscape(time);
		// 멘트
		// toMent(time);

		// 전투목록에서 가장 근접한 사용자 찾기.
		object o = null;
		for (object oo : getAttackList()) {
			if (o == null)
				o = oo;
			else if (Util.getDistance(this, oo) < Util.getDistance(this, o))
				o = oo;
		}

		// 못찾앗을경우 무시. 가끔생길수 잇는 현상이기에..
		if (o == null) {
			setAiStatus(Lineage.AI_STATUS_WALK);
			return;
		}

		// 반대방향 이동처리.
		setHeading(Util.oppositionHeading(this, o));
		int temp_heading = getHeading();
		do {
			// 이동 좌표 추출.
			int x = Util.getXY(getHeading(), true) + getX();
			int y = Util.getXY(getHeading(), false) + getY();
			// 해당 좌표 이동가능한지 체크.
			boolean tail = World.isThroughObject(getX(), getY(), getMap(), getHeading())
					&& (World.isMapdynamic(x, y, getMap()) == false || isBoss());
			if (tail) {
				// 타일이 이동가능하고 객체가 방해안하면 이동처리.
				toMoving(null, x, y, getHeading(), false);
				break;
			} else {
				setHeading(getHeading() + 1);
				if (temp_heading == getHeading())
					break;
			}
		} while (true);
	}

	@Override
	protected void toAiPickup(long time) {
		// 가장 근접한 아이템 찾기.
		object o = null;
		for (object oo : getInsideList()) {
			if (oo instanceof ItemInstance && !containsAstarList(o)) {
				if (o == null)
					o = oo;
				else if (Util.getDistance(this, oo) < Util.getDistance(this, o))
					o = oo;
			}
		}
		// 못찾앗을경우 다시 랜덤워킹으로 전환.
		if (o == null) {
			setAiStatus(Lineage.AI_STATUS_WALK);
			// 소환객체라면 아이템 줍기 완료 상태로 전환.
			if (this instanceof SummonInstance)
				((SummonInstance) this).setSummonMode(SUMMON_MODE.ItemPickUpFinal);
			return;
		}

		// 객체 거리 확인
		if (Util.isDistance(this, o, 0)) {
			super.toAiPickup(time);
			// 줍기
			synchronized (o.sync_pickup) {
				if (o.isWorldDelete() == false) {
					if (o instanceof ItemInstance) {
						ItemInstance item = (ItemInstance) o;
						int count = (int) o.getCount();

						for (int i = 0; i < count; i++) {
							if (inv.isAppendItem(item, 1))
								inv.toPickup(o, 1);
						}

						setAiStatus(Lineage.AI_STATUS_WALK);
						// 소환객체라면 아이템 줍기 완료 상태로 전환.
						if (this instanceof SummonInstance)
							((SummonInstance) this).setSummonMode(SUMMON_MODE.ItemPickUpFinal);
						return;
					}
				}
			}
		} else {
			ai_time = SpriteFrameDatabase.getGfxFrameTime(this, gfx, gfxMode + Lineage.GFX_MODE_WALK);
			// 아이템쪽으로 이동.
			toMoving(o, o.getX(), o.getY(), 0, true);
		}
	}

	/**
	 * 오토루팅 처리시 처리해도 되는 객체인지 확인하는 함수. : 중복코드 방지용.
	 * 
	 * @param o
	 * @return
	 */
	private boolean isAutoPickupItem(object o) {
		// 죽지않았고, 케릭터면서, 범위안에 있을경우.
		return !o.isDead() && (o instanceof PcInstance || o instanceof SummonInstance)
				&& Util.isDistance(o, this, Lineage.SEARCH_LOCATIONRANGE);
	}

	/**
	 * 자동루팅 상황에서 아이템 지급처리 담당하는 함수. : 중복 코드 방지용.
	 *
	 * @param cha
	 * @param ii
	 * @return
	 */
	private boolean toAutoPickupItem(Character cha, ItemInstance ii) {

		// 소환객체는 무시.
		if (this instanceof SummonInstance)
			return false;

		if (cha == null || ii == null || cha.getInventory() == null)
			return false;

		// 자동루팅 상태 + 인벤에 들어갈 수 있을 때만
		if (!cha.isAutoPickup() || !cha.getInventory().isAppend(ii, ii.getCount(), false))
			return false;

		// 성혈맹원이라면 자동픽업될 아이템 량 증가.
		if (cha instanceof PcInstance && KingdomController.find((PcInstance) cha) != null) {
			ii.setCount(Math.round(ii.getCount() * Lineage.kingdom_item_count_rate));
		}

		// 메시지용
		String msg_db = ii.toStringDB();

		// 원본 흐름 유지 (드랍 -> 픽업)
		ii.toDrop(this);
		ii.toPickup(cha);

		// =========================================================
		// 1) ✅ 여기서부터 "원본처럼" 실제 인벤 지급을 다시 수행해야 함
		// (네 서버는 toPickup만으로 지급이 끝나지 않음)
		// =========================================================
		Item itemDef = ItemDatabase.find(ii.getItem().getName());
		if (itemDef == null) {
			// 아이템DB 못찾으면 어쩔 수 없이 종료(원본도 i!=null일 때만 처리했음)
			return true;
		}

		// 먼저 루터(cha)에게 "원본 방식"으로 지급(스택/비스택)
		ItemInstance pickedForLooter = cha.getInventory().find(itemDef.getName(), ii.getBless(), itemDef.isPiles());
		if (pickedForLooter != null
				&& (pickedForLooter.getBless() != ii.getBless() || pickedForLooter.getEnLevel() != ii.getEnLevel()))
			pickedForLooter = null;

		if (pickedForLooter == null) {
			if (itemDef.isPiles()) {
				ItemInstance t = ItemDatabase.newInstance(itemDef);
				t.setObjectId(ServerDatabase.nextItemObjId());
				t.setBless(ii.getBless());
				t.setEnLevel(ii.getEnLevel());
				t.setCount(ii.getCount());
				t.setDefinite(true);
				cha.getInventory().append(t, true);
			} else {
				for (int k = 0; k < ii.getCount(); k++) {
					ItemInstance t = ItemDatabase.newInstance(itemDef);
					t.setObjectId(ServerDatabase.nextItemObjId());
					t.setBless(ii.getBless());
					t.setEnLevel(ii.getEnLevel());
					t.setCount(1);
					t.setDefinite(true);
					cha.getInventory().append(t, true);
				}
			}
		} else {
			// 겹치는 아이템이 존재할 경우.
			cha.getInventory().count(pickedForLooter, pickedForLooter.getCount() + ii.getCount(), true);
		}

		// =========================================================
		// 2) ✅ 파티 자동 분배 (아데나 / 신비한 날개깃털)
		// - "지급 후" 인벤에서 다시 나눠주는 방식(드랍 시스템 안 깨짐)
		// - 분배 성공하면 파티 요약 1줄만 출력하고 return true
		// =========================================================
		if (cha instanceof PcInstance && ((PcInstance) cha).getPartyId() > 0) {
			PcInstance pc = (PcInstance) cha;
			Party p = PartyController.find(pc);

			if (p != null && p.isParty(pc, p)) {

				boolean isAden = (ii.getItem().getNameIdNumber() == 4); // 아데나
				boolean isWing = ii.getItem().getName().equalsIgnoreCase("신비한 날개깃털");

				if (isAden || isWing) {

					// 근처 파티원 리스트
					List<PcInstance> near = new ArrayList<PcInstance>();
					for (PcInstance pt : p.getListTemp()) {
						if (pt != null && Util.isDistance(pc, pt, Lineage.SEARCH_LOCATIONRANGE))
							near.add(pt);
					}

					int memberCount = near.size();
					if (memberCount > 1) {

						long total = ii.getCount();
						long share = total / memberCount;

						if (share > 0) {
							// (A) 스택 아이템(아데나 등)
							if (itemDef.isPiles()) {

								// 루터 인벤에서 total 만큼 차감
								ItemInstance looterStack = pc.getInventory().find(itemDef.getName(), ii.getBless(),
										true);
								if (looterStack != null && (looterStack.getBless() != ii.getBless()
										|| looterStack.getEnLevel() != ii.getEnLevel()))
									looterStack = null;

								if (looterStack != null && looterStack.getCount() >= total) {
									pc.getInventory().count(looterStack, looterStack.getCount() - total, true);

									// 파티원에게 share 지급
									for (PcInstance pt : near) {
										ItemInstance t = pt.getInventory().find(itemDef.getName(), ii.getBless(), true);
										if (t != null
												&& (t.getBless() != ii.getBless() || t.getEnLevel() != ii.getEnLevel()))
											t = null;

										if (t == null) {
											t = ItemDatabase.newInstance(itemDef);
											t.setObjectId(ServerDatabase.nextItemObjId());
											t.setBless(ii.getBless());
											t.setEnLevel(ii.getEnLevel());
											t.setCount(share);
											t.setDefinite(true);
											pt.getInventory().append(t, true);
										} else {
											pt.getInventory().count(t, t.getCount() + share, true);
										}
									}

									// remainder는 루터에게
									long remain = total - (share * memberCount);
									if (remain > 0) {
										ItemInstance t = pc.getInventory().find(itemDef.getName(), ii.getBless(), true);
										if (t == null) {
											t = ItemDatabase.newInstance(itemDef);
											t.setObjectId(ServerDatabase.nextItemObjId());
											t.setBless(ii.getBless());
											t.setEnLevel(ii.getEnLevel());
											t.setCount(remain);
											t.setDefinite(true);
											pc.getInventory().append(t, true);
										} else {
											pc.getInventory().count(t, t.getCount() + remain, true);
										}
									}

									// 파티 요약 1줄
									if (Lineage.party_autopickup_item_print) {
										String n = isAden ? "아데나" : "신비한 날개깃털";
										ChattingController.toChatting(pc,
												String.format("%s %d 획득 (총 %d/%d명)", n, share, (share * memberCount),
														memberCount),
												Lineage.CHATTING_MODE_PARTY_MESSAGE);
									}
									return true;
								}

							} else {
								// (B) 비스택 아이템(깃털이 여기일 가능성 큼)
								// 루터 인벤에서 total개를 "1개씩" 제거하고, 파티원에게 share개씩 생성 지급

								// 1) 루터에서 total개 제거
								int removed = 0;
								for (int k = 0; k < total; k++) {
									ItemInstance one = pc.getInventory().find(itemDef.getName(), ii.getBless(), false);
									if (one != null && one.getEnLevel() == ii.getEnLevel()) {
										pc.getInventory().count(one, one.getCount() - 1, true); // count=1 -> 0이면 제거될 것
										removed++;
									} else {
										break;
									}
								}

								if (removed == total) {
									// 2) 파티원에게 share개씩 지급
									for (PcInstance pt : near) {
										for (int x = 0; x < share; x++) {
											ItemInstance t = ItemDatabase.newInstance(itemDef);
											t.setObjectId(ServerDatabase.nextItemObjId());
											t.setBless(ii.getBless());
											t.setEnLevel(ii.getEnLevel());
											t.setCount(1);
											t.setDefinite(true);
											pt.getInventory().append(t, true);
										}
									}

									// 3) remainder는 루터에게
									long remain = total - (share * memberCount);
									for (int x = 0; x < remain; x++) {
										ItemInstance t = ItemDatabase.newInstance(itemDef);
										t.setObjectId(ServerDatabase.nextItemObjId());
										t.setBless(ii.getBless());
										t.setEnLevel(ii.getEnLevel());
										t.setCount(1);
										t.setDefinite(true);
										pc.getInventory().append(t, true);
									}

									// 파티 요약 1줄
									if (Lineage.party_autopickup_item_print) {
										String n = isAden ? "아데나" : "신비한 날개깃털";
										ChattingController.toChatting(pc,
												String.format("%s %d 획득 (총 %d/%d명)", n, share, (share * memberCount),
														memberCount),
												Lineage.CHATTING_MODE_PARTY_MESSAGE);
									}
									return true;
								}
							}
						}
					}
				}
			}
		}

		// =========================================================
		// 3) ✅ 일반 아이템 메시지 출력 (원본처럼)
		// =========================================================

		if (Lineage.party_autopickup_item_print && cha instanceof PcInstance) {
			PcInstance pc = (PcInstance) cha;
			Party p = PartyController.find(pc);
			if (p != null && p.isParty(pc, p)) {
				ChattingController.toChatting(pc,
						String.format("%s 획득: %s", msg_db, getMonster().getName()),
						Lineage.CHATTING_MODE_PARTY_MESSAGE);
				return true;
			}
		}

		if (cha instanceof PcInstance) {
			PcInstance pc = (PcInstance) cha;
			if (pc.isAutoPickMessage()) {
				ChattingController.toChatting(pc,
						String.format("%s 획득: %s", msg_db, getMonster().getName()),
						Lineage.CHATTING_MODE_MESSAGE);
			}
			return true;
		}

		return true;
	}

	// MonsterInstance 안에 추가
	private Character normalizeAttacker(Character cha) {
		if (cha == null)
			return null;

		// 펫/소환이면 주인으로 치환
		if (cha instanceof SummonInstance) {
			SummonInstance si = (SummonInstance) cha;

			try {
				// SummonInstance 코드에서 summon.getMaster()를 쓰는 걸로 보아,
				// summon 객체는 존재하고 getMaster()는 object를 리턴함.
				if (si.summon != null) {
					object m = si.summon.getMaster();
					if (m instanceof PcInstance)
						return (PcInstance) m;
				}
			} catch (Throwable ignore) {
			}
		}

		return cha;
	}

	/**
	 * 해당 아이템 오토루팅 할지 여부.
	 * 
	 * @param ii
	 * @return
	 */
	private boolean isAutoPickup(ItemInstance ii) {
		// 아데나 오토루팅 여부
		if (ii.getItem().getNameIdNumber() == 4) {
			return Lineage.auto_pickup_aden;
		} else {
			return Lineage.auto_pickup;
		}
	}

	@Override
	public int getTotalAc() {
		return super.getTotalAc() + mon.getAc();
	}

	@Override
	public int getDynamicMr() {
		return super.getDynamicMr() + mon.getMr();
	}

	/**
	 * 동족 확인 2017-10-26 by all-night
	 */
	public boolean isFamily(MonsterInstance o, String name) {
		StringTokenizer st = new StringTokenizer(name, ",");

		while (st.hasMoreTokens()) {
			if (o.getMonster().getFamily().contains(st.nextToken()))
				return true;
		}

		return false;
	}

	private PcInstance getLootReceiver(object oo) {
		if (oo == null)
			return null;

		// PC면 그대로
		if (oo instanceof PcInstance)
			return (PcInstance) oo;

		// 펫/소환이면 주인 PC로
		if (oo instanceof SummonInstance) {
			SummonInstance si = (SummonInstance) oo;
			try {
				if (si.summon != null) {
					object m = si.summon.getMaster();
					if (m instanceof PcInstance)
						return (PcInstance) m;
				}
			} catch (Throwable t) {
			}
		}

		return null;
	}
}
