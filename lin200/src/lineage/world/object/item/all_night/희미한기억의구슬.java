package lineage.world.object.item.all_night;

import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.BookController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 희미한기억의구슬 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 희미한기억의구슬();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if (cha instanceof PcInstance) {
			PcInstance pc = (PcInstance) cha;
			
			// =========================================================
			// 💡 [수정] 한 번에 등록할 사냥터 목록 (배열 사용)
			// { "기억창에 표시될 이름", "X좌표", "Y좌표", "맵번호" }
			// image_6f1b98.png 처럼 대괄호와 점(.)을 이용해 간격을 수동으로 맞춥니다.
			// =========================================================
			String[][] locList = {
				{ "\\f1[공성전]... 기란성 5시", "33641", "32761", "4" },
				{ "\\f1[공성전]... 기란성 7시", "33558", "32673", "4" },
				{ "\\fT[마을]... 잡화상점", "33443", "32807", "4" },
				{ "\\fT[마을]... 창     고", "33427", "32816", "4" },
				{ "\\fT[마을]... 기란게시", "33429", "32801", "4" },
				{ "\\fT[마을]... 로테이션", "33448", "32790", "4" },
				{ "\\fU[일반]... 오아시스", "32863", "33252", "4" },
				{ "\\fU[일반]... 난쟁이밭 1", "33946", "32305", "4" },
				{ "\\fU[일반]... 난쟁이밭 2", "33901", "32247", "4" },
				{ "\\fU[일반]... 하이네필드 1", "33376", "33215", "4" },
				{ "\\fU[일반]... 하이네필드 2", "33358", "33339", "4" },
				{ "\\fU[일반]... 오우거밭 1", "32735", "32268", "4" },
				{ "\\fU[일반]... 오우거밭 2", "32782", "32953", "4" },
				{ "\\fU[일반]... 도펠밭 1", "33744", "33378", "4" },
				{ "\\fU[일반]... 싸이밭 1", "33649", "33064", "4" },
				{ "\\fV[일반]... 엘모어밭 1", "33993", "32459", "4" },
				{ "\\fV[일반]... 엘모어밭 2", "33981", "32547", "4" },
				{ "\\fV[일반]... 엘모어밭 4", "34050", "32459", "4" },
				{ "\\fV[일반]... 오렌설벽 1", "34262", "32437", "4" },
				{ "\\fV[일반]... 오렌설벽 2", "34216", "32543", "4" },
				{ "\\fV[일반]... 용의계곡 1", "33348", "32351", "4" },
				{ "\\fV[일반]... 용의계곡 2", "33396", "32328", "4" },
				{ "\\fV[일반]... 용의계곡 3", "33370", "32385", "4" },
				{ "\\fV[일반]... 용의계곡 4", "33412", "32414", "4" },
				{ "\\fW[일반]... 용계 작은뼈", "33261", "32395", "4" },
				{ "\\fW[일반]... 용계 큰  뼈", "33389", "32335", "4" },
				{ "\\fW[일반]... 화룡의둥지", "33625", "32418", "4" }
//				{ "\\fR[068]..용의계곡 01번", "34244", "33100", "4" }
				// 콤마(,)를 붙여서 무한정 추가하실 수 있습니다!
			};
			
			// 인벤토리에서 구슬 1개 차감
			pc.getInventory().count(this, this.getCount() - 1, true);
			
			int addCount = 0; // 성공적으로 추가된 개수 카운트
			
			// 배열에 들어있는 개수만큼 반복해서 기억창에 추가합니다.
			for (int i = 0; i < locList.length; i++) {
				String bName = locList[i][0];
				int locX = Integer.parseInt(locList[i][1]);
				int locY = Integer.parseInt(locList[i][2]);
				int mapId = Integer.parseInt(locList[i][3]);
				
				// 이미 동일한 이름의 북마크가 있는지 검사 (중복 추가 방지)
				if (BookController.find(pc, bName) == null) {
					BookController.append(pc, bName, locX, locY, mapId);
					addCount++;
				}
			}
			
			// 완료 메세지
			if (addCount > 0) {
				ChattingController.toChatting(pc, "\\fV기억의 구슬이 빛나며 " + addCount + "개의 장소가 기억되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			} else {
				ChattingController.toChatting(pc, "이미 모든 장소가 기억되어 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}
}