package lineage.world.object.item.weapon;

import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;

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
