package lineage.world.object.item;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.ItemChanceBundle;
import lineage.database.ItemChanceBundleDatabase;
import lineage.database.ItemDatabase;
import lineage.database.ItemDropMessageDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcRobotInstance;

public class ChanceBundle extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ChanceBundle();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() != null && cha.getInventory().getList().size() >= Lineage.inventory_max) {
			ChattingController.toChatting(cha, "인벤토리가 가득찼습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		List<ItemChanceBundle> list = new ArrayList<ItemChanceBundle>();
		ItemChanceBundleDatabase.find(list, getItem().getName());
		
		if (list.size() < 1) return; // 상자 내용물이 없으면 중단

		// =================================================================
		// 💡 1단계: 상자 안의 모든 아이템 확률(가중치)을 합산합니다. (0.01% = 1)
		// =================================================================
		int totalWeight = 0;
		for (ItemChanceBundle ib : list) {
			// 기존 DB의 확률값(예: 100)을 10000(100.00%) 기준으로 변환하여 더함
			totalWeight += (int)(ib.getItemChance() * 10000); 
		}

		if (totalWeight <= 0) return; // 확률이 설정된 아이템이 하나도 없으면 중단

		// =================================================================
		// 💡 2단계: 0부터 총합계-1 사이의 난수를 단 한 번만 뽑습니다.
		// =================================================================
		int randomValue = Util.random(0, totalWeight - 1);
		
		// =================================================================
		// 💡 3단계: 누적 가중치를 통해 어떤 아이템에 당첨되었는지 결정합니다.
		// =================================================================
		int currentWeight = 0;
		ItemChanceBundle winner = null;
		
		for (ItemChanceBundle ib : list) {
			currentWeight += (int)(ib.getItemChance() * 10000);
			if (randomValue < currentWeight) {
				winner = ib;
				break; // 당첨자를 찾았으므로 즉시 종료 (무한루프 없음)
			}
		}

		// =================================================================
		// 💡 4단계: 당첨된 아이템 지급 및 상자 1개 정상 소모
		// =================================================================
		if (winner != null) {
			if (cha instanceof PcRobotInstance) {
				cha.getInventory().count(this, getCount() - 1, true); // 로봇이면 상자만 지움
				return;
			}
			
			Item i = ItemDatabase.find(winner.getItem());
			if (i != null) {
				ItemInstance temp = cha.getInventory().find(i.getName(), winner.getItemBless(), i.isPiles());
				int count = Util.random(winner.getItemCountMin(), winner.getItemCountMax());

				if (temp != null && (temp.getBless() != winner.getItemBless() || temp.getEnLevel() != winner.getItemEnchant()))
					temp = null;

				if (temp == null) {
					// 겹칠 수 있는 아이템인 경우
					if (i.isPiles()) {
						temp = ItemDatabase.newInstance(i);
						temp.setObjectId(ServerDatabase.nextItemObjId());
						temp.setBless(winner.getItemBless());
						temp.setEnLevel(winner.getItemEnchant());
						temp.setCount(count);
						temp.setDefinite(true);
						cha.getInventory().append(temp, true);
					} else {
						// 안 겹치는 아이템인 경우 (무기, 방어구 등)
						for (int idx = 0; idx < count; idx++) {
							temp = ItemDatabase.newInstance(i);
							temp.setObjectId(ServerDatabase.nextItemObjId());
							temp.setBless(winner.getItemBless());
							temp.setEnLevel(winner.getItemEnchant());
							temp.setDefinite(true);
							cha.getInventory().append(temp, true);
						}
					}
				} else {
					// 이미 인벤토리에 있고 겹쳐지는 아이템인 경우
					cha.getInventory().count(temp, temp.getCount() + count, true);
				}
				
				// 드랍 메시지 방송
				if (Lineage.is_item_drop_msg_item && getItem() != null) {
					ItemDropMessageDatabase.sendMessage(cha, i.getName(), getItem().getName());
				}
				
				ItemChanceBundleDatabase.updateCount(i.getName());
				ChattingController.toChatting(cha, String.format("%s(%d) 획득: %s", i.getName(), count, getItem().getName()), Lineage.CHATTING_MODE_MESSAGE);
				
			}
		}
		
		// 💡 [핵심 수정] 당첨 여부와 관계없이 무조건 마지막에 상자 1개를 소모합니다. 
		// 그래야 상자가 먹통이 되는 버그가 생기지 않습니다.
		cha.getInventory().count(this, getCount() - 1, true);
	}
/*
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
//		ItemChanceBundleDatabase.reload();
		if (cha.getInventory() != null && cha.getInventory().getList().size() >= Lineage.inventory_max) {
			ChattingController.toChatting(cha, "인벤토리가 가득찼습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		// 아이템 지급.
		int random = 0;
		int randomCount = 0;
		//double probability = Math.random();
		List<ItemChanceBundle> list = new ArrayList<ItemChanceBundle>();
		ItemChanceBundleDatabase.find(list, getItem().getName());
		
		//야도란 찬스아이템 보정
//		if(list.get(random).getCount() > 0){
//			list.remove(list.get(random).getName());
//			ChattingController.toChatting(cha, String.format("나 많이 나와서 안나올거야"+list.size()), Lineage.CHATTING_MODE_MESSAGE);
//		}
		if (list.size() < 1)
			return;

		for (;;) {
			if (randomCount++ > 50)
				break;
			
//			if (randomCount++ > list.size())
//				probability = Math.random();
			
			random = Util.random(0, list.size() - 1);
			
		
			if (list.get(random).getItemCountMin() < 1)
				break;
			
			double probability = Math.random();
			if (probability < list.get(random).getItemChance()) {
				if (cha instanceof PcRobotInstance) {
					// 수량 하향.
					cha.getInventory().count(this, getCount() - 1, true);
					break;
				}
				
				ItemChanceBundle ib = list.get(random);
				Item i = ItemDatabase.find(ib.getItem());
				
			
				if (i != null) {
					ItemInstance temp = cha.getInventory().find(i.getName(), ib.getItemBless(), i.isPiles());
					int count = Util.random(ib.getItemCountMin(), ib.getItemCountMax());

					if (temp != null && (temp.getBless() != list.get(random).getItemBless() || temp.getEnLevel() != ib.getItemEnchant()))
						temp = null;

					if (temp == null) {
						// 겹칠수 있는 아이템이 존재하지 않을경우.
						if (i.isPiles()) {
							temp = ItemDatabase.newInstance(i);
							temp.setObjectId(ServerDatabase.nextItemObjId());
							temp.setBless(ib.getItemBless());
							temp.setEnLevel(ib.getItemEnchant());
							temp.setCount(count);
							temp.setDefinite(true);
					
							cha.getInventory().append(temp, true);
						} else {
							for (int idx = 0; idx < count; idx++) {
								temp = ItemDatabase.newInstance(i);
								temp.setObjectId(ServerDatabase.nextItemObjId());
								temp.setBless(ib.getItemBless());
								temp.setEnLevel(ib.getItemEnchant());
								temp.setDefinite(true);

							
								cha.getInventory().append(temp, true);
							}
						}
					} else
						// 겹치는 아이템이 존재할 경우.

					cha.getInventory().count(temp, temp.getCount() + count, true);
					
					if (Lineage.is_item_drop_msg_item && i != null && this != null && getItem() != null) {
						ItemDropMessageDatabase.sendMessage(cha, i.getName(), getItem().getName());
					}
					//야도란 찬스아이템 보정
					 ItemChanceBundleDatabase.updateCount( i.getName());
					// 알림.
					ChattingController.toChatting(cha, String.format("%s(%d) 획득: %s", i.getName(), count, getItem().getName()), Lineage.CHATTING_MODE_MESSAGE);
					
				
						 cha.getInventory().count(this, getCount() - 1, true);
					 // 수량 하향.
					
				}
				break;
			}
		}
	}
*/	
}
