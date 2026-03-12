package lineage.network.packet.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Fx.server.MJTemplate.MJProto.MJEProtoMessages;
import Fx.server.MJTemplate.MJProto.ProtoOutputStream;
import Fx.server.MJTemplate.MJProto.Models.SC_SELECT_CHARACTER_STATUS_NOTI;
import lineage.database.DatabaseConnection;
import lineage.network.LineageClient;
import lineage.network.LineageServer;
import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ReturnToCharacterSelect;
import lineage.share.Lineage;
import lineage.world.object.instance.PcInstance;

public class C_WorldtoOut extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_WorldtoOut(data, length);
		else
			((C_WorldtoOut)bp).clone(data, length);
		return bp;
	}
	
	public C_WorldtoOut(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc){
		// 버그 방지.
		if(pc==null || pc.isWorldDelete()) {
			return this;
		}
		
		if (pc.isAutoAttack) {
			pc.isAutoAttack = false;
			pc.getClientSetting().toggleAttackContinue().update();
		}

		LineageClient find_c = LineageServer.find(pc.getAccountUid());
		pc.toWorldOut();
		if(Lineage.server_version > 230) {
			pc.toSender(S_ReturnToCharacterSelect.clone(BasePacketPooling.getPool(S_ReturnToCharacterSelect.class)));
		}
		
		if (find_c != null) {
			AcProto(find_c);
		}
		return this;
	}
	
	public void AcProto(LineageClient c) {
		if (c.getAccountUid() > 0) {
			PreparedStatement st = null;
			ResultSet rs = null;
			Connection con = null;
			try {
				con = DatabaseConnection.getLineage();
				st = con.prepareStatement("SELECT ac FROM characters WHERE account_uid=?");
				st.setInt(1, c.getAccountUid());
				rs = st.executeQuery();
				SC_SELECT_CHARACTER_STATUS_NOTI statusNoti = SC_SELECT_CHARACTER_STATUS_NOTI.newInstance();

				while (rs.next()) {
					int ac = rs.getInt(1);
					statusNoti.addArmorClass(ac * -1);
				}

				ProtoOutputStream stream = statusNoti.writeTo(MJEProtoMessages.SC_SELECT_CHARACTER_STATUS_NOTI);
				c.sendProto(stream, false);
				stream.dispose();
			} catch (Exception e) {
				lineage.share.System.printf("%s : init(Client c) 3\r\n", C_NoticeOk.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(st, rs);
				DatabaseConnection.close(con);
			}
		}
	}

}
