package lineage.world.object.instance;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import all_night.Lineage_Balance;
import lineage.bean.database.ItemSkill;
import lineage.bean.database.Skill;
import lineage.bean.lineage.Inventory;
import lineage.database.ItemSkillDatabase;
import lineage.database.PolyDatabase;
import lineage.database.SkillDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.network.packet.server.S_ObjectMode;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.DamageController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.magic.Disease;
import lineage.world.object.magic.EraseMagic;
import lineage.world.object.magic.ShockStun;
import lineage.world.object.magic.Silence;

public class ItemWeaponInstance extends ItemIllusionInstance {

	private int skill_dmg;
	private int skill_effect;
	private enum Elem { NONE, FIRE, WIND, WATER, EARTH }
	private Elem getElemOfThisWeapon() {
	    if (getEnFire()  > 0) return Elem.FIRE;
	    if (getEnWind()  > 0) return Elem.WIND;
	    if (getEnWater() > 0) return Elem.WATER;
	    if (getEnEarth() > 0) return Elem.EARTH;
	    return Elem.NONE;
	}

	private int getElemStageOfThisWeapon() {
	    if (getEnFire()  > 0) return getEnFire();
	    if (getEnWind()  > 0) return getEnWind();
	    if (getEnWater() > 0) return getEnWater();
	    if (getEnEarth() > 0) return getEnEarth();
	    return 0;
	}

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ItemWeaponInstance();
		return item;
	}

	@Override
	public void close() {
		super.close();

		skill_dmg = skill_effect = 0;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (!equipped)
			ChattingController.toChatting(cha, String.format("\\fY[%s] 안전인첸트: %d", getItem().getName(), getItem().getSafeEnchant()), Lineage.CHATTING_MODE_MESSAGE);
		if (isLvCheck(cha)) {
			if (isClassCheck(cha)) {
				if ((isEquippedGfx(cha) && PolyDatabase.toEquipped(cha, this)) || equipped || getItem().getType2().equals("fishing_rod")) {
		
					Inventory inv = cha.getInventory();
					ItemInstance weapon = inv.getSlot(Lineage.SLOT_WEAPON);
					if (weapon != null && weapon.isEquipped() && weapon.getObjectId() != this.getObjectId()) {
						weapon.toClick(cha, null);
					}
					if (inv != null) {
						if (equipped) {
							if (bless == 2) {
								// \f1그렇게 할 수 없습니다. 저주 받은 것 같습니다.
								cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 150));
								return;
							}
							
							setEquipped(false);
							cha.setRestMode(this, equipped);

							if (getItem().getType2().equalsIgnoreCase("fishing_rod")) {
								cha.setFishing(false);
								cha.setFishingTime(0L);
								ChattingController.toChatting(cha, "낚시를 종료합니다.", Lineage.CHATTING_MODE_MESSAGE);
							}
						} else {
							// 낚시대 및 낚시존 확인
							if (getItem().getType2().equalsIgnoreCase("fishing_rod") && !cha.isFishingZone()) {
								ChattingController.toChatting(cha, "마을 낚시터에서 사용가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
								return;
							}
							if (!cha.isFishing() && getItem().getType2().equalsIgnoreCase("fishing_rod")) {
								// 변신 확인
								if (cha.getGfx() != cha.getClassGfx()) {
									if (cha.isSetPoly) {
										ChattingController.toChatting(cha, "세트 아이템 착용 중 일 경우 낚시가 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
										return;
									}
									
									cha.setTempFishing(this);
									// 낚시를 시작하면 변신이 해제됩니다. 계속 하시겠습니까? (y/n)
									cha.toSender(S_MessageYesNo.clone(BasePacketPooling.getPool(S_MessageYesNo.class), 771));
									return;
								} else {
									cha.setFishing(true);
									cha.setFishingTime(System.currentTimeMillis());
									ChattingController.toChatting(cha, "낚시를 시작합니다.", Lineage.CHATTING_MODE_MESSAGE);
								}
							}
							if (getItem().isTohand()) {
								if (inv.getSlot(Lineage.SLOT_SHIELD) != null) {
									if (!Lineage.item_equipped_type) {
										// \f1방패를 착용하고서는 두손 무기를 쓸수 없습니다.
										cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 128));
										return;
									} else {
										inv.getSlot(Lineage.SLOT_SHIELD).toClick(cha, null);
									}
								} 
							}
							if (inv.getSlot(Lineage.SLOT_WEAPON) != null) {
								if (Lineage.item_equipped_type && inv.getSlot(Lineage.SLOT_WEAPON).getBless() != 2) {
									inv.getSlot(Lineage.SLOT_WEAPON).toClick(cha, null);
								} else {
									// \f1이미 뭔가를 착용하고 있습니다.
									cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 124));
									return;
								}
							}
							
							setEquipped(true);
							cha.setRestMode(this, equipped);
						}

						toSetoption(cha, true);
						toEquipped(cha, inv);
						toOption(cha, true);
						toBuffCheck(cha);
					}
				} else {
					ChattingController.toChatting(cha, "착용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
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
	 * 무기 착용 및 해제 처리 메서드.
	 */
	@Override
	public void toEquipped(Character cha, Inventory inv) {
		if (inv == null)
			return;

		if (equipped) {
			inv.setSlot(item.getSlot(), this);
			
			if (Lineage.is_weapon_speed) {
				if (!cha.checkSpear()) {
					if (SpriteFrameDatabase.findGfxMode(cha.getGfx(), item.getGfxMode()))
						// 변신상태일 경우 spr_frame 테이블에서 해당 gfx에 모드가 있을경우 변경
						cha.setGfxMode(item.getGfxMode());
				}
			} else {
				if (cha.getGfx() == cha.getClassGfx())
					// 변신상태가 아닐때만 변경하도록 함.
					cha.setGfxMode(cha.getGfxMode() + item.getGfxMode());
			}
			
			cha.toSender(S_ObjectMode.clone(BasePacketPooling.getPool(S_ObjectMode.class), cha), true);

			if (getBless() == 2) {
				//\f1%0%s 손에 달라 붙었습니다.
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 149, getName()));
			}
		} else {
			inv.setSlot(item.getSlot(), null);
			
			if (Lineage.is_weapon_speed) {
				if (SpriteFrameDatabase.findGfxMode(cha.getGfx(), cha.getGfxMode() - item.getGfxMode())) {
					// 변신상태일 경우 무기 해제시 변신의 기본 모드로 변경
					if ((cha.getGfx() != cha.getClassGfx())
						&& (cha.getGfx() != Lineage.royal_male_gfx && cha.getGfx() != Lineage.royal_female_gfx
						&& cha.getGfx() != Lineage.knight_male_gfx && cha.getGfx() != Lineage.knight_female_gfx
						&& cha.getGfx() != Lineage.elf_male_gfx && cha.getGfx() != Lineage.elf_female_gfx
						&& cha.getGfx() != Lineage.darkelf_male_gfx && cha.getGfx() != Lineage.darkelf_female_gfx
						&& cha.getGfx() != Lineage.wizard_male_gfx && cha.getGfx() != Lineage.wizard_female_gfx)) {
						cha.setGfxMode(PolyDatabase.getPolyGfx(cha.getGfx()).getGfxMode());
					} else {
						cha.setGfxMode(cha.getGfxMode() - item.getGfxMode());
					}
				}
			} else {
				if (cha.getGfx() == cha.getClassGfx()) {
					// 변신상태가 아닐때만 변경하도록 함.
					cha.setGfxMode(cha.getGfxMode() - item.getGfxMode());
					cha.toSender(S_ObjectMode.clone(BasePacketPooling.getPool(S_ObjectMode.class), cha), true);
				}
			}
			
			cha.toSender(S_ObjectMode.clone(BasePacketPooling.getPool(S_ObjectMode.class), cha), true);
		}
			
		cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
	}
	
	/**
	 * 무기를 착용할 경우 해당 gfx에 모션이있는지 체크
	 * 2017-11-19
	 * by all-night
	 */
	public boolean isEquippedGfx(Character cha) {
		if (!equipped) {
			if (!cha.isSpearAction(this)) {
				if (!SpriteFrameDatabase.findGfxMode(cha.getGfx(), item.getGfxMode() + Lineage.GFX_MODE_ATTACK))
					return false;
				else
					return true;
			}
		}
		
		return true;
	}

	/**
	 * 리니지 월드에 접속했을때 착용중인 아이템 처리를 위해 사용되는 메서드.
	 */
	@Override
	public void toWorldJoin(Connection con, PcInstance pc) {
		super.toWorldJoin(con, pc);
		if (equipped) {
			toSetoption(pc, false);
			pc.getInventory().setSlot(item.getSlot(), this);
			toOption(pc, false);
		}
	}

	/**
	 * 인첸트 활성화 됫을때 아이템의 뒷처리를 처리하도록 요청하는 메서드.
	 */
	@Override
	public void toEnchant(PcInstance pc, int en) {
		//
		if (en == -125 ||en == -126 || en == -127)
			return;
		//
		if (en != 0) {
			if (isEquipped()) {
				// 성공햇으면서 착용중이면 뭔갈 해줘야할까?
			}
		} else {
			Inventory inv = pc.getInventory();
			if (equipped) {
				setEquipped(false);
				toSetoption(pc, true);
				toEquipped(pc, inv);
				toOption(pc, true);
				toBuffCheck(pc);
			}
			// 인벤에서 제거하면서 메모리도 함께 제거함.
			inv.count(this, 0, true);
		}
		//
		super.toEnchant(pc, en);
	}
	
	// 앨리스용 변수  
	static private List<object> list = new ArrayList<object>();
	static private int x;
	static private int y;	

	@Override
	public boolean toDamage(Character cha, object o) {
		if (o == null || cha == null || o.isDead() || cha.isDead() || item == null)
			return false;
		
		skill_dmg = 0;
		skill_effect = 0;

		// 플러그인 확인.
		Object pco = PluginController.init(ItemWeaponInstance.class, "toDamage", this, cha, o);
		if (pco != null)
			return (Boolean) pco;

		boolean r_bool = false;
		
		// 체력 스틸하기
		if (o.getNowHp() > 0 && item.getStealHp() > 0 && (o instanceof MonsterInstance || o instanceof PcInstance)) {
			// 1~3랜덤 추출
			int steal_hp = Util.random(1, getEnLevel()) + item.getStealHp();
			
			// 인챈트에 따른 메리트
			if (getEnLevel() > 6) {
				switch (getEnLevel()) {
				case 7:
					steal_hp *= Lineage_Balance.weapon_en_7_damage;
					break;
				case 8:
					steal_hp *= Lineage_Balance.weapon_en_8_damage;
					break;
				case 9:
					steal_hp *= Lineage_Balance.weapon_en_9_damage;
					break;
				case 10:
					steal_hp *= Lineage_Balance.weapon_en_10_damage;
					break;
				case 11:
					steal_hp *= Lineage_Balance.weapon_en_11_damage;
					break;
				case 12:
					steal_hp *= Lineage_Balance.weapon_en_12_damage;
					break;
				case 13:
					steal_hp *= Lineage_Balance.weapon_en_13_damage;
					break;
				case 14:
					steal_hp *= Lineage_Balance.weapon_en_14_damage;
					break;
				case 15:
					steal_hp *= Lineage_Balance.weapon_en_15_damage;
					break;
				}
			}
			
			// 타켓에 hp가 스틸할 값보다 작을경우 현재 가지고있는 hp값으로 변경		
			steal_hp = SkillController.getMrDamage(cha, o, steal_hp, o instanceof Character);
			
			if (o.getNowHp() < steal_hp)
				steal_hp = o.getNowHp();
			// hp제거하기.
			o.setNowHp(o.getNowHp() - steal_hp);
			// hp추가하기.
			cha.setNowHp(cha.getNowHp() + steal_hp);
		}
		
		// 마나 스틸하기
		if (o.getNowMp() > 0 && item.getStealMp() > 0 && (o instanceof MonsterInstance || o instanceof PcInstance)) {
			// 랜덤 추출
			int steal_mp = Util.random(1, getEnLevel()) + item.getStealMp();

			// 인챈트에 따른 메리트
			if (getEnLevel() > 6) {
				switch (getEnLevel()) {
				case 7:
					steal_mp *= Lineage_Balance.weapon_en_7_damage;
					break;
				case 8:
					steal_mp *= Lineage_Balance.weapon_en_8_damage;
					break;
				case 9:
					steal_mp *= Lineage_Balance.weapon_en_9_damage;
					break;
				case 10:
					steal_mp *= Lineage_Balance.weapon_en_10_damage;
					break;
				case 11:
					steal_mp *= Lineage_Balance.weapon_en_11_damage;
					break;
				case 12:
					steal_mp *= Lineage_Balance.weapon_en_12_damage;
					break;
				case 13:
					steal_mp *= Lineage_Balance.weapon_en_13_damage;
					break;
				case 14:
					steal_mp *= Lineage_Balance.weapon_en_14_damage;
					break;
				case 15:
					steal_mp *= Lineage_Balance.weapon_en_15_damage;
					break;
				}
			}
			
			steal_mp = SkillController.getMrDamage(cha, o, steal_mp, o instanceof Character);			

			// 타켓에 mp가 스틸할 값보다 작을경우 현재 가지고있는 mp값으로 변경
			if (o.getNowMp() < steal_mp)
				steal_mp = o.getNowMp();
			// mp제거하기.
			o.setNowMp(o.getNowMp() - steal_mp);
			// mp추가하기.
			cha.setNowMp(cha.getNowMp() + steal_mp);
		}
		
/*
		// [여기] 흡혈/흡마 처리 블록 바로 다음에 삽입
		// ─────────────────────────────────────────────
		{
		    Elem elem = getElemOfThisWeapon();
		    int  stage = getElemStageOfThisWeapon(); // 1~5

		    if (elem != Elem.NONE && stage > 0) {
		        if (stage > 5) stage = 5; // 상한

		        double proc = 0.0;
		        int add = 0;
		        int fx  = 0;

		        switch (elem) {
		            case FIRE:
		                proc = Lineage_Balance.elem_fire_proc[stage];
		                add  = Lineage_Balance.elem_fire_add[stage];
		                fx   = Lineage_Balance.elem_fire_fx[stage];
		                break;
		            case WIND:
		                proc = Lineage_Balance.elem_wind_proc[stage];
		                add  = Lineage_Balance.elem_wind_add[stage];
		                fx   = Lineage_Balance.elem_wind_fx[stage];
		                break;
		            case WATER:
		                proc = Lineage_Balance.elem_water_proc[stage];
		                add  = Lineage_Balance.elem_water_add[stage];
		                fx   = Lineage_Balance.elem_water_fx[stage];
		                break;
		            case EARTH:
		                proc = Lineage_Balance.elem_earth_proc[stage];
		                add  = Lineage_Balance.elem_earth_add[stage];
		                fx   = Lineage_Balance.elem_earth_fx[stage];
		                break;
		            default:
		                break;
		        }

		        // 확률 판정: proc=0.12 -> 12%
		        // Util.random(1,10000)로 세밀 판정 (0.01% 단위까지도 가능)
		        if (proc > 0 && Util.random(1, 10000) <= (int)Math.round(proc * 10000)) {
		            // 여기서 "속성 추가타" 확정
		            this.setSkillDmg(add);     // => toDamage(int)에서 반환됨
		            this.setSkillEffect(fx);   // => toDamageEffect()에서 반환됨
		            return true;               // 발동했으니 종료 (원하면 누적도 가능)
		        }
		    }
		}
*/

		// ↓↓↓ 여기부터 기존 아이템스킬(DB) 발동 로직 (그대로 유지)
		// 마법 발동.
		// ---------------------------------------------------------
				// [수정된 아이템 스킬 발동 로직] - findAll을 사용하여 중복 발동 지원
				// ---------------------------------------------------------
				
				// 1. 해당 아이템에 붙은 '모든' 스킬 리스트를 가져옵니다.
				List<ItemSkill> skillList = ItemSkillDatabase.findAll(item == null ? "" : item.getName());
				
				if (skillList != null && !skillList.isEmpty()) {
					// 2. 리스트에 있는 스킬들을 하나씩 순서대로 검사합니다.
					for (ItemSkill is : skillList) {
						
						// 확률 체크: (기본확률 + 인챈트 * 추가확률)
						// 발동 조건(인챈트 레벨 등) 불만족 시 다음 스킬로 넘어감(continue)
						if (Util.random(1, 100) > (is.getDefaultProbability() + (getEnLevel() * is.getAddEnchantProbability())) 
								|| getEnLevel() < is.getEnLevel()) {
							continue;
						}

						Skill skill = SkillDatabase.find(is.getSkillUid());
						
						if (skill != null) {
							int uid = skill.getUid();
							
							// -------------------------------------------------
							// [A] 앨리스(광역) 처리 로직
							// -------------------------------------------------
							if(is.getName().startsWith("앨리스")) {
								// (기존 로직 유지) 메인 타겟 리스트 추가
								if(skill_dmg > 0) list.add(o);
								
								for(object oo : cha.getInsideList()){
									boolean isNomarl = World.isNormalZone(o.getX(), o.getY(), o.getMap());
									if(o.getObjectId() != oo.getObjectId() && 
											Util.isDistance(o, oo, 3) && 
											isNomarl) {
										
										// 인챈트에 따른 추가 데미지
										int bonusDmg = 0;
										if(this.getEnLevel() > 0) bonusDmg = this.getEnLevel();
										
										// 광역 데미지 계산 (skill_dmg 변수 오염 방지를 위해 별도 계산)
										int aoe_base_dmg = skill_dmg + bonusDmg;
										int oo_dmg = SkillController.getDamage(cha, o, oo, skill, aoe_base_dmg, skill.getElement());
										
										if(oo instanceof SummonInstance) continue;
										else DamageController.toDamage(cha, oo, oo_dmg, Lineage.ATTACK_TYPE_MAGIC);
										
										if(oo_dmg > 0) list.add(oo);
									}
								}
							}

							// -------------------------------------------------
							// [B] 이펙트 및 스킬 로직 분기 처리
							// -------------------------------------------------
							
							// 1. 디버프 계열 (자체 로직에서 이펙트 처리하므로 여기선 0)
							if (uid == 636 || uid == 637) { // 디지즈
								skill_effect = 0; 
								Disease.init2(cha, skill, (int) o.getObjectId());
							}
							else if (uid == 726) { // 사일런스
								skill_effect = 0; 
								Silence.init2(cha, skill, (int) o.getObjectId());
							}
							else if (uid == 16) { // 쇼크 스턴
								skill_effect = 0;
								ShockStun.init(cha, skill, o);
							}
							// 2. 일반 데미지/버프 계열
							else {
								// 무기 이펙트 옵션이 켜져있을 때만 이펙트 설정
								if (cha.무기이펙트) {
									skill_effect = skill.getCastGfx();
								} else {
									skill_effect = 0;
								}

								// 데미지 계산 및 적용
								int current_skill_dmg = 0;
								
								if (is.isSetInt()) {
									// INT 영향 받는 경우
									current_skill_dmg = SkillController.getDamage(cha, o, o, skill, 0, skill.getElement());
									current_skill_dmg *= is.getRateDmg();

									if (current_skill_dmg > 0 && o.isBuffEraseMagic())
										BuffController.remove(o, EraseMagic.class);
								} else {
									// 고정 데미지 (Min ~ Max)
									current_skill_dmg = (int) Math.round(Util.random(skill.getMindmg(), skill.getMaxdmg()));
									current_skill_dmg *= is.getRateDmg();
								}
								
								// [중요] 계산된 데미지를 즉시 적용 (여러 스킬이 터질 수 있으므로)
								if (current_skill_dmg > 0) {
									// 기존 변수 skill_dmg에 합산하거나, 바로 데미지를 주어야 함
									// 여기서는 바로 데미지 처리를 하거나 skill_dmg에 누적합니다.
									// (만약 아래쪽 코드에서 skill_dmg를 한 번에 처리한다면 += 사용)
									skill_dmg += current_skill_dmg; 
									
									// 혹은 즉시 데미지 적용 (추천 방식)
									// DamageController.toDamage(cha, o, current_skill_dmg, Lineage.ATTACK_TYPE_MAGIC);
								}
							}
							
							// 하나라도 발동되면 true
							r_bool = true;
						}
					} // end for loop
				} else {
					// 스킬이 없는 경우
					skill_effect = 0;
				}

				return r_bool;
	}

	@Override
	public int toDamage(int dmg) {
		// 
		PluginController.init(ItemWeaponInstance.class, "toDamage", this, dmg);

		return skill_dmg;
	}

	@Override
	public int toDamageEffect() {
		return skill_effect;
	}

	public void setSkillDmg(int skillDmg) {
		skill_dmg = skillDmg;
	}

	public void setSkillEffect(int skillEffect) {
		skill_effect = skillEffect;	
	}
}