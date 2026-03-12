package lineage.world.object.npc;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 장신구제작NPC extends object {
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "accCreateNpc"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
		if (pc.getInventory() != null) {
			if (action.equalsIgnoreCase("신성한 완력의 목걸이")) {
				createItem(pc, "신성한 완력의 목걸이", "완력의 목걸이", "최고급 루비", 25, 25);

			} else if (action.equalsIgnoreCase("신성한 민첩의 목걸이")) {
				createItem(pc, "신성한 민첩의 목걸이", "민첩의 목걸이", "최고급 에메랄드", 25, 25);

			} else if (action.equalsIgnoreCase("신성한 지식의 목걸이")) {
				createItem(pc, "신성한 지식의 목걸이", "지식의 목걸이", "최고급 사파이어", 25, 25);

			} else if (action.equalsIgnoreCase("신성한 완력의 벨트")) {
				createItem(pc, "신성한 완력의 벨트", "완력의 벨트", "최고급 루비", 25, 25);

			} else if (action.equalsIgnoreCase("신성한 민첩의 벨트")) {
				createItem(pc, "신성한 민첩의 벨트", "민첩의 벨트", "최고급 에메랄드", 25, 25);

			} else if (action.equalsIgnoreCase("신성한 지식의 벨트")) {
				createItem(pc, "신성한 지식의 벨트", "지식의 벨트", "최고급 사파이어", 25, 25);

			} else if (action.equalsIgnoreCase("신성한 완력의 반지")) {
				createItem(pc, "신성한 완력의 반지", "완력의 반지", "최고급 루비", 25, 25);

			} else if (action.equalsIgnoreCase("신성한 민첩의 반지")) {
				createItem(pc, "신성한 민첩의 반지", "민첩의 반지", "최고급 에메랄드", 25, 25);

			} else if (action.equalsIgnoreCase("신성한 지식의 반지")) {
				createItem(pc, "신성한 지식의 반지", "지식의 반지", "최고급 사파이어", 25, 25);

			} else if (action.equalsIgnoreCase("신성한 기백의 반지")) {
				createItem(pc, "신성한 기백의 반지", "기백의 반지", "최고급 다이아몬드", 25, 25);
			}
		}
	}

	public void createItem(PcInstance pc, String newItemName, String itemName, String materialItem, long count, double percent) {
		if (pc.getInventory() != null) {
			// 재료
			ItemInstance item1 = null;
			// 재료
			ItemInstance item2 = null;

			for (ItemInstance i : pc.getInventory().getList()) {
				if (i.getItem().getName().equalsIgnoreCase(itemName) && i.getEnLevel() == 0 && i.getBless() == 1)
					item1 = i;
				if (i.getItem().getName().equalsIgnoreCase(materialItem) && i.getCount() >= count)
					item2 = i;
			}

			if (item1 != null && item2 != null) {
				if (pc.getGm() > 0 || Math.random() < (percent * 0.01)) {
					Item item = ItemDatabase.find(newItemName);

					if (item != null) {
						ItemInstance temp = ItemDatabase.newInstance(item);
						temp.setObjectId(ServerDatabase.nextItemObjId());
						temp.setBless(1);
						temp.setEnLevel(0);
						temp.setDefinite(true);
						pc.getInventory().append(temp, true);

						ChattingController.toChatting(pc, String.format("'%s' 제작에 성공하였습니다! ", newItemName), Lineage.CHATTING_MODE_MESSAGE);
					}
				} else {
					ChattingController.toChatting(pc, String.format("'%s' 제작에 실패하였습니다. ", newItemName), Lineage.CHATTING_MODE_MESSAGE);
				}

				pc.getInventory().count(item1, item1.getCount() - 1, true);
				pc.getInventory().count(item2, item2.getCount() - count, true);
			} else {
				ChattingController.toChatting(pc, String.format("%s, %s(%d개)가 필요합니다.", itemName, materialItem, count), Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}
}
