package lineage.world.object.instance;

import java.sql.Connection;

import lineage.bean.database.Item;
import lineage.bean.database.ItemSetoption;
import lineage.bean.database.Poly;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.bean.lineage.Inventory;
import lineage.database.ItemSetoptionDatabase;
import lineage.database.PolyDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_CharacterSpMr;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectLock;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.DamageController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.item.MagicFlute;
import lineage.world.object.item.potion.HealingPotion;
import lineage.world.object.magic.CounterBarrier;
import lineage.world.object.magic.ShapeChange;
import lineage.world.object.magic.SolidCarriage;

public class ItemInstance extends object implements BuffInterface {

	protected Character cha; // 아이템을 소지하고있는 객체
	protected Item item;
	protected int bless; // 축저주 여부
	protected int quantity; // 소막같은 수량
	protected int enLevel; // en
	protected int durability; // 손상도
	protected double dynamicStunDefence; // 스턴내성
	protected int dynamicMr; // mr
	protected boolean definite; // 확인 여부
	protected boolean equipped; // 착용 여부
	protected int nowTime; // 아이템 사용 남은 시간.
	protected long itemnowTime; // 죽림 서버 아이템 사용 남은 시간.
	protected long click_delay; // 아이템 클릭 딜레이를 주기위한 변수.
	protected int dynamicLight; // 동적 라이트값. 현재는 양초쪽에서 사용중. 해당 아이템에 밝기값을 저장하기 위해.
	protected long time_drop; // 드랍된 시간값.
	protected int dynamicAc; // 동적 ac 값.
	protected int enEarth; // 땅 속성 인첸트
	protected int enWater; // 물 속성 인첸트
	protected int enFire; // 불 속성 인첸트
	protected int enWind; // 바람 속성 인첸트
	protected double dynamicStunHit; // 스턴 적중
	protected int dynamicSp; // SP
	protected int dynamicReduction; // 리덕션
	protected int dynamicIgnoreReduction; // 리덕션 무시
	protected int dynamicSwordCritical; // 근접 크리티컬
	protected int dynamicBowCritical; // 원거리 크리티컬
	protected int dynamicMagicCritical; // 마법 크리티컬
	protected int dynamicPvpDmg; // pvp 데미지
	protected int dynamicPvpReduction; // pvp 리덕션
	protected long deleteTime;

	protected int tollTipMp;
	protected int tollTipAc;
	protected int tollTipSp;
	protected int tollTipHealingPotion;
	protected int tollTipStunDefens;
	protected int tollTipHp;
	protected int tollTipReduction;
	protected int tollTipHit;
	protected int tollTipDmg;
	protected int tollTipMr;
	protected int tollTipPvPReduction;
	protected int tollTipPvPDmg;

	protected int DolloptionA;
	protected int DolloptionB;
	protected int DolloptionC;
	protected int DolloptionD;
	protected int DolloptionE;

	// 개인상점에 사용되는 변수
	private int usershopIdx; // sell 처리시 위치값 지정용.
	private int usershopBuyPrice; // 판매 가격
	private int usershopSellPrice; // 구입 가격
	private int usershopBuyCount; // 판매 갯수
	private int usershopSellCount; // 구입 갯수

	public ItemInstance() {
		//
	}

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ItemInstance();
		return item;
	}

	public ItemInstance clone(Item item) {
		this.item = item;
		name = item.getNameId();
		gfx = item.getGroundGfx();

		return this;
	}

	@Override
	public void close() {
		super.close();
		// 메모리 초기화 함수.
		item = null;
		cha = null;
		time_drop = click_delay = quantity = enLevel = durability = dynamicMr = nowTime = usershopBuyPrice = usershopSellPrice = usershopBuyCount = usershopSellCount = usershopIdx = dynamicLight = dynamicAc = enEarth = enWater = enFire = enWind = 0;
		bless = 1;
		dynamicStunDefence = 0;
		definite = equipped = false;
		dynamicStunHit = 0;
		DolloptionA = DolloptionB = DolloptionC = DolloptionD = DolloptionE = 0;
		dynamicSp = dynamicReduction = dynamicIgnoreReduction = dynamicSwordCritical = dynamicBowCritical = dynamicMagicCritical = 0;
		tollTipMp = tollTipSp = tollTipHealingPotion = tollTipStunDefens = tollTipHp = tollTipReduction = tollTipHit = tollTipDmg = tollTipMr = tollTipPvPReduction = tollTipPvPDmg = 0;
		itemnowTime = 0;

	}

	public long getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(long deleteTime) {
		this.deleteTime = deleteTime;
	}

	/**
	 * 아이템을 사용해도 되는지 확인해주는 함수.<br/>
	 * : 아이템 더블클릭하면 젤 우선적으로 호출됨.<br/>
	 * : C_ItemClick 에서 사용중.<br/>
	 * 
	 * @return
	 */
	public boolean isClick(PcInstance pc) {
		if (pc != null) {
			// 맵에따른 아이템 제한 확인.
			switch (pc.getMap()) {
				case 22:
					// 게라드 시험 퀘 맵일경우 비취물약만 사용가능하도록 하기.
					if (item.getNameIdNumber() != 233) {
						// 귀환이나 순간이동 주문서 사용시 케릭동작에 락이 걸리기때문에 그것을 풀기위한것.
						pc.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x09));
						return false;
					}
					break;
				case 201:
					// 마법사 30 퀘 일때. 귀환 빼고 다 불가능.
					if (item.getNameIdNumber() != 505) {
						pc.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x09));
						return false;
					}
					break;
			}
			// 마법의 플룻에 따른 제한 확인.
			if (BuffController.find(pc).find(MagicFlute.class) != null) {
				pc.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x09));
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 343));
				return false;
			}
		}

		// 딜레이 확인.
		long time = System.currentTimeMillis();
		// 물약은 따로 딜레이 체크
		if (this instanceof HealingPotion) {
			if (time - pc.getClickHealingPotionTime() >= item.getDelay()) {
				pc.setClickHealingPotionTime(time);
				return true;
			}
		} else {
			if (time - click_delay >= item.getDelay()) {
				click_delay = time;
				return true;
			}
		}

		return false;
	}

	public int getInvDolloptionA() {
		return DolloptionA;
	}

	public void setInvDolloptionA(int DolloptionA) {
		this.DolloptionA = DolloptionA;
	}

	public int getInvDolloptionB() {
		return DolloptionB;
	}

	public void setInvDolloptionB(int DolloptionB) {
		this.DolloptionB = DolloptionB;
	}

	public int getInvDolloptionC() {
		return DolloptionC;
	}

	public void setInvDolloptionC(int DolloptionC) {
		this.DolloptionC = DolloptionC;
	}

	public int getInvDolloptionD() {
		return DolloptionD;
	}

	public void setInvDolloptionD(int DolloptionD) {
		this.DolloptionD = DolloptionD;
	}

	public int getInvDolloptionE() {
		return DolloptionE;
	}

	public void setInvDolloptionE(int DolloptionE) {
		this.DolloptionE = DolloptionE;
	}

	public int getDynamicPvpDmg() {
		return dynamicPvpDmg;
	}

	public void setDynamicPvpDmg(int dynamicPvpDmg) {
		this.dynamicPvpDmg = dynamicPvpDmg;
	}

	public int getDynamicPvpReduction() {
		return dynamicPvpReduction;
	}

	public void setDynamicPvpReduction(int dynamicPvpReduction) {
		this.dynamicPvpReduction = dynamicPvpReduction;
	}

	public double getDynamicStunHit() {
		return dynamicStunHit;
	}

	public void setDynamicStunHit(double dynamicStunHit) {
		this.dynamicStunHit = dynamicStunHit;
	}

	public int getDynamicSp() {
		return dynamicSp;
	}

	public void setDynamicSp(int dynamicSp) {
		this.dynamicSp = dynamicSp;
	}

	public int getDynamicReduction() {
		return dynamicReduction;
	}

	public void setDynamicReduction(int dynamicReduction) {
		this.dynamicReduction = dynamicReduction;
	}

	public int getDynamicIgnoreReduction() {
		return dynamicIgnoreReduction;
	}

	public void setDynamicIgnoreReduction(int dynamicIgnoreReduction) {
		this.dynamicIgnoreReduction = dynamicIgnoreReduction;
	}

	public int getDynamicSwordCritical() {
		return dynamicSwordCritical;
	}

	public void setDynamicSwordCritical(int dynamicSwordCritical) {
		this.dynamicSwordCritical = dynamicSwordCritical;
	}

	public int getDynamicBowCritical() {
		return dynamicBowCritical;
	}

	public void setDynamicBowCritical(int dynamicBowCritical) {
		this.dynamicBowCritical = dynamicBowCritical;
	}

	public int getDynamicMagicCritical() {
		return dynamicMagicCritical;
	}

	public void setDynamicMagicCritical(int dynamicMagicCritical) {
		this.dynamicMagicCritical = dynamicMagicCritical;
	}

	public double getDynamicStunDefence() {
		return dynamicStunDefence;
	}

	public void setDynamicStunDefence(double dynamicStunDefence) {
		this.dynamicStunDefence = dynamicStunDefence;
	}

	public long getTimeDrop() {
		return time_drop;
	}

	public void setTimeDrop(long time_drop) {
		this.time_drop = time_drop;
	}

	public int getDynamicLight() {
		return dynamicLight;
	}

	public void setDynamicLight(int dynamicLight) {
		this.dynamicLight = dynamicLight;
	}

	public int getDynamicAc() {
		return dynamicAc;
	}

	public void setDynamicAc(int dynamicAc) {
		this.dynamicAc = dynamicAc;
	}

	public int getEnEarth() {
		return enEarth;
	}

	public int getEnEarthDamage() {
		switch (enEarth) {
			case 1:
				return 1;
			case 2:
				return 3;
			case 3:
				return 7;
			case 4:
				return 4;
			case 5:
				return 5;
			default:
				return enEarth;
		}
	}

	public void setEnEarth(int enEarth) {
		if (enEarth > 5)
			enEarth = 5;
		this.enEarth = enEarth;
	}

	public int getEnWater() {
		return enWater;
	}

	public int getEnWaterDamage() {
		switch (enWater) {
			case 1:
				return 1;
			case 2:
				return 3;
			case 3:
				return 7;
			case 4:
				return 4;
			case 5:
				return 5;
			default:
				return enWater;
		}
	}

	public void setEnWater(int enWater) {
		if (enWater > 5)
			enWater = 5;
		this.enWater = enWater;
	}

	public int getEnFire() {
		return enFire;
	}

	public int getEnFireDamage() {
		switch (enFire) {
			case 1:
				return 1;
			case 2:
				return 3;
			case 3:
				return 7;
			case 4:
				return 4;
			case 5:
				return 5;
			default:
				return enFire;
		}
	}

	public void setEnFire(int enFire) {
		if (enFire > 5)
			enFire = 5;
		this.enFire = enFire;
	}

	public int getEnWind() {
		return enWind;
	}

	public int getEnWindDamage() {
		switch (enWind) {
			case 1:
				return 1;
			case 2:
				return 3;
			case 3:
				return 7;
			case 4:
				return 4;
			case 5:
				return 5;
			default:
				return enWind;
		}
	}

	public void setEnWind(int enWind) {
		if (enWind > 5)
			enWind = 5;
		this.enWind = enWind;
	}

	@Override
	public Character getCharacter() {
		return cha;
	}

	public Item getItem() {
		return item;
	}

	public int getBless() {
		return bless;
	}

	public void setBless(int bless) {
		this.bless = bless;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getEnLevel() {
		return enLevel;
	}

	public void setEnLevel(int enLevel) {
		this.enLevel = enLevel;
	}

	public int getDurability() {
		return durability;
	}

	public void setDurability(int durability) {
		if (durability > Lineage.item_durability_max)
			durability = Lineage.item_durability_max;
		else if (durability < 0)
			durability = 0;
		this.durability = durability;

	}

	public int getDynamicMr() {
		return dynamicMr;
	}

	public void setDynamicMr(int dynamicMr) {
		this.dynamicMr = dynamicMr;
	}

	public boolean isDefinite() {
		return definite;
	}

	public void setDefinite(boolean definite) {
		this.definite = definite;
	}

	public boolean isEquipped() {
		return equipped;
	}

	public void setEquipped(boolean equipped) {
		this.equipped = equipped;
	}

	public int getNowTime() {
		return nowTime < 0 ? 0 : nowTime;
	}

	public void setNowTime(int nowTime) {
		if (nowTime >= 0)
			this.nowTime = nowTime;
	}

	public int getWeight() {
		return (int) Math.round(item.getWeight() * getCount());
	}

	public int getUsershopBuyPrice() {
		return usershopBuyPrice;
	}

	public void setUsershopBuyPrice(int usershopBuyPrice) {
		this.usershopBuyPrice = usershopBuyPrice;
	}

	public int getUsershopSellPrice() {
		return usershopSellPrice;
	}

	public void setUsershopSellPrice(int usershopSellPrice) {
		this.usershopSellPrice = usershopSellPrice;
	}

	public int getUsershopBuyCount() {
		return usershopBuyCount;
	}

	public void setUsershopBuyCount(int usershopBuyCount) {
		this.usershopBuyCount = usershopBuyCount;
	}

	public int getUsershopSellCount() {
		return usershopSellCount;
	}

	public void setUsershopSellCount(int usershopSellCount) {
		this.usershopSellCount = usershopSellCount;
	}

	public int getUsershopIdx() {
		return usershopIdx;
	}

	public void setUsershopIdx(int usershopIdx) {
		this.usershopIdx = usershopIdx;
	}

	public int getTollTipPvPDmg() {
		return tollTipPvPDmg;
	}

	public void setTollTipPvPDmg(int tollTipPvPDmg) {
		this.tollTipPvPDmg = tollTipPvPDmg;
	}

	public int getTollTipPvPReduction() {
		return tollTipPvPReduction;
	}

	public void setTollTipPvPReduction(int tollTipPvPReduction) {
		this.tollTipPvPReduction = tollTipPvPReduction;
	}

	public int getTollTipMr() {
		return tollTipMr;
	}

	public void setTollTipMr(int tollTipMr) {
		this.tollTipMr = tollTipMr;
	}

	public int getTollTipDmg() {
		return tollTipDmg;
	}

	public void setTollTipDmg(int tollTipDmg) {
		this.tollTipDmg = tollTipDmg;
	}

	public int getTollTipHit() {
		return tollTipHit;
	}

	public void setTollTipHit(int tollTipHit) {
		this.tollTipHit = tollTipHit;
	}

	public int getTollTipReduction() {
		return tollTipReduction;
	}

	public void setTollTipReduction(int tollTipReduction) {
		this.tollTipReduction = tollTipReduction;
	}

	public int getTollTipHp() {
		return tollTipHp;
	}

	public void setTollTipHp(int tollTipHp) {
		this.tollTipHp = tollTipHp;
	}

	public int getTollTipStunDefens() {
		return tollTipStunDefens;
	}

	public void setTollTipStunDefens(int tollTipStunDefens) {
		this.tollTipStunDefens = tollTipStunDefens;
	}

	public int getTollTipHealingPotion() {
		return tollTipHealingPotion;
	}

	public void setTollTipHealingPotion(int tollTipHealingPotion) {
		this.tollTipHealingPotion = tollTipHealingPotion;
	}

	public int getTollTipSp() {
		return tollTipSp;
	}

	public void setTollTipSp(int tollTipSp) {
		this.tollTipSp = tollTipSp;
	}

	public int getTollTipMp() {
		return tollTipMp;
	}

	public void setTollTipMp(int tollTipMp) {
		this.tollTipMp = tollTipMp;
	}

	public int getTollTipAc() {
		return tollTipAc;
	}

	public void setTollTipAc(int tollTipAc) {
		this.tollTipAc = tollTipAc;
	}

	public long getItemNowTime() {
		return itemnowTime < 0 ? 0 : itemnowTime;
	}

	public void setItemNowTime(long itemnowTime) {
		if (nowTime >= 0)
			this.itemnowTime = itemnowTime;
	}

	/**
	 * 리니지 월드에 접속했을때 착용중인 아이템 처리를 위해 사용되는 메서드.
	 * 
	 * @param pc
	 */
	public void toWorldJoin(Connection con, PcInstance pc) {
		//
		cha = pc;
		//

		if (getItem().getEnchantMr() != 0 || getItem().getEnchantStunDefense() != 0
				|| getItem().getEnchantStunHit() != 0 ||
				getItem().getEnchantSp() != 0 || getItem().getEnchantReduction() != 0
				|| getItem().getEnchantIgnoreReduction() != 0 ||
				getItem().getEnchantSwordCritical() != 0 || getItem().getEnchantBowCritical() != 0
				|| getItem().getEnchantMagicCritical() != 0 ||
				getItem().getEnchantPvpDamage() != 0 || getItem().getEnchantPvpReduction() != 0) {
			if (getItem().getName().equalsIgnoreCase("신성한 엘름의 축복")) {
				if (getEnLevel() > 4)
					setDynamicMr((getEnLevel() - 4) * getItem().getEnchantMr());
			} else {
				setDynamicMr(getEnLevel() * getItem().getEnchantMr());
			}
			setDynamicStunDefence(getEnLevel() * getItem().getEnchantStunDefense());
			setDynamicStunHit(getEnLevel() * getItem().getEnchantStunHit());
			setDynamicSp(getEnLevel() * getItem().getEnchantSp());
			setDynamicReduction(getEnLevel() * getItem().getEnchantReduction());
			setDynamicIgnoreReduction(getEnLevel() * getItem().getEnchantIgnoreReduction());
			setDynamicSwordCritical(getEnLevel() * getItem().getEnchantSwordCritical());
			setDynamicBowCritical(getEnLevel() * getItem().getEnchantBowCritical());
			setDynamicMagicCritical(getEnLevel() * getItem().getEnchantMagicCritical());
			// setDynamicPvpDmg((getEnLevel() * getItem().getEnchantPvpDamage()) +
			// getItem().getPvpDamage());
			// 기본 대미지는 제거하고, 인첸트 보너스만 계산
			setDynamicPvpDmg(getEnLevel() * getItem().getEnchantPvpDamage());
			setDynamicPvpReduction(getEnLevel() * getItem().getEnchantPvpReduction());

			if (Lineage.server_version > 144 && Lineage.server_version <= 200)
				pc.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
		}
	}

	@Override
	public void toPickup(Character cha) {
		this.cha = cha;
	}

	/**
	 * 해당 아이템이 드랍됫을때 호출되는 메서드.
	 * 
	 * @param cha
	 */
	public void toDrop(Character cha) {
		this.cha = null;
		setTimeDrop(System.currentTimeMillis());
	}

	/**
	 * 아이템 착용 및 해제시 호출되는 메서드.
	 * 
	 * @param cha
	 * @param inv
	 */
	public void toEquipped(Character cha, Inventory inv) {
		this.cha = cha;
		toOption(cha, true); // 착용 시 옵션 즉시 적용
	}

	/**
	 * 인첸트 활성화 됫을때 아이템의 뒷처리를 처리하도록 요청하는 메서드.
	 * 
	 * @param pc
	 * @param en
	 */
	public void toEnchant(PcInstance pc, int en) {
		if (getItem() == null ||
				(getItem().getEnchantMr() == 0 && getItem().getEnchantStunDefense() == 0
						&& getItem().getEnchantStunHit() == 0 &&
						getItem().getEnchantSp() == 0 && getItem().getEnchantReduction() == 0
						&& getItem().getEnchantIgnoreReduction() == 0 &&
						getItem().getEnchantSwordCritical() == 0 && getItem().getEnchantBowCritical() == 0
						&& getItem().getEnchantMagicCritical() == 0 &&
						getItem().getEnchantPvpDamage() == 0 && getItem().getEnchantPvpReduction() == 0)
				||
				en == -126 || en == -127)
			return;
		// 인첸을 성공했다면 마법망토는 mr값을 상승해야함.
		if (en != 0) {
			int new_mr = getEnLevel() * getItem().getEnchantMr();
			double new_stunDefence = getEnLevel() * getItem().getEnchantStunDefense();
			double new_stunHit = getEnLevel() * getItem().getEnchantStunHit();
			int new_sp = getEnLevel() * getItem().getEnchantSp();
			int new_reduction = getEnLevel() * getItem().getEnchantReduction();
			int new_ignoreReduction = getEnLevel() * getItem().getEnchantIgnoreReduction();
			int new_swordCritical = getEnLevel() * getItem().getEnchantSwordCritical();
			int new_bowCritical = getEnLevel() * getItem().getEnchantBowCritical();
			int new_magicCritical = getEnLevel() * getItem().getEnchantMagicCritical();
			int new_pvp_dmg = getEnLevel() * getItem().getEnchantPvpDamage();
			int new_pvp_reduction = getEnLevel() * getItem().getEnchantPvpReduction();

			if (getItem().getName().equalsIgnoreCase("신성한 엘름의 축복")) {
				if (getEnLevel() > 4)
					new_mr = (getEnLevel() - 4) * getItem().getEnchantMr();
				else
					new_mr = 0;
			}
			if (equipped) {
				// 이전에 세팅값 빼기.
				pc.setDynamicMr(pc.getDynamicMr() - getDynamicMr());
				pc.setDynamicStunResist(pc.getDynamicStunResist() - getDynamicStunDefence());
				pc.setDynamicStunHit(pc.getDynamicStunHit() - getDynamicStunHit());
				pc.setDynamicSp(pc.getDynamicSp() - getDynamicSp());
				pc.setDynamicReduction(pc.getDynamicReduction() - getDynamicReduction());
				pc.setDynamicIgnoreReduction(pc.getDynamicIgnoreReduction() - getDynamicIgnoreReduction());
				pc.setDynamicCritical(pc.getDynamicCritical() - getDynamicSwordCritical());
				pc.setDynamicBowCritical(pc.getDynamicBowCritical() - getDynamicBowCritical());
				pc.setDynamicMagicCritical(pc.getDynamicMagicCritical() - getDynamicMagicCritical());
				pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() - getDynamicPvpDmg());
				pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() - getDynamicPvpReduction());

				// 인첸에따른 새로운값 적용.
				pc.setDynamicMr(pc.getDynamicMr() + new_mr);
				pc.setDynamicStunResist(pc.getDynamicStunResist() + new_stunDefence);
				pc.setDynamicStunHit(pc.getDynamicStunHit() + new_stunHit);
				pc.setDynamicSp(pc.getDynamicSp() + new_sp);
				pc.setDynamicReduction(pc.getDynamicReduction() + new_reduction);
				pc.setDynamicIgnoreReduction(pc.getDynamicIgnoreReduction() + new_ignoreReduction);
				pc.setDynamicCritical(pc.getDynamicCritical() + new_swordCritical);
				pc.setDynamicBowCritical(pc.getDynamicBowCritical() + new_bowCritical);
				pc.setDynamicMagicCritical(pc.getDynamicMagicCritical() + new_magicCritical);
				pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() + new_pvp_dmg);
				pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() + new_pvp_reduction);
				pc.toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), pc));
			}
			setDynamicMr(new_mr);
			setDynamicStunDefence(new_stunDefence);
			setDynamicStunHit(new_stunHit);
			setDynamicSp(new_sp);
			setDynamicReduction(new_reduction);
			setDynamicIgnoreReduction(new_ignoreReduction);
			setDynamicSwordCritical(new_swordCritical);
			setDynamicBowCritical(new_bowCritical);
			setDynamicMagicCritical(new_magicCritical);
			setDynamicPvpDmg(new_pvp_dmg);
			setDynamicPvpReduction(new_pvp_reduction);

			if (Lineage.server_version <= 144)
				pc.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
			else
				pc.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
		}
	}

	/**
	 * 마법책 및 수정에 스킬값 지정하는 함수.
	 * 
	 * @param skill_level
	 */
	@Override
	public void setSkill(Skill skill) {
	}

	/**
	 * 아이템을 이용해 cha 가 o 에게 피해를 입히면 호출되는 함수.
	 * 
	 * @param cha
	 * @param o
	 * @return
	 */
	public boolean toDamage(Character cha, object o) {
		return false;
	}

	/**
	 * toDamage(Character cha, object o) 거친후 값이 true가 될경우 아래 함수를 호출해 추가적으로 데미지를 더하도록
	 * 함.
	 * 
	 * @return
	 */
	public int toDamage(int dmg) {
		return 0;
	}

	/**
	 * toDamage(Character cha, object o) 거친후 값이 true가 될경우 이팩트를 표현할 값을 턴.
	 * 
	 * @return
	 */
	public int toDamageEffect() {
		return 0;
	}

	/**
	 * 펫의 오프젝트값 리턴.
	 * 
	 * @return
	 */
	public long getPetObjectId() {
		return 0;
	}

	public void setPetObjectId(final long id) {
	}

	/**
	 * 여관방 열쇠 키값
	 * 
	 * @return
	 */
	public long getInnRoomKey() {
		return 0;
	}

	public void setInnRoomKey(final long key) {
	}

	/**
	 * 편지지 디비 연결 고리인 uid
	 * 
	 * @return
	 */
	public int getLetterUid() {
		return 0;
	}

	public void setLetterUid(final int uid) {
	}

	/**
	 * 레이스 관련 함수
	 * 
	 * @return
	 */
	public String getRaceTicket() {
		return "";
	}

	public void setRaceTicket(String ticket) {
	}

	public int getBressPacket() {
		if (Lineage.server_version > 144) {
			if (definite) {
				if (bless < 0)
					return Lineage.server_version > 280 ? bless : bless + 128;
				else
					return bless;
			} else {
				return 3;
			}
		} else {
			return bless;
		}
	}

	/**
	 * 레벨 제한 체크
	 */
	protected boolean isLvCheck(Character cha) {
		// 착용하지 않은 상태에서만 체크
		if (!isEquipped()) {
			if (item.getLevelMin() > 0 && item.getLevelMin() > cha.getLevel()) {
				// cha.toSender(new SItemLevelFails(item.Level));
				// 672 : 이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.
				ChattingController.toChatting(cha, String.format("이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.", item.getLevelMin()),
						Lineage.CHATTING_MODE_MESSAGE);
				return false;
			}
			if (item.getLevelMax() > 0 && item.getLevelMax() < cha.getLevel()) {
				// 673 : 이 아이템은 %d레벨 이하일때만 사용할 수 있습니다.
				ChattingController.toChatting(cha, String.format("이 아이템은 %d레벨 이하일때만 사용할 수 있습니다.", item.getLevelMax()),
						Lineage.CHATTING_MODE_MESSAGE);
				return false;
			}
		}

		return true;
	}

	/**
	 * 클레스 착용가능 여부 체크 부분
	 */
	protected boolean isClassCheck(Character cha) {
		switch (cha.getClassType()) {
			case Lineage.LINEAGE_CLASS_ROYAL: // 군주
				return item.getRoyal() > 0;
			case Lineage.LINEAGE_CLASS_KNIGHT: // 기사
				return item.getKnight() > 0;
			case Lineage.LINEAGE_CLASS_ELF: // 요정
				return item.getElf() > 0;
			case Lineage.LINEAGE_CLASS_WIZARD: // 법사
				return item.getWizard() > 0;
			case Lineage.LINEAGE_CLASS_DARKELF: // 다크엘프
				return item.getDarkElf() > 0;
			case Lineage.LINEAGE_CLASS_DRAGONKNIGHT: // 용기사
				return item.getDragonKnight() > 0;
			case Lineage.LINEAGE_CLASS_BLACKWIZARD: // 환술사
				return item.getBlackWizard() > 0;
		}
		return true;
	}

	/**
	 * 아이템을 착용 및 해제할때 호출됨. : 장비를 해제할때 제거해야할 버프가 있는지 확인하고 제거하는 메서드.
	 * 
	 * @param cha
	 */
	public void toBuffCheck(Character cha) {
		// 착용상태는 무시.
		if (isEquipped())
			return;

		if (getItem().getSlot() == Lineage.SLOT_WEAPON)
			BuffController.remove(cha, CounterBarrier.class);
		if (getItem().getSlot() == Lineage.SLOT_SHIELD)
			BuffController.remove(cha, SolidCarriage.class);
	}

	/**
	 * 아이템 부가옵션 적용및 해제 부분
	 */
	public void toOption(Character cha, boolean sendPacket) {
		if (getItem() == null)
			return;

		boolean equipped = isEquipped();
		int sign = equipped ? 1 : -1;

		// 🔥 화염 속성: 근거리 대미지
		int fire = getEnFire(); // ← 내성 제거
		if (fire > 0) {
			cha.setDynamicAddDmg(cha.getDynamicAddDmg() + (sign * fire));
			if (equipped)
				ChattingController.toChatting(cha, String.format("화령속성: %s - 근거리 대미지 +%d", getItem().getName(), fire),
						Lineage.CHATTING_MODE_MESSAGE);
		}

		// 💧 물 속성: SP 증가
		int water = getEnWater();
		if (water > 0) {
			cha.setDynamicSp(cha.getDynamicSp() + (sign * water));
			if (equipped)
				ChattingController.toChatting(cha, String.format("수령속성: %s - SP +%d", getItem().getName(), water),
						Lineage.CHATTING_MODE_MESSAGE);
		}

		// 🍃 바람 속성: 원거리 대미지
		int wind = getEnWind();
		if (wind > 0) {
			cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + (sign * wind));
			if (equipped)
				ChattingController.toChatting(cha, String.format("풍령속성: %s - 원거리 대미지 +%d", getItem().getName(), wind),
						Lineage.CHATTING_MODE_MESSAGE);
		}

		// 🟫 땅 속성: 리덕션
		int earth = getEnEarth();
		if (earth > 0) {
			cha.setDynamicReduction(cha.getDynamicReduction() + (sign * earth));
			if (equipped)
				ChattingController.toChatting(cha, String.format("지령속성: %s - 리덕션 +%d", getItem().getName(), earth),
						Lineage.CHATTING_MODE_MESSAGE);
		}

		if (this.getItem().getName().contains("마법인형: ") && getInvDolloptionA() > 0) {

			double 스턴내성 = 0;

			스턴내성 = (getInvDolloptionA() * 0.01);

			if (equipped) {
				cha.setDynamicStunResist(cha.getDynamicStunResist() + 스턴내성);
				if (getInvDolloptionA() > 0)
					ChattingController.toChatting(cha,
							String.format("%s [인형 부여 옵션] : 스턴 내성+ %d", getItem().getName(), getInvDolloptionA()),
							Lineage.CHATTING_MODE_MESSAGE);

			} else {
				cha.setDynamicStunResist(cha.getDynamicStunResist() - 스턴내성);
			}
		}
		if (this.getItem().getName().contains("마법인형: ") && getInvDolloptionB() > 0) {
			double 스턴적중 = 0;
			스턴적중 = (getInvDolloptionB() * 0.01);
			if (equipped) {
				cha.setDynamicStunHit(cha.getDynamicStunHit() + 스턴적중);
				if (getInvDolloptionB() > 0)
					ChattingController.toChatting(cha,
							String.format("%s [인형 부여 옵션] : 스턴 적중+ %d", getItem().getName(), getInvDolloptionB()),
							Lineage.CHATTING_MODE_MESSAGE);

			} else {
				cha.setDynamicStunHit(cha.getDynamicStunHit() - 스턴적중);
			}
		}
		if (this.getItem().getName().contains("마법인형: ") && getInvDolloptionC() > 0) {
			int 마법적중 = 0;
			마법적중 = getInvDolloptionC();

			if (equipped) {
				cha.setDynamicMagicHit(cha.getDynamicMagicHit() + 마법적중);
				if (getInvDolloptionC() > 0)
					ChattingController.toChatting(cha,
							String.format("%s [인형 부여 옵션] : 마법 적중+ %d", getItem().getName(), getInvDolloptionC()),
							Lineage.CHATTING_MODE_MESSAGE);

			} else {
				cha.setDynamicMagicHit(cha.getDynamicMagicHit() - 마법적중);
			}
		}
		if (this.getItem().getName().contains("마법인형: ") && getInvDolloptionD() > 0) {
			int 대미지 = 0;
			대미지 = getInvDolloptionD();
			if (equipped) {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 대미지);
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + 대미지);
				if (getInvDolloptionD() > 0)
					ChattingController.toChatting(cha,
							String.format("%s [인형 부여 옵션] : 근/원거리 대미지 + %d", getItem().getName(), getInvDolloptionD()),
							Lineage.CHATTING_MODE_MESSAGE);

			} else {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 대미지);
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - 대미지);
			}
		}
		if (this.getItem().getName().contains("마법인형: ") && getInvDolloptionE() > 0) {
			int 명중 = 0;
			명중 = getInvDolloptionE();
			if (equipped) {
				cha.setDynamicAddHit(cha.getDynamicAddHit() + 명중);
				cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + 명중);

				if (getInvDolloptionE() > 0)
					ChattingController.toChatting(cha,
							String.format("%s [인형 부여 옵션] : 근/원거리 명중 + %d", getItem().getName(), getInvDolloptionE()),
							Lineage.CHATTING_MODE_MESSAGE);

			} else {
				cha.setDynamicAddHit(cha.getDynamicAddHit() - 명중);
				cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - 명중);
			}
		}

		if (getItem().getAddStr() != 0) {
			if (equipped) {
				cha.setDynamicStr(cha.getDynamicStr() + getItem().getAddStr());
			} else {
				cha.setDynamicStr(cha.getDynamicStr() - getItem().getAddStr());
			}
		}

		if (getItem().getAddDex() != 0) {
			if (equipped) {
				cha.setDynamicDex(cha.getDynamicDex() + getItem().getAddDex());
			} else {
				cha.setDynamicDex(cha.getDynamicDex() - getItem().getAddDex());
			}

		}
		if (getItem().getAddCon() != 0) {
			if (equipped) {
				cha.setDynamicCon(cha.getDynamicCon() + getItem().getAddCon());
			} else {
				cha.setDynamicCon(cha.getDynamicCon() - getItem().getAddCon());
			}
		}
		if (getItem().getAddInt() != 0) {
			if (equipped) {
				cha.setDynamicInt(cha.getDynamicInt() + getItem().getAddInt());
			} else {
				cha.setDynamicInt(cha.getDynamicInt() - getItem().getAddInt());
			}
		}
		if (getItem().getAddCha() != 0) {
			if (equipped) {
				cha.setDynamicCha(cha.getDynamicCha() + getItem().getAddCha());
			} else {
				cha.setDynamicCha(cha.getDynamicCha() - getItem().getAddCha());
			}
		}
		if (getItem().getAddWis() != 0) {
			if (equipped) {
				cha.setDynamicWis(cha.getDynamicWis() + getItem().getAddWis());
			} else {
				cha.setDynamicWis(cha.getDynamicWis() - getItem().getAddWis());
			}
		}
		if (getItem().getAddHp() > 0) {
			if (equipped)
				cha.setDynamicHp(cha.getDynamicHp() + getItem().getAddHp());
			else
				cha.setDynamicHp(cha.getDynamicHp() - getItem().getAddHp());
		}
		if (getItem().getAddMp() > 0) {
			if (equipped) {
				cha.setDynamicMp(cha.getDynamicMp() + getItem().getAddMp());
			} else {
				cha.setDynamicMp(cha.getDynamicMp() - getItem().getAddMp());
			}
		}
		if (getItem().getAddMr() > 0 || getDynamicMr() > 0) {
			if (equipped) {
				cha.setDynamicMr(cha.getDynamicMr() + getItem().getAddMr() + getDynamicMr());
			} else {
				cha.setDynamicMr(cha.getDynamicMr() - getItem().getAddMr() - getDynamicMr());
			}
		}
		if (getItem().getStunDefense() > 0 || getDynamicStunDefence() > 0) {
			if (equipped) {
				cha.setDynamicStunResist(Math.round(
						(cha.getDynamicStunResist() + getItem().getStunDefense() + getDynamicStunDefence()) * 100)
						/ 100.0);
			} else {
				cha.setDynamicStunResist(Math.round(
						(cha.getDynamicStunResist() - getItem().getStunDefense() - getDynamicStunDefence()) * 100)
						/ 100.0);
			}
		}
		if (getItem().getAddWeight() > 0) {
			if (equipped) {
				cha.setItemWeight(cha.getItemWeight() + getItem().getAddWeight());
			} else {
				cha.setItemWeight(cha.getItemWeight() - getItem().getAddWeight());
			}
		}
		if (getItem().getTicHp() > 0) {
			if (equipped) {
				cha.setDynamicTicHp(cha.getDynamicTicHp() + getItem().getTicHp());
			} else {
				cha.setDynamicTicHp(cha.getDynamicTicHp() - getItem().getTicHp());
			}
		}
		if (getItem().getTicMp() > 0) {
			if (equipped) {
				cha.setDynamicTicMp(cha.getDynamicTicMp() + getItem().getTicMp());
			} else {
				cha.setDynamicTicMp(cha.getDynamicTicMp() - getItem().getTicMp());
			}
		}
		if (getItem().getEarthress() > 0) {
			if (equipped) {
				cha.setDynamicEarthress(cha.getDynamicEarthress() + getItem().getEarthress());
			} else {
				cha.setDynamicEarthress(cha.getDynamicEarthress() - getItem().getEarthress());
			}
		}
		if (getItem().getFireress() > 0) {
			if (equipped) {
				cha.setDynamicFireress(cha.getDynamicFireress() + getItem().getFireress());
			} else {
				cha.setDynamicFireress(cha.getDynamicFireress() - getItem().getFireress());
			}
		}
		if (getItem().getWindress() > 0) {
			if (equipped) {
				cha.setDynamicWindress(cha.getDynamicWindress() + getItem().getWindress());
			} else {
				cha.setDynamicWindress(cha.getDynamicWindress() - getItem().getWindress());
			}
		}
		if (getItem().getWaterress() > 0) {
			if (equipped) {
				cha.setDynamicWaterress(cha.getDynamicWaterress() + getItem().getWaterress());
			} else {
				cha.setDynamicWaterress(cha.getDynamicWaterress() - getItem().getWaterress());
			}
		}
		if (getItem().getPolyName() != null && getItem().getPolyName().length() > 0) {
			Poly p = PolyDatabase.getPolyName(getItem().getPolyName());
			// 변신 상태가 아니거나 변신하려는 gfx 와 같을때만 처리.
			if (cha.getGfx() == cha.getClassGfx() || cha.getGfx() == p.getGfxId()) {
				if (equipped) {
					ShapeChange.onBuff(cha, cha, p, -1, false, sendPacket);
				} else {
					BuffController.remove(cha, ShapeChange.class);
				}
			}
		}
		if (getItem().getAddReduction() > 0 || getDynamicReduction() > 0) {
			if (equipped) {
				cha.setDynamicReduction(
						cha.getDynamicReduction() + getItem().getAddReduction() + getDynamicReduction());
			} else {
				cha.setDynamicReduction(
						cha.getDynamicReduction() - getItem().getAddReduction() - getDynamicReduction());
			}
		}

		if (getItem().getIgnoreReduction() > 0 || getDynamicIgnoreReduction() > 0) {
			if (equipped) {
				cha.setDynamicIgnoreReduction(
						cha.getDynamicIgnoreReduction() + getItem().getIgnoreReduction() + getDynamicIgnoreReduction());
			} else {
				cha.setDynamicIgnoreReduction(
						cha.getDynamicIgnoreReduction() - getItem().getIgnoreReduction() - getDynamicIgnoreReduction());
			}
		}

		if (getItem().getAddSp() > 0 || getDynamicSp() > 0) {
			if (equipped)
				cha.setDynamicSp(cha.getDynamicSp() + item.getAddSp() + getDynamicSp());
			else
				cha.setDynamicSp(cha.getDynamicSp() - item.getAddSp() - getDynamicSp());
		}

		if (getItem().getAddDmg() > 0) {
			if (equipped) {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() + item.getAddDmg());
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + item.getAddDmg());
			} else {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() - item.getAddDmg());
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - item.getAddDmg());
			}
		}

		if (getItem().getAddHit() > 0) {
			if (equipped) {
				cha.setDynamicAddHit(cha.getDynamicAddHit() + item.getAddHit());
				cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + item.getAddHit());
			} else {
				cha.setDynamicAddHit(cha.getDynamicAddHit() - item.getAddHit());
				cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - item.getAddHit());
			}
		}

		if (getItem().getAddMagicHit() > 0) {
			if (equipped)
				cha.setDynamicMagicHit(cha.getDynamicMagicHit() + item.getAddMagicHit());
			else
				cha.setDynamicMagicHit(cha.getDynamicMagicHit() - item.getAddMagicHit());
		}

		if (getItem().getAddCriticalSword() > 0 || getDynamicSwordCritical() > 0) {
			if (equipped)
				cha.setDynamicCritical(
						cha.getDynamicCritical() + item.getAddCriticalSword() + getDynamicSwordCritical());
			else
				cha.setDynamicCritical(
						cha.getDynamicCritical() - item.getAddCriticalSword() - getDynamicSwordCritical());
		}

		if (getItem().getAddCriticalBow() > 0 || getDynamicBowCritical() > 0) {
			if (equipped)
				cha.setDynamicBowCritical(
						cha.getDynamicBowCritical() + item.getAddCriticalBow() + getDynamicBowCritical());
			else
				cha.setDynamicBowCritical(
						cha.getDynamicBowCritical() - item.getAddCriticalBow() - getDynamicBowCritical());
		}

		if (getItem().getAddCriticalMagic() > 0 || getDynamicMagicCritical() > 0) {
			if (equipped)
				cha.setDynamicMagicCritical(
						cha.getDynamicMagicCritical() + item.getAddCriticalMagic() + getDynamicMagicCritical());
			else
				cha.setDynamicMagicCritical(
						cha.getDynamicMagicCritical() - item.getAddCriticalMagic() - getDynamicMagicCritical());
		}

		if (getItem().getStunHit() > 0 || getDynamicStunHit() > 0) {
			if (equipped)
				cha.setDynamicStunHit(
						Math.round((cha.getDynamicStunHit() + getItem().getStunHit() + getDynamicStunHit()) * 100)
								/ 100.0);
			else
				cha.setDynamicStunHit(
						Math.round((cha.getDynamicStunHit() - getItem().getStunHit() - getDynamicStunHit()) * 100)
								/ 100.0);
		}

		if (getItem().getPvpDamage() > 0 || getDynamicPvpDmg() > 0) {
			if (equipped)
				cha.setDynamicAddPvpDmg(cha.getDynamicAddPvpDmg() + getItem().getPvpDamage() + getDynamicPvpDmg());
			else
				cha.setDynamicAddPvpDmg(cha.getDynamicAddPvpDmg() - getItem().getPvpDamage() - getDynamicPvpDmg());
		}

		if (getItem().getPvpReduction() > 0 || getDynamicPvpReduction() > 0) {
			if (equipped)
				cha.setDynamicAddPvpReduction(
						cha.getDynamicAddPvpReduction() + getItem().getPvpReduction() + getDynamicPvpReduction());
			else
				cha.setDynamicAddPvpReduction(
						cha.getDynamicAddPvpReduction() - getItem().getPvpReduction() - getDynamicPvpReduction());
		}

		// 지팡이를 제외한 축무기 추가 대미지
		if ((getBless() == 0 || getBless() == -128) && getItem().getType1().equalsIgnoreCase("weapon")
				&& !getItem().getType2().equalsIgnoreCase("wand")) {
			if (equipped) {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 2);
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + 2);
			} else {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 2);
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - 2);
			}
		}

		// 축복 지팡이 추가 SP
		if ((getBless() == 0 || getBless() == -128) && getItem().getType2().equalsIgnoreCase("wand")) {
			if (equipped)
				cha.setDynamicSp(cha.getDynamicSp() + 1);
			else
				cha.setDynamicSp(cha.getDynamicSp() - 1);
		}

		// 축복 방어구 추가 HP
		if ((getBless() == 0 || getBless() == -128) && !isAcc() && getItem().getType1().equalsIgnoreCase("armor")) {
			if (equipped)
				cha.setDynamicHp(cha.getDynamicHp() + 10);
			else
				cha.setDynamicHp(cha.getDynamicHp() - 10);
		}

		// 축복 장신구 추가 대미지, 추가 명중
		if ((getBless() == 0 || getBless() == -128) && isAcc()) {
			if (equipped) {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 1);
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + 1);
				cha.setDynamicAddHit(cha.getDynamicAddHit() + 1);
				cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + 1);
			} else {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 1);
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - 1);
				cha.setDynamicAddHit(cha.getDynamicAddHit() - 1);
				cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - 1);
			}
		}

		if (getItem().getType2().equalsIgnoreCase("necklace") && getEnLevel() > 0) {
			int hp = 0;
			int hpPotion = 0;
			double stunResist = 0;

			switch (getEnLevel()) {
				case 1:
					hp += 10;
					break;
				case 2:
					hp += 20;
					break;
				case 3:
					hp += 30;
					break;
				case 4:
					hp += 40;
					break;
				case 5:
					hp += 50;
					hpPotion += 1;
					break;
				case 6:
					hp += 60;
					hpPotion += 2;
					break;
				case 7:
					hp += 70;
					hpPotion += 3;
					stunResist += 0.05;
					break;
				case 8:
					hp += 80;
					hpPotion += 4;
					stunResist += 0.10;
					break;
				case 9:
					hp += 90;
					hpPotion += 5;
					stunResist += 0.04;
					break;
				case 10:
					hp += 100;
					hpPotion += 6;
					stunResist += 0.05;
					break;
			}

			if (equipped) {
				cha.setDynamicHp(cha.getDynamicHp() + hp);
				cha.setDynamicHpPotion(cha.getDynamicHpPotion() + hpPotion);
				cha.setDynamicStunResist(cha.getDynamicStunResist() + stunResist);
			} else {
				cha.setDynamicHp(cha.getDynamicHp() - hp);
				cha.setDynamicHpPotion(cha.getDynamicHpPotion() - hpPotion);
				cha.setDynamicStunResist(cha.getDynamicStunResist() - stunResist);
			}
		}

		if (getItem().getType2().equalsIgnoreCase("ring") && getEnLevel() > 0) {
			int hp = 0;
			int addDmg = 0;
			int addPvpDmg = 0;
			int mr = 0;
			int sp = 0;

			switch (getEnLevel()) {
				case 1:
					hp += 10;
					break;
				case 2:
					hp += 20;
					break;
				case 3:
					hp += 30;
					break;
				case 4:
					hp += 40;
					break;
				case 5:
					hp += 50;
					addDmg += 1;
					sp += 1;
					break;
				case 6:
					hp += 60;
					addDmg += 2;
					sp += 2;
					break;
				case 7:
					hp += 70;
					addDmg += 3;
					addPvpDmg += 10;
					mr += 3;
					sp += 3;
					break;
				case 8:
					hp += 80;
					addDmg += 4;
					addPvpDmg += 30;
					mr += 5;
					sp += 4;
					break;
				case 9:
					hp += 90;
					addDmg += 5;
					mr += 7;
					sp += 3;
					break;
				case 10:
					hp += 100;
					addDmg += 6;
					mr += 8;
					sp += 4;
					break;
			}

			if (equipped) {
				cha.setDynamicHp(cha.getDynamicHp() + hp);
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() + addDmg);
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + addDmg);
				cha.setDynamicAddPvpDmg(cha.getDynamicAddPvpDmg() + addPvpDmg);
				cha.setDynamicMr(cha.getDynamicMr() + mr);
				cha.setDynamicSp(cha.getDynamicSp() + sp);
			} else {
				cha.setDynamicHp(cha.getDynamicHp() - hp);
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() - addDmg);
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - addDmg);
				cha.setDynamicAddPvpDmg(cha.getDynamicAddPvpDmg() - addPvpDmg);
				cha.setDynamicMr(cha.getDynamicMr() - mr);
				cha.setDynamicSp(cha.getDynamicSp() - sp);
			}
		}

		if (getItem().getType2().equalsIgnoreCase("belt")) {
			int mp = 0;
			int reduction = 0;
			int hp = 0;
			int pvpReduction = 0;

			switch (getEnLevel()) {
				case 1:
					mp += 5;
					hp += 5;
					break;
				case 2:
					mp += 10;
					hp += 10;
					break;
				case 3:
					mp += 15;
					hp += 15;
					break;
				case 4:
					mp += 20;
					hp += 20;
					break;
				case 5:
					mp += 25;
					hp += 25;
					reduction += 1;
					break;
				case 6:
					mp += 30;
					hp += 30;
					reduction += 2;
					break;
				case 7:
					mp += 35;
					hp += 35;
					reduction += 3;
					pvpReduction = 10;
					break;
				case 8:
					mp += 40;
					hp += 40;
					reduction += 4;
					pvpReduction = 30;
					break;
				case 9:
					mp += 30;
					reduction += 5;
					hp += 30;
					pvpReduction += 4;
					break;
				case 10:
					mp += 40;
					reduction += 6;
					hp += 40;
					pvpReduction += 5;
					break;
			}

			if (equipped) {
				cha.setDynamicMp(cha.getDynamicMp() + mp);
				cha.setDynamicReduction(cha.getDynamicReduction() + reduction);
				cha.setDynamicHp(cha.getDynamicHp() + hp);
				cha.setDynamicAddPvpReduction(cha.getDynamicAddPvpReduction() + pvpReduction);
			} else {
				cha.setDynamicMp(cha.getDynamicMp() - mp);
				cha.setDynamicReduction(cha.getDynamicReduction() - reduction);
				cha.setDynamicHp(cha.getDynamicHp() - hp);
				cha.setDynamicAddPvpReduction(cha.getDynamicAddPvpReduction() - pvpReduction);
			}
		}

		if (getItem().getName().equalsIgnoreCase("완력의 부츠") || getItem().getName().equalsIgnoreCase("민첩의 부츠")
				|| getItem().getName().equalsIgnoreCase("지식의 부츠")) {
			int addHp = 0;
			int reduction = 0;

			switch (getEnLevel()) {
				case 7:
					addHp += 10;
					break;
				case 8:
					addHp += 20;
					break;
				case 9:
					addHp += 30;
					reduction += 1;
					break;
				case 10:
					addHp += 40;
					reduction += 2;
					break;
			}

			if (equipped) {
				cha.setDynamicHp(cha.getDynamicHp() + addHp);
				cha.setDynamicReduction(cha.getDynamicReduction() + reduction);
			} else {
				cha.setDynamicHp(cha.getDynamicHp() - addHp);
				cha.setDynamicReduction(cha.getDynamicReduction() - reduction);
			}
		}

		// 고대 마물 시리즈 (장갑, 부츠, 망토)
		if (getItem().getName().startsWith("고대 마물의")) { // 이름 검사 간소화 (startsWith 사용 가능 시)
			// 위 startsWith가 안 먹히면 기존처럼 equalsIgnoreCase 3개 쓰시면 됩니다.
			// if (getItem().getName().equalsIgnoreCase("고대 마물의 장갑") || ... ) {

			int addPvpDmg = 0;
			int currentEnchant = getEnLevel(); // 현재 인첸트 수치

			// [공식 적용] 5인첸 이상일 때, (인첸트 - 4) 만큼 부여
			// 예: 5강 -> 1, 6강 -> 2 ... 10강 -> 6, 11강 -> 7 (자동 적용)
			if (currentEnchant >= 5) {
				addPvpDmg = (currentEnchant - 4) * 5;
			}

			// 적용할 수치가 있을 때만 실행
			if (addPvpDmg > 0 && cha instanceof PcInstance) {
				PcInstance pc = (PcInstance) cha;

				if (equipped) {
					// 착용 시 +
					pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() + addPvpDmg);
					// 확인용 멘트 (테스트 후 주석 처리하세요)
					ChattingController.toChatting(pc, "마물셋 효과: PVP추타+" + addPvpDmg, Lineage.CHATTING_MODE_MESSAGE);
				} else {
					// 해제 시 -
					pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() - addPvpDmg);
				}
			}
		}

		// 고대 암석 시리즈 (장갑, 부츠, 망토)
		if (getItem().getName().startsWith("고대 암석의")) { // 이름 검사 간소화 (startsWith 사용 가능 시)
			// 위 startsWith가 안 먹히면 기존처럼 equalsIgnoreCase 3개 쓰시면 됩니다.
			// if (getItem().getName().equalsIgnoreCase("고대 마물의 장갑") || ... ) {

			int pvpReduction = 0;
			int currentEnchant = getEnLevel(); // 현재 인첸트 수치

			// [공식 적용] 5인첸 이상일 때, (인첸트 - 4) 만큼 부여
			// 예: 5강 -> 1, 6강 -> 2 ... 10강 -> 6, 11강 -> 7 (자동 적용)
			if (currentEnchant >= 5) {
				pvpReduction = (currentEnchant - 4) * 5;
			}

			// 적용할 수치가 있을 때만 실행
			if (pvpReduction > 0 && cha instanceof PcInstance) {
				PcInstance pc = (PcInstance) cha;

				if (equipped) {
					// 착용 시 +
					pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() + pvpReduction);
					// 확인용 멘트 (테스트 후 주석 처리하세요)
					ChattingController.toChatting(pc, "암석셋 효과: PVP리덕션+" + pvpReduction, Lineage.CHATTING_MODE_MESSAGE);
				} else {
					// 해제 시 -
					pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() - pvpReduction);
				}
			}
		}

		/*
		 * if (getItem().getName().equalsIgnoreCase("수호성의 파워 글로브") ||
		 * getItem().getName().equalsIgnoreCase("수호성의 활 골무")
		 * || getItem().getName().equalsIgnoreCase("빛나는 마력의 장갑")) {
		 * int addHit = 0;
		 * 
		 * switch (getEnLevel()) {
		 * case 5:
		 * addHit += 1;
		 * break;
		 * case 6:
		 * addHit += 2;
		 * break;
		 * case 7:
		 * addHit += 3;
		 * break;
		 * case 8:
		 * addHit += 4;
		 * break;
		 * case 9:
		 * addHit += 5;
		 * break;
		 * case 10:
		 * addHit += 6;
		 * break;
		 * }
		 * 
		 * if (equipped) {
		 * if (getItem().getName().equalsIgnoreCase("수호성의 파워 글로브"))
		 * cha.setDynamicAddHit(cha.getDynamicAddHit() + addHit);
		 * else
		 * cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + addHit);
		 * } else {
		 * if (getItem().getName().equalsIgnoreCase("수호성의 파워 글로브"))
		 * cha.setDynamicAddHit(cha.getDynamicAddHit() - addHit);
		 * else
		 * cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - addHit);
		 * }
		 * 
		 * if (equipped) {
		 * if (getItem().getName().equalsIgnoreCase("수호성의 활 골무"))
		 * cha.setDynamicAddHit(cha.getDynamicAddHit() + addHit);
		 * else
		 * cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + addHit);
		 * } else {
		 * if (getItem().getName().equalsIgnoreCase("수호성의 활 골무"))
		 * cha.setDynamicAddHit(cha.getDynamicAddHit() - addHit);
		 * else
		 * cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - addHit);
		 * }
		 * 
		 * if (equipped) {
		 * if (getItem().getName().equalsIgnoreCase("빛나는 마력의 장갑"))
		 * cha.setDynamicAddHit(cha.getDynamicAddHit() + addHit);
		 * else
		 * cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + addHit);
		 * } else {
		 * if (getItem().getName().equalsIgnoreCase("빛나는 마력의 장갑"))
		 * cha.setDynamicAddHit(cha.getDynamicAddHit() - addHit);
		 * else
		 * cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - addHit);
		 * }
		 * }
		 */

		// [수정된 코드] 수호성/마력 장갑 추가 옵션 및 멘트 출력
		String itemName = getItem().getName();

		if (itemName.equalsIgnoreCase("수호성의 파워 글로브") || itemName.equalsIgnoreCase("수호성의 활 골무")
				|| itemName.equalsIgnoreCase("빛나는 마력의 장갑")) {

			int addHit = 0;

			// 인첸트 수치별 보너스 설정
			switch (getEnLevel()) {
				case 5:
					addHit = 1;
					break;
				case 6:
					addHit = 2;
					break;
				case 7:
					addHit = 3;
					break;
				case 8:
					addHit = 4;
					break;
				case 9:
					addHit = 5;
					break;
				case 10:
					addHit = 6;
					break;
			}

			// 보너스 수치가 0보다 클 때만 로직 수행
			if (addHit > 0) {

				// 1. 수호성의 파워 글로브 (근거리 명중)
				if (itemName.equalsIgnoreCase("수호성의 파워 글로브")) {
					if (equipped) {
						cha.setDynamicAddHit(cha.getDynamicAddHit() + addHit);
						ChattingController.toChatting(cha, String.format("\\fY%s: 근거리명중+%d", itemName, addHit),
								Lineage.CHATTING_MODE_MESSAGE);
					} else {
						cha.setDynamicAddHit(cha.getDynamicAddHit() - addHit);
					}
				}
				// 2. 수호성의 활 골무 (원거리 명중)
				else if (itemName.equalsIgnoreCase("수호성의 활 골무")) {
					if (equipped) {
						cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + addHit);
						ChattingController.toChatting(cha, String.format("\\fY%s: 원거리명중+%d", itemName, addHit),
								Lineage.CHATTING_MODE_MESSAGE);
					} else {
						cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - addHit);
					}
				}
				// 3. 빛나는 마력의 장갑 (근거리 명중으로 설정됨 - 필요시 수정 가능)
				else if (itemName.equalsIgnoreCase("빛나는 마력의 장갑")) {
					if (equipped) {
						cha.setDynamicMagicHit(cha.getDynamicMagicHit() + addHit);
						ChattingController.toChatting(cha, String.format("\\fY%s: 마법명중+%d", itemName, addHit),
								Lineage.CHATTING_MODE_MESSAGE);
					} else {
						cha.setDynamicMagicHit(cha.getDynamicMagicHit() - addHit);
					}
				}
			}
		}

		if (getItem().getName().equalsIgnoreCase("안타라스의 완력") || getItem().getName().equalsIgnoreCase("안타라스의 마력")
				|| getItem().getName().equalsIgnoreCase("안타라스의 인내력")
				|| getItem().getName().equalsIgnoreCase("안타라스의 예지력")) {
			int reduction = 0;

			switch (getEnLevel()) {
				case 7:
					reduction += 1;
					break;
				case 8:
					reduction += 2;
					break;
				case 9:
					reduction += 3;
					break;
				case 10:
					reduction += 4;
					break;
			}

			if (equipped) {
				cha.setAntarasArmor(true);
				cha.setDynamicReduction(cha.getDynamicReduction() + reduction);
			} else {
				cha.setAntarasArmor(false);
				cha.setDynamicReduction(cha.getDynamicReduction() - reduction);
			}
		}

		if (getItem().getName().equalsIgnoreCase("파푸리온의 완력") || getItem().getName().equalsIgnoreCase("파푸리온의 마력")
				|| getItem().getName().equalsIgnoreCase("파푸리온의 인내력")
				|| getItem().getName().equalsIgnoreCase("파푸리온의 예지력")) {
			if (equipped) {
				cha.setFafurionArmor(true);
				cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 2245));
			} else {
				cha.setFafurionArmor(false);
			}
		}

		if (getItem().getName().equalsIgnoreCase("린드비오르의 완력") || getItem().getName().equalsIgnoreCase("린드비오르의 마력")
				|| getItem().getName().equalsIgnoreCase("린드비오르의 인내력")
				|| getItem().getName().equalsIgnoreCase("린드비오르의 예지력")) {
			if (equipped)
				cha.setLindviorArmor(true);
			else
				cha.setLindviorArmor(false);
		}

		if (getItem().getName().equalsIgnoreCase("발라카스의 완력") || getItem().getName().equalsIgnoreCase("발라카스의 마력")
				|| getItem().getName().equalsIgnoreCase("발라카스의 인내력")
				|| getItem().getName().equalsIgnoreCase("발라카스의 예지력")) {
			int ignore_reduction = 0;
			int critical = getItem().getName().equalsIgnoreCase("발라카스의 완력") ? 3
					: getItem().getName().equalsIgnoreCase("발라카스의 예지력") ? 2 : 0;
			int criticalBow = getItem().getName().equalsIgnoreCase("발라카스의 인내력") ? 2 : 0;
			int criticalMagic = getItem().getName().equalsIgnoreCase("발라카스의 마력") ? 2 : 0;

			switch (getEnLevel()) {
				case 7:
					ignore_reduction += 1;
					critical += getItem().getName().equalsIgnoreCase("발라카스의 완력")
							|| getItem().getName().equalsIgnoreCase("발라카스의 예지력") ? 1 : 0;
					criticalBow += getItem().getName().equalsIgnoreCase("발라카스의 인내력") ? 1 : 0;
					criticalMagic += getItem().getName().equalsIgnoreCase("발라카스의 마력") ? 1 : 0;
					break;
				case 8:
					ignore_reduction += 2;
					critical += getItem().getName().equalsIgnoreCase("발라카스의 완력")
							|| getItem().getName().equalsIgnoreCase("발라카스의 예지력") ? 2 : 0;
					criticalBow += getItem().getName().equalsIgnoreCase("발라카스의 인내력") ? 2 : 0;
					criticalMagic += getItem().getName().equalsIgnoreCase("발라카스의 마력") ? 2 : 0;
					break;
				case 9:
					ignore_reduction += 3;
					critical += getItem().getName().equalsIgnoreCase("발라카스의 완력")
							|| getItem().getName().equalsIgnoreCase("발라카스의 예지력") ? 3 : 0;
					criticalBow += getItem().getName().equalsIgnoreCase("발라카스의 인내력") ? 3 : 0;
					criticalMagic += getItem().getName().equalsIgnoreCase("발라카스의 마력") ? 3 : 0;
					break;
				case 10:
					ignore_reduction += 3;
					critical += getItem().getName().equalsIgnoreCase("발라카스의 완력")
							|| getItem().getName().equalsIgnoreCase("발라카스의 예지력") ? 4 : 0;
					criticalBow += getItem().getName().equalsIgnoreCase("발라카스의 인내력") ? 4 : 0;
					criticalMagic += getItem().getName().equalsIgnoreCase("발라카스의 마력") ? 4 : 0;
					break;
			}

			if (equipped) {
				cha.setValakasArmor(true);
				cha.setDynamicIgnoreReduction(cha.getDynamicIgnoreReduction() + ignore_reduction);
				cha.setDynamicCritical(cha.getDynamicCritical() + critical);
				cha.setDynamicBowCritical(cha.getDynamicBowCritical() + criticalBow);
				cha.setDynamicMagicCritical(cha.getDynamicMagicCritical() + criticalMagic);
			} else {
				cha.setValakasArmor(false);
				cha.setDynamicIgnoreReduction(cha.getDynamicIgnoreReduction() - ignore_reduction);
				cha.setDynamicCritical(cha.getDynamicCritical() - critical);
				cha.setDynamicBowCritical(cha.getDynamicBowCritical() - criticalBow);
				cha.setDynamicMagicCritical(cha.getDynamicMagicCritical() - criticalMagic);
			}
		}

		if (getItem().getName().equalsIgnoreCase("고대 신의 창")) {
			int addDmg = 0;
			int critical = 3;

			if (getEnLevel() > 0) {
				addDmg = getEnLevel() * 2;
				critical = getEnLevel();
			}

			if (equipped) {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() + addDmg);
				cha.setDynamicCritical(cha.getDynamicCritical() + critical);

				if (addDmg > 0 || critical > 0)
					ChattingController.toChatting(cha,
							String.format("%s: 근거리 대미지+%d, 근거리 치명+%d%%", getItem().getName(), addDmg, critical),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() - addDmg);
				cha.setDynamicCritical(cha.getDynamicCritical() - critical);
			}
		}
		/*
		 * if (getItem().getName().equalsIgnoreCase("포르세의 검")) {
		 * int 근거리명중 = 10;
		 * 
		 * if (equipped) {
		 * cha.setDynamicAddHit(cha.getDynamicAddHit() + 근거리명중);
		 * 
		 * if (근거리명중 > 0)
		 * ChattingController.toChatting(cha, String.format("%s: 근거리 명중+%d",
		 * getItem().getName(), 근거리명중), Lineage.CHATTING_MODE_MESSAGE);
		 * } else {
		 * cha.setDynamicAddHit(cha.getDynamicAddHit() - 근거리명중);
		 * }
		 * }
		 */
		if (getItem().getName().equalsIgnoreCase("바람칼날의 단검")) {
			int addDmg = 0;
			int critical = 0;
			int 근거리명중 = 0;

			if (getEnLevel() > 0) {
				addDmg = getEnLevel() * 2;
				critical = getEnLevel();
				근거리명중 = getEnLevel();
			}

			if (equipped) {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() + addDmg);
				cha.setDynamicCritical(cha.getDynamicCritical() + critical);
				cha.setDynamicAddHit(cha.getDynamicAddHit() + 근거리명중);

				if (addDmg > 0 || critical > 0 || 근거리명중 > 0)
					ChattingController.toChatting(cha, String.format("%s: 근거리 대미지+%d, 근거리 치명+%d%% , 근거리 명중+%d",
							getItem().getName(), addDmg, critical, 근거리명중), Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() - addDmg);
				cha.setDynamicCritical(cha.getDynamicCritical() - critical);
				cha.setDynamicAddHit(cha.getDynamicAddHit() - 근거리명중);
			}
		}

		if (getItem().getName().equalsIgnoreCase("진명황의 집행검")) {
			int addDmg = 0;
			int critical = 0;
			double skillHit = 0;

			if (getEnLevel() > 0) {
				addDmg = getEnLevel() * 2;
				critical = getEnLevel();
				skillHit += getEnLevel();
			}

			if (equipped) {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() + addDmg);
				cha.setDynamicCritical(cha.getDynamicCritical() + critical);
				cha.setKnightSkillHit(cha.getKnightSkillHit() + skillHit);

				if (addDmg > 0 || critical > 0 || skillHit > 0)
					ChattingController.toChatting(cha, String.format("%s: 근거리데미지+%d, 근거리 치명+%d%%, 스턴 적중+%.0f%%",
							getItem().getName(), addDmg, critical, skillHit), Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() - addDmg);
				cha.setDynamicCritical(cha.getDynamicCritical() - critical);
				cha.setKnightSkillHit(cha.getKnightSkillHit() - skillHit);
			}
		}

		if (getItem().getName().equalsIgnoreCase("사신의 검")) {
			int addDmg = 0;
			int critical = 0;
			double skillHit = 0;

			if (getEnLevel() > 0) {
				addDmg = getEnLevel() * 2;
				critical = getEnLevel();
				skillHit += getEnLevel();
			}

			if (equipped) {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() + addDmg);
				cha.setDynamicCritical(cha.getDynamicCritical() + critical);
				cha.setKnightSkillHit(cha.getKnightSkillHit() + skillHit);

				if (addDmg > 0 || critical > 0 || skillHit > 0)
					ChattingController.toChatting(cha, String.format("%s: 근거리데미지+%d, 근거리 치명+%d%%, 스턴 적중+%.0f%%",
							getItem().getName(), addDmg, critical, skillHit), Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() - addDmg);
				cha.setDynamicCritical(cha.getDynamicCritical() - critical);
				cha.setKnightSkillHit(cha.getKnightSkillHit() - skillHit);
			}
		}

		if (getItem().getName().equalsIgnoreCase("붉은 그림자의 이도류")) {
			int addDmg = 0;
			int critical = 0;
			int 근거리명중 = 0;

			if (getEnLevel() > 0) {
				addDmg = getEnLevel() * 2;
				critical = getEnLevel();
				근거리명중 = getEnLevel();
			}

			if (equipped) {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() + addDmg);
				cha.setDynamicCritical(cha.getDynamicCritical() + critical);
				cha.setDynamicAddHit(cha.getDynamicAddHit() + 근거리명중);

				if (addDmg > 0 || critical > 0 || 근거리명중 > 0)
					ChattingController.toChatting(cha, String.format("%s: 근거리 대미지+%d, 근거리 치명+%d%% , 근거리 명중+%d",
							getItem().getName(), addDmg, critical, 근거리명중), Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() - addDmg);
				cha.setDynamicCritical(cha.getDynamicCritical() - critical);
				cha.setDynamicAddHit(cha.getDynamicAddHit() - 근거리명중);
			}
		}

		if (getItem().getName().equalsIgnoreCase("가이아의 격노")) {
			int addDmg = 0;
			int critical = 0;
			int ignoreReduction = 0;
			double skillHit = 2;

			if (getEnLevel() > 0) {
				addDmg = getEnLevel() * 2;
				critical = getEnLevel();
				ignoreReduction = getEnLevel();
				skillHit += getEnLevel();
			}

			if (equipped) {
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + addDmg);
				cha.setDynamicBowCritical(cha.getDynamicBowCritical() + critical);
				cha.setDynamicIgnoreReduction(cha.getDynamicIgnoreReduction() + ignoreReduction);
				cha.setElfSkillHit(cha.getElfSkillHit() + skillHit);

				if (addDmg > 0 || critical > 0 || ignoreReduction > 0 || skillHit > 0)
					ChattingController.toChatting(
							cha, String.format("%s: 원거리데미지+%d, 원거리 치명+%d%%, 리덕션 무시+%d, 정령 적중+%.0f%%",
									getItem().getName(), addDmg, critical, ignoreReduction, skillHit),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - addDmg);
				cha.setDynamicBowCritical(cha.getDynamicBowCritical() - critical);
				cha.setDynamicIgnoreReduction(cha.getDynamicIgnoreReduction() - ignoreReduction);
				cha.setElfSkillHit(cha.getElfSkillHit() - skillHit);
			}
		}

		if (getItem().getName().equalsIgnoreCase("수정 결정체 지팡이")) {
			int addDmg = 0;
			int sp = 0;
			int magicHit = 0;

			if (getEnLevel() > 0) {
				addDmg = getEnLevel() * 2;
				sp = getEnLevel();
				magicHit = getEnLevel();
			}

			if (equipped) {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() + addDmg);
				cha.setDynamicSp(cha.getDynamicSp() + sp);
				cha.setDynamicMagicHit(cha.getDynamicMagicHit() + magicHit);

				if (addDmg > 0 || sp > 0 || magicHit > 0)
					ChattingController.toChatting(cha, String.format("%s: 근거리데미지+%d, SP+%d, 마법 적중+%d%%",
							getItem().getName(), addDmg, sp, magicHit), Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() - addDmg);
				cha.setDynamicSp(cha.getDynamicSp() - sp);
				cha.setDynamicMagicHit(cha.getDynamicMagicHit() - magicHit);
			}
		}

		if (getItem().getName().equalsIgnoreCase("시어의 심안")) {
			int 마법적중 = 0;
			int sp = 0;
			int HP = 0;

			switch (getEnLevel()) {
				case 5:
					// 마법적중 = 1;
					// sp = 1;
					HP = 10;
					break;
				case 6:
					// 마법적중 = 2;
					// sp = 2;
					HP = 20;
					break;
				case 7:
					마법적중 = 1;
					sp = 1;
					HP = 30;
					break;
				case 8:
					마법적중 = 2;
					sp = 2;
					HP = 40;
					break;
				case 9:
					마법적중 = 3;
					sp = 3;
					HP = 50;
					break;
			}

			if (equipped) {
				cha.setDynamicMagicHit(cha.getDynamicMagicHit() + 마법적중);
				cha.setDynamicSp(cha.getDynamicSp() + sp);
				cha.setDynamicHp(cha.getDynamicHp() + HP);

				if (마법적중 > 0 || sp > 0 || HP > 0)
					ChattingController.toChatting(cha,
							String.format("%s: 마법 적중+%d%%, SP+%d, HP+%d", getItem().getName(), 마법적중, sp, HP),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicMagicHit(cha.getDynamicMagicHit() - 마법적중);
				cha.setDynamicSp(cha.getDynamicSp() - sp);
				cha.setDynamicHp(cha.getDynamicHp() - HP);
			}
		}

		if (getItem().getName().equalsIgnoreCase("리치 로브")) {
			int sp = 0;

			switch (getEnLevel()) {
				case 7:
					sp = 1;
					break;
				case 8:
					sp = 2;
					break;
				case 9:
					sp = 3;
					break;
				case 10:
					sp = 4;
					break;
			}

			if (equipped) {
				cha.setDynamicSp(cha.getDynamicSp() + sp);

				if (sp > 0)
					ChattingController.toChatting(cha, String.format("%s: SP+%d", getItem().getName(), sp),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicSp(cha.getDynamicSp() - sp);
			}
		}

		if (getItem().getName().equalsIgnoreCase("살천의 활") ||
				getItem().getName().equalsIgnoreCase("강철 마나의 지팡이") ||
				getItem().getName().equalsIgnoreCase("제로스의 지팡이") ||
				getItem().getName().equalsIgnoreCase("악몽의 장궁") ||
				getItem().getName().equalsIgnoreCase("달의 장궁") ||
				getItem().getName().equalsIgnoreCase("나이트발드의 양손검") ||
				getItem().getName().equalsIgnoreCase("제로스의 지팡이") ||
				getItem().getName().equalsIgnoreCase("포르세의 검") ||
				getItem().getName().equalsIgnoreCase("진 레이피어") ||
				getItem().getName().equalsIgnoreCase("파멸의 대검") ||
				getItem().getName().equalsIgnoreCase("흑왕도") ||
				getItem().getName().equalsIgnoreCase("테베 오시리스의 양손검") ||
				getItem().getName().equalsIgnoreCase("테베 오시리스의 이도류") ||
				getItem().getName().equalsIgnoreCase("테베 오시리스의 지팡이") ||
				getItem().getName().equalsIgnoreCase("테베 오시리스의 활") ||
				getItem().getName().equalsIgnoreCase("데스나이트의 불검") ||
				getItem().getName().equalsIgnoreCase("악마왕의 양손검") ||
				getItem().getName().equalsIgnoreCase("악마왕의 이도류") ||
				getItem().getName().equalsIgnoreCase("악마왕의 활") ||
				getItem().getName().equalsIgnoreCase("악마왕의 지팡이") ||
				getItem().getName().equalsIgnoreCase("악마왕의 한손검") ||
				getItem().getName().equalsIgnoreCase("흑왕궁") ||
				getItem().getName().equalsIgnoreCase("론드의 이도류") ||
				getItem().getName().equalsIgnoreCase("바포메트의 지팡이") ||
				getItem().getName().equalsIgnoreCase("얼음 여왕의 지팡이") ||
				getItem().getName().equalsIgnoreCase("커츠의 검")) {

			int 근거리명중 = 0;

			switch (getEnLevel()) {
				case 1:
					근거리명중 = 1;
					break;
				case 2:
					근거리명중 = 2;
					break;
				case 3:
					근거리명중 = 3;
					break;
				case 4:
					근거리명중 = 4;
					break;
				case 5:
					근거리명중 = 5;
					break;
				case 6:
					근거리명중 = 6;
					break;
				case 7:
					근거리명중 = 7;
					break;
				case 8:
					근거리명중 = 8;
					break;
				case 9:
					근거리명중 = 9;
					break;
				case 10:
					근거리명중 = 10;
					break;
			}

			if (equipped) {
				cha.setDynamicAddHit(cha.getDynamicAddHit() + 근거리명중);
				cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + 근거리명중);

				if (근거리명중 > 0)
					ChattingController.toChatting(cha, String.format("%s: 명중+%d", getItem().getName(), 근거리명중),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicAddHit(cha.getDynamicAddHit() - 근거리명중);
				cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - 근거리명중);
			}
		}

		/*
		 * if (getItem().getName().equalsIgnoreCase("악마왕의 양손검")) {
		 * int 근거리대미지 = 0;
		 * int 근거리명중 = 0;
		 * 
		 * switch (getEnLevel()) {
		 * case 1:
		 * 근거리대미지=1;
		 * 근거리명중 = 1;
		 * break;
		 * case 2:
		 * 근거리대미지=2;
		 * 근거리명중 = 2;
		 * break;
		 * case 3:
		 * 근거리대미지=3;
		 * 근거리명중 = 3;
		 * break;
		 * case 4:
		 * 근거리대미지=4;
		 * 근거리명중 = 4;
		 * break;
		 * case 5:
		 * 근거리대미지=5;
		 * 근거리명중 = 5;
		 * break;
		 * case 6:
		 * 근거리대미지=6;
		 * 근거리명중 = 6;
		 * break;
		 * case 7:
		 * 근거리대미지=7;
		 * 근거리명중 = 7;
		 * break;
		 * case 8:
		 * 근거리대미지=9;
		 * 근거리명중 = 8;
		 * break;
		 * case 9:
		 * 근거리대미지=11;
		 * 근거리명중 = 9;
		 * break;
		 * case 10:
		 * 근거리대미지=13;
		 * 근거리명중 = 10;
		 * break;
		 * case 11:
		 * 근거리대미지=15;
		 * 근거리명중 = 11;
		 * break;
		 * }
		 * 
		 * if (equipped) {
		 * cha.setDynamicAddHit(cha.getDynamicAddHit() + 근거리명중);
		 * cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 근거리대미지);
		 * 
		 * 
		 * if (근거리명중 > 0 || 근거리대미지 > 0 )
		 * ChattingController.toChatting(cha, String.format("%s: 근거리명중+%d, 근거리 대미지+%d",
		 * getItem().getName(), 근거리명중, 근거리대미지), Lineage.CHATTING_MODE_MESSAGE);
		 * } else {
		 * cha.setDynamicAddHit(cha.getDynamicAddHit() - 근거리명중);
		 * cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 근거리대미지);
		 * 
		 * }
		 * }
		 * if (getItem().getName().equalsIgnoreCase("악마왕의 한손검")) {
		 * int 근거리대미지 = 0;
		 * int 근거리명중 = 0;
		 * 
		 * switch (getEnLevel()) {
		 * case 1:
		 * 근거리대미지=1;
		 * 근거리명중 = 1;
		 * break;
		 * case 2:
		 * 근거리대미지=2;
		 * 근거리명중 = 2;
		 * break;
		 * case 3:
		 * 근거리대미지=3;
		 * 근거리명중 = 3;
		 * break;
		 * case 4:
		 * 근거리대미지=4;
		 * 근거리명중 = 4;
		 * break;
		 * case 5:
		 * 근거리대미지=5;
		 * 근거리명중 = 5;
		 * break;
		 * case 6:
		 * 근거리대미지=6;
		 * 근거리명중 = 6;
		 * break;
		 * case 7:
		 * 근거리대미지=7;
		 * 근거리명중 = 7;
		 * break;
		 * case 8:
		 * 근거리대미지=9;
		 * 근거리명중 = 8;
		 * break;
		 * case 9:
		 * 근거리대미지=11;
		 * 근거리명중 = 9;
		 * break;
		 * case 10:
		 * 근거리대미지=13;
		 * 근거리명중 = 10;
		 * break;
		 * case 11:
		 * 근거리대미지=15;
		 * 근거리명중 = 11;
		 * break;
		 * }
		 * 
		 * if (equipped) {
		 * cha.setDynamicAddHit(cha.getDynamicAddHit() + 근거리명중);
		 * cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 근거리대미지);
		 * 
		 * 
		 * if (근거리명중 > 0 || 근거리대미지 > 0 )
		 * ChattingController.toChatting(cha, String.format("%s: 근거리명중+%d, 근거리 대미지+%d",
		 * getItem().getName(), 근거리명중, 근거리대미지), Lineage.CHATTING_MODE_MESSAGE);
		 * } else {
		 * cha.setDynamicAddHit(cha.getDynamicAddHit() - 근거리명중);
		 * cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 근거리대미지);
		 * 
		 * }
		 * }
		 * if (getItem().getName().equalsIgnoreCase("악마왕의 단검")) {
		 * int 근거리대미지 = 0;
		 * int 근거리명중 = 0;
		 * 
		 * switch (getEnLevel()) {
		 * case 1:
		 * 근거리대미지=1;
		 * 근거리명중 = 1;
		 * break;
		 * case 2:
		 * 근거리대미지=2;
		 * 근거리명중 = 2;
		 * break;
		 * case 3:
		 * 근거리대미지=3;
		 * 근거리명중 = 3;
		 * break;
		 * case 4:
		 * 근거리대미지=4;
		 * 근거리명중 = 4;
		 * break;
		 * case 5:
		 * 근거리대미지=5;
		 * 근거리명중 = 5;
		 * break;
		 * case 6:
		 * 근거리대미지=6;
		 * 근거리명중 = 6;
		 * break;
		 * case 7:
		 * 근거리대미지=7;
		 * 근거리명중 = 7;
		 * break;
		 * case 8:
		 * 근거리대미지=9;
		 * 근거리명중 = 8;
		 * break;
		 * case 9:
		 * 근거리대미지=11;
		 * 근거리명중 = 9;
		 * break;
		 * case 10:
		 * 근거리대미지=13;
		 * 근거리명중 = 10;
		 * break;
		 * case 11:
		 * 근거리대미지=15;
		 * 근거리명중 = 11;
		 * break;
		 * }
		 * 
		 * if (equipped) {
		 * cha.setDynamicAddHit(cha.getDynamicAddHit() + 근거리명중);
		 * cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 근거리대미지);
		 * 
		 * 
		 * if (근거리명중 > 0 || 근거리대미지 > 0 )
		 * ChattingController.toChatting(cha, String.format("%s: 근거리명중+%d, 근거리 대미지+%d",
		 * getItem().getName(), 근거리명중, 근거리대미지), Lineage.CHATTING_MODE_MESSAGE);
		 * } else {
		 * cha.setDynamicAddHit(cha.getDynamicAddHit() - 근거리명중);
		 * cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 근거리대미지);
		 * 
		 * }
		 * }
		 * if (getItem().getName().equalsIgnoreCase("악마왕의 지팡이")) {
		 * int 근거리대미지 = 0;
		 * int 근거리명중 = 0;
		 * 
		 * switch (getEnLevel()) {
		 * case 1:
		 * 근거리대미지=1;
		 * 근거리명중 = 1;
		 * break;
		 * case 2:
		 * 근거리대미지=2;
		 * 근거리명중 = 2;
		 * break;
		 * case 3:
		 * 근거리대미지=3;
		 * 근거리명중 = 3;
		 * break;
		 * case 4:
		 * 근거리대미지=4;
		 * 근거리명중 = 4;
		 * break;
		 * case 5:
		 * 근거리대미지=5;
		 * 근거리명중 = 5;
		 * break;
		 * case 6:
		 * 근거리대미지=6;
		 * 근거리명중 = 6;
		 * break;
		 * case 7:
		 * 근거리대미지=7;
		 * 근거리명중 = 7;
		 * break;
		 * case 8:
		 * 근거리대미지=9;
		 * 근거리명중 = 8;
		 * break;
		 * case 9:
		 * 근거리대미지=11;
		 * 근거리명중 = 9;
		 * break;
		 * case 10:
		 * 근거리대미지=13;
		 * 근거리명중 = 10;
		 * break;
		 * case 11:
		 * 근거리대미지=15;
		 * 근거리명중 = 11;
		 * break;
		 * }
		 * 
		 * if (equipped) {
		 * cha.setDynamicAddHit(cha.getDynamicAddHit() + 근거리명중);
		 * cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 근거리대미지);
		 * 
		 * 
		 * if (근거리명중 > 0 || 근거리대미지 > 0 )
		 * ChattingController.toChatting(cha, String.format("%s: 근거리명중+%d, 근거리 대미지+%d",
		 * getItem().getName(), 근거리명중, 근거리대미지), Lineage.CHATTING_MODE_MESSAGE);
		 * } else {
		 * cha.setDynamicAddHit(cha.getDynamicAddHit() - 근거리명중);
		 * cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 근거리대미지);
		 * 
		 * }
		 * }
		 * if (getItem().getName().equalsIgnoreCase("악마왕의 활")) {
		 * int 원거리대미지 = 0;
		 * int 원거리명중 = 0;
		 * 
		 * switch (getEnLevel()) {
		 * case 1:
		 * 원거리대미지=1;
		 * 원거리명중 = 1;
		 * break;
		 * case 2:
		 * 원거리대미지=2;
		 * 원거리명중 = 2;
		 * break;
		 * case 3:
		 * 원거리대미지=3;
		 * 원거리명중 = 3;
		 * break;
		 * case 4:
		 * 원거리대미지=4;
		 * 원거리명중 = 4;
		 * break;
		 * case 5:
		 * 원거리대미지=5;
		 * 원거리명중 = 5;
		 * break;
		 * case 6:
		 * 원거리대미지=6;
		 * 원거리명중 = 6;
		 * break;
		 * case 7:
		 * 원거리대미지=7;
		 * 원거리명중 = 7;
		 * break;
		 * case 8:
		 * 원거리대미지=9;
		 * 원거리명중 = 8;
		 * break;
		 * case 9:
		 * 원거리대미지=11;
		 * 원거리명중 = 9;
		 * break;
		 * case 10:
		 * 원거리대미지=13;
		 * 원거리명중 = 10;
		 * break;
		 * case 11:
		 * 원거리대미지=15;
		 * 원거리명중 = 11;
		 * break;
		 * }
		 * 
		 * if (equipped) {
		 * cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + 원거리명중);
		 * cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + 원거리대미지);
		 * 
		 * 
		 * if (원거리명중 > 0 || 원거리대미지 > 0 )
		 * ChattingController.toChatting(cha, String.format("%s: 원거리 명중+%d, 원거리 대미지+%d",
		 * getItem().getName(), 원거리명중, 원거리대미지), Lineage.CHATTING_MODE_MESSAGE);
		 * } else {
		 * cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - 원거리명중);
		 * cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - 원거리대미지);
		 * 
		 * }
		 * }
		 */
		if (getItem().getName().equalsIgnoreCase("크로노스의 귀걸이")) {
			int 대미지감소 = 0;
			double 스턴내성 = 0;

			switch (getEnLevel()) {
				case 1:
					대미지감소 = 2;
					스턴내성 = 0.01;
					break;
				case 2:
					대미지감소 = 2;
					스턴내성 = 0.02;
					break;
				case 3:
					대미지감소 = 3;
					스턴내성 = 0.03;
					break;
				case 4:
					대미지감소 = 4;
					스턴내성 = 0.03;
					break;
				case 5:
					대미지감소 = 5;
					스턴내성 = 0.04;
					break;
				case 6:
					대미지감소 = 7;
					스턴내성 = 0.05;
					break;
				case 7:
					대미지감소 = 9;
					스턴내성 = 0.07;
					break;
				case 8:
					대미지감소 = 10;
					스턴내성 = 0.1;
					break;
				case 9:
					대미지감소 = 12;
					스턴내성 = 0.12;
					break;
			}

			if (equipped) {
				cha.setDynamicStunResist(cha.getDynamicStunResist() + 스턴내성);
				cha.setDynamicReduction(cha.getDynamicReduction() + 대미지감소);

				if (스턴내성 > 0 || 대미지감소 > 0)
					ChattingController.toChatting(cha,
							String.format("%s: 대미지 감소+%d,  스턴 내성+%.0f%% ", getItem().getName(), 대미지감소, (스턴내성 * 100)),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicStunResist(cha.getDynamicStunResist() - 스턴내성);
				cha.setDynamicReduction(cha.getDynamicReduction() - 대미지감소);
			}
		}

		if (getItem().getName().equalsIgnoreCase("크로노스의 갑옷")) {
			int 대미지감소 = 0;
			double 스턴내성 = 0;
			int hp = 0;

			switch (getEnLevel()) {
				case 1:
				case 2:
				case 3:
				case 4:
					대미지감소 = 5;
					스턴내성 = 0.03;
					hp = 100;
					break;
				case 5:
					대미지감소 = 7;
					스턴내성 = 0.05;
					hp = 150;
					break;
				case 6:
					대미지감소 = 10;
					스턴내성 = 0.07;
					hp = 200;
					break;
				case 7:
					대미지감소 = 12;
					스턴내성 = 0.09;
					hp = 250;
					break;
				case 8:
					대미지감소 = 15;
					스턴내성 = 0.1;
					hp = 300;
					break;
				case 9:
					대미지감소 = 20;
					스턴내성 = 0.12;
					hp = 400;
					break;
			}

			if (equipped) {
				cha.setDynamicStunResist(cha.getDynamicStunResist() + 스턴내성);
				cha.setDynamicReduction(cha.getDynamicReduction() + 대미지감소);
				cha.setDynamicHp(cha.getDynamicHp() + hp);

				if (스턴내성 > 0 || 대미지감소 > 0)
					ChattingController.toChatting(cha, String.format("%s: hp + %d, 대미지 감소+%d,  스턴 내성+%.0f%% ",
							getItem().getName(), hp, 대미지감소, (스턴내성 * 100)), Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicStunResist(cha.getDynamicStunResist() - 스턴내성);
				cha.setDynamicReduction(cha.getDynamicReduction() - 대미지감소);
				cha.setDynamicHp(cha.getDynamicHp() - hp);
			}
		}

		if (getItem().getName().equalsIgnoreCase("붉은빛 크로노스 목걸이")) {

			int critical = 0;
			int ignoreReduction = 0;

			if (getEnLevel() > 0) {
				critical = getEnLevel();
				ignoreReduction = getEnLevel();
			}

			if (equipped) {
				cha.setDynamicBowCritical(cha.getDynamicBowCritical() + critical);
				cha.setDynamicIgnoreReduction(cha.getDynamicIgnoreReduction() + ignoreReduction);
				cha.setDynamicCritical(cha.getDynamicCritical() + critical);

				if (critical > 0 || ignoreReduction > 0)
					ChattingController.toChatting(cha, String.format("%s: 근/원거리 치명+%d%%, 리덕션 무시+%d",
							getItem().getName(), critical, ignoreReduction), Lineage.CHATTING_MODE_MESSAGE);

			} else {
				cha.setDynamicCritical(cha.getDynamicCritical() - critical);
				cha.setDynamicBowCritical(cha.getDynamicBowCritical() - critical);
				cha.setDynamicIgnoreReduction(cha.getDynamicIgnoreReduction() - ignoreReduction);
			}

		}
		if (getItem().getName().equalsIgnoreCase("푸른빛 크로노스 목걸이")) {

			int magicHit = 0;
			int magiccri = 0;

			if (getEnLevel() > 0) {
				magicHit = getEnLevel();
				magiccri = getEnLevel();
			}

			if (equipped) {
				cha.setDynamicMagicHit(cha.getDynamicMagicHit() + magicHit);
				cha.setDynamicMagicCritical(cha.getDynamicMagicCritical() + magiccri);

				if (magicHit > 0 || magiccri > 0)
					ChattingController.toChatting(cha,
							String.format("%s: 마법 치명+%d%%, 마법적중+%d", getItem().getName(), magiccri, magicHit),
							Lineage.CHATTING_MODE_MESSAGE);

			} else {
				cha.setDynamicMagicHit(cha.getDynamicMagicHit() - magicHit);
				cha.setDynamicMagicCritical(cha.getDynamicMagicCritical() - magiccri);

			}

		}
		if (getItem().getName().equalsIgnoreCase("녹색빛 크로노스 목걸이")) {
			int 대미지감소 = 0;
			double 스턴내성 = 0;

			switch (getEnLevel()) {
				case 1:
					대미지감소 = 2;
					스턴내성 = 0.01;
					break;
				case 2:
					대미지감소 = 2;
					스턴내성 = 0.02;
					break;
				case 3:
					대미지감소 = 3;
					스턴내성 = 0.03;
					break;
				case 4:
					대미지감소 = 4;
					스턴내성 = 0.03;
					break;
				case 5:
					대미지감소 = 5;
					스턴내성 = 0.04;
					break;
				case 6:
					대미지감소 = 7;
					스턴내성 = 0.05;
					break;
				case 7:
					대미지감소 = 9;
					스턴내성 = 0.07;
					break;
				case 8:
					대미지감소 = 10;
					스턴내성 = 0.1;
					break;
				case 9:
					대미지감소 = 12;
					스턴내성 = 0.12;
					break;
			}

			if (equipped) {
				cha.setDynamicStunResist(cha.getDynamicStunResist() + 스턴내성);
				cha.setDynamicReduction(cha.getDynamicReduction() + 대미지감소);

				if (스턴내성 > 0 || 대미지감소 > 0)
					ChattingController.toChatting(cha,
							String.format("%s: 대미지 감소+%d,  스턴 내성+%.0f%% ", getItem().getName(), 대미지감소, (스턴내성 * 100)),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicStunResist(cha.getDynamicStunResist() - 스턴내성);
				cha.setDynamicReduction(cha.getDynamicReduction() - 대미지감소);
			}
		}
		if (getItem().getName().equalsIgnoreCase("지령의 가더")) {
			int mr = 0;
			int 대미지감소 = 0;

			switch (getEnLevel()) {
				case 1:
					대미지감소 = 0;
					break;
				case 2:
					대미지감소 = 0;
					break;
				case 3:
					mr = 0;
					대미지감소 = 0;
					break;
				case 4:
					mr = 0;
					대미지감소 = 0;
					break;
				case 5:
					mr = 5;
					대미지감소 = 1;
					break;
				case 6:
					mr = 10;
					대미지감소 = 2;
					break;
				case 7:
					mr = 15;
					대미지감소 = 3;
					break;
				case 8:
					mr = 20;
					대미지감소 = 4;
					break;
				case 9:
					mr = 25;
					대미지감소 = 5;
					break;
			}

			if (equipped) {
				cha.setDynamicMr(cha.getDynamicMr() + mr);
				cha.setDynamicReduction(cha.getDynamicReduction() + 대미지감소);

				if (mr > 0 || 대미지감소 > 0)
					ChattingController.toChatting(cha,
							String.format("%s: MR+%d, 대미지 감소+%d", getItem().getName(), mr, 대미지감소),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicMr(cha.getDynamicMr() - mr);
				cha.setDynamicReduction(cha.getDynamicReduction() - 대미지감소);
			}
		}

		if (getItem().getName().equalsIgnoreCase("화령의 가더")) {
			int 근거리명중 = 0;
			int 근거리대미지 = 0;
			int 스턴내성 = 0;

			switch (getEnLevel()) {
				case 5:
					근거리명중 = 1;
					// 근거리대미지 = 1;
					스턴내성 = 5;
					break;
				case 6:
					근거리명중 = 2;
					// 근거리대미지 = 2;
					스턴내성 = 10;
					break;
				case 7:
					근거리명중 = 3;
					// 근거리대미지 = 1;
					스턴내성 = 15;
					break;
				case 8:
					근거리명중 = 4;
					// 근거리대미지 = 2;
					스턴내성 = 20;
					break;
				case 9:
					근거리명중 = 5;
					// 근거리대미지 = 3;
					스턴내성 = 25;
					break;
			}

			if (equipped) {
				cha.setDynamicAddHit(cha.getDynamicAddHit() + 근거리명중);
				// cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 근거리대미지);
				cha.setDynamicHp(cha.getDynamicHp() + 스턴내성);
				// cha.setDynamicStunResist(cha.getDynamicStunResist() + 스턴내성);

				// if (근거리대미지 > 0 || 근거리명중 > 0 || 스턴내성 > 0)
				// ChattingController.toChatting(cha, String.format("%s: 근거리대미지+%d, 근거리명중+%d,
				// HP+%d", getItem().getName(), 근거리명중, 스턴내성), Lineage.CHATTING_MODE_MESSAGE);
				if (근거리명중 > 0 || 스턴내성 > 0)
					ChattingController.toChatting(cha,
							String.format("%s: 근거리명중+%d, HP+%d", getItem().getName(), 근거리명중, 스턴내성),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicAddHit(cha.getDynamicAddHit() - 근거리명중);
				// cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 근거리대미지);
				cha.setDynamicHp(cha.getDynamicHp() - 스턴내성);
				// cha.setDynamicStunResist(cha.getDynamicStunResist() - 스턴내성);
			}
		}

		if (getItem().getName().equalsIgnoreCase("쿠거의 가더")) {
			int 근거리명중 = 0;
			int 근거리대미지 = 0;
			int 스턴내성 = 0;

			switch (getEnLevel()) {
				case 5:
					근거리명중 = 1;
					근거리대미지 = 1;
					스턴내성 = 10;
					break;
				case 6:
					근거리명중 = 2;
					근거리대미지 = 2;
					스턴내성 = 20;
					break;
				case 7:
					근거리명중 = 3;
					근거리대미지 = 3;
					스턴내성 = 30;
					break;
				case 8:
					근거리명중 = 4;
					근거리대미지 = 4;
					스턴내성 = 40;
					break;
				case 9:
					근거리명중 = 5;
					근거리대미지 = 5;
					스턴내성 = 50;
					break;
			}

			if (equipped) {
				cha.setDynamicAddHit(cha.getDynamicAddHit() + 근거리명중);
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 근거리대미지);
				cha.setDynamicHp(cha.getDynamicHp() + 스턴내성);
				// cha.setDynamicStunResist(cha.getDynamicStunResist() + 스턴내성);

				if (근거리명중 > 0 || 근거리대미지 > 0 || 스턴내성 > 0)
					ChattingController.toChatting(cha,
							String.format("%s: 근거리명중+%d, 근거리 대미지+%d, HP+%d", getItem().getName(), 근거리명중, 근거리대미지, 스턴내성),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicAddHit(cha.getDynamicAddHit() - 근거리명중);
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 근거리대미지);
				cha.setDynamicHp(cha.getDynamicHp() - 스턴내성);
				// cha.setDynamicStunResist(cha.getDynamicStunResist() - 스턴내성);
			}
		}

		if (getItem().getName().equalsIgnoreCase("풍령의 가더")) {
			int 원거리명중 = 0;
			int 원거리대미지 = 0;
			int HP = 0;

			switch (getEnLevel()) {
				case 5:
					원거리명중 = 1;
					// 원거리대미지 = 1;
					HP = 5;
					break;
				case 6:
					원거리명중 = 2;
					// 원거리대미지 = 2;
					HP = 10;
					break;
				case 7:
					원거리명중 = 3;
					// 원거리대미지 = 1;
					HP = 15;
					break;
				case 8:
					원거리명중 = 4;
					// 원거리대미지 = 2;
					HP = 20;
					break;
				case 9:
					원거리명중 = 5;
					// 원거리대미지 = 3;
					HP = 30;
					break;
			}

			if (equipped) {
				cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + 원거리명중);
				// cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + 원거리대미지);
				cha.setDynamicHp(cha.getDynamicHp() + HP);

				// if (원거리명중 > 0 || 원거리대미지 > 0 || HP > 0)
				// ChattingController.toChatting(cha, String.format("%s: 원거리 명중+%d, 원거리 대미지+%d,
				// HP+%d", getItem().getName(), 원거리명중, 원거리대미지, HP),
				// Lineage.CHATTING_MODE_MESSAGE);
				if (원거리명중 > 0 || HP > 0)
					ChattingController.toChatting(cha,
							String.format("%s: 원거리 명중+%d, HP+%d", getItem().getName(), 원거리명중, HP),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - 원거리명중);
				// cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - 원거리대미지);
				cha.setDynamicHp(cha.getDynamicHp() - HP);
			}
		}

		if (getItem().getName().equalsIgnoreCase("우그누스의 가더")) {
			int 원거리명중 = 0;
			int 원거리대미지 = 0;
			int HP = 0;

			switch (getEnLevel()) {
				case 3:
				case 4:
				case 5:
					원거리명중 = 1;
					원거리대미지 = 1;
					HP = 10;
					break;
				case 6:
					원거리명중 = 2;
					원거리대미지 = 2;
					HP = 20;
					break;
				case 7:
					원거리명중 = 3;
					원거리대미지 = 3;
					HP = 30;
					break;
				case 8:
					원거리명중 = 4;
					원거리대미지 = 4;
					HP = 40;
					break;
				case 9:
					원거리명중 = 5;
					원거리대미지 = 5;
					HP = 50;
					break;
			}

			if (equipped) {
				cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + 원거리명중);
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + 원거리대미지);
				cha.setDynamicHp(cha.getDynamicHp() + HP);

				if (원거리명중 > 0 || 원거리대미지 > 0 || HP > 0)
					ChattingController.toChatting(cha,
							String.format("%s: 원거리 명중+%d, 원거리 대미지+%d, HP+%d", getItem().getName(), 원거리명중, 원거리대미지, HP),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - 원거리명중);
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - 원거리대미지);
				cha.setDynamicHp(cha.getDynamicHp() - HP);
			}
		}

		if (getItem().getName().equalsIgnoreCase("수령의 가더")) {
			int 마법적중 = 0;
			int sp = 0;
			int HP = 0;

			switch (getEnLevel()) {
				case 5:
					마법적중 = 1;
					// sp = 1;
					HP = 5;
					break;
				case 6:
					마법적중 = 2;
					// sp = 2;
					HP = 10;
					break;
				case 7:
					마법적중 = 3;
					// sp = 3;
					HP = 15;
					break;
				case 8:
					마법적중 = 4;
					// sp = 4;
					HP = 20;
					break;
				case 9:
					마법적중 = 5;
					// sp = 5;
					HP = 30;
					break;
			}

			if (equipped) {
				cha.setDynamicMagicHit(cha.getDynamicMagicHit() + 마법적중);
				// cha.setDynamicSp(cha.getDynamicSp() + sp);
				cha.setDynamicHp(cha.getDynamicHp() + HP);

				// if (마법적중 > 0 || sp > 0 || HP > 0)
				// ChattingController.toChatting(cha, String.format("%s: 마법 적중+%d%%, SP+%d,
				// HP+%d", getItem().getName(), 마법적중, sp, HP), Lineage.CHATTING_MODE_MESSAGE);
				if (마법적중 > 0 || HP > 0)
					ChattingController.toChatting(cha,
							String.format("%s: 마법 적중+%d%%, HP+%d", getItem().getName(), 마법적중, HP),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicMagicHit(cha.getDynamicMagicHit() - 마법적중);
				// cha.setDynamicSp(cha.getDynamicSp() - sp);
				cha.setDynamicHp(cha.getDynamicHp() - HP);
			}
		}

		if (getItem().getName().equalsIgnoreCase("고대 투사의 가더")) {
			int 근거리명중 = 0;

			switch (getEnLevel()) {
				case 1:
					근거리명중 = 0;
					break;
				case 2:
					근거리명중 = 0;
					break;
				case 3:
					근거리명중 = 0;
					break;
				case 4:
					근거리명중 = 0;
					break;
				case 5:
					근거리명중 = 1;
					break;
				case 6:
					근거리명중 = 2;
					break;
				case 7:
					근거리명중 = 3;
					break;
				case 8:
					근거리명중 = 4;
					break;
				case 9:
					근거리명중 = 9;
					break;
			}

			if (equipped) {
				cha.setDynamicAddHit(cha.getDynamicAddHit() + 근거리명중);

				if (근거리명중 > 0)
					ChattingController.toChatting(cha, String.format("%s: 근거리명중+%d", getItem().getName(), 근거리명중),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicAddHit(cha.getDynamicAddHit() - 근거리명중);
			}
		}

		if (getItem().getName().equalsIgnoreCase("고대 명궁의 가더")) {
			int 원거리명중 = 0;

			switch (getEnLevel()) {
				case 1:
					원거리명중 = 0;
					break;
				case 2:
					원거리명중 = 0;
					break;
				case 3:
					원거리명중 = 0;
					break;
				case 4:
					원거리명중 = 0;
					break;
				case 5:
					원거리명중 = 1;
					break;
				case 6:
					원거리명중 = 2;
					break;
				case 7:
					원거리명중 = 3;
					break;
				case 8:
					원거리명중 = 4;
					break;
				case 9:
					원거리명중 = 5;
					break;
			}

			if (equipped) {
				cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + 원거리명중);
				if (원거리명중 > 0)
					ChattingController.toChatting(cha, String.format("%s: 원거리 명중+%d", getItem().getName(), 원거리명중),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - 원거리명중);
			}
		}

		if (getItem().getName().equalsIgnoreCase("마법사의 가더")) {
			int 마법적중 = 0;

			switch (getEnLevel()) {
				case 1:
					마법적중 = 0;
					break;
				case 2:
					마법적중 = 0;
					break;
				case 3:
					마법적중 = 0;
					break;
				case 4:
					마법적중 = 0;
					break;
				case 5:
					마법적중 = 1;
					break;
				case 6:
					마법적중 = 2;
					break;
				case 7:
					마법적중 = 3;
					break;
				case 8:
					마법적중 = 4;
					break;
				case 9:
					마법적중 = 5;
					break;
			}

			if (equipped) {
				cha.setDynamicMagicHit(cha.getDynamicMagicHit() + 마법적중);

				if (마법적중 > 0)
					ChattingController.toChatting(cha, String.format("%s: 마법 적중+%d%%", getItem().getName(), 마법적중),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicMagicHit(cha.getDynamicMagicHit() - 마법적중);
			}
		}

		// if (getItem().getName().contains("가더")) {
		// int 추타 = getEnLevel() - 4;
		// if(getEnLevel() >= 5) {
		// if (equipped) {
		// cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 추타);
		// cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + 추타);
		//
		// if (추타 > 0)
		// ChattingController.toChatting(cha, String.format("%s: 추타+%d",
		// getItem().getName(), 추타), Lineage.CHATTING_MODE_MESSAGE);
		// } else {
		// cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 추타);
		// cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - 추타);
		// }
		// }
		// }
		/*
		 * if (getItem().getName().equalsIgnoreCase("노예의 귀걸이") ||
		 * getItem().getName().equalsIgnoreCase("화령의 귀걸이") ||
		 * getItem().getName().equalsIgnoreCase("수령의 귀걸이") ||
		 * getItem().getName().equalsIgnoreCase("풍령의 귀걸이")) {
		 * int 추타 = getEnLevel() - 4;
		 * int 명중 = getEnLevel() - 4;
		 * if(getEnLevel() >= 5) {
		 * if (equipped) {
		 * cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 추타);
		 * cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + 추타);
		 * cha.setDynamicAddHit(cha.getDynamicAddHit() + 명중);
		 * cha.setDynamicAddHitBow(cha.getDynamicAddDmg() + 명중);
		 * 
		 * if (추타 > 0 || 명중 > 0)
		 * ChattingController.toChatting(cha, String.format("%s: 추타+%d, 명중+%d",
		 * getItem().getName(), 추타, 명중), Lineage.CHATTING_MODE_MESSAGE);
		 * } else {
		 * cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 추타);
		 * cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - 추타);
		 * cha.setDynamicAddHit(cha.getDynamicAddHit() - 명중);
		 * cha.setDynamicAddHitBow(cha.getDynamicAddDmg() - 명중);
		 * }
		 * }
		 * }
		 */

		if (getItem().getName().equalsIgnoreCase("노예의 귀걸이")) {
			if (getEnLevel() >= 5) {
				int value = getEnLevel() - 4;
				if (equipped) {
					cha.setDynamicAddHit(cha.getDynamicAddHit() + value);
					cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + value);
					cha.setDynamicMagicHit(cha.getDynamicMagicHit() + value);
					ChattingController.toChatting(cha,
							String.format("%s: 공격성공+%d, 원거리성공+%d, 마법명중+%d", getItem().getName(), value, value, value),
							Lineage.CHATTING_MODE_MESSAGE);
				} else {
					cha.setDynamicAddHit(cha.getDynamicAddHit() - value);
					cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - value);
					cha.setDynamicMagicHit(cha.getDynamicMagicHit() - value);
				}
			}
		} else if (getItem().getName().equalsIgnoreCase("화령의 귀걸이")) {
			if (getEnLevel() >= 5) {
				int value = getEnLevel() - 4;
				if (equipped) {
					cha.setDynamicAddDmg(cha.getDynamicAddDmg() + value);
					cha.setDynamicAddHit(cha.getDynamicAddHit() + value);
					ChattingController.toChatting(cha,
							String.format("%s: 근거리데미지+%d, 공격성공+%d", getItem().getName(), value, value),
							Lineage.CHATTING_MODE_MESSAGE);
				} else {
					cha.setDynamicAddDmg(cha.getDynamicAddDmg() - value);
					cha.setDynamicAddHit(cha.getDynamicAddHit() - value);
				}
			}
		} else if (getItem().getName().equalsIgnoreCase("풍령의 귀걸이")) {
			if (getEnLevel() >= 5) {
				int value = getEnLevel() - 4;
				if (equipped) {
					cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + value);
					cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + value);
					ChattingController.toChatting(cha,
							String.format("%s: 원거리공격력+%d, 원거리공격성공+%d", getItem().getName(), value, value),
							Lineage.CHATTING_MODE_MESSAGE);
				} else {
					cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - value);
					cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - value);
				}
			}
		} else if (getItem().getName().equalsIgnoreCase("수령의 귀걸이")) {
			if (getEnLevel() >= 5) {
				int value = getEnLevel() - 4;
				if (equipped) {
					cha.setDynamicSp(cha.getDynamicSp() + value);
					cha.setDynamicMagicHit(cha.getDynamicMagicHit() + value);
					ChattingController.toChatting(cha,
							String.format("%s: SP+%d, 마법명중+%d", getItem().getName(), value, value),
							Lineage.CHATTING_MODE_MESSAGE);
				} else {
					cha.setDynamicSp(cha.getDynamicSp() - value);
					cha.setDynamicMagicHit(cha.getDynamicMagicHit() - value);
				}
			}
		} else if (getItem().getName().equalsIgnoreCase("지령의 귀걸이")) {
			if (getEnLevel() >= 5) {
				int dr = getEnLevel() - 4;
				int hp = (getEnLevel() - 4) * 10;
				if (equipped) {
					cha.setDynamicReduction(cha.getDynamicReduction() + dr);
					cha.setDynamicHp(cha.getDynamicHp() + hp);
					ChattingController.toChatting(cha,
							String.format("%s: 데미지리덕션+%d, 체력+%d", getItem().getName(), dr, hp),
							Lineage.CHATTING_MODE_MESSAGE);
				} else {
					cha.setDynamicReduction(cha.getDynamicReduction() - dr);
					cha.setDynamicHp(cha.getDynamicHp() - hp);
				}
			}
		}

		if (getItem().getName().equalsIgnoreCase("룸티스의검은빛귀걸이")) {
			int 추타 = getEnLevel() - 2;
			if (getEnLevel() >= 3) {
				if (equipped) {
					cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 추타);
					// cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + 추타);

					if (추타 > 0)
						ChattingController.toChatting(cha, String.format("%s: 추타+%d", getItem().getName(), 추타),
								Lineage.CHATTING_MODE_MESSAGE);
				} else {
					cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 추타);
					// cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - 추타);
				}
			}
		}

		if (getItem().getName().equalsIgnoreCase("룸티스의붉은빛귀걸이")) {
			int 체력 = (getEnLevel() - 2) * 10;
			int 리덕 = getEnLevel() - 2;
			if (getEnLevel() >= 3) {
				if (equipped) {
					cha.setDynamicHp(cha.getDynamicHp() + 체력);
					cha.setDynamicReduction(cha.getDynamicReduction() + 리덕);

					if (체력 > 0 || 리덕 > 0)
						ChattingController.toChatting(cha,
								String.format("%s: 체력+%d, 리덕+%d", getItem().getName(), 체력, 리덕),
								Lineage.CHATTING_MODE_MESSAGE);
				} else {
					cha.setDynamicHp(cha.getDynamicHp() - 체력);
					cha.setDynamicReduction(cha.getDynamicReduction() - 리덕);
				}
			}
		}

		if (getItem().getName().equalsIgnoreCase("룸티스의보라빛귀걸이")) {
			int 마나 = (getEnLevel() - 2) * 10;
			int sp = getEnLevel() - 2;
			if (getEnLevel() >= 3) {
				if (equipped) {
					cha.setDynamicMp(cha.getDynamicMp() + 마나);
					cha.setDynamicSp(cha.getDynamicSp() + sp);

					if (마나 > 0 || sp > 0)
						ChattingController.toChatting(cha,
								String.format("%s: 마나+%d, SP+%d", getItem().getName(), 마나, sp),
								Lineage.CHATTING_MODE_MESSAGE);
				} else {
					cha.setDynamicHp(cha.getDynamicHp() - 마나);
					cha.setDynamicSp(cha.getDynamicSp() - sp);
				}
			}
		}
		// 야도란 아이템추가
		if (getItem().getName().equalsIgnoreCase("머미로드의 왕관")) {
			int addDmg = 0;

			switch (getEnLevel()) {
				case 5:
					// addDmg = 1;
					break;
				case 6:
					// addDmg = 2;
					break;
				case 7:
					addDmg = 1;
					break;
				case 8:
					addDmg = 2;
					break;
				case 9:
					addDmg = 3;
					break;
				case 10:
					addDmg = 6;
					break;
			}

			if (equipped) {
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + addDmg);

				if (addDmg > 0)
					ChattingController.toChatting(cha, String.format("%s: 원거리 대미지+%d", getItem().getName(), addDmg),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - addDmg);
			}
		}
		if (getItem().getName().equalsIgnoreCase("대마법사의 모자")) {
			int sp = 0;

			switch (getEnLevel()) {

				case 5:
					// sp = 1;
					break;
				case 6:
					// sp = 2;
					break;
				case 7:
					sp = 1;
					break;
				case 8:
					sp = 2;
					break;
				case 9:
					sp = 3;
					break;
				case 10:
					sp = 7;
					break;
			}

			if (equipped) {
				cha.setDynamicSp(cha.getDynamicSp() + sp);

				if (sp > 0)
					ChattingController.toChatting(cha, String.format("%s: sp+%d", getItem().getName(), sp),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicSp(cha.getDynamicSp() - sp);
			}
		}
		if (getItem().getName().equalsIgnoreCase("지휘관의 투구")) {
			int 대미지감소 = 0;

			switch (getEnLevel()) {
				case 5:
					// 대미지감소 = 1;
					break;
				case 6:
					// 대미지감소 = 2;
					break;
				case 7:
					대미지감소 = 1;
					break;
				case 8:
					대미지감소 = 2;
					break;
				case 9:
					대미지감소 = 3;
					break;
				case 10:
					대미지감소 = 6;
					break;

			}

			if (equipped) {
				cha.setDynamicReduction(cha.getDynamicReduction() + 대미지감소);

				if (대미지감소 > 0)
					ChattingController.toChatting(cha, String.format("%s: 대미지감소 +%d", getItem().getName(), 대미지감소),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicReduction(cha.getDynamicReduction() - 대미지감소);
			}
		}

		if (getItem().getName().equalsIgnoreCase("빛나는 민첩의 티셔츠")) {
			int 원거리명중 = 0;
			int 원거리대미지 = 0;
			int mr = 0;
			switch (getEnLevel()) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
					원거리명중 = 1;
					원거리대미지 = 1;
					mr = 5;
					break;
				case 8:
					원거리명중 = 2;
					원거리대미지 = 2;
					mr = 10;
					break;
				case 9:
					원거리명중 = 3;
					원거리대미지 = 3;
					mr = 15;
					break;
				case 10:
					원거리명중 = 7;
					원거리대미지 = 7;
					mr = 10;
					break;
			}

			if (equipped) {
				cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + 원거리명중);
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + 원거리대미지);
				cha.setDynamicMr(cha.getDynamicMr() + mr);

				if (원거리명중 > 0 || 원거리대미지 > 0 || mr > 0)
					ChattingController.toChatting(cha,
							String.format("%s: 원거리명중+%d , 원거리 대미지+%d , mr+%d", getItem().getName(), 원거리명중, 원거리대미지, mr),
							Lineage.CHATTING_MODE_MESSAGE);

			} else {
				cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - 원거리명중);
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - 원거리대미지);
				cha.setDynamicMr(cha.getDynamicMr() - mr);

			}
		}
		if (getItem().getName().equalsIgnoreCase("빛나는 완력의 티셔츠")) {
			int 근거리명중 = 0;
			int 근거리대미지 = 0;
			int mr = 0;
			switch (getEnLevel()) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
					근거리명중 = 1;
					근거리대미지 = 1;
					mr = 5;
					break;
				case 8:
					근거리명중 = 2;
					근거리대미지 = 2;
					mr = 10;
					break;
				case 9:
					근거리명중 = 3;
					근거리대미지 = 3;
					mr = 15;
					break;
				case 10:
					근거리명중 = 7;
					근거리대미지 = 7;
					mr = 10;
					break;
			}

			if (equipped) {
				cha.setDynamicAddHit(cha.getDynamicAddHit() + 근거리명중);
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 근거리대미지);
				cha.setDynamicMr(cha.getDynamicMr() + mr);

				if (근거리명중 > 0 || 근거리대미지 > 0 || mr > 0)
					ChattingController.toChatting(cha,
							String.format("%s: 근거리명중+%d , 근거리대미지+%d , mr+%d", getItem().getName(), 근거리명중, 근거리대미지, mr),
							Lineage.CHATTING_MODE_MESSAGE);

			} else {
				cha.setDynamicAddHit(cha.getDynamicAddHit() - 근거리명중);
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 근거리대미지);
				cha.setDynamicMr(cha.getDynamicMr() - mr);

			}
		}
		if (getItem().getName().equalsIgnoreCase("빛나는 지식의 티셔츠")) {
			int sp = 0;
			int 마법적중 = 0;
			int mr = 0;
			switch (getEnLevel()) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
					sp = 1;
					마법적중 = 1;
					mr = 5;
					break;

				case 8:
					sp = 2;
					마법적중 = 2;
					mr = 10;
					break;
				case 9:
					sp = 3;
					마법적중 = 3;
					mr = 15;
					break;
				case 10:
					sp = 7;
					마법적중 = 7;
					mr = 10;
					break;
			}

			if (equipped) {
				cha.setDynamicSp(cha.getDynamicSp() + sp);
				cha.setDynamicMagicHit(cha.getDynamicMagicHit() + 마법적중);
				cha.setDynamicMr(cha.getDynamicMr() + mr);

				if (sp > 0 || 마법적중 > 0 || mr > 0)
					ChattingController.toChatting(cha,
							String.format("%s: sp+%d , 마법적중+%d , mr+%d", getItem().getName(), sp, 마법적중, mr),
							Lineage.CHATTING_MODE_MESSAGE);

			} else {
				cha.setDynamicSp(cha.getDynamicSp() - sp);
				cha.setDynamicMagicHit(cha.getDynamicMagicHit() - 마법적중);
				cha.setDynamicMr(cha.getDynamicMr() - mr);

			}
		}

		if (getItem().getName().equalsIgnoreCase("격분의 장갑")) {
			int 대미지감소 = 0;
			int 근거리데미지 = 0;

			switch (getEnLevel()) {
				case 5:
					// 근거리데미지 = 1;
					// 대미지감소 = 1;
					break;
				case 6:
					// 근거리데미지 = 2;
					// 대미지감소 = 2;
					break;
				case 7:
					근거리데미지 = 1;
					대미지감소 = 1;
					break;
				case 8:
					근거리데미지 = 2;
					대미지감소 = 2;
					break;
				case 9:
					근거리데미지 = 3;
					대미지감소 = 3;
					break;
				case 10:
					근거리데미지 = 6;
					대미지감소 = 6;
					break;
			}

			if (equipped) {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 근거리데미지);
				cha.setDynamicReduction(cha.getDynamicReduction() + 대미지감소);
				if (대미지감소 > 0 || 근거리데미지 > 0)
					ChattingController.toChatting(cha,
							String.format("%s:  근거리데미지+%d, 대미지감소+%d", getItem().getName(), 근거리데미지, 대미지감소),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 근거리데미지);
				cha.setDynamicReduction(cha.getDynamicReduction() - 대미지감소);
			}
		}
		if (getItem().getName().equalsIgnoreCase("아이리스의 장갑")) {
			int 대미지감소 = 0;
			int 원거리데미지 = 0;

			switch (getEnLevel()) {
				case 5:
					// 원거리데미지 = 1;
					// 대미지감소 = 1;
					break;
				case 6:
					// 원거리데미지 = 2;
					// 대미지감소 = 2;
					break;
				case 7:
					원거리데미지 = 1;
					대미지감소 = 1;
					break;
				case 8:
					원거리데미지 = 2;
					대미지감소 = 2;
					break;
				case 9:
					원거리데미지 = 3;
					대미지감소 = 3;
					break;
				case 10:
					원거리데미지 = 6;
					대미지감소 = 6;
					break;
			}
			if (equipped) {
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + 원거리데미지);
				cha.setDynamicReduction(cha.getDynamicReduction() + 대미지감소);
				if (대미지감소 > 0 || 원거리데미지 > 0)
					ChattingController.toChatting(cha,
							String.format("%s:  원거리데미지+%d, 대미지감소+%d", getItem().getName(), 원거리데미지, 대미지감소),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - 원거리데미지);
				cha.setDynamicReduction(cha.getDynamicReduction() - 대미지감소);
			}
		}
		if (getItem().getName().equalsIgnoreCase("대마법사의 장갑")) {
			int 대미지감소 = 0;
			int sp = 0;

			switch (getEnLevel()) {
				case 5:
					// sp = 1;
					// 대미지감소 = 1;
					break;
				case 6:
					// sp = 2;
					// 대미지감소 = 2;
					break;
				case 7:
					sp = 1;
					대미지감소 = 1;
					break;
				case 8:
					sp = 2;
					대미지감소 = 2;
					break;
				case 9:
					sp = 3;
					대미지감소 = 3;
					break;
				case 10:
					sp = 7;
					대미지감소 = 7;
					break;
			}
			if (equipped) {
				cha.setDynamicSp(cha.getDynamicSp() + sp);
				cha.setDynamicReduction(cha.getDynamicReduction() + 대미지감소);
				if (대미지감소 > 0 || sp > 0)
					ChattingController.toChatting(cha,
							String.format("%s:  sp+%d, 대미지감소+%d", getItem().getName(), sp, 대미지감소),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicSp(cha.getDynamicSp() - sp);
				cha.setDynamicReduction(cha.getDynamicReduction() - 대미지감소);
			}
		}
		if (getItem().getName().equalsIgnoreCase("가디언의 망토")) {

			int mr = 0;
			switch (getEnLevel()) {
				case 0:
					mr = 0;
					break;
				case 1:
					mr = 5;
					break;
				case 2:
					mr = 10;
					break;
				case 3:
					mr = 15;
					break;
				case 4:
					mr = 20;
					break;
				case 5:
					mr = 25;
					break;
				case 6:
					mr = 30;
					break;
				case 7:
					mr = 35;
					break;
				case 8:
					mr = 40;
					break;
				case 9:
					mr = 45;
					break;
			}
			if (equipped) {
				cha.setDynamicMr(cha.getDynamicMr() + mr);
				if (mr > 0)
					ChattingController.toChatting(cha, String.format("%s: MR+%d", getItem().getName(), mr),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicMr(cha.getDynamicMr() - mr);
			}
		}
		if (getItem().getName().equalsIgnoreCase("크로노스의 망토")) {

			int mr = 0;
			int hpPotion = 0;

			switch (getEnLevel()) {
				case 0:
					mr = 0;
					break;
				case 1:
				case 2:
				case 3:
				case 4:
					mr = 25;
					hpPotion = 2;
					break;
				case 5:
					hpPotion = 4;
					mr = 30;
					break;
				case 6:
					hpPotion = 6;
					mr = 35;
					break;
				case 7:
					hpPotion = 10;
					mr = 40;
					break;
				case 8:
					hpPotion = 12;
					mr = 45;
					break;
				case 9:
					hpPotion = 15;
					mr = 55;
					break;
			}
			if (equipped) {
				cha.setDynamicMr(cha.getDynamicMr() + mr);
				cha.setDynamicHpPotion(cha.getDynamicHpPotion() + hpPotion);
				if (mr > 0)
					ChattingController.toChatting(cha,
							String.format("%s: MR+%d ,물약 회복량+%d ", getItem().getName(), mr, hpPotion),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicMr(cha.getDynamicMr() - mr);
				cha.setDynamicHpPotion(cha.getDynamicHpPotion() - hpPotion);
			}
		}
		if (getItem().getName().equalsIgnoreCase("뱀파이어의 망토")) {

			int mr = 0;
			switch (getEnLevel()) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
					// mr = 10;
					break;
				case 6:
					// mr = 15;
					break;
				case 7:
					// mr = 20;
					break;
				case 8:
					// mr = 25;
					break;
				case 9:
					// mr = 30;
					break;
			}
			if (equipped) {
				cha.setDynamicMr(cha.getDynamicMr() + mr);
				if (mr > 0)
					ChattingController.toChatting(cha, String.format("%s: MR+%d", getItem().getName(), mr),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicMr(cha.getDynamicMr() - mr);
			}
		}
		if (getItem().getName().equalsIgnoreCase("쿠거가죽 망토")) {

			int mr = 0;
			switch (getEnLevel()) {
				case 0:
					mr = 0;
					break;
				case 1:
					mr = 5;
					break;
				case 2:
					mr = 10;
					break;
				case 3:
					mr = 15;
					break;
				case 4:
					mr = 20;
					break;
				case 5:
					mr = 25;
					break;
				case 6:
					mr = 30;
					break;
				case 7:
					mr = 35;
					break;
				case 8:
					mr = 40;
					break;
				case 9:
					mr = 45;
					break;
			}
			if (equipped) {
				cha.setDynamicMr(cha.getDynamicMr() + mr);
				if (mr > 0)
					ChattingController.toChatting(cha, String.format("%s: MR+%d", getItem().getName(), mr),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicMr(cha.getDynamicMr() - mr);
			}
		}
		if (getItem().getName().equalsIgnoreCase("가디언의 부츠")) {
			int 대미지감소 = 0;

			switch (getEnLevel()) {
				case 5:
					대미지감소 = 1;
					break;
				case 6:
					대미지감소 = 2;
					break;
				case 7:
					대미지감소 = 3;
					break;
				case 8:
					대미지감소 = 4;
					break;
				case 9:
					대미지감소 = 5;
					break;
				case 10:
					대미지감소 = 6;
					break;
			}

			if (equipped) {
				cha.setDynamicReduction(cha.getDynamicReduction() + 대미지감소);
				if (대미지감소 > 0)
					ChattingController.toChatting(cha, String.format("%s: 대미지감소+%d", getItem().getName(), 대미지감소),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicReduction(cha.getDynamicReduction() - 대미지감소);
			}
		}
		if (getItem().getName().equalsIgnoreCase("뱀파이어의 부츠")) {
			int 대미지감소 = 0;

			switch (getEnLevel()) {
				case 5:
					// 대미지감소 = 1;
					break;
				case 6:
					// 대미지감소 = 2;
					break;
				case 7:
					대미지감소 = 1;
					break;
				case 8:
					대미지감소 = 2;
					break;
				case 9:
					대미지감소 = 3;
					break;
				case 10:
					대미지감소 = 4;
					break;
			}

			if (equipped) {
				cha.setDynamicReduction(cha.getDynamicReduction() + 대미지감소);
				if (대미지감소 > 0)
					ChattingController.toChatting(cha, String.format("%s: 대미지감소+%d", getItem().getName(), 대미지감소),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicReduction(cha.getDynamicReduction() - 대미지감소);
			}
		}
		if (getItem().getName().equalsIgnoreCase("나이트발드의 부츠")) {
			int 대미지감소 = 0;
			// int ignoreReduction = 8;

			switch (getEnLevel()) {
				case 1:
					// 대미지감소 = 2;
					break;
				case 2:
					// 대미지감소 = 3;
					break;
				case 3:
					// 대미지감소 = 4;
					break;
				case 4:
					// 대미지감소 = 5;
					break;
				case 5:
					// 대미지감소 = 6;
					break;
				case 6:
					// 대미지감소 = 7;
					break;
				case 7:
					대미지감소 = 1;
					break;
				case 8:
					대미지감소 = 2;
					break;
				case 9:
					대미지감소 = 3;
					break;
			}

			if (equipped) {

				cha.setDynamicReduction(cha.getDynamicReduction() + 대미지감소);
				// cha.setDynamicIgnoreReduction(cha.getDynamicIgnoreReduction() +
				// ignoreReduction);
				if (대미지감소 > 0)
					ChattingController.toChatting(cha,
							// String.format("%s: 대미지감소+%d , 리덕션무시 +%d", getItem().getName(), 대미지감소,
							// ignoreReduction),
							String.format("%s: 대미지감소+%d", getItem().getName(), 대미지감소),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				// cha.setDynamicIgnoreReduction(cha.getDynamicIgnoreReduction() -
				// ignoreReduction);
				cha.setDynamicReduction(cha.getDynamicReduction() - 대미지감소);
			}
		}
		if (getItem().getName().equalsIgnoreCase("아이리스의 부츠")) {
			int 대미지감소 = 0;

			switch (getEnLevel()) {
				case 5:
					// 대미지감소 = 1;
					break;
				case 6:
					// 대미지감소 = 2;
					break;
				case 7:
					대미지감소 = 1;
					break;
				case 8:
					대미지감소 = 2;
					break;
				case 9:
					대미지감소 = 3;
					break;
				case 10:
					대미지감소 = 6;
					break;
			}

			if (equipped) {
				cha.setDynamicReduction(cha.getDynamicReduction() + 대미지감소);
				if (대미지감소 > 0)
					ChattingController.toChatting(cha, String.format("%s: 대미지감소+%d", getItem().getName(), 대미지감소),
							Lineage.CHATTING_MODE_MESSAGE);
			} else {
				cha.setDynamicReduction(cha.getDynamicReduction() - 대미지감소);
			}
		}

		if (sendPacket && cha instanceof PcInstance) {
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
			cha.toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), cha));
		}
	}

	/**
	 * 셋트아이템 착용여부 확인하여 옵션 적용및 해제처리하는 함수. : 인벤토리와 연동하여 적용된 세트가 있는지 확인. 있는데 현재 착용된
	 * 아이템에서 없을경우 옵션 해제. 없는데 전체 셋트착용중일경우 옵션 적용.
	 */
	public void toSetoption(Character cha, boolean sendPacket) {
		Inventory inv = cha.getInventory();
		if (inv != null && item.getSetId() > 0) {
			ItemSetoption is = ItemSetoptionDatabase.find(item.getSetId());
			if (is != null) {
				if (equipped) {
					// 적용된 셋트가 없다면.
					if (!inv.isSetoption(is)) {
						// 셋트아이템 갯수 이상일 경우에만 적용.
						int cnt = 1; // 해당 아이템이 착용될 것이기때문에 초기값을 1
						for (int i = 0; i <= Lineage.SLOT_NONE; ++i) {
							ItemInstance slot = inv.getSlot(i);
							if (slot != null && slot.getItem().getSetId() == is.getUid())
								cnt += 1;
						}
						if (is.getCount() <= cnt) {
							inv.appendSetoption(is);
							ItemSetoptionDatabase.setting(cha, is, equipped, sendPacket);
						}
					}
				} else {
					// 적용된 셋트가 있다면
					if (inv.isSetoption(is)) {
						// 셋트아이템 갯수 미만일경우에만 해제.
						int cnt = 0;
						for (int i = 0; i <= Lineage.SLOT_NONE; ++i) {
							ItemInstance slot = inv.getSlot(i);
							if (slot != null && slot.getItem().getSetId() == is.getUid())
								cnt += 1;
						}
						if (is.getCount() >= cnt) {
							inv.removeSetoption(is);
							ItemSetoptionDatabase.setting(cha, is, equipped, sendPacket);
						}
					}
				}
			}
		}
	}

	/**
	 * 아이템을 사용해도 되는 상태인지 확인해주는 함수.
	 * 아이템 사용시 버그 체크
	 * 
	 * @param cha
	 * @return
	 */
	protected boolean isClick(Character cha) {
		if (this != null && getItem() != null && cha != null && cha instanceof PcInstance && cha.getInventory() != null
				&& !cha.isWorldDelete()) {
			return true;
		}

		if (cha.isBuffDecayPotion())
			return false;

		return true;
	}

	/**
	 * 아이템 사용해도 되는 상황인지 확인
	 * 2020-12-01
	 * by connector12@nate.com
	 */
	public boolean isClickState(object o) {
		if (this == null || getItem() == null || o == null || o.getInventory() == null || o.isWorldDelete()) {
			return false;
		}

		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;

			if (pc.isDead()) {
				ChattingController.toChatting(pc, "\\fY죽은 상태에서 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return false;
			}

			if (pc.isLock()) {
				ChattingController.toChatting(pc, "\\fY기절하거나 굳은 상태에서 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return false;
			}

			if (pc.isFishing()) {
				ChattingController.toChatting(pc, "\\fY낚시중에 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return false;
			}

			return true;
		}

		return false;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha instanceof PcInstance)
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 330, toString()));
	}

	@Override
	public String toString() {
		StringBuffer msg = new StringBuffer();
		if (definite && (this instanceof ItemWeaponInstance || this instanceof ItemArmorInstance)) {
			if (enLevel >= 0)
				msg.append("+");
			msg.append(enLevel);
			msg.append(" ");
		}
		msg.append(name);
		if (getCount() > 1) {
			msg.append(" (");
			msg.append(getCount());
			msg.append(")");
		}

		return msg.toString();
	}

	public String toStringDB() {
		StringBuffer msg = new StringBuffer();
		if (definite && (this instanceof ItemWeaponInstance || this instanceof ItemArmorInstance)) {
			if (enLevel >= 0)
				msg.append("+");
			msg.append(enLevel);
			msg.append(" ");
		}
		msg.append(item.getName());
		if (getCount() > 1) {
			msg.append(" (");
			msg.append(getCount());
			msg.append(")");
		}
		return msg.toString();
	}

	public String toStringSearch() {
		StringBuffer msg = new StringBuffer();

		if (definite) {
			if (getBless() < 0)
				msg.append("[봉인] ");
			if (getBless() == 0)
				msg.append("[축복받은] ");
			if (getBless() == 2)
				msg.append("[저주받은] ");
		}

		if (definite && (this instanceof ItemWeaponInstance || this instanceof ItemArmorInstance)) {
			if (getEnFire() > 0)
				msg.append("속성: " + getEnFire() + "단 ");
			if (enLevel >= 0)
				msg.append("+");

			msg.append(enLevel);
			msg.append(" ");
		}

		msg.append(item.getName());

		if (getCount() > 1) {
			msg.append(" (");
			msg.append(getCount());
			msg.append(")");
		}
		return msg.toString();
	}

	@Override
	public Skill getSkill() {
		return null;
	}

	@Override
	public void setTime(int time) {
	}

	@Override
	public int getTime() {
		return nowTime;
	}

	@Override
	public void setCharacter(Character cha) {
	}

	@Override
	public boolean isBuff(long time) {
		return --nowTime > 0;
	}

	@Override
	public void toBuffStart(object o) {
	}

	@Override
	public void toBuffUpdate(object o) {
	}

	@Override
	public void toBuff(object o) {
	}

	@Override
	public void toBuffStop(object o) {
	}

	@Override
	public void toBuffEnd(object o) {
	}

	@Override
	public boolean equal(BuffInterface bi) {
		return false;
	}

	@Override
	public void setTime(int time, boolean restart) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEffect(BackgroundInstance effect) {
		// TODO Auto-generated method stub

	}

	@Override
	public BackgroundInstance getEffect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDamage(int damage) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getDamage() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 장신구 확인.
	 * 2020-09-04
	 * by connector12@nate.com
	 */
	public boolean isAcc() {
		if (this != null && this instanceof ItemArmorInstance && getItem() != null) {
			if (getItem().getType2().equalsIgnoreCase("necklace") ||
					getItem().getType2().equalsIgnoreCase("belt") ||
					getItem().getType2().equalsIgnoreCase("ring") ||
					getItem().getType2().equalsIgnoreCase("earring")) {
				return true;
			}
		}

		return false;
	}
}
