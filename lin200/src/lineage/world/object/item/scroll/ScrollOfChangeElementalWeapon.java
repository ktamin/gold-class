package lineage.world.object.item.scroll;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryCount;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.share.Lineage;
import lineage.share.Log;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.PcInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScrollOfChangeElementalWeapon extends ItemInstance {

    static synchronized public ItemInstance clone(ItemInstance item) {
        if (item == null)
            item = new ScrollOfChangeElementalWeapon();
        return item;
    }

    @Override
    public void toClick(Character cha, ClientBasePacket cbp) {
        if (cha.getInventory() == null)
            return;

        // 대상 아이템(무기) 선택
        ItemInstance weapon = cha.getInventory().value(cbp.readD());
        if (!(weapon instanceof ItemWeaponInstance)) {
            ChattingController.toChatting(cha, "속성 변경이 가능한 무기가 아닙니다.", 20);
            return;
        }
        if (!weapon.getItem().isEnchant()) {
            ChattingController.toChatting(cha, "이 아이템에는 속성을 부여/변경할 수 없습니다.", 20);
            return;
        }
        if (weapon.isEquipped()) {
            ChattingController.toChatting(cha, "무기를 착용한 상태에서는 사용할 수 없습니다.", 20);
            return;
        }

        // 현재 부여된 속성 수집
        List<String> curTypes = new ArrayList<>();
        if (weapon.getEnFire()  > 0) curTypes.add("fire");
        if (weapon.getEnWind()  > 0) curTypes.add("wind");
        if (weapon.getEnEarth() > 0) curTypes.add("earth");
        if (weapon.getEnWater() > 0) curTypes.add("water");

        if (curTypes.isEmpty()) {
            ChattingController.toChatting(cha, "이 무기는 속성이 없습니다.", 20);
            return;
        }
        if (curTypes.size() > 1) {
            ChattingController.toChatting(cha, "2개 이상의 속성이 부여된 무기는 사용할 수 없습니다.", 20);
            return;
        }

        // 현재 속성/단수 파악
        String curType = curTypes.get(0);
        int curLevel = 0;
        switch (curType) {
            case "fire":  curLevel = weapon.getEnFire();  break;
            case "wind":  curLevel = weapon.getEnWind();  break;
            case "earth": curLevel = weapon.getEnEarth(); break;
            case "water": curLevel = weapon.getEnWater(); break;
        }

        // 변경 대상 속성 후보(현재 속성 제외)
        List<String> pool = new ArrayList<>(Arrays.asList("fire", "wind", "earth", "water"));
        pool.remove(curType);
        // 랜덤 선택 (Util.random(a,b) 가 양끝 포함이라 가정)
        String newType = pool.get(Util.random(0, pool.size() - 1));

        // 변경 전 표기(로그용)
        String beforeElemName = toKorElem(curType);
        int beforeStep       = curLevel;

        // 모든 속성 0으로 초기화 후 새 속성에 단수 적용
        weapon.setEnFire(0);
        weapon.setEnWind(0);
        weapon.setEnEarth(0);
        weapon.setEnWater(0);

        switch (newType) {
            case "fire":  weapon.setEnFire(curLevel);  break;
            case "wind":  weapon.setEnWind(curLevel);  break;
            case "earth": weapon.setEnEarth(curLevel); break;
            case "water": weapon.setEnWater(curLevel); break;
        }

        // 주문서 1장 소모
        cha.getInventory().count(this, getCount() - 1, true);

        // 인벤토리 갱신
        if (Lineage.server_version <= 144) {
            cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), weapon));
            cha.toSender(S_InventoryCount.clone(BasePacketPooling.getPool(S_InventoryCount.class), weapon));
        } else {
            cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), weapon));
        }

        // 알림
        String afterElemName = toKorElem(newType);
        ChattingController.toChatting(cha,
                String.format("무기 속성이 [%s %d단]에서 [%s %d단]으로 변경되었습니다.",
                        beforeElemName, beforeStep, afterElemName, curLevel), 20);

        // 로그 (매니저창 + 파일 JSON)
        String scrollName = getItem().getName();
        String itemName   = weapon.getName();
        int enLevel       = weapon.getEnLevel(); // +강

        String mgrLine = String.format(
                "계정:%s 캐릭:%s | 주문서:%s | 대상:%s+%d | 이전:%s %d단 -> 이후:%s %d단 | 결과:변경",
                (cha instanceof PcInstance) ? ((PcInstance) cha).getClient().getAccountId() : "-",
                cha.getName(),
                scrollName,
                itemName, enLevel,
                beforeElemName, beforeStep,
                afterElemName, curLevel
        );
        Log.append매니저창("인첸트", mgrLine);

        Log.appendItem(cha,
                "type|element_change",
                "scroll|"+scrollName,
                "item|"+itemName,
                "enchant|"+enLevel,
                "before_element|"+beforeElemName,
                "before_step|"+beforeStep,
                "after_element|"+afterElemName,
                "after_step|"+curLevel
        );
    }

    private String toKorElem(String key) {
        switch (key) {
            case "fire":  return "화령";
            case "wind":  return "풍령";
            case "earth": return "지령";
            case "water": return "수령";
            default:      return "-";
        }
    }
}
