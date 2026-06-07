package lineage.world.object.item.all_night;

import all_night.Lineage_Balance;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class ScrollOfRoomtisEnchant extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ScrollOfRoomtisEnchant();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() != null) {
			ItemInstance targetItem = cha.getInventory().value(cbp.readD());
			if (targetItem == null)
				return;

			// 1. 조건 검사 (주문서 소모 전)
			String name = targetItem.getItem().getName();
			String cleanName = name.replace(" ", "");

			// 대상 아이템 확인
			if (!cleanName.contains("룸티스의검은빛귀걸이") &&
					!cleanName.contains("룸티스의붉은빛귀걸이") &&
					!cleanName.contains("룸티스의보랏빛귀걸이") &&
					!cleanName.contains("룸티스의푸른빛귀걸이")) {
				ChattingController.toChatting(cha, "룸티스 귀걸이에만 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return; // 여기서 종료되면 주문서가 사라지지 않음
			}

			// 착용 여부 확인
			if (targetItem.isEquipped()) {
				ChattingController.toChatting(cha, "착용 중인 상태로는 강화할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			// 최대 강화 확인
			int maxEnchant = Lineage.item_enchant_accessory_max;
			if (maxEnchant > 0 && targetItem.getEnLevel() >= maxEnchant) {
				ChattingController.toChatting(cha, String.format("최대 +%d까지 인챈트 가능합니다.", maxEnchant),
						Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			// 2. 확률 설정 로드
			double chance = 0;
			int currentEnLevel = targetItem.getEnLevel();
			switch (currentEnLevel) {
				case 0:
					chance = Lineage_Balance.roomtis_enchant_prob0;
					break;
				case 1:
					chance = Lineage_Balance.roomtis_enchant_prob1;
					break;
				case 2:
					chance = Lineage_Balance.roomtis_enchant_prob2;
					break;
				case 3:
					chance = Lineage_Balance.roomtis_enchant_prob3;
					break;
				case 4:
					chance = Lineage_Balance.roomtis_enchant_prob4;
					break;
				case 5:
					chance = Lineage_Balance.roomtis_enchant_prob5;
					break;
				case 6:
					chance = Lineage_Balance.roomtis_enchant_prob6;
					break;
				case 7:
					chance = Lineage_Balance.roomtis_enchant_prob7;
					break;
				case 8:
					chance = Lineage_Balance.roomtis_enchant_prob8;
					break;
				case 9:
					chance = Lineage_Balance.roomtis_enchant_prob9;
					break;
				default:
					chance = 0;
					break;
			}

			boolean isProtectScroll = this.getItem().getName().contains("보호");

			// 3. 모든 조건을 통과했을 때만 주문서 소모
			cha.getInventory().count(this, getCount() - 1, true);

			// 4. 강화 로직 실행 (100% 방식 계산)
			if (Math.random() < (chance / 100.0)) {
				// [성공]
				targetItem.setEnLevel(currentEnLevel + 1);
				cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), targetItem));
				ChattingController.toChatting(cha, name + " 강화에 성공하였습니다. (+" + targetItem.getEnLevel() + ")",
						Lineage.CHATTING_MODE_MESSAGE);

				// ✅ [로그 출력] 성공
				final String log = String.format("[인첸트 성공]\t [캐릭터: %s]\t [아이템: %s (+%d)]\t [주문서: %s]",
						cha.getName(), targetItem.getItem().getName(), targetItem.getEnLevel(),
						this.getItem().getName());
				lineage.gui.GuiMain.display.asyncExec(new Runnable() {
					public void run() {
						lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
					}
				});
			} else {
				// [실패]
				if (this.getItem().getName().contains("보호")) {
					ChattingController.toChatting(cha, name + " 보호주문서로 아이템이 보호되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
					cha.toSender(
							S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), targetItem));

					// ✅ [로그 출력] 실패 (보호됨) - 실패 색상(빨간색)으로 뜹니다.
					final String log = String.format("[인첸트 실패(보호)]\t [캐릭터: %s]\t [아이템: %s (+%d)]\t [주문서: %s]",
							cha.getName(), targetItem.getItem().getName(), targetItem.getEnLevel(),
							this.getItem().getName());
					lineage.gui.GuiMain.display.asyncExec(new Runnable() {
						public void run() {
							lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
						}
					});

				} else {
					ChattingController.toChatting(cha, name + " 강화 실패로 아이템이 증발되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
					// ✅ [로그 출력] 실패 (증발)
					final String log = String.format("[인첸트 실패(증발)]\t [캐릭터: %s]\t [아이템: %s (+%d)]\t [주문서: %s]",
							cha.getName(), targetItem.getItem().getName(), targetItem.getEnLevel(),
							this.getItem().getName());
					lineage.gui.GuiMain.display.asyncExec(new Runnable() {
						public void run() {
							lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
						}
					});

					cha.getInventory().count(targetItem, 0, true);
				}
			}
		}
	}
}