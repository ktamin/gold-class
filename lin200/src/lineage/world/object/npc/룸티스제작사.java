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

        public CreateItem(String itemName, boolean isCheckBless, int bless, boolean isCheckEnchant, int enchant, int count) {
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

            String searchName = "";
            if (action.startsWith("black_")) searchName = "룸티스의 검은빛 귀걸이";
            else if (action.startsWith("red_")) searchName = "룸티스의 붉은빛 귀걸이";
            else if (action.startsWith("purple_")) searchName = "룸티스의 보라빛 귀걸이";

            if (!searchName.equals("")) {
                int enchant = Integer.parseInt(action.substring(action.lastIndexOf("_") + 1));
                
                // 장비 아이템은 낱개이므로 리스트에 2번 등록하여 각각 찾게 유도
                createList.add(new CreateItem(searchName, true, 1, true, enchant, 1));
                createList.add(new CreateItem(searchName, true, 1, true, enchant, 1));
                
                checkItem(pc, createList, itemList);
                // 결과물: 축복(0), 해당 인챈트, 1개 생성
                createItem(pc, createList, itemList, searchName, 0, enchant, 1);
            }
        }
    }

    // 재료 체크 (중복 선택 방지 로직 포함)
    public void checkItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList) {
        if (createList != null && itemList != null) {
            itemList.clear();
            for (CreateItem list : createList) {
                for (ItemInstance i : pc.getInventory().getList()) {
                    // 이미 리스트에 담긴 객체는 제외 (중복 방지 핵심)
                    if (itemList.contains(i)) continue;

                    if (i.getItem() != null && i.getItem().getName().equalsIgnoreCase(list.itemName) && !i.isEquipped()) {
                        if (list.isCheckBless && i.getBless() != list.bless) continue;
                        if (list.isCheckEnchant && i.getEnLevel() != list.enchant) continue;
                        
                        itemList.add(i);
                        break; // 한 종류(1개) 찾았으니 다음 리스트로
                    }
                }
            }
        }
    }

    public void createItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList, String createItemName, int bless, int enchant, int count) {
        // 요구한 재료 개수와 찾은 아이템 개수가 정확히 일치할 때만 실행
        if (createList.size() > 0 && itemList.size() > 0 && createList.size() == itemList.size()) {
            
            Item i = ItemDatabase.find(createItemName);
            if (i != null) {
                // [재료 차감 시작] 리스트가 빌 때까지 하나씩 확실히 삭제
                while (!itemList.isEmpty()) {
                    ItemInstance material = itemList.remove(0); // 리스트에서 첫 번째 아이템을 꺼냄
                    if (material != null) {
                        // 수량을 0으로 만들어 인벤토리에서 완전히 제거
                        pc.getInventory().count(material, 0, true); 
                    }
                }

                // [결과물 생성]
                for (int idx = 0; idx < count; idx++) {
                    ItemInstance temp = ItemDatabase.newInstance(i);
                    temp.setObjectId(ServerDatabase.nextItemObjId());
                    temp.setBless(bless);
                    temp.setEnLevel(enchant);
                    temp.setDefinite(true);
                    pc.getInventory().append(temp, true);
                }

                ChattingController.toChatting(pc, String.format("'%s' +%d 축복 제작 완료!", createItemName, enchant), Lineage.CHATTING_MODE_MESSAGE);
            }
        } else {
            ChattingController.toChatting(pc, String.format("[%s] 재료가 부족합니다. (동일 강화 2개 필요)", createItemName), Lineage.CHATTING_MODE_MESSAGE);
        }
    }
}