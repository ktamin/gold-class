package lineage.world.object.item.scroll;

import lineage.database.TeleportHomeDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.world.controller.LocationController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class ScrollLabeledVerrYedHorae extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ScrollLabeledVerrYedHorae();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// [수정] 이름에 '기란 마을 이동 부적'이 포함되어 있는지 확인
		if (getItem().getName().contains("기란 마을 귀환 부적")) {
			// 부적 아이템이므로 수량을 감소시키지 않음 (영구 사용)
		} else {
			// 그 외의 일반 주문서는 기존처럼 1개 소모
			cha.getInventory().count(this, getCount() - 1, true);
		}

		// 텔레포트 로직 (공통 실행)
		if (LocationController.isTeleportVerrYedHoraeZone(cha, true)) {
			TeleportHomeDatabase.toLocation(cha);
			cha.toTeleport(cha.getHomeX(), cha.getHomeY(), cha.getHomeMap(), true);
		}
	}

}