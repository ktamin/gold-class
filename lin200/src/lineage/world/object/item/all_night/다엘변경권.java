package lineage.world.object.item.all_night;

import lineage.database.CharactersDatabase;
import lineage.network.LineageServer;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Disconnect;
import lineage.share.Lineage;
import lineage.world.controller.CharacterController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 다엘변경권 extends ItemInstance {

    static synchronized public ItemInstance clone(ItemInstance item) {
        if (item == null) {
            item = new 다엘변경권();
        }
        return item;
    }
    
    @Override
    public void toClick(Character cha, ClientBasePacket cbp) {
        // 1. 방어 로직: 사용자가 PC(플레이어)가 아니면 실행하지 않음
        if (!(cha instanceof PcInstance)) {
            return;
        }
        
        PcInstance pc = (PcInstance) cha;
        
        // 💡 [추가] 레벨 제한 로직: 53레벨 이상은 사용 불가 (52레벨까지만 허용)
        if (pc.getLevel() >= 53) {
            ChattingController.toChatting(pc, "해당 아이템은 52레벨 이하만 사용할 수 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
            return; // 로직을 여기서 멈추고 아이템 사용을 취소합니다.
        }
        
        long objId = pc.getObjectId();
        int sex = pc.getClassSex();
        int classGfx;

        // 2. 성별에 따른 다크엘프 외형(Gfx) 판별 로직 최적화
        if (sex == 0) { // 남성
            classGfx = Lineage.darkelf_male_gfx;
        } else { // 여성
            classGfx = Lineage.darkelf_female_gfx;
        }

        // 3. 클래스 및 외형 데이터 갱신
        pc.setClassType(Lineage.LINEAGE_CLASS_DARKELF);
        pc.setGfx(classGfx);
        pc.setClassGfx(classGfx);

        // 4. 착용 중인 장비 모두 강제 해제 (클래스 변경 시 장착 불가 버그 방지)
        if (pc.getInventory() != null) {
            for (ItemInstance item : pc.getInventory().getList()) {
                if (item != null && item.isEquipped()) {
                    item.toClick(pc, null);
                }
            }
            
            // 5. 오류가 발생하는 removeItem 대신 서버팩 기본 방식인 count 적용
            pc.getInventory().count(this, getCount() - 1, true);
        }

        // 6. 다크엘프에 맞게 기본 스탯 초기화
        CharacterController.toResetStat(pc, Lineage.LINEAGE_CLASS_DARKELF);
        
        // =================================================================
        // 7. [추가] 변경된 다크엘프 클래스 기준으로 HP/MP 전면 재조정
        // =================================================================
        int hp = Lineage.darkelf_hp; // 다크엘프 기본 시작 HP
        int mp = Lineage.darkelf_mp; // 다크엘프 기본 시작 MP
        
        for (int i = 2; i <= pc.getLevel(); i++) {
            // 💡 50레벨 이하는 기본 성장(BaseUP), 51레벨부터는 스탯 성장(UP) 적용
            if (i <= 50) {
                hp += CharacterController.toStatusBaseUP(pc, true);
                mp += CharacterController.toStatusBaseUP(pc, false);
            } else {
                hp += CharacterController.toStatusUP(pc, true);
                mp += CharacterController.toStatusUP(pc, false);
            }
        }

        pc.setMaxHp(hp);
        pc.setNowHp(hp);
        pc.setMaxMp(mp);
        pc.setNowMp(mp);
        // =================================================================
        
        // 8. 데이터베이스에 변경된 클래스 정보(classType = 4) 저장
        CharactersDatabase.classChange(objId, sex, 4, classGfx);    
        
        // 9. 딜레이 없이 즉시 강제 종료하여 메모리 초기화 및 변경 내역 저장
        pc.toSender(S_Disconnect.clone(BasePacketPooling.getPool(S_Disconnect.class), 0x0A));
        LineageServer.close(pc.getClient());
    }
}