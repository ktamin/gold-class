package lineage.world.object.npc.shop;

import lineage.bean.database.Npc;
import lineage.database.NpcSpawnlistDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.KingdomController;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.ShopInstance;

public class BuyShop3 extends ShopInstance {
	
	public BuyShop3(Npc npc){
		super(npc);
		kingdom = KingdomController.find(Lineage.KINGDOM_GIRAN);
	}
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "yadoshop2"));
		
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {

		if (action.equalsIgnoreCase("a")) {
			for (ShopInstance si : NpcSpawnlistDatabase.getShopList()) {
				
				if (si.getNpc().getName().contains("무기 상인")){
					si.toTalk((PcInstance) pc, null);
	
			
				}
			}
		}
		if (action.equalsIgnoreCase("b")) {
			for (ShopInstance si : NpcSpawnlistDatabase.getShopList()) {
				
				if (si.getNpc().getName().contains("방어구 상인")){
					si.toTalk((PcInstance) pc, null);
	
			
				}
			}
		}
		if (action.equalsIgnoreCase("c")) {
			for (ShopInstance si : NpcSpawnlistDatabase.getShopList()) {
				
				if (si.getNpc().getName().contains("마법서 상인")){
					si.toTalk((PcInstance) pc, null);
	
			
				}
			}
		}
		
//		super.toTalk(pc, "buy", null, null);
	}

}
