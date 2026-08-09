package lineage.world.object.item.all_night;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.TalkScroll;
import lineage.database.TalkScrollDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class TalkingScroll extends ItemInstance {

	static public final int TeleportHomeImpossibilityMap[] = { 70, 89, 509, 809, 810, 811, 1400 };

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new TalkingScroll();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha == null)
			return;

		if (!(cha instanceof PcInstance))
			return;
		PcInstance pc = (PcInstance) cha;

		// =========================================================
		// 🚨 [추가] 오픈대기, 일부행동, 일부지역 제한 방어 로직
		// =========================================================

		// 1. 자동사냥 중일 경우 자동사냥 상태 초기화 및 해제
		if (pc.isAutoHunt) {
			pc.isAutoHunt = false;
			pc.autohunt_target = null;
			pc.is_auto_return_home = false;
		}

		// 2. 오픈 대기 상태일 때 이동 제한
		if (Lineage.open_wait) {
			ChattingController.toChatting(pc, "[오픈대기] 오픈대기에는 이동 하실수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		// 3. 사망, 행동 제한(락), 낚시 중일 때 사용 제한
		if (pc.isDead() || pc.isLock() || pc.isFishing()) {
			ChattingController.toChatting(pc, "현재 상태에선 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		// 4. 이동 불가능 지역(맵 번호) 체크
		// 💡 팁: 만약 컴파일 시 TeleportHomeImpossibilityMap 부분에 빨간 줄(에러)이 뜬다면,
		// 해당 배열이 정의되어 있는 원본 클래스명(예: TeleportHomeDatabase.TeleportHomeImpossibilityMap)을
		// 앞에 붙여주세요.
		for (int cantMap : TeleportHomeImpossibilityMap) {
			if (pc.getMap() == cantMap) {
				ChattingController.toChatting(pc, "이곳에서는 해당 아이템을 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
		}
		// =========================================================

		// 모든 제한 조건을 통과한 경우에만 아래의 텔레포트 목록 생성 코드 실행
		List<String> list = new ArrayList<String>();
		List<TalkScroll> slotList = TalkScrollDatabase.getDisplaySlotList();

		for (int i = 0; i < slotList.size() && i < 80; i++) {
			TalkScroll ts = slotList.get(i);

			if (ts == null) {
				list.add(" ");
			} else {
				list.add(ts.getName());
			}
		}

		while (list.size() < 80)
			list.add(" ");

		cha.toSender(S_Html.clone(
				BasePacketPooling.getPool(S_Html.class),
				cha,
				"victor1",
				null,
				list));
	}
}