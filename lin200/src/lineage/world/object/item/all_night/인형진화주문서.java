package lineage.world.object.item.all_night;

import all_night.Lineage_Balance;
import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ItemDropMessageDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.item.MagicDoll;

public class 인형진화주문서 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 인형진화주문서();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		ItemInstance target = cha.getInventory().value(cbp.readD());

		if (target == null || target.getItem() == null)
			return;

		if (target instanceof MagicDoll) {
			String targetName = target.getItem().getName();
			if (!targetName.equalsIgnoreCase("마법인형: 군주") && !targetName.equalsIgnoreCase("마법인형: 기사") &&
					!targetName.equalsIgnoreCase("마법인형: 요정") && !targetName.equalsIgnoreCase("마법인형: 마법사")
					&& !targetName.equalsIgnoreCase("마법인형: 다크엘프")) {
				ChattingController.toChatting(cha, "해당 인형에 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			// ✅ [로그용 변수 미리 저장]
			long time = System.currentTimeMillis();
			String timeString = lineage.util.Util.getLocaleString(time, true);
			String charName = cha.getName();
			String oldDollName = targetName;
			String scrollName = this.getItem().getName();

			// PcInstance 캐스팅 (천장 카운트 접근용)
			lineage.world.object.instance.PcInstance pc = (lineage.world.object.instance.PcInstance) cha;

			// ==========================================
			// ✅ [천장 시스템 로직]
			// ==========================================
			boolean isSuccess = false;
			int maxPity = Lineage_Balance.doll_pity_count; // 외부 콘프의 천장 횟수

			if (pc.dollEvoCount >= (maxPity - 1)) {
				// 천장 도달! 100% 성공
				isSuccess = true;
				pc.dollEvoCount = 0; // 스택 초기화
				// ChattingController.toChatting(pc, "\\fW[시스템] 마법의 기운이 모여 100% 진화에 성공했습니다!",
				// 20);
			} else {
				// 천장 도달 전: 기존 확률 적용
				isSuccess = Math.random() < Lineage_Balance.doll_upgrade_percent;
				if (isSuccess) {
					pc.dollEvoCount = 0; // 운 좋게 성공 시 스택 초기화
				} else {
					pc.dollEvoCount += 1; // 실패 시 스택 누적
				}
			}
			// ==========================================

			if (isSuccess) {
				// ✅ 성공 처리
				String name = null;

				switch (target.getItem().getName()) {
					case "마법인형: 군주":
						name = "마법인형: 진 군주";
						break;
					case "마법인형: 기사":
						name = "마법인형: 진 기사";
						break;
					case "마법인형: 요정":
						name = "마법인형: 진 요정";
						break;
					case "마법인형: 마법사":
						name = "마법인형: 진 마법사";
						break;
					case "마법인형: 다크엘프":
						name = "마법인형: 진 다크엘프";
						break;
				}

				Item i = ItemDatabase.find(name);
				if (i != null) {
					ItemInstance temp = ItemDatabase.newInstance(i);
					temp.setObjectId(ServerDatabase.nextItemObjId());
					temp.setDefinite(true);
					cha.getInventory().append(temp, true);

					// 주문서 및 인형 제거
					cha.getInventory().count(this, getCount() - 1, true);
					cha.getInventory().count(target, target.getCount() - 1, true);
					ChattingController.toChatting(cha, "\\fR인형 진화에 성공하였습니다!", Lineage.CHATTING_MODE_MESSAGE);

					ItemDropMessageDatabase.sendMessageMagicDoll2(cha, name);

					// ✅ [로그 출력] 진화 성공 (노란색)
					final String logMessage = String.format("[%s] [진화 성공]\t [캐릭터: %s]\t [기존: %s]\t [결과: %s]",
							timeString, charName, oldDollName, temp.getItem().getName());
					lineage.gui.GuiMain.display.asyncExec(new Runnable() {
						public void run() {
							lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(logMessage);
						}
					});
				}
			} else {
				// ✅ 실패 처리 (증발)
				ChattingController.toChatting(cha, "\\fY인형 진화에 실패하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
				// ChattingController.toChatting(pc, "\\fV[시스템] 인형 진화 실패! (현재 누적: " +
				// pc.dollEvoCount + "/" + maxPity + ")", 20);

				// 주문서 및 인형 제거
				cha.getInventory().count(this, getCount() - 1, true);
				cha.getInventory().count(target, target.getCount() - 1, true);

				// ✅ [로그 출력] 진화 실패 (빨간색)
				final String logMessage = String.format("[%s] [진화 실패(증발)]\t [캐릭터: %s]\t [소멸: %s]\t [주문서: %s]",
						timeString, charName, oldDollName, scrollName);
				lineage.gui.GuiMain.display.asyncExec(new Runnable() {
					public void run() {
						lineage.gui.GuiMain.getViewComposite().getEnchantComposite().toLog(logMessage);
					}
				});
			}
		} else {
			ChattingController.toChatting(cha, "인형에만 사용가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
		}
	}
}