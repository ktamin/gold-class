package lineage.world.object.item.potion;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.Blue;

public class BluePotion extends ItemInstance {

    static synchronized public ItemInstance clone(ItemInstance item){
        if(item == null)
            item = new BluePotion();
        return item;
    }

    @Override
    public void toClick(Character cha, ClientBasePacket cbp){
        if (!isClick(cha))
            return;

        // ✅ 효과 이펙트 출력
        cha.toSender(S_ObjectEffect.clone(
            BasePacketPooling.getPool(S_ObjectEffect.class),
            cha,
            getItem().getEffect()
        ), true);

        // ✅ 버프 적용 (기존 시간 무시하고 리셋)
        int time = getItem().getNameIdNumber() == 232 ? 300 : 2400; // 232번 아이템은 5분, 아니면 40분
        Blue.init(cha, time);

        // ✅ 수량 감소
        cha.getInventory().count(this, getCount() - 1, true);
    }
}
