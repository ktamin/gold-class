package lineage.world.object.item.yadolan;

import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 자동사냥캐릭시간초기화 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 자동사냥캐릭시간초기화();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		PcInstance pc = (PcInstance)cha;
		
		if (cha.getInventory() != null) {
			
			pc.auto_hunt_time = Lineage.auto_hunt_time;
			
			ChattingController.toChatting(cha, "자동사냥 계정자동사냥 캐릭 시간 초기화 시간이 초기화 되었습니다..", Lineage.CHATTING_MODE_MESSAGE);
			cha.getInventory().count(this, getCount()-1, true);
		}
	}
}
