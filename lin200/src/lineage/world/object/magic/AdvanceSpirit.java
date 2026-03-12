package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
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

public class AdvanceSpirit extends Magic {

	public AdvanceSpirit(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new AdvanceSpirit(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character c = (Character) o;

			// HP/MP 20% 증가 계산
			c.setBuffAdvanceSpiritHp((int) (c.getMaxHp() * 0.2));
			c.setBuffAdvanceSpiritMp((int) (c.getMaxMp() * 0.2));

			// 실제 수치 적용
			c.setDynamicHp(c.getDynamicHp() + c.getBuffAdvanceSpiritHp());
			c.setDynamicMp(c.getDynamicMp() + c.getBuffAdvanceSpiritMp());

			// 스탯 갱신
			c.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), c));

			// 아이콘 표시 (31번)
			if (o instanceof PcInstance) {
				PcInstance pc = (PcInstance) o;
				SC_BUFFICON_NOTI.on(pc, 31, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			}
		}

		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		// 버프 갱신 시 아이콘 유지 (31번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 31, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		if (o instanceof Character) {
			Character c = (Character) o;

			// HP/MP 원상복구
			c.setDynamicHp(c.getDynamicHp() - c.getBuffAdvanceSpiritHp());
			c.setDynamicMp(c.getDynamicMp() - c.getBuffAdvanceSpiritMp());

			// 종료 멘트 중복 방지 (주석 처리)
			// ChattingController.toChatting(c, "\\fY어드밴스 스피릿 종료",
			// Lineage.CHATTING_MODE_MESSAGE);

			// 스탯 갱신
			c.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), c));

			// [추가] 종료 시 아이콘 삭제 (31번)
			if (c instanceof PcInstance) {
				SC_BUFFICON_NOTI.on((PcInstance) c, 31, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			}
		}
	}

	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() ==
		// Lineage.buff_magic_time_min)
		// ChattingController.toChatting(o, "\\fY어드밴스 스피릿: " + getTime() + "초 후 종료",
		// Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(Character cha, int time) {
		BuffController.append(cha,
				AdvanceSpirit.clone(BuffController.getPool(AdvanceSpirit.class), SkillDatabase.find(9, 2), time));
	}

	static public void init(Character cha, Skill skill, int object_id) {
		// 초기화
		object o = null;
		// 타겟 찾기
		if (Lineage.is_advance_spirit_target) {
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

			// [추가] 시전 딜레이 패킷 (안전하게 캐스팅)
			if (cha instanceof PcInstance) {
				SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(1200).send((PcInstance) cha);
			}

			if (SkillController.isMagic(cha, skill, true) && Util.isAreaAttack(cha, o) && Util.isAreaAttack(o, cha)) {
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()),
						true);

				BuffController.append(o, AdvanceSpirit.clone(BuffController.getPool(AdvanceSpirit.class), skill,
						skill.getBuffDuration()));

				// 시작 멘트 중복 방지 (주석 처리)
				// ChattingController.toChatting(o, "어드밴스 스피릿: 최대 HP+20%, 최대 MP+20%",
				// Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}

	/*
	 * 마법주문서에서 사용중
	 */
	static public void onBuff(object o, Skill skill) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);

		BuffController.append(o,
				AdvanceSpirit.clone(BuffController.getPool(AdvanceSpirit.class), skill, skill.getBuffDuration()));
	}

}