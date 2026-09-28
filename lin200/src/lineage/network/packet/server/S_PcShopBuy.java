package lineage.network.packet.server;

import lineage.bean.database.PcShop;
import lineage.database.CharacterMarbleDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.object.instance.PcShopInstance;

public class S_PcShopBuy extends S_Inventory {

	static synchronized public BasePacket clone(BasePacket bp, PcShopInstance psi) {
		if (bp == null)
			bp = new S_PcShopBuy(psi);
		else
			((S_PcShopBuy) bp).toClone(psi);
		return bp;
	}

	public S_PcShopBuy(PcShopInstance psi) {
		toClone(psi);
	}

	public void toClone(PcShopInstance psi) {
		clear();

		writeC(Opcodes.S_OPCODE_SHOPBUY);
		writeD(psi.getObjectId());

		// 일반상점 구성구간.
		writeH(psi.getListSize());

		for (PcShop s : psi.getShopList().values()) {
			writeD(s.getInvItemObjectId());
			writeH(s.getItem().getInvGfx());
			writeD(s.getPrice());

			StringBuilder sb = new StringBuilder();

			// 💡 1. [아덴] / [코인] 화폐 접두어 표시
			sb.append("[").append(s.getAdenType().equalsIgnoreCase("아데나") ? "아덴" : "코인").append("] ");

			// 💡 2. (축) / (저주) 표시
			if (s.getInvItemBress() == 0) {
				sb.append("(축) ");
			} else if (s.getInvItemBress() != 1) {
				sb.append("(저주) ");
			}

			// 💡 3. 무기 / 방어구 인챈트 (+9, -1 등) 표시
			if (s.getItem().getType1().equalsIgnoreCase("weapon") || s.getItem().getType1().equalsIgnoreCase("armor")) {
				sb.append(s.getInvItemEn() >= 0 ? "+" : "").append(s.getInvItemEn()).append(" ");
			}

			// 💡 4. 무기 속성 인챈트 (화령 3단계 등) 표시
			if (s.getItem().getType1().equalsIgnoreCase("weapon")) {
				if (s.getInvItemEnFire() > 0) {
					sb.append("화령 ").append(s.getInvItemEnFire()).append("단계 ");
				} else if (s.getInvItemEnWater() > 0) {
					sb.append("수령 ").append(s.getInvItemEnWater()).append("단계 ");
				} else if (s.getInvItemEnWind() > 0) {
					sb.append("풍령 ").append(s.getInvItemEnWind()).append("단계 ");
				} else if (s.getInvItemEnEarth() > 0) {
					sb.append("지령 ").append(s.getInvItemEnEarth()).append("단계 ");
				}
			}

			// 💡 5. 아이템 명칭 표시
			String itemName = CharacterMarbleDatabase.getItemName(s.getInvItemObjectId());
			if (itemName != null) {
				sb.append(itemName);
			} else {
				sb.append(s.getItem().getName());
			}

			// 💡 6. 수량 표현 (2개 이상일 때만 표시)
			if (s.getInvItemCount() > 1) {
				sb.append(" (").append(Util.changePrice(s.getInvItemCount())).append(")");
			}

			writeS(sb.toString().trim()); // 끝부분 불필요한 공백 제거

			// 스탯 및 상세 속성 패킷 전송 구간 (기존 로직 유지)
			if (Lineage.server_version > 144) {
				if (s.getItem().getType1().equalsIgnoreCase("armor")) {
					if (s.getItem().getName().equalsIgnoreCase("신성한 엘름의 축복"))
						toArmor(s.getItem(), 0, s.getInvItemEn(), (int) s.getItem().getWeight(), s.getInvItemEn() > 4 ? (s.getInvItemEn() - 4) * s.getItem().getEnchantMr() : 0, s.getInvItemBress(),
								s.getInvItemEn() * s.getItem().getEnchantStunDefense(), s.getInvItemEn() * s.getItem().getEnchantSp(), s.getInvItemEn() * s.getItem().getEnchantReduction());
					else
						toArmor(s.getItem(), 0, s.getInvItemEn(), (int) s.getItem().getWeight(), s.getInvItemEn() * s.getItem().getEnchantMr(), s.getInvItemBress(), s.getInvItemEn() * s.getItem().getEnchantStunDefense(),
								s.getInvItemEn() * s.getItem().getEnchantSp(), s.getInvItemEn() * s.getItem().getEnchantReduction());
				} else if (s.getItem().getType1().equalsIgnoreCase("weapon")) {
					toWeapon(s.getItem(), 0, s.getInvItemEn(), (int) s.getItem().getWeight(), s.getInvItemBress(), s.getInvItemEn() * s.getItem().getEnchantMr(), s.getInvItemEn() * s.getItem().getEnchantStunDefense(),
							s.getInvItemEn() * s.getItem().getEnchantSp(), s.getInvItemEn() * s.getItem().getEnchantReduction());
				} else {
					toEtc(s.getItem(), (int) s.getItem().getWeight());
				}
			}
		}
	}

}