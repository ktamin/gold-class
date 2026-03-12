package lineage.world.object.npc.event;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.world.controller.CraftController;
import lineage.world.object.instance.EventInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class FishingBoy extends EventInstance {

	List<Item> list;
	
	public FishingBoy(Npc npc) {
		super(npc);
		list = new ArrayList<Item>();
		list.add( ItemDatabase.find( 5246 ) );
		list.add( ItemDatabase.find( 5275 ) );
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		if(getMap()==4)
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "fk_in_1"));
		else
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "fk_out_1"));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp){
		if(action.startsWith("L"))
			in_fishing(pc, true);
		else if(action.startsWith("S"))
			in_fishing(pc, false);
		else
			out_fishing(pc);
	}
	
	/**
	 * 낚시터 입장
	 * type : true(긴낚싯대), false(짧은낚싯대)
	 */
	private void in_fishing(PcInstance pc, boolean type){
		if(pc.getInventory().isAden(1000, true)){
			CraftController.toCraft(this, pc, list.get(type ? 0 : 1), 1, true);
			pc.toTeleport(32814, 32809, 5124, true);
		}else{
			pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
		}
		
	}
	
	/**
	 * 낚시터 퇴장
	 */
	private void out_fishing(PcInstance pc){
		for(Item item : list) {
			ItemInstance ii = pc.getInventory().find(item);
			//
			pc.getInventory().count(ii, 0, true);
		}
		pc.toTeleport(32624, 32812, 4, true);
	}
	
}
