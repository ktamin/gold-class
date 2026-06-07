package lineage.world.object.item.all_night;

import all_night.Lineage_Balance;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.Enchant;

public class ScrollOfAccessory extends Enchant {
	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ScrollOfAccessory();
		return item;
	}
	/*
	 * @Override
	 * public void toClick(Character cha, ClientBasePacket cbp) {
	 * if (cha.getInventory() != null) {
	 * ItemInstance item = cha.getInventory().value(cbp.readD());
	 * 
	 * if (item.isAcc() && item.getItem().isEnchant()) {
	 * if (item != null) {
	 * 
	 * 
	 * if (getItem() != null &&
	 * getItem().getName().equalsIgnoreCase("오림의 장신구 마법 주문서") && bless == 0) {
	 * if (item.getEnLevel() < Lineage_Balance.bless_orim_acc_min_en) {
	 * ChattingController.toChatting(cha, String.format("+%d 이상 장신구에 사용 가능합니다.",
	 * Lineage_Balance.bless_orim_acc_min_en), Lineage.CHATTING_MODE_MESSAGE);
	 * return;
	 * }
	 * }
	 * 
	 * if (cha instanceof PcInstance) {
	 * int en = toEnchant(cha, item, this);
	 * 
	 * item.toEnchant((PcInstance) cha, en);
	 * 
	 * if (en != -127)
	 * cha.getInventory().count(this, getCount() - 1, true);
	 * }
	 * }
	 * } else {
	 * if (!item.getItem().isEnchant())
	 * ChattingController.toChatting(cha, "인챈트가 불가능한 장신구입니다.",
	 * Lineage.CHATTING_MODE_MESSAGE);
	 * else
	 * ChattingController.toChatting(cha, "장신구에만 사용 가능합니다.",
	 * Lineage.CHATTING_MODE_MESSAGE);
	 * }
	 * }
	 * }
	 * }
	 */

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() != null) {
			ItemInstance item = cha.getInventory().value(cbp.readD());

			// 0. 대상 아이템 유효성 체크
			if (item == null || item.getItem() == null)
				return;

			// ▼▼▼ [수정 포인트] 룸티스 & 스냅퍼 차단 로직 ▼▼▼
			String itemName = item.getItem().getName();
			if (itemName.contains("룸티스") || itemName.contains("스냅퍼")) {
				ChattingController.toChatting(cha, "해당 아이템은 전용 주문서로만 강화가 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			// ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲

			if (item.isAcc() && item.getItem().isEnchant()) {
				if (item != null) {

					// 오림의 장신구 마법 주문서 최소 강화 수치 체크
					if (getItem() != null && getItem().getName().equalsIgnoreCase("오림의 장신구 마법 주문서") && bless == 0) {
						if (item.getEnLevel() < Lineage_Balance.bless_orim_acc_min_en) {
							ChattingController.toChatting(cha,
									String.format("+%d 이상 장신구에 사용 가능합니다.", Lineage_Balance.bless_orim_acc_min_en),
									Lineage.CHATTING_MODE_MESSAGE);
							return;
						}
					}

					if (cha instanceof PcInstance) {
						int en = toEnchant(cha, item, this);

						item.toEnchant((PcInstance) cha, en);

						if (en != -127)
							cha.getInventory().count(this, getCount() - 1, true);
					}
				}
			} else {
				if (!item.getItem().isEnchant())
					ChattingController.toChatting(cha, "인챈트가 불가능한 장신구입니다.", Lineage.CHATTING_MODE_MESSAGE);
				else
					ChattingController.toChatting(cha, "장신구에만 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}

}

	

	
				

					
									
									