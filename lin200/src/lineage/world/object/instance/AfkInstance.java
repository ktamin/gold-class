package lineage.world.object.instance;

import lineage.bean.database.Item;
import lineage.bean.lineage.Inventory;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.database.AfkDatabase; // 💡 추가됨
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.AfkController;
import lineage.world.object.Character;

public class AfkInstance extends Character {
	private long pc_objectId;
	private int pc_accountUid;
	private String pc_name;
	private Inventory inventory;
	private long startTime; // 무인잠수 시작 시간 저장
	
	private long premium_item_time;
	private boolean isMemberStatus;
	
	public boolean isMemberStatus() { return isMemberStatus; } // 💡 DB 저장을 위해 추가
	public long getStartTime() { return startTime; }

	// 1️⃣ 기존 생성자 (주인이 게임 끄고 무인잠수 시작할 때)
	public AfkInstance(PcInstance pc) {
		this.pc_objectId = pc.getObjectId();
		this.pc_accountUid = pc.getAccountUid();
		this.pc_name = pc.getName() != null ? pc.getName() : pc.getTempName() == null ? "" : pc.getTempName();
		this.inventory = new Inventory().clone(this);
		this.isMemberStatus = pc.isMember();
		this.premium_item_time = 0; 
		this.startTime = System.currentTimeMillis();

		setObjectId(ServerDatabase.nextEtcObjId()); // 새 고유번호 발급
//		setName(pc_name + "(무인)");
		setName(pc_name + " ");
		setClanId(0);
		setClanName("");
		setTitle("");
		setLawful(66536);
		setGfx(pc.getClassGfx());
		setGfxMode(pc.getGfxMode());
		setX(pc.getX());
		setY(pc.getY());
		setMap(pc.getMap());
		setHeading(pc.getHeading());
		
		toTeleport(getX(), getY(), getMap(), false);
		AfkController.appendAfkRobot(pc_accountUid, this);
		
		// 💡 DB에 방금 만든 로봇 정보 기록!
		AfkDatabase.insert(this); 
	}

	// 2️⃣ 💡 신규 생성자 (서버 켜질 때 DB에서 죽은 로봇들을 부활시킬 때 사용)
		// 맨 끝에 long startTime 이 추가되었습니다!
		public AfkInstance(int accountUid, long pcObjId, String pcName, long botObjId, int x, int y, int map, int heading, int gfx, int gfxMode, boolean isMember, long startTime) {
			this.pc_accountUid = accountUid;
			this.pc_objectId = pcObjId;
			this.pc_name = pcName;
			
			this.startTime = startTime; // 💡 DB에서 불러온 시작 시간을 로봇에게 기억시킵니다!
			
			this.inventory = new Inventory().clone(this);
			this.isMemberStatus = isMember;
			this.premium_item_time = 0;

			setObjectId(botObjId);
//			setName(pc_name + "(무인)");
			setName(pc_name + " ");
			setClanId(0);
			setClanName("");
			setTitle("");
			setLawful(66536);
			setGfx(gfx);
			setGfxMode(gfxMode);
			setX(x);
			setY(y);
			setMap(map);
			setHeading(heading);
			
			toTeleport(getX(), getY(), getMap(), false);
			AfkController.appendAfkRobot(pc_accountUid, this);

			// 💡 [최종 정답 적용] 회원님의 서버 팩 전용 가방 로딩 메서드!
			lineage.database.CharactersDatabase.readInventory(this);
		}

	@Override
	public void close() {
		super.close();
		pc_objectId = 0L;
		pc_accountUid = 0;
		pc_name = null;
		inventory = null;
	}

	public long getPc_objectId() { return pc_objectId; }
	public int getPc_accountUid() { return pc_accountUid; }
	public String getPc_name() { return pc_name == null ? "" : pc_name; }
	public Inventory getInventory() { return inventory; }

	public void toWorldOut() {
		AfkController.removeAfkRobot(pc_accountUid);
		
		// 💡 로봇이 월드에서 지워질 때 DB 기록도 깔끔하게 삭제!
		AfkDatabase.delete(pc_accountUid);
		
		inventory.close();
		clearList(true); // 남은 아이템은 DB에서 완벽히 소각 (복사버그 방지)
		World.remove(this);
		close();
	}

	@Override
	public void toTimer(long time) {
		try {
			if (premium_item_time == 0) {
				premium_item_time = time + Lineage.world_premium_item_delay;
			}

			if (Lineage.world_premium_item_is && premium_item_time <= time) {
				if (premium_item_time != 0) {
					int x = getX();
					int y = getY();
					int map = getMap();

					if (map == 4 && x >= 33401 && x <= 33456 && y >= 32784 && y <= 32837) {
						boolean canReceive = true;
						if (Lineage.world_premium_item_member_only && !this.isMemberStatus) {
							canReceive = false;
						}

						if (canReceive && inventory != null) {
							Item i = ItemDatabase.find(Lineage.world_premium_item);
							if (i != null) {
								ItemInstance temp = inventory.find(i.getName(), i.isPiles());
								int count = Util.random(Lineage.world_premium_item_min, Lineage.world_premium_item_max);

								if (temp == null) {
									temp = ItemDatabase.newInstance(i);
									temp.setObjectId(ServerDatabase.nextItemObjId());
									temp.setCount(count);
									temp.setDefinite(true);
									// 💡 [핵심] false -> true 로 변경하여 획득 시점마다 DB에 실시간 백업!
									inventory.append(temp, true); 
								} else {
									// 💡 [핵심] false -> true 로 변경
									inventory.count(temp, temp.getCount() + count, true);
								}
							}
						}
					}
				}
				premium_item_time = time + Lineage.world_premium_item_delay;
			}
		} catch (Exception e) {}
	}
	
	// =======================================================
		// 💡 [추가] 무인잠수 로봇 강제어택 사망 방지 (무적 처리)
		// =======================================================

		@Override
		public void setNowHp(int hp) {
			// 외부에서 들어오는 데미지로 인해 체력이 깎이려고 하면 무시합니다.
			if (hp < getNowHp()) {
				return; 
			}
			// 피를 채워주는 효과(힐 등)는 정상적으로 받습니다.
			super.setNowHp(hp);
		}

		@Override
		public boolean isDead() {
			// 시스템에서 이 캐릭터가 죽었는지 물어볼 때 항상 "살아있다(false)"고 답합니다.
			return false;
		}

		@Override
		public void setDead(boolean dead) {
			// 강제로 사망 상태를 부여하는 명령이 들어와도 절대 죽지 않도록 고정합니다.
			super.setDead(false);
		}
}