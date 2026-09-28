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

public class 귀걸이2차변경주문서 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 귀걸이2차변경주문서();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() == null) return;
		
		ItemInstance item = cha.getInventory().value(cbp.readD());
		
		// 1. 방어 로직: 아이템이 없거나 착용 중일 때 튕겨내기
		if (item == null) return;
		if (item.isEquipped()) {
			ChattingController.toChatting(cha, "착용 중인 아이템에는 사용할 수 없습니다. 장착 해제 후 사용해주세요.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		List<String> itemList = new ArrayList<String>();
		itemList.add("시어의 심안");
		itemList.add("반역자의 방패");
		
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
				
				// 속성 보존 (방패/가더류)
				temp.setEnFire(item.getEnFire());
				temp.setEnWater(item.getEnWater());
				temp.setEnEarth(item.getEnEarth());
				temp.setEnWind(item.getEnWind());
				
				// 💡 [핵심] 처리 순서 변경: 무조건 소모를 먼저 진행합니다!
				// 주문서 1장 소모
				cha.getInventory().count(this, getCount() - 1, true);
				
				// 기존 재료 아이템 소모 
				// (주의: 만약 이 부분에서 여전히 장비가 0개로 안 지워진다면, 
				// cha.getInventory().remove(item, true); 또는 cha.getInventory().deleteItem(item); 으로 변경하세요)
				cha.getInventory().count(item, item.getCount() - 1, true);
				
				// 마지막에 새 아이템 지급
				cha.getInventory().append(temp, true);
				
				ChattingController.toChatting(cha, String.format("%s 획득하였습니다.", Util.getStringWord(temp.getItem().getName(), "을", "를")), Lineage.CHATTING_MODE_MESSAGE);
			}
		} else {
			ChattingController.toChatting(cha, "해당 아이템에 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
		}
	}
}