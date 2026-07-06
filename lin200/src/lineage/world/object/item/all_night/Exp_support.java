package lineage.world.object.item.all_night;

import lineage.bean.database.Exp;
import lineage.database.ExpDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.RankController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class Exp_support extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new Exp_support();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() != null && !cha.isWorldDelete() && !cha.isLock() && !cha.isDead()) {
			
			// 1. 지원 기능 활성화 여부 확인
			if (!Lineage.is_exp_support) {
				ChattingController.toChatting(cha, "\\fY레벨업 지원은 현재 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			// 2. 목표 레벨 계산 (랭커 연동 로직)
			// rankTargetLevel: 현재 서버 1등 레벨 - 갭(3)
			int rankTargetLevel = RankController.rank_top_level - Lineage.exp_support_level_gap;
			
			// 3. 최종 목표 레벨: [랭커 연동 레벨]과 [설정된 기본 시작레벨(52)] 중 높은 값 선택
			// 랭커가 낮을 땐 52레벨로 시작, 랭커가 60렙이 넘어가면 그에 맞춰 레벨업 지원
			int targetLevel = Math.max(rankTargetLevel, Lineage.exp_support_max_level);
			
			// 4. 레벨업 및 경험치 지급
			if (cha.getLevel() < targetLevel) {
				// 스탯 잔여량 확인 (안전장치)
				if (cha.getResetBaseStat() <= 0 && cha.getResetLevelStat() <= 0 && cha.getLevelUpStat() <= 0) {
					
					// 목표 레벨로 경험치 즉시 점프
					Exp e = ExpDatabase.find(targetLevel - 1);
					cha.setExp(e.getBonus());
					
					ChattingController.toChatting(cha, "\\fY레벨업 지원을 받아 " + targetLevel + "레벨이 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
				} else {
					ChattingController.toChatting(cha, "\\fY스탯 능력치를 올리신 후 사용가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			} else {
				// 이미 목표 레벨이거나 그 이상일 경우
				ChattingController.toChatting(cha, String.format("\\fY현재 레벨이 이미 목표치(%d) 이상입니다.", targetLevel), Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}
}