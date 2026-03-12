package lineage.world.object.item.yadolan;

import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 트리소울 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 트리소울();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
			PcInstance pc = (PcInstance)cha;
			long saveTime = System.currentTimeMillis();
			long currTime = 0;
			int delayTime = 2000;

			if (cha.getInventory() != null) {
				
				Skill s = SkillDatabase.find(115);
				Skill s1 = SkillDatabase.find(116);

					
				
			}
		} 	
	


}
