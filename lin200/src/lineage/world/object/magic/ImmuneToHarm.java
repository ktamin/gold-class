package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
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
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ImmuneToHarm extends Magic {

	public ImmuneToHarm(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new ImmuneToHarm(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setBuffImmuneToHarm(true);

		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		// [추가] 버프 갱신 및 아이콘 유지 (75번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 75, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		o.setBuffImmuneToHarm(false);

		// 종료 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "\\fY이뮨 투 함 종료",
		// Lineage.CHATTING_MODE_MESSAGE);

		// [추가] 종료 시 아이콘 삭제 (75번)
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 75, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() ==
		// Lineage.buff_magic_time_min)
		// ChattingController.toChatting(o, "\\fY이뮨 투 함: " + getTime() + "초 후 종료",
		// Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(Character cha, Skill skill, int object_id) {
		// 초기화
		object o = null;
		// 세인트 이뮨 아이템 체크
		ItemInstance item = cha.getInventory().find("세인트 이뮨 투함", 0, 1);

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

			// 기존 이뮨 투 함 버프 제거 (갱신을 위해)
			BuffController.remove(o, ImmuneToHarm.class);

			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha,
					Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);

			if ((SkillController.isMagic(cha, skill, true)
					&& SkillController.isFigure(cha, o, skill, false, SkillController.isClan(cha, o))
					|| cha.getGm() > 0)) {
				// 타겟 유효성 검사 (PK 가능 여부 등)
				if (!Util.isAreaAttack(cha, o) && !Util.isAreaAttack(o, cha))
					return;

				// [수정] 안전하게 형변환 검사 후 딜레이 패킷 전송
				if (cha instanceof PcInstance) {
					SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(1200).send((PcInstance) cha);
				}

				// 이펙트 처리 (세인트 이뮨 / 일반 이뮨)
				if (item != null) {
					o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 13547), true);
				} else {
					o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o,
							skill.getCastGfx()), true);
				}

				// 버프 등록
				BuffController.append(o,
						ImmuneToHarm.clone(BuffController.getPool(ImmuneToHarm.class), skill, skill.getBuffDuration()));

				// 멘트 처리 (필요 없으면 주석 처리)
				if (item != null) {
					ChattingController.toChatting(o, "세인트 이뮨 투 함: 대미지의 일정량 추가감소", Lineage.CHATTING_MODE_MESSAGE);
				} else {
					ChattingController.toChatting(o, "이뮨 투 함: 대미지의 일정량 감소", Lineage.CHATTING_MODE_MESSAGE);
				}

			}
		}
	}

	static public void init(Character cha, int time) {
		BuffController.append(cha,
				ImmuneToHarm.clone(BuffController.getPool(ImmuneToHarm.class), SkillDatabase.find(9, 3), time));
	}

	static public void onBuff(object o, Skill skill) {
		onBuff(o, skill, skill.getBuffDuration());
	}

	static public void onBuff(object o, Skill skill, int time) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		BuffController.append(o, ImmuneToHarm.clone(BuffController.getPool(ImmuneToHarm.class), skill, time));
	}
}