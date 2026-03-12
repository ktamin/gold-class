package party_auto_sell;

import java.sql.Connection;
import java.util.List;

import lineage.bean.database.Exp;
import lineage.bean.database.Item;
import lineage.database.DatabaseConnection;
import lineage.database.ExpDatabase;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.share.Lineage;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class S_AutoSellList extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, PcInstance pc, List<AutosellItem> list) {
		if (bp == null)
			bp = new S_AutoSellList(pc, list);
		else
			((S_AutoSellList) bp).toClone(pc, list);
		return bp;
	}

	public S_AutoSellList(PcInstance pc, List<AutosellItem> list) {
		toClone(pc, list);
	}

	public void toClone(PcInstance pc, List<AutosellItem> list) {
		try {
			clear();

			writeC(Opcodes.S_OPCODE_WAREHOUSE);
			writeD(pc.getObjectId());
			writeH(list.size());
			writeC(3);
			readDB(pc, list); // 창고 목록
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readDB(PcInstance pc, List<AutosellItem> list) {
		try {
			for (AutosellItem wh : list) {
				
				Item item = ItemDatabase.find(wh.getName());
				ItemInstance iteminstance = ItemDatabase.newInstance(item);
				writeD(wh.getItemobjid()); // 번호
				writeC(wh.getType()); // 타입
				writeH(wh.getInvGfx()); // GFX 아이디
				writeC(wh.getBless()); // 1: 보통 0: 축 2: 저주
				writeD(1); // 최대 측정할수있는 가격을 20억으로 설정
				writeC(1); // 1: 확인 0: 미확인
				
					writeS(wh.getName());
				

//				writeS(wh.getName()); // 이름
				if (Lineage.server_version >= 380)
					writeC(0x00);
			}
			writeD(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected String getName(ItemInstance item) {
		StringBuffer sb = new StringBuffer();

		
			if (!item.getItem().getNameId().startsWith("$"))
				sb.append(item.getName());
			else
				sb.append(item.getName());
		


		return sb.toString().trim();
	}

}
