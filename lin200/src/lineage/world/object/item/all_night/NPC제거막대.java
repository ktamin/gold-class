package lineage.world.object.item.all_night;

import lineage.bean.database.Npc;
import lineage.database.NpcDatabase;
import lineage.database.NpcSpawnlistDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;

public class NPC제거막대 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new NPC제거막대();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		int obj_id = cbp.readD();
		int x = cbp.readH();
		int y = cbp.readH();
		
		object o = cha.findInsideList(obj_id);
		
		if (o != null) {
			Npc n = NpcDatabase.findNameid(o.getName());
			
			if (n == null)
				return;
			
			NpcSpawnlistDatabase.delete(n);
			ChattingController.toChatting(cha, String.format("[%s] 삭제 완료.", n.getName()), Lineage.CHATTING_MODE_MESSAGE);
			
			o.clearList(true);
			World.remove(o);
		}
	}
}
