package lineage.world.object.item.all_night;

import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.Exp_Potion;

public class Exp_potion extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Exp_potion();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha instanceof PcInstance) {
			PcInstance pc = (PcInstance) cha;
			Skill s = SkillDatabase.find(211);

			if (s != null) {
				if (isLvCheck(pc)) {
					int time = s.getBuffDuration();
					
					if (getItem().getName().contains("30일")) {
						time = 36000;
					}
					
					// [수정] 매직 클래스의 onBuff를 호출 (아이콘 패킷은 매직 클래스 내부에서 처리함)
					Exp_Potion.onBuff(pc, s, time, false);

					// 경험치/아데나 버프 종료 시간 설정 (메모리 로직 유지)
					pc.setExpPotionAdenaUntil(System.currentTimeMillis() + (time * 1000L));

					// 아이템 수량 갱신
					if (!getItem().getName().contains("30일")) {
						pc.getInventory().count(this, getCount() - 1, true);
					}
				}
			}
		}
	}
}