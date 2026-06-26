package lineage.world.object.item.all_night;

import java.text.SimpleDateFormat;
import java.util.Date;

import all_night.Lineage_Balance;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.share.Lineage;
import lineage.util.Util; 
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ScrollOfRoomtisEnchant extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ScrollOfRoomtisEnchant();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// 주문서를 클릭하자마자 콘솔창에 무조건 출력!
		lineage.share.System.println("[디버그] 룸티스 주문서 클릭됨: " + cha.getName());
		if (!(cha instanceof PcInstance) || cha.getInventory() == null) 
			return;

		PcInstance pc = (PcInstance) cha;
		if (cbp == null) return;

		int targetObjId = cbp.readD();
		ItemInstance targetItem = pc.getInventory().value(targetObjId);

		if (targetItem == null) {
			for (ItemInstance item : pc.getInventory().getList()) {
				if (item.getObjectId() == targetObjId) {
					targetItem = item;
					break;
				}
			}
		}

		if (targetItem == null) {
			ChattingController.toChatting(pc, "강화 대상을 찾을 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		if (targetItem.getObjectId() == this.getObjectId()) {
			ChattingController.toChatting(pc, "주문서 자신에게는 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		if (targetItem.isEquipped()) {
			ChattingController.toChatting(pc, "착용 중인 장비는 강화할 수 없습니다. 해제 후 사용하세요.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		String name = targetItem.getItem().getName();
		if (!name.contains("룸티스")) {
			ChattingController.toChatting(pc, "룸티스 귀걸이에만 사용 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		int maxEnchant = Lineage.item_enchant_accessory_max; 
		if (maxEnchant > 0 && targetItem.getEnLevel() >= maxEnchant) {
			ChattingController.toChatting(pc, String.format("장신구는 최대 +%d까지 인챈트 가능합니다.", maxEnchant), Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		double chance = 0;
		int currentEnLevel = targetItem.getEnLevel();
		
		switch (currentEnLevel) {
			case 0: chance = Lineage_Balance.roomtis_enchant_prob0; break;
			case 1: chance = Lineage_Balance.roomtis_enchant_prob1; break;
			case 2: chance = Lineage_Balance.roomtis_enchant_prob2; break;
			case 3: chance = Lineage_Balance.roomtis_enchant_prob3; break;
			case 4: chance = Lineage_Balance.roomtis_enchant_prob4; break;
			case 5: chance = Lineage_Balance.roomtis_enchant_prob5; break;
			case 6: chance = Lineage_Balance.roomtis_enchant_prob6; break;
			case 7: chance = Lineage_Balance.roomtis_enchant_prob7; break;
			case 8: chance = Lineage_Balance.roomtis_enchant_prob8; break;
			case 9: chance = Lineage_Balance.roomtis_enchant_prob9; break;
			default: chance = 0; break;
		}

		// ==========================================
		// ✨ [보안 강화] 오직 "보호 주문서"만 천장 시스템 이용!
		// ==========================================
		boolean isProtectScroll = this.getItem().getName().contains("보호");
		
		int maxPityCount = 0; 
		int currentPityCount = 0;
		boolean isPityTriggered = false;

		if (currentEnLevel == 5) {
			maxPityCount = Lineage_Balance.roomtis_pity_count_5;
			currentPityCount = pc.roomtisCount5;
		} else if (currentEnLevel == 6) {
			maxPityCount = Lineage_Balance.roomtis_pity_count_6;
			currentPityCount = pc.roomtisCount6;
		} else if (currentEnLevel == 7) {
			maxPityCount = Lineage_Balance.roomtis_pity_count_7;
			currentPityCount = pc.roomtisCount7;
		}

		// 💡 [핵심] 보호 주문서(isProtectScroll)를 사용했을 때만 100% 천장 혜택이 발동됩니다!
		// 일반 주문서를 쓰면 스택이 다 찼더라도 본래 확률(chance)대로 굴러갑니다.
		if (isProtectScroll && maxPityCount > 0 && currentPityCount >= (maxPityCount - 1)) {
			chance = 1.1; 
			isPityTriggered = true;
		}
		// ==========================================

		pc.getInventory().count(this, getCount() - 1, true);

		final String timeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		final String charName = pc.getName();
		final String scrollName = this.getItem().getName();
		final String oldItemName = "+" + currentEnLevel + " " + name; 

		// 4. 강화 연산 시작 
		if (Math.random() < chance) {
			// [강화 성공]
			int newEnLevel = targetItem.getEnLevel() + 1;
			targetItem.setEnLevel(newEnLevel);
			
			// ✨ 성공 시 해당 구간의 스택을 0으로 초기화
			if (currentEnLevel == 5) pc.roomtisCount5 = 0;
			else if (currentEnLevel == 6) pc.roomtisCount6 = 0;
			else if (currentEnLevel == 7) pc.roomtisCount7 = 0;
			
//			if (isPityTriggered) {
//				ChattingController.toChatting(pc, "\\fV[시스템] 천장 달성! 보호 주문서의 힘으로 100% 강화에 성공하였습니다. (+" + newEnLevel + ")", Lineage.CHATTING_MODE_MESSAGE);
//			} else {
//				ChattingController.toChatting(pc, "\\fV" + name + " 강화에 성공하였습니다. (+" + newEnLevel + ")", Lineage.CHATTING_MODE_MESSAGE);
//			}
			
			// 천장이 발동했든 안 했든, 유저에게는 티 내지 않고 일반 성공 메시지만 출력합니다.
			ChattingController.toChatting(pc, "\\fV" + name + " 강화에 성공하였습니다. (+" + newEnLevel + ")", Lineage.CHATTING_MODE_MESSAGE);
						
			pc.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), targetItem));

			final String logMessage = String.format("[%s] [룸티스 성공]\t [캐릭터: %s]\t [결과: +%d %s]\t [주문서: %s]", 
					timeString, charName, newEnLevel, name, scrollName);
			lineage.gui.GuiMain.display.asyncExec(new Runnable() {
				public void run() { lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(logMessage); }
			});

		} else {
			// [강화 실패]
			if (isProtectScroll) {
				
				// ✨ 보호 주문서로 실패 시 천장 스택 1 증가
				if (currentEnLevel == 5) pc.roomtisCount5 += 1;
				else if (currentEnLevel == 6) pc.roomtisCount6 += 1;
				else if (currentEnLevel == 7) pc.roomtisCount7 += 1;
				
				// 방금 올린 스택을 변수에 다시 담아 출력 준비
				int updatedPity = (currentEnLevel == 5) ? pc.roomtisCount5 : (currentEnLevel == 6) ? pc.roomtisCount6 : (currentEnLevel == 7) ? pc.roomtisCount7 : 0;
				
				String pityStatus = "";
//				if (maxPityCount > 0) pityStatus = " (천장 누적: " + updatedPity + " / " + maxPityCount + ")";

				ChattingController.toChatting(pc, "\\fT" + name + "가 보호되었습니다." + pityStatus, Lineage.CHATTING_MODE_MESSAGE);
				
				final String logMessage = String.format("[%s] [룸티스 보호]\t [캐릭터: %s]\t [아이템: %s]\t [누적:%d/%d]", 
						timeString, charName, oldItemName, updatedPity, maxPityCount);
				lineage.gui.GuiMain.display.asyncExec(new Runnable() {
					public void run() { lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(logMessage); }
				});
				
			} else {
				// 💀 일반 주문서로 실패하여 증발 (스택은 함께 소멸되도록 초기화)
				if (currentEnLevel == 5) pc.roomtisCount5 = 0;
				else if (currentEnLevel == 6) pc.roomtisCount6 = 0;
				else if (currentEnLevel == 7) pc.roomtisCount7 = 0;

				ChattingController.toChatting(pc, "\\fR" + name + " 강화 실패로 아이템이 증발되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
				pc.getInventory().count(targetItem, 0, true); 

				final String logMessage = String.format("[%s] [룸티스 증발]\t [캐릭터: %s]\t [소멸: %s]\t [주문서: %s]", 
						timeString, charName, oldItemName, scrollName);
				lineage.gui.GuiMain.display.asyncExec(new Runnable() {
					public void run() { lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(logMessage); }
				});
			}
		}
		
		pc.toCharacterSave2(); // DB 저장 유도
	}
}