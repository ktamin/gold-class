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
import lineage.world.controller.고라스컨트롤러;
import lineage.world.controller.고무컨트롤러;
import lineage.world.controller.그신컨트롤러;
import lineage.world.controller.기감컨트롤러;
import lineage.world.controller.드워프컨트롤러;
import lineage.world.controller.라바던전컨트롤러;
import lineage.world.controller.마족신전컨트롤러;
import lineage.world.controller.악마왕의영토컨트롤러;
import lineage.world.controller.얼던컨트롤러;
import lineage.world.controller.정무컨트롤러;
import lineage.world.controller.지옥컨트롤러;
import lineage.world.controller.지하수로컨트롤러;
import lineage.world.controller.칠흑던전컨트롤러;
import lineage.world.controller.테베라스컨트롤러;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class 야도란텔5 extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		showHtml(pc);
	}
	
	public void showHtml(PcInstance pc){
			List<String> ynlist = new ArrayList<String>();
			
			ynlist.add(String.format("%s", 지옥컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
			ynlist.add(String.format("%s", 기감컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
			ynlist.add(String.format("%s", 마족신전컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
			ynlist.add(String.format("%s", 악마왕의영토컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
			ynlist.add(String.format("%s", 테베라스컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
			ynlist.add(String.format("%s", 얼던컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
			ynlist.add(String.format("%s", 칠흑던전컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
			ynlist.add(String.format("%s", 지하수로컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
			ynlist.add(String.format("%s", 라바던전컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
			ynlist.add(String.format("%s", 고무컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
			ynlist.add(String.format("%s", 정무컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
			ynlist.add(String.format("%s", 그신컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
			ynlist.add(String.format("%s", 고라스컨트롤러.isOpen ? "[열림]" : "[닫힘]"));
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "yadolantel5", null, ynlist));
		
	}	
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp){
		if (action.equalsIgnoreCase("yadolan_teleport"))

		
		if (Lineage.open_wait) {
			ChattingController.toChatting(pc, "[오픈대기] 오픈대기에는 이동 하실수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}		
		if (pc.isDead() || pc.isLock() || pc.isFishing()){
			ChattingController.toChatting(pc, "현재 상태에선 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
		
			return;
		}
		
		// ---- 테베 던전(yadon1) ----
		if (action.contains("yadolantel5-yadon1")) {
		    if (pc.getGm() == 0 && !테베라스컨트롤러.isOpen) {
		        ChattingController.toChatting(pc, "현재는 사냥터가 닫혀 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
		        return;
		    }
		    if (pc.getGm() == 0 && pc.getLevel() < Lineage.tebe_level) {
		        ChattingController.toChatting(pc,
		            String.format("테베 던전은 %d레벨 이상 입장 가능합니다.", Lineage.tebe_level),
		            Lineage.CHATTING_MODE_MESSAGE);
		        return;
		    }
		    if (pc.getGm() == 0 && Lineage.tebe_wanted && !WantedController.checkWantedPc(pc)) {
		        ChattingController.toChatting(pc, "테베 던전은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
		        return;
		    }
		    if (pc.getGm() == 0
		        && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
		        ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
		        return;
		    }
		    if (pc.getGm() == 0 && Lineage.tebe_clan && pc.getClanId() == 0) {
		        ChattingController.toChatting(pc, "테베 던전은 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
		        return;
		    }
		    if (pc.getGm() == 0 && !pc.getInventory().isAden("아데나", Lineage.go_tebe, true)) {
		        ChattingController.toChatting(pc, "테베 던전 입장 재료가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
		        return;
		    }
		    pc.toPotal(Util.random(32742, 32742), Util.random(32803, 32797), 781);
		    return;
		}
		
        // ---- 기란 감옥(yadon12) ----
        if (action.contains("yadolantel5-yadon2")) {
            if (pc.getGm() == 0 && !기감컨트롤러.isOpen) {
                ChattingController.toChatting(pc, "현재는 사냥터가 닫혀 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (pc.getGm() == 0 && pc.getLevel() < Lineage.prison_level) {
                ChattingController.toChatting(pc,
                    String.format("기란 감옥은 %d레벨 이상 입장 가능합니다.", Lineage.prison_level),
                    Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (pc.getGm() == 0 && Lineage.prison_wanted && !WantedController.checkWantedPc(pc)) {
                ChattingController.toChatting(pc, "기란 감옥은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (pc.getGm() == 0
                && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
                ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (pc.getGm() == 0 && Lineage.prison_clan && pc.getClanId() == 0) {
                ChattingController.toChatting(pc, "기란 감옥은 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (pc.getGm() == 0 && !pc.getInventory().isAden("아데나", Lineage.go_prison, true)) {
                ChattingController.toChatting(pc, "기란 감옥 입장 재료가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            pc.toPotal(Util.random(32764, 32771), Util.random(32767, 32768), 56);
            return;
            }
        
        // ---- 얼음 던전(yadon3) ----
        if (action.contains("yadolantel5-yadon3")) {
            if (pc.getGm() == 0 && !얼던컨트롤러.isOpen) {
                ChattingController.toChatting(pc, "현재는 사냥터가 닫혀 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (pc.getGm() == 0 && pc.getLevel() < Lineage.ice_level) {
                ChattingController.toChatting(pc,
                    String.format("얼음 던전은 %d레벨 이상 입장 가능합니다.", Lineage.ice_level),
                    Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (pc.getGm() == 0 && Lineage.ice_wanted && !WantedController.checkWantedPc(pc)) {
                ChattingController.toChatting(pc, "얼음 던전은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (pc.getGm() == 0
                && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
                ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (pc.getGm() == 0 && Lineage.ice_clan && pc.getClanId() == 0) {
                ChattingController.toChatting(pc, "얼음 던전은 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (pc.getGm() == 0 && !pc.getInventory().isAden("아데나", Lineage.go_ice, true)) {
                ChattingController.toChatting(pc, "얼음 던전 입장 재료가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            pc.toPotal(Util.random(32783, 32785), Util.random(32898, 32901), 74);
            return;
        }
        
        // ---- 악마왕의 영토(yadon4) ----
        if (action.contains("yadolantel5-yadon4")) {
            if (pc.getGm() == 0 && !악마왕의영토컨트롤러.isOpen) {
                ChattingController.toChatting(pc, "현재는 사냥터가 닫혀 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (pc.getGm() == 0 && pc.getLevel() < Lineage.devil_level) {
                ChattingController.toChatting(pc,
                    String.format("악마왕의 영토는 %d레벨 이상 입장 가능합니다.", Lineage.devil_level),
                    Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (pc.getGm() == 0 && Lineage.devil_wanted && !WantedController.checkWantedPc(pc)) {
                ChattingController.toChatting(pc, "악마왕의 영토는 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (pc.getGm() == 0
                && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
                ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (pc.getGm() == 0 && Lineage.devil_clan && pc.getClanId() == 0) {
                ChattingController.toChatting(pc, "악마왕의 영토는 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            if (pc.getGm() == 0 && !pc.getInventory().isAden("아데나", Lineage.go_devil, true)) {
                ChattingController.toChatting(pc, "악마왕의 영토 입장 재료가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            pc.toPotal(Util.random(32721, 32725), Util.random(32796, 32803), 5167);
            return;
        }
        
        // ---- 정령의 무덤(yadon5) ----
        if (action.contains("yadolantel5-yadon5")) {
           // 1) 오픈 여부
           if (pc.getGm() == 0 && !정무컨트롤러.isOpen) {
               ChattingController.toChatting(pc, "현재는 사냥터가 닫혀 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
               return;
           }
           // 2) 레벨
           if (pc.getGm() == 0 && pc.getLevel() < Lineage.jungmu_level) {
               ChattingController.toChatting(pc,
                   String.format("정령의 무덤은 %d레벨 이상 입장 가능합니다.", Lineage.jungmu_level),
                   Lineage.CHATTING_MODE_MESSAGE);
               return;
           }
           // 3) 수배자
           if (pc.getGm() == 0 && Lineage.jungmu_wanted && !WantedController.checkWantedPc(pc)) {
               ChattingController.toChatting(pc, "정령의 무덤은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
               return;
           }
           // 4) 혈맹 가입
           if (pc.getGm() == 0
               && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
               ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
               return;
           }
           // 5) 혈맹 조건
           if (pc.getGm() == 0 && Lineage.jungmu_clan && pc.getClanId() == 0) {
               ChattingController.toChatting(pc, "정령의 무덤은 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
               return;
           }
           // 6) 아데나
           if (pc.getGm() == 0 && !pc.getInventory().isAden("아데나", Lineage.go_jungmu, true)) {
               ChattingController.toChatting(pc, "정령의 무덤 입장 재료가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
               return;
           }
           // 이동
           pc.toPotal(Util.random(32926, 32925), Util.random(32796, 32805), 430);
           return;
        }

	       // ---- 지하 던전(yadon6) ----
     if (action.contains("yadolantel5-yadon6")) {
         // 1) 오픈 여부
         if (pc.getGm() == 0 && !지하수로컨트롤러.isOpen) {
             ChattingController.toChatting(pc, "현재는 사냥터가 닫혀 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
             return;
         }
         // 2) 레벨
         if (pc.getGm() == 0 && pc.getLevel() < Lineage.wg_level) {
             ChattingController.toChatting(pc,
                 String.format("지하수로는 %d레벨 이상 입장 가능합니다.", Lineage.wg_level),
                 Lineage.CHATTING_MODE_MESSAGE);
             return;
         }
         // 3) 수배자
         if (pc.getGm() == 0 && Lineage.wg_wanted && !WantedController.checkWantedPc(pc)) {
             ChattingController.toChatting(pc, "지하수로는 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
             return;
         }
         // 4) 혈맹 가입
         if (pc.getGm() == 0
             && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
             ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
             return;
         }
         // 5) 혈맹 조건
         if (pc.getGm() == 0 && Lineage.wg_clan && pc.getClanId() == 0) {
             ChattingController.toChatting(pc, "지하수로는 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
             return;
         }
         // 6) 아데나
         if (pc.getGm() == 0 && !pc.getInventory().isAden("아데나", Lineage.go_wg, true)) {
             ChattingController.toChatting(pc, "지하수로 입장 재료가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
             return;
         }
         // 이동
         pc.toPotal(Util.random(32665, 32671), Util.random(32853, 32857), 85);
         return;
     }
     
     // ---- 지옥 던전(yadon7) ----
     if (action.contains("yadolantel5-yadon7")) {
         if (pc.getGm() == 0 && !지옥컨트롤러.isOpen) {
             ChattingController.toChatting(pc, "현재는 사냥터가 닫혀 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
             return;
         }
         if (pc.getGm() == 0 && pc.getLevel() < Lineage.hell_level) {
             ChattingController.toChatting(pc,
                 String.format("지옥 던전은 %d레벨 이상 입장 가능합니다.", Lineage.hell_level),
                 Lineage.CHATTING_MODE_MESSAGE);
             return;
         }
         if (pc.getGm() == 0 && Lineage.hell_wanted && !WantedController.checkWantedPc(pc)) {
             ChattingController.toChatting(pc, "지옥 던전은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
             return;
         }
         if (pc.getGm() == 0
             && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
             ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
             return;
         }
         if (pc.getGm() == 0 && Lineage.hell_clan && pc.getClanId() == 0) {
             ChattingController.toChatting(pc, "지옥 던전은 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
             return;
         }
         if (pc.getGm() == 0 && !pc.getInventory().isAden("아데나", Lineage.go_hell, true)) {
             ChattingController.toChatting(pc, "지옥 던전 입장 재료가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
             return;
         }
         pc.toPotal(Util.random(32743, 32737), Util.random(32799, 32794), 666);
         return;
     }
     
    // ---- 고대 거인의 무덤(yadon8) ----
    if (action.contains("yadolantel5-yadon8")) {
   // 1) 오픈 여부
     if (pc.getGm() == 0 && !고무컨트롤러.isOpen) {
         ChattingController.toChatting(pc, "현재는 사냥터가 닫혀 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
         return;
     }
   // 2) 레벨
     if (pc.getGm() == 0 && pc.getLevel() < Lineage.gomu_level) {
         ChattingController.toChatting(pc,
           String.format("고대거인의 무덤은 %d레벨 이상 입장 가능합니다.", Lineage.gomu_level),
           Lineage.CHATTING_MODE_MESSAGE);
       return;
     }
   // 3) 수배자
     if (pc.getGm() == 0 && Lineage.gomu_wanted && !WantedController.checkWantedPc(pc)) {
       ChattingController.toChatting(pc, "고대거인의 무덤은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
       return;
     }
   // 4) 혈맹 가입
     if (pc.getGm() == 0
       && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
       ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
       return;
     }
   // 5) 혈맹 조건
     if (pc.getGm() == 0 && Lineage.gomu_clan && pc.getClanId() == 0) {
       ChattingController.toChatting(pc, "고대거인의 무덤은 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
       return;
     }
   // 6) 아데나
     if (pc.getGm() == 0 && !pc.getInventory().isAden("아데나", Lineage.go_wg, true)) {
       ChattingController.toChatting(pc, "고대거인의 무덤 입장 재료가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
       return;
     }
   // 이동
       pc.toPotal(Util.random(32766, 32770), Util.random(32894, 32901), 400);
       return;
}


// ---- 마족 신전(yadon9) ----
if (action.contains("yadolantel5-yadon9")) {
    if (pc.getGm() == 0 && !마족신전컨트롤러.isOpen) {
        ChattingController.toChatting(pc, "현재는 사냥터가 닫혀 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
        return;
    }
    if (pc.getGm() == 0 && pc.getLevel() < Lineage.dete_level) {
        ChattingController.toChatting(pc,
            String.format("마족 신전은 %d레벨 이상 입장 가능합니다.", Lineage.dete_level),
            Lineage.CHATTING_MODE_MESSAGE);
        return;
    }
    if (pc.getGm() == 0 && Lineage.dete_wanted && !WantedController.checkWantedPc(pc)) {
        ChattingController.toChatting(pc, "마족 신전은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
        return;
    }
    if (pc.getGm() == 0
        && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
        ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
        return;
    }
    if (pc.getGm() == 0 && Lineage.dete_clan && pc.getClanId() == 0) {
        ChattingController.toChatting(pc, "마족 신전은 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
        return;
    }
    if (pc.getGm() == 0 && !pc.getInventory().isAden("아데나", Lineage.go_dete, true)) {
        ChattingController.toChatting(pc, "마족 신전 입장 재료가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
        return;
    }
    pc.toPotal(Util.random(32929, 32926), Util.random(32990, 32997), 410);
    return;
}
      
//---- 그림자 신전(yadon10) ----
if (action.contains("yadolantel5-yadon0")) {
//1) 오픈 여부
if (pc.getGm() == 0 && !그신컨트롤러.isOpen) {
ChattingController.toChatting(pc, "현재는 사냥터가 닫혀 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
return;
}
//2) 레벨
if (pc.getGm() == 0 && pc.getLevel() < Lineage.shadow_level) {
ChattingController.toChatting(pc,
    String.format("그림자 신전은 %d레벨 이상 입장 가능합니다.", Lineage.shadow_level),
    Lineage.CHATTING_MODE_MESSAGE);
return;
}
//3) 수배자
if (pc.getGm() == 0 && Lineage.shadow_wanted && !WantedController.checkWantedPc(pc)) {
ChattingController.toChatting(pc, "그림자 신전은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
return;
}
//4) 혈맹 가입
if (pc.getGm() == 0
&& (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
return;
}
//5) 혈맹 조건
if (pc.getGm() == 0 && Lineage.shadow_clan && pc.getClanId() == 0) {
ChattingController.toChatting(pc, "그림자 신전은 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
return;
}
//6) 아데나
if (pc.getGm() == 0 && !pc.getInventory().isAden("아데나", Lineage.go_shadow, true)) {
ChattingController.toChatting(pc, "그림자 신전 입장 재료가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
return;
}
//이동
pc.toPotal(Util.random(32660, 32659), Util.random(32889, 32897), 523);
return;
}

	       // ---- 칠흑 던전(yadon5) ----
        if (action.contains("yadolantel4-yadon1")) {
            // 1) 오픈 여부
            if (pc.getGm() == 0 && !칠흑던전컨트롤러.isOpen) {
                ChattingController.toChatting(pc, "현재는 사냥터가 닫혀 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            // 2) 레벨
            if (pc.getGm() == 0 && pc.getLevel() < Lineage.dark_level) {
                ChattingController.toChatting(pc,
                    String.format("칠흑 던전은 %d레벨 이상 입장 가능합니다.", Lineage.dark_level),
                    Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            // 3) 수배자
            if (pc.getGm() == 0 && Lineage.dark_wanted && !WantedController.checkWantedPc(pc)) {
                ChattingController.toChatting(pc, "칠흑 던전은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            // 4) 혈맹 가입
            if (pc.getGm() == 0
                && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
                ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            // 5) 혈맹 조건
            if (pc.getGm() == 0 && Lineage.dark_clan && pc.getClanId() == 0) {
                ChattingController.toChatting(pc, "칠흑 던전은 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            // 6) 아데나
            if (pc.getGm() == 0 && !pc.getInventory().isAden("아데나", Lineage.go_dark, true)) {
                ChattingController.toChatting(pc, "칠흑 던전 입장 재료가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            // 이동
            pc.toPotal(Util.random(32810, 32813), Util.random(32722, 32725), 811);
            return;
        }
        
	       // ---- 라바 던전(yadon6) ----
     if (action.contains("yadolantel4-yadon2")) {
         // 1) 오픈 여부
         if (pc.getGm() == 0 && !라바던전컨트롤러.isOpen) {
             ChattingController.toChatting(pc, "현재는 사냥터가 닫혀 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
             return;
         }
         // 2) 레벨
         if (pc.getGm() == 0 && pc.getLevel() < Lineage.lasta_level) {
             ChattingController.toChatting(pc,
                 String.format("라스타바드 던전은 %d레벨 이상 입장 가능합니다.", Lineage.lasta_level),
                 Lineage.CHATTING_MODE_MESSAGE);
             return;
         }
         // 3) 수배자
         if (pc.getGm() == 0 && Lineage.lasta_wanted && !WantedController.checkWantedPc(pc)) {
             ChattingController.toChatting(pc, "라스타바드 던전은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
             return;
         }
         // 4) 혈맹 가입
         if (pc.getGm() == 0
             && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
             ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
             return;
         }
         // 5) 혈맹 조건
         if (pc.getGm() == 0 && Lineage.lasta_clan && pc.getClanId() == 0) {
             ChattingController.toChatting(pc, "라스타바드 던전은 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
             return;
         }
         // 6) 아데나
         if (pc.getGm() == 0 && !pc.getInventory().isAden("아데나", Lineage.go_lasta, true)) {
             ChattingController.toChatting(pc, "라스타바드 던전 입장 재료가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
             return;
         }
         // 이동
         pc.toPotal(Util.random(32726, 32726), Util.random(32822, 32828), 452);
         return;
     }
     
     // ---- 고라스 던전(yadon6) ----
if (action.contains("yadolantel4-yadon3")) {
   // 1) 오픈 여부
   if (pc.getGm() == 0 && !고라스컨트롤러.isOpen) {
       ChattingController.toChatting(pc, "현재는 사냥터가 닫혀 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
       return;
   }
   // 2) 레벨
   if (pc.getGm() == 0 && pc.getLevel() < Lineage.goras_level) {
       ChattingController.toChatting(pc,
           String.format("고라스 던전은 %d레벨 이상 입장 가능합니다.", Lineage.goras_level),
           Lineage.CHATTING_MODE_MESSAGE);
       return;
   }
   // 3) 수배자
   if (pc.getGm() == 0 && Lineage.goras_wanted && !WantedController.checkWantedPc(pc)) {
       ChattingController.toChatting(pc, "고라스 던전은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
       return;
   }
   // 4) 혈맹 가입
   if (pc.getGm() == 0
       && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
       ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
       return;
   }
   // 5) 혈맹 조건
   if (pc.getGm() == 0 && Lineage.goras_clan && pc.getClanId() == 0) {
       ChattingController.toChatting(pc, "고라스 던전은 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
       return;
   }
   // 6) 아데나
   if (pc.getGm() == 0 && !pc.getInventory().isAden("아데나", Lineage.go_goras, true)) {
       ChattingController.toChatting(pc, "고라스 던전 입장 재료가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
       return;
   }
   // 이동
   pc.toPotal(Util.random(32730, 32733), Util.random(32828, 32835), 2004);
   return;
}

// ---- 고라스 던전(yadon6) ----
if (action.contains("yadolantel4-yadon4")) {
// 1) 오픈 여부
if (pc.getGm() == 0 && !드워프컨트롤러.isOpen) {
  ChattingController.toChatting(pc, "현재는 사냥터가 닫혀 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
  return;
}
// 2) 레벨
if (pc.getGm() == 0 && pc.getLevel() < Lineage.dwarf_level) {
  ChattingController.toChatting(pc,
      String.format("드워프 광산은 %d레벨 이상 입장 가능합니다.", Lineage.goras_level),
      Lineage.CHATTING_MODE_MESSAGE);
  return;
}
// 3) 수배자
if (pc.getGm() == 0 && Lineage.dwarf_wanted && !WantedController.checkWantedPc(pc)) {
  ChattingController.toChatting(pc, "드워프 광산은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
  return;
}
// 4) 혈맹 가입
if (pc.getGm() == 0
  && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
  ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
  return;
}
// 5) 혈맹 조건
if (pc.getGm() == 0 && Lineage.dwarf_clan && pc.getClanId() == 0) {
  ChattingController.toChatting(pc, "드워프 광산은 혈맹 가입자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
  return;
}
// 6) 아데나
if (pc.getGm() == 0 && !pc.getInventory().isAden("아데나", Lineage.go_dwarf, true)) {
  ChattingController.toChatting(pc, "드워프 광산 입장 재료가 부족합니다.", Lineage.CHATTING_MODE_MESSAGE);
  return;
}
// 이동
pc.toPotal(Util.random(32679, 32680), Util.random(32794, 32801), 15450);
return;
}
		showHtml(pc);
	}
}