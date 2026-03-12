package Fx.server.MJTemplate.MJProto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage.database.DatabaseConnection;

public class Selector implements Executor{
	public static void exec(String query, SelectorHandler handler){
		new Selector().execute(query, handler);
	}
	
	@Override
	public int execute(String query, Handler handler){
		if(!(handler instanceof SelectorHandler))
			throw new IllegalArgumentException("handler is not SelectorHandler...!");
		
		SelectorHandler sHandler = (SelectorHandler)handler;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try{
			con = DatabaseConnection.getLineage();
			pstm = con.prepareStatement(query);
			sHandler.handle(pstm);
			rs = pstm.executeQuery();
			sHandler.result(rs);
			return 1;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DatabaseConnection.close(con, pstm, rs);
		}
		return 0;
	}
}
