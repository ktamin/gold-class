package lineage.world.object.item.all_night;

import java.util.HashMap;
import java.util.Map;

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

public class 무기2차변경주문서 extends ItemInstance {

    // 확정 매핑 (좌: 재료 무기 → 우: 결과 무기)
    private static final Map<String, String> FIXED_MAP = new HashMap<>();
    static {
        FIXED_MAP.put("나이트발드의 양손검".toLowerCase(), "진명황의 집행검");
        FIXED_MAP.put("포르세의 검".toLowerCase(),         "사신의 검");
        FIXED_MAP.put("악몽의 장궁".toLowerCase(),         "가이아의 격노");
        FIXED_MAP.put("포효의 이도류".toLowerCase(),       "붉은 그림자의 이도류");
        FIXED_MAP.put("제로스의 지팡이".toLowerCase(),     "수정 결정체 지팡이");
    }

    static synchronized public ItemInstance clone(ItemInstance item) {
        if (item == null) item = new 무기2차변경주문서();
        return item;
    }

    @Override
    public void toClick(Character cha, ClientBasePacket cbp) {
        if (cha.getInventory() == null) return;

        ItemInstance src = cha.getInventory().value(cbp.readD());
        if (src == null || src.getItem() == null || !(src instanceof ItemWeaponInstance)) {
            ChattingController.toChatting(cha, "무기에 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        final String srcName = src.getItem().getName();
        final String key = srcName == null ? "" : srcName.toLowerCase();
        final String dstName = FIXED_MAP.get(key);

        if (dstName == null) {
            ChattingController.toChatting(cha, "해당 무기는 변경 대상이 아닙니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        // 강화 수치 체크: +10 이상만 가능
        if (src.getEnLevel() < 10) {
            ChattingController.toChatting(cha, "+10이상 무기에만 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        // 대상 아이템 찾기
        Item dstItem = ItemDatabase.find(dstName);
        if (dstItem == null) {
            ChattingController.toChatting(cha, "대상 아이템 정보를 찾을 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        // 새 인스턴스 생성 및 주요 속성 승계
        ItemInstance dst = ItemDatabase.newInstance(dstItem);
        dst.setObjectId(ServerDatabase.nextItemObjId());
    //    dst.setBless(src.getBless());           // 축복/저주 유지
    //    dst.setEnLevel(src.getEnLevel());       // 강화 수치 유지
        dst.setDefinite(true);

        // 속성(화/수/지/풍) 승계 (무기 속성 사용 중이라면)
   //     try { dst.setEnFire(src.getEnFire()); }   catch (Throwable ignore) {}
   //     try { dst.setEnWater(src.getEnWater()); } catch (Throwable ignore) {}
   //     try { dst.setEnEarth(src.getEnEarth()); } catch (Throwable ignore) {}
   //     try { dst.setEnWind(src.getEnWind()); }   catch (Throwable ignore) {}

        // 인벤토리에 지급
        cha.getInventory().append(dst, true);

        // 기존 재료 무기/주문서 소모
        cha.getInventory().count(src, src.getCount() - 1, true);
        cha.getInventory().count(this, getCount() - 1, true);

        // 안내 메시지
        ChattingController.toChatting(
            cha,
            String.format("%s 획득하였습니다.", Util.getStringWord(dst.getItem().getName(), "을", "를")),
            Lineage.CHATTING_MODE_MESSAGE
        );
    }
}
