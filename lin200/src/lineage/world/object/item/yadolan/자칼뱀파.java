package lineage.world.object.item.yadolan;

import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.util.Util;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.ChillTouch;

public class 자칼뱀파 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 자칼뱀파();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
			PcInstance pc = (PcInstance)cha;
			
			if (cha.getInventory() != null) {
				
				Skill s = SkillDatabase.find(28);

				if (SkillController.find(cha, 28, false) != null ) {
		
					if (SkillController.isHpMpCheck(cha, s.getHpConsume(), s.getMpConsume()) && Util.isDistance(pc, pc.autoAttackTarget, 8) && pc.autoAttackTarget != null ) {
						if (SkillController.isDelay(cha, SkillDatabase.find(28)))
							ChillTouch.init(pc, s, (int) pc.autoAttackTarget.getObjectId());
					}
				} 
			}
	}
}
