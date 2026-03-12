package Fx.server.MJTemplate.MJProto.Models;

import java.io.IOException;

import Fx.server.MJTemplate.MJProto.MJEProtoMessages;
import Fx.server.MJTemplate.MJProto.MJIProtoMessage;
import Fx.server.MJTemplate.MJProto.ProtoInputStream;
import Fx.server.MJTemplate.MJProto.ProtoOutputStream;
import Fx.server.MJTemplate.MJProto.WireFormat;
import lineage.network.LineageClient;
import lineage.world.object.instance.PcInstance;

public class SC_BUFFICON_NOTI implements Fx.server.MJTemplate.MJProto.MJIProtoMessage{
	public static final int REMAINING_TYPE_SECONDS = 0;
	public static final int REMAINING_TYPE_BLESS   = 1;
	public static final int REMAINING_TYPE_MINUTE  = 2;
	
	public static SC_BUFFICON_NOTI newInstance(){
		return new SC_BUFFICON_NOTI();
	}
	
	public static void on(PcInstance pc, int effectId, int remainingTime, int remainingType) {
		SC_BUFFICON_NOTI noti = SC_BUFFICON_NOTI.newInstance();
		noti.setEffectId(effectId);
		noti.setRemainingTime(remainingTime);
		noti.setRemainingType(remainingType);
		pc.sendProto(noti, MJEProtoMessages.SC_BUFFICON_NOTI.toInt());		
	}
	
	public static void off(PcInstance pc, int effectId) {
		on(pc, effectId, 0, REMAINING_TYPE_SECONDS);
	}
	
	public static void onSeconds(PcInstance pc, int effectId, int remainingTime) {
		on(pc, effectId, remainingTime, REMAINING_TYPE_SECONDS);
	}
	
	public static void onMinute(PcInstance pc, int effectId, int remainingTime) {
		on(pc, effectId, remainingTime, REMAINING_TYPE_MINUTE);
	}
	
	public static void onBless(PcInstance pc, int effectId, int remainingTime) {
		on(pc, effectId, remainingTime, REMAINING_TYPE_BLESS);
	}

	
	private int mEffectId;
	private int mRemainingTime;
	private int mRemainingType;

	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BUFFICON_NOTI(){}

	public void setEffectId(int effectId){
		_bit |= 0x01;
		mEffectId = effectId;
	}
	public boolean hasEffectId(){
		return (_bit & 0x01) == 0x01;
	}
	public int getEffectId(){
		return mEffectId;
	}

	public void setRemainingTime(int remainingTime){
		_bit |= 0x02;
		mRemainingTime = remainingTime;
	}
	public boolean hasRemainingTime(){
		return (_bit & 0x02) == 0x02;
	}
	public int getRemainingTime(){
		return mRemainingTime;
	}
	
	public void setRemainingType(int remainingType){
		_bit |= 0x04;
		mRemainingType = remainingType;
	}
	public boolean hasRemainingType(){
		return (_bit & 0x04) == 0x04;
	}
	public int getRemainingType(){
		return mRemainingType;
	}
	
	
	@Override
	public int getSerializedSize() {
		int size = 0;
		if(hasEffectId())
			size += Fx.server.MJTemplate.MJProto.ProtoOutputStream.computeInt32Size(1, mEffectId);
		if(hasRemainingTime())
			size += Fx.server.MJTemplate.MJProto.ProtoOutputStream.computeInt32Size(2, mRemainingTime);
		if(hasRemainingType())
			size += Fx.server.MJTemplate.MJProto.ProtoOutputStream.computeInt32Size(3, mRemainingType);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public int getMemorizedSerializeSizedSize() {
		return _memorizedSerializedSize;
	}

	@Override
	public boolean isInitialized() {
		if(_memorizedIsInitialized == 1)
			return true;
		
		boolean isInitialized = hasEffectId() && hasRemainingTime() && hasRemainingType();
		_memorizedIsInitialized = (byte) (isInitialized ? 1 : -1);
		return isInitialized;
	}

	@Override
	public long getInitializeBit() {
		return (long)_bit;
	}

	@Override
	public ProtoOutputStream writeTo(MJEProtoMessages message) {
		Fx.server.MJTemplate.MJProto.ProtoOutputStream stream =
				Fx.server.MJTemplate.MJProto.ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try{
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public void writeTo(ProtoOutputStream stream) throws IOException {
		stream.writeUInt32(1, mEffectId);	
		stream.writeUInt32(2, mRemainingTime);	
		stream.writeUInt32(3, mRemainingType);			
	}

	@Override
	public MJIProtoMessage readFrom(ProtoInputStream stream) throws IOException {
		return this;
	}

	@Override
	public MJIProtoMessage readFrom(LineageClient clnt, byte[] bytes) {
		Fx.server.MJTemplate.MJProto.ProtoInputStream is = Fx.server.MJTemplate.MJProto.ProtoInputStream.newInstance(bytes, Fx.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + Fx.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);
			
			if (!isInitialized())
				return this;
						
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_BUFFICON_NOTI();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}