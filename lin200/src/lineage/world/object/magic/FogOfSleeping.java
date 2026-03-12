package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffectLocation;
import lineage.network.packet.server.S_ObjectLock;
import lineage.network.packet.server.S_ObjectPoisonLock;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController; // 멘트 필요시 사용
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class FogOfSleeping extends Magic {

	public FogOfSleeping(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new FogOfSleeping(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setBuffFogOfSleeping(true);

		// 행동 불가(Lock) 처리
		if (!o.isLockLow()) {
			o.setLockLow(true);
			o.toSender(S_ObjectPoisonLock.clone(BasePacketPooling.getPool(S_ObjectPoisonLock.class), o), true);
			o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x02));
		}

		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		// [추가] 버프 갱신 및 아이콘 유지 (76번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 76, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		if (o.isWorldDelete() || !o.isLock())
			return;

		o.setBuffFogOfSleeping(false);

		// 행동 불가 해제
		o.setLockLow(false);
		o.toSender(S_ObjectPoisonLock.clone(BasePacketPooling.getPool(S_ObjectPoisonLock.class), o), true);
		o.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 0x00));

		// [추가] 종료 시 아이콘 삭제 (76번)
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 76, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	static public void init(Character cha, Skill skill, int object_id) {
		//
		object o = cha.findInsideList(object_id);

		// ▼▼▼ [수정 1] 타겟이 벽 뒤에 있는지 우선 검사 (시전 모션 방지) ▼▼▼
		if (o != null && !Util.isAreaAttack(cha, o)) {
			// 벽 뒤라면 즉시 종료
			return;
		}
		// ▲▲▲ [여기까지 추가] ▲▲▲

		if (o != null)
			init(cha, skill, o.getX(), o.getY());
		else
			init(cha, skill, cha.getX(), cha.getY());
	}

	static public void init(Character cha, Skill skill, int x, int y) {
		// [수정] 모션과 딜레이를 주기 전에, 여기서도 유효한 타겟이 하나라도 있는지 체크하는 것이 좋지만,
		// 범위 마법 특성상 일단 시전은 하되 효과만 안 들어가게 처리합니다.

		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha,
				Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);

		// [추가] 시전 딜레이 패킷
		if (cha instanceof PcInstance) {
			SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(1200).send((PcInstance) cha);
		}

		// 범위 내 대상 추출
		for (object o : cha.getInsideList()) {
			// 대상이 지정 좌표에 있고, 10칸 이내이며, 마법 유효성 체크 통과 시
			if (o instanceof Character && o.getX() == x && o.getY() == y && Util.isDistance(cha, o, 10)
					&& SkillController.isMagic(cha, skill, true) && cha.getObjectId() != o.getObjectId()) {

				// ▼▼▼ [수정 2] 범위 내 대상이라도 벽 뒤에 있는지 2차 검사 (효과 적용 방지) ▼▼▼
				if (!Util.isAreaAttack(cha, o)) {
					continue; // 벽 뒤에 있는 대상은 건너뜀
				}
				// ▲▲▲ [여기까지 추가] ▲▲▲

				// 적용 (확률 체크)
				if (SkillController.isFigure(cha, o, skill, true, false)) {

					// 바닥 이펙트 (성공했을 때만 이펙트 나오게 처리된 상태)
					// (만약 이펙트는 벽 뒤라도 나오게 하고 싶으면 이 부분을 위로 올려야 하지만, 보통은 안 나오는 게 맞습니다)
					cha.toSender(S_ObjectEffectLocation.clone(BasePacketPooling.getPool(S_ObjectEffectLocation.class),
							skill.getCastGfx(), x, y), true);

					onBuff(cha, o, skill, skill.getBuffDuration());
				}
			}
		}
		// 투망상태 해제
		Detection.onBuff(cha);
	}

	/**
	 * 중복 코드 방지용.
	 */
	static public void onBuff(Character cha, object o, Skill skill, int time) {
		BuffController.append(o, FogOfSleeping.clone(BuffController.getPool(FogOfSleeping.class), skill, time));
		// 공격당한거 알리기.
		o.toDamage(cha, 0, Lineage.ATTACK_TYPE_MAGIC);
	}

}