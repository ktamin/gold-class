package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class BounceAttack extends Magic {
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new BounceAttack(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
	
	public BounceAttack(Skill skill) {
		super(null, skill);
	}
	
	@Override
	public void toBuffStart(object o) {
		if(o instanceof Character) {
			Character cha = (Character)o;
			cha.setDynamicAddHit( cha.getDynamicAddHit() + 6 );
			cha.setDynamicAddHitBow( cha.getDynamicAddHitBow() + 6 );
			cha.setBuffBounceAttack(true);

			// 아이콘(22번) 표시
			if (o instanceof PcInstance) {
				PcInstance pc = (PcInstance) o;
				SC_BUFFICON_NOTI.on(pc, 21, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			}
		//	cha.toSender(S_Ext_BuffTime.clone(BasePacketPooling.getPool(S_Ext_BuffTime.class), S_Ext_BuffTime.BUFFID_1889, getTime()));
		}
		toBuffUpdate(o);
		
	}
	
	public void toBuffUpdate(object o) {	
		// 버프 갱신 시 아이콘 유지
				if (o instanceof PcInstance) {
					PcInstance pc = (PcInstance) o;
					SC_BUFFICON_NOTI.on(pc, 21, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
				}
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		if(o instanceof Character) {
			Character cha = (Character)o;
			cha.setDynamicAddHit( cha.getDynamicAddHit() - 6 );
			cha.setDynamicAddHitBow( cha.getDynamicAddHitBow() - 6 );
			cha.setBuffBounceAttack(false);
		}
	}

	/**
	 * 
	 * @param cha
	 * @param skill
	 * @param object_id
	 * @param x
	 * @param y
	 */
	static public void init(Character cha, Skill skill) {
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		if(SkillController.isMagic(cha, skill, true)) {
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			BuffController.append(cha, BounceAttack.clone(BuffController.getPool(BounceAttack.class), skill, skill.getBuffDuration()));
		}
		
	}

}
