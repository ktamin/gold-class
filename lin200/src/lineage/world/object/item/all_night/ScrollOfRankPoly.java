package lineage.world.object.item.all_night;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Poly;
import lineage.bean.lineage.BuffInterface;
import lineage.database.PolyDatabase;
import lineage.database.SkillDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectPoly;
// import lineage.network.packet.server.S_ObjectPolyIcon; // 구버전 아이콘 미사용
import lineage.share.Lineage;
import java.util.ArrayList;
import java.util.List;
import lineage.world.World;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.RankController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.ShapeChange;

public class ScrollOfRankPoly extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ScrollOfRankPoly();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (!cha.isDead() && !cha.isWorldDelete() && cha.getInventory() != null && cha instanceof PcInstance) {
			
			PcInstance pc = (PcInstance) cha;
			// int allRank = RankController.getAllRank(cha.getObjectId());
			// int classRank = RankController.getClassRank(cha.getObjectId(), cha.getClassType());
			
			if(cha.getMap() == 807){
				ChattingController.toChatting(cha, "여기서는 사용 하실 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			// 팀대전, 낚시중에 변신 불가.
			if (!World.isTeamBattleMap(cha) && !cha.isFishing()) {
				BuffInterface temp = BuffController.find(cha, SkillDatabase.find(208));
				
				// 세트 아이템 착용 중 일 경우 변신 불가
				if (cha.getMap() != Lineage.teamBattleMap && cha.getMap() != Lineage.BattleRoyalMap && temp != null && temp.getTime() == -1) {
					if (!pc.isAutoHunt) {
						ChattingController.toChatting(cha, "세트 아이템 착용 중 일 경우 변신이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
					}
					return;
				}

				String polyName = null;
				int time = getItem().getDuration();

				switch (cha.getClassType()) {
					case 0: polyName = (cha.getClassSex() == 0) ? "왕자 랭커" : "공주 랭커"; break;
					case 1: polyName = (cha.getClassSex() == 0) ? "남자기사 랭커" : "여자기사 랭커"; break;
					case 2: polyName = (cha.getClassSex() == 0) ? "남자요정 랭커" : "여자요정 랭커"; break;
					case 3: polyName = (cha.getClassSex() == 0) ? "남자법사 랭커" : "여자법사 랭커"; break;
					case 4: polyName = (cha.getClassSex() == 0) ? "남자다엘 랭커" : "여자다엘 랭커"; break;	
				}			

				Poly p = PolyDatabase.getName(polyName);

				if (p != null) {
					
					// ==========================================
					// 1. 가장 먼저 기존 변신 버프를 삭제 (외형 리셋 및 기존 아이콘 지우기)
					// ==========================================
					BuffController.remove(cha, ShapeChange.class);
					
					// 2. 장비 해제 및 새로운 외형 적용
					PolyDatabase.toEquipped(cha, p);
					cha.setGfx(p.getGfxId());

					if (Lineage.is_weapon_speed) {
						if (!cha.checkSpear()) {
							ItemInstance weapon = cha.getInventory().getSlot(Lineage.SLOT_WEAPON);
							if (weapon != null && weapon.getItem() != null && SpriteFrameDatabase.findGfxMode(cha.getGfx(), weapon.getItem().getGfxMode() + Lineage.GFX_MODE_ATTACK)) {
								cha.setGfxMode(weapon.getItem().getGfxMode());
							} else {
								cha.setGfxMode(p.getGfxMode());
							}
						}
					} else {
						cha.setGfxMode(p.getGfxMode());
					}

					// 3. 랭커 변신 외형 패킷 전송
					cha.toSender(S_ObjectPoly.clone(BasePacketPooling.getPool(S_ObjectPoly.class), cha), true);
					
					// 4. 신규 아이콘(40번) 전송
					if (cha instanceof PcInstance) {
						// 1번 단계에서 이미 기존 아이콘이 지워졌으므로 바로 새 시간을 띄우면 됩니다.
						SC_BUFFICON_NOTI.on((PcInstance)cha, 109, time, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
					}
					
					// 5. 새 변신 버프 등록 (여기에 있던 중복 remove 삭제됨!)
					BuffController.append(cha, ShapeChange.clone(BuffController.getPool(ShapeChange.class), SkillDatabase.find(208), time));

					// 6. 변신 이펙트 출력
					cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, 6082), true);

					// 7. 아이템 수량 갱신
					if (!getItem().getName().contains("카드") && !getItem().getName().contains("반지")) {
						cha.getInventory().count(this, getCount() - 1, true);
					}
				}
			}
		}
	}
}