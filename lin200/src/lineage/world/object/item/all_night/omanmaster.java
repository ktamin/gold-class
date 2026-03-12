package lineage.world.object.item.all_night;

import java.util.ArrayList;
import java.util.List;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class omanmaster extends ItemInstance {

	// [이동 불가능한 맵 ID 목록]
	static public final int TeleportHomeImpossibilityMap[] = { 70, 89, 509, 1400 };

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new omanmaster();
		return item;
	}

	// 1. 아이템 클릭 시 HTML 열기
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha instanceof PcInstance && cha.getInventory() != null){
			PcInstance pc = (PcInstance) cha;
			// ★중요★: this를 넘겨줘야 toTalk가 실행됩니다.
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "yadolantel3", null, null));
		}
	}

	// 2. HTML 링크 클릭 시 처리 로직
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
		
		// 2-1. 기본 제약 조건 체크
		if (Lineage.open_wait) {
			ChattingController.toChatting(pc, "[오픈대기] 오픈대기에는 이동 하실수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}		
		if (pc.isDead() || pc.isLock() || pc.isFishing()){
			ChattingController.toChatting(pc, "현재 상태에선 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		// 2-2. [추가된 로직] 특정 맵에서 사용 금지 체크
		for (int cantMap : TeleportHomeImpossibilityMap) {
			if (pc.getMap() == cantMap) {
				ChattingController.toChatting(pc, "이곳에서는 해당 아이템을 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
		}
		
		// 2-3. 텔레포트 로직
		// 오만 1층
		if (action.contains("yadolantel3-yadon1")) {
			if(pc.getInventory().isAden(1000, true)){
				pc.toPotal(32794,32799, 101);
			}else{
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
		// 오만 2층
		else if (action.contains("yadolantel3-yadon2")) {
			if(pc.getInventory().isAden(1000, true)){
				pc.toPotal(32793,32800, 102);
			}else{
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
		// 오만 3층
		else if (action.contains("yadolantel3-yadon3")) {
			if(pc.getInventory().isAden(1000, true)){
				pc.toPotal(32793,32800, 103);
			}else{
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
		// 오만 4층
		else if (action.contains("yadolantel3-yadon4")) {
			if(pc.getInventory().isAden(1000, true)){
				pc.toPotal(32668,32860, 104);
			}else{
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
		// 오만 5층
		else if (action.contains("yadolantel3-yadon5")) {
			if(pc.getInventory().isAden(1000, true)){
				pc.toPotal(32668,32863, 105);
			}else{
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
		// 오만 6층
		else if (action.contains("yadolantel3-yadon6")) {
			if(pc.getInventory().isAden(1000, true)){
				pc.toPotal(32666,32863, 106);
			}else{
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
		// 오만 7층
		else if (action.contains("yadolantel3-yadon7")) {
			if(pc.getInventory().isAden(1000, true)){
				pc.toPotal(32668,32861, 107);
			}else{
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
		// 오만 8층
		else if (action.contains("yadolantel3-yadon8")) {
			if(pc.getInventory().isAden(1000, true)){
				pc.toPotal(32668,32860, 108);
			}else{
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
		// 오만 9층
		else if (action.contains("yadolantel3-yadon9")) {
			if(pc.getInventory().isAden(1000, true)){
				pc.toPotal(32668,32860, 109);
			}else{
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
		// 오만 10층
		else if (action.contains("yadolantel3-yadon0")) {
			if(pc.getInventory().isAden(1000, true)){
				pc.toPotal(32794,32799, 110);
			}else{
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}			
		// 오만 정상
		else if (action.contains("yadolantel3-yadontop")) {
			if(pc.getInventory().isAden(1000, true)){
				pc.toPotal(32691,32946, 200);
			}else{
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
		
		// 3. 창 다시 보여주기 (필요 없으면 주석 처리)
		// pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "yadolantel3", null, null));
	}
}