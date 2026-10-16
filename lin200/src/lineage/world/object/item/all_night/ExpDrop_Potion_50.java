package lineage.world.object.item.all_night;

import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.ExpDropBuff_50;

public class ExpDrop_Potion_50 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ExpDrop_Potion_50();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha instanceof PcInstance) {
			PcInstance pc = (PcInstance) cha;
			
			// 💡 ExpDropBuff_50 마법 번호인 703번 호출
			Skill s = SkillDatabase.find(703);

			if (s != null) {
				if (isLvCheck(pc)) {
					
					// 💡 매직 클래스의 init 메서드를 호출하여 버프 및 아이콘 적용
					ExpDropBuff_50.init(pc, s);

					// 아이템 수량 갱신 (무한 물약이 아닐 경우 1개 차감)
					if (!getItem().getName().contains("무한")) {
						pc.getInventory().count(this, getCount() - 1, true);
					}
				}
			}
		}
	}
}