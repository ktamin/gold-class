package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.ServerBasePacket;

public class S_EXTENDED_PROTOBUF extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, byte[] buffer){
		if(bp == null)
			bp = new S_EXTENDED_PROTOBUF(buffer);
		else
			((S_EXTENDED_PROTOBUF)bp).toClone(buffer);
		return bp;
	}
	
	public S_EXTENDED_PROTOBUF(byte[] buffer){
		toClone(buffer);
	}
	
	public void toClone(byte[] buffer){
		clear();
		writeB(buffer);
	}
}
