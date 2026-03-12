package lineage.world.object.item;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.ItemBundle;
import lineage.database.ItemBundleDatabase;
import lineage.database.ItemDatabase;
import lineage.database.ItemDropMessageDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class Bundle extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new Bundle();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (isLvCheck(cha)) {
			if (cha.getInventory() != null && cha.getInventory().getList().size() >= Lineage.inventory_max) {
				ChattingController.toChatting(cha, "인벤토리가 가득찼습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			if( item.getName().equalsIgnoreCase("신화 기술서 (임페리얼 아머)") ){
				if(cha.getInventory().find("임페리얼 아머") != null){
					ChattingController.toChatting(cha, String.format("이미 습득 하였습니다."), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(cha.getClassType() != Lineage.LINEAGE_CLASS_ROYAL ){
					ChattingController.toChatting(cha, String.format("군주 클래스만 사용 가능합니다."), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
			}
			if( item.getName().equalsIgnoreCase("신화 마법서 (세인트 이뮨)") ){
				if(cha.getInventory().find("세인트 이뮨 투함") != null){
					ChattingController.toChatting(cha, String.format("이미 습득 하였습니다."), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(cha.getClassType() != Lineage.LINEAGE_CLASS_WIZARD ){
					ChattingController.toChatting(cha, String.format("마법사 클래스만 사용 가능합니다."), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
			}
			if( item.getName().equalsIgnoreCase("신화 기술서 (바운스 어택)")){
				if(cha.getInventory().find("바운스 어택") != null){
					ChattingController.toChatting(cha, String.format("이미 습득 하였습니다."), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(cha.getClassType() != Lineage.LINEAGE_CLASS_KNIGHT){
					ChattingController.toChatting(cha, String.format("기사 클래스만 사용 가능합니다."), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
			}
			if( item.getName().equalsIgnoreCase("신화 정령서 (부스트)")){
				if(cha.getInventory().find("트리플 애로우(부스트)") != null){
					ChattingController.toChatting(cha, String.format("이미 습득 하였습니다."), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(cha.getClassType() != Lineage.LINEAGE_CLASS_ELF ){
					ChattingController.toChatting(cha, String.format("요정 클래스만 사용 가능합니다."), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
			}
			if( item.getName().equalsIgnoreCase("신화 기술서(포스스턴)")){
				if(cha.getInventory().find("포스스턴") != null){
					ChattingController.toChatting(cha, String.format("이미 습득 하였습니다."), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(cha.getClassType() != Lineage.LINEAGE_CLASS_KNIGHT){
					ChattingController.toChatting(cha, String.format("기사 클래스만 사용 가능합니다."), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
			}
			if( item.getName().equalsIgnoreCase("신화 기술서(엑스칼리버)")){
				if(cha.getInventory().find("엑스칼리버") != null){
					ChattingController.toChatting(cha, String.format("이미 습득 하였습니다."), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(cha.getClassType() != Lineage.LINEAGE_CLASS_ROYAL ){
					ChattingController.toChatting(cha, String.format("군주 클래스만 사용 가능합니다."), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
			}
			if( item.getName().equalsIgnoreCase("신화 마법서(디스에이션트)")){
				
				if(cha.getInventory().find("디스(에이션트)") != null){
					ChattingController.toChatting(cha, String.format("이미 습득 하였습니다."), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				
				if(cha.getClassType() != Lineage.LINEAGE_CLASS_WIZARD ){
					ChattingController.toChatting(cha, String.format("마법사 클래스만 사용 가능합니다."), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
			}
			if( item.getName().equalsIgnoreCase("신화 정령서(엘리멘탈샷)")){
				
	
				if(cha.getInventory().find("엘리멘탈샷") != null){
					ChattingController.toChatting(cha, String.format("이미 습득 하였습니다."), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				if(cha.getClassType() != Lineage.LINEAGE_CLASS_ELF ){
					ChattingController.toChatting(cha, String.format("요정 클래스만 사용 가능합니다."), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
			}
			// 아이템 지급.
			List<ItemBundle> list = new ArrayList<ItemBundle>();
			ItemBundleDatabase.find(list, getItem().getName());

			if (list.size() < 1)
				return;

			for (ItemBundle ib : list) {
				if (ib.getItemCountMin() > 0) {
					Item i = ItemDatabase.find(ib.getItem());

					if (i != null) {
						ItemInstance temp = cha.getInventory().find(i.getName(), ib.getItemBless(), i.isPiles());
						int count = Util.random(ib.getItemCountMin(), ib.getItemCountMax());

						if (temp != null && (temp.getBless() != ib.getItemBless() || temp.getEnLevel() != ib.getItemEnchant()))
							temp = null;

						if (temp == null) {
							// 겹칠수 있는 아이템이 존재하지 않을경우.
							if (i.isPiles()) {
								temp = ItemDatabase.newInstance(i);
								temp.setObjectId(ServerDatabase.nextItemObjId());
								temp.setBless(ib.getItemBless());
								temp.setEnLevel(ib.getItemEnchant());
								temp.setCount(count);
								temp.setDefinite(true);
								cha.getInventory().append(temp, true);
							} else {
								for (int idx = 0; idx < count; idx++) {
									temp = ItemDatabase.newInstance(i);
									temp.setObjectId(ServerDatabase.nextItemObjId());
									temp.setBless(ib.getItemBless());
									temp.setEnLevel(ib.getItemEnchant());
									temp.setDefinite(true);
									cha.getInventory().append(temp, true);
								}
							}
						} else {
							// 겹치는 아이템이 존재할 경우.
							cha.getInventory().count(temp, temp.getCount() + count, true);
						}

						if (Lineage.is_item_drop_msg_item && i != null && this != null && getItem() != null) {
							ItemDropMessageDatabase.sendMessage(cha, i.getName(), getItem().getName());
						}
						
						if(item.getEffect() > 0)
							cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, item.getEffect()), true);
						
						// 알림.
						ChattingController.toChatting(cha, String.format("%s(%d) 획득: %s", i.getName(), count, getItem().getName()), Lineage.CHATTING_MODE_MESSAGE);
					}
				}
			}

			// 수량 하향.
			cha.getInventory().count(this, getCount() - 1, true);
		}
	}

}
