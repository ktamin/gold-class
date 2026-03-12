package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI.ToastType;
import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 마안제작사 extends object {
	
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
	
	// 성공 확률(예: 30%)
	// 필요하면 숫자만 바꾸면 됨.
	private static final double LIFE_MAAN_SUCCESS_RATE = 0.05;

	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "maancreate"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
		if (pc.getInventory() != null) {
			List<CreateItem> createList = new ArrayList<CreateItem>();
			List<CreateItem> createList2 = new ArrayList<CreateItem>();
			List<ItemInstance> itemList = new ArrayList<ItemInstance>();
			
			if (action.equalsIgnoreCase("생명의 마안")) {
			    createList.add(new CreateItem("마안 조각", false, 1, false, 0, 100));
			    createList.add(new CreateItem("아데나", false, 1, false, 0, 100000000));
			    checkItem(pc, createList, itemList);

			    // ★ 확률형 합성: 성공 시 '생명의 마안', 실패 시 '생명의 마안 조각' 1개 보상
			    createItemWithChance(
			        pc,
			        createList, createList2, itemList,
			        "생명의 마안",     // 성공 보상 아이템명
			        1,                 // bless
			        0,                 // enchant
			        1,                 // 지급 수량
			        LIFE_MAAN_SUCCESS_RATE, // 성공 확률(0.30=30%)
			        "생명의 마안 조각",     // 실패 보상 아이템명
			        1                  // 실패 보상 수량
			    );

				
			} else if (action.equalsIgnoreCase("생명의 마안 완제")) {
				createList.add(new CreateItem("생명의 마안 조각", false, 0, false, 0, 10));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 100000000));
				checkItem(pc, createList, itemList);			
				createItem(pc, createList, createList2, itemList, "생명의 마안", 1, 0, 1);
				
			} else if (action.equalsIgnoreCase("풍룡의 마안")) {
				createList.add(new CreateItem("마안 조각", false, 0, false, 0, 100));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 1000000));
				checkItem(pc, createList, itemList);			
				createItem(pc, createList, createList2, itemList, "풍룡의 마안", 1, 0, 1);	
			
		    } else if (action.equalsIgnoreCase("화룡의 마안")) {		
				createList.add(new CreateItem("마안 조각", false, 0, false, 0, 100));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 1000000));
			   checkItem(pc, createList, itemList);			
			   createItem(pc, createList, createList2, itemList, "화룡의 마안", 1, 0, 1);	 
			   
		    } else if (action.equalsIgnoreCase("지룡의 마안")) {		
				createList.add(new CreateItem("마안 조각", false, 0, false, 0, 100));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 1000000));
			   checkItem(pc, createList, itemList);			
			   createItem(pc, createList, createList2, itemList, "지룡의 마안", 1, 0, 1);
			   
		    } else if (action.equalsIgnoreCase("수룡의 마안")) {		
				createList.add(new CreateItem("마안 조각", false, 0, false, 0, 100));	
				createList.add(new CreateItem("아데나", false, 1, false, 0, 1000000));
			   checkItem(pc, createList, itemList);			
			   createItem(pc, createList, createList2, itemList, "수룡의 마안", 1, 0, 1);   
			
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
			
			ChattingController.toChatting(pc, String.format("[%s] %s 필요합니다", createItemName, msg), Lineage.CHATTING_MODE_MESSAGE);
		}		
	}
	
	// 확률형 제작: 재료가 충족되면 재료를 소모하고 → 성공/실패 분기
	private void createItemWithChance(
	        PcInstance pc,
	        List<CreateItem> createList,
	        List<CreateItem> createList2,
	        List<ItemInstance> itemList,
	        String successItemName, int bless, int enchant, int count,
	        double successRate,
	        String failRewardName, int failRewardCount
	) {
	    // 재료 충족 여부 확인 (기존 createItem과 동일한 판정)
	    boolean ok =
	        (createList.size()  > 0 && itemList.size() > 0 && createList.size()  == itemList.size()) ||
	        (createList2.size() > 0 && itemList.size() > 0 && createList2.size() == itemList.size());

	    if (!ok) {
	        ChattingController.toChatting(pc,
	            String.format("[%s] 재료가 부족합니다.", successItemName),
	            Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // 결과/보상 아이템 찾기
	    Item successItem = ItemDatabase.find(successItemName);
	    Item failItem    = ItemDatabase.find(failRewardName);
	    if (successItem == null) {
	        ChattingController.toChatting(pc, String.format("'%s' 아이템 정보가 없습니다.", successItemName), Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }
	    if (failItem == null) {
	        ChattingController.toChatting(pc, String.format("'%s' 아이템 정보가 없습니다.", failRewardName), Lineage.CHATTING_MODE_MESSAGE);
	        return;
	    }

	    // (1) 재료 소모 — 성공/실패 모두 소모 (원하면 성공시에만 소모하도록 바꿀 수 있음)
	    if (createList2.size() == 0) {
	        for (CreateItem need : createList) {
	            for (ItemInstance have : itemList) {
	                if (have != null && have.getItem() != null &&
	                    need.itemName.equalsIgnoreCase(have.getItem().getName())) {
	                    pc.getInventory().count(have, have.getCount() - need.count, true);
	                }
	            }
	        }
	    } else {
	        for (CreateItem need : createList2) {
	            for (ItemInstance have : itemList) {
	                if (have != null && have.getItem() != null &&
	                    need.itemName.equalsIgnoreCase(have.getItem().getName())) {
	                    pc.getInventory().count(have, have.getCount() - need.count, true);
	                }
	            }
	        }
	    }

	    // (2) 성공/실패 판정
	    boolean success = Math.random() < successRate;

	    if (success) {
	        // 성공: 결과 아이템 지급
	        giveItem(pc, successItem, bless, enchant, count);
	        
	        // ★ 성공 시 전체 토스트 브로드캐스트 (클라 중앙 큰 토스트)
	        String title = "★ 제작 성공 ★";
	        String desc  = String.format("어느 아덴 용사가 '%s' 제작에 성공했습니다!", pc.getName(), successItemName);

	        for (PcInstance p : World.getPcList()) {
	            SC_TOAST_NOTI.newInstance()
	                .setMessage(title)       // 1행
	                .setMessage2(desc)       // 2행
	                .setToastType(ToastType.HeavyText)
	                .send(p);
	        }
	        ChattingController.toChatting(pc,
	            String.format("'%s' 제작 성공!", successItemName),
	            Lineage.CHATTING_MODE_MESSAGE);
	    } else {
	        // 실패: 보상 아이템 지급 (생명의 마안 조각 1개)
	        giveItem(pc, failItem, 0, 0, failRewardCount);
	        ChattingController.toChatting(pc,
	            String.format("'%s' 제작 실패... 보상으로 '%s' %,d개를 획득했습니다.",
	                successItemName, failRewardName, failRewardCount),
	            Lineage.CHATTING_MODE_MESSAGE);
	    }
	}
	
	// 인벤토리에 아이템 지급 (스택 가능/불가 모두 처리)
	private void giveItem(PcInstance pc, Item item, int bless, int enchant, int count) {
	    if (pc == null || item == null || pc.getInventory() == null) return;

	    ItemInstance stack = pc.getInventory().find(item.getName(), bless, item.isPiles());
	    if (stack != null && (stack.getBless() != bless || stack.getEnLevel() != enchant)) {
	        // 축/인첸이 다른 스택은 합치지 않음
	        stack = null;
	    }

	    if (stack == null) {
	        if (item.isPiles()) {
	            ItemInstance ni = ItemDatabase.newInstance(item);
	            ni.setObjectId(ServerDatabase.nextItemObjId());
	            ni.setBless(bless);
	            ni.setEnLevel(enchant);
	            ni.setCount(count);
	            ni.setDefinite(true);
	            pc.getInventory().append(ni, true);
	        } else {
	            for (int k = 0; k < count; k++) {
	                ItemInstance ni = ItemDatabase.newInstance(item);
	                ni.setObjectId(ServerDatabase.nextItemObjId());
	                ni.setBless(bless);
	                ni.setEnLevel(enchant);
	                ni.setDefinite(true);
	                pc.getInventory().append(ni, true);
	            }
	        }
	    } else {
	        pc.getInventory().count(stack, stack.getCount() + count, true);
	    }
	}


} 
