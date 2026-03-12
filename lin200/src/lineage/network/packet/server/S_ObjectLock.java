package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;

public class S_ObjectLock extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, int type) {
		if (bp == null)
			bp = new S_ObjectLock(type);
		else
			((S_ObjectLock) bp).clone(type);
		return bp;
	}
	
	static synchronized public BasePacket clone(BasePacket bp, int type, long objectId){
		if(bp == null)
			bp = new S_ObjectLock(type, objectId);
		else
			((S_ObjectLock)bp).clone(type, objectId);
		return bp;
	}

	public S_ObjectLock(int type) {
		clone(type);
	}

	public S_ObjectLock(int type, long objectId) {
		clone(type, objectId);
	}

	public void clone(int type) {
		clear();

		writeC(Opcodes.S_OPCODE_SetClientLock);
		writeC(type); 
	}
	
	public void clone(int type, long objectId){
		clone(type);
		writeD(objectId);
	}
}
