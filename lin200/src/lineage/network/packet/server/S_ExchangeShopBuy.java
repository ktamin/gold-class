package lineage.network.packet.server;

import java.util.List;

import lineage.bean.database.ExchangeItem;
import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.share.Lineage;
import lineage.util.Util;

public class S_ExchangeShopBuy extends S_Inventory {

	// 💡 1. int npcObjId -> long npcObjId 로 변경
		static synchronized public BasePacket clone(BasePacket bp, long npcObjId, List<ExchangeItem> list) {
			if (bp == null)
				bp = new S_ExchangeShopBuy(npcObjId, list);
			else
				((S_ExchangeShopBuy) bp).toClone(npcObjId, list);
			return bp;
		}

		// 💡 2. int npcObjId -> long npcObjId 로 변경
		public S_ExchangeShopBuy(long npcObjId, List<ExchangeItem> list) {
			toClone(npcObjId, list);
		}

		// 💡 3. int npcObjId -> long npcObjId 로 변경
		public void toClone(long npcObjId, List<ExchangeItem> list) {
			clear();

			// 클라이언트에게 "상점 창을 띄워라"라는 명령(Opcode)
			writeC(Opcodes.S_OPCODE_SHOPBUY);
			writeD((int) npcObjId); // 💡 4. 클라이언트로 전송할 때는 (int)로 형변환
			writeH(list.size());

		for (ExchangeItem eItem : list) {
			// 아이템 데이터베이스에서 원본 아이템 정보를 가져옴 (그래픽, 무게 등)
			Item item = ItemDatabase.find(eItem.getItemName());
			if (item == null) continue;

			// 유저가 아이템을 구매할 때 서버로 보낼 고유 식별번호 (exchange_items 테이블의 id)
			writeD(eItem.getId()); 
			writeH(item.getInvGfx());
			// 가격이 21억을 넘을 수 있으므로 int로 캐스팅 시 주의가 필요하지만, 리니지 클라이언트 표기 한계상 int로 전송
			writeD((int) eItem.getPrice()); 

			StringBuffer sb = new StringBuffer();
			
			// 1. 결제 화폐 표기
			sb.append("[").append(eItem.getCurrencyType() == 0 ? "아덴" : "코인").append("] ");
			
			// 2. 축복/저주 표기
			sb.append(eItem.getBless() == 0 ? "(축) " : (eItem.getBless() == 1 ? "" : "(저주) "));
			
			// 3. 인챈트 표기 (무기나 방어구일 경우)
			if (eItem.getEnchant() != 0 && (item.getType1().equalsIgnoreCase("weapon") || item.getType1().equalsIgnoreCase("armor"))) {
				sb.append(eItem.getEnchant() > 0 ? "+" : "").append(eItem.getEnchant()).append(" ");
			}
			
			// 4. 아이템 이름
			sb.append(eItem.getItemName());
			
			// 5. 수량 표기 (겹치는 아이템)
			if (eItem.getCount() > 1) {
				sb.append(" (").append(Util.changePrice(eItem.getCount())).append(")");
			}
			
			// 완성된 이름을 클라이언트로 전송
			writeS(sb.toString());

			// 하위 버전 호환 (아이템 상세 정보 전송)
			if (Lineage.server_version > 144) {
				if (item.getType1().equalsIgnoreCase("armor")) {
					toArmor(item, 0, eItem.getEnchant(), (int) item.getWeight(), eItem.getEnchant() * item.getEnchantMr(), eItem.getBless(),
							eItem.getEnchant() * item.getEnchantStunDefense(), eItem.getEnchant() * item.getEnchantSp(), eItem.getEnchant() * item.getEnchantReduction());
				} else if (item.getType1().equalsIgnoreCase("weapon")) {
					toWeapon(item, 0, eItem.getEnchant(), (int) item.getWeight(), eItem.getBless(), eItem.getEnchant() * item.getEnchantMr(), 
							eItem.getEnchant() * item.getEnchantStunDefense(), eItem.getEnchant() * item.getEnchantSp(), eItem.getEnchant() * item.getEnchantReduction());
				} else {
					toEtc(item, (int) item.getWeight());
				}
			}
		}
	}
}