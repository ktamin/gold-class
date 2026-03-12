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

public class ExoticVitalize extends Magic {

	public ExoticVitalize(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new ExoticVitalize(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		// 1. 상태 설정 (무게 초과 회복 로직은 회복 틱 처리 부분에서 이 플래그를 체크함)
		o.setBuffExoticVitalize(true);
		
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {
		// 2. 버프 갱신 및 아이콘 유지 (61번)
		// 기존의 S_ObjectEffect 반복 호출 삭제
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 61, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o){
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o){
		// 3. 상태 해제
		o.setBuffExoticVitalize( false );
		
		// 4. 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 61, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		
		// 종료 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "\\fY엑조틱 바이탈라이즈 종료", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	ChattingController.toChatting(o, "\\fY엑조틱 바이탈라이즈: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	static public void init(Character cha, Skill skill, long object_id) {
		// 처리
		if (cha != null) {
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
			
			if(SkillController.isMagic(cha, skill, true)) {
				// [이동] 이펙트는 시작할 때 한 번만 출력 (Update에서 이동해옴)
				cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
				
				BuffController.append(cha, ExoticVitalize.clone(BuffController.getPool(ExoticVitalize.class), skill, skill.getBuffDuration()));
				
				// 시작 멘트 중복 방지 (주석 처리)
				// ChattingController.toChatting(cha, "엑조틱 바이탈라이즈: 무게 50% 초과해도 HP/MP 자연 회복", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}
	
	static public void init(Character cha, Skill skill) {
		// 처리
		if (cha != null) {
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
			
			if(SkillController.isMagic(cha, skill, true)) {
				// [이동] 이펙트 추가
				cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
				
				BuffController.append(cha, ExoticVitalize.clone(BuffController.getPool(ExoticVitalize.class), skill, skill.getBuffDuration()));
				
				// 시작 멘트 중복 방지 (주석 처리)
				// ChattingController.toChatting(cha, "엑조틱 바이탈라이즈: 무게 50% 초과해도 HP/MP 자연 회복", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}
}