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

public class DoubleBreak extends Magic {
	
	public DoubleBreak(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new DoubleBreak(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		// 1. 상태 설정 (대미지 2배 로직은 타격 연산 파일에서 이 플래그를 체크함)
		o.setBuffDoubleBreak(true);
		
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		// 2. 버프 갱신 및 아이콘 유지 (49번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 49, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o){
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o){
		// 3. 상태 해제
		o.setBuffDoubleBreak(false);
		
		// 4. 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 49, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		
		// 종료 메시지 중복 방지 (주석 처리)
		// if(o instanceof Character){
		// 	Character cha = (Character) o;
		// 	ChattingController.toChatting(cha, "\\fY더블 브레이크 효과가 종료되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
		// }	
	}

	static public void init(Character cha, Skill skill){
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		if(SkillController.isMagic(cha, skill, true)){
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			BuffController.append(cha, DoubleBreak.clone(BuffController.getPool(DoubleBreak.class), skill, skill.getBuffDuration()));
			
			// 시작 메시지 중복 방지 (주석 처리)
			// ChattingController.toChatting(cha, "더블 브레이크 : 일정 확률로 근거리 대미지 2배", Lineage.CHATTING_MODE_MESSAGE);
		}
	}
}