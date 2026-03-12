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
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class 용갑옷속성변경주문서 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 용갑옷속성변경주문서();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() != null) {
			ItemInstance item = cha.getInventory().value(cbp.readD());

			if (item != null && item.getItem() != null) {
				if (item instanceof ItemArmorInstance) {
//					if (item.getEnLevel() > 4) {
						List<String> itemList = new ArrayList<String>();
						itemList.add("완력");
						itemList.add("예지력");
						itemList.add("인내력");
						itemList.add("마력");

						boolean result = false;
						if (item.getItem().getName().contains("안타라스의 완력") || item.getItem().getName().contains("안타라스의 마력") || item.getItem().getName().contains("안타라스의 인내력") || item.getItem().getName().contains("안타라스의 예지력") || 
							item.getItem().getName().contains("파푸리온의 완력") || item.getItem().getName().contains("파푸리온의 마력") || item.getItem().getName().contains("파푸리온의 인내력") || item.getItem().getName().contains("파푸리온의 예지력") || 
							item.getItem().getName().contains("린드비오르의 완력") || item.getItem().getName().contains("린드비오르의 마력") || item.getItem().getName().contains("린드비오르의 인내력") || item.getItem().getName().contains("린드비오르의 예지력") || 
							item.getItem().getName().contains("발라카스의 완력") || item.getItem().getName().contains("발라카스의 마력") || item.getItem().getName().contains("발라카스의 인내력") || item.getItem().getName().contains("발라카스의 예지력")) {
							result = true;
						}

						if (result) {
							itemList.remove(item.getItem().getName().substring(item.getItem().getName().indexOf("의") + 1).trim());
							
							String tempName = item.getItem().getName().substring(0, item.getItem().getName().indexOf("의") + 1).trim();
							String itemName = tempName + " " + itemList.get(Util.random(0, itemList.size() - 1));

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
//					} else {
//						ChattingController.toChatting(cha, "+5이상 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
//					}
				} else {
					ChattingController.toChatting(cha, "방어구에 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			}
		}
	}
}
