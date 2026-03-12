package lineage.network.packet.server;

import lineage.bean.lineage.Useshop;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.share.Lineage;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class S_ObjectAction extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, object o){
		if(bp == null)
			bp = new S_ObjectAction(o);
		else
			((S_ObjectAction)bp).clone(o);
		return bp;
	}

	static synchronized public BasePacket clone(BasePacket bp, object o, int action){
		if(bp == null)
			bp = new S_ObjectAction(o, action);
		else
			((S_ObjectAction)bp).clone(o, action);
		return bp;
	}

	static synchronized public BasePacket clone(BasePacket bp, object o, Useshop us){
		if(bp == null)
			bp = new S_ObjectAction(o, us);
		else
			((S_ObjectAction)bp).clone(o, us);
		return bp;
	}
	
	public S_ObjectAction(object o){
		clone(o);
	}
	
	public S_ObjectAction(object o, int action){
		clone(o, action);
	}
	
	public S_ObjectAction(object o, Useshop us){
		clone(o, us);
	}
	
	public void clone(object o){
		clear();
		writeC(Opcodes.S_OPCODE_DOACTION);
		writeD(o.getObjectId());
		writeC(o.getGfxMode());
	}
	
	/*public void clone(object o, int action){
		if (o instanceof PcInstance && (action == Lineage.GFX_MODE_SPELL_DIRECTION || action == Lineage.GFX_MODE_SPELL_NO_DIRECTION)) {
			PcInstance pc = (PcInstance) o;
			pc.isFrameSpeed(action);
		}
		
		clear();
		writeC(Opcodes.S_OPCODE_DOACTION);
		writeD(o.getObjectId());
		writeC(action);
	}*/
        public void clone(object o, int action) {
		
		if (o instanceof PcInstance && (action == Lineage.GFX_MODE_SPELL_DIRECTION || action == Lineage.GFX_MODE_SPELL_NO_DIRECTION)) {
			PcInstance pc = (PcInstance) o;
			// 마법 사용 모션 및 피격 모션 밀림 현상 개선.
			if (SpriteFrameDatabase.findGfxMode(o.getGfx(), action)){
				long time = lineage.share.System.currentTimeMillis();
				if(pc.ai_Time > time){
					pc.ai_Time = time + SpriteFrameDatabase.getGfxFrameTime(pc, pc.getGfx(), action) + (pc.ai_Time - time);
				}else{
					pc.ai_Time = time + SpriteFrameDatabase.getGfxFrameTime(pc, pc.getGfx(), action);
				}
			}
		}
		
		clear();
		writeC(Opcodes.S_OPCODE_DOACTION);
		writeD(o.getObjectId());
		writeC(action);
	}
	
	public void clone(object o, Useshop us){
		clear();
		writeC(Opcodes.S_OPCODE_DOACTION);
		writeD(o.getObjectId());
		writeC(o.getGfxMode());
		if(us.getMsg() != null)
			writeB(us.getMsg());
	}
}
