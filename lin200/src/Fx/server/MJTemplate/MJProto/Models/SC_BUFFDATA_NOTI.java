package Fx.server.MJTemplate.MJProto.Models;

import java.io.IOException;

import Fx.server.MJTemplate.MJProto.MJBytesOutputStream;
import Fx.server.MJTemplate.MJProto.MJEProtoMessages;
	
public class SC_BUFFDATA_NOTI extends Fx.server.MJTemplate.MJProto.MJAProtoMessage {
	
	public static SC_BUFFDATA_NOTI newInstance() {
		return new SC_BUFFDATA_NOTI();
	}

	@Override
	public MJEProtoMessages getMessageType() {
		return MJEProtoMessages.SC_BUFFDATA_NOTI;
	}	

	MJBytesOutputStream mStream;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_BUFFDATA_NOTI() {
		mStream = new MJBytesOutputStream();
	}
	
	public SC_BUFFDATA_NOTI addBuffData(int icon, int duration, int group, int startMsg, int endMsg){
		try {
			mStream.writeD(icon);
			mStream.writeD(duration);
			mStream.writeD(group);
			mStream.writeD(startMsg);
			mStream.writeD(endMsg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	@Override
	public long getInitializeBit() {
		return (long) _bit;
	}

	@Override
	public int getMemorizedSerializeSizedSize() {
		return _memorizedSerializedSize;
	}

	@Override
	public int getSerializedSize() {
		int size = 0;
		size += mStream.getPosition() + 4;
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1) {
			return true;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(Fx.server.MJTemplate.MJProto.ProtoOutputStream output) throws java.io.IOException {
		int sequence = 1;
		output.writeBytes(sequence++, mStream.toArray());
	}

	@Override
	public Fx.server.MJTemplate.MJProto.ProtoOutputStream writeTo(Fx.server.MJTemplate.MJProto.MJEProtoMessages message) {
		Fx.server.MJTemplate.MJProto.ProtoOutputStream stream = Fx.server.MJTemplate.MJProto.ProtoOutputStream.newInstance(getSerializedSize() + Fx.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try {
			writeTo(stream);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public Fx.server.MJTemplate.MJProto.MJIProtoMessage readFrom(Fx.server.MJTemplate.MJProto.ProtoInputStream input) throws java.io.IOException {
		return this;
	}

	@Override
	public Fx.server.MJTemplate.MJProto.MJIProtoMessage readFrom(lineage.network.LineageClient clnt, byte[] bytes) {
		Fx.server.MJTemplate.MJProto.ProtoInputStream is = Fx.server.MJTemplate.MJProto.ProtoInputStream.newInstance(bytes, Fx.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
				((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + Fx.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);

			if (!isInitialized())
				return this;

			lineage.world.object.instance.PcInstance pc = clnt.getPc();
			if (pc == null) {
				return this;
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Ex.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public Fx.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new SC_BUFFDATA_NOTI();
	}

	@Override
	public Fx.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}