package lineage.world.object.item.etc;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.ItemChanceBundle;
import lineage.database.ItemChanceBundleDatabase;
import lineage.database.ItemDatabase;
import lineage.database.ItemDropMessageDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.PcRobotInstance;

public class LimitBundle extends ItemInstance {

	// 🔮 아이템 이름별 '최초 생성 시 횟수' 지정
	private int getMaxCharge(String itemName) {
		switch (itemName) {
			case "연금술사의 돌": return 20;   
			case "기운 무기 상자": return 10;
			default: return 1;   
		}
	}

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null) 
			item = new LimitBundle();
		return item;
	}

	@Override
	public ItemInstance clone(Item item) {
		ItemInstance inst = super.clone(item);
		
		// 아이템이 처음 생성될 때 무조건 횟수(quantity)를 20개로 고정 세팅
		if (item != null && item.getName().equalsIgnoreCase("연금술사의 돌")) {
			this.quantity = 20;   
		} else {
			this.quantity = getMaxCharge(item.getName());
		}
		
		return inst;
	}

	// ==========================================
	// 🎁 우클릭 시: 찬스 번들(랜덤 보상 지급) + 횟수 1개 차감
	// ==========================================
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (!(cha instanceof PcInstance) || ((PcInstance) cha).isFishing())
			return;
			
		PcInstance pc = (PcInstance) cha;
		String itemName = getItem().getName();

		if (pc.getInventory() != null && pc.getInventory().getList().size() >= Lineage.inventory_max) {
			ChattingController.toChatting(pc, "인벤토리가 가득찼습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		List<ItemChanceBundle> list = new ArrayList<ItemChanceBundle>();
		ItemChanceBundleDatabase.find(list, itemName);
		
		if (list.size() > 0) {
			// 1단계: 가중치 확률 합산
			int totalWeight = 0;
			for (ItemChanceBundle ib : list) {
				totalWeight += (int)(ib.getItemChance() * 10000); 
			}

			if (totalWeight > 0) {
				// 2단계: 난수 추출 및 당첨 아이템 선정 (딱 1개만 당첨)
				int randomValue = Util.random(0, totalWeight - 1);
				int currentWeight = 0;
				ItemChanceBundle winner = null;
				
				for (ItemChanceBundle ib : list) {
					currentWeight += (int)(ib.getItemChance() * 10000);
					if (randomValue < currentWeight) {
						winner = ib;
						break; 
					}
				}

				// 3단계: 당첨된 아이템 인벤토리에 지급 (기존 ChanceBundle의 완벽한 겹침/생성 로직 적용)
				if (winner != null) {
					Item i = ItemDatabase.find(winner.getItem());
					if (i != null) {
						ItemInstance temp = pc.getInventory().find(i.getName(), winner.getItemBless(), i.isPiles());
						int count = Util.random(winner.getItemCountMin(), winner.getItemCountMax());

						if (temp != null && (temp.getBless() != winner.getItemBless() || temp.getEnLevel() != winner.getItemEnchant()))
							temp = null;

						if (temp == null) {
							if (i.isPiles()) {
								temp = ItemDatabase.newInstance(i);
								temp.setObjectId(ServerDatabase.nextItemObjId());
								temp.setBless(winner.getItemBless());
								temp.setEnLevel(winner.getItemEnchant());
								temp.setCount(count);
								temp.setDefinite(true);
								pc.getInventory().append(temp, true);
							} else {
								for (int idx = 0; idx < count; idx++) {
									temp = ItemDatabase.newInstance(i);
									temp.setObjectId(ServerDatabase.nextItemObjId());
									temp.setBless(winner.getItemBless());
									temp.setEnLevel(winner.getItemEnchant());
									temp.setDefinite(true);
									pc.getInventory().append(temp, true);
								}
							}
						} else {
							// 이미 인벤토리에 겹치는 아이템이 있으면 개수를 자동으로 합쳐줌!
							pc.getInventory().count(temp, temp.getCount() + count, true);
						}
						
						// 드랍 메시지 및 획득 알림
						if (Lineage.is_item_drop_msg_item && getItem() != null) {
							ItemDropMessageDatabase.sendMessage(pc, i.getName(), getItem().getName());
						}
						
						ItemChanceBundleDatabase.updateCount(i.getName());
						ChattingController.toChatting(pc, String.format("%s(%d) 획득: %s", i.getName(), count, itemName), Lineage.CHATTING_MODE_MESSAGE);
					}
				}
			}
		}

		// ==========================================
		// ⏳ 4단계: 연금술사의 돌 횟수 1개 차감 및 소멸 처리
		// ==========================================
		setQuantity(quantity - 1);
		
		if (getQuantity() < 1) {
			pc.getInventory().count(this, getCount() - 1, true);   
			pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 74, itemName + "이(가) 마력을 다하여 소멸되었습니다."));
		} else {
			pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 74, itemName + " 남은 횟수: " + getQuantity() + "회"));
		}
		
		pc.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
	}
}