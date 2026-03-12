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
import lineage.world.object.instance.ItemBookInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;

public class 마법변경주문서 extends ItemInstance {



	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 마법변경주문서();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() != null) {
			ItemInstance item = cha.getInventory().value(cbp.readD());

			if (item != null && item.getItem() != null) {	
						List<String> itemList = new ArrayList<String>();
						itemList.add("기술서 (카운터 배리어)");
						itemList.add("마법서 (브레이브 멘탈)");
						itemList.add("마법서 (디스인티그레이트)");
						itemList.add("아머 브레이크");
						itemList.add("블랙미스릴 화살");
						
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
					ChattingController.toChatting(cha, "4대 법서에 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			}
		}
	}
