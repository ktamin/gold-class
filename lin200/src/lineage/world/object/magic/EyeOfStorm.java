package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.bean.lineage.Party;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.PartyController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class EyeOfStorm extends Magic {
	
	public EyeOfStorm(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new EyeOfStorm(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		Character cha = (Character) o;
		cha.setBuffEyeOfStorm(true);
		
		// 원거리 추타+3, 명중+2
		cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + 3);
		cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + 2);
		
		// [추가] 스탯 갱신 패킷 (파티원 정보창 즉시 갱신)
		cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {
		// [수정] 이펙트 반복 재생 삭제 (onBuff로 이동)
		// [수정] 구형 패킷(S_BuffElf) 삭제하고 신규 아이콘(58번) 적용
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 58, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o){
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o){
		Character cha = (Character) o;
		cha.setBuffEyeOfStorm(false);
		
		// 스탯 원상복구
		cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - 3);
		cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - 2);
		
		// 스탯 갱신
		cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 58, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		
		// 종료 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "\\fY아이 오브 스톰 종료", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	ChattingController.toChatting(o, "\\fY아이 오브 스톰: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	static public void init(Character cha, Skill skill){
		// 활 착용 검사
		if (cha.getInventory().getSlot(Lineage.SLOT_WEAPON) == null || !cha.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2().equalsIgnoreCase("bow")) {
			ChattingController.toChatting(cha, "\\fY활을 착용해야 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		if(SkillController.isMagic(cha, skill, true) && cha instanceof PcInstance){
			PcInstance pc = (PcInstance)cha;
			Party p = PartyController.find(pc);
			
			if(p == null){
				// 파티가 없으면 나만 적용
				onBuff(pc, skill, skill.getBuffDuration());
			}else{
				// 파티원이면 화면 내(Lineage.SEARCH_LOCATIONRANGE) 파티원 전체 적용
				for(PcInstance use : p.getList()){
					if(Util.isDistance(cha, use, Lineage.SEARCH_LOCATIONRANGE))
						onBuff(use, skill, skill.getBuffDuration());
				}
			}
		}
	}
	
	static public void onBuff(PcInstance pc, Skill skill, int time){
		// 중복되지 않게 하위 호환 버프 제거
		// 윈드샷, 스톰샷 제거
		BuffController.remove(pc, WindShot.class);	
		BuffController.remove(pc, StormShot.class);	
		
		// [이동] 이펙트는 여기서 한 번만 출력 (Update에서 이동해옴)
		pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, skill.getCastGfx()), true);
		
		BuffController.append(pc, EyeOfStorm.clone(BuffController.getPool(EyeOfStorm.class), skill, time));
		
		// 시작 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(pc, "아이 오브 스톰: 원거리 대미지+3, 원거리 명중+2", Lineage.CHATTING_MODE_MESSAGE);
	}

}