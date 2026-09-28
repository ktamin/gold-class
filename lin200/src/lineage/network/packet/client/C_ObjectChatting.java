package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.ClientBasePacket;
import lineage.world.controller.ChattingController;
import lineage.world.object.instance.PcInstance;

public class C_ObjectChatting extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_ObjectChatting(data, length);
		else
			((C_ObjectChatting)bp).clone(data, length);
		return bp;
	}
	
	public C_ObjectChatting(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc){
		// 버그 방지.
		if(pc==null || pc.isWorldDelete())
			return this;
		
		int mode = readC();
		String msg = readS();
		
		// ==========================================
		// ✅ [추가] 잊섬 채팅 및 /명령어(/파티 등) 원천 차단
		// ==========================================
				if ((pc.getMap() == 70 || pc.getMap() == 809) && pc.getGm() == 0 && !lineage.share.Lineage.is_twistisland_chatting) {
					// 일반 채팅이나 /명령어 시도 시 시스템 메세지만 띄우고 종료
					ChattingController.toChatting(pc, "잊혀진 섬에서는 채팅 및 모든 명령어를 사용할 수 없습니다.", lineage.share.Lineage.CHATTING_MODE_MESSAGE);
					return this; // 패킷 처리 중단 (파티 로직으로 넘어가지 않음)
				}
		// ==========================================
		
		if(pc.getGm()>0 || !pc.isTransparent())
			ChattingController.toChatting(pc, msg, mode);
		
		return this;
	}
}
