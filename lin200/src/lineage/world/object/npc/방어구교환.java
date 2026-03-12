package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.controller.악마왕의영토컨트롤러;
import lineage.world.controller.얼던컨트롤러;
import lineage.world.controller.지옥컨트롤러;
import lineage.world.controller.테베라스컨트롤러;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 방어구교환 extends object {
	
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
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "archange"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
		if (pc.getInventory() != null) {
			if (action.equalsIgnoreCase("a1")) {
				
				
				if(pc.getInventory().getSlot(Lineage.SLOT_HELM) == null){
					ChattingController.toChatting(pc, "변환 하실 투구를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(pc.getInventory().getSlot(Lineage.SLOT_HELM).getItem().getName().equalsIgnoreCase("가디언의 투구")){
					ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				if(pc.getInventory().getSlot(Lineage.SLOT_HELM).getItem().getName().equalsIgnoreCase("머미로드의 왕관")
				    || pc.getInventory().getSlot(Lineage.SLOT_HELM).getItem().getName().equalsIgnoreCase("기사 대장의 면갑")){
					
			
						ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_HELM);
						ItemInstance item2 =pc.getInventory().find("방어구 변환");
						
						ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("가디언의 투구"));
						
						if(pc.getInventory().find("방어구 변환") != null){
							ii.setCount(1);
							ii.setEnLevel(item.getEnLevel());
							ii.setDefinite(true);
					
							ii.setBless(item.getBless());
						
							pc.getInventory().getSlot(Lineage.SLOT_HELM).toClick(pc, null);
							pc.getInventory().count(item, item.getCount() - item.getCount(), true);
							pc.toGiveItem(null, ii, ii.getCount());
							pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
	
						}else{
							ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
							return;
						}
						
				}
			}
			if (action.equalsIgnoreCase("a2")) {
				
				
				if(pc.getInventory().getSlot(Lineage.SLOT_HELM) == null){
					ChattingController.toChatting(pc, "변환 하실 투구를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(pc.getInventory().getSlot(Lineage.SLOT_HELM).getItem().getName().equalsIgnoreCase("머미로드의 왕관")){
					ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				if(pc.getInventory().getSlot(Lineage.SLOT_HELM).getItem().getName().equalsIgnoreCase("가디언의 투구")
				    || pc.getInventory().getSlot(Lineage.SLOT_HELM).getItem().getName().equalsIgnoreCase("기사 대장의 면갑")){
					
			
						ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_HELM);
						ItemInstance item2 =pc.getInventory().find("방어구 변환");
						
						ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("머미로드의 왕관"));
						
						if(pc.getInventory().find("방어구 변환") != null){
							ii.setCount(1);
							ii.setEnLevel(item.getEnLevel());
							ii.setDefinite(true);
					
							ii.setBless(item.getBless());
						
							pc.getInventory().getSlot(Lineage.SLOT_HELM).toClick(pc, null);
							pc.getInventory().count(item, item.getCount() - item.getCount(), true);
							pc.toGiveItem(null, ii, ii.getCount());
							pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
	
						}else{
							ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
							return;
						}
						
				}
			}	
			if (action.equalsIgnoreCase("a3")) {
				
				
				if(pc.getInventory().getSlot(Lineage.SLOT_HELM) == null){
					ChattingController.toChatting(pc, "변환 하실 투구를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(pc.getInventory().getSlot(Lineage.SLOT_HELM).getItem().getName().equalsIgnoreCase("기사 대장의 면갑")){
					ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				if(pc.getInventory().getSlot(Lineage.SLOT_HELM).getItem().getName().equalsIgnoreCase("가디언의 투구")
				    || pc.getInventory().getSlot(Lineage.SLOT_HELM).getItem().getName().equalsIgnoreCase("머미로드의 왕관")){
					
			
						ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_HELM);
						ItemInstance item2 =pc.getInventory().find("방어구 변환");
						
						ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("기사 대장의 면갑"));
						
						if(pc.getInventory().find("방어구 변환") != null){
							ii.setCount(1);
							ii.setEnLevel(item.getEnLevel());
							ii.setDefinite(true);
					
							ii.setBless(item.getBless());
						
							pc.getInventory().getSlot(Lineage.SLOT_HELM).toClick(pc, null);
							pc.getInventory().count(item, item.getCount() - item.getCount(), true);
							pc.toGiveItem(null, ii, ii.getCount());
							pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
	
						}else{
							ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
							return;
						}
						
				}
			}
			if (action.equalsIgnoreCase("b1")) {
				
				
				if(pc.getInventory().getSlot(Lineage.SLOT_ARMOR) == null){
					ChattingController.toChatting(pc, "변환 하실 갑옷을 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(pc.getInventory().getSlot(Lineage.SLOT_ARMOR).getItem().getName().equalsIgnoreCase("리치 로브")){
					ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				if(pc.getInventory().getSlot(Lineage.SLOT_ARMOR).getItem().getName().equalsIgnoreCase("나이트발드의 갑옷")
				    || pc.getInventory().getSlot(Lineage.SLOT_ARMOR).getItem().getName().equalsIgnoreCase("뱀파이어 가죽 로브")){
					
			
						ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_ARMOR);
						ItemInstance item2 =pc.getInventory().find("방어구 변환");
						
						ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("리치 로브"));
						
						if(pc.getInventory().find("방어구 변환") != null){
							ii.setCount(1);
							ii.setEnLevel(item.getEnLevel());
							ii.setDefinite(true);
					
							ii.setBless(item.getBless());
						
							pc.getInventory().getSlot(Lineage.SLOT_ARMOR).toClick(pc, null);
							pc.getInventory().count(item, item.getCount() - item.getCount(), true);
							pc.toGiveItem(null, ii, ii.getCount());
							pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
	
						}else{
							ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
							return;
						}
						
				}
			}
			if (action.equalsIgnoreCase("b2")) {
				
				
				if(pc.getInventory().getSlot(Lineage.SLOT_ARMOR) == null){
					ChattingController.toChatting(pc, "변환 하실 갑옷을 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(pc.getInventory().getSlot(Lineage.SLOT_ARMOR).getItem().getName().equalsIgnoreCase("나이트발드의 갑옷")){
					ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				if(pc.getInventory().getSlot(Lineage.SLOT_ARMOR).getItem().getName().equalsIgnoreCase("리치 로브")
				    || pc.getInventory().getSlot(Lineage.SLOT_ARMOR).getItem().getName().equalsIgnoreCase("뱀파이어 가죽 로브")){
					
			
						ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_ARMOR);
						ItemInstance item2 =pc.getInventory().find("방어구 변환");
						
						ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("나이트발드의 갑옷"));
						
						if(pc.getInventory().find("방어구 변환") != null){
							ii.setCount(1);
							ii.setEnLevel(item.getEnLevel());
							ii.setDefinite(true);
					
							ii.setBless(item.getBless());
						
							pc.getInventory().getSlot(Lineage.SLOT_ARMOR).toClick(pc, null);
							pc.getInventory().count(item, item.getCount() - item.getCount(), true);
							pc.toGiveItem(null, ii, ii.getCount());
							pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
	
						}else{
							ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
							return;
						}
						
				}
			}
			if (action.equalsIgnoreCase("b3")) {
				
				
				if(pc.getInventory().getSlot(Lineage.SLOT_ARMOR) == null){
					ChattingController.toChatting(pc, "변환 하실 갑옷을 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(pc.getInventory().getSlot(Lineage.SLOT_ARMOR).getItem().getName().equalsIgnoreCase("뱀파이어 가죽 로브")){
					ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				if(pc.getInventory().getSlot(Lineage.SLOT_ARMOR).getItem().getName().equalsIgnoreCase("리치 로브")
				    || pc.getInventory().getSlot(Lineage.SLOT_ARMOR).getItem().getName().equalsIgnoreCase("나이트발드의 갑옷")){
					
			
						ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_ARMOR);
						ItemInstance item2 =pc.getInventory().find("방어구 변환");
						
						ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("뱀파이어 가죽 로브"));
						
						if(pc.getInventory().find("방어구 변환") != null){
							ii.setCount(1);
							ii.setEnLevel(item.getEnLevel());
							ii.setDefinite(true);
					
							ii.setBless(item.getBless());
						
							pc.getInventory().getSlot(Lineage.SLOT_ARMOR).toClick(pc, null);
							pc.getInventory().count(item, item.getCount() - item.getCount(), true);
							pc.toGiveItem(null, ii, ii.getCount());
							pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
	
						}else{
							ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
							return;
						}
						
				}
			}
		if (action.equalsIgnoreCase("c1")) {
				
				
				if(pc.getInventory().getSlot(Lineage.SLOT_GLOVE) == null){
					ChattingController.toChatting(pc, "변환 하실 장갑을 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(pc.getInventory().getSlot(Lineage.SLOT_GLOVE).getItem().getName().equalsIgnoreCase("대마법사의 장갑")){
					ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				if(pc.getInventory().getSlot(Lineage.SLOT_GLOVE).getItem().getName().equalsIgnoreCase("격분의 장갑")
				    || pc.getInventory().getSlot(Lineage.SLOT_GLOVE).getItem().getName().equalsIgnoreCase("아이리스의 장갑")){
					
			
						ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_GLOVE);
						ItemInstance item2 =pc.getInventory().find("방어구 변환");
						
						ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("대마법사의 장갑"));
						
						if(pc.getInventory().find("방어구 변환") != null){
							ii.setCount(1);
							ii.setEnLevel(item.getEnLevel());
							ii.setDefinite(true);
					
							ii.setBless(item.getBless());
						
							pc.getInventory().getSlot(Lineage.SLOT_GLOVE).toClick(pc, null);
							pc.getInventory().count(item, item.getCount() - item.getCount(), true);
							pc.toGiveItem(null, ii, ii.getCount());
							pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
	
						}else{
							ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
							return;
						}
						
				}
			}
		if (action.equalsIgnoreCase("c2")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_GLOVE) == null){
				ChattingController.toChatting(pc, "변환 하실 장갑을 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_GLOVE).getItem().getName().equalsIgnoreCase("격분의 장갑")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_GLOVE).getItem().getName().equalsIgnoreCase("대마법사의 장갑")
			    || pc.getInventory().getSlot(Lineage.SLOT_GLOVE).getItem().getName().equalsIgnoreCase("아이리스의 장갑")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_GLOVE);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("격분의 장갑"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_GLOVE).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("c3")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_GLOVE) == null){
				ChattingController.toChatting(pc, "변환 하실 장갑을 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_GLOVE).getItem().getName().equalsIgnoreCase("아이리스의 장갑")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_GLOVE).getItem().getName().equalsIgnoreCase("대마법사의 장갑")
			    || pc.getInventory().getSlot(Lineage.SLOT_GLOVE).getItem().getName().equalsIgnoreCase("격분의 장갑")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_GLOVE);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("아이리스의 장갑"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_GLOVE).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("d1")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_BOOTS) == null){
				ChattingController.toChatting(pc, "변환 하실 부츠를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_BOOTS).getItem().getName().equalsIgnoreCase("아이리스의 부츠")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_BOOTS).getItem().getName().equalsIgnoreCase("뱀파이어의 부츠")
			    || pc.getInventory().getSlot(Lineage.SLOT_BOOTS).getItem().getName().equalsIgnoreCase("가디언의 부츠")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_BOOTS);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("아이리스의 부츠"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_BOOTS).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("d2")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_BOOTS) == null){
				ChattingController.toChatting(pc, "변환 하실 부츠를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_BOOTS).getItem().getName().equalsIgnoreCase("뱀파이어의 부츠")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_BOOTS).getItem().getName().equalsIgnoreCase("아이리스의 부츠")
			    || pc.getInventory().getSlot(Lineage.SLOT_BOOTS).getItem().getName().equalsIgnoreCase("가디언의 부츠")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_BOOTS);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("뱀파이어의 부츠"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_BOOTS).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("d3")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_BOOTS) == null){
				ChattingController.toChatting(pc, "변환 하실 부츠를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_BOOTS).getItem().getName().equalsIgnoreCase("가디언의 부츠")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_BOOTS).getItem().getName().equalsIgnoreCase("아이리스의 부츠")
			    || pc.getInventory().getSlot(Lineage.SLOT_BOOTS).getItem().getName().equalsIgnoreCase("뱀파이어의 부츠")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_BOOTS);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("가디언의 부츠"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_BOOTS).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("e1")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_CLOAK) == null){
				ChattingController.toChatting(pc, "변환 하실 방어구를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_CLOAK).getItem().getName().equalsIgnoreCase("쿠거가죽 망토")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_CLOAK).getItem().getName().equalsIgnoreCase("가디언의 망토")
			    || pc.getInventory().getSlot(Lineage.SLOT_CLOAK).getItem().getName().equalsIgnoreCase("뱀파이어의 망토")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_CLOAK);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("쿠거가죽 망토"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_CLOAK).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("e2")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_CLOAK) == null){
				ChattingController.toChatting(pc, "변환 하실 방어구를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_CLOAK).getItem().getName().equalsIgnoreCase("가디언의 망토")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_CLOAK).getItem().getName().equalsIgnoreCase("쿠거가죽 망토")
			    || pc.getInventory().getSlot(Lineage.SLOT_CLOAK).getItem().getName().equalsIgnoreCase("뱀파이어의 망토")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_CLOAK);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("가디언의 망토"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_CLOAK).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("e3")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_CLOAK) == null){
				ChattingController.toChatting(pc, "변환 하실 방어구를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_CLOAK).getItem().getName().equalsIgnoreCase("뱀파이어의 망토")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_CLOAK).getItem().getName().equalsIgnoreCase("쿠거가죽 망토")
			    || pc.getInventory().getSlot(Lineage.SLOT_CLOAK).getItem().getName().equalsIgnoreCase("가디언의 망토")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_CLOAK);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("뱀파이어의 망토"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_CLOAK).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("f1")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_SHIRT) == null){
				ChattingController.toChatting(pc, "변환 하실 티셔츠를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_SHIRT).getItem().getName().equalsIgnoreCase("빛나는 지식의 티셔츠")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_SHIRT).getItem().getName().equalsIgnoreCase("빛나는 민첩의 티셔츠")
			    || pc.getInventory().getSlot(Lineage.SLOT_SHIRT).getItem().getName().equalsIgnoreCase("빛나는 완력의 티셔츠")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_SHIRT);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("빛나는 지식의 티셔츠"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_SHIRT).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("f2")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_SHIRT) == null){
				ChattingController.toChatting(pc, "변환 하실 티셔츠를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_SHIRT).getItem().getName().equalsIgnoreCase("빛나는 민첩의 티셔츠")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_SHIRT).getItem().getName().equalsIgnoreCase("빛나는 지식의 티셔츠")
			    || pc.getInventory().getSlot(Lineage.SLOT_SHIRT).getItem().getName().equalsIgnoreCase("빛나는 완력의 티셔츠")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_SHIRT);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("빛나는 민첩의 티셔츠"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_SHIRT).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("f3")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_SHIRT) == null){
				ChattingController.toChatting(pc, "변환 하실 티셔츠를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_SHIRT).getItem().getName().equalsIgnoreCase("빛나는 완력의 티셔츠")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_SHIRT).getItem().getName().equalsIgnoreCase("빛나는 지식의 티셔츠")
			    || pc.getInventory().getSlot(Lineage.SLOT_SHIRT).getItem().getName().equalsIgnoreCase("빛나는 민첩의 티셔츠")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_SHIRT);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("빛나는 완력의 티셔츠"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_SHIRT).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("g1")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_SHIELD) == null){
				ChattingController.toChatting(pc, "변환 하실 방패를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_SHIELD).getItem().getName().equalsIgnoreCase("시어의 심안")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_SHIELD).getItem().getName().equalsIgnoreCase("반역자의 방패")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_SHIELD);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("시어의 심안"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_SHIELD).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("g2")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_SHIELD) == null){
				ChattingController.toChatting(pc, "변환 하실 방패를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_SHIELD).getItem().getName().equalsIgnoreCase("반역자의 방패")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_SHIELD).getItem().getName().equalsIgnoreCase("시어의 심안")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_SHIELD);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("반역자의 방패"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_SHIELD).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("h1")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_GUARDER) == null){
				ChattingController.toChatting(pc, "변환 하실 가더를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_GUARDER).getItem().getName().equalsIgnoreCase("화령의 가더")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_GUARDER).getItem().getName().equalsIgnoreCase("풍령의 가더")
				    || pc.getInventory().getSlot(Lineage.SLOT_GUARDER).getItem().getName().equalsIgnoreCase("수령의 가더")
				    || pc.getInventory().getSlot(Lineage.SLOT_GUARDER).getItem().getName().equalsIgnoreCase("지령의 가더")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_GUARDER);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("화령의 가더"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_GUARDER).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("h2")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_GUARDER) == null){
				ChattingController.toChatting(pc, "변환 하실 가더를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_GUARDER).getItem().getName().equalsIgnoreCase("풍령의 가더")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_GUARDER).getItem().getName().equalsIgnoreCase("화령의 가더")
				    || pc.getInventory().getSlot(Lineage.SLOT_GUARDER).getItem().getName().equalsIgnoreCase("수령의 가더")
				    || pc.getInventory().getSlot(Lineage.SLOT_GUARDER).getItem().getName().equalsIgnoreCase("지령의 가더")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_GUARDER);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("풍령의 가더"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_GUARDER).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("h3")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_GUARDER) == null){
				ChattingController.toChatting(pc, "변환 하실 가더를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_GUARDER).getItem().getName().equalsIgnoreCase("수령의 가더")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_GUARDER).getItem().getName().equalsIgnoreCase("화령의 가더")
				    || pc.getInventory().getSlot(Lineage.SLOT_GUARDER).getItem().getName().equalsIgnoreCase("풍령의 가더")
				    || pc.getInventory().getSlot(Lineage.SLOT_GUARDER).getItem().getName().equalsIgnoreCase("지령의 가더")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_GUARDER);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("수령의 가더"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_GUARDER).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("h4")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_GUARDER) == null){
				ChattingController.toChatting(pc, "변환 하실 가더를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_GUARDER).getItem().getName().equalsIgnoreCase("지령의 가더")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_GUARDER).getItem().getName().equalsIgnoreCase("화령의 가더")
				    || pc.getInventory().getSlot(Lineage.SLOT_GUARDER).getItem().getName().equalsIgnoreCase("풍령의 가더")
				    || pc.getInventory().getSlot(Lineage.SLOT_GUARDER).getItem().getName().equalsIgnoreCase("수령의 가더")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_GUARDER);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("지령의 가더"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_GUARDER).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("i1")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_BELT) == null){
				ChattingController.toChatting(pc, "변환 하실 벨트를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_BELT).getItem().getName().equalsIgnoreCase("지식의 벨트")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_BELT).getItem().getName().equalsIgnoreCase("민첩의 벨트")
				    || pc.getInventory().getSlot(Lineage.SLOT_BELT).getItem().getName().equalsIgnoreCase("완력의 벨트")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_BELT);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("지식의 벨트"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_BELT).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("i2")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_BELT) == null){
				ChattingController.toChatting(pc, "변환 하실 벨트를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_BELT).getItem().getName().equalsIgnoreCase("민첩의 벨트")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_BELT).getItem().getName().equalsIgnoreCase("지식의 벨트")
				    || pc.getInventory().getSlot(Lineage.SLOT_BELT).getItem().getName().equalsIgnoreCase("완력의 벨트")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_BELT);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("민첩의 벨트"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_BELT).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("i3")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_BELT) == null){
				ChattingController.toChatting(pc, "변환 하실 벨트를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_BELT).getItem().getName().equalsIgnoreCase("완력의 벨트")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_BELT).getItem().getName().equalsIgnoreCase("지식의 벨트")
				    || pc.getInventory().getSlot(Lineage.SLOT_BELT).getItem().getName().equalsIgnoreCase("민첩의 벨트")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_BELT);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("완력의 벨트"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_BELT).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("j1")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_BELT) == null){
				ChattingController.toChatting(pc, "변환 하실 벨트를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_BELT).getItem().getName().equalsIgnoreCase("신성한 지식의 벨트")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_BELT).getItem().getName().equalsIgnoreCase("신성한 민첩의 벨트")
				    || pc.getInventory().getSlot(Lineage.SLOT_BELT).getItem().getName().equalsIgnoreCase("신성한 완력의 벨트")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_BELT);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("신성한 지식의 벨트"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_BELT).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("j2")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_BELT) == null){
				ChattingController.toChatting(pc, "변환 하실 벨트를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_BELT).getItem().getName().equalsIgnoreCase("신성한 민첩의 벨트")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_BELT).getItem().getName().equalsIgnoreCase("신성한 지식의 벨트")
				    || pc.getInventory().getSlot(Lineage.SLOT_BELT).getItem().getName().equalsIgnoreCase("신성한 완력의 벨트")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_BELT);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("신성한 민첩의 벨트"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_BELT).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("j3")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_BELT) == null){
				ChattingController.toChatting(pc, "변환 하실 벨트를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_BELT).getItem().getName().equalsIgnoreCase("신성한 완력의 벨트")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_BELT).getItem().getName().equalsIgnoreCase("신성한 지식의 벨트")
				    || pc.getInventory().getSlot(Lineage.SLOT_BELT).getItem().getName().equalsIgnoreCase("신성한 민첩의 벨트")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_BELT);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("신성한 완력의 벨트"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_BELT).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
	if (action.equalsIgnoreCase("k1")) {
			
			
			if(pc.getInventory().getSlot(Lineage.SLOT_RING_LEFT) == null){
				ChattingController.toChatting(pc, "변환 하실 왼쪽반지를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if(pc.getInventory().getSlot(Lineage.SLOT_RING_LEFT).getItem().getName().equalsIgnoreCase("신성한 지식의 반지")){
				ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getInventory().getSlot(Lineage.SLOT_RING_LEFT).getItem().getName().equalsIgnoreCase("신성한 민첩의 반지")
				    || pc.getInventory().getSlot(Lineage.SLOT_RING_LEFT).getItem().getName().equalsIgnoreCase("신성한 완력의 반지")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_RING_LEFT);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("신성한 지식의 반지"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_RING_LEFT).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
	if (action.equalsIgnoreCase("k2")) {
		
		
		if(pc.getInventory().getSlot(Lineage.SLOT_RING_LEFT) == null){
			ChattingController.toChatting(pc, "변환 하실 왼쪽반지를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		if(pc.getInventory().getSlot(Lineage.SLOT_RING_LEFT).getItem().getName().equalsIgnoreCase("신성한 민첩의 반지")){
			ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		if(pc.getInventory().getSlot(Lineage.SLOT_RING_LEFT).getItem().getName().equalsIgnoreCase("신성한 지식의 반지")
			    || pc.getInventory().getSlot(Lineage.SLOT_RING_LEFT).getItem().getName().equalsIgnoreCase("신성한 완력의 반지")){
			
	
				ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_RING_LEFT);
				ItemInstance item2 =pc.getInventory().find("방어구 변환");
				
				ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("신성한 민첩의 반지"));
				
				if(pc.getInventory().find("방어구 변환") != null){
					ii.setCount(1);
					ii.setEnLevel(item.getEnLevel());
					ii.setDefinite(true);
			
					ii.setBless(item.getBless());
				
					pc.getInventory().getSlot(Lineage.SLOT_RING_LEFT).toClick(pc, null);
					pc.getInventory().count(item, item.getCount() - item.getCount(), true);
					pc.toGiveItem(null, ii, ii.getCount());
					pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

				}else{
					ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
		}
	}
	if (action.equalsIgnoreCase("k3")) {
		
		
		if(pc.getInventory().getSlot(Lineage.SLOT_RING_LEFT) == null){
			ChattingController.toChatting(pc, "변환 하실 왼쪽반지를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		if(pc.getInventory().getSlot(Lineage.SLOT_RING_LEFT).getItem().getName().equalsIgnoreCase("신성한 완력의 반지")){
			ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		if(pc.getInventory().getSlot(Lineage.SLOT_RING_LEFT).getItem().getName().equalsIgnoreCase("신성한 지식의 반지")
			    || pc.getInventory().getSlot(Lineage.SLOT_RING_LEFT).getItem().getName().equalsIgnoreCase("신성한 민첩의 반지")){
			
	
				ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_RING_LEFT);
				ItemInstance item2 =pc.getInventory().find("방어구 변환");
				
				ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("신성한 완력의 반지"));
				
				if(pc.getInventory().find("방어구 변환") != null){
					ii.setCount(1);
					ii.setEnLevel(item.getEnLevel());
					ii.setDefinite(true);
			
					ii.setBless(item.getBless());
				
					pc.getInventory().getSlot(Lineage.SLOT_RING_LEFT).toClick(pc, null);
					pc.getInventory().count(item, item.getCount() - item.getCount(), true);
					pc.toGiveItem(null, ii, ii.getCount());
					pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

				}else{
					ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
		}
	}
	if (action.equalsIgnoreCase("k4")) {
		
		
		if(pc.getInventory().getSlot(Lineage.SLOT_RING_RIGHT) == null){
			ChattingController.toChatting(pc, "변환 하실 으른쪽반지를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		if(pc.getInventory().getSlot(Lineage.SLOT_RING_RIGHT).getItem().getName().equalsIgnoreCase("신성한 지식의 반지")){
			ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		if(pc.getInventory().getSlot(Lineage.SLOT_RING_RIGHT).getItem().getName().equalsIgnoreCase("신성한 민첩의 반지")
			    || pc.getInventory().getSlot(Lineage.SLOT_RING_RIGHT).getItem().getName().equalsIgnoreCase("신성한 완력의 반지")){
			
	
				ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_RING_RIGHT);
				ItemInstance item2 =pc.getInventory().find("방어구 변환");
				
				ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("신성한 지식의 반지"));
				
				if(pc.getInventory().find("방어구 변환") != null){
					ii.setCount(1);
					ii.setEnLevel(item.getEnLevel());
					ii.setDefinite(true);
			
					ii.setBless(item.getBless());
				
					pc.getInventory().getSlot(Lineage.SLOT_RING_RIGHT).toClick(pc, null);
					pc.getInventory().count(item, item.getCount() - item.getCount(), true);
					pc.toGiveItem(null, ii, ii.getCount());
					pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);

				}else{
					ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
		}
	}
			if (action.equalsIgnoreCase("k5")) {
				
				
				if(pc.getInventory().getSlot(Lineage.SLOT_RING_RIGHT) == null){
					ChattingController.toChatting(pc, "변환 하실 으른쪽반지를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(pc.getInventory().getSlot(Lineage.SLOT_RING_RIGHT).getItem().getName().equalsIgnoreCase("신성한 민첩의 반지")){
					ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				if(pc.getInventory().getSlot(Lineage.SLOT_RING_RIGHT).getItem().getName().equalsIgnoreCase("신성한 지식의 반지")
					    || pc.getInventory().getSlot(Lineage.SLOT_RING_RIGHT).getItem().getName().equalsIgnoreCase("신성한 완력의 반지")){
					
			
						ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_RING_RIGHT);
						ItemInstance item2 =pc.getInventory().find("방어구 변환");
						
						ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("신성한 민첩의 반지"));
						
						if(pc.getInventory().find("방어구 변환") != null){
							ii.setCount(1);
							ii.setEnLevel(item.getEnLevel());
							ii.setDefinite(true);
					
							ii.setBless(item.getBless());
						
							pc.getInventory().getSlot(Lineage.SLOT_RING_RIGHT).toClick(pc, null);
							pc.getInventory().count(item, item.getCount() - item.getCount(), true);
							pc.toGiveItem(null, ii, ii.getCount());
							pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
			
						}else{
							ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
							return;
						}
						
				}
			}
			if (action.equalsIgnoreCase("k6")) {
				
				
				if(pc.getInventory().getSlot(Lineage.SLOT_RING_RIGHT) == null){
					ChattingController.toChatting(pc, "변환 하실 으른쪽반지를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(pc.getInventory().getSlot(Lineage.SLOT_RING_RIGHT).getItem().getName().equalsIgnoreCase("신성한 완력의 반지")){
					ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				if(pc.getInventory().getSlot(Lineage.SLOT_RING_RIGHT).getItem().getName().equalsIgnoreCase("신성한 지식의 반지")
					    || pc.getInventory().getSlot(Lineage.SLOT_RING_RIGHT).getItem().getName().equalsIgnoreCase("신성한 민첩의 반지")){
					
			
						ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_RING_RIGHT);
						ItemInstance item2 =pc.getInventory().find("방어구 변환");
						
						ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("신성한 완력의 반지"));
						
						if(pc.getInventory().find("방어구 변환") != null){
							ii.setCount(1);
							ii.setEnLevel(item.getEnLevel());
							ii.setDefinite(true);
					
							ii.setBless(item.getBless());
						
							pc.getInventory().getSlot(Lineage.SLOT_RING_RIGHT).toClick(pc, null);
							pc.getInventory().count(item, item.getCount() - item.getCount(), true);
							pc.toGiveItem(null, ii, ii.getCount());
							pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
			
						}else{
							ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
							return;
						}
						
				}
			}
			if (action.equalsIgnoreCase("l1")) {
				
				
				if(pc.getInventory().getSlot(Lineage.SLOT_NECKLACE) == null){
					ChattingController.toChatting(pc, "변환 하실 목거리를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).getItem().getName().equalsIgnoreCase("신성한 지식의 목걸이")){
					ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				if(pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).getItem().getName().equalsIgnoreCase("신성한 민첩의 목걸이")
					    || pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).getItem().getName().equalsIgnoreCase("신성한 완력의 목걸이")){
					
			
						ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_NECKLACE);
						ItemInstance item2 =pc.getInventory().find("방어구 변환");
						
						ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("신성한 지식의 목걸이"));
						
						if(pc.getInventory().find("방어구 변환") != null){
							ii.setCount(1);
							ii.setEnLevel(item.getEnLevel());
							ii.setDefinite(true);
					
							ii.setBless(item.getBless());
						
							pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).toClick(pc, null);
							pc.getInventory().count(item, item.getCount() - item.getCount(), true);
							pc.toGiveItem(null, ii, ii.getCount());
							pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
			
						}else{
							ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
							return;
						}
						
				}
			}
			if (action.equalsIgnoreCase("l2")) {
				
				
				if(pc.getInventory().getSlot(Lineage.SLOT_NECKLACE) == null){
					ChattingController.toChatting(pc, "변환 하실 목거리를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).getItem().getName().equalsIgnoreCase("신성한 민첩의 목걸이")){
					ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				if(pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).getItem().getName().equalsIgnoreCase("신성한 지식의 목걸이")
					    || pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).getItem().getName().equalsIgnoreCase("신성한 완력의 목걸이")){
					
			
						ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_NECKLACE);
						ItemInstance item2 =pc.getInventory().find("방어구 변환");
						
						ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("신성한 민첩의 목걸이"));
						
						if(pc.getInventory().find("방어구 변환") != null){
							ii.setCount(1);
							ii.setEnLevel(item.getEnLevel());
							ii.setDefinite(true);
					
							ii.setBless(item.getBless());
						
							pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).toClick(pc, null);
							pc.getInventory().count(item, item.getCount() - item.getCount(), true);
							pc.toGiveItem(null, ii, ii.getCount());
							pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
			
						}else{
							ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
							return;
						}
						
					}
				}
			if (action.equalsIgnoreCase("l3")) {
				
				
				if(pc.getInventory().getSlot(Lineage.SLOT_NECKLACE) == null){
					ChattingController.toChatting(pc, "변환 하실 목거리를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).getItem().getName().equalsIgnoreCase("신성한 완력의 목걸이")){
					ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				if(pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).getItem().getName().equalsIgnoreCase("신성한 민첩의 목걸이")
				    || pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).getItem().getName().equalsIgnoreCase("신성한 지식의 목걸이")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_NECKLACE);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("신성한 완력의 목걸이"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
		
					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
			}
		}
		if (action.equalsIgnoreCase("m1")) {
				
				
				if(pc.getInventory().getSlot(Lineage.SLOT_NECKLACE) == null){
					ChattingController.toChatting(pc, "변환 하실 목거리를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).getItem().getName().equalsIgnoreCase("붉은빛 크로노스 목걸이")){
					ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				if(pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).getItem().getName().equalsIgnoreCase("푸른빛 크로노스 목걸이")
				    || pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).getItem().getName().equalsIgnoreCase("녹색빛 크로노스 목걸이")){
				
		
					ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_NECKLACE);
					ItemInstance item2 =pc.getInventory().find("방어구 변환");
					
					ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("붉은빛 크로노스 목걸이"));
					
					if(pc.getInventory().find("방어구 변환") != null){
						ii.setCount(1);
						ii.setEnLevel(item.getEnLevel());
						ii.setDefinite(true);
				
						ii.setBless(item.getBless());
					
						pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).toClick(pc, null);
						pc.getInventory().count(item, item.getCount() - item.getCount(), true);
						pc.toGiveItem(null, ii, ii.getCount());
						pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
		
					}else{
						ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
				}
			}
				if (action.equalsIgnoreCase("m2")) {
					
					
					if(pc.getInventory().getSlot(Lineage.SLOT_NECKLACE) == null){
						ChattingController.toChatting(pc, "변환 하실 목거리를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					if(pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).getItem().getName().equalsIgnoreCase("푸른빛 크로노스 목걸이")){
						ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
					if(pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).getItem().getName().equalsIgnoreCase("붉은빛 크로노스 목걸이")
					    || pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).getItem().getName().equalsIgnoreCase("녹색빛 크로노스 목걸이")){
					
			
						ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_NECKLACE);
						ItemInstance item2 =pc.getInventory().find("방어구 변환");
						
						ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("푸른빛 크로노스 목걸이"));
						
						if(pc.getInventory().find("방어구 변환") != null){
							ii.setCount(1);
							ii.setEnLevel(item.getEnLevel());
							ii.setDefinite(true);
					
							ii.setBless(item.getBless());
						
							pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).toClick(pc, null);
							pc.getInventory().count(item, item.getCount() - item.getCount(), true);
							pc.toGiveItem(null, ii, ii.getCount());
							pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
			
						}else{
							ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
							return;
						}
						
				}
			}
				if (action.equalsIgnoreCase("m3")) {
					
					
					if(pc.getInventory().getSlot(Lineage.SLOT_NECKLACE) == null){
						ChattingController.toChatting(pc, "변환 하실 목거리를 장착해주세요", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					if(pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).getItem().getName().equalsIgnoreCase("녹색빛 크로노스 목걸이")){
						ChattingController.toChatting(pc, "같은 아이템으로 변경이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
					if(pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).getItem().getName().equalsIgnoreCase("붉은빛 크로노스 목걸이")
					    || pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).getItem().getName().equalsIgnoreCase("푸른빛 크로노스 목걸이")){
					
			
						ItemInstance item =pc.getInventory().getSlot(Lineage.SLOT_NECKLACE);
						ItemInstance item2 =pc.getInventory().find("방어구 변환");
						
						ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("녹색빛 크로노스 목걸이"));
						
						if(pc.getInventory().find("방어구 변환") != null){
							ii.setCount(1);
							ii.setEnLevel(item.getEnLevel());
							ii.setDefinite(true);
					
							ii.setBless(item.getBless());
						
							pc.getInventory().getSlot(Lineage.SLOT_NECKLACE).toClick(pc, null);
							pc.getInventory().count(item, item.getCount() - item.getCount(), true);
							pc.toGiveItem(null, ii, ii.getCount());
							pc.getInventory().count(item2, item2.getCount() - item2.getCount(), true);
			
						}else{
							ChattingController.toChatting(pc, "방어구 변환 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
							return;
						}
						
				}
			}
		}
	}
}