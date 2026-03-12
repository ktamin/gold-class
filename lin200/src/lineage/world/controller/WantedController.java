package lineage.world.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Item;
import lineage.bean.database.Wanted;
import lineage.bean.lineage.Kingdom;
import lineage.database.DatabaseConnection;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_CharacterSpMr;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.World;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

final public class WantedController {

	static private List<Wanted> list;

	static public void init(Connection con) {
		TimeLine.start("WantedController..");
		list = new ArrayList<Wanted>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM wanted");
			rs = st.executeQuery();
			while (rs.next()) {
				Wanted wanted = new Wanted();
				wanted.objId = rs.getLong("objId");
				wanted.target_name = rs.getString("name");
				wanted.target_price = rs.getLong("price");
				wanted.date = rs.getTimestamp("date").getTime();
				list.add(wanted);
			}
		} catch (Exception e) {
		} finally {
			DatabaseConnection.close(st, rs);
		}
		TimeLine.end();
	}

	static public void clear() {
		synchronized (list) {
			list.clear();
		}
		World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, Lineage.CHATTING_MODE_MESSAGE, "현상수배 목록이 초기화 되었습니다."));
	}

	static public List<Wanted> getList() {
		return new ArrayList<Wanted>(list);
	}

	static public void toTimer(long time) { }

	static public void append(object o, String target_name, long target_price) {
		if (target_name == null || target_name.length() == 0) {
			ChattingController.toChatting(o, "캐릭터명이 잘못되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		if (target_price < 0 && target_price > 3) {
			ChattingController.toChatting(o, "수배 단수 설정이 잘못되었습니다. 1,2,3단", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		long objId = 0;
		boolean isFind = false;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters WHERE LOWER(name)=?");
			st.setString(1, target_name.toLowerCase());
			rs = st.executeQuery();
			if (rs.next()) {
				isFind = true;
				objId = rs.getLong("objID");
			}
			rs.close();
			st.close();
			st = con.prepareStatement("SELECT * FROM _robot WHERE LOWER(name)=?");
			st.setString(1, target_name.toLowerCase());
			rs = st.executeQuery();
			if (rs.next()) {
				isFind = true;
				objId = rs.getLong("objId");
			}
		} catch (Exception e) {
		} finally {
			DatabaseConnection.close(con, st, rs);
		}

		if (!isFind) {
			ChattingController.toChatting(o, "캐릭터가 존재하지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if(o.getName() == null ? target_name != null : !o.getName().equals(target_name)) {
			ChattingController.toChatting(o, "자기 자신한테만 수배를 걸 수 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if (checkWantedPc(objId)) {
			ChattingController.toChatting(o, String.format("%s님은 이미 수배중 입니다.", target_name), Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if(target_price == 1)      target_price = 50000;
		else if(target_price == 2) target_price = 100000;
		else if(target_price == 3) target_price = 150000;

		if (!o.getInventory().isAden(target_price, true)) {
			ChattingController.toChatting(o, String.format("수배금액: %d아데나", target_price), Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		Wanted wanted = new Wanted();
		wanted.objId = objId;
		wanted.target_name = target_name;
		wanted.target_price = target_price;
		wanted.date = System.currentTimeMillis();

		synchronized (list) {
			list.add(wanted);
		}

		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("INSERT INTO wanted SET objId=?, name=?, price=?, date=?");
			st.setLong(1, wanted.objId);
			st.setString(2, wanted.target_name);
			st.setLong(3, wanted.target_price);
			st.setTimestamp(4, new Timestamp(wanted.date));
			st.executeUpdate();
			
			World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("%s님을 수배 합니다.", target_name)));
			ChattingController.toChatting(o, String.format("수배금액: %d아데나", target_price), Lineage.CHATTING_MODE_MESSAGE);

			PcInstance pc = World.findPc(objId);
			if (pc != null) {
				applyWantedBuff(pc, target_price);
			}
		} catch (Exception e) {
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	static public void toDead(Character cha, object o) {
		if (cha instanceof PcInstance && cha.getMap() != Lineage.teamBattleMap && !World.isCombatZone(cha.getX(), cha.getY(), cha.getMap()) && !World.isCombatZone(o.getX(), o.getY(), o.getMap())) {
			Wanted wanted = null;
			for (Wanted w : list) {
				if (w.objId == o.getObjectId()) {
					wanted = w;
					break;
				}
			}
			if (wanted == null)
				return;
			
			Kingdom kingdom = KingdomController.findKingdomLocation(o);
			if (kingdom != null && kingdom.isWar())
				return;

			if (o instanceof PcInstance) {
				removeWantedBuff((PcInstance)o, wanted.target_price);
			}

			World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("%s 현상금 지급  \\fS수배자:%s", cha.getName(), wanted.target_name)));

			synchronized (list) {
				list.remove(wanted);
			}

			Connection con = null;
			PreparedStatement st = null;
			try {
				con = DatabaseConnection.getLineage();
				st = con.prepareStatement("DELETE FROM wanted WHERE objId=?");
				st.setLong(1, wanted.objId);
				st.executeUpdate();
			} catch (Exception e) {
			} finally {
				DatabaseConnection.close(con, st);
			}

			Item i = ItemDatabase.find("아데나");
			if (i != null) {
				ItemInstance temp = cha.getInventory().find(i.getName(), i.isPiles());
				if (temp == null) {
					temp = ItemDatabase.newInstance(i);
					temp.setObjectId(ServerDatabase.nextItemObjId());
					temp.setBless(1);
					temp.setCount(wanted.target_price / 2);
					temp.setDefinite(true);
					cha.getInventory().append(temp, true);
				} else {
					cha.getInventory().count(temp, temp.getCount() + (wanted.target_price / 2), true);
				}
			}
			World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format("\\fS아데나:%d",wanted.target_price / 2)));
		}
	}

	static public void checkWanted(PcInstance pc) {
		synchronized (list) {
			if (list.size() == 0) {
				ChattingController.toChatting(pc, "수배자가 존재하지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
			for (Wanted w : list)
				ChattingController.toChatting(pc, String.format("수배자: %s", w.target_name), Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	static public void toWorldJoin(PcInstance pc) {
		PreparedStatement st = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM wanted WHERE objId=?");
			st.setLong(1, pc.getObjectId());
			rs = st.executeQuery();

			if (rs.next()) {
				long price = rs.getLong("price");
				String name = rs.getString("name");
				long date = rs.getTimestamp("date").getTime();

				boolean alreadyExists = false;
				synchronized (list) {
					for (Wanted w : list) {
						if (w.objId == pc.getObjectId()) {
							alreadyExists = true;
							break;
						}
					}
					if (!alreadyExists) {
						Wanted wanted = new Wanted();
						wanted.objId = pc.getObjectId();
						wanted.target_name = name;
						wanted.target_price = price;
						wanted.date = date;
						list.add(wanted);
					}
				}
				applyWantedBuff(pc, price);
			}
		} catch (Exception e) {
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}

	static public void toWorldOut(PcInstance pc) { }
	
	static public void changeName(String name, String newName) {
		for (Wanted w : list) {
			if (w.target_name.equalsIgnoreCase(name)) {
				w.target_name = newName;
				Connection con = null;
				PreparedStatement st = null;
				try {
					con = DatabaseConnection.getLineage();
					st = con.prepareStatement("UPDATE wanted SET name=? WHERE LOWER(name)=?");
					st.setString(1, newName);
					st.setString(2, name.toLowerCase());
					st.executeUpdate();
				} catch (Exception e) {
				} finally {
					DatabaseConnection.close(con, st);
				}
				break;
			}
		}
	}
	
	// [수정] 원본 메서드 복구: object 인자를 받는 버전
	static public boolean checkWantedPc(object o) {
		for (Wanted w : list) {
			if (w.objId == o.getObjectId())
				return true;
		}
		return false;
	}
	
	static public boolean checkWantedPc(long objId) {
		for (Wanted w : list) {
			if (w.objId == objId)
				return true;
		}
		return false;
	}
	
	// ★ 수배 단계별 버프 부여/해제 유틸 (아이콘 추가) ★
	private static void applyWantedBuff(PcInstance pc, long price) {
		if (pc == null) return;
		int iconId = 0;
		int bonus = 0;
		String rankStr = "";

		if (price == 50000) {
			iconId = 88; bonus = 1; rankStr = "1단";
		} else if (price == 100000) {
			iconId = 89; bonus = 2; rankStr = "2단";
		} else if (price == 150000) {
			iconId = 90; bonus = 3; rankStr = "3단";
		}

		if (iconId > 0) {
			pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() + bonus);
			pc.setDynamicAddDmg(pc.getDynamicAddDmg() + bonus);
			pc.setDynamicSp(pc.getDynamicSp() + bonus);
			SC_BUFFICON_NOTI.on(pc, iconId, -1, 0); 
//			ChattingController.toChatting(pc, String.format("[수배%s] 원/근 대미지 +%d, sp+%d", rankStr, bonus, bonus), Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	private static void removeWantedBuff(PcInstance pc, long price) {
		if (pc == null) return;
		int iconId = 0;
		int bonus = 0;
		String rankStr = "";

		if (price == 50000) {
			iconId = 88; bonus = 1; rankStr = "1단";
		} else if (price == 100000) {
			iconId = 89; bonus = 2; rankStr = "2단";
		} else if (price == 150000) {
			iconId = 90; bonus = 3; rankStr = "3단";
		}

		if (iconId > 0) {
			pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() - bonus);
			pc.setDynamicAddDmg(pc.getDynamicAddDmg() - bonus);
			pc.setDynamicSp(pc.getDynamicSp() - bonus);
			SC_BUFFICON_NOTI.on(pc, iconId, 0, 1); 
			ChattingController.toChatting(pc, String.format("[수배%s] 해제", rankStr), Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	public static void cancelMyWanted(PcInstance pc) {
		if (pc == null) return;
		Wanted target = null;
		synchronized (list) {
			for (Wanted w : list) {
				if (w.objId == pc.getObjectId()) {
					target = w;
					break;
				}
			}
			if (target != null) list.remove(target);
		}
		if (target == null) {
			ChattingController.toChatting(pc, "현재 본인에게 걸린 수배가 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("DELETE FROM wanted WHERE objId=?");
			st.setLong(1, target.objId);
			st.executeUpdate();
		} catch (Exception e) {
		} finally {
			DatabaseConnection.close(con, st);
		}
		removeWantedBuff(pc, target.target_price);
		ChattingController.toChatting(pc, "본인 수배가 초기화되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
	}
}