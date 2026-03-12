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

public class Girtastelepoter extends object {
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		List<String> list = new ArrayList<String>();
		
		list.add(String.format("입장 가능 여부: %s", Lineage.is_girtas_join ? "가능" : "불가"));
		list.add(String.format("입장 레벨: %d이상 입장 가능", Lineage.girtas_min_level));
		
		if (Lineage.girtas_join_item_count > 0)
			list.add(String.format("입장료: %s(%,d)", Lineage.girtas_join_item, Lineage.girtas_join_item_count));
		
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "girtastel", null, list));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
	    if (!"girtas_teleport".equalsIgnoreCase(action) || pc.getInventory() == null)
	        return;

	    // 1. 던전 오픈여부(GM은 무시)
	    if (pc.getGm() == 0 && !Lineage.is_girtas_join) {
	        ChattingController.toChatting(pc, "현재 고대 거인의 무덤은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 2. 레벨 제한
	    if (pc.getGm() == 0 && pc.getLevel() < Lineage.girtas_min_level) {
	        ChattingController.toChatting(pc, String.format("고대 거인의 무덤은 %d레벨 이상 입장 가능합니다.", Lineage.girtas_min_level), Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 3. 혈맹 제한(GM 제외, 신규혈맹 불가)
	    if (pc.getGm() == 0 && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
	        ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 4. 입장료(없으면 바로 입장, 있으면 차감, 부족하면 안내)
	    if (pc.getGm() == 0 && Lineage.girtas_join_item_count > 0) {
	        if (!pc.getInventory().isAden(Lineage.girtas_join_item, Lineage.girtas_join_item_count, true)) {
	            ChattingController.toChatting(pc, String.format("[입장료] %s(%,d) 부족합니다.", Lineage.girtas_join_item, Lineage.girtas_join_item_count), Lineage.CHATTING_MODE_MESSAGE);
	            return;
	        }
	    }

	    // 5. 입장(좌표/맵번호는 기존과 동일)
	    pc.toPotal(Util.random(32767, 32773), Util.random(32899, 32901), 400);
	}
}
