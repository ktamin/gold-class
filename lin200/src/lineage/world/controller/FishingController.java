package lineage.world.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import lineage.bean.database.FishList;
import lineage.bean.database.Item;
import lineage.bean.lineage.Inventory;
import lineage.database.CharactersDatabase;
import lineage.database.DatabaseConnection;
import lineage.database.FishItemListDatabase;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.LineageServer;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Disconnect;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.thread.AutoFishThread;
import lineage.util.Util;
import lineage.world.object.object;
import lineage.world.object.instance.FishermanInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.ShapeChange;

public final class FishingController {
	static private Map<Integer, FishermanInstance> auto_fish_list;

	static public void init() {
		TimeLine.start("FishingController..");

		auto_fish_list = new HashMap<Integer, FishermanInstance>();

		auto_fish_list.clear();

		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM auto_fish_list");
			rs = st.executeQuery();

			while (rs.next()) {
				FishermanInstance fi = new FishermanInstance(rs.getLong("pc_objId"), rs.getInt("account_uid"),
						rs.getString("pc_name"), rs.getInt("gfx"), rs.getInt("gfx_mode"), rs.getInt("loc_x"),
						rs.getInt("loc_y"), rs.getInt("loc_map"), rs.getInt("heading"), rs.getInt("fish_time"));

				fi.toWorldJoin(con);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", FishingController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}

		AutoFishThread.init();

		TimeLine.end();
	}

	static public Map<Integer, FishermanInstance> getFishRobotList() {
		synchronized (auto_fish_list) {
			return new HashMap<Integer, FishermanInstance>(auto_fish_list);
		}
	}

	static public void appendFishRobot(int accountUid, FishermanInstance fi) {
		synchronized (auto_fish_list) {
			if (!auto_fish_list.containsKey(accountUid))
				auto_fish_list.put(accountUid, fi);
		}
	}

	static public void removeFishRobot(int accountUid) {
		synchronized (auto_fish_list) {
			if (auto_fish_list.containsKey(accountUid))
				auto_fish_list.remove(accountUid);
		}
	}

	static public FishermanInstance getFishRobot(int accountUid) {
		synchronized (auto_fish_list) {
			return auto_fish_list.get(accountUid);
		}
	}

	static public int getFishRobotListSize() {
		return auto_fish_list.size();
	}

	/**
	 * 게임 접속시 자동낚시 확인.
	 * 2020-03-11
	 * by connector12@nate.com
	 */
	static public void toWorldJoin(object o) {
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			FishermanInstance fi = FishingController.getFishRobot(pc.getAccountUid());
			if (fi != null && fi.getInventory() != null && fi.getPc_objectId() == pc.getObjectId())
				fi.toWorldOut(true);
		}
	}

	/**
	 * 게임 종료시 자동낚시 확인.
	 * 2020-03-11
	 * by connector12@nate.com
	 */
	static public boolean toWorldOut(object o) {
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			FishermanInstance fi = FishingController.getFishRobot(pc.getAccountUid());
			if (fi != null && fi.getInventory() != null && fi.getPc_objectId() == pc.getObjectId())
				return true;
		}

		return false;
	}

	static public void close(Connection con) {
		AutoFishThread.close();

		PreparedStatement st = null;
		try {
			st = con.prepareStatement("DELETE FROM auto_fish_list");
			st.executeUpdate();
			st.close();

			for (FishermanInstance fi : getFishRobotList().values()) {
				if (fi == null || fi.getInventory() == null)
					continue;

				saveInventory(con, fi);

				st = con.prepareStatement(
						"INSERT INTO auto_fish_list SET account_uid=?, pc_objId=?, pc_name=?, loc_x=?, loc_y=?, loc_map=?, heading=?, gfx=?, gfx_mode=?, fish_time=?");
				st.setInt(1, fi.getPc_accountUid());
				st.setLong(2, fi.getPc_objectId());
				st.setString(3, fi.getPc_name());
				st.setInt(4, fi.getX());
				st.setInt(5, fi.getY());
				st.setInt(6, fi.getMap());
				st.setInt(7, fi.getHeading());
				st.setInt(8, fi.getGfx());
				st.setInt(9, fi.getGfxMode());
				st.setInt(10, fi.getFishTime());
				st.executeUpdate();
				st.close();
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : close(Connection con)\r\n", FishingController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st);
		}
	}

	static public void save(Connection con) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("DELETE FROM auto_fish_list");
			st.executeUpdate();
			st.close();

			for (FishermanInstance fi : getFishRobotList().values()) {
				if (fi == null || fi.getInventory() == null)
					continue;

				saveInventory(con, fi);

				st = con.prepareStatement(
						"INSERT INTO auto_fish_list SET account_uid=?, pc_objId=?, pc_name=?, loc_x=?, loc_y=?, loc_map=?, heading=?, gfx=?, gfx_mode=?, fish_time=?");
				st.setInt(1, fi.getPc_accountUid());
				st.setLong(2, fi.getPc_objectId());
				st.setString(3, fi.getPc_name());
				st.setInt(4, fi.getX());
				st.setInt(5, fi.getY());
				st.setInt(6, fi.getMap());
				st.setInt(7, fi.getHeading());
				st.setInt(8, fi.getGfx());
				st.setInt(9, fi.getGfxMode());
				st.setInt(10, fi.getFishTime());
				st.executeUpdate();
				st.close();
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : close(Connection con)\r\n", FishingController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st);
		}
	}

	static public void toSave(FishermanInstance fi) {
		Connection con = null;
		PreparedStatement st = null;

		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement(
					"INSERT INTO auto_fish_list SET account_uid=?, pc_objId=?, pc_name=?, loc_x=?, loc_y=?, loc_map=?, heading=?, gfx=?, gfx_mode=?, fish_time=?");
			st.setInt(1, fi.getPc_accountUid());
			st.setLong(2, fi.getPc_objectId());
			st.setString(3, fi.getPc_name());
			st.setInt(4, fi.getX());
			st.setInt(5, fi.getY());
			st.setInt(6, fi.getMap());
			st.setInt(7, fi.getHeading());
			st.setInt(8, fi.getGfx());
			st.setInt(9, fi.getGfxMode());
			st.setInt(10, fi.getFishTime());
			st.executeUpdate();
			st.close();
		} catch (Exception e) {
			lineage.share.System.printf("%s : close(Connection con)\r\n", FishingController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	static public void toDelete(FishermanInstance fi) {
		Connection con = null;
		PreparedStatement st = null;

		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("DELETE FROM auto_fish_list WHERE pc_objId=?");
			st.setLong(1, fi.getPc_objectId());
			st.executeUpdate();
			st.close();
		} catch (Exception e) {
			lineage.share.System.printf("%s : toDelete(FishermanInstance fi)\r\n", FishingController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	/**
	 * 변신 상태일 경우 변신해제를 위해 메시지 창을 띄워서 y/n을 입력받았을때 처리
	 */
	public static void toAsk(PcInstance pc, boolean yes) {
		if (yes && pc.isFishingZone()) {
			BuffController.remove(pc, ShapeChange.class);
			ChattingController.toChatting(pc, "변신 해제", Lineage.CHATTING_MODE_MESSAGE);
			if (pc.getClassType() == 0) {
				if (pc.getClassSex() == 0) {
					pc.setGfx(0);
				} else {
					pc.setGfx(1);
				}
			}
			if (pc.getClassType() == 1) {
				if (pc.getClassSex() == 0) {
					pc.setGfx(61);
				} else {
					pc.setGfx(48);
				}
			}
			if (pc.getClassType() == 2) {
				if (pc.getClassSex() == 0) {
					pc.setGfx(138);
				} else {
					pc.setGfx(37);
				}
			}
			if (pc.getClassType() == 3) {
				if (pc.getClassSex() == 0) {
					pc.setGfx(734);
				} else {
					pc.setGfx(1186);
				}
			}
			if (pc.getClassType() == 4) {
				if (pc.getClassSex() == 0) {
					pc.setGfx(2786);
				} else {
					pc.setGfx(2796);
				}
			}
			pc.getTempFishing().toClick(pc, null);
		}
	}

	/*
	 * public static void startFishing(PcInstance pc) {
	 * ItemInstance fishing = pc.getInventory().getSlot(Lineage.SLOT_WEAPON);
	 * 
	 * if (Lineage.open_wait) {
	 * ChattingController.toChatting(pc, "오픈대기 상태에서는 불가능합니다",
	 * Lineage.CHATTING_MODE_MESSAGE);
	 * return;
	 * }
	 * // 인벤토리에서 영양 미끼 찾음
	 * if (fishing != null) {
	 * // 낚시중 기본 gfx가 아닐경우 낚시종료.
	 * if (pc.getGfx() != pc.getClassGfx()) {
	 * pc.setFishing(false);
	 * fishing.toClick(pc, null);
	 * ChattingController.toChatting(pc, "세트 아이템 착용 중 일 경우 낚시가 불가능합니다.",
	 * Lineage.CHATTING_MODE_MESSAGE);
	 * return;
	 * }
	 * 
	 * ItemInstance rice =
	 * pc.getInventory().find(ItemDatabase.find(Lineage.fish_rice));
	 * // 영양미끼가 없으면 낚시 종료
	 * if (rice == null) {
	 * pc.setFishing(false);
	 * fishing.toClick(pc, null);
	 * // 영양 미끼가 충분치 않습니다.
	 * pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 776,
	 * Lineage.fish_rice));
	 * return;
	 * } else {
	 * // 영양 미끼가 있을 경우, 낚시 시작. 주기적으로 위치 체크
	 * if (pc.isFishingZone()) {
	 * fishing(pc, rice, fishing);
	 * } else {
	 * pc.setFishing(false);
	 * fishing.toClick(pc, null);
	 * 
	 * }
	 * }
	 * }
	 * }
	 */
	public static void startFishing(PcInstance pc) {
		ItemInstance fishing = pc.getInventory().getSlot(Lineage.SLOT_WEAPON);

		if (Lineage.open_wait) {
			ChattingController.toChatting(pc, "오픈대기 상태에서는 불가능합니다", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		if (fishing == null)
			return;

		// 낚시중 기본 gfx가 아닐경우 낚시종료.
		if (pc.getGfx() != pc.getClassGfx()) {
			pc.setFishing(false);
			fishing.toClick(pc, null);
			ChattingController.toChatting(pc, "세트 아이템 착용 중 일 경우 낚시가 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		// 1) 기본 영양 미끼 찾기
		ItemInstance rice = pc.getInventory().find(ItemDatabase.find(Lineage.fish_rice));
		// 2) 없으면 "무한" 미끼 탐색 (이름에 '미끼'와 '무한' 포함)
		if (rice == null) {
			for (ItemInstance it : pc.getInventory().getList()) {
				if (it == null || it.getItem() == null)
					continue;
				String nm = it.getItem().getName();
				if (nm != null && nm.contains("미끼") && nm.contains("무한")) {
					rice = it; // "영양 미끼(무한)" 같은 아이템
					break;
				}
			}
		}

		// 3) 미끼 최종 검증
		if (rice == null) {
			pc.setFishing(false);
			fishing.toClick(pc, null);
			// 영양 미끼가 충분치 않습니다.
			pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 776, Lineage.fish_rice));
			return;
		}

		// 4) 낚시 시작
		if (pc.isFishingZone()) {
			fishing(pc, rice, fishing);
		} else {
			pc.setFishing(false);
			fishing.toClick(pc, null);
		}
	}

	// 낚싯대 마다 시간체크하여 물고기 낚음
	public static void fishing(PcInstance pc, ItemInstance fishRice, ItemInstance fishing) {
		if (fishing != null) {
			// if (pc.getFishingTime() + (1000 * Lineage.fish_delay) <
			// System.currentTimeMillis()) {
			// huntFish(pc);
			// pc.setFishingTime(System.currentTimeMillis());
			// pc.getInventory().count(fishRice, fishRice.getCount() - 1, true);
			// }
			// }
			// }
			if (pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getName().contains("고급 성장의 낚싯대")
					&& pc.getFishingTime() + (1000 * (Lineage.fish_delay / 2)) < System.currentTimeMillis()) {
				huntFish(pc);
				pc.setFishingTime(System.currentTimeMillis());
				// pc.getInventory().count(fishRice, fishRice.getCount() - 1, true);
				if (fishRice != null && fishRice.getItem() != null) {
					String nm = fishRice.getItem().getName();
					if (nm == null || !nm.contains("무한")) {
						pc.getInventory().count(fishRice, fishRice.getCount() - 1, true);
					}
				}
			} else if (pc.getFishingTime() + (1000 * Lineage.fish_delay) < System.currentTimeMillis()) {
				huntFish(pc);
				pc.setFishingTime(System.currentTimeMillis());
				// pc.getInventory().count(fishRice, fishRice.getCount() - 1, true);
				if (fishRice != null && fishRice.getItem() != null) {
					String nm = fishRice.getItem().getName();
					if (nm == null || !nm.contains("무한")) {
						pc.getInventory().count(fishRice, fishRice.getCount() - 1, true);
					}
				}
			}
		}
	}

	// 물고기 낚는 처리
	public static void huntFish(PcInstance pc) {
		Item i = ItemDatabase.find(Lineage.fish_exp);
		// 경험치 지급단
		if (i != null) {
			ItemInstance temp = pc.getInventory().find(i.getName(), i.isPiles());

			if (temp == null) {
				temp = ItemDatabase.newInstance(i);
				temp.setObjectId(ServerDatabase.nextItemObjId());
				temp.setBless(1);
				temp.setEnLevel(0);
				temp.setCount(1);
				temp.setDefinite(true);
				pc.getInventory().append(temp, true);
			} else {
				// 겹치는 아이템이 존재할 경우.
				pc.getInventory().count(temp, temp.getCount() + 1, true);
			}
		}

		if (FishItemListDatabase.getFishList().size() > 0) {
			// fishing_item_list 테이블의 목록중 랜덤으로 하나 추출
			FishList fishList = FishItemListDatabase.getFishList()
					.get(Util.random(0, FishItemListDatabase.getFishList().size() - 1));

			if (fishList != null) {
				Item ii = ItemDatabase.find(fishList.getItemName());

				if (ii != null) {
					ItemInstance temp = pc.getInventory().find(fishList.getItemName(), fishList.getItemBless(),
							ii.isPiles());
					int count = Util.random(fishList.getItemCountMin(), fishList.getItemCountMax());

					if (temp != null && (temp.getBless() != fishList.getItemBless()
							|| temp.getEnLevel() != fishList.getItemEnchant()))
						temp = null;

					if (temp == null) {
						// 겹칠수 있는 아이템이 존재하지 않을경우.
						if (ii.isPiles()) {
							temp = ItemDatabase.newInstance(ii);
							temp.setObjectId(ServerDatabase.nextItemObjId());
							temp.setBless(fishList.getItemBless());
							temp.setEnLevel(fishList.getItemEnchant());
							temp.setCount(count);
							temp.setDefinite(true);
							pc.getInventory().append(temp, true);
						} else {
							for (int idx = 0; idx < count; idx++) {
								temp = ItemDatabase.newInstance(ii);
								temp.setObjectId(ServerDatabase.nextItemObjId());
								temp.setBless(fishList.getItemBless());
								temp.setEnLevel(fishList.getItemEnchant());
								temp.setDefinite(true);
								pc.getInventory().append(temp, true);
							}
						}
					} else {
						// 겹치는 아이템이 존재할 경우.
						pc.getInventory().count(temp, temp.getCount() + count, true);
					}

					ChattingController.toChatting(pc, String.format("%s(%d) 획득: 낚시", fishList.getItemName(), count),
							Lineage.CHATTING_MODE_MESSAGE);
				}
			}
		}
	}

	/**
	 * 자동낚시가 가능한지 여부를 체크하는 함수
	 * 
	 * @param
	 * @return
	 *         2017-09-06
	 *         by all_night.
	 */
	public static void isAutoFishing(PcInstance pc) {
		if (getFishRobot(pc.getAccountUid()) != null) {
			ChattingController.toChatting(pc, "계정에 자동낚시 중인 캐릭터가 존재합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if (PcMarketController.getShop(pc.getObjectId()) != null) {
			ChattingController.toChatting(pc, "상점 운영중엔 자동낚시를 할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		// 군터의 인장 찾아서 체크
		ItemInstance coin = pc.getInventory().find(ItemDatabase.find(Lineage.auto_fish_coin));
		ItemInstance rice = pc.getInventory().find(ItemDatabase.find(Lineage.fish_rice));

		// 자동낚시 미끼 체크
		if (rice == null) {
			for (ItemInstance it : pc.getInventory().getList()) {
				if (it == null || it.getItem() == null)
					continue;
				String nm = it.getItem().getName();
				if (nm != null && nm.contains("미끼") && nm.contains("무한")) {
					rice = it; // 무한 미끼로 인정
					break;
				}
			}
		}

		if (coin == null || coin.getCount() < Lineage.auto_fish_expense) {
			// 군터의인장이 충분치 않습니다.
			pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 776, Lineage.auto_fish_coin));
			return;
		}

		if (rice == null) {
			// 영양 미끼가 충분치 않습니다.
			pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 776, Lineage.fish_rice));
			return;
		}

		// 자동낚시를 시작하시겠습니까? (y/N)
		pc.toSender(S_MessageYesNo.clone(BasePacketPooling.getPool(S_MessageYesNo.class), 778));
	}

	public static void startAutoFishing(PcInstance pc, boolean yes) {
		if (yes && pc.isFishing() && !pc.isLock() && !pc.isWorldDelete() && !pc.isDead()) {
			ChattingController.toChatting(pc, "자동낚시 시작. 클라이언트가 종료됩니다.", Lineage.CHATTING_MODE_MESSAGE);

			try {
				FishermanInstance fi = new FishermanInstance(pc);
				toSave(fi);
			} catch (Exception e) {
				lineage.share.System.printf("%s : startAutoFishing(PcInstance pc, boolean yes)\r\n",
						FishingController.class.toString());
				lineage.share.System.println(e);
			}

			// 사용자 강제종료 시키기.
			pc.toSender(S_Disconnect.clone(BasePacketPooling.getPool(S_Disconnect.class), 0x0A));
			LineageServer.close(pc.getClient());
		}
	}

	/**
	 * 캐릭터 삭제시
	 * 2019-06-25
	 * by connector12@nate.com
	 */
	static public void removeCharacter(Connection con, long objId, int accountId) {
		FishermanInstance fi = getFishRobot(accountId);

		if (fi != null && fi.getPc_objectId() == objId)
			fi.toWorldOut(false);

		PreparedStatement st = null;
		try {
			st = con.prepareStatement("DELETE FROM auto_fish_list WHERE pc_objId=? AND account_uid=?");
			st.setLong(1, objId);
			st.setInt(2, accountId);
			st.executeUpdate();
			st.close();
		} catch (Exception e) {
			lineage.share.System.printf("%s : removeCharacter(Connection con, long objId)\r\n",
					FishingController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st);
		}
	}

	static public void toTimer(long time) {
		for (FishermanInstance fi : getFishRobotList().values()) {
			fi.toTimer(time);
		}
	}

	static public void saveFishermanInstance(FishermanInstance fi) {
		Connection con = null;

		try {
			con = DatabaseConnection.getLineage();
			saveInventory(con, fi);
		} catch (Exception e) {

		} finally {
			DatabaseConnection.close(con);
		}
	}

	static public void saveAllFishermanInstance() {
		Connection con = null;

		try {
			con = DatabaseConnection.getLineage();
			for (FishermanInstance fi : getFishRobotList().values())
				saveInventory(con, fi);
		} catch (Exception e) {

		} finally {
			DatabaseConnection.close(con);
		}
	}

	static public void saveInventory(Connection con, FishermanInstance fi) {
		if (fi != null && fi.getInventory() != null) {
			PreparedStatement st = null;

			try {
				Inventory inv = fi.getInventory();

				if (inv != null) {
					//
					st = con.prepareStatement("DELETE FROM characters_inventory WHERE cha_objId=?");
					st.setLong(1, fi.getPc_objectId());
					st.executeUpdate();
					st.close();

					//
					for (ItemInstance item : inv.getList()) {
						if (item.getItem() == null)
							continue;
						if (!item.getItem().isInventorySave())
							continue;

						try {
							st = con.prepareStatement("INSERT INTO characters_inventory SET "
									+ "objId=?, cha_objId=?, cha_name=?, name=?, count=?, quantity=?, en=?, equipped=?, definite=?, bress=?, "
									+ "durability=?, nowtime=?, pet_objid=?, inn_key=?, letter_uid=?, slimerace=?, 구분1=?, 구분2=?, enfire=?, enwater=?, enwind=?, enearth=?, dolloption_a=?,dolloption_b=?,dolloption_c=?,dolloption_d=?,dolloption_e=?");
							st.setLong(1, item.getObjectId());
							st.setLong(2, fi.getPc_objectId());
							st.setString(3, fi.getPc_name());
							st.setString(4, item.getItem().getName());
							st.setLong(5, item.getCount());
							st.setInt(6, item.getQuantity());
							st.setInt(7, item.getEnLevel());
							st.setInt(8, item.isEquipped() ? 1 : 0);
							st.setInt(9, item.isDefinite() ? 1 : 0);
							st.setInt(10, item.getBless());
							st.setInt(11, item.getDurability());
							st.setInt(12, item.getNowTime());
							st.setLong(13, item.getPetObjectId());
							st.setLong(14, item.getInnRoomKey());
							st.setInt(15, item.getLetterUid());
							st.setString(16, item.getRaceTicket());
							st.setString(17, item.getItem().getType1());
							st.setString(18, item.getItem().getType2());
							st.setInt(19, item.getEnFire());
							st.setInt(20, item.getEnWater());
							st.setInt(21, item.getEnWind());
							st.setInt(22, item.getEnEarth());
							st.setInt(23, item.getInvDolloptionA());
							st.setInt(24, item.getInvDolloptionB());
							st.setInt(25, item.getInvDolloptionC());
							st.setInt(26, item.getInvDolloptionD());
							st.setInt(27, item.getInvDolloptionE());
							st.executeUpdate();
						} catch (Exception e) {
							lineage.share.System.printf("%s : 자동낚시 인벤 저장 에러. 캐릭명: %s  아이템: %s(%d)\r\n",
									CharactersDatabase.class.toString(), fi.getPc_name(), item.getItem().getName(),
									item.getCount());
							lineage.share.System.println(e);
						} finally {
							DatabaseConnection.close(st);
						}
					}
				}
			} catch (Exception e) {
				lineage.share.System.printf("%s : saveInventory(FishermanInstance fi)\r\n",
						FishingController.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(st);
			}
		}
	}

	static public void readInventory(Connection con, FishermanInstance fi) {
		if (fi != null && fi.getInventory() != null) {
			Inventory inv = fi.getInventory();

			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				st = con.prepareStatement("SELECT * FROM characters_inventory WHERE cha_objId=?");
				st.setLong(1, fi.getPc_objectId());
				rs = st.executeQuery();
				while (rs.next()) {
					// 중복 제거.
					if (fi.getInventory().find(rs.getInt(1)) != null)
						continue;

					ItemInstance item = ItemDatabase.newInstance(ItemDatabase.find(rs.getString("name")));
					if (item != null && item.getItem() != null) {
						item.setObjectId(rs.getInt(1));
						item.setCount(rs.getLong(5));
						item.setQuantity(rs.getInt(6));
						item.setEnLevel(rs.getInt(7));
						item.setEquipped(rs.getInt(8) == 1);
						item.setDefinite(rs.getInt(9) == 1);
						item.setBless(rs.getInt(10));
						item.setDurability(rs.getInt(11));
						item.setNowTime(rs.getInt(12));
						item.setPetObjectId(rs.getInt(13));
						item.setInnRoomKey(rs.getInt(14));
						item.setLetterUid(rs.getInt(15));
						item.setRaceTicket(rs.getString(16));
						item.setEnFire(rs.getInt(19));
						item.setEnWater(rs.getInt(20));
						item.setEnWind(rs.getInt(21));
						item.setEnEarth(rs.getInt(22));
						item.setInvDolloptionA(rs.getInt(23));
						item.setInvDolloptionB(rs.getInt(24));
						item.setInvDolloptionC(rs.getInt(25));
						item.setInvDolloptionD(rs.getInt(26));
						item.setInvDolloptionE(rs.getInt(27));
						inv.appendList(item);
					}
				}
			} catch (Exception e) {
				lineage.share.System.printf("%s : readInventory(FishermanInstance fi)\r\n",
						FishingController.class.toString());
				lineage.share.System.println(e + "   캐릭터: " + fi.getPc_name());
			} finally {
				DatabaseConnection.close(st, rs);
			}
		}
	}
}
