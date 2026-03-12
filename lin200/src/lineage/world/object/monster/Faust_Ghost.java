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
		if (nowHp <= 0 && Math.random() < Lineage_Balance.faust_spawn_probability && !BossController.isSpawn("파우스트", getMap())) {
			Monster monster = MonsterDatabase.find("파우스트");

			if (monster != null && MonsterSpawnlistDatabase.toSpawnMonster(monster, x, y, map, heading, true, this)) {
				return;
			}
		}

		super.setNowHp(nowHp);
	}

}
