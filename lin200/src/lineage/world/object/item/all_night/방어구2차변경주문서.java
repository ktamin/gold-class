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
import lineage.world.object.instance.ItemArmorInstance;

public class 방어구2차변경주문서 extends ItemInstance {

    // 확정 매핑 (좌: 재료 방어구 → 우: 결과 방어구)
    private static final Map<String, String> FIXED_MAP = new HashMap<>();
    static {
        // 투구류
        FIXED_MAP.put("신성한 마법 방어 투구", "지휘관의 투구");
        FIXED_MAP.put("신성한 엘름의 축복", "머미로드의 왕관");
        FIXED_MAP.put("메로키오르의 모자", "대마법사의 모자");

        // 티셔츠
        FIXED_MAP.put("완력의 티셔츠", "화룡의 티셔츠");
        FIXED_MAP.put("민첩의 티셔츠", "풍룡의 티셔츠");
        FIXED_MAP.put("지식의 티셔츠", "수룡의 티셔츠");

        // 장갑류
        FIXED_MAP.put("수호성의 파워 글로브", "격분의 장갑");
        FIXED_MAP.put("수호성의 활 골무", "머미로드의 장갑");
        FIXED_MAP.put("빛나는 마력의 장갑", "아이리스의 장갑");

        // 망토류
        FIXED_MAP.put("거대 여왕 개미의 금빛 날개", "뱀파이어의 망토");
        FIXED_MAP.put("거대 여왕 개미의 은빛 날개", "뱀파이어의 망토");

        // 부츠류
        FIXED_MAP.put("완력의 부츠", "나이트발드의 부츠");
        FIXED_MAP.put("민첩의 부츠", "뱀파이어의 부츠");
        FIXED_MAP.put("지식의 부츠", "아이리스의 부츠");

        // 가더류
        FIXED_MAP.put("화령의 가더", "쿠거의 가더");
        FIXED_MAP.put("풍령의 가더", "우그누스의 가더");
        FIXED_MAP.put("수령의 가더", "시어의 심안");
    }

    static synchronized public ItemInstance clone(ItemInstance item) {
        if (item == null)
            item = new 방어구2차변경주문서();
        return item;
    }

    @Override
    public void toClick(Character cha, ClientBasePacket cbp) {
        if (cha.getInventory() == null)
            return;

        ItemInstance src = cha.getInventory().value(cbp.readD());

        if (src == null || src.getItem() == null || !(src instanceof ItemArmorInstance)) {
            ChattingController.toChatting(cha, "방어구에만 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        final String srcName = src.getItem().getName();
        final String dstName = FIXED_MAP.get(srcName);

        if (dstName == null) {
            ChattingController.toChatting(cha, "해당 방어구는 변경 대상이 아닙니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        // 강화 수치 체크: +9 이상만 가능
        if (src.getEnLevel() < 9) {
            ChattingController.toChatting(cha, "+9 이상 방어구에만 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
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

        // 💡 [수정된 부분] 축복 상태는 복사하되, 인챈트 수치는 기존 수치에서 -1 하여 적용합니다.
        dst.setBless(src.getBless());
        dst.setEnLevel(src.getEnLevel() - 1);
        dst.setDefinite(true);

        // [로그용 변수 미리 저장] 아이템이 삭제되기 전에 형태 추출 (여기서 -1된 결과가 정상적으로 담깁니다)
        long time = System.currentTimeMillis();
        String timeString = Util.getLocaleString(time, true);
        String charName = cha.getName();
        String oldItemName = Util.getItemNameToString(src, 1);
        String newItemName = Util.getItemNameToString(dst, 1);

        // 인벤토리에 지급
        cha.getInventory().append(dst, true);

        // 기존 재료 방어구 및 주문서 소모
        cha.getInventory().count(src, src.getCount() - 1, true);
        cha.getInventory().count(this, getCount() - 1, true);

        // 안내 메시지
        ChattingController.toChatting(
                cha,
                String.format("%s 획득하였습니다.", Util.getStringWord(dst.getItem().getName(), "을", "를")),
                Lineage.CHATTING_MODE_MESSAGE);

        // [매니저 창 로그 출력]
        final String logMessage = String.format("[%s] [변경 성공]\t [캐릭터: %s]\t [기존: %s]\t [결과: %s]",
                timeString, charName, oldItemName, newItemName);

        lineage.gui.GuiMain.display.asyncExec(new Runnable() {
            public void run() {
                lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(logMessage);
            }
        });
    }
}