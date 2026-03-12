package Fx.server.MJTemplate.MJProto.Models;

import Fx.server.MJTemplate.MJProto.MJEProtoMessages;
import lineage.bean.lineage.Party;
import lineage.network.LineageClient;
import lineage.world.controller.PartyController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public class SC_OBJECT_ATTRIBUTE_NOTI extends Fx.server.MJTemplate.MJProto.MJAProtoMessage {	
	public static void onLevelChanged(PcInstance pc, object target){
		SC_OBJECT_ATTRIBUTE_NOTI.newInstance()
			.setObjectId((int) target.getObjectId())
			.setAttribute(ObjectAttribute.Level)
			.setAttributeValue(target.getLevel())
			.send(pc);
	}
	
	public static void onNameSurfChanged(PcInstance pc, object target, int surfId){
		SC_OBJECT_ATTRIBUTE_NOTI.newInstance()
			.setObjectId((int) target.getObjectId())
			.setAttribute(ObjectAttribute.NameSurf)
			.setAttributeValue(surfId)
			.send(pc);
	}
	
	public static void onObjectTypeChanged(PcInstance pc, object obj){
		ObjectType oType = ObjectType.None;
		if (obj instanceof PcInstance) {
			oType = ObjectType.Player;

			Party p = PartyController.find(pc);
			if (p != null) {
				PcInstance other = (PcInstance) obj;
				if (p.isParty(other, p)) {
					oType = oType.or(ObjectType.Party);
				}
			}
		}else if(obj instanceof MonsterInstance){
			MonsterInstance mon = (MonsterInstance) obj;
			if (!mon.isDead()) {
				oType = oType.or(ObjectType.Monster);
				if (mon.getMonster().getAtkType() == 1) {
					oType = oType.or(ObjectType.Violent);
				}
			}
		}else if(obj instanceof ItemInstance){
			oType = ObjectType.Item;
		}

		SC_OBJECT_ATTRIBUTE_NOTI.newInstance()
			.setObjectId((int) obj.getObjectId())
			.setAttribute(ObjectAttribute.ObjectType)
			.setAttributeValue(oType.getFlags())
			.send(pc);
	}
	
	public static SC_OBJECT_ATTRIBUTE_NOTI newInstance() {
		return new SC_OBJECT_ATTRIBUTE_NOTI();
	}

	@Override
	public MJEProtoMessages getMessageType() {
		return MJEProtoMessages.SC_OBJECT_ATTRIBUTE_NOTI;
	}

	private int mObjectId;
	private ObjectAttribute mAttribute;
	private int mAttributeValue;
	
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_OBJECT_ATTRIBUTE_NOTI() {
		mAttribute = ObjectAttribute.None;
	}

	public SC_OBJECT_ATTRIBUTE_NOTI setObjectId(int objectId){
		mObjectId = objectId;
		return this;
	}

	public SC_OBJECT_ATTRIBUTE_NOTI setAttribute(ObjectAttribute attribute){
		mAttribute = attribute;
		return this;
	}
	
	public SC_OBJECT_ATTRIBUTE_NOTI setAttributeValue(int value){
		mAttributeValue = value;
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
		size += Fx.server.MJTemplate.MJProto.ProtoOutputStream.computeUInt32Size(1, mObjectId);
		size += Fx.server.MJTemplate.MJProto.ProtoOutputStream.computeEnumSize(2, mAttribute.toInt());
		size += Fx.server.MJTemplate.MJProto.ProtoOutputStream.computeUInt32Size(3, mAttributeValue);
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
		output.writeUInt32(1, mObjectId);
		output.writeUInt32(2, mAttribute.toInt());
		output.writeUInt32(3, mAttributeValue);
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
	public Fx.server.MJTemplate.MJProto.MJIProtoMessage readFrom(LineageClient clnt, byte[] bytes) {
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
		return new SC_OBJECT_ATTRIBUTE_NOTI();
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

	public enum ObjectAttribute{
		None(-1),
		Level(0),
		NameSurf(1),
		ObjectType(2),
		;
		
		private int mVal;
		ObjectAttribute(int val){
			mVal = val;
		}
		
		int toInt(){
			return mVal;
		}
	}
	
	public static class ObjectType {
		public static final ObjectType None = new ObjectType(0);
		public static final ObjectType Npc = new ObjectType(1 << 0);
		public static final ObjectType Player = new ObjectType(1 << 1);
		public static final ObjectType Monster = new ObjectType(1 << 2);
		public static final ObjectType Violent = new ObjectType(1 << 3);
		public static final ObjectType Item = new ObjectType(1 << 4);
		public static final ObjectType Party = new ObjectType(1 << 5);
		
		private int mFlags;
		private ObjectType(int flags){
			mFlags = flags;
		}
		
		public int getFlags(){
			return mFlags;
		}
		
		public ObjectType or(ObjectType other){
			return new ObjectType(mFlags | other.getFlags());
		}
	}
}