package lineage.world.object.item.yadolan;

import lineage.bean.lineage.Buff;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 버프시간 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 버프시간();
		return item;
	}

	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha != null && getItem() != null ) {
			
			Buff buff = BuffController.find(cha);
			if (buff != null) {
			
				for (BuffInterface b : buff.getList()) {
					if (b != null && b.getTime() > 0) {
						if (b.getTime() / 3600 > 0) {
							ChattingController.toChatting(cha, String.format("%s: %d시간 %d분 %d초", b.getSkill().getName(), b.getTime() / 3600, b.getTime() % 3600 / 60, b.getTime() % 3600 % 60),
									Lineage.CHATTING_MODE_MESSAGE);
						} else if (b.getTime() % 3600 / 60 > 0) {
							ChattingController.toChatting(cha, String.format("%s: %d분 %d초", b.getSkill().getName(), b.getTime() % 3600 / 60, b.getTime() % 3600 % 60),
									Lineage.CHATTING_MODE_MESSAGE);
						} else {
							ChattingController.toChatting(cha, String.format("%s: %d초", b.getSkill().getName(), b.getTime() % 3600 % 60), Lineage.CHATTING_MODE_MESSAGE);
						}
					}
				}
				
				if (cha instanceof PcInstance) {
					PcInstance pc = (PcInstance) cha;
					
					if (pc.getInventory() != null) {
						ItemInstance weapon = pc.getInventory().getSlot(Lineage.SLOT_WEAPON);								
						if (weapon != null && weapon.getItem() != null) {
							Buff weaponBuff = BuffController.find(weapon);
							if (weaponBuff != null) {
								for (BuffInterface b : weaponBuff.getList()) {
									if (b != null && b.getTime() > 0) {
										if (b.getTime() / 3600 > 0) {
											ChattingController.toChatting(cha, String.format("[%s] %s: %d시간 %d분 %d초", weapon.getItem().getName(), b.getSkill().getName(), b.getTime() / 3600, b.getTime() % 3600 / 60, b.getTime() % 3600 % 60),
													Lineage.CHATTING_MODE_MESSAGE);
										} else if (b.getTime() % 3600 / 60 > 0) {
											ChattingController.toChatting(cha, String.format("[%s] %s: %d분 %d초", weapon.getItem().getName(), b.getSkill().getName(), b.getTime() % 3600 / 60, b.getTime() % 3600 % 60),
													Lineage.CHATTING_MODE_MESSAGE);
										} else {
											ChattingController.toChatting(cha, String.format("[%s] %s: %d초", weapon.getItem().getName(), b.getSkill().getName(), b.getTime() % 3600 % 60), Lineage.CHATTING_MODE_MESSAGE);
										}
									}
								}
							}
						}
						
						ItemInstance armor = pc.getInventory().getSlot(Lineage.SLOT_ARMOR);				
						if (armor != null && armor.getItem() != null) {
							Buff armorBuff = BuffController.find(armor);
							if (armorBuff != null) {
								for (BuffInterface b : armorBuff.getList()) {
									if (b != null && b.getTime() > 0) {
										if (b.getTime() / 3600 > 0) {
											ChattingController.toChatting(cha, String.format("[%s] %s: %d시간 %d분 %d초", armor.getItem().getName(), b.getSkill().getName(), b.getTime() / 3600, b.getTime() % 3600 / 60, b.getTime() % 3600 % 60),
													Lineage.CHATTING_MODE_MESSAGE);
										} else if (b.getTime() % 3600 / 60 > 0) {
											ChattingController.toChatting(cha, String.format("[%s] %s: %d분 %d초", armor.getItem().getName(), b.getSkill().getName(), b.getTime() % 3600 / 60, b.getTime() % 3600 % 60),
													Lineage.CHATTING_MODE_MESSAGE);
										} else {
											ChattingController.toChatting(cha, String.format("[%s] %s: %d초", armor.getItem().getName(), b.getSkill().getName(), b.getTime() % 3600 % 60), Lineage.CHATTING_MODE_MESSAGE);
										}
									}
								}
							}
						}
					}
				}

				
			}
			
		}
	}
	

}
