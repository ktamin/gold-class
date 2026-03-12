package lineage.world.object.magic;

import java.util.ArrayList;
import java.util.List;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Poly;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.ItemMaplewandDatabase;
import lineage.database.PolyDatabase;
import lineage.database.SkillDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectPoly;
import lineage.network.packet.server.S_ObjectPolyIcon;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.RankController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public class ShapeChange extends Magic {

	public ShapeChange(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new ShapeChange(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		if (o.isWorldDelete())
			return;

		o.setGfx(o.getClassGfx());
		if (o.getInventory() != null && o.getInventory().getSlot(Lineage.SLOT_WEAPON) != null)
			o.setGfxMode(o.getClassGfxMode() + o.getInventory().getSlot(Lineage.SLOT_WEAPON).getItem().getGfxMode());
		else
			o.setGfxMode(o.getClassGfxMode());
		o.toSender(S_ObjectPoly.clone(BasePacketPooling.getPool(S_ObjectPoly.class), o), true);
		
		// [추가] 변신 종료 시 아이콘(40번) 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 40, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		
		ChattingController.toChatting(o, "\\fY변신 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	@Override
	public void toBuff(object o) {
		if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
			ChattingController.toChatting(o, "\\fY변신: "+getTime()+"초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(Character cha, Skill skill, int object_id) {
		// 초기화
		object o = null;
		// 타겟 찾기
		if (object_id == cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList(object_id);
		// 처리
		if (o != null) {
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
			
			if (SkillController.isMagic(cha, skill, true) && SkillController.isFigure(cha, o, skill, false, false)) {
				// 변신
				onBuff(cha, o, null, skill.getBuffDuration(), true, true);
				// 이팩트
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
			}
		}
	}

	static public void init(Character cha, int time) {
		// [수정] 아이콘 처리는 onBuff 내부에서 통합 관리하거나, 여기서 호출 시에도 40번 아이콘 사용
		// 기존 S_ObjectPolyIcon(구버전 아이콘 패킷)은 주석 처리하거나 놔둬도 되지만, 신규 아이콘 패킷을 우선시합니다.
		// if (Lineage.server_version > 182)
		// 	cha.toSender(S_ObjectPolyIcon.clone(BasePacketPooling.getPool(S_ObjectPolyIcon.class), time));
		
		// GM 명령어 등으로 인한 변신일 수 있음. 일단 아이콘 출력.
		if (cha instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)cha, 40, time, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		
		BuffController.append(cha, ShapeChange.clone(BuffController.getPool(ShapeChange.class), SkillDatabase.find(208), time));
	}

	/**
	 * 몬스터가 사용하는 변신 처리 함수.
	 */
	static public void init(MonsterInstance mi, int loc, Poly poly, int time) {
		for (object o : mi.getInsideList()) {
			if (o instanceof PcInstance) {
				// 변신
				onBuff(mi, o, poly, time, true, true);
			}
		}
	}

	/**
	 * 변신주문서 등에서 호출
	 */
	static public boolean init(Character cha, Character target, Poly p, int time, int bress) {
		if (cha.getInventory() != null && p != null) {
			boolean isRingPoly = false;
			
			if (cha instanceof PcInstance) {
				PcInstance pc = (PcInstance) cha;
				
				// [핵심] 주문서(Scroll)를 쓰면 pc.isTempPoly()가 true가 됩니다.
				// 반지를 쓰면(ScrollPolymorph3) 이 값이 false로 남아있어서 isRingPoly가 true가 됩니다.
				if (!pc.isTempPoly())
					isRingPoly = true;
			}

			// 1. 기준 레벨 설정
			int checkLevel = cha.getLevel();

			// 2. 반지 변신인지 확인
			if (isRingPoly) {
				checkLevel += 2;
				// [추가] 반지의 경우 시간 재설정 (7200초 = 2시간)
				// 여기서 시간을 강제로 2시간으로 맞춥니다.
				time = 7200;
			}

			// 3. 조건 비교
			if (p.getMinLevel() <= checkLevel || Lineage.event_poly || 
				cha.getGm() > 0 || cha.getMap() == Lineage.teamBattleMap) {
				
				// 축복 변줌일 경우 10분 추가 (반지가 아닐 때만 적용하는 것이 좋으나, 로직상 시간은 위에서 결정됨)
				// 만약 반지도 축복받은게 있다면 2시간+10분이 될 수 있음.
				// 일반 변줌 시간(1800초) + 600초 = 2400초 (40분)
				if(!isRingPoly && bress == 1) // bress 체크 로직이 필요하다면 추가
					time += 600;
				
				onBuff(cha, target, p, time, false, true);
			} else {
				ChattingController.toChatting(cha, String.format("%s: %d레벨 이상 변신 가능", p.getPolyName(), p.getMinLevel()), Lineage.CHATTING_MODE_MESSAGE);
				return false;
			}
		} else {
			BuffController.remove(cha, ShapeChange.class);
		}
		return true;
	}
	
	/**
	 * 변신 최종 뒷처리 구간
	 */
	static public void onBuff(Character cha, object o, Poly p, int time, boolean ring, boolean packet) {
		if (cha == null || o == null || !(o instanceof PcInstance))
			return;
		
		if (cha.getMap() != Lineage.teamBattleMap && cha.getMap() != Lineage.BattleRoyalMap && cha.isSetPoly) {
			ChattingController.toChatting(cha, "세트 아이템 착용 중 일 경우 변신이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		if (o.getInventory() != null && ring && o.getInventory().isRingOfPolymorphControl()) {
			// 변신할 괴물의 이름을 넣으십시오. (반지 클릭 시 목록 보여주기)
			if (packet) {
				List<String> quickPolymorph = new ArrayList<String>();
				int allRank = RankController.getAllRank(cha.getObjectId());
				int classRank = RankController.getClassRank(cha.getObjectId(), cha.getClassType());
				
				quickPolymorph.clear();
				quickPolymorph.add(cha.getQuickPolymorph() == null || cha.getQuickPolymorph().equalsIgnoreCase("") || cha.getQuickPolymorph().length() < 1 ? "빠른 변신 목록 없음" : cha.getQuickPolymorph());
				
				cha.toSender(S_MessageYesNo.clone(BasePacketPooling.getPool(S_MessageYesNo.class), 180));
				
				if (Lineage.is_rank_poly) {
					if ((((allRank > 0 && allRank <= Lineage.rank_poly_all) || (classRank > 0 && classRank <= Lineage.rank_poly_class)) && cha.getLevel() >= Lineage.rank_min_level) || Lineage.event_rank_poly)
						cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), cha, "monlistsR", null, quickPolymorph));
					else
						cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), cha, "monlists", null, quickPolymorph));
				} else {
					cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), cha, "monlists", null, quickPolymorph));
				}
				
				// [추가] 반지를 눌러서 목록을 띄울 때 아이콘 출력 (2시간)
				if (cha instanceof PcInstance) {
					SC_BUFFICON_NOTI.on((PcInstance)cha, 40, 7200, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
				}
			}
		} else {
			// 실제 변신 수행
			PcInstance pc = (PcInstance) o;
			if (p == null)
				p = ItemMaplewandDatabase.randomPoly();
			if (p != null && !o.isDead()) {
				if (o instanceof Character)
					// 장비 해제.
					PolyDatabase.toEquipped((Character) o, p);
				
				// 변신
				o.setGfx(p.getGfxId());

				if (Lineage.is_weapon_speed) {
					if (!o.checkSpear()) {
						ItemInstance weapon = o.getInventory().getSlot(Lineage.SLOT_WEAPON);
						
						if (weapon != null && weapon.getItem() != null && SpriteFrameDatabase.findGfxMode(o.getGfx(), weapon.getItem().getGfxMode() + Lineage.GFX_MODE_ATTACK)) {
							o.setGfxMode(weapon.getItem().getGfxMode());
						} else {
							o.setGfxMode(p.getGfxMode());
						}
					}
				} else {
					o.setGfxMode(p.getGfxMode());
				}
			
				if (packet) {
					o.toSender(S_ObjectPoly.clone(BasePacketPooling.getPool(S_ObjectPoly.class), o), true);
					
					// [수정] 구버전 아이콘 패킷 대신 신규 아이콘 패킷(40번) 사용
					// if (Lineage.server_version > 182)
					//	o.toSender(S_ObjectPolyIcon.clone(BasePacketPooling.getPool(S_ObjectPolyIcon.class), time));
					
					// [추가] 40번 아이콘 출력 (전달받은 time 시간만큼)
					if (o instanceof PcInstance) {
						SC_BUFFICON_NOTI.on((PcInstance)o, 40, time, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
					}
				}
				// 버프등록
				BuffController.append(o, ShapeChange.clone(BuffController.getPool(ShapeChange.class), SkillDatabase.find(208), time));

				// 일반 변신 이팩트 6082, 단풍 나무 변신 6130
				if (!p.getName().contains("세트") && !p.getName().contains("운영자") && !p.getName().contains("좀비 변신")) {
					o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 6082), true);
				
					// 아이템 수량 갱신 (주문서 사용 시)
					if (pc.getTempPolyScroll() != null)
						pc.getInventory().count(pc.getTempPolyScroll(), pc.getTempPolyScroll().getCount() - 1, true);
					pc.setTempPoly(false);
					pc.setTempPolyScroll(null);
					if (!p.getName().contains("랭커") && !p.getName().contains("질리언(80)") && !p.getName().contains("헬바인(80)") && !p.getName().contains("군터(80)") && !p.getName().contains("켄라우헬(80)"))
						pc.setQuickPolymorph(p.getName());
				}
			} else {
				if (packet)
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
			}
		}
	}

	/**
	 * 세트 아이템 변신이 있을경우 여기에서 처리
	 */
	static public void onBuff(Character cha, object o, Poly p, int time, boolean packet) {
		if (cha == null || o == null || !(o instanceof PcInstance))
			return;

		if (cha.getMap() == Lineage.teamBattleMap || cha.getMap() == Lineage.BattleRoyalMap)
			return;

		if (p == null)
			p = ItemMaplewandDatabase.randomPoly();
		if (p != null && !o.isDead()) {
			if (o instanceof Character)
				// 장비 해제.
				PolyDatabase.toEquipped((Character) o, p);
			// 변신
			o.setGfx(p.getGfxId());
			
			if (Lineage.is_weapon_speed) {
				if (!cha.checkSpear()) {
					ItemInstance weapon = o.getInventory().getSlot(Lineage.SLOT_WEAPON);
					
					if (weapon != null && weapon.getItem() != null && SpriteFrameDatabase.findGfxMode(o.getGfx(), weapon.getItem().getGfxMode() + Lineage.GFX_MODE_ATTACK)) {
						o.setGfxMode(weapon.getItem().getGfxMode());
					} else {
						if (o.getInventory().getSlot(Lineage.SLOT_WEAPON) != null && o instanceof Character) {
							Character c = (Character) o;
							c.getInventory().getSlot(Lineage.SLOT_WEAPON).toClick(c, null);
							ChattingController.toChatting(c, "현재 착용중인 무기는 해당 변신으로 착용 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
						}		
						o.setGfxMode(p.getGfxMode());
					}
				}
			} else {
				o.setGfxMode(p.getGfxMode());
			}
			
			if (packet) {
				o.toSender(S_ObjectPoly.clone(BasePacketPooling.getPool(S_ObjectPoly.class), o), true);
				
				// [수정] 세트 아이템은 시간이 무제한이므로 매우 긴 시간을 아이콘으로 표시
				// if (Lineage.server_version > 182)
				//	o.toSender(S_ObjectPolyIcon.clone(BasePacketPooling.getPool(S_ObjectPolyIcon.class), time));
				
				if (o instanceof PcInstance) {
					// 세트 아이템은 time이 보통 -1 등으로 오거나 별도 처리되는데, 아이콘 상으로는 무한대 느낌을 위해 아주 큰 값을 줍니다.
					// 2000000000 (약 23일) 혹은 time 그대로 사용
					int iconTime = (time < 0) ? 2000000000 : time;
					SC_BUFFICON_NOTI.on((PcInstance)o, 40, iconTime, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
				}
			}
			// 버프등록
			BuffController.append(o, ShapeChange.clone(BuffController.getPool(ShapeChange.class), SkillDatabase.find(208), time));

		} else {
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
		}
	}
}