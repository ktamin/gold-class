package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 액세서리제작사 extends object {
	
	public class CreateItem {
		public String itemName;
		public boolean isCheckBless;
		public int bless;
		public boolean isCheckEnchant;
		public int enchant;
		public int count;		
		
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
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "accCreate"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
		if (pc.getInventory() != null) {
			List<CreateItem> createList = new ArrayList<CreateItem>();
			List<CreateItem> createList2 = new ArrayList<CreateItem>();
			List<ItemInstance> itemList = new ArrayList<ItemInstance>();
			
			if (action.equalsIgnoreCase("신성한완력의목걸이6")) {				
				createList.add(new CreateItem("완력의 목걸이", true, 0, true, 7, 1));
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 20000000));
				checkItem(pc, createList, itemList);				
				createItem(pc, createList, createList2, itemList, "신성한 완력의 목걸이", 1, 6, 1);
				
			} else if (action.equalsIgnoreCase("신성한완력의목걸이7")) {					
				createList.add(new CreateItem("완력의 목걸이", true, 0, true, 8, 1));
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 30000000));	
				checkItem(pc, createList, itemList);				
				createItem(pc, createList, createList2, itemList, "신성한 완력의 목걸이", 1, 7, 1);
					
			} else if (action.equalsIgnoreCase("신성한민첩의목걸이6")) {				
				createList.add(new CreateItem("민첩의 목걸이", true, 0, true, 7, 1));
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 20000000));	
				checkItem(pc, createList, itemList);				
				createItem(pc, createList, createList2, itemList, "신성한 민첩의 목걸이", 1, 6, 1);
				
			} else if (action.equalsIgnoreCase("신성한민첩의목걸이7")) {				
				createList.add(new CreateItem("민첩의 목걸이", true, 0, true, 8, 1));
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 30000000));	
				checkItem(pc, createList, itemList);				
				createItem(pc, createList, createList2, itemList, "신성한 민첩의 목걸이", 1, 7, 1);	
				
			} else if (action.equalsIgnoreCase("신성한지식의목걸이6")) {				
				createList.add(new CreateItem("지식의 목걸이", true, 0, true, 7, 1));
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 20000000));	
				checkItem(pc, createList, itemList);				
				createItem(pc, createList, createList2, itemList, "신성한 지식의 목걸이", 1, 6, 1);
				
			} else if (action.equalsIgnoreCase("신성한지식의목걸이7")) {				
				createList.add(new CreateItem("지식의 목걸이", true, 0, true, 8, 1));
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 30000000));	
				checkItem(pc, createList, itemList);				
				createItem(pc, createList, createList2, itemList, "신성한 지식의 목걸이", 1, 7, 1);
				
			} else if (action.equalsIgnoreCase("신성한완력의반지6")) {				
				createList.add(new CreateItem("완력의 반지", true, 0, true, 7, 1));
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 20000000));
				checkItem(pc, createList, itemList);				
				createItem(pc, createList, createList2, itemList, "신성한 완력의 반지", 1, 6, 1);
					
			} else if (action.equalsIgnoreCase("신성한완력의반지7")) {					
				createList.add(new CreateItem("완력의 반지", true, 0, true, 8, 1));
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 30000000));	
				checkItem(pc, createList, itemList);				
				createItem(pc, createList, createList2, itemList, "신성한 완력의 반지", 1, 7, 1);
				
			} else if (action.equalsIgnoreCase("신성한민첩의반지6")) {				
				createList.add(new CreateItem("민첩의 반지", true, 0, true, 7, 1));
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 20000000));	
				checkItem(pc, createList, itemList);				
				createItem(pc, createList, createList2, itemList, "신성한 민첩의 반지", 1, 6, 1);
				
			} else if (action.equalsIgnoreCase("신성한민첩의반지7")) {				
				createList.add(new CreateItem("민첩의 반지", true, 0, true, 8, 1));
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 30000000));	
				checkItem(pc, createList, itemList);				
				createItem(pc, createList, createList2, itemList, "신성한 민첩의 반지", 1, 7, 1);
				
			} else if (action.equalsIgnoreCase("신성한지식의반지6")) {				
				createList.add(new CreateItem("지식의 반지", true, 0, true, 7, 1));
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 20000000));	
				checkItem(pc, createList, itemList);				
				createItem(pc, createList, createList2, itemList, "신성한 지식의 반지", 1, 6, 1);
				
			} else if (action.equalsIgnoreCase("신성한지식의반지7")) {				
				createList.add(new CreateItem("지식의 반지", true, 0, true, 8, 1));
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 30000000));	
				checkItem(pc, createList, itemList);				
				createItem(pc, createList, createList2, itemList, "신성한 지식의 반지", 1, 7, 1);	
				
			} else if (action.equalsIgnoreCase("신성한완력의벨트6")) {				
				createList.add(new CreateItem("완력의 벨트", true, 0, true, 7, 1));
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 20000000));
				checkItem(pc, createList, itemList);				
				createItem(pc, createList, createList2, itemList, "신성한 완력의 벨트", 1, 6, 1);
					
			} else if (action.equalsIgnoreCase("신성한완력의벨트7")) {					
				createList.add(new CreateItem("완력의 벨트", true, 0, true, 8, 1));
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 30000000));	
				checkItem(pc, createList, itemList);				
				createItem(pc, createList, createList2, itemList, "신성한 완력의 벨트", 1, 7, 1);
				
			} else if (action.equalsIgnoreCase("신성한민첩의벤트6")) {				
				createList.add(new CreateItem("민첩의 벨트", true, 0, true, 7, 1));
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 20000000));	
				checkItem(pc, createList, itemList);				
				createItem(pc, createList, createList2, itemList, "신성한 민첩의 벨트", 1, 6, 1);
				
			} else if (action.equalsIgnoreCase("신성한민첩의벨트7")) {				
				createList.add(new CreateItem("민첩의 벨트", true, 0, true, 8, 1));
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 30000000));	
				checkItem(pc, createList, itemList);				
				createItem(pc, createList, createList2, itemList, "신성한 민첩의 벨트", 1, 7, 1);
				
			} else if (action.equalsIgnoreCase("신성한지식의벨트6")) {				
				createList.add(new CreateItem("지식의 벨트", true, 0, true, 7, 1));
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 20000000));	
				checkItem(pc, createList, itemList);				
				createItem(pc, createList, createList2, itemList, "신성한 지식의 벨트", 1, 6, 1);
				
			} else if (action.equalsIgnoreCase("신성한지식의벨트7")) {				
				createList.add(new CreateItem("지식의 벨트", true, 0, true, 8, 1));
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 30000000));
				checkItem(pc, createList, itemList);				
				createItem(pc, createList, createList2, itemList, "신성한 지식의 벨트", 1, 7, 1);	
				
			} else if (action.equalsIgnoreCase("노예의귀걸이")) {
				
				createList.add(new CreateItem("희귀 제작 비법서(악세)", false, 1, false, 0, 1));
				createList.add(new CreateItem("축복 부여 주문서", false, 1, false, 0, 500));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 50000000));
				checkItem(pc, createList, itemList);
				
				createItem(pc, createList, createList2, itemList, "노예의 귀걸이", 1, 0, 1);				
				
			}
		}
	}
	
	public void checkItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList) {
		if (createList != null && itemList != null) {
			if (itemList.size() > 0)
				itemList.clear();
			
			for (CreateItem list : createList) {
				for (ItemInstance i : pc.getInventory().getList()) {
					if (i.getItem() != null && i.getItem().getName().equalsIgnoreCase(list.itemName) && i.getCount() >= list.count && !i.isEquipped()) {
						// 축여부 체크일 경우
						if (list.isCheckBless) {
							// 인첸트 체크일 경우
							if (list.isCheckEnchant) {
								if (i.getBless() == list.bless && i.getEnLevel() == list.enchant) {
									itemList.add(i);
									break;
								}
							} else {
								if (i.getBless() == list.bless) {
									itemList.add(i);
									break;
								}
							}
						} else {
							// 인첸트 체크일 경우
							if (list.isCheckEnchant) {
								if (i.getEnLevel() == list.enchant) {
									itemList.add(i);
									break;
								}
							} else {
								itemList.add(i);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	public void createItem(PcInstance pc, List<CreateItem> createList, List<CreateItem> createList2, List<ItemInstance> itemList, String createItemName, int bless, int enchant, int count) {
		if ((createList.size() > 0 && itemList.size() > 0 && createList.size() == itemList.size()) || 
			(createList2.size() > 0 && itemList.size() > 0 && createList2.size() == itemList.size())) {
			
			Item i = ItemDatabase.find(createItemName);

			if (i != null) {
				ItemInstance temp = pc.getInventory().find(i.getName(), bless, i.isPiles());

				if (temp != null && (temp.getBless() != bless || temp.getEnLevel() != enchant))
					temp = null;

				if (temp == null) {
					// 겹칠수 있는 아이템이 존재하지 않을경우.
					if (i.isPiles()) {
						temp = ItemDatabase.newInstance(i);
						temp.setObjectId(ServerDatabase.nextItemObjId());
						temp.setBless(bless);
						temp.setEnLevel(enchant);
						temp.setCount(count);
						temp.setDefinite(true);
						pc.getInventory().append(temp, true);
					} else {
						for (int idx = 0; idx < count; idx++) {
							temp = ItemDatabase.newInstance(i);
							temp.setObjectId(ServerDatabase.nextItemObjId());
							temp.setBless(bless);
							temp.setEnLevel(enchant);
							temp.setDefinite(true);
							pc.getInventory().append(temp, true);
						}
					}
				} else {
					// 겹치는 아이템이 존재할 경우.
					pc.getInventory().count(temp, temp.getCount() + count, true);
				}

				if (createList2.size() == 0) {
					for (CreateItem list : createList) {
						for (ItemInstance item : itemList) {
							if (item != null && item.getItem() != null && list.itemName.equalsIgnoreCase(item.getItem().getName()))
								pc.getInventory().count(item, item.getCount() - list.count, true);
						}
					}
				} else {
					for (CreateItem list : createList2) {
						for (ItemInstance item : itemList) {
							if (item != null && item.getItem() != null && list.itemName.equalsIgnoreCase(item.getItem().getName()))
								pc.getInventory().count(item, item.getCount() - list.count, true);
						}
					}
				}

				ChattingController.toChatting(pc, String.format("'%s' 제작 완료!", createItemName), Lineage.CHATTING_MODE_MESSAGE);
			}
		} else {
			String msg = "";
			
			if (createList2.size() > 0) {
				int idx = 0;
				
				for (CreateItem list : createList) {
					idx++;
					
					if (list.enchant > 0 && list.count > 1)
						msg += String.format("+%d %s(%,d)", list.enchant, list.itemName, list.count);
					else if (list.enchant > 0 && list.count == 1)
						msg += String.format("+%d %s", list.enchant, list.itemName);
					else  if (list.enchant == 0 && list.count > 1)
						msg += String.format("%s(%,d)", list.itemName, list.count);
					else if (list.enchant == 0 && list.count == 1)
						msg += String.format("%s", list.itemName);
					
					if (idx < createList.size())
						msg += ", ";
				}
				
				idx = 0;
				msg += " 또는 ";
				
				for (CreateItem list : createList2) {
					idx++;
					
					if (list.enchant > 0 && list.count > 1)
						msg += String.format("+%d %s(%,d)", list.enchant, list.itemName, list.count);
					else if (list.enchant > 0 && list.count == 1)
						msg += String.format("+%d %s", list.enchant, list.itemName);
					else  if (list.enchant == 0 && list.count > 1)
						msg += String.format("%s(%,d)", list.itemName, list.count);
					else if (list.enchant == 0 && list.count == 1)
						msg += String.format("%s", list.itemName);
					
					if (idx < createList2.size())
						msg += ", ";
				}
			} else {
				int idx = 0;
				
				for (CreateItem list : createList) {
					idx++;
					
					if (list.enchant > 0 && list.count > 1)
						msg += String.format("+%d %s(%,d)", list.enchant, list.itemName, list.count);
					else if (list.enchant > 0 && list.count == 1)
						msg += String.format("+%d %s", list.enchant, list.itemName);
					else  if (list.enchant == 0 && list.count > 1)
						msg += String.format("%s(%,d)", list.itemName, list.count);
					else if (list.enchant == 0 && list.count == 1)
						msg += String.format("%s", list.itemName);
					
					if (idx < createList.size())
						msg += ", ";
				}
			}
			
			ChattingController.toChatting(pc, String.format("[%s] %s 필요합니다.", createItemName, msg), Lineage.CHATTING_MODE_MESSAGE);
		}
	}
} 
