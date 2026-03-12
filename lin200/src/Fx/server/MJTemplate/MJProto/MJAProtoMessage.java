package Fx.server.MJTemplate.MJProto;

import lineage.network.LineageClient;
import lineage.world.World;
import lineage.world.object.instance.PcInstance;

public abstract class MJAProtoMessage implements MJIProtoMessage {
	public abstract MJEProtoMessages getMessageType();
	
	public void send(PcInstance pc){
		pc.sendProto(this, getMessageType(), true);
	}
	
	public void send(LineageClient clnt) {
		clnt.sendProto(this, getMessageType());
	}
	
	public void sendAll(Iterable<PcInstance> recievers){
		ProtoOutputStream stream = writeTo(getMessageType());
		for(PcInstance pc : recievers){
			pc.sendProto(stream, false);
		}
		stream.dispose();
	}

	public void sendAll(PcInstance[] recievers){
		ProtoOutputStream stream = writeTo(getMessageType());
		for(PcInstance pc : recievers){
			pc.sendProto(stream, false);
		}
		stream.dispose();
	}
	
	public void sendAll(){
		sendAll(World.getPcList());
	}
}
