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

public class ReductionArmor extends Magic {
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new ReductionArmor(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
	
	public ReductionArmor(Skill skill){
		super(null, skill);
	}
	
	@Override
	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;
			// 리덕션 계산 로직 유지
			cha.setReductionAromr(1 + (cha.getLevel() - 50 < 5 ? 0 : ((cha.getLevel() - 50) / 5)));
			cha.setDynamicReduction(cha.getDynamicReduction() + cha.getReductionAromr());
		}
		
		// 아이콘 중복 방지를 위해 여기서는 삭제 (toBuffUpdate에서 처리)
		
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {	
		// 버프 갱신 및 아이콘 유지 (22번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 22, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;		
			cha.setDynamicReduction(cha.getDynamicReduction() - cha.getReductionAromr());
		}
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 22, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}

		// 종료 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "리덕션 아머 종료", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 멘트 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	ChattingController.toChatting(o, "\\fY리덕션 아머: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	/**
	 * * @param cha
	 * @param skill
	 */
	static public void init(Character cha, Skill skill) {
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(2000).send((PcInstance) cha);
		
		if(SkillController.isMagic(cha, skill, true)) {
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			BuffController.append(cha, ReductionArmor.clone(BuffController.getPool(ReductionArmor.class), skill, skill.getBuffDuration()));
			
			// 시작 멘트 중복 방지 (주석 처리)
			// ChattingController.toChatting(cha, "리덕션 아머: 대미지 감소+1, 50레벨 이후 5레벨 당 대미지 감소+1씩 증가", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

}