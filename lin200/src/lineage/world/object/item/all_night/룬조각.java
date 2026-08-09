package lineage.world.object.item.all_night;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 룬조각 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 룬조각();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		
		String myName = this.getItem().getName(); // 현재 클릭한 조각의 이름
		String targetName = ""; // 지급할 완성품의 이름을 담을 변수
		int needCount = 0; // 💡 [추가] 완제 제작에 필요한 조각의 개수
		
		// 1. 클릭한 조각 이름에 따라 지급할 완성품과 필요 개수(1군, 2군 등) 지정하기
		if (myName.equalsIgnoreCase("생명의 룬 조각")) {
			targetName = "생명의 룬";
			needCount = 5;  // 1군: 5개 필요
		} else if (myName.equalsIgnoreCase("방어의 룬 조각")) {
			targetName = "방어의 룬";
			needCount = 5; // 2군: 10개 필요
		} else if (myName.equalsIgnoreCase("전투의 룬 조각")) {
			targetName = "전투의 룬";
			needCount = 5; // 3군: 15개 필요 (예시)
		} else if (myName.equalsIgnoreCase("한양 코인 조각")) {
			targetName = "한양 코인";
			needCount = 5;  // 5개 필요
		} else if (myName.equalsIgnoreCase("기운을 잃은 나이트발드의 양손검")) {
			targetName = "나이트발드의 양손검";
			needCount = 5;  // 5개 필요	
		} else if (myName.equalsIgnoreCase("기운을 잃은 포르세의 검")) {
			targetName = "포르세의 검";
			needCount = 5;  // 5개 필요
		} else if (myName.equalsIgnoreCase("기운을 잃은 악몽의 장궁")) {
			targetName = "악몽의 장궁";
			needCount = 5;  // 5개 필요	
		} else if (myName.equalsIgnoreCase("기운을 잃은 제로스의 지팡이")) {
			targetName = "제로스의 지팡이";
			needCount = 5;  // 5개 필요	
		} else if (myName.equalsIgnoreCase("기운을 잃은 포효의 이도류")) {
			targetName = "포효의 이도류";
			needCount = 5;  // 5개 필요	
		// =====================4대 마법	
		} else if (myName.equalsIgnoreCase("기운을 잃은 마법서 (브레이브 멘탈)")) {
			targetName = "마법서 (브레이브 멘탈)";
			needCount = 10;  // 10개 필요	
		} else if (myName.equalsIgnoreCase("기운을 잃은 마법서 (디스인티그레이트)")) {
			targetName = "마법서 (디스인티그레이트)";
			needCount = 10;  // 10개 필요
		} else if (myName.equalsIgnoreCase("기운을 잃은 기술서 (카운터 배리어)")) {
			targetName = "기술서 (카운터 배리어)";
			needCount = 10;  // 10개 필요	
		} else if (myName.equalsIgnoreCase("기운을 잃은 정령의 수정 (스트라이커 게일)")) {
			targetName = "정령의 수정 (소울 오브 프레임)";
			needCount = 10;  // 10개 필요		
		} else if (myName.equalsIgnoreCase("정령의 수정 (스트라이커 게일)")) {
			targetName = "정령의 수정 (스트라이커 게일)";
			needCount = 10;  // 10개 필요		
		} else if (myName.equalsIgnoreCase("기운을 잃은 흑정령의 수정 (아머 브레이크)")) {
			targetName = "흑정령의 수정 (아머 브레이크)";
			needCount = 10;  // 10개 필요	
		// ==================신화 마법	
		} else if (myName.equalsIgnoreCase("기운을 잃은 포스 스턴")) {
			targetName = "포스 스턴";
			needCount = 10;  // 10개 필요	
		} else if (myName.equalsIgnoreCase("기운을 잃은 임페리얼 아머")) {
			targetName = "임페리얼 아머";
			needCount = 10;  // 10개 필요		
		} else if (myName.equalsIgnoreCase("기운을 잃은 쉐도우 스턴")) {
			targetName = "쉐도우 스턴";
			needCount = 10;  // 10개 필요		
		} else if (myName.equalsIgnoreCase("기운을 잃은 네메시스")) {
			targetName = "네메시스";
			needCount = 10;  // 10개 필요		
		} else if (myName.equalsIgnoreCase("기운을 잃은 엘리멘탈 샷")) {
			targetName = "엘리멘탈 샷";
			needCount = 10;  // 10개 필요		
		} else if (myName.equalsIgnoreCase("기운을 잃은 지휘관의 투구")) {
			targetName = "지휘관의 투구";
			needCount = 3;  // 10개 필요		
		} else if (myName.equalsIgnoreCase("기운을 잃은 머미로드의 왕관")) {
			targetName = "머미로드의 왕관";
			needCount = 3;  // 10개 필요		
		} else if (myName.equalsIgnoreCase("기운을 잃은 대마법사의 모자")) {
			targetName = "대마법사의 모자";
			needCount = 3;  // 10개 필요		
		} else if (myName.equalsIgnoreCase("기운을 잃은 뱀파이어의 망토")) {
			targetName = "뱀파이어의 망토";
			needCount = 3;  // 10개 필요		
		} else if (myName.equalsIgnoreCase("기운을 잃은 격분의 장갑")) {
			targetName = "격분의 장갑";
			needCount = 3;  // 10개 필요		
		} else if (myName.equalsIgnoreCase("기운을 잃은 아이리스의 장갑")) {
			targetName = "아이리스의 장갑";
			needCount = 3;  // 10개 필요		
		} else if (myName.equalsIgnoreCase("기운을 잃은 머미로드의 장갑")) {
			targetName = "머미로드의 장갑";
			needCount = 3;  // 10개 필요		
		} else if (myName.equalsIgnoreCase("기운을 잃은 나이트발드의 부츠")) {
			targetName = "나이트발드의 부츠";
			needCount = 3;  // 10개 필요		
		} else if (myName.equalsIgnoreCase("기운을 잃은 아이리스의 부츠")) {
			targetName = "아이리스의 부츠";
			needCount = 3;  // 10개 필요		
		} else if (myName.equalsIgnoreCase("우그누스의 가더")) {
			targetName = "우그누스의 가더";
			needCount = 3;  // 10개 필요		
		} else if (myName.equalsIgnoreCase("쿠거의 가더")) {
			targetName = "쿠거의 가더";
			needCount = 3;  // 10개 필요		
		} else if (myName.equalsIgnoreCase("시어의 심안")) {
			targetName = "시어의 심안";
			needCount = 3;  // 10개 필요			
		
		} else {
			// 지정되지 않은 룬 조각일 경우 방어용
			return; 
		}
		
		// 2. 조각이 필요 개수(needCount) 이상 있는지 확인
				if (this.getCount() >= needCount) {
					Item resultItem = ItemDatabase.find(targetName);
					
					if (resultItem != null) {
						// 3. 인벤토리에서 조각 먼저 차감
						cha.getInventory().count(this, this.getCount() - needCount, true);
						
						// ==========================================
						// 💡 [수정] 5. 아이템 속성에 따른 겹침 처리 및 확인 상태 지급
						// (생명의 나뭇잎 소스코드 로직 100% 반영)
						// ==========================================
						ItemInstance existItem = cha.getInventory().find(targetName);
						
						// DB 아이템 속성이 겹칠 수 있는 아이템(isPiles)이면서 인벤토리에 이미 존재할 경우
						if (resultItem.isPiles() && existItem != null) {
							// 겹치는 아이템이 존재할 경우 수량 합산
							cha.getInventory().count(existItem, existItem.getCount() + 1, true);
						} 
						else {
							// 겹칠 수 없는 아이템(무기/방어구 등)이거나, 인벤토리에 처음 들어오는 경우
							ItemInstance temp = ItemDatabase.newInstance(resultItem);
							temp.setObjectId(ServerDatabase.nextItemObjId());
							temp.setCount(1);
							temp.setDefinite(true); // 💡 서버팩 고유 명령어: 미확인 해제 (확인 상태로 변경)
							cha.getInventory().append(temp, true);
						}
						// ==========================================
						
						// 4. 성공 메시지 출력
						ChattingController.toChatting(cha, "\\fR조각 " + needCount + "개로 '" + targetName + "'을(를) 획득하였습니다!", Lineage.CHATTING_MODE_MESSAGE);
						
					} else {
						ChattingController.toChatting(cha, "서버에 '" + targetName + "' 아이템이 존재하지 않습니다. 운영자에게 문의하세요.", Lineage.CHATTING_MODE_MESSAGE);
					}
					
				} else {
					ChattingController.toChatting(cha, "조각이 " + needCount + "개 이상 필요합니다. (현재: " + this.getCount() + "개)", Lineage.CHATTING_MODE_MESSAGE);
				}
			}
}