package lineage.world.object.item.all_night;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 최대HP증가물약 extends ItemInstance {
/*
	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 최대HP증가물약();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha instanceof PcInstance) {
			if (cha.isAddHp) {
				ChattingController.toChatting(cha, "이미 효과가 적용 중입니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			cha.isAddHp = true;
			cha.setDynamicHp(cha.getDynamicHp() + Util.random(getItem().getSmallDmg(), getItem().getBigDmg()));
				
			// 아이템 수량 갱신
			cha.getInventory().count(this, getCount()-1, true);
		}
	}
*/	
	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 최대HP증가물약();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha instanceof PcInstance) {
			PcInstance pc = (PcInstance) cha; 

			if (pc.isAddHp) {
				ChattingController.toChatting(pc, "이미 효과가 적용 중입니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			// 1. 지속 시간 및 상승할 HP량 설정
			int durationSeconds = 3600; // 버프 지속 시간 (1800초 = 30분)
			final int addedHp = Util.random(getItem().getSmallDmg(), getItem().getBigDmg());
			
			// 2. 캐릭터 HP 증가 적용
			pc.isAddHp = true;
			pc.setDynamicHp(pc.getDynamicHp() + addedHp);
			
			// 3. 💡 [수정됨] 찾아주신 패킷을 적용하여 114번 버프 아이콘 출력!
			// 아이콘 번호: 114, 지속 시간: durationSeconds
			SC_BUFFICON_NOTI.on(pc, 114, durationSeconds, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
				
			// 4. 아이템 수량 차감
			pc.getInventory().count(this, getCount()-1, true);

			// 5. 타이머 실행 (지속 시간 종료 후 효과 해제)
			new java.util.Timer().schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					// 캐릭터가 게임 안에 정상적으로 존재할 때만 원상복구 진행
					if (pc != null && !pc.isDead()) {
						pc.isAddHp = false; // 플래그 해제
						pc.setDynamicHp(pc.getDynamicHp() - addedHp); // 올랐던 HP 다시 차감
						
						// 💡 [수정됨] 아이콘 삭제 (지속 시간을 0으로 보내어 클라이언트에서 지움)
						// ※ 참고: 만약 팩에 SC_BUFFICON_NOTI.off(pc, 114); 같은 메서드가 따로 있다면 그것을 사용하셔도 좋습니다.
						SC_BUFFICON_NOTI.on(pc, 114, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
						
						ChattingController.toChatting(pc, "최대 HP 증가 물약의 효과가 사라졌습니다.", Lineage.CHATTING_MODE_MESSAGE);
					}
				}
			}, durationSeconds * 1000L); // 밀리초(ms) 단위로 타이머 예약
		}
	}
}

