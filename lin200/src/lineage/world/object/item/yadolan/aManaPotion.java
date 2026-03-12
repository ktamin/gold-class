package lineage.world.object.item.yadolan;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class aManaPotion extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new aManaPotion();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
            PcInstance pc = (PcInstance) cha;
		if( !isClick(cha) )
			return;
               /* long time = lineage.share.System.currentTimeMillis();
                //int timeString = Util.getSeconds(time);
                if (pc.getLastLackTime2() < time) {
			pc.setLastLackTime2(time + 300000);
		} else {
			ChattingController.toChatting(pc, String.format("\\fR마나회복제 딜레이는 5분 입니다."), Lineage.CHATTING_MODE_MESSAGE);
			ChattingController.toChatting(pc, String.format("%d초가 남았습니다.", (pc.getLastLackTime2() - time) / 1000), Lineage.CHATTING_MODE_MESSAGE);
                        return;
		}*/
		// 이팩트 표현
		cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, getItem().getEffect()), true);
		// 메세지 표현
		cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 818));
		// 마나 상승
		cha.setNowMp( cha.getNowMp()+Util.random(getItem().getSmallDmg(), getItem().getBigDmg()) );
		// 아이템 수량 갱신
		cha.getInventory().count(this, getCount()-1, true);
	}
	
}
