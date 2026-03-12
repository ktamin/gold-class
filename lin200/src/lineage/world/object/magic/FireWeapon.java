package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
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

public class FireWeapon extends Magic {

	public FireWeapon(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new FireWeapon(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setBuffFireWeapon(true);
		
		if (o instanceof Character) {
			Character target = (Character) o;
			// 근거리 대미지+2, 명중+4
			target.setDynamicAddDmg(target.getDynamicAddDmg() + 2);
			target.setDynamicAddHit(target.getDynamicAddHit() + 4);
			
			// 스탯 갱신 패킷 (이걸 보내야 내 정보창에 바로 반영됨)
			target.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), target));
			
			// 시작 멘트 삭제 (주석 처리)
			// ChattingController.toChatting(o, "\\fY근거리 대미지+2, 근거리 명중+4", Lineage.CHATTING_MODE_MESSAGE);
		}
		
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {
		// [수정] 구형 패킷(S_BuffElf) 및 이펙트 반복 전송 삭제
		// 신규 아이콘 패킷 전송 (13번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 13, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o){
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o){
		o.setBuffFireWeapon(false);
		
		if (o instanceof Character) {
			Character target = (Character) o;
			// 스탯 원상복구
			target.setDynamicAddDmg(target.getDynamicAddDmg() - 2);
			target.setDynamicAddHit(target.getDynamicAddHit() - 4);
			
			// 스탯 갱신
			target.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), target));
			
			// 종료 멘트 삭제 (주석 처리)
			// ChattingController.toChatting(o, "\\fY파이어 웨폰 종료", Lineage.CHATTING_MODE_MESSAGE);
		}
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 13, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}
	
	static public void init(Character cha, Skill skill, long object_id){	
		if(cha != null){
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
			
			if(SkillController.isMagic(cha, skill, true) && SkillController.isFigure(cha, cha, skill, false, false)){
				// 버닝 웨폰(132번 스킬)과 중복 방지
				if (BuffController.find(cha, SkillDatabase.find(132)) != null)
					return;
				
				// 이펙트 전송 (Update에서 빼고 여기로 이동)
				cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
				
				BuffController.append(cha, FireWeapon.clone(BuffController.getPool(FireWeapon.class), skill, skill.getBuffDuration()));
			}
		}
	}
}