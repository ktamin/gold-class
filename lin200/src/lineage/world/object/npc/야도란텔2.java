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
import lineage.world.object.instance.PcInstance;

public class 야도란텔2 extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		showHtml(pc);
	}
	
	public void showHtml(PcInstance pc){
			List<String> ynlist2 = new ArrayList<String>();
	
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "yadolantel2", null, ynlist2));
		
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp){
		

			//오만의탑1층
			if (action.contains("yadolantel2-yadons")) {
				if(pc.getInventory().isAden(30000, true)){
					pc.toPotal(32796, 32800, 101);
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			
			}
			
			if (action.contains("yadolantel2-yadon2")) {
				boolean check1 = false;
				boolean check2 = false;
				check1 = pc.getInventory().find("오만의탑 2층 이동 부적") == null ? false : true;
				check2 = pc.getInventory().find("오만의 탑 2층 지배 부적") == null ? false : true;
					if(check1 || check2){
						pc.toPotal(32796, 32800, 102);
						}else if(pc.getInventory().isAden("오만의탑 2층 이동 주문서",1, true)){
								pc.toPotal(32796, 32800, 102);
								}else{
									// \f1아데나가 충분치 않습니다.
									ChattingController.toChatting(pc, "오만의탑2층 이동 주문서가 부족합니다", Lineage.CHATTING_MODE_MESSAGE);
								}
				}
			if (action.contains("yadolantel2-yadon3")) {
				boolean check1 = false;
				boolean check2 = false;
				check1 = pc.getInventory().find("오만의탑 3층 이동 부적") == null ? false : true;
				check2 = pc.getInventory().find("오만의 탑 3층 지배 부적") == null ? false : true;
					if(check1 || check2){
						pc.toPotal(32796, 32800, 103);
						}else if(pc.getInventory().isAden("오만의탑 3층 이동 주문서",1, true)){
								pc.toPotal(32796, 32800, 103);
								}else{
									// \f1아데나가 충분치 않습니다.
									ChattingController.toChatting(pc, "오만의탑2층 이동 주문서가 부족합니다", Lineage.CHATTING_MODE_MESSAGE);
								}
				}
			if (action.contains("yadolantel2-yadon4")) {
				boolean check1 = false;
				boolean check2 = false;
				check1 = pc.getInventory().find("오만의탑 4층 이동 부적") == null ? false : true;
				check2 = pc.getInventory().find("오만의 탑 4층 지배 부적") == null ? false : true;
					if(check1 || check2){
						pc.toPotal(32670, 32862, 104);
						}else if(pc.getInventory().isAden("오만의탑 4층 이동 주문서",1, true)){
								pc.toPotal(32670, 32862, 104);
								}else{
									// \f1아데나가 충분치 않습니다.
									ChattingController.toChatting(pc, "오만의탑4층 이동 주문서가 부족합니다", Lineage.CHATTING_MODE_MESSAGE);
								}
				}
			if (action.contains("yadolantel2-yadon5")) {
				boolean check1 = false;
				boolean check2 = false;
				check1 = pc.getInventory().find("오만의탑 5층 이동 부적") == null ? false : true;
				check2 = pc.getInventory().find("오만의 탑 5층 지배 부적") == null ? false : true;
					if(check1 || check2){
						pc.toPotal(32670, 32863, 105);
						}else if(pc.getInventory().isAden("오만의탑 5층 이동 주문서",1, true)){
								pc.toPotal(32670, 32863, 105);
								}else{
									// \f1아데나가 충분치 않습니다.
									ChattingController.toChatting(pc, "오만의탑5층 이동 주문서가 부족합니다", Lineage.CHATTING_MODE_MESSAGE);
								}
				}
			if (action.contains("yadolantel2-yadon6")) {
				boolean check1 = false;
				boolean check2 = false;
				check1 = pc.getInventory().find("오만의탑 6층 이동 부적") == null ? false : true;
				check2 = pc.getInventory().find("오만의 탑 6층 지배 부적") == null ? false : true;
					if(check1 || check2){
						pc.toPotal(32670, 32863, 106);
						}else if(pc.getInventory().isAden("오만의탑 6층 이동 주문서",1, true)){
								pc.toPotal(32670, 32863, 106);
								}else{
									// \f1아데나가 충분치 않습니다.
									ChattingController.toChatting(pc, "오만의탑6층 이동 주문서가 부족합니다", Lineage.CHATTING_MODE_MESSAGE);
								}
				}
			if (action.contains("yadolantel2-yadon7")) {
				boolean check1 = false;
				boolean check2 = false;
				check1 = pc.getInventory().find("오만의탑 7층 이동 부적") == null ? false : true;
				check2 = pc.getInventory().find("오만의 탑 7층 지배 부적") == null ? false : true;
					if(check1 || check2){
						pc.toPotal(32670, 32862, 107);
						}else if(pc.getInventory().isAden("오만의탑 7층 이동 주문서",1, true)){
								pc.toPotal(32670, 32862, 107);
								}else{
									// \f1아데나가 충분치 않습니다.
									ChattingController.toChatting(pc, "오만의탑7층 이동 주문서가 부족합니다", Lineage.CHATTING_MODE_MESSAGE);
								}
				}
			if (action.contains("yadolantel2-yadon8")) {
				boolean check1 = false;
				boolean check2 = false;
				check1 = pc.getInventory().find("오만의탑 8층 이동 부적") == null ? false : true;
				check2 = pc.getInventory().find("오만의 탑 8층 지배 부적") == null ? false : true;
					if(check1 || check2){
						pc.toPotal(32669, 32862, 108);
						}else if(pc.getInventory().isAden("오만의탑 8층 이동 주문서",1, true)){
								pc.toPotal(32669, 32862, 108);
								}else{
									// \f1아데나가 충분치 않습니다.
									ChattingController.toChatting(pc, "오만의탑8층 이동 주문서가 부족합니다", Lineage.CHATTING_MODE_MESSAGE);
								}
				}
			if (action.contains("yadolantel2-yadon9")) {
				boolean check1 = false;
				boolean check2 = false;
				check1 = pc.getInventory().find("오만의탑 9층 이동 부적") == null ? false : true;
				check2 = pc.getInventory().find("오만의 탑 9층 지배 부적") == null ? false : true;
					if(check1 || check2){
						pc.toPotal(32669, 32863, 109);
						}else if(pc.getInventory().isAden("오만의탑 9층 이동 주문서",1, true)){
								pc.toPotal(32669, 32863, 109);
								}else{
									// \f1아데나가 충분치 않습니다.
									ChattingController.toChatting(pc, "오만의탑9층 이동 주문서가 부족합니다", Lineage.CHATTING_MODE_MESSAGE);
								}
				}
			if (action.contains("yadolantel2-yadon10")) {
				boolean check1 = false;
				boolean check2 = false;
				check1 = pc.getInventory().find("오만의탑 10층 이동 부적") == null ? false : true;
				check2 = pc.getInventory().find("오만의 탑 10층 지배 부적") == null ? false : true;
					if(check1 || check2){
						pc.toPotal(32797, 32800, 110);
						}else if(pc.getInventory().isAden("오만의탑 10층 이동 주문서",1, true)){
								pc.toPotal(32797, 32800, 110);
								}else{
									// \f1아데나가 충분치 않습니다.
									ChattingController.toChatting(pc, "오만의탑10층 이동 주문서가 부족합니다", Lineage.CHATTING_MODE_MESSAGE);
								}
				}
	 
	
		showHtml(pc);
	
	}
}