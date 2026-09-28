package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import all_night.Lineage_Balance;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class UncannyDodge extends Magic {
	
	public UncannyDodge(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new UncannyDodge(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		if(o instanceof Character) {
			Character cha = (Character)o;
			
			// 💡 [수정] 콘프 파일(Lineage.java)에서 설정한 값만큼 상승
			cha.setDynamicDg( cha.getDynamicDg() + Lineage_Balance.uncanny_dodge_dg );
			
			// 스탯 갱신 패킷 전송
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}
		
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 52, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o){
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o){
		if(o instanceof Character) {
			Character cha = (Character)o;
			
			// 💡 [수정] 콘프 파일에서 설정한 값만큼 원상복구
			cha.setDynamicDg( cha.getDynamicDg() - Lineage_Balance.uncanny_dodge_dg );
			
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 52, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}	

	static public void init(Character cha, Skill skill){
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		if(SkillController.isMagic(cha, skill, true)){
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			BuffController.append(cha, UncannyDodge.clone(BuffController.getPool(UncannyDodge.class), skill, skill.getBuffDuration()));
		}
	}
}