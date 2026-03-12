package lineage.world.object.item.all_night;

import lineage.database.NpcSpawnlistDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class AutoPotion extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new AutoPotion();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() != null) {
                    if(getName().contains("물약")) {
			NpcSpawnlistDatabase.autoPotion.toTalk((PcInstance) cha, null);
                    } else {
                        cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), cha, "autohunt"));
                    }
                }
	}
}
