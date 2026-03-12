package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectSpeed;
import lineage.world.controller.BuffController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class HastePotionMagic extends Magic {

	public HastePotionMagic(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time, boolean restart) {
		if (bi == null)
			bi = new HastePotionMagic(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setSpeed(1);
		o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 0, o.getSpeed(), getTime()), true);
		o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 184));
		
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 0, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			SC_BUFFICON_NOTI.on((PcInstance)o, 0, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffUpdate(object o) {
		o.setSpeed(1);
		o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 0, o.getSpeed(), getTime()), true);
		o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 183));
		
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 0, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		if (o.isWorldDelete())
			return;
		o.setSpeed(0);
		o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 0, o.getSpeed(), 0), true);
		o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 185));
		
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 0, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	static public void init(Character cha, int time, boolean restart) {
		if (cha.getSpeed() == 2) {
			BuffController.remove(cha, Slow.class);
			return;
		}

		// [중요] 중복 제거
		if (cha instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) cha, 0, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		BuffController.remove(cha, Haste.class); // 마법 헤이스트 제거
		BuffController.remove(cha, GreaterHaste.class); // 그레이터 제거
		BuffController.remove(cha, HastePotionMagic.class); // 물약 갱신용 제거
		
		ItemInstance item1 = cha.getInventory() != null ? cha.getInventory().getSlot(lineage.share.Lineage.SLOT_WEAPON) : null;
		ItemInstance item2 = cha.getInventory() != null ? cha.getInventory().getSlot(lineage.share.Lineage.SLOT_SHIELD) : null;
		if ((item1 != null && item1.getItem().getNameIdNumber() == 418) || (item2 != null && item2.getItem().getNameIdNumber() == 419))
			return;

		BuffController.append(cha, HastePotionMagic.clone(BuffController.getPool(HastePotionMagic.class), null, time, restart));
	}
}