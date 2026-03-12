package lineage.world.object.item.all_night;

import lineage.bean.database.Exp;
import lineage.database.ExpDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class LevelupSupport2 extends ItemInstance {

    // ★ 여기를 외부에서 쉽게 바꿀 수 있게 하자! (예: 95)
    public static final int LEVELUP_TARGET = 60; // 목표 레벨

    static synchronized public ItemInstance clone(ItemInstance item) {
        if (item == null)
            item = new LevelupSupport2();
        return item;
    }

    @Override
    public void toClick(Character cha, ClientBasePacket cbp) {
        if (!(cha instanceof PcInstance) || cha.getInventory() == null || cha.isWorldDelete() || cha.isLock() || cha.isDead()) {
            return;
        }
        PcInstance pc = (PcInstance) cha;

        int targetLevel = Lineage.levelup_support2_target; // 외부 conf에서 불러온 값 사용!

        if (pc.getLevel() >= targetLevel) {
            ChattingController.toChatting(pc, String.format("\\fY이미 %d레벨 이상입니다.", targetLevel), Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        if (pc.getResetBaseStat() > 0 || pc.getResetLevelStat() > 0 || pc.getLevelUpStat() > 0) {
            ChattingController.toChatting(pc, "\\fY스탯 능력치를 올리신 후 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        Exp e = ExpDatabase.find(targetLevel - 1);
        if (e == null) {
            ChattingController.toChatting(pc, "\\fR목표 레벨 경험치를 불러올 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        pc.setLevel(targetLevel);
        pc.setExp(e.getBonus());

        ChattingController.toChatting(pc, String.format("\\fG%d레벨로 상승했습니다!", targetLevel), Lineage.CHATTING_MODE_MESSAGE);

        // 1회용/무한 구분
        if (!getItem().getName().contains("무한")) {
            pc.getInventory().count(this, getCount() - 1, true);
        }
    }
}
