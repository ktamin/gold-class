package lineage.world.object.instance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import Fx.server.MJTemplate.MJProto.ClientSetting;
import Fx.server.MJTemplate.MJProto.MJEProtoMessages;
import Fx.server.MJTemplate.MJProto.MJIProtoMessage;
import Fx.server.MJTemplate.MJProto.ProtoOutputStream;
import Fx.server.MJTemplate.MJProto.Models.SC_AUTO_SUPPORT_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import Fx.server.UIBoard.UIBoardService;
import all_night.Lineage_Balance;
import lineage.bean.database.Exp;
import lineage.bean.database.Item;
import lineage.bean.database.PcTrade;
import lineage.bean.database.Poly;
import lineage.bean.database.Shop;
import lineage.bean.database.Skill;
import lineage.bean.database.marketPrice;
import lineage.bean.lineage.Agit;
import lineage.bean.lineage.Clan;
import lineage.bean.lineage.Inventory;
import lineage.bean.lineage.Kingdom;
import lineage.bean.lineage.Summon;
import lineage.bean.lineage.Swap;
import lineage.database.AccountDatabase;
import lineage.database.BackgroundDatabase;
import lineage.database.CharactersDatabase;
import lineage.database.DatabaseConnection;
import lineage.database.ExpDatabase;
import lineage.database.HackNoCheckDatabase;
import lineage.database.ItemDatabase;
import lineage.database.NpcSpawnlistDatabase;
import lineage.database.PolyDatabase;
import lineage.database.ServerDatabase;
import lineage.database.SkillDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.database.TeleportHomeDatabase;
import lineage.database.TeleportResetDatabase;
import lineage.database.TimeDungeonDatabase;
import lineage.gui.GuiMain;
import lineage.network.LineageClient;
import lineage.network.LineageServer;
import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.ServerBasePacket;
import lineage.network.packet.server.S_Ability;
import lineage.network.packet.server.S_CharacterExp;
import lineage.network.packet.server.S_CharacterHp;
import lineage.network.packet.server.S_CharacterMp;
import lineage.network.packet.server.S_CharacterSpMr;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_ClanWar;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_InterfaceRead;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.network.packet.server.S_ObjectAttack;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectCriminal;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectHeading;
import lineage.network.packet.server.S_ObjectHitratio;
import lineage.network.packet.server.S_ObjectInvis;
import lineage.network.packet.server.S_ObjectLawful;
import lineage.network.packet.server.S_ObjectLight;
import lineage.network.packet.server.S_ObjectLock;
import lineage.network.packet.server.S_ObjectMoving;
import lineage.network.packet.server.S_ObjectRevival;
import lineage.network.packet.server.S_Potal;
import lineage.network.packet.server.S_Weather;
import lineage.plugin.PluginController;
import lineage.share.Common;
import lineage.share.Lineage;
import lineage.share.Log;
import lineage.share.System;
import lineage.util.Util;
import lineage.world.AStar;
import lineage.world.Node;
import lineage.world.World;
import lineage.world.controller.AgitController;
import lineage.world.controller.AutoHuntCheckController;
import lineage.world.controller.BaphometSystemController;
import lineage.world.controller.BookController;
import lineage.world.controller.BuffController;
import lineage.world.controller.CharacterController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.ClanController;
import lineage.world.controller.CommandController;
import lineage.world.controller.DamageController;
import lineage.world.controller.FishingController;
import lineage.world.controller.FriendController;
import lineage.world.controller.InventoryController;
import lineage.world.controller.KingdomController;
import lineage.world.controller.LetterController;
import lineage.world.controller.MagicDollController;
import lineage.world.controller.PartyController;
import lineage.world.controller.QuestController;
import lineage.world.controller.RankController;
import lineage.world.controller.SkillController;
import lineage.world.controller.SummonController;
import lineage.world.controller.TeamBattleController;
import lineage.world.controller.TradeController;
import lineage.world.controller.UserShopController;
import lineage.world.controller.WantedController;
import lineage.world.controller.고라스컨트롤러;
import lineage.world.controller.고무컨트롤러;
import lineage.world.controller.그신컨트롤러;
import lineage.world.controller.기감컨트롤러;
import lineage.world.controller.드워프컨트롤러;
import lineage.world.controller.라바던전컨트롤러;
import lineage.world.controller.마족신전컨트롤러;
import lineage.world.controller.보물찾기컨트롤러;
import lineage.world.controller.악마왕의영토컨트롤러;
import lineage.world.controller.얼던컨트롤러;
import lineage.world.controller.오만10층컨트롤러;
import lineage.world.controller.오만1층컨트롤러;
import lineage.world.controller.오만2층컨트롤러;
import lineage.world.controller.오만3층컨트롤러;
import lineage.world.controller.오만4층컨트롤러;
import lineage.world.controller.오만5층컨트롤러;
import lineage.world.controller.오만6층컨트롤러;
import lineage.world.controller.오만7층컨트롤러;
import lineage.world.controller.오만8층컨트롤러;
import lineage.world.controller.오만9층컨트롤러;
import lineage.world.controller.오만정상컨트롤러;
import lineage.world.controller.월드보스컨트롤러;
import lineage.world.controller.정무컨트롤러;
import lineage.world.controller.지옥컨트롤러;
import lineage.world.controller.지하수로컨트롤러;
import lineage.world.controller.칠흑던전3층컨트롤러;
import lineage.world.controller.칠흑던전4층컨트롤러;
import lineage.world.controller.칠흑던전컨트롤러;
import lineage.world.controller.타임이벤트컨트롤러;
import lineage.world.controller.테베라스컨트롤러;
import lineage.world.controller.펭귄사냥컨트롤러;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.item.MagicDoll;
import lineage.world.object.item.potion.HealingPotion;
import lineage.world.object.magic.BloodToSoul;
import lineage.world.object.magic.BraveAvatar;
import lineage.world.object.magic.BraveMental;
import lineage.world.object.magic.BurningWeapon;
import lineage.world.object.magic.ClearMind;
import lineage.world.object.magic.CounterBarrier;
import lineage.world.object.magic.Criminal;
import lineage.world.object.magic.EagleEye;
import lineage.world.object.magic.ElementalFire;
import lineage.world.object.magic.EyeOfStorm;
import lineage.world.object.magic.FrameSpeedOverStun;
import lineage.world.object.magic.GlowingWeapon;
import lineage.world.object.magic.HolyWalk;
import lineage.world.object.magic.ImmuneToHarm;
import lineage.world.object.magic.NaturesTouch;
import lineage.world.object.magic.ReductionArmor;
import lineage.world.object.magic.ResistElemental;
import lineage.world.object.magic.ResistMagic;
import lineage.world.object.magic.ShapeChange;
import lineage.world.object.magic.ShiningShield;
import lineage.world.object.magic.SolidCarriage;
import lineage.world.object.magic.SoulOfFlame;
import lineage.world.object.magic.StormShot;
import lineage.world.object.magic.Sunburst;
import lineage.world.object.magic.TripleArrow;
import lineage.world.object.magic.TurnUndead;
import lineage.world.object.npc.SpotCrown;
import lineage.world.object.npc.SpotTower;
import lineage.world.object.npc.background.BackgroundTile;
import lineage.world.object.npc.background.Cracker;
import lineage.world.object.npc.background.DeathEffect;
import lineage.world.object.npc.background.Firewall;
import lineage.world.object.npc.background.FishExp;
import lineage.world.object.npc.background.LifeStream;
import lineage.world.object.npc.background.RestCracker;
import lineage.world.object.npc.background.Switch;
import lineage.world.object.npc.background.door.Door;
import lineage.world.object.npc.kingdom.KingdomCastleTop;
import lineage.world.object.npc.kingdom.KingdomCrown;
import lineage.world.object.npc.kingdom.KingdomDoor;
import lineage.world.object.npc.teleporter.Esmereld;
import party_auto_sell.AutosellDatabase;
import party_auto_sell.AutosellItem;
import party_auto_sell.S_AutoSell;
import party_auto_sell.S_AutoSellDelete;
import party_auto_sell.S_AutoSellList;

public class PcInstance extends Character {

	private LineageClient client;
	private String accountId;
	private int accountUid;

	private int attribute; // 요정 클레스들 속성마법 값 1[물] 2[바람] 3[땅] 4[불]
	// 운영자 여부 판단 변수.
	private int gm;
	public int testTime;
	public int testTime2;
	public int gmTime;
	public long checkTime = 0;
	public long checkTimes = 0;
	public int checkaccess = 0;

	public int PcMarket_Step;
	public int PcMarket_Count;
	public object tempObject;
	private object tempShop;
	public int attack_count;
	// 파티변수
	private long partyid;
	// PcInstance 클래스 멤버에 추가
	private long lastPremiumMentTime = 0;

	private long expPotionAdenaUntil = 0L;

	// 채팅 사용 유무
	private boolean chattingWhisper;
	private boolean chattingGlobal;
	private boolean chattingTrade;
	// 피케이 처리 변수
	private int PkCount; // 누적된 피케이 횟수.
	private long PkTime; // 최근에 실행한 피케이 시간.
	public long damage_action_Time;
	private boolean isSealBuff;
	private int Seal_Level;
	private boolean isSealBuff2;
	private int Seal_Level2;
	private boolean isSealBuff10;
	private boolean isSealBuff20;
	private boolean isSealBuff30;
	private boolean isSealBuff40;
	private boolean isSealBuff50;
	private boolean isSealBuff60;
	private boolean isSealBuff70;
	private boolean isSealBuff80;
	private boolean isSealBuff90;
	private boolean isSealBuff100;
	private boolean isSealBuff110;
	private boolean isSealBuff120;
	private boolean isSealBuff130;
	private boolean isImmortalityPremiumBuff = false; // 고급 가호 효과 적용여부
	private double premiumExpBonus = 0.0;
	private double premiumAdenaBonus = 0.0;

	// 자동판매
	private static List<AutosellItem> sellList; // 자동사냥 구매목록.
	private boolean persnalShopInsert;
	private boolean persnalShopSellEdit;

	// 인벤토리
	private Inventory inv;
	private List<String> listBlockName;
	private Esmereld npc_esmereld; // 에스메랄다 npc 포인터
	// 딜레이용
	private long message_time; // 서버 메세지가 표현된 마지막 시간 저장.
	private long auto_save_time; // 자동저장 이전시간 기록용.
	private long premium_item_time; // 자동지급 시간 저장.
	private long open_wait_item_time; // 자동지급 시간 저장.
	// 로그 기록용
	private long register_date; // 케릭 생성 시간
	private long join_date; // 케릭 접속 시간
	// 죽으면서 착감된 경험치 실시간 기록용.
	private double lost_exp;
	// 오토루팅 처리할지 여부를 확인할 변수.
	private boolean auto_pickup;
	// 자신에 hp바를 머리위에 표현할지 여부.
	private boolean is_hpbar;
	// 인터페이스 및 인벤 정보.
	private byte[] db_interface;
	// 엘릭서 사용된 갯수
	private int elixir;
	// 추가 경험치 지급처리에 사용되는 변수(버프에서 증가 혹은 감사시킴)
	private double dynamicExp;
	// 나이 설정
	private int age;
	// 변신 주문서를 위한 임시 저장 변수
	private boolean tempPoly;
	private ItemInstance tempPolyScroll;
	// 고정 멤버 유무
	private boolean member;
	// 오토루팅 메세지 on/off
	private boolean isAutoPickMessage;
	// 칼렉 딜레이를 위한 변수
	private long lastLackTime;
	private long lastLackTime2;
	// 바포메트 시스템을 위한 변수
	private boolean isBaphomet;
	private int baphometLevel;

	// 랭킹 시스템을 위한 변수
	private int rank; // 현재 랭크
	private int lastRank; // 직전 랭크
	private int lastRankClass; // 0:없음, 1:1~5, 2:6~15, 3:16~30

	// 새로 추가 (이전에 적용했던 보너스 총값: 1,3,5 중 하나)
	private int immortalityPremiumAppliedVal = 0;

	// 마법인형을 위한 변수
	private MagicDoll magicDoll;
	private MagicDollInstance magicDollinstance;
	// exp 버프 아이콘을 위한 변수
	private boolean icon;
	// 팀대전 & 난투전 위한 변수
	private String tempName;
	private String tempClanName;
	private String tempTitle;
	private int tempClanId;
	private int tempClanGrade;
	private int battleTeam;
	private boolean isTeamBattleDead;

	// 시간제 던전을 위한 변수
	private int giran_dungeon_time;
	// 결투장을 위한 변수
	public boolean isBattlezone;

	// 자동사냥 카운터
	private int autoHuntMonsterCount;
	// 자동사냥 답
	private String autoHuntAnswer;
	// 자동사냥 방지 인증번호 받은 후 공격불가 까지 딜레이
	private long autoHuntAnswerTime;

	// 장비거래 정보
	private String infoName;
	private String infoPhoneNum;
	private String infoBankName;
	private String infoBankNum;
	private PcTrade pcTrade;
	// 기란감옥 시간 초기화 주문서 횟수 제한
	public int giran_dungeon_count;

	// 자동사냥 시간 초기화 주문서 횟수 제한
	public int auto_count;
	// 시세검색
	public List<marketPrice> marketPrice;
	// 장비 스왑
	private Map<String, Swap[]> swap;
	public String[] swapIdx;
	public String selectSwap;
	public boolean isInsertSwap;

	// 출석체크
	public int daycount;
	public int daycheck;
	public int dayptime;

	// 자동 사냥
	private AStar aStar; // 길찾기 변수
	private Node tail; // 길찾기 변수
	private int[] iPath; // 길찾기 변수
	public boolean isAutoHunt;
	public object autohunt_target;
	public int start_x;
	public int start_y;
	public int start_map;
	public int temp_x;
	public int temp_y;
	public int temp_map;
	public int auto_hunt_account_time;
	public int auto_hunt_time;
	public boolean is_auto_return_home;
	public int auto_return_home_hp;
	public boolean is_auto_buff;
	public long auto_buff_time;
	public boolean is_auto_potion_buy;
	public boolean is_auto_poly_select;
	public boolean is_auto_rank_poly;
	public boolean is_auto_rank_poly_buy;
	public boolean is_auto_poly;
	public boolean is_auto_poly_buy;
	public boolean is_auto_teleport;
	public boolean is_auto_teleport_buy;
	public boolean is_auto_haste;
	public boolean is_auto_haste_buy;
	public boolean is_auto_bravery;
	public boolean is_auto_bravery_buy;
	public boolean is_auto_arrow_buy;
	public boolean is_auto_trunundead;
	public boolean is_auto_sell;
	public long auto_hunt_teleport_time;
	public int pclevel_gift_check;
	public long now_Time;
	public long frame_Time;
	public long ai_Time;

	public PcInstance(LineageClient client) {
		this.client = client;
		listBlockName = new ArrayList<String>();
		marketPrice = new ArrayList<marketPrice>();
		swap = new HashMap<String, Swap[]>();
		swapIdx = new String[Lineage.SLOT_ARROW];
		autoPotionIdx = new String[20];
		sellList = new ArrayList<AutosellItem>();

		aStar = new AStar();
		iPath = new int[2];
	}

	@Override
	public void close() {
		super.close();
		persnalShopSellEdit = persnalShopInsert = false;
		dayptime = 0;
		auto_save_time = 0;
		persnalShopInsert = false;
		age = accountUid = tempClanId = tempClanGrade = battleTeam = lastRankClass = rank = giran_dungeon_time = 0;
		tempPoly = member = isBaphomet = isTeamBattleDead = isBattlezone = isSealBuff = isSealBuff2 = isSealBuff10 = isSealBuff20 = isSealBuff30 = isSealBuff40 = isSealBuff50 = isSealBuff60 = isSealBuff70 = isSealBuff80 = isSealBuff90 = isSealBuff100 = isSealBuff110 = isSealBuff120 = isSealBuff130 = isImmortalityPremiumBuff = false;
		auto_pickup = is_hpbar = false;
		chattingWhisper = chattingGlobal = chattingTrade = isAutoPickMessage = true;
		lost_exp = premium_item_time = register_date = join_date = message_time = PkTime = partyid = attribute = PkCount = gm = elixir = Seal_Level = 0;
		dynamicExp = baphometLevel = autoHuntMonsterCount = 0;
		inv = null;
		npc_esmereld = null;
		db_interface = null;
		tempPolyScroll = null;
		accountId = null;
		magicDoll = null;
		magicDollinstance = null;
		tempShop = null;
		pcTrade = null;
		tempName = tempClanName = tempTitle = autoHuntAnswer = null;
		icon = true;
		lastLackTime = autoHuntAnswerTime = 0L;
		infoName = infoPhoneNum = infoBankName = infoBankNum = null;
		giran_dungeon_count = 0;
		auto_count = 0;
		daycount = 0;
		daycheck = 0;
		damage_action_Time = 0;
		pclevel_gift_check = 0;
		if (listBlockName != null)
			listBlockName.clear();
		if (marketPrice != null)
			marketPrice.clear();

		if (swap != null)
			swap.clear();
		swapIdx = null;
		selectSwap = null;
		isInsertSwap = false;

		open_wait_item_time = 0;

		damage_action_Time = 0;

		auto_hunt_account_time = auto_hunt_time = 0;
		isAutoHunt = is_auto_return_home = is_auto_buff = is_auto_potion_buy = is_auto_poly_select = is_auto_rank_poly = is_auto_rank_poly_buy = is_auto_poly = is_auto_poly_buy = false;
		is_auto_teleport = is_auto_haste = is_auto_haste_buy = is_auto_bravery = is_auto_bravery_buy = is_auto_trunundead = is_auto_sell = false;
		is_auto_arrow_buy = false;
		autohunt_target = null;
		start_x = start_y = start_map = temp_x = temp_y = temp_map = auto_return_home_hp = 0;
		auto_buff_time = auto_hunt_teleport_time = 0L;

		if (aStar != null)
			aStar.cleanTail();
	}

	public Map<String, Swap[]> getSwap() {
		synchronized (swap) {
			return swap;
		}
	}

	public void setExpPotionAdenaUntil(long untilMillis) {
		this.expPotionAdenaUntil = untilMillis;
	}

	// f1상점
	public object getTempShop() {
		return tempShop;
	}

	public void setTempShop(object tempShop) {
		this.tempShop = tempShop;
	}

	public void setSwap(Map<String, Swap[]> swap) {
		this.swap = swap;
	}

	public int getGiran_dungeon_count() {
		return giran_dungeon_count;
	}

	public void setGiran_dungeon_count(int giran_dungeon_count) {
		this.giran_dungeon_count = giran_dungeon_count;
	}

	public int getAuto_count() {
		return auto_count;
	}

	public void setAuto_count(int auto_count) {
		this.auto_count = auto_count;
	}

	public int getPclevel_gift_check() {
		return pclevel_gift_check;
	}

	public void setPclevel_gift_check(int pclevel_gift_check) {
		this.pclevel_gift_check = pclevel_gift_check;
	}

	public PcTrade getPcTrade() {
		return pcTrade;
	}

	public void setPcTrade(PcTrade pcTrade) {
		this.pcTrade = pcTrade;
	}

	public String getInfoName() {
		return infoName;
	}

	public void setInfoName(String infoName) {
		this.infoName = infoName;
	}

	public String getInfoPhoneNum() {
		return infoPhoneNum;
	}

	public void setInfoPhoneNum(String infoPhoneNum) {
		this.infoPhoneNum = infoPhoneNum;
	}

	public String getInfoBankName() {
		return infoBankName;
	}

	public void setInfoBankName(String infoBankName) {
		this.infoBankName = infoBankName;
	}

	public String getInfoBankNum() {
		return infoBankNum;
	}

	public void setInfoBankNum(String infoBankNum) {
		this.infoBankNum = infoBankNum;
	}

	public long getAutoHuntAnswerTime() {
		return autoHuntAnswerTime;
	}

	public void setAutoHuntAnswerTime(long autoHuntAnswerTime) {
		this.autoHuntAnswerTime = autoHuntAnswerTime;
	}

	public String getAutoHuntAnswer() {
		return autoHuntAnswer;
	}

	public void setAutoHuntAnswer(String autoHuntAnswer) {
		this.autoHuntAnswer = autoHuntAnswer;
	}

	public int getAutoHuntMonsterCount() {
		return autoHuntMonsterCount;
	}

	public void setAutoHuntMonsterCount(int autoHuntMonsterCount) {
		this.autoHuntMonsterCount = autoHuntMonsterCount;
	}

	@Override
	public int getGm() {
		return gm;
	}

	@Override
	public void setGm(int gm) {
		this.gm = gm;
	}

	public List<String> getListBlockName() {
		return listBlockName;
	}

	public int getPkCount() {
		return PkCount;
	}

	public void setPkCount(int pkCount) {
		PkCount = pkCount;
	}

	public long getPkTime() {
		return PkTime;
	}

	public void setPkTime(long pkTime) {
		PkTime = pkTime;
	}

	@Override
	public long getPartyId() {
		return partyid;
	}

	@Override
	public void setPartyId(long partyid) {
		this.partyid = partyid;
	}

	public LineageClient getClient() {
		return client;
	}

	@Override
	public Inventory getInventory() {
		return inv;
	}

	@Override
	public void setInventory(Inventory inv) {
		this.inv = inv;
	}

	public boolean isChattingTrade() {
		return chattingTrade;
	}

	public void setChattingTrade(boolean chattingTrade) {
		this.chattingTrade = chattingTrade;
	}

	public boolean isExpPotionAdenaOn() {
		return System.currentTimeMillis() < expPotionAdenaUntil;
	}

	public boolean isChattingWhisper() {
		return chattingWhisper;
	}

	public void setChattingWhisper(boolean chattingWhisper) {
		this.chattingWhisper = chattingWhisper;
	}

	public boolean isChattingGlobal() {
		return chattingGlobal;
	}

	public void setChattingGlobal(boolean chattingGlobal) {
		this.chattingGlobal = chattingGlobal;
	}

	@Override
	public int getAttribute() {
		return attribute;
	}

	public void setAttribute(final int attribute) {
		this.attribute = attribute;
	}

	public void setNpcEsmereld(Esmereld esmereld) {
		npc_esmereld = esmereld;
	}

	public Esmereld getNpcEsmereld() {
		return npc_esmereld;
	}

	public boolean isTeamBattleDead() {
		return isTeamBattleDead;
	}

	public void setTeamBattleDead(boolean isBattleRoyalDead) {
		this.isTeamBattleDead = isBattleRoyalDead;
	}

	public int getBattleTeam() {
		return battleTeam;
	}

	public void setBattleTeam(int battleTeam) {
		this.battleTeam = battleTeam;
	}

	public int getTempClanId() {
		return tempClanId;
	}

	public void setTempClanId(int tempClanId) {
		this.tempClanId = tempClanId;
	}

	public String getTempClanName() {
		return tempClanName;
	}

	public void setTempClanName(String tempClanName) {
		this.tempClanName = tempClanName;
	}

	public int getTempClanGrade() {
		return tempClanGrade;
	}

	public void setTempClanGrade(int tempClanGrade) {
		this.tempClanGrade = tempClanGrade;
	}

	public String getTempTitle() {
		return tempTitle;
	}

	public void setTempTitle(String tempTitle) {
		this.tempTitle = tempTitle;
	}

	public boolean isIcon() {
		return icon;
	}

	public void setIcon(boolean icon) {
		this.icon = icon;
	}

	public MagicDollInstance getMagicDollinstance() {
		return magicDollinstance;
	}

	public void setMagicDollinstance(MagicDollInstance magicDollinstance) {
		this.magicDollinstance = magicDollinstance;
	}

	public static List<AutosellItem> getSellList() {
		return sellList;
	}

	public void setSellList(List<AutosellItem> sellList) {
		this.sellList = sellList;
	}

	public boolean isPersnalShopInsert() {
		return persnalShopInsert;
	}

	public void setPersnalShopInsert(boolean persnalShopInsert) {
		this.persnalShopInsert = persnalShopInsert;
	}

	public boolean isPersnalShopSellEdit() {
		return persnalShopSellEdit;
	}

	public void setPersnalShopSellEdit(boolean persnalShopSellEdit) {
		this.persnalShopSellEdit = persnalShopSellEdit;
	}

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	public MagicDoll getMagicDoll() {
		return magicDoll;
	}

	public void setMagicDoll(MagicDoll magicDoll) {
		this.magicDoll = magicDoll;
	}

	public int getBaphometLevel() {
		return baphometLevel;
	}

	public void setBaphometLevel(int baphometLevel) {
		this.baphometLevel = baphometLevel;
	}

	public boolean isBaphomet() {
		return isBaphomet;
	}

	public void setBaphomet(boolean isBaphomet) {
		this.isBaphomet = isBaphomet;
	}

	public long getLastLackTime() {
		return lastLackTime;
	}

	public void setLastLackTime(long lastLackTime) {
		this.lastLackTime = lastLackTime;
	}

	public long getLastLackTime2() {
		return lastLackTime2;
	}

	public void setLastLackTime2(long lastLackTime2) {
		this.lastLackTime2 = lastLackTime2;
	}

	public boolean isAutoPickMessage() {
		return isAutoPickMessage;
	}

	public void setAutoPickMessage(boolean isAutoPickMessage) {
		this.isAutoPickMessage = isAutoPickMessage;
	}

	public int getAccountUid() {
		return accountUid;
	}

	public void setAccountUid(int accountUid) {
		this.accountUid = accountUid;
	}

	public boolean isMember() {
		return member;
	}

	public void setMember(boolean member) {
		this.member = member;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public ItemInstance getTempPolyScroll() {
		return tempPolyScroll;
	}

	public void setTempPolyScroll(ItemInstance tempPolyScroll) {
		this.tempPolyScroll = tempPolyScroll;
	}

	public boolean isTempPoly() {
		return tempPoly;
	}

	public void setTempPoly(boolean tempPoly) {
		this.tempPoly = tempPoly;
	}

	public double getDynamicExp() {
		return dynamicExp;
	}

	public void setDynamicExp(double dynamicExp) {
		this.dynamicExp = dynamicExp;
	}

	public long getRegisterDate() {
		return register_date;
	}

	public void setRegisterDate(long register_date) {
		this.register_date = register_date;
	}

	public long getJoinDate() {
		return join_date;
	}

	public void setJoinDate(long join_date) {
		this.join_date = join_date;
	}

	public double getLostExp() {
		return lost_exp;
	}

	public void setLostExp(double lost_exp) {
		this.lost_exp = lost_exp;
	}

	@Override
	public void setAge(int a) {
		age = a;
	}

	@Override
	public int getAge() {
		return age;
	}

	public int getGiran_dungeon_time() {
		return giran_dungeon_time;
	}

	public void setGiran_dungeon_time(int giran_dungeon_time) {
		this.giran_dungeon_time = giran_dungeon_time;
	}

	@Override
	public void setHeading(int heading) {
		super.setHeading(heading);
		if (!worldDelete)
			toSender(S_ObjectHeading.clone(BasePacketPooling.getPool(S_ObjectHeading.class), this), false);
	}

	@Override
	public void setLawful(int lawful) {
		super.setLawful(lawful);
		if (!worldDelete)
			toSender(S_ObjectLawful.clone(BasePacketPooling.getPool(S_ObjectLawful.class), this), true);
	}

	@Override
	public void setNowHp(int nowhp) {
		super.setNowHp(nowhp);
		if (!worldDelete) {
			toSender(S_CharacterHp.clone(BasePacketPooling.getPool(S_CharacterHp.class), this));

			PartyController.toUpdate(this);

			if (TeamBattleController.checkList(this) && !isTeamBattleDead())
				TeamBattleController.hpUpdate(this);
		}
	}

	@Override
	public void setNowMp(int nowmp) {
		super.setNowMp(nowmp);
		if (!worldDelete)
			toSender(S_CharacterMp.clone(BasePacketPooling.getPool(S_CharacterMp.class), this));
	}

	@Override
	public void setLight(int light) {
		super.setLight(light);
		if (!worldDelete)
			toSender(S_ObjectLight.clone(BasePacketPooling.getPool(S_ObjectLight.class), this), true);
	}

	@Override
	public void setInvis(boolean invis) {
		super.setInvis(invis);
		if (!worldDelete)
			toSender(S_ObjectInvis.clone(BasePacketPooling.getPool(S_ObjectInvis.class), this), true);

		if (getMagicDollinstance() != null)
			getMagicDollinstance().setInvis(invis);
	}

	@Override
	public void setFood(int food) {
		super.setFood(food);
		if (!worldDelete)
			toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
	}

	@Override
	public void toDamage(Character cha, int dmg, int type, Object... opt) {
		// 버그 방지 및 자기자신이 공격햇을경우 무시.
		if (cha == null || cha.getObjectId() == getObjectId() || dmg <= 0)
			return;

		checkAutoPotionPvP(cha);

		// 소환객체에게 알리기.
		SummonController.toDamage(this, cha, dmg);

		// 공격자가 사용자일때 구간.
		if (cha instanceof PcInstance) {
			// 보라도리로 변경하기.
			if (Lineage.server_version > 160 && World.isNormalZone(getX(), getY(), getMap()))
				Criminal.init(cha, this);
			// 경비병에게 도움요청.
			DamageController.toGuardHelper(this, cha);
		}
	}

	@Override
	public void setExp(double exp) {
		if (isDead()) {
			// 경험치 하향시키려 할때만.
			if (getExp() > exp)
				super.setExp(exp);
			return;
		}

		// 경험치 버그 처리
		Exp tempExp = ExpDatabase.find(getLevel() - 1);

		if (tempExp != null && exp < tempExp.getBonus()) {
			exp = tempExp.getBonus();
		}

		Exp max = ExpDatabase.find(Lineage.level_max);
		Exp max_prev = ExpDatabase.find(Lineage.level_max);

		if (max != null && max_prev != null && exp > 0 && level <= max.getLevel()) {
			Exp e = ExpDatabase.find(level);

			if (max_prev.getBonus() > exp)
				super.setExp(exp);
			else
				super.setExp(max_prev.getBonus() - 0.01);

			if (e != null) {
				boolean lvUp = e.getBonus() <= getExp();
				if (lvUp) {
					int hp = CharacterController.toStatusUP(this, true);
					int mp = CharacterController.toStatusUP(this, false);
					for (int i = 1; i <= Lineage.level_max; i++) {
						e = ExpDatabase.find(i);
						if (getExp() < e.getBonus())
							break;
					}
					for (int i = e.getLevel() - level; i > 1; i--) {
						hp += CharacterController.toStatusUP(this, true);
						mp += CharacterController.toStatusUP(this, false);
					}
					int new_hp = getMaxHp() + hp;
					int new_mp = getMaxMp() + mp;

					switch (classType) {
						case Lineage.LINEAGE_CLASS_ROYAL:
							if (new_hp >= Lineage.royal_max_hp)
								new_hp = Lineage.royal_max_hp;
							if (new_mp >= Lineage.royal_max_mp)
								new_mp = Lineage.royal_max_mp;
							break;
						case Lineage.LINEAGE_CLASS_KNIGHT:
							if (new_hp >= Lineage.knight_max_hp)
								new_hp = Lineage.knight_max_hp;
							if (new_mp >= Lineage.knight_max_mp)
								new_mp = Lineage.knight_max_mp;
							break;
						case Lineage.LINEAGE_CLASS_ELF:
							if (new_hp >= Lineage.elf_max_hp)
								new_hp = Lineage.elf_max_hp;
							if (new_mp >= Lineage.elf_max_mp)
								new_mp = Lineage.elf_max_mp;
							break;
						case Lineage.LINEAGE_CLASS_WIZARD:
							if (new_hp >= Lineage.wizard_max_hp)
								new_hp = Lineage.wizard_max_hp;
							if (new_mp >= Lineage.wizard_max_mp)
								new_mp = Lineage.wizard_max_mp;
							break;
						case Lineage.LINEAGE_CLASS_DARKELF:
							if (new_hp >= Lineage.darkelf_max_hp)
								new_hp = Lineage.darkelf_max_hp;
							if (new_mp >= Lineage.darkelf_max_mp)
								new_mp = Lineage.darkelf_max_mp;
							break;
						case Lineage.LINEAGE_CLASS_DRAGONKNIGHT:
							if (new_hp >= Lineage.dragonknight_max_hp)
								new_hp = Lineage.dragonknight_max_hp;
							if (new_mp >= Lineage.dragonknight_max_mp)
								new_mp = Lineage.dragonknight_max_mp;
							break;
						case Lineage.LINEAGE_CLASS_BLACKWIZARD:
							if (new_hp >= Lineage.blackwizard_max_hp)
								new_hp = Lineage.blackwizard_max_hp;
							if (new_mp >= Lineage.blackwizard_max_mp)
								new_mp = Lineage.blackwizard_max_mp;
							break;
					}

					setMaxHp(new_hp);
					setMaxMp(new_mp);
					setNowHp(getNowHp() + hp);
					setNowMp(getNowMp() + mp);
					setLevel(e.getLevel());

					toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
					PluginController.init(PcInstance.class, "toLevelup", this);
				} else {
					toSender(S_CharacterExp.clone(BasePacketPooling.getPool(S_CharacterExp.class), this));
				}
			}
		}
	}

	@Override
	public void toDead(Character cha) {
		//
		ClanController.toDead(this);
		MagicDollController.toDead(this);
		WantedController.toDead(cha, this);
	}

	/**
	 * 50이상 레벨 보너스스탯 지급해야 하는지, 체크용 메서드
	 */
	public boolean toLvStat(boolean packet) {

		if (Lineage.server_version < 163)
			return false;

		if (getLevelUpStat() > 0 || getResetBaseStat() > 0 || getResetLevelStat() > 0) {
			if (packet) {
				List<String> point = new ArrayList<String>();

				point.add(String.format("초기화 기초 스탯 [%d]개", getResetBaseStat()));
				point.add(String.format("초기화 레벨업 스탯 [%d]개", getResetLevelStat()));
				point.add(String.format("레벨업 스탯 [%d]개", getLevelUpStat()));
				toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "RaiseAttr", null, point));
			}
			return true;
		}

		return false;
	}

	/**
	 * 사용자 정보 저장 처리 함수.
	 */
	private void toSave() {
		Connection con = null;
		try {
			con = DatabaseConnection.getLineage();
			toSave(con);
		} catch (Exception e) {
			lineage.share.System.println(PcInstance.class.toString() + " : toSave()");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con);
		}
	}

	/**
	 * gui에서 사용중
	 * 
	 * @param
	 * @return
	 *         2017-09-06
	 *         by all_night.
	 */
	public void toCharacterSave() {
		Connection con = null;
		try {
			con = DatabaseConnection.getLineage();
			toSave(con);
		} catch (Exception e) {
			lineage.share.System.println(PcInstance.class.toString() + " : toSave()");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con);
		}
	}

	// 야도란 개인적인 저장에 사용
	public void toCharacterSave2() {
		Connection con = null;
		try {
			con = DatabaseConnection.getLineage();
			toSave2(con);
		} catch (Exception e) {
			lineage.share.System.println(PcInstance.class.toString() + " : toSave()");
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con);
		}
	}

	/**
	 * CharacterThread에서 주기적으로 모든 캐릭터 저장하는 함수.
	 * 2018-05-10
	 * by all-night.
	 */
	public void autoSave(Connection con, long time) {
		// 월드 접속하고 타이머에 등록된후 해당 함수가 호출됫을때 auto_save_time 값이 0 임.
		// 접속하자마자 저장하는걸 방지하기위해 아래 코드 삽입.
		if (auto_save_time > 0 && auto_save_time <= System.currentTimeMillis()) {
			auto_save_time = System.currentTimeMillis() + 1000;
			toSave(con);
		}
	}

	/**
	 * 자동 저장 버그 방지.
	 * 2019-07-09
	 * by connector12@nate.com
	 */
	public void setAutoSaveTime(boolean isWorldJoin) {
		if (isWorldJoin)
			auto_save_time = System.currentTimeMillis() + (1000 * 5);
		else
			auto_save_time = 0;
	}

	/**
	 * 팀대전 오류시 캐릭터 정보 날아가는 현상 방지.
	 * 2019-06-28
	 * by connector12@nate.com
	 */
	public void checkTeamBattle(boolean isRead) {
		if (isRead) {
			// 캐릭터 접속시 체크.
			if (getBattleTeam() != 0) {
				setBattleTeam(0);

				if (getTempName() != null)
					setName(getTempName());

				if (getTempClanName() != null)
					setClanName(getTempClanName());

				if (getTempTitle() != null)
					setTitle(getTempTitle());

				setClanId(getTempClanId());
				setClanGrade(getTempClanGrade());

				setGfx(getClassGfx());

				if (getInventory() != null && getInventory().getSlot(Lineage.SLOT_WEAPON) != null)
					setGfxMode(getClassGfxMode() + getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getGfxMode());
				else
					setGfxMode(getClassGfxMode());

				int[] loc = Lineage.getHomeXY();
				setX(loc[0]);
				setY(loc[1]);
				setMap(loc[2]);
			}
		} else {
			// 팀대전이 정상 종료 되지않았을 경우 체크.
			if (getBattleTeam() != 0 && !TeamBattleController.startTeamBattle && !TeamBattleController.askTeamBattle) {
				setBattleTeam(0);

				if (getTempName() != null)
					setName(getTempName());

				if (getTempClanName() != null)
					setClanName(getTempClanName());

				if (getTempTitle() != null)
					setTitle(getTempTitle());

				setClanId(getTempClanId());
				setClanGrade(getTempClanGrade());

				setGfx(getClassGfx());

				if (getInventory() != null && getInventory().getSlot(Lineage.SLOT_WEAPON) != null)
					setGfxMode(getClassGfxMode() + getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getGfxMode());
				else
					setGfxMode(getClassGfxMode());

				int[] loc = Lineage.getHomeXY();
				toPotal(loc[0], loc[1], loc[2]);
			}
		}
	}

	/**
	 * 사용자 정보 저장 처리 함수.
	 */
	public void toSave(Connection con) {
		if (auto_save_time > System.currentTimeMillis()) {
			return;
		}

		// 저장
		try {
			CharactersDatabase.saveInventory(con, this);

		} catch (Exception e) {
			lineage.share.System.println("saveInventory | 캐릭명: " + getName());
			lineage.share.System.println(e);
		}

		try {
			CharactersDatabase.saveSkill(con, this);
		} catch (Exception e) {
			lineage.share.System.println("saveSkill | 캐릭명: " + getName());
			lineage.share.System.println(e);
		}

		try {
			CharactersDatabase.saveBuff(con, this);
		} catch (Exception e) {
			lineage.share.System.println("saveBuff | 캐릭명: " + getName());
			lineage.share.System.println(e);
		}

		try {
			CharactersDatabase.saveBook(con, this);
		} catch (Exception e) {
			lineage.share.System.println("saveBook | 캐릭명: " + getName());
			lineage.share.System.println(e);
		}

		try {
			CharactersDatabase.saveCharacter(con, this);
		} catch (Exception e) {
			lineage.share.System.println("saveCharacter | 캐릭명: " + getName());
			lineage.share.System.println(e);
		}

		try {
			CharactersDatabase.saveMember(con, this);
		} catch (Exception e) {
			lineage.share.System.println("saveMember | 캐릭명: " + getName());
			lineage.share.System.println(e);
		}

		try {
			CharactersDatabase.saveBlockList(con, this);
		} catch (Exception e) {
			lineage.share.System.println("saveBlockList | 캐릭명: " + getName());
			lineage.share.System.println(e);
		}

		try {
			SummonController.toSave(con, this);
		} catch (Exception e) {
			lineage.share.System.println("SummonController.toSave | 캐릭명: " + getName());
			lineage.share.System.println(e);
		}

		try {
			FriendController.toSave(con, this);
		} catch (Exception e) {
			lineage.share.System.println("FriendController.toSave | 캐릭명: " + getName());
			lineage.share.System.println(e);
		}

		saveSwap(con);
	}

	public void toSave2(Connection con) {

		// 저장
		try {
			CharactersDatabase.saveInventory(con, this);

		} catch (Exception e) {
			lineage.share.System.println("saveInventory | 캐릭명: " + getName());
			lineage.share.System.println(e);
		}

		try {
			CharactersDatabase.saveSkill(con, this);
		} catch (Exception e) {
			lineage.share.System.println("saveSkill | 캐릭명: " + getName());
			lineage.share.System.println(e);
		}

		try {
			CharactersDatabase.saveBuff(con, this);
		} catch (Exception e) {
			lineage.share.System.println("saveBuff | 캐릭명: " + getName());
			lineage.share.System.println(e);
		}

		try {
			CharactersDatabase.saveBook(con, this);
		} catch (Exception e) {
			lineage.share.System.println("saveBook | 캐릭명: " + getName());
			lineage.share.System.println(e);
		}

		try {
			CharactersDatabase.saveCharacter(con, this);
		} catch (Exception e) {
			lineage.share.System.println("saveCharacter | 캐릭명: " + getName());
			lineage.share.System.println(e);
		}

		try {
			CharactersDatabase.saveMember(con, this);
		} catch (Exception e) {
			lineage.share.System.println("saveMember | 캐릭명: " + getName());
			lineage.share.System.println(e);
		}

		try {
			CharactersDatabase.saveBlockList(con, this);
		} catch (Exception e) {
			lineage.share.System.println("saveBlockList | 캐릭명: " + getName());
			lineage.share.System.println(e);
		}

		try {
			SummonController.toSave(con, this);
		} catch (Exception e) {
			lineage.share.System.println("SummonController.toSave | 캐릭명: " + getName());
			lineage.share.System.println(e);
		}

		try {
			FriendController.toSave(con, this);
		} catch (Exception e) {
			lineage.share.System.println("FriendController.toSave | 캐릭명: " + getName());
			lineage.share.System.println(e);
		}

		saveSwap(con);
	}

	/**
	 * 월드 진입할때 호출됨.
	 */
	public void toWorldJoin() {
		// 버그 방지.
		if (!isWorldDelete())
			return;

		// 인터페이스 전송. db_interface
		if (db_interface != null)
			toSender(S_InterfaceRead.clone(BasePacketPooling.getPool(S_InterfaceRead.class), db_interface));

		FishingController.toWorldJoin(this);

		// 메모리 세팅
		setAutoPickup(Lineage.auto_pickup);
		World.appendPc(this);
		BookController.toWorldJoin(this);
		ClanController.toWorldJoin(this);
		InventoryController.toWorldJoin(this);
		SkillController.toWorldJoin(this);
		CharacterController.toWorldJoin(this);
		TradeController.toWorldJoin(this);
		BuffController.toWorldJoin(this);
		SummonController.toWorldJoin(this);
		QuestController.toWorldJoin(this);
		ChattingController.toWorldJoin(this);
		MagicDollController.toWorldJoin(this);
		// 운영자일때 정보 갱신
		// 기억리스트 추출 및 전송
		CharactersDatabase.readBook(this);
		// 스킬 추출 및 전송
		CharactersDatabase.readSkill(this);
		// 인벤토리 추출 및 전송
		CharactersDatabase.readInventory(this);
		// 차단 리스트 추출 및 전송
		CharactersDatabase.readBlockList(this);
		// 팀대전 오류 확인.
		checkTeamBattle(true);
		// 케릭터 정보 전송
		toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
		// 월드 스폰.
		super.toTeleport(x, y, map, false);
		// 상태에따라 패킷전송 [광전사의 도끼를 착용하고 있을수 있기때문에 추가됨.]
		// 버프 잡아주기
		CharactersDatabase.readBuff(this);
		// 날씨 보내기
		toSender(S_Weather.clone(BasePacketPooling.getPool(S_Weather.class), LineageServer.weather));
		// 맵핵 자동켜짐
		setMapHack(true);
		toSender(new S_Ability(3, true));
		// 피바 자동켜짐
		setHpbar(true);
		toSender(S_ObjectHitratio.clone(BasePacketPooling.getPool(S_ObjectHitratio.class), this, isHpbar()));
		// 성처리
		KingdomController.toWorldJoin(this);
		// 편지 확인
		LetterController.toWorldJoin(this);
		// 친구 목록 갱신.
		FriendController.toWorldJoin(this);

		WantedController.toWorldJoin(this);

		// 서버 상태에 따른 처리
		if (Lineage.event_buff)
			CommandController.toBuff(this);
		// %0님께서 방금 게임에 접속하셨습니다.
		if (Lineage.world_message_join)
			World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null,
					Lineage.CHATTING_MODE_MESSAGE, String.format("%s님께서 방금 게임에 접속하셨습니다.", getName())));
		//
		PluginController.init(PcInstance.class, "toWorldJoin", this);

		BoardInstance b = BackgroundDatabase.getGuideBoard();

		// 50렙 이하 서버정보 창 띄우기
		if (getLevel() < 50)

			if (b != null)
				b.toClick(this, null);

		// 고정 멤버 버프
		고정멤버버프(false);

		if (KingdomController.find(this) != null) {
			setDynamicAddDmg(getDynamicAddDmg() + 1);
			setDynamicAddDmgBow(getDynamicAddDmgBow() + 1);
			setDynamicAddHit(getDynamicAddHit() + 1);
			setDynamicAddHitBow(getDynamicAddHitBow() + 1);
			setDynamicSp(getDynamicSp() + 1);
			setDynamicExp(getDynamicExp() + Lineage.kingdom_clan_rate_exp);
			setAddDropAdenRate(getAddDropAdenRate() + Lineage.kingdom_clan_rate_aden);
			setAddDropItemRate(getAddDropItemRate() + Lineage.kingdom_clan_rate_drop);
			ChattingController.toChatting(this, "성혈맹: 추가 대미지+1, 추가 명중+1, SP+1, 경험치/아데나/드랍률+20%",
					Lineage.CHATTING_MODE_MESSAGE);
		} else {
			if (getClanId() > 3) {
				setDynamicExp(getDynamicExp() + Lineage.clan_rate_exp);
				setAddDropAdenRate(getAddDropAdenRate() + Lineage.clan_rate_aden);
				setAddDropItemRate(getAddDropItemRate() + Lineage.clan_rate_drop);
				ChattingController.toChatting(this, "혈맹: 경험치/아데나/드랍률+10%", Lineage.CHATTING_MODE_MESSAGE);
			}

			if (getGm() == 0 && KingdomController.isKingdomInsideLocation(this)) {
				int[] loc = Lineage.getHomeXY();
				toTeleport(loc[0], loc[1], loc[2], true);
			}
		}

		if (getLevel() >= Lineage.rank_min_level) {
			rankSystem();
		}

		boolean 행운의인장 = false;
		if (this != null && this.getInventory() != null)
			행운의인장 = this.getInventory().find("행운의인장(20%)") == null ? false : true;
		boolean 대박의인장 = false;
		if (this != null && this.getInventory() != null)
			대박의인장 = this.getInventory().find("대박의인장(50%)") == null ? false : true;

		boolean 오만의탑1층지배부적 = false;
		if (this != null && this.getInventory() != null)
			오만의탑1층지배부적 = this.getInventory().find("오만의 탑 1층 지배 부적") == null ? false : true;
		boolean 오만의탑2층지배부적 = false;
		if (this != null && this.getInventory() != null)
			오만의탑2층지배부적 = this.getInventory().find("오만의 탑 2층 지배 부적") == null ? false : true;
		boolean 오만의탑3층지배부적 = false;
		if (this != null && this.getInventory() != null)
			오만의탑3층지배부적 = this.getInventory().find("오만의 탑 3층 지배 부적") == null ? false : true;
		boolean 오만의탑4층지배부적 = false;
		if (this != null && this.getInventory() != null)
			오만의탑4층지배부적 = this.getInventory().find("오만의 탑 4층 지배 부적") == null ? false : true;
		boolean 오만의탑5층지배부적 = false;
		if (this != null && this.getInventory() != null)
			오만의탑5층지배부적 = this.getInventory().find("오만의 탑 5층 지배 부적") == null ? false : true;
		boolean 오만의탑6층지배부적 = false;
		if (this != null && this.getInventory() != null)
			오만의탑6층지배부적 = this.getInventory().find("오만의 탑 6층 지배 부적") == null ? false : true;
		boolean 오만의탑7층지배부적 = false;
		if (this != null && this.getInventory() != null)
			오만의탑7층지배부적 = this.getInventory().find("오만의 탑 7층 지배 부적") == null ? false : true;
		boolean 오만의탑8층지배부적 = false;
		if (this != null && this.getInventory() != null)
			오만의탑8층지배부적 = this.getInventory().find("오만의 탑 8층 지배 부적") == null ? false : true;
		boolean 오만의탑9층지배부적 = false;
		if (this != null && this.getInventory() != null)
			오만의탑9층지배부적 = this.getInventory().find("오만의 탑 9층 지배 부적") == null ? false : true;
		boolean 오만의탑10층지배부적 = false;
		if (this != null && this.getInventory() != null)
			오만의탑10층지배부적 = this.getInventory().find("오만의 탑 10층 지배 부적") == null ? false : true;
		boolean 오만의탑정상지배부적 = false;
		if (this != null && this.getInventory() != null)
			오만의탑정상지배부적 = this.getInventory().find("오만의 탑 정상 지배 부적") == null ? false : true;
		boolean 오만의탑환상의지배부적 = false;
		if (this != null && this.getInventory() != null)
			오만의탑환상의지배부적 = this.getInventory().find("오만의 탑 환상의 지배 부적") == null ? false : true;

		if (대박의인장)
			ChattingController.toChatting(this, "대박의인장: 경험치/드랍률+50%", Lineage.CHATTING_MODE_MESSAGE);
		if (행운의인장)
			ChattingController.toChatting(this, "행운의인장: 경험치/드랍률+20%", Lineage.CHATTING_MODE_MESSAGE);

		if (오만의탑1층지배부적)
			ChattingController.toChatting(this, "오만의탑1층지배부적 효과를 받고 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
		if (오만의탑2층지배부적)
			ChattingController.toChatting(this, "오만의탑2층지배부적 효과를 받고 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
		if (오만의탑3층지배부적)
			ChattingController.toChatting(this, "오만의탑3층지배부적 효과를 받고 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
		if (오만의탑4층지배부적)
			ChattingController.toChatting(this, "오만의탑4층지배부적 효과를 받고 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
		if (오만의탑5층지배부적)
			ChattingController.toChatting(this, "오만의탑5층지배부적 효과를 받고 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
		if (오만의탑6층지배부적)
			ChattingController.toChatting(this, "오만의탑6층지배부적 효과를 받고 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
		if (오만의탑7층지배부적)
			ChattingController.toChatting(this, "오만의탑7층지배부적 효과를 받고 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
		if (오만의탑8층지배부적)
			ChattingController.toChatting(this, "오만의탑8층지배부적 효과를 받고 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
		if (오만의탑9층지배부적)
			ChattingController.toChatting(this, "오만의탑9층지배부적 효과를 받고 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
		if (오만의탑10층지배부적)
			ChattingController.toChatting(this, "오만의탑10층지배부적 효과를 받고 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
		if (오만의탑정상지배부적)
			ChattingController.toChatting(this, "오만의 탑 정상 지배 부적 효과를 받고 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
		if (오만의탑환상의지배부적)
			ChattingController.toChatting(this, "오만의 탑 환상의 지배 부적 효과를 받고 있습니다.", Lineage.CHATTING_MODE_MESSAGE);

		if (getGm() == 0) {
			Kingdom k = KingdomController.findKingdomLocation(this);
			if (k != null && k.isWar()) {
				int[] loc = Lineage.getHomeXY();
				toTeleport(loc[0], loc[1], loc[2], true);
			}
		}

		AutoHuntCheckController.toWorldJoin(this);

		// sp, mr 갱신.
		toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));

		for (PcInstance master : World.getPcList()) {
			if (master.getGm() > 0 && !master.isWorldDelete() && getObjectId() != master.getObjectId()) {
				ChattingController.toChatting(master,
						String.format("\\fY[계정: %s   Lv.%d %s] \\fR접속", getAccountId(), getLevel(), getName()),
						Lineage.CHATTING_MODE_MESSAGE);
				break;
			}
		}

		try {
			UIBoardService.service().onEnterWorld(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 오픈 대기 중일때 메세지 전송
		if (Lineage.open_wait)
			ChattingController.toChatting(this, String.format("%s [오픈 대기중] 입니다.", ServerDatabase.getName()),
					Lineage.CHATTING_MODE_MESSAGE);

		// 장비 스왑 데이터 로드.
		readSwap();

		// 로그 기록.
		if (Log.isLog(this))
			Log.appendConnect(getRegisterDate(), client.getAccountIp(), client.getAccountId(), getName(), "접속");

		setAutoSaveTime(true);
	}

	/**
	 * 월드에서 나갈때 호출됨.
	 */
	public void toWorldOut() {
		setAutoSaveTime(false);

		restCrackerWorldOut();
		UIBoardService.service().onLeaveWorld(this);

		// 버그 방지.
		if (isWorldDelete())
			return;

		// 낚시중에 종료시 낚싯대 장착 해제
		if (isFishing()) {
			getInventory().getSlot(Lineage.SLOT_WEAPON).toClick(this, null);
		}
		// 팀대전 중 리스할 경우
		if (TeamBattleController.checkList(this))
			TeamBattleController.removeList(this);

		PluginController.init(PcInstance.class, "toWorldOut", this);

		for (PcInstance master : World.getPcList()) {
			if (master.getGm() > 0 && !master.isWorldDelete()) {
				ChattingController.toChatting(master,
						String.format("\\fY[계정: %s   Lv.%d %s] \\fR종료", getAccountId(), getLevel(), getName()),
						Lineage.CHATTING_MODE_MESSAGE);
				break;
			}
		}
		// 로그 기록.
		if (Log.isLog(this))
			Log.appendConnect(getRegisterDate(), client.getAccountIp(), client.getAccountId(), getName(), "종료");
		// 사용자 정보 저장전에 처리해야될 메모리 처리
		TradeController.toWorldOut(this); // 거래중인 아이템 때문에 저장전에 처리.
		// 죽어있을경우에 처리를 위해.
		toReset(true);
		// 에스메랄다 미래보기 상태라면
		if (npc_esmereld != null)
			npc_esmereld.toTeleport(this);
		// 근처 마을로 좌표 변경.
		if (TeleportResetDatabase.toLocation(this) && getGm() == 0) {
			x = homeX;
			y = homeY;
			map = homeMap;
		}
		// 성주면에 있는지 확인. 있을경우 근처마을로 좌표 변경.
		Kingdom k = KingdomController.findKingdomLocation(this);
		if (k != null && k.getClanId() > 0 && k.getClanId() != getClanId()) {
			TeleportHomeDatabase.toLocation(this);
			x = homeX;
			y = homeY;
			map = homeMap;
		}

		// 사용자 정보 저장
		toSave();
		savePet();
		// 이전에 관리중이던 목록 제거
		clearList(true);
		// 월드에 갱신
		World.remove(this);
		// 사용된 메모리 제거
		World.removePc(this);
		SummonController.toWorldOut(this);
		BookController.toWorldOut(this);
		ClanController.toWorldOut(this);
		InventoryController.toWorldOut(this);
		SkillController.toWorldOut(this);
		CharacterController.toWorldOut(this);
		PartyController.toWorldOut(this, false);
		UserShopController.toStop(this);
		QuestController.toWorldOut(this);
		ChattingController.toWorldOut(this);
		FriendController.toWorldOut(this);
		MagicDollController.toWorldOut(this);

		// 메모리 초기화
		close();
	}

	@Override
	public void toReset(boolean world_out) {
		super.toReset(world_out);
		if (isDead()) {
			// 죽을 경우 이팩트
			setDeathEffect(false);

			try {
				if (world_out) {
					// 죽은상태로 월드를 나갈경우 좌표를 근처 마을로..
					// 성이 존재한다면 내성으로 (잊섬 제외)
					Kingdom k = KingdomController.find(this);
					// 아지트 확인.
					Agit a = AgitController.find(this);

					if (k != null && map != 70) {
						homeX = k.getX();
						homeY = k.getY();
						homeMap = k.getMap();
					} else if (a != null) {
						homeX = a.getAgitX();
						homeY = a.getAgitY();
						homeMap = a.getAgitMap();
					} else {
						TeleportHomeDatabase.toLocation(this);
					}

					x = homeX;
					y = homeY;
					map = homeMap;
				}

				// 다이상태 풀기.
				setDead(false);

				if (World.isBattleZone(getX(), getY(), getMap())) {
					setNowHp(getTotalHp());
				} else {
					// 체력 채우기.
					setNowHp(level);
					// food게이지 하향.
					if (getFood() > Lineage.MIN_FOOD)
						setFood(Lineage.MIN_FOOD);

					// gfx 복구
					gfx = classGfx;
					if (inv.getSlot(Lineage.SLOT_WEAPON) != null) {
						gfxMode = classGfxMode + inv.getSlot(Lineage.SLOT_WEAPON).getItem().getGfxMode();
					} else {
						gfxMode = classGfxMode;
					}
				}
				//
				PluginController.init(PcInstance.class, "toReset", this, world_out);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void toRevival(object o) {
		if (isDead()) {
			// 공성전중 부활이 불가능한지 확인.
			if (!Lineage.kingdom_war_revival) {
				Kingdom k = KingdomController.findKingdomLocation(this);
				if (k != null && k.isWar())
					return;
			}

			temp_object_1 = o;
			toSender(S_MessageYesNo.clone(BasePacketPooling.getPool(S_MessageYesNo.class), 321));
		}
	}

	@Override
	public void toRevivalFinal(object o) {
		if (isDead() && temp_object_1 != null) {
			setDeathEffect(false);
			// 리셋처리.
			super.toReset(false);
			// 다이상태 풀기.
			setDead(false);
			// 체력 채우기.
			setNowHp(temp_hp != -1 ? temp_hp : level);
			setNowMp(temp_mp != -1 ? temp_mp : getNowMp());
			// gfx_mode 복원
			if (inv.getSlot(Lineage.SLOT_WEAPON) != null) {
				gfxMode = classGfxMode + inv.getSlot(Lineage.SLOT_WEAPON).getItem().getGfxMode();
			} else {
				gfxMode = classGfxMode;
			}
			// 패킷 처리.
			toSender(S_ObjectRevival.clone(BasePacketPooling.getPool(S_ObjectRevival.class), temp_object_1, this),
					true);
			toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), this, 230), true);
			//
			temp_object_1 = null;
			temp_hp = temp_mp = -1;
		}
	}

	@Override
	public void toMoving(final int x, final int y, final int h) {
		lastMovingTime = System.currentTimeMillis();

		setMoving(true);
		CharacterController.toMoving(this);

		if (Util.isDistance(this.x, this.y, map, x, y, map, 1) || getGfx() == 369 || isTransparent()) {
			isFrameSpeed(Lineage.GFX_MODE_WALK);

			super.toMoving(x, y, h);

			if (World.get_map(x, y, map) == 127) {
				// 던전 이동 처리
				TimeDungeonDatabase.toMovingDungeon(this);

				if (Lineage.server_version <= 163) {
					// 163이하 버전 공성전 처리.
					// 현재 위치에 성정보 추출.
					Kingdom k = KingdomController.findKingdomLocation(this);
					// 공성중이면서, 옥좌라면 면류관 픽업처리를 통해 성주 변경처리하기.
					if (k != null && k.isWar() && k.isThrone(this)) {
						k.getCrown().toPickup(this);
					}
				}
			}
		} else {
			toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x04));
			toTeleport(this.x, this.y, map, false);
			toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x05));
		}

	}

	@Override
	public synchronized void toSender(BasePacket bp) {
		if (client != null) {
			client.toSender(bp);
		} else {
			BasePacketPooling.setPool(bp);
		}
	}

	@Override
	public void toPotal(int x, int y, int map) {
		resetAutoAttack();
		// 버그방지.
		if (World.get_map(map) == null) {
			toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x09));
			if (getGm() > 0)
				ChattingController.toChatting(this, map + "맵이 존재하지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if (isFishing()) {
			ChattingController.toChatting(this, "낚시중엔 텔레포트가 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x09));
			return;
		}
		homeX = x;
		homeY = y;
		homeMap = map;
		toSender(S_Potal.clone(BasePacketPooling.getPool(S_Potal.class), this.map, map));
		// 소환객체 텔레포트
		SummonController.toTeleport(this);
		MagicDollController.toTeleport(this);
	}

	@Override
	public void toTeleport(final int x, final int y, final int map, final boolean effect) {
		resetAutoAttack();
		// 버그방지.
		if (World.get_map(map) == null) {
			toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x09));
			if (getGm() > 0)
				ChattingController.toChatting(this, map + "맵이 존재하지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if (isFishing()) {
			ChattingController.toChatting(this, "낚시중엔 텔레포트가 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x09));
			return;
		}
		// 2.00이하 버전에서 텔레포트후 sp, mr 이 정상표현 안되는 문제로 인해 추가.
		// if (Lineage.server_version <= 200)
		// toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class),
		// this));

		super.toTeleport(x, y, map, effect);
		// 소환객체 텔레포트
		SummonController.toTeleport(this);
		MagicDollController.toTeleport(this);
	}

	@Override
	public void toTeleportRange(final int x, final int y, final int map, final boolean effect, int range) {
		resetAutoAttack();
		// 버그방지.
		if (World.get_map(map) == null) {
			toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x09));
			if (getGm() > 0)
				ChattingController.toChatting(this, map + "맵이 존재하지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if (isFishing()) {
			ChattingController.toChatting(this, "낚시중엔 텔레포트가 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x09));
			return;
		}
		// 2.00이하 버전에서 텔레포트후 sp, mr 이 정상표현 안되는 문제로 인해 추가.
		// if (Lineage.server_version <= 200)
		// toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class),
		// this));

		super.toTeleportRange(x, y, map, effect, range);
		// 소환객체 텔레포트
		SummonController.toTeleport(this);
		MagicDollController.toTeleport(this);
	}

	public void 칼렉풀기() {
		if (isDead()) {
			ChattingController.toChatting(this, "죽은 상태에선 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		} else if (isLock()) {
			ChattingController.toChatting(this, "기절 상태에선 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if (lastMovingTime + 2000 > System.currentTimeMillis()) {
			ChattingController.toChatting(this, "이동중에 칼렉풀기는 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if (getLastLackTime() < System.currentTimeMillis()) {
			setLastLackTime(System.currentTimeMillis() + (1000 * Lineage.sword_rack_delay));
		} else {
			ChattingController.toChatting(this, String.format("\\fR칼렉풀기의 딜레이는 %d초 입니다.", Lineage.sword_rack_delay),
					Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		resetAutoAttack();
		// 버그방지.
		if (World.get_map(map) == null) {
			toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x09));
			if (getGm() > 0)
				ChattingController.toChatting(this, map + "맵이 존재하지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if (isFishing()) {
			ChattingController.toChatting(this, "낚시중엔 텔레포트가 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x09));
			return;
		}

		super.toTeleport(getX(), getY(), getMap(), false);
		// super.toPotal(getX(), getY(), getMap());
		// 소환객체 텔레포트
		SummonController.toTeleport(this);
		MagicDollController.toTeleport(this);

		ChattingController.toChatting(this, "칼렉이 풀렸습니다.", Lineage.CHATTING_MODE_MESSAGE);
	}

	/**
	 * 매개변수 객체 에게물리공격을 가할때 처리하는 메서드.
	 */
	@Override
	public void toAttack(object o, int x, int y, boolean bow, int gfxMode, int alpha_dmg, boolean isTriple) {
		long time = System.currentTimeMillis();

		if (!isTriple && autoAttackTime > time) {
			return;
		}

		// 자동 칼질
		// if (o != null && isAutoAttack && !isTriple) {
		// autoAttackTarget = o;
		// targetX = o.getX();
		// targetY = o.getY();
		// autoAttackTime = time + SpriteFrameDatabase.getGfxFrameTime(this, getGfx(),
		// getGfxMode() + Lineage.GFX_MODE_ATTACK);
		// }

		if (!isTriple && !isActionCheck(false)) {
			return;
		}

		// 대상이 없고 세이프티존이 아닐경우 공격안됨.
		if (o == null && !World.isSafetyZone(getX(), getY(), getMap()))
			return;

		// ▼▼▼ [핵심 추가] 활(Bow) 공격 시 벽(장애물) 검사 ▼▼▼
		if (bow) {
			// Util.isAreaAttack: 나와 상대방 사이에 장애물이 없는지 확인 (false면 벽이 있음)
			if (o != null && !Util.isAreaAttack(this, o)) {
				// 벽에 막혔으므로 공격 중단 (화살 소모 X, 데미지 X)
				// 필요하다면 시스템 메시지 추가: ChattingController.toChatting(this, "장애물로 인해 공격할 수 없습니다.",
				// Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
		}
		// ▲▲▲ [여기까지 추가] ▲▲▲

		int attackAction = 0;
		int effect = 0;
		double dmg = 0;
		ItemInstance weapon = inv.getSlot(Lineage.SLOT_WEAPON);
		ItemInstance arrow = null;
		List<object> insideList = getInsideList();

		setFight(true);

		if (bow && weapon != null) {
			// 잠수용 허수아비 체크.
			if (isRestCracker) {
				arrow = setRestCrackerArrow();

				if (weapon.getItem().getEffect() > 0 && arrow == null) {
					effect = weapon.getItem().getEffect();
				} else {
					if (arrow != null)
						effect = arrow.getItem().getEffect();
				}

				if (arrow == null) {
					if (o instanceof RestCracker)
						endRestCracker();
				}
			} else {
				arrow = weapon.getItem().getType2().equalsIgnoreCase("gauntlet") ? inv.findThrowingKnife()
						: inv.findArrow();

				if (weapon.getItem().getEffect() > 0 && arrow == null) {
					effect = weapon.getItem().getEffect();
				} else {
					if (arrow != null)
						effect = arrow.getItem().getEffect();
				}
			}
		}

		// 좌표에따라 방향전환.
		heading = Util.calcheading(this, x, y);

		// 자동사냥 방지 확인.
		if (AutoHuntCheckController.checkMonster(o) && !AutoHuntCheckController.checkCount(this))
			return;

		// 장로 변신중이면 콜라이트닝 이팩트 표현.
		if (Lineage.server_version <= 200 && getGfx() == 3879)
			effect = Lineage.call_lighting_effect;

		// 공속 확인.
		if (isTriple || (isFrameSpeed(Lineage.GFX_MODE_ATTACK) && !isTransparent())) {
			// 죽엇는지 확인.
			if (!isDead()) {
				// 무게 확인. 82% 미만
				if (inv.isWeightPercent(82)) {
					if (isRestCracker) {
						dmg = getRestCrackerDmg(weapon, arrow, o);
					} else {
						if (o instanceof RestCracker) {
							ChattingController.toChatting(this,
									String.format("\\fY[%s] 소유자만 공격가능.", Lineage.rest_cracker_name),
									Lineage.CHATTING_MODE_MESSAGE);
						} else {
							// 데미지 추출
							dmg = DamageController.getDamage(this, o, bow, weapon, arrow, 0);
						}
					}

					attackAction = getGfxMode() + Lineage.GFX_MODE_ATTACK;

					// 치명타 이팩트 처리
					if (isCriticalEffect() && weapon != null) {
						if (weapon.getItem().getType2().equalsIgnoreCase("dagger"))
							effect = Lineage.CRITICAL_EFFECT_DAGGER;
						else if (weapon.getItem().getType2().equalsIgnoreCase("sword"))
							effect = Lineage.CRITICAL_EFFECT_SWORD;
						else if (weapon.getItem().getType2().equalsIgnoreCase("tohandsword"))
							effect = Lineage.CRITICAL_EFFECT_TWOHAND_SWORD;
						else if (weapon.getItem().getType2().equalsIgnoreCase("axe")
								|| weapon.getItem().getType2().equalsIgnoreCase("blunt"))
							effect = weapon.getItem().isTohand() ? Lineage.CRITICAL_EFFECT_TWOHAND_AXE
									: Lineage.CRITICAL_EFFECT_AXE;
						else if (weapon.getItem().getType2().equalsIgnoreCase("spear"))
							effect = Lineage.CRITICAL_EFFECT_SPEAR;
						else if (weapon.getItem().getType2().equalsIgnoreCase("wand")
								|| weapon.getItem().getType2().equalsIgnoreCase("staff"))
							effect = Lineage.CRITICAL_EFFECT_STAFF;
						else
							effect = bow ? 374 : Lineage.CRITICAL_EFFECT_NONE;

						// 군터, 켄라우헬, 헬바인, 질리언, 케레니스, 조우, 진 다크엘프, 하이엘프, 랭커 변신
						// 치명타시 특정모션 발동
						if (!isTriple && extraAttackMotion(0, getGfx()) && Util.random(0, 99) < 30) {
							if (!bow) {
								// 랭커 변신은 무기의 종류에 따라서 특정모션이 다르기때문에 다르게 처리
								if (extraAttackMotion(1, getGfx())) {
									if (effect == Lineage.CRITICAL_EFFECT_DAGGER)
										attackAction = 74;
									if (effect == Lineage.CRITICAL_EFFECT_SWORD
											|| effect == Lineage.CRITICAL_EFFECT_STAFF)
										attackAction = 76;
									if (effect == Lineage.CRITICAL_EFFECT_TWOHAND_SWORD
											|| effect == Lineage.CRITICAL_EFFECT_AXE
											|| effect == Lineage.CRITICAL_EFFECT_TWOHAND_AXE
											|| effect == Lineage.CRITICAL_EFFECT_SPEAR) {
										if (getGfx() == 411 || getGfx() == 419)
											attackAction = 75;
										else
											attackAction = 79;
									}
								} else {
									// 군주 랭커
									if (extraAttackMotion(2, getGfx()) && effect == Lineage.CRITICAL_EFFECT_SWORD)
										attackAction = 74;
									// 기사 랭커
									if (extraAttackMotion(6, getGfx())
											&& effect == Lineage.CRITICAL_EFFECT_TWOHAND_SWORD)
										attackAction = 74;
									// 요정 랭커
									if (extraAttackMotion(3, getGfx()) && effect == Lineage.CRITICAL_EFFECT_SWORD)
										attackAction = 74;
									// 마법사 랭커
									if (extraAttackMotion(4, getGfx()) && effect == Lineage.CRITICAL_EFFECT_STAFF)
										attackAction = 74;
									// 변신 하지않은 상태에서 특정 gfx 모션
									// 군주
									if ((getGfx() == Lineage.royal_male_gfx || getGfx() == Lineage.royal_female_gfx)
											&& effect == Lineage.CRITICAL_EFFECT_SWORD)
										attackAction = 55;
									// 기사
									if ((getGfx() == Lineage.knight_male_gfx || getGfx() == Lineage.knight_female_gfx)
											&& effect == Lineage.CRITICAL_EFFECT_SWORD)
										attackAction = 55;
									if ((getGfx() == Lineage.knight_male_gfx || getGfx() == Lineage.knight_female_gfx)
											&& effect == Lineage.CRITICAL_EFFECT_TWOHAND_SWORD)
										attackAction = 56;
								}
							} else if (bow && extraAttackMotion(5, getGfx())) {
								attackAction = 75;
							}
						}
					}

					if (dmg > 0) {
						// 무기에 따른 처리.
						if (weapon != null) {
							// 무기에게 공격했다는거 알리기.
							if (weapon.toDamage(this, o)) {
								dmg += weapon.toDamage((int) Math.round(dmg));
								effect = weapon.toDamageEffect();
								setCriticalEffect(false);

								if (bow) {
									if (effect > 0 && effect != 6288) {
										ServerBasePacket sbp = (ServerBasePacket) (S_ObjectEffect
												.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, effect));

										if (무기이펙트)
											toSender(sbp);

										if (insideList != null) {
											// 주위의 유저에게 패킷 보냄.
											for (object oo : insideList) {
												if (oo instanceof PcInstance) {
													if (oo.무기이펙트)
														oo.toSender(ServerBasePacket.clone(
																BasePacketPooling.getPool(ServerBasePacket.class),
																sbp.getBytes()));
												}
											}
										}
									}

									if (bow && weapon != null) {
										arrow = weapon.getItem().getType2().equalsIgnoreCase("gauntlet")
												? inv.findThrowingKnife()
												: inv.findArrow();

										if (weapon.getItem().getEffect() > 0 && arrow == null) {
											effect = weapon.getItem().getEffect();
										} else {
											if (effect != 6288) {
												if (arrow != null)
													effect = arrow.getItem().getEffect();
											}
										}
									}
								}
							}

							if (o != null && o instanceof PcInstance && weapon != null && weapon.getItem() != null
									&& !isAutoAttack) {
								int range = 1;

								switch (weapon.getItem().getType2()) {
									case "bow":
										range = Lineage.SEARCH_LOCATIONRANGE;
										break;
									case "spear":
										range = 2;
										break;
								}

								if (!Util.isDistance(this, o, range)) {
									if (effect != 66)
										effect = 0;

									dmg = 1;
								}
							}

							// 트리플 중첩시 데미지 감소.
							if (dmg > 0 && isTriple && Lineage_Balance.triple_damage_reduction > 0 && o != null
									&& o instanceof PcInstance) {
								if (0 < o.tripleAttackObjId) {
									if (System.currentTimeMillis() < o.tripleDamageTime) {
										if (o.tripleAttackObjId != getObjectId())
											dmg = dmg * Lineage_Balance.triple_damage_reduction;
									} else {
										o.tripleAttackObjId = 0;
									}
								} else {
									o.tripleAttackObjId = getObjectId();
									o.tripleDamageTime = System.currentTimeMillis()
											+ (int) (Lineage_Balance.triple_damage_reduction_time);
								}
							}
						}
						// 데미지 처리하는 구간

						ItemInstance item = getInventory().find("트리플 애로우(부스트)", 0, 1);
						if (item != null) {
							dmg += 20;
						}

						DamageController.toDamage(this, o,
								isTriple ? (int) Math.round(dmg * Lineage_Balance.triple_arrow_damage)
										: (int) Math.round(dmg),
								bow ? Lineage.ATTACK_TYPE_BOW : Lineage.ATTACK_TYPE_WEAPON);
					}

					if (effect == 2230) {
						ServerBasePacket sbp = (ServerBasePacket) (S_ObjectEffect
								.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, effect));

						if (무기이펙트)
							toSender(sbp);

						if (insideList != null) {
							// 주위의 유저에게 패킷 보냄.
							for (object oo : insideList) {
								if (oo instanceof PcInstance) {
									if (oo.무기이펙트)
										oo.toSender(ServerBasePacket.clone(
												BasePacketPooling.getPool(ServerBasePacket.class), sbp.getBytes()));
								}
							}
						}

						effect = 0;
					}

					// 현재 시간
					now_Time = System.currentTimeMillis();

					// 프레임 시간
					frame_Time = SpriteFrameDatabase.getGfxFrameTime(this, this.getGfx(), isTriple
							? (attackAction == Lineage.ACTION_TRIPLE_ARROW_2 ? attackAction : gfxMode)
							: attackAction);

					// 트리플 에로우 밀림 현상 개선
					if ((getClassType() == Lineage.LINEAGE_CLASS_ELF) && isTriple) {

						if ((isTriple && ((now_Time > ai_Time) || ((now_Time - ai_Time) < -345)))) {

							toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), this, o,
									isTriple ? (attackAction == Lineage.ACTION_TRIPLE_ARROW_2 ? attackAction : gfxMode)
											: attackAction,
									(int) Math.round(dmg), effect, bow, effect > 0, x, y), true);
						}

						// 마법 사용 모션 및 피격 모션 칼렉 개선
					} else if (!isTriple) {
						if (ai_Time <= now_Time) {

							toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), this, o,
									isTriple ? (attackAction == Lineage.ACTION_TRIPLE_ARROW_2 ? attackAction : gfxMode)
											: attackAction,
									(int) Math.round(dmg), effect, bow, effect > 0, x, y), true);

							if (ai_Time <= now_Time)
								ai_Time = System.currentTimeMillis() + (frame_Time - 5);
						} else {

							if (ai_Time <= now_Time)
								ai_Time = System.currentTimeMillis() + (frame_Time - 5);

						}

					} else {

						toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), this, o,
								isTriple ? (attackAction == Lineage.ACTION_TRIPLE_ARROW_2 ? attackAction : gfxMode)
										: attackAction,
								(int) Math.round(dmg), effect, bow, effect > 0, x, y), true);
					}

					if (!isCriticalEffect() && effect > 66)
						effect = 0;
					////
					//// // 자신에게 패킷 보냄.
					//// if (무기이펙트)
					//// toSender(sbp);
					//// else
					//// toSender(sbp1);
					//
					//// // 주위의 유저에게 패킷 보냄.
					//// if (insideList != null) {
					//// for (object oo : insideList) {
					//// if (oo instanceof PcInstance) {
					//// if (oo.무기이펙트)
					//// oo.toSender(ServerBasePacket.clone(BasePacketPooling.getPool(ServerBasePacket.class),
					//// sbp.getBytes()));
					//// else
					//// oo.toSender(ServerBasePacket.clone(BasePacketPooling.getPool(ServerBasePacket.class),
					//// sbp1.getBytes()));
					//// }
					//// }
					//// }
					////
					// 화살 갯수 하향
					ItemInstance arrow2 = getInventory().findArrow();
					if (arrow2 != null && !arrow2.getName().contains("블랙미스릴")) {
						if (bow && arrow != null && !arrow.getName().contains("블랙미스릴")) {
							inv.count(arrow, arrow.getCount() - 1, true);
						}
					}

					// 로봇에게 알리기.
					if (o instanceof RobotInstance && Util.isDistance(this, o, 1))
						o.toDamage(this, 0, Lineage.ATTACK_TYPE_WEAPON);

					// 크리티컬 이팩트 초기화
					setCriticalEffect(false);
					setCriticalMagicEffect(false);
				} else {
					// \f1소지품이 너무 무거워서 전투를 할 수 없습니다.
					toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 110));

				}
			}
		}
	}

	/**
	 * extra 액션이 존재하는 gfx인지 확인하는 함수
	 * 
	 * @param
	 * @return
	 *         2017-09-06
	 *         by all_night.
	 */
	public boolean extraAttackMotion(int mode, int gfx) {
		if (mode == 0) {
			if (gfx == Lineage.royal_male_gfx || gfx == Lineage.royal_female_gfx || gfx == Lineage.knight_male_gfx
					|| gfx == Lineage.knight_female_gfx)
				return true;
		}

		Poly poly = PolyDatabase.getPolyGfx(gfx);

		if (poly == null)
			return false;

		switch (mode) {
			case 0:
				if (poly.getPolyName().contains("랭커"))
					return false;
				else if (poly.getPolyName().contains("군터"))
					return true;
				else if (poly.getPolyName().contains("켄라우헬"))
					return true;
				else if (poly.getPolyName().contains("질리언"))
					return true;
				else if (poly.getPolyName().contains("헬바인"))
					return true;
				else if (poly.getPolyName().contains("조우"))
					return true;
				else if (poly.getPolyName().contains("케레니스"))
					return true;
				else if (poly.getPolyName().contains("진 다크엘프"))
					return true;
				else if (poly.getPolyName().contains("하이엘프"))
					return true;
				else if (poly.getPolyName().contains("반왕 세트"))
					return true;
				else if (poly.getPolyName().contains("케레니스 세트"))
					return true;
				break;
			case 1:
				if (!poly.getPolyName().contains("랭커"))
					return true;
				break;
			case 2:
				if (poly.getPolyName().contains("왕자 랭커"))
					return true;
				if (poly.getPolyName().contains("공주 랭커"))
					return true;
				break;
			case 3:
				if (poly.getPolyName().contains("남자요정 랭커"))
					return true;
				if (poly.getPolyName().contains("여자요정 랭커"))
					return true;
				break;
			case 4:
				if (poly.getPolyName().contains("남자법사 랭커"))
					return true;
				if (poly.getPolyName().contains("여자법사 랭커"))
					return true;
				break;
			case 5:
				if (poly.getPolyName().contains("진 다크엘프"))
					return true;
				else if (poly.getPolyName().contains("하이엘프"))
					return true;
				else if (poly.getPolyName().contains("질리언"))
					return true;
				else if (poly.getPolyName().contains("헬바인"))
					return true;
				else if (poly.getPolyName().contains("남자요정 랭커"))
					return true;
				else if (poly.getPolyName().contains("여자요정 랭커"))
					return true;
				break;
			case 6:
				if (poly.getPolyName().contains("남자기사 랭커"))
					return true;
				if (poly.getPolyName().contains("여자기사 랭커"))
					return true;
				break;
		}

		return false;
	}

	@Override
	public synchronized void toExp(object o, double exp) {
		//
		if (Lineage.open_wait) {
			ChattingController.toChatting(this, "오픈대기 상태에서는 경험치가 오르지 않습니다", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		if (PluginController.init(PcInstance.class, "", this, o, exp) != null)
			return;

		// 경험치 배율
		// 구간마다 경험치 반으로 감소
		if (level > 49) {
			switch (level) {
				case 50:
					exp /= Lineage.lv_50_exp_rate;
					break;
				case 51:
					exp /= Lineage.lv_51_exp_rate;
					break;
				case 52:
					exp /= Lineage.lv_52_exp_rate;
					break;
				case 53:
					exp /= Lineage.lv_53_exp_rate;
					break;
				case 54:
					exp /= Lineage.lv_54_exp_rate;
					break;
				case 55:
					exp /= Lineage.lv_55_exp_rate;
					break;
				case 56:
					exp /= Lineage.lv_56_exp_rate;
					break;
				case 57:
					exp /= Lineage.lv_57_exp_rate;
					break;
				case 58:
					exp /= Lineage.lv_58_exp_rate;
					break;
				case 59:
					exp /= Lineage.lv_59_exp_rate;
					break;
				case 60:
					exp /= Lineage.lv_60_exp_rate;
					break;
				case 61:
					exp /= Lineage.lv_61_exp_rate;
					break;
				case 62:
					exp /= Lineage.lv_62_exp_rate;
					break;
				case 63:
					exp /= Lineage.lv_63_exp_rate;
					break;
				case 64:
					exp /= Lineage.lv_64_exp_rate;
					break;
				case 65:
					exp /= Lineage.lv_65_exp_rate;
					break;
				case 66:
					exp /= Lineage.lv_66_exp_rate;
					break;
				case 67:
					exp /= Lineage.lv_67_exp_rate;
					break;
				case 68:
					exp /= Lineage.lv_68_exp_rate;
					break;
				case 69:
					exp /= Lineage.lv_69_exp_rate;
					break;
				case 70:
					exp /= Lineage.lv_70_exp_rate;
					break;
				case 71:
					exp /= Lineage.lv_71_exp_rate;
					break;
				case 72:
					exp /= Lineage.lv_72_exp_rate;
					break;
				case 73:
					exp /= Lineage.lv_73_exp_rate;
					break;
				case 74:
					exp /= Lineage.lv_74_exp_rate;
					break;
				case 75:
					exp /= Lineage.lv_75_exp_rate;
					break;
				case 76:
					exp /= Lineage.lv_76_exp_rate;
					break;
				case 77:
					exp /= Lineage.lv_77_exp_rate;
					break;
				case 78:
					exp /= Lineage.lv_78_exp_rate;
					break;
				case 79:
					exp /= Lineage.lv_79_exp_rate;
					break;
				case 80:
					exp /= Lineage.lv_80_exp_rate;
					break;
				case 81:
					exp /= Lineage.lv_81_exp_rate;
					break;
				case 82:
					exp /= Lineage.lv_82_exp_rate;
					break;
				case 83:
					exp /= Lineage.lv_83_exp_rate;
					break;
				case 84:
					exp /= Lineage.lv_84_exp_rate;
					break;
				case 85:
					exp /= Lineage.lv_85_exp_rate;
					break;
				case 86:
					exp /= Lineage.lv_86_exp_rate;
					break;
				case 87:
					exp /= Lineage.lv_87_exp_rate;
					break;
				case 88:
					exp /= Lineage.lv_88_exp_rate;
					break;
				case 89:
					exp /= Lineage.lv_89_exp_rate;
					break;
				default:
					exp /= Lineage.lv_90_exp_rate;
					break;
			}
		}

		// 동적 변경된 추가경험치 증가.
		if (getDynamicExp() > 0 && !(o instanceof FishExp))
			exp += exp * getDynamicExp();

		if (isBuffExpPotion() && !(o instanceof FishExp))
			exp *= 2;

		if (타임이벤트컨트롤러.isOpen && 타임이벤트컨트롤러.num == 1 && !(o instanceof FishExp)) {
			exp *= 1.20;
		}
		if (타임이벤트컨트롤러.isOpen && 타임이벤트컨트롤러.num == 4 && !(o instanceof FishExp)) {
			exp *= 1.50;
		}
		boolean 행운의인장 = false;
		if (this != null && this.getInventory() != null)
			행운의인장 = this.getInventory().find("행운의인장(20%)") == null ? false : true;
		boolean 대박의인장 = false;
		if (this != null && this.getInventory() != null)
			대박의인장 = this.getInventory().find("대박의인장(50%)") == null ? false : true;
		if (대박의인장)
			exp = exp + (exp * 0.5);
		else if (행운의인장)
			exp = exp + (exp * 0.2);

		exp *= Lineage.rate_exp;

		/*
		 * // 패널티 적용
		 * if (level >= Lineage.penalty_level)
		 * exp *= Lineage.penalty_exp;
		 * 
		 * if (isAutoHunt) {
		 * boolean 강화자동 = false;
		 * if (this != null && this.getInventory() != null)
		 * 강화자동 = this.getInventory().find("자동 사냥30일경100%/드50%") != null ||
		 * this.getInventory().find("자동 사냥7일경100%/드50%") != null ||
		 * this.getInventory().find("자동 사냥1일경100%/드50%") != null;
		 * if (강화자동) {
		 * exp = exp * 1;
		 * } else {
		 * exp = exp * 0.2;
		 * }
		 * //exp = exp * Lineage.is_auto_hunt_exp_percent;
		 * }
		 * 
		 * setExp(getExp() + exp);
		 * 
		 * if(getGm() > 0){
		 * ChattingController.toChatting(this, String.format("경험치 획득량 %f.",exp),
		 * Lineage.CHATTING_MODE_MESSAGE);
		 * 
		 * }
		 */
		// 패널티 적용
		if (level >= Lineage.penalty_level)
			exp *= Lineage.penalty_exp;

		// 자동 사냥 경험치 배율 적용 (콘프 값만 반영)
		if (isAutoHunt) {
			exp *= Lineage.is_auto_hunt_exp_percent; // 예: 0.2 = 20%
		}

		setExp(getExp() + exp);

		if (getGm() > 0) {
			ChattingController.toChatting(this, String.format("경험치 획득량 %f.", exp), Lineage.CHATTING_MODE_MESSAGE);
		}

		// 로그 기록.
		if (Log.isLog(this)) {
			int o_lv = 0;
			String o_name = null;
			int o_exp = 0;
			if (o instanceof MonsterInstance) {
				MonsterInstance mon = (MonsterInstance) o;
				o_lv = mon.getMonster().getLevel();
				o_name = mon.getMonster().getName();
				o_exp = mon.getMonster().getExp();
			}
			if (o instanceof Cracker) {
				o_name = "허수아비";
			}
			if (o instanceof FishExp)
				o_name = "낚시";
			if (o instanceof NpcInstance) {
				NpcInstance npc = (NpcInstance) o;
				o_name = npc.getNpc().getName();
			}
			if (Log.isLog(this))
				Log.appendExp(getRegisterDate(), getLevel(), (int) exp, (int) getExp(), o_lv, o_name, o_exp);
		}
	}
	/*
	 * private boolean isInRankRange(int rank, int rankClass) {
	 * if (rankClass == 1) return rank >= 1 && rank <= 5;
	 * if (rankClass == 2) return rank >= 6 && rank <= 15;
	 * if (rankClass == 3) return rank >= 16 && rank <= 30;
	 * return false;
	 * }
	 * 
	 * public void rankSystem() {
	 * int rank = RankController.getAllRank(getObjectId());
	 * this.rank = rank;
	 * this.lastRank = rank; // 필요 없으면 지워도 OK
	 * 
	 * boolean changed = false;
	 * 
	 * // 기존 랭커였는데 범위를 벗어나면 먼저 해제
	 * if (lastRankClass > 0 && !isInRankRange(rank, lastRankClass)) {
	 * if (lastRankClass == 1) {
	 * setDynamicHp(getDynamicHp() - 300);
	 * // setDynamicAc(getDynamicAc() - 5);
	 * // setDynamicSp(getDynamicSp() - 5);
	 * // setDynamicAddDmg(getDynamicAddDmg() - 5);
	 * // setDynamicAddDmgBow(getDynamicAddDmgBow() - 5);
	 * } else if (lastRankClass == 2) {
	 * setDynamicHp(getDynamicHp() - 200);
	 * // setDynamicAc(getDynamicAc() - 3);
	 * // setDynamicSp(getDynamicSp() - 3);
	 * // setDynamicAddDmg(getDynamicAddDmg() - 3);
	 * // setDynamicAddDmgBow(getDynamicAddDmgBow() - 3);
	 * } else if (lastRankClass == 3) {
	 * setDynamicHp(getDynamicHp() - 100);
	 * // setDynamicAc(getDynamicAc() - 1);
	 * // setDynamicSp(getDynamicSp() - 1);
	 * // setDynamicAddDmg(getDynamicAddDmg() - 1);
	 * // setDynamicAddDmgBow(getDynamicAddDmgBow() - 1);
	 * }
	 * ChattingController.toChatting(this, "[랭킹 시스템]: 랭킹 버프가 제거되었습니다.",
	 * Lineage.CHATTING_MODE_MESSAGE);
	 * lastRankClass = 0;
	 * changed = true;
	 * }
	 * 
	 * // 이번 랭크로 등급 계산
	 * int newRankClass = 0;
	 * if (rank >= 1 && rank <= 5) newRankClass = 1;
	 * else if (rank >= 6 && rank <= 15) newRankClass = 2;
	 * else if (rank >= 16 && rank <= 30)newRankClass = 3;
	 * 
	 * // 새 등급 적용
	 * if (newRankClass > 0 && newRankClass != lastRankClass) {
	 * lastRankClass = newRankClass;
	 * 
	 * ChattingController.toChatting(this,
	 * String.format("[랭킹 시스템]: %d등 랭커 버프가 적용됩니다.", rank),
	 * Lineage.CHATTING_MODE_MESSAGE);
	 * 
	 * if (newRankClass == 1) {
	 * setDynamicHp(getDynamicHp() + 300);
	 * // setDynamicAc(getDynamicAc() + 5);
	 * // setDynamicSp(getDynamicSp() + 5);
	 * // setDynamicAddDmg(getDynamicAddDmg() + 5);
	 * // setDynamicAddDmgBow(getDynamicAddDmgBow() + 5);
	 * ChattingController.toChatting(this, "[랭킹 시스템]: HP+300",
	 * Lineage.CHATTING_MODE_MESSAGE);
	 * } else if (newRankClass == 2) {
	 * setDynamicHp(getDynamicHp() + 200);
	 * // setDynamicAc(getDynamicAc() + 3);
	 * // setDynamicSp(getDynamicSp() + 3);
	 * // setDynamicAddDmg(getDynamicAddDmg() + 3);
	 * // setDynamicAddDmgBow(getDynamicAddDmgBow() + 3);
	 * ChattingController.toChatting(this, "[랭킹 시스템]: HP+200",
	 * Lineage.CHATTING_MODE_MESSAGE);
	 * } else if (newRankClass == 3) {
	 * setDynamicHp(getDynamicHp() + 100);
	 * // setDynamicAc(getDynamicAc() + 1);
	 * // setDynamicSp(getDynamicSp() + 1);
	 * // setDynamicAddDmg(getDynamicAddDmg() + 1);
	 * // setDynamicAddDmgBow(getDynamicAddDmgBow() + 1);
	 * ChattingController.toChatting(this, "[랭킹 시스템]: HP+100",
	 * Lineage.CHATTING_MODE_MESSAGE);
	 * }
	 * changed = true;
	 * }
	 * 
	 * if (changed) {
	 * toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.
	 * class), this));
	 * toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.
	 * class), this));
	 * }
	 * }
	 */

	// 아이콘 번호 정의 (서버 상황에 맞게 번호 수정 가능)
	private static final int ICON_RANK_CLASS_1 = 81; // 1~5등
	private static final int ICON_RANK_CLASS_2 = 82; // 6~15등
	private static final int ICON_RANK_CLASS_3 = 83; // 16~30등
	private static final int ICON_RANK_CLASS_4 = 84; // 31~50등

	private boolean isInRankRange(int rank, int rankClass) {
		if (rankClass == 1)
			return rank >= 1 && rank <= 5;
		if (rankClass == 2)
			return rank >= 6 && rank <= 15;
		if (rankClass == 3)
			return rank >= 16 && rank <= 30;
		if (rankClass == 4)
			return rank >= 31 && rank <= 50; // Class 4 추가
		return false;
	}

	private void removeRankIcon(int rankClass) {
		int iconId = 0;
		if (rankClass == 1)
			iconId = ICON_RANK_CLASS_1;
		else if (rankClass == 2)
			iconId = ICON_RANK_CLASS_2;
		else if (rankClass == 3)
			iconId = ICON_RANK_CLASS_3;
		else if (rankClass == 4)
			iconId = ICON_RANK_CLASS_4;

		if (iconId > 0) {
			SC_BUFFICON_NOTI.on(this, iconId, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	public void rankSystem() {
		int rank = RankController.getAllRank(getObjectId());
		this.rank = rank;

		boolean changed = false;

		// 1. 현재 등수 기반의 새로운 등급 계산
		int newRankClass = 0;
		if (rank >= 1 && rank <= 5)
			newRankClass = 1;
		else if (rank >= 6 && rank <= 15)
			newRankClass = 2;
		else if (rank >= 16 && rank <= 30)
			newRankClass = 3;
		else if (rank >= 31 && rank <= 50)
			newRankClass = 4; // Class 4 범위 설정

		// 2. 등 등급이 변동되었을 경우 (이전 등급 버프 및 아이콘 제거)
		if (lastRankClass > 0 && lastRankClass != newRankClass) {
			removeRankIcon(lastRankClass); // 이전 아이콘 삭제

			if (lastRankClass == 1)
				setDynamicHp(getDynamicHp() - 200);
			else if (lastRankClass == 2)
				setDynamicHp(getDynamicHp() - 150);
			else if (lastRankClass == 3)
				setDynamicHp(getDynamicHp() - 100);
			else if (lastRankClass == 4)
				setDynamicHp(getDynamicHp() - 50); // Class 4 버프 해제

			ChattingController.toChatting(this, "[랭킹 시스템]: 기존 랭커 버프가 해제되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			lastRankClass = 0;
			changed = true;
		}

		// 3. 새로운 등급 버프 및 아이콘 적용
		if (newRankClass > 0 && newRankClass != lastRankClass) {
			lastRankClass = newRankClass;
			int iconId = 0;
			int hpBonus = 0;

			if (newRankClass == 1) {
				iconId = ICON_RANK_CLASS_1;
				hpBonus = 200;
			} else if (newRankClass == 2) {
				iconId = ICON_RANK_CLASS_2;
				hpBonus = 150;
			} else if (newRankClass == 3) {
				iconId = ICON_RANK_CLASS_3;
				hpBonus = 100;
			} else if (newRankClass == 4) {
				iconId = ICON_RANK_CLASS_4;
				hpBonus = 50;
			}

			if (hpBonus > 0) {
				setDynamicHp(getDynamicHp() + hpBonus);
				// 아이콘 출력 (시간을 -1 또는 큰 값으로 주어 무제한 느낌 유도)
				SC_BUFFICON_NOTI.on(this, iconId, 86400, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);

				ChattingController.toChatting(this, String.format("[랭킹 시스템]: %d등 랭커 버프 적용 (HP+%d)", rank, hpBonus),
						Lineage.CHATTING_MODE_MESSAGE);
				changed = true;
			}
		}

		// 4. 스태틱 갱신 패킷 전송
		if (changed) {
			toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
			toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
		}
	}

	@Override
    public void toTimer(long time) {
        if (this.getInventory() != null) {
            // 현재 시간을 '초' 단위로 가져옴
            long currentTime = System.currentTimeMillis() / 1000; 
            
            for (lineage.world.object.instance.ItemInstance invenItem : this.getInventory().getList()) {
                if (invenItem == null || invenItem.getItem() == null) continue;
                
                // 1. 나비켓 아이템 설정값 (예: 2592000초 = 30일)
                long duration = invenItem.getItem().getExpireTime();
                
                if (duration > 0) {
                    // 2. 만약 아이템에 저장된 시간이 없다면(최초 인식), 현재시간 + 기간으로 세팅
                    if (invenItem.getDeleteTime() <= 0) {
                        invenItem.setDeleteTime(currentTime + duration);
                    }

                    // 3. ★ 핵심: 패킷을 보내기 전에 시간을 다시 한번 확인 시켜줍니다.
                    // 이 패킷(S_InventoryStatus)이 아이템의 '남은 시간'을 클라이언트로 보냅니다.
                    this.toSender(lineage.network.packet.server.S_InventoryStatus.clone(lineage.network.packet.BasePacketPooling.getPool(lineage.network.packet.server.S_InventoryStatus.class), invenItem));                    
                    // 4. 만료 처리 (현재 시간이 저장된 시간보다 크면 삭제)
                    if (invenItem.getDeleteTime() < currentTime) {
                        this.getInventory().count(invenItem, 0, true);
                        lineage.world.controller.ChattingController.toChatting(this, "[알림] 기간 만료로 삭제되었습니다.", 20);
                    }
                }
            }
        }


		// ▼▼▼ [2단계] 스킬 쿨타임(딜레이) 알림 시스템 (원본 유지) ▼▼▼
		if (isViewDelay() && getViewDelaySkillName() != null && getViewDelaySkillName().length() > 0
				&& this.magic_time < this.delay_magic && (this.delay_magic - time) / 1000 >= 0) {
			
			if ((this.delay_magic - time) / 1000 > 0) {
				lineage.world.controller.ChattingController.toChatting(this,
						"\\fU" + getViewDelaySkillName() + " 남은시간(초) " + (this.delay_magic - time) / 1000,
						lineage.share.Lineage.CHATTING_MODE_MESSAGE);
			}
			
			if ((this.delay_magic - time) / 1000 == 0) {
				this.magic_time = 0;
				this.delay_magic = 0;
				lineage.world.controller.ChattingController.toChatting(this, "\\fY재사용가능", lineage.share.Lineage.CHATTING_MODE_MESSAGE);
			}
		}
		// ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲


		// ▼▼▼ [3단계] 63번 맵 외 메테오 스트라이크 자동 압수 로직 (원본 유지) ▼▼▼
		if (this.getMap() != 63 && this.getInventory() != null && this.getInventory().find("메테오 스트라이크") != null) {
			lineage.world.object.instance.ItemInstance meteorItem = this.getInventory().find("메테오 스트라이크", 0, 1);
			if (meteorItem != null) {
				this.getInventory().count(meteorItem, 0, true);
			}
		}
		// ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
		

		if (보물찾기컨트롤러.isOpen && getMap() == 807) {

			if (this.getGfx() != 54) {

				ItemInstance weapon = getInventory().getSlot(Lineage.SLOT_WEAPON);

				if (weapon != null && weapon.isEquipped())
					weapon.toClick(this, null);

				setGfx(54);
				setGfxMode(0);

				super.toTeleport(getX(), getY(), getMap(), false);
			}

		}

		if (!보물찾기컨트롤러.isOpen && getInventory().find("보물상자 획득 점수") != null) {

			ItemInstance item = getInventory().find("보물상자 획득 점수", true);

			ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("보물 사냥꾼 주머니"));

			if (item.getCount() >= 10) {

				if (item != null && item.getCount() >= 30) {
					ii.setCount(3);
				}
				if (item != null && item.getCount() >= 50) {
					ii.setCount(5);
				}
				if (item != null && item.getCount() >= 70) {

					ii.setCount(7);

				}

				super.toGiveItem(null, ii, ii.getCount());

			}
			setGfx(getClassGfx());
			setGfxMode(0);
			getInventory().count(item, 0, true);

		}

		if (++testTime >= Lineage.time_ment && 타임이벤트컨트롤러.isOpen) {

			testTime = 0;

			if (타임이벤트컨트롤러.isOpen && 타임이벤트컨트롤러.num == 1) {

				ChattingController.toChatting(this, String.format("\\fU메티스: 타임이벤트 경험치 20퍼 획득 증가 [1시간]"),
						Lineage.CHATTING_MODE_MESSAGE);

			}
			if (타임이벤트컨트롤러.isOpen && 타임이벤트컨트롤러.num == 2) {

				ChattingController.toChatting(this, String.format("\\fU메티스: 타임이벤트 드랍률 20퍼 증가 [1시간]"),
						Lineage.CHATTING_MODE_MESSAGE);

			}
			if (타임이벤트컨트롤러.isOpen && 타임이벤트컨트롤러.num == 3) {

				ChattingController.toChatting(this, String.format("\\fU메티스: 보너스 상자 나올확률이 5퍼 증가  [1시간]"),
						Lineage.CHATTING_MODE_MESSAGE);

			}

			if (타임이벤트컨트롤러.isOpen && 타임이벤트컨트롤러.num == 4) {

				ChattingController.toChatting(this, String.format("\\fU메티스: 경험치 50퍼 획득 증가  [1시간]"),
						Lineage.CHATTING_MODE_MESSAGE);

			}
			if (타임이벤트컨트롤러.isOpen && 타임이벤트컨트롤러.num == 5) {

				ChattingController.toChatting(this, String.format("\\fU메티스:  타임이벤트 드랍률 40퍼 증가  [1시간]"),
						Lineage.CHATTING_MODE_MESSAGE);

			}
		}
		/*
		 * if(level >= 52 && this.getPclevel_gift_check() == 0){
		 * ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("52레벨 달성 보상"));
		 * this.setPclevel_gift_check(1);
		 * ii.setCount(1);
		 * super.toGiveItem(null, ii, ii.getCount());
		 * AccountDatabase.uplevelcheck(1,this.accountUid);
		 * }
		 */

		// 신규 혈맹 자동탈퇴
		if (getClanId() != 0 && getClanName().equalsIgnoreCase(Lineage.new_clan_name) && Lineage.is_new_clan_auto_out
				&& level >= Lineage.new_clan_max_level) {
			ClanController.toOut(this);
			ChattingController.toChatting(this, String.format("신규 혈맹은 %d레벨 이상 자동탈퇴 됩니다.", Lineage.new_clan_max_level),
					Lineage.CHATTING_MODE_MESSAGE);
		}
		if (this.getInventory() != null && !this.isWorldDelete()) {
			ItemInstance item = this.getInventory().find(ItemDatabase.find("오만의 탑 1층 지배 부적"));
			int check = 0;

			// 수량 체크
			for (ItemInstance check_item : this.getInventory().getList()) {
				if (check_item != null && check_item.getItem().getName().equalsIgnoreCase("오만의 탑 1층 지배 부적"))
					check = check + 1;
			}

			if (check == 0 && isSealBuff10) {
				this.setDynamicStr(this.getDynamicStr() - 3);
				this.setDynamicDex(this.getDynamicDex() - 3);
				this.setDynamicInt(this.getDynamicInt() - 3);
				this.setDynamicHp(this.getDynamicHp() - 100);
				// this.setDynamicMp(this.getDynamicMp() - 100);
				// this.setDynamicStunHit(this.getDynamicStunHit() - 0.1);
				isSealBuff10 = false;
				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
				ChattingController.toChatting(this, "[알림] 오만의 탑 1층 지배 부적 효과가 해제됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

			if (item != null) {
				// 정상적으로 효과 적용할 경우
				if (check == 1 && !isSealBuff10) {
					isSealBuff10 = true;
					this.setDynamicStr(this.getDynamicStr() + 3);
					this.setDynamicDex(this.getDynamicDex() + 3);
					this.setDynamicInt(this.getDynamicInt() + 3);
					this.setDynamicHp(this.getDynamicHp() + 100);
					// this.setDynamicMp(this.getDynamicMp() + 100);
					// this.setDynamicStunHit(this.getDynamicStunHit() + 0.1);
					ChattingController.toChatting(this,
							String.format("[알림] %s 효과 적용 : 힘덱인+3 HP+100", item.getItem().getName()),
							Lineage.CHATTING_MODE_MESSAGE);
					toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
					toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));

					// 정상적으로 효과 적용 중이면서 인첸트 레벨이 변경 될 경우
				}
			}
		}
		if (this.getInventory() != null && !this.isWorldDelete()) {
			ItemInstance item = this.getInventory().find(ItemDatabase.find("오만의 탑 2층 지배 부적"));
			int check = 0;

			// 수량 체크
			for (ItemInstance check_item : this.getInventory().getList()) {
				if (check_item != null && check_item.getItem().getName().equalsIgnoreCase("오만의 탑 2층 지배 부적"))
					check = check + 1;
			}

			if (check == 0 && isSealBuff20) {
				this.setDynamicAddDmg(this.getDynamicAddDmg() - 2);
				this.setDynamicAddDmgBow(this.getDynamicAddDmgBow() - 2);
				this.setDynamicAddHit(this.getDynamicAddHit() - 2);
				this.setDynamicAddHitBow(this.getDynamicAddHitBow() - 2);
				this.setDynamicSp(this.getDynamicSp() - 2);
				this.setDynamicHp(this.getDynamicHp() - 100);
				isSealBuff20 = false;
				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
				ChattingController.toChatting(this, "[알림] 오만의 탑 2층 지배 부적 효과가 해제됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

			if (item != null) {
				// 정상적으로 효과 적용할 경우
				if (check == 1 && !isSealBuff20) {
					isSealBuff20 = true;
					this.setDynamicAddDmg(this.getDynamicAddDmg() + 2);
					this.setDynamicAddDmgBow(this.getDynamicAddDmgBow() + 2);
					this.setDynamicAddHit(this.getDynamicAddHit() + 2);
					this.setDynamicAddHitBow(this.getDynamicAddHitBow() + 2);
					this.setDynamicSp(this.getDynamicSp() + 2);
					this.setDynamicHp(this.getDynamicHp() + 100);
					ChattingController.toChatting(this,
							String.format("[알림] %s 효과 적용 : 추타+2 명중+2 SP+2 HP+100", item.getItem().getName()),
							Lineage.CHATTING_MODE_MESSAGE);
					toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
					toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));

					// 정상적으로 효과 적용 중이면서 인첸트 레벨이 변경 될 경우
				}
			}
		}
		if (this.getInventory() != null && !this.isWorldDelete()) {
			ItemInstance item = this.getInventory().find(ItemDatabase.find("오만의 탑 3층 지배 부적"));
			int check = 0;

			// 수량 체크
			for (ItemInstance check_item : this.getInventory().getList()) {
				if (check_item != null && check_item.getItem().getName().equalsIgnoreCase("오만의 탑 3층 지배 부적"))
					check = check + 1;
			}

			if (check == 0 && isSealBuff30) {
				this.setDynamicCritical(this.getDynamicCritical() - 5);
				this.setDynamicBowCritical(this.getDynamicBowCritical() - 5);
				this.setDynamicHp(this.getDynamicHp() - 100);
				isSealBuff30 = false;
				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
				ChattingController.toChatting(this, "[알림] 오만의 탑 3층 지배 부적 효과가 해제됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

			if (item != null) {
				// 정상적으로 효과 적용할 경우
				if (check == 1 && !isSealBuff30) {
					isSealBuff30 = true;
					this.setDynamicCritical(this.getDynamicCritical() + 5);
					this.setDynamicBowCritical(this.getDynamicBowCritical() + 5);
					this.setDynamicHp(this.getDynamicHp() + 100);
					ChattingController.toChatting(this,
							String.format("[알림] %s 효과 적용 : 근거리치명+5 원거리치명+5 HP+100", item.getItem().getName()),
							Lineage.CHATTING_MODE_MESSAGE);
					toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
					toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));

					// 정상적으로 효과 적용 중이면서 인첸트 레벨이 변경 될 경우
				}
			}
		}
		if (this.getInventory() != null && !this.isWorldDelete()) {
			ItemInstance item = this.getInventory().find(ItemDatabase.find("오만의 탑 4층 지배 부적"));
			int check = 0;

			// 수량 체크
			for (ItemInstance check_item : this.getInventory().getList()) {
				if (check_item != null && check_item.getItem().getName().equalsIgnoreCase("오만의 탑 4층 지배 부적"))
					check = check + 1;
			}

			if (check == 0 && isSealBuff40) {
				this.setDynamicStunHit(this.getDynamicStunHit() - 0.05);
				this.setDynamicStunResist(this.getDynamicStunResist() - 0.05);
				this.setDynamicHp(this.getDynamicHp() - 100);
				isSealBuff40 = false;
				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
				ChattingController.toChatting(this, "[알림] 오만의 탑 4층 지배 부적 효과가 해제됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

			if (item != null) {
				// 정상적으로 효과 적용할 경우
				if (check == 1 && !isSealBuff40) {
					isSealBuff40 = true;
					this.setDynamicStunHit(this.getDynamicStunHit() + 0.05);
					this.setDynamicStunResist(this.getDynamicStunResist() + 0.05);
					this.setDynamicHp(this.getDynamicHp() + 100);
					ChattingController.toChatting(this,
							String.format("[알림] %s 효과 적용 : 스턴적중+5 스턴내성+5 HP+100", item.getItem().getName()),
							Lineage.CHATTING_MODE_MESSAGE);
					toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
					toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));

					// 정상적으로 효과 적용 중이면서 인첸트 레벨이 변경 될 경우
				}
			}
		}
		if (this.getInventory() != null && !this.isWorldDelete()) {
			ItemInstance item = this.getInventory().find(ItemDatabase.find("오만의 탑 5층 지배 부적"));
			int check = 0;

			// 수량 체크
			for (ItemInstance check_item : this.getInventory().getList()) {
				if (check_item != null && check_item.getItem().getName().equalsIgnoreCase("오만의 탑 5층 지배 부적"))
					check = check + 1;
			}

			if (check == 0 && isSealBuff50) {
				this.setDynamicAddPvpDmg(this.getDynamicAddDmg() - 3);
				this.setDynamicAddPvpDmg(this.getDynamicAddDmgBow() - 3);
				this.setDynamicAddPvpDmg(this.getDynamicMagicDmg() - 3);
				this.setDynamicHp(this.getDynamicHp() - 100);
				isSealBuff50 = false;
				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
				ChattingController.toChatting(this, "[알림] 오만의 탑 5층 지배 부적 효과가 해제됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

			if (item != null) {
				// 정상적으로 효과 적용할 경우
				if (check == 1 && !isSealBuff50) {
					isSealBuff50 = true;
					this.setDynamicAddPvpDmg(this.getDynamicAddDmg() + 3);
					this.setDynamicAddPvpDmg(this.getDynamicAddDmgBow() + 3);
					this.setDynamicAddPvpDmg(this.getDynamicMagicDmg() + 3);
					this.setDynamicHp(this.getDynamicHp() + 100);
					ChattingController.toChatting(this,
							String.format("[알림] %s 효과 적용 : 근뎀,원뎀,마뎀+3 HP+100", item.getItem().getName()),
							Lineage.CHATTING_MODE_MESSAGE);
					toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
					toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));

					// 정상적으로 효과 적용 중이면서 인첸트 레벨이 변경 될 경우
				}
			}
		}
		if (this.getInventory() != null && !this.isWorldDelete()) {
			ItemInstance item = this.getInventory().find(ItemDatabase.find("오만의 탑 6층 지배 부적"));
			int check = 0;

			// 수량 체크
			for (ItemInstance check_item : this.getInventory().getList()) {
				if (check_item != null && check_item.getItem().getName().equalsIgnoreCase("오만의 탑 6층 지배 부적"))
					check = check + 1;
			}

			if (check == 0 && isSealBuff60) {
				this.setDynamicStr(this.getDynamicStr() - 3);
				this.setDynamicDex(this.getDynamicDex() - 3);
				this.setDynamicInt(this.getDynamicInt() - 3);
				this.setDynamicHp(this.getDynamicHp() - 100);
				isSealBuff60 = false;
				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
				ChattingController.toChatting(this, "[알림] 오만의 탑 6층 지배 부적 효과가 해제됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

			if (item != null) {
				// 정상적으로 효과 적용할 경우
				if (check == 1 && !isSealBuff60) {
					isSealBuff60 = true;
					this.setDynamicStr(this.getDynamicStr() + 3);
					this.setDynamicDex(this.getDynamicDex() + 3);
					this.setDynamicInt(this.getDynamicInt() + 3);
					this.setDynamicHp(this.getDynamicHp() + 100);
					ChattingController.toChatting(this,
							String.format("[알림] %s 효과 적용 : 힘덱인+3 HP+100", item.getItem().getName()),
							Lineage.CHATTING_MODE_MESSAGE);
					toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
					toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));

					// 정상적으로 효과 적용 중이면서 인첸트 레벨이 변경 될 경우
				}
			}
		}
		if (this.getInventory() != null && !this.isWorldDelete()) {
			ItemInstance item = this.getInventory().find(ItemDatabase.find("오만의 탑 7층 지배 부적"));
			int check = 0;

			// 수량 체크
			for (ItemInstance check_item : this.getInventory().getList()) {
				if (check_item != null && check_item.getItem().getName().equalsIgnoreCase("오만의 탑 7층 지배 부적"))
					check = check + 1;
			}

			if (check == 0 && isSealBuff70) {
				this.setDynamicAddDmg(this.getDynamicAddDmg() - 3);
				this.setDynamicAddDmgBow(this.getDynamicAddDmgBow() - 3);
				this.setDynamicAddHit(this.getDynamicAddHit() - 3);
				this.setDynamicAddHitBow(this.getDynamicAddHitBow() - 3);
				this.setDynamicSp(this.getDynamicSp() - 3);
				this.setDynamicHp(this.getDynamicHp() - 100);
				isSealBuff70 = false;
				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
				ChattingController.toChatting(this, "[알림] 오만의 탑 7층 지배 부적 효과가 해제됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

			if (item != null) {
				// 정상적으로 효과 적용할 경우
				if (check == 1 && !isSealBuff70) {
					isSealBuff70 = true;
					this.setDynamicAddDmg(this.getDynamicAddDmg() + 3);
					this.setDynamicAddDmgBow(this.getDynamicAddDmgBow() + 3);
					this.setDynamicAddHit(this.getDynamicAddHit() + 3);
					this.setDynamicAddHitBow(this.getDynamicAddHitBow() + 3);
					this.setDynamicSp(this.getDynamicSp() + 3);
					this.setDynamicHp(this.getDynamicHp() + 100);
					ChattingController.toChatting(this,
							String.format("[알림] %s 효과 적용 : 추타+3 명중+3 SP+3 HP+100", item.getItem().getName()),
							Lineage.CHATTING_MODE_MESSAGE);
					toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
					toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));

					// 정상적으로 효과 적용 중이면서 인첸트 레벨이 변경 될 경우
				}
			}
		}
		if (this.getInventory() != null && !this.isWorldDelete()) {
			ItemInstance item = this.getInventory().find(ItemDatabase.find("오만의 탑 8층 지배 부적"));
			int check = 0;

			// 수량 체크
			for (ItemInstance check_item : this.getInventory().getList()) {
				if (check_item != null && check_item.getItem().getName().equalsIgnoreCase("오만의 탑 8층 지배 부적"))
					check = check + 1;
			}

			if (check == 0 && isSealBuff80) {
				this.setDynamicCritical(this.getDynamicCritical() - 10);
				this.setDynamicBowCritical(this.getDynamicBowCritical() - 10);
				this.setDynamicHp(this.getDynamicHp() - 100);
				isSealBuff80 = false;
				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
				ChattingController.toChatting(this, "[알림] 오만의 탑 8층 지배 부적 효과가 해제됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

			if (item != null) {
				// 정상적으로 효과 적용할 경우
				if (check == 1 && !isSealBuff80) {
					isSealBuff80 = true;
					this.setDynamicCritical(this.getDynamicCritical() + 10);
					this.setDynamicBowCritical(this.getDynamicBowCritical() + 10);
					this.setDynamicHp(this.getDynamicHp() + 100);
					ChattingController.toChatting(this,
							String.format("[알림] %s 효과 적용 : 근거리치명+10 원거리치명+10 HP+100", item.getItem().getName()),
							Lineage.CHATTING_MODE_MESSAGE);
					toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
					toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));

					// 정상적으로 효과 적용 중이면서 인첸트 레벨이 변경 될 경우
				}
			}
		}
		if (this.getInventory() != null && !this.isWorldDelete()) {
			ItemInstance item = this.getInventory().find(ItemDatabase.find("오만의 탑 9층 지배 부적"));
			int check = 0;

			// 수량 체크
			for (ItemInstance check_item : this.getInventory().getList()) {
				if (check_item != null && check_item.getItem().getName().equalsIgnoreCase("오만의 탑 9층 지배 부적"))
					check = check + 1;
			}

			if (check == 0 && isSealBuff90) {
				this.setDynamicStunHit(this.getDynamicStunHit() - 0.10);
				this.setDynamicStunResist(this.getDynamicStunResist() - 0.10);
				this.setDynamicHp(this.getDynamicHp() - 100);
				isSealBuff90 = false;
				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
				ChattingController.toChatting(this, "[알림] 오만의 탑 9층 지배 부적 효과가 해제됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

			if (item != null) {
				// 정상적으로 효과 적용할 경우
				if (check == 1 && !isSealBuff90) {
					isSealBuff90 = true;
					this.setDynamicStunHit(this.getDynamicStunHit() + 0.10);
					this.setDynamicStunResist(this.getDynamicStunResist() + 0.10);
					this.setDynamicHp(this.getDynamicHp() + 100);
					ChattingController.toChatting(this,
							String.format("[알림] %s 효과 적용 : 스턴적중+10 스턴내성+10 HP+100", item.getItem().getName()),
							Lineage.CHATTING_MODE_MESSAGE);
					toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
					toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));

					// 정상적으로 효과 적용 중이면서 인첸트 레벨이 변경 될 경우
				}
			}
		}
		if (this.getInventory() != null && !this.isWorldDelete()) {
			ItemInstance item = this.getInventory().find(ItemDatabase.find("오만의 탑 10층 지배 부적"));
			int check = 0;

			// 수량 체크
			for (ItemInstance check_item : this.getInventory().getList()) {
				if (check_item != null && check_item.getItem().getName().equalsIgnoreCase("오만의 탑 10층 지배 부적"))
					check = check + 1;
			}

			if (check == 0 && isSealBuff100) {
				this.setDynamicAddPvpDmg(this.getDynamicAddPvpDmg() - 3);
				this.setDynamicAddPvpReduction(this.getDynamicAddPvpReduction() - 3);
				this.setDynamicHp(this.getDynamicHp() - 100);
				isSealBuff100 = false;
				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
				ChattingController.toChatting(this, "[알림] 오만의 탑 10층 지배 부적 효과가 해제됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

			if (item != null) {
				// 정상적으로 효과 적용할 경우
				if (check == 1 && !isSealBuff100) {
					isSealBuff100 = true;
					this.setDynamicAddPvpDmg(this.getDynamicAddPvpDmg() + 3);
					this.setDynamicAddPvpReduction(this.getDynamicAddPvpReduction() + 3);
					this.setDynamicHp(this.getDynamicHp() + 100);
					ChattingController.toChatting(this,
							String.format("[알림] %s 효과 적용 : PVP추타+3 PVP리덕+3 HP+100", item.getItem().getName()),
							Lineage.CHATTING_MODE_MESSAGE);
					toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
					toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));

					// 정상적으로 효과 적용 중이면서 인첸트 레벨이 변경 될 경우
				}
			}
		}
		/*
		 * // ── [고급 불멸의 가호] 1/2/3+개 → (1/2/3) 적용 (리덕/PVP리덕)
		 * ─────────────────────────────
		 * if (this.getInventory() != null && !this.isWorldDelete()) {
		 * // (1) 개수 세기
		 * int count = 0;
		 * for (ItemInstance it : this.getInventory().getList()) {
		 * if (it == null || it.getItem() == null)
		 * continue;
		 * String n = it.getItem().getName();
		 * if (n.equalsIgnoreCase("고급 불멸의 가호") || n.equalsIgnoreCase("고급 불멸의 가호(각인)")) {
		 * long c = it.getCount();
		 * count += (c > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) c;
		 * }
		 * }
		 * 
		 * // (2) 새 보너스 값 결정: 0, 1, 2, 3 (3개 이상은 3)
		 * final int newVal = (count <= 0) ? 0 : (count == 1 ? 1 : (count == 2 ? 2 :
		 * 3));
		 * 
		 * // (선택) 경험치/아데나 보너스
		 * final double newExp = (newVal > 0) ? Lineage.immortality_premium_exp_percent
		 * : 0.0;
		 * final double newAdena = (newVal > 0) ?
		 * Lineage.immortality_premium_aden_percent : 0.0;
		 * 
		 * boolean needReapply = false;
		 * if (!isImmortalityPremiumBuff && newVal > 0)
		 * needReapply = true;
		 * else if (isImmortalityPremiumBuff && immortalityPremiumAppliedVal != newVal)
		 * needReapply = true;
		 * else if (this.premiumExpBonus != newExp || this.premiumAdenaBonus !=
		 * newAdena)
		 * needReapply = true;
		 * 
		 * // (3) 해제: 0개가 되었거나, 적용 상태인데 newVal=0
		 * if (newVal == 0 && (isImmortalityPremiumBuff || immortalityPremiumAppliedVal
		 * > 0)) {
		 * // 이전 값 되돌리기
		 * if (immortalityPremiumAppliedVal != 0) {
		 * // [기존 옵션 주석 처리]
		 * // this.setDynamicAddDmg( this.getDynamicAddDmg() -
		 * // immortalityPremiumAppliedVal); // 추타
		 * // this.setDynamicAddHit( this.getDynamicAddHit() -
		 * // immortalityPremiumAppliedVal); // 공성/명중
		 * // this.setDynamicAddHitBow( this.getDynamicAddHitBow() -
		 * // immortalityPremiumAppliedVal); // 공성/명중
		 * // this.setDynamicSp( this.getDynamicSp() - immortalityPremiumAppliedVal); //
		 * SP
		 * 
		 * // [신규 옵션 해제] 리덕션 및 PVP 리덕션 제거
		 * this.setDynamicReduction(this.getDynamicReduction() -
		 * immortalityPremiumAppliedVal);
		 * this.setDynamicAddPvpReduction(this.getDynamicAddPvpReduction() -
		 * immortalityPremiumAppliedVal);
		 * }
		 * this.premiumExpBonus = 0.0;
		 * this.premiumAdenaBonus = 0.0;
		 * 
		 * isImmortalityPremiumBuff = false;
		 * immortalityPremiumAppliedVal = 0;
		 * 
		 * toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.
		 * class), this));
		 * toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.
		 * class), this));
		 * ChattingController.toChatting(this, "[알림] 고급 불멸의 가호 효과가 해제되었습니다.",
		 * Lineage.CHATTING_MODE_MESSAGE);
		 * }
		 * 
		 * // (4) 적용/갱신
		 * if (newVal > 0 && needReapply) {
		 * // 기존 값 제거 (갱신을 위해 먼저 뺌)
		 * if (immortalityPremiumAppliedVal != 0) {
		 * // [기존 옵션 주석 처리]
		 * // this.setDynamicAddDmg( this.getDynamicAddDmg() -
		 * // immortalityPremiumAppliedVal);
		 * // this.setDynamicAddHit( this.getDynamicAddHit() -
		 * // immortalityPremiumAppliedVal);
		 * // this.setDynamicAddHitBow( this.getDynamicAddHitBow() -
		 * // immortalityPremiumAppliedVal);
		 * // this.setDynamicSp( this.getDynamicSp() - immortalityPremiumAppliedVal);
		 * 
		 * // [신규 옵션 갱신 전 제거]
		 * this.setDynamicReduction(this.getDynamicReduction() -
		 * immortalityPremiumAppliedVal);
		 * this.setDynamicAddPvpReduction(this.getDynamicAddPvpReduction() -
		 * immortalityPremiumAppliedVal);
		 * }
		 * 
		 * // 새 값 적용 (리덕션/PVP리덕션)
		 * // [기존 옵션 주석 처리]
		 * // this.setDynamicAddDmg( this.getDynamicAddDmg() + newVal);
		 * // this.setDynamicAddHit( this.getDynamicAddHit() + newVal);
		 * // this.setDynamicAddHitBow( this.getDynamicAddHitBow() + newVal);
		 * // this.setDynamicSp( this.getDynamicSp() + newVal);
		 * 
		 * // [신규 옵션 적용]
		 * this.setDynamicReduction(this.getDynamicReduction() + newVal); // 일반 리덕션
		 * this.setDynamicAddPvpReduction(this.getDynamicAddPvpReduction() + newVal); //
		 * PVP 리덕션
		 * 
		 * // 경험치/아데나
		 * this.premiumExpBonus = newExp;
		 * this.premiumAdenaBonus = newAdena;
		 * 
		 * isImmortalityPremiumBuff = true;
		 * immortalityPremiumAppliedVal = newVal;
		 * 
		 * toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.
		 * class), this));
		 * toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.
		 * class), this));
		 * 
		 * // 멘트 수정 (리덕션 정보 출력)
		 * ChattingController.toChatting(this,
		 * // String.format("\\fY고급가호:경험치+%.0f%%,아데나획득+%.0f%%, 추타+%d, 공성+%d, SP+%d",
		 * String.format("\\fY고급가호:경험치+%.0f%%,아데나+%.0f%%, 리덕션+%d, PVP리덕+%d",
		 * newExp * 100, newAdena * 100, newVal, newVal),
		 * Lineage.CHATTING_MODE_MESSAGE);
		 * }
		 * }
		 * //
		 * ─────────────────────────────────────────────────────────────────────────────
		 */

		// ── [고급 불멸의 가호] 1/2/3+개 → (1/2/3) 적용 (리덕/PVP리덕) ─────────────────────────────
		if (this.getInventory() != null && !this.isWorldDelete()) {
			// (1) 개수 세기
			int count = 0;
			for (ItemInstance it : this.getInventory().getList()) {
				if (it == null || it.getItem() == null)
					continue;
				String n = it.getItem().getName();
				if (n.equalsIgnoreCase("고급 불멸의 가호") || n.equalsIgnoreCase("고급 불멸의 가호(각인)")) {
					long c = it.getCount();
					count += (c > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) c;
				}
			}

			// (2) 새 보너스 값 결정: 0, 1, 2, 3 (3개 이상은 3)
			final int newVal = (count <= 0) ? 0 : (count == 1 ? 1 : (count == 2 ? 2 : 3));

			// (선택) 경험치/아데나 보너스
			final double newExp = (newVal > 0) ? Lineage.immortality_premium_exp_percent : 0.0;
			final double newAdena = (newVal > 0) ? Lineage.immortality_premium_aden_percent : 0.0;

			boolean needReapply = false;
			if (!isImmortalityPremiumBuff && newVal > 0)
				needReapply = true;
			else if (isImmortalityPremiumBuff && immortalityPremiumAppliedVal != newVal)
				needReapply = true;
			else if (this.premiumExpBonus != newExp || this.premiumAdenaBonus != newAdena)
				needReapply = true;

			// (3) 해제: 0개가 되었거나, 적용 상태인데 newVal=0
			if (newVal == 0 && (isImmortalityPremiumBuff || immortalityPremiumAppliedVal > 0)) {
				// [아이콘 삭제] 기존 등급 아이콘 모두 끄기 (85, 86, 87)
				SC_BUFFICON_NOTI.on(this, 85, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
				SC_BUFFICON_NOTI.on(this, 86, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
				SC_BUFFICON_NOTI.on(this, 87, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);

				if (immortalityPremiumAppliedVal != 0) {
					this.setDynamicReduction(this.getDynamicReduction() - immortalityPremiumAppliedVal);
					this.setDynamicAddPvpReduction(this.getDynamicAddPvpReduction() - immortalityPremiumAppliedVal);
				}
				this.premiumExpBonus = 0.0;
				this.premiumAdenaBonus = 0.0;
				isImmortalityPremiumBuff = false;
				immortalityPremiumAppliedVal = 0;

				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
				ChattingController.toChatting(this, "[알림] 고급 불멸의 가호 효과가 해제되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

			// (4) 적용/갱신
			if (newVal > 0 && needReapply) {
				// 먼저 모든 가호 아이콘을 끈 뒤, 현재 개수에 맞는 아이콘만 켭니다.
				SC_BUFFICON_NOTI.on(this, 85, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
				SC_BUFFICON_NOTI.on(this, 86, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
				SC_BUFFICON_NOTI.on(this, 87, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);

				int currentIcon = (newVal == 1) ? 85 : (newVal == 2 ? 86 : 87);

				// [핵심 수정]: REMAINING_TYPE_ANYTIME 을 사용하여 시간 숫자를 없앰
				// 세 번째 인자인 시간 값은 무시되지만, 보통 -1이나 0을 넣습니다.
				SC_BUFFICON_NOTI.on(this, currentIcon, -1, 0);

				// 기존 능력치 제거
				if (immortalityPremiumAppliedVal != 0) {
					this.setDynamicReduction(this.getDynamicReduction() - immortalityPremiumAppliedVal);
					this.setDynamicAddPvpReduction(this.getDynamicAddPvpReduction() - immortalityPremiumAppliedVal);
				}

				// 새 능력치 적용
				this.setDynamicReduction(this.getDynamicReduction() + newVal);
				this.setDynamicAddPvpReduction(this.getDynamicAddPvpReduction() + newVal);

				this.premiumExpBonus = newExp;
				this.premiumAdenaBonus = newAdena;
				isImmortalityPremiumBuff = true;
				immortalityPremiumAppliedVal = newVal;

				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));

				// ChattingController.toChatting(this,
				// String.format("\\fY고급가호:경험치+%.0f%%,아데나+%.0f%%, 리덕션+%d, PVP리덕+%d",
				// newExp * 100, newAdena * 100, newVal, newVal),
				// Lineage.CHATTING_MODE_MESSAGE);
			}
		}

		if (this.getInventory() != null && !this.isWorldDelete()) {
			ItemInstance item = this.getInventory().find(ItemDatabase.find("전투가호"));
			if (item == null)
				item = this.getInventory().find(ItemDatabase.find("전투가호(각인)"));
			int check = 0;

			// 현재 인벤토리에 존재하는 '전투가호' 또는 '전투가호(각인)' 개수 체크
			for (ItemInstance check_item : this.getInventory().getList()) {
				if (check_item != null && check_item.getItem() != null) {
					String name = check_item.getItem().getName();
					if (name.equalsIgnoreCase("전투가호") || name.equalsIgnoreCase("전투가호(각인)")) {
						check++;
					}
				}
			}

			// 아이템이 사라졌을 때 효과 해제 (인벤토리에 없는 경우)
			if (check == 0 && isSealBuff) {
				// this.setDynamicAddDmg(this.getDynamicAddDmg() - 3);
				// this.setDynamicAddDmgBow(this.getDynamicAddDmgBow() - 3);
				this.setDynamicAddPvpDmg(this.getDynamicAddPvpDmg() - 5);
				this.setDynamicAddPvpReduction(this.getDynamicAddPvpReduction() - 50);
				// this.setDynamicSp(this.getDynamicSp() - 3);
				this.setDynamicHp(this.getDynamicHp() - 500);

				isSealBuff = false;

				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));

				ChattingController.toChatting(this, "[알림] 전투가호 효과가 해제됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

			// 아이템이 존재하면서, 효과가 적용되지 않은 경우 효과 적용
			if (item != null && check == 1 && !isSealBuff) {
				isSealBuff = true;

				// this.setDynamicAddDmg(this.getDynamicAddDmg() + 3);
				// this.setDynamicAddDmgBow(this.getDynamicAddDmgBow() + 3);
				this.setDynamicAddPvpDmg(this.getDynamicAddPvpDmg() + 5);
				this.setDynamicAddPvpReduction(this.getDynamicAddPvpReduction() + 50);
				// this.setDynamicSp(this.getDynamicSp() - 3);
				this.setDynamicHp(this.getDynamicHp() + 500);

				ChattingController.toChatting(this,
						String.format("[알림] %s 효과적용:pvp데미지+5,pvp리덕+50,HP+500", item.getItem().getName()),
						Lineage.CHATTING_MODE_MESSAGE);

				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
			}
		}

		if (this.getInventory() != null && !this.isWorldDelete()) {
			ItemInstance item = this.getInventory().find(ItemDatabase.find("순간이동 지배 반지"));
			int check = 0;

			// 현재 인벤토리에 존재하는 '순간이동 지배 반지' 개수 체크
			for (ItemInstance check_item : this.getInventory().getList()) {
				if (check_item != null && check_item.getItem() != null
						&& check_item.getItem().getName().equalsIgnoreCase("순간이동 지배 반지"))
					check++;
			}

			// 아이템이 사라졌을 때 효과 해제 (인벤토리에 없는 경우)
			if (check == 0 && isSealBuff2) {

				this.setDynamicAddDmg(this.getDynamicAddDmg() - 3);
				this.setDynamicAddDmgBow(this.getDynamicAddDmgBow() - 3);
				this.setDynamicSp(this.getDynamicSp() - 3);
				this.setDynamicAddPvpDmg(this.getDynamicAddPvpDmg() - 30);
				this.setDynamicHp(this.getDynamicHp() - 300);

				isSealBuff2 = false;
				Seal_Level2 = 0; // 💡 Seal_Level2 초기화 추가

				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));

				ChattingController.toChatting(this, "[알림] 순간이동 지배 반지 효과가 해제됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

			// 아이템이 존재하면서, 효과가 적용되지 않은 경우 효과 적용
			if (item != null && check >= 1 && !isSealBuff2) { // 💡 `check >= 1` 조건 변경
				isSealBuff2 = true;
				Seal_Level2 = item.getEnLevel(); // 💡 아이템의 인첸트 레벨 저장

				this.setDynamicAddDmg(this.getDynamicAddDmg() + 3);
				this.setDynamicAddDmgBow(this.getDynamicAddDmgBow() + 3);
				this.setDynamicSp(this.getDynamicSp() + 3);
				this.setDynamicAddPvpDmg(this.getDynamicAddPvpDmg() + 30);
				this.setDynamicHp(this.getDynamicHp() + 300);

				ChattingController.toChatting(this,
						String.format("[알림] %s:추타+3,PVP데미지+30,SP+3,HP+300", item.getItem().getName()),
						Lineage.CHATTING_MODE_MESSAGE);

				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
			}
		}
		/*
		 * if (this.getInventory() != null && !this.isWorldDelete()) {
		 * // 1. 아이템 인스턴스 찾기 (이름으로)
		 * ItemInstance prem = this.getInventory().find(ItemDatabase.find(Lineage.
		 * immortality_premium_item_name));
		 * int count = 0;
		 * 
		 * // 2. 인벤토리 전체에서 '고급 불멸의 가호' 개수 세기
		 * for (ItemInstance check_item : this.getInventory().getList()) {
		 * if (check_item != null && check_item.getItem() != null &&
		 * check_item.getItem().getName().equalsIgnoreCase(Lineage.
		 * immortality_premium_item_name)) {
		 * count++;
		 * }
		 * }
		 * 
		 * // 3. 효과 해제: 더 이상 아이템이 없고, 효과가 적용중이면 옵션 해제
		 * if (count == 0 && isImmortalityPremiumBuff) {
		 * isImmortalityPremiumBuff = false;
		 * this.setDynamicAddPvpReduction(this.getDynamicAddPvpReduction() - 3);
		 * this.premiumExpBonus = 0.0;
		 * this.premiumAdenaBonus = 0.0;
		 * 
		 * // 능력치 갱신 패킷 전송
		 * toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.
		 * class), this));
		 * toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.
		 * class), this));
		 * ChattingController.toChatting(this, "[알림] 고급 불멸의 가호 효과가 해제되었습니다.",
		 * Lineage.CHATTING_MODE_MESSAGE);
		 * }
		 * 
		 * // 4. 효과 적용: 아이템이 1개 이상, 아직 미적용이면 옵션 적용
		 * if (prem != null && count >= 1 && !isImmortalityPremiumBuff) {
		 * isImmortalityPremiumBuff = true;
		 * this.setDynamicAddPvpReduction(this.getDynamicAddPvpReduction() + 3);
		 * this.premiumExpBonus = Lineage.immortality_premium_exp_percent;
		 * this.premiumAdenaBonus = Lineage.immortality_premium_aden_percent;
		 * 
		 * // 능력치 갱신 패킷 전송
		 * toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.
		 * class), this));
		 * toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.
		 * class), this));
		 * ChattingController.toChatting(this,
		 * String.format("\\fY고급가호 : 경험치+%.0f%%,아데나획득+%.0f%%,PVP리덕+3",
		 * Lineage.immortality_premium_exp_percent * 100,
		 * Lineage.immortality_premium_aden_percent * 100),
		 * Lineage.CHATTING_MODE_MESSAGE);
		 * }
		 * }
		 * 
		 */
		if (this.getInventory() != null && !this.isWorldDelete()) {
			ItemInstance item = this.getInventory().find(ItemDatabase.find("신화변신 지배 반지"));
			if (item == null)
				item = this.getInventory().find(ItemDatabase.find("신화변신 지배 반지(각인)"));
			int check = 0;

			// 현재 인벤토리에 존재하는 '변신 지배 반지' 또는 '변신 지배 반지(각인)' 개수 체크
			for (ItemInstance check_item : this.getInventory().getList()) {
				if (check_item != null && check_item.getItem() != null) {
					String name = check_item.getItem().getName();
					if (name.equalsIgnoreCase("신화변신 지배 반지") || name.equalsIgnoreCase("변신 조종 지배 반지")) {
						check++;
					}
				}
			}

			// 아이템이 사라졌을 때 효과 해제 (인벤토리에 없는 경우)
			if (check == 0 && isSealBuff110) {
				this.setDynamicSp(this.getDynamicSp() - 3);
				this.setDynamicReduction(this.getDynamicReduction() - 3);
				this.setDynamicAddPvpReduction(this.getDynamicAddPvpReduction() - 30);
				this.setDynamicHp(this.getDynamicHp() - 300);

				isSealBuff110 = false; // 효과 해제
				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
				ChattingController.toChatting(this, "[알림] 신화변신 지배 반지 효과가 해제됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

			// 아이템이 존재하면서, 효과가 적용되지 않은 경우만 효과 적용
			if (item != null && check > 0 && !isSealBuff110) {
				isSealBuff110 = true; // 효과 적용됨을 표시

				this.setDynamicSp(this.getDynamicSp() + 3);
				this.setDynamicReduction(this.getDynamicReduction() + 3);
				this.setDynamicAddPvpReduction(this.getDynamicAddPvpReduction() + 30);
				this.setDynamicHp(this.getDynamicHp() + 300);

				ChattingController.toChatting(this,
						String.format("[알림] %s:리덕션+3,PVP리덕션+30,SP+3,HP+300", item.getItem().getName()),
						Lineage.CHATTING_MODE_MESSAGE);

				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
			}
		}

		if (this.getInventory() != null && !this.isWorldDelete()) {
			ItemInstance item = this.getInventory().find(ItemDatabase.find("오만의 탑 환상의 지배 부적"));
			int check = 0;

			// 수량 체크
			for (ItemInstance check_item : this.getInventory().getList()) {
				if (check_item != null && check_item.getItem().getName().equalsIgnoreCase("오만의 탑 환상의 지배 부적"))
					check = check + 1;
			}

			if (check == 0 && isSealBuff120) {
				this.setDynamicReduction(this.getDynamicReduction() - 5);
				this.setDynamicAddPvpReduction(this.getDynamicAddPvpReduction() - 50);
				this.setDynamicHp(this.getDynamicHp() - 500);

				isSealBuff120 = false;
				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
				ChattingController.toChatting(this, "[알림] 오만의 탑 환상의 지배 부적 효과가 해제됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

			if (item != null) {
				// 정상적으로 효과 적용할 경우
				if (check == 1 && !isSealBuff120) {
					isSealBuff120 = true;
					this.setDynamicReduction(this.getDynamicReduction() + 5);
					this.setDynamicAddPvpReduction(this.getDynamicAddPvpReduction() + 50);
					this.setDynamicHp(this.getDynamicHp() + 500);

					ChattingController.toChatting(this,
							String.format("[알림] %s:PVP리덕션+50,리덕션+5,HP+500", item.getItem().getName()),
							Lineage.CHATTING_MODE_MESSAGE);
					toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
					toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));

					// 정상적으로 효과 적용 중이면서 인첸트 레벨이 변경 될 경우
				}
			}
		}

		if (this.getInventory() != null && !this.isWorldDelete()) {
			ItemInstance item = this.getInventory().find(ItemDatabase.find("신성한 룬"));
			int check = 0;

			// 수량 체크
			for (ItemInstance check_item : this.getInventory().getList()) {
				if (check_item != null && check_item.getItem().getName().equalsIgnoreCase("신성한 룬"))
					check = check + 1;
			}

			if (check == 0 && isSealBuff130) {
				this.setDynamicAc(getDynamicAc() - 5);
				this.setDynamicMr(this.getDynamicMr() - 20);
				this.setDynamicHp(this.getDynamicHp() - 500);
				isSealBuff130 = false;
				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
				ChattingController.toChatting(this, "[알림] 신성한 룬 효과가 해제됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

			if (item != null) {
				// 정상적으로 효과 적용할 경우
				if (check == 1 && !isSealBuff130) {
					isSealBuff130 = true;
					this.setDynamicAc(getDynamicAc() + 5);
					this.setDynamicMr(this.getDynamicMr() + 20);
					this.setDynamicHp(this.getDynamicHp() + 500);
					ChattingController.toChatting(this,
							String.format("[알림] %s 효과 적용:AC+5 MR+20 HP+500", item.getItem().getName()),
							Lineage.CHATTING_MODE_MESSAGE);
					toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
					toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));

					// 정상적으로 효과 적용 중이면서 인첸트 레벨이 변경 될 경우
				}
			}
		}

		// 오만의탑 정상
		if (getMap() == 200 && getGm() == 0
				&& (getClanId() == 0 || getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
			if (getClanId() == 0 && !Lineage.is_no_clan_oman_top) {
				ChattingController.toChatting(this, "혈맹이 없으면 오만의탑 정상을 이용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);

				int[] loc = Lineage.getHomeXY();
				toTeleport(loc[0], loc[1], loc[2], true);
			}

			if (getClanName().equalsIgnoreCase(Lineage.new_clan_name) && !Lineage.is_new_clan_oman_top) {
				ChattingController.toChatting(this, "신규 혈맹은 이용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);

				int[] loc = Lineage.getHomeXY();
				toTeleport(loc[0], loc[1], loc[2], true);
			}
		}

		// 테베라스 사막
		if (!테베라스컨트롤러.isOpen && getGm() == 0 && (getMap() == 781)) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!월드보스컨트롤러.isOpen && getMap() == 1400 && !월드보스컨트롤러.isWait) {

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!얼던컨트롤러.isOpen && getGm() == 0 && (getMap() == 74)) {

			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}
		// 지옥
		if (!지옥컨트롤러.isOpen && getGm() == 0 && getMap() == 666) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}
		if (!지하수로컨트롤러.isOpen && getGm() == 0 && getMap() == 85) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!악마왕의영토컨트롤러.isOpen && getGm() == 0 && getMap() == 5167) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!칠흑던전컨트롤러.isOpen && getGm() == 0 && getMap() == 811) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!칠흑던전3층컨트롤러.isOpen && getGm() == 0 && getMap() == 809) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!칠흑던전4층컨트롤러.isOpen && getGm() == 0 && getMap() == 810) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!보물찾기컨트롤러.isOpen && getGm() == 0 && getMap() == 807) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!펭귄사냥컨트롤러.isOpen && getGm() == 0 && getMap() == 63) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!마족신전컨트롤러.isOpen && getGm() == 0 && getMap() == 410) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!고무컨트롤러.isOpen && getGm() == 0 && getMap() == 400) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!정무컨트롤러.isOpen && getGm() == 0 && getMap() == 430) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!그신컨트롤러.isOpen && getGm() == 0 && getMap() == 523) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!기감컨트롤러.isOpen && getGm() == 0 && getMap() == 56) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!고라스컨트롤러.isOpen && getGm() == 0 && getMap() == 2004) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!드워프컨트롤러.isOpen && getGm() == 0 && getMap() == 15450) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!오만1층컨트롤러.isOpen && getGm() == 0 && getMap() == 101) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!오만2층컨트롤러.isOpen && getGm() == 0 && getMap() == 102) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!오만3층컨트롤러.isOpen && getGm() == 0 && getMap() == 103) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!오만4층컨트롤러.isOpen && getGm() == 0 && getMap() == 104) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!오만5층컨트롤러.isOpen && getGm() == 0 && getMap() == 105) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!오만6층컨트롤러.isOpen && getGm() == 0 && getMap() == 106) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!오만7층컨트롤러.isOpen && getGm() == 0 && getMap() == 107) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!오만8층컨트롤러.isOpen && getGm() == 0 && getMap() == 108) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!오만9층컨트롤러.isOpen && getGm() == 0 && getMap() == 109) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!오만10층컨트롤러.isOpen && getGm() == 0 && getMap() == 110) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!오만정상컨트롤러.isOpen && getGm() == 0 && getMap() == 200) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (!라바던전컨트롤러.isOpen && getGm() == 0
				&& (getMap() == 451 || getMap() == 452 || getMap() == 453 || getMap() == 454 || getMap() == 455 ||
						getMap() == 456 || getMap() == 457 || getMap() == 460 ||
						getMap() == 463 || getMap() == 464 || getMap() == 465 || getMap() == 466 || getMap() == 467
						|| getMap() == 468 ||
						getMap() == 470 || getMap() == 471 || getMap() == 472 || getMap() == 473 || getMap() == 474
						|| getMap() == 475 ||
						getMap() == 476 || getMap() == 477 || getMap() == 478 || getMap() == 479 ||
						getMap() == 490 || getMap() == 491 || getMap() == 492 || getMap() == 493 || getMap() == 494
						|| getMap() == 495 || getMap() == 496)) {
			if (isAutoHunt) {
				endAutoHunt(false, false);
			}

			int[] loc = Lineage.getHomeXY();
			toTeleport(loc[0], loc[1], loc[2], true);
		}

		if (isAutoHunt) {
			boolean isMap = false;
			for (int map : Lineage.auto_hunt_map_list) {
				if (map == getMap()) {
					isMap = true;
					break;
				}
			}
			if (!isMap) {
				ChattingController.toChatting(this, "           \\fY자동 사냥을 사용할 수 없는 위치입니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				endAutoHunt(false, false);
			}
		}

		try {
			// 성혈이 아닐 경우 내성 입장 못함
			Kingdom k = KingdomController.findKingdomInsideLocation(this);
			if (getGm() == 0 && k != null) {
				Kingdom temp = KingdomController.find(this);
				if (temp == null || temp.getUid() != k.getUid()) {
					int[] loc = Lineage.getHomeXY();
					toTeleport(loc[0], loc[1], loc[2], true);
				}
			}
		} catch (Exception e) {

		}

		// 바포메트 시스템
		if (Lineage.is_batpomet_system)
			BaphometSystemController.setBaphomet(this);

		// 자동매입
		autoHuntItemSell();

		if (!Lineage.is_batpomet_system && isBaphomet) {
			BaphometSystemController.removeBaphomet(this);
			isBaphomet = false;
		}

		// 낚시 컨트롤러
		if (isFishing() && !isWorldDelete() && isFishingZone())
			FishingController.startFishing(this);

		// 기란감옥 던전 시간 확인
		if (Lineage.is_giran_dungeon_time && TimeDungeonDatabase.isTimeDungeon(getMap())
				&& (getMap() == 53 || getMap() == 54 || getMap() == 55 || getMap() == 56)) {
			if (--giran_dungeon_time < 1 && getGm() == 0)
				TimeDungeonDatabase.isTimeDungeonFinal(this, 0);
		}

		// 서버 메세지 표현 처리.
		if (Common.SERVER_MESSAGE && message_time <= time) {
			message_time = time + Common.SERVER_MESSAGE_TIME;
			for (String msg : Common.SERVER_MESSAGE_LIST)
				ChattingController.toChatting(this, msg, Lineage.CHATTING_MODE_MESSAGE);
		}

		// 현재 진행된 time값과 비교하여 24시간이 오바됫을경우 0으로 초기화
		if (getPkTime() > 0 && getPkTime() + (1000 * 60 * 60 * 24) <= time)
			setPkTime(0);

		if (Lineage.open_wait) {
			// 오픈대기시 아이템 자동 지급.
			if (Lineage.is_world_open_wait_item && open_wait_item_time <= time) {
				if (open_wait_item_time != 0) {
					// 아이템 지급
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find(Lineage.world_open_wait_item));
					if (ii != null) {
						ii.setCount(Util.random(Lineage.world_open_wait_item_min, Lineage.world_open_wait_item_max));
						super.toGiveItem(null, ii, ii.getCount());
					}
				}
				open_wait_item_time = time + Lineage.world_open_wait_item_delay;
			}
		} else {
			// 아이템 자동 지급.
			/*
			 * if (Lineage.world_premium_item_is && premium_item_time <= time) {
			 * if (premium_item_time != 0) {
			 * // 아이템 지급
			 * ItemInstance ii =
			 * ItemDatabase.newInstance(ItemDatabase.find(Lineage.world_premium_item));
			 * if (ii != null) {
			 * ii.setCount(Util.random(Lineage.world_premium_item_min,
			 * Lineage.world_premium_item_max));
			 * super.toGiveItem(null, ii, ii.getCount());
			 * }
			 * }
			 * premium_item_time = time + Lineage.world_premium_item_delay;
			 * }
			 */
			if (Lineage.world_premium_item_is && premium_item_time <= time) {
				if (premium_item_time != 0) {
					// 캐릭터 위치 확인
					int x = getX();
					int y = getY();
					int map = getMap();

					// 특정 위치에 있을 때만 아이템 지급
					if (map == 4 && x >= 33401 && x <= 33456 && y >= 32784 && y <= 32837) {
						ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find(Lineage.world_premium_item));
						if (ii != null) {
							ii.setCount(Util.random(Lineage.world_premium_item_min, Lineage.world_premium_item_max));
							super.toGiveItem(null, ii, ii.getCount());
						}
					}
				}
				premium_item_time = time + Lineage.world_premium_item_delay;
			}
		}

		// 팀대전 에러 처리.
		checkTeamBattle(false);
		if (getMap() != Lineage.teamBattleMap && getGm() < 1 && (getGfx() == 369 || isTransparent())) {
			setTransparent(false);
			setGfx(getClassGfx());
			toTeleport(getX(), getY(), getMap(), false);
		}

		// 전체 채팅 매크로
		if (isMacro && macroMsg != null && macroMsg.length() > 0) {
			if (--macroDelay < 1) {
				macroDelay = Lineage.chatting_global_macro_delay;
				ChattingController.toChatting(this, macroMsg, Lineage.CHATTING_MODE_GLOBAL);
			}
		}

		//
		PluginController.init(PcInstance.class, "toTimer", this, time);
	}

	public void itemTime() {
		if (!isWorldDelete()) {
			try {
				for (ItemInstance item : inv.getList()) {
					if (item.getItemNowTime() > 0) {
						toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));

					}

				}
			} catch (Exception e) {
				// 예외 정보 출력
				System.println(e.getMessage());
				e.printStackTrace();
			}
		}

		try {

			if (!this.isWorldDelete()) {

				for (PcInstance pc : World.getPcList()) {
					if (!this.isWorldDelete()) {
						for (ItemInstance i : inv.getList()) {

							if (i.getItemNowTime() > 0 && !i.getItem().getName().equalsIgnoreCase("양초")) {

								SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

								Calendar cal = Calendar.getInstance();

								cal.add(Calendar.DAY_OF_MONTH, 0);

								int year = cal.get(Calendar.YEAR);
								int month = cal.get(Calendar.MONTH) + 1;
								int date = cal.get(Calendar.DATE);
								int hour = cal.get(Calendar.HOUR_OF_DAY);
								int minute = cal.get(Calendar.MINUTE);
								int second = cal.get(Calendar.SECOND);

								String to = Integer.toString(year);
								String to2 = String.format("%02d", month);
								String to3 = String.format("%02d", date);
								String to4 = String.format("%02d", hour);
								String to5 = String.format("%02d", minute);
								String to6 = String.format("%02d", second);
								String to7 = to + to2 + to3 + to4 + to5 + to6;

								long i2 = i.getItemNowTime();
								long to9 = Long.parseLong(to7);

								String to8 = Long.toString(i2);

								// 아이템에 설정된 날짜
								try {
									// 아이템에 설정된 날짜
									Date date2 = sdf.parse(to8);
									// 오늘 시간
									Date date3 = sdf.parse(to7);

									if (to9 >= i2) {

										if (i.isEquipped())
											// 착용되잇을경우 해제.
											i.toClick(pc, null);

										ChattingController.toChatting(pc,
												String.format(i.getItem().getName() + " 기간이 만료하여 아이템이 삭제됩니다."),
												Lineage.CHATTING_MODE_MESSAGE);
										inv.remove(i, true);

									}

								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								// 오늘날짜

							}
						}

					}
				}
			}
		} catch (Exception e) {

		}

	}

	/**
	 * 결투장 관련 메서드.
	 * 2018-05-02
	 * by all-night.
	 */
	public void battleZone() {
		// 결투장 입장
		if (World.isBattleZone(getX(), getY(), getMap()) && !isBattlezone) {
			isBattlezone = true;
			ChattingController.toChatting(this, "결투장에 입장하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
		}

		// 결투장 피바
		if (!World.isBattleZone(getX(), getY(), getMap()) && isBattlezone) {
			ChattingController.toChatting(this, "결투장을 퇴장하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
			toSender(S_ObjectCriminal.clone(BasePacketPooling.getPool(S_ObjectCriminal.class), this, 0), true);

			isBattlezone = false;
		}

		if (World.isBattleZone(getX(), getY(), getMap()) && isBattlezone)
			toSender(S_ObjectCriminal.clone(BasePacketPooling.getPool(S_ObjectCriminal.class), this, 1), true);

		if (Lineage.is_battle_zone_hp_bar && isBattlezone) {
			for (object o : getInsideList()) {
				if (o instanceof PcInstance && ((PcInstance) o).isBattlezone) {
					toSender(S_ObjectHitratio.clone(BasePacketPooling.getPool(S_ObjectHitratio.class), (Character) o,
							true));
				}
			}
		}
	}

	/**
	 * 51레벨 이상 스탯 보너스 계산 메서드.
	 * 2018-05-02
	 * by all-night.
	 */
	public void setStat() {
		// 레벨 51이상시 스탯보너스 부분
		if (level > 50
				&& 50 + getLvStr() + getLvDex() + getLvCon() + getLvInt() + getLvWis() + getLvCha() + getLevelUpStat()
						- getElixir() < level
				&& getResetBaseStat() == 0 && getResetLevelStat() == 0)
			setLevelUpStat(getLevelUpStat() + 1);
	}

	@Override
	public void toGiveItem(object o, ItemInstance item, long count) {
		// 같은 혈맹이외에 다른 대상에게 아이템 줄수 없음.
		if (o != null && !(o instanceof RobotInstance) && o instanceof PcInstance) {
			if (o.getClanId() == 0 || getClanId() == 0 || o.getClanId() != getClanId())
				return;
		}

		if (!getInventory().isWeightPercent(82)) {
			ChattingController.toChatting(o, "대상에게 아이템을 더이상 줄 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		super.toGiveItem(o, item, count);
	}

	@Override
	public boolean isAutoPickup() {
		return auto_pickup;
	}

	@Override
	public void setAutoPickup(boolean is) {
		auto_pickup = is;
	}

	@Override
	public boolean isHpbar() {
		return is_hpbar;
	}

	@Override
	public void setHpbar(boolean is_hpbar) {
		this.is_hpbar = is_hpbar;
	}

	public byte[] getDbInterface() {
		return db_interface;
	}

	public void setDbInterface(byte[] dbInterface) {
		db_interface = dbInterface;
	}

	public int getElixir() {
		return elixir;
	}

	public void setElixir(int elixir) {
		this.elixir = elixir;
	}

	public int getSpeedHackWarningCounting() {
		return SpeedhackWarningCounting;
	}

	public void setSpeedHackWarningCounting(int SpeedhackWarningCounting) {
		this.SpeedhackWarningCounting = SpeedhackWarningCounting;
	}

	public void savePet() {
		Summon s = SummonController.find(this);
		if (s != null) {
			Connection con = null;
			try {
				con = DatabaseConnection.getLineage();
				// 모든 펫 저장.
				SummonController.toSave(con, this);
				// 모든 펫 제거 하면서 펫목걸이도 갱신.
				s.removeAllPet();
			} catch (Exception e) {
				lineage.share.System.println(PetMasterInstance.class.toString() + " : toPush(PcInstance pc)");
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con);
			}
		}
	}

	/**
	 * 자동물약 함수.
	 * 2018-05-10
	 * by all-night.
	 */
	public void autoPotion() {
		if (getInventory() != null && !isWorldDelete() && !isInvis() && !isTransparent() && !isDead() && !isLock()
				&& !isLockHigh() && !isLockLow()) {
			ItemInstance item = null;

			for (ItemInstance potion : getInventory().getList()) {
				if (potion instanceof HealingPotion) {
					if (potion.getItem() != null && potion.getItem().getName().equalsIgnoreCase(autoPotionName)) {
						item = potion;
						break;
					}
				}
			}

			if (item != null) {
				if (item.isClick(this))
					item.toClick(this, null);
			} else {
				ChattingController.toChatting(this, "[자동 물약] 설정된 물약이 모두 소모되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
				autoPotionName = null;
			}
		}
	}


	/**
	 * 자동칼질 타겟 정보 초기화.
	 * 2019-02-14
	 * by connector12@nate.com
	 */
	public void resetAutoAttack() {
		autoAttackTarget = null;
		autoAttackTime = 0L;
		targetX = 0;
		targetY = 0;
	}

	/**
	 * 자동칼질 비활성화 메소드.
	 * 2019-07-07
	 * by connector12@nate.com
	 */
	public void cancelAutoAttack() {
		isAutoAttack = false;
		autoAttackTarget = null;
		autoAttackTime = 0L;
		targetX = 0;
		targetY = 0;
		ChattingController.toChatting(this, "자동칼질이 비활성화 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
	}

	/**
	 * 자동물약 PvP 여부 확인 메소드.
	 * 2019-07-04
	 * by connector12@nate.com
	 */
	public void checkAutoPotionPvP(Character cha) {
		if (cha instanceof PcInstance && !Lineage.is_pvp_auto_potion) {
			PcInstance use = (PcInstance) cha;

			if (isAutoPotion && (World.isNormalZone(getX(), getY(), getMap()) || World.isTeamBattleMap(this))) {
				isAutoPotion = false;
				ChattingController.toChatting(this, "PK 또는 팀대전시 자동물약이 비활성화 됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

			if (use.isAutoPotion
					&& (World.isNormalZone(use.getX(), use.getY(), use.getMap()) || World.isTeamBattleMap(use))) {
				use.isAutoPotion = false;
				ChattingController.toChatting(use, "PK 또는 팀대전시 자동물약이 비활성화 됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}

	/**
	 * 장비 스왑 데이터 로드.
	 * 2019-08-01
	 * by connector12@nate.com
	 */
	public void readSwap() {
		if (swap != null) {
			Connection con = null;
			PreparedStatement st = null;
			ResultSet rs = null;

			try {
				synchronized (swap) {
					con = DatabaseConnection.getLineage();
					st = con.prepareStatement("SELECT * FROM characters_swap WHERE cha_objId=?");
					st.setLong(1, getObjectId());
					rs = st.executeQuery();

					while (rs.next()) {
						if (swap == null)
							swap = new HashMap<String, Swap[]>();

						String key = rs.getString("swap_name");
						Swap[] item = swap.get(key);

						if (item == null)
							item = new Swap[Lineage.SLOT_ARROW + 1];
						else
							swap.remove(key);

						Swap temp = null;
						if (!rs.getString("swap_item_name").equalsIgnoreCase("null")) {
							temp = new Swap();
							temp.setObjId(rs.getLong("swap_item_objId"));
							temp.setItem(rs.getString("swap_item_name"));
							temp.setBless(rs.getInt("swap_item_bless"));
							temp.setEnLevel(rs.getInt("swap_item_en"));
						}

						item[rs.getInt("swap_idx")] = temp;
						swap.put(key, item);
					}
				}
			} catch (Exception e) {
				lineage.share.System.println("readSwap 에러. 캐릭터: " + getName());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, st, rs);
			}
		}
	}

	/**
	 * 장비 스왑 저장.
	 * 2019-08-01
	 * by connector12@nate.com
	 */
	public void saveSwap(Connection con) {
		if (swap != null) {
			PreparedStatement st = null;

			try {
				synchronized (swap) {
					st = con.prepareStatement("DELETE FROM characters_swap WHERE cha_objId=?");
					st.setLong(1, getObjectId());
					st.executeUpdate();
					st.close();

					Iterator<Map.Entry<String, Swap[]>> entries = swap.entrySet().iterator();
					while (entries.hasNext()) {
						Entry<String, Swap[]> entry = (Entry<String, Swap[]>) entries.next();
						String key = entry.getKey();
						Swap[] s = entry.getValue();

						for (int i = 0; i < s.length; i++) {
							boolean isNull = s[i] == null ? true : false;
							st = con.prepareStatement(
									"INSERT INTO characters_swap SET cha_objId=?, swap_name=?, swap_idx=?, swap_item_objId=?, swap_item_name=?, swap_item_bless=?, swap_item_en=?");
							st.setLong(1, getObjectId());
							st.setString(2, key);
							st.setInt(3, i);
							st.setLong(4, isNull ? 0 : s[i].getObjId());
							st.setString(5, isNull ? "null" : s[i].getItem());
							st.setInt(6, isNull ? 0 : s[i].getBless());
							st.setInt(7, isNull ? 0 : s[i].getEnLevel());
							st.executeUpdate();
							st.close();
						}
					}
				}
			} catch (Exception e) {
				lineage.share.System.println("saveSwap 에러. 캐릭터: " + getName());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(st);
			}
		}
	}

	public void removeSwap(String key) {
		if (swap != null) {
			Connection con = null;
			PreparedStatement st = null;

			try {
				synchronized (swap) {
					con = DatabaseConnection.getLineage();
					st = con.prepareStatement("DELETE FROM characters_swap WHERE cha_objId=? AND swap_name=?");
					st.setLong(1, getObjectId());
					st.setString(2, key);
					st.executeUpdate();

					swap.remove(key);
				}
			} catch (Exception e) {
				lineage.share.System.println("removeSwap 에러. 캐릭터: " + getName());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, st);
			}
		}
	}

	public void putSwap(String key, Swap[] swapList) {
		synchronized (swap) {
			swap.put(key, swapList);
		}
	}

	public Swap[] getSwap(String key) {
		synchronized (swap) {
			return swap.get(key);
		}
	}

	/**
	 * 장비 스왑 등록.
	 * 2019-08-01
	 * by connector12@nate.com
	 */
	public boolean insertSwap(String key) {
		if (isInsertSwap) {
			synchronized (swap) {
				boolean insert = true;
				Inventory inv = getInventory();
				Swap[] item = new Swap[Lineage.SLOT_ARROW + 1];

				if (swap.get(key) != null) {
					insert = false;
					swap.remove(key);
				}

				if (inv != null) {
					for (int i = 0; i <= Lineage.SLOT_ARROW; i++) {
						ItemInstance slot = inv.getSlot(i);

						if (slot == null) {
							item[i] = null;
						} else {
							Swap temp = new Swap();
							temp.setObjId(slot.getObjectId());
							temp.setItem(slot.getItem().getName());
							temp.setEnLevel(slot.getEnLevel());
							temp.setBless(slot.getBless());

							item[i] = temp;
						}
					}

					swap.put(key, item);
				}

				if (insert)
					ChattingController.toChatting(this, String.format("[%s] 장비 스왑 등록 완료.", key),
							Lineage.CHATTING_MODE_MESSAGE);
				else
					ChattingController.toChatting(this, String.format("[%s] 장비 스왑 수정 완료.", key),
							Lineage.CHATTING_MODE_MESSAGE);

				NpcSpawnlistDatabase.itemSwap.toTalk(this, null);
				isInsertSwap = false;
				return true;
			}
		}
		return false;
	}

	/**
	 * 스피드핵 체크.
	 * 2019-08-29
	 * by connector12@nate.com
	 */
	public boolean isFrameSpeed(int action) {
		// 스피드핵을 체크 안할경우 무시.
		if (Lineage.speedhack == false)
			return true;
		// 로봇은 무시.
		if (this instanceof RobotInstance)
			return true;
		if (speedCheck > System.currentTimeMillis())
			return true;
		if (HackNoCheckDatabase.isHackCheck(this))
			return true;

		double frame = 0;
		long FrameTime = 0;
		long time = System.currentTimeMillis();

		switch (action) {
			case Lineage.GFX_MODE_WALK:
				frame = SpriteFrameDatabase.getSpeedCheckGfxFrameTime(this, getGfx(),
						getGfxMode() + Lineage.GFX_MODE_WALK);
				FrameTime = WalkFrameTime;
				WalkFrameTime = time;
				break;
			case Lineage.GFX_MODE_ATTACK:
				frame = SpriteFrameDatabase.getSpeedCheckGfxFrameTime(this, getGfx(),
						getGfxMode() + Lineage.GFX_MODE_ATTACK);
				FrameTime = AttackFrameTime;
				AttackFrameTime = time;
				break;
			default:
				frame = SpriteFrameDatabase.getSpeedCheckGfxFrameTime(this, getGfx(), action);
				break;
		}

		lastActionTime = time + (long) frame;

		switch (action) {
			case Lineage.GFX_MODE_WALK:
				frame = Math.round(frame * Lineage.speed_check_walk_frame_rate);
				break;
			case Lineage.GFX_MODE_ATTACK:
				frame = Math.round(frame * Lineage.speed_check_attack_frame_rate);
				break;
		}

		long 유저프레임 = time - FrameTime;
		long 정상프레임 = (long) frame;

		// 스핵 체크
		if (유저프레임 < 정상프레임) {
			if (action == Lineage.GFX_MODE_ATTACK) {
				if (Lineage.is_attack_count_packet)
					badPacketCount++;
				// 비정상적인 패킷이 공격 패킷일 경우 공격 못하게함.
				return false;
			} else if (action == Lineage.GFX_MODE_SPELL_DIRECTION || action == Lineage.GFX_MODE_SPELL_NO_DIRECTION) {
				// 반딜 노딜일 경우 배드패킷 누적횟수+2.
				badPacketCount++;
			} else {
				// 비정상적인 패킷이 들어올 경우.
				badPacketCount++;
			}
		} else {
			if (action == Lineage.GFX_MODE_ATTACK) {
				if (Lineage.is_attack_count_packet) {
					// 정상적인 패킷이 들어올 경우.
					if (badPacketCount > 0)
						badPacketCount--;

					if (++goodPacketCount >= Lineage.speed_good_packet_count) {
						goodPacketCount = 0;

						if (--SpeedhackWarningCounting < 0)
							SpeedhackWarningCounting = 0;
					}
				}
			} else {
				// 정상적인 패킷이 들어올 경우.
				if (badPacketCount > 0)
					badPacketCount--;

				if (++goodPacketCount >= Lineage.speed_good_packet_count) {
					goodPacketCount = 0;

					if (--SpeedhackWarningCounting < 0)
						SpeedhackWarningCounting = 0;
				}
			}
		}

		// 스핵 횟수 체크.
		if (badPacketCount >= Lineage.speed_bad_packet_count) {
			speedCheck(action, 유저프레임, 정상프레임);
			badPacketCount = 0;
		}
		return true;
	}

	/**
	 * 액션 가능한지 확인
	 * 2020-09-27
	 * by connector12@nate.com
	 */
	public boolean isActionCheck(boolean isWalk) {
		long time = System.currentTimeMillis();

		if (isWalk) {
			if (time < lastMagicActionTime) {
				halfDelayCount++;
				FrameSpeedOverStun.init(this, 2);

				if (!Common.system_config_console) {
					String timeString = Util.getLocaleString(time, true);
					String log = String.format(
							"[%s]\t [반딜, 노딜]\t [캐릭터: %s]\t [경고 횟수: %d회]\t [GFX: %d]\t [GFX MODE: %d]\t [프레임: %d]",
							timeString, getName(), halfDelayCount, getGfx(), getGfxMode(),
							lastMagicActionTime - time);

					GuiMain.display.asyncExec(new Runnable() {
						public void run() {
							GuiMain.getViewComposite().getSpeedHackComposite().toLog(log);
						}
					});
				}
				return false;
			}
		} else {
			if (Lineage.attackAndMagic_delay > 0 && time < lastActionTime
					&& lastActionTime - time > Lineage.attackAndMagic_delay) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 스피드핵 경고.
	 * 2019-08-28
	 * by connector12@nate.com
	 */
	public void speedCheck(int mode, long 유저프레임, long 정상프레임) {
		++SpeedhackWarningCounting;

		if (Lineage.speedhack_stun) {
			if (SpeedhackWarningCounting >= Lineage.speed_hack_message_count
					&& SpeedhackWarningCounting < Lineage.speedhack_warning_count) {
				if (Lineage.speed_hack_block_time > 0) {
					if (Lineage.speed_hack_block_time >= 60)
						ChattingController.toChatting(this,
								String.format("[스피드핵 경고 %d회] %d회 이상 경고받을 경우 %d분간 마비.", SpeedhackWarningCounting,
										Lineage.speedhack_warning_count, (Lineage.speed_hack_block_time / 60)),
								Lineage.CHATTING_MODE_MESSAGE);
					else
						ChattingController.toChatting(this,
								String.format("[스피드핵 경고 %d회] %d회 이상 경고받을 경우 %d초간 마비.", SpeedhackWarningCounting,
										Lineage.speedhack_warning_count, Lineage.speed_hack_block_time),
								Lineage.CHATTING_MODE_MESSAGE);
				}
			}

			if (SpeedhackWarningCounting >= Lineage.speedhack_warning_count) {
				if (!Common.system_config_console) {
					long time = System.currentTimeMillis();
					String timeString = Util.getLocaleString(time, true);

					String log = String.format(
							"[%s]\t [스피드핵]\t [캐릭터: %s]\t [경고 횟수: %d회]\t [GFX: %d]\t [GFX MODE: %d]\t [유저 프레임: %d]\t [정상 프레임: %d]",
							timeString, getName(), SpeedhackWarningCounting, getGfx(), mode, 유저프레임, 정상프레임);

					GuiMain.display.asyncExec(new Runnable() {
						public void run() {
							GuiMain.getViewComposite().getSpeedHackComposite().toLog(log);
						}
					});
				}

				if (Lineage.speed_hack_block_time > 0) {
					FrameSpeedOverStun.init(this, true);

					if (Lineage.speed_hack_block_time >= 60)
						ChattingController.toChatting(this,
								String.format("스피드핵 경고 횟수 초과 %d분간 캐릭터 마비.", (Lineage.speed_hack_block_time / 60)),
								Lineage.CHATTING_MODE_MESSAGE);
					else
						ChattingController.toChatting(this,
								String.format("스피드핵 경고 횟수 초과 %d초간 캐릭터 마비.", Lineage.speed_hack_block_time),
								Lineage.CHATTING_MODE_MESSAGE);
				}

				SpeedhackWarningCounting = 0;
			}
		}
	}

	public boolean 뚫어핵체크객체(object o) {
		if (o == null)
			return false;

		return o instanceof object;
	}

	public boolean 뚫어핵체크제외객체(object o) {
		if (o == null)
			return true;

		return o instanceof ItemInstance || o instanceof Door || o instanceof KingdomDoor
				|| o instanceof KingdomCastleTop || o instanceof KingdomCrown || o instanceof SpotTower
				|| o instanceof SpotCrown ||
				o instanceof Switch || o instanceof Firewall || o instanceof LifeStream || o instanceof BackgroundTile
				|| o instanceof DeathEffect || o instanceof MagicDollInstance;
	}

	public boolean 뚫어핵체크제외Gfx(object o) {
		if (o == null)
			return true;

		return o.getGfx() == 1284 || o.getGfx() == 1036;
	}

	public boolean 뚫어핵체크(int x, int y) {
		boolean result = false;
		object target = null;

		if (getGm() == 0) {
			for (object o : getInsideList()) {
				if (o != null && o.getX() == x && o.getY() == y && o.getMap() == getMap()) {
					if (!o.isWorldDelete() && !o.isDead() && !o.isTransparent() && !뚫어핵체크제외Gfx(o) && !뚫어핵체크제외객체(o)
							&& 뚫어핵체크객체(o)) {
						result = true;
						target = o;
						gostHackCount++;
						break;
					}
				}
			}
		}

		if (result && target != null) {
			if (Lineage.gost_hack_block_time > 0) {
				FrameSpeedOverStun.init(this, false);

				if (Lineage.gost_hack_block_time >= 60)
					ChattingController.toChatting(this,
							String.format("[뚫어핵 의심] %d분간 캐릭터 마비.", (Lineage.gost_hack_block_time / 60)),
							Lineage.CHATTING_MODE_MESSAGE);
				else
					ChattingController.toChatting(this,
							String.format("[뚫어핵 의심] %d초간 캐릭터 마비.", Lineage.gost_hack_block_time),
							Lineage.CHATTING_MODE_MESSAGE);
			}

			if (!Common.system_config_console) {
				long time = System.currentTimeMillis();
				String timeString = Util.getLocaleString(time, true);

				String log = String.format(
						"[%s]\t [뚫어핵 의심]\t [캐릭터: %s]\t [뚤어핵 횟수: %d회]\t [GFX: %d]\t [object GFX: %d]\t [%s]", timeString,
						getName(), gostHackCount, getGfx(), target.getGfx(), target.getClass());

				GuiMain.display.asyncExec(new Runnable() {
					public void run() {
						GuiMain.getViewComposite().getSpeedHackComposite().toLog(log);
					}
				});
			}
			return false;
		}
		return true;
	}

	/**
	 * 장비 확인 막대
	 * 2020-10-21
	 * by connector12@nate.com
	 */
	public void pcItemCheck(boolean yes) {
		if (getInventory() != null) {
			if (yes) {
				PcInstance use = getItemCheckPc();
				if (use != null && use.getInventory() != null) {

					long cost = Lineage.item_check_count;
					long reward = cost / 2;
					boolean paid = (getGm() > 0);

					if (!paid)
						paid = getInventory().isAden(Lineage.item_check_name, cost, false);

					// 아데나 차감
					if (getGm() > 0 || getInventory().isAden(Lineage.item_check_name, cost, true)) {

						// 대상 유저(use)에게 아데나 지급
						if (getGm() == 0) {
							Item adena = ItemDatabase.find("아데나");
							if (adena != null && use.getInventory() != null) {
								ItemInstance temp = use.getInventory().find(adena.getName(), adena.isPiles());
								if (temp == null) {
									temp = ItemDatabase.newInstance(adena);
									temp.setObjectId(ServerDatabase.nextItemObjId());
									temp.setBless(1);
									temp.setCount(reward);
									temp.setDefinite(true);
									use.getInventory().append(temp, true);
								} else {
									use.getInventory().count(temp, temp.getCount() + reward, true);
								}

								// 지급 알림
								ChattingController.toChatting(use, String.format("장비 확인 보상으로 아데나 %,d를 받았습니다.", reward),
										Lineage.CHATTING_MODE_MESSAGE);
							}
						}
						List<String> list = new ArrayList<String>();
						list.add(use.getName());

						int idx = 0;
						for (ItemInstance i : use.getInventory().getList()) {
							if (i != null && i.getItem() != null) {

								String name = i.getItem().getName();
								int bless = i.getBless();
								int en = i.getEnLevel();
								long count = i.getCount();

								if (i.isEquipped()) {
									if (i.getEnLevel() > 0) {
										if (bless == 1)
											list.add(String.format("%d. [착용] +%d %s(%,d)", ++idx, en, name, count));
										else
											list.add(String.format("%d. [착용] (%s) +%d %s(%,d)", ++idx,
													bless == 0 ? "축" : "저주", en, name, count));
									} else {
										if (bless == 1)
											list.add(String.format("%d. [착용] %s(%,d)", ++idx, name, count));
										else
											list.add(String.format("%d. [착용] %s(%,d)", ++idx,
													String.format("(%s) %s", bless == 0 ? "축" : "저주", name), count));
									}
								} else {
									if (getGm() > 0) {
										if (i.getEnLevel() > 0) {
											if (bless == 1)
												list.add(String.format("%d. +%d %s(%,d)", ++idx, en, name, count));
											else
												list.add(String.format("%d. (%s) +%d %s(%,d)", ++idx,
														bless == 0 ? "축" : "저주", en, name, count));
										} else {
											if (bless == 1)
												list.add(String.format("%d. %s(%,d)", ++idx, name, count));
											else
												list.add(String.format("%d. %s(%,d)", ++idx,
														String.format("(%s) %s", bless == 0 ? "축" : "저주", name),
														count));
										}
									}
								}
							}
						}

						if (getGm() == 0) {
							String msg = String.format("\\fY[장비 확인] %s 님이 당신의 장비를 확인하였습니다.", getName());
							ChattingController.toChatting(use, msg, Lineage.CHATTING_MODE_MESSAGE);
						}

						if (list.size() < 2)
							toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "pcitemch1", null,
									list));
						else
							toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "pcitemch", null,
									list));
					} else {
						toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 776,
								Lineage.item_check_name));
					}
				}
			}
		}

		setItemCheckPc(null);
	}

	/**
	 * 고정 멤버 버프
	 * 2020-11-19
	 * by connector12@nate.com
	 */
	public void 고정멤버버프(boolean isPacket) {
		if (isMember()) {
			setDynamicCritical(getDynamicCritical() + 2);
			setDynamicBowCritical(getDynamicBowCritical() + 2);
			setDynamicMagicCritical(getDynamicMagicCritical() + 2);
			ChattingController.toChatting(this, "\\fY고정 멤버: 근거리 치명타, 원거리 치명타, 마법 치명타 +2%",
					Lineage.CHATTING_MODE_MESSAGE);

			if (isPacket) {
				toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), this));
				toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), this));
			}
		}
	}

	public void showAutoHuntHtml() {
		try {
			List<String> autoHunt = new ArrayList<String>();
			autoHunt.clear();

			String msg = null;
			if (getInventory().isAden("자동사냥무제한", 1, false)) {
				long time = 0;

				if (Lineage.is_auto_hunt_time_account) {
					time = auto_hunt_account_time;
				} else {
					time = auto_hunt_time;
				}

				if (time > 0) {
					if (time / 3600 > 0) {
						msg = String.format("[남은 시간: %d시간 %d분 %d초]", time / 3600, time % 3600 / 60, time % 3600 % 60);
					} else if (time % 3600 / 60 > 0) {
						msg = String.format("[남은 시간: %d분 %d초]", time % 3600 / 60, time % 3600 % 60);
					} else {
						msg = String.format("[남은 시간: %d초]", time % 3600 % 60);
					}
				} else {
					msg = "[남은 시간: 없음]";
				}
			} else {
				msg = "[자동 사냥]";
			}

			autoHunt.add(msg == null ? " " : msg);
			autoHunt.add(isAutoHunt ? "ON" : "OFF");
			autoHunt.add(auto_return_home_hp < 1 ? "설정 X" : String.format("%d%%", auto_return_home_hp));

			for (int i = 0; i < Lineage.auto_hunt_home_hp_list.size(); i++) {
				autoHunt.add(String.format("%d%%", Lineage.auto_hunt_home_hp_list.get(i)));

				if (i > 6) {
					break;
				}
			}

			for (int i = 0; i < 7 - Lineage.auto_hunt_home_hp_list.size(); i++) {
				autoHunt.add(" ");
			}

			autoHunt.add(is_auto_buff ? "ON" : "OFF");

			autoHunt.add(isAutoPotion ? "ON" : "OFF");
			autoHunt.add(is_auto_potion_buy ? "ON" : "OFF");
			autoHunt.add(autoPotionPercent < 1 ? "설정 X" : String.format("%d%%", autoPotionPercent));
			autoHunt.add(autoPotionName == null || autoPotionName.length() < 1 ? "설정 X" : autoPotionName);

			autoHunt.add(is_auto_poly_select ? "랭변" : "일반");

			autoHunt.add(is_auto_rank_poly ? "ON" : "OFF");
			autoHunt.add(is_auto_rank_poly_buy ? "ON" : "OFF");

			autoHunt.add(is_auto_poly ? "ON" : "OFF");
			autoHunt.add(is_auto_poly_buy ? "ON" : "OFF");
			autoHunt.add(
					getQuickPolymorph() == null || getQuickPolymorph().length() < 1 ? "설정 X" : getQuickPolymorph());

			autoHunt.add(is_auto_teleport ? "ON" : "OFF");

			autoHunt.add(is_auto_bravery ? "ON" : "OFF");
			autoHunt.add(is_auto_bravery_buy ? "ON" : "OFF");

			autoHunt.add(is_auto_haste ? "ON" : "OFF");
			autoHunt.add(is_auto_haste_buy ? "ON" : "OFF");

			autoHunt.add(is_auto_arrow_buy ? "ON" : "OFF");

			autoHunt.add(is_auto_sell ? "ON" : "OFF");

			autoHunt.add(is_auto_trunundead ? "ON" : "OFF");

			toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohunt", null, autoHunt));
		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] showAutoHuntHtml()\r\n : %s\r\n", e.toString());
		}
	}

	public void showAutoSellHtml() {
		try {
			List<String> autosell = new ArrayList<String>();
			autosell.clear();

			autosell.add(this.is_auto_sell ? "ON" : "OFF");
			autosell.add("자동매입 물품추가");
			autosell.add("자동매입 물품목록");
			autosell.add("자동매입 물품삭제");
			autosell.add("자동매입 전체삭제");

			toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohuntsell", null, autosell));

		} catch (Exception e) {
			lineage.share.System.printf("[자동 매입] showAutoSellHtml()\r\n : %s\r\n", e.toString());
		}
	}

	// PcInstance 내부에 그대로 추가
	private void giveAdena(long amount) {
		if (amount <= 0)
			return;
		lineage.bean.database.Item adena = lineage.database.ItemDatabase.find("아데나");
		if (adena == null)
			return;

		lineage.world.object.instance.ItemInstance has = this.getInventory().find(adena.getName(), adena.isPiles());

		if (has == null) {
			if (adena.isPiles()) {
				lineage.world.object.instance.ItemInstance ni = lineage.database.ItemDatabase.newInstance(adena);
				ni.setObjectId(lineage.database.ServerDatabase.nextItemObjId());
				ni.setBless(1);
				ni.setEnLevel(0);
				ni.setCount(amount);
				ni.setDefinite(true);
				this.getInventory().append(ni, true);
			} else {
				for (long i = 0; i < amount; i++) {
					lineage.world.object.instance.ItemInstance ni = lineage.database.ItemDatabase.newInstance(adena);
					ni.setObjectId(lineage.database.ServerDatabase.nextItemObjId());
					ni.setBless(1);
					ni.setEnLevel(0);
					ni.setDefinite(true);
					this.getInventory().append(ni, true);
				}
			}
		} else {
			this.getInventory().count(has, has.getCount() + amount, true);
		}
	}

	// PcInstance 내부에 추가 (기존 코드 덮어쓰기)
	public void tryAutoSellOnPickup(lineage.world.object.instance.ItemInstance item) {
		try {
			// 1. 빈 껍데기가 들어오면 즉시 패스
			if (!this.is_auto_sell || item == null || item.getItem() == null || item.getItem().getName() == null
					|| this.getInventory() == null)
				return;
			if ("아데나".equals(item.getItem().getName()))
				return;

			// ▼▼▼ [절대 보호 구역] 착용 중이거나 인챈트된 아이템은 무슨 일이 있어도 판매 거부! ▼▼▼
			if (item.isEquipped())
				return;
			if (item.getEnLevel() != 0)
				return;
			// ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲

			// ★ 핵심: 아이템이 메모리에서 날아가기 전에 "이름"을 미리 안전한 곳에 저장해둡니다!
			String itemName = item.getItem().getName();

			// 2. 등록된 리스트에 있는지 확인
			java.util.List<party_auto_sell.AutosellItem> list = party_auto_sell.AutosellDatabase
					.getList(this.getObjectId());
			if (list == null || list.isEmpty())
				return;

			boolean isMatch = false;
			for (party_auto_sell.AutosellItem ai : list) {
				// 원본 객체(item) 대신 미리 빼둔 이름표(itemName)와 비교
				if (ai != null && ai.getName() != null && ai.getName().equals(itemName)) {
					isMatch = true;
					break;
				}
			}

			if (!isMatch)
				return;

			// 3. 가격 책정 (★ ONDO 서버 '매입 상인' 직접 타겟팅 ★)
			long price = 0;

			// 서버 메모리에서 이름이 "매입 상인"인 NPC를 직접 찾아냅니다.
			lineage.bean.database.Npc buyerNpc = lineage.database.NpcDatabase.find("매입 상인");

			if (buyerNpc != null) {
				// 매입 상인의 장부(shop)에서 해당 아이템의 가격을 묻습니다.
				lineage.bean.database.Shop shop = buyerNpc.findShopItemId(itemName, item.getBless());

				if (shop != null && shop.getPrice() > 0) {
					// 1순위: 매입 상인이 부르는 정확한 가격!
					price = shop.getPrice();
				} else {
					// 2순위: 매입 상인 장부에 없는 잡템일 경우 기본가의 절반
					price = item.getItem().getShopPrice() / 2;
				}
			} else {
				// 만약 매입 상인 NPC 데이터를 못 불러왔을 경우의 방어막
				price = item.getItem().getShopPrice() / 2;
			}

			// 최후의 안전장치 (0원 방지)
			if (price <= 0) {
				price = 10;
			}

			long count = item.getCount();
			long pay = price * count;

			// 4. 인벤에서 주운 아이템 즉시 증발 (여기서 아이템 객체가 완전히 타버립니다)
			this.getInventory().count(item, 0, true);

			// 5. 아데나 즉시 지급
			lineage.bean.database.Item adena = lineage.database.ItemDatabase.find("아데나");
			if (adena != null) {
				lineage.world.object.instance.ItemInstance hasAdena = this.getInventory().find(adena.getName(), true);
				if (hasAdena == null) {
					hasAdena = lineage.database.ItemDatabase.newInstance(adena);
					if (hasAdena != null) {
						hasAdena.setObjectId(lineage.database.ServerDatabase.nextItemObjId());
						hasAdena.setBless(1);
						hasAdena.setCount(pay);
						hasAdena.setDefinite(true);
						this.getInventory().append(hasAdena, true);
					}
				} else {
					this.getInventory().count(hasAdena, hasAdena.getCount() + pay, true);
				}
			}

			// 6. 성공 메세지 출력! (타버린 item 객체 대신, 미리 주머니에 챙겨둔 itemName을 꺼내 씁니다)
			lineage.world.controller.ChattingController.toChatting(
					this, String.format("[자동매입] %s -> %,d 아데나 교환 완료", itemName, pay),
					lineage.share.Lineage.CHATTING_MODE_MESSAGE);

		} catch (Exception e) {
			lineage.share.System.println("[자동매입] tryAutoSellOnPickup() 에러: " + e.toString());
			e.printStackTrace();
		}
	}

	public void checkPotion() {
		try {
			// 인벤토리에서 설정한 물약 찾기.
			boolean isPotion = false;
			for (ItemInstance potion : getInventory().getList()) {
				if (potion != null && potion.getItem() != null && potion instanceof HealingPotion
						&& potion.getItem().getName().equalsIgnoreCase(autoPotionName)) {
					isPotion = true;
					break;
				}
			}

			// 설정한 물약이 인벤토리에 존재하지 않으면 설정 초기화.
			if (!isPotion)
				autoPotionName = null;
		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] checkPotion()\r\n : %s\r\n", e.toString());
		}
	}

	public void showAutoPotionHtml() {
		try {
			if (getInventory() != null) {
				if (autoPotionIdx == null)
					autoPotionIdx = new String[20];

				checkPotion();

				List<String> autoPotion = new ArrayList<String>();
				autoPotion.clear();
				autoPotion.add(autoPotionPercent < 1 ? "설정 X" : String.format("%d%% 이하 물약 복용", autoPotionPercent));

				for (int i = 0; i < Lineage.auto_hunt_potion_hp_list.size(); i++) {
					autoPotion.add(String.format("%d%%", Lineage.auto_hunt_potion_hp_list.get(i)));

					if (i > 6) {
						break;
					}
				}

				for (int i = 0; i < 7 - Lineage.auto_hunt_potion_hp_list.size(); i++) {
					autoPotion.add(" ");
				}

				autoPotion.add(autoPotionName == null || autoPotionName.length() < 2 ? "설정 X" : autoPotionName);

				// 인벤토리에서 물약종류를 선택.
				int idx = 0;
				for (ItemInstance potion : getInventory().getList()) {
					if (potion != null && potion.getItem() != null && potion instanceof HealingPotion) {
						autoPotion.add(String.format("%s (%s)", potion.getItem().getName(),
								Util.changePrice(potion.getCount())));
						autoPotionIdx[idx] = potion.getItem().getName();
						idx++;
					}
				}

				for (int i = 0; i < autoPotionIdx.length; i++) {
					if (idx == 0 && i == 0) {
						autoPotion.add("인벤토리에 물약이 존재하지 않습니다.");
					} else {
						autoPotion.add(" ");
					}
				}

				toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "autohunt1", null, autoPotion));
			}
		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] showAutoPotionHtml()\r\n : %s\r\n", e.toString());
		}
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
		try {
			if (action.contains("autohunt-")) {
				action = action.replace("autohunt-", "");

				if (action.contains("potion-")) {
					action = action.replace("potion-", "");

					if (action.contains("percent-")) {
						action = action.replace("percent-", "");

						if (action.equalsIgnoreCase("1")) {
							if (Lineage.auto_hunt_potion_hp_list.size() > 0) {
								autoPotionPercent = Lineage.auto_hunt_potion_hp_list.get(0);
							}
						} else if (action.equalsIgnoreCase("2")) {
							if (Lineage.auto_hunt_potion_hp_list.size() > 1) {
								autoPotionPercent = Lineage.auto_hunt_potion_hp_list.get(1);
							}
						} else if (action.equalsIgnoreCase("3")) {
							if (Lineage.auto_hunt_potion_hp_list.size() > 2) {
								autoPotionPercent = Lineage.auto_hunt_potion_hp_list.get(2);
							}
						} else if (action.equalsIgnoreCase("4")) {
							if (Lineage.auto_hunt_potion_hp_list.size() > 3) {
								autoPotionPercent = Lineage.auto_hunt_potion_hp_list.get(3);
							}
						} else if (action.equalsIgnoreCase("5")) {
							if (Lineage.auto_hunt_potion_hp_list.size() > 4) {
								autoPotionPercent = Lineage.auto_hunt_potion_hp_list.get(4);
							}
						} else if (action.equalsIgnoreCase("6")) {
							if (Lineage.auto_hunt_potion_hp_list.size() > 5) {
								autoPotionPercent = Lineage.auto_hunt_potion_hp_list.get(5);
							}
						} else if (action.equalsIgnoreCase("7")) {
							if (Lineage.auto_hunt_potion_hp_list.size() > 6) {
								autoPotionPercent = Lineage.auto_hunt_potion_hp_list.get(6);
							}
						}
					}

					if (action.contains("item-")) {
						try {
							int idx = Integer.valueOf(action.replace("item-", "").trim());
							pc.autoPotionName = pc.autoPotionIdx[idx];
						} catch (Exception e) {
							ChattingController.toChatting(pc, "[자동 물약] 물약 설정이 잘못되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
						}
					}

					showAutoPotionHtml();
				} else {
					if (action.equalsIgnoreCase("on")) {
						/*
						 * if (Lineage.is_auto_hunt_time || !getInventory().isAden("자동사냥무제한",1, false))
						 * {
						 * long time = 0;
						 * if (Lineage.is_auto_hunt_time_account) {
						 * time = auto_hunt_account_time;
						 * } else {
						 * time = auto_hunt_time;
						 * }
						 * 
						 * if (time < 1) {
						 * ChattingController.toChatting(this, "              \\fY자동 사냥 이용 시간이 부족합니다.",
						 * Lineage.CHATTING_MODE_MESSAGE);
						 * return;
						 * }
						 * }
						 */

						if (!isAutoHuntCheck()) {
							ChattingController.toChatting(this, "             \\fY자동 사냥을 사용할 수 없는 상태입니다.",
									Lineage.CHATTING_MODE_MESSAGE);
							return;
						}

						if (!Lineage.is_auto_hunt) {
							ChattingController.toChatting(this, "                \\fY자동 사냥은 사용할 수 없습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
							return;
						}

						if (Lineage.is_auto_hunt_member && !isMember()) {
							ChattingController.toChatting(this, "                \\fY고정 멤버만 사용 가능합니다.",
									Lineage.CHATTING_MODE_MESSAGE);
							return;
						}

						if (World.isSafetyZone(getX(), getY(), getMap()) && getMap() != 70) {
							ChattingController.toChatting(this, "              \\fY세이프티존에서 시작할 수 없습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
							return;
						}

						if (auto_return_home_hp < 1) {
							ChattingController.toChatting(this, "          \\fY자동 귀환 체력 설정을 해주시기 바랍니다.",
									Lineage.CHATTING_MODE_MESSAGE);
							return;
						}

						if (!isAutoPotion || autoPotionPercent < 1 || autoPotionName == null
								|| autoPotionName.length() < 1) {
							ChattingController.toChatting(this, "             \\fY자동 물약 설정을 해주시기 바랍니다.",
									Lineage.CHATTING_MODE_MESSAGE);
							return;
						}

						boolean isMap = false;
						for (int map : Lineage.auto_hunt_map_list) {
							if (map == getMap()) {
								isMap = true;
								break;
							}
						}

						if (!isMap) {
							ChattingController.toChatting(this, "           \\fY자동 사냥을 사용할 수 없는 위치입니다.",
									Lineage.CHATTING_MODE_MESSAGE);
							return;
						}

						if (lastMovingTime + 500 > System.currentTimeMillis()) {
							ChattingController.toChatting(this, "                \\fY이동 중에 시작할 수 없습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
							return;
						}

						if (!isAutoHunt) {
							autohunt_target = null;
							is_auto_return_home = false;
							isAutoHunt = true;
							ChattingController.toChatting(this, "                   \\fR자동 사냥을 시작합니다.",
									Lineage.CHATTING_MODE_MESSAGE);

							start_x = getX();
							start_y = getY();
							start_map = getMap();
							temp_x = getX();
							temp_y = getY();
							temp_map = getMap();

							SC_AUTO_SUPPORT_NOTI.newInstance()
									.setEnabled(true)
									.send(pc);

						}
					} else if (action.equalsIgnoreCase("off")) {
						if (isAutoHunt) {
							endAutoHunt(false, false);
						}
					} else if (action.equalsIgnoreCase("1")) {
						if (Lineage.auto_hunt_home_hp_list.size() > 0) {
							auto_return_home_hp = Lineage.auto_hunt_home_hp_list.get(0);
						}
					} else if (action.equalsIgnoreCase("2")) {
						if (Lineage.auto_hunt_home_hp_list.size() > 1) {
							auto_return_home_hp = Lineage.auto_hunt_home_hp_list.get(1);
						}
					} else if (action.equalsIgnoreCase("3")) {
						if (Lineage.auto_hunt_home_hp_list.size() > 2) {
							auto_return_home_hp = Lineage.auto_hunt_home_hp_list.get(2);
						}
					} else if (action.equalsIgnoreCase("4")) {
						if (Lineage.auto_hunt_home_hp_list.size() > 3) {
							auto_return_home_hp = Lineage.auto_hunt_home_hp_list.get(3);
						}
					} else if (action.equalsIgnoreCase("5")) {
						if (Lineage.auto_hunt_home_hp_list.size() > 4) {
							auto_return_home_hp = Lineage.auto_hunt_home_hp_list.get(4);
						}
					} else if (action.equalsIgnoreCase("6")) {
						if (Lineage.auto_hunt_home_hp_list.size() > 5) {
							auto_return_home_hp = Lineage.auto_hunt_home_hp_list.get(5);
						}
					} else if (action.equalsIgnoreCase("7")) {
						if (Lineage.auto_hunt_home_hp_list.size() > 6) {
							auto_return_home_hp = Lineage.auto_hunt_home_hp_list.get(6);
						}
					} else if (action.equalsIgnoreCase("buffon")) {
						pc.is_auto_buff = true;
					} else if (action.equalsIgnoreCase("buffoff")) {
						pc.is_auto_buff = false;
					} else if (action.equalsIgnoreCase("potionon")) {
						pc.isAutoPotion = true;
					} else if (action.equalsIgnoreCase("potionoff")) {
						pc.isAutoPotion = false;
					} else if (action.equalsIgnoreCase("potionbuy")) {
						if (Lineage.is_auto_potion_buy) {
							pc.is_auto_potion_buy = true;
						} else {
							ChattingController.toChatting(this, "           \\fY자동 물약 구매는 사용할 수 없습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
						}
					} else if (action.equalsIgnoreCase("potionnobuy")) {
						pc.is_auto_potion_buy = false;
					} else if (action.equalsIgnoreCase("poly-nomal")) {
						pc.is_auto_poly_select = false;
					} else if (action.equalsIgnoreCase("poly-rank")) {
						pc.is_auto_poly_select = true;
					} else if (action.equalsIgnoreCase("poly-rank-on")) {
						if (Lineage.is_auto_poly_rank) {
							pc.is_auto_rank_poly = true;
						} else {
							ChattingController.toChatting(this, " \\fY자동 랭변은 사용할 수 없습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
						}
					} else if (action.equalsIgnoreCase("poly-rank-off")) {
						pc.is_auto_rank_poly = false;
					} else if (action.equalsIgnoreCase("poly-rank-buy")) {
						if (Lineage.is_auto_poly_rank_buy) {
							pc.is_auto_rank_poly_buy = true;
						} else {
							ChattingController.toChatting(this, "             \\fY자동 랭변 구매는 사용할 수 없습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
						}
					} else if (action.equalsIgnoreCase("poly-rank-nobuy")) {
						pc.is_auto_rank_poly_buy = false;
					} else if (action.equalsIgnoreCase("polyon")) {
						if (Lineage.is_auto_poly) {
							Poly p = PolyDatabase.getName(pc.getQuickPolymorph());

							if (p != null) {
								pc.is_auto_poly = true;
							} else {
								ChattingController.toChatting(this, "      \\fY변신 주문서로 사용할 변신을 선택하시기 바랍니다.",
										Lineage.CHATTING_MODE_MESSAGE);
							}
						} else {
							ChattingController.toChatting(this, "              \\fY자동 변신은 사용할 수 없습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
						}
					} else if (action.equalsIgnoreCase("polyoff")) {
						pc.is_auto_poly = false;
					} else if (action.equalsIgnoreCase("polybuy")) {
						if (Lineage.is_auto_poly_buy) {
							pc.is_auto_poly_buy = true;
						} else {
							ChattingController.toChatting(this, "             \\fY자동 변줌 구매는 사용할 수 없습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
						}
					} else if (action.equalsIgnoreCase("polynobuy")) {
						pc.is_auto_poly_buy = false;
					} else if (action.equalsIgnoreCase("teleporton")) {
						if (Lineage.is_auto_teleport) {
							pc.is_auto_teleport = true;

							if (!isAutoHuntTeleportMap()) {
								ChattingController.toChatting(this, "           \\fY자동 텔레포트를 사용할 수 없는 맵입니다.",
										Lineage.CHATTING_MODE_MESSAGE);
							}
						} else {
							ChattingController.toChatting(this, "              \\fY자동 텔레포트는 사용할 수 없습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
						}
					} else if (action.equalsIgnoreCase("teleportoff")) {
						pc.is_auto_teleport = false;
					} else if (action.equalsIgnoreCase("braveryon")) {
						if (Lineage.is_auto_bravery) {
							pc.is_auto_bravery = true;
						} else {
							ChattingController.toChatting(this, "              \\fY자동 용기는 사용할 수 없습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
						}
					} else if (action.equalsIgnoreCase("braveryoff")) {
						pc.is_auto_bravery = false;
					} else if (action.equalsIgnoreCase("braverybuy")) {
						if (Lineage.is_auto_bravery_buy) {
							pc.is_auto_bravery_buy = true;
						} else {
							ChattingController.toChatting(this, "             \\fY자동 용기 구매는 사용할 수 없습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
						}
					} else if (action.equalsIgnoreCase("braverynobuy")) {
						pc.is_auto_bravery_buy = false;
					} else if (action.equalsIgnoreCase("hasteon")) {
						if (Lineage.is_auto_haste) {
							pc.is_auto_haste = true;
						} else {
							ChattingController.toChatting(this, "              \\fY자동 촐기는 사용할 수 없습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
						}
					} else if (action.equalsIgnoreCase("hasteoff")) {
						pc.is_auto_haste = false;
					} else if (action.equalsIgnoreCase("hastebuy")) {
						if (Lineage.is_auto_haste_buy) {
							pc.is_auto_haste_buy = true;
						} else {
							ChattingController.toChatting(this, "             \\fY자동 촐기 구매는 사용할 수 없습니다.",
									Lineage.CHATTING_MODE_MESSAGE);
						}
					} else if (action.equalsIgnoreCase("hastenobuy")) {
						pc.is_auto_haste_buy = false;
					} else if (action.equalsIgnoreCase("arrowbuy")) {
						pc.is_auto_arrow_buy = true;
					} else if (action.equalsIgnoreCase("arrowbuynobuy")) {
						pc.is_auto_arrow_buy = false;
					}

					showAutoHuntHtml();
				}
			}

			// ▼▼▼ 자동 매입(판매) 버튼 액션 처리 ▼▼▼
			if (action != null && action.startsWith("autosell-")) {
				
				// [운영자 전역 설정 체크]
			    // Config 파일에서 false로 바꾸면 모든 유저가 이 아래 로직으로 못 들어옵니다.
			    if (!lineage.share.Lineage.is_autosell_global) { // 변수 위치는 팩에 따라 다를 수 있음
			        lineage.world.controller.ChattingController.toChatting(this, "\\f3[알림] 현재 운영자에 의해 자동매입 기능이 중지되었습니다.", lineage.share.Lineage.CHATTING_MODE_MESSAGE);
			        return;
			    }

				// 0. 처음에 자동판매 HTML 창을 열어주는 역할 (복구 완료!)
				if (action.equals("autosell-")) {
					this.showAutoSellHtml();
					return;
				}

				// 1. 기능 ON
				if (action.equals("autosell-on")) {
					this.is_auto_sell = true;

					// ▼▼▼ [업그레이드!] ON 누르는 순간 인벤토리 싹 훑어서 즉시 판매! ▼▼▼
					try {
						// 내 가방(인벤토리)에 있는 모든 아이템 목록을 가져옵니다.
						java.util.List<lineage.world.object.instance.ItemInstance> invList = this.getInventory()
								.getList();

						// 가방 안의 아이템을 하나씩 꺼내서 무적판 판매기에 던져 넣습니다!
						for (lineage.world.object.instance.ItemInstance item : invList) {
							// 무적판 코드가 알아서 리스트에 있는 템만 골라서 아데나로 바꿔줍니다.
							this.tryAutoSellOnPickup(item);
						}
					} catch (Exception e) {
						lineage.share.System.println("[자동매입 ON 소급적용 에러] : " + e.toString());
					}
					// ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲

					this.showAutoSellHtml(); // 창 새로고침하여 상태 반영
					return;
				}

				// 1-2. 기능 OFF
				if (action.equals("autosell-off")) {
					this.is_auto_sell = false;
					this.showAutoSellHtml(); // 창 새로고침하여 상태 반영
					return;
				}

				// 2. 물품 추가 창
				if (action.equals("autosell-add")) {
					this.setPersnalShopInsert(true);
					this.setPersnalShopSellEdit(false);

					// ▼▼▼ [아이템 보호 필터링] 가방을 뒤져서 안전한 템만 골라냅니다 ▼▼▼
					java.util.List<lineage.world.object.instance.ItemInstance> safeList = new java.util.ArrayList<lineage.world.object.instance.ItemInstance>();

					for (lineage.world.object.instance.ItemInstance item : this.getInventory().getList()) {
						if (item == null)
							continue;

						// 1. 착용 중인 장비 제외 (목록에 안 띄움)
						if (item.isEquipped())
							continue;

						// 2. 인챈트(+1 이상이거나 저주받은 템)된 장비 제외
						if (item.getEnLevel() != 0)
							continue;

						// 위 검사를 모두 통과한 안전한(착용 안 한 +0) 아이템만 리스트에 넣습니다.
						safeList.add(item);
					}

					// 전체 인벤토리가 아닌, 안전하게 걸러진 safeList만 클라이언트 창에 띄워줍니다!
					this.toSender(party_auto_sell.S_AutoSell.clone(
							lineage.network.packet.BasePacketPooling.getPool(party_auto_sell.S_AutoSell.class), this,
							safeList));
					// ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
					return;
				}

				// 3. 물품 목록 창
				if (action.equals("autosell-list")) {
					java.util.List<party_auto_sell.AutosellItem> list = party_auto_sell.AutosellDatabase
							.getList(this.getObjectId());
					this.toSender(party_auto_sell.S_AutoSellList.clone(
							lineage.network.packet.BasePacketPooling.getPool(party_auto_sell.S_AutoSellList.class),
							this, list));
					return;
				}

				// 4. 물품 삭제 창
				if (action.equals("autosell-delete")) {
					this.setPersnalShopSellEdit(true); // ★ 핵심: 삭제 스위치 ON
					this.setPersnalShopInsert(false);
					java.util.List<party_auto_sell.AutosellItem> list = party_auto_sell.AutosellDatabase
							.getList(this.getObjectId());
					this.toSender(party_auto_sell.S_AutoSellDelete.clone(
							lineage.network.packet.BasePacketPooling.getPool(party_auto_sell.S_AutoSellDelete.class),
							this, list));
					return;
				}

				// 5. 물품 전체 삭제
				if (action.equals("autosell-alldelete")) {
					party_auto_sell.AutosellDatabase.AlldeleteautoSell(this);
					lineage.world.controller.ChattingController.toChatting(this, "자동매입 물품이 전체 삭제되었습니다.",
							lineage.share.Lineage.CHATTING_MODE_MESSAGE);
					this.showAutoSellHtml();
					return;
				}
			}
			// ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲

		} catch (Exception e) {
			lineage.share.System.printf(
					"[자동 사냥] toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp)\r\n : %s\r\n",
					e.toString());
		}
	}

	public void endAutoHunt(boolean isHome, boolean isTimeOver) {
		try {
			isAutoHunt = false;
			autohunt_target = null;
			is_auto_return_home = false;

			SC_AUTO_SUPPORT_NOTI.newInstance()
					.setEnabled(false)
					.send(this);

			if (isTimeOver) {
				ChattingController.toChatting(this, "      \\fY이용 가능 시간이 부족하여 자동 사냥을 종료합니다.",
						Lineage.CHATTING_MODE_MESSAGE);
			} else {
				ChattingController.toChatting(this, "                   \\fY자동 사냥을 종료합니다.",
						Lineage.CHATTING_MODE_MESSAGE);
			}

			toTeleport(getX(), getY(), getMap(), false);

			if (isHome) {
				int[] home = null;
				home = Lineage.getHomeXY();
				toTeleport(home[0], home[1], home[2], true);
			}
		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] endAutoHunt()\r\n : %s\r\n", e.toString());
		}
	}

	/**
	 * 자동 사냥 가능한 상태인지 확인
	 * 
	 * @return
	 */
	public boolean isAutoHuntCheck() {
		try {

			if (this != null && !isDead() && !isWorldDelete() && !isLock() && !isInvis() && !isTransparent()) {
				return true;
			}
		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] isAutoHuntCheck()\r\n : %s\r\n", e.toString());
		}

		return false;
	}

	/**
	 * 자동 사냥 ai부분
	 * 
	 * @param time
	 */
	public void toAutoHunt(long time) {
		try {

			if (Lineage.is_auto_hunt && isAutoHunt) {
				if (this == null || isDead() || isWorldDelete() || isInvis() || isTransparent()) {
					endAutoHunt(false, false);
					return;
				}
				if (this.getLevel() < 50) {
					ChattingController.toChatting(this, "자동사냥은 50레벨 부터 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					endAutoHunt(false, false);
					return;
				}
				if (!getInventory().isAutoHuntInventory()) {
					endAutoHunt(true, false);
					return;
				}

				if (isAutoHuntCheck()) {
					if (isAi(time)) {
						if (Lineage.is_auto_hunt_member && !isMember()) {
							return;
						}

						Clan clan = ClanController.find((PcInstance) this);

						if (this.isMark) {
							this.isMark = false;

							for (Clan c : ClanController.getClanList().values()) {
								if (c != null && !c.getName().equalsIgnoreCase(clan.getName())
										&& !c.getName().equalsIgnoreCase(Lineage.new_clan_name) &&
										!c.getName().equalsIgnoreCase(Lineage.teamBattle_A_team)
										&& !c.getName().equalsIgnoreCase(Lineage.teamBattle_B_team)) {
									this.toSender(S_ClanWar.clone(BasePacketPooling.getPool(S_ClanWar.class), 3,
											clan.getName(), c.getName()));
								}
							}
						}

						// 자동 귀환
						autoHuntGotoHome(time);
						// 자동 아이템 구매
						autoHuntItemBuy();

						// (이미 있던) autoHuntItemBuy();
						if (this.is_auto_sell) {
							autoHuntItemSell(); // ← 한 줄 추가
						}

						// 자동 버프
						autoHuntBuff(time);
						autoHuntbuff2();

						ItemInstance item = getInventory().find("도플갱어 변신 카드", 0, 1);
						ItemInstance item2 = getInventory().find("가드리아 변신 카드", 0, 1);
						ItemInstance item3 = getInventory().find("드루가 변신 카드", 0, 1);
						ItemInstance item4 = getInventory().find("플래바포 변신 카드", 0, 1);
						ItemInstance item5 = getInventory().find("발키리 변신 카드", 0, 1);
						ItemInstance item6 = getInventory().find("필리아 변신 카드", 0, 1);
						ItemInstance item7 = getInventory().find("플래티넘데스 변신 카드", 0, 1);
						ItemInstance item8 = getInventory().find("황금도플 변신 카드", 0, 1);
						ItemInstance item9 = getInventory().find("랭킹 변신 주문서", 0, 1);
						ItemInstance item10 = getInventory().find("데스나이트 변신 카드", 0, 1);
						ItemInstance item11 = getInventory().find("다크엘프 변신 카드", 0, 1);
						ItemInstance item12 = getInventory().find("달의질리언 변신 카드", 0, 1);
						ItemInstance item13 = getInventory().find("철의아툰 변신 카드", 0, 1);

						if (item != null && item2 == null && item3 == null && item4 == null && item5 == null
								&& item6 == null &&
								item7 == null && item8 == null && item9 == null && item10 == null && item11 == null
								&& item12 == null & item13 == null) {

							Poly p = PolyDatabase.getName("도펠갱어");

							if (p != null) {
								if (getGfx() != p.getGfxId()) {
									if (item != null && item.getCount() > 0) {
										item.toClick(this, null);
									}
								}

							}

						} else {
							// 자동 변신
							autoHuntPoly();
						}
						if (item == null && item2 != null && item3 == null && item4 == null && item5 == null
								&& item6 == null &&
								item7 == null && item8 == null && item9 == null && item10 == null && item11 == null
								&& item12 == null & item13 == null) {

							Poly p = PolyDatabase.getName("가드리아");

							if (p != null) {
								if (getGfx() != p.getGfxId()) {
									if (item2 != null && item2.getCount() > 0) {
										item2.toClick(this, null);
									}
								}

							}

						} else {
							// 자동 변신
							autoHuntPoly();
						}
						if (item == null && item2 == null && item3 != null && item4 == null && item5 == null
								&& item6 == null &&
								item7 == null && item8 == null && item9 == null && item10 == null && item11 == null
								&& item12 == null & item13 == null) {

							Poly p = PolyDatabase.getName("드루가");

							if (p != null) {
								if (getGfx() != p.getGfxId()) {
									if (item3 != null && item3.getCount() > 0) {
										item3.toClick(this, null);
									}
								}

							}

						} else {
							// 자동 변신
							autoHuntPoly();
						}
						if (item == null && item2 == null && item3 == null && item4 != null && item5 == null &&
								item6 == null && item7 == null && item8 == null && item9 == null && item10 == null
								&& item11 == null && item12 == null & item13 == null) {

							Poly p = PolyDatabase.getName("플래바포");

							if (p != null) {
								if (getGfx() != p.getGfxId()) {
									if (item4 != null && item4.getCount() > 0) {
										item4.toClick(this, null);
									}
								}

							}

						} else {
							// 자동 변신
							autoHuntPoly();
						}
						if (item == null && item2 == null && item3 == null && item4 == null && item5 != null
								&& item6 == null &&
								item7 == null && item8 == null && item9 == null && item10 == null && item11 == null
								&& item12 == null & item13 == null) {

							Poly p = PolyDatabase.getName("발키리 에이르");

							if (p != null) {
								if (getGfx() != p.getGfxId()) {
									if (item5 != null && item5.getCount() > 0) {
										item5.toClick(this, null);
									}
								}

							}

						} else {
							// 자동 변신
							autoHuntPoly();
						}
						if (item == null && item2 == null && item3 == null && item4 == null && item5 == null
								&& item6 != null && item7 == null && item8 == null && item9 == null && item10 == null
								&& item11 == null && item12 == null & item13 == null) {

							Poly p = PolyDatabase.getName("필리아");

							if (p != null) {
								if (getGfx() != p.getGfxId()) {
									if (item6 != null && item6.getCount() > 0) {
										item6.toClick(this, null);
									}
								}

							}

						} else {
							// 자동 변신
							autoHuntPoly();
						}
						if (item == null && item2 == null && item3 == null && item4 == null && item5 == null
								&& item6 == null && item7 != null && item8 == null && item9 == null && item10 == null
								&& item11 == null && item12 == null & item13 == null) {

							Poly p = PolyDatabase.getName("플래티넘데스");

							if (p != null) {
								if (getGfx() != p.getGfxId()) {
									if (item7 != null && item7.getCount() > 0) {
										item7.toClick(this, null);
									}
								}

							}

						} else {
							// 자동 변신
							autoHuntPoly();
						}
						if (item == null && item2 == null && item3 == null && item4 == null && item5 == null
								&& item6 == null && item7 == null && item8 != null && item9 == null && item10 == null
								&& item11 == null && item12 == null & item13 == null) {

							Poly p = PolyDatabase.getName("황금도플");

							if (p != null) {
								if (getGfx() != p.getGfxId()) {
									if (item8 != null && item8.getCount() > 0) {
										item8.toClick(this, null);
									}
								}

							}

						} else {
							// 자동 변신
							autoHuntPoly();
						}

						if (item == null && item2 == null && item3 == null && item4 == null && item5 == null
								&& item6 == null && item7 == null && item8 == null && item9 == null && item10 != null
								&& item11 == null && item12 == null & item13 == null) {

							Poly p = PolyDatabase.getName("데스나이트(신화)");

							if (p != null) {
								if (getGfx() != p.getGfxId()) {
									if (item10 != null && item10.getCount() > 0) {
										item10.toClick(this, null);
									}
								}

							}

						} else {
							// 자동 변신
							autoHuntPoly();
						}
						if (item == null && item2 == null && item3 == null && item4 == null && item5 == null
								&& item6 == null && item7 == null && item8 == null && item9 == null && item10 == null
								&& item11 != null && item12 == null & item13 == null) {

							Poly p = PolyDatabase.getName("다크엘프(신화)");

							if (p != null) {
								if (getGfx() != p.getGfxId()) {
									if (item11 != null && item11.getCount() > 0) {
										item11.toClick(this, null);
									}
								}

							}

						} else {
							// 자동 변신
							autoHuntPoly();
						}
						if (item == null && item2 == null && item3 == null && item4 == null && item5 == null
								&& item6 == null && item7 == null && item8 == null && item9 == null && item10 == null
								&& item11 == null && item12 != null & item13 == null) {

							Poly p = PolyDatabase.getName("달의질리언");

							if (p != null) {
								if (getGfx() != p.getGfxId()) {
									if (item12 != null && item12.getCount() > 0) {
										item12.toClick(this, null);
									}
								}

							}

						} else {
							// 자동 변신
							autoHuntPoly();
						}
						if (item == null && item2 == null && item3 == null && item4 == null && item5 == null
								&& item6 == null && item7 == null && item8 == null && item9 == null && item10 == null
								&& item11 == null && item12 == null & item13 != null) {

							Poly p = PolyDatabase.getName("철의아툰");

							if (p != null) {
								if (getGfx() != p.getGfxId()) {
									if (item13 != null && item13.getCount() > 0) {
										item13.toClick(this, null);
									}
								}

							}

						} else {
							// 자동 변신
							autoHuntPoly();
						}
						if (!is_auto_return_home) {
							if (World.isSafetyZone(getX(), getY(), getMap()) && getMap() != 70) {
								endAutoHunt(false, false);
								ChattingController.toChatting(this, "              \\fY세이프티존에서 할 수 없습니다.",
										Lineage.CHATTING_MODE_MESSAGE);

								return;
							}
							// 몬스터 검색
							findMonster();
							// 화면 갱신
							autoHuntStartLocation();
							// 야도란 변경
							if (autohunt_target != null) {
								if (autohunt_target.isDead() || autohunt_target.isWorldDelete()
										|| !World.isAttack(this, autohunt_target)
										|| !Util.isAreaAttack(this, autohunt_target)
										|| !Util.isAreaAttack(autohunt_target, this)) {
									autohunt_target = null;
									return;

								}
								toAiAttack(time);
							} else {
								if (!autoHuntTeleport(time)) {
									toAiWalk(time);
								} else {
									autoHuntTeleport(time);
								}

							}

						}
					}
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] toAutoHunt(long time)\r\n : %s\r\n", e.toString());
		}
	}

	@Override
	protected void toAiAttack(long time) {
		try {
			if (autoHuntSkill()) {
				return;
			}

			super.toAiAttack(time);

			if (this instanceof PcRobotInstance) {
				return;
			}

			auto_hunt_teleport_time = time + (Lineage.auto_hunt_telpeport_delay);

			object o = autohunt_target;

			// 활공격인지 판단.
			boolean bow = getInventory().활장착여부();
			int atkRange = bow ? 8 : 1;
			// 객체 거리 확인
			if (Util.isDistance(this, o, atkRange) && Util.isAreaAttack(this, o) && Util.isAreaAttack(o, this)) {
				// 물리공격 범위내로 잇을경우 처리.
				int frame = (int) (SpriteFrameDatabase.getSpeedCheckGfxFrameTime(this, getGfx(),
						getGfxMode() + Lineage.GFX_MODE_ATTACK) + 40);
				ai_time = frame;
				toAttack(o, o.getX(), o.getY(), bow, getGfxMode() + Lineage.GFX_MODE_ATTACK, 0, false);
			} else {
				int frame = (int) (SpriteFrameDatabase.getSpeedCheckGfxFrameTime(this, getGfx(),
						getGfxMode() + Lineage.GFX_MODE_WALK) + 50);
				ai_time = frame;
				autoHuntMoving(this, o.getX(), o.getY(), 0, true);
			}
		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] toAiAttack(long time)\r\n : %s\r\n", e.toString());
		}
	}

	@Override
	protected void toAiWalk(long time) {
		try {
			super.toAiWalk(time);

			if (this instanceof PcRobotInstance) {
				return;
			}

			do {
				switch (Util.random(0, 7)) {
					case 0:
					case 1:
						break;
					case 2:
						setHeading(getHeading() + 1);
						break;
					case 3:
					case 4:
					case 5:
						break;
					case 6:
						setHeading(getHeading() - 1);
						break;
					case 7:
						setHeading(Util.random(0, 7));
						break;
				}
				// 이동 좌표 추출.
				int x = Util.getXY(heading, true) + this.x;
				int y = Util.getXY(heading, false) + this.y;

				if ((getMap() == 0 || getMap() == 4) && !Util.isDistance(x, y, map, start_x, start_y, map, 60)
						&& auto_hunt_teleport_time < time) {
					toTeleport(start_x, start_y, start_map, true);
				}

				// 해당 좌표 이동가능한지 체크.
				boolean tail = World.isThroughObject(this.x, this.y, this.map, heading)
						&& (World.isMapdynamic(x, y, map) == false) && !World.isNotMovingTile(x, y, map);
				if (tail) {
					// 타일이 이동가능하고 객체가 방해안하면 이동처리.
					autoHuntMoving(this, x, y, heading, true);
					auto_hunt_teleport_time = time + (Lineage.auto_hunt_telpeport_delay);
				}
			} while (false);
		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] toAiWalk(long time)\r\n : %s\r\n", e.toString());
		}
	}

	/**
	 * 자동 사냥 몬스터 검색
	 */
	private void findMonster() {
		try {
			if (autohunt_target == null) {
				object temp = null;

				for (object o : getInsideList()) {
					if (o != null && o instanceof MonsterInstance && !o.isDead() && !o.isWorldDelete()
							&& World.isAttack(this, o) && Util.isAreaAttack(this, o) && Util.isAreaAttack(o, this)) {
						if (o instanceof SummonInstance) {
							SummonInstance s = (SummonInstance) o;

							if (s.getSummon() != null && s.getSummon().getMaster() != null
									&& s.getSummon().getMaster() instanceof PcInstance) {
								continue;
							}
						}

						MonsterInstance m = (MonsterInstance) o;

						if (m.getAttackList() == null || m.getAttackListSize() == 0
								|| (m.getAttackList(0) != null && m.getAttackList(0).getObjectId() == getObjectId())) {
							if (temp == null) {
								temp = o;
								continue;
							} else {
								int range_1 = Util.getDistance(this, o);
								int range_2 = Util.getDistance(this, temp);

								if (range_1 < range_2) {
									temp = o;
								}
							}
						}
					}
				}

				autohunt_target = temp;
			}
		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] findMonster()\r\n : %s\r\n", e.toString());
		}
	}

	/**
	 * 시작 위치에서 벗어났을경우 화면 갱신
	 */
	public void autoHuntStartLocation() {
		try {
			if (!Util.isDistance(temp_x, temp_y, temp_map, getX(), getY(), getMap(),
					Lineage.BOW_ATTACK_LOCATIONRANGE)) {
				temp_x = getX();
				temp_y = getY();
				temp_map = getMap();
				toTeleport(getX(), getY(), getMap(), false);
			}
		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] autoHuntStartLocation()\r\n : %s\r\n", e.toString());
		}
	}

	/**
	 * 설정한 체력 이하로 내려갈 경우 자동 귀환
	 */
	public void autoHuntGotoHome(long time) {

		try {
			if (isAutoHuntCheck()) {
				if (!is_auto_return_home && getHpPercent() < auto_return_home_hp) {
					auto_hunt_teleport_time = time + 2000;
					autohunt_target = null;
					is_auto_return_home = true;
					int[] home = null;
					home = Lineage.getHomeXY();
					toTeleport(home[0], home[1], home[2], true);
				}

				if (is_auto_return_home && getHpPercent() >= Lineage.auto_hunt_go_min_hp
						&& auto_hunt_teleport_time < time) {
					is_auto_return_home = false;
					toTeleport(start_x, start_y, start_map, true);
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] autoHuntGotoHome()\r\n : %s\r\n", e.toString());
		}
	}

	/**
	 * 매개변수 좌표로 A스타를 발동시켜 이동시키기. 객체가 존재하는 지역은 패스하도록 함. 이동할때마다 aStar가 새로 그려지기때문에
	 * 과부하가 심함.
	 */
	private boolean autoHuntMoving(object o, final int x, final int y, final int h, final boolean astar) {
		try {
			if (o == null)
				return false;

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

					toSender(new S_ObjectMoving(getObjectId(), iPath[0], iPath[1],
							Util.calcheading(this.x, this.y, iPath[0], iPath[1])));
					// toMoving(iPath[0], iPath[1], Util.calcheading(this.x, this.y, iPath[0],
					// iPath[1]));
					// toSender(S_ObjectMoving.clone(BasePacketPooling.getPool(S_ObjectMoving.class),
					// this));
					return true;
				} else {
					return false;
				}
			} else {
				toMoving(x, y, h);
				return true;
			}
		} catch (Exception e) {
			lineage.share.System.printf(
					"[자동 사냥] autoHuntMoving(object o, final int x, final int y, final int h, final boolean astar)\r\n : %s\r\n",
					e.toString());
		}

		return false;
	}

	/**
	 * 자동 스킬
	 */
	public boolean autoHuntSkill() {
		try {
			if (this instanceof PcRobotInstance) {
				return false;
			}
			int rand = new Random().nextInt(100) + 1;

			if (getInventory() != null && autohunt_target != null) {
				switch (getClassType()) {
					case Lineage.LINEAGE_CLASS_ROYAL:

						break;
					case Lineage.LINEAGE_CLASS_KNIGHT:

						break;
					case Lineage.LINEAGE_CLASS_ELF:
						if (getInventory().활장착여부()) {
							Skill s = SkillDatabase.find(115);
							Skill s1 = SkillDatabase.find(116);

							if (getMpPercent() < 50) {
								if (s1 != null && SkillController.find(this, s1.getUid()) != null) {
									ai_time = SpriteFrameDatabase.getGfxFrameTime(this, getGfx(),
											getGfxMode() + Lineage.GFX_MODE_SPELL_NO_DIRECTION);
									ai_time = 1500;
									BloodToSoul.init(this, s1);
									return true;
								}
							}

							if (rand < Lineage.is_auto_hunt_skill_percent) {

								if (s != null && SkillController.find(this, s.getUid()) != null) {
									if (SkillController.isHpMpCheck(this, s.getHpConsume(), s.getMpConsume())
											&& Util.isDistance(this, autohunt_target, 8)) {
										ai_time = 800;
										TripleArrow.init(this, s, (int) autohunt_target.getObjectId(),
												autohunt_target.getX(), autohunt_target.getY());
										return true;
									}
								}

							}

						}

						break;
					case Lineage.LINEAGE_CLASS_WIZARD:
						if (getInventory().getSlot(Lineage.SLOT_WEAPON) != null) {
							Skill s = SkillDatabase.find(46);
							Skill s19 = SkillDatabase.find(18);

							MonsterInstance mon = (MonsterInstance) autohunt_target;
							ItemInstance item33 = getInventory().find("자동사냥 턴언데드", 0, 1);

							if (item33 != null) {
								if (rand < Lineage.is_auto_hunt_skill_percent) {
									if (s19 != null && SkillController.find(this, s19.getUid()) != null) {
										if (SkillController.isFigure(this, mon, s19, true, false)) {
											if (SkillController.isDelay(this, SkillDatabase.find(18)))
												TurnUndead.onBuff(this, autohunt_target, s19, autohunt_target.getX(),
														autohunt_target.getY());
											return true;
										}
									}
								}
							}

							if (item33 == null) {
								if (rand < Lineage.is_auto_hunt_skill_percent) {
									if (s != null && SkillController.find(this, s.getUid()) != null) {
										if (SkillController.isHpMpCheck(this, s.getHpConsume(), s.getMpConsume())
												&& Util.isDistance(this, autohunt_target, 3)) {
											if (SkillController.isDelay(this, SkillDatabase.find(46)))
												Sunburst.init(this, s, (int) autohunt_target.getObjectId());
											return true;
										}
									}
								}
							} else {
								return false;
							}
						}

						break;

				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] autoHuntSkill()\r\n : %s\r\n", e.toString());
		}

		return false;
	}

	public boolean autoHuntbuff2() {
		try {
			if (this instanceof PcRobotInstance) {
				return false;
			}

			if (getInventory() != null) {
				switch (getClassType()) {
					case Lineage.LINEAGE_CLASS_ROYAL:
						// 브레이브멘탈
						Skill s15 = SkillDatabase.find(309);
						Skill s16 = SkillDatabase.find(307);
						Skill s17 = SkillDatabase.find(100);
						Skill s18 = SkillDatabase.find(101);

						if (s15 != null && SkillController.find(this, s15.getUid()) != null
								&& SkillController.isHpMpCheck(this, s15.getHpConsume(), s15.getMpConsume())) {

							if (BuffController.find(this, s15) == null) {
								BraveMental.init(this, s15);
								return true;
							}
						}
						if (s16 != null && SkillController.find(this, s16.getUid()) != null
								&& SkillController.isHpMpCheck(this, s16.getHpConsume(), s16.getMpConsume())) {

							if (BuffController.find(this, s16) == null) {
								BraveAvatar.init(this, s16);
								return true;
							}
						}
						if (s17 != null && SkillController.find(this, s17.getUid()) != null
								&& SkillController.isHpMpCheck(this, s17.getHpConsume(), s17.getMpConsume())) {

							if (BuffController.find(this, s17) == null) {
								GlowingWeapon.init(this, s17);
								return true;
							}
						}
						if (s18 != null && SkillController.find(this, s18.getUid()) != null
								&& SkillController.isHpMpCheck(this, s18.getHpConsume(), s18.getMpConsume())) {

							if (BuffController.find(this, s18) == null) {
								ShiningShield.init(this, s18);
								return true;
							}
						}

						break;
					case Lineage.LINEAGE_CLASS_KNIGHT:
						Skill s12 = SkillDatabase.find(55);
						Skill s13 = SkillDatabase.find(72);
						Skill s14 = SkillDatabase.find(80);

						if (s12 != null && SkillController.find(this, s12.getUid()) != null
								&& SkillController.isHpMpCheck(this, s12.getHpConsume(), s12.getMpConsume())) {

							if (BuffController.find(this, s12) == null) {
								ReductionArmor.init(this, s12);
								return true;
							}
						}
						if (s13 != null && SkillController.find(this, s13.getUid()) != null
								&& SkillController.isHpMpCheck(this, s13.getHpConsume(), s13.getMpConsume())
								&& (this.getInventory().getSlot(Lineage.SLOT_SHIELD) != null
										|| this.getInventory().getSlot(Lineage.SLOT_GUARDER) != null)) {

							if (BuffController.find(this, s13) == null) {
								SolidCarriage.init(this, s13);
								return true;
							}
						}
						if (s14 != null && SkillController.find(this, s14.getUid()) != null
								&& SkillController.isHpMpCheck(this, s14.getHpConsume(), s14.getMpConsume())
								&& this.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2()
										.equalsIgnoreCase("tohandsword")) {

							if (BuffController.find(this, s14) == null) {
								CounterBarrier.init(this, s14);
								return true;
							}
						}

						break;
					case Lineage.LINEAGE_CLASS_ELF:
						if (getInventory().getSlot(Lineage.SLOT_WEAPON) != null) {
							Skill s = SkillDatabase.find(115);
							// 아이오브 스톰
							Skill s2 = SkillDatabase.find(126);
							// 스톰샷
							Skill s3 = SkillDatabase.find(135);
							// 이글아이
							Skill s4 = SkillDatabase.find(117);
							// 레지스트매직
							Skill s5 = SkillDatabase.find(107);
							// 클리어마인드
							Skill s6 = SkillDatabase.find(113);
							// 레지스트 엘리멘트
							Skill s7 = SkillDatabase.find(114);
							// 버닝웨펀
							Skill s8 = SkillDatabase.find(124);
							// 엘리멘탈 파이어
							Skill s9 = SkillDatabase.find(125);
							// 소울오프프레임
							Skill s10 = SkillDatabase.find(136);
							// 네이쳐스
							Skill s11 = SkillDatabase.find(128);
							ItemInstance item = getInventory().find("정령옥", 0, 1);

							if (item == null || (item != null && item.getCount() <= 10)) {

								autoHuntBuyShop("잡화 상인", "정령옥", 100);

							}

							if (s2 != null && SkillController.find(this, s2.getUid()) != null && s3 == null
									&& SkillController.find(this, s3.getUid()) == null
									&& SkillController.isHpMpCheck(this, s2.getHpConsume(), s2.getMpConsume())
									&& this.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2()
											.equalsIgnoreCase("bow")) {

								if (BuffController.find(this, s2) == null) {
									EyeOfStorm.init(this, s2);
									return true;
								}
							}
							if (s3 != null && SkillController.find(this, s3.getUid()) != null
									&& SkillController.isHpMpCheck(this, s3.getHpConsume(), s3.getMpConsume())
									&& this.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2()
											.equalsIgnoreCase("bow")) {

								if (BuffController.find(this, s3) == null) {
									StormShot.init(this, s3, (int) this.getObjectId());
									return true;
								}

							}
							if (s4 != null && SkillController.find(this, s4.getUid()) != null
									&& SkillController.isHpMpCheck(this, s4.getHpConsume(), s4.getMpConsume())
									&& this.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2()
											.equalsIgnoreCase("bow")) {

								if (BuffController.find(this, s4) == null) {
									EagleEye.init(this, s4);
									return true;
								}

							}

							if (s5 != null && SkillController.find(this, s5.getUid()) != null
									&& SkillController.isHpMpCheck(this, s5.getHpConsume(), s5.getMpConsume())) {

								if (BuffController.find(this, s5) == null) {
									ResistMagic.init(this, s5);
									return true;
								}

							}
							if (s6 != null && SkillController.find(this, s6.getUid()) != null
									&& SkillController.isHpMpCheck(this, s6.getHpConsume(), s6.getMpConsume())) {

								if (BuffController.find(this, s6) == null) {
									ClearMind.init(this, s6);
									return true;
								}

							}
							if (s7 != null && SkillController.find(this, s7.getUid()) != null
									&& SkillController.isHpMpCheck(this, s7.getHpConsume(), s7.getMpConsume())) {

								if (BuffController.find(this, s7) == null) {
									ResistElemental.init(this, s7);
									return true;
								}
							}

							if (s8 != null && SkillController.find(this, s8.getUid()) != null
									&& SkillController.isHpMpCheck(this, s8.getHpConsume(), s8.getMpConsume())
									&& (this.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2()
											.equalsIgnoreCase("sword")
											|| this.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2()
													.equalsIgnoreCase("dagger"))) {

								if (BuffController.find(this, s8) == null) {
									BurningWeapon.init(this, s8);
									return true;
								}
							}
							if (s9 != null && SkillController.find(this, s9.getUid()) != null
									&& SkillController.isHpMpCheck(this, s9.getHpConsume(), s9.getMpConsume())
									&& (this.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2()
											.equalsIgnoreCase("sword")
											|| this.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2()
													.equalsIgnoreCase("dagger"))) {

								if (BuffController.find(this, s9) == null) {
									ElementalFire.init(this, s9);
									return true;
								}
							}
							if (s10 != null && SkillController.find(this, s10.getUid()) != null
									&& SkillController.isHpMpCheck(this, s10.getHpConsume(), s10.getMpConsume())
									&& (this.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2()
											.equalsIgnoreCase("sword")
											|| this.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2()
													.equalsIgnoreCase("dagger"))) {

								if (BuffController.find(this, s10) == null) {
									SoulOfFlame.init(this, s10);
									return true;
								}
							}
							if (s11 != null && SkillController.find(this, s11.getUid()) != null
									&& SkillController.isHpMpCheck(this, s11.getHpConsume(), s11.getMpConsume())) {

								if (BuffController.find(this, s11) == null) {
									NaturesTouch.init(this, s11, (int) this.getObjectId());
									return true;
								}
							}

						}

						break;
					case Lineage.LINEAGE_CLASS_WIZARD:
						// 이뮨투함
						Skill s21 = SkillDatabase.find(68);
						Skill s22 = SkillDatabase.find(68);
						ItemInstance item = getInventory().find("마력의 돌", 0, 1);

						if (item == null || (item != null && item.getCount() <= 10)) {

							autoHuntBuyShop("잡화 상인", "마력의 돌", 100);

						}
						if (s21 != null && SkillController.find(this, s21.getUid()) != null
								&& SkillController.isHpMpCheck(this, s21.getHpConsume(), s21.getMpConsume())) {

							if (BuffController.find(this, s21) == null) {
								ImmuneToHarm.init(this, s21, (int) this.getObjectId());
								return true;
							}
						}
						if (s22 != null && SkillController.find(this, s22.getUid()) != null
								&& SkillController.isHpMpCheck(this, s22.getHpConsume(), s22.getMpConsume())) {

							if (BuffController.find(this, s22) == null) {
								HolyWalk.init(this, s22);
								return true;
							}
						}

						break;

				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] autoHuntbuff()\r\n : %s\r\n", e.toString());
		}

		return false;
	}

	/**
	 * 자동 버프, 촐기, 용기
	 */
	public void autoHuntBuff(long time) {
		try {
			if (getInventory() != null) {
				if (is_auto_buff && auto_buff_time < time) {
					ItemInstance i = getInventory().find("버프 물약30일");
					if (getLevel() < Lineage.buff_max_level) {
						auto_buff_time = time + (Lineage.auto_buff_delay * 1000);
						CommandController.toBuff(this);
					} else if (i != null) {
						auto_buff_time = time + (Lineage.auto_buff_delay * 1000);
						CommandController.toBuff(this);
					} else {
						if (getInventory().isAden(Lineage.buff_aden, true)) {
							auto_buff_time = time + (Lineage.auto_buff_delay * 1000);
							CommandController.toBuff(this);
							ChattingController.toChatting(this,
									String.format("\\fR자동 버프: %,d아데나 소모", Lineage.buff_aden),
									Lineage.CHATTING_MODE_MESSAGE);
						}
					}
				}

				if (is_auto_haste && getSpeed() == 0) {
					ItemInstance item = getInventory().find(Lineage.auto_haste_item_name, 0, 1);

					if (item != null && item.getCount() > 0) {
						ai_time = 500;
						item.toClick(this, null);
					}
				}

				if (is_auto_bravery && !isBrave()) {
					String itemName = null;

					switch (getClassType()) {
						case Lineage.LINEAGE_CLASS_ROYAL:
							itemName = Lineage.auto_bravery_item_name_royal;
							break;
						case Lineage.LINEAGE_CLASS_KNIGHT:
							itemName = Lineage.auto_bravery_item_name_knight;
							break;
						case Lineage.LINEAGE_CLASS_ELF:
							itemName = Lineage.auto_bravery_item_name_elf;
							break;
						case Lineage.LINEAGE_CLASS_DARKELF:
							itemName = Lineage.auto_bravery_item_name_darkelf;
							break;
						case Lineage.LINEAGE_CLASS_WIZARD:
							if (!Lineage.is_auto_bravery_wizard_magic) {
								itemName = Lineage.auto_bravery_item_name_wizard;
							} else {
								try {
									Skill s = SkillDatabase
											.find(Integer.valueOf(Lineage.auto_bravery_item_name_wizard));

									if (s != null && SkillController.find(this, s.getUid()) != null) {
										ai_time = SpriteFrameDatabase.getGfxFrameTime(this, getGfx(),
												getGfxMode() + Lineage.GFX_MODE_SPELL_NO_DIRECTION);
										HolyWalk.init(this, s);
									}
								} catch (Exception e) {

								}
							}
							break;
					}

					if (itemName != null) {
						ItemInstance item = getInventory().find(itemName, 0, 1);

						if (item != null && item.getCount() > 0) {
							ai_time = 500;
							item.toClick(this, null);
						}
					}
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] autoHuntBuff(long time)\r\n : %s\r\n", e.toString());
		}
	}

	/**
	 * 자동 텔레포트 사용 가능한 맵 확인
	 * 
	 * @return
	 */
	public boolean isAutoHuntTeleportMap() {
		try {
			for (int map : Lineage.auto_hunt_teleport_map_list) {
				if (map == getMap()) {
					if (getMap() == 104 && this.getInventory().find("오만의 탑 4층 지배 부적") == null) {
						return false;
					}
					if (getMap() == 110 && this.getInventory().find("오만의 탑 10층 지배 부적") == null) {
						return false;
					}

					return true;
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] isAutoHuntTeleportMap()\r\n : %s\r\n", e.toString());
		}

		return false;
	}

	/**
	 * 자동 텔레포트
	 */
	public boolean autoHuntTeleport(long time) {

		try {

			if (is_auto_teleport && isAutoHuntTeleportMap() && auto_hunt_teleport_time < time) {
				// Thread.sleep(2500);
				auto_hunt_teleport_time = time + (Lineage.auto_hunt_telpeport_delay);
				autohunt_target = null;
				// Util.toRndLocation(this);
				// toTeleport(this.x, this.y, map, false);
				Util.toRndLocation(this);
				toTeleport(getHomeX(), getHomeY(), getHomeMap(), true);

				// toTeleport(getHomeX(), getHomeY(), getHomeMap(), true);
				//

			}
		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] autoHuntTeleport()\r\n : %s\r\n", e.toString());
		}

		return false;
	}

	/**
	 * 자동 변신
	 */
	public void autoHuntPoly() {
		try {
			if (getInventory() != null) {
				if (is_auto_poly_select) {
					if (is_auto_rank_poly) {
						Poly p = PolyDatabase.getName(getRankPolyName());

						if (p != null) {
							if (getGfx() != p.getGfxId()) {
								ItemInstance item = getInventory().find("무한 " + Lineage.auto_poly_rank_item_name, 0, 1);

								if (item != null) {
									if (item.getCount() > 0) {
										item.toClick(this, null);
										return;
									}
								}

								item = getInventory().find(Lineage.auto_poly_rank_item_name, 0, 1);

								if (item == null
										|| (item != null && item.getCount() <= Lineage.auto_poly_rank_buy_min_count)) {
									if (Lineage.is_auto_poly_rank_buy && is_auto_rank_poly_buy) {
										int allRank = RankController.getAllRank(this.getObjectId());
										if (allRank > 0 && allRank <= Lineage.rank_poly_all) {
											autoHuntBuyShop(Lineage.auto_poly_rank_buy_npc,
													Lineage.auto_poly_rank_item_name, Lineage.auto_poly_rank_buy_count);
											item = getInventory().find(Lineage.auto_poly_rank_item_name, 0, 1);
										}
									}
								}

								if (item != null) {
									if (item.getCount() > 0) {
										item.toClick(this, null);
										return;
									}
								}
							}
						}
					}

					if (is_auto_poly && getQuickPolymorph() != null) {
						Poly temp = PolyDatabase.getName(getRankPolyName());
						if (temp != null && getGfx() == temp.getGfxId()) {
							return;
						}

						Poly p = PolyDatabase.getName(getQuickPolymorph());

						if (p != null) {
							if (getGfx() != p.getGfxId() && (Lineage.event_poly || p.getMinLevel() <= getLevel())) {
								ItemInstance item = getInventory().find(Lineage.auto_poly_item_name, 0, 1);

								if (item == null
										|| (item != null && item.getCount() <= Lineage.auto_poly_buy_min_count)) {
									if (Lineage.is_auto_poly_buy && is_auto_poly_buy) {
										autoHuntBuyShop(Lineage.auto_poly_buy_npc, Lineage.auto_poly_item_name,
												Lineage.auto_poly_buy_count);
										item = getInventory().find(Lineage.auto_poly_item_name, 0, 1);
									}
								}

								if (item != null) {
									if (item.getCount() > 0) {
										setTempPoly(true);
										setTempPolyScroll(item);

										ShapeChange.init(this, this, PolyDatabase.getPolyName(getQuickPolymorph()),
												1800, getTempPolyScroll().getBless());
										return;
									}
								}
							}
						}
					}
				} else {
					if (is_auto_poly && getQuickPolymorph() != null) {
						Poly p = PolyDatabase.getName(getQuickPolymorph());

						if (p != null) {
							if (getGfx() != p.getGfxId()) {
								ItemInstance item = getInventory().find(Lineage.auto_poly_item_name, 0, 1);

								if (item == null
										|| (item != null && item.getCount() <= Lineage.auto_poly_buy_min_count)) {
									if (Lineage.is_auto_poly_buy && is_auto_poly_buy) {
										autoHuntBuyShop(Lineage.auto_poly_buy_npc, Lineage.auto_poly_item_name,
												Lineage.auto_poly_buy_count);
										item = getInventory().find(Lineage.auto_poly_item_name, 0, 1);
									}
								}

								if (item != null) {
									if (item.getCount() > 0) {
										setTempPoly(true);
										setTempPolyScroll(item);

										ShapeChange.init(this, this, PolyDatabase.getPolyName(getQuickPolymorph()),
												1800, getTempPolyScroll().getBless());
										return;
									}
								}
							}
						}
					}

					if (is_auto_rank_poly) {
						Poly temp = PolyDatabase.getName(getQuickPolymorph());
						if (temp != null && getGfx() == temp.getGfxId()) {
							return;
						}

						Poly p = PolyDatabase.getName(getRankPolyName());

						if (p != null) {
							if (getGfx() != p.getGfxId()
									&& (Lineage.event_rank_poly || p.getMinLevel() <= getLevel())) {
								ItemInstance item = getInventory().find("무한 " + Lineage.auto_poly_rank_item_name, 0, 1);

								if (item != null) {
									if (item.getCount() > 0) {
										item.toClick(this, null);
										return;
									}
								}

								item = getInventory().find(Lineage.auto_poly_rank_item_name, 0, 1);

								if (item == null
										|| (item != null && item.getCount() <= Lineage.auto_poly_rank_buy_min_count)) {
									if (Lineage.is_auto_poly_rank_buy && is_auto_rank_poly_buy) {
										autoHuntBuyShop(Lineage.auto_poly_rank_buy_npc,
												Lineage.auto_poly_rank_item_name, Lineage.auto_poly_rank_buy_count);
										item = getInventory().find(Lineage.auto_poly_rank_item_name, 0, 1);
									}
								}

								if (item != null) {
									if (item.getCount() > 0) {
										item.toClick(this, null);
										return;
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] autoHuntPoly()\r\n : %s\r\n", e.toString());
		}
	}

	/**
	 * 랭커 변신 이름 리턴
	 * 
	 * @return
	 */
	public String getRankPolyName() {
		String polyName = null;

		switch (getClassType()) {
			case Lineage.LINEAGE_CLASS_ROYAL:
				if (getClassSex() == 0)
					polyName = "왕자 랭커";
				else
					polyName = "공주 랭커";
				break;
			case Lineage.LINEAGE_CLASS_KNIGHT:
				if (getClassSex() == 0)
					polyName = "남자기사 랭커";
				else
					polyName = "여자기사 랭커";
				break;
			case Lineage.LINEAGE_CLASS_ELF:
				if (getClassSex() == 0)
					polyName = "남자요정 랭커";
				else
					polyName = "여자요정 랭커";
				break;
			case Lineage.LINEAGE_CLASS_WIZARD:
				if (getClassSex() == 0)
					polyName = "남자법사 랭커";
				else
					polyName = "여자법사 랭커";
				break;
			case Lineage.LINEAGE_CLASS_DARKELF:
				if (getClassSex() == 0)
					polyName = "남자다엘 랭커";
				else
					polyName = "여자다엘 랭커";
				break;
		}

		return polyName;
	}

	/**
	 * 구매하려는 아이템을 판매하는 상점 찾은 후 구매
	 * 
	 * @param item_name
	 * @return
	 */
	public void autoHuntBuyShop(String npc_name, String item_name, long item_count) {
		try {
			if (npc_name != null) {
				ShopInstance si = null;

				for (ShopInstance temp : NpcSpawnlistDatabase.getShopList()) {
					if (temp != null && temp.getNpc() != null && temp.getNpc().getName() != null
							&& temp.getNpc().getName().equalsIgnoreCase(npc_name)) {
						for (Shop s : temp.getNpc().getShop_list()) {
							if (s.getItemName().equalsIgnoreCase(item_name) && s.getItemBress() == 1
									&& s.getAdenType().equalsIgnoreCase("아데나")) {
								si = temp;
								break;
							}
						}

						if (si != null) {
							break;
						}
					}
				}

				if (si != null) {
					Shop s = si.getNpc().findShopItemId(item_name, 1);

					if (s != null) {
						Item i = ItemDatabase.find(s.getItemName());

						if (i != null) {
							int shop_price = 0;

							if (s.getPrice() != 0) {
								shop_price = si.getTaxPrice(s.getPrice(), false);
							} else {
								shop_price = si.getTaxPrice(i.getShopPrice() * s.getItemCount(), false);
							}

							long new_item_count = item_count * s.getItemCount();
							long price = shop_price * item_count;

							if (getInventory().isAppend(i, item_count, i.isPiles() ? 1 : new_item_count, false)) {
								if (getInventory().isAden(s.getAdenType(), price, false)) {
									ServerBasePacket sbp = (ServerBasePacket) ServerBasePacket
											.clone(BasePacketPooling.getPool(ServerBasePacket.class), null);
									sbp.writeC(0); // opcode
									sbp.writeC(0); // 상점구입
									sbp.writeH(1); // 구매할 전체 갯수
									sbp.writeD(s.getUid()); // 상점 아이템 고유값
									sbp.writeD(item_count); // 구매 갯수.
									byte[] data = sbp.getBytes();
									BasePacketPooling.setPool(sbp);
									BasePacket bp = ClientBasePacket.clone(
											BasePacketPooling.getPool(ClientBasePacket.class), data, data.length);
									// 처리 요청.
									si.toDwarfAndShop(this, (ClientBasePacket) bp);
									// 메모리 재사용.
									BasePacketPooling.setPool(bp);

									ChattingController.toChatting(this, String.format("\\fY자동 구매: %s(%,d) -> %,d아데나 소모",
											item_name, item_count, price), Lineage.CHATTING_MODE_MESSAGE);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf(
					"[자동 사냥] autoHuntBuyShop(String npc_name, String item_name, long item_count)\r\n : %s\r\n",
					e.toString());
		}
	}

	/**
	 * 자동 사냥시 아이템 자동 구매
	 */
	public void autoHuntItemBuy() {
		try {
			if (getInventory() != null) {
				if (Lineage.is_auto_potion_buy && isAutoPotion && is_auto_potion_buy && autoPotionName != null) {
					ItemInstance item = getInventory().find(autoPotionName, 0, 1);

					if (item == null || (item != null && item.getCount() <= Lineage.auto_potion_buy_min_count)) {
						autoHuntBuyShop(Lineage.auto_potion_buy_npc, autoPotionName, Lineage.auto_potion_buy_count);
					}
				}

				if (Lineage.is_auto_haste_buy && is_auto_haste && is_auto_haste_buy) {
					ItemInstance item = getInventory().find(Lineage.auto_haste_item_name, 0, 1);

					if (item == null || (item != null && item.getCount() <= Lineage.auto_haste_buy_min_count)) {
						autoHuntBuyShop(Lineage.auto_haste_buy_npc, Lineage.auto_haste_item_name,
								Lineage.auto_haste_buy_count);
					}
				}

				if (Lineage.is_auto_bravery && is_auto_bravery && is_auto_bravery_buy) {
					String itemName = null;

					switch (getClassType()) {
						case Lineage.LINEAGE_CLASS_ROYAL:
							itemName = Lineage.auto_bravery_item_name_royal;
							break;
						case Lineage.LINEAGE_CLASS_KNIGHT:
							itemName = Lineage.auto_bravery_item_name_knight;
							break;
						case Lineage.LINEAGE_CLASS_ELF:
							itemName = Lineage.auto_bravery_item_name_elf;
							break;
						case Lineage.LINEAGE_CLASS_DARKELF:
							itemName = Lineage.auto_bravery_item_name_darkelf;
							break;
						case Lineage.LINEAGE_CLASS_WIZARD:
							if (!Lineage.is_auto_bravery_wizard_magic) {
								itemName = Lineage.auto_bravery_item_name_wizard;
							}
							break;
					}

					if (itemName != null) {
						ItemInstance item = getInventory().find(itemName, 0, 1);

						if (item == null || (item != null && item.getCount() <= Lineage.auto_bravery_buy_min_count)) {
							autoHuntBuyShop(Lineage.auto_bravery_buy_npc, itemName, Lineage.auto_bravery_buy_count);
						}
					}
				}

				if (Lineage.is_auto_arrow_buy && is_auto_arrow_buy && getInventory().활장착여부()) {
					if (getInventory().getSlot(Lineage.SLOT_ARROW) == null) {
						ItemInstance item = getInventory().find(Lineage.auto_arrow_item_name, 0, 1);

						if (item == null) {
							autoHuntBuyShop(Lineage.auto_arrow_buy_npc, Lineage.auto_arrow_item_name,
									Lineage.auto_arrow_buy_count);

							item = getInventory().find(Lineage.auto_arrow_item_name, 0, 1);
							if (item != null) {
								item.toClick(this, null);
							}
						} else {
							item.toClick(this, null);
						}

						if (getInventory().getSlot(Lineage.SLOT_ARROW) == null) {
							endAutoHunt(true, false);
						}
					}
				}
			}

		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] autoHuntItemBuy()\r\n : %s\r\n", e.toString());
		}
	}

	public void addShopItem(PcInstance pc, ClientBasePacket cbp) {
		try {
			cbp.readC(); // type
			int count = cbp.readH();

			if (count == 0) {
				pc.setPersnalShopInsert(false);
				return;
			}

			if (count > 0) {
				long item_id = 0;
				int item_count = 0;

				for (int i = 0; i < count; ++i) {
					item_id = cbp.readD();
					item_count = (int) cbp.readD();

					if (item_count < 0 || item_count > 2000000000)
						continue;

					ItemInstance item = pc.getInventory().findByObjId(item_id);
					if (item == null)
						continue;

					// ▼▼▼ [에러 해결] 중복 등록 방지 방어막 ▼▼▼
					boolean isDuplicate = false;
					java.util.List<party_auto_sell.AutosellItem> currentList = party_auto_sell.AutosellDatabase
							.getList(pc.getObjectId());
					if (currentList != null) {
						for (party_auto_sell.AutosellItem exist : currentList) {
							if (exist.getName() != null && exist.getName().equals(item.getItem().getName())) {
								isDuplicate = true;
								break;
							}
						}
					}
					if (isDuplicate) {
						lineage.world.controller.ChattingController.toChatting(pc,
								"\\fW[안내] 이미 등록된 아이템입니다: " + item.getItem().getName(),
								lineage.share.Lineage.CHATTING_MODE_MESSAGE);
						continue; // 중복이면 DB에 억지로 넣지 않고 자연스럽게 패스!
					}
					// ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲

					AutosellItem psItem = new AutosellItem();
					psItem.setchaobjid(this.getObjectId());
					psItem.setCharName(this.getName());
					psItem.setItemobjid(item_id);
					psItem.setItem_id((int) item_id);
					psItem.setName(item.getItem().getName());
					psItem.setBless(item.getBless());
					psItem.setEnLevel(item.getEnLevel());
					psItem.setInvGfx(item.getItem().getInvGfx());

					if (sellList == null)
						sellList = new java.util.ArrayList<AutosellItem>();
					sellList.add(psItem);

					party_auto_sell.AutosellDatabase.UpdateautoSell(pc, psItem);

					// ▼▼▼ [여기에 추가!] 등록 완료 즉시 인벤토리에 있는 템 팔아버리기! ▼▼▼
					if (pc.is_auto_sell) {
						pc.tryAutoSellOnPickup(item);
					}
					// ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
				}

				lineage.world.controller.ChattingController.toChatting(pc, "자동매입 물품 추가 완료",
						lineage.share.Lineage.CHATTING_MODE_MESSAGE);
				pc.setPersnalShopInsert(false);
			}
		} catch (Exception e) {
			lineage.share.System.println("[자동판매 등록 에러] addShopItem : " + e.toString());
		}
	}

	/**
	 * 자동 사냥시 아이템 자동 판매
	 */
	public void autoHuntItemSell() {

		List<ItemInstance> sellitemlist1 = new ArrayList<ItemInstance>();

		try {
			if (getInventory() != null) {

				if (this.is_auto_sell) {
					for (AutosellItem item : AutosellDatabase.getList(this.getObjectId())) {

						if (item.getchaobjid() == this.getObjectId()) {

							ItemInstance sellitem = this.getInventory().find(item.getName(), item.getEnLevel(),
									item.getBless());

							String name = null;
							long solvent_cnt = 0;
							long price = 0;
							long count = 0;

							if (sellitem != null) {

								if (NpcSpawnlistDatabase.sellShop == null
										|| NpcSpawnlistDatabase.sellShop.getNpc() == null
										|| !sellitem.getItem().isSell())
									solvent_cnt = 0;

								Shop shop = NpcSpawnlistDatabase.sellShop.getNpc()
										.findShopItemId(sellitem.getItem().getName(), sellitem.getBless());
								if (shop != null && shop.isItemSell()) {
									if (shop.getPrice() != 0)
										solvent_cnt = shop.getPrice();

									sellitemlist1.add(sellitem);

									for (ItemInstance si : sellitemlist1) {
										count = si.getCount();
										price = solvent_cnt * count;
										name = si.getItem().getName();
									}

									Item i = ItemDatabase.find("아데나");

									if (i != null) {
										ItemInstance temp = this.getInventory().find(i.getName(), i.isPiles());

										if (temp == null) {
											// 겹칠수 있는 아이템이 존재하지 않을경우.
											if (i.isPiles()) {
												temp = ItemDatabase.newInstance(i);
												temp.setObjectId(ServerDatabase.nextItemObjId());
												temp.setBless(1);
												temp.setEnLevel(0);
												temp.setCount(price);
												temp.setDefinite(true);
												this.getInventory().append(temp, true);
											} else {
												for (int idx = 0; idx < solvent_cnt; idx++) {
													temp = ItemDatabase.newInstance(i);
													temp.setObjectId(ServerDatabase.nextItemObjId());
													temp.setBless(1);
													temp.setEnLevel(0);
													temp.setDefinite(true);
													this.getInventory().append(temp, true);
												}
											}
										} else {
											// 겹치는 아이템이 존재할 경우.
											this.getInventory().count(temp, temp.getCount() + price, true);
										}

										Log.appendItem(this, "type|자동매입 매입금", "매입금|" + solvent_cnt,
												"아이템|" + sellitem.getItem().getName(),
												"아이템_objid|" + sellitem.getObjectId());
										ChattingController.toChatting(this, String.format("[자동매입] %s(%d개): %s(%d) 획득.",
												name, count, i.getName(), price), Lineage.CHATTING_MODE_MESSAGE);

										// 아이템 수량 갱신
										this.getInventory().count(sellitem, 0, true);

									}

								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("[자동 사냥] autoHuntItemSell()\r\n : %s\r\n", e.toString());
		}
	}

	public void addDeleteSellShopItem(PcInstance pc, ClientBasePacket cbp) {
		try {
			cbp.readC(); // type
			int count = cbp.readH();

			if (count == 0) {
				pc.setPersnalShopSellEdit(false);
				return;
			}

			if (count > 0) {
				for (int i = 0; i < count; ++i) {
					long item_id = cbp.readD();
					int item_count = (int) cbp.readD();

					if (item_count < 0 || item_count > 2000000000)
						continue;

					// ★ 핵심: 구버전의 findItem()을 쓰지 않고, 가짜(Dummy) 객체를 만들어
					// DB 캐시 전담 매니저에게 "이 번호표 가진 애 지워줘!" 하고 명령만 내립니다.
					party_auto_sell.AutosellItem dummy = new party_auto_sell.AutosellItem();
					dummy.setchaobjid(pc.getObjectId());
					dummy.setItemobjid(item_id);

					party_auto_sell.AutosellDatabase.deleteautoSell(dummy, pc);
				}

				pc.setPersnalShopSellEdit(false); // 스위치 끄기
				lineage.world.controller.ChattingController.toChatting(pc, "\\fS[알림] 매입 아이템 삭제가 완료되었습니다.",
						lineage.share.Lineage.CHATTING_MODE_MESSAGE);

				// 삭제 후 창이 자동으로 새로고침 되도록 추가
				pc.showAutoSellHtml();
			}
		} catch (Exception e) {
			lineage.share.System.println("[물품삭제 에러] addDeleteSellShopItem : " + e.toString());
			e.printStackTrace();
		}
	}

	private AutosellItem findItem(long objid) {
		try {

			for (AutosellItem item : sellList) {
				if (item.getItemobjid() == objid) {
					return item;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void deleteSellShopItem(AutosellItem item) {
		AutosellItem shopItem = null;
		for (Iterator<AutosellItem> iter = sellList.iterator(); iter.hasNext();) {
			shopItem = iter.next();
			if (shopItem != null && shopItem.getItemobjid() == item.getItemobjid()) {
				iter.remove();
			}
		}
	}

	public void sendProto(ProtoOutputStream stream, boolean isClear) {
		client.sendProto(stream, isClear);
	}

	public void sendProto(ProtoOutputStream stream) {
		client.sendProto(stream);
	}

	public void sendProto(MJIProtoMessage message, int messageId, boolean isClear) {
		client.sendProto(message, messageId, isClear);
	}

	public void sendProto(MJIProtoMessage message, MJEProtoMessages messageId) {
		client.sendProto(message, messageId);
	}

	public void sendProto(MJIProtoMessage message, int messageId) {
		client.sendProto(message, messageId);
	}

	public void sendProto(MJIProtoMessage message, MJEProtoMessages messageId, boolean isClear) {
		client.sendProto(message, messageId, isClear);
	}

	private final ClientSetting mClientSetting = ClientSetting.newEmpty(this);

	public ClientSetting getClientSetting() {
		return mClientSetting;
	}
}
