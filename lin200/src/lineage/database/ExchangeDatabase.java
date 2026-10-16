package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.ExchangeItem;
import lineage.share.TimeLine;

public final class ExchangeDatabase {

	// 서버 구동 시 판매 중인 거래소 물품을 담아둘 메모리 리스트
	private static List<ExchangeItem> exchangeList;

	/**
	 * 서버 부팅 시 거래소에 등록된 '판매 중(is_sold=0)'인 아이템만 불러옵니다.
	 */
	static public void init(Connection con) {
		TimeLine.start("ExchangeDatabase..");
		exchangeList = new ArrayList<ExchangeItem>();

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			// is_sold가 0(판매중)인 물품만 최신 등록순으로 가져옵니다.
			st = con.prepareStatement("SELECT * FROM exchange_items WHERE is_sold = 0 ORDER BY id DESC");
			rs = st.executeQuery();
			
			while (rs.next()) {
				ExchangeItem item = new ExchangeItem();
				item.setId(rs.getInt("id"));
				item.setItemObjId(rs.getLong("item_obj_id"));
				item.setItemName(rs.getString("item_name"));
				item.setEnchant(rs.getInt("enchant"));
				item.setAttrLevel(rs.getInt("attr_level"));
				item.setBless(rs.getInt("bless"));
				item.setCount(rs.getLong("count"));
				item.setSellerName(rs.getString("seller_name"));
				item.setPrice(rs.getLong("price"));
				item.setCurrencyType(rs.getInt("currency_type"));
				item.setCategory(rs.getInt("category"));
				item.setIsSold(rs.getInt("is_sold"));
				
				exchangeList.add(item);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", ExchangeDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}

		TimeLine.end();
	}
	
	/**
	 * 메모리에 적재된 판매 중인 거래소 아이템 리스트를 반환합니다.
	 */
	static public List<ExchangeItem> getList() {
		return exchangeList;
	}
	
	/**
	 * 고유 번호(id)로 거래소에 등록된 아이템을 검색합니다.
	 */
	public static ExchangeItem findById(int id) {
		// getList() 또는 메모리에 저장된 리스트 변수명(예: list)을 순회합니다.
		for (ExchangeItem item : getList()) { 
			if (item.getId() == id) {
				return item; // 일치하는 번호를 찾으면 그 아이템 정보를 반환!
			}
		}
		return null; // 못 찾았으면 null 반환
	}

	/**
	 * 새로운 거래소 아이템을 DB에 등록합니다.
	 */
	public static void insertItem(ExchangeItem item) {
		java.sql.Connection con = null;
		java.sql.PreparedStatement pstm = null;
		try {
			con = lineage.database.DatabaseConnection.getLineage();
			pstm = con.prepareStatement(
				"INSERT INTO exchange_items (id, seller_name, seller_objectid, item_name, count, price, currency_type, category, enchant, bless, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)"
			);
			pstm.setInt(1, item.getId());
			pstm.setString(2, item.getSellerName());
			pstm.setLong(3, item.getSellerObjectId());
			pstm.setString(4, item.getItemName());
			pstm.setLong(5, item.getCount());
			pstm.setLong(6, item.getPrice());
			pstm.setInt(7, item.getCurrencyType());
			pstm.setInt(8, item.getCategory());
			pstm.setInt(9, item.getEnchant());
			pstm.setInt(10, item.getBless());
			pstm.execute();
		} catch (Exception e) {
			lineage.share.System.println("ExchangeDatabase insertItem 에러: " + e);
		} finally {
			lineage.database.DatabaseConnection.close(con, pstm);
		}
	}
	
	/**
	 * 아이템이 판매되거나 정산되었을 때 상태(is_sold)를 업데이트합니다.
	 * (상태 - 0: 판매중, 1: 판매완료(정산대기), 2: 정산완료/취소)
	 */
	static public void updateItemState(int id, int isSold) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("UPDATE exchange_items SET is_sold=? WHERE id=?");
			st.setInt(1, isSold);
			st.setInt(2, id);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateItemState(int id, int isSold)\r\n", ExchangeDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}

	/**
	 * 거래소 이용 내역(등록, 구매, 취소 등)을 로그 DB에 기록합니다.
	 */
	static public void insertLog(String actionType, String itemName, String sellerName, String buyerName, long price, int currencyType) {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement(
					"INSERT INTO exchange_logs (action_type, item_name, seller_name, buyer_name, price, currency_type, log_date) " +
					"VALUES (?, ?, ?, ?, ?, ?, NOW())"
			);
			st.setString(1, actionType);
			st.setString(2, itemName);
			st.setString(3, sellerName);
			st.setString(4, buyerName != null ? buyerName : "");
			st.setLong(5, price);
			st.setInt(6, currencyType);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : insertLog(...)\r\n", ExchangeDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	/**
	 * 거래소 아이템의 상태(status)를 DB에 업데이트합니다.
	 */
	public static void updateItemStatus(ExchangeItem item) {
		java.sql.Connection con = null;
		java.sql.PreparedStatement pstm = null;
		try {
			con = lineage.database.DatabaseConnection.getLineage();
			pstm = con.prepareStatement("UPDATE exchange_items SET status=? WHERE id=?");
			pstm.setInt(1, item.getStatus());
			pstm.setInt(2, item.getId());
			pstm.execute();
		} catch (Exception e) {
			lineage.share.System.println("ExchangeDatabase updateItemStatus 에러: " + e);
		} finally {
			lineage.database.DatabaseConnection.close(con, pstm);
		}
	}
	
	/**
	 * 정산이 완료된 거래소 아이템을 DB에서 영구 삭제합니다.
	 */
	public static void deleteItem(ExchangeItem item) {
		java.sql.Connection con = null;
		java.sql.PreparedStatement pstm = null;
		try {
			con = lineage.database.DatabaseConnection.getLineage();
			pstm = con.prepareStatement("DELETE FROM exchange_items WHERE id=?");
			pstm.setInt(1, item.getId());
			pstm.execute();
		} catch (Exception e) {
			lineage.share.System.println("ExchangeDatabase deleteItem 에러: " + e);
		} finally {
			lineage.database.DatabaseConnection.close(con, pstm);
		}
	}
}