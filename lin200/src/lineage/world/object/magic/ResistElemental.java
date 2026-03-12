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

public class ResistElemental extends Magic {

	public ResistElemental(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new ResistElemental(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;
			// 4대 속성 저항 +10
			cha.setDynamicEarthress(cha.getDynamicEarthress() + 10);
			cha.setDynamicWaterress(cha.getDynamicWaterress() + 10);
			cha.setDynamicFireress(cha.getDynamicFireress() + 10);
			cha.setDynamicWindress(cha.getDynamicWindress() + 10);
			
			// 스탯 갱신 패킷
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}
		
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {
		// 버프 갱신 및 아이콘 유지 (10번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 10, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
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
			// 속성 저항 원상복구
			cha.setDynamicEarthress(cha.getDynamicEarthress() - 10);
			cha.setDynamicWaterress(cha.getDynamicWaterress() - 10);
			cha.setDynamicFireress(cha.getDynamicFireress() - 10);
			cha.setDynamicWindress(cha.getDynamicWindress() - 10);
			
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 10, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		
		// 종료 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(cha, "\\fY레지스트 엘리멘트 종료", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	ChattingController.toChatting(o, "\\fY레지스트 엘리멘트: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(Character cha, Skill skill) {
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(5500).send((PcInstance) cha);
		
		if (SkillController.isMagic(cha, skill, true)) {
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			BuffController.append(cha, ResistElemental.clone(BuffController.getPool(ResistElemental.class), skill, skill.getBuffDuration()));
			
			// 시작 멘트 중복 방지 (주석 처리)
			// ChattingController.toChatting(cha, "레지스트 엘리멘트: 모든 속성 저항+10%", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	static public void init(Character cha, int time) {
		BuffController.append(cha, ResistElemental.clone(BuffController.getPool(ResistElemental.class), SkillDatabase.find(18, 1), time));
	}

}