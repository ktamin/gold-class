package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI.ToastType;
import lineage.bean.database.Item;
import lineage.bean.database.ItemDropMessage;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ItemDropMessageDatabase {
	static private List<ItemDropMessage> list;
	
	static public void init(Connection con) {
		TimeLine.start("ItemDropMessageDatabase..");

		list = new ArrayList<ItemDropMessage>();
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = con.prepareStatement("SELECT * FROM item_drop_msg");
			rs = st.executeQuery();

			while (rs.next()) {
				ItemDropMessage idm = new ItemDropMessage();
				idm.setItem(rs.getString("아이템"));
				idm.set획득시알림여부(rs.getInt("아이템획득시_알림여부") == 1);
				idm.setEn(rs.getInt("인첸트"));
				list.add(idm);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", ItemDropMessageDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}

		TimeLine.end();	
	}
	
	static public void reload() {
		TimeLine.start("ItemDropMessageDatabase..");

		list = new ArrayList<ItemDropMessage>();
		PreparedStatement st = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			list.clear();
			
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM item_drop_msg");
			rs = st.executeQuery();

			while (rs.next()) {
				ItemDropMessage idm = new ItemDropMessage();
				idm.setItem(rs.getString("아이템"));
				idm.set획득시알림여부(rs.getInt("아이템획득시_알림여부") == 1);
				idm.setEn(rs.getInt("인첸트"));
				list.add(idm);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : reload()\r\n", ItemDropMessageDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}

		TimeLine.end();	
	}
	
	static public List<ItemDropMessage> getList() {
		return new ArrayList<ItemDropMessage>(list);
	}
	
	static public boolean find(ItemInstance item, boolean is) {
		if (item != null && item.getItem() != null) {
			for (ItemDropMessage idm : getList()) {
				if (is) {
					if (idm.getItem().equalsIgnoreCase(item.getItem().getName()) && item.getEnLevel() >= idm.getEn()) {
						return true;
					}
				} else {
					if (idm.is획득시알림여부() && idm.getItem().equalsIgnoreCase(item.getItem().getName())) {
						return true;
					}
				}
			}
		}

		return false;
	}
	
	static public boolean find(Item item, int en, boolean is) {
		if (item != null) {
			for (ItemDropMessage idm : getList()) {
				if (is) {
					if (idm.getItem().equalsIgnoreCase(item.getName()) && en >= idm.getEn()) {
						return true;
					}
				} else {
					if (idm.is획득시알림여부() && idm.getItem().equalsIgnoreCase(item.getName())) {
						return true;
					}
				}
			}
		}

		return false;
	}
	
	static public boolean find(String item, int en, boolean is) {
		if (item != null) {
			for (ItemDropMessage idm : getList()) {
				if (is) {
					if (idm.getItem().equalsIgnoreCase(item) && en <= idm.getEn()) {
						return true;
					}
				} else {
					if (idm.is획득시알림여부() && idm.getItem().equalsIgnoreCase(item)) {
						return true;
					}
				}
			}
		}

		return false;
	}
	
	/**
	 * 특정 아이템 획득시 전체 메세지 여부.
	 * 몬스터 드랍, 상자 획득
	 * 2020-11-29
	 * by connector12@nate.com
	 */
	static public void sendMessage(object o, String item1, String item2) {
	    if (!(o instanceof PcInstance) || item1 == null || item2 == null)
	        return;

	    PcInstance pc = (PcInstance) o;
	    String local = Util.getMapName((Character) o);

	    if (find(item1, 0, false)) {
	        if (Lineage.is_item_drop_msg_name) {
	            World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, Lineage.CHATTING_MODE_MESSAGE,
	                String.format("\\fR어느 아덴 용사가 \\fU%s \\fR에서", local)));
	            World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, Lineage.CHATTING_MODE_MESSAGE,
	                String.format("\\fY%s \\fR을(를) 획득하였습니다.", item1)));
	        } else {
	            World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, Lineage.CHATTING_MODE_MESSAGE,
	                String.format("\\fT%s \\fR님이 \\fU%s \\fR에서", pc.getName(), local)));
	            World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, Lineage.CHATTING_MODE_MESSAGE,
	                String.format("\\fY%s \\fR을(를) 획득하였습니다.", item1)));
	        }

	        // ✅ 전체 유저에게 토스트 메시지 전송
	        for (PcInstance onlinePc : World.getPcList()) {
	            SC_TOAST_NOTI.newInstance()
	                .setMessage(String.format("\\g1* 아이템 획득 [ %s ] *", item1))
	                .setMessage2(String.format("\\fH%s에서 [%s]을(를) 획득하였습니다.", local, item1))
	                .setToastType(ToastType.HeavyText)
	                .send(onlinePc);
	        }
	    }
	}

	
	/**
	 * 특정 아이템 획득시 전체 메세지 여부.
	 * 2020-11-29
	 * by connector12@nate.com
	 */
	static public void sendMessageMagicDoll(object o, String item) {
		if (Lineage.is_item_drop_msg_doll && o instanceof PcInstance && item != null) {
			PcInstance pc = (PcInstance) o;
			String itemName = item;
			String name = pc.getName();

			if (find(itemName, 0, false)) {
				String toastLine1 = String.format("\\g1* 인형 합성 [ %s ] *", itemName);
				String toastLine2 = Lineage.is_item_drop_msg_name
					? String.format("\\fH아덴의 어느 용사가 인형 합성으로 [%s]을(를) 획득하였습니다.", itemName)
					: String.format("\\fH%s님이 인형 합성으로 [%s]을(를) 획득하였습니다.", name, itemName);

				// 전체 채팅 메시지
				if (Lineage.is_item_drop_msg_name) {
					World.toSender(S_ObjectChatting.clone(
						BasePacketPooling.getPool(S_ObjectChatting.class),
						null,
						Lineage.CHATTING_MODE_MESSAGE,
						"\\fR어느 아덴 용사가 인형 합성으로"
					));
					World.toSender(S_ObjectChatting.clone(
						BasePacketPooling.getPool(S_ObjectChatting.class),
						null,
						Lineage.CHATTING_MODE_MESSAGE,
						String.format("\\fY%s \\fR을(를) 획득하였습니다.", itemName)
					));
				} else {
					World.toSender(S_ObjectChatting.clone(
						BasePacketPooling.getPool(S_ObjectChatting.class),
						null,
						Lineage.CHATTING_MODE_MESSAGE,
						String.format("\\fT%s \\fR님이 인형 합성으로", name)
					));
					World.toSender(S_ObjectChatting.clone(
						BasePacketPooling.getPool(S_ObjectChatting.class),
						null,
						Lineage.CHATTING_MODE_MESSAGE,
						String.format("\\fY%s \\fR을(를) 획득하였습니다.", itemName)
					));
				}

				// 전체 유저에게 토스트 알림 전송
				for (PcInstance online : World.getPcList()) {
					SC_TOAST_NOTI.newInstance()
						.setMessage(toastLine1)
						.setMessage2(toastLine2)
						.setToastType(ToastType.HeavyText)
						.send(online);
				}
			}
		}
	}

	
	/**
	 * 특정 아이템 획득시 전체 메세지 여부.
	 * 2020-11-29
	 * by connector12@nate.com
	 */
	static public void sendMessageMagicDoll2(object o, String item) {
		if (Lineage.is_item_drop_msg_doll && o != null && o instanceof PcInstance && item != null) {
			if (Lineage.is_item_drop_msg_name) {
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, Lineage.CHATTING_MODE_MESSAGE, "\\fR어느 아덴 용사가 인형 진화에 성공하여"));
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, Lineage.CHATTING_MODE_MESSAGE, String.format("\\fY%s \\fR을(를) 획득하였습니다.", item)));
			} else {
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, Lineage.CHATTING_MODE_MESSAGE, String.format("\\fT%s \\fR님이 인형 진화에 성공하여", o.getName())));
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, Lineage.CHATTING_MODE_MESSAGE, String.format("\\fY%s \\fR을(를) 획득하였습니다.", item)));
			}
		}
	}
	
	/**
	 * 특정 아이템 획득시 전체 메세지 여부.
	 * 2020-11-29
	 * by connector12@nate.com
	 */
	static public void sendMessageLife(object o, String item) {
		if (Lineage.is_item_drop_msg_life && o instanceof PcInstance && item != null) {
			PcInstance pc = (PcInstance) o;
			String name = pc.getName();
			String itemName = item;

			String toastLine1 = String.format("\\g1* 생명 부여 [ %s ] *", itemName);
			String toastLine2 = Lineage.is_item_drop_msg_name
				? String.format("\\fH아덴의 어느 용사가 [%s]에 생명을 부여하였습니다.", itemName)
				: String.format("\\fH%s님이 [%s]에 생명을 부여하였습니다.", name, itemName);

			// 전체 채팅 메시지
			if (Lineage.is_item_drop_msg_name) {
				World.toSender(S_ObjectChatting.clone(
					BasePacketPooling.getPool(S_ObjectChatting.class),
					null,
					Lineage.CHATTING_MODE_MESSAGE,
					String.format("\\fR어느 아덴 용사가 \\fY%s \\fR에", itemName)
				));
				World.toSender(S_ObjectChatting.clone(
					BasePacketPooling.getPool(S_ObjectChatting.class),
					null,
					Lineage.CHATTING_MODE_MESSAGE,
					"\\fR생명을 부여했습니다."
				));
			} else {
				World.toSender(S_ObjectChatting.clone(
					BasePacketPooling.getPool(S_ObjectChatting.class),
					null,
					Lineage.CHATTING_MODE_MESSAGE,
					String.format("\\fT%s \\fR님이 \\fY%s \\fR에", name, itemName)
				));
				World.toSender(S_ObjectChatting.clone(
					BasePacketPooling.getPool(S_ObjectChatting.class),
					null,
					Lineage.CHATTING_MODE_MESSAGE,
					"\\fR생명을 부여했습니다."
				));
			}

			// 전체 토스트 알림
			for (PcInstance online : World.getPcList()) {
				SC_TOAST_NOTI.newInstance()
					.setMessage(toastLine1)
					.setMessage2(toastLine2)
					.setToastType(ToastType.HeavyText)
					.send(online);
			}
		}
	}

	
	/**
	 * 특정 아이템 획득시 전체 메세지 여부.
	 * 2020-11-29
	 * by connector12@nate.com
	 */
	static public void sendMessageEn(object o, ItemInstance item, boolean is) {
		if (Lineage.is_item_drop_msg_en && o instanceof PcInstance && item != null && item.getItem() != null) {
			if (find(item, true)) {
				PcInstance pc = (PcInstance) o;
				String name = pc.getName();
				String itemName = item.getItem().getName();
				int en = item.getEnLevel();

				String toastLine1 = String.format("\\g1* 인첸트 [%s +%d] *", itemName, en);
				String toastLine2;

				if (is) {
					// 성공
					toastLine2 = Lineage.is_item_drop_msg_name
						? String.format("\\fH아덴의 어느 용사가 +%d [%s] 인첸트에 성공하였습니다.", en, itemName)
						: String.format("\\fH%s님이 +%d [%s] 인첸트에 성공하였습니다.", name, en, itemName);

					// 채팅
					if (Lineage.is_item_drop_msg_name) {
						World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null,
							Lineage.CHATTING_MODE_MESSAGE, String.format("\\fR어느 아덴 용사가 \\fY+%d %s", en, itemName)));
						World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null,
							Lineage.CHATTING_MODE_MESSAGE, "\\fR인첸트에 성공했습니다."));
					} else {
						World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null,
							Lineage.CHATTING_MODE_MESSAGE, String.format("\\fT%s \\fR님이 \\fY+%d %s", name, en, itemName)));
						World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null,
							Lineage.CHATTING_MODE_MESSAGE, "\\fR인첸트에 성공했습니다."));
					}
				} else {
					// 실패
					toastLine2 = Lineage.is_item_drop_msg_name
						? String.format("\\fH아덴의 어느 용사가 +%d [%s] 인첸트에 실패하였습니다.", en, itemName)
						: String.format("\\fH%s님이 +%d [%s] 인첸트에 실패하였습니다.", name, en, itemName);

					// 채팅
					if (Lineage.is_item_drop_msg_name) {
						World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null,
							Lineage.CHATTING_MODE_MESSAGE, String.format("\\fR어느 아덴 용사가 \\fY+%d %s", en, itemName)));
						World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null,
							Lineage.CHATTING_MODE_MESSAGE, "\\fR인첸트에 실패하였습니다."));
					} else {
						World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null,
							Lineage.CHATTING_MODE_MESSAGE, String.format("\\fT%s \\fR님이 \\fY+%d %s", name, en, itemName)));
						World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null,
							Lineage.CHATTING_MODE_MESSAGE, "\\fR인첸트에 실패하였습니다."));
					}
				}

				// 전체 토스트 발송
				for (PcInstance online : World.getPcList()) {
					SC_TOAST_NOTI.newInstance()
						.setMessage(toastLine1)
						.setMessage2(toastLine2)
						.setToastType(ToastType.HeavyText)
						.send(online);
				}
			}
		}
	}

	
	/**
	 * 특정 아이템 획득시 전체 메세지 여부.
	 * 2020-11-29
	 * by connector12@nate.com
	 */
	static public void sendMessageCreateItem(object o, Item item) {
		if (Lineage.is_item_drop_msg_create && o != null && o instanceof PcInstance && item != null) {
			if (find(item.getName(), 0, false)) {
				String itemName = item.getName();
				String makerName = o.getName();

				String toastLine1 = String.format("\\g1* 아이템 제작 [ %s ] *", itemName);
				String toastLine2;

				if (Lineage.is_item_drop_msg_name) {
					toastLine2 = String.format("\\fH어느 아덴 용사가 [%s] 제작에 성공하였습니다.", itemName);
					World.toSender(S_ObjectChatting.clone(
						BasePacketPooling.getPool(S_ObjectChatting.class), null,
						Lineage.CHATTING_MODE_MESSAGE,
						String.format("\\fR어느 아덴 용사가 \\fY%s \\fR을(를)", itemName)
					));
				} else {
					toastLine2 = String.format("\\fH%s님이 [%s] 제작에 성공하였습니다.", makerName, itemName);
					World.toSender(S_ObjectChatting.clone(
						BasePacketPooling.getPool(S_ObjectChatting.class), null,
						Lineage.CHATTING_MODE_MESSAGE,
						String.format("\\fT%s \\fR님이 \\fY%s \\fR을(를)", makerName, itemName)
					));
				}

				World.toSender(S_ObjectChatting.clone(
					BasePacketPooling.getPool(S_ObjectChatting.class), null,
					Lineage.CHATTING_MODE_MESSAGE, "\\fR제작하였습니다."
				));

				// 전체 유저에게 토스트 메시지 전송
				for (PcInstance pc : World.getPcList()) {
					SC_TOAST_NOTI.newInstance()
						.setMessage(toastLine1)
						.setMessage2(toastLine2)
						.setToastType(ToastType.HeavyText)
						.send(pc);
				}
			}
		}
	}
}
