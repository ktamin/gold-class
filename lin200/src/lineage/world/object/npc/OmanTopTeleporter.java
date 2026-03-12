package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class OmanTopTeleporter extends object {
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		List<String> list = new ArrayList<String>();
		
		list.add(String.format("입장 가능 여부: %s", Lineage.is_oman_join ? "가능" : "불가"));
		list.add(String.format("입장 레벨: %d이상 입장 가능", Lineage.oman_min_level));
		
		if (Lineage.oman_join_item_count > 0)
			list.add(String.format("입장료: %s(%,d)", Lineage.oman_join_item, Lineage.oman_join_item_count));
		
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "omantel", null, list));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
		if (pc.getInventory() != null) {
			if (action.equalsIgnoreCase("oman_teleport")) {
				if (pc.getGm() > 0 || Lineage.is_oman_join) {
					if (pc.getGm() > 0 || (Lineage.oman_min_level <= pc.getLevel())) {			
						if (Lineage.oman_join_item_count < 1 || pc.getGm() > 0 || pc.getInventory().isAden(Lineage.oman_join_item, Lineage.oman_join_item_count, true))
							pc.toPotal(Util.random(32731, 32734), Util.random(32796, 32801), 101);
						else
							ChattingController.toChatting(pc, String.format("[입장료] %s(%,d) 부족합니다.", Lineage.oman_join_item, Lineage.oman_join_item_count), Lineage.CHATTING_MODE_MESSAGE);			
					} else {
						ChattingController.toChatting(pc, String.format("오만의 탑은 %d레벨 이상 입장 가능합니다.", Lineage.oman_min_level), Lineage.CHATTING_MODE_MESSAGE);
					}
				} else {
					ChattingController.toChatting(pc, "현재 오만의 탑은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			}
		}
	}
}
