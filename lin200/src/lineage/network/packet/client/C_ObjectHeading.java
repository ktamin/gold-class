package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectHeading;
import lineage.world.object.instance.PcInstance;

public class C_ObjectHeading extends ClientBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length) {
		if (bp == null)
			bp = new C_ObjectHeading(data, length);
		else
			((C_ObjectHeading) bp).clone(data, length);
		return bp;
	}

	public C_ObjectHeading(byte[] data, int length) {
		clone(data, length);
	}

	@Override
	public BasePacket init(PcInstance pc) {
		// 버그 방지.
		if (pc == null || pc.isDead() || !isRead(1) || pc.isWorldDelete())
			return this;
		
		int heading = readC();
		if (heading < 0 || heading > 7) {
			return this;
		}

		int oldHeading = pc.getHeading();
		if (heading == oldHeading) {
			return this;
		}
		
		pc.setHeading(heading);
		pc.toSender(S_ObjectHeading.clone(BasePacketPooling.getPool(S_ObjectHeading.class), pc), false);

//		pc.setHeading(readC());
		return this;
	}
}
