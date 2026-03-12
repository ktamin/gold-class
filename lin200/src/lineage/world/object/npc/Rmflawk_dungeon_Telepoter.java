package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.WantedController;   // ★ 추가
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class Rmflawk_dungeon_Telepoter extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		String time = null;
		List<String> msg = new ArrayList<String>();
			
		if (Lineage.giran_dungeon_inti_time < 12)
			time = "오전 " + String.valueOf(Lineage.giran_dungeon_inti_time) + "시";
		else
			time = "오후 "+ String.valueOf(Lineage.giran_dungeon_inti_time - 12) + "시";
		
		msg.add(String.valueOf(Lineage.giran_dungeon_time / 3600));
		msg.add(time);
		msg.add(String.format("%s", Lineage.giran_dungeon_level2));
		msg.add(String.format("%s", Lineage.giran_dungeon_level3));

		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "yadolantel4", null, msg));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp){

		// 1층
		if (action.equalsIgnoreCase("teleport rmflawk dungeon")) {
			if (pc.getLevel() < Lineage.rmflawk_dungeon_level) {
				ChattingController.toChatting(pc,
						String.format("1층은 %d레벨 이상 입장가능합니다.", Lineage.rmflawk_dungeon_level),
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			// ★ 혈맹 체크
			if (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name)) {
				ChattingController.toChatting(pc,
						"혈맹에 가입된 수배자만 입장할 수 있습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			// ★ 수배 체크
			if (!WantedController.checkWantedPc(pc)) {
				ChattingController.toChatting(pc,
						"수배 상태인 캐릭터만 입장할 수 있습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			pc.toPotal(32655, 32897, 522);
		}

		// 2층
		if (action.equalsIgnoreCase("teleport rmflawk dungeon2")) {
			if (pc.getLevel() < Lineage.rmflawk_dungeon_level2) {
				ChattingController.toChatting(pc,
						String.format("2층은 %d레벨 이상 입장가능합니다.", Lineage.rmflawk_dungeon_level2),
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			// ★ 혈맹 체크
			if (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name)) {
				ChattingController.toChatting(pc,
						"혈맹에 가입된 수배자만 입장할 수 있습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			// ★ 수배 체크
			if (!WantedController.checkWantedPc(pc)) {
				ChattingController.toChatting(pc,
						"수배 상태인 캐릭터만 입장할 수 있습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			pc.toPotal(32663, 32896, 523);
		}

		// 3층
		if (action.equalsIgnoreCase("teleport rmflawk dungeon3")) {
			if (pc.getLevel() < Lineage.rmflawk_dungeon_level3) {
				ChattingController.toChatting(pc,
						String.format("3층은 %d레벨 이상 입장가능합니다.", Lineage.rmflawk_dungeon_level3),
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			// ★ 혈맹 체크
			if (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name)) {
				ChattingController.toChatting(pc,
						"혈맹에 가입된 수배자만 입장할 수 있습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			// ★ 수배 체크
			if (!WantedController.checkWantedPc(pc)) {
				ChattingController.toChatting(pc,
						"수배 상태인 캐릭터만 입장할 수 있습니다.",
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			pc.toPotal(32655, 32895, 524);
		}
	}
}
