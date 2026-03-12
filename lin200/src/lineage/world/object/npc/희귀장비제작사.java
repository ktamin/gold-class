package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

public class 희귀장비제작사 extends object {

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
        pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "armorcreate4"));
    }

    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
        if (pc.getInventory() != null) {
            List<CreateItem> createList = new ArrayList<>();
            List<ItemInstance> itemList = new ArrayList<>();

            if (action.equalsIgnoreCase("파멸의 대검")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 30000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "파멸의 대검", 1, 0, 1, 100);    

            } else if (action.equalsIgnoreCase("진 레이피어")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 30000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "진 레이피어", 1, 0, 1, 100); 
                
            } else if (action.equalsIgnoreCase("달의 장궁")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 30000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "달의 장궁", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("흑왕도")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 30000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "흑왕도", 1, 0, 1, 100);    
                
            } else if (action.equalsIgnoreCase("강철 마나의 지팡이")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 30000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "강철 마나의 지팡이", 1, 0, 1, 100);   
          //---------------------------------------------------------------------------
                
            } else if (action.equalsIgnoreCase("완력의 티셔츠")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 10000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "완력의 티셔츠", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("민첩의 티셔츠")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 10000));
                if (!checkItem(pc, createList, itemList)) return;
                
                createItem(pc, createList, itemList, "민첩의 티셔츠", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("지식의 티셔츠")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 10000));
                if (!checkItem(pc, createList, itemList)) return;
                
                createItem(pc, createList, itemList, "지식의 티셔츠", 1, 0, 1, 100);
          //--------------------------------------------------------------------------- 
                               
            } else if (action.equalsIgnoreCase("완력의 부츠")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 10000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "완력의 부츠", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("민첩의 부츠")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 10000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "민첩의 부츠", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("지식의 부츠")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 10000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "지식의 부츠", 1, 0, 1, 100);   
         //------------------------------------------------------------------------------     
                
            } else if (action.equalsIgnoreCase("고대 투사의 가더")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 10000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "고대 투사의 가더", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("고대 명궁의 가더")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 10000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "고대 명궁의 가더", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("마법사의 가더")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 10000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "마법사의 가더", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("마나 수정구")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 10000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "마나 수정구", 1, 0, 1, 100);
          //--------------------------------------------------------------------------------  
                
            } else if (action.equalsIgnoreCase("완력의 목걸이")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 20000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "완력의 목걸이", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("민첩의 목걸이")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 20000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "민첩의 목걸이", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("지식의 목걸이")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 20000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "지식의 목걸이", 1, 0, 1, 100);
          //--------------------------------------------------------------------------------
                
            } else if (action.equalsIgnoreCase("완력의 반지")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 20000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "완력의 반지", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("민첩의 반지")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 20000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "민첩의 반지", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("지식의 반지")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 20000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "지식의 반지", 1, 0, 1, 100);
          //--------------------------------------------------------------------------------    
                
            } else if (action.equalsIgnoreCase("완력의 벨트")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 20000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "완력의 벨트", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("민첩의 벨트")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 20000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "민첩의 벨트", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("지식의 벨트")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 20000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "지식의 벨트", 1, 0, 1, 100);
          //--------------------------------------------------------------------------------      
                
            } else if (action.equalsIgnoreCase("노예의 귀걸이")) {
                createList.add(new CreateItem("희귀 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 20000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "노예의 귀걸이", 1, 0, 1, 100);

            }
        }
    }

    public boolean checkItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList) {
        boolean allItemsAvailable = true;
        StringBuilder missingItems = new StringBuilder("부족한 재료: ");

        for (CreateItem list : createList) {
            boolean found = false;
            for (ItemInstance i : pc.getInventory().getList()) {
                if (i.getItem() != null && i.getItem().getName().equalsIgnoreCase(list.itemName) && 
                    i.getCount() >= list.count && !i.isEquipped()) {
                    itemList.add(i);
                    found = true;
                    break;
                }
            }
            if (!found) {
                allItemsAvailable = false;
                missingItems.append(String.format("%s(%,d) ", list.itemName, list.count));
            }
        }

        if (!allItemsAvailable) {
            ChattingController.toChatting(pc, missingItems.toString(), Lineage.CHATTING_MODE_MESSAGE);
        }

        return allItemsAvailable;
    }

    public void createItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList, String createItemName, int bless, int enchant, int count, int successRate) {
        Random random = new Random();
        boolean isSuccess = random.nextInt(100) < successRate;

        // 재료 차감
        for (CreateItem list : createList) {
            for (ItemInstance item : itemList) {
                if (item != null && item.getItem() != null && list.itemName.equalsIgnoreCase(item.getItem().getName())) {
                    pc.getInventory().count(item, item.getCount() - list.count, true);
                }
            }
        }

        if (!isSuccess) {
            ChattingController.toChatting(pc, String.format("'%s' 제작에 실패하였습니다. 재료가 차감되었습니다.", createItemName), Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        Item i = ItemDatabase.find(createItemName);

        if (i != null) {
            ItemInstance temp = pc.getInventory().find(i.getName(), bless, i.isPiles());

            if (temp == null) {
                temp = ItemDatabase.newInstance(i);
                temp.setObjectId(ServerDatabase.nextItemObjId());
                temp.setBless(bless);
                temp.setEnLevel(enchant);
                temp.setCount(count);
                temp.setDefinite(true);
                pc.getInventory().append(temp, true);
            } else {
                pc.getInventory().count(temp, temp.getCount() + count, true);
            }

            ChattingController.toChatting(pc, String.format("'%s' 제작에 성공하였습니다!", createItemName), Lineage.CHATTING_MODE_MESSAGE);
        }
    }
}
