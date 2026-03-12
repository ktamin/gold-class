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

public class BurningWeapon extends Magic {

	public BurningWeapon(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new BurningWeapon(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setBuffBurningWeapon(true);
		
		if (o instanceof Character) {
			Character target = (Character) o;
			// 근거리 대미지+6, 명중+6
			target.setDynamicAddDmg(target.getDynamicAddDmg() + 6);
			target.setDynamicAddHit(target.getDynamicAddHit() + 6);
			
			// [추가] 스탯 갱신 패킷 (정보창 즉시 반영)
			target.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), target));
			
			// 아이콘 처리는 toBuffUpdate로 위임
			toBuffUpdate(o);
		}
	}
	
	@Override
	public void toBuffUpdate(object o) {
		// [수정] 구형 패킷(S_BuffElf) 삭제 후 신규 아이콘(59번) 적용
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 59, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o){
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o){
		o.setBuffBurningWeapon(false);
		
		if (o instanceof Character) {
			Character target = (Character) o;
			// 스탯 원상복구
			target.setDynamicAddDmg(target.getDynamicAddDmg() - 6);
			target.setDynamicAddHit(target.getDynamicAddHit() - 6);
			
			// 스탯 갱신
			target.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), target));
			
			// 종료 시 아이콘 즉시 삭제
			if (target instanceof PcInstance) {
				SC_BUFFICON_NOTI.on((PcInstance)target, 59, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			}

			// 종료 멘트 중복 방지 (주석 처리)
			// ChattingController.toChatting(o, "\\fY버닝웨폰 종료", Lineage.CHATTING_MODE_MESSAGE);
		}	
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	ChattingController.toChatting(o, "\\fY버닝웨폰: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	static public void init(Character cha, Skill skill){
		
		// 검이나 단검 착용 시에만 사용 가능
		if (cha.getInventory().getSlot(Lineage.SLOT_WEAPON) != null &&( cha.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2().equalsIgnoreCase("sword") || cha.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2().equalsIgnoreCase("dagger"))) {
		
			if(cha != null){
				cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
				
				if(SkillController.isMagic(cha, skill, true) && SkillController.isFigure(cha, cha, skill, false, false))
					onBuff(cha, skill);
			}
	
		}else{
			// [수정] 오타 수정 ("검을을" -> "검을")
			ChattingController.toChatting(cha, "\\fY검을 착용해야 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
	}
	
	static public void onBuff(object o, Skill skill) {
		// 하위 버프(파이어 웨폰) 제거
		BuffController.remove(o, FireWeapon.class);
		
		// 이펙트 처리
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		
		// 버프 등록
		BuffController.append(o, BurningWeapon.clone(BuffController.getPool(BurningWeapon.class), skill, skill.getBuffDuration()));
		
		// 시작 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "버닝웨폰: 근거리 대미지+6, 근거리 명중+6", Lineage.CHATTING_MODE_MESSAGE);
	}
	
}