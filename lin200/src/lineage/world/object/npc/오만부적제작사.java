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

public class 오만부적제작사 extends object {

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
        pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "orimCreate"));
    }

    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
        if (pc.getInventory() != null) {
            List<CreateItem> createList = new ArrayList<>();
            List<ItemInstance> itemList = new ArrayList<>();

            if (action.equalsIgnoreCase("오만의 탑 1층 이동 부적")) {
                createList.add(new CreateItem("오만의 탑 1층 이동 주문서", false, 1, true, 0, 100));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 30000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "오만의 탑 1층 이동 부적", 1, 0, 1, 10);

            } else if (action.equalsIgnoreCase("오만의 탑 2층 이동 부적")) {
                createList.add(new CreateItem("오만의 탑 2층 이동 주문서", false, 1, false, 0, 100));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 30000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "오만의 탑 2층 이동 부적", 1, 0, 1, 10);

            } else if (action.equalsIgnoreCase("오만의 탑 3층 이동 부적")) {
                createList.add(new CreateItem("오만의 탑 3층 이동 주문서", false, 1, false, 0, 100));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 30000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "오만의 탑 3층 이동 부적", 1, 0, 1, 10);

            } else if (action.equalsIgnoreCase("오만의 탑 4층 이동 부적")) {
                createList.add(new CreateItem("오만의 탑 4층 이동 주문서", false, 1, false, 0, 100));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 30000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "오만의 탑 4층 이동 부적", 1, 0, 1, 10);

            } else if (action.equalsIgnoreCase("오만의 탑 5층 이동 부적")) {
                createList.add(new CreateItem("오만의 탑 5층 이동 주문서", false, 1, false, 0, 100));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 30000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "오만의 탑 5층 이동 부적", 1, 0, 1, 10);

            } else if (action.equalsIgnoreCase("오만의 탑 6층 이동 부적")) {
                createList.add(new CreateItem("오만의 탑 6층 이동 주문서", false, 1, false, 0, 100));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 30000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "오만의 탑 6층 이동 부적", 1, 0, 1, 10);

            } else if (action.equalsIgnoreCase("오만의 탑 7층 이동 부적")) {
                createList.add(new CreateItem("오만의 탑 7층 이동 주문서", false, 1, false, 0, 100));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 30000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "오만의 탑 7층 이동 부적", 1, 0, 1, 10);

            } else if (action.equalsIgnoreCase("오만의 탑 8층 이동 부적")) {
                createList.add(new CreateItem("오만의 탑 8층 이동 주문서", false, 1, false, 0, 100));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 30000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "오만의 탑 8층 이동 부적", 1, 0, 1, 10);

            } else if (action.equalsIgnoreCase("오만의 탑 9층 이동 부적")) {
                createList.add(new CreateItem("오만의 탑 9층 이동 주문서", false, 1, false, 0, 100));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 30000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "오만의 탑 9층 이동 부적", 1, 0, 1, 10);

            } else if (action.equalsIgnoreCase("오만의 탑 10층 이동 부적")) {
                createList.add(new CreateItem("오만의 탑 10층 이동 주문서", false, 1, false, 0, 100));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 30000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "오만의 탑 10층 이동 부적", 1, 0, 1, 10);

            } else if (action.equalsIgnoreCase("오만의 탑 정상 이동 부적")) {
                createList.add(new CreateItem("오만의 탑 정상 이동 주문서", false, 1, false, 0, 100));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 10000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "오만의 탑 정상 이동 부적", 1, 0, 1, 100);  
                
            } else if (action.equalsIgnoreCase("오만의 탑 환상의 지배 부적")) {
                // [수정된 부분] 1층~10층 부적 + 깃털 50만개
                createList.add(new CreateItem("오만의 탑 1층 이동 부적", false, 1, false, 0, 1));
                createList.add(new CreateItem("오만의 탑 2층 이동 부적", false, 1, false, 0, 1));
                createList.add(new CreateItem("오만의 탑 3층 이동 부적", false, 1, false, 0, 1));
                createList.add(new CreateItem("오만의 탑 4층 이동 부적", false, 1, false, 0, 1));
                createList.add(new CreateItem("오만의 탑 5층 이동 부적", false, 1, false, 0, 1));
                createList.add(new CreateItem("오만의 탑 6층 이동 부적", false, 1, false, 0, 1));
                createList.add(new CreateItem("오만의 탑 7층 이동 부적", false, 1, false, 0, 1));
                createList.add(new CreateItem("오만의 탑 8층 이동 부적", false, 1, false, 0, 1));
                createList.add(new CreateItem("오만의 탑 9층 이동 부적", false, 1, false, 0, 1));
                createList.add(new CreateItem("오만의 탑 10층 이동 부적", false, 1, false, 0, 1));
                
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 500000));
                
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "오만의 탑 환상의 지배 부적", 1, 0, 1, 100);     
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

    public void createItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList,
            String createItemName, int bless, int enchant, int count, int successRate) {
        Random random = new Random();
        boolean isSuccess = random.nextInt(100) < successRate;

        // 재료 차감
        for (CreateItem list : createList) {
            for (ItemInstance item : itemList) {
                if (item != null && item.getItem() != null
                        && list.itemName.equalsIgnoreCase(item.getItem().getName())) {
                    pc.getInventory().count(item, item.getCount() - list.count, true);
                }
            }
        }

        if (!isSuccess) {
            ChattingController.toChatting(pc, String.format("'%s' 제작에 실패하였습니다. 재료가 차감되었습니다.", createItemName),
                    Lineage.CHATTING_MODE_MESSAGE);
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
            ChattingController.toChatting(pc, String.format("'%s' 제작에 성공하였습니다!", createItemName),
                    Lineage.CHATTING_MODE_MESSAGE);
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