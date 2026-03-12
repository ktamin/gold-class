package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.MonsterSkill;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectSpeed;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public class Haste extends Magic {

	public Haste(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time, boolean restart) {
		if (bi == null)
			bi = new Haste(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setSpeed(1);
		o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 0, o.getSpeed(), getTime()),
				true);
		o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 184));

		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 0, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			SC_BUFFICON_NOTI.on((PcInstance) o, 0, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffUpdate(object o) {
		o.setSpeed(1);
		o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 0, o.getSpeed(), getTime()),
				true);
		o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 183));

		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 0, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
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
			SC_BUFFICON_NOTI.on((PcInstance) o, 0, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuff(object o) {
		if (getTime() == 1)
			o.speedCheck = System.currentTimeMillis() + 2000;
	}

	// [오류 해결 1] (Character, Skill, long) 형식 유지
	static public void init(Character cha, Skill skill, long object_id) {
		object o = (object_id == cha.getObjectId()) ? cha : cha.findInsideList((int) object_id);
		if (o != null) {
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha,
					Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
			if (o != null && !Util.isAreaAttack(cha, o)) {
				return; // 벽 뒤면 무조건 정지
			}
			if (cha instanceof PcInstance) {
				SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(700).send((PcInstance) cha);
			}
			if (SkillController.isMagic(cha, skill, true) && SkillController.isFigure(cha, o, skill, false, false))
				onBuff(o, skill);
		}
	}

	// [오류 해결 2] (Character/PcInstance, int, boolean) 형식 추가
	static public void init(Character cha, int time, boolean restart) {
		if (cha.getSpeed() == 2) {
			BuffController.remove(cha, Slow.class);
			return;
		}

		// 중복 제거
		if (cha instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) cha, 0, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		BuffController.remove(cha, HastePotionMagic.class);
		BuffController.remove(cha, GreaterHaste.class);

		BuffController.append(cha,
				Haste.clone(BuffController.getPool(Haste.class), SkillDatabase.find(6, 2), time, restart));
	}

	// [오류 해결 3] (MonsterInstance, object, MonsterSkill) 형식 추가
	static public void init(MonsterInstance mi, object o, MonsterSkill ms) {
		if (o != null) {
			mi.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), mi,
					Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
			if (SkillController.isMagic(mi, ms, true))
				onBuff(o, ms.getSkill());
		}
	}

	static public void onBuff(object o, Skill skill) {
		onBuff(o, skill, skill.getBuffDuration());
	}

	static public void onBuff(object o, Skill skill, int time) {
		ItemInstance item1 = o.getInventory() != null ? o.getInventory().getSlot(Lineage.SLOT_WEAPON) : null;
		ItemInstance item2 = o.getInventory() != null ? o.getInventory().getSlot(Lineage.SLOT_SHIELD) : null;
		if ((item1 != null && item1.getItem().getNameIdNumber() == 418)
				|| (item2 != null && item2.getItem().getNameIdNumber() == 419))
			return;

		if (o.getSpeed() == 2) {
			BuffController.remove(o, Slow.class);
			return;
		}

		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 0, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		BuffController.remove(o, HastePotionMagic.class);
		BuffController.remove(o, GreaterHaste.class);
		BuffController.remove(o, Haste.class);

		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		BuffController.append(o, Haste.clone(BuffController.getPool(Haste.class), skill, time, false));
	}
}