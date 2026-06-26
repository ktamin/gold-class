package lineage.world.object.item;

import lineage.network.packet.ClientBasePacket;
import lineage.world.controller.LetterController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class PledgeLetter extends Letter {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new PledgeLetter();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (getItem().getInvGfx() == 464) {
			// 새편지 작성
			cbp.readH();
			String to = cbp.readS(); // 혈맹 이름
			String subject = cbp.readSS(); // 제목
			String memo = cbp.readSS(); // 내용

			// =========================================================
			// ✨ [보안 강화] 실제 소속 혈맹 & 진짜 혈맹주(lord)인지 철통 검증!
			// =========================================================
			if (cha instanceof PcInstance) {
				PcInstance pc = (PcInstance) cha;

				if (pc.getGm() == 0) { // 운영자(GM)는 무조건 프리패스

					// 캐릭터가 소속된 혈맹 이름을 가져옵니다.
					String myClanName = pc.getClanName();

					// 방어 1단계: 혈맹에 가입하지 않은 유저 컷
					if (myClanName == null || myClanName.trim().isEmpty()) {
						lineage.world.controller.ChattingController.toChatting(pc, "혈맹에 가입된 상태가 아닙니다.",
								lineage.share.Lineage.CHATTING_MODE_MESSAGE);
						return;
					}

					// 방어 2단계: 수신자(to) 칸에 남의 혈맹 이름을 적는 행위 컷
					if (!myClanName.equalsIgnoreCase(to)) {
						lineage.world.controller.ChattingController.toChatting(pc, "자신이 속한 혈맹원에게만 편지를 보낼 수 있습니다.",
								lineage.share.Lineage.CHATTING_MODE_MESSAGE);
						return;
					}

					// 방어 3단계: 진짜 방장(혈맹주)인지 컷
					lineage.bean.lineage.Clan clan = lineage.world.controller.ClanController.find(myClanName);

					if (clan == null || !clan.getLord().equalsIgnoreCase(pc.getName())) {
						lineage.world.controller.ChattingController.toChatting(pc, "혈맹 편지지는 창설자인 군주(혈맹주)만 사용할 수 있습니다.",
								lineage.share.Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
				}
			}
			// =========================================================

			if (subject.length() < 2)
				subject = "제목 없음";

			if (memo.length() < 2)
				subject += "   ";

			// 수량 하향
			cha.getInventory().count(this, getCount() - 1, true);
			// 편지작성한거 처리.
			LetterController.toPledgeLetter(cha.getName(), to, subject, memo);
		} else {
			super.toClick(cha, cbp);
		}
	}
}