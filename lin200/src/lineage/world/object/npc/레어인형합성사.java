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

public class 레어인형합성사 extends object {

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
        pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "premiummon"));
    }

    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
        if (pc.getInventory() != null) {
            List<CreateItem> createList = new ArrayList<>();
            List<ItemInstance> itemList = new ArrayList<>();

            if (action.equalsIgnoreCase("마법인형: 진 군주")) {
                createList.add(new CreateItem("마법인형: 안타라스", false, 1, false, 0, 1));
                createList.add(new CreateItem("마법인형: 발라카스", false, 1, false, 0, 1));
                createList.add(new CreateItem("아데나", false, 1, false, 0, 100000000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "마법인형: 진 군주", 1, 0, 1, 10);

            } else if (action.equalsIgnoreCase("마법인형: 진 기사")) {
                createList.add(new CreateItem("마법인형: 발라카스", false, 1, false, 0, 1));
                createList.add(new CreateItem("마법인형: 안타라스", false, 1, false, 0, 1));
                createList.add(new CreateItem("아데나", false, 1, false, 0, 100000000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "마법인형: 진 기사", 1, 0, 1, 10);

            } else if (action.equalsIgnoreCase("마법인형: 진 요정")) {
                createList.add(new CreateItem("마법인형: 린드비오르", false, 1, false, 0, 1));
                createList.add(new CreateItem("마법인형: 파푸리온", false, 1, false, 0, 1));
                createList.add(new CreateItem("아데나", false, 1, false, 0, 100000000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "마법인형: 진 요정", 1, 0, 1, 10);

            } else if (action.equalsIgnoreCase("마법인형: 진 마법사")) {
                createList.add(new CreateItem("마법인형: 파푸리온", false, 1, false, 0, 1));
                createList.add(new CreateItem("마법인형: 린드비오르", false, 1, false, 0, 1));
                createList.add(new CreateItem("아데나", false, 1, false, 0, 50000000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "마법인형: 진 마법사", 1, 0, 1, 10);
                
            } else if (action.equalsIgnoreCase("마법인형: 진 다크엘프")) {
                createList.add(new CreateItem("마법인형: 발라카스", false, 1, false, 0, 1));
                createList.add(new CreateItem("마법인형: 린드비오르", false, 1, false, 0, 1));
                createList.add(new CreateItem("아데나", false, 1, false, 0, 50000000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "마법인형: 진 다크엘프", 1, 0, 1, 10);    
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

        if (!isSuccess) {
            // 실패 시: 인형 2개 중 랜덤 1개만 소모 + 비(非)인형 재료는 정상 차감
            List<ItemInstance> dolls = new ArrayList<>();
            for (ItemInstance item : itemList) {
                if (item != null && item.getItem() != null) {
                    String nm = item.getItem().getName();
                    if (nm.contains("마법인형: 안타라스")
                     || nm.contains("마법인형: 발라카스")
                     || nm.contains("마법인형: 린드비오르")
                     || nm.contains("마법인형: 파푸리온")) {
                        dolls.add(item);
                    }
                }
            }

            // 1) 인형 랜덤 1개 차감
            ItemInstance loseDoll = null;
            if (!dolls.isEmpty()) {
                loseDoll = dolls.get(random.nextInt(dolls.size()));
                pc.getInventory().count(loseDoll, loseDoll.getCount() - 1, true);
            }

            // 2) 비(非)인형 재료(아데나, 신화 제작 비법서 등) 차감
            for (CreateItem need : createList) {
                // 인형 이름이면 스킵 (이미 랜덤 1개만 소모했으므로)
                if (need.itemName.contains("마법인형: 안타라스")
                 || need.itemName.contains("마법인형: 발라카스")
                 || need.itemName.contains("마법인형: 린드비오르")
                 || need.itemName.contains("마법인형: 파푸리온")) {
                    continue; // 인형은 추가 차감하지 않음
                }

                // 그 외 재료는 정상 차감 (아데나/비법서 등)
                for (ItemInstance have : itemList) {
                    if (have != null && have.getItem() != null
                     && need.itemName.equalsIgnoreCase(have.getItem().getName())) {
                        pc.getInventory().count(have, have.getCount() - need.count, true);
                        break;
                    }
                }
            }

            ChattingController.toChatting(pc, String.format("'%s' 제작에 실패하였습니다. 인형 중 1개가 차감되었습니다.", createItemName), Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        // ★ 성공 시에는 모든 재료 차감
        for (CreateItem list : createList) {
            for (ItemInstance item : itemList) {
                if (item != null && item.getItem() != null && list.itemName.equalsIgnoreCase(item.getItem().getName())) {
                    pc.getInventory().count(item, item.getCount() - list.count, true);
                    break;
                }
            }
        }

        // 아이템 생성
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

            // 성공 시 전체 채팅 메시지 출력
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