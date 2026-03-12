package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.world.object.object;
import lineage.world.object.item.CookBook;

public class S_Cook extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, object o, CookBook cb){
		if(bp == null)
			bp = new S_Cook(o, cb);
		else
			((S_Cook)bp).toClone(o, cb);
		return bp;
	}
	
	public S_Cook(object o, CookBook cb){
		toClone(o, cb);
	}
	
	public void toClone(object o, CookBook cb){
		clear();
		
		int lv = cb.getItem().getNameIdNumber() - 4922;
		writeC(Opcodes.S_OPCODE_UNKNOWN2);
		writeC(0x34);
		writeD(o.getObjectId());
		writeC(lv>0 ? 1 : 0);	// 1단계
		writeC(lv>1 ? 1 : 0);	// 2단계
	}

}
