package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
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

public class StrikerGale extends Magic {

	public StrikerGale(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new StrikerGale(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		// 1. 상태 설정 (ER 감소 로직은 피격/회피 연산 부분에서 이 플래그를 체크함)
		o.setBuffStrikerGale(true);

		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		// 2. 버프 갱신 및 아이콘 유지 (27번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 27, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		// 3. 상태 해제
		o.setBuffStrikerGale(false);

		// 4. 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 27, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}

		// 종료 멘트 중복 방지 (주석 처리)
		// if (o instanceof PcInstance)
		// ChattingController.toChatting(o, "\\fY스트라이커 게일 종료",
		// Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(Character cha, Skill skill, long object_id) {
		// 초기화
		object o = cha.findInsideList(object_id);
		if (o != null && !Util.isAreaAttack(cha, o)) {
			return; // 벽 뒤면 무조건 정지
		}
		// 처리
		if (o != null) {
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha,
					Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);

			// [수정] 깨진 공백 문자 수정 및 정상 코드로 변경
			if (cha instanceof PcInstance) {
				SC_SKILL_DELAY_NOTI.newInstance()
						.setDurationMs(700)
						.send((PcInstance) cha);
			}

			// 성공 확률 및 거리 체크 (8칸)
			if (SkillController.isMagic(cha, skill, true) && SkillController.isFigure(cha, o, skill, true, false)
					&& Util.isDistance(cha, o, 8)) {
				// 이펙트
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()),
						true);
				// 버프 등록
				BuffController.append(o,
						StrikerGale.clone(BuffController.getPool(StrikerGale.class), skill, skill.getBuffDuration()));

				// 시작 멘트 중복 방지 (주석 처리)
				// ChattingController.toChatting(o, "스트라이커 게일: ER(원거리 회피) 1/3 감소",
				// Lineage.CHATTING_MODE_MESSAGE);
			} else {
				if (!Util.isDistance(cha, o, 6))
					ChattingController.toChatting(cha, "\\fY대상이 너무 멀리 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}

}