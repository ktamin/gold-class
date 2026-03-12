package lineage.world.object.npc;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 마법주문서합성사 extends object {
    @Override
    public void toTalk(PcInstance pc, ClientBasePacket cbp) {
        pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "scrollCreate"));
    }

    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
        if (pc.getInventory() != null) {
            if (action.equalsIgnoreCase("[축] 무기 마법 주문서")) {
                createItem(pc, "무기 마법 주문서", "무기 마법 주문서", 0, 10, 100, "아데나", 500000, 1);

            } else if (action.equalsIgnoreCase("[저주] 무기 마법 주문서")) {
                createItem(pc, "무기 마법 주문서", "무기 마법 주문서", 2, 10, 100, "아데나", 500000, 1);

            } else if (action.equalsIgnoreCase("[축] 갑옷 마법 주문서")) {
                createItem(pc, "갑옷 마법 주문서", "갑옷 마법 주문서", 0, 10, 100, "아데나", 500000, 1);

            } else if (action.equalsIgnoreCase("[저주] 갑옷 마법 주문서")) {
                createItem(pc, "갑옷 마법 주문서", "갑옷 마법 주문서", 2, 10, 100, "아데나", 500000, 1);

            } else if (action.equalsIgnoreCase("[축] 오림의 장신구 마법 주문서")) {
                createItem(pc, "오림의 장신구 마법 주문서", "오림의 장신구 마법 주문서", 0, 5, 100, "아데나", 1000000, 1);

            } else if (action.equalsIgnoreCase("[축] 오림의 장신구 마법 주문서(각인)")) {
                createItem(pc, "오림의 장신구 마법 주문서(각인)", "오림의 장신구 마법 주문서(각인)", 0, 5, 100, "아데나", 1000000, 1);

            } else if (action.equalsIgnoreCase("[저주] 오림의 장신구 마법 주문서")) {
                createItem(pc, "오림의 장신구 마법 주문서", "오림의 장신구 마법 주문서", 2, 5, 100, "아데나", 1000000, 1);

            } else if (action.equalsIgnoreCase("장인의 무기 마법 주문서")) {
                createItem(pc, "장인의 무기 마법 주문서", "축복 부여 주문서", 1, 300, 100, "아데나", 1000000, 1);

            } else if (action.equalsIgnoreCase("[축] 장인의 무기 마법 주문서")) {
                createItem(pc, "장인의 무기 마법 주문서", "장인의 무기 마법 주문서", 0, 10, 100, "아데나", 1000000, 1);

            } else if (action.equalsIgnoreCase("장인의 갑옷 마법 주문서")) {
                createItem(pc, "장인의 갑옷 마법 주문서", "축복 부여 주문서", 1, 150, 100, "아데나", 1000000, 1);

            } else if (action.equalsIgnoreCase("[축] 장인의 갑옷 마법 주문서")) {
                createItem(pc, "장인의 갑옷 마법 주문서", "장인의 갑옷 마법 주문서", 0, 10, 100, "아데나", 1000000, 1);

            } else if (action.equalsIgnoreCase("[축] 생명의 나뭇잎")) {
                createItem(pc, "생명의 나뭇잎", "생명의 나뭇잎", 0, 5, 100, "아데나", 1000000, 1);

            }
        }
    }

    public void createItem(PcInstance pc, String newItemName, String itemName, int bless, long count, double percent,
            String aden, long adenCount, long createCount) {
        if (pc.getInventory() != null) {
            // 재료 확인
            ItemInstance item1 = null;
            ItemInstance item2 = null;

            for (ItemInstance i : pc.getInventory().getList()) {
                if (i != null && i.getItem() != null && i.getItem().getName().equalsIgnoreCase(itemName)
                        && i.getBless() == 1 && i.getCount() >= count && !i.isEquipped())
                    item1 = i;
                if (i != null && i.getItem() != null && i.getItem().getName().equalsIgnoreCase(aden)
                        && i.getCount() >= adenCount && !i.isEquipped())
                    item2 = i;

                if (item1 != null && item2 != null)
                    break;
            }

            if (item1 == null || item2 == null) {
                StringBuilder missingItems = new StringBuilder("부족한 재료: ");
                if (item1 == null)
                    missingItems.append(String.format("%s(%,d) ", itemName, count));
                if (item2 == null)
                    missingItems.append(String.format("%s(%,d) ", aden, adenCount));

                ChattingController.toChatting(pc, missingItems.toString(), Lineage.CHATTING_MODE_MESSAGE);
                return;
            }

            // 아이템 제작 로직
            Item i = ItemDatabase.find(newItemName);

            if (i != null) {
                if (pc.getGm() > 0 || Math.random() < (percent * 0.01)) {
                    ItemInstance temp = pc.getInventory().find(i.getName(), bless, i.isPiles());

                    if (temp == null) {
                        temp = ItemDatabase.newInstance(i);
                        temp.setObjectId(ServerDatabase.nextItemObjId());
                        temp.setBless(bless);
                        temp.setEnLevel(0);
                        temp.setCount(createCount);
                        temp.setDefinite(true);
                        pc.getInventory().append(temp, true);
                    } else {
                        pc.getInventory().count(temp, temp.getCount() + createCount, true);
                    }

                    ChattingController.toChatting(pc, String.format("'%s' 제작에 성공하였습니다! ", newItemName),
                            Lineage.CHATTING_MODE_MESSAGE);
                } else {
                    ChattingController.toChatting(pc, String.format("'%s' 제작에 실패하였습니다.", newItemName),
                            Lineage.CHATTING_MODE_MESSAGE);
                }

                // 재료 차감
                pc.getInventory().count(item1, item1.getCount() - count, true);
                pc.getInventory().count(item2, item2.getCount() - adenCount, true);
            }
        }
    }
}
