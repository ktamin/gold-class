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
import lineage.world.object.object;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 야도란텔 extends object {

	static public final int TeleportHomeImpossibilityMap[] = { 70, 89, 509, 707, 809, 810, 811, 1400 };

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		showHtml(pc);
	}
	
	public void showHtml(PcInstance pc){
		List<String> ynlist = new ArrayList<String>();
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "yadolantel", null, ynlist));
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp){
		
		if(pc.isAutoHunt){
			pc.isAutoHunt = false;
			pc.autohunt_target = null;
			pc.is_auto_return_home = false;
		}
		
		if (Lineage.open_wait) {
			ChattingController.toChatting(pc, "[오픈대기] 오픈대기에는 이동 하실수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}		
		if (pc.isDead() || pc.isLock() || pc.isFishing()){
			ChattingController.toChatting(pc, "현재 상태에선 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		// 이동 불가능 지역 체크
		for (int cantMap : TeleportHomeImpossibilityMap) {
			if (pc.getMap() == cantMap) {
				ChattingController.toChatting(pc, "이곳에서는 해당 아이템을 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
		}
		
		// -----------------------------------
		// 1. 일반 사냥터 (무료)
		// -----------------------------------
		if (action.equals("yadolantel-freefield-01")) { pc.toPotal(32807, 32729, 19); return; }
		if (action.equals("yadolantel-freefield-02")) { pc.toPotal(32668, 32806, 1); return; }
		if (action.equals("yadolantel-freefield-03")) { pc.toPotal(32781, 32736, 43); return; }
		if (action.equals("yadolantel-freefield-04")) { pc.toPotal(32710, 32789, 59); return; }
		if (action.equals("yadolantel-freefield-05")) { pc.toPotal(32746, 32790, 72); return; }
		if (action.equals("yadolantel-freefield-06")) { pc.toPotal(32806, 32811, 25); return; }
		if (action.equals("yadolantel-freefield-07")) { pc.toPotal(32810, 32730, 7); return; }
		if (action.equals("yadolantel-freefield-08")) { pc.toPotal(32892, 32773, 78); return; }
		if (action.equals("yadolantel-freefield-09")) { pc.toPotal(32723, 32728, 30); return; }

		// -----------------------------------
		// 2. 중급 사냥터 (2만 아데나)
		// -----------------------------------
		if (action.equals("yadolantel-lowfield-00")) { teleport(pc, 20000, 32765, 32798, 20); return; }
		if (action.equals("yadolantel-lowfield-01")) { teleport(pc, 20000, 32680, 32797, 2); return; }
		if (action.equals("yadolantel-lowfield-02")) { teleport(pc, 20000, 32668, 32738, 51); return; }
		if (action.equals("yadolantel-lowfield-03")) { teleport(pc, 20000, 32727, 32808, 61); return; }
		if (action.equals("yadolantel-lowfield-04")) { teleport(pc, 20000, 32752, 32874, 73); return; }
		if (action.equals("yadolantel-lowfield-05")) { teleport(pc, 20000, 32807, 32811, 27); return; }
		if (action.equals("yadolantel-lowfield-06")) { teleport(pc, 20000, 32728, 32727, 11); return; }
		if (action.equals("yadolantel-lowfield-07")) { teleport(pc, 20000, 32763, 32851, 81); return; }
		if (action.equals("yadolantel-lowfield-08")) { teleport(pc, 20000, 32669, 32869, 33); return; }

		// -----------------------------------
		// 3. 하급 보스 (1만 아데나, 랜덤)
		// -----------------------------------
		if (action.equals("yadolantel-littleboss-00")) { randomTeleport(pc, 10000, new int[][]{{33403,32396,4},{33387,32419,4},{33399,32408,4}}); return; }
		if (action.equals("yadolantel-littleboss-01")) { randomTeleport(pc, 10000, new int[][]{{33660,32321,4},{33651,32347,4},{33684,32348,4}}); return; }
		if (action.equals("yadolantel-littleboss-02")) { randomTeleport(pc, 10000, new int[][]{{32752,32777,12},{32750,32787,12},{32725,32791,12}}); return; }
		if (action.equals("yadolantel-littleboss-03")) { randomTeleport(pc, 10000, new int[][]{{32754,32761,10},{32780,32768,10},{32775,32774,10}}); return; }

		// -----------------------------------
		// 4. 중급 보스 (2만 아데나, 랜덤)
		// -----------------------------------
		if (action.equals("yadolantel-lowboss-00")) { randomTeleport(pc, 20000, new int[][]{{33254,32395,4},{33274,32400,4},{33269,32422,4}}); return; }
		if (action.equals("yadolantel-lowboss-01")) { randomTeleport(pc, 20000, new int[][]{{32707,32834,2},{32707,32860,2},{32693,32846,2}}); return; }
		if (action.equals("yadolantel-lowboss-02")) { randomTeleport(pc, 20000, new int[][]{{32783,32898,74},{32794,32877,74},{32791,32917,74}}); return; }
		if (action.equals("yadolantel-lowboss-03")) { randomTeleport(pc, 20000, new int[][]{{33724,32247,4},{33719,32281,4},{33742,32284,4}}); return; }
		if (action.equals("yadolantel-lowboss-04")) { randomTeleport(pc, 20000, new int[][]{{32752,32795,51},{32726,32818,51},{32718,32803,51}}); return; }
		if (action.equals("yadolantel-lowboss-05")) { randomTeleport(pc, 20000, new int[][]{{34257,33388,4},{34233,33386,4},{34225,33421,4}}); return; }
		if (action.equals("yadolantel-lowboss-06")) { randomTeleport(pc, 20000, new int[][]{{32680,32838,0},{32655,32850,0},{32655,32873,0}}); return; }
		if (action.equals("yadolantel-lowboss-07")) { randomTeleport(pc, 20000, new int[][]{{32700,32808,82},{32676,32825,82},{32690,32844,82}}); return; }
		if (action.equals("yadolantel-lowboss-08")) { randomTeleport(pc, 20000, new int[][]{{32806,32803,11},{32811,32808,11},{32784,32810,11}}); return; }

		// -----------------------------------
		// 5. 상급 보스 (3만 아데나, 랜덤)
		// -----------------------------------
		if (action.equals("yadolantel-midboss-00")) { randomTeleport(pc, 30000, new int[][]{{32809,32810,430},{32821,32828,430},{32792,32845,430}}); return; }
		if (action.equals("yadolantel-midboss-01")) { randomTeleport(pc, 30000, new int[][]{{32779,32810,782},{32755,32832,782},{32771,32852,782}}); return; }
		if (action.equals("yadolantel-midboss-02")) { randomTeleport(pc, 30000, new int[][]{{32779,32810,782},{32755,32832,782},{32771,32852,782}}); return; }
		if (action.equals("yadolantel-midboss-03")) { randomTeleport(pc, 30000, new int[][]{{32778,32739,12},{32796,32733,12},{32805,32745,12}}); return; }
		if (action.equals("yadolantel-midboss-04")) { randomTeleport(pc, 30000, new int[][]{{32920,32792,410},{32903,32819,410},{32887,32792,410}}); return; }
		if (action.equals("yadolantel-midboss-05")) { randomTeleport(pc, 30000, new int[][]{{32719,32910,524},{32710,32895,524},{32718,32881,524}}); return; }
		if (action.equals("yadolantel-midboss-06")) { randomTeleport(pc, 30000, new int[][]{{32778,32820,452},{32763,32838,452},{32777,32855,452}}); return; }

		// -----------------------------------
		// 6. 최상급 보스 (3만 아데나, 랜덤)
		// -----------------------------------
		if (action.equals("yadolantel-highboss-00")) { randomTeleport(pc, 50000, new int[][]{{32708,32903,200},{32676,32903,200},{32692,32922,200}}); return; }
		if (action.equals("yadolantel-highboss-01")) { randomTeleport(pc, 50000, new int[][]{{34251,32282,4},{34249,32299,4},{34259,32314,4}}); return; }
		if (action.equals("yadolantel-highboss-02")) { randomTeleport(pc, 50000, new int[][]{{32717,32793,37},{32728,32819,37},{32724,32804,37}}); return; }
		if (action.equals("yadolantel-highboss-03")) { randomTeleport(pc, 50000, new int[][]{{32770,32805,65},{32767,32832,65},{32801,32831,65}}); return; }
		if (action.equals("yadolantel-highboss-04")) { randomTeleport(pc, 50000, new int[][]{{34020,32997,4},{34011,33032,4},{34040,33028,4}}); return; }
		if (action.equals("yadolantel-highboss-05")) { randomTeleport(pc, 50000, new int[][]{{32735,32860,67},{32726,32842,67},{32693,32852,67}}); return; }
		if (action.equals("yadolantel-highboss-06")) { randomTeleport(pc, 50000, new int[][]{{32763,32794,13},{32786,32787,13},{32777,32792,13}}); return; }

		// 알 수 없는 액션이면 아무 일도 하지 않고 HTML 창만 유지합니다.
		showHtml(pc);
	}
	
	// 일반 이동용 도우미
	private void teleport(PcInstance pc, int reqAden, int x, int y, int map) {
		if (pc.getInventory().isAden(reqAden, true)) {
			pc.toPotal(x, y, map);
		} else {
			pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189)); // 아데나 부족
		}
	}
	
	// 랜덤 배열 이동용 도우미
	private void randomTeleport(PcInstance pc, int reqAden, int[][] locations) {
		if (pc.getInventory().isAden(reqAden, true)) {
			int randomIndex = Util.random(0, locations.length - 1);
			pc.toPotal(locations[randomIndex][0], locations[randomIndex][1], locations[randomIndex][2]);
		} else {
			pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189)); // 아데나 부족
		}
	}

	public static ItemArmorInstance clone(ItemInstance pool) {
		return null;
	}
}