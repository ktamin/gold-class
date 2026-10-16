package lineage.world.object.item.all_night;

import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.ExpDropBuff_20;

public class ExpDrop_Potion_20 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ExpDrop_Potion_20();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha instanceof PcInstance) {
			PcInstance pc = (PcInstance) cha;
			
			// 💡 요청하신 데이터베이스 마법 번호 702번 호출
			Skill s = SkillDatabase.find(702);

			if (s != null) {
				if (isLvCheck(pc)) {
					
					// 💡 매직 클래스(ExpDropBuff_20)의 init 메서드를 호출하여 버프 적용
					// (해당 init 메서드 안에 다른 버프 삭제, 이펙트 출력 로직이 모두 포함되어 있습니다)
					ExpDropBuff_20.init(pc, s);

					// 아이템 수량 갱신 (무한 물약이 아닐 경우 1개 차감)
					if (!getItem().getName().contains("무한")) {
						pc.getInventory().count(this, getCount() - 1, true);
					}
				}
			}
		}
	}
}