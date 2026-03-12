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
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class 암석변경주문서 extends ItemInstance {

    static synchronized public ItemInstance clone(ItemInstance item) {
        if (item == null)
            item = new 암석변경주문서();
        return item;
    }

    @Override
    public void toClick(Character cha, ClientBasePacket cbp) {
        if (cha.getInventory() != null) {
            ItemInstance item = cha.getInventory().value(cbp.readD());
            

            if (item != null && item instanceof ItemArmorInstance) {
                if (item.getEnLevel() < 5) {
                    ChattingController.toChatting(cha, "+5 이상 아이템만 변경 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                    return;
                }

                List<String> types = new ArrayList<String>();
                types.add("장갑");
                types.add("부츠");
                types.add("망토");

                for (String type : types) {
                    if (item.getItem().getName().startsWith("고대 암석의") && item.getItem().getName().contains(type)) {
                        String newName = item.getItem().getName().replace("고대 암석의", "고대 마물의").trim();

                        Item tempItem = ItemDatabase.find(newName);
                        if (tempItem != null) {
                            ItemInstance newItem = ItemDatabase.newInstance(tempItem);
                            newItem.setObjectId(ServerDatabase.nextItemObjId());
                            newItem.setBless(item.getBless());
                            newItem.setEnLevel(item.getEnLevel());
                            newItem.setDefinite(true);

                            cha.getInventory().append(newItem, true);
                            cha.getInventory().count(item, item.getCount() - 1, true);
                            cha.getInventory().count(this, getCount() - 1, true);

                            ChattingController.toChatting(cha, String.format("%s 획득하였습니다.", newItem.getItem().getName()), Lineage.CHATTING_MODE_MESSAGE);
                            return;
                        }
                    }
                }
                ChattingController.toChatting(cha, "해당 아이템은 암석 계열만 사용할 수 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
            } else {
                ChattingController.toChatting(cha, "방어구 아이템에만 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
            }
        }
    }
}
