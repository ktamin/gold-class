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

public class 신화장비제작사 extends object {

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
        // html 파일명: armorcreate7.html
        pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "armorcreate7"));
    }

    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
        if (pc.getInventory() != null) {
            List<CreateItem> createList = new ArrayList<>();
            List<ItemInstance> itemList = new ArrayList<>();

            // -----------------------------------------------------
            // [공통 재료 설정]
            // 모든 신화 장비에 기본으로 들어가는 재료
            // -----------------------------------------------------
            // 1. 신화 제작 비법서 1개
            createList.add(new CreateItem("신화 제작 비법서", false, 1, true, 0, 1));
            // 2. 신비한 날개깃털 200,000개
            createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 100000));

            String requestItemName = null;

            // -----------------------------------------------------
            // [제작 요청 아이템 매칭 및 추가 재료 설정]
            // -----------------------------------------------------
            
            // [무기류] : +9 강화된 재료가 추가됨
            if (action.equalsIgnoreCase("진명황의 집행검")) {
                requestItemName = "진명황의 집행검";
                // 추가 재료: +9 나이트 발드의 양손검
                createList.add(new CreateItem("나이트발드의 양손검", false, 1, true, 9, 1));
            } 
            else if (action.equalsIgnoreCase("가이아의 격노")) {
                requestItemName = "가이아의 격노";
                // 추가 재료: +9 악몽의 장궁
                createList.add(new CreateItem("악몽의 장궁", false, 1, true, 9, 1));
            } 
            else if (action.equalsIgnoreCase("사신의 검")) {
                requestItemName = "사신의 검";
                // 추가 재료: +9 포르세의 검
                createList.add(new CreateItem("포르세의 검", false, 1, true, 9, 1));
            } 
            else if (action.equalsIgnoreCase("수정 결정체 지팡이")) {
                requestItemName = "수정 결정체 지팡이";
                // 추가 재료: +9 제로스의 지팡이
                createList.add(new CreateItem("제로스의 지팡이", false, 1, true, 9, 1));
            } 
            else if (action.equalsIgnoreCase("붉은 그림자의 이도류")) {
                requestItemName = "붉은 그림자의 이도류";
                // 추가 재료: +9 파괴의 이도류
                createList.add(new CreateItem("파괴의 이도류", false, 1, true, 9, 1));
            }
            else if (action.equalsIgnoreCase("변신 조종 지배 반지")) {
                requestItemName = "변신 조종 지배 반지";
                // 추가 재료: 변신 조종 반지
                createList.add(new CreateItem("변신 조종 반지", false, 1, false, 0, 1));
            }
            else if (action.equalsIgnoreCase("순간이동 지배 반지")) {
                requestItemName = "순간이동 지배 반지";
                // 추가 재료: 변신 조종 반지
                createList.add(new CreateItem("순간이동 조종 반지", false, 1, false, 0, 1));
            }

            // [갑옷류 - 안타라스] : 공통 재료만 필요
            else if (action.equalsIgnoreCase("안타라스의 완력")) requestItemName = "안타라스의 완력";
            else if (action.equalsIgnoreCase("안타라스의 마력")) requestItemName = "안타라스의 마력";
            else if (action.equalsIgnoreCase("안타라스의 인내력")) requestItemName = "안타라스의 인내력";
            else if (action.equalsIgnoreCase("안타라스의 예지력")) requestItemName = "안타라스의 예지력";

            // [갑옷류 - 파푸리온]
            else if (action.equalsIgnoreCase("파푸리온의 완력")) requestItemName = "파푸리온의 완력";
            else if (action.equalsIgnoreCase("파푸리온의 마력")) requestItemName = "파푸리온의 마력";
            else if (action.equalsIgnoreCase("파푸리온의 인내력")) requestItemName = "파푸리온의 인내력";
            else if (action.equalsIgnoreCase("파푸리온의 예지력")) requestItemName = "파푸리온의 예지력";

            // [갑옷류 - 린드비오르]
            else if (action.equalsIgnoreCase("린드비오르의 완력")) requestItemName = "린드비오르의 완력";
            else if (action.equalsIgnoreCase("린드비오르의 마력")) requestItemName = "린드비오르의 마력";
            else if (action.equalsIgnoreCase("린드비오르의 인내력")) requestItemName = "린드비오르의 인내력";
            else if (action.equalsIgnoreCase("린드비오르의 예지력")) requestItemName = "린드비오르의 예지력";

            // [갑옷류 - 발라카스]
            else if (action.equalsIgnoreCase("발라카스의 완력")) requestItemName = "발라카스의 완력";
            else if (action.equalsIgnoreCase("발라카스의 마력")) requestItemName = "발라카스의 마력";
            else if (action.equalsIgnoreCase("발라카스의 인내력")) requestItemName = "발라카스의 인내력";
            else if (action.equalsIgnoreCase("발라카스의 예지력")) requestItemName = "발라카스의 예지력";


            // -----------------------------------------------------
            // [제작 실행]
            // -----------------------------------------------------
            if (requestItemName != null) {
                if (!checkItem(pc, createList, itemList)) return;
                
                // 성공확률 100%
                createItem(pc, createList, itemList, requestItemName, 1, 0, 1, 100);
            }
        }
    }

    // 재료 확인 메서드
    public boolean checkItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList) {
        boolean allItemsAvailable = true;
        StringBuilder missingItems = new StringBuilder("부족한 재료: ");

        for (CreateItem list : createList) {
            boolean found = false;
            for (ItemInstance i : pc.getInventory().getList()) {
                // 이름 일치 & 수량 확인 & 장착 해제 확인
                if (i.getItem() != null && i.getItem().getName().equalsIgnoreCase(list.itemName) &&
                    i.getCount() >= list.count && !i.isEquipped()) {
                    
                    // 강화 수치 확인 (isCheckEnchant가 true일 때만 확인)
                    if (list.isCheckEnchant) {
                        if (i.getEnLevel() != list.enchant) {
                            continue; // 강화 수치가 다르면 다음 아이템 검색
                        }
                    }
                    
                    itemList.add(i);
                    found = true;
                    break;
                }
            }
            if (!found) {
                allItemsAvailable = false;
                if (list.isCheckEnchant && list.enchant > 0) {
                    missingItems.append(String.format("+%d %s(%,d) ", list.enchant, list.itemName, list.count));
                } else {
                    missingItems.append(String.format("%s(%,d) ", list.itemName, list.count));
                }
            }
        }

        if (!allItemsAvailable) {
            ChattingController.toChatting(pc, missingItems.toString(), Lineage.CHATTING_MODE_MESSAGE);
        }

        return allItemsAvailable;
    }

    // 아이템 생성 및 재료 차감 메서드
    public void createItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList, String createItemName, int bless, int enchant, int count, int successRate) {
        Random random = new Random();
        boolean isSuccess = random.nextInt(100) < successRate;

        // 재료 차감
        for (CreateItem list : createList) {
            for (ItemInstance item : itemList) {
                if (item != null && item.getItem() != null && list.itemName.equalsIgnoreCase(item.getItem().getName())) {
                    // 강화 수치까지 맞는지 더블 체크 (안전장치)
                    if (list.isCheckEnchant && item.getEnLevel() != list.enchant) {
                        continue;
                    }
                    pc.getInventory().count(item, item.getCount() - list.count, true);
                    break; // 해당 재료 찾아서 차감했으면 루프 탈출 (중복 차감 방지)
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