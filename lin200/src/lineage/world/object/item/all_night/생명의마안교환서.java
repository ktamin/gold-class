package lineage.world.object.item.all_night;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 생명의마안교환서 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 생명의마안교환서();
		return item;
	}
	
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() != null) {
			if (cha.getInventory() != null) {
				ItemInstance 수룡의마안 = null;
				ItemInstance 풍룡의마안 = null;
				ItemInstance 지룡의마안 = null;
				ItemInstance 화룡의마안 = null;

				for (ItemInstance item : cha.getInventory().getList()) {
					if (수룡의마안 == null && item.getItem() != null && item.getItem().getName().equalsIgnoreCase("수룡의 마안"))
						수룡의마안 = item;

					if (풍룡의마안 == null && item.getItem() != null && item.getItem().getName().equalsIgnoreCase("풍룡의 마안"))
						풍룡의마안 = item;

					if (지룡의마안 == null && item.getItem() != null && item.getItem().getName().equalsIgnoreCase("지룡의 마안"))
						지룡의마안 = item;

					if (화룡의마안 == null && item.getItem() != null && item.getItem().getName().equalsIgnoreCase("화룡의 마안"))
						화룡의마안 = item;

					if (수룡의마안 != null && 풍룡의마안 != null && 지룡의마안 != null && 화룡의마안 != null)
						break;
				}
				if (수룡의마안 != null && 풍룡의마안 != null && 지룡의마안 != null && 화룡의마안 != null) {
					Item i = ItemDatabase.find("생명의 마안");

					if (i != null) {
						ItemInstance temp = cha.getInventory().find(i.getName(), bless, i.isPiles());

						if (temp != null && (temp.getBless() != 1 || temp.getEnLevel() != 0))
							temp = null;

						if (temp == null) {
							// 겹칠수 있는 아이템이 존재하지 않을경우.
							if (i.isPiles()) {
								temp = ItemDatabase.newInstance(i);
								temp.setObjectId(ServerDatabase.nextItemObjId());
								temp.setBless(1);
								temp.setEnLevel(0);
								temp.setCount(1);
								temp.setDefinite(true);
								cha.getInventory().append(temp, true);
							} else {
								temp = ItemDatabase.newInstance(i);
								temp.setObjectId(ServerDatabase.nextItemObjId());
								temp.setBless(1);
								temp.setEnLevel(0);
								temp.setDefinite(true);
								cha.getInventory().append(temp, true);
							}
						} else {
							// 겹치는 아이템이 존재할 경우.
							cha.getInventory().count(temp, temp.getCount() + 1, true);
						}
                                                if (!cha.getInventory().isAden(Lineage.생명의마안_제작_아덴_수량, true)) {
                                                        ChattingController.toChatting(cha, "아데나가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
                                                        return;
                                                }
						cha.getInventory().count(수룡의마안, 수룡의마안.getCount() - 1, true);
						cha.getInventory().count(풍룡의마안, 풍룡의마안.getCount() - 1, true);
						cha.getInventory().count(지룡의마안, 지룡의마안.getCount() - 1, true);
						cha.getInventory().count(화룡의마안, 화룡의마안.getCount() - 1, true);
						ChattingController.toChatting(cha, "[생명의 마안]을 획득하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						
						cha.getInventory().count(this, getCount() - 1, true);
					}
				} else {
					if (수룡의마안 == null)
						ChattingController.toChatting(cha, "'수룡의 마안'이 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
					if (풍룡의마안 == null)
						ChattingController.toChatting(cha, "'풍룡의 마안'이 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
					if (지룡의마안 == null)
						ChattingController.toChatting(cha, "'지룡의 마안'이 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
					if (화룡의마안 == null)
						ChattingController.toChatting(cha, "'화룡의 마안'이 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			}
		}
            }
        }