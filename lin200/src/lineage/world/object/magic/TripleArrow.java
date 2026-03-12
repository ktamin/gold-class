package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class TripleArrow {

	static public void init(Character cha, Skill skill, int object_id, int x, int y) {
		// 인벤토리에 화살이 있는지 체크하여 화살 객체를 가져옴
		ItemInstance arrow = cha.getInventory().findArrow();
		ItemInstance weapon = cha.getInventory().getSlot(Lineage.SLOT_WEAPON);
		
		int count = 3;
		
		ItemInstance item = cha.getInventory().find("트리플 애로우(부스트)", 0, 1);

		// 타겟 찾기
		object o = cha.findInsideList( object_id );
		
		// 1. 대상이 없으면 종료
		if (o == null)
			return;

		// ▼▼▼ [핵심 추가] 벽(장애물) 검사 ▼▼▼
		// 대상이 존재하는데, 벽이 가로막고 있다면 즉시 종료
		if (!Util.isAreaAttack(cha, o)) {
			return; 
		}
		// ▲▲▲ [여기까지 추가] ▲▲▲

		// 투망상태 해제 (만약 Detection 오류나면 이 줄을 지우고 Slow.java처럼 직접 해제 코드로 바꾸세요)
		Detection.onBuff(cha);
		
		PcInstance pc = (PcInstance)cha;

		if (weapon != null && weapon.getItem().getType2().equalsIgnoreCase("bow")) {
			// 거리 검사 (벽 검사는 위에서 했으므로 여기서는 거리만 봅니다)
			if (!World.isAttack(cha, o) || !Util.isDistance(cha, o, 11)) 
				return;
				
			// 화살이 없을경우, 사이하 활을 착용하고 있으면 사이하 이펙트로 데미지
			if (arrow == null) {
				if (weapon.getItem().getNameIdNumber() == 1821) {
					if(o!=null && SkillController.isMagic(cha, skill, true)) {
						cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);	
						if (item != null  ) {
							cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 9400), true);	
						}
						for (int i = 0; i < count; i++) {
							cha.toAttack(o, x, y, true, Lineage.ACTION_TRIPLE_ARROW_1, 0, true);
							
							if(i == 0){
								long time = System.currentTimeMillis();
								if(pc.ai_Time > time)
									pc.ai_Time = time + (SpriteFrameDatabase.getGfxFrameTime(pc, pc.getGfx(), Lineage.ACTION_TRIPLE_ARROW_1) * 3) + (pc.ai_Time - time);
								else
									pc.ai_Time = time + (SpriteFrameDatabase.getGfxFrameTime(pc, pc.getGfx(), Lineage.ACTION_TRIPLE_ARROW_1) * 3);
							}
						}
					}
				} else {
					// 화살도 없고 사이하도 아닐 때 (기존 로직 유지)
					if(o!=null && SkillController.isMagic(cha, skill, true)) {
						// ... (기존 코드: 이펙트는 나가지만 데미지 없음) ...
						cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
						if (item != null  ) {
							cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 9400), true);	
						}
						for (int i = 0; i < count; i++) {
							cha.toAttack(o, x, y, true, Lineage.ACTION_TRIPLE_ARROW_1, 0, true);
							
							if(i == 0){
								long time = System.currentTimeMillis();
								if(pc.ai_Time > time)
									pc.ai_Time = time + (SpriteFrameDatabase.getGfxFrameTime(pc, pc.getGfx(), Lineage.ACTION_TRIPLE_ARROW_1) * 3) + (pc.ai_Time - time);
								else
									pc.ai_Time = time + (SpriteFrameDatabase.getGfxFrameTime(pc, pc.getGfx(), Lineage.ACTION_TRIPLE_ARROW_1) * 3);
							}
						}
					}
					ChattingController.toChatting(cha, "화살이 부족합니다. 화살을 무장 하십시오.", Lineage.CHATTING_MODE_MESSAGE);
				}
				return;
			} else {
				// 화살이 있을 때
				if(o!=null && SkillController.isMagic(cha, skill, true)) {
					cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
					if (item != null  ) {
						cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 9400), true);	
					}
					for (int i = 0; i < count; i++) {
						cha.toAttack(o, x, y, true, Lineage.ACTION_TRIPLE_ARROW_1, 0, true);
						
						if(i == 0){
							long time = System.currentTimeMillis();
							if(pc.ai_Time > time)
								pc.ai_Time = time + (SpriteFrameDatabase.getGfxFrameTime(pc, pc.getGfx(), Lineage.ACTION_TRIPLE_ARROW_1) * 3) + (pc.ai_Time - time);
							else
								pc.ai_Time = time + (SpriteFrameDatabase.getGfxFrameTime(pc, pc.getGfx(), Lineage.ACTION_TRIPLE_ARROW_1) * 3);
						}
					}
				}
			}
		} else {
			ChattingController.toChatting(cha, "원거리 무기 착용시 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
		}
	}
}