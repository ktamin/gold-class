package lineage.world.object.item.weapon;

import all_night.Lineage_Balance;
import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.PcInstance;

public class Edoryu extends ItemWeaponInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Edoryu();
		return item;
	}
	
	@Override
	public boolean toDamage(Character cha, object o){
		
		// 1) 부모 로직 먼저 실행 → DB 기반 아이템 스킬 발동 시도
		boolean triggeredByParent = super.toDamage(cha, o);
		
		// 2) 이도류 고유의 더블 데미지 발동 확률 (20%)
		boolean triggeredByEdoryu = Util.random(0, 100) < 10;
		
		// ==========================================
				// ✅ 쉐도우 스턴 패시브 발동 (디버그 제거 완료)
				// ==========================================
				if (cha instanceof PcInstance && cha.getClassType() == Lineage.LINEAGE_CLASS_DARKELF) {
					
					// 1. '쉐도우 스턴' 아이템 소지 확인
					if (cha.getInventory().find("쉐도우 스턴", 0, 1) != null) {
						
						// 2. 발동 확률 계산 (Lineage_Balance.shadow_stun_prob 연동)
						if (Math.random() < Lineage_Balance.shadow_stun_prob) {
							
							// 3. 스킬 DB 번호 확인 (16번)
							Skill stunSkill = lineage.database.SkillDatabase.find(16); 
							
							// 4. 스턴 마법 정상 호출
							if (stunSkill != null) {
								lineage.world.object.magic.ShockStun.init(cha, stunSkill, o);
							}
						}
					}
				}
				
				return triggeredByParent || triggeredByEdoryu;
			}

	@Override
	public int toDamage(int dmg){
		int parent = super.toDamage(dmg);
		int extra = 0;
		return parent + extra;
	}
	
	@Override
	public int toDamageEffect(){
		int parentFx = super.toDamageEffect();
		return (parentFx != 0) ? parentFx : 3398;
	}
}
/*
	@Override
	public boolean toDamage(Character cha, object o){
		
	    // 1) 부모 로직 먼저 실행 → DB 기반 아이템 스킬 발동 시도
	    boolean triggeredByParent = super.toDamage(cha, o);
	    
	    boolean triggeredByEdoryu = Util.random(0, 100) < 20;
	    
	    return triggeredByParent || triggeredByEdoryu;
	    
//		return Util.random(0, 100)<20;
	}

	@Override
	public int toDamage(int dmg){
		
	    // 부모가 계산한 skill_dmg 을 살리기 위해 super 사용
	    int parent = super.toDamage(dmg);
	    int extra = 0;

	    // (선택) 이도류 전용 추가 데미지 예시
	    // if (원하는 조건) extra = 10;

	    return parent + extra;
//		return dmg;
	}
	
	@Override
	public int toDamageEffect(){
		
	    // 부모가 정한 이펙트 보존
	    int parentFx = super.toDamageEffect();

	    // (선택) 이도류 전용 이펙트로 덮어쓰고 싶으면 조건부로 교체
	     return (parentFx != 0) ? parentFx : 3398;

	    // 부모 효과를 그대로 쓰려면:
//	    return parentFx;
	    
//		return 3398;
	}
}
*/