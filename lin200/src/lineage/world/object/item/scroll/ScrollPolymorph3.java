package lineage.world.object.item.scroll;

import java.util.ArrayList;
import java.util.List;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.RankController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ScrollPolymorph3 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ScrollPolymorph3();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getMap() == 807) {
			ChattingController.toChatting(cha, "여기서는 사용 하실 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		if (!cha.isFishing()) {
			List<String> quickPolymorph = new ArrayList<String>();
			int allRank = RankController.getAllRank(cha.getObjectId());
			int classRank = RankController.getClassRank(cha.getObjectId(), cha.getClassType());
			
			quickPolymorph.clear();
			quickPolymorph.add(cha.getQuickPolymorph() == null || cha.getQuickPolymorph().equalsIgnoreCase("") || cha.getQuickPolymorph().length() < 1 ? "빠른 변신 목록 없음" : cha.getQuickPolymorph());
			
			cha.toSender(S_MessageYesNo.clone(BasePacketPooling.getPool(S_MessageYesNo.class), 180));
			
			// 💡 주문서의 축복 상태 확인 (0: 축복, 1: 일반, 2: 저주)
			boolean isBlessed = (this.getBless() == 0);

			// 💡 HTML은 모두 원본(monlists110)으로 복구했습니다.
			if (Lineage.item_polymorph_bless && isBlessed) {
				// 만약 축복받은 반지 전용으로 레벨 제한이 풀린 HTML 파일이 따로 있다면, 
				// 아래의 "monlists110" 부분을 해당 파일명으로 수정해 주시면 됩니다!
				cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), cha, "monlists110", null, quickPolymorph));
			} 
			else if (Lineage.is_rank_poly) {
				if ((((allRank > 0 && allRank <= Lineage.rank_poly_all) || (classRank > 0 && classRank <= Lineage.rank_poly_class)) && cha.getLevel() >= Lineage.rank_min_level) ||
					Lineage.event_rank_poly || cha.getMap() == Lineage.teamBattleMap || cha.getGm() > 0)
					cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), cha, "monlists110", null, quickPolymorph));
				else
					cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), cha, "monlists110", null, quickPolymorph));
			} else {
				cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), cha, "monlists110", null, quickPolymorph));
			}
			
			// 💡 [버그 해결 핵심] 주석(//)을 해제하여 서버가 아이템 사용 상태를 정상적으로 기억하게 만듭니다.
			((PcInstance) cha).setTempPoly(true);
			((PcInstance) cha).setTempPolyScroll(this);
			
		} else {
			ChattingController.toChatting(cha, "낚시중에는 변신할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
		}
	}
}