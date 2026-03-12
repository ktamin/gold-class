package Fx.server.MJTemplate.MJProto.Models;

import java.util.HashMap;

import Fx.server.MJTemplate.MJProto.MJEProtoMessages;

public class SC_TIMER_UI_NOTI extends Fx.server.MJTemplate.MJProto.MJAProtoMessage {
	
	public static SC_TIMER_UI_NOTI newInstance() {
		return new SC_TIMER_UI_NOTI();
	}
	//PSS 관련
	@Override
	public MJEProtoMessages getMessageType() {
		return MJEProtoMessages.SC_TIMER_UI_NOTI;
	}	

	private TimerType mTimerType;
	private int mRemainTime;
	
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_TIMER_UI_NOTI() {
		mTimerType = TimerType.Invalid;
		mRemainTime = 0;
	}

	public SC_TIMER_UI_NOTI setTimerType(TimerType timerType){
		mTimerType = timerType;
		return this;
	}

	public SC_TIMER_UI_NOTI setRemainTime(int remainTime){
		mRemainTime = remainTime;
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
		int sequence = 1;
		size += Fx.server.MJTemplate.MJProto.ProtoOutputStream.computeUInt32Size(sequence++, mTimerType.toInt());
		size += Fx.server.MJTemplate.MJProto.ProtoOutputStream.computeUInt32Size(sequence++, mRemainTime);
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
		output.writeUInt32(sequence++, mTimerType.toInt());
		output.writeUInt32(sequence++, mRemainTime);
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
		return new SC_TIMER_UI_NOTI();
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

	private static final HashMap<Integer, TimerType> mIds = new HashMap<>();
	public enum TimerType{
		Invalid(-1),
		Normal(0x00),
		;
		
		private int mVal;
		TimerType(int val){
			mVal = val;
			mIds.put(val, this);
		}
		
		public int toInt(){
			return mVal;
		}
		
		public static TimerType fromInt(int i){
			if(mIds.containsKey(i)){
				return mIds.get(i);
			} else {
				System.out.println("SC_TIMER_UI_NOTI::TimerType::fromInt() Invalid Id " + i);
				return Invalid;
			}
		}
	}
}