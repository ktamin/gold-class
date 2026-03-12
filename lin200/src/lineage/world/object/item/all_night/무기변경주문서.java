package lineage.world.object.item.all_night;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;

public class 무기변경주문서 extends ItemInstance {

    // 1급 / 2급 그룹 정의 (표기명은 서버 아이템 이름과 정확히 맞추세요)
    private static final List<String> GRADE1 = Arrays.asList(
        "진명황의 집행검",
        "사신의 검",
        "가이아의 격노",
        "붉은 그림자의 이도류",
        "수정 결정체 지팡이",
        "바람칼날의 단검"
    );

    private static final List<String> GRADE2 = Arrays.asList(
        "나이트발드의 양손검",
        "포르세의 검",
        "제로스의 지팡이",
        "악몽의 장궁",
        "포효의 이도류"
    );

    static synchronized public ItemInstance clone(ItemInstance item) {
        if (item == null) item = new 무기변경주문서();
        return item;
    }

    @Override
    public void toClick(Character cha, ClientBasePacket cbp) {
        if (cha.getInventory() == null) return;

        ItemInstance src = cha.getInventory().value(cbp.readD());
        if (src == null || !(src instanceof ItemWeaponInstance)) {
            ChattingController.toChatting(cha, "무기에 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }
        if (src.getItem() == null) {
            ChattingController.toChatting(cha, "해당 아이템 정보를 찾을 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        final String srcName = src.getItem().getName();
        List<String> pool = null;

        // 어느 등급(그룹)에 속하는지 판별
        if (containsIgnoreCase(GRADE1, srcName)) {
            pool = new ArrayList<>(GRADE1);
        } else if (containsIgnoreCase(GRADE2, srcName)) {
            pool = new ArrayList<>(GRADE2);
        } else {
            ChattingController.toChatting(cha, "해당 무기에 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        // 자기 자신 제외
        removeIgnoreCase(pool, srcName);
        if (pool.isEmpty()) {
            ChattingController.toChatting(cha, "변경 가능한 대상이 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        // 랜덤 선택
        String dstName = pool.get(Util.random(0, pool.size() - 1));
        Item dstItem = ItemDatabase.find(dstName);
        if (dstItem == null) {
            ChattingController.toChatting(cha, "대상 아이템 정보를 찾을 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        // 새 무기 생성: 기본 속성(축/저주, 강화수치 등) 승계
        ItemInstance dst = ItemDatabase.newInstance(dstItem);
        dst.setObjectId(ServerDatabase.nextItemObjId());
        dst.setBless(src.getBless());
        dst.setEnLevel(src.getEnLevel());
        dst.setDefinite(true);
     // ★ 속성 복사 추가
        dst.setEnFire(src.getEnFire());
        dst.setEnWater(src.getEnWater());
        dst.setEnEarth(src.getEnEarth());
        dst.setEnWind(src.getEnWind());

        // 인벤 적용
        cha.getInventory().append(dst, true);

        // 기존 무기/주문서 소모
        cha.getInventory().count(src, src.getCount() - 1, true);
        cha.getInventory().count(this, getCount() - 1, true);

        // 안내
        ChattingController.toChatting(
            cha,
            String.format("%s 획득하였습니다.", Util.getStringWord(dst.getItem().getName(), "을", "를")),
            Lineage.CHATTING_MODE_MESSAGE
        );
    }

    // ───────────────────────── 헬퍼 ─────────────────────────

    private boolean containsIgnoreCase(List<String> list, String name) {
        if (name == null) return false;
        for (String s : list) {
            if (s != null && s.equalsIgnoreCase(name)) return true;
        }
        return false;
        }

    private void removeIgnoreCase(List<String> list, String name) {
        if (name == null) return;
        for (int i = list.size() - 1; i >= 0; i--) {
            String s = list.get(i);
            if (s != null && s.equalsIgnoreCase(name)) {
                list.remove(i);
                return;
            }
        }
    }
}
