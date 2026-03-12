package lineage.world.object.instance;

import java.sql.Connection;

import lineage.bean.lineage.Inventory;
import lineage.database.PolyDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;

public class ItemArmorInstance extends ItemIllusionInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ItemArmorInstance();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (!equipped)
			ChattingController.toChatting(cha, String.format("\\fY[%s] 안전인첸트: %d", getItem().getName(), getItem().getSafeEnchant()), Lineage.CHATTING_MODE_MESSAGE);
		if (isLvCheck(cha)) {
			if (isClassCheck(cha)) {
				Inventory inv = cha.getInventory();
				if (inv != null && isEquipped(cha, inv)) {
					if (PolyDatabase.toEquipped(cha, this) || equipped) {
						if (equipped) {
							if (bless == 2) {
								// \f1그렇게 할 수 없습니다. 저주 받은 것 같습니다.
								cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 150));
								return;
							}
							setEquipped(false);                          
						} else {
							if (item.getType2().equalsIgnoreCase("shield")) {
								ItemInstance weapon = inv.getSlot(Lineage.SLOT_WEAPON);
								
								if (weapon != null && weapon.getItem().isTohand()) {
									// \f1두손 무기를 무장하고 방패를 착용할 수 없습니다.
									cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 129));
									return;
								}
							}

							setEquipped(true);
						}

						toSetoption(cha, true);
						toEquipped(cha, inv);
						toOption(cha, true);
						toBuffCheck(cha);
					} else {
						ChattingController.toChatting(cha, "착용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
					}
				} else {
					// \f1이미 뭔가를 착용하고 있습니다.
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 124));
				}
			} else {
				if (equipped) {
					setEquipped(false);			
					toSetoption(cha, true);
					toEquipped(cha, cha.getInventory());
					toOption(cha, true);
					toBuffCheck(cha);
				} else {
					// \f1당신의 클래스는 이 아이템을 사용할 수 없습니다.
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 264));
				}
			}
		}
	}

	/**
	 * 방어구 착용 및 해제 처리 메서드.
	 */
	@Override
	public void toEquipped(Character cha, Inventory inv) {
		if (equipped) {
			cha.setAc(cha.getAc() + getTotalAc());
		} else {
			cha.setAc(cha.getAc() - getTotalAc());
		}

		switch (item.getSlot()) {
		case Lineage.SLOT_RING_LEFT:
		case Lineage.SLOT_RING_RIGHT:
			if (equipped) {
				if (inv.getSlot(Lineage.SLOT_RING_RIGHT) == null) {
					inv.setSlot(Lineage.SLOT_RING_RIGHT, this);
				} else {
					inv.setSlot(Lineage.SLOT_RING_LEFT, this);
				}
			} else {
				if (inv.getSlot(Lineage.SLOT_RING_RIGHT) != null && inv.getSlot(Lineage.SLOT_RING_RIGHT).getObjectId() == getObjectId()) {
					inv.setSlot(Lineage.SLOT_RING_RIGHT, null);
				} else if (inv.getSlot(Lineage.SLOT_RING_LEFT) != null && inv.getSlot(Lineage.SLOT_RING_LEFT).getObjectId() == getObjectId()) {
					inv.setSlot(Lineage.SLOT_RING_LEFT, null);
				} else {
					inv.setSlot(Lineage.SLOT_RING_RIGHT, null);
					inv.setSlot(Lineage.SLOT_RING_LEFT, null);
				}
			}
			break;
		default:
			inv.setSlot(item.getSlot(), equipped ? this : null);
			break;
		}
		
		if (!cha.isBlessArmor() && inv.getSlot(Lineage.SLOT_HELM) != null && inv.getSlot(Lineage.SLOT_SHIRT) != null && inv.getSlot(Lineage.SLOT_ARMOR) != null
			&& inv.getSlot(Lineage.SLOT_CLOAK) != null && inv.getSlot(Lineage.SLOT_GLOVE) != null && inv.getSlot(Lineage.SLOT_BOOTS) != null) {
			if ((inv.getSlot(Lineage.SLOT_HELM).getBless() == 0 || inv.getSlot(Lineage.SLOT_HELM).getBless() == -128)
				&& (inv.getSlot(Lineage.SLOT_HELM).getBless() == 0 || inv.getSlot(Lineage.SLOT_HELM).getBless() == -128)
				&& (inv.getSlot(Lineage.SLOT_SHIRT).getBless() == 0 || inv.getSlot(Lineage.SLOT_SHIRT).getBless() == -128)
				&& (inv.getSlot(Lineage.SLOT_ARMOR).getBless() == 0 || inv.getSlot(Lineage.SLOT_ARMOR).getBless() == -128)
				&& (inv.getSlot(Lineage.SLOT_CLOAK).getBless() == 0 || inv.getSlot(Lineage.SLOT_CLOAK).getBless() == -128)
				&& (inv.getSlot(Lineage.SLOT_GLOVE).getBless() == 0 || inv.getSlot(Lineage.SLOT_GLOVE).getBless() == -128)
				&& (inv.getSlot(Lineage.SLOT_BOOTS).getBless() == 0 || inv.getSlot(Lineage.SLOT_BOOTS).getBless() == -128)) {
				ChattingController.toChatting(cha, "축복받은 방어구 세트 효과 적용: AC-5", Lineage.CHATTING_MODE_MESSAGE);
				cha.setDynamicAc(cha.getDynamicAc() + 5);
				cha.setBlessArmor(true);
			}		
		}
		
		if (!cha.isBlessAcc() && inv.getSlot(Lineage.SLOT_NECKLACE) != null && inv.getSlot(Lineage.SLOT_RING_LEFT) != null 
			&& inv.getSlot(Lineage.SLOT_RING_RIGHT) != null && inv.getSlot(Lineage.SLOT_BELT) != null) {
				if ((inv.getSlot(Lineage.SLOT_NECKLACE).getBless() == 0 || inv.getSlot(Lineage.SLOT_NECKLACE).getBless() == -128)
					&& (inv.getSlot(Lineage.SLOT_RING_LEFT).getBless() == 0 || inv.getSlot(Lineage.SLOT_RING_LEFT).getBless() == -128)
					&& (inv.getSlot(Lineage.SLOT_RING_RIGHT).getBless() == 0 || inv.getSlot(Lineage.SLOT_RING_RIGHT).getBless() == -128)
					&& (inv.getSlot(Lineage.SLOT_BELT).getBless() == 0 || inv.getSlot(Lineage.SLOT_BELT).getBless() == -128)) {
					ChattingController.toChatting(cha, "축복받은 장신구 세트 효과 적용: 대미지 감소+2, mr+5", Lineage.CHATTING_MODE_MESSAGE);
					cha.setDynamicReduction(cha.getDynamicReduction() + 2);
					cha.setDynamicMr(cha.getDynamicMr() + 5);
					
					cha.setBlessAcc(true);
				}		
			}
		
		if (cha.isBlessArmor() && (inv.getSlot(Lineage.SLOT_HELM) == null || inv.getSlot(Lineage.SLOT_SHIRT) == null || inv.getSlot(Lineage.SLOT_ARMOR) == null
			|| inv.getSlot(Lineage.SLOT_CLOAK) == null || inv.getSlot(Lineage.SLOT_GLOVE) == null || inv.getSlot(Lineage.SLOT_BOOTS) == null)) {
			ChattingController.toChatting(cha, "축복받은 방어구 세트 효과 해제", Lineage.CHATTING_MODE_MESSAGE);
			cha.setDynamicAc(cha.getDynamicAc() - 5);
			cha.setBlessArmor(false);
		}
		
		if (cha.isBlessAcc() && (inv.getSlot(Lineage.SLOT_NECKLACE) == null || inv.getSlot(Lineage.SLOT_RING_LEFT) == null 
			|| inv.getSlot(Lineage.SLOT_RING_RIGHT) == null || inv.getSlot(Lineage.SLOT_BELT) == null)) {
				ChattingController.toChatting(cha, "축복받은 장신구 세트 효과 해제", Lineage.CHATTING_MODE_MESSAGE);
				cha.setDynamicReduction(cha.getDynamicReduction() - 2);
				cha.setDynamicMr(cha.getDynamicMr() - 5);
				cha.setBlessAcc(false);
			}
		 if ((!cha.armorEnchant8) && (!cha.armorEnchant9) && (inv.getSlot(0) != null) && (inv.getSlot(3) != null) && (inv.getSlot(4) != null) && 
				 /* 175 */       (inv.getSlot(5) != null) && (inv.getSlot(9) != null) && (inv.getSlot(12) != null) && 
				 /* 176 */       (inv.getSlot(0).getEnLevel() >= 8) && 
				 /* 177 */       (inv.getSlot(0).getEnLevel() >= 8) && 
				 /* 178 */       (inv.getSlot(3).getEnLevel() >= 8) && 
				 /* 179 */       (inv.getSlot(4).getEnLevel() >= 8) && 
				 /* 180 */       (inv.getSlot(5).getEnLevel() >= 8) && 
				 /* 181 */       (inv.getSlot(9).getEnLevel() >= 8) && 
				 /* 182 */       (inv.getSlot(12).getEnLevel() >= 8)) {
				 /* 183 */       if ((inv.getSlot(0).getEnLevel() >= 8) && 
				 /* 184 */         (inv.getSlot(0).getEnLevel() >= 9) && 
				 /* 185 */         (inv.getSlot(3).getEnLevel() >= 9) && 
				 /* 186 */         (inv.getSlot(4).getEnLevel() >= 9) && 
				 /* 187 */         (inv.getSlot(5).getEnLevel() >= 9) && 
				 /* 188 */         (inv.getSlot(9).getEnLevel() >= 9) && 
				 /* 189 */         (inv.getSlot(12).getEnLevel() >= 9)) {
				 /* 190 */         ChattingController.toChatting(cha, "+9 방어구 세트 효과 적용: 대미지 감소+5", 20);
				 /* 191 */         cha.setDynamicReduction(cha.getDynamicReduction() + 5);
				 /* 192 */         cha.armorEnchant8 = false;
				 /* 193 */         cha.armorEnchant9 = true;
				 /*     */       } else {
				 /* 195 */         ChattingController.toChatting(cha, "+8 방어구 세트 효과 적용: 대미지 감소+3", 20);
				 /* 196 */         cha.setDynamicReduction(cha.getDynamicReduction() + 3);
				 /* 197 */         cha.armorEnchant8 = true;
				 /* 198 */         cha.armorEnchant9 = false;
				 /*     */       }
				 /*     */ 
				 /*     */     }
				 /*     */ 
				 /* 203 */     if ((cha.armorEnchant8) && ((inv.getSlot(0) == null) || (inv.getSlot(3) == null) || (inv.getSlot(4) == null) || 
				 /* 204 */       (inv.getSlot(5) == null) || (inv.getSlot(9) == null) || (inv.getSlot(12) == null))) {
				 /* 205 */       ChattingController.toChatting(cha, "+8 방어구 세트 효과 해제", 20);
				 /* 206 */       cha.setDynamicReduction(cha.getDynamicReduction() - 3);
				 /* 207 */       cha.armorEnchant8 = false;
				 /*     */     }
				 /*     */ 
				 /* 210 */     if ((cha.armorEnchant9) && ((inv.getSlot(0) == null) || (inv.getSlot(3) == null) || (inv.getSlot(4) == null) || 
				 /* 211 */       (inv.getSlot(5) == null) || (inv.getSlot(9) == null) || (inv.getSlot(12) == null))) {
				 /* 212 */       ChattingController.toChatting(cha, "+9 방어구 세트 효과 해제", 20);
				 /* 213 */       cha.setDynamicReduction(cha.getDynamicReduction() - 5 );
				 /* 214 */       cha.armorEnchant9 = false;
				 /*     */     }
		if (getBless() == 2 && equipped) {
			//\f1%0%s 손에 달라 붙었습니다.
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 149, getName()));
		}

		cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
	}

	/**
	 * 방어구 착용순서 체크하기.
	 */
	private boolean isEquipped(Character cha, Inventory inv) {
		// 착용해제 하려는가?
		if (equipped) {
			if(!Lineage.item_equipped_type) {
				// 갑옷해제시 망토 확인
				if (item.getSlot() == Lineage.SLOT_ARMOR && inv.getSlot(Lineage.SLOT_CLOAK) != null) {
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 127));
					return false;
				}
				// 티셔츠해제시 망토 확인
				if (item.getSlot() == Lineage.SLOT_SHIRT && inv.getSlot(Lineage.SLOT_CLOAK) != null) {
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 127));
					return false;
				}
				// 티셔츠해제시 아머 확인
				if (item.getSlot() == Lineage.SLOT_SHIRT && inv.getSlot(Lineage.SLOT_ARMOR) != null) {
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 127));
					return false;
				}
			}
			// 착용 하려는가?
		} else {
			if(!Lineage.item_equipped_type) {
				// 갑옷 착용시 망토 확인
				if (item.getSlot() == Lineage.SLOT_ARMOR && inv.getSlot(Lineage.SLOT_CLOAK) != null) {
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 126, "$226", "$225"));
					return false;
				}
				// 티셔츠 착용시 갑옷 확인
				if (item.getSlot() == Lineage.SLOT_SHIRT && inv.getSlot(Lineage.SLOT_ARMOR) != null) {
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 126, "$168", "$226"));
					return false;
				}
				// 티셔츠 착용시 망토 확인
				if (item.getSlot() == Lineage.SLOT_SHIRT && inv.getSlot(Lineage.SLOT_CLOAK) != null) {
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 126, "$168", "$225"));
					return false;
				}
			}
			// 방패 착용시 양손무기 확인
			if (item.getSlot() == Lineage.SLOT_SHIELD && inv.getSlot(Lineage.SLOT_WEAPON) != null && inv.getSlot(Lineage.SLOT_WEAPON).getItem().isTohand()) {
				if(!Lineage.item_equipped_type) {
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 129));
					return false;
				} else {
					inv.getSlot(Lineage.SLOT_WEAPON).toClick(cha, null);
				}
			}
			// 방패 착용시 가더 해제.
			if (item.getSlot() == Lineage.SLOT_SHIELD && inv.getSlot(Lineage.SLOT_GUARDER) != null)
				inv.getSlot(Lineage.SLOT_GUARDER).toClick(cha, null);			
			// 가더 착용시 방패 해제.
			if (item.getSlot() == Lineage.SLOT_GUARDER && inv.getSlot(Lineage.SLOT_SHIELD) != null)
				inv.getSlot(Lineage.SLOT_SHIELD).toClick(cha, null);
			
			switch (item.getSlot()) {
			case Lineage.SLOT_RING_LEFT:
			case Lineage.SLOT_RING_RIGHT:
				if (inv.getSlot(Lineage.SLOT_RING_LEFT) != null && inv.getSlot(Lineage.SLOT_RING_RIGHT) != null) {
					if (Lineage.item_equipped_type && inv.getSlot(item.getSlot()).getBless() != 2) {
						do {
							inv.getSlot(item.getSlot()).toClick(cha, null);
						} while (inv.getSlot(item.getSlot()) != null);
					} else {
						return false;
					}
				}
				return true;
			default:
				if (inv.getSlot(item.getSlot()) != null) {
					if (Lineage.item_equipped_type && inv.getSlot(item.getSlot()).getBless() != 2) {
						do {
							inv.getSlot(item.getSlot()).toClick(cha, null);
						} while (inv.getSlot(item.getSlot()) != null);
					} else {
						return false;
					}
				}
				return true;
			}
		}
		return true;
	}

	/**
	 * 방어구 상태에따라 ac전체값 계산하여 리턴.
	 */
	private int getTotalAc() {
		int enLevel = getEnLevel();
	
		int ac = getItem().getAc() + enLevel + getDynamicAc();
		
		// 장신구는 따로 처리
	
		
		return ac < 0 ? 0 : ac;
	}

	/**
	 * 리니지 월드에 접속했을때 착용중인 아이템 처리를 위해 사용되는 메서드.
	 */
	@Override
	public void toWorldJoin(Connection con, PcInstance pc) {
		super.toWorldJoin(con, pc);
		if (equipped) {
			toSetoption(pc, false);
			toEquipped(pc, pc.getInventory());
			toOption(pc, false);
		}
	}

	/**
	 * 인첸트 활성화 됫을때 아이템의 뒷처리를 처리하도록 요청하는 메서드.
	 */
	@Override
	public void toEnchant(PcInstance pc, int en) {
		//
		if (en == -125 || en == -127)
			return;
		//
		if (en != 0) {
			if (equipped && getTotalAc() > 0) {
				pc.setAc(pc.getAc() + en);
				
				if ((getItem().getName().equalsIgnoreCase("수호성의 파워 글로브") || getItem().getName().equalsIgnoreCase("수호성의 활 골무")) && getEnLevel() > 4) {
					if (getItem().getName().equalsIgnoreCase("수호성의 파워 글로브"))
						pc.setDynamicAddHit(pc.getDynamicAddHit() + en);
					else
						pc.setDynamicAddHitBow(pc.getDynamicAddHitBow() + en);
				}
				
				pc.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), pc));
			}
		} else {
			Inventory inv = pc.getInventory();
			if (equipped) {
				setEquipped(false);
				toEquipped(pc, inv);
				toOption(pc, true);
				toSetoption(pc, true);
				toBuffCheck(pc);
			}
			inv.count(this, 0, true);
		}
		//
		super.toEnchant(pc, en);
	}

}
