package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.MonsterSkill;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
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
import lineage.world.object.instance.PcInstance;

public class Slow extends Magic {

	public Slow(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new Slow(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setSpeed(2); // 속도 2단계 (느림)
		toBuffUpdate(o);

		// [아이콘 추가] 80번 아이콘 표시
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 80, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}

		// 공격당한거 알리기.
		o.toDamage(cha, 0, Lineage.ATTACK_TYPE_MAGIC);
	}

	@Override
	public void toBuffUpdate(object o) {
		o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 0, o.getSpeed(), getTime()),
				true);

		// [아이콘 갱신]
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 80, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
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
		o.setSpeed(0); // 정상 속도로 복구
		o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 0, o.getSpeed(), 0), true);

		// [아이콘 삭제]
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 80, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	static public void init(Character cha, Skill skill, int object_id, boolean slow) {
		object o = null;
		if (object_id == cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList(object_id);
		if (o != null && !Util.isAreaAttack(cha, o)) {
			return; // 벽 뒤면 무조건 정지
		}

		if (o != null) {
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha,
					Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);

			if (cha instanceof PcInstance)
				SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(1200).send((PcInstance) cha);

			if (Util.isDistance(cha, o, 10) && SkillController.isMagic(cha, skill, true)) {
				if (SkillController.isFigure(cha, o, skill, true, false))
					onBuff(o, skill, slow);

				Detection.onBuff(cha);
			}
		}
	}

	static public void onBuff(object o, Skill skill, boolean slow) {
		// 특정 아이템 착용 시 무시 로직
		ItemInstance item1 = o.getInventory() != null ? o.getInventory().getSlot(Lineage.SLOT_WEAPON) : null;
		ItemInstance item2 = o.getInventory() != null ? o.getInventory().getSlot(Lineage.SLOT_SHIELD) : null;
		if ((item1 != null && item1.getItem().getNameIdNumber() == 418)
				|| (item2 != null && item2.getItem().getNameIdNumber() == 419)) {
			return;
		}

		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);

		// 1단계 & 2단계 가속 모두 제거
		// 슬로우/인탱글 공통: 가속 상태를 모두 지웁니다.
		BuffController.remove(o, Haste.class);
		BuffController.remove(o, HastePotionMagic.class);
		BuffController.remove(o, GreaterHaste.class);
		BuffController.remove(o, Bravery.class);
		BuffController.remove(o, HolyWalk.class);
		BuffController.remove(o, Wafer.class);
		BuffController.remove(o, movingacceleratic.class);

		// 실제로 느려지게 만드는 버프 등록
		// 기존 코드에서는 'else'(인탱글) 부분에 append가 없어서 안 느려졌던 것입니다.
		BuffController.append(o, Slow.clone(BuffController.getPool(Slow.class), skill, skill.getBuffDuration()));
	}

	static public void init(Character cha, object o, MonsterSkill ms, int action, int effect, boolean slow) {
		if (o != null && SkillController.isMagic(cha, ms, true)) {
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, action), true);
			onBuff(o, ms.getSkill(), slow);
		}
	}

	static public void init(Character cha, int time) {
		BuffController.append(cha, Slow.clone(BuffController.getPool(Slow.class), SkillDatabase.find(4, 4), time));
	}
}