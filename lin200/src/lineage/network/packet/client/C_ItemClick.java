package lineage.network.packet.client;

import lineage.network.packet.BasePacket;
import lineage.network.packet.ClientBasePacket;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class C_ItemClick extends ClientBasePacket {
/*	
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
*/	
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
		
		if (java.util.Arrays.asList("전투가호", "전투의 룬", "방어의 룬", "생명의 룬", "드래곤의 진주").contains(item.getItem().getName())) {
			ChattingController.toChatting(pc, "해당 아이템은 착용 할 수 없으며 인벤토리에서 효과가 적용됩니다.", Lineage.CHATTING_MODE_MESSAGE);
			return this;
		}

		if(item!=null && item.isClick(pc) && (pc.getGm()>0 || !pc.isTransparent())) {
			
			// =========================================================
			// 💡 [추가] 통합 거래소 아이템 판매 등록 모드 가로채기!
			// =========================================================
			if (pc.getExchangeShopStep() == 1) {
				// 1. 착용 중인 아이템인지 검사
				if (item.isEquipped()) {
					ChattingController.toChatting(pc, "\\fR착용 중인 아이템은 거래소에 등록할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
					return this;
				}
				// 2. 거래 불가능 아이템인지 검사
				if (!item.getItem().isTrade() || item.getBless() < 0) {
					ChattingController.toChatting(pc, "\\fR거래가 불가능한 아이템입니다.", Lineage.CHATTING_MODE_MESSAGE);
					return this;
				}
				// 3. 아데나/코인 자체는 등록 못 하게 방지
				if (item.getItem().getNameIdNumber() == 4 || item.getItem().getName().equalsIgnoreCase("코인")) {
					ChattingController.toChatting(pc, "\\fR화폐는 판매 물품으로 등록할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
					return this;
				}

				// 💡 4. 거래소 등록 처리 메서드 호출!
				// (아래 주석 참고: 운영자님이 설계하신 방식에 따라 거래소 등록 메서드를 호출합니다)
				// 예시 A (컨트롤러를 사용하는 경우):
				// ExchangeController.appendShopItem(pc, item);
				
				// 예시 B (단순 알림 및 테스트용 - 실제 등록 메서드로 연결해 주세요):
				ChattingController.toChatting(pc, "\\fY[" + item.getItem().getName() + "] 거래소 등록이 처리됩니다.", Lineage.CHATTING_MODE_MESSAGE);
				
				// 5. 처리가 끝났으므로 상태 초기화 (일반 사용 toClick이 실행되지 않고 여기서 종료!)
				pc.setExchangeShopStep(0);
				return this;
			}
			// =========================================================

			// 플러그인 확인.
			if(PluginController.init(C_ItemClick.class, "pcTrade", this, pc, item) == null && PluginController.init(C_ItemClick.class, "pcShop", this, pc, item) == null)
				item.toClick(pc, this);
		}
		
		return this;
	}
}