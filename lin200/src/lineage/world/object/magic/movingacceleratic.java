package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectSpeed;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class movingacceleratic extends Magic {

	public movingacceleratic(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new movingacceleratic(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		o.setBrave(true);
		
		// [속도 패킷] 실제 이동 속도 증가 적용
		if(Lineage.server_version > 200)
			o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 1, 1, getTime()), true);
		else
			o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 1, 1, getTime()), true);
			
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		o.setBrave(true);
		
		// [속도 패킷] 재접속 시 속도 유지
		if(Lineage.server_version > 200)
			o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 1, 1, getTime()), true);
		else
			o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 1, 1, getTime()), true);

		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			
			// [중요] 용기 아이콘(2번) 및 1번 아이콘 강제 삭제
			// 무빙 악셀레이션 사용 시 용기 아이콘이 뜨는 것을 막습니다.
			SC_BUFFICON_NOTI.on(pc, 2, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			SC_BUFFICON_NOTI.on(pc, 1, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			
			// [아이콘] 47번 무빙 악셀레이션 아이콘 전송
			SC_BUFFICON_NOTI.on(pc, 47, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
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
			
		o.setBrave(false);
		// 속도 원상복구 (Normal)
		o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 1, 0, 0), true);
		
		// 종료 시 아이콘 삭제
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			
			// 무빙 악셀 아이콘 삭제 (47번)
			SC_BUFFICON_NOTI.on(pc, 47, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			
			// 혹시 남아있을 용기 아이콘도 삭제 (2번)
			SC_BUFFICON_NOTI.on(pc, 2, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		
		// 종료 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "\\fY무빙 악셀레이션 종료", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	ChattingController.toChatting(o, "\\fY무빙 악셀레이션: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
		
		if (getTime() == 1)
			o.speedCheck = System.currentTimeMillis() + 2000;
	}
	
	static public void init(Character cha, Skill skill){
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		// [추가] 시전 딜레이 패킷
		if (cha instanceof PcInstance) {
			SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(1100).send((PcInstance) cha);
		}
		
		if(SkillController.isMagic(cha, skill, true)){
			// 패킷 처리
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			
			// 처리.
			if(cha.getSpeed() != 2){
				// 용기(Bravery)와 중복 불가 -> 용기 제거
				// toBuffUpdate에서 아이콘도 지워줍니다.
				BuffController.remove(cha, Bravery.class);
				
				// 슬로우 상태가 아닐경우 버프 적용
				BuffController.append(cha, movingacceleratic.clone(BuffController.getPool(movingacceleratic.class), skill, skill.getBuffDuration()));
				
				// 시작 멘트 중복 방지 (주석 처리)
				// ChattingController.toChatting(cha, "무빙 악셀레이션: 속도 향상", Lineage.CHATTING_MODE_MESSAGE);
			}else{
				// 슬로우 상태일경우 슬로우 제거.
				BuffController.remove(cha, Slow.class);
			}
		}
	}
	
	static public void init(Character cha, int time){
		// 적용 (GM 명령어 등에서 호출 시)
		if (cha.getClassType() == Lineage.LINEAGE_CLASS_WIZARD || cha.getClassType() == Lineage.LINEAGE_CLASS_DARKELF)
			BuffController.append(cha, movingacceleratic.clone(BuffController.getPool(movingacceleratic.class), SkillDatabase.find(7, 3), time));
	}

}