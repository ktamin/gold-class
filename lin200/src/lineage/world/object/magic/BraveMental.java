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

public class BraveMental extends Magic {

	public BraveMental(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new BraveMental(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		// 1. 상태 설정
		o.setBuffBraveMental(true);
	
		// 아이콘 중복 방지를 위해 여기서는 삭제 (toBuffUpdate로 위임)
	
		toBuffUpdate(o);	
	}

	@Override
	public void toBuffUpdate(object o) {	
		// 버프 갱신 및 아이콘 유지 (19번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 19, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o){
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o){
		if(o.isWorldDelete())
			return;
			
		// 상태 해제
		o.setBuffBraveMental(false);
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 19, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}

		// 종료 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "\\fY브레이브 멘탈 종료", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 멘트 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	ChattingController.toChatting(o, "\\fY브레이브 멘탈: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	static public void init(Character cha, Skill skill){
		// 패킷
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		// 시전가능 확인
		if(SkillController.isMagic(cha, skill, true) && cha instanceof PcInstance){		
			// 처리.
			onBuff(cha, skill);
		}
	}
	
	static public void init2(Character cha, Skill skill){
		// 시전가능 확인
		if(SkillController.isMagic(cha, skill, true) && cha instanceof PcInstance){		
			// 처리.
			onBuff(cha, skill);
		}
	}

	/**
	 * 중복코드 방지용.
	 * @param cha
	 * @param pc
	 * @param skill
	 */
	static public void onBuff(object o, Skill skill) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		BuffController.append(o, BraveMental.clone(BuffController.getPool(BraveMental.class), skill, skill.getBuffDuration()));
		
		// 시작 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "브레이브 멘탈: 일정 확률로 근거리 대미지 1.8배", Lineage.CHATTING_MODE_MESSAGE);
	}
}