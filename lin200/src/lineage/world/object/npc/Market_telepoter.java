package lineage.world.object.npc;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class Market_telepoter extends object {
	
	@Override
	public void toTalk (PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_MessageYesNo.clone(BasePacketPooling.getPool(S_MessageYesNo.class), 777));
	}
	
	public void toAsk (PcInstance pc, boolean yes) {
		if (pc != null && yes) {
			pc.toPotal(Util.random(32796, 32800), Util.random(32920, 32925), Lineage.market_map);
		}
	}
}
