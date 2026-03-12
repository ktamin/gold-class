package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.Skill;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class Eruption {

	/**
	 * 사용자 용
	 * 
	 * @param cha
	 * @param skill
	 * @param object_id
	 */
	static public void init(Character cha, Skill skill, int object_id) {
		// 타겟 찾기
		object o = cha.findInsideList(object_id);
		if (o != null && !Util.isAreaAttack(cha, o)) {
			return; // 벽 뒤면 무조건 정지
		}
		if (o != null && Util.isDistance(cha, o, 8) && SkillController.isMagic(cha, skill, true))
			EnergyBolt.toBuff(cha, o, skill, Lineage.GFX_MODE_SPELL_DIRECTION, skill.getCastGfx(), 0);
		SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(1100).send((PcInstance) cha);
	}

}
