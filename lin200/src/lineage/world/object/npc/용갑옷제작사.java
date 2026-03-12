package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI;
import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 용갑옷제작사 extends object {

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
        pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "armorcreate2"));
    }

    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
        if (pc.getInventory() != null) {
            List<CreateItem> createList = new ArrayList<>();
            List<ItemInstance> itemList = new ArrayList<>();

            if (action.equalsIgnoreCase("발라카스의 완력")) {
//              createList.add(new CreateItem("신화 제작 비법서(방어)", false, 1, false, 0, 1));
                createList.add(new CreateItem("신성한 요정족 판금 갑옷", true, 0, true, 9, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "발라카스의 완력", 1, 5, 1, 100);
                
            } else if (action.equalsIgnoreCase("발라카스의 예지력")) {
//            	createList.add(new CreateItem("신화 제작 비법서(방어)", false, 1, false, 0, 1));
                createList.add(new CreateItem("신성한 요정족 판금 갑옷", true, 0, true, 9, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "발라카스의 예지력", 1, 5, 1, 100);
                
            } else if (action.equalsIgnoreCase("발라카스의 인내력")) {
//            	createList.add(new CreateItem("신화 제작 비법서(방어)", false, 1, false, 0, 1));
                createList.add(new CreateItem("신성한 요정족 판금 갑옷", true, 0, true, 9, 1));              
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "발라카스의 인내력", 1, 5, 1, 100);
                
            } else if (action.equalsIgnoreCase("발라카스의 마력")) {
//            	createList.add(new CreateItem("신화 제작 비법서(방어)", false, 1, false, 0, 1));
                createList.add(new CreateItem("신성한 요정족 판금 갑옷", true, 0, true, 9, 1));               
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "발라카스의 마력", 1, 5, 1, 100);     

            } else if (action.equalsIgnoreCase("안타라스의 완력")) {
//            	createList.add(new CreateItem("신화 제작 비법서(방어)", false, 1, false, 0, 1));
                createList.add(new CreateItem("신성한 요정족 판금 갑옷", true, 0, true, 9, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "안타라스의 완력", 1, 5, 1, 100);
                
            } else if (action.equalsIgnoreCase("안타라스의 예지력")) {
//            	createList.add(new CreateItem("신화 제작 비법서(방어)", false, 1, false, 0, 1));
                createList.add(new CreateItem("신성한 요정족 판금 갑옷", true, 0, true, 9, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "안타라스의 예지력", 1, 5, 1, 100);
                
            } else if (action.equalsIgnoreCase("안타라스의 인내력")) {
//            	createList.add(new CreateItem("신화 제작 비법서(방어)", false, 1, false, 0, 1));
                createList.add(new CreateItem("신성한 요정족 판금 갑옷", true, 0, true, 9, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "안타라스의 인내력", 1, 5, 1, 100);   

            } else if (action.equalsIgnoreCase("안타라스의 마력")) {
//            	createList.add(new CreateItem("신화 제작 비법서(방어)", false, 1, false, 0, 1));
                createList.add(new CreateItem("신성한 요정족 판금 갑옷", true, 0, true, 9, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "안타라스의 마력", 1, 5, 1, 100);
                
            } else if (action.equalsIgnoreCase("린드비오르의 완력")) {
//            	createList.add(new CreateItem("신화 제작 비법서(방어)", false, 1, false, 0, 1));
                createList.add(new CreateItem("신성한 요정족 판금 갑옷", true, 0, true, 9, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "린드비오르의 완력", 1, 5, 1, 100);
                
            } else if (action.equalsIgnoreCase("린드비오르의 예지력")) {
//            	createList.add(new CreateItem("신화 제작 비법서(방어)", false, 1, false, 0, 1));
                createList.add(new CreateItem("신성한 요정족 판금 갑옷", true, 0, true, 9, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "린드비오르의 예지력", 1, 5, 1, 100);   

            } else if (action.equalsIgnoreCase("린드비오르의 인내력")) {
//            	createList.add(new CreateItem("신화 제작 비법서(방어)", false, 1, false, 0, 1));
                createList.add(new CreateItem("신성한 요정족 판금 갑옷", true, 0, true, 9, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "린드비오르의 인내력", 1, 5, 1, 100);
                
            } else if (action.equalsIgnoreCase("린드비오르의 마력")) {
//            	createList.add(new CreateItem("신화 제작 비법서(방어)", false, 1, false, 0, 1));
                createList.add(new CreateItem("신성한 요정족 판금 갑옷", true, 0, true, 9, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "린드비오르의 마력", 1, 5, 1, 100);
                
            } else if (action.equalsIgnoreCase("파푸리온의 완력")) {
//            	createList.add(new CreateItem("신화 제작 비법서(방어)", false, 1, false, 0, 1));
                createList.add(new CreateItem("신성한 요정족 판금 갑옷", true, 0, true, 9, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "파푸리온의 완력", 1, 5, 1, 100);  
                
            } else if (action.equalsIgnoreCase("파푸리온의 예지력")) {
//            	createList.add(new CreateItem("신화 제작 비법서(방어)", false, 1, false, 0, 1));
                createList.add(new CreateItem("신성한 요정족 판금 갑옷", true, 0, true, 9, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "파푸리온의 예지력", 1, 5, 1, 100);  
                
            } else if (action.equalsIgnoreCase("파푸리온의 인내력")) {
//            	createList.add(new CreateItem("신화 제작 비법서(방어)", false, 1, false, 0, 1));
                createList.add(new CreateItem("신성한 요정족 판금 갑옷", true, 0, true, 9, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "파푸리온의 인내력", 1, 5, 1, 100);  
                
            } else if (action.equalsIgnoreCase("파푸리온의 마력")) {
//            	createList.add(new CreateItem("신화 제작 비법서(방어)", false, 1, false, 0, 1));
                createList.add(new CreateItem("신성한 요정족 판금 갑옷", true, 0, true, 9, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "파푸리온의 마력", 1, 5, 1, 100);       
     
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
                // 인챈트 수치가 필요한 경우, 출력 메시지에 포함
                String itemDisplayName = list.itemName;
                if (list.isCheckEnchant) {
                    itemDisplayName = String.format("+%d %s", list.enchant, list.itemName);
                }

                missingItems.append(String.format("%s(%,d) ", itemDisplayName, list.count));
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
            World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class),
                String.format("\\fR어느 아덴 용사가 %s 제작에 성공하였습니다!", createItemName)));

            // 토스트 전체 메시지 전송
            for (PcInstance p : World.getPcList()) {
                SC_TOAST_NOTI.newInstance()
                    .setMessage(String.format("\\g1* 아이템 제작 [%s] *", createItemName))
                    .setMessage2(String.format("\\fH어느 아덴 용사가 [%s] 제작에 성공하였습니다.", createItemName))
                    .setToastType(SC_TOAST_NOTI.ToastType.HeavyText)
                    .send(p);
            }

    }
}
}