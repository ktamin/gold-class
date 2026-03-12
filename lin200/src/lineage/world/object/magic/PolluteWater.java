package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
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

public class PolluteWater extends Magic {

	public PolluteWater(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new PolluteWater(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		// 1. 상태 설정 (회복량 감소 로직은 힐/물약 처리 부분에서 이 플래그를 체크함)
		o.setBuffPolluteWater(true);

		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		// 2. 버프 갱신 및 아이콘 유지 (26번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 26, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		// 3. 상태 해제
		o.setBuffPolluteWater(false);

		// 4. 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 26, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}

		// 종료 멘트 중복 방지 (주석 처리)
		// if (o instanceof PcInstance)
		// ChattingController.toChatting(o, "\\fY폴루트 워터 종료",
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

			// 거리 5칸 이내, 마법 성공 여부 확인
			if (SkillController.isMagic(cha, skill, true) && SkillController.isFigure(cha, o, skill, true, false)
					&& Util.isDistance(cha, o, 5)) {
				// 이펙트
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()),
						true);
				// 버프 등록
				BuffController.append(o,
						PolluteWater.clone(BuffController.getPool(PolluteWater.class), skill, skill.getBuffDuration()));

				// 시작 멘트 중복 방지 (주석 처리)
				// ChattingController.toChatting(o, "폴루트 워터: 힐 계열 마법, 회복용 물약 효과 1/2 감소",
				// Lineage.CHATTING_MODE_MESSAGE);
			} else {
				if (!Util.isDistance(cha, o, 5))
					ChattingController.toChatting(cha, "\\fY대상이 너무 멀리 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}

}