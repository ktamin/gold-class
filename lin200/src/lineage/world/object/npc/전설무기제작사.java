package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI.ToastType;
import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 전설무기제작사 extends object {

    private static PcInstance pc;

    public class CreateItem {
        public String itemName;
        public boolean isCheckBless;
        public int bless;
        public boolean isCheckEnchant;
        public int enchant;
        public int count;
        public int yitem = 0;

        /**
         * @param itemName       : 재료 아이템 이름
         * @param isCheckBless   : 축여부 체크
         * @param bless          : 축복(0~2)
         * @param isCheckEnchant : 인첸트 체크
         * @param enchant        : 인첸트 레벨
         * @param count          : 필요 수량
         */
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
        pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "weaponCreate1"));
    }

    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
        if (pc.getInventory() == null)
            return;

        List<CreateItem> createList  = new ArrayList<>();
        List<CreateItem> createList2 = new ArrayList<>();
        List<ItemInstance> itemList  = new ArrayList<>();

        if (action.equalsIgnoreCase("앨리스 양손검")) {
            createList.add(new CreateItem("파멸의 대검", true, 0, true, 10, 1));
            createList.add(new CreateItem("신화 제작 비법서", false, 1, false, 0, 1));
            createList.add(new CreateItem("축복 부여 주문서", false, 1, false, 0, 200));
            createList.add(new CreateItem("아데나", false, 1, false, 0, 100000000));
            checkItem(pc, createList, itemList);
            createItem(pc, createList, createList2, itemList, "앨리스 양손검", 1, 0, 1);

        } else if (action.equalsIgnoreCase("앨리스 한손검")) {
            createList.add(new CreateItem("진 레이피어", true, 0, true, 10, 1));
            createList.add(new CreateItem("신화 제작 비법서", false, 1, false, 0, 1));
            createList.add(new CreateItem("축복 부여 주문서", false, 1, false, 0, 200));
            createList.add(new CreateItem("아데나", false, 1, false, 0, 100000000));
            checkItem(pc, createList, itemList);
            createItem(pc, createList, createList2, itemList, "앨리스 한손검", 1, 0, 1);

        } else if (action.equalsIgnoreCase("진명황의 집행검")) {
            createList.add(new CreateItem("나이트발드의 양손검", true, 0, true, 10, 1));
            createList.add(new CreateItem("신화 제작 비법서", false, 1, false, 0, 1));
            createList.add(new CreateItem("축복 부여 주문서", false, 1, false, 0, 300));
            createList.add(new CreateItem("아데나", false, 1, false, 0, 100000000));
            checkItem(pc, createList, itemList);
            createItem(pc, createList, createList2, itemList, "진명황의 집행검", 1, 0, 1);

        } else if (action.equalsIgnoreCase("사신의 검")) {
            createList.add(new CreateItem("포르세의 검", true, 0, true, 10, 1));
            createList.add(new CreateItem("신화 제작 비법서", false, 1, false, 0, 1));
            createList.add(new CreateItem("축복 부여 주문서", false, 1, false, 0, 300));
            createList.add(new CreateItem("아데나", false, 1, false, 0, 100000000));
            checkItem(pc, createList, itemList);
            createItem(pc, createList, createList2, itemList, "사신의 검", 1, 0, 1);
            
        } else if (action.equalsIgnoreCase("붉은 그림자의 이도류")) {
            createList.add(new CreateItem("흑왕도", true, 0, true, 10, 1));
            createList.add(new CreateItem("신화 제작 비법서", false, 1, false, 0, 1));
            createList.add(new CreateItem("축복 부여 주문서", false, 1, false, 0, 300));
            createList.add(new CreateItem("아데나", false, 1, false, 0, 100000000));
            checkItem(pc, createList, itemList);
            createItem(pc, createList, createList2, itemList, "붉은 그림자의 이도류", 1, 0, 1);  

        } else if (action.equalsIgnoreCase("가이아의 격노")) {
            createList.add(new CreateItem("악몽의 장궁", true, 0, true, 10, 1));
            createList.add(new CreateItem("신화 제작 비법서", false, 1, false, 0, 1));
            createList.add(new CreateItem("축복 부여 주문서", false, 1, false, 0, 300));
            createList.add(new CreateItem("아데나", false, 1, false, 0, 100000000));
            checkItem(pc, createList, itemList);
            createItem(pc, createList, createList2, itemList, "가이아의 격노", 1, 0, 1);

        } else if (action.equalsIgnoreCase("수정 결정체 지팡이")) {
            createList.add(new CreateItem("제로스의 지팡이", true, 0, true, 10, 1));
            createList.add(new CreateItem("신화 제작 비법서", false, 1, false, 0, 1));
            createList.add(new CreateItem("축복 부여 주문서", false, 1, false, 0, 300));
            createList.add(new CreateItem("아데나", false, 1, false, 0, 100000000));
            checkItem(pc, createList, itemList);
            createItem(pc, createList, createList2, itemList, "수정 결정체 지팡이", 1, 0, 1);
        }
    }

    public void checkItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList) {
        if (createList == null || itemList == null)
            return;
        itemList.clear();
        for (CreateItem req : createList) {
            for (ItemInstance i : pc.getInventory().getList()) {
                if (i.getItem() == null) continue;
                if (!i.getItem().getName().equalsIgnoreCase(req.itemName)) continue;
                if (i.getCount() < req.count) continue;
                if (req.isCheckBless && i.getBless() != req.bless) continue;
                if (req.isCheckEnchant && i.getEnLevel() != req.enchant) continue;
                if (i.isEquipped()) continue;
                itemList.add(i);
                break;
            }
        }
    }

    static public void toAskTeamBattle(String time) {
        if (pc != null) {
            pc.toSender(S_MessageYesNo.clone(
                BasePacketPooling.getPool(S_MessageYesNo.class),
                773,
                time
            ));
        }
    }

    public void createItem(
            PcInstance pc,
            List<CreateItem> createList,
            List<CreateItem> createList2,
            List<ItemInstance> itemList,
            String createItemName,
            int bless,
            int enchant,
            int count) {

        // 성공 처리
        if ((createList.size() > 0 && itemList.size() > 0 && createList.size() == itemList.size())
         || (createList2.size() > 0 && itemList.size() > 0 && createList2.size() == itemList.size())) {

            Item i = ItemDatabase.find(createItemName);
            if (i != null) {
                ItemInstance temp = pc.getInventory().find(i.getName(), bless, i.isPiles());
                if (temp != null && (temp.getBless() != bless || temp.getEnLevel() != enchant))
                    temp = null;

                if (temp == null) {
                    // 새로 생성
                    if (i.isPiles()) {
                        temp = ItemDatabase.newInstance(i);
                        temp.setObjectId(ServerDatabase.nextItemObjId());
                        temp.setBless(bless);
                        temp.setEnLevel(enchant);
                        temp.setCount(count);
                        temp.setDefinite(true);
                        pc.getInventory().append(temp, true);
                    } else {
                        for (int idx = 0; idx < count; idx++) {
                            temp = ItemDatabase.newInstance(i);
                            temp.setObjectId(ServerDatabase.nextItemObjId());
                            temp.setBless(bless);
                            temp.setEnLevel(enchant);
                            temp.setDefinite(true);
                            pc.getInventory().append(temp, true);
                        }
                    }
                } else {
                    pc.getInventory().count(temp, temp.getCount() + count, true);
                }

                // 재료 차감
                if (createList2.isEmpty()) {
                    for (CreateItem req : createList) {
                        for (ItemInstance used : itemList) {
                            if (used.getItem() != null
                             && req.itemName.equalsIgnoreCase(used.getItem().getName()))
                                pc.getInventory().count(used, used.getCount() - req.count, true);
                        }
                    }
                } else {
                    for (CreateItem req : createList2) {
                        for (ItemInstance used : itemList) {
                            if (used.getItem() != null
                             && req.itemName.equalsIgnoreCase(used.getItem().getName()))
                                pc.getInventory().count(used, used.getCount() - req.count, true);
                        }
                    }
                }

                // 완료 메시지
                ChattingController.toChatting(
                    pc,
                    String.format("'%s' 제작 완료!", createItemName),
                    Lineage.CHATTING_MODE_MESSAGE
                );

                // 전체 채팅 + 토스트
                World.toSender(S_ObjectChatting.clone(
                    BasePacketPooling.getPool(S_ObjectChatting.class),
                    String.format("\\fR어느 아덴 용사가 %s 제작에 성공하였습니다!", createItemName)
                ));
                String toast1 = String.format("\\g1* 아이템 제작 [ %s ] *", createItemName);
                String toast2 = String.format("\\fH아덴의 어느 용사가 [%s] 제작에 성공하였습니다.", createItemName);
                for (PcInstance online : World.getPcList()) {
                    SC_TOAST_NOTI.newInstance()
                        .setMessage(toast1)
                        .setMessage2(toast2)
                        .setToastType(ToastType.HeavyText)
                        .send(online);
                }
            }

        // 부족 재료 처리
        } else {
            List<CreateItem> required = (createList2.size() > 0) ? createList2 : createList;
            List<String> lacking = new ArrayList<>();

            for (CreateItem req : required) {
                int have = 0;
                for (ItemInstance it : pc.getInventory().getList()) {
                    if (it.getItem() == null) continue;
                    if (!it.getItem().getName().equalsIgnoreCase(req.itemName)) continue;
                    if (req.isCheckBless && it.getBless() != req.bless) continue;
                    if (req.isCheckEnchant && it.getEnLevel() != req.enchant) continue;
                    have += it.getCount();
                }
                if (have < req.count) {
                    String display = (req.isCheckEnchant && req.enchant > 0)
                        ? String.format("+%d%s", req.enchant, req.itemName)
                        : req.itemName;
                    lacking.add(String.format("%s×%,d", display, req.count - have));
                }
            }

            if (!lacking.isEmpty()) {
                String lackMsg = String.join(",", lacking);
                ChattingController.toChatting(
                    pc,
                    String.format("[제작 실패] 부족한 재료: %s", lackMsg),
                    Lineage.CHATTING_MODE_MESSAGE
                );
            } else {
                ChattingController.toChatting(
                    pc,
                    String.format("[%s] 재료가 부족합니다.", createItemName),
                    Lineage.CHATTING_MODE_MESSAGE
                );
            }
        }
    }

}
