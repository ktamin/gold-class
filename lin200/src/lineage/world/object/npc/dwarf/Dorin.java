package lineage.world.object.npc.dwarf;

import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.instance.DwarfInstance;
import lineage.world.object.instance.PcInstance;

public class Dorin extends DwarfInstance {
	
	public Dorin(Npc npc){
		super(npc);
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		
		// 1. 리스(접속) 후 경과 시간 계산 (현재 시간 - 캐릭터가 로그인한 시간)
		long elapsedTime = System.currentTimeMillis() - pc.getLoginTime();
		
		// 2. 30초(30000 밀리초)가 지나지 않았다면 창고 오픈 차단
		if (elapsedTime < 30000) {
			int remainTime = (int) ((30000 - elapsedTime) / 1000);
			ChattingController.toChatting(pc, "접속 후 30초가 지나야 창고를 이용할 수 있습니다. (남은 시간: " + remainTime + "초)", Lineage.CHATTING_MODE_MESSAGE);
			return; // 코드를 여기서 강제 종료하여 아래의 창고 창을 띄우지 않음
		}
		
		if(isLevel(pc.getLevel())){
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "dorin"));
		}else{
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "dorinl"));
		}
	}
}
