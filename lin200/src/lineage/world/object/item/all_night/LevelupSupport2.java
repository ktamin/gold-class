package lineage.world.object.item.all_night;

import lineage.bean.database.Exp;
import lineage.database.ExpDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.RankController; // 💡 RankController import 추가
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class LevelupSupport2 extends ItemInstance {

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

        // 💡 1. 랭킹 시스템에서 최고 레벨을 가져옵니다.
        int topLevel = RankController.rank_top_level;
        
        // 💡 2. 랭킹이 아직 계산되지 않았거나(0), 비정상적일 경우 기존 설정값 사용
        int targetLevel = (topLevel > 0) ? (topLevel - 2) : Lineage.levelup_support2_target;

        // 타겟 레벨이 너무 낮게 설정되지 않도록 방어 (예: 5레벨 미만이면 사용 불가 등)
        if (targetLevel < 5) {
             ChattingController.toChatting(pc, "\\fY아직 랭킹 정보가 충분하지 않아 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
             return;
        }

        if (pc.getLevel() >= targetLevel) {
            ChattingController.toChatting(pc, String.format("\\fY이미 %d레벨 이상입니다. (현재 서버 1위 레벨: %d)", targetLevel, topLevel), Lineage.CHATTING_MODE_MESSAGE);
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