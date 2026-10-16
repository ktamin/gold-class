package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage.world.object.instance.AfkInstance;
import lineage.world.controller.AfkController;

public class AfkDatabase {

	// 1. 서버가 켜질 때 DB를 읽어 로봇들을 부활시키는 메서드
	public static void load() {
		Connection con = null;
		PreparedStatement p = null;
		ResultSet r = null;
		int count = 0;
		try {
			con = DatabaseConnection.getLineage();
			p = con.prepareStatement("SELECT * FROM character_afk");
			r = p.executeQuery();
			
			while (r.next()) {
				// 💡 맨 마지막에 r.getLong("start_time") 추가됨!
				new AfkInstance(
					r.getInt("account_uid"), r.getLong("pc_object_id"), r.getString("pc_name"), 
					r.getLong("bot_object_id"), r.getInt("x"), r.getInt("y"), r.getInt("map"), 
					r.getInt("heading"), r.getInt("gfx"), r.getInt("gfx_mode"), r.getInt("is_member") == 1,
					r.getLong("start_time")
				);
				count++;
			}
		} catch (Exception e) {
			lineage.share.System.println("AfkDatabase load() 에러: " + e.getMessage());
		} finally {
			DatabaseConnection.close(con, p, r);
		}
		lineage.share.System.printf("무인잠수 봇 로딩: %d개\r\n", count);
	}

	// 2. 무인잠수 시작 시 DB에 로봇 등록
	public static void insert(AfkInstance ai) {
		Connection con = null;
		PreparedStatement p = null;
		try {
			con = DatabaseConnection.getLineage();
			// 💡 쿼리문에 start_time=? 추가됨!
			p = con.prepareStatement("INSERT INTO character_afk SET account_uid=?, pc_object_id=?, pc_name=?, bot_object_id=?, x=?, y=?, map=?, heading=?, gfx=?, gfx_mode=?, is_member=?, start_time=?");
			p.setInt(1, ai.getPc_accountUid());
			p.setLong(2, ai.getPc_objectId());
			p.setString(3, ai.getPc_name());
			p.setLong(4, ai.getObjectId());
			p.setInt(5, ai.getX());
			p.setInt(6, ai.getY());
			p.setInt(7, ai.getMap());
			p.setInt(8, ai.getHeading());
			p.setInt(9, ai.getGfx());
			p.setInt(10, ai.getGfxMode());
			p.setInt(11, ai.isMemberStatus() ? 1 : 0);
			// 💡 로봇 객체에서 시작 시간을 가져와 12번째에 세팅!
			p.setLong(12, ai.getStartTime()); 
			p.executeUpdate();
		} catch (Exception e) {
		} finally {
			DatabaseConnection.close(con, p);
		}
	}

	// 3. 주인이 접속해서 보상을 받았을 때 DB에서 로봇 삭제
	public static void delete(int accountUid) {
		Connection con = null;
		PreparedStatement p = null;
		try {
			con = DatabaseConnection.getLineage();
			p = con.prepareStatement("DELETE FROM character_afk WHERE account_uid=?");
			p.setInt(1, accountUid);
			p.executeUpdate();
		} catch (Exception e) {
		} finally {
			DatabaseConnection.close(con, p);
		}
	}
}