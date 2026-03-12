package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;
import lineage.world.controller.WantedController;

public class Giran_dungeon_Telepoter extends object {

    @Override
    public void toTalk(PcInstance pc, ClientBasePacket cbp){
        String time = null;
        List<String> msg = new ArrayList<String>();
            
        if (Lineage.giran_dungeon_inti_time < 12)
            time = "오전 " + String.valueOf(Lineage.giran_dungeon_inti_time) + "시";
        else
            time = "오후 "+ String.valueOf(Lineage.giran_dungeon_inti_time - 12) + "시";
        
        msg.add(String.valueOf(Lineage.giran_dungeon_time / 3600));
        msg.add(time);
        msg.add(String.format("%s", Lineage.giran_dungeon_level2));
        msg.add(String.format("%s", Lineage.giran_dungeon_level3));
        msg.add(String.format("%s", Lineage.giran_dungeon_level4));

        pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "yadolantel2", null, msg));
    }
    
    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp){
    	
    	// 혈맹 제한: GM 제외, 혈맹이 없거나 신규 혈맹이면 입장 불가
    	if (pc.getGm() == 0 && (pc.getClanId() == 0 || pc.getClanName().equalsIgnoreCase(Lineage.new_clan_name))) {
    	    ChattingController.toChatting(pc, "혈맹이 없거나 신규 혈맹은 입장할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
    	    return;
    	}

        // 수배 조건 체크용 공통 메소드(원하는 경우 메소드로 분리해도 됨)
        // GM인 경우는 패스하도록 하고, 수배 조건이 활성화되어 있을 경우 WantedController를 통해 검사합니다.
        boolean isWantedOk = pc.getGm() > 0 
                                || !Lineage.giran_dungeon_wanted 
                                || (Lineage.giran_dungeon_wanted && WantedController.checkWantedPc(pc));
        if(action.equalsIgnoreCase("teleport giran dungeon")){
            if(pc.getLevel() < Lineage.giran_dungeon_level){
                ChattingController.toChatting(pc, String.format("기란감옥은 %d레벨 이상 입장가능합니다.", Lineage.giran_dungeon_level), Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            
            // 수배 조건 적용
            if(!isWantedOk){
                ChattingController.toChatting(pc, "기란감옥은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            
            if(!Lineage.is_giran_dungeon_time || pc.getGiran_dungeon_time() > 0)
                pc.toPotal(32810, 32731, 53);
            else
                ChattingController.toChatting(pc, "기란감옥 이용시간을 모두 사용하셨습니다.", Lineage.CHATTING_MODE_MESSAGE);
        }
        if(action.equalsIgnoreCase("teleport giran dungeon2")){
            if(pc.getLevel() < Lineage.giran_dungeon_level2){
                ChattingController.toChatting(pc, String.format("기란감옥은 %d레벨 이상 입장가능합니다.", Lineage.giran_dungeon_level2), Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            
            // 수배 조건 적용
            if(!isWantedOk){
                ChattingController.toChatting(pc, "기란감옥은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            
            if(!Lineage.is_giran_dungeon_time || pc.getGiran_dungeon_time() > 0)
                pc.toPotal(32790, 32800, 54);
            else
                ChattingController.toChatting(pc, "기란감옥 이용시간을 모두 사용하셨습니다.", Lineage.CHATTING_MODE_MESSAGE);
        }
        if(action.equalsIgnoreCase("teleport giran dungeon3")){
            if(pc.getLevel() < Lineage.giran_dungeon_level3){
                ChattingController.toChatting(pc, String.format("기란감옥은 %d레벨 이상 입장가능합니다.", Lineage.giran_dungeon_level3), Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            
            // 수배 조건 적용
            if(!isWantedOk){
                ChattingController.toChatting(pc, "기란감옥은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            
            if(!Lineage.is_giran_dungeon_time || pc.getGiran_dungeon_time() > 0)
                pc.toPotal(32736, 32809, 55);
            else
                ChattingController.toChatting(pc, "기란감옥 이용시간을 모두 사용하셨습니다.", Lineage.CHATTING_MODE_MESSAGE);
        }
        if(action.equalsIgnoreCase("teleport giran dungeon4")){
            if(pc.getLevel() < Lineage.giran_dungeon_level4){
                ChattingController.toChatting(pc, String.format("기란감옥은 %d레벨 이상 입장가능합니다.", Lineage.giran_dungeon_level4), Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            
            // 수배 조건 적용
            if(!isWantedOk){
                ChattingController.toChatting(pc, "기란감옥은 수배자만 입장 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
                return;
            }
            
            if(!Lineage.is_giran_dungeon_time || pc.getGiran_dungeon_time() > 0)
                pc.toPotal(32767, 32777, 56);
            else
                ChattingController.toChatting(pc, "기란감옥 이용시간을 모두 사용하셨습니다.", Lineage.CHATTING_MODE_MESSAGE);
        }
    }
}
