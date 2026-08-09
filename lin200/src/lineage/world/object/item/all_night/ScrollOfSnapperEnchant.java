package lineage.world.object.item.all_night;

import all_night.Lineage_Balance;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class ScrollOfSnapperEnchant extends ItemInstance {

    // ItemDatabase 로드를 위한 복제 메서드
    static synchronized public ItemInstance clone(ItemInstance item) {
        if (item == null)
            item = new ScrollOfSnapperEnchant();
        return item;
    }

    @Override
    public void toClick(Character cha, ClientBasePacket cbp) {
        if (cha.getInventory() != null) {
            ItemInstance targetItem = cha.getInventory().value(cbp.readD());
            if (targetItem == null) return;

            String targetName = targetItem.getItem().getName();
            
            // 1. 스냅퍼 반지인지 검사
            if (!targetName.contains("스냅퍼의 용사 반지") && 
                !targetName.contains("스냅퍼의 체력 반지") && 
                !targetName.contains("스냅퍼의 지혜 반지")) {
                ChattingController.toChatting(cha, "스냅퍼 반지류에만 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }

            // 2. 최대 강화 수치 제한 (장신구 설정값 공유)
            int maxEnchant = Lineage.item_enchant_accessory_max; 
            if (maxEnchant > 0 && targetItem.getEnLevel() >= maxEnchant) {
                ChattingController.toChatting(cha, String.format("장신구는 최대 +%d까지 인챈트 가능합니다.", maxEnchant), Lineage.CHATTING_MODE_MESSAGE);
                return;
            }

            // 3. 단계별 확률 가져오기 (룸티스와 별개의 밸런스값이 있다면 수정 가능)
            double chance = 0;
            int currentEnLevel = targetItem.getEnLevel();
 /*           
            switch (currentEnLevel) {
                case 0: chance = Lineage_Balance.snapper_enchant_prob0; break;
                case 1: chance = Lineage_Balance.snapper_enchant_prob1; break;
                case 2: chance = Lineage_Balance.snapper_enchant_prob2; break;
                case 3: chance = Lineage_Balance.snapper_enchant_prob3; break;
                case 4: chance = Lineage_Balance.snapper_enchant_prob4; break;
                case 5: chance = Lineage_Balance.snapper_enchant_prob5; break;
                case 6: chance = Lineage_Balance.snapper_enchant_prob6; break;
                case 7: chance = Lineage_Balance.snapper_enchant_prob7; break;
                case 8: chance = Lineage_Balance.snapper_enchant_prob8; break;
                case 9: chance = Lineage_Balance.snapper_enchant_prob9; break;
                default: chance = 0; break;
            }
*/
            // 4. 주문서 먼저 1개 소모
            cha.getInventory().count(this, getCount() - 1, true);

            // 5. 강화 시도
            if (Math.random() < chance) {
                // [성공] 강화 수치 1 증가
                int oldEnchant = targetItem.getEnLevel();
                targetItem.setEnLevel(oldEnchant + 1);
                
                // 정보 갱신 패킷 전송
                cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), targetItem));
                ChattingController.toChatting(cha, targetName + " 강화에 성공하였습니다. (+" + targetItem.getEnLevel() + ")", Lineage.CHATTING_MODE_MESSAGE);
            } else {
                // [실패]
                // 현재 사용한 주문서가 "스냅퍼 보호 주문서"인 경우
                if (this.getItem().getName().equalsIgnoreCase("스냅퍼 보호 주문서")) {
                    ChattingController.toChatting(cha, targetName + " 보호주문서로 아이템이 보호되었습니다", Lineage.CHATTING_MODE_MESSAGE);
                    return; 
                } 
                // 일반 주문서(스냅퍼 강화 주문서 등)인 경우
                else {
                    ChattingController.toChatting(cha, targetName + " 강화 실패로 아이템이 증발되었습니다", Lineage.CHATTING_MODE_MESSAGE);
                    cha.getInventory().count(targetItem, 0, true); 
                }
            }
        }
    }
}