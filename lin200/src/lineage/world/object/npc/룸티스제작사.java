package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

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

public class 룸티스제작사 extends object {

    public class CreateItem {
        public String itemName;
        public boolean isCheckBless;
        public int bless;
        public boolean isCheckEnchant;
        public int enchant;
        public int count;

        public CreateItem(String itemName, boolean isCheckBless, int bless, boolean isCheckEnchant, int enchant,
                int count) {
            this.itemName = itemName;
            this.isCheckBless = isCheckBless;
            this.bless = bless;
            this.isCheckEnchant = isCheckEnchant;
            this.enchant = enchant;
            this.count = count;
        }
    }

    @Override
    public void toTalk(PcInstance pc, ClientBasePacket cbp) {
        pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "roomtiscreate"));
    }

    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
        if (pc.getInventory() != null) {
            List<CreateItem> createList = new ArrayList<CreateItem>();
            List<ItemInstance> itemList = new ArrayList<ItemInstance>();

            String materialName = "";
            if (action.startsWith("black_"))
                materialName = "룸티스의 검은빛 귀걸이";
            else if (action.startsWith("red_"))
                materialName = "룸티스의 붉은빛 귀걸이";
            else if (action.startsWith("purple_"))
                materialName = "룸티스의 보랏빛 귀걸이";

            if (!materialName.equals("")) {
                int enchant = Integer.parseInt(action.substring(action.lastIndexOf("_") + 1));

                if (enchant < 3) {
                    ChattingController.toChatting(pc, "합성은 +3 강화부터 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                    return;
                }

                // 결과물 이름: (축)룸티스의 검은빛 귀걸이
                String resultName = "(축)" + materialName;

                // 재료: 일반 상태(bless 1)의 아이템 2개 요구
                createList.add(new CreateItem(materialName, true, 1, true, enchant, 1));
                createList.add(new CreateItem(materialName, true, 1, true, enchant, 1));

                checkItem(pc, createList, itemList);

                // [중요] bless 인자를 1로 전달하여 시스템이 "축복받은"을 접두사로 붙이지 않게 함
                createItem(pc, createList, itemList, resultName, 1, enchant, 1);
            }
        }
    }

    public void checkItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList) {
        if (createList != null && itemList != null) {
            itemList.clear();
            for (CreateItem list : createList) {
                for (ItemInstance i : pc.getInventory().getList()) {
                    if (itemList.contains(i))
                        continue;
                    if (i.getItem() != null && i.getItem().getName().equalsIgnoreCase(list.itemName)
                            && !i.isEquipped()) {
                        if (list.isCheckBless && i.getBless() != list.bless)
                            continue;
                        if (list.isCheckEnchant && i.getEnLevel() != list.enchant)
                            continue;
                        itemList.add(i);
                        break;
                    }
                }
            }
        }
    }

    public void createItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList,
            String createItemName, int bless, int enchant, int count) {
        if (createList.size() > 0 && itemList.size() > 0 && createList.size() == itemList.size()) {

            Item i = ItemDatabase.find(createItemName);

            if (i != null) {
                // 재료 삭제
                while (!itemList.isEmpty()) {
                    ItemInstance material = itemList.remove(0);
                    if (material != null) {
                        pc.getInventory().count(material, 0, true);
                    }
                }

                // 결과물 생성
                for (int idx = 0; idx < count; idx++) {
                    ItemInstance temp = ItemDatabase.newInstance(i);
                    temp.setObjectId(ServerDatabase.nextItemObjId());
                    temp.setBless(bless); // 여기를 1로 세팅함으로써 시스템 접두사 방지
                    temp.setEnLevel(enchant);
                    temp.setDefinite(true);
                    pc.getInventory().append(temp, true);
                }

                // 채팅 메시지에서도 DB 이름(createItemName)이 그대로 나오도록 수정
                ChattingController.toChatting(pc, String.format("'+%d %s' 제작에 성공하였습니다!", enchant, createItemName),
                        Lineage.CHATTING_MODE_MESSAGE);
            } else {
                ChattingController.toChatting(pc, String.format("시스템 에러: DB에서 '%s'을(를) 찾을 수 없습니다.", createItemName),
                        Lineage.CHATTING_MODE_MESSAGE);
            }
        } else {
            ChattingController.toChatting(pc, String.format("+%d 재료 아이템이 부족합니다.", enchant),
                    Lineage.CHATTING_MODE_MESSAGE);
        }
    }
}