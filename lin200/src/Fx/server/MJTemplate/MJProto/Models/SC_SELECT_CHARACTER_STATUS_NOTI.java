package Fx.server.MJTemplate.MJProto.Models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Fx.server.MJTemplate.MJProto.MJBytesOutputStream;
import Fx.server.MJTemplate.MJProto.MJEProtoMessages;
import Fx.server.MJTemplate.MJProto.MJIProtoMessage;
import Fx.server.MJTemplate.MJProto.ProtoInputStream;
import Fx.server.MJTemplate.MJProto.ProtoOutputStream;
import Fx.server.MJTemplate.MJProto.WireFormat;

public class SC_SELECT_CHARACTER_STATUS_NOTI implements Fx.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_SELECT_CHARACTER_STATUS_NOTI newInstance() {
		return new SC_SELECT_CHARACTER_STATUS_NOTI();
	}

	private List<Integer> mArmorClasses;
	private int _memorizedSerializedSize = -1;

	private SC_SELECT_CHARACTER_STATUS_NOTI() {
		mArmorClasses = new ArrayList<>(3);
	}

	public SC_SELECT_CHARACTER_STATUS_NOTI addArmorClass(int armor) {
		mArmorClasses.add(armor);
		return this;
	}

	@Override
	public int getSerializedSize() {
		int size = 0;
		for (Integer i : mArmorClasses) {
			size += Fx.server.MJTemplate.MJProto.ProtoOutputStream.computeInt32Size(1, i);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public int getMemorizedSerializeSizedSize() {
		return _memorizedSerializedSize;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public long getInitializeBit() {
		return 1;
	}

	@Override
	public ProtoOutputStream writeTo(MJEProtoMessages message) {
		Fx.server.MJTemplate.MJProto.ProtoOutputStream stream = Fx.server.MJTemplate.MJProto.ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try {
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public void writeTo(ProtoOutputStream stream) throws IOException {
		MJBytesOutputStream output = new MJBytesOutputStream();
		try {
			for (Integer i : mArmorClasses) {
				output.writeH((short) i.shortValue());
			}
			stream.writeBytes(1, output.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		output.close();
	}

	@Override
	public MJIProtoMessage readFrom(ProtoInputStream stream) throws IOException {
		return this;
	}

	@Override
	public MJIProtoMessage readFrom(lineage.network.LineageClient clnt, byte[] bytes) {
		Fx.server.MJTemplate.MJProto.ProtoInputStream is = Fx.server.MJTemplate.MJProto.ProtoInputStream.newInstance(bytes, Fx.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
				((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + Fx.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);

			if (!isInitialized())
				return this;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_SELECT_CHARACTER_STATUS_NOTI();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
	}

}
