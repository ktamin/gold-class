package lineage.world.object.npc.buff;

import lineage.database.PolyDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.ShapeChange;

public class PolymorphMagician extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "newgypsy"));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp){
		if(action.equalsIgnoreCase("해골(10)")){
			type = "해골(10)";
		}else if(action.equalsIgnoreCase("라이칸스로프(10)")){
			type = "라이칸스로프(10)";
		}else if(action.equalsIgnoreCase("구울(10)")){
			type = "구울(10)";
		}else if(action.equalsIgnoreCase("가스트(10)")){
			type = "가스트(10)";
		}else if(action.equalsIgnoreCase("아투바오크(10)")){
			type = "아투바오크(10)";
		}else if(action.equalsIgnoreCase("해골도끼병(10)")){
			type = "해골도끼병(10)";
		}else{
			type = "트롤(15)";
		}
		
		if(type!=null && pc.getInventory().isAden(100, true)){
			ShapeChange.onBuff(pc, pc, PolyDatabase.getPolyName(type), 1800, false, true);
		}else{
			pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
		}
	}

}
