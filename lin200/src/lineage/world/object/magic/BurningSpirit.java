package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
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

public class BurningSpirit extends Magic {
	
	public BurningSpirit(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new BurningSpirit(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		// 1. 상태 변경
		o.setBuffBurningSpirit(true);
	
		// 아이콘 중복 방지를 위해 여기서는 삭제 (toBuffUpdate로 위임)
		
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {	
		// 2. 버프 갱신 및 아이콘 유지 (42번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 42, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o){
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o){
		o.setBuffBurningSpirit(false);
		
		// 3. 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 42, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}

		// 종료 멘트 중복 방지 (주석 처리)
		// if(o instanceof Character){
		// 	Character cha = (Character) o;
		// 	ChattingController.toChatting(cha, "\\fY버닝 스프릿츠 효과가 종료되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
		// }		
	}

	@Override
	public void toBuff(object o) {
		// 시간 알림 멘트 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	 ChattingController.toChatting(o, "\\fY버닝 스프릿츠: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(Character cha, Skill skill){
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		if(SkillController.isMagic(cha, skill, true)){
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			BuffController.append(cha, BurningSpirit.clone(BuffController.getPool(BurningSpirit.class), skill, skill.getBuffDuration()));
			
			// 시작 멘트 중복 방지 (주석 처리)
			// ChattingController.toChatting(cha, "버닝 스프릿츠 : 일정 확률로 근거리 대미지 1.5배", Lineage.CHATTING_MODE_MESSAGE);
		}
	}
	
}