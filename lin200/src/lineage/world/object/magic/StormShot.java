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

public class StormShot extends Magic {

	public StormShot(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new StormShot(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;
			o.setBuffStormShot(true);
			
			// 원거리 추타+6, 명중+3
			cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + 6);
			cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + 3);
			
			// [추가] 스탯 갱신 패킷 (정보창 즉시 반영)
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}
		
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {
		// [수정] 구형 패킷(S_BuffElf) 및 반복 이펙트 삭제
		// 신규 아이콘 패킷 전송 (62번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 62, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o){
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o){
		if (o instanceof Character) {
			Character cha = (Character) o;
			o.setBuffStormShot(false);
			
			// 스탯 원상복구
			cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - 6);
			cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - 3);
			
			// 스탯 갱신
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
			
			// 종료 멘트 중복 방지 (주석 처리)
			// ChattingController.toChatting(o, "\\fY스톰 샷 종료", Lineage.CHATTING_MODE_MESSAGE);
		}
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 62, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	ChattingController.toChatting(o, "\\fY스톰 샷: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	static public void init(Character cha, Skill skill, long object_id){
		
		// 활 착용 검사
		if (cha.getInventory().getSlot(Lineage.SLOT_WEAPON) == null || !cha.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2().equalsIgnoreCase("bow")) {
			ChattingController.toChatting(cha, "\\fY활을 착용해야 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		// 초기화
		object o = null;
		// 타겟 찾기
		if (Lineage.is_storm_shot_target) {
			if (object_id == cha.getObjectId())
				o = cha;
			else
				o = cha.findInsideList(object_id);
		} else {
			o = cha;
		}
		
		// 처리
		if(o != null){
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
			
			if(SkillController.isMagic(cha, skill, true)){
				// [이동] 이펙트는 시작할 때 한 번만 출력 (Update에서 이동해옴)
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
				
				// 중복되지 않게 다른 버프 제거.
				// 윈드 샷 (하위) 제거
				BuffController.remove(o, WindShot.class);
				// 아이오브스톰 (상호 배제) 제거
				BuffController.remove(o, EyeOfStorm.class);
				
				// 버프 등록
				BuffController.append(o, StormShot.clone(BuffController.getPool(StormShot.class), skill, skill.getBuffDuration()));
				
				// 시작 멘트 중복 방지 (주석 처리)
				// ChattingController.toChatting(o, "스톰 샷: 원거리 대미지+6, 원거리 명중+3", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}
	
}