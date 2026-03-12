package lineage.world.object.item.all_night;

import all_night.Lineage_Balance;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryBress;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class ScrollOfChangeBless extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ScrollOfChangeBless();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getInventory() != null) {
			ItemInstance item = cha.getInventory().value(cbp.readD());

			if ((item.getItem().getType1().equals("weapon") || item.getItem().getType1().equals("armor")) && !item.getItem().getType2().equalsIgnoreCase("arrow") &&
					item.getItem().getNameIdNumber() != 1206 && item.getItem().getNameIdNumber() != 1207) {
				if (item.getBless() != 0 && item.getBless() != -128) {
					
					if(getItem().getSmallDmg() == 1){
						
						if (Math.random() < Lineage_Balance.bless_change_probability1) {
							if (item.isEquipped()) {
								item.setEquipped(false);
								item.toOption(cha, false);
								
								item.setBless(0);
								
								item.setEquipped(true);
								item.toOption(cha, true);
							} else {
								item.setBless(0);
							}
							
							cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
							cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
							cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 9009), true);
							ChattingController.toChatting(cha, "축복 부여에 성공하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						} else {
							ChattingController.toChatting(cha, "축복 부여에 실패하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						}
						
					}
					if(getItem().getSmallDmg() == 2){
						
						if (Math.random() < Lineage_Balance.bless_change_probability2) {
							if (item.isEquipped()) {
								item.setEquipped(false);
								item.toOption(cha, false);
								
								item.setBless(0);
								
								item.setEquipped(true);
								item.toOption(cha, true);
							} else {
								item.setBless(0);
							}
							
							cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
							cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
							cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 9009), true);
							ChattingController.toChatting(cha, "축복 부여에 성공하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						} else {
							ChattingController.toChatting(cha, "축복 부여에 실패하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						}
						
					}
					if(getItem().getSmallDmg() == 3){
						
						if (Math.random() < Lineage_Balance.bless_change_probability3) {
							if (item.isEquipped()) {
								item.setEquipped(false);
								item.toOption(cha, false);
								
								item.setBless(0);
								
								item.setEquipped(true);
								item.toOption(cha, true);
							} else {
								item.setBless(0);
							}
							
							cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
							cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
							cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 9009), true);
							ChattingController.toChatting(cha, "축복 부여에 성공하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						} else {
							ChattingController.toChatting(cha, "축복 부여에 실패하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						}
						
					}
					if(getItem().getSmallDmg() == 4){
						
						if (Math.random() < Lineage_Balance.bless_change_probability4) {
							if (item.isEquipped()) {
								item.setEquipped(false);
								item.toOption(cha, false);
								
								item.setBless(0);
								
								item.setEquipped(true);
								item.toOption(cha, true);
							} else {
								item.setBless(0);
							}
							
							cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
							cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
							cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 9009), true);
							ChattingController.toChatting(cha, "축복 부여에 성공하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						} else {
							ChattingController.toChatting(cha, "축복 부여에 실패하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						}
						
					}
					if(getItem().getSmallDmg() == 5){
						
						if (Math.random() < Lineage_Balance.bless_change_probability5) {
							if (item.isEquipped()) {
								item.setEquipped(false);
								item.toOption(cha, false);
								
								item.setBless(0);
								
								item.setEquipped(true);
								item.toOption(cha, true);
							} else {
								item.setBless(0);
							}
							
							cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
							cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
							cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 9009), true);
							ChattingController.toChatting(cha, "축복 부여에 성공하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						} else {
							ChattingController.toChatting(cha, "축복 부여에 실패하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						}
						
					}
					if(getItem().getSmallDmg() == 6){
						
						if (Math.random() < Lineage_Balance.bless_change_probability6) {
							if (item.isEquipped()) {
								item.setEquipped(false);
								item.toOption(cha, false);
								
								item.setBless(0);
								
								item.setEquipped(true);
								item.toOption(cha, true);
							} else {
								item.setBless(0);
							}
							
							cha.toSender(S_InventoryBress.clone(BasePacketPooling.getPool(S_InventoryBress.class), item));
							cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
							cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 9009), true);
							ChattingController.toChatting(cha, "축복 부여에 성공하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						} else {
							ChattingController.toChatting(cha, "축복 부여에 실패하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
						}
						
					}
					
					cha.getInventory().count(this, getCount() - 1, true);
					
				} else {
					ChattingController.toChatting(cha, "이미 축복이 부여된 아이템입니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			} else {
				ChattingController.toChatting(cha, "무기, 방어구, 장신구에 사용가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
	}
}
