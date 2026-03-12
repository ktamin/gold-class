/*
교환원하는 재료가없을때
rrafons6
*/

package lineage.world.object.npc.craft;

import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.instance.CraftInstance;
import lineage.world.object.instance.PcInstance;

public class Rafons extends CraftInstance {

	public Rafons(Npc npc){
		super(npc);
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "rrafons"));
	}

}

/*@Override
public void Talk(PcInstance cha, String text1, String text2){
	ItemInstance item = cha.getInventory().getItemIdType(675, 0);
	switch(text1.charAt(0)){
		case 'A':	// 1만아데나
			if(item==null){
				if(cha.getInventory().Aden(10000, true)){
					item = Items.getInstance().newItem(675, 0, false);
					item.setCookbookLv(1);
					Items.getInstance().ChangeNameId(item);
					cha.getInventory().Controler(item, Config.ITEM_ADD, 1);
					cha.SendPacket(new ShowHtmlPacket(get_objectId(), "rrafons1"), false);
				}else{
					cha.SendPacket(new ShowHtmlPacket(get_objectId(), "rrafons2"), false);
				}
			}
			break;
		case 'B':	// 3천아데나
			if(item!=null){
				if(item.getCookbookLv()==1){
					if(cha.getInventory().Aden(3000, true)){
						item.setCookbookLv(2);
						Items.getInstance().ChangeNameId(item);
						cha.SendPacket(new InventoryStatus(item), false);
						cha.SendPacket(new ShowHtmlPacket(get_objectId(), "rrafons4"), false);
					}else{
						cha.SendPacket(new ShowHtmlPacket(get_objectId(), "rrafons2"), false);
					}
				}else if(item.getCookbookLv()==2){
					cha.SendPacket(new ShowHtmlPacket(get_objectId(), "rrafons3"), false);
				}
			}else{
				cha.SendPacket(new ShowHtmlPacket(get_objectId(), "rrafons5"), false);
			}
			break;
		case 'a':	// 괴물눈 스테이크
			rrafons(cha, 677, 1);
			break;
		case 'b':	// 곰 구이
			rrafons(cha, 678, 1);
			break;
		case 'c':	// 씨호떡
			rrafons(cha, 679, 1);
			break;
		case 'd':	// 개미다리치즈
			rrafons(cha, 680, 1);
			break;
		case 'e':	// 과일샐러드
			rrafons(cha, 681, 1);
			break;
		case 'f':	// 과일탕수육
			rrafons(cha, 682, 1);
			break;
		case 'g':	// 멧돼지 꼬치구이
			rrafons(cha, 683, 1);
			break;
		case 'h':	// 버섯 스프
			rrafons(cha, 684, 1);
			break;
		case 'i':	// 괴물눈 스테이크
			rrafons(cha, 677, 10);
			break;
		case 'j':	// 곰 구이
			rrafons(cha, 678, 10);
			break;
		case 'k':	// 씨호떡
			rrafons(cha, 679, 10);
			break;
		case 'l':	// 개미다리치즈
			rrafons(cha, 680, 10);
			break;
		case 'm':	// 과일샐러드
			rrafons(cha, 681, 10);
			break;
		case 'n':	// 과일탕수육
			rrafons(cha, 682, 10);
			break;
		case 'o':	// 멧돼지 꼬치구이
			rrafons(cha, 683, 10);
			break;
		case 'p':	// 버섯 스프
			rrafons(cha, 684, 10);
			break;
		default:
			cha.SendPacket(new ShowHtmlPacket(get_objectId(), "rrafons6"), false);
			break;
	}
}

private void rrafons(PcInstance cha, int id, int count){
	ItemInstance item = cha.getInventory().getItemIdType(id, 0);
	if(item != null && item.get_count()>=count){
		cha.getInventory().Controler(item, Config.ITEM_REMOVE, count);
		cha.getInventory().Controler(Items.getInstance().newItem(854, 0, false), Config.ITEM_ADD, count);
		cha.SendPacket(new ShowHtmlPacket(get_objectId(), "rrafons7"), false);
	}else{
		cha.SendPacket(new ShowHtmlPacket(get_objectId(), "rrafons6"), false);
	}
}*/