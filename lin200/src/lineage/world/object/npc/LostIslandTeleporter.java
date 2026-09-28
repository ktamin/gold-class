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

public class LostIslandTeleporter extends object {
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		List<String> list = new ArrayList<String>();
		
		list.add(String.format("입장 가능 여부: %s", Lineage.is_lost_island_join ? "가능" : "불가"));
		list.add(String.format("입장 레벨: %d이상 입장 가능", Lineage.lost_island_min_level));
		
		if (Lineage.lost_island_join_item_count > 0)
			list.add(String.format("입장료: %s(%,d)", Lineage.lost_island_join_item, Lineage.lost_island_join_item_count));
		
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "losttel", null, list));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
		if (pc.getInventory() != null) {
			if (action.equalsIgnoreCase("lostIsland_teleport")) {
				if (pc.getGm() > 0 || Lineage.is_lost_island_join) {
					if (pc.getGm() > 0 || (Lineage.lost_island_min_level <= pc.getLevel())) {
						
						// 혈맹 제한: GM 제외, 혈맹이 없거나 신규 혈맹이면 입장 불가
						if (pc.getGm() == 0 && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
						    ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
						    return;
						}

						
						if (Lineage.lost_island_join_item_count < 1 || pc.getGm() > 0 || pc.getInventory().isAden(Lineage.lost_island_join_item, Lineage.lost_island_join_item_count, true))
							pc.toPotal(Util.random(32825, 32827), Util.random(32848, 32851), 70);
						else
							ChattingController.toChatting(pc, String.format("[입장료] %s(%,d) 부족합니다.", Lineage.lost_island_join_item, Lineage.lost_island_join_item_count), Lineage.CHATTING_MODE_MESSAGE);						
					} else {
						ChattingController.toChatting(pc, String.format("잊혀진 섬은 %d레벨 이상 입장 가능합니다.", Lineage.lost_island_min_level), Lineage.CHATTING_MODE_MESSAGE);
					}
				} else {
					ChattingController.toChatting(pc, "현재 잊혀진 섬은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			}
		}
	}
}
