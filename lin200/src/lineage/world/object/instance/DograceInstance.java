package lineage.world.object.instance;

import lineage.bean.database.Npc;
import lineage.bean.database.Shop;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.controller.DogRaceController;
import lineage.world.object.item.RaceTicket;

public class DograceInstance extends SlimeraceInstance {

	public DograceInstance(Npc n){
		super(n);
		
		// 레이스표 넣기.
		n.getShop_list().clear();
		for(int i=0 ; i<5 ; ++i){
			n.getShop_list().add( new Shop("레이스 표", 1, 1) );
			n.getShop_list().get(i).setUid(i);
		}
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		switch(DogRaceController.getStatus()){
			case STOP:
				pc.toSender( S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "maeno5") );
				break;
			case CLEAR:
				pc.toSender( S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "maeno1") );
				break;
			case READY:
			case PLAY:
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "maeno3", null, DogRaceController.getRacerStatus()));
				break;
			default:
				pc.toSender( S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "maeno2") );
				break;
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp){
		if(action.equalsIgnoreCase("status")){
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "maeno4", null, DogRaceController.getRacerStatus()));
		}else{
			super.toTalk(pc, action, type, cbp);
		}
	}
	
	@Override
	public void toTeleport(final int x, final int y, final int map, final boolean effect){
		super.toTeleport(x, y, map, effect);
		// 관리목록에 등록.
		DogRaceController.appendNpc(this);
	}
	
	@Override
	protected boolean isSellAdd(ItemInstance item){
		if(item instanceof RaceTicket){
			RaceTicket rt = (RaceTicket)item;
			return rt.getRacerType().equalsIgnoreCase("dog");
		}
		return false;
	}
	
}
