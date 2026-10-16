package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.database.AccountDatabase;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;

import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class AttendanceCheck extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		showHtml(pc);
	}
	
	public void showHtml(PcInstance pc){
		
		// 💡 [수정] 신규 유저(0)일 경우 1일차 화면이 보이도록 보정
				if (pc.getDaycount() == 0) {
					pc.setDaycount(1);
				}
				
			List<String> ynlist = new ArrayList<String>();

			for(int i= 0;i<3;i++){
				
			}
			ynlist.add(String.format("%d",  pc.getDayptime() ));
			ynlist.add(String.format("%d", pc.getDaycount() ));
			
		
		
			switch (pc.getDaycount()) {
			
	            case 1:    
	              	ynlist.add(String.format("%s",Lineage.dayc1));
	            	ynlist.add(String.format("%s개",Lineage.daycc1));
					break;
	            case 2:  
	              	ynlist.add(String.format("%s",Lineage.dayc2));
	            	ynlist.add(String.format("%s개",Lineage.daycc2));
		            	break;
	            case 3:  
	              	ynlist.add(String.format("%s",Lineage.dayc3));
	            	ynlist.add(String.format("%s개",Lineage.daycc3));
	            		break;
	            case 4:  
	              	ynlist.add(String.format("%s",Lineage.dayc4));
	            	ynlist.add(String.format("%s개",Lineage.daycc4));
	  
	              break;
	            case 5:  
	              	ynlist.add(String.format("%s",Lineage.dayc5));
	            	ynlist.add(String.format("%s개",Lineage.daycc5));
	  
	              break;
	            case 6:  
	              	ynlist.add(String.format("%s",Lineage.dayc6));
	            	ynlist.add(String.format("%s개",Lineage.daycc6));
	  
	              break;
	            case 7:  
	              	ynlist.add(String.format("%s",Lineage.dayc7));
	            	ynlist.add(String.format("%s개",Lineage.daycc7));
	  
	              break;
	            case 8:  
	              	ynlist.add(String.format("%s",Lineage.dayc8));
	            	ynlist.add(String.format("%s개",Lineage.daycc8));
	  
	              break;
	            case 9:  
	              	ynlist.add(String.format("%s",Lineage.dayc9));
	            	ynlist.add(String.format("%s개",Lineage.daycc9));
	  
	              break;
	            case 10:  
	              	ynlist.add(String.format("%s",Lineage.dayc10));
	            	ynlist.add(String.format("%s개",Lineage.daycc10));
	  
	              break;
	            case 11:  
	              	ynlist.add(String.format("%s",Lineage.dayc11));
	            	ynlist.add(String.format("%s개",Lineage.daycc11));
	  
	              break;
	            case 12:  
	              	ynlist.add(String.format("%s",Lineage.dayc12));
	            	ynlist.add(String.format("%s개",Lineage.daycc12));
	  
	              break;
	            case 13:  
	              	ynlist.add(String.format("%s",Lineage.dayc13));
	            	ynlist.add(String.format("%s개",Lineage.daycc13));
	  
	              break;
	            case 14:  
	              	ynlist.add(String.format("%s",Lineage.dayc14));
	            	ynlist.add(String.format("%s개",Lineage.daycc14));
	  
	              break;
	            case 15:  
	              	ynlist.add(String.format("%s",Lineage.dayc15));
	            	ynlist.add(String.format("%s개",Lineage.daycc15));
	  
	              break;
	            case 16:  
	              	ynlist.add(String.format("%s",Lineage.dayc16));
	            	ynlist.add(String.format("%s개",Lineage.daycc16));
	  
	              break;
	            case 17:  
	              	ynlist.add(String.format("%s",Lineage.dayc17));
	            	ynlist.add(String.format("%s개",Lineage.daycc17));
	  
	              break;
	            case 18:  
	              	ynlist.add(String.format("%s",Lineage.dayc18));
	            	ynlist.add(String.format("%s개",Lineage.daycc18));
	  
	              break;
	            case 19:  
	              	ynlist.add(String.format("%s",Lineage.dayc19));
	            	ynlist.add(String.format("%s개",Lineage.daycc19));
	  
	              break;
	            case 20:  
	              	ynlist.add(String.format("%s",Lineage.dayc20));
	            	ynlist.add(String.format("%s개",Lineage.daycc20));
	  
	              break;
	            case 21:  
	              	ynlist.add(String.format("%s",Lineage.dayc21));
	            	ynlist.add(String.format("%s개",Lineage.daycc21));
	  
	              break;
	            case 22:  
	              	ynlist.add(String.format("%s",Lineage.dayc22));
	            	ynlist.add(String.format("%s개",Lineage.daycc22));
	  
	              break;
	            case 23:  
	              	ynlist.add(String.format("%s",Lineage.dayc23));
	            	ynlist.add(String.format("%s개",Lineage.daycc23));
	  
	              break;
	            case 24:  
	              	ynlist.add(String.format("%s",Lineage.dayc24));
	            	ynlist.add(String.format("%s개",Lineage.daycc24));
	  
	              break;
	            case 25:  
	              	ynlist.add(String.format("%s",Lineage.dayc25));
	            	ynlist.add(String.format("%s개",Lineage.daycc25));
	  
	              break;
	            case 26:  
	              	ynlist.add(String.format("%s",Lineage.dayc26));
	            	ynlist.add(String.format("%s개",Lineage.daycc26));
	  
	              break;
	            case 27:  
	              	ynlist.add(String.format("%s",Lineage.dayc27));
	            	ynlist.add(String.format("%s개",Lineage.daycc27));
	  
	              break;
	            case 28:  
	              	ynlist.add(String.format("%s",Lineage.dayc28));
	            	ynlist.add(String.format("%s개",Lineage.daycc28));
	  
	              break;
	            case 29:  
	              	ynlist.add(String.format("%s",Lineage.dayc29));
	            	ynlist.add(String.format("%s개",Lineage.daycc29));
	  
	              break;
	            case 30:  
	              	ynlist.add(String.format("%s",Lineage.dayc30));
	            	ynlist.add(String.format("%s개",Lineage.daycc30));
	  
	              break;
	            default: 
	              break;
        }
			ynlist.add(String.format("출석 여부 : %s", pc.getDaycheck() == 0? "[X]" : "[O]"));
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "playcheck", null, ynlist));
		
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp){

		if (action.equalsIgnoreCase("playcheck-checkp")) {
			
			if(pc.getDaycheck() == 1){
				ChattingController.toChatting(pc, " \\fY 오늘은 이미 출석체크를 하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getDayptime()>= Lineage.dayc){
				if(pc.getDaycheck() == 0 ){
					
					// 💡 [수정] 신규 유저(0)일 경우 1일차 보상을 받도록 보정
					if (pc.getDaycount() == 0) {
						pc.setDaycount(1);
					}

					if(pc.getDaycount() == Lineage.lastday ){
						ChattingController.toChatting(pc, " \\fY 출석체크를 전부 완료 하였습니다..", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
		
					switch (pc.getDaycount()) {  
		            case 1:  
						giveDailyRewards(pc, Lineage.dayc1, Lineage.daycc1);
		            	break;
		            case 2:  
						giveDailyRewards(pc, Lineage.dayc2, Lineage.daycc2);
		            	break;
		            case 3:  
						giveDailyRewards(pc, Lineage.dayc3, Lineage.daycc3);
		            	break;
		            case 4:  
						giveDailyRewards(pc, Lineage.dayc4, Lineage.daycc4);
		            	break;
		            case 5:  
						giveDailyRewards(pc, Lineage.dayc5, Lineage.daycc5);
		            	break;
		            case 6:  
						giveDailyRewards(pc, Lineage.dayc6, Lineage.daycc6);
		            	break;
		            case 7:  
						giveDailyRewards(pc, Lineage.dayc7, Lineage.daycc7);
		            	break;
		            case 8:  
						giveDailyRewards(pc, Lineage.dayc8, Lineage.daycc8);
		            	break;
		            case 9:  
						giveDailyRewards(pc, Lineage.dayc9, Lineage.daycc9);
		            	break; 
		            case 10:  
						giveDailyRewards(pc, Lineage.dayc10, Lineage.daycc10);
		            	break;  
		            case 11:  
						giveDailyRewards(pc, Lineage.dayc11, Lineage.daycc11);
		            	break;
		            case 12:  
						giveDailyRewards(pc, Lineage.dayc12, Lineage.daycc12);
		            	break;
		            case 13:  
						giveDailyRewards(pc, Lineage.dayc13, Lineage.daycc13);
		            	break;
		            case 14:  
						giveDailyRewards(pc, Lineage.dayc14, Lineage.daycc14);
		            	break;
		            case 15:  
						giveDailyRewards(pc, Lineage.dayc15, Lineage.daycc15);
		            	break;
		            case 16:  
						giveDailyRewards(pc, Lineage.dayc16, Lineage.daycc16);
		            	break;
		            case 17:  
						giveDailyRewards(pc, Lineage.dayc17, Lineage.daycc17);
		            	break;   
		            case 18:  
						giveDailyRewards(pc, Lineage.dayc18, Lineage.daycc18);
		            	break; 
		            case 19:  
						giveDailyRewards(pc, Lineage.dayc19, Lineage.daycc19);
		            	break;   
		            case 20:  
						giveDailyRewards(pc, Lineage.dayc20, Lineage.daycc20);
		            	break; 
		            case 21:  
						giveDailyRewards(pc, Lineage.dayc21, Lineage.daycc21);
		            	break;  
		            case 22:  
						giveDailyRewards(pc, Lineage.dayc22, Lineage.daycc22);
		            	break; 
		            case 23:  
						giveDailyRewards(pc, Lineage.dayc23, Lineage.daycc23);
		            	break; 
		            case 24:  
						giveDailyRewards(pc, Lineage.dayc24, Lineage.daycc24);
		            	break;
		            case 25:  
						giveDailyRewards(pc, Lineage.dayc25, Lineage.daycc25);
		            	break; 
		            case 26:  
						giveDailyRewards(pc, Lineage.dayc26, Lineage.daycc26);
		            	break;
		            case 27:  
						giveDailyRewards(pc, Lineage.dayc27, Lineage.daycc27);
		            	break; 
		            case 28:  
						giveDailyRewards(pc, Lineage.dayc28, Lineage.daycc28);
		            	break;
		            case 29:  
						giveDailyRewards(pc, Lineage.dayc29, Lineage.daycc29);
		            	break;
		            case 30:  
						giveDailyRewards(pc, Lineage.dayc30, Lineage.daycc30);
		            	break;
			            default: 
			              break;
					}
					pc.setDaycheck(1);
					pc.setDaycount(pc.getDaycount()+1);
					AccountDatabase.updateDaycp(1,pc.getAccountUid());
					AccountDatabase.updateDaycount(pc.getDaycount(),pc.getAccountUid());
					AccountDatabase.updateDaycheck(pc.getAccountUid());
					ChattingController.toChatting(pc, String.format("\\fY %d일차 출석체크 완료. ", pc.getDaycount()-1), Lineage.CHATTING_MODE_MESSAGE);
					ChattingController.toChatting(pc, String.format("\\fY %d일차 보상이 지급되었습니다. ", pc.getDaycount()-1), Lineage.CHATTING_MODE_MESSAGE);
				}
			}else{
				ChattingController.toChatting(pc, String.format("\\fY %d초후 출석이 가능합니다. ", Lineage.dayc - pc.getDayptime() ), Lineage.CHATTING_MODE_MESSAGE);
			}
		}	
		showHtml(pc);
	}
	private void giveDailyRewards(PcInstance pc, String itemsStr, String countsStr) {
		try {
			String[] items = itemsStr.split(",");
			String[] counts = countsStr.split(",");

			for (int i = 0; i < items.length; i++) {
				String itemName = items[i].trim(); 
				int count = Integer.parseInt(counts[i].trim());
				
				int blessState = 1; 

				if (itemName.startsWith("축복받은 ")) {
					blessState = 0; 
					itemName = itemName.replace("축복받은 ", "").trim(); 
				} else if (itemName.startsWith("저주받은 ")) {
					blessState = 2; 
					itemName = itemName.replace("저주받은 ", "").trim();
				}

				ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find(itemName));
				
				if (ii != null) {
					ii.setCount(count);
					ii.setBless(blessState); // 서버팩에 따라 setBless 로 수정 필요할 수 있음					
					// 💡 [추가] 아이템을 '확인(Identified)' 상태로 만듭니다.
					ii.setDefinite(true);					
					pc.toGiveItem(null, ii, ii.getCount());
				} else {
					System.out.println("[출석 보상 에러] DB에 없는 아이템: " + itemName);
				}
			}
		} catch (Exception e) {
			System.out.println("[출석 보상 에러] 콘프 설정에 콤마(,) 개수나 오타가 있는지 확인하세요.");
		}
	}
}