package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.Skill;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class GreaterHeal {

	static public void init(Character cha, Skill skill, long object_id) {
		// 초기화
		object o = null;
		// 타겟 찾기
		if (Lineage.is_greater_heal_target) {
			if (object_id == cha.getObjectId())
				o = cha;
			else
				o = cha.findInsideList(object_id);
		} else {
			o = cha;
		}
		if (o != null && !Util.isAreaAttack(cha, o)) {
			return; // 벽 뒤면 무조건 정지
		}

		// 처리
		if (o != null) {
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha,
					Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);

			// 아래 처럼 사용하실 스킬자바에서 아래패킷을 보내주시면됩니다.
			SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(1100).send((PcInstance) cha);

			if (SkillController.isMagic(cha, skill, true)) {
				// 적용
				Heal.onBuff(cha, o, skill, skill.getCastGfx(), 0);
			}
		}
	}

}
