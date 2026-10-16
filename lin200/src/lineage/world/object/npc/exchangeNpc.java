package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.ExchangeItem;
import lineage.bean.database.Item;
import lineage.database.ExchangeDatabase;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling; // 💡 패킷 풀링을 위한 import 추가
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ExchangeShopBuy;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.object;

public class exchangeNpc extends object {
	
	// 💡 1. 게임 내에서 NPC를 처음 클릭했을 때 호출되는 메서드 (새로 추가!)
		@Override
		public void toTalk(PcInstance pc, ClientBasePacket cbp) {
			// html 폴더의 "exchange_main.htm" 파일을 불러와서 창을 띄웁니다.
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "exchange_main"));
		}

		// 💡 2. HTML 창에서 버튼(action)을 눌렀을 때 호출되는 메서드
		@Override
		public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
		// 2. HTML 창에서 '무기 구매' 버튼(action="buy_weapon")을 클릭했을 때
				if (action.equalsIgnoreCase("buy_weapon")) {
					// 카테고리가 '무기(1)'인 아이템만 골라담을 새로운 리스트 생성
					List<ExchangeItem> weaponList = new ArrayList<ExchangeItem>();
					
					for (ExchangeItem item : ExchangeDatabase.getList()) {
						// 💡 카테고리가 1(무기)이면서, 동시에 상태가 0(판매 중)인 것만 리스트에 담는다!
						if (item.getCategory() == 1 && item.getStatus() == 0) {
							weaponList.add(item);
						}
					}
					
					// 무기가 하나도 등록되어 있지 않은 경우 처리
					if (weaponList.isEmpty()) {
						ChattingController.toChatting(pc, "현재 등록된 무기가 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					
					// 추출한 무기 리스트를 방금 만든 전용 패킷에 담아서 클라이언트로 발송!
					pc.toSender(S_ExchangeShopBuy.clone(BasePacketPooling.getPool(S_ExchangeShopBuy.class), (int) this.getObjectId(), weaponList));
					return;
				}
				
				// 2-2. '방어구 구매' 버튼(action="buy_armor")
				if (action.equalsIgnoreCase("buy_armor")) {
					List<ExchangeItem> armorList = new ArrayList<ExchangeItem>();
					for (ExchangeItem item : ExchangeDatabase.getList()) {
						// 💡 카테고리 2(방어구) 지정
						if (item.getCategory() == 2 && item.getStatus() == 0) {
							armorList.add(item);
						}
					}
					if (armorList.isEmpty()) {
						ChattingController.toChatting(pc, "현재 등록된 방어구가 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					pc.toSender(S_ExchangeShopBuy.clone(BasePacketPooling.getPool(S_ExchangeShopBuy.class), (int) this.getObjectId(), armorList));
					return;
				}

				// 2-3. '악세사리 구매' 버튼(action="buy_accessory")
				if (action.equalsIgnoreCase("buy_accessory")) {
					List<ExchangeItem> accList = new ArrayList<ExchangeItem>();
					for (ExchangeItem item : ExchangeDatabase.getList()) {
						// 💡 카테고리 3(악세사리) 지정
						if (item.getCategory() == 3 && item.getStatus() == 0) {
							accList.add(item);
						}
					}
					if (accList.isEmpty()) {
						ChattingController.toChatting(pc, "현재 등록된 악세사리가 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					pc.toSender(S_ExchangeShopBuy.clone(BasePacketPooling.getPool(S_ExchangeShopBuy.class), (int) this.getObjectId(), accList));
					return;
				}

				// 2-4. '기타(잡템) 구매' 버튼(action="buy_etc")
				if (action.equalsIgnoreCase("buy_etc")) {
					List<ExchangeItem> etcList = new ArrayList<ExchangeItem>();
					for (ExchangeItem item : ExchangeDatabase.getList()) {
						// 💡 카테고리 4(기타) 지정
						if (item.getCategory() == 4 && item.getStatus() == 0) {
							etcList.add(item);
						}
					}
					if (etcList.isEmpty()) {
						ChattingController.toChatting(pc, "현재 등록된 기타 물품이 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
					pc.toSender(S_ExchangeShopBuy.clone(BasePacketPooling.getPool(S_ExchangeShopBuy.class), (int) this.getObjectId(), etcList));
					return;
				}

				// 💡 아데나로 판매 등록 버튼 클릭 시
				if (action.equalsIgnoreCase("sell_item_aden")) {
				    pc.setExchangeCurrencyType(0); // 0: 아데나
				    // 유저에게 입력 안내 메시지 전송
				    ChattingController.toChatting(pc, "\\fY[거래소 등록] 채팅창에: [판매가격] [수량] 을 입력하세요.", Lineage.CHATTING_MODE_MESSAGE);
				    ChattingController.toChatting(pc, "\\fY예시) 100000 1  (10만 아덴에 1개 등록)", Lineage.CHATTING_MODE_MESSAGE);
				    
				    // 💡 유저의 상태를 '거래소 등록 대기 모드'로 변경 (커스텀 상태 변수 활용)
				    pc.setExchangeShopStep(1); 
				    return;
				}

				// 💡 코인으로 판매 등록 버튼 클릭 시
				if (action.equalsIgnoreCase("sell_item_coin")) {
				    pc.setExchangeCurrencyType(1); // 1: 코인
				    ChattingController.toChatting(pc, "\\fY[거래소 등록] 채팅창에: [판매 코인 가격] [수량] 을 입력하세요.", Lineage.CHATTING_MODE_MESSAGE);
				    pc.setExchangeShopStep(1);
				    return;
				}
		
		// 4. HTML 창에서 '판매 대금 수령' 버튼(action="settlement")을 클릭했을 때
				if (action.equalsIgnoreCase("settlement")) {
					long totalAden = 0;
					long totalCoin = 0;
					
					// 메모리 충돌(ConcurrentModificationException)을 방지하기 위해 정산할 아이템을 임시로 담아둘 리스트
					List<ExchangeItem> settledItems = new ArrayList<ExchangeItem>();

					// 거래소 전체 리스트를 돌면서 내 물건 중 '팔린(status=1)' 물건을 찾습니다.
					for (ExchangeItem item : ExchangeDatabase.getList()) {
						// 💡 주의: item.getSellerName() 부분은 운영자님의 ExchangeItem 빈(Bean) 파일에 등록된 
						// 판매자 이름(또는 objectId)을 불러오는 getter 이름과 정확히 일치해야 합니다!
						if (item.getSellerName().equalsIgnoreCase(pc.getName()) && item.getStatus() == 1) {
							
							// 판매 대금 누적
							long price = item.getPrice() * item.getCount();
							if (item.getCurrencyType() == 0) {
								totalAden += price;
							} else {
								totalCoin += price;
							}
							
							// 삭제할 아이템 리스트에 담기
							settledItems.add(item);
						}
					}

					// 정산할 아이템이 하나도 없을 경우
					if (settledItems.isEmpty()) {
						ChattingController.toChatting(pc, "현재 수령할 판매 대금이 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}

					// 💰 모인 대금을 인벤토리에 지급
					if (totalAden > 0) {
						ItemInstance adena = ItemDatabase.newInstance(ItemDatabase.find("아데나"));
						adena.setObjectId(ServerDatabase.nextItemObjId());
						adena.setCount(totalAden);
						pc.getInventory().append(adena, true);
						ChattingController.toChatting(pc, "판매 대금: 아데나 (" + totalAden + ") 수령 완료.", Lineage.CHATTING_MODE_MESSAGE);
					}

					if (totalCoin > 0) {
						ItemInstance coin = ItemDatabase.newInstance(ItemDatabase.find("코인")); // 코인 아이템명 확인
						coin.setObjectId(ServerDatabase.nextItemObjId());
						coin.setCount(totalCoin);
						pc.getInventory().append(coin, true);
						ChattingController.toChatting(pc, "판매 대금: 코인 (" + totalCoin + ") 수령 완료.", Lineage.CHATTING_MODE_MESSAGE);
					}

					// 🗑️ 정산이 끝난 영수증은 DB와 메모리에서 완전히 삭제
					for (ExchangeItem item : settledItems) {
						ExchangeDatabase.deleteItem(item); // DB에서 삭제
						ExchangeDatabase.getList().remove(item); // 메모리 리스트에서 삭제
					}
					
					return;
				}
	}
	
	@Override
	public void toDwarfAndShop(PcInstance pc, ClientBasePacket cbp) {
		switch (cbp.readC()) {
			case 0: // 구매 버튼을 눌렀을 때
				toBuy(pc, cbp);
				break;
			case 1: // 💡 판매 등록 버튼을 눌렀을 때 연결!
				toSell(pc, cbp);
				break;
		}
	}

	private void toBuy(PcInstance pc, ClientBasePacket cbp) {
		// 1. 유저가 구매하려고 선택한 아이템의 '종류' 개수를 읽어옵니다.
		int count = cbp.readH(); 

		// 비정상적인 패킷(0개 이하, 너무 많은 개수) 방어
		if (count > 0 && count <= 100) { 
			for (int j = 0; j < count; ++j) { 
				// 2. S_ExchangeShopBuy에서 writeD(eItem.getId())로 보냈던 그 고유번호
				int exchangeItemId = cbp.readD(); 
				
				// 3. 유저가 입력한 구매 수량
				long item_count = cbp.readD(); 

				if (item_count > 0) { 
					// 4. DB(메모리)에서 해당 등록 물품을 찾습니다. 
					// (주의: ExchangeDatabase에 findById 같은 검색 메서드가 없다면 추가로 만들어야 합니다)
					ExchangeItem eItem = ExchangeDatabase.findById(exchangeItemId); 

					if (eItem == null) {
						ChattingController.toChatting(pc, "이미 판매되었거나 존재하지 않는 물품입니다.", Lineage.CHATTING_MODE_MESSAGE);
						continue;
					}

					// 5. 수량 검증 (묶음 판매 강제 처리 - 낱개 구매 방지)
					if (item_count != eItem.getCount()) {
						ChattingController.toChatting(pc, "해당 물품은 낱개로 구매할 수 없으며, 묶음 전체 구매만 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
						continue; // 결제를 중단하고 다음 아이템으로 넘어감
					}

					long totalPrice = eItem.getPrice() * item_count;

					// 6. 재화 확인 및 차감 로직
					boolean canBuy = false;
					String currencyName = (eItem.getCurrencyType() == 0) ? "아덴" : "코인";

					if (eItem.getCurrencyType() == 0) { 
						// 아데나 결제
						if (pc.getInventory().isAden("아데나", totalPrice, false)) { 
							pc.getInventory().isAden("아데나", totalPrice, true);  
							canBuy = true;
						}
					} else { 
						// 코인 결제
						if (pc.getInventory().isAden("코인", totalPrice, false)) { 
							pc.getInventory().isAden("코인", totalPrice, true);
							canBuy = true;
						}
					}

					// 돈이 부족할 경우 처리
					if (!canBuy) {
						pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 776, currencyName)); 
						continue;
					}

					// 7. 유저에게 아이템 생성 및 지급
					Item item = ItemDatabase.find(eItem.getItemName());
					if (item != null) {
						ItemInstance temp = ItemDatabase.newInstance(item); 
						temp.setObjectId(ServerDatabase.nextItemObjId()); 
						temp.setCount(item_count); 
						temp.setEnLevel(eItem.getEnchant()); 
						temp.setBless(eItem.getBless()); 
						
						pc.getInventory().append(temp, true); 
						ChattingController.toChatting(pc, eItem.getItemName() + " (" + item_count + ") 구매가 완료되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
					}

					// 8. DB 및 메모리 정리 (상태를 '판매 완료(1)'로 변경)
					eItem.setStatus(1); 
					ExchangeDatabase.updateItemStatus(eItem);
				}
			}
		}
	}
	
	/**
	 * 아이템 판매 등록 처리 및 자동 카테고리 분류
	 */
	protected void toSell(PcInstance pc, ClientBasePacket cbp) {
		// 클라이언트가 전송한 판매 등록 정보 읽기 (서버 팩 패킷 구조에 맞게 조절 필요)
		long item_object_id = cbp.readD(); // 등록할 인벤토리 아이템의 고유 객체 번호
		long item_count = cbp.readD();     // 등록할 수량
		long price = cbp.readD();          // 판매 희망 가격
		int currencyType = cbp.readC();    // 화폐 종류 (0: 아데나, 1: 코인 등)

		// 1. 유저의 인벤토리에서 실제 해당 아이템을 찾습니다.
		ItemInstance targetItem = pc.getInventory().find(item_object_id);
		if (targetItem == null) {
			ChattingController.toChatting(pc, "존재하지 않거나 잘못된 아이템입니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		// 2. 수량 검증
		if (targetItem.getCount() < item_count || item_count <= 0) {
			ChattingController.toChatting(pc, "등록하려는 수량이 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		// 3. 🛡️ 자동 카테고리 분류 로직 (핵심!)
		int category = 4; // 기본값: 4 (기타/소모품)
		String type1 = targetItem.getItem().getType1(); // 아이템 대분류 타입 가져오기
		String itemName = targetItem.getItem().getName();

		if (type1 != null) {
			if (type1.equalsIgnoreCase("weapon")) {
				category = 1; // 무기
			} else if (type1.equalsIgnoreCase("armor")) {
				// 방어구 중 악세사리(반지, 목걸이 등)를 따로 분류하고 싶다면 여기서 세부 이름이나 타입 검사 가능
				if (itemName.contains("목걸이") || itemName.contains("반지") || itemName.contains("귀걸이") || itemName.equalsIgnoreCase("벨트")) {
					category = 3; // 악세사리
				} else {
					category = 2; // 방어구 (예: 파워 글로브, 투구 등)
				}
			} else if (type1.equalsIgnoreCase("accessory")) {
				category = 3; // 악세사리
			}
		}

		// 4. 새로운 ExchangeItem 객체 생성 및 데이터 세팅
				ExchangeItem newItem = new ExchangeItem();
				newItem.setId((int) ServerDatabase.nextItemObjId()); // 💡 int형으로 형변환하여 에러 방지
				newItem.setSellerName(pc.getName());
				newItem.setSellerObjectId(pc.getObjectId()); // 💡 방금 추가한 메서드 연동
				newItem.setItemName(itemName);
				newItem.setCount(item_count);
				newItem.setPrice(price);
				newItem.setCurrencyType(currencyType);
				newItem.setCategory(category); // 자동 분류된 카테고리 번호 장착
				newItem.setEnchant(targetItem.getEnLevel());
				newItem.setBless(targetItem.getBless());
				newItem.setStatus(0); // 0: 판매 중 상태로 설정

		// 5. 메모리 리스트에 추가 및 DB에 저장
		ExchangeDatabase.getList().add(newItem);
		ExchangeDatabase.insertItem(newItem);

		// 6. 유저 인벤토리에서 해당 아이템 차감 (압수하여 거래소로 이동)
//		pc.getInventory().remove(targetItem, item_count, true);
		// 💡 인벤토리 차감 최종 적용 코드 (소스 내 count 메서드 패턴 활용)
		pc.getInventory().count(targetItem, targetItem.getCount() - item_count, true);


		// 7. 완료 메시지 출력
		ChattingController.toChatting(pc, String.format("'%s' (수량: %d) 아이템이 거래소에 성공적으로 등록되었습니다.", itemName, item_count), Lineage.CHATTING_MODE_MESSAGE);
	}
}