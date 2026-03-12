package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.BossSpawn;
import lineage.database.MonsterBossSpawnlistDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class 보스타임 extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		showHtml(pc);
	}
	
	public void showHtml(PcInstance pc){
	
		List<String> bossList = new ArrayList<String>();
		bossList.clear();
		
		for (BossSpawn bossSpawn : MonsterBossSpawnlistDatabase.getSpawnList()) {
			bossList.add(String.format("[%s]", bossSpawn.getMonster()));
			bossList.add(String.format("%s", bossSpawn.getSpawnTime()));
			bossList.add(String.format("요일: %s", bossSpawn.getSpawnDay().trim()));
			bossList.add(" ");
		}
		
		for (int i = 0; i < 150; i++)
			bossList.add(" ");
		
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "bossList", null, bossList));
		
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp){
		
		
		
	
		showHtml(pc);
	
	}
}