package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterStat;
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

public class Berserks extends Magic {

	public Berserks(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new Berserks(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setBuffBerserks(true);
		if (o instanceof Character) {
			Character cha = (Character) o;
			cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 2);
			cha.setDynamicAddHit(cha.getDynamicAddHit() + 8);
			cha.setDynamicAc(cha.getDynamicAc() - 10);

			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));

			// 아이콘(22번) 표시
			if (o instanceof PcInstance) {
				PcInstance pc = (PcInstance) o;
				SC_BUFFICON_NOTI.on(pc, 30, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			}
			// cha.toSender(S_Ext_BuffTime.clone(BasePacketPooling.getPool(S_Ext_BuffTime.class),
			// S_Ext_BuffTime.BUFFID_1889, getTime()));
		}
		toBuffUpdate(o);

	}

	public void toBuffUpdate(object o) {
		// 버프 갱신 시 아이콘 유지
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 30, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		if (o.isWorldDelete())
			return;

		o.setBuffBerserks(false);
		if (o instanceof Character) {
			Character cha = (Character) o;
			cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 2);
			cha.setDynamicAddHit(cha.getDynamicAddHit() - 8);
			cha.setDynamicAc(cha.getDynamicAc() + 10);
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
			ChattingController.toChatting(o, "\\fY버서커스 종료", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	@Override
	public void toBuff(object o) {
		if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
			ChattingController.toChatting(o, "\\fY버서커스: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(Character cha, Skill skill, long object_id) {
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
			SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(700).send((PcInstance) cha);
			// 확인.
			if (Util.isDistance(cha, o, 15) && SkillController.isMagic(cha, skill, true)) {
				if (object_id != cha.getObjectId() && (cha.getClanId() == 0 && cha.getPartyId() == 0)) {
					ChattingController.toChatting(cha, "혈맹원 또는 파티원에게 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}

				if (object_id != cha.getObjectId() && cha.getClanId() > 0 && cha.getClanId() != o.getClanId()) {
					ChattingController.toChatting(cha, "혈맹원 또는 파티원에게 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}

				if (object_id != cha.getObjectId() && cha.getPartyId() > 0 && cha.getPartyId() != o.getPartyId()) {
					ChattingController.toChatting(cha, "혈맹원 또는 파티원에게 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}

				// 기존 버서커스 버프 제거 (중복 방지)
				BuffController.remove(o, Berserks.class);

				// 이팩트 표현.
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()),
						true);
				// 적용.
				BuffController.append(o,
						Berserks.clone(BuffController.getPool(Berserks.class), skill, skill.getBuffDuration()));
				ChattingController.toChatting(o, "버서커스: 근거리 대미지+2, 근거리 명중+8, AC+10, HP회복 불가",
						Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}

}
