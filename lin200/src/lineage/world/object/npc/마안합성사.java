package lineage.world.object.npc;

import all_night.Lineage_Balance;
import java.util.ArrayList;
import java.util.List;
import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import static lineage.share.Lineage.생명의마안_제작_아덴_수량;
import static lineage.share.Lineage.탄생의마안_확률;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.MagicDoll;

//... 생략된 import는 유지하세요.
import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI.ToastType;

public class 마안합성사 extends object {
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "maanNpc"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
		if (pc.isWorldDelete() || pc.isDead() || pc.isLock() || pc.getInventory() == null) return;

		ItemInstance 수룡 = null, 풍룡 = null, 지룡 = null, 화룡 = null, 탄생 = null, 형상 = null;
		for (ItemInstance item : pc.getInventory().getList()) {
			String name = item.getItem().getName();
			if (수룡 == null && name.equalsIgnoreCase("수룡의 마안")) 수룡 = item;
			else if (풍룡 == null && name.equalsIgnoreCase("풍룡의 마안")) 풍룡 = item;
			else if (지룡 == null && name.equalsIgnoreCase("지룡의 마안")) 지룡 = item;
			else if (화룡 == null && name.equalsIgnoreCase("화룡의 마안")) 화룡 = item;
			else if (탄생 == null && name.equalsIgnoreCase("탄생의 마안")) 탄생 = item;
			else if (형상 == null && name.equalsIgnoreCase("형상의 마안")) 형상 = item;
		}

		String itemName = null;

		if (action.equalsIgnoreCase("탄생의 마안")) {
			
			if (수룡 == null || 지룡 == null) {
				ChattingController.toChatting(pc, "수룡 또는 지룡의 마안이 부족합니다.", Lineage.CHATTING_MODE_MESSAGE); return;
			}
			if (!pc.getInventory().isAden("아데나", Lineage.탄생의마안_제작_아덴_수량, true)) {
				ChattingController.toChatting(pc, "아데나가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE); return;
			}
			pc.getInventory().count(수룡, 수룡.getCount() - 1, true);
			pc.getInventory().count(지룡, 지룡.getCount() - 1, true);

			boolean success = Math.random() < Lineage.탄생의마안_확률;
			itemName = success ? Lineage.magicDoll[7][1] : "지룡의 마안";

			if (success) {
				ChattingController.toChatting(pc, "마안 합성 성공!", Lineage.CHATTING_MODE_MESSAGE);
				pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, 2048), true);
				broadcastToast(pc, itemName, "마안 합성");
			} else {
				ChattingController.toChatting(pc, "마안 합성에 실패하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
		else if (action.equalsIgnoreCase("형상의 마안")) {
			if (!Lineage.oman) {
				ChattingController.toChatting(pc, "현재는 형상의 마안을 제작할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE); return;
			}
			
			if (풍룡 == null || 탄생 == null) {
				ChattingController.toChatting(pc, "풍룡 또는 탄생의 마안이 부족합니다.", Lineage.CHATTING_MODE_MESSAGE); return;
			}
			
			if (!pc.getInventory().isAden("아데나", Lineage.형상의마안_제작_아덴_수량, true)) {
				ChattingController.toChatting(pc, "아데나가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE); return;
			}
			pc.getInventory().count(풍룡, 풍룡.getCount() - 1, true);
			pc.getInventory().count(탄생, 탄생.getCount() - 1, true);

			boolean success = Math.random() < Lineage.형상의마안_확률;
			itemName = success ? Lineage.magicDoll[7][2] : "탄생의 마안";

			if (success) {
				ChattingController.toChatting(pc, "마안 합성 성공!", Lineage.CHATTING_MODE_MESSAGE);
				pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, 2048), true);
				broadcastToast(pc, itemName, "마안 합성");
			} else {
				ChattingController.toChatting(pc, "마안 합성에 실패하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
		else if (action.equalsIgnoreCase("생명의 마안")) {
			if (!Lineage.oman4) {
				ChattingController.toChatting(pc, "현재는 생명의 마안을 제작할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE); return;
			}
			if (형상 == null || 화룡 == null) {
				ChattingController.toChatting(pc, "형상 또는 화룡의 마안이 부족합니다.", Lineage.CHATTING_MODE_MESSAGE); return;
			}
			if (!pc.getInventory().isAden("아데나", Lineage.생명의마안_제작_아덴_수량, true)) {
				ChattingController.toChatting(pc, "아데나가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE); return;
			}
			pc.getInventory().count(형상, 형상.getCount() - 1, true);
			pc.getInventory().count(화룡, 화룡.getCount() - 1, true);

			boolean success = Math.random() < Lineage.생명의마안_확률;
			itemName = success ? Lineage.magicDoll[7][0] : "형상의 마안";			

			if (success) {
				ChattingController.toChatting(pc, "마안 합성 성공!", Lineage.CHATTING_MODE_MESSAGE);
				pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, 2048), true);
				broadcastToast(pc, itemName, "마안 합성");
			} else {
				ChattingController.toChatting(pc, "마안 합성에 실패하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

		}

		// 아이템 지급 (성공/실패 둘 다)
		if (itemName != null) {
			Item item = ItemDatabase.find(itemName);
			if (item != null) {
				ItemInstance temp = ItemDatabase.newInstance(item);
				temp.setObjectId(ServerDatabase.nextItemObjId());
				temp.setBless(1);
				temp.setEnLevel(0);
				temp.setDefinite(true);
				pc.getInventory().append(temp, true);
			}
		}
	}

	private void broadcastToast(PcInstance pc, String itemName, String title) {
		String line1 = String.format("\\g1* %s [ %s ] *", title, itemName);
		String line2 = String.format("\\fH어느 아덴 용사가 %s [%s]을(를) 획득하였습니다.", title, itemName);
		
		SC_TOAST_NOTI.newInstance()
			.setMessage(line1)
			.setMessage2(line2)
			.setToastType(ToastType.HeavyText)
			.send(pc); // ✔ 대상은 여전히 본인이지만 메시지는 익명 처리됨
	}
}
