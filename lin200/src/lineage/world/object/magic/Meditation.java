package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
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

public class Meditation extends Magic {

	public Meditation(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new Meditation(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setBuffMeditation(true);
		
		if (o instanceof Character) {
			Character cha = (Character) o;
			// 틱당 MP 회복량 +5, 초기 레벨 0
			cha.setDynamicTicMp(cha.getDynamicTicMp() + 5);
			cha.setBuffMeditaitonLevel(0);
		}
		
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {
		// [추가] 버프 갱신 및 아이콘 유지 (66번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 66, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		o.setBuffMeditation(false);

		if (o instanceof Character) {
			Character cha = (Character) o;
			cha.setDynamicTicMp(cha.getDynamicTicMp() - 5);
			cha.setBuffMeditaitonLevel(0);
			
			// 종료 멘트 중복 방지 (주석 처리)
			// ChattingController.toChatting(cha, "\\fY메디테이션 종료", Lineage.CHATTING_MODE_MESSAGE);
		}
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 66, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	ChattingController.toChatting(o, "\\fY메디테이션: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(Character cha, Skill skill) {
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		// [수정] 안전하게 형변환 검사 후 딜레이 패킷 전송
		if (cha instanceof PcInstance) {
			SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(700).send((PcInstance) cha);
		}

		if (SkillController.isMagic(cha, skill, true))
			onBuff(cha, skill, skill.getBuffDuration());
	}

	static public void onBuff(object o, Skill skill, int time) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		BuffController.append(o, Meditation.clone(BuffController.getPool(Meditation.class), skill, time));
		
		// 시작 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "메디테이션: MP회복+5 유지되는 시간동안 회복량 점차 증가", Lineage.CHATTING_MODE_MESSAGE);
	}

}