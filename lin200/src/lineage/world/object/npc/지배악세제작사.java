package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI;
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
import lineage.world.object.npc.전설장비제작사.CreateItem;

public class 지배악세제작사 extends object {

    public class CreateItem {
        public String itemName;
        public boolean isCheckBless;
        public int bless;
        public boolean isCheckEnchant;
        public int enchant;
        public int count;

        public CreateItem(String itemName, boolean isCheckBless, int bless, boolean isCheckEnchant, int enchant,
                int count) {
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
        pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "orimCreate2"));
    }

    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
        if (pc.getInventory() != null) {
            List<CreateItem> createList = new ArrayList<>();
            List<ItemInstance> itemList = new ArrayList<>();

            if (action.equalsIgnoreCase("전투가호") && !Lineage.is_wjsxn) {
                ChattingController.toChatting(pc, "현재는 전투가호를 제작할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (action.equalsIgnoreCase("순간이동 지배 반지") && !Lineage.is_tnsrks) {
                ChattingController.toChatting(pc, "현재는 순간이동 지배 반지를 제작할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (action.equalsIgnoreCase("변신 조종 지배 반지") && !Lineage.is_qustls) {
                ChattingController.toChatting(pc, "현재는 변신 조종 지배 반지를 제작할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (action.equalsIgnoreCase("신성한 룬") && !Lineage.is_tlstjd) {
                ChattingController.toChatting(pc, "현재는 신성한 룬을 제작할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }

            if (action.equalsIgnoreCase("순간이동 지배 반지")) {
                // createList.add(new CreateItem("순간이동 조종 반지", false, 1, false, 0, 1));
                createList.add(new CreateItem("신화 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 500000));
                // createList.add(new CreateItem("아데나", false, 1, false, 0, 100000000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "순간이동 지배 반지", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("변신 조종 지배 반지")) {
                // createList.add(new CreateItem("변신 조종 반지", false, 1, false, 0, 1));
                createList.add(new CreateItem("신화 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 500000));
                // createList.add(new CreateItem("아데나", false, 1, false, 0, 100000000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "변신 조종 지배 반지", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 룬")) {
                createList.add(new CreateItem("신화 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 500000));
                // createList.add(new CreateItem("타버린 고급 불멸의 가호", false, 1, false, 0, 100));
                // createList.add(new CreateItem("아데나", false, 1, false, 0, 100000000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 룬", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("전투가호")) {
                createList.add(new CreateItem("신화 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 500000));
                // createList.add(new CreateItem("타버린 고급 불멸의 가호", false, 1, false, 0, 100));
                // createList.add(new CreateItem("아데나", false, 1, false, 0, 50000000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "전투가호", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("마법서 (미티어 스트라이크)")) {
                createList.add(new CreateItem("영웅 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "마법서 (미티어 스트라이크)", 1, 0, 1, 100);
                
            } else if (action.equalsIgnoreCase("정령의 수정 (스트라이커 게일)")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "정령의 수정 (스트라이커 게일)", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("정령의 수정 (폴루트 워터)")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "정령의 수정 (폴루트 워터)", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("정령의 수정 (소울 오브 프레임)")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "정령의 수정 (소울 오브 프레임)", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("정령의 수정 (어스 바인드)")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "정령의 수정 (어스 바인드)", 1, 0, 1, 100);      

            } else if (action.equalsIgnoreCase("기술서 (카운터 배리어)")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 500000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "기술서 (카운터 배리어)", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("마법서 (브레이브 멘탈)")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 500000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "마법서 (브레이브 멘탈)", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("마법서 (디스인티그레이트)")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 500000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "마법서 (디스인티그레이트)", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("아머 브레이크")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 500000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "아머 브레이크", 1, 0, 1, 100);
  
            }
        }
    }

    public boolean checkItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList) {
        boolean allItemsAvailable = true;
        StringBuilder missingItems = new StringBuilder("부족한 재료: ");

        for (CreateItem list : createList) {
            boolean found = false;
            for (ItemInstance i : pc.getInventory().getList()) {
                if (i.getItem() != null && i.getItem().getName().equalsIgnoreCase(list.itemName) &&
                        i.getCount() >= list.count && !i.isEquipped()) {
                    itemList.add(i);
                    found = true;
                    break;
                }
            }
            if (!found) {
                allItemsAvailable = false;
                missingItems.append(String.format("%s(%,d) ", list.itemName, list.count));
            }
        }

        if (!allItemsAvailable) {
            ChattingController.toChatting(pc, missingItems.toString(), Lineage.CHATTING_MODE_MESSAGE);
        }

        return allItemsAvailable;
    }

    public void createItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList,
			String createItemName, int bless, int enchant, int count, int successRate) {
		
		// ▼▼▼ [안전장치] 재료를 빼기 전에, 만들려는 아이템이 DB에 진짜 있는지부터 확인! ▼▼▼
		Item i = ItemDatabase.find(createItemName);
		if (i == null) {
			ChattingController.toChatting(pc, "[오류] '" + createItemName + "' 아이템의 이름(DB)이 잘못 설정되었습니다. 운영자에게 문의하세요.", Lineage.CHATTING_MODE_MESSAGE);
			lineage.share.System.println("[제작 오류] DB에서 찾을 수 없는 아이템: " + createItemName);
			return; // 아이템이 없으면 재료를 빼지 않고 여기서 즉시 멈춥니다!
		}
		// ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲

		Random random = new Random();
		boolean isSuccess = random.nextInt(100) < successRate;

		// 모든 재료 차감 (이제 안전하게 차감 가능)
		for (CreateItem list : createList) {
			for (ItemInstance item : itemList) {
				if (item != null && item.getItem() != null
						&& list.itemName.equalsIgnoreCase(item.getItem().getName())) {
					pc.getInventory().count(item, item.getCount() - list.count, true);
					break;
				}
			}
		}

		if (!isSuccess) {
			ChattingController.toChatting(pc, String.format("'%s' 제작에 실패하였습니다. 모든 재료가 차감되었습니다.", createItemName),
					Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		// 아이템 생성 및 지급
		ItemInstance temp = pc.getInventory().find(i.getName(), bless, i.isPiles());

		if (temp == null) {
			temp = ItemDatabase.newInstance(i);
			temp.setObjectId(ServerDatabase.nextItemObjId());
			temp.setBless(bless);
			temp.setEnLevel(enchant);
			temp.setCount(count);
			temp.setDefinite(true);
			pc.getInventory().append(temp, true);
		} else {
			pc.getInventory().count(temp, temp.getCount() + count, true);
		}

		// 성공 시 전체 채팅 메시지 출력
		ChattingController.toChatting(pc, String.format("'%s' 제작에 성공하였습니다!", createItemName),
				Lineage.CHATTING_MODE_MESSAGE);
		World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class),
				String.format("\\fR어느 아덴 용사가 %s 제작에 성공하였습니다!", createItemName)));

		// 토스트 전체 메시지 전송
		for (PcInstance p : World.getPcList()) {
			SC_TOAST_NOTI.newInstance()
					.setMessage(String.format("\\g1* 아이템 제작 [%s] *", createItemName))
					.setMessage2(String.format("\\fH어느 아덴 용사가 [%s] 제작에 성공하였습니다.", createItemName))
					.setToastType(SC_TOAST_NOTI.ToastType.HeavyText)
					.send(p);
		}
	}
}