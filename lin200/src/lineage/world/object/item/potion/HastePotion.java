package lineage.world.object.item.potion;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.AbsoluteBarrier;
import lineage.world.object.magic.HastePotionMagic;

public class HastePotion extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new HastePotion();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (!isClick(cha))
			return;

		// [추가] 디케이 포션 상태 체크
		if (cha.isBuffDecayPotion()) {
			ChattingController.toChatting(cha, "디케이포션:물약사용불가", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		// [추가] 앱솔루트 배리어 해제
		if (cha.isBuffAbsoluteBarrier())
			BuffController.remove(cha, AbsoluteBarrier.class);

		// 이펙트 표현
		cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, getItem().getEffect()), true);

		// 버프 적용
		// [수정] 인자 개수를 맞추기 위해 false 추가 (Character, int, boolean)
		HastePotionMagic.init(cha, getItem().getDuration(), false);

		// 아이템 수량 감소
		cha.getInventory().count(this, getCount() - 1, true);
	}
}