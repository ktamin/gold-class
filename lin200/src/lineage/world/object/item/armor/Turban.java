package lineage.world.object.item.armor;

import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class Turban extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if(item == null)
			item = new Turban();
		// 시간 설정 - 깃털 갯수
		//	: 180개 3시간
		//	: 240개 5시간
		//	: 450개 10시간
		// 기본 시간을 3시간으로 설정.
		item.setNowTime(60*60*3);
		return item;
	}

	@Override
	public void toOption(Character cha, boolean sendPacket) {
		super.toOption(cha, sendPacket);
		
		//
		switch(getItem().getNameIdNumber()) {
			case 5132:	// 드레이크 선장 변신터번
				// turban Drake, 6089
				break;
		}
	}
	
}
