package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 야도란텔 extends object {

	// [추가됨] 이동 불가능한 맵 ID 목록 설정
	static public final int TeleportHomeImpossibilityMap[] = { 70, 89, 509, 1400 };

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		showHtml(pc);
	}
	
	public void showHtml(PcInstance pc){
			List<String> ynlist = new ArrayList<String>();
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "yadolantel", null, ynlist));
		
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp){
		
		if(pc.isAutoHunt){
			pc.isAutoHunt = false;
			pc.autohunt_target = null;
			pc.is_auto_return_home = false;
		}
		
		if (Lineage.open_wait) {
			ChattingController.toChatting(pc, "[오픈대기] 오픈대기에는 이동 하실수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}		
		if (pc.isDead() || pc.isLock() || pc.isFishing()){
			ChattingController.toChatting(pc, "현재 상태에선 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		// [추가된 로직] 이동 불가능 지역 체크
		for (int cantMap : TeleportHomeImpossibilityMap) {
			if (pc.getMap() == cantMap) {
				ChattingController.toChatting(pc, "이곳에서는 해당 아이템을 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
		}
		
		// -------------------------------------------------------------
		
		if (action.contains("yadolantel-yadon1")) {
				if(pc.getInventory().isAden(0, true)){
					pc.toPotal(32728,  32727, 11);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}

			}
		
		if (action.contains("yadolantel-yadon2")) {
			if(pc.getInventory().isAden(0, true)){
				pc.toPotal(32671,  32872, 33);
			}else{
				// \f1아데나가 충분치 않습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}

		}
		
		if (action.contains("yadolantel-yadon3")) {
			if(pc.getInventory().isAden(0, true)){
				pc.toPotal(32807,  32811, 27);
			}else{
				// \f1아데나가 충분치 않습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}

		}
		
		if (action.contains("yadolantel-yadon4")) {
			if(pc.getInventory().isAden(30000, true)){
				pc.toPotal(32726,  32730, 13);
			}else{
				// \f1아데나가 충분치 않습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}

		}
		
		if (action.contains("yadolantel-yadon5")) {
			if(pc.getInventory().isAden(30000, true)){
				pc.toPotal(32668,  32852, 36);
			}else{
				// \f1아데나가 충분치 않습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}

		}
		
		if (action.contains("yadolantel-yadon6")) {
			if(pc.getInventory().isAden(30000, true)){
				pc.toPotal(32795,  32799, 28);
			}else{
				// \f1아데나가 충분치 않습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
		
		if (action.contains("yadolantel-yadon7")) {
			if(pc.getInventory().isAden(30000, true)){
				pc.toPotal(33617,  32397, 4);
			}else{
				// \f1아데나가 충분치 않습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
		
		if (action.contains("yadolantel-yadon8")) {
			if(pc.getInventory().isAden(30000, true)){
				pc.toPotal(34011,  32454, 4);
			}else{
				// \f1아데나가 충분치 않습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
		
		if (action.contains("yadolantel-yadon9")) {
			if(pc.getInventory().isAden(10000, true)){
				pc.toPotal(33641, 32761, 4);
			}else{
				// \f1아데나가 충분치 않습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
		
		if (action.contains("yadolantel-yadon10")) {
			if(pc.getInventory().isAden(10000, true)){
				pc.toPotal(33558, 32673, 4);
			}else{
				// \f1아데나가 충분치 않습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
		
		if (action.contains("yadolantel-yadon11")) {
			if(pc.getInventory().isAden(10000, true)){
				pc.toPotal(33335, 32449, 4);
			}else{
				// \f1아데나가 충분치 않습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
		
		if (action.contains("yadolantel-yadon12")) {
			if(pc.getInventory().isAden(0, true)){
				pc.toPotal(32891, 32777, 78);
			}else{
				// \f1아데나가 충분치 않습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
		
		if (action.contains("yadolantel-yadon13")) {
			if(pc.getInventory().isAden(30000, true)){
				pc.toPotal(32736, 32799, 82);
			}else{
				// \f1아데나가 충분치 않습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
	
		showHtml(pc);
	}
	
	public static ItemArmorInstance clone(ItemInstance pool) {
		// TODO Auto-generated method stub
		return null;
	}
}