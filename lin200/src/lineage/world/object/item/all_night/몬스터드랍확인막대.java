package lineage.world.object.item.all_night;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Drop;
import lineage.database.MonsterDropDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;

public class 몬스터드랍확인막대 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 몬스터드랍확인막대();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha != null) {
			int obj_id = cbp.readD();

			object o = cha.findInsideList(obj_id);

			if (o != null && o instanceof MonsterInstance) {
				MonsterInstance mi = (MonsterInstance) o;

				if (mi != null && mi.getMonster() != null) {
					String name = mi.getMonster().getName();
					List<String> list = new ArrayList<String>();

					list.add(name);

					int idx = 0;
					for (Drop d : MonsterDropDatabase.getDropList()) {
						if (list.size() >= 250)
							break;

						if (d.getMonName().equalsIgnoreCase(name)) {
							if (d.getItemBress() == 1)
								list.add(String.format("%d. %s", ++idx, d.getItemName()));
							else
								list.add(String.format("%d. %s", ++idx, String.format("(%s) %s", d.getItemBress() == 0 ? "축" : "저주", d.getItemName())));
						}
					}

					if (list.size() < 2)
						cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), cha, "mobdrop1", null, list));
					else
						cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), cha, "mobdrop", null, list));
				}
			}
		}
	}
}
