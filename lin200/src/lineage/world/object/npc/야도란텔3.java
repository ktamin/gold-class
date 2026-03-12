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

public class 야도란텔3 extends object {
	
	// [추가됨] 이동 불가능한 맵 ID 목록 설정
	static public final int TeleportHomeImpossibilityMap[] = { 70, 89, 509, 1400 };

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		showHtml(pc);
	}
	
	public void showHtml(PcInstance pc){
			List<String> ynlist = new ArrayList<String>();	
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "yadolantel3", null, ynlist));
		
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp){
	//	if (action.equalsIgnoreCase("yadolan3_teleport"))

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
		
			//오만 1층
			if (action.contains("yadolantel3-yadon1")) {
				if(pc.getInventory().isAden(1000, true)){
					pc.toPotal(32794,32799, 101);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//오만 2층
			if (action.contains("yadolantel3-yadon2")) {
				if(pc.getInventory().isAden(1000, true)){
					pc.toPotal(32793,32800, 102);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//그신2
			if (action.contains("yadolantel3-yadon3")) {
				if(pc.getInventory().isAden(1000, true)){
					pc.toPotal(32793,32800, 103);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//그신3
			if (action.contains("yadolantel3-yadon4")) {
				if(pc.getInventory().isAden(50000, true)){
					pc.toPotal(32668,32860, 104);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			if (action.contains("yadolantel3-yadon5")) {
				if(pc.getInventory().isAden(1000, true)){
					pc.toPotal(32668,32863, 105);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			
			if (action.contains("yadolantel3-yadon6")) {
				if(pc.getInventory().isAden(1000, true)){
					pc.toPotal(32666,32863, 106);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			
			if (action.contains("yadolantel3-yadon7")) {
				if(pc.getInventory().isAden(1000, true)){
					pc.toPotal(32668,32861, 107);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			
			if (action.contains("yadolantel3-yadon8")) {
				if(pc.getInventory().isAden(1000, true)){
					pc.toPotal(32668,32860, 108);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			
			if (action.contains("yadolantel3-yadon9")) {
				if(pc.getInventory().isAden(1000, true)){
					pc.toPotal(32668,32860, 109);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			
			if (action.contains("yadolantel3-yadon0")) {
				if(pc.getInventory().isAden(1000, true)){
					pc.toPotal(32794,32799, 110);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}			
			
			if (action.contains("yadolantel3-yadontop")) {
				if(pc.getInventory().isAden(1000, true)){
					pc.toPotal(32691,32946, 200);
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
