package Fx.server.MJTemplate.MJProto;

import java.util.HashMap;

import lineage.network.LineageClient;

public enum MJEProtoMessages {
	SC_SELECT_CHARACTER_STATUS_NOTI(0x0000, 					Fx.server.MJTemplate.MJProto.Models.SC_SELECT_CHARACTER_STATUS_NOTI.newInstance()),
	SC_CUSTOM_SETTINGS_NOTI(0x002A,	 							Fx.server.MJTemplate.MJProto.Models.SC_CUSTOM_SETTINGS_NOTI.newInstance()),
	SC_OBJECT_ATTRIBUTE_NOTI(0x0035,	 						Fx.server.MJTemplate.MJProto.Models.SC_OBJECT_ATTRIBUTE_NOTI.newInstance()),
	SC_TOAST_NOTI(0x0206,	 									Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI.newInstance()),
	SC_TIMER_UI_NOTI(0x0038,	 								Fx.server.MJTemplate.MJProto.Models.SC_TIMER_UI_NOTI.newInstance()),
	SC_AUTO_SUPPORT_NOTI(0x0039,	 							Fx.server.MJTemplate.MJProto.Models.SC_AUTO_SUPPORT_NOTI.newInstance()),
	SC_SKILL_DELAY_NOTI(0x003A,		 							Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI.newInstance()),
	SC_BUFFDATA_NOTI(0x0020, 									Fx.server.MJTemplate.MJProto.Models.SC_BUFFDATA_NOTI.newInstance()),
	SC_BUFFICON_NOTI(0x0021, 									Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI.newInstance()),
	;

	private int value;
	private MJIProtoMessage message;
	MJEProtoMessages(int value, MJIProtoMessage message){
		this.value = value;
		this.message = message;
	}
	
	public int toInt(){
		return value;
	}
	
	public boolean equals(MJEProtoMessages v){
		return value == v.value;
	}
	
	public MJIProtoMessage getMessageInstance(){
		return message;
	}
	
	public void reloadMessage(){
		MJIProtoMessage tmp = message;
		message = message.reloadInstance();
		if(tmp != null){
			tmp.dispose();
			tmp = null;
		}
	}
	
	public MJIProtoMessage copyInstance(){
		return message == null ? null : message.copyInstance();
	}
	
	private static final HashMap<Integer, MJEProtoMessages> messages;
	static{
		MJEProtoMessages[] msgs = MJEProtoMessages.values();
		messages = new HashMap<Integer, MJEProtoMessages>(msgs.length);
		for(MJEProtoMessages m : msgs)
			messages.put(m.toInt(), m);
	}
	
	public static MJEProtoMessages fromInt(int messageId){
		return messages.get(messageId);
	}
	
	public static boolean existsProto(LineageClient clnt, byte[] bytes){
		//System.out.println(MJHexHelper.toString(bytes, bytes.length));
		int messageId = bytes[1] & 0xff | bytes[2] << 8 & 0xff00;
		/*if(messageId == 0x33d) {
			System.out.println(MJHexHelper.toString(bytes, bytes.length));
		}*/
		
		MJEProtoMessages message = fromInt(messageId);
		
		if (message == null) {
//			System.out.println(MJHexHelper.toString(bytes, bytes.length));
			return false;
		}
		
		MJIProtoMessage iMessage = message.message.copyInstance().readFrom(clnt, bytes);
		if(!iMessage.isInitialized()) {
//			System.out.println(MJHexHelper.toString(bytes, bytes.length));	
			printNotInitialized(clnt.getPc() == null ? clnt.getAccountIp() : clnt.getPc().getName(), messageId, iMessage.getInitializeBit());
		}
		iMessage.dispose();
		return true;
	}
	
	public static void printNotInitialized(String ownerInfo, int messageId, long bit){
//		System.out.println(createNotInitializedMessage(ownerInfo, messageId, bit));
	}
	
	public static String createNotInitializedMessage(String ownerInfo, int messageId, long bit){
		return String.format("MJProto `It was not initialized.` ownerInfo : %s, messageid : %s, initializeBit : %08X", ownerInfo, fromInt(messageId).name(), bit);
	}
	
	public static void getInstance(){
//		System.out.println("MJEProtoMessages Initialized.");
	}
}
