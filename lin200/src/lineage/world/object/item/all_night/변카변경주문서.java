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

public class 변카변경주문서 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 변카변경주문서();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() != null) {
			ItemInstance item = cha.getInventory().value(cbp.readD());

			if (item != null && item.getItem() != null) {
				if (item.getName().contains("변신 카드")) {
		
						List<String> itemList = new ArrayList<String>();
						itemList.add("도플갱어 변신 카드");
						itemList.add("가드리아 변신 카드");
						itemList.add("드루가 변신 카드");
						itemList.add("플래바포 변신 카드");
						itemList.add("발키리 변신 카드");
						itemList.add("필리아 변신 카드");
						itemList.add("플래티넘데스 변신 카드");
						itemList.add("황금도플 변신 카드");
						itemList.add("드슬 변신 카드");
						itemList.add("기르타스 변신 카드");
                        itemList.add("신화 랭킹 변신 카드");
						
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
							ChattingController.toChatting(cha, "해당 아이템에 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
						}

				} else {
					ChattingController.toChatting(cha, "변신카드에만 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			}
		}
	}
}
