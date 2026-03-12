package lineage.world.object.item.scroll;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import all_night.Lineage_Balance;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;

public class 발라카스의숨결 extends ItemInstance {

    // 대상 무기 이름(서버 DB 표기명과 일치하도록 유지)
    private static final Set<String> ALLOWED = new HashSet<>(Arrays.asList(
        "진명황의 집행검",
        "사신의 검",
        "붉은 그림자의 이도류",
        "가이아의 격노",        // DB가 '격노'로 되어 있으면 이것도 허용
        "수정 결정체 지팡이"
    ));

    // 기본 확률표(%) : cur +N → +N+1 (0..9)
    private static final double[] DEFAULT_RATE_PERCENT = {
        70, 60, 50, 40, 30, 20, 15, 10, 7, 5
    };

    static synchronized public ItemInstance clone(ItemInstance item){
        if(item == null)
            item = new 발라카스의숨결();
        return item;
    }

    @Override
    public void toClick(Character cha, ClientBasePacket cbp){
        if (cha.getInventory() == null) return;

        // 대상 아이템 (선택)
        ItemInstance target = cha.getInventory().value(cbp.readD());
        if (target == null || !(target instanceof ItemWeaponInstance)) {
            ChattingController.toChatting(cha, "집행급 무기에만 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }
        if (target.getItem() == null) {
            ChattingController.toChatting(cha, "대상 아이템 정보를 찾을 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        final String name = safeName(target);
        if (!isAllowed(name)) {
            ChattingController.toChatting(cha, "발라카스 숨결은 집행급 무기에만 사용이 가능합니다!", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        final int cur = target.getEnLevel();

        // ▶ 최소 사용 강화치(설정값) 체크
        int minEn = Math.max(0, Lineage_Balance.valakas_min_en);
        if (cur < minEn) {
            ChattingController.toChatting(
                cha,
                String.format("+%d 이상부터 사용할 수 있습니다.", minEn),
                Lineage.CHATTING_MODE_MESSAGE
            );
            return;
        }

        // ▶ 최대 +10 제한
        if (cur >= 5) {
            ChattingController.toChatting(cha, "더 이상 강화할 수 없습니다. (최대 + 5)", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        // ▶ 성공 확률 계산 (0..1 정규화)
        double rate = rateForLevel(cur);
        boolean success = Math.random() < rate;

        if (success) {
            target.setEnLevel(cur + 1);
            ChattingController.toChatting(cha,
                String.format("강화에 성공했습니다. (+%d → +%d)", cur, cur+1),
                Lineage.CHATTING_MODE_MESSAGE
            );
        } else {
            ChattingController.toChatting(cha, "강화에 실패 하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
        }

        // 인벤토리 갱신 & 주문서 소모
        cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), target));
        cha.getInventory().count(this, getCount() - 1, true);
    }

    // ───────────────────────── 헬퍼 ─────────────────────────

    private boolean isAllowed(String n) {
        if (n == null) return false;
        for (String a : ALLOWED) {
            if (a.equalsIgnoreCase(n)) return true;
        }
        return false;
    }

    private String safeName(ItemInstance it) {
        try {
            String n = it.getItem().getName();
            return (n == null || n.isEmpty()) ? it.getName() : n;
        } catch (Throwable t) {
            return it.getName();
        }
    }

    /**
     * cur(+N)의 성공 확률 반환 (0.0~1.0).
     * Lineage_Balance.valakas_success_rates[cur] 우선,
     * 없거나 0이면 DEFAULT_RATE_PERCENT[cur] 사용.
     * (퍼센트/실수 모두 허용: >1이면 100으로 나눔)
     */
    private double rateForLevel(int cur) {
        double r = 0.0;
        try {
            if (Lineage_Balance.valakas_success_rates != null &&
                cur >= 0 && cur < Lineage_Balance.valakas_success_rates.length) {
                r = Lineage_Balance.valakas_success_rates[cur];
            }
        } catch (Throwable ignore) {}

        if (r <= 0.0) {
            r = DEFAULT_RATE_PERCENT[Math.max(0, Math.min(cur, DEFAULT_RATE_PERCENT.length - 1))];
        }

        if (r > 1.0) r = r / 100.0;   // 퍼센트 값 보정
        if (r < 0.0) r = 0.0;
        if (r > 1.0) r = 1.0;
        return r;
    }
}
