package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class InvisiBility extends Magic {

	public InvisiBility(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new InvisiBility(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		o.setInvis(true);
		o.setBuffInvisiBility(true);
		
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		// [추가] 버프 갱신 및 아이콘 유지 (73번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 73, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o){
		// 강제적으로 종료를 요청하는 부분이므로 투망체크 안함.
		// 캔슬이나 디텍션 같은거..
		o.setInvis(false);
		o.setBuffInvisiBility(false);
		
		// 강제 종료 시에도 아이콘 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 73, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffEnd(object o){
		if(o.isWorldDelete())
			return;
			
		o.setBuffInvisiBility(false);
		
		// [추가] 종료 시 아이콘 삭제 (73번)
		// 투명 망토를 입고 있어도 마법 시간은 끝났으므로 아이콘은 지워야 함
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 73, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		
		if(o.getInventory() != null){
			// 투명망토(180) 착용상태일경우에는 투명해제 안해도됨.
			ItemInstance item = o.getInventory().getSlot(Lineage.SLOT_CLOAK);
			if(item==null || item.getItem().getNameIdNumber()!=180)
				o.setInvis(false);
		}else{
			o.setInvis(false);
		}
	}
	
	static public void init(Character cha, Skill skill){
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		// [추가] 시전 딜레이 패킷
		if (cha instanceof PcInstance) {
			SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(1000).send((PcInstance) cha);
		}
		
		if(SkillController.isMagic(cha, skill, true))
			BuffController.append(cha, InvisiBility.clone(BuffController.getPool(InvisiBility.class), skill, skill.getBuffDuration()));
	}
	
	static public void init2(Character cha, Skill skill){
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
	
		BuffController.append(cha, InvisiBility.clone(BuffController.getPool(InvisiBility.class), skill, skill.getBuffDuration()));
	}
	
	static public void init(Character cha, int time){
		BuffController.append(cha, InvisiBility.clone(BuffController.getPool(InvisiBility.class), SkillDatabase.find(8, 3), time));
	}
	
}