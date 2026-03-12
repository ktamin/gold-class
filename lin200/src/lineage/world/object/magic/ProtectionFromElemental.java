package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
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

public class ProtectionFromElemental extends Magic {

	public ProtectionFromElemental(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new ProtectionFromElemental(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;

			if (pc.getAttribute() > 0) {
				switch (pc.getAttribute()) {
				case Lineage.ELEMENT_EARTH:
					pc.setDynamicEarthress(pc.getDynamicEarthress() + 50);
					break;
				case Lineage.ELEMENT_FIRE:
					pc.setDynamicFireress(pc.getDynamicFireress() + 50);
					break;
				case Lineage.ELEMENT_WIND:
					pc.setDynamicWindress(pc.getDynamicWindress() + 50);
					break;
				case Lineage.ELEMENT_WATER:
					pc.setDynamicWaterress(pc.getDynamicWaterress() + 50);
					break;
				}
			}
			pc.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), pc));
		}
		
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {
		// 버프 갱신 및 아이콘 유지 (54번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 54, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			switch (pc.getAttribute()) {
			case Lineage.ELEMENT_EARTH:
				pc.setDynamicEarthress(pc.getDynamicEarthress() - 50);
				break;
			case Lineage.ELEMENT_FIRE:
				pc.setDynamicFireress(pc.getDynamicFireress() - 50);
				break;
			case Lineage.ELEMENT_WIND:
				pc.setDynamicWindress(pc.getDynamicWindress() - 50);
				break;
			case Lineage.ELEMENT_WATER:
				pc.setDynamicWaterress(pc.getDynamicWaterress() - 50);
				break;
			}
			
			// 종료 멘트 중복 방지 (주석 처리)
			// ChattingController.toChatting(pc, "\\fY프로텍션 프롬 엘리멘트 종료", Lineage.CHATTING_MODE_MESSAGE);
			
			pc.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), pc));
			
			// 종료 시 아이콘 즉시 삭제
			SC_BUFFICON_NOTI.on(pc, 54, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	ChattingController.toChatting(o, "\\fY프로텍션 프롬 엘리멘트: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(Character cha, Skill skill) {
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		if (SkillController.isMagic(cha, skill, true)) {
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			BuffController.append(cha, ProtectionFromElemental.clone(BuffController.getPool(ProtectionFromElemental.class), skill, skill.getBuffDuration()));

			// 속성 확인 및 멘트 출력 부분 주석 처리 (중복 방지)
			/*
			String att = "";
			switch (cha.getAttribute()) {
			case Lineage.ELEMENT_EARTH:
				att = "땅";
				break;
			case Lineage.ELEMENT_FIRE:
				att = "불";
				break;
			case Lineage.ELEMENT_WIND:
				att = "바람";
				break;
			case Lineage.ELEMENT_WATER:
				att = "물";
				break;
			}
			ChattingController.toChatting(cha, "프로텍션 프롬 엘리멘트: " + att + "속성 저항+50%", Lineage.CHATTING_MODE_MESSAGE);
			*/
		}
	}

}