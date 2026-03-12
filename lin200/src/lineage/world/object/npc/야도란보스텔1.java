package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.controller.WantedController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 야도란보스텔1 extends object {
	
	// [추가됨] 이동 불가능한 맵 ID 목록 설정
	static public final int TeleportHomeImpossibilityMap[] = { 70, 89, 509, 1400 };

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		showHtml(pc);
	}
	
	public void showHtml(PcInstance pc){
			List<String> ynlist = new ArrayList<String>();	
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "yadolantelbs1", null, ynlist));		
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
		// 혈맹 제한: GM 제외, 혈맹이 없거나 신규 혈맹이면 입장 불가
		if (pc.getGm() == 0 && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
		    ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
		    return;
		}
		// WantedController.checkWantedPc(pc)가 true면 '수배 상태'
				if (!WantedController.checkWantedPc(pc)) {
					ChattingController.toChatting(pc, "수배자가 아니면 이용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
					return; // 여기서 즉시 종료 (아래 이동 코드 실행 안 됨)
				}
		
		// [추가된 로직] 이동 불가능 지역 체크
		for (int cantMap : TeleportHomeImpossibilityMap) {
			if (pc.getMap() == cantMap) {
				ChattingController.toChatting(pc, "이곳에서는 해당 아이템을 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
		}
		
			//흑장로
			if (action.contains("yadolantelbs1-yadon0")) {
				if(pc.getInventory().isAden(50000, true)){
//					pc.toPotal(33433,32814, 4);
                    int rnd = (int)(Math.random() * 3);
					
					switch (rnd) {
					case 0:
						// 첫 번째 좌표 (기존 좌표)
						pc.toPotal(33259, 32415, 4);
						break;
					case 1:
						// 두 번째 좌표 (여기에 원하시는 좌표를 적으세요)
						pc.toPotal(33256, 32395, 4); 
						break;
					case 2:
						// 세 번째 좌표 (여기에 원하시는 좌표를 적으세요)
						pc.toPotal(33283, 32400, 4); 
						break;
					}
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//드레이크
			if (action.contains("yadolantelbs1-yadon1")) {
				if(pc.getInventory().isAden(50000, true)){
//					pc.toPotal(33439,32804, 4);
                    int rnd = (int)(Math.random() * 3);
					
					switch (rnd) {
					case 0:
						// 첫 번째 좌표 (기존 좌표)
						pc.toPotal(33408, 32409, 4);
						break;
					case 1:
						// 두 번째 좌표 (여기에 원하시는 좌표를 적으세요)
						pc.toPotal(33402, 32395, 4); 
						break;
					case 2:
						// 세 번째 좌표 (여기에 원하시는 좌표를 적으세요)
						pc.toPotal(33390, 32417, 4); 
						break;
					}
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//네크로맨서
			if (action.contains("yadolantelbs1-yadon3")) {
				if(pc.getInventory().isAden(50000, true)){
//					pc.toPotal(33437,32819, 4);
                    int rnd = (int)(Math.random() * 3);
					
					switch (rnd) {
					case 0:
						// 첫 번째 좌표 (기존 좌표)
						pc.toPotal(32747, 32786, 12);
						break;
					case 1:
						// 두 번째 좌표 (여기에 원하시는 좌표를 적으세요)
						pc.toPotal(32737, 32775, 12); 
						break;
					case 2:
						// 세 번째 좌표 (여기에 원하시는 좌표를 적으세요)
						pc.toPotal(32726, 32791, 12); 
						break;
					}
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//카스파
			if (action.contains("yadolantelbs1-yadon2")) {
				if(pc.getInventory().isAden(50000, true)){
//					pc.toPotal(33431,32797, 4);
                    int rnd = (int)(Math.random() * 3);
					
					switch (rnd) {
					case 0:
						// 첫 번째 좌표 (기존 좌표)
						pc.toPotal(32771, 32756, 10);
						break;
					case 1:
						// 두 번째 좌표 (여기에 원하시는 좌표를 적으세요)
						pc.toPotal(32753, 32762, 10); 
						break;
					case 2:
						// 세 번째 좌표 (여기에 원하시는 좌표를 적으세요)
						pc.toPotal(32772, 32766, 10); 
						break;
					}
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//피닉스
			if (action.contains("yadolantelbs1-yadon4")) {
				if(pc.getInventory().isAden(50000, true)){
//					pc.toPotal(33447,32794, 4);
                    int rnd = (int)(Math.random() * 3);
					
					switch (rnd) {
					case 0:
						// 첫 번째 좌표 (기존 좌표)
						pc.toPotal(33733, 32270, 4);
						break;
					case 1:
						// 두 번째 좌표 (여기에 원하시는 좌표를 적으세요)
						pc.toPotal(33724, 32255, 4); 
						break;
					case 2:
						// 세 번째 좌표 (여기에 원하시는 좌표를 적으세요)
						pc.toPotal(33741, 32283, 4); 
						break;
					}
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//바포메트
			if (action.contains("yadolantelbs1-yadon5")) {
				if(pc.getInventory().isAden(50000, true)){
//					pc.toPotal(33412,32801, 4);
                    int rnd = (int)(Math.random() * 3);
					
					switch (rnd) {
					case 0:
						// 첫 번째 좌표 (기존 좌표)
						pc.toPotal(32695, 32846, 2);
						break;
					case 1:
						// 두 번째 좌표 (여기에 원하시는 좌표를 적으세요)
						pc.toPotal(32707, 32834, 2); 
						break;
					case 2:
						// 세 번째 좌표 (여기에 원하시는 좌표를 적으세요)
						pc.toPotal(32706, 32861, 2); 
						break;
					}
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//얼음여왕
			if (action.contains("yadolantelbs1-yadon6")) {
				if(pc.getInventory().isAden(50000, true)){
//					pc.toPotal(33443,32822, 4);
                    int rnd = (int)(Math.random() * 3);
					
					switch (rnd) {
					case 0:
						// 첫 번째 좌표 (기존 좌표)
						pc.toPotal(32784, 32898, 74);
						break;
					case 1:
						// 두 번째 좌표 (여기에 원하시는 좌표를 적으세요)
						pc.toPotal(32798, 32880, 74); 
						break;
					case 2:
						// 세 번째 좌표 (여기에 원하시는 좌표를 적으세요)
						pc.toPotal(32793, 32915, 74); 
						break;
					}
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			
			//데스나이트
			if (action.contains("yadolantelbs1-yadon7")) {
				if(pc.getInventory().isAden(50000, true)){
//					pc.toPotal(33428,32821, 4);
	                   int rnd = (int)(Math.random() * 3);
						
						switch (rnd) {
						case 0:
							// 첫 번째 좌표 (기존 좌표)
							pc.toPotal(32774, 32786, 13);
							break;
						case 1:
							// 두 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(32727, 32735, 13); 
							break;
						case 2:
							// 세 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(32804, 32751, 13); 
							break;
						}
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			
			//거대 여왕개미
			if (action.contains("yadolantelbs1-yadon8")) {
				if(pc.getInventory().isAden(50000, true)){
//					pc.toPotal(33445,32815, 4);
	                   int rnd = (int)(Math.random() * 3);
						
						switch (rnd) {
						case 0:
							// 첫 번째 좌표 (기존 좌표)
							pc.toPotal(32728, 32805, 51);
							break;
						case 1:
							// 두 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(32753, 32795, 51); 
							break;
						case 2:
							// 세 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(32716, 32811, 51); 
							break;
						}
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//커츠
			if (action.contains("yadolantelbs1-yadon9")) {
				if(pc.getInventory().isAden(50000, true)){
//					pc.toPotal(33421,32797, 4);
	                   int rnd = (int)(Math.random() * 3);
						
						switch (rnd) {
						case 0:
							// 첫 번째 좌표 (기존 좌표)
							pc.toPotal(32665, 32843, 0);
							break;
						case 1:
							// 두 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(32658, 32872, 0); 
							break;
						case 2:
							// 세 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(32696, 32846, 0); 
							break;
						}
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}	
			//거인모닝스타
			if (action.contains("yadolantelbs1-yadon11")) {
				if(pc.getInventory().isAden(0, true)){
//					pc.toPotal(33421,32797, 4);
	                   int rnd = (int)(Math.random() * 3);
						
						switch (rnd) {
						case 0:
							// 첫 번째 좌표 (기존 좌표)
							pc.toPotal(34257, 33402, 4);
							break;
						case 1:
							// 두 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(34232, 33387, 4); 
							break;
						case 2:
							// 세 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(34227, 33421, 4); 
							break;
						}
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}	
			//데몬
			if (action.contains("yadolantelbs1-yadon12")) {
				if(pc.getInventory().isAden(0, true)){
//					pc.toPotal(33421,32797, 4);
	                   int rnd = (int)(Math.random() * 3);
						
						switch (rnd) {
						case 0:
							// 첫 번째 좌표 (기존 좌표)
							pc.toPotal(32685, 32812, 82);
							break;
						case 1:
							// 두 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(32710, 32828, 82); 
							break;
						case 2:
							// 세 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(32691, 32843, 82); 
							break;
						}
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//그미노
			if (action.contains("yadolantelbs1-yadon13")) {
				if(pc.getInventory().isAden(0, true)){
//					pc.toPotal(33421,32797, 4);
	                   int rnd = (int)(Math.random() * 3);
						
						switch (rnd) {
						case 0:
							// 첫 번째 좌표 (기존 좌표)
							pc.toPotal(32653, 32763, 70);
							break;
						case 1:
							// 두 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(32710, 32828, 82); 
							break;
						case 2:
							// 세 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(32691, 32843, 82); 
							break;
						}
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}	
			//테베제단
			if (action.contains("yadolantelbs1-yadon14")) {
				if(pc.getInventory().isAden(0, true)){
//					pc.toPotal(33421,32797, 4);
	                   int rnd = (int)(Math.random() * 3);
						
						switch (rnd) {
						case 0:
							// 첫 번째 좌표 (기존 좌표)
							pc.toPotal(32768, 32813, 782);
							break;
						case 1:
							// 두 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(32745, 32831, 782); 
							break;
						case 2:
							// 세 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(32768, 32850, 782); 
							break;
						}
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}
			//심연의 주인
			if (action.contains("yadolantelbs1-yadon15")) {
				if(pc.getInventory().isAden(0, true)){
//					pc.toPotal(33421,32797, 4);
	                   int rnd = (int)(Math.random() * 3);
						
						switch (rnd) {
						case 0:
							// 첫 번째 좌표 (기존 좌표)
							pc.toPotal(32819, 32794, 430);
							break;
						case 1:
							// 두 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(32804, 32814, 430); 
							break;
						case 2:
							// 세 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(32839, 32816, 430); 
							break;
						}
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}	
			//설벽의 드레이크
			if (action.contains("yadolantelbs1-yadon16")) {
				if(pc.getInventory().isAden(0, true)){
//					pc.toPotal(33421,32797, 4);
	                   int rnd = (int)(Math.random() * 3);
						
						switch (rnd) {
						case 0:
							// 첫 번째 좌표 (기존 좌표)
							pc.toPotal(34251, 32278, 4);
							break;
						case 1:
							// 두 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(34255, 32312, 4); 
							break;
						case 2:
							// 세 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(34260, 32301, 4); 
							break;
						}
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}	
			//분노한 발록
			if (action.contains("yadolantelbs1-yadon17")) {
				if(pc.getInventory().isAden(0, true)){
//					pc.toPotal(33421,32797, 4);
	                   int rnd = (int)(Math.random() * 3);
						
						switch (rnd) {
						case 0:
							// 첫 번째 좌표 (기존 좌표)
							pc.toPotal(32720, 32868, 524);
							break;
						case 1:
							// 두 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(32690, 32895, 524); 
							break;
						case 2:
							// 세 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(32720, 32925, 524); 
							break;
						}
				}else{
					// \f1아데나가 충분치 않습니다.
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				}
			}	
			//안타라스
			if (action.contains("yadolantelbs1-yadon18")) {
				if(pc.getInventory().isAden(0, true)){
//					pc.toPotal(33421,32797, 4);
	                   int rnd = (int)(Math.random() * 3);
						
						switch (rnd) {
						case 0:
							// 첫 번째 좌표 (기존 좌표)
							pc.toPotal(32718, 32799, 37);
							break;
						case 1:
							// 두 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(32730, 32835, 37); 
							break;
						case 2:
							// 세 번째 좌표 (여기에 원하시는 좌표를 적으세요)
							pc.toPotal(32742, 32818, 37); 
							break;
						}
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
