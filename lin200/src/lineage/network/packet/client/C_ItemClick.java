package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.ClientBasePacket;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class C_ItemClick extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_ItemClick(data, length);
		else
			((C_ItemClick)bp).clone(data, length);
		return bp;
	}
	
	public C_ItemClick(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc){
		// 버그 방지.
		if(pc==null || pc.getInventory()==null || !isRead(4) || pc.isDead() || pc.isWorldDelete())
			return this;
		
		ItemInstance item = pc.getInventory().value( readD() );
		
		if (item.getItem().getName().equalsIgnoreCase("전투가호")){
			ChattingController.toChatting(pc, "해당 아이템은 착용 할 수 없으며 인벤토리에서 효과가 적용됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			return this;
		}

		if(item!=null && item.isClick(pc) && (pc.getGm()>0 || !pc.isTransparent())) {
			// 플러그인 확인.
			if(PluginController.init(C_ItemClick.class, "pcTrade", this, pc, item) == null && PluginController.init(C_ItemClick.class, "pcShop", this, pc, item) == null)
				item.toClick(pc, this);
		}
		
		return this;
	}
}
