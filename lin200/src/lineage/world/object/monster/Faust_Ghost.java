package lineage.world.object.monster;

import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI;
import all_night.Lineage_Balance;
import lineage.bean.database.Monster;
import lineage.database.MonsterDatabase;
import lineage.database.MonsterSpawnlistDatabase;
import lineage.world.World;
import lineage.world.controller.BossController;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public class Faust_Ghost extends MonsterInstance {
	
	static synchronized public MonsterInstance clone(MonsterInstance mi, Monster m){
		if(mi == null)
			mi = new Faust_Ghost();
		
		return MonsterInstance.clone(mi, m);
	}
	
	@Override
	public void setNowHp(int nowHp) {
		// 몬스터의 체력이 0 이하(사망)가 되었을 때
		if (nowHp <= 0) {
			
			// =========================================================
			// 💡 [수정] 현재 죽은 맵의 파우스트 스위치가 켜져 있는지 확인합니다.
			// =========================================================
			boolean isMapActive = false;
			if (getMap() == 53 && Lineage_Balance.faust_map_53_active) isMapActive = true;
			else if (getMap() == 54 && Lineage_Balance.faust_map_54_active) isMapActive = true;
			else if (getMap() == 55 && Lineage_Balance.faust_map_55_active) isMapActive = true;
			
			// 1) 맵 스위치가 켜져 있고
			// 2) 확률에 당첨되었으며
			// 3) 파우스트가 아직 해당 맵에 스폰되지 않았다면
			if (isMapActive && Math.random() < Lineage_Balance.faust_spawn_probability && !BossController.isSpawn("파우스트", getMap())) {
				Monster monster = MonsterDatabase.find("파우스트");

				if (monster != null && MonsterSpawnlistDatabase.toSpawnMonster(monster, x, y, map, heading, true, this)) {
					return;
				}
			}
		}

		super.setNowHp(nowHp);
	}
/*	
	@Override
	public void setNowHp(int nowHp) {
		if (nowHp <= 0 && Math.random() < Lineage_Balance.faust_spawn_probability && !BossController.isSpawn("파우스트", getMap())) {
			Monster monster = MonsterDatabase.find("파우스트");

			if (monster != null && MonsterSpawnlistDatabase.toSpawnMonster(monster, x, y, map, heading, true, this)) {
				return;
			}
		}

		super.setNowHp(nowHp);
	}
*/
}
