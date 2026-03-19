package lineage.world.object.item.all_night;

import all_night.Lineage_Balance;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class ScrollOfRoomtisEnchant extends ItemInstance {

	// ▼▼▼ [핵심] 이 메서드가 없으면 ItemDatabase에서 인식을 못 합니다! ▼▼▼
	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ScrollOfRoomtisEnchant();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
	    if (cha.getInventory() != null) {
	        ItemInstance targetItem = cha.getInventory().value(cbp.readD());
	        if (targetItem == null) return;

	        String name = targetItem.getItem().getName();
	        
	        // 1. 룸티스 귀걸이인지 검사
	        if (!name.contains("룸티스의 검은빛 귀걸이") && 
	            !name.contains("룸티스의 붉은빛 귀걸이") && 
	            !name.contains("룸티스의 보라빛 귀걸이")) {
	            ChattingController.toChatting(cha, "룸티스 귀걸이에만 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
	            return;
	        }

	        // [수정됨] 2. 최대 강화 수치 제한 (외부 컨프값 적용)
	        int maxEnchant = Lineage.item_enchant_accessory_max; 
	        if (maxEnchant > 0 && targetItem.getEnLevel() >= maxEnchant) {
	            ChattingController.toChatting(cha, String.format("장신구는 최대 +%d까지 인챈트 가능합니다.", maxEnchant), Lineage.CHATTING_MODE_MESSAGE);
	            return;
	        }

	        // 3. 단계별 확률 가져오기 (인덱스 에러 방지를 위해 9단계까지만 체크)
	        double chance = 0;
	        int currentEnLevel = targetItem.getEnLevel();
	        
	        switch (currentEnLevel) {
	            case 0: chance = Lineage_Balance.roomtis_enchant_prob0; break;
	            case 1: chance = Lineage_Balance.roomtis_enchant_prob1; break;
	            case 2: chance = Lineage_Balance.roomtis_enchant_prob2; break;
	            case 3: chance = Lineage_Balance.roomtis_enchant_prob3; break;
	            case 4: chance = Lineage_Balance.roomtis_enchant_prob4; break;
	            case 5: chance = Lineage_Balance.roomtis_enchant_prob5; break;
	            case 6: chance = Lineage_Balance.roomtis_enchant_prob6; break;
	            case 7: chance = Lineage_Balance.roomtis_enchant_prob7; break;
	            case 8: chance = Lineage_Balance.roomtis_enchant_prob8; break;
	            case 9: chance = Lineage_Balance.roomtis_enchant_prob9; break;
	            default: chance = 0; break; // 10강 이상은 확률 0
	        }

			// 4. 주문서 먼저 1개 소모
			cha.getInventory().count(this, getCount() - 1, true);

			// 5. 강화 시도
			if (Math.random() < chance) {
				// [성공] 강화 수치 1 증가 (사장님 팩 전용 setEnLevel 사용)
				int oldEnchant = targetItem.getEnLevel();
				targetItem.setEnLevel(oldEnchant + 1);
				
				// 성공 효과 및 패킷
				cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), targetItem));
//				cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 197), true); 
				ChattingController.toChatting(cha, name + " 강화에 성공하였습니다. (+" + targetItem.getEnLevel() + ")", Lineage.CHATTING_MODE_MESSAGE);
			} else {
				// [실패] 아이템 증발 (귀걸이 삭제)
				ChattingController.toChatting(cha, name + " 강화에 실패하여 아이템이 소멸되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
//				cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 198), true); 
				cha.getInventory().count(targetItem, 0, true); 
			}
		}
	}
}