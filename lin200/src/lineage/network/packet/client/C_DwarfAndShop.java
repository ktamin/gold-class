package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class C_DwarfAndShop extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_DwarfAndShop(data, length);
		else
			((C_DwarfAndShop)bp).clone(data, length);
		return bp;
	}
	
	public C_DwarfAndShop(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc){
	    // 버그 방지.
	    if (pc == null || pc.isWorldDelete() || !isRead(4) || pc.getInventory() == null)
	        return this;

	    long objid = readD();  // ★ 한 번만 읽기!

	    if (objid == pc.getObjectId()) {
	        // ★ 등록/삭제 선택 패킷 처리 분기
	        try {
	            if (pc.isPersnalShopInsert()) {
	                pc.addShopItem(pc, this);             // pcshop 대신 pc로 호출 (가장 간단)
	                // (원본 스타일 원하면)
	                // PcInstance pcshop = World.findPc(pc.getObjectId());
	                // pcshop.addShopItem(pc, this);
	            } else if (pc.isPersnalShopSellEdit()) {
	                pc.addDeleteSellShopItem(pc, this);   // pcshop 대신 pc
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    } else {
	        // ★ NPC(또는 F1 임시 상점)로 전달
	        object o = pc.findInsideList(objid);  // ← readD()를 또 하지 말고, 위에서 읽은 objid 사용
	        if (o != null && (pc.getGm() > 0 || !pc.isTransparent())) {
	            o.toDwarfAndShop(pc, this);
	        } else if (o == null && pc.getTempShop() != null) {
	            pc.getTempShop().toDwarfAndShop(pc, this);
	        }
	    }

	    return this;  // ★ 마지막에 한 번만
	}
}
