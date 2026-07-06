package lineage.world.object.npc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import all_night.Lineage_Balance;
import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ItemDropMessageDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.MagicDoll;

public class MagicdollCompose extends object {

	public static int 인형_1단계_합성_아데나 = 0;
	public static int 인형_2단계_합성_아데나 = 0;
	public static int 인형_3단계_합성_아데나 = 0;
	public static int 인형_4단계_합성_아데나 = 0;
	public static int 인형_5단계_합성_아데나 = 0;

	public static void loadConfig(String key, String value) {
		if (key.equalsIgnoreCase("doll_aden_cost_1")) 인형_1단계_합성_아데나 = Integer.valueOf(value);
		else if (key.equalsIgnoreCase("doll_aden_cost_2")) 인형_2단계_합성_아데나 = Integer.valueOf(value);
		else if (key.equalsIgnoreCase("doll_aden_cost_3")) 인형_3단계_합성_아데나 = Integer.valueOf(value);
		else if (key.equalsIgnoreCase("doll_aden_cost_4")) 인형_4단계_합성_아데나 = Integer.valueOf(value);
		else if (key.equalsIgnoreCase("doll_aden_cost_5")) 인형_5단계_합성_아데나 = Integer.valueOf(value);
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		List<String> ynlist2 = new ArrayList<>();
		ynlist2.add(String.format("소모되는 아데나 : %s (단계별로 상이)", "설정값 참조"));
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "Magicdoll", null, ynlist2));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
		if (pc.isWorldDelete() || pc.isDead() || pc.isLock() || pc.getInventory() == null) return;

		int count = action.equalsIgnoreCase("특수 합성") ? 3 : 4;
		String itemName = null;
		List<MagicDoll> list = new ArrayList<>();
		List<MagicDoll> magicDollList = new ArrayList<>();

		for (ItemInstance item : pc.getInventory().getList()) {
			if (item instanceof MagicDoll && !item.isEquipped())
				list.add((MagicDoll) item);
		}

		int level = -1;
		int adenaCost = 0;
		String composeLevelName = ""; // 로그 출력을 위한 합성 단계 이름

		if (action.equalsIgnoreCase("1단계 합성")) {
			level = 0; adenaCost = 인형_1단계_합성_아데나; composeLevelName = "1단계 합성";
		} else if (action.equalsIgnoreCase("2단계 합성")) {
			level = 1; adenaCost = 인형_2단계_합성_아데나; composeLevelName = "2단계 합성";
		} else if (action.equalsIgnoreCase("3단계 합성")) {
			level = 2; adenaCost = 인형_3단계_합성_아데나; composeLevelName = "3단계 합성";
		} else if (action.equalsIgnoreCase("4단계 합성")) {
			if (!Lineage.oman2) {
				ChattingController.toChatting(pc, "현재는 5단계인형을 제작 할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			level = 3; adenaCost = 인형_4단계_합성_아데나; composeLevelName = "4단계 합성";
		} else if (action.equalsIgnoreCase("용 합성")) {
			if (!Lineage.oman3) {
				ChattingController.toChatting(pc, "현재는 용인형을 제작 할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			level = 4; count = 2; adenaCost = 인형_5단계_합성_아데나; composeLevelName = "용인형 합성";
		} else {
			return;
		}

		for (MagicDoll magicdoll : list) {
			for (int i = 0; i < Lineage.magicDoll[level].length; i++) {
				if (magicdoll.getItem().getName().equalsIgnoreCase(Lineage.magicDoll[level][i]))
					magicDollList.add(magicdoll);
			}
		}

		if (magicDollList.size() < count) {
			ChattingController.toChatting(pc, String.format("%d단계 인형 %d개 부족합니다.", level + 1, count - magicDollList.size()), Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if (!pc.getInventory().isAden("아데나", adenaCost, true)) {
			ChattingController.toChatting(pc, "아데나가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		double probability = Math.random();
		double perfectChance = 0.0;
		double normalChance = 0.0;

		if (level == 0) {
			perfectChance = Lineage_Balance.magicDoll_class_1_perfect_probability;
			normalChance = Lineage_Balance.magicDoll_class_1_probability;
		} else if (level == 1) {
			perfectChance = Lineage_Balance.magicDoll_class_2_perfect_probability;
			normalChance = Lineage_Balance.magicDoll_class_2_probability;
		} else if (level == 2) {
			perfectChance = Lineage_Balance.magicDoll_class_3_perfect_probability;
			normalChance = Lineage_Balance.magicDoll_class_3_probability + (pc.dollBonus4 / 100.0);
		} else if (level == 3) {
			normalChance = Lineage_Balance.magicDoll_class_4_probability + (pc.dollBonus5 / 100.0);
		} else if (level == 4) {
			normalChance = Lineage_Balance.magicDoll_class_5_probability + (pc.dollBonusDragon / 100.0);
		}

		int maxPity = 0;
		boolean isPity = false;

		if (level == 2) { 
			maxPity = Lineage_Balance.doll_pity_count_4;
			isPity = (maxPity > 0 && pc.dollCount4 >= maxPity);
		} else if (level == 3) {
			maxPity = Lineage_Balance.doll_pity_count_5;
			isPity = (maxPity > 0 && pc.dollCount5 >= maxPity);
		} else if (level == 4) {
			maxPity = Lineage_Balance.doll_pity_count_dragon;
			isPity = (maxPity > 0 && pc.dollCountDragon >= maxPity);
		}

		if (level >= 2) {
			ChattingController.toChatting(pc, String.format("현재 성공 확률: %.1f%%", normalChance * 100.0), Lineage.CHATTING_MODE_MESSAGE);
		}

		boolean isSuccess = false;
		boolean isPerfect = false;

		if (level == 4) {
			if (isPity || probability < normalChance) isSuccess = true;
		} else {
			if (probability < perfectChance) {
				isPerfect = true; 
				isSuccess = true;
			} else if (isPity || probability < normalChance) {
				isSuccess = true;
			}
		}

		if (isPerfect) {
			itemName = Lineage.magicDoll[level + 2][Util.random(0, Lineage.magicDoll[level + 2].length - 1)];
			ChattingController.toChatting(pc, String.format("%d단계 마법인형 합성 대성공!", level + 1), Lineage.CHATTING_MODE_MESSAGE);
		} else if (isSuccess) {
			itemName = Lineage.magicDoll[level + 1][Util.random(0, Lineage.magicDoll[level + 1].length - 1)];
			if (level == 4) {
				pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, 2048), true);
			} else {
				ChattingController.toChatting(pc, String.format("%d단계 마법인형 합성 성공!", level + 1), Lineage.CHATTING_MODE_MESSAGE);
			}
			if (isPity) {
				ChattingController.toChatting(pc, "\\fV[시스템] 천장 달성! 100% 확률로 합성에 성공하였습니다!", Lineage.CHATTING_MODE_MESSAGE);
			}
		} else {
			itemName = Lineage.magicDoll[level][Util.random(0, Lineage.magicDoll[level].length - 1)];
		}

		// =========================================================
		// 💡 GUI 및 DB 로그 기록 로직 시작
		// =========================================================
		final String timeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		final String charName = pc.getName();
		final String finalItemName = itemName;
		final String finalLevelName = composeLevelName;
		
		int currentStackForLog = (level == 2) ? pc.dollCount4 : (level == 3) ? pc.dollCount5 : (level == 4) ? pc.dollCountDragon : 0;

		if (isSuccess) {
			// [성공 시 스택 초기화]
			if (level == 2) { pc.dollCount4 = 0; pc.dollBonus4 = 0.0; }
			else if (level == 3) { pc.dollCount5 = 0; pc.dollBonus5 = 0.0; }
			else if (level == 4) { pc.dollCountDragon = 0; pc.dollBonusDragon = 0.0; }
			
			// 1. GUI 로그 출력 (대성공 / 천장 성공 / 일반 성공 분기)
			final String guiLogMsg;
			if (isPerfect) {
				guiLogMsg = String.format("[%s] [인형 대성공]\t [캐릭터: %s]\t [%s] 획득: [%s] (스택 리셋)", timeString, charName, finalLevelName, finalItemName);
			} else if (isPity) {
				guiLogMsg = String.format("[%s] [인형 천장성공]\t [캐릭터: %s]\t [%s] 획득: [%s] (확정 천장: %d)", timeString, charName, finalLevelName, finalItemName, currentStackForLog);
			} else {
				guiLogMsg = String.format("[%s] [인형 성공]\t [캐릭터: %s]\t [%s] 획득: [%s] (스택 리셋)", timeString, charName, finalLevelName, finalItemName);
			}
			
			lineage.gui.GuiMain.display.asyncExec(new Runnable() {
				public void run() { lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(guiLogMsg); }
			});
			
			// 2. DB 로그 기록
			String dbLogDetail = isPerfect ? "[대성공]" : isPity ? "[천장 성공]" : "[일반 성공]";
//			lineage.database.EnchantLogDatabase.insert(charName, finalItemName, "성공", finalLevelName + " " + dbLogDetail);

		} else {
			// [실패 시 스택 증가 및 로직 처리]
			String pityMsg = "";
			if (level == 2) {
				pc.dollCount4++;
				if (Util.random(1, 100) <= 10) {
					pc.dollBonus4 += Lineage_Balance.doll_bonus_val_4;
					ChattingController.toChatting(pc, "운이 따르기 시작합니다! (4단계 인형 성공 확률 상승)", Lineage.CHATTING_MODE_MESSAGE);
				}
				if (maxPity > 0) pityMsg = " (누적: " + pc.dollCount4 + " / " + maxPity + ")";
			} else if (level == 3) {
				pc.dollCount5++;
				if (Util.random(1, 100) <= 10) {
					pc.dollBonus5 += Lineage_Balance.doll_bonus_val_5;
					ChattingController.toChatting(pc, "운이 따르기 시작합니다! (5단계 인형 성공 확률 상승)", Lineage.CHATTING_MODE_MESSAGE);
				}
				if (maxPity > 0) pityMsg = " (누적: " + pc.dollCount5 + " / " + maxPity + ")";
			} else if (level == 4) {
				pc.dollCountDragon++;
				if (Util.random(1, 100) <= 10) {
					pc.dollBonusDragon += Lineage_Balance.doll_bonus_val_dragon;
					ChattingController.toChatting(pc, "운이 따르기 시작합니다! (용인형 성공 확률 상승)", Lineage.CHATTING_MODE_MESSAGE);
				}
				if (maxPity > 0) pityMsg = " (누적: " + pc.dollCountDragon + " / " + maxPity + ")";
			}
			
			if (level >= 2) {
				ChattingController.toChatting(pc, "마법인형 합성에 실패하였습니다." + pityMsg, Lineage.CHATTING_MODE_MESSAGE);
			} else {
				ChattingController.toChatting(pc, "마법인형 합성에 실패하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

			// 방금 올라간 최신 스택을 가져옴
			int updatedStack = (level == 2) ? pc.dollCount4 : (level == 3) ? pc.dollCount5 : (level == 4) ? pc.dollCountDragon : 0;
			
			// 1. GUI 로그 출력
			final String guiLogMsg = String.format("[%s] [인형 실패]\t [캐릭터: %s]\t [%s] 획득: [%s] [스택: %d / %d]", timeString, charName, finalLevelName, finalItemName, updatedStack, maxPity);
			lineage.gui.GuiMain.display.asyncExec(new Runnable() {
				public void run() { lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(guiLogMsg); }
			});
			
			// 2. DB 로그 기록
//			lineage.database.EnchantLogDatabase.insert(charName, finalItemName, "실패", finalLevelName + " 천장스택: " + updatedStack);
		}

		// 아이템 지급 및 DB 저장 처리
		if (itemName != null) {
			Item item = ItemDatabase.find(itemName);
			if (item != null) {
				ItemInstance temp = ItemDatabase.newInstance(item);
				temp.setObjectId(ServerDatabase.nextItemObjId());
				temp.setBless(1);
				temp.setEnLevel(0);
				temp.setDefinite(true);
				pc.getInventory().append(temp, true);

				for (int i = 0; i < count; i++)
					pc.getInventory().count(magicDollList.get(i), magicDollList.get(i).getCount() - 1, true);

				ItemDropMessageDatabase.sendMessageMagicDoll(pc, itemName);
				ChattingController.toChatting(pc, String.format("[마법인형 합성] %s 획득!", temp.toStringDB()), Lineage.CHATTING_MODE_MESSAGE);
			}
		}
		
		pc.toCharacterSave2();
	}
}