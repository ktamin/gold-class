package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.Skill;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.monster.CurseGhoul;

public class RemoveCurse {

	static public void init(Character cha, Skill skill, int object_id) {
		if (cha.getMap() == 807) {
			ChattingController.toChatting(cha, "여기서는 사용 하실 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		// 초기화
		object o = null;
		// 타겟 찾기
		if (object_id == cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList(object_id);
		if (o != null && !Util.isAreaAttack(cha, o)) {
			return; // 벽 뒤면 무조건 정지
		}
		// 처리
		if (o != null) {
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha,
					Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
			SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(1200).send((PcInstance) cha);
			if (SkillController.isMagic(cha, skill, true))
				onBuff(o, skill);
		}
	}

	static public void onBuff(object o, Skill skill) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);

		// 커스: 포이즌
		BuffController.remove(o, CursePoison.class);
		// 커스: 블라인드
		BuffController.remove(o, CurseBlind.class);
		// 커스: 패럴라이즈
		BuffController.remove(o, CurseParalyze.class);
		// 구울 독
		BuffController.remove(o, CurseGhoul.class);
	}
}
