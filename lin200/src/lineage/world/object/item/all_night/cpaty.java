package lineage.world.object.item.all_night;

import lineage.bean.lineage.Clan;
import lineage.bean.lineage.Party;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.ClanController;
import lineage.world.controller.PartyController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class cpaty extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new cpaty();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		Party clanParty = null;
		boolean result = true;
		
		if (cha.getClanId() == 0) {
			ChattingController.toChatting(cha, "혈맹에 가입되어 있지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		PcInstance cha2 = (PcInstance) cha;
		PartyController.checkParty(cha2);
		
		Party temp = PartyController.find(cha);
		if (temp != null && !temp.isClanParty() && temp.getMaster().getObjectId() != cha2.getObjectId()) {
			PartyController.close(cha2);
		}	
		
		try {
			Clan c = ClanController.find(cha2);
			if (c != null) {
				for (PcInstance pc : c.getList()) {
					Party p = PartyController.find(pc);
					
					if (p != null && pc != null && pc.getClanId() == cha2.getClanId() && p.isClanParty()) {
						clanParty = p;
						break;
					}
				}
				
				// 혈맹파티를 찾아서 혈맹파티가 있을경우 혈맹파티 명령어를 입력한 대상이 혈맹파티로 가입
				if (clanParty != null) {		
					for (PcInstance clan : clanParty.getList()) {
						if (clan.getObjectId() == cha2.getObjectId()) {
							result = false;
							break;
						}
					}
					
					if (result) {
						cha2.setPartyId(clanParty.getKey());
						PartyController.toClanParty(cha2, true);
					}
				} else {
					for (PcInstance pc : ClanController.find(cha2).getList()) {
							PartyController.toClanParty(cha2, pc);
					}
				}
			}
		} catch (Exception e) {
			ChattingController.toChatting(cha, Lineage.command + "혈맹파티", Lineage.CHATTING_MODE_MESSAGE);
		}

	}
}
