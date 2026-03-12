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
import lineage.world.object.npc.전설장비제작사.CreateItem;

public class 영웅장비제작사 extends object {

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
        pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "armorcreate5"));
    }

    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
        if (pc.getInventory() != null) {
            List<CreateItem> createList = new ArrayList<>();
            List<ItemInstance> itemList = new ArrayList<>();

            if (action.equalsIgnoreCase("나이트발드의 양손검")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "나이트발드의 양손검", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("나이트발드의 양손검1")) {
                createList.add(new CreateItem("파멸의 대검", true, 0, true, 10, 1));
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "나이트발드의 양손검", 1, 9, 1, 100);

            } else if (action.equalsIgnoreCase("나이트발드의 양손검2")) {
                createList.add(new CreateItem("파멸의 대검", true, 0, true, 9, 1));
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "나이트발드의 양손검", 1, 8, 1, 100);

            } else if (action.equalsIgnoreCase("포르세의 검")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "포르세의 검", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("포르세의 검1")) {
                createList.add(new CreateItem("진 레이피어", true, 0, true, 10, 1));
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "포르세의 검", 1, 9, 1, 100);

            } else if (action.equalsIgnoreCase("포르세의 검2")) {
                createList.add(new CreateItem("진 레이피어", true, 0, true, 9, 1));
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, false, 0, 2));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "포르세의 검", 1, 8, 1, 100);

            } else if (action.equalsIgnoreCase("악몽의 장궁")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "악몽의 장궁", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("악몽의 장궁1")) {
                createList.add(new CreateItem("달의 장궁", true, 0, true, 10, 1));
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "악몽의 장궁", 1, 9, 1, 100);

            } else if (action.equalsIgnoreCase("악몽의 장궁2")) {
                createList.add(new CreateItem("달의 장궁", true, 0, true, 9, 1));
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "악몽의 장궁", 1, 8, 1, 100);

            } else if (action.equalsIgnoreCase("포효의 이도류")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "포효의 이도류", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("포효의 이도류1")) {
                createList.add(new CreateItem("흑왕도", true, 0, true, 10, 1));
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "포효의 이도류", 1, 9, 1, 100);

            } else if (action.equalsIgnoreCase("포효의 이도류2")) {
                createList.add(new CreateItem("흑왕도", true, 0, true, 9, 1));
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "포효의 이도류", 1, 8, 1, 100);

            } else if (action.equalsIgnoreCase("제로스의 지팡이")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "제로스의 지팡이", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("제로스의 지팡이1")) {
                createList.add(new CreateItem("흑왕도", true, 0, true, 10, 1));
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "제로스의 지팡이", 1, 9, 1, 100);

            } else if (action.equalsIgnoreCase("제로스의 지팡이2")) {
                createList.add(new CreateItem("흑왕도", true, 0, true, 9, 1));
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "제로스의 지팡이", 1, 8, 1, 100);
                // ---------------------------------------------------------------------------

            } else if (action.equalsIgnoreCase("신성한 마법 방어 투구")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 마법 방어 투구", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 엘름의 축복")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 엘름의 축복", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("메르키오르의 모자")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "메르키오르의 모자", 1, 0, 1, 100);
                // ---------------------------------------------------------------------------
               
                        } else if (action.equalsIgnoreCase("멸마의 판금 갑옷")) {
            	createList.add(new CreateItem("고대의 판금 갑옷", false, 1, true, 0, 1));
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList)) return;

                createItem(pc, createList, itemList, "멸마의 판금 갑옷", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("멸마의 비늘 갑옷")) {
            	createList.add(new CreateItem("고대의 비늘 갑옷", false, 1, true, 0, 1));
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList)) return;
                
                createItem(pc, createList, itemList, "멸마의 비늘 갑옷", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("멸마의 가죽 갑옷")) {
            	createList.add(new CreateItem("고대의 가죽 갑옷", false, 1, true, 0, 1));
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList)) return;
                
                createItem(pc, createList, itemList, "멸마의 가죽 갑옷", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("멸마의 로브")) {
            	createList.add(new CreateItem("고대의 로브", false, 1, true, 0, 1));
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList)) return;
                
                createItem(pc, createList, itemList, "멸마의 로브", 1, 0, 1, 100);
          //---------------------------------------------------------------------------     

            } else if (action.equalsIgnoreCase("거대 여왕 개미의 금빛 날개")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "거대 여왕 개미의 금빛 날개", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("거대 여왕 개미의 은빛 날개")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "거대 여왕 개미의 은빛 날개", 1, 0, 1, 100);
                // ------------------------------------------------------------------------------

            } else if (action.equalsIgnoreCase("수호성의 파워 글로브")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "수호성의 파워 글로브", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("수호성의 활 골무")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "수호성의 활 골무", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("빛나는 마력의 장갑")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "빛나는 마력의 장갑", 1, 0, 1, 100);
                // --------------------------------------------------------------------------------

            } else if (action.equalsIgnoreCase("화령의 가더")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "화령의 가더", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("풍령의 가더")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "풍령의 가더", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("수령의 가더")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "수령의 가더", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 요정족 방패")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 요정족 방패", 1, 0, 1, 100);
                // --------------------------------------------------------------------------------

            } else if (action.equalsIgnoreCase("화령의 귀걸이")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "화령의 귀걸이", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("풍령의 귀걸이")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "풍령의 귀걸이", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("수령의 귀걸이")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "수령의 귀걸이", 1, 0, 1, 100);
                // --------------------------------------------------------------------------------

            } else if (action.equalsIgnoreCase("노예의 귀걸이")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "노예의 귀걸이", 1, 0, 1, 100);

            }
        }
    }

    public boolean checkItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList) {
        boolean allItemsAvailable = true;
        StringBuilder missingItems = new StringBuilder("부족한 재료: ");

        if (createList != null) {
            for (CreateItem list : createList) {
                boolean found = false;
                for (ItemInstance i : pc.getInventory().getList()) {
                    if (i.getItem() != null &&
                            i.getItem().getName().equalsIgnoreCase(list.itemName) &&
                            i.getCount() >= list.count &&
                            !i.isEquipped()) {

                        // 인첸트 확인
                        if (list.isCheckEnchant && i.getEnLevel() != list.enchant) {
                            continue;
                        }
                        // 축복 확인
                        if (list.isCheckBless && i.getBless() != list.bless) {
                            continue;
                        }

                        itemList.add(i);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    allItemsAvailable = false;
                    String prefix = "";
                    if (list.isCheckEnchant && list.enchant > 0)
                        prefix += "+" + list.enchant + " ";
                    if (list.isCheckBless && list.bless == 0)
                        prefix += "축복받은 ";

                    missingItems.append(String.format("%s%s(%,d) ", prefix, list.itemName, list.count));
                }
            }
        }

        if (!allItemsAvailable) {
            ChattingController.toChatting(pc, missingItems.toString(), Lineage.CHATTING_MODE_MESSAGE);
            itemList.clear(); // 실패했으니 담아둔 리스트 초기화
        }

        return allItemsAvailable;
    }

    // ▼▼▼ [핵심] createItem 메서드 구현 (재료 차감 및 아이템 생성) ▼▼▼
    public void createItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList,
            String createItemName, int bless, int enchant, int count, int successRate) {
        Random random = new Random();
        boolean isSuccess = random.nextInt(100) < successRate;

        // 재료 차감 (itemList에는 이미 검증된 아이템들이 들어있음)
        for (CreateItem list : createList) {
            for (ItemInstance item : itemList) {
                if (item != null && item.getItem() != null
                        && list.itemName.equalsIgnoreCase(item.getItem().getName())) {
                    // 사장님 서버 방식인 count() 메서드 사용
                    pc.getInventory().count(item, item.getCount() - list.count, true);
                    break;
                }
            }
        }

        if (!isSuccess) {
            ChattingController.toChatting(pc, String.format("'%s' 제작에 실패하였습니다. 재료가 차감되었습니다.", createItemName),
                    Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        // 아이템 지급
        Item i = ItemDatabase.find(createItemName);
        if (i != null) {
            // 겹치는 아이템(주문서 등) 처리
            if (i.isPiles()) {
                ItemInstance temp = pc.getInventory().find(i.getName(), bless, i.isPiles());
                if (temp != null) {
                    pc.getInventory().count(temp, temp.getCount() + count, true);
                } else {
                    ItemInstance newItem = ItemDatabase.newInstance(i);
                    newItem.setObjectId(ServerDatabase.nextItemObjId());
                    newItem.setBless(bless);
                    newItem.setEnLevel(enchant);
                    newItem.setCount(count);
                    newItem.setDefinite(true);
                    pc.getInventory().append(newItem, true);
                }
            } else {
                // 장비류 처리 (count 만큼 반복 생성하거나 1개 생성)
                ItemInstance newItem = ItemDatabase.newInstance(i);
                newItem.setObjectId(ServerDatabase.nextItemObjId());
                newItem.setBless(bless);
                newItem.setEnLevel(enchant);
                newItem.setCount(count);
                newItem.setDefinite(true);
                pc.getInventory().append(newItem, true);
            }
            ChattingController.toChatting(pc, String.format("'%s' 제작에 성공하였습니다!", createItemName),
                    Lineage.CHATTING_MODE_MESSAGE);
        }
    }
}