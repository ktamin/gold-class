package lineage.world.object.item.all_night;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 최대MP증가물약 extends ItemInstance {
/*
	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 최대MP증가물약();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha instanceof PcInstance) {
			if (cha.isAddMp) {
				ChattingController.toChatting(cha, "이미 효과가 적용 중입니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			cha.isAddMp = true;
			cha.setDynamicMp(cha.getDynamicMp() + Util.random(getItem().getSmallDmg(), getItem().getBigDmg()));
				
			// 아이템 수량 갱신
			cha.getInventory().count(this, getCount()-1, true);
		}
	}
*/	
	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 최대MP증가물약();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha instanceof PcInstance) {
			PcInstance pc = (PcInstance) cha;

			if (pc.isAddMp) {
				ChattingController.toChatting(pc, "이미 효과가 적용 중입니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			// 1. 지속 시간 및 상승할 MP량 설정
			int durationSeconds = 3600; // 버프 지속 시간 (1800초 = 30분)
			final int addedMp = Util.random(getItem().getSmallDmg(), getItem().getBigDmg());
			
			// 2. 캐릭터 MP 증가 적용
			pc.isAddMp = true;
			pc.setDynamicMp(pc.getDynamicMp() + addedMp);
			
			// 3. 버프 아이콘 패킷 전송 
			// 💡 HP 물약과 똑같이 114번을 넣었습니다. MP 전용 아이콘 번호를 원하시면 이 숫자를 변경해 주세요!
			SC_BUFFICON_NOTI.on(pc, 115, durationSeconds, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
				
			// 4. 아이템 수량 차감
			pc.getInventory().count(this, getCount()-1, true);

			// 5. 타이머 실행 (지속 시간 종료 후 효과 해제)
			new java.util.Timer().schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					// 캐릭터가 게임 안에 정상적으로 존재할 때만 원상복구 진행
					if (pc != null && !pc.isDead()) {
						pc.isAddMp = false; // 플래그 해제
						pc.setDynamicMp(pc.getDynamicMp() - addedMp); // 올랐던 MP 다시 차감
						
						// 아이콘 삭제 (지속 시간을 0으로 전송)
						SC_BUFFICON_NOTI.on(pc, 115, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
						
						ChattingController.toChatting(pc, "최대 MP 증가 물약의 효과가 사라졌습니다.", Lineage.CHATTING_MODE_MESSAGE);
					}
				}
			}, durationSeconds * 1000L); // 밀리초(ms) 단위로 타이머 예약
		}
	}
}
