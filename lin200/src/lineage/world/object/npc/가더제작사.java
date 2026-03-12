package lineage.world.object.npc;

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

public class 가더제작사 extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		// data/html/guader.htm 파일을 열어줍니다.
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "guader"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
		if (pc.getInventory() == null) return;

		// 1. 가더 제작
		if (action.equalsIgnoreCase("화령의 가더")) {
			// +5이상 고대 투사의 가더 + 화룡 비늘(50) + 신비한 날개 깃털(10만) -> 화령의 가더
			upgradeItem(pc, "고대 투사의 가더", "영웅 제작 비법서", 50, "신비한 날개깃털", 100000, "화령의 가더");
			
		} else if (action.equalsIgnoreCase("지령의 가더")) {
			// +5이상 마법사의 가더 + 수룡 비늘(50) + 신비한 날개 깃털(10만) -> 수령의 가더
			upgradeItem(pc, "고대 투사의 가더", "영웅 제작 비법서", 1, "신비한 날개깃털", 100000, "지령의 가더");		

		} else if (action.equalsIgnoreCase("풍령의 가더")) {
			// +5이상 고대 명궁의 가더 + 풍룡 비늘(50) + 신비한 날개 깃털(10만) -> 풍령의 가더
			upgradeItem(pc, "고대 명궁의 가더", "영웅 제작 비법서", 1, "신비한 날개깃털", 100000, "풍령의 가더");

		} else if (action.equalsIgnoreCase("수령의 가더")) {
			// +5이상 마법사의 가더 + 수룡 비늘(50) + 신비한 날개 깃털(10만) -> 수령의 가더
			upgradeItem(pc, "마법사의 가더", "영웅 제작 비법서", 1, "신비한 날개깃털", 100000, "수령의 가더");	

		} 
		// 2. 티셔츠 제작
		else if (action.equalsIgnoreCase("화룡의 티셔츠")) {
			// +5이상 완력의 티셔츠 + 화룡 비늘(50) + 깃털 -> 화룡의 티셔츠
			upgradeItem(pc, "완력의 티셔츠", "영웅 제작 비법서", 1, "신비한 날개깃털", 100000, "화룡의 티셔츠");

		} else if (action.equalsIgnoreCase("풍룡의 티셔츠")) {
			// +5이상 민첩의 티셔츠 + 풍룡 비늘(50) + 깃털 -> 풍룡의 티셔츠
			upgradeItem(pc, "민첩의 티셔츠", "영웅 제작 비법서", 1, "신비한 날개깃털", 100000, "풍룡의 티셔츠");

		} else if (action.equalsIgnoreCase("수룡의 티셔츠")) {
			// +5이상 지식의 티셔츠 + 수룡 비늘(50) + 깃털 -> 수룡의 티셔츠
			upgradeItem(pc, "지식의 티셔츠", "영웅 제작 비법서", 1, "신비한 날개깃털", 100000, "수룡의 티셔츠");

		}
		// 3. 귀걸이 제작 (노예의 귀걸이 업그레이드)
		else if (action.equalsIgnoreCase("화령의 귀걸이")) {
			upgradeItem(pc, "노예의 귀걸이", "영웅 제작 비법서", 1, "신비한 날개깃털", 100000, "화령의 귀걸이");

		} else if (action.equalsIgnoreCase("풍령의 귀걸이")) {
			upgradeItem(pc, "노예의 귀걸이", "영웅 제작 비법서", 1, "신비한 날개깃털", 100000, "풍령의 귀걸이");

		} else if (action.equalsIgnoreCase("수령의 귀걸이")) {
			upgradeItem(pc, "노예의 귀걸이", "영웅 제작 비법서", 1, "신비한 날개깃털", 100000, "수령의 귀걸이");

		} else if (action.equalsIgnoreCase("지령의 귀걸이")) {
			upgradeItem(pc, "노예의 귀걸이", "영웅 제작 비법서", 1, "신비한 날개깃털", 100000, "지령의 귀걸이");
		}
	}

	/**
	 * 아이템 업그레이드 처리 함수 (인첸트 유지)
	 */
	private void upgradeItem(PcInstance pc, String srcItemName, String mat1Name, int mat1Count, String mat2Name, int mat2Count, String targetItemName) {
		// 1. 인벤토리에서 +5 이상인 재료 장비 찾기 (착용중이지 않은 것)
		ItemInstance srcItem = null;
		for (ItemInstance item : pc.getInventory().getList()) {
			if (item.getItem().getName().equalsIgnoreCase(srcItemName) && item.getEnLevel() >= 5 && !item.isEquipped()) {
				srcItem = item;
				break; 
			}
		}

		// 2. 재료 아이템 확인
		ItemInstance mat1 = pc.getInventory().find(mat1Name);
		ItemInstance mat2 = pc.getInventory().find(mat2Name);

		// 3. 조건 확인
		if (srcItem != null && 
			(mat1 != null && mat1.getCount() >= mat1Count) && 
			(mat2 != null && mat2.getCount() >= mat2Count)) {

			int currentEnchant = srcItem.getEnLevel();
			int currentBless = srcItem.getBless();

			// 4. 재료 삭제
			pc.getInventory().count(srcItem, srcItem.getCount() - 1, true); 
			pc.getInventory().count(mat1, mat1.getCount() - mat1Count, true);
			pc.getInventory().count(mat2, mat2.getCount() - mat2Count, true);

			// 5. 결과 아이템 지급
			Item i = ItemDatabase.find(targetItemName);
			if (i != null) {
				ItemInstance newItem = ItemDatabase.newInstance(i);
				newItem.setObjectId(ServerDatabase.nextItemObjId());
				newItem.setBless(currentBless);
				newItem.setEnLevel(currentEnchant); // 인첸트 유지
				newItem.setCount(1);
				newItem.setDefinite(true);
				pc.getInventory().append(newItem, true);

				ChattingController.toChatting(pc, String.format("\\fY[제작 성공] +%d %s", currentEnchant, targetItemName), Lineage.CHATTING_MODE_MESSAGE);
			}
		} else {
			String msg = String.format("재료부족: +5이상 %s, %s(%d), %s(%,d)", srcItemName, mat1Name, mat1Count, mat2Name, mat2Count);
			ChattingController.toChatting(pc, msg, Lineage.CHATTING_MODE_MESSAGE);
		}
	}
}