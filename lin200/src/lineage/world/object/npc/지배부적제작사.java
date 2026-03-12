package lineage.world.object.npc;

import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI.ToastType;
import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 지배부적제작사 extends object {
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "orimCreate0"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
		if (pc.getInventory() != null) {
			
            if (action.equalsIgnoreCase("오만의 탑 1층 지배 부적") && !Lineage.is_1cmdwlqo) {
                ChattingController.toChatting(pc, "현재는 1층 지배 부적을 제작할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (action.equalsIgnoreCase("오만의 탑 5층 지배 부적") && !Lineage.is_5cmdwlqo) {
                ChattingController.toChatting(pc, "현재는 5층 지배 부적을 제작할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (action.equalsIgnoreCase("오만의 탑 10층 지배 부적") && !Lineage.is_10cmdwlqo) {
                ChattingController.toChatting(pc, "현재는 10층 지배 부적을 제작할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }

                   if (action.equalsIgnoreCase("오만의 탑 1층 지배 부적")) {
				createItem(pc, "오만의 탑 1층 이동 부적", 1, "1층 지배 부적 조각", 100, "아데나", 50000000, "오만의 탑 1층 지배 부적 상자", 1, 1, 100);

			} else if (action.equalsIgnoreCase("오만의 탑 2층 지배 부적")) {
				createItem(pc, "오만의 탑 2층 이동 부적", 1, "2층 지배 부적 조각", 100, "아데나", 30000000, "오만의 탑 2층 지배 부적 상자", 1, 1, 20);

			} else if (action.equalsIgnoreCase("오만의 탑 3층 지배 부적")) {
				createItem(pc, "오만의 탑 3층 이동 부적", 1, "3층 지배 부적 조각", 100, "아데나", 30000000, "오만의 탑 3층 지배 부적 상자", 1, 1, 20);

			} else if (action.equalsIgnoreCase("오만의 탑 4층 지배 부적")) {
				createItem(pc, "오만의 탑 4층 이동 부적", 1, "4층 지배 부적 조각", 100, "아데나", 30000000, "오만의 탑 4층 지배 부적 상자", 1, 1, 100);

			} else if (action.equalsIgnoreCase("오만의 탑 5층 지배 부적")) {
				createItem(pc, "오만의 탑 5층 이동 부적", 1, "5층 지배 부적 조각", 100, "아데나", 50000000, "오만의 탑 5층 지배 부적 상자", 1, 1, 100);

			} else if (action.equalsIgnoreCase("오만의 탑 6층 지배 부적")) {
				createItem(pc, "오만의 탑 6층 이동 부적", 1, "6층 지배 부적 조각", 100, "아데나", 30000000, "오만의 탑 6층 지배 부적 상자", 1, 1, 20);

			} else if (action.equalsIgnoreCase("오만의 탑 7층 지배 부적")) {
				createItem(pc, "오만의 탑 7층 이동 부적", 1, "7층 지배 부적 조각", 100, "아데나", 30000000, "오만의 탑 7층 지배 부적 상자", 1, 1, 20);

			} else if (action.equalsIgnoreCase("오만의 탑 8층 지배 부적")) {
				createItem(pc, "오만의 탑 8층 이동 부적", 1, "8층 지배 부적 조각", 100, "아데나", 30000000, "오만의 탑 8층 지배 부적 상자", 1, 1, 20);

			} else if (action.equalsIgnoreCase("오만의 탑 9층 지배 부적")) {
				createItem(pc, "오만의 탑 9층 이동 부적", 1, "9층 지배 부적 조각", 50, "아데나", 30000000, "오만의 탑 9층 지배 부적 상자", 1, 1, 20);

			} else if (action.equalsIgnoreCase("오만의 탑 10층 지배 부적")) {
				createItem(pc, "오만의 탑 10층 이동 부적", 1, "10층 지배 부적 조각", 100, "아데나", 50000000, "오만의 탑 10층 지배 부적 상자", 1, 1, 100);
                                
             } /*else if (action.equalsIgnoreCase("순간이동 지배 반지")) {
				createItem(pc, "순간이동 조종 반지", 1, "신성한 조각", 200, "아데나", 300000000, "순간이동 지배 반지", 1, 1, 100);

			}*/
		}
	}

	public void createItem(PcInstance pc, String itemName, long count, String newItemName, int bless, long createCount, double percent) {
		if (pc.getInventory() != null) {
			// 재료
			ItemInstance item1 = null;
		
			for (ItemInstance i : pc.getInventory().getList()) {
				if (i != null && i.getItem() != null && i.getItem().getName().equalsIgnoreCase(itemName) && i.getBless() == 1 && i.getCount() >= count && !i.isEquipped())
					item1 = i;
				
				if (item1 != null)
					break;
			}

			if (item1 != null) {
				Item i = ItemDatabase.find(newItemName);
				
				if (i != null) {
					if (pc.getGm() > 0 || Math.random() < (percent * 0.01)) {
						ItemInstance temp = pc.getInventory().find(i.getName(), bless, i.isPiles());

						if (temp != null && (temp.getBless() != bless || temp.getEnLevel() != 0))
							temp = null;

						if (temp == null) {
							// 겹칠수 있는 아이템이 존재하지 않을경우.
							if (i.isPiles()) {
								temp = ItemDatabase.newInstance(i);
								temp.setObjectId(ServerDatabase.nextItemObjId());
								temp.setBless(bless);
								temp.setEnLevel(0);
								temp.setCount(createCount);
								temp.setDefinite(true);
								pc.getInventory().append(temp, true);
							} else {
								for (int idx = 0; idx < createCount; idx++) {
									temp = ItemDatabase.newInstance(i);
									temp.setObjectId(ServerDatabase.nextItemObjId());
									temp.setBless(bless);
									temp.setEnLevel(0);
									temp.setDefinite(true);
									pc.getInventory().append(temp, true);
								}
							}
						} else {
							// 겹치는 아이템이 존재할 경우.
							pc.getInventory().count(temp, temp.getCount() + createCount, true);
						}

						ChattingController.toChatting(pc, String.format("'%s' 제작에 성공하였습니다! ", newItemName), Lineage.CHATTING_MODE_MESSAGE);
					} else {
						ChattingController.toChatting(pc, String.format("'%s' 제작에 실패하였습니다. ", newItemName), Lineage.CHATTING_MODE_MESSAGE);
					}

					pc.getInventory().count(item1, item1.getCount() - count, true);
				}
			} else {
				ChattingController.toChatting(pc, String.format("%s(%,d)가 필요합니다.", itemName, count), Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}
	
	public void createItem(PcInstance pc, String name1, long count, String name2, long count2, String newItemName, int bless, long createCount, double percent) {
		if (pc.getInventory() != null) {
			// 재료
			ItemInstance item1 = null;
			// 재료
			ItemInstance item2 = null;

			for (ItemInstance i : pc.getInventory().getList()) {
				if (i != null && i.getItem() != null && i.getItem().getName().equalsIgnoreCase(name1) && !i.isEquipped() && i.getCount() >= count)
					item1 = i;
				if (i != null && i.getItem() != null && i.getItem().getName().equalsIgnoreCase(name2) && !i.isEquipped() && i.getCount() >= count2)
					item2 = i;

				if (item1 != null && item2 != null)
					break;
			}

			if (item1 != null && item2 != null) {
				Item i = ItemDatabase.find(newItemName);

				if (i != null) {
					if (pc.getGm() > 0 || Math.random() < (percent * 0.01)) {
						ItemInstance temp = pc.getInventory().find(i.getName(), bless, i.isPiles());

						if (temp != null && (temp.getBless() != bless || temp.getEnLevel() != 0))
							temp = null;

						if (temp == null) {
							// 겹칠수 있는 아이템이 존재하지 않을경우.
							if (i.isPiles()) {
								temp = ItemDatabase.newInstance(i);
								temp.setObjectId(ServerDatabase.nextItemObjId());
								temp.setBless(bless);
								temp.setEnLevel(0);
								temp.setCount(createCount);
								temp.setDefinite(true);
								pc.getInventory().append(temp, true);
							} else {
								for (int idx = 0; idx < createCount; idx++) {
									temp = ItemDatabase.newInstance(i);
									temp.setObjectId(ServerDatabase.nextItemObjId());
									temp.setBless(bless);
									temp.setEnLevel(0);
									temp.setDefinite(true);
									pc.getInventory().append(temp, true);
								}
							}
						} else {
							// 겹치는 아이템이 존재할 경우.
							pc.getInventory().count(temp, temp.getCount() + createCount, true);
						}

						ChattingController.toChatting(pc, String.format("'%s' 제작에 성공하였습니다! ", newItemName), Lineage.CHATTING_MODE_MESSAGE);
					} else {
						ChattingController.toChatting(pc, String.format("'%s' 제작에 실패하였습니다. ", newItemName), Lineage.CHATTING_MODE_MESSAGE);
					}

					pc.getInventory().count(item1, item1.getCount() - count, true);
					pc.getInventory().count(item2, item2.getCount() - count2, true);
				}
			} else {
				ChattingController.toChatting(pc, String.format("%s(%,d), %s(%,d) 필요합니다.", name1, count, name2, count2), Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}
        
	public void createItem(PcInstance pc, String name1, long count, String name2, long count2, String name3, long count3, String newItemName, int bless, long createCount, double percent) {
	    if (pc.getInventory() != null) {
	        // 재료
	        ItemInstance item1 = null; // "오만의 탑 1층 이동 부적" → 성공 시 차감
	        ItemInstance item2 = null; // "지배 부적 조각" → 항상 차감
	        ItemInstance item3 = null; // "아데나" → 항상 차감

	        for (ItemInstance i : pc.getInventory().getList()) {
	            if (i != null && i.getItem() != null && i.getItem().getName().equalsIgnoreCase(name1) && !i.isEquipped() && i.getCount() >= count)
	                item1 = i;
	            if (i != null && i.getItem() != null && i.getItem().getName().equalsIgnoreCase(name2) && !i.isEquipped() && i.getCount() >= count2)
	                item2 = i;
	            if (i != null && i.getItem() != null && i.getItem().getName().equalsIgnoreCase(name3) && !i.isEquipped() && i.getCount() >= count3)
	                item3 = i;

	            if (item1 != null && item2 != null && item3 != null)
	                break;
	        }

	        if (item1 != null && item2 != null && item3 != null) {
	            Item i = ItemDatabase.find(newItemName);

	            if (i != null) {
	                boolean isSuccess = pc.getGm() > 0 || Math.random() < (percent * 0.01);

	                // 항상 차감되는 재료 → 실패 여부에 관계없이 소모
	                pc.getInventory().count(item2, item2.getCount() - count2, true);
	                pc.getInventory().count(item3, item3.getCount() - count3, true);

	                if (isSuccess) {
	                    // 제작 성공 시
	                    ItemInstance temp = pc.getInventory().find(i.getName(), bless, i.isPiles());

	                    if (temp != null && (temp.getBless() != bless || temp.getEnLevel() != 0))
	                        temp = null;

	                    if (temp == null) {
	                        // 겹칠 수 없는 아이템일 경우
	                        if (i.isPiles()) {
	                            temp = ItemDatabase.newInstance(i);
	                            temp.setObjectId(ServerDatabase.nextItemObjId());
	                            temp.setBless(bless);
	                            temp.setEnLevel(0);
	                            temp.setCount(createCount);
	                            temp.setDefinite(true);
	                            pc.getInventory().append(temp, true);
	                        } else {
	                            for (int idx = 0; idx < createCount; idx++) {
	                                temp = ItemDatabase.newInstance(i);
	                                temp.setObjectId(ServerDatabase.nextItemObjId());
	                                temp.setBless(bless);
	                                temp.setEnLevel(0);
	                                temp.setDefinite(true);
	                                pc.getInventory().append(temp, true);
	                            }
	                        }
	                    } else {
	                        // 겹치는 아이템이 존재할 경우
	                        pc.getInventory().count(temp, temp.getCount() + createCount, true);
	                    }

	                    // 제작 성공 시 첫 번째 재료 차감
	                    pc.getInventory().count(item1, item1.getCount() - count, true);

	                    // 성공 메시지
	                    ChattingController.toChatting(pc, String.format("'%s' 제작에 성공하였습니다!", newItemName), Lineage.CHATTING_MODE_MESSAGE);
	                    World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class),
	                        String.format("\\fR어느 아덴 용사가 %s 제작에 성공하였습니다!", newItemName)));
	                    
	                 // ✅ 이 아래 라인을 전체 유저용 루프로 변경
	                    SC_TOAST_NOTI.newInstance()
	                    .setMessage(String.format("\\g1* 제작 성공 [%s] *", newItemName))
	                    .setMessage2(String.format("\\fH아덴의 어느 용사가 [%s] 제작에 성공하였습니다.", newItemName))
	                    .setToastType(ToastType.HeavyText)
	                    .send(pc);
	                    
	                } else {
	                    // 제작 실패 시 → 첫 번째 재료는 차감되지 않음
	                    ChattingController.toChatting(pc, String.format("'%s' 제작에 실패하였습니다.", newItemName), Lineage.CHATTING_MODE_MESSAGE);
	                }
	            }
	        } else {
	            // 재료 부족 메시지
	            ChattingController.toChatting(pc, String.format("%s(%,d), %s(%,d), %s(%,d) 필요합니다.",
	                    name1, count, name2, count2, name3, count3), Lineage.CHATTING_MODE_MESSAGE);
	        }
	    }
	}
}