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
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class Shield extends Magic {

	public Shield(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new Shield(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;
			
			// [수정] AC -2 (방어력 향상)
			// 기존 코드(+2)는 방어력이 나빠지는 오류가 있었습니다.
			cha.setDynamicAc(cha.getDynamicAc() + 2);
			
			// 스탯 갱신
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
			
			// [삭제] 구형 패킷(S_BuffShield) 제거
		}
		
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}
	
	public void toBuffUpdate(object o) {	
		// 버프 갱신 및 아이콘 유지 (4번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 4, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
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
			
			// [수정] AC 원상복구 (+2)
			cha.setDynamicAc(cha.getDynamicAc() - 2);
			
			// 스탯 갱신
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
			
			// 종료 멘트 중복 방지 (주석 처리)
			// ChattingController.toChatting(cha, "\\fY실드 종료", Lineage.CHATTING_MODE_MESSAGE);
		}
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 4, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	ChattingController.toChatting(o, "\\fY실드: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(Character cha, Skill skill) {
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		// 스킬 딜레이 패킷 전송
		if (cha instanceof PcInstance) {
			SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(1000).send((PcInstance) cha);
		}
		
		if (SkillController.isMagic(cha, skill, true))
			onBuff(cha, skill);
	}

	static public void init(Character cha, int time) {
		BuffController.append(cha, Shield.clone(BuffController.getPool(Shield.class), SkillDatabase.find(1, 2), time));
	}

	static public void onBuff(Character cha, Skill skill) {
		// 이펙트
		cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);

		// 중복 버프 마법 우선순위 적용 (상위 버프가 있으면 실드 적용 안 함)
		// 어스 스킨
		if (BuffController.find(cha, SkillDatabase.find(600)) != null)
			return;
		// 아이언 스킨
		if (BuffController.find(cha, SkillDatabase.find(137)) != null)
			return;
			
		// 쉴드 적용
		BuffController.append(cha, Shield.clone(BuffController.getPool(Shield.class), skill, skill.getBuffDuration()));
		
		// 시작 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(cha, "실드: AC-2", Lineage.CHATTING_MODE_MESSAGE);
	}
}