package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import all_night.Lineage_Balance;
import lineage.bean.database.MonsterSkill;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.ServerDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectLock;
import lineage.network.packet.server.S_ObjectPoisonLock;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.DamageController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

/*
public class ShockStun extends Magic {

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time, object o, int effect) {
		if (bi == null)
			bi = new ShockStun(skill, o);
		bi.setSkill(skill);
		bi.setTime(time);
		
		bi.setEffect(new lineage.world.object.npc.background.ShockStun());
		bi.getEffect().setGfx(effect);	
		bi.getEffect().setObjectId(ServerDatabase.nextEtcObjId());
		bi.getEffect().toTeleport(o.getX(), o.getY(), o.getMap(), false);
		if (!o.isLockLow()) {
			o.setLockLow(true);
			o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x02));
			o.toSender(S_ObjectPoisonLock.clone(BasePacketPooling.getPool(S_ObjectPoisonLock.class), o), true);
		}
		return bi;
	  }


	public ShockStun(Skill skill, object o) {
		super(null, skill);
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 78, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuff(object o) {
		o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x02));
		o.toSender(S_ObjectPoisonLock.clone(BasePacketPooling.getPool(S_ObjectPoisonLock.class), o), true);
	}

	@Override
	public void toBuffUpdate(object o) {
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 78, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		getEffect().clearList(true);
		World.remove(getEffect());
		
		if (o.isWorldDelete())
			return;
		
		o.setLockLow(false);
		o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x00));
		o.toSender(S_ObjectPoisonLock.clone(BasePacketPooling.getPool(S_ObjectPoisonLock.class), o), true);
		
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 78, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}
*/

public class ShockStun extends Magic {

	// ==========================================
	// 💡 [독립 시계] 서버 시스템과 별개로 정확하게 째깍째깍 도는 자바 순수 타이머!
	// ==========================================
	private static final java.util.Timer effectTimer = new java.util.Timer("ShockStunTimer", true);

	public static java.util.concurrent.ConcurrentHashMap<Long, ShockStun> activeStuns = new java.util.concurrent.ConcurrentHashMap<>();
	private java.util.concurrent.CopyOnWriteArrayList<EffectData> effectList = new java.util.concurrent.CopyOnWriteArrayList<>();

	static class EffectData {
		public lineage.world.object.npc.background.ShockStun effectNpc;
		public EffectData(lineage.world.object.npc.background.ShockStun npc) {
			this.effectNpc = npc;
		}
	}

	static public void applyStun(object o, Skill skill, int time, int effectGfx) {
		ShockStun active = activeStuns.get(o.getObjectId());
		if (active != null && o.isLockLow()) {
			active.addEffect(time, effectGfx, o);
		} else {
			BuffController.append(o, ShockStun.clone(BuffController.getPool(ShockStun.class), skill, time, o, effectGfx));
		}
	}

	public void addEffect(int extendTime, int effectGfx, object o) {
		if (extendTime > this.getTime()) {
			this.setTime(extendTime); 
		}

		final lineage.world.object.npc.background.ShockStun bg = new lineage.world.object.npc.background.ShockStun();
		bg.setGfx(effectGfx);	
		bg.setObjectId(ServerDatabase.nextEtcObjId());
		bg.toTeleport(o.getX(), o.getY(), o.getMap(), false);

		final EffectData data = new EffectData(bg);
		this.effectList.add(data);

		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 78, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}

		// ==========================================
		// 💡 [핵심 해결] 새로 태어난 이펙트에게 '자폭 타이머'를 부착합니다.
		// "extendTime(초) 뒤에 너 스스로 월드에서 사라져라!"
		// ==========================================
		effectTimer.schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				// 이미 죽거나 풀린 이펙트가 아니라면 삭제!
				if (effectList.contains(data)) {
					if (data.effectNpc != null) {
						data.effectNpc.clearList(true);
						World.remove(data.effectNpc);
					}
					effectList.remove(data);
				}
			}
		}, extendTime * 1000L); // extendTime(초) * 1000 을 해서 밀리초 단위로 정확히 예약!
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time, object o, int effect) {
		ShockStun ss = (bi == null) ? new ShockStun(skill, o) : (ShockStun) bi;
		ss.setSkill(skill);
		
		if (!o.isLockLow()) {
			for (EffectData data : ss.effectList) {
				if (data.effectNpc != null) {
					data.effectNpc.clearList(true);
					World.remove(data.effectNpc);
				}
			}
			ss.effectList.clear(); 
		}
		
		ss.addEffect(time, effect, o); 
		
		if (!o.isLockLow()) {
			o.setLockLow(true);
			o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x02));
			o.toSender(S_ObjectPoisonLock.clone(BasePacketPooling.getPool(S_ObjectPoisonLock.class), o), true);
		}
		return ss;
	}

	public ShockStun(Skill skill, object o) { super(null, skill); }

	@Override
	public void toBuffStart(object o) {
		activeStuns.put(o.getObjectId(), this);
	}

	@Override
	public void toBuff(object o) {
		o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x02));
		o.toSender(S_ObjectPoisonLock.clone(BasePacketPooling.getPool(S_ObjectPoisonLock.class), o), true);
	}

	@Override
	public void toBuffUpdate(object o) {
		// 자폭 타이머가 다 알아서 하므로 서버는 꿀을 빨아도 됩니다!
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 78, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o) { toBuffEnd(o); }

	@Override
	public void toBuffEnd(object o) {
		activeStuns.remove(o.getObjectId()); 
		
		for (EffectData data : effectList) {
			if (data.effectNpc != null) {
				data.effectNpc.clearList(true);
				World.remove(data.effectNpc);
			}
		}
		effectList.clear(); // 💡 자폭 타이머가 폭발하기 전에 스턴이 풀리면 리스트를 비워서 작동 중지
		
		if (o.isWorldDelete()) return;
		o.setLockLow(false);
		o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x00));
		o.toSender(S_ObjectPoisonLock.clone(BasePacketPooling.getPool(S_ObjectPoisonLock.class), o), true);
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 78, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}
// === 이 아래로는 기존의 [1] 스턴 시전 메인 로직 ~ 이 그대로 이어집니다 ===

// === 이 아래로는 기존의 [1] 스턴 시전 메인 로직 ~ 이 그대로 이어집니다 ===

	/**
	 * [1] 스턴 시전 메인 로직 (데미지 포함)
	 */
	static public void init(Character cha, Skill skill, int object_id) {
		
		boolean isDarkElfStun = false;
		if (cha.getClassType() == Lineage.LINEAGE_CLASS_DARKELF) {
			ItemInstance deItem = cha.getInventory().find("쉐도우 스턴", 0, 1);
			if (deItem != null) {
				isDarkElfStun = true;
			}
		}

		if (cha.getGm() == 0 && cha.getClassType() != Lineage.LINEAGE_CLASS_ROYAL && cha.getClassType() != Lineage.LINEAGE_CLASS_KNIGHT && !isDarkElfStun) {
			ChattingController.toChatting(cha, "\\fY당신의 클래스는 사용할 수 없거나, 특수 아이템이 필요합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		int dmg = 0;
		int range = 1;
		
		if (cha.getClassType() == Lineage.LINEAGE_CLASS_ROYAL) {
			ItemInstance item2 = cha.getInventory().find("엑스칼리버", 0, 1);
			if(item2 != null){
				range = 4;
			} else {
				range = 2;
			}
		}

		object o = cha.findInsideList(object_id);
		if (!World.isAttack(cha, o))
			return;

		if (cha.getInventory().getSlot(Lineage.SLOT_WEAPON) == null) {
			ChattingController.toChatting(cha, "\\fY무기를 착용해야 사용가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if (isDarkElfStun) {
			String wpType = cha.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2();
			if (!wpType.equalsIgnoreCase("claw") && !wpType.equalsIgnoreCase("edoryu")) {
				ChattingController.toChatting(cha, "\\fY크로우나 이도류를 착용해야 사용가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
		} else {
			if (Lineage_Balance.is_stun_twohandsword) {
				if (cha.getClassType() == Lineage.LINEAGE_CLASS_ROYAL) {
					if (!cha.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2().equalsIgnoreCase("spear")) {
						ChattingController.toChatting(cha, "\\fY창을 착용해야 사용가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
				} else {
					if (!cha.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2().equalsIgnoreCase("tohandsword")) {
						ChattingController.toChatting(cha, "\\fY양손검을 착용해야 사용가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
				}
			} else {
				if (!cha.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2().equalsIgnoreCase("sword") && 
					!cha.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2().equalsIgnoreCase("tohandsword") &&
					!cha.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getType2().equalsIgnoreCase("spear")) {
					ChattingController.toChatting(cha, "\\fY검을 착용해야 사용가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
			}
		}

		if (!Util.isDistance(cha, o, range)) {
			cha.delay_magic = 0;
			ChattingController.toChatting(cha, "\\fY상대방이 너무 멀리있습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if (o != null && Util.isAreaAttack(cha, o) && Util.isAreaAttack(o, cha)) {
			if (SkillController.isMagic(cha, skill, true)) {
				
				if (!isDarkElfStun) {
					if (cha instanceof PcInstance) {
						SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(7000).send((PcInstance) cha);
					}
					o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
				}

				if (!o.isLockHigh()) {
					dmg = DamageController.getDamage(cha, o, false, cha.getInventory().getSlot(Lineage.SLOT_WEAPON), null, 0);
					dmg *= 1; 
					DamageController.toDamage(cha, o, dmg, Lineage.ATTACK_TYPE_WEAPON);

					if (SpriteFrameDatabase.findGfxMode(o.getGfx(), o.getGfxMode() + Lineage.GFX_MODE_DAMAGE))
						o.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), o, Lineage.GFX_MODE_DAMAGE), true);
					
					Detection.onBuff(cha);
					
					if (!isDarkElfStun) {
						cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_ATTACK), true);
					}
					
					if (SkillController.isFigure(cha, o, skill, true, false)) {
						int time = 0;						

						if (isDarkElfStun) {
							time = Util.random(2, 3); 
							ChattingController.toChatting(cha, "\\fV[시스템] 쉐도우 스턴이 발동되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
						//	BuffController.append(o, ShockStun.clone(BuffController.getPool(ShockStun.class), skill, time, o, 21765));
							ShockStun.applyStun(o, skill, time, 21765); // 다크엘프 쉐도우스턴
						
						} else {
							int level = cha.getLevel() - o.getLevel();
							if (level <= 0 && level >= -3) time = Util.random(2, 3);
							else if (level <= 0 && level >= -5) time = Util.random(1, 2);
							else if (level <= -6) time = Util.random(0, 1);
							else time = Util.random(1, skill.getBuffDuration());
							
							ItemInstance forceStunItem = cha.getInventory().find("포스 스턴", 0, 1);
							ItemInstance excaliburItem = (cha.getClassType() == Lineage.LINEAGE_CLASS_ROYAL) ? cha.getInventory().find("엑스칼리버", 0, 1) : null;
							                             
								
								// ==========================================
								// 🟡 1. 포스스턴 (확률 발동)
								// ==========================================
							if (forceStunItem != null && cha.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT && Util.random(1, 100) <= Lineage_Balance.force_stun_chance) {
								time = time + Util.random(2, 3);
									
									// 🚨 [삭제함] o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 25338), true); 
									
									// 오직 BuffController 하나만 남깁니다!
									//BuffController.append(o, ShockStun.clone(BuffController.getPool(ShockStun.class), skill, time, o, 25338));
									ShockStun.applyStun(o, skill, time, 25338); // 기사 포스스턴
								} 
								
								// ==========================================
								// ⚪ 2. 엑스칼리버 착용 시 
								// ==========================================
								else if (excaliburItem != null) {
									// 🚨 [삭제함] o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 23354), true);
									
								//	BuffController.append(o, ShockStun.clone(BuffController.getPool(ShockStun.class), skill, time, o, 23354));
									ShockStun.applyStun(o, skill, time, 23354); // 군주 엑스칼리버
								} 
								
								// ==========================================
								// ⚪ 3. 기본 기사/군주 스턴 
								// ==========================================
								else {
									// 🚨 [삭제함] o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 16229), true);
									
								//	BuffController.append(o, ShockStun.clone(BuffController.getPool(ShockStun.class), skill, time, o, 16229));
									ShockStun.applyStun(o, skill, time, 16229); // 기사/군주 기본
								}
							}
						}
					}
				}
			}
		}
	
/*	
	(원본 백업 생략 - 유저님 코드에 있던 백업 부분 그대로 유지)
*/	
	
	/**
	 * [2] 보조 스턴 로직 (데미지 없음, 발동용)
	 */
	static public void init(Character cha, Skill skill, object o) {
		if (cha.getInventory().getSlot(Lineage.SLOT_WEAPON) == null) {
			return;
		}
		
		int range = 1;
		
		// ✅ [버그 수정] 군주 사거리 체크 시 '포스스턴'이 아닌 '엑스칼리버'로 정상 검사
		if (cha.getClassType() == Lineage.LINEAGE_CLASS_ROYAL) {
			ItemInstance item2 = cha.getInventory().find("엑스칼리버", 0, 1); 
			if(item2 != null){
				range = 4;
			} else {
				range = 2;
			}
		}

		if (!World.isAttack(cha, o)) return;
		if (!Util.isDistance(cha, o, range)) return;
		
		if (o != null && Util.isAreaAttack(cha, o) && Util.isAreaAttack(o, cha)) {
			if (!o.isLockHigh()) {
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
				
				if (SkillController.isFigure(cha, o, skill, true, false)) {
					int time = 0;
										
					// ✅ [추가] 두 번째 함수에도 다크엘프 예외 로직 동기화 적용
					boolean isDarkElfStun = false;
					if (cha.getClassType() == Lineage.LINEAGE_CLASS_DARKELF) {
						if (cha.getInventory().find("쉐도우 스턴", 0, 1) != null) {
							isDarkElfStun = true;
						}
					}

					if (isDarkElfStun) {
						time = Util.random(2, 3);
					//	BuffController.append(o, ShockStun.clone(BuffController.getPool(ShockStun.class), skill, time, o, 21765));
						ShockStun.applyStun(o, skill, time, 21765); // 다크엘프 쉐도우스턴
					
					} else {
						int level = cha.getLevel() - o.getLevel();
						if (level <= 0 && level >= -3) time = Util.random(2, 3);
						else if (level <= 0 && level >= -5) time = Util.random(1, 2);
						else if (level <= -6) time = Util.random(0, 1);
						else time = Util.random(1, skill.getBuffDuration());

						ItemInstance forceStunItem = cha.getInventory().find("포스 스턴", 0, 1);
						ItemInstance excaliburItem = (cha.getClassType() == Lineage.LINEAGE_CLASS_ROYAL) ? cha.getInventory().find("엑스칼리버", 0, 1) : null;
						
							
							// ==========================================
							// 🟡 1. 포스스턴 (확률 발동)
							// ==========================================
						if (forceStunItem != null && cha.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT && Util.random(1, 100) <= Lineage_Balance.force_stun_chance) {
							time = time + Util.random(2, 3);
								
								// 🚨 [삭제함] o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 25338), true); 
								
								// 오직 BuffController 하나만 남깁니다!
							//	BuffController.append(o, ShockStun.clone(BuffController.getPool(ShockStun.class), skill, time, o, 25338));
								ShockStun.applyStun(o, skill, time, 25338); // 기사 포스스턴
							} 
							
							// ==========================================
							// ⚪ 2. 엑스칼리버 착용 시 
							// ==========================================
							else if (excaliburItem != null) {
								// 🚨 [삭제함] o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 23354), true);
								
							//	BuffController.append(o, ShockStun.clone(BuffController.getPool(ShockStun.class), skill, time, o, 23354));
								ShockStun.applyStun(o, skill, time, 23354); // 군주 엑스칼리버
							} 
							
							// ==========================================
							// ⚪ 3. 기본 기사/군주 스턴 
							// ==========================================
							else {
								// 🚨 [삭제함] o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 16229), true);
								
							//	BuffController.append(o, ShockStun.clone(BuffController.getPool(ShockStun.class), skill, time, o, 16229));
								ShockStun.applyStun(o, skill, time, 16229); // 기사/군주 기본
							}
						}
					}
				}
			}
		}

	/**
	 * [3] 몬스터용 (나이트발드 등)
	 */
	static public void init(MonsterInstance mi, object o, MonsterSkill ms, int action) {
		if (o.isLock()) return;
		if (!SkillController.isMagic(mi, ms, true)) return;
		
		mi.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), mi, action), true);
		
		if (ms.getCastGfx() > 0)
			mi.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), mi, ms.getCastGfx()), true);
		
		if (SkillController.isFigure(mi, o, ms.getSkill(), true, false))	
	//		BuffController.append(o, ShockStun.clone(BuffController.getPool(ShockStun.class), ms.getSkill(), Util.random(1, ms.getSkill().getBuffDuration()), o, 9717));
			ShockStun.applyStun(o, ms.getSkill(), Util.random(1, ms.getSkill().getBuffDuration()), 9717);
	}
}