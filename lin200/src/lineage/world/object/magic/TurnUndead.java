package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectEffectLocation;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.DamageController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public class TurnUndead {

	static public void init(Character cha, Skill skill, int object_id, int x, int y) {
		// 타겟 찾기
		object o = cha.findInsideList(object_id);
		if (o != null && !Util.isAreaAttack(cha, o)) {
			return; // 벽 뒤면 무조건 정지
		}
		if (o != null) {
			// 모션취하기.
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha,
					Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);

			SC_SKILL_DELAY_NOTI.newInstance()
					.setDurationMs(700)
					.send((PcInstance) cha);

			if (SkillController.isMagic(cha, skill, true) && onBuff(cha, o, skill, x, y))
				return;
		}
		// \f1마법이 무효화되었습니다.
		cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 281));
	}

	/**
	 * 중복코드 방지용.
	 * 
	 * @param cha
	 * @param o
	 * @param skill
	 * @param x
	 * @param y
	 * @return
	 */
	static public boolean onBuff(Character cha, object o, Skill skill, int x, int y) {
		if (o instanceof MonsterInstance && Util.isAreaAttack(cha, o) && Util.isAreaAttack(o, cha)) {
			MonsterInstance mon = (MonsterInstance) o;
			if (mon.getMonster().isUndead() && mon.getMonster().isTurnUndead()
					&& SkillController.isFigure(cha, mon, skill, true, false)) {
				// 데미지 처리.
				DamageController.toDamage(cha, mon, mon.getTotalHp(), Lineage.ATTACK_TYPE_MAGIC);
				// 패킷 처리.
				if (Lineage.server_version > 144)
					mon.toSender(S_ObjectEffectLocation.clone(BasePacketPooling.getPool(S_ObjectEffectLocation.class),
							skill.getCastGfx(), x, y), false);
				else
					mon.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), mon,
							skill.getCastGfx()), false);
			} else {
				// 인식 처리.
				mon.toDamage(cha, 0, Lineage.ATTACK_TYPE_MAGIC);
				// 투망상태 해제
				Detection.onBuff(cha);
				// \f1마법이 무효화되었습니다.
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 281));
				// 턴 언데드 실패시 확률적으로 버서커스 상태
				if (mon.getMonster().isUndead() && mon.getMonster().isTurnUndead()
						&& Util.random(0, 99) < Util.random(1, 100))
					BuffController.append(mon,
							Berserks.clone(BuffController.getPool(Berserks.class), SkillDatabase.find(23), -1));

			}
			return true;
		}
		return false;
	}
}
