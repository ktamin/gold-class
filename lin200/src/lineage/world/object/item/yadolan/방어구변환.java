package lineage.world.object.item.yadolan;

import lineage.database.NpcSpawnlistDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 방어구변환 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 방어구변환();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory().isWeightPercent(60)== false) {
			ChattingController.toChatting(cha, "무게 60% 초과시 변환이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
		}
		if (cha.getInventory() != null){
			NpcSpawnlistDatabase.armornchange.toTalk((PcInstance) cha, null);
		}
	}
}
