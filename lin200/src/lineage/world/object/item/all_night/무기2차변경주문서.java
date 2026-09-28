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

    // ==================================================
    // 1그룹 (최상위 변환) : 무조건 +10 이상 필요, 결과물은 +0 인챈트로 고정
    // ==================================================
    private static final Map<String, String> TIER2_MAP = new HashMap<>();
    static {
        TIER2_MAP.put("나이트발드의 양손검", "진명황의 집행검");
        TIER2_MAP.put("포르세의 검",         "사신의 검");
        TIER2_MAP.put("악몽의 장궁",           "가이아의 격노");
        TIER2_MAP.put("포효의 이도류",         "붉은 그림자의 이도류");
        TIER2_MAP.put("제로스의 지팡이",       "수정 결정체 지팡이");
    }

    // ==================================================
    // 2그룹 (중간 변환) : +9 이상 필요, 결과물은 기존 인챈트 - 1
    // ==================================================
    private static final Map<String, String> TIER1_MAP = new HashMap<>();
    static {
        // (주의: DB에 '무관의 양손검'으로 되어 있을 수 있어 두 가지 모두 등록해 둡니다)
        TIER1_MAP.put("무관의 양손검",         "나이트발드의 양손검");       
        TIER1_MAP.put("살천의 활",             "악몽의 장궁");
        TIER1_MAP.put("진 레이피어",             "포르세의 검");
        TIER1_MAP.put("흑왕도",                "포효의 이도류");
        TIER1_MAP.put("강철 마나의 지팡이",    "제로스의 지팡이");
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
        String dstName = null;
        int targetEnchant = 0;

        // 1. 최상위(집행급) 변환 그룹인지 확인
        if (TIER2_MAP.containsKey(srcName)) {
            if (src.getEnLevel() < 10) {
                ChattingController.toChatting(cha, "+10이상 무기에만 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            dstName = TIER2_MAP.get(srcName);
            targetEnchant = 0; // 집행급은 +0으로 초기화
        } 
        // 2. 중간(나발급) 변환 그룹인지 확인
        else if (TIER1_MAP.containsKey(srcName)) {
            if (src.getEnLevel() < 9) {
                ChattingController.toChatting(cha, "+9이상 무기에만 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            dstName = TIER1_MAP.get(srcName);
            targetEnchant = src.getEnLevel() - 1; // 기존 인챈트에서 1 차감 (예: +9 -> +8, +10 -> +9)
        } 
        // 3. 둘 다 아닐 경우
        else {
            ChattingController.toChatting(cha, "해당 무기는 변경 대상이 아닙니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        // 대상 아이템 찾기
        Item dstItem = ItemDatabase.find(dstName);
        if (dstItem == null) {
            ChattingController.toChatting(cha, "대상 아이템 정보를 찾을 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        // 새 인스턴스 생성 및 속성 적용
        ItemInstance dst = ItemDatabase.newInstance(dstItem);
        dst.setObjectId(ServerDatabase.nextItemObjId());
        dst.setBless(src.getBless()); // 축복/저주 유지
        
        if (targetEnchant > 0) {
            dst.setEnLevel(targetEnchant); // 계산된 인챈트 수치 적용
        }
        dst.setDefinite(true);

        // 속성(화/수/지/풍) 승계 (주석 해제 시 적용됨)
        try { dst.setEnFire(src.getEnFire()); }   catch (Throwable ignore) {}
        try { dst.setEnWater(src.getEnWater()); } catch (Throwable ignore) {}
        try { dst.setEnEarth(src.getEnEarth()); } catch (Throwable ignore) {}
        try { dst.setEnWind(src.getEnWind()); }   catch (Throwable ignore) {}

        // 로그용 이름 사전 추출
        long time = System.currentTimeMillis();
        String timeString = Util.getLocaleString(time, true);
        String charName = cha.getName();
        String oldItemName = Util.getItemNameToString(src, 1);
        String newItemName = Util.getItemNameToString(dst, 1);

        // 인벤토리에 지급 및 기존 아이템 소모
        cha.getInventory().append(dst, true);
        cha.getInventory().count(src, src.getCount() - 1, true);
        cha.getInventory().count(this, getCount() - 1, true);

        // 인게임 안내 메시지
        ChattingController.toChatting(
            cha,
            String.format("%s 획득하였습니다.", Util.getStringWord(dst.getItem().getName(), "을", "를")),
            Lineage.CHATTING_MODE_MESSAGE
        );

        // 매니저 창 로그 출력 (GUI)
        final String logMessage = String.format("[%s] [무기 변경 성공]\t [캐릭터: %s]\t [기존: %s]\t [결과: %s]", 
                timeString, charName, oldItemName, newItemName);
                
        lineage.gui.GuiMain.display.asyncExec(new Runnable() {
            public void run() {
                lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(logMessage);
            }
        });
    }
}