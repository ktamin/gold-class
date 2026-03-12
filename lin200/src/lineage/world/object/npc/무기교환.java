package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 무기교환 extends object {
	
	public class CreateItem {
		public String itemName;
		public boolean isCheckBless;
		public int bless;
		public boolean isCheckEnchant;
		public int enchant;
		public int count;
		public int yitem=0; 
				
		/**
		 * @param itemName			: 재료 아이템 이름
		 * @param isCheckBless		: 재료 축여부 체크
		 * @param bless				: 축복(0~2)
		 * @param isCheckEnchant	: 재료 인첸트 체크
		 * @param enchant			: 인첸트
		 * @param count				: 수량
		 */
		public CreateItem(String itemName, boolean isCheckBless, int bless, boolean isCheckEnchant, int enchant, int count) {
			this.itemName = itemName;
			this.isCheckBless = isCheckBless;
			this.bless = bless;
			this.isCheckEnchant = isCheckEnchant;
			this.enchant = enchant;
			this.count = count;
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "weaponCreate"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
		if (pc.getInventory() != null) {
	
			
			if (action.equalsIgnoreCase("가이아의격노")) {
				
	
					if(pc.getInventory().getSlot(Lineage.SLOT_WEAPON) == null){
						ChattingController.toChatting(pc, "변환 하실 무기를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					if(pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("가이아의 격노")){
						ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
					if(pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("수정 결정체 지팡이")
					    || pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("진명황의 집행검")
						|| pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("포르세의 검")
						|| pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("바람칼날의 단검")){
						
				
							ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_WEAPON);
							ItemInstance item2 =pc.getInventory().find("무기 변환");
							
							ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("가이아의 격노"));
							
							if(pc.getInventory().find("무기 변환") != null){
								ii.setCount(1);
								ii.setEnLevel(item.getEnLevel());
								ii.setDefinite(true);
						
								ii.setBless(item.getBless());
							
								pc.getInventory().getSlot(Lineage.SLOT_WEAPON).toClick(pc, null);
								pc.getInventory().count(item, item.getCount() - item.getCount(), true);
								pc.toGiveItem(null, ii, ii.getCount());
								pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
								List<String> ynlist = new ArrayList<String>();
								
								ynlist.add(String.format("%s", ii.getItem().getName()));	
		
								pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "wcinfo", null, ynlist));
							}else{
								ChattingController.toChatting(pc, "무기 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
								return;
							}
							
					}
				}
		
			
			if (action.equalsIgnoreCase("진명황의집행검")) {
				
					if(pc.getInventory().getSlot(Lineage.SLOT_WEAPON) == null){
						ChattingController.toChatting(pc, "변환 하실 무기를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
					if(pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("진명황의 집행검")){
						ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다..", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
					if(pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("수정 결정체 지팡이")
					    || pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("가이아의 격노")
						|| pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("포르세의 검")
						|| pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("바람칼날의 단검")){
						
				
							ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_WEAPON);
							ItemInstance item2 =pc.getInventory().find("무기 변환");
							
							ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("진명황의 집행검"));
							if(pc.getInventory().find("무기 변환") != null){
								ii.setCount(1);
								ii.setEnLevel(item.getEnLevel());
								ii.setDefinite(true);
						
								ii.setBless(item.getBless());
							
			
								pc.getInventory().getSlot(Lineage.SLOT_WEAPON).toClick(pc, null);
								pc.getInventory().count(item, item.getCount() - item.getCount(), true);
								pc.toGiveItem(null, ii, ii.getCount());
								pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
								List<String> ynlist = new ArrayList<String>();
								
								ynlist.add(String.format("%s", ii.getItem().getName()));	
		
								pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "wcinfo", null, ynlist));
							}
				
							
					}else{
						ChattingController.toChatting(pc, "신화급 무기만 변경이 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
				}
			if (action.equalsIgnoreCase("수정결정체지팡이")) {
			
					
					if(pc.getInventory().getSlot(Lineage.SLOT_WEAPON) == null){
						ChattingController.toChatting(pc, "변환 하실 무기를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
					if(pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("수정 결정체 지팡이")){
						ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
					if(pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("진명황의 집행검")
					    || pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("가이아의 격노")
						|| pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("포르세의 검")
						|| pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("바람칼날의 단검")){
						
					
							ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_WEAPON);
							
							ItemInstance item2 =pc.getInventory().find("무기 변환");
							
							ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("수정 결정체 지팡이"));
							
							if(pc.getInventory().find("무기 변환") != null){
								ii.setCount(1);
								ii.setEnLevel(item.getEnLevel());
								ii.setDefinite(true);
						
								ii.setBless(item.getBless());
							
			
								pc.getInventory().getSlot(Lineage.SLOT_WEAPON).toClick(pc, null);
								pc.getInventory().count(item, item.getCount() - item.getCount(), true);
								pc.toGiveItem(null, ii, ii.getCount());
								pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
								List<String> ynlist = new ArrayList<String>();
								
								ynlist.add(String.format("%s", ii.getItem().getName()));	
		
								pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "wcinfo", null, ynlist));
							}
							
							
					}else{
						ChattingController.toChatting(pc, "신화급 무기만 변경이 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
				}

			if (action.equalsIgnoreCase("포르세의검")) {
					if(pc.getInventory().getSlot(Lineage.SLOT_WEAPON) == null){
						ChattingController.toChatting(pc, "변환 하실 무기를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					if(pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("포르세의 검")){
						ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
				
					if(pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("진명황의 집행검")
					    || pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("가이아의 격노")
						|| pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("수정 결정체 지팡이")
						|| pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("바람칼날의 단검")){
						
					
							ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_WEAPON);
							ItemInstance item2 =pc.getInventory().find("무기 변환");
							
							
							ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("포르세의 검"));
							
							if(pc.getInventory().find("무기 변환") != null){
								ii.setCount(1);
								ii.setEnLevel(item.getEnLevel());
								ii.setDefinite(true);
						
								ii.setBless(item.getBless());
							
			
								pc.getInventory().getSlot(Lineage.SLOT_WEAPON).toClick(pc, null);
								pc.getInventory().count(item, item.getCount() - item.getCount(), true);
								pc.toGiveItem(null, ii, ii.getCount());
								pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
								List<String> ynlist = new ArrayList<String>();
								
								ynlist.add(String.format("%s", ii.getItem().getName()));	
		
								pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "wcinfo", null, ynlist));
							}
						

					}else{
						ChattingController.toChatting(pc, "신화급 무기만 변경이 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
				}
	
			if (action.equalsIgnoreCase("바람칼날의단검")) {
				
				if(pc.getInventory().getSlot(Lineage.SLOT_WEAPON) == null){
					ChattingController.toChatting(pc, "변환 하실 무기를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("바람칼날의 단검")){
					ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
	
					if(pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("진명황의 집행검")
					    || pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("가이아의 격노")
						|| pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("수정 결정체 지팡이")
						|| pc.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getName().equalsIgnoreCase("포르세의 검")){
						
					
							ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_WEAPON);
							ItemInstance item2 =pc.getInventory().find("무기 변환");
							
							
							ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("바람칼날의 단검"));
							if(pc.getInventory().find("무기 변환") != null){
								ii.setCount(1);
								ii.setEnLevel(item.getEnLevel());
								ii.setDefinite(true);
						
								ii.setBless(item.getBless());
							
			
								pc.getInventory().getSlot(Lineage.SLOT_WEAPON).toClick(pc, null);
								pc.getInventory().count(item, item.getCount() - item.getCount(), true);
								pc.toGiveItem(null, ii, ii.getCount());
								pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
								List<String> ynlist = new ArrayList<String>();
								
								ynlist.add(String.format("%s", ii.getItem().getName()));	
		
								pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "wcinfo", null, ynlist));
							}
							

					}else{
						ChattingController.toChatting(pc, "신화급 무기만 변경이 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
			}
			
		}
	}
}