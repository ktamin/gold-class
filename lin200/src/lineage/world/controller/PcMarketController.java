package lineage.world.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import lineage.bean.database.Item;
import lineage.bean.database.PcShop;
import lineage.bean.database.marketPrice;
import lineage.database.CharacterMarbleDatabase;
import lineage.database.CharactersDatabase;
import lineage.database.DatabaseConnection;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ObjectLock;
import lineage.network.packet.server.S_ObjectName;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.PcShopInstance;
import lineage.world.object.npc.시세검색;

public class PcMarketController {

	static private Map<Long, PcShopInstance> shop_list;
	static public object marketPriceNPC;

	static public void init(Connection con) {
		TimeLine.start("PcMarketController..");

		shop_list = new HashMap<Long, PcShopInstance>();
		marketPriceNPC = new 시세검색();
		marketPriceNPC.setObjectId(ServerDatabase.nextEtcObjId());

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM pc_shop_robot");
			rs = st.executeQuery();
			
			while (rs.next()) {
				PcShopInstance pc_shop = shop_list.get(rs.getLong("pc_objId"));
				if (pc_shop == null) {
					pc_shop = new PcShopInstance(rs.getLong("pc_objId"), rs.getString("pc_name"), rs.getInt("class_type"), rs.getInt("class_sex"));
					shop_list.put(rs.getLong("pc_objId"), pc_shop);
					pc_shop.setX(rs.getInt("loc_x"));
					pc_shop.setY(rs.getInt("loc_y"));
					pc_shop.setMap(rs.getInt("loc_map"));
					pc_shop.setHeading(rs.getInt("heading"));
					pc_shop.shop_comment = rs.getString("ment");
					
					if (pc_shop.getX() > 0 && pc_shop.getY() > 0)
						pc_shop.toTeleport(pc_shop.getX(), pc_shop.getY(), pc_shop.getMap(), false);
				}
			}

			rs.close();
			st.close();

			st = con.prepareStatement("SELECT * FROM pc_shop");
			rs = st.executeQuery();
			while (rs.next()) {
				PcShopInstance pc_shop = checkPcShop(con, rs.getLong("pc_objId"));		
				PcShop s = new PcShop(pc_shop, rs.getLong("price"), rs.getString("aden_type"), rs.getLong("count"));
				s.setInvItemObjectId(rs.getLong("item_objId"));
				s.setItem(ItemDatabase.find(rs.getString("item_name")));
				s.setInvItemEn(rs.getInt("en_level"));
				s.setInvItemBress(rs.getInt("bless"));
				s.setInvItemDefinite(rs.getString("definite").equalsIgnoreCase("true"));
				pc_shop.appendItem(s.getInvItemObjectId(), s);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", PcMarketController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}

		TimeLine.end();
	}
	
	static private PcShopInstance checkPcShop(Connection con, long objId) {
		PcShopInstance pc_shop = shop_list.get(objId);
		
		if (pc_shop == null) {
			PreparedStatement st = null;
			ResultSet rs = null;
			
			try {
				st = con.prepareStatement("SELECT * FROM characters WHERE objID=?");
				st.setLong(1, objId);
				rs = st.executeQuery();
				
				if (rs.next()) {
					pc_shop = new PcShopInstance(rs.getLong("objID"), rs.getString("name"), rs.getInt("class"), rs.getInt("sex"));
					shop_list.put(rs.getLong("objID"), pc_shop);
				}
			} catch (Exception e) {
				lineage.share.System.printf("%s : checkPcShop(Connection con, long objId)\r\n", PcMarketController.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(st, rs);
			}
		}
		return pc_shop;
	}

	static synchronized public Object toCommand(object o, String cmd, StringTokenizer st) {
		if (cmd.equalsIgnoreCase(Lineage.command + "상점")) {
			shop((PcInstance) o, st);
			return true;
		}
		return null;
	}

	static private void shop(PcInstance pc, StringTokenizer st) {	
		try {		
			String type = st.nextToken();
			
			// 시장맵인지 확인
			if (pc.getMap() != Lineage.market_map) {
				ChattingController.toChatting(pc, "\\fR상점 명령어는 시장에서 이용가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			// 초기화
			PcShopInstance pc_shop = getShop(pc.getObjectId());
			if (pc_shop == null) {
				pc_shop = new PcShopInstance(pc.getObjectId(), pc.getName(), pc.getClassType(), pc.getClassSex());
				appendShop(pc.getObjectId(), pc_shop);
			}

			if (type.equalsIgnoreCase("시작")) {
				shopStart(pc, st, pc_shop);
			} else if (type.equalsIgnoreCase("추가")) {
				appendItem(pc, st, pc_shop);
			} else if (type.equalsIgnoreCase("홍보")) {
				ment(pc, st, pc_shop);
			} else if (type.equalsIgnoreCase("종료")) {
				deleteShop(pc, st, pc_shop);
			} else if (type.equalsIgnoreCase("목록")) {
				shopList(pc, st, pc_shop);
			} else {
				ChattingController.toChatting(pc, "\\fR[.상점] 옵션 : 시작, 추가, 목록, 홍보, 종료", Lineage.CHATTING_MODE_MESSAGE);
			}
		} catch (Exception e) {
			ChattingController.toChatting(pc, "\\fR---------- 상점 명령어 ---------", Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(pc, "\\fRex) .상점 시작", Lineage.CHATTING_MODE_MESSAGE);
			if (Lineage.is_market_only_aden)
				ChattingController.toChatting(pc, "\\fRex) .상점 추가 판매가격 아이템수량", Lineage.CHATTING_MODE_MESSAGE);
			else
				ChattingController.toChatting(pc, "\\fRex) .상점 추가 ['아덴' or '달러'] 판매가격 아이템수량", Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(pc, "\\fRex) .상점 목록", Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(pc, "\\fRex) .상점 홍보 홍보멘트", Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(pc, "\\fRex) .상점 종료", Lineage.CHATTING_MODE_MESSAGE);
		}
	}
	
	/**
	 * 상점 시작.
	 * 2019-08-19
	 * by connector12@nate.com
	 */
	static private void shopStart(PcInstance pc, StringTokenizer st, PcShopInstance pc_shop) {
		if (pc_shop.getShopList().size() > 0) {
			if (pc_shop.getX() == 0 || pc_shop.getY() == 0)
				insertShopRobot(pc, pc_shop); 

			// 사용자 샵 객체 스폰시키기.
			pc_shop.setHeading(pc.getHeading());
			pc_shop.toTeleport(pc.getX(), pc.getY(), pc.getMap(), false);
			updateShopRobot(pc_shop);
			ChattingController.toChatting(pc, "\\fR상점이 시작되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			
			// 사용자 강제종료 시키기.
			// pc.toSender(S_Disconnect.clone(BasePacketPooling.getPool(S_Disconnect.class), 0x0A) );
			// LineageServer.close(pc.getClient());
		} else {
			ChattingController.toChatting(pc, "\\fR상점에 물품을 등록 후 사용하시기 바랍니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
	}
	
	/**
	 * 상점 추가.
	 * 2019-08-19
	 * by connector12@nate.com
	 */
	static private void appendItem(PcInstance pc, StringTokenizer st, PcShopInstance pc_shop) {
		try {				
			if (pc_shop.getItem(0L) == null) {
				if (pc_shop.getListSize() >= Lineage.market_max_count) {
					ChattingController.toChatting(pc, String.format("\\fR물품은 최대 %d개까지 등록가능합니다.", Lineage.market_max_count), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				String aden = null;
				
				if (Lineage.is_market_only_aden) {
					aden = "아데나";
				} else {
					String adenType = st.nextToken();
					if (adenType.equalsIgnoreCase("아덴"))
						aden = "아데나";
					else if (adenType.equalsIgnoreCase("달러"))
						aden = "달러";
					else {
						ChattingController.toChatting(pc, "\\fRex) .상점 추가 ['아덴' or '달러'] 판매가격 아이템수량", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
				}
					
				long price = Long.valueOf(st.nextToken());
				if (price < 1 || price > 2000000000) {
					ChattingController.toChatting(pc, "\\fR가격이 잘못되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				long count = Long.valueOf(st.nextToken());
				if (count < 1 || count > 2000000000) {
					ChattingController.toChatting(pc, "\\fR수량이 잘못되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				pc_shop.appendItem(0L, new PcShop(pc_shop, price, aden, count));
				ChattingController.toChatting(pc, "\\fR판매할 물품을 인벤토리에서 더블클릭하세요.", Lineage.CHATTING_MODE_MESSAGE);
			} else {
				ChattingController.toChatting(pc, "\\fR판매할 물품을 인벤토리에서 더블클릭하세요.", Lineage.CHATTING_MODE_MESSAGE);
			}
		} catch (Exception e) {
			if (Lineage.is_market_only_aden)
				ChattingController.toChatting(pc, "\\fRex) .상점 추가 판매가격 아이템수량", Lineage.CHATTING_MODE_MESSAGE);
			else
				ChattingController.toChatting(pc, "\\fRex) .상점 추가 ['아덴' or '달러'] 판매가격 아이템수량", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
	}
	
	/**
	 * 홍보 멘트.
	 * 2019-08-19
	 * by connector12@nate.com
	 */
	static private void ment(PcInstance pc, StringTokenizer st, PcShopInstance pc_shop) {
		StringBuffer sb = new StringBuffer();
		while (st.hasMoreTokens()) {
			sb.append(st.nextToken());
			if (st.hasMoreTokens())
				sb.append(" ");
		}
		pc_shop.shop_comment = sb.toString();
		updateShopRobot(pc_shop);
		ChattingController.toChatting(pc, "\\fR\"" + sb.toString() + "\" 홍보멘트 설정", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	/**
	 * 상점 종료.
	 * 2019-08-19
	 * by connector12@nate.com
	 */
	static private void deleteShop(PcInstance pc, StringTokenizer st, PcShopInstance pc_shop) {
		if (pc_shop.getListSize() > 0) {
			for (PcShop s : pc_shop.getShopList().values()) {
				if (s.getItem() == null)
					continue;
				Item i = ItemDatabase.find(s.getItem().getName());
				if (i != null) {
					ItemInstance temp = pc.getInventory().find(i.getName(), s.getInvItemBress(), i.isPiles());

					if (temp != null && (temp.getBless() != s.getInvItemBress() || temp.getEnLevel() != s.getInvItemEn()))
						temp = null;

					if (temp == null) {
						// 겹칠수 있는 아이템이 존재하지 않을경우.
						if (i.isPiles()) {
							temp = ItemDatabase.newInstance(i);
							temp.setObjectId(s.getInvItemObjectId() == 0 ? ServerDatabase.nextItemObjId() : s.getInvItemObjectId());
							temp.setBless(s.getInvItemBress());
							temp.setEnLevel(s.getInvItemEn());
							temp.setCount(s.getInvItemCount());
							temp.setDefinite(true);
							pc.getInventory().append(temp, true);
						} else {
							for (int idx = 0; idx < s.getInvItemCount(); idx++) {
								temp = ItemDatabase.newInstance(i);
								temp.setObjectId(s.getInvItemObjectId() == 0 ? ServerDatabase.nextItemObjId() : s.getInvItemObjectId());
								temp.setBless(s.getInvItemBress());
								temp.setEnLevel(s.getInvItemEn());
								temp.setDefinite(true);
								pc.getInventory().append(temp, true);
							}
						}
					} else {
						// 겹치는 아이템이 존재할 경우.
						pc.getInventory().count(temp, temp.getCount() + s.getInvItemCount(), true);
					}
					
					deleteItem(s);
				}
			}
			pc_shop.clearShopList();
		}
		pc_shop.close();
		removeShop(pc.getObjectId());
		deleteShopRobot(pc);

		ChattingController.toChatting(pc, "\\fR상점이 종료되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	/**
	 * 상점 시세.
	 * 2019-08-19
	 * by connector12@nate.com
	 */
	static public void shopPrice(PcInstance pc, StringTokenizer st) {
		try {
			pc.marketPrice.clear();
			List<String> list = new ArrayList<String>();
			String itemName = st.nextToken();
			int en = 0;
			int bless = 1;
			int index = 1;
			boolean isEn = false;
			boolean isBless = false;
			
			// 인챈 검색
			if (st.hasMoreTokens()) {
				en = Integer.valueOf(st.nextToken());
				
				if (en != 0)
					isEn = true;
			}
			
			// 축복 여부 검색
			if (st.hasMoreTokens()) {
				switch (st.nextToken()) {
				case "축" :
				case "축복" :
					bless = 0;
					isBless = true;
					break;
				case "일반" :
					bless = 1;
					isBless = true;
					break;
				case "저주" :
					bless = 2;
					isBless = true;
					break;
				}
			}				

			list.add((bless == 0 ? "(축) " : bless == 2 ? "(저주) " : "") + (en > 0 ? "+" + en + " " : en < 0 ? en + " " : "") + itemName);

			for (PcShopInstance shpoList : getShopList().values()) {
				if (shpoList.getX() > 0 && shpoList.getY() > 0 && shpoList.getPc_objectId() != pc.getObjectId()) {
					for (PcShop s : shpoList.getShopList().values()) {
						if (s.getItem() == null)
							continue;

						if (s.getItem().getName().contains(itemName) && (!isEn || (isEn && s.getInvItemEn() == en)) && (!isBless || (isBless && s.getInvItemBress() == bless))) {
							marketPrice mp = new marketPrice();
							StringBuffer sb = new StringBuffer();

							sb.append(String.format("%d. %s", index++, s.getInvItemBress() == 0 ? "(축) " : s.getInvItemBress() == 1 ? "" : "(저주) "));

							if (s.getInvItemEn() > 0)
								sb.append(String.format("+%d ", s.getInvItemEn()));

							sb.append(String.format("%s", s.getItem().getName()));

							if (s.getInvItemCount() > 1)
								sb.append(String.format("(%d)", s.getInvItemCount()));
							
							if(s.getAdenType().equalsIgnoreCase("달러")){
								sb.append(String.format(" [판매 금액]: %s 달러", Util.changePrice(s.getPrice())));
							}else{
								sb.append(String.format(" [판매 금액]: %s 아데나", Util.changePrice(s.getPrice())));
							}
							list.add(sb.toString());

							mp.setShopNpc(shpoList);
							mp.setX(shpoList.getX());
							mp.setY(shpoList.getY());
							mp.setMap(shpoList.getMap());
							mp.setObjId(shpoList.getObjectId());
							pc.marketPrice.add(mp);
						}
					}
				} else {
					continue;
				}
			}

			if (list.size() < 2 || pc.marketPrice.size() < 1) {
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), marketPriceNPC, "marketprice1", null, list));
				return;
			} else {
				int count = 60 - (list.size() - 1);
				for (int i = 0; i < count; i++)
					list.add(" ");				
				// 시세 검색 결과 html 패킷 보냄.
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), marketPriceNPC, "marketprice", null, list));
			}
		} catch (Exception e) {
			ChattingController.toChatting(pc, "\\fR.상점 시세 아이템", Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(pc, "\\fR.상점 시세 아이템 인첸(0은 모두)", Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(pc, "\\fR.상점 시세 아이템 인첸(0은 모두) 축여부(축,저주,일반)", Lineage.CHATTING_MODE_MESSAGE);
		}
	}
	
	static private void shopList(PcInstance pc, StringTokenizer st, PcShopInstance pc_shop) {
		if (pc_shop != null && pc_shop.getShopList() != null) {		
			if (pc_shop.getShopList().size() < 1) {
				ChattingController.toChatting(pc, "\\fR판매중인 아이템이 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			List<String> shopList = new ArrayList<String>();
			shopList.clear();

			int idx = 1;

			for (PcShop s : pc_shop.getShopList().values()) {
				shopList.add(String.format("%d. %s", idx++, Util.getItemNameToString(s.getItem().getName(), s.getInvItemBress(), s.getInvItemEn(), s.getInvItemCount())));
				shopList.add(String.format("가격: %s %s", Util.changePrice(s.getPrice()), s.getAdenType()));
			}

			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), pc, "pcShopList", null, shopList));
		}
	}

	/**
	 * PcInventory에서 아이템 사용요청 처리 구간 에서 호출해서 사용함. : 아이템 사용을 하기전에 상점판매목록에 갱신해야 되는지 확인함. : 성공 여부 리턴하여 그에따라 인벤토리아이템 처리요청구간을 수행함.
	 * 
	 * @param pc
	 * @param item
	 * @return
	 */
	static public boolean isShopToAppend(PcInstance pc, ItemInstance item, long count) {
		pc.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x09));
		// 초기화 안된거 무시.
		PcShopInstance pc_shop = getShop(pc.getObjectId());
		if (pc_shop == null)
			return false;

		// 더이상 등록할 공간이 없는건 일반 아이템 사용하듯 처리.
		if (pc_shop.getItem(0L) == null)
			return false;

		// 착용 한거 무시.
		if (item.isEquipped()) {
			pc_shop.removeItem(0L);
			ChattingController.toChatting(pc, "\\fR사용중인 아이템은 등록할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(pc, "\\fR판매등록이 취소되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return true;
		}

		// 거래 안되는 아이템은 무시.
		if (!item.getItem().isTrade() || item.getBless() < 0) {
			pc_shop.removeItem(0L);
			ChattingController.toChatting(pc, "\\fR거래가 불가능한 아이템 입니다.", Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(pc, "\\fR판매등록이 취소되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return true;
		}

		// 아데나 등록 못하도록 처리
		if (item.getItem().getNameIdNumber() == 4) {
			pc_shop.removeItem(0L);
			ChattingController.toChatting(pc, "\\fR아데나는 판매할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(pc, "\\fR판매등록이 취소되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return true;
		}
	
		PcShop s = pc_shop.getItem(0L);
		pc_shop.removeItem(0L);
		
		if (count < s.getInvItemCount()) {
			ChattingController.toChatting(pc, "\\fR판매등록할 아이템 수량이 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(pc, "\\fR판매등록이 취소되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return true;
		}
		
		s.setItem(item.getItem());
		s.setInvItemObjectId(item.getItem().isPiles() ? ServerDatabase.nextItemObjId() : item.getObjectId());
		s.setInvItemEn(item.getEnLevel());
		s.setInvItemBress(item.getBless());
		s.setInvItemDefinite(true);
		pc_shop.appendItem(s.getInvItemObjectId(), s);
		insertItem(pc, s);
		
		String itemName = CharacterMarbleDatabase.getItemName(item);
		if (itemName != null) {
			ChattingController.toChatting(pc, String.format("개인상점: '%s' %s(%,d) 등록", itemName, s.getAdenType(), s.getPrice()), Lineage.CHATTING_MODE_MESSAGE);
		} else {
			if (count > 1)
				ChattingController.toChatting(pc, String.format("개인상점: '%s(%,d)' 1개당 %s(%,d) 등록", item.getItem().getName(), s.getInvItemCount(), s.getAdenType(), s.getPrice()), Lineage.CHATTING_MODE_MESSAGE);
			else {
				if (item.getItem().getType1().equalsIgnoreCase("weapon") || item.getItem().getType1().equalsIgnoreCase("armor"))
					ChattingController.toChatting(pc, String.format("개인상점: '+%d%s' %s(%,d) 등록", item.getEnLevel(), item.getItem().getName(), s.getAdenType(), s.getPrice()),
							Lineage.CHATTING_MODE_MESSAGE);
				else
					ChattingController.toChatting(pc, String.format("개인상점: '%s' %s(%,d) 등록", item.getItem().getName(), s.getAdenType(), s.getPrice()), Lineage.CHATTING_MODE_MESSAGE);
			}
		}
		
		pc.getInventory().count(item, item.getCount() - s.getInvItemCount(), true);
		return true;
	}
	
	/**
	 * 캐릭터 삭제
	 * 2019-06-25
	 * by connector12@nate.com
	 */
	static public void removeCharacter(Connection con, long objId) {
		synchronized (shop_list) {
			PcShopInstance pc_shop = shop_list.get(objId);
			
			if (pc_shop != null) {
				pc_shop.clearShopList();
				pc_shop.close();
				shop_list.remove(objId);
			}
		}
		
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("DELETE FROM pc_shop_robot WHERE pc_objId=?");
			st.setLong(1, objId);
			st.executeUpdate();
			st.close();
			
			st = con.prepareStatement("DELETE FROM pc_shop WHERE pc_objId=?");
			st.setLong(1, objId);
			st.executeUpdate();
			st.close();
		} catch (Exception e) {
			lineage.share.System.printf("%s : removeCharacter(Connection con, long objId)\r\n", PcMarketController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st);
		}
	}
	
	/**
	 * 캐릭명 변경 처리
	 * 2019-07-29
	 * by connector12@nate.com
	 */
	static public void changeName(long objId, String name) {
		if (name != null) {
			PcShopInstance ps = getShop(objId);
			
			if (ps != null) {
				ps.pc_name = name;
				ps.setName(name + "의 상점");
				ps.toSender(S_ObjectName.clone(BasePacketPooling.getPool(S_ObjectName.class), ps), true);
			}
		}
	}
	
	static public Map<Long, PcShopInstance> getShopList() {
		synchronized (shop_list) {
			return shop_list;
		}
	}
	
	static public PcShopInstance getShop(long key) {
		synchronized (shop_list) {
			return shop_list.get(key);
		}
	}
	
	static public void removeShop(long key) {
		synchronized (shop_list) {
			if (shop_list.containsKey(key))
				shop_list.remove(key);
		}
	}
	
	static public void appendShop(long key, PcShopInstance pi) {
		if (pi != null) {
			synchronized (shop_list) {
				shop_list.put(key, pi);
			}
		}
	}
	
	/**
	 * 아이템 추가시 DB 저장.
	 * 2019-08-17
	 * by connector12@nate.com
	 */
	static public void insertItem(PcInstance pc, PcShop s) {
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("INSERT INTO pc_shop SET pc_objId=?, pc_name=?, item_objId=?, item_name=?, bless=?, en_level=?, count=?, aden_type=?, price=?");
			st.setLong(1, pc.getObjectId());
			st.setString(2, pc.getName());
			st.setLong(3, s.getInvItemObjectId());
			st.setString(4, s.getItem().getName());
			st.setInt(5, s.getInvItemBress());
			st.setInt(6, s.getInvItemEn());
			st.setLong(7, s.getInvItemCount());
			st.setString(8, s.getAdenType());
			st.setLong(9, s.getPrice());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : insertItem(PcInstance pc, PcShop s)\r\n", PcMarketController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 아이템 정보 변경시 DB 저장.
	 * 2019-08-17
	 * by connector12@nate.com
	 */
	static public void updateItem(PcInstance pc, PcShop s) {
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE pc_shop SET pc_name=?, item_name=?, bless=?, en_level=?, count=?, aden_type=?, price=? WHERE item_objId=?");
			st.setString(1, s.getPc().pc_name == null ? "" : s.getPc().pc_name);
			st.setString(2, s.getItem().getName());
			st.setInt(3, s.getInvItemBress());
			st.setInt(4, s.getInvItemEn());
			st.setLong(5, s.getInvItemCount());
			st.setString(6, s.getAdenType());
			st.setLong(7, s.getPrice());
			st.setLong(8, s.getInvItemObjectId());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateItem(PcInstance pc, PcShop s)\r\n", PcMarketController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 아이템 삭제시 DB 제거.
	 * 2019-08-17
	 * by connector12@nate.com
	 */
	static public void deleteItem(PcShop s) {
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("DELETE FROM pc_shop WHERE item_objId=?");
			st.setLong(1, s.getInvItemObjectId());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : deleteItem(PcShop s)\r\n", PcMarketController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 상점 시작시 상점 로봇 DB 저장.
	 * 2019-08-17
	 * by connector12@nate.com
	 */
	static public void insertShopRobot(PcInstance pc, PcShopInstance pc_shop) {
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("INSERT INTO pc_shop_robot SET pc_objId=?, pc_name=?, loc_x=?, loc_y=?, loc_map=?, class_type=?, class_sex=?, heading=?, ment=?");
			st.setLong(1, pc_shop.getPc_objectId());
			st.setString(2, pc_shop.pc_name);
			st.setInt(3, pc.getX());
			st.setInt(4, pc.getY());
			st.setInt(5, pc.getMap());
			st.setInt(6, pc_shop.classType);
			st.setInt(7, pc_shop.classSex);
			st.setInt(8, pc.getHeading());
			st.setString(9, pc_shop.shop_comment == null ? "" : pc_shop.shop_comment);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : insertShopRobot(PcShopInstance pc_shop)\r\n", PcMarketController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 상점 로봇 업데이트.
	 * 2019-08-17
	 * by connector12@nate.com
	 */
	static public void updateShopRobot(PcShopInstance pc_shop) {
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE pc_shop_robot SET pc_name=?, loc_x=?, loc_y=?, loc_map=?, class_type=?, class_sex=?, heading=?, ment=? WHERE pc_objId=?");
			st.setString(1, pc_shop.pc_name);
			st.setInt(2, pc_shop.getX());
			st.setInt(3, pc_shop.getY());
			st.setInt(4, pc_shop.getMap());
			st.setInt(5, pc_shop.classType);
			st.setInt(6, pc_shop.classSex);
			st.setInt(7, pc_shop.getHeading());
			st.setString(8, pc_shop.shop_comment == null ? "" : pc_shop.shop_comment);
			st.setLong(9, pc_shop.getPc_objectId());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateShopRobot(PcShopInstance pc_shop)\r\n", PcMarketController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 상점 종료시 상점 로봇 DB 제거.
	 * 2019-08-17
	 * by connector12@nate.com
	 */
	static public void deleteShopRobot(PcInstance pc) {
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("DELETE FROM pc_shop_robot WHERE pc_objId=?");
			st.setLong(1, pc.getObjectId());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : deleteShopRobot(PcInstance pc)\r\n", PcMarketController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	static public void deleteShopRobot(long objId) {
		PreparedStatement st = null;
		Connection con = null;
		
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("DELETE FROM pc_shop_robot WHERE pc_objId=?");
			st.setLong(1, objId);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : deleteShopRobot(PcInstance pc)\r\n", PcMarketController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	static public void toTimer(long time) {
		// 개인상점.
		for(PcShopInstance psi : getShopList().values())
			psi.toTimer(time);
	}
}
