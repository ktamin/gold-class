package lineage.world.object.item.yadolan;

import java.util.List;

import lineage.database.CharactersDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 좌표복구주문서 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 좌표복구주문서();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if(cha instanceof PcInstance) {
			PcInstance pc = (PcInstance)cha;
			
			List<String> list = CharactersDatabase.getCharacters(pc.getClient().getAccountId());
			for(String name : list) {
				if(cha.getName().equalsIgnoreCase(name))
					continue;
				
				CharactersDatabase.updateLocation(null, name, 33428, 32823, 4);
				ChattingController.toChatting(cha, String.format("\\fY'%s' 케릭터의 좌표가 복구되었습니다.", name), Lineage.CHATTING_MODE_MESSAGE);
			}
			list.clear();
			// 아이템 수량 갱신
			cha.getInventory().count(this, getCount() - 1, true);
		}
	}
}
