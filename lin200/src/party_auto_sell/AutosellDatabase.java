package party_auto_sell;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lineage.database.CharactersDatabase;
import lineage.database.DatabaseConnection;
import lineage.database.WarehouseDatabase;
import lineage.share.TimeLine;
import lineage.world.object.instance.PcInstance;

public class AutosellDatabase {	

	// ★ 핵심 최적화: DB 대신 리스트를 들고 있을 메모리(캐시) 주머니!
	private static final Map<Long, List<AutosellItem>> cache = new ConcurrentHashMap<Long, List<AutosellItem>>();

	static public void init() {
		TimeLine.start("AutosellDatabase..");
		Connection con = null;
		PreparedStatement st = null;		
		ResultSet rs = null;
		try {	
			cache.clear(); // 초기화
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters_autosell");
			rs = st.executeQuery();

			while (rs.next()) {
				long ownerObjId = rs.getLong("char_obj_id");
				
				AutosellItem psItem = new AutosellItem();
				psItem.setchaobjid(ownerObjId);
				psItem.setCharName(rs.getString("char_name"));
				psItem.setItem_id(rs.getInt("item_id"));
				psItem.setItemobjid(rs.getInt("item_obj_id"));		
				psItem.setName(rs.getString("item_name"));
				psItem.setBless(rs.getInt("bless"));				
				psItem.setEnLevel(rs.getInt("enchant_level"));				
				psItem.setInvGfx(rs.getInt("gfxid"));	
				
				// 메모리에 저장!
				if (!cache.containsKey(ownerObjId)) {
					cache.put(ownerObjId, new ArrayList<AutosellItem>());
				}
				cache.get(ownerObjId).add(psItem);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : AutosellDatabase.init()\r\n", CharactersDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		TimeLine.end();
	}	
	
	static public void UpdateautoSell(PcInstance pc, AutosellItem sellitem) {
		// 1. 메모리에 먼저 추가 (즉시 반영)
		long objId = pc.getObjectId();
		if (!cache.containsKey(objId)) {
			cache.put(objId, new ArrayList<AutosellItem>());
		}
		cache.get(objId).add(sellitem);

		// 2. DB에 비동기/동기 저장 (REPLACE INTO로 중복 에러 원천 차단)
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("REPLACE INTO characters_autosell SET char_obj_id=?, char_name=?, item_obj_id=?, item_id=?, item_name=?, bless=?, enchant_level=?, gfxid=?");
			
			st.setLong(1, sellitem.getchaobjid());
			st.setString(2, sellitem.getCharName());
			st.setLong(3, sellitem.getItemobjid());
			st.setInt(4, sellitem.getItem_id());		
			st.setString(5, sellitem.getName());
			st.setInt(6, sellitem.getBless());
			st.setInt(7, sellitem.getEnLevel());
			st.setInt(8, sellitem.getInvGfx());		
			
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : UpdateautoSell()\r\n", AutosellDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	public static void deleteautoSell(AutosellItem psItem, PcInstance pc) {
		// 1. 메모리에서 삭제
		List<AutosellItem> list = cache.get(pc.getObjectId());
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getItemobjid() == psItem.getItemobjid()) {
					list.remove(i);
					break;
				}
			}
		}

		// 2. DB에서 삭제
		Connection con = null;
		PreparedStatement st = null;
		try {
			if (psItem.getchaobjid() == pc.getObjectId()) {
				con = DatabaseConnection.getLineage();
				st = con.prepareStatement("DELETE FROM characters_autosell WHERE char_obj_id=? AND item_obj_id=?");
				st.setLong(1, psItem.getchaobjid());		
				st.setLong(2, psItem.getItemobjid());
				st.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	public static void AlldeleteautoSell(PcInstance pc) {
		// 1. 메모리에서 싹 삭제
		cache.remove(pc.getObjectId());

		// 2. DB에서 싹 삭제
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("DELETE FROM characters_autosell WHERE char_obj_id=?");
			st.setLong(1, pc.getObjectId());		
			st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	// ★ 가장 많이 호출되는 부분! 이제 DB 접속 없이 메모리에서 0.001초 만에 꺼내줍니다! ★
	public static List<AutosellItem> getList(long id) {
		List<AutosellItem> list = cache.get(id);
		// 만약 리스트가 없으면 빈 깡통을 리턴해서 에러(Null) 방지
		if (list == null) {
			return new ArrayList<AutosellItem>();
		}
		return list;
	}
}