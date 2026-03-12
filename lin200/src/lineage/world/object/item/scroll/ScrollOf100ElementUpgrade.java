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

public class ScrollOf100ElementUpgrade extends ItemInstance {

    static synchronized public ItemInstance clone(ItemInstance item){
        if(item == null)
            item = new ScrollOf100ElementUpgrade();
        return item;
    }

    @Override
    public void toClick(Character cha, ClientBasePacket cbp){
        if (cha.getInventory() == null) return;

        ItemInstance weapon = cha.getInventory().value(cbp.readD());
        if (weapon == null || !(weapon instanceof ItemWeaponInstance)) return;

        int maxDan = Lineage.danlevel;
        int nextDan = 0;
        boolean upgraded = false;

        if (weapon.getEnFire() > 0) {
            if (weapon.getEnFire() >= maxDan) {
                ChattingController.toChatting(cha, "더 이상 강화 불가", 20);
                return;
            }
            weapon.setEnFire(weapon.getEnFire() + 1);
            nextDan = weapon.getEnFire();
            upgraded = true;
        } else if (weapon.getEnWater() > 0) {
            if (weapon.getEnWater() >= maxDan) {
                ChattingController.toChatting(cha, "더 이상 강화 불가", 20);
                return;
            }
            weapon.setEnWater(weapon.getEnWater() + 1);
            nextDan = weapon.getEnWater();
            upgraded = true;
        } else if (weapon.getEnWind() > 0) {
            if (weapon.getEnWind() >= maxDan) {
                ChattingController.toChatting(cha, "더 이상 강화 불가", 20);
                return;
            }
            weapon.setEnWind(weapon.getEnWind() + 1);
            nextDan = weapon.getEnWind();
            upgraded = true;
        } else if (weapon.getEnEarth() > 0) {
            if (weapon.getEnEarth() >= maxDan) {
                ChattingController.toChatting(cha, "더 이상 강화 불가", 20);
                return;
            }
            weapon.setEnEarth(weapon.getEnEarth() + 1);
            nextDan = weapon.getEnEarth();
            upgraded = true;
        } else {
            // 속성 없을 때
            ChattingController.toChatting(cha, "속성 없음", 20);
            return;
        }

        if (upgraded) {
            ChattingController.toChatting(cha, nextDan + "단계 속성 부여 성공", 20);
            cha.getInventory().count(this, getCount() - 1, true);

            if (Lineage.server_version <= 144) {
                cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), weapon));
                cha.toSender(S_InventoryCount.clone(BasePacketPooling.getPool(S_InventoryCount.class), weapon));
            } else {
                cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), weapon));
            }
        }
    }
}
