package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.MonsterSkill;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
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

public class Silence extends Magic {

	public Silence(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new Silence(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setBuffSilence(true);

		// 공격당한거 알리기.
		o.toDamage(cha, 0, Lineage.ATTACK_TYPE_MAGIC);

		// 멘트 출력
		ChattingController.toChatting(o, "\\fY채팅 & 마법 사용 불가", Lineage.CHATTING_MODE_MESSAGE);

		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		// 버프 갱신 및 아이콘 유지 (72번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 72, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		o.setBuffSilence(false);

		// 종료 시 아이콘 삭제 (72번)
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 72, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	// [일반 마법 사용 시 호출]
	static public void init(Character cha, Skill skill, int object_id) {
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
			// 투망상태 해제
			Detection.onBuff(cha);
			// 모션
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha,
					Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);

			// 시전 딜레이 패킷
			if (cha instanceof PcInstance) {
				SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(1000).send((PcInstance) cha);
			}

			if (SkillController.isMagic(cha, skill, true) && SkillController.isFigure(cha, o, skill, true, false))
				onBuff(o, skill, skill.getBuffDuration());
		}
	}

	// [추가됨] 무기 발동 효과(침묵의 검 등)에서 호출할 메서드
	static public void init2(Character cha, Skill skill, int object_id) {
		// 초기화
		object o = null;
		// 타겟 찾기
		if (object_id == cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList(object_id);

		// 처리
		if (o != null) {
			// 투망상태 해제
			Detection.onBuff(cha);

			// 확률 체크 (SkillController.isFigure를 통해 마법 적중 여부 판단)
			// 무기 발동은 보통 모션이나 딜레이 없이 이펙트와 효과만 들어갑니다.
			if (SkillController.isFigure(cha, o, skill, true, false)) {
				onBuff(o, skill, skill.getBuffDuration());
			}
		}
	}

	static public void onBuff(object o, Skill skill, int time) {
		// 이펙트
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		// 적용
		BuffController.append(o, Silence.clone(BuffController.getPool(Silence.class), skill, time));
	}

	/**
	 * 몬스터용
	 */
	static public void init(Character cha, object o, MonsterSkill ms, int action) {
		// 처리
		if (o != null) {
			Detection.onBuff(cha);

			if (action > 0)
				cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, action), true);

			if (SkillController.isMagic(cha, ms, true) && SkillController.isFigure(cha, o, ms, false, false)) {
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o,
						ms.getCastGfx() < 1 ? ms.getSkill().getCastGfx() : ms.getCastGfx()), true);
				BuffController.append(o, Silence.clone(BuffController.getPool(Silence.class), ms.getSkill(),
						ms.getBuffDuration() < 1 ? ms.getSkill().getBuffDuration() : ms.getBuffDuration()));
			}
		}
	}
}