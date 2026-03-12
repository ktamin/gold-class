package lineage.world.object.monster;

import lineage.bean.database.Monster;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectMode;
import lineage.share.Lineage;
import lineage.world.object.instance.MonsterInstance;

public class Spartoi extends MonsterInstance {
	
	static synchronized public MonsterInstance clone(MonsterInstance mi, Monster m){
		if(mi == null)
			mi = new Spartoi();
		return MonsterInstance.clone(mi, m);
	}

	@Override
	public void readDrop(int map){
//		super.readDrop(map);
		
	}
	
	@Override
	public void toTeleport(final int x, final int y, final int map, final boolean effect){
		// 땅속에 박힌 모드로 변경.
		// standby
		setGfxMode(Lineage.GFX_MODE_OPEN);
		// 처리
		super.toTeleport(x, y, map, effect);
		// 땅속에서 나오는 액션 취하기.
		// rise
		if(getGfxMode() == Lineage.GFX_MODE_OPEN){
			toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), this, Lineage.GFX_MODE_RISE), false);
			setGfxMode(Lineage.GFX_MODE_WALK);
			toSender(S_ObjectMode.clone(BasePacketPooling.getPool(S_ObjectMode.class), this), false);
		}
	}

}
