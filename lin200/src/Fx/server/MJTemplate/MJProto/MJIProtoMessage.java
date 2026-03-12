package Fx.server.MJTemplate.MJProto;

import java.io.IOException;

public interface MJIProtoMessage {
	public int getSerializedSize();
	public int getMemorizedSerializeSizedSize();
	public boolean isInitialized();
	public long getInitializeBit();
	public ProtoOutputStream writeTo(MJEProtoMessages message);
	public void writeTo(ProtoOutputStream stream) throws IOException;
	public MJIProtoMessage readFrom(ProtoInputStream stream) throws IOException;
	public MJIProtoMessage readFrom(lineage.network.LineageClient clnt, byte[] bytes);
	public MJIProtoMessage copyInstance();
	public MJIProtoMessage reloadInstance();
	public void dispose();
}
