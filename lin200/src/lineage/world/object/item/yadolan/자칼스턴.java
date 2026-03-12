package lineage.world.object.item.yadolan;

import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.util.Util;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.ShockStun;

public class 자칼스턴 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 자칼스턴();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
			PcInstance pc = (PcInstance)cha;
			
			if (cha.getInventory() != null) {
				
				Skill s = SkillDatabase.find(16);

				if (SkillController.find(cha, 16, false) != null ) {
		
					if (SkillController.isHpMpCheck(cha, s.getHpConsume(), s.getMpConsume()) && Util.isDistance(pc, pc.autoAttackTarget, 1) && pc.autoAttackTarget != null ) {
						if (SkillController.isDelay(cha, SkillDatabase.find(16)))
							ShockStun.init(pc, s, (int) pc.autoAttackTarget.getObjectId());
					}
				} 
			}
	}
}
