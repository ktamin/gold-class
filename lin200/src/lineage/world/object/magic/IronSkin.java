package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
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

public class IronSkin extends Magic {

	public IronSkin(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new IronSkin(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;

			// [수정] AC 계산식 수정 (+10 -> -10)
			// 리니지는 AC가 낮을수록 방어력이 높습니다. 아이언스킨은 방어력을 올려야 하므로 값을 빼야 합니다.
			cha.setDynamicAc(cha.getDynamicAc() + 10);

			// 스탯 갱신 (정보창 반영)
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));

			// 구형 패킷 삭제 (S_BuffShield)
		}

		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		// [수정] 신규 아이콘 패킷 전송 (37번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 37, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
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

		if (o instanceof Character) {
			Character cha = (Character) o;

			// [수정] AC 원상복구 (-10 -> +10)
			cha.setDynamicAc(cha.getDynamicAc() - 10);

			// 스탯 갱신
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));

			// 종료 멘트 중복 방지 (주석 처리)
			// ChattingController.toChatting(cha, "\\fY아이언 스킨 종료",
			// Lineage.CHATTING_MODE_MESSAGE);
		}

		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 37, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() ==
		// Lineage.buff_magic_time_min)
		// ChattingController.toChatting(o, "\\fY아이언 스킨: " + getTime() + "초 후 종료",
		// Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(Character cha, Skill skill, long object_id) {
		// 초기화
		object o = null;
		// 타겟 찾기
		if (Lineage.is_iron_skin_target) {
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

			if (SkillController.isMagic(cha, skill, true))
				onBuff(o, skill);
		}
	}

	static public void init(Character cha, int time) {
		BuffController.append(cha,
				IronSkin.clone(BuffController.getPool(IronSkin.class), SkillDatabase.find(21, 7), time));
	}

	static public void onBuff(object o, Skill skill) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);

		// 쉴드 제거
		BuffController.remove(o, Shield.class);
		// 어스스킨 제거
		BuffController.remove(o, EarthSkin.class);

		// 아이언스킨 적용
		BuffController.append(o,
				IronSkin.clone(BuffController.getPool(IronSkin.class), skill, skill.getBuffDuration()));

		// 시작 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "아이언 스킨: AC-10",
		// Lineage.CHATTING_MODE_MESSAGE);
	}
}