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

public class 야도란텔1 extends object {
	
	// [추가됨] 이동 불가능한 맵 ID 목록 설정
	static public final int TeleportHomeImpossibilityMap[] = { 70, 89, 509, 1400 };

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		showHtml(pc);
	}
	
	public void showHtml(PcInstance pc){
			List<String> ynlist = new ArrayList<String>();	
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "yadolantel1", null, ynlist));
		
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
		
			//기란 창고
			if (action.contains("yadolantel1-yadon1")) {
				if(pc.getInventory().isAden(0, true)){
					pc.toPotal(33433,32814, 4);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//잡화상점
			if (action.contains("yadolantel1-yadon2")) {
				if(pc.getInventory().isAden(0, true)){
					pc.toPotal(33439,32804, 4);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//잊혀진 섬
			if (action.contains("yadolantel1-yadon3")) {
				if(pc.getInventory().isAden(0, true)){
					pc.toPotal(33437,32819, 4);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//기란 여관
			if (action.contains("yadolantel1-yadon4")) {
				if(pc.getInventory().isAden(0, true)){
					pc.toPotal(33431,32797, 4);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//마을 12시
			if (action.contains("yadolantel1-yadon5")) {
				if(pc.getInventory().isAden(0, true)){
					pc.toPotal(33447,32794, 4);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//낚싯터
			if (action.contains("yadolantel1-yadon6")) {
				if(pc.getInventory().isAden(0, true)){
					pc.toPotal(33412,32801, 4);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//펫 창고
			if (action.contains("yadolantel1-yadon7")) {
				if(pc.getInventory().isAden(0, true)){
					pc.toPotal(33443,32822, 4);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//기감 입구
			if (action.contains("yadolantel1-yadon8")) {
				if(pc.getInventory().isAden(0, true)){
					pc.toPotal(33428,32821, 4);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//사냥터 상점
			if (action.contains("yadolantel1-yadon9")) {
				if(pc.getInventory().isAden(0, true)){
					pc.toPotal(33445,32815, 4);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//게시판
			if (action.contains("yadolantel1-yadon0")) {
				if(pc.getInventory().isAden(0, true)){
					pc.toPotal(33421,32797, 4);
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
