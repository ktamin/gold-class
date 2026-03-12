package lineage.network.packet.client;

import lineage.database.BackgroundDatabase;
import lineage.database.NpcSpawnlistDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.PcMarketController;
import lineage.world.controller.무인혈맹컨트롤러;
import lineage.world.object.object;
import lineage.world.object.instance.BoardInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.ShopInstance;
import lineage.world.object.item.all_night.인첸트복구주문서;
import lineage.world.object.item.all_night.클래스변경권;
import lineage.world.object.npc.무인혈맹;

public class C_ObjectTalkAction extends ClientBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length) {
		if (bp == null)
			bp = new C_ObjectTalkAction(data, length);
		else
			((C_ObjectTalkAction) bp).clone(data, length);
		return bp;
	}

	public C_ObjectTalkAction(byte[] data, int length) {
		clone(data, length);
	}

	@Override
	public BasePacket init(PcInstance pc) {
		// 버그 방지.
		if (pc == null || pc.isWorldDelete() || !isRead(4))
			return this;

		int objId = readD();
		String action = readS();
		String type = readS();
		object o = pc.findInsideList(objId);
		// f1상점
		pc.setTempShop(null);
		// f1상점

		if (action.contains("autohunt-")) {
			pc.toTalk(pc, action, type, this);
			return this;
		}

		if (action != null && action.startsWith("autosell-")) {
			pc.toTalk(pc, action, type, this);
			return this;
		}

		// by 야도란 사냥터이동 && 보스 ses950317
		if (action.contains("yadolantel-")) {
			NpcSpawnlistDatabase.yadolantel.toTalk(pc, action, type, this);
			return this;
		}

		if (action.contains("yadolantel3-")) {
			NpcSpawnlistDatabase.yadolantel3.toTalk(pc, action, type, this);
			return this;
		}
		
		if (action.contains("yadolantel1-")) {
			NpcSpawnlistDatabase.yadolantel1.toTalk(pc, action, type, this);
			return this;
		}

		if (action.contains("yadolantelbs1-")) {
			NpcSpawnlistDatabase.yadolantelbs1.toTalk(pc, action, type, this);
			return this;
		}

		if (action.contains("rankcheck-")) {
			BoardInstance b = BackgroundDatabase.getRankBoard();
			b.toClick(pc, this);
			return this;
		}

		if (action.contains("bossList2")) {
			if (pc.isDead()) {
				ChattingController.toChatting(pc, "죽은 상태에선 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return this;
			} else if (pc.isLock()) {
				ChattingController.toChatting(pc, "기절 상태에선 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return this;
			}

			if (pc.isAutoAttack) {
				pc.isAutoAttack = false;
				pc.getClientSetting().toggleAttackContinue().update();

				// 원본
				// pc.resetAutoAttack();
			} else {
				pc.isAutoAttack = true;
				pc.getClientSetting().toggleAttackContinue().update();
			}

			ChattingController.toChatting(pc, String.format("[자동칼질: %s]", pc.isAutoAttack ? "활성화" : "비활성화"),
					Lineage.CHATTING_MODE_MESSAGE);
			return this;
		}
		if (action.contains("bossList3")) {
			NpcSpawnlistDatabase.autoPotion.toTalk(pc, action, type, this);
			return this;
		}

		if (action.equalsIgnoreCase("bossList4")) {
			object shop = NpcSpawnlistDatabase.bossList4;
			if (shop != null) {
				shop.toTalk(pc, null);
				pc.setTempShop(shop);
			}
			return this;
		}

		if (action.equalsIgnoreCase("bossList5")) {
			object shop = NpcSpawnlistDatabase.bossList5;
			if (shop != null) {
				shop.toTalk(pc, null);
				pc.setTempShop(shop);
			}
			return this;
		}

		if (action.equalsIgnoreCase("bossList6")) {
			object shop = NpcSpawnlistDatabase.bossList6;
			if (shop != null) {
				shop.toTalk(pc, null);
				pc.setTempShop(shop);
			}
			return this;
		}

		if (action.contains("bossList-")) {
			NpcSpawnlistDatabase.bosstime.toTalk(pc, action, type, this);
			return this;
		}

		if (action.contains("yadolantelboss-")) {
			NpcSpawnlistDatabase.yadolantelboss.toTalk(pc, action, type, this);
			return this;
		}
		if (objId == NpcSpawnlistDatabase.weaponchange.getObjectId()) {
			NpcSpawnlistDatabase.weaponchange.toTalk(pc, action, type, this);
			return this;
		}
		if (objId == NpcSpawnlistDatabase.armornchange.getObjectId()) {
			NpcSpawnlistDatabase.armornchange.toTalk(pc, action, type, this);
			return this;
		}
		// 시세 검색
		if (objId == PcMarketController.marketPriceNPC.getObjectId()) {
			PcMarketController.marketPriceNPC.toTalk(pc, action, type, this);
			return this;
		}

		// 장비 스왑
		if (objId == NpcSpawnlistDatabase.itemSwap.getObjectId()) {
			NpcSpawnlistDatabase.itemSwap.toTalk(pc, action, type, this);
			return this;
		}

		// 자동 물약
		if (objId == NpcSpawnlistDatabase.autoPotion.getObjectId()) {
			NpcSpawnlistDatabase.autoPotion.toTalk(pc, action, type, this);
			return this;
		}

		무인혈맹 ci = 무인혈맹컨트롤러.find무인혈맹(objId);
		// 무인혈맹
		if (ci != null) {
			ci.toTalk(pc, action, type, this);
			return this;
		}

		if (o != null && (pc.getGm() > 0 || !pc.isTransparent())) {
			o.toTalk(pc, action, type, this);
			return this;
		}

		if (pc.getInventory() != null) {
			인첸트복구주문서 enchant = pc.getInventory().is인첸트복구주문서(pc, objId);
			if (enchant != null) {
				enchant.toTalk(pc, action, type, this);
				return this;
			}

			클래스변경권 classChange = pc.getInventory().is클래스변경주문서(pc, objId);
			if (classChange != null) {
				classChange.toTalk(pc, action, type, this);
				return this;
			}
		}

		return this;
	}
}
