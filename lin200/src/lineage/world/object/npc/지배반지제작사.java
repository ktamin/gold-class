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

public class 지배반지제작사 extends object {

    // ===== 설정( conf 사용 안 함 ) =====
    private static final double SUCCESS_RATE = 0.05;      // 10%
    private static final int    COST_ADENA   = 100_000_000; // 1억

    // 재료 표현용
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
        // HTML: domringcreate.html 같은 템플릿에 버튼 액션만 맞춰 쓰면 됨
        // (아래 액션 문자열과 동일하게)
        pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "domringcreate"));
    }

    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
        if (pc.getInventory() == null) return;

        // 공통 리스트(매 호출마다 새로 만듦)
        List<CreateItem> needs = new ArrayList<>();
        List<ItemInstance> owns = new ArrayList<>();

        try {
            // ─────────────────────────────────────────────────────
            // 1) 순간이동 지배 반지 (확률 10%)
            //    재료: "이반 조각"x100 + 아데나 1억
            //    성공: "순간이동 지배 반지" 1개
            //    실패: "지배 이반 조각" 1개
            // ─────────────────────────────────────────────────────
            if ("순간이동 지배 반지".equalsIgnoreCase(action)) {
                needs.add(new CreateItem("이반 조각", false, 0, false, 0, 100));
                needs.add(new CreateItem("아데나",  false, 0, false, 0, COST_ADENA));
                checkItem(pc, needs, owns);
                createItemWithChance(
                        pc, needs, new ArrayList<>(), owns,
                        "순간이동 지배 반지",               // 성공 지급
                        1, 0, 1,
                        SUCCESS_RATE,
                        "지배 이반 조각", 1                 // 실패 보상
                );
                return;
            }

            // 1-완제) 지배 이반 조각 10개 + 1억 → 100%
            if ("순간이동 지배 반지 완제".equalsIgnoreCase(action)) {
                needs.add(new CreateItem("지배 이반 조각", false, 0, false, 0, 10));
                needs.add(new CreateItem("아데나",        false, 0, false, 0, COST_ADENA));
                checkItem(pc, needs, owns);
                createItemGuaranteed(pc, needs, owns, "순간이동 지배 반지", 1, 0, 1);
                return;
            }

            // ─────────────────────────────────────────────────────
            // 2) 신화변신 랭킹 반지 (확률 10%)
            //    재료: "변반 조각"x100 + 아데나 1억
            //    성공: "신화변신 지배 반지" 1개
            //    실패: "지배 변반 조각" 1개
            // ─────────────────────────────────────────────────────
            if ("신화변신 지배 반지".equalsIgnoreCase(action)) {
                needs.add(new CreateItem("변반 조각", false, 0, false, 0, 100));
                needs.add(new CreateItem("아데나",  false, 0, false, 0, COST_ADENA));
                checkItem(pc, needs, owns);
                createItemWithChance(
                        pc, needs, new ArrayList<>(), owns,
                        "신화변신 지배 반지",
                        1, 0, 1,
                        SUCCESS_RATE,
                        "지배 변반 조각", 1
                );
                return;
            }

            // 2-완제) 지배 변반 조각 10개 + 1억 → 100%
            if ("신화변신 지배 반지 완제".equalsIgnoreCase(action)) {
                needs.add(new CreateItem("지배 변반 조각", false, 0, false, 0, 10));
                needs.add(new CreateItem("아데나",        false, 0, false, 0, COST_ADENA));
                checkItem(pc, needs, owns);
                createItemGuaranteed(pc, needs, owns, "신화변신 지배 반지", 1, 0, 1);
                return;
            }

            // ─────────────────────────────────────────────────────
            // 3) 전투가호 (확률 10%)
            //    재료: "가호 조각"x100 + 아데나 1억
            //    성공: "전투가호" 1개
            //    실패: "전투가호 조각" 1개
            // ─────────────────────────────────────────────────────
            if ("전투가호".equalsIgnoreCase(action)) {
                needs.add(new CreateItem("가호 조각", false, 0, false, 0, 100));
                needs.add(new CreateItem("아데나",  false, 0, false, 0, COST_ADENA));
                checkItem(pc, needs, owns);
                createItemWithChance(
                        pc, needs, new ArrayList<>(), owns,
                        "전투가호",
                        1, 0, 1,
                        SUCCESS_RATE,
                        "전투가호 조각", 1
                );
                return;
            }

            // 3-완제) 전투가호 조각 10개 + 1억 → 100%
            if ("전투가호 완제".equalsIgnoreCase(action)) {
                needs.add(new CreateItem("전투가호 조각", false, 0, false, 0, 10));
                needs.add(new CreateItem("아데나",      false, 0, false, 0, COST_ADENA));
                checkItem(pc, needs, owns);
                createItemGuaranteed(pc, needs, owns, "전투가호", 1, 0, 1);
                return;
            }

        } catch (Throwable t) {
            ChattingController.toChatting(pc, "제작 처리 중 오류가 발생했습니다.", Lineage.CHATTING_MODE_MESSAGE);
        }
    }

    // ====== 공통 유틸 ======

    // 재료 체크(인벤에서 조건 만족하는 아이템을 모아 owns에 담음)
    private void checkItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList) {
        if (createList == null || itemList == null) return;

        itemList.clear();
        for (CreateItem need : createList) {
            boolean ok = false;
            for (ItemInstance invItem : pc.getInventory().getList()) {
                if (invItem == null || invItem.getItem() == null) continue;
                if (!invItem.getItem().getName().equalsIgnoreCase(need.itemName)) continue;
                if (invItem.isEquipped()) continue;
                if (invItem.getCount() < need.count) continue;

                // 축/인첸 조건 검사
                if (need.isCheckBless && invItem.getBless() != need.bless) continue;
                if (need.isCheckEnchant && invItem.getEnLevel() != need.enchant) continue;

                itemList.add(invItem);
                ok = true;
                break;
            }
            if (!ok) {
                // 부족 메시지
                ChattingController.toChatting(pc,
                        String.format("[%s] 재료 부족: %s(%,d)",
                                getClass().getSimpleName(), need.itemName, need.count),
                        Lineage.CHATTING_MODE_MESSAGE);
                itemList.clear();
                return;
            }
        }
    }

    // 확률 제작: 성공 시 목표 아이템, 실패 시 실패보상 아이템 지급
    private void createItemWithChance(
            PcInstance pc,
            List<CreateItem> needs, List<CreateItem> altNeeds, List<ItemInstance> owns,
            String resultItemName, int bless, int enchant, int count,
            double successRate,
            String failRewardName, int failCount
    ) {
        if (!canConsume(needs, altNeeds, owns)) return;

        // 재료 먼저 소모
        consume(pc, (altNeeds != null && !altNeeds.isEmpty()) ? altNeeds : needs, owns);

        boolean success = Math.random() < successRate;
        if (success) {
            giveItem(pc, resultItemName, bless, enchant, count);
            ChattingController.toChatting(pc,
                    String.format("제작 성공! [%s] 획득", resultItemName),
                    Lineage.CHATTING_MODE_MESSAGE);
        } else {
            // 실패 보상 지급
            giveItem(pc, failRewardName, 0, 0, failCount);
            ChattingController.toChatting(pc,
                    String.format("제작 실패. 보상 [%s](%,d) 획득", failRewardName, failCount),
                    Lineage.CHATTING_MODE_MESSAGE);
        }
    }

    // 보장 제작(100%)
    private void createItemGuaranteed(
            PcInstance pc,
            List<CreateItem> needs, List<ItemInstance> owns,
            String resultItemName, int bless, int enchant, int count
    ) {
        if (!canConsume(needs, null, owns)) return;

        consume(pc, needs, owns);
        giveItem(pc, resultItemName, bless, enchant, count);
        ChattingController.toChatting(pc,
                String.format("제작 성공! [%s] 획득", resultItemName),
                Lineage.CHATTING_MODE_MESSAGE);
    }

    private boolean canConsume(List<CreateItem> needs, List<CreateItem> altNeeds, List<ItemInstance> owns) {
        if ((needs != null && !needs.isEmpty() && owns.size() == needs.size())) return true;
        if (altNeeds != null && !altNeeds.isEmpty() && owns.size() == altNeeds.size()) return true;
        return false;
    }

    private void consume(PcInstance pc, List<CreateItem> needs, List<ItemInstance> owns) {
        for (CreateItem need : needs) {
            for (ItemInstance it : owns) {
                if (it != null && it.getItem() != null && need.itemName.equalsIgnoreCase(it.getItem().getName())) {
                    pc.getInventory().count(it, it.getCount() - need.count, true);
                }
            }
        }
    }

    private void giveItem(PcInstance pc, String name, int bless, int en, int count) {
        Item proto = ItemDatabase.find(name);
        if (proto == null) return;

        ItemInstance stack = pc.getInventory().find(proto.getName(), bless, proto.isPiles());
        if (stack != null && (stack.getBless() != bless || stack.getEnLevel() != en)) {
            stack = null;
        }

        if (stack == null) {
            if (proto.isPiles()) {
                ItemInstance ni = ItemDatabase.newInstance(proto);
                ni.setObjectId(ServerDatabase.nextItemObjId());
                ni.setBless(bless);
                ni.setEnLevel(en);
                ni.setCount(count);
                ni.setDefinite(true);
                pc.getInventory().append(ni, true);
            } else {
                for (int i = 0; i < count; i++) {
                    ItemInstance ni = ItemDatabase.newInstance(proto);
                    ni.setObjectId(ServerDatabase.nextItemObjId());
                    ni.setBless(bless);
                    ni.setEnLevel(en);
                    ni.setDefinite(true);
                    pc.getInventory().append(ni, true);
                }
            }
        } else {
            pc.getInventory().count(stack, stack.getCount() + count, true);
        }
    }
}
