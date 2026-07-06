package lineage.world.object.item;

import all_night.Lineage_Balance;
import lineage.database.EnchantLostItemDatabase;
import lineage.database.ItemDropMessageDatabase;
import lineage.gui.GuiMain;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_InventoryCount;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_Message;
import lineage.share.Common;
import lineage.share.Lineage;
import lineage.share.Log;
import lineage.share.System;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.all_night.ScrollOfMetis;
import lineage.world.object.item.all_night.ScrollOfOrimArmor;
import lineage.world.object.item.all_night.ScrollOfOrimWeapon;
import lineage.world.object.item.all_night.ScrollOfWeapon;

public class Enchant extends ItemInstance {

	protected String EnMsg[];

	public Enchant() {
		EnMsg = new String[3];
	}

	/**
	 * 인첸트 확률계산할지 여부를 리턴함.
	 */
	protected boolean isChance(ItemInstance item) {
		// 안전인첸보다 높거나, 저주일경우 인첸확률 체크.
		return bless == 2 ? item.getEnLevel() <= 0 : item.getEnLevel() >= item.getItem().getSafeEnchant();
	}

	/**
	 * 인첸트를 진행할지 여부.
	 * 
	 * @param item
	 * @return
	 */
	protected boolean isEnchant(ItemInstance item) {
		if (item == null)
			return false;
		if (item.isEquipped()) {
			ChattingController.toChatting(cha,
					String.format("착용하고 있는 장비는 인챈할 수 없습니다.", Lineage.item_enchant_accessory_max),
					Lineage.CHATTING_MODE_MESSAGE);
			return false;
		}
		if (!item.isAcc() && item instanceof ItemArmorInstance && Lineage.item_enchant_armor_max > 0
				&& item.getItem().getmaxEnchant() <= item.getEnLevel() && item.getItem().getmaxEnchant() != 0)
			return false;
		// 방어구 확인.
		if (!item.isAcc() && item instanceof ItemArmorInstance && Lineage.item_enchant_armor_max > 0
				&& Lineage.item_enchant_armor_max <= item.getEnLevel())
			return false;
		// 장신구 확인.
		if (item.isAcc() && Lineage.item_enchant_accessory_max > 0
				&& Lineage.item_enchant_accessory_max <= item.getEnLevel())
			return false;
		// 무기 확인.

		if (item instanceof ItemWeaponInstance && item.getItem().getmaxEnchant() <= item.getEnLevel()
				&& item.getItem().getmaxEnchant() != 0)
			return false;

		if (item instanceof ItemWeaponInstance && Lineage.item_enchant_weapon_max > 0
				&& Lineage.item_enchant_weapon_max <= item.getEnLevel())
			return false;
		// 봉인 확인.
		if (item.getBless() < 0)
			return false;

		return true;
	}

	/**
	 * 인첸트를 처리할 메서드.
	 */

	protected int toEnchant(Character cha, ItemInstance item, ItemInstance accScroll) {

		if (item.isEquipped()) {
			ChattingController.toChatting(cha, String.format("착용한 장비는 인챈할 수 없습니다.", Lineage.item_enchant_accessory_max),
					Lineage.CHATTING_MODE_MESSAGE);
			return -127;
		}
		// 기본 인첸 가능여부 판단.
		if (!isEnchant(item)) {
			if (item instanceof ItemWeaponInstance && item.getItem().getmaxEnchant() <= item.getEnLevel()
					&& item.getItem().getmaxEnchant() != 0)
				ChattingController.toChatting(cha,
						String.format("해당 아이템의 최고인챈은 +%d까지 입니다.", item.getItem().getmaxEnchant()),
						Lineage.CHATTING_MODE_MESSAGE);

			if (item instanceof ItemWeaponInstance && Lineage.item_enchant_weapon_max > 0
					&& Lineage.item_enchant_weapon_max <= item.getEnLevel())
				ChattingController.toChatting(cha,
						String.format("무기는 최대 +%d까지 인챈트 가능합니다.", Lineage.item_enchant_weapon_max),
						Lineage.CHATTING_MODE_MESSAGE);

			if (item instanceof ItemArmorInstance && Lineage.item_enchant_armor_max > 0
					&& Lineage.item_enchant_armor_max <= item.getEnLevel() && !item.isAcc())
				ChattingController.toChatting(cha,
						String.format("방어구는 +%d까지 인챈트 가능합니다.", Lineage.item_enchant_armor_max),
						Lineage.CHATTING_MODE_MESSAGE);

			if (item instanceof ItemArmorInstance && Lineage.item_enchant_accessory_max > 0
					&& Lineage.item_enchant_accessory_max <= item.getEnLevel() && item.isAcc())
				ChattingController.toChatting(cha,
						String.format("장신구는 +%d까지 인챈트 가능합니다.", Lineage.item_enchant_accessory_max),
						Lineage.CHATTING_MODE_MESSAGE);

			return -127;
		}

		return toEnchantNew(cha, item, accScroll);
	}

	protected int toEnchantNew(Character cha, ItemInstance item, ItemInstance accScroll) {
		boolean chance = isChance(item);
		boolean isEnchant = true;
		// boolean isEnchantTop = false;
		int rnd = 0;
		int safeEnLevel = item.getItem().getSafeEnchant();
		String item_name = item.toStringDB();
		long item_objid = item.getObjectId();

		// 메세지 설정.
		EnMsg[0] = item.toString();
		// 검게, 파랗게, 은색으로
		EnMsg[1] = bless == 2 ? "$246" : item instanceof ItemWeaponInstance ? "$245" : "$252";
		EnMsg[2] = "$247"; // 한 순간

		if (!item.isAcc()) {
			// ✅ [천장 시스템] 장인의 갑옷 마법 주문서 (전통 확률 유지형 + 실시간 DB 저장)

			if (this.getItem().getName().contains("장인의 갑옷") || this.toString().contains("ScrollOfArmor")) {
				double orimChance = 0;

				if (bless == 1) { // bless가 1(축복/장인)인 경우에만 작동

					// 1. 천장 적용 대상 구간 (+7, +8, +9 방어구 강화 시)
					if (item.getEnLevel() >= 7 && item.getEnLevel() <= 9) {
						PcInstance pc = (PcInstance) cha;
						int currentLevel = item.getEnLevel();

						// 💡 [수정] 나중에 에러가 나지 않도록 여기서 미리 카운트와 천장 최대치를 다 구해둡니다!
						int currentCount = (currentLevel == 7) ? pc.scrollArmorCount7
								: (currentLevel == 8) ? pc.scrollArmorCount8 : pc.scrollArmorCount9;
						int maxPity = (currentLevel == 7) ? Lineage_Balance.armor_enchant_7_pity_count
								: (currentLevel == 8) ? Lineage_Balance.armor_enchant_8_pity_count
										: Lineage_Balance.armor_enchant_9_pity_count;

						// [확률 계산] 천장 횟수 도달 시 100%(1.0) 성공, 미도달 시 안전인챈트별 기존 전통 확률 적용
						if (currentLevel == 7) {
							orimChance = (currentCount >= maxPity) ? 1.0
									: (item.getItem().getSafeEnchant() == 0)
											? Lineage_Balance.orim_armor_0_7_probability
											: (item.getItem().getSafeEnchant() == 4)
													? Lineage_Balance.orim_armor_4_7_probability
													: Lineage_Balance.orim_armor_7_probability;
						} else if (currentLevel == 8) {
							orimChance = (currentCount >= maxPity) ? 1.0
									: (item.getItem().getSafeEnchant() == 0)
											? Lineage_Balance.orim_armor_0_8_probability
											: (item.getItem().getSafeEnchant() == 4)
													? Lineage_Balance.orim_armor_4_8_probability
													: Lineage_Balance.orim_armor_8_probability;
						} else if (currentLevel == 9) {
							orimChance = (currentCount >= maxPity) ? 1.0
									: (item.getItem().getSafeEnchant() == 0)
											? Lineage_Balance.orim_armor_0_9_probability
											: (item.getItem().getSafeEnchant() == 4)
													? Lineage_Balance.orim_armor_4_9_probability
													: Lineage_Balance.orim_armor_9_probability;
						}

						// 확률 주사위 굴리기
						isEnchant = Math.random() < orimChance;

						// 💡 [추가] 인챈트 로그에 띄울 현재 시간 가져오기
						String timeString = Util.getLocaleString(System.currentTimeMillis(), true);

						if (isEnchant) {
							// [강화 성공] 해당 레벨의 천장 스택 초기화
							if (currentLevel == 7)
								pc.scrollArmorCount7 = 0;
							else if (currentLevel == 8)
								pc.scrollArmorCount8 = 0;
							else if (currentLevel == 9)
								pc.scrollArmorCount9 = 0;
							rnd = 1;

							// ✅ [수정] 성공 로그를 GUI 인챈트 창으로 출력
							final String successLog = String.format("[%s] [장인갑옷 성공]\t [캐릭터: %s]\t [+%d -> +%d] (스택 리셋)",
									timeString, cha.getName(), currentLevel, currentLevel + 1);
							GuiMain.display.asyncExec(new Runnable() {
								public void run() {
									GuiMain.getViewComposite().getEnchantComposite().toLog(successLog);
								}
							});
						} else {
							// [강화 실패] 해당 레벨의 천장 스택 1 증가
							if (currentLevel == 7)
								pc.scrollArmorCount7 += 1;
							else if (currentLevel == 8)
								pc.scrollArmorCount8 += 1;
							else if (currentLevel == 9)
								pc.scrollArmorCount9 += 1;

							isEnchant = true; // 기본적으로 아이템 보호 처리
							if (Math.random() < Lineage_Balance.orim_scroll_armor_nothing_probability) {
								rnd = 0; // 아무 일도 일어나지 않음 (인챈트 수치 유지)
							} else {
								isEnchant = false;
								rnd = -1; // 증발 처리
							}

							// 방금 올린 스택을 다시 가져와서 출력
							int updatedCount = (currentLevel == 7) ? pc.scrollArmorCount7
									: (currentLevel == 8) ? pc.scrollArmorCount8 : pc.scrollArmorCount9;

							// ✅ [수정] 실패 및 천장 누적 로그를 GUI 인챈트 창으로 출력
							final String failLog = String.format(
									"[%s] [장인갑옷 실패]\t [캐릭터: %s]\t [+%d 구간]\t [스택: %d / %d]", timeString, cha.getName(),
									currentLevel, updatedCount, maxPity);
							GuiMain.display.asyncExec(new Runnable() {
								public void run() {
									GuiMain.getViewComposite().getEnchantComposite().toLog(failLog);
								}
							});
						}

						// 💾 성공이든 실패든 변경된 스택 수치를 즉시 캐릭터 DB에 반영
						pc.toCharacterSave2();
					}

					// 2. 천장 적용 대상이 아닌 일반 구간 (+6 이하 방어구 강화 시)
					else {
						if (item.getItem().getSafeEnchant() == 0) {
							switch (item.getEnLevel()) {
								case 0:
									orimChance = Lineage_Balance.orim_armor_0_0_probability;
									break;
								case 1:
									orimChance = Lineage_Balance.orim_armor_0_1_probability;
									break;
								default:
									orimChance = Lineage_Balance.orim_armor_0_9_probability;
									break;
							}
						} else if (item.getItem().getSafeEnchant() == 4) {
							switch (item.getEnLevel()) {
								case 4:
									orimChance = Lineage_Balance.orim_armor_4_4_probability;
									break;
								case 5:
									orimChance = Lineage_Balance.orim_armor_4_5_probability;
									break;
								case 6:
									orimChance = Lineage_Balance.orim_armor_4_6_probability;
									break;
								default:
									orimChance = Lineage_Balance.orim_armor_4_13_probability;
									break;
							}
						} else {
							switch (item.getEnLevel()) {
								case 6:
									orimChance = Lineage_Balance.orim_armor_6_probability;
									break;
								default:
									orimChance = Lineage_Balance.orim_armor_15_probability;
									break;
							}
						}

						isEnchant = Math.random() < orimChance;

						if (!isEnchant) {
							isEnchant = true;
							if (Math.random() < Lineage_Balance.orim_scroll_armor_nothing_probability) {
								rnd = 0;
							} else {
								rnd = -1;
							}
							if (item.getEnLevel() < 1 && rnd == -1)
								rnd = 0;
							if (rnd == -1)
								EnMsg[1] = "$246";
						}
					}
				}
			}
			// ==========================================
			// ✅ 장인의 갑옷 마법 주문서 단일 천장 끝
			// ==========================================
			else if (this instanceof ScrollOfOrimWeapon || this instanceof ScrollOfOrimArmor) {
				double orimChance = 0;
				rnd = 1;
				// 오림의 갑옷 마법 주문서
				if (this instanceof ScrollOfOrimWeapon) {
					// 안전 인챈까진 100%확률
					if (item.getEnLevel() >= item.getItem().getSafeEnchant()) {
						if (bless == 1) {
							if (item.getItem().getSafeEnchant() == 0) {
								switch (item.getEnLevel()) {
									case 0:
										orimChance = Lineage_Balance.orim_weapon_0_0_probability;
										break;
									case 1:
										orimChance = Lineage_Balance.orim_weapon_0_1_probability;
										break;
									case 2:
										orimChance = Lineage_Balance.orim_weapon_0_2_probability;
										break;
									case 3:
										orimChance = Lineage_Balance.orim_weapon_0_3_probability;
										break;
									case 4:
										orimChance = Lineage_Balance.orim_weapon_0_4_probability;
										break;
									case 5:
										orimChance = Lineage_Balance.orim_weapon_0_5_probability;
										break;
									case 6:
										orimChance = Lineage_Balance.orim_weapon_0_6_probability;
										break;
									case 7:
										orimChance = Lineage_Balance.orim_weapon_0_7_probability;
										break;
									case 8:
										orimChance = Lineage_Balance.orim_weapon_0_8_probability;
										break;
									default:
										orimChance = Lineage_Balance.orim_weapon_0_9_probability;
										break;
								}
							} else {
								switch (item.getEnLevel()) {
									case 6:
										orimChance = Lineage_Balance.orim_weapon_6_probability;
										break;
									case 7:
										orimChance = Lineage_Balance.orim_weapon_7_probability;
										break;
									case 8:
										orimChance = Lineage_Balance.orim_weapon_8_probability;
										break;
									case 9:
										orimChance = Lineage_Balance.orim_weapon_9_probability;
										break;
									case 10:
										orimChance = Lineage_Balance.orim_weapon_10_probability;
										break;
									case 11:
										orimChance = Lineage_Balance.orim_weapon_11_probability;
										break;
									case 12:
										orimChance = Lineage_Balance.orim_weapon_12_probability;
										break;
									case 13:
										orimChance = Lineage_Balance.orim_weapon_13_probability;
										break;
									case 14:
										orimChance = Lineage_Balance.orim_weapon_14_probability;
										break;
									default:
										orimChance = Lineage_Balance.orim_weapon_15_probability;
										break;
								}
							}

							isEnchant = Math.random() < orimChance;

							if (!isEnchant) {
								isEnchant = true;

								if (Math.random() < Lineage_Balance.orim_scroll_weapon_nothing_probability)
									rnd = 0;
								else
									rnd = -1;

								if (item.getEnLevel() < 1 && rnd == -1)
									rnd = 0;

								if (rnd == -1)
									EnMsg[1] = "$246";
							}
						} else if (bless == 0 || bless == -128) {
							if (item.getItem().getSafeEnchant() == 0) {
								switch (item.getEnLevel()) {
									case 0:
										orimChance = Lineage_Balance.orim_bless_weapon_0_0_probability;
										break;
									case 1:
										orimChance = Lineage_Balance.orim_bless_weapon_0_1_probability;
										break;
									case 2:
										orimChance = Lineage_Balance.orim_bless_weapon_0_2_probability;
										break;
									case 3:
										orimChance = Lineage_Balance.orim_bless_weapon_0_3_probability;
										break;
									case 4:
										orimChance = Lineage_Balance.orim_bless_weapon_0_4_probability;
										break;
									case 5:
										orimChance = Lineage_Balance.orim_bless_weapon_0_5_probability;
										break;
									case 6:
										orimChance = Lineage_Balance.orim_bless_weapon_0_6_probability;
										break;
									case 7:
										orimChance = Lineage_Balance.orim_bless_weapon_0_7_probability;
										break;
									case 8:
										orimChance = Lineage_Balance.orim_bless_weapon_0_8_probability;
										break;
									default:
										orimChance = Lineage_Balance.orim_bless_weapon_0_9_probability;
										break;
								}
							} else {
								switch (item.getEnLevel()) {
									case 6:
										orimChance = Lineage_Balance.orim_bless_weapon_6_probability;
										break;
									case 7:
										orimChance = Lineage_Balance.orim_bless_weapon_7_probability;
										break;
									case 8:
										orimChance = Lineage_Balance.orim_bless_weapon_8_probability;
										break;
									case 9:
										orimChance = Lineage_Balance.orim_bless_weapon_9_probability;
										break;
									case 10:
										orimChance = Lineage_Balance.orim_bless_weapon_10_probability;
										break;
									case 11:
										orimChance = Lineage_Balance.orim_bless_weapon_11_probability;
										break;
									case 12:
										orimChance = Lineage_Balance.orim_bless_weapon_12_probability;
										break;
									case 13:
										orimChance = Lineage_Balance.orim_bless_weapon_13_probability;
										break;
									case 14:
										orimChance = Lineage_Balance.orim_bless_weapon_14_probability;
										break;
									default:
										orimChance = Lineage_Balance.orim_bless_weapon_15_probability;
										break;
								}
							}

							isEnchant = Math.random() < orimChance;

							if (!isEnchant) {
								isEnchant = true;
								rnd = 0;
							}
						}
					}
				} else if (this instanceof ScrollOfOrimArmor) {
					// 안전 인챈까진 100%확률
					if (item.getEnLevel() >= item.getItem().getSafeEnchant()) {
						if (bless == 1) {
							if (item.getItem().getSafeEnchant() == 0) {
								switch (item.getEnLevel()) {
									case 0:
										orimChance = Lineage_Balance.orim_armor_0_0_probability;
										break;
									case 1:
										orimChance = Lineage_Balance.orim_armor_0_1_probability;
										break;
									case 2:
										orimChance = Lineage_Balance.orim_armor_0_2_probability;
										break;
									case 3:
										orimChance = Lineage_Balance.orim_armor_0_3_probability;
										break;
									case 4:
										orimChance = Lineage_Balance.orim_armor_0_4_probability;
										break;
									case 5:
										orimChance = Lineage_Balance.orim_armor_0_5_probability;
										break;
									case 6:
										orimChance = Lineage_Balance.orim_armor_0_6_probability;
										break;
									case 7:
										orimChance = Lineage_Balance.orim_armor_0_7_probability;
										break;
									case 8:
										orimChance = Lineage_Balance.orim_armor_0_8_probability;
										break;
									default:
										orimChance = Lineage_Balance.orim_armor_0_9_probability;
										break;
								}
							} else if (item.getItem().getSafeEnchant() == 4) {
								switch (item.getEnLevel()) {
									case 4:
										orimChance = Lineage_Balance.orim_armor_4_4_probability;
										break;
									case 5:
										orimChance = Lineage_Balance.orim_armor_4_5_probability;
										break;
									case 6:
										orimChance = Lineage_Balance.orim_armor_4_6_probability;
										break;
									case 7:
										orimChance = Lineage_Balance.orim_armor_4_7_probability;
										break;
									case 8:
										orimChance = Lineage_Balance.orim_armor_4_8_probability;
										break;
									case 9:
										orimChance = Lineage_Balance.orim_armor_4_9_probability;
										break;
									case 10:
										orimChance = Lineage_Balance.orim_armor_4_10_probability;
										break;
									case 11:
										orimChance = Lineage_Balance.orim_armor_4_11_probability;
										break;
									case 12:
										orimChance = Lineage_Balance.orim_armor_4_12_probability;
										break;
									default:
										orimChance = Lineage_Balance.orim_armor_4_13_probability;
										break;
								}
							} else {
								switch (item.getEnLevel()) {
									case 6:
										orimChance = Lineage_Balance.orim_armor_6_probability;
										break;
									case 7:
										orimChance = Lineage_Balance.orim_armor_7_probability;
										break;
									case 8:
										orimChance = Lineage_Balance.orim_armor_8_probability;
										break;
									case 9:
										orimChance = Lineage_Balance.orim_armor_9_probability;
										break;
									case 10:
										orimChance = Lineage_Balance.orim_armor_10_probability;
										break;
									case 11:
										orimChance = Lineage_Balance.orim_armor_11_probability;
										break;
									case 12:
										orimChance = Lineage_Balance.orim_armor_12_probability;
										break;
									case 13:
										orimChance = Lineage_Balance.orim_armor_13_probability;
										break;
									case 14:
										orimChance = Lineage_Balance.orim_armor_14_probability;
										break;
									default:
										orimChance = Lineage_Balance.orim_armor_15_probability;
										break;
								}
							}

							isEnchant = Math.random() < orimChance;

							if (!isEnchant) {
								isEnchant = true;

								if (Math.random() < Lineage_Balance.orim_scroll_armor_nothing_probability)
									rnd = 0;
								else
									rnd = -1;

								if (item.getEnLevel() < 1 && rnd == -1)
									rnd = 0;

								if (rnd == -1)
									EnMsg[1] = "$246";
							}
						} else if (bless == 0 || bless == -128) {
							if (item.getItem().getSafeEnchant() == 0) {
								switch (item.getEnLevel()) {
									case 0:
										orimChance = Lineage_Balance.orim_bless_armor_0_0_probability;
										break;
									case 1:
										orimChance = Lineage_Balance.orim_bless_armor_0_1_probability;
										break;
									case 2:
										orimChance = Lineage_Balance.orim_bless_armor_0_2_probability;
										break;
									case 3:
										orimChance = Lineage_Balance.orim_bless_armor_0_3_probability;
										break;
									case 4:
										orimChance = Lineage_Balance.orim_bless_armor_0_4_probability;
										break;
									case 5:
										orimChance = Lineage_Balance.orim_bless_armor_0_5_probability;
										break;
									case 6:
										orimChance = Lineage_Balance.orim_bless_armor_0_6_probability;
										break;
									case 7:
										orimChance = Lineage_Balance.orim_bless_armor_0_7_probability;
										break;
									case 8:
										orimChance = Lineage_Balance.orim_bless_armor_0_8_probability;
										break;
									default:
										orimChance = Lineage_Balance.orim_bless_armor_0_9_probability;
										break;
								}
							} else if (item.getItem().getSafeEnchant() == 4) {
								switch (item.getEnLevel()) {
									case 4:
										orimChance = Lineage_Balance.orim_bless_armor_4_4_probability;
										break;
									case 5:
										orimChance = Lineage_Balance.orim_bless_armor_4_5_probability;
										break;
									case 6:
										orimChance = Lineage_Balance.orim_bless_armor_4_6_probability;
										break;
									case 7:
										orimChance = Lineage_Balance.orim_bless_armor_4_7_probability;
										break;
									case 8:
										orimChance = Lineage_Balance.orim_bless_armor_4_8_probability;
										break;
									case 9:
										orimChance = Lineage_Balance.orim_bless_armor_4_9_probability;
										break;
									case 10:
										orimChance = Lineage_Balance.orim_bless_armor_4_10_probability;
										break;
									case 11:
										orimChance = Lineage_Balance.orim_bless_armor_4_11_probability;
										break;
									case 12:
										orimChance = Lineage_Balance.orim_bless_armor_4_12_probability;
										break;
									default:
										orimChance = Lineage_Balance.orim_bless_armor_4_13_probability;
										break;
								}
							} else {
								switch (item.getEnLevel()) {
									case 6:
										orimChance = Lineage_Balance.orim_bless_armor_6_probability;
										break;
									case 7:
										orimChance = Lineage_Balance.orim_bless_armor_7_probability;
										break;
									case 8:
										orimChance = Lineage_Balance.orim_bless_armor_8_probability;
										break;
									case 9:
										orimChance = Lineage_Balance.orim_bless_armor_9_probability;
										break;
									case 10:
										orimChance = Lineage_Balance.orim_bless_armor_10_probability;
										break;
									case 11:
										orimChance = Lineage_Balance.orim_bless_armor_11_probability;
										break;
									case 12:
										orimChance = Lineage_Balance.orim_bless_armor_12_probability;
										break;
									case 13:
										orimChance = Lineage_Balance.orim_bless_armor_13_probability;
										break;
									case 14:
										orimChance = Lineage_Balance.orim_bless_armor_14_probability;
										break;
									default:
										orimChance = Lineage_Balance.orim_bless_armor_15_probability;
										break;
								}
							}

							isEnchant = Math.random() < orimChance;

							if (!isEnchant) {
								isEnchant = true;
								rnd = 0;
							}
						}
					}
				}
			} else {
				switch (bless) {
					// 일반 주문서
					// 축 주문서
					case 0:
					case 1:
						// 인첸트값 설정
						rnd = 1;

						// 축복받은 주문서
						if (bless == 0) {
							if (item instanceof ItemWeaponInstance) {
								switch (safeEnLevel) {
									// 안전인첸트 0
									case 0:
										switch (item.getEnLevel()) {
											case 0:
												if (Math.random() < Lineage_Balance.weapon_safe_enchant0_0_3_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.weapon_safe_enchant0_0_2_probability)
													rnd = 2;
												break;
											case 1:
												if (Math.random() < Lineage_Balance.weapon_safe_enchant0_1_4_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.weapon_safe_enchant0_1_3_probability)
													rnd = 2;
												break;
											case 2:
												if (Math.random() < Lineage_Balance.weapon_safe_enchant0_2_5_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.weapon_safe_enchant0_2_4_probability)
													rnd = 2;
												break;
											case 3:
												if (Math.random() < Lineage_Balance.weapon_safe_enchant0_3_6_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.weapon_safe_enchant0_3_5_probability)
													rnd = 2;
												break;
											case 4:
												if (Math.random() < Lineage_Balance.weapon_safe_enchant0_4_7_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.weapon_safe_enchant0_4_6_probability)
													rnd = 2;
												break;
											case 5:
												if (Math.random() < Lineage_Balance.weapon_safe_enchant0_5_8_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.weapon_safe_enchant0_5_7_probability)
													rnd = 2;
												break;
											default:
												if (item.getEnLevel() >= 6) {
													if (Math.random() < Lineage_Balance.weapon_safe_enchant0_6_enchant3_probability)
														rnd = 3;
													else if (Math
															.random() < Lineage_Balance.weapon_safe_enchant0_6_enchant2_probability)
														rnd = 2;
												} else {
													rnd = 1;
												}
												break;
										}
										break;
									// 안전인첸트 6
									case 6:
										switch (item.getEnLevel()) {
											case 0:
												if (Math.random() < Lineage_Balance.weapon_safe_enchant6_0_3_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.weapon_safe_enchant6_0_2_probability)
													rnd = 2;
												break;
											case 1:
												if (Math.random() < Lineage_Balance.weapon_safe_enchant6_1_4_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.weapon_safe_enchant6_1_3_probability)
													rnd = 2;
												break;
											case 2:
												if (Math.random() < Lineage_Balance.weapon_safe_enchant6_2_5_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.weapon_safe_enchant6_2_4_probability)
													rnd = 2;
												break;
											case 3:
												if (Math.random() < Lineage_Balance.weapon_safe_enchant6_3_6_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.weapon_safe_enchant6_3_5_probability)
													rnd = 2;
												break;
											case 4:
												if (Math.random() < Lineage_Balance.weapon_safe_enchant6_4_7_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.weapon_safe_enchant6_4_6_probability)
													rnd = 2;
												break;
											case 5:
												if (Math.random() < Lineage_Balance.weapon_safe_enchant6_5_8_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.weapon_safe_enchant6_5_7_probability)
													rnd = 2;
												break;
											default:
												if (item.getEnLevel() >= 6) {
													if (Math.random() < Lineage_Balance.weapon_safe_enchant6_6_enchant3_probability)
														rnd = 3;
													else if (Math
															.random() < Lineage_Balance.weapon_safe_enchant6_6_enchant2_probability)
														rnd = 2;
												} else {
													rnd = 1;
												}
												break;
										}
										break;
								}
							} else if (item instanceof ItemArmorInstance) {
								switch (safeEnLevel) {
									// 안전인첸트 0
									case 0:
										switch (item.getEnLevel()) {
											case 0:
												if (Math.random() < Lineage_Balance.armor_safe_enchant0_0_3_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.armor_safe_enchant0_0_2_probability)
													rnd = 2;
												break;
											case 1:
												if (Math.random() < Lineage_Balance.armor_safe_enchant0_1_4_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.armor_safe_enchant0_1_3_probability)
													rnd = 2;
												break;
											case 2:
												if (Math.random() < Lineage_Balance.armor_safe_enchant0_2_5_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.armor_safe_enchant0_2_4_probability)
													rnd = 2;
												break;
											case 3:
												if (Math.random() < Lineage_Balance.armor_safe_enchant0_3_6_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.armor_safe_enchant0_3_5_probability)
													rnd = 2;
												break;
											case 4:
												if (Math.random() < Lineage_Balance.armor_safe_enchant0_4_7_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.armor_safe_enchant0_4_6_probability)
													rnd = 2;
												break;
											case 5:
												if (Math.random() < Lineage_Balance.armor_safe_enchant0_5_8_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.armor_safe_enchant0_5_7_probability)
													rnd = 2;
												break;
											default:
												if (item.getEnLevel() >= 6) {
													if (Math.random() < Lineage_Balance.armor_safe_enchant0_6_enchant3_probability)
														rnd = 3;
													else if (Math
															.random() < Lineage_Balance.armor_safe_enchant0_6_enchant2_probability)
														rnd = 2;
												} else {
													rnd = 1;
												}
												break;
										}
										break;
									// 안전인첸트 4
									case 4:
										switch (item.getEnLevel()) {
											case 0:
												if (Math.random() < Lineage_Balance.armor_safe_enchant4_0_3_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.armor_safe_enchant4_0_2_probability)
													rnd = 2;
												break;
											case 1:
												if (Math.random() < Lineage_Balance.armor_safe_enchant4_1_4_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.armor_safe_enchant4_1_3_probability)
													rnd = 2;
												break;
											case 2:
												if (Math.random() < Lineage_Balance.armor_safe_enchant4_2_5_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.armor_safe_enchant4_2_4_probability)
													rnd = 2;
												break;
											case 3:
												if (Math.random() < Lineage_Balance.armor_safe_enchant4_3_6_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.armor_safe_enchant4_3_5_probability)
													rnd = 2;
												break;
											case 4:
												if (Math.random() < Lineage_Balance.armor_safe_enchant4_4_7_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.armor_safe_enchant4_4_6_probability)
													rnd = 2;
												break;
											case 5:
												if (Math.random() < Lineage_Balance.armor_safe_enchant4_5_8_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.armor_safe_enchant4_5_7_probability)
													rnd = 2;
												break;
											default:
												if (item.getEnLevel() >= 6) {
													if (Math.random() < Lineage_Balance.armor_safe_enchant4_6_enchant3_probability)
														rnd = 3;
													else if (Math
															.random() < Lineage_Balance.armor_safe_enchant4_6_enchant2_probability)
														rnd = 2;
												} else {
													rnd = 1;
												}
												break;
										}
										break;
									// 안전인첸트 6
									case 6:
										switch (item.getEnLevel()) {
											case 0:
												if (Math.random() < Lineage_Balance.armor_safe_enchant6_0_3_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.armor_safe_enchant6_0_2_probability)
													rnd = 2;
												break;
											case 1:
												if (Math.random() < Lineage_Balance.armor_safe_enchant6_1_4_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.armor_safe_enchant6_1_3_probability)
													rnd = 2;
												break;
											case 2:
												if (Math.random() < Lineage_Balance.armor_safe_enchant6_2_5_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.armor_safe_enchant6_2_4_probability)
													rnd = 2;
												break;
											case 3:
												if (Math.random() < Lineage_Balance.armor_safe_enchant6_3_6_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.armor_safe_enchant6_3_5_probability)
													rnd = 2;
												break;
											case 4:
												if (Math.random() < Lineage_Balance.armor_safe_enchant6_4_7_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.armor_safe_enchant6_4_6_probability)
													rnd = 2;
												break;
											case 5:
												if (Math.random() < Lineage_Balance.armor_safe_enchant6_5_8_probability)
													rnd = 3;
												else if (Math
														.random() < Lineage_Balance.armor_safe_enchant6_5_7_probability)
													rnd = 2;
												break;
											default:
												if (item.getEnLevel() >= 6) {
													if (Math.random() < Lineage_Balance.armor_safe_enchant6_6_enchant3_probability)
														rnd = 3;
													else if (Math
															.random() < Lineage_Balance.armor_safe_enchant6_6_enchant2_probability)
														rnd = 2;
												} else {
													rnd = 1;
												}
												break;
										}
										break;
								}
							}

							// "잠시" 메세지 설정
							if (rnd > 1)
								EnMsg[2] = "$248";
						}

						// 인첸트 확률
						if (chance) {
							if (item instanceof ItemWeaponInstance) {
								switch (safeEnLevel) {
									// 안전인첸트 0
									case 0:
										switch (item.getEnLevel()) {
											case 0:
												isEnchant = Math
														.random() < Lineage_Balance.weapon_safe_enchant0_0_probability;
												break;
											case 1:
												isEnchant = Math
														.random() < Lineage_Balance.weapon_safe_enchant0_1_probability;
												break;
											case 2:
												isEnchant = Math
														.random() < Lineage_Balance.weapon_safe_enchant0_2_probability;
												break;
											case 3:
												isEnchant = Math
														.random() < Lineage_Balance.weapon_safe_enchant0_3_probability;
												break;
											case 4:
												isEnchant = Math
														.random() < Lineage_Balance.weapon_safe_enchant0_4_probability;
												break;
											case 5:
												isEnchant = Math
														.random() < Lineage_Balance.weapon_safe_enchant0_5_probability;
												break;
											case 6:
												isEnchant = Math
														.random() < Lineage_Balance.weapon_safe_enchant0_6_probability;
												break;
											case 7:
												isEnchant = Math
														.random() < Lineage_Balance.weapon_safe_enchant0_7_probability;
												break;
											case 8:
												isEnchant = Math
														.random() < Lineage_Balance.weapon_safe_enchant0_8_probability;
												break;
											default:
												isEnchant = Math
														.random() < Lineage_Balance.weapon_safe_enchant0_9_probability;
												break;
										}
										break;
									// 안전인첸트 6
									case 6:
										switch (item.getEnLevel()) {
											case 6:
												isEnchant = Math
														.random() < Lineage_Balance.weapon_safe_enchant6_6_probability;
												break;
											case 7:
												isEnchant = Math
														.random() < Lineage_Balance.weapon_safe_enchant6_7_probability;
												break;
											case 8:
												isEnchant = Math
														.random() < Lineage_Balance.weapon_safe_enchant6_8_probability;
												break;
											default:
												isEnchant = Math
														.random() < Lineage_Balance.weapon_safe_enchant6_9_probability;
												break;
										}
										break;
								}

								if (item.getEnLevel() >= 9 && !isEnchant) {
									if (Math.random() < Lineage_Balance.weapon_enchant_9_success_probability) {
										isEnchant = true;
										rnd = 1;
									} else if (Math.random() < Lineage_Balance.weapon_enchant_9_nothing_probability) {
										isEnchant = true;
										rnd = 0;
									}
								}
								/*
								 * // 장인의 무기 마법 주문서
								 * if (this instanceof ScrollOfWeapon) {
								 * double orimChance = 0;
								 * 
								 * if (bless == 1) {
								 * if (item.getItem().getSafeEnchant() == 0) {
								 * switch (item.getEnLevel()) {
								 * case 0:
								 * orimChance = Lineage_Balance.orim_weapon_0_0_probability;
								 * break;
								 * case 1:
								 * orimChance = Lineage_Balance.orim_weapon_0_1_probability;
								 * break;
								 * case 2:
								 * orimChance = Lineage_Balance.orim_weapon_0_2_probability;
								 * break;
								 * case 3:
								 * orimChance = Lineage_Balance.orim_weapon_0_3_probability;
								 * break;
								 * case 4:
								 * orimChance = Lineage_Balance.orim_weapon_0_4_probability;
								 * break;
								 * case 5:
								 * orimChance = Lineage_Balance.orim_weapon_0_5_probability;
								 * break;
								 * case 6:
								 * orimChance = Lineage_Balance.orim_weapon_0_6_probability;
								 * break;
								 * case 7:
								 * orimChance = Lineage_Balance.orim_weapon_0_7_probability;
								 * break;
								 * case 8:
								 * orimChance = Lineage_Balance.orim_weapon_0_8_probability;
								 * break;
								 * default:
								 * orimChance = Lineage_Balance.orim_weapon_0_9_probability;
								 * break;
								 * }
								 * } else {
								 * switch (item.getEnLevel()) {
								 * case 6:
								 * orimChance = Lineage_Balance.orim_weapon_6_probability;
								 * break;
								 * case 7:
								 * orimChance = Lineage_Balance.orim_weapon_7_probability;
								 * break;
								 * case 8:
								 * orimChance = Lineage_Balance.orim_weapon_8_probability;
								 * break;
								 * case 9:
								 * orimChance = Lineage_Balance.orim_weapon_9_probability;
								 * break;
								 * case 10:
								 * orimChance = Lineage_Balance.orim_weapon_10_probability;
								 * break;
								 * case 11:
								 * orimChance = Lineage_Balance.orim_weapon_11_probability;
								 * break;
								 * case 12:
								 * orimChance = Lineage_Balance.orim_weapon_12_probability;
								 * break;
								 * case 13:
								 * orimChance = Lineage_Balance.orim_weapon_13_probability;
								 * break;
								 * case 14:
								 * orimChance = Lineage_Balance.orim_weapon_14_probability;
								 * break;
								 * default:
								 * orimChance = Lineage_Balance.orim_weapon_15_probability;
								 * break;
								 * }
								 * }
								 * 
								 * isEnchant = Math.random() < orimChance;
								 * 
								 * if (!isEnchant) {
								 * isEnchant = true;
								 * 
								 * if (Math.random() < Lineage_Balance.orim_scroll_weapon_nothing_probability)
								 * rnd = 0;
								 * else
								 * rnd = -1;
								 * 
								 * if (item.getEnLevel() < 1 && rnd == -1)
								 * rnd = 0;
								 * 
								 * if (rnd == -1)
								 * EnMsg[1] = "$246";
								 * }
								 * 
								 * 
								 * 
								 * } else if (bless == 0 || bless == -128) {
								 */
								// 장인의 무기 마법 주문서
								if (this instanceof ScrollOfWeapon) {
									double orimChance = 0;

									if (bless == 1) {

										// ==========================================
										// ✅ [천장 시스템] +9, +10, +11 무기 전용 처리
										// ==========================================
										if (item.getItem().getSafeEnchant() != 0 && item.getEnLevel() >= 9
												&& item.getEnLevel() <= 11) {
											PcInstance pc = (PcInstance) cha;
											int currentLevel = item.getEnLevel();

											// 🟦 [+9 -> +10 구간] : 기존의 다단계(계단식) 확률 유지
											if (currentLevel == 9) {
												int currentCount = pc.scrollWeaponCount;

												if (currentCount >= Lineage_Balance.weapon_enchant_9_use_count_3) {
													orimChance = Lineage_Balance.weapon_enchant_9_scroll_probability;
												} else if (currentCount >= Lineage_Balance.weapon_enchant_9_use_count_2) {
													orimChance = Lineage_Balance.weapon_enchant_9_use_count_3_probability;
												} else if (currentCount >= Lineage_Balance.weapon_enchant_9_use_count_1) {
													orimChance = Lineage_Balance.weapon_enchant_9_use_count_2_probability;
												} else {
													orimChance = Lineage_Balance.weapon_enchant_9_use_count_1_probability;
												}

												isEnchant = Math.random() < orimChance;

												if (isEnchant) {
													pc.scrollWeaponCount = 0;
													rnd = 1;

													// ✅ 성공 로그 전송
													String timeString = Util.getLocaleString(System.currentTimeMillis(),
															true);
													final String successLog = String.format(
															"[%s] [장인무기 성공]\t [캐릭터: %s]\t [+9 -> +10] (스택 리셋)",
															timeString, cha.getName());
													GuiMain.display.asyncExec(new Runnable() {
														public void run() {
															GuiMain.getViewComposite().getEnchantComposite()
																	.toLog(successLog);
														}
													});
												} else {
													pc.scrollWeaponCount += 1;
													isEnchant = true;
													if (Math.random() < Lineage_Balance.orim_scroll_weapon_nothing_probability) {
														rnd = 0;
													} else {
														isEnchant = false;
														rnd = -1;
													}
													// ✅ 실패 누적 로그 전송
													String timeString = Util.getLocaleString(System.currentTimeMillis(),
															true);
													final String failLog = String.format(
															"[%s] [장인무기 실패]\t [캐릭터: %s]\t [+9 구간]\t [스택: %d / %d]",
															timeString, cha.getName(), pc.scrollWeaponCount,
															Lineage_Balance.weapon_enchant_9_use_count_3);
													GuiMain.display.asyncExec(new Runnable() {
														public void run() {
															GuiMain.getViewComposite().getEnchantComposite()
																	.toLog(failLog);
														}
													});
												}
											}
											// 🟩 [+10 -> +11 구간]
											else if (currentLevel == 10) {
												int currentCount = pc.scrollWeaponCount10; // 👈 +10 전용 스택 변수 확인

												// 천장 도달 시 100%, 아닐 시 기본 오림 무기 확률 적용
												orimChance = (currentCount >= Lineage_Balance.weapon_enchant_10_pity_count)
														? 1.0
														: Lineage_Balance.orim_weapon_10_probability;
												isEnchant = Math.random() < orimChance;

												String timeString = Util.getLocaleString(System.currentTimeMillis(),
														true);

												if (isEnchant) {
													pc.scrollWeaponCount10 = 0; // 👈 +10 스택 초기화
													rnd = 1;

													final String successLog = String.format(
															"[%s] [장인무기 성공]\t [캐릭터: %s]\t [+10 -> +11] (스택 리셋)",
															timeString, cha.getName());
													GuiMain.display.asyncExec(new Runnable() {
														public void run() {
															GuiMain.getViewComposite().getEnchantComposite()
																	.toLog(successLog);
														}
													});
												} else {
													pc.scrollWeaponCount10 += 1; // 👈 여기가 +9로 되어 있어서 안 올랐던 겁니다! (+10 스택
																					// 증가)
													isEnchant = true;
													if (Math.random() < Lineage_Balance.orim_scroll_weapon_nothing_probability)
														rnd = 0;
													else {
														isEnchant = false;
														rnd = -1;
													}

													// 👈 로그 출력부에도 pc.scrollWeaponCount10 과 10구간 밸런스 설정값 삽입
													final String failLog = String.format(
															"[%s] [장인무기 실패]\t [캐릭터: %s]\t [+10 구간]\t [스택: %d / %d]",
															timeString, cha.getName(), pc.scrollWeaponCount10,
															Lineage_Balance.weapon_enchant_10_pity_count);
													GuiMain.display.asyncExec(new Runnable() {
														public void run() {
															GuiMain.getViewComposite().getEnchantComposite()
																	.toLog(failLog);
														}
													});
												}
											}
											// 🟨 [+11 -> +12 구간]
											else if (currentLevel == 11) {
												int currentCount = pc.scrollWeaponCount11; // 👈 +11 전용 스택 변수

												orimChance = (currentCount >= Lineage_Balance.weapon_enchant_11_pity_count)
														? 1.0
														: Lineage_Balance.orim_weapon_11_probability;
												isEnchant = Math.random() < orimChance;

												String timeString = Util.getLocaleString(System.currentTimeMillis(),
														true);

												if (isEnchant) {
													pc.scrollWeaponCount11 = 0; // 👈 +11 스택 초기화
													rnd = 1;

													final String successLog = String.format(
															"[%s] [장인무기 성공]\t [캐릭터: %s]\t [+11 -> +12] (스택 리셋)",
															timeString, cha.getName());
													GuiMain.display.asyncExec(new Runnable() {
														public void run() {
															GuiMain.getViewComposite().getEnchantComposite()
																	.toLog(successLog);
														}
													});
												} else {
													pc.scrollWeaponCount11 += 1; // 👈 +11 스택 증가
													isEnchant = true;
													if (Math.random() < Lineage_Balance.orim_scroll_weapon_nothing_probability)
														rnd = 0;
													else {
														isEnchant = false;
														rnd = -1;
													}

													// 👈 로그 출력부에도 pc.scrollWeaponCount11 과 11구간 밸런스 설정값 삽입
													final String failLog = String.format(
															"[%s] [장인무기 실패]\t [캐릭터: %s]\t [+11 구간]\t [스택: %d / %d]",
															timeString, cha.getName(), pc.scrollWeaponCount11,
															Lineage_Balance.weapon_enchant_11_pity_count);
													GuiMain.display.asyncExec(new Runnable() {
														public void run() {
															GuiMain.getViewComposite().getEnchantComposite()
																	.toLog(failLog);
														}
													});
												}
											}

											// 💾 결과 저장 (모든 구간 공통)
											pc.toCharacterSave2();
										}
										// ==========================================
										// ✅ [기존 로직] +8 이하 또는 뼈/블랙미스릴 무기
										// ==========================================
										else {
											if (item.getItem().getSafeEnchant() == 0) {
												switch (item.getEnLevel()) {
													case 0:
														orimChance = Lineage_Balance.orim_weapon_0_0_probability;
														break;
													case 1:
														orimChance = Lineage_Balance.orim_weapon_0_1_probability;
														break;
													case 2:
														orimChance = Lineage_Balance.orim_weapon_0_2_probability;
														break;
													case 3:
														orimChance = Lineage_Balance.orim_weapon_0_3_probability;
														break;
													case 4:
														orimChance = Lineage_Balance.orim_weapon_0_4_probability;
														break;
													case 5:
														orimChance = Lineage_Balance.orim_weapon_0_5_probability;
														break;
													case 6:
														orimChance = Lineage_Balance.orim_weapon_0_6_probability;
														break;
													case 7:
														orimChance = Lineage_Balance.orim_weapon_0_7_probability;
														break;
													case 8:
														orimChance = Lineage_Balance.orim_weapon_0_8_probability;
														break;
													default:
														orimChance = Lineage_Balance.orim_weapon_0_9_probability;
														break;
												}
											} else {
												switch (item.getEnLevel()) {
													case 6:
														orimChance = Lineage_Balance.orim_weapon_6_probability;
														break;
													case 7:
														orimChance = Lineage_Balance.orim_weapon_7_probability;
														break;
													case 8:
														orimChance = Lineage_Balance.orim_weapon_8_probability;
														break;
													default:
														orimChance = Lineage_Balance.orim_weapon_15_probability;
														break;
												}
											}

											isEnchant = Math.random() < orimChance;

											if (!isEnchant) {
												isEnchant = true;

												if (Math.random() < Lineage_Balance.orim_scroll_weapon_nothing_probability)
													rnd = 0;
												else
													rnd = -1;

												if (item.getEnLevel() < 1 && rnd == -1)
													rnd = 0;

												if (rnd == -1)
													EnMsg[1] = "$246";
											}
										}
									} else if (bless == 0 || bless == -128) {
										// ----------------천장시스템 끝
										if (item.getItem().getSafeEnchant() == 0) {
											switch (item.getEnLevel()) {
												case 0:
													orimChance = Lineage_Balance.orim_bless_weapon_0_0_probability;
													break;
												case 1:
													orimChance = Lineage_Balance.orim_bless_weapon_0_1_probability;
													break;
												case 2:
													orimChance = Lineage_Balance.orim_bless_weapon_0_2_probability;
													break;
												case 3:
													orimChance = Lineage_Balance.orim_bless_weapon_0_3_probability;
													break;
												case 4:
													orimChance = Lineage_Balance.orim_bless_weapon_0_4_probability;
													break;
												case 5:
													orimChance = Lineage_Balance.orim_bless_weapon_0_5_probability;
													break;
												case 6:
													orimChance = Lineage_Balance.orim_bless_weapon_0_6_probability;
													break;
												case 7:
													orimChance = Lineage_Balance.orim_bless_weapon_0_7_probability;
													break;
												case 8:
													orimChance = Lineage_Balance.orim_bless_weapon_0_8_probability;
													break;
												default:
													orimChance = Lineage_Balance.orim_bless_weapon_0_9_probability;
													break;
											}
										} else {
											switch (item.getEnLevel()) {
												case 6:
													orimChance = Lineage_Balance.orim_bless_weapon_6_probability;
													break;
												case 7:
													orimChance = Lineage_Balance.orim_bless_weapon_7_probability;
													break;
												case 8:
													orimChance = Lineage_Balance.orim_bless_weapon_8_probability;
													break;
												case 9:
													orimChance = Lineage_Balance.orim_bless_weapon_9_probability;
													break;
												case 10:
													orimChance = Lineage_Balance.orim_bless_weapon_10_probability;
													break;
												case 11:
													orimChance = Lineage_Balance.orim_bless_weapon_11_probability;
													break;
												case 12:
													orimChance = Lineage_Balance.orim_bless_weapon_12_probability;
													break;
												case 13:
													orimChance = Lineage_Balance.orim_bless_weapon_13_probability;
													break;
												case 14:
													orimChance = Lineage_Balance.orim_bless_weapon_14_probability;
													break;
												default:
													orimChance = Lineage_Balance.orim_bless_weapon_15_probability;
													break;
											}
										}

										isEnchant = Math.random() < orimChance;

										if (!isEnchant) {
											isEnchant = true;
											rnd = 0;
										}
									}
								}
							} else if (item instanceof ItemArmorInstance) {
								switch (safeEnLevel) {
									// 안전인첸트 0
									case 0:
										switch (item.getEnLevel()) {
											case 0:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant0_0_probability;
												break;
											case 1:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant0_1_probability;
												break;
											case 2:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant0_2_probability;
												break;
											case 3:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant0_3_probability;
												break;
											case 4:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant0_4_probability;
												break;
											case 5:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant0_5_probability;
												break;
											case 6:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant0_6_probability;
												break;
											case 7:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant0_7_probability;
												break;
											case 8:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant0_8_probability;
												break;
											default:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant0_9_probability;
												break;
										}
										break;
									// 안전인첸트 4
									case 4:
										switch (item.getEnLevel()) {
											case 4:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant4_4_probability;
												break;
											case 5:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant4_5_probability;
												break;
											case 6:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant4_6_probability;
												break;
											case 7:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant4_7_probability;
												break;
											case 8:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant4_8_probability;
												break;
											default:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant4_9_probability;
												break;
										}
										break;
									// 안전인첸트 6
									case 6:
										switch (item.getEnLevel()) {
											case 6:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant6_6_probability;
												break;
											case 7:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant6_7_probability;
												break;
											case 8:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant6_8_probability;
												break;
											default:
												isEnchant = Math
														.random() < Lineage_Balance.armor_safe_enchant6_9_probability;
												break;
										}
										break;
								}
							}
						}
						break;
					// 저주 주문서
					case 2:
						rnd = -1;
						isEnchant = true;

						if (chance && Math.random() < 0.5)
							isEnchant = false;

						break;
				}
			}
		} else {
			// ==========================================
			// 🟦 1. 일반 [장신구 마법 주문서] (기본)
			// ==========================================
			if (getItem().getName().equalsIgnoreCase("장신구 마법 주문서")) {
				rnd = 1;
				switch (safeEnLevel) {
					case 0:
						switch (item.getEnLevel()) {
							case 0:
								isEnchant = Math.random() < Lineage_Balance.accessories_0_probability;
								break;
							case 1:
								isEnchant = Math.random() < Lineage_Balance.accessories_1_probability;
								break;
							case 2:
								isEnchant = Math.random() < Lineage_Balance.accessories_2_probability;
								break;
							case 3:
								isEnchant = Math.random() < Lineage_Balance.accessories_3_probability;
								break;
							case 4:
								isEnchant = Math.random() < Lineage_Balance.accessories_4_probability;
								break;
							case 5:
								isEnchant = Math.random() < Lineage_Balance.accessories_5_probability;
								break;
							case 6:
								isEnchant = Math.random() < Lineage_Balance.accessories_6_probability;
								break;
							case 7:
								isEnchant = Math.random() < Lineage_Balance.accessories_7_probability;
								break;
							case 8:
								isEnchant = Math.random() < Lineage_Balance.accessories_8_probability;
								break;
							default:
								isEnchant = Math.random() < Lineage_Balance.accessories_9_probability;
								break;
						}
						break;
					case 2:
						switch (item.getEnLevel()) {
							case 0:
							case 1:
								rnd = 1;
								isEnchant = true;
								break;
							case 2:
								isEnchant = Math.random() < Lineage_Balance.accessories_2_probability;
								break;
							case 3:
								isEnchant = Math.random() < Lineage_Balance.accessories_3_probability;
								break;
							case 4:
								isEnchant = Math.random() < Lineage_Balance.accessories_4_probability;
								break;
							case 5:
								isEnchant = Math.random() < Lineage_Balance.accessories_5_probability;
								break;
							case 6:
								isEnchant = Math.random() < Lineage_Balance.accessories_6_probability;
								break;
							case 7:
								isEnchant = Math.random() < Lineage_Balance.accessories_7_probability;
								break;
							case 8:
								isEnchant = Math.random() < Lineage_Balance.accessories_8_probability;
								break;
							default:
								isEnchant = Math.random() < Lineage_Balance.accessories_9_probability;
								break;
						}
						break;
				}
			}
			// ==========================================
			// 🟩 2. 하얀색 [오림의 장신구 마법 주문서] (bless == 1, 천장 X, 실패시 증발/유지)
			// ==========================================
			/*
			 * else if (getItem().getName().equalsIgnoreCase("오림의 장신구 마법 주문서") && bless ==
			 * 1) {
			 * rnd = 1;
			 * 
			 * switch (item.getEnLevel()) {
			 * case 0: isEnchant = Math.random() <
			 * Lineage_Balance.accessories_0_probability; break;
			 * case 1: isEnchant = Math.random() <
			 * Lineage_Balance.accessories_1_probability; break;
			 * case 2: isEnchant = Math.random() <
			 * Lineage_Balance.accessories_2_probability; break;
			 * case 3: isEnchant = Math.random() <
			 * Lineage_Balance.accessories_3_probability; break;
			 * case 4: isEnchant = Math.random() <
			 * Lineage_Balance.accessories_4_probability; break;
			 * case 5: isEnchant = Math.random() <
			 * Lineage_Balance.accessories_5_probability; break;
			 * case 6: isEnchant = Math.random() <
			 * Lineage_Balance.accessories_6_probability; break;
			 * case 7: isEnchant = Math.random() <
			 * Lineage_Balance.accessories_7_probability; break;
			 * case 8: isEnchant = Math.random() <
			 * Lineage_Balance.accessories_8_probability; break;
			 * default: isEnchant = Math.random() <
			 * Lineage_Balance.accessories_9_probability; break;
			 * }
			 * 
			 * if (!isEnchant) {
			 * isEnchant = true;
			 * 
			 * if (Math.random() < Lineage_Balance.accessories_nothing_probability)
			 * rnd = 0; // 운 좋게 증발 면함
			 * else
			 * rnd = -1; // 증발
			 * 
			 * if (item.getEnLevel() < 1 && rnd == -1)
			 * rnd = 0;
			 * 
			 * if (rnd == -1)
			 * EnMsg[1] = "$246";
			 * }
			 * }
			 */
			// ==========================================
			// 🟩 2. 하얀색 [오림의 장신구 마법 주문서] (bless == 1, 천장 X, 실패시 증발/유지)
			// ==========================================
			else if (getItem().getName().equalsIgnoreCase("오림의 장신구 마법 주문서") && bless == 1) {
				PcInstance pc = (PcInstance) cha;
				int currentLevel = item.getEnLevel();
				String timeString = Util.getLocaleString(System.currentTimeMillis(), true);

				rnd = 1;

				switch (currentLevel) {
					case 0:
						isEnchant = Math.random() < Lineage_Balance.accessories_0_probability;
						break;
					case 1:
						isEnchant = Math.random() < Lineage_Balance.accessories_1_probability;
						break;
					case 2:
						isEnchant = Math.random() < Lineage_Balance.accessories_2_probability;
						break;
					case 3:
						isEnchant = Math.random() < Lineage_Balance.accessories_3_probability;
						break;
					case 4:
						isEnchant = Math.random() < Lineage_Balance.accessories_4_probability;
						break;
					case 5:
						isEnchant = Math.random() < Lineage_Balance.accessories_5_probability;
						break;
					case 6:
						isEnchant = Math.random() < Lineage_Balance.accessories_6_probability;
						break;
					case 7:
						isEnchant = Math.random() < Lineage_Balance.accessories_7_probability;
						break;
					case 8:
						isEnchant = Math.random() < Lineage_Balance.accessories_8_probability;
						break;
					default:
						isEnchant = Math.random() < Lineage_Balance.accessories_9_probability;
						break;
				}

				// ✅ [추가] 1. 성공 로그 처리
				if (isEnchant) {
					final String log = String.format("[%s] [오림장신구 성공]\t [캐릭터: %s]\t [+%d -> +%d]", timeString,
							cha.getName(), currentLevel, currentLevel + 1);
					lineage.gui.GuiMain.display.asyncExec(new Runnable() {
						public void run() {
							lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
						}
					});
				}
				// ❌ 2. 실패 로그 처리
				else {
					isEnchant = true;

					if (Math.random() < Lineage_Balance.accessories_nothing_probability) {
						rnd = 0; // 운 좋게 하락 면함 (유지)

						final String log = String.format("[%s] [오림장신구 유지]\t [캐릭터: %s]\t [+%d 구간]\t (수치 유지)", timeString,
								cha.getName(), currentLevel);
						lineage.gui.GuiMain.display.asyncExec(new Runnable() {
							public void run() {
								lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
							}
						});
					} else {
						rnd = -1; // 수치 하락

						if (currentLevel < 1 && rnd == -1) {
							rnd = 0; // +0에서는 더 이상 떨어지지 않고 유지됨

							final String log = String.format("[%s] [오림장신구 유지]\t [캐릭터: %s]\t [+0 구간]\t (최소 수치 방어)",
									timeString, cha.getName());
							lineage.gui.GuiMain.display.asyncExec(new Runnable() {
								public void run() {
									lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
								}
							});
						} else {
							// 실제 하락 발생
							int nextLevel = currentLevel - 1;
							final String log = String.format("[%s] [오림장신구 하락]\t [캐릭터: %s]\t [+%d -> +%d] (수치 하락)",
									timeString, cha.getName(), currentLevel, nextLevel);
							lineage.gui.GuiMain.display.asyncExec(new Runnable() {
								public void run() {
									lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
								}
							});
						}
					}

					if (rnd == -1)
						EnMsg[1] = "$246";
				}
			}
			// ==========================================
			// 🟨 3. 노란색(축복) [오림의 장신구 마법 주문서] (bless == 0, 천장 O, 실패해도 무조건 유지)
			// ==========================================
			else if (getItem().getName().equalsIgnoreCase("오림의 장신구 마법 주문서") && bless == 0) {
				PcInstance pc = (PcInstance) cha;
				int currentLevel = item.getEnLevel();
				String timeString = Util.getLocaleString(System.currentTimeMillis(), true);

				rnd = 1; // 기본적으로 성공 시 +1

				// 1. [+5 -> +6] 도전
				if (currentLevel == 5) {
					int maxPity5 = Lineage_Balance.accessories_pity_count_5;

					if (pc.accCount5 >= (maxPity5 - 1)) {
						isEnchant = true;
						pc.accCount5 = 0;
						final String log = String.format("[%s] [오림장신구 천장성공]\t [캐릭터: %s]\t [+5 -> +6] (확정 천장)",
								timeString, cha.getName());
						lineage.gui.GuiMain.display.asyncExec(new Runnable() {
							public void run() {
								lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
							}
						});
					} else {
						isEnchant = Math.random() < Lineage_Balance.accessories_bless_5_probability;
						if (isEnchant) {
							pc.accCount5 = 0;
							final String log = String.format("[%s] [오림장신구 성공]\t [캐릭터: %s]\t [+5 -> +6] (스택 리셋)",
									timeString, cha.getName());
							lineage.gui.GuiMain.display.asyncExec(new Runnable() {
								public void run() {
									lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
								}
							});
						} else {
							isEnchant = true;
							rnd = 0;
							pc.accCount5 += 1;
							final String log = String.format("[%s] [오림장신구 실패]\t [캐릭터: %s]\t [+5 구간]\t [스택: %d / %d]",
									timeString, cha.getName(), pc.accCount5, maxPity5);
							lineage.gui.GuiMain.display.asyncExec(new Runnable() {
								public void run() {
									lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
								}
							});
						}
					}
				}
				// 2. [+6 -> +7] 도전
				else if (currentLevel == 6) {
					int maxPity6 = Lineage_Balance.accessories_pity_count_6;

					if (pc.accCount6 >= (maxPity6 - 1)) {
						isEnchant = true;
						pc.accCount6 = 0;
						final String log = String.format("[%s] [오림장신구 천장성공]\t [캐릭터: %s]\t [+6 -> +7] (확정 천장)",
								timeString, cha.getName());
						lineage.gui.GuiMain.display.asyncExec(new Runnable() {
							public void run() {
								lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
							}
						});
					} else {
						isEnchant = Math.random() < Lineage_Balance.accessories_bless_6_probability;
						if (isEnchant) {
							pc.accCount6 = 0;
							final String log = String.format("[%s] [오림장신구 성공]\t [캐릭터: %s]\t [+6 -> +7] (스택 리셋)",
									timeString, cha.getName());
							lineage.gui.GuiMain.display.asyncExec(new Runnable() {
								public void run() {
									lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
								}
							});
						} else {
							isEnchant = true;
							rnd = 0;
							pc.accCount6 += 1;
							final String log = String.format("[%s] [오림장신구 실패]\t [캐릭터: %s]\t [+6 구간]\t [스택: %d / %d]",
									timeString, cha.getName(), pc.accCount6, maxPity6);
							lineage.gui.GuiMain.display.asyncExec(new Runnable() {
								public void run() {
									lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
								}
							});
						}
					}
				}
				// 3. [+7 -> +8] 도전
				else if (currentLevel == 7) {
					int maxPity7 = Lineage_Balance.accessories_pity_count_7;

					if (pc.accCount7 >= (maxPity7 - 1)) {
						isEnchant = true;
						pc.accCount7 = 0;
						final String log = String.format("[%s] [오림장신구 천장성공]\t [캐릭터: %s]\t [+7 -> +8] (확정 천장)",
								timeString, cha.getName());
						lineage.gui.GuiMain.display.asyncExec(new Runnable() {
							public void run() {
								lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
							}
						});
					} else {
						isEnchant = Math.random() < Lineage_Balance.accessories_bless_7_probability;
						if (isEnchant) {
							pc.accCount7 = 0;
							final String log = String.format("[%s] [오림장신구 성공]\t [캐릭터: %s]\t [+7 -> +8] (스택 리셋)",
									timeString, cha.getName());
							lineage.gui.GuiMain.display.asyncExec(new Runnable() {
								public void run() {
									lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
								}
							});
						} else {
							isEnchant = true;
							rnd = 0;
							pc.accCount7 += 1;
							final String log = String.format("[%s] [오림장신구 실패]\t [캐릭터: %s]\t [+7 구간]\t [스택: %d / %d]",
									timeString, cha.getName(), pc.accCount7, maxPity7);
							lineage.gui.GuiMain.display.asyncExec(new Runnable() {
								public void run() {
									lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
								}
							});
						}
					}
				}
				// 4. 천장이 없는 나머지 구간 (+0~+4, +8 이상)
				else {
					switch (currentLevel) {
						case 0:
							isEnchant = Math.random() < Lineage_Balance.accessories_bless_0_probability;
							break;
						case 1:
							isEnchant = Math.random() < Lineage_Balance.accessories_bless_1_probability;
							break;
						case 2:
							isEnchant = Math.random() < Lineage_Balance.accessories_bless_2_probability;
							break;
						case 3:
							isEnchant = Math.random() < Lineage_Balance.accessories_bless_3_probability;
							break;
						case 4:
							isEnchant = Math.random() < Lineage_Balance.accessories_bless_4_probability;
							break;
						case 8:
							isEnchant = Math.random() < Lineage_Balance.accessories_bless_8_probability;
							break;
						default:
							isEnchant = Math.random() < Lineage_Balance.accessories_bless_9_probability;
							break;
					}

					if (isEnchant) {
						final String log = String.format("[%s] [오림장신구 성공]\t [캐릭터: %s]\t [+%d -> +%d] (일반구간)",
								timeString, cha.getName(), currentLevel, currentLevel + 1);
						lineage.gui.GuiMain.display.asyncExec(new Runnable() {
							public void run() {
								lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
							}
						});
					} else {
						isEnchant = true;
						rnd = 0; // 축복이라 실패해도 템 유지

						final String log = String.format("[%s] [오림장신구 보호]\t [캐릭터: %s]\t [+%d 구간]\t (일반구간 유지)",
								timeString, cha.getName(), currentLevel);
						lineage.gui.GuiMain.display.asyncExec(new Runnable() {
							public void run() {
								lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
							}
						});
					}
				}

				// 💾 [핵심 추가] 천장 스택을 데이터베이스(DB)에 즉시 저장합니다!
				pc.toCharacterSave2();
			}
		}
		// 메티스의 축복
		if (this instanceof ScrollOfMetis) {
			rnd = 1;
			isEnchant = true;
		}

		StringBuffer itemName = new StringBuffer();

		long time = System.currentTimeMillis();
		String timeString = Util.getLocaleString(time, true);
		// 인첸 성공시 아이템에 실제 변수 설정하는 부분.
		if (isEnchant || cha.getGm() > 0) {
			if (cha.getGm() > 0)
				isEnchant = true;

			if (item.getItem().getSafeEnchant() <= item.getEnLevel() && rnd > 0)
				itemName.append(String.format("%s ->", Util.getItemNameToString(item, item.getCount())));
			// 아이템 인첸트값 set
			if (item instanceof ItemWeaponInstance && (item.getEnLevel() + rnd) > item.getItem().getmaxEnchant()
					&& item.getItem().getmaxEnchant() > 0) {

				if (item.isEquipped()) {
					item.setEquipped(false);
					item.toOption(cha, false);

					item.setEnLevel(item.getItem().getmaxEnchant());

					item.setEquipped(true);
					item.toOption(cha, true);
					return item.getItem().getmaxEnchant();

				} else {
					item.setEnLevel(item.getItem().getmaxEnchant());
					return item.getItem().getmaxEnchant();
				}

			}
			if (item instanceof ItemWeaponInstance && (item.getEnLevel() + rnd) > Lineage.item_enchant_weapon_max) {

				if (item.isEquipped()) {
					item.setEquipped(false);
					item.toOption(cha, false);

					item.setEnLevel(Lineage.item_enchant_weapon_max);

					item.setEquipped(true);
					item.toOption(cha, true);
				} else {
					item.setEnLevel(Lineage.item_enchant_weapon_max);
				}
			} else if (!item.isAcc() && item instanceof ItemArmorInstance
					&& (item.getEnLevel() + rnd) > Lineage.item_enchant_armor_max) {
				if (item.isEquipped()) {
					item.setEquipped(false);
					item.toOption(cha, false);

					item.setEnLevel(Lineage.item_enchant_armor_max);

					item.setEquipped(true);
					item.toOption(cha, true);
				} else {
					item.setEnLevel(Lineage.item_enchant_armor_max);
				}
			} else if (item.isAcc() && (item.getEnLevel() + rnd) > Lineage.item_enchant_accessory_max) {
				if (item.isEquipped()) {
					item.setEquipped(false);
					item.toOption(cha, false);

					item.setEnLevel(Lineage.item_enchant_accessory_max);

					item.setEquipped(true);
					item.toOption(cha, true);
				} else {
					item.setEnLevel(Lineage.item_enchant_accessory_max);
				}
			} else {
				if (item.isEquipped()) {
					item.setEquipped(false);
					item.toOption(cha, false);

					item.setEnLevel(item.getEnLevel() + rnd);

					item.setEquipped(true);
					item.toOption(cha, true);
				} else {
					item.setEnLevel(item.getEnLevel() + rnd);
				}
			}

			if (item.getItem().getSafeEnchant() < item.getEnLevel() && rnd > 0)
				itemName.append(Util.getItemNameToString(item, item.getCount()));

			if (Lineage.server_version <= 144) {
				cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), item));
				cha.toSender(S_InventoryCount.clone(BasePacketPooling.getPool(S_InventoryCount.class), item));
			} else {
				cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
			}

			// \f1%0%s %2 강렬하게 %1 빛났지만 다행히 아무 일도 없었습니다.
			if (rnd == 0) {
				cha.toSender(
						S_Message.clone(BasePacketPooling.getPool(S_Message.class), 160, EnMsg[0], EnMsg[1], EnMsg[2]));

				// ✅ [갯수 표시 통일 패치] Util.getItemNameToString 을 사용합니다.
				final String log = String.format("[%s] [인첸트 실패(유지)]\t [캐릭터: %s]\t [아이템: %s]\t [주문서: %s]",
						timeString, cha.getName(), Util.getItemNameToString(item, item.getCount()),
						Util.getItemNameToString(this, getCount()));

				lineage.gui.GuiMain.display.asyncExec(new Runnable() {
					public void run() {
						lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(log);
					}
				});

				return -125;
			}

			// \f1%0%s %2 %1 빛납니다.
			if (rnd != 0)
				cha.toSender(
						S_Message.clone(BasePacketPooling.getPool(S_Message.class), 161, EnMsg[0], EnMsg[1], EnMsg[2]));

			if ((item.getItem().getType2().equalsIgnoreCase("ring")
					|| item.getItem().getType2().equalsIgnoreCase("necklace")
					|| item.getItem().getType2().equalsIgnoreCase("belt")) && rnd == -1)
				return -126;

			if (isEnchant && rnd > 0) {
				ItemDropMessageDatabase.sendMessageEn(cha, item, true);
			}
		} else {
			rnd = 0;
			// \f1%0%s %2 강렬하게 %1 빛나더니 증발되어 사라집니다.
			cha.toSender(
					S_Message.clone(BasePacketPooling.getPool(S_Message.class), 164, EnMsg[0], EnMsg[1], EnMsg[2]));

			ItemDropMessageDatabase.sendMessageEn(cha, item, false);
		}

		// (원래 여기 있던 time과 timeString 선언은 삭제 또는 유지하셔도 됩니다. 위에서 이미 선언했으므로 삭제하는 것이 깔끔합니다.)

		// log
		if (isEnchant) {
			Log.appendItem(cha, "type|인첸트성공", String.format("item_name|%s", item_name),
					String.format("item_objid|%d", item_objid), String.format("scroll_name|%s", toStringDB()),
					String.format("scroll_objid|%d", getObjectId()), String.format("scroll_bress|%d", getBless()),
					String.format("enchant_value|%d", rnd));

			if (!Common.system_config_console && item.getItem().getSafeEnchant() < item.getEnLevel() && rnd > 0) {
				String log = String.format("[%s] [인첸트 성공]\t [캐릭터: %s]\t [아이템: %s]\t [주문서: %s]\t [인첸증가: %d]", timeString,
						cha.getName(), Util.getItemNameToString(item, item.getCount()),
						Util.getItemNameToString(this, getCount()), rnd);

				GuiMain.display.asyncExec(new Runnable() {
					public void run() {
						GuiMain.getViewComposite().getEnchantComposite().toLog(log);
					}
				});
			}
		} else {
			if (rnd == 0) {
				EnchantLostItemDatabase.append(cha, item, this);
			}

			Log.appendItem(cha, "type|인첸트실패", String.format("item_name|%s", item_name),
					String.format("item_objid|%d", item_objid), String.format("scroll_name|%s", toStringDB()),
					String.format("scroll_objid|%d", getObjectId()), String.format("scroll_bress|%d", getBless()));

			if (!Common.system_config_console) {
				String log = String.format("[%s] [인첸트 실패]\t [캐릭터: %s]\t [아이템: %s]\t [주문서: %s]", timeString,
						cha.getName(), Util.getItemNameToString(item, item.getCount()),
						Util.getItemNameToString(this, getCount()));

				GuiMain.display.asyncExec(new Runnable() {
					public void run() {
						GuiMain.getViewComposite().getEnchantComposite().toLog(log);
					}
				});
			}
		}
		return rnd;
	}
}
