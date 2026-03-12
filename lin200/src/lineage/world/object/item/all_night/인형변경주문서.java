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
import lineage.world.object.item.MagicDoll;

public class 인형변경주문서 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 인형변경주문서();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() != null) {
			ItemInstance item = cha.getInventory().value(cbp.readD());

			if (item != null && item.getItem() != null) {
				if (item instanceof MagicDoll) {
					List<String> tempList = null;
					
					List<String> itemList_1 = new ArrayList<String>();
					itemList_1.add("마법인형: 데몬");
					itemList_1.add("마법인형: 데스나이트");
					itemList_1.add("마법인형: 바란카");
					itemList_1.add("마법인형: 타락");
					itemList_1.add("마법인형: 바포메트");
					itemList_1.add("마법인형: 얼음여왕");
					itemList_1.add("마법인형: 커츠");
					
					List<String> itemList_2 = new ArrayList<String>();
					itemList_2.add("마법인형: 안타라스");
					itemList_2.add("마법인형: 발라카스");
					itemList_2.add("마법인형: 린드비오르");
					itemList_2.add("마법인형: 파푸리온");

					List<String> itemList_3 = new ArrayList<String>();
					itemList_3.add("마법인형: 군주");
					itemList_3.add("마법인형: 기사");
					itemList_3.add("마법인형: 요정");
					itemList_3.add("마법인형: 마법사");
					
					List<String> itemList_4 = new ArrayList<String>();
					itemList_4.add("마법인형: 리치");
					itemList_4.add("마법인형: 사이클롭스");
					itemList_4.add("마법인형: 나이트발드");
					itemList_4.add("마법인형: 시어");
                    itemList_4.add("마법인형: 아이리스");
                    itemList_4.add("마법인형: 머미로드");
                    itemList_4.add("마법인형: 뱀파이어");
                    
					List<String> itemList_5 = new ArrayList<String>();
					itemList_5.add("마법인형: 진 기사");
					itemList_5.add("마법인형: 진 군주");
					itemList_5.add("마법인형: 진 요정");
					itemList_5.add("마법인형: 진 마법사");

					
					if (tempList == null) {
						for (String list : itemList_1) {
							if (list.equalsIgnoreCase(item.getItem().getName())) {
								tempList = itemList_1;
								break;
							}
						}
					}
					
					if (tempList == null) {
						for (String list : itemList_2) {
							if (list.equalsIgnoreCase(item.getItem().getName())) {
								tempList = itemList_2;
								break;
							}
						}
					}
					
					if (tempList == null) {
						for (String list : itemList_3) {
							if (list.equalsIgnoreCase(item.getItem().getName())) {
								tempList = itemList_3;
								break;
							}
						}
					}
					
					
					if (tempList == null) {
						for (String list : itemList_4) {
							if (list.equalsIgnoreCase(item.getItem().getName())) {
								tempList = itemList_4;
								break;
							}
						}
					}
					
					if (tempList == null) {
						for (String list : itemList_5) {
							if (list.equalsIgnoreCase(item.getItem().getName())) {
								tempList = itemList_5;
								break;
							}
						}
					}

					if (tempList != null) {
						tempList.remove(item.getItem().getName());

						String itemName = tempList.get(Util.random(0, tempList.size() - 1));

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

							ChattingController.toChatting(cha, String.format("\\fY%s \\fR을(를) 획득하였습니다.", temp.getItem().getName()), Lineage.CHATTING_MODE_MESSAGE);
						}
					} else {
						ChattingController.toChatting(cha, "해당 인형에 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
					}
				} else {
					ChattingController.toChatting(cha, "마법인형에 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			}
		}
	}
}
