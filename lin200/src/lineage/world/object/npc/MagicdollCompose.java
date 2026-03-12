package lineage.world.object.npc;

import java.util.ArrayList;
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

	// 아데나 소모량 (conf에서 설정)
	public static int 인형_1단계_합성_아데나 = 0;
	public static int 인형_2단계_합성_아데나 = 0;
	public static int 인형_3단계_합성_아데나 = 0;
	public static int 인형_4단계_합성_아데나 = 0;
	public static int 인형_5단계_합성_아데나 = 0;

	public static void loadConfig(String key, String value) {
		if (key.equalsIgnoreCase("doll_aden_cost_1"))
			인형_1단계_합성_아데나 = Integer.valueOf(value);
		else if (key.equalsIgnoreCase("doll_aden_cost_2"))
			인형_2단계_합성_아데나 = Integer.valueOf(value);
		else if (key.equalsIgnoreCase("doll_aden_cost_3"))
			인형_3단계_합성_아데나 = Integer.valueOf(value);
		else if (key.equalsIgnoreCase("doll_aden_cost_4"))
			인형_4단계_합성_아데나 = Integer.valueOf(value);
		else if (key.equalsIgnoreCase("doll_aden_cost_5"))
			인형_5단계_합성_아데나 = Integer.valueOf(value);
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
		if (action.equalsIgnoreCase("1단계 합성")) {
			level = 0;
			adenaCost = 인형_1단계_합성_아데나;
		} else if (action.equalsIgnoreCase("2단계 합성")) {
			level = 1;
			adenaCost = 인형_2단계_합성_아데나;
		} else if (action.equalsIgnoreCase("3단계 합성")) {
			level = 2;
			adenaCost = 인형_3단계_합성_아데나;
		} else if (action.equalsIgnoreCase("4단계 합성")) {
			if (!Lineage.oman2) {
				ChattingController.toChatting(pc, "현재는 5단계인형을 제작 할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			level = 3;
			adenaCost = 인형_4단계_합성_아데나;
		} else if (action.equalsIgnoreCase("용 합성")) {
			if (!Lineage.oman3) {
				ChattingController.toChatting(pc, "현재는 용인형을 제작 할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			level = 4;
			count = 2;
			adenaCost = 인형_5단계_합성_아데나;
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
			normalChance = Lineage_Balance.magicDoll_class_3_probability;
		} else if (level == 3) {
			normalChance = Lineage_Balance.magicDoll_class_4_probability;
		} else if (level == 4) {
			normalChance = Lineage_Balance.magicDoll_class_5_probability;
		}

		if (level == 4) {
			if (probability < normalChance) {
				itemName = Lineage.magicDoll[level + 1][Util.random(0, Lineage.magicDoll[level + 1].length - 1)];
				pc.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), pc, 2048), true);
			} else {
				itemName = Lineage.magicDoll[level][Util.random(0, Lineage.magicDoll[level].length - 1)];
			}
		} else {
			if (probability < perfectChance) {
				itemName = Lineage.magicDoll[level + 2][Util.random(0, Lineage.magicDoll[level + 2].length - 1)];
				ChattingController.toChatting(pc, String.format("%d단계 마법인형 합성 대성공!", level + 1), Lineage.CHATTING_MODE_MESSAGE);
			} else if (probability < normalChance) {
				itemName = Lineage.magicDoll[level + 1][Util.random(0, Lineage.magicDoll[level + 1].length - 1)];
				ChattingController.toChatting(pc, String.format("%d단계 마법인형 합성 성공!", level + 1), Lineage.CHATTING_MODE_MESSAGE);
			} else {
				itemName = Lineage.magicDoll[level][Util.random(0, Lineage.magicDoll[level].length - 1)];
			}
		}

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
	}
}