package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.database.TalkScroll;
import lineage.share.TimeLine;

public class TalkScrollDatabase {

	private static final List<TalkScroll> list = new ArrayList<TalkScroll>();
	private static final Map<Integer, TalkScroll> map = new HashMap<Integer, TalkScroll>();

	public static void init(Connection con) {
		TimeLine.start("TalkScrollDatabase..");
		list.clear();
		map.clear();

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = con.prepareStatement(
				"SELECT * FROM talk_scroll WHERE ts_enable=1 ORDER BY ts_group ASC, ts_uid ASC"
			);
			rs = st.executeQuery();

			while (rs.next()) {
				TalkScroll ts = new TalkScroll();

				ts.setUid(rs.getInt("ts_uid"));
				ts.setName(rs.getString("ts_name"));
				ts.setX(rs.getInt("ts_x"));
				ts.setY(rs.getInt("ts_y"));
				ts.setMap(rs.getInt("ts_map"));
				ts.setMinLevel(rs.getInt("ts_min_level"));
				ts.setClassType(rs.getString("ts_class"));
				ts.setPrice(rs.getInt("ts_price"));
				ts.setEnable(rs.getInt("ts_enable") == 1);
				ts.setGroup(rs.getString("ts_group"));

				list.add(ts);
				map.put(ts.getUid(), ts);
			}

			System.out.println("TalkScrollDatabase 로딩 완료. " + list.size() + "개");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("TalkScrollDatabase 로딩 에러.");
		} finally {
			DatabaseConnection.close(st, rs);
		}
	}

	public static List<TalkScroll> getList() {
		return list;
	}

	public static TalkScroll find(int uid) {
		return map.get(uid);
	}

	public static List<TalkScroll> getDisplaySlotList() {
		List<TalkScroll> result = new ArrayList<TalkScroll>();

		addDisplayGroup(result, "마을");
		addDisplayGroup(result, "사냥터");
		addDisplayGroup(result, "던전");

		return result;
	}

	private static void addDisplayGroup(List<TalkScroll> result, String group) {
		boolean exists = false;

		for (TalkScroll ts : list) {
			if (group.equalsIgnoreCase(ts.getGroup())) {
				exists = true;
				break;
			}
		}

		if (!exists)
			return;

		// 그룹 제목 슬롯
		TalkScroll title = new TalkScroll();
		title.setName("========== [ " + group + " ] ==========");
		title.setGroup("__TITLE__");
		result.add(title);

		
		for (TalkScroll ts : list) {
			if (group.equalsIgnoreCase(ts.getGroup())) {				
				result.add(ts);
			}
		}
	}

	public static boolean isTitleSlot(TalkScroll ts) {
		return ts != null && "__TITLE__".equalsIgnoreCase(ts.getGroup());
	}
	
	public static void reload() {
		TimeLine.start("TalkScrollDatabase 테이블 리로드 완료");
		
		list.clear();
		map.clear();
		
		PreparedStatement st = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement(
				"SELECT * FROM talk_scroll WHERE ts_enable=1 ORDER BY ts_group ASC, ts_uid ASC"
			);
			rs = st.executeQuery();

			while (rs.next()) {
				TalkScroll ts = new TalkScroll();

				ts.setUid(rs.getInt("ts_uid"));
				ts.setName(rs.getString("ts_name"));
				ts.setX(rs.getInt("ts_x"));
				ts.setY(rs.getInt("ts_y"));
				ts.setMap(rs.getInt("ts_map"));
				ts.setMinLevel(rs.getInt("ts_min_level"));
				ts.setClassType(rs.getString("ts_class"));
				ts.setPrice(rs.getInt("ts_price"));
				ts.setEnable(rs.getInt("ts_enable") == 1);
				ts.setGroup(rs.getString("ts_group"));

				list.add(ts);
				map.put(ts.getUid(), ts);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : reload()\r\n", TalkScrollDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con,st, rs);
		}
		TimeLine.end();
	}
}

