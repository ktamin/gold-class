package lineage.world.object.item.all_night;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 마안변경주문서 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 마안변경주문서();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() != null) {
			ItemInstance item = cha.getInventory().value(cbp.readD());

			if (item != null && item.getItem() != null) {
				if (item instanceof ItemInstance) {
		
						List<String> itemList = new ArrayList<String>();
						itemList.add("탄생의 마안");
						itemList.add("형상의 마안");
						itemList.add("생명의 마안");
						
						boolean result = false;
						for (String list : itemList) {
							if (list.equalsIgnoreCase(item.getItem().getName())) {
								result = true;
								break;
							}
						}
						
						if (result) {
							itemList.remove(item.getItem().getName());
							
							String itemName = itemList.get(Util.random(0, itemList.size() - 1));
							
							Item tempItem = ItemDatabase.find(itemName);						
							if (tempItem != null) {
								ItemInstance temp = ItemDatabase.newInstance(tempItem);
								temp.setObjectId(ServerDatabase.nextItemObjId());
								temp.setBless(item.getBless());
								temp.setEnLevel(item.getEnLevel());
								temp.setDefinite(true);
								cha.getInventory().append(temp, true);
								
								cha.getInventory().count(item, item.getCount() - 1, true);
								cha.getInventory().count(this, getCount() - 1, true);
								
								ChattingController.toChatting(cha, String.format("%s 획득하였습니다.", Util.getStringWord(temp.getItem().getName(), "을", "를")), Lineage.CHATTING_MODE_MESSAGE);
							}
						} else {
							ChattingController.toChatting(cha, "해당 마안에 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
						}

				} else {
					ChattingController.toChatting(cha, "영웅급 마안에 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			}
		}
	}
}
