package lineage.world.object.magic;

import java.util.ArrayList;
import java.util.List;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.bean.lineage.Clan;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.ClanController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class WaterLife extends Magic {

	public WaterLife(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new WaterLife(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		// 1. 상태 설정 (힐 효과 2배 로직은 힐 마법 처리 부분에서 이 플래그를 체크함)
		o.setBuffWaterLife(true);

		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		// 2. 버프 갱신 및 아이콘 유지 (28번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 28, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		// 3. 상태 해제
		o.setBuffWaterLife(false);

		// 4. 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 28, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}

		// 종료 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "\\fY워터 라이프 종료",
		// Lineage.CHATTING_MODE_MESSAGE);
	}

	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() ==
		// Lineage.buff_magic_time_min)
		// ChattingController.toChatting(o, "\\fY워터 라이프: " + getTime() + "초 후 종료",
		// Lineage.CHATTING_MODE_MESSAGE);
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

			if (SkillController.isMagic(cha, skill, true)) {
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()),
						true);
				BuffController.append(o,
						WaterLife.clone(BuffController.getPool(WaterLife.class), skill, skill.getBuffDuration()));

				// 시작 멘트 중복 방지 (주석 처리)
				// ChattingController.toChatting(cha, "워터 라이프: 힐 계열 마법의 효과 2배 1회 적용",
				// Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}

	static public void init2(Character cha, Skill skill, long object_id) {
		// 처리
		if (SkillController.isMagic(cha, skill, true)) {

			// [수정] 깨진 공백 문자 수정
			if (cha.getClanId() > 0) {
				PcInstance elf = (PcInstance) cha;
				List<object> list_temp = new ArrayList<object>();
				list_temp.add(elf);
				// 혈맹원 추출.
				Clan c = ClanController.find(elf);
				if (c != null) {
					for (PcInstance pc : c.getList()) {
						if (!list_temp.contains(pc) && Util.isDistance(cha, pc, 8))
							list_temp.add(pc);
					}
				}
				cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha,
						Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);

				// 처리.
				for (object o : list_temp) {
					o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o,
							skill.getCastGfx()), true);
					BuffController.append(o,
							WaterLife.clone(BuffController.getPool(WaterLife.class), skill, skill.getBuffDuration()));
				}

				// 멘트 중복 방지
				// ChattingController.toChatting(cha, "워터 라이프: 힐 계열 마법의 효과 2배 1회 적용",
				// Lineage.CHATTING_MODE_MESSAGE);
			}

			if (cha.getClanId() < 1) {
				// 초기화
				object o = null;
				// 타겟 찾기
				if (object_id == cha.getObjectId())
					o = cha;
				else
					o = cha.findInsideList(object_id);
				// 처리
				if (o != null) {
					cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha,
							Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);

					if (SkillController.isMagic(cha, skill, true)) {
						o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o,
								skill.getCastGfx()), true);
						BuffController.append(o, WaterLife.clone(BuffController.getPool(WaterLife.class), skill,
								skill.getBuffDuration()));

						// 멘트 중복 방지
						// ChattingController.toChatting(cha, "워터 라이프: 힐 계열 마법의 효과 2배 1회 적용",
						// Lineage.CHATTING_MODE_MESSAGE);
					}
				}
			}
		}
	}
}