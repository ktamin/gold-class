package lineage.world.object.item.scroll;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryCount;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.share.Log;
import lineage.world.object.instance.PcInstance;

public class ScrollOfEnchantElementalWeapon extends ItemInstance {

    // ▶▶ 여기만 채워주면 예외 적용됨: +1→1단, +2→2단…
    private static final String[] SAFE1_NAMES = {
            "진명황의 집행검", "사신의 검", "붉은 그림자의 이도류", "가이아의 격노", "수정 결정체 지팡이"
    };

    static synchronized public ItemInstance clone(ItemInstance item) {
        if (item == null)
            item = new ScrollOfEnchantElementalWeapon();
        return item;
    }

    @Override
    public void toClick(Character cha, ClientBasePacket cbp) {
        if (cha.getInventory() == null)
            return;

        ItemInstance weapon = cha.getInventory().value(cbp.readD());
        if (!(weapon instanceof ItemWeaponInstance))
            return;

        if (!weapon.getItem().isEnchant()) {
            ChattingController.toChatting(cha, "이 아이템에는 속성을 부여할 수 없습니다.", 20);
            return;
        }

        if (weapon.isEquipped()) {
            ChattingController.toChatting(cha, "무기를 착용한 상태에서는 사용할 수 없습니다.", 20);
            return;
        }

        // 이미 최종 단수(최대 단) 체크
        if (weapon.getEnEarth() >= Lineage.danlevel ||
                weapon.getEnWater() >= Lineage.danlevel ||
                weapon.getEnFire() >= Lineage.danlevel ||
                weapon.getEnWind() >= Lineage.danlevel) {
            ChattingController.toChatting(cha, "더 이상 속성 강화가 불가능합니다.", 20);
            return;
        }

        final int scrollNid = getItem().getNameIdNumber();
        final String scrollName = getItem().getName();
        final String itemName = weapon.getName();
        final int itemEnchant = weapon.getEnLevel(); // 현재 +강
        final int safeBase = safeEnchantOf(weapon); // 6(기본) or 1(예외 무기)

        // 로그용 이전 상태
        String beforeElem;
        int beforeStep;
        if (weapon.getEnFire() > 0) {
            beforeElem = "화령";
            beforeStep = weapon.getEnFire();
        } else if (weapon.getEnWater() > 0) {
            beforeElem = "수령";
            beforeStep = weapon.getEnWater();
        } else if (weapon.getEnEarth() > 0) {
            beforeElem = "지령";
            beforeStep = weapon.getEnEarth();
        } else if (weapon.getEnWind() > 0) {
            beforeElem = "풍령";
            beforeStep = weapon.getEnWind();
        } else {
            beforeElem = "-";
            beforeStep = 0;
        }

        boolean handled = false;

        // -------------------------
        // 5725: 풍령
        // -------------------------
        if (scrollNid == 5725) {
            handled = true;

            if (weapon.getEnEarth() > 0 || weapon.getEnWater() > 0 || weapon.getEnFire() > 0) {
                ChattingController.toChatting(cha, "무기에 다른 속성이 이미 부여되어 있습니다.", 20);
                return;
            }

            int curStep = weapon.getEnWind(); // 0~4
            int required = requiredEnchantForNextStep(curStep, safeBase);
            if (itemEnchant < required) {
                ChattingController.toChatting(cha,
                        String.format("+%d 이상이어야 %d단계 속성 부여 가능.", required, curStep + 1), 20);
                return;
            }

            int chance = chanceByStep(curStep);
            int roll = Util.random(1, 10000);
            boolean success = (chance >= roll);

            if (success) {
                weapon.setEnWind(curStep + 1);
                ChattingController.toChatting(cha, "무기에 속성 부여 성공.", 20);
            } else {
                ChattingController.toChatting(cha, "무기에 속성 부여 실패.", 20);
            }

            appendElementLog(cha, scrollName, itemName, itemEnchant,
                    beforeElem, beforeStep,
                    success ? "풍령" : beforeElem,
                    success ? beforeStep + 1 : beforeStep,
                    success, chance, roll);
        }

        // -------------------------
        // 5726: 지령
        // -------------------------
        if (scrollNid == 5726) {
            handled = true;

            if (weapon.getEnWind() > 0 || weapon.getEnWater() > 0 || weapon.getEnFire() > 0) {
                ChattingController.toChatting(cha, "무기에 다른 속성이 이미 부여되어 있습니다.", 20);
                return;
            }

            int curStep = weapon.getEnEarth();
            int required = requiredEnchantForNextStep(curStep, safeBase);
            if (itemEnchant < required) {
                ChattingController.toChatting(cha,
                        String.format("+%d 이상이어야 %d단계 속성 부여 가능.", required, curStep + 1), 20);
                return;
            }

            int chance = chanceByStep(curStep);
            int roll = Util.random(1, 100);
            boolean success = (chance >= roll);

            if (success) {
                weapon.setEnEarth(curStep + 1);
                ChattingController.toChatting(cha, "무기에 속성 부여 성공.", 20);
            } else {
                ChattingController.toChatting(cha, "무기에 속성 부여 실패.", 20);
            }

            appendElementLog(cha, scrollName, itemName, itemEnchant,
                    beforeElem, beforeStep,
                    success ? "지령" : beforeElem,
                    success ? beforeStep + 1 : beforeStep,
                    success, chance, roll);
        }

        // -------------------------
        // 5727: 수령
        // -------------------------
        if (scrollNid == 5727) {
            handled = true;

            if (weapon.getEnWind() > 0 || weapon.getEnEarth() > 0 || weapon.getEnFire() > 0) {
                ChattingController.toChatting(cha, "무기에 다른 속성이 이미 부여되어 있습니다.", 20);
                return;
            }

            int curStep = weapon.getEnWater();
            int required = requiredEnchantForNextStep(curStep, safeBase);
            if (itemEnchant < required) {
                ChattingController.toChatting(cha,
                        String.format("+%d 이상이어야 %d단계 속성 부여 가능.", required, curStep + 1), 20);
                return;
            }

            int chance = chanceByStep(curStep);
            int roll = Util.random(1, 100);
            boolean success = (chance >= roll);

            if (success) {
                weapon.setEnWater(curStep + 1);
                ChattingController.toChatting(cha, "무기에 속성 부여 성공.", 20);
            } else {
                ChattingController.toChatting(cha, "무기에 속성 부여 실패.", 20);
            }

            appendElementLog(cha, scrollName, itemName, itemEnchant,
                    beforeElem, beforeStep,
                    success ? "수령" : beforeElem,
                    success ? beforeStep + 1 : beforeStep,
                    success, chance, roll);
        }

        // -------------------------
        // 5728: 화령
        // -------------------------
        if (scrollNid == 5728) {
            handled = true;

            if (weapon.getEnWind() > 0 || weapon.getEnEarth() > 0 || weapon.getEnWater() > 0) {
                ChattingController.toChatting(cha, "무기에 다른 속성이 이미 부여되어 있습니다.", 20);
                return;
            }

            int curStep = weapon.getEnFire();
            int required = requiredEnchantForNextStep(curStep, safeBase);
            if (itemEnchant < required) {
                ChattingController.toChatting(cha,
                        String.format("+%d 이상이어야 %d단계 속성 부여 가능.", required, curStep + 1), 20);
                return;
            }

            int chance = chanceByStep(curStep);
            int roll = Util.random(1, 100);
            boolean success = (chance >= roll);

            if (success) {
                weapon.setEnFire(curStep + 1);
                ChattingController.toChatting(cha, "무기에 속성 부여 성공.", 20);
            } else {
                ChattingController.toChatting(cha, "무기에 속성 부여 실패.", 20);
            }

            appendElementLog(cha, scrollName, itemName, itemEnchant,
                    beforeElem, beforeStep,
                    success ? "화령" : beforeElem,
                    success ? beforeStep + 1 : beforeStep,
                    success, chance, roll);
        }

        if (!handled) {
            // 다른 주문서가 이 클래스로 들어왔다면 무시
            return;
        }

        // 주문서 1장 소모 및 인벤 갱신
        cha.getInventory().count(this, getCount() - 1, true);
        if (Lineage.server_version <= 144) {
            cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), weapon));
            cha.toSender(S_InventoryCount.clone(BasePacketPooling.getPool(S_InventoryCount.class), weapon));
        } else {
            cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), weapon));
        }
    }

    // =========================================================
    // 헬퍼 메서드들
    // =========================================================

    /** 단계별 성공 확률 반환 (0단→dan1, 1단→dan2, …) */
    private int chanceByStep(int step) {
        switch (step) {
            case 0:
                return Lineage.dan1;
            case 1:
                return Lineage.dan2;
            case 2:
                return Lineage.dan3;
            case 3:
                return Lineage.dan4;
            case 4:
                return Lineage.dan5;
            default:
                return 0;
        }
    }

    /**
     * "다음 단계" 최소 요구 +강
     * - safeBase=6(기본): 1단=+6 … 5단=+10
     * - safeBase=1(예외): 1단=+1 … 5단=+5
     * - 그 외(s): 1단=+s … 5단=+(s+4)
     */
    private int requiredEnchantForNextStep(int currentStep, int safeBase) {
        int next = currentStep + 1; // 1~5
        if (next < 1 || next > 5)
            return Integer.MAX_VALUE;
        return (safeBase - 1) + next;
    }

    /** 안전인첸값 계산: 지정 아이템명은 1(예외), 그 외는 getSafeEnchant() (실패 시 6 폴백) */
    private int safeEnchantOf(ItemInstance weapon) {
        String nm = null;
        try {
            nm = weapon.getItem().getName();
            if (nm == null || nm.isEmpty())
                nm = weapon.getName();
        } catch (Throwable ignore) {
            nm = weapon.getName();
        }

        // 1) 예외 처리: SAFE1_NAMES 안에 있으면 안전 1로 취급
        if (nm != null && matchesAny(nm, SAFE1_NAMES)) {
            return 1;
        }

        // 2) 기본: 아이템에서 안전값 읽기
        try {
            return weapon.getItem().getSafeEnchant();
        } catch (Throwable ignore) {
            // 3) 실패시 폴백 → 기존과 동일하게 안전 6 무기 취급
            return 6;
        }
    }

    private boolean matchesAny(String name, String[] arr) {
        for (String s : arr) {
            if (s != null && s.equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    /**
     * 속성 부여 로그 (매니저창 + 파일)
     */
    private void appendElementLog(Character cha,
            String scrollName,
            String itemName,
            int itemEnchant,
            String beforeElem,
            int beforeStep,
            String afterElem,
            int afterStep,
            boolean success,
            int chance,
            int roll) {

        String line = String.format(
                "계정:%s 캐릭:%s | 주문서:%s | 대상:%s+%d | 이전:%s %d단 -> 이후:%s %d단 | 결과:%s | 확률:%d%% 주사위:%d",
                (cha instanceof PcInstance) ? ((PcInstance) cha).getClient().getAccountId() : "-",
                cha.getName(),
                scrollName,
                itemName, itemEnchant,
                beforeElem, beforeStep,
                afterElem, afterStep,
                success ? "성공" : "실패",
                chance, roll);
        Log.append매니저창("인첸트", line);

        Log.appendItem(cha,
                "type|element_enchant",
                "scroll|" + scrollName,
                "item|" + itemName,
                "enchant|" + itemEnchant,
                "before_element|" + beforeElem,
                "before_step|" + beforeStep,
                "after_element|" + afterElem,
                "after_step|" + afterStep,
                "success|" + success,
                "chance|" + chance,
                "roll|" + roll);
    }
}
