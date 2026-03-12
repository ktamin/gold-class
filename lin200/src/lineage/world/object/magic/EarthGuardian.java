package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class EarthGuardian extends Magic {

	public EarthGuardian(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new EarthGuardian(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;
			// 대미지 감소 +2
			cha.setDynamicReduction(cha.getDynamicReduction() + 2);
			
			// [삭제] 구형 쉴드 버프 패킷 제거 (S_BuffShield)
		}
		
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		// [수정] 신규 아이콘 패킷 전송 (36번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 36, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
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
			// 대미지 감소 원상복구
			cha.setDynamicReduction(cha.getDynamicReduction() - 2);
			
			// 종료 멘트 중복 방지 (주석 처리)
			// ChattingController.toChatting(cha, "\\fY어스 가디언 종료", Lineage.CHATTING_MODE_MESSAGE);
			
			// 구형 패킷 삭제
			// cha.toSender(S_BuffShield.clone(BasePacketPooling.getPool(S_BuffShield.class), 0, 7));
		}
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 36, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	ChattingController.toChatting(o, "\\fY어스 가디언: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(Character cha, Skill skill) {
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		if (SkillController.isMagic(cha, skill, true) && cha instanceof PcInstance) {
			PcInstance pc = (PcInstance) cha;
			onBuff(pc, skill, skill.getBuffDuration());
		}
	}

	static public void init(Character cha, int time) {
		BuffController.append(cha, EarthGuardian.clone(BuffController.getPool(EarthGuardian.class), SkillDatabase.find(20, 2), time));
	}

	static public void onBuff(PcInstance pc, Skill skill, int time) {
		pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, skill.getCastGfx()), true);
		BuffController.append(pc, EarthGuardian.clone(BuffController.getPool(EarthGuardian.class), skill, time));
		
		// 시작 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(pc, "어스 가디언: 대미지 감소+2", Lineage.CHATTING_MODE_MESSAGE);
	}
}