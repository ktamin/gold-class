package lineage.world.object.item.scroll;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryCount;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;

public class ScrollOfRemoveElementalWeapon extends ItemInstance {

    static synchronized public ItemInstance clone(ItemInstance item){
        if(item == null)
            item = new ScrollOfRemoveElementalWeapon();
        return item;
    }

    @Override
    public void toClick(Character cha, ClientBasePacket cbp){
        if(cha.getInventory() != null){
            ItemInstance weapon = cha.getInventory().value(cbp.readD());
            if(weapon != null && weapon instanceof ItemWeaponInstance){
                // 이미 속성(단수)이 모두 0일 때
                if(weapon.getEnWind() == 0 && weapon.getEnEarth() == 0 && weapon.getEnWater() == 0 && weapon.getEnFire() == 0){
                    ChattingController.toChatting(cha, "해당 무기는 속성이 부여되어 있지 않습니다.", 20);
                    return;
                }

                // 장착 중이면 불가
                if(weapon.isEquipped()){
                    ChattingController.toChatting(cha, "무기를 착용상태에서는 속성 제거가 불가능합니다.", 20);
                    return;
                }

                // 속성 모두 제거 (단수 0으로)
                weapon.setEnWind(0);
                weapon.setEnEarth(0);
                weapon.setEnWater(0);
                weapon.setEnFire(0);

                ChattingController.toChatting(cha, "무기에서 속성이 제거되었습니다.", 20);

                // 주문서 차감
                cha.getInventory().count(this, getCount()-1, true);

                // 인벤토리 패킷 갱신
                if(Lineage.server_version<=144){
                    cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), weapon));
                    cha.toSender(S_InventoryCount.clone(BasePacketPooling.getPool(S_InventoryCount.class), weapon));
                }else{
                    cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), weapon));
                }
            }
        }
    }
}
