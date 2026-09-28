package lineage.world.object.item.all_night;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 마법변경주문서 extends ItemInstance {

	// ==========================================
	// ✅ 1급 마법 그룹 정의 (기존 리스트)
	// ==========================================
	private static final List<String> GRADE1 = Arrays.asList(
		"기술서 (카운터 배리어)",
		"마법서 (브레이브 멘탈)",
		"마법서 (디스인티그레이트)",
		"흑정령의 수정 (아머 브레이크)",
		"정령의 수정 (스트라이커 게일)",
		"매스 이뮨 투 함",
		"정령의 수정 (소울 오브 프레임)"
	);

	// ==========================================
	// ✅ 2급 마법 그룹 정의 (신규 추가 리스트)
	// ==========================================
	private static final List<String> GRADE2 = Arrays.asList(
		"포스 스턴",
		"네메시스",
		"쉐도우 스턴",
		"엘리멘탈 샷",
		"임페리얼 아머"
	);

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 마법변경주문서();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() == null) return;

		ItemInstance src = cha.getInventory().value(cbp.readD());

		if (src != null && src.getItem() != null) {
			final String srcName = src.getItem().getName();
			List<String> pool = null;

			// 어느 등급(그룹)에 속하는지 판별
			if (containsIgnoreCase(GRADE1, srcName)) {
				pool = new ArrayList<>(GRADE1);
			} else if (containsIgnoreCase(GRADE2, srcName)) {
				pool = new ArrayList<>(GRADE2);
			} else {
				ChattingController.toChatting(cha, "해당 마법서에는 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			// 자기 자신 제외 (같은 마법서가 나오는 것 방지)
			removeIgnoreCase(pool, srcName);
			if (pool.isEmpty()) {
				ChattingController.toChatting(cha, "변경 가능한 대상이 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			// 랜덤 선택
			String dstName = pool.get(Util.random(0, pool.size() - 1));
			Item dstItem = ItemDatabase.find(dstName);
			
			if (dstItem != null) {
				ItemInstance dst = ItemDatabase.newInstance(dstItem);
				dst.setObjectId(ServerDatabase.nextItemObjId());
				dst.setBless(src.getBless()); // 마법서의 축/저주 상태 승계
				dst.setDefinite(true);
				
				// 인벤 적용
				cha.getInventory().append(dst, true);
				
				// 기존 마법서/주문서 소모
				cha.getInventory().count(src, src.getCount() - 1, true);
				cha.getInventory().count(this, getCount() - 1, true);
				
				// 안내 멘트
				ChattingController.toChatting(
					cha, 
					String.format("%s 획득하였습니다.", Util.getStringWord(dst.getItem().getName(), "을", "를")), 
					Lineage.CHATTING_MODE_MESSAGE
				);
			} else {
				ChattingController.toChatting(cha, "대상 아이템 정보를 찾을 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

		} else {
			ChattingController.toChatting(cha, "지정된 법서에만 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	// ───────────────────────── 헬퍼 (리스트 관리용) ─────────────────────────

	private boolean containsIgnoreCase(List<String> list, String name) {
		if (name == null) return false;
		for (String s : list) {
			if (s != null && s.equalsIgnoreCase(name)) return true;
		}
		return false;
	}

	private void removeIgnoreCase(List<String> list, String name) {
		if (name == null) return;
		for (int i = list.size() - 1; i >= 0; i--) {
			String s = list.get(i);
			if (s != null && s.equalsIgnoreCase(name)) {
				list.remove(i);
				return;
			}
		}
	}
}