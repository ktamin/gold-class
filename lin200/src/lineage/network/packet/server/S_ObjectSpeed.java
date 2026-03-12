package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.world.object.object;

public class S_ObjectSpeed extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, object o, int type, int speed, int time){
		if(bp == null)
			bp = new S_ObjectSpeed(o, type, speed, time);
		else
			((S_ObjectSpeed)bp).clone(o, type, speed, time);
		return bp;
	}
	
	public S_ObjectSpeed(object o, int type, int speed, int time){
		clone(o, type, speed, time);
	}
	
	public void clone(object o, int type, int speed, int time){
		clear();
		switch(type){
			case 0:		// 촐기, 헤이등 1차가속
				writeC(Opcodes.S_OPCODE_SKILLHASTE);
				break;
			case 1:		// 용기, 와퍼등 2차가속+
				writeC(Opcodes.S_OPCODE_SKILLBRAVE);
				break;
		}
		writeD(o.getObjectId());
		writeC(speed);	// 2.0버전 기준	0: 노멀, 1: 2단가속, 2: 지혜물약 버프아이콘 생기면서 2단가속 해제, 3: 요정 버프관련 2차가속 적용 안됨.
		writeH(time);	// 시간
	}
}
