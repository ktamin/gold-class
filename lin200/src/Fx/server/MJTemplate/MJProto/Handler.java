package Fx.server.MJTemplate.MJProto;

import java.sql.PreparedStatement;

public interface Handler {
	public void handle(PreparedStatement pstm) throws Exception;
}
