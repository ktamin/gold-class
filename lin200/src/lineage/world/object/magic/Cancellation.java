package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI; // 아이콘 패킷
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.MonsterSkill;
import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectSpeed; // 속도 패킷
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.monster.CurseGhoul;

public class Cancellation {

	static public void init(Character cha, Skill skill, int object_id) {
		// 초기화
		object o = null;
		// 타겟 찾기
		if (object_id == cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList(object_id);
		if (o != null && !Util.isAreaAttack(cha, o)) {
			return; // 벽 뒤면 무조건 정지
		}
		// 처리
		if (o != null) {
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha,
					Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
			SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(1400).send((PcInstance) cha);
			if (SkillController.isMagic(cha, skill, true)) {
				// 투망상태 해제
				Detection.onBuff(cha);
				// 공격당한거 알리기.
				o.toDamage(cha, 0, Lineage.ATTACK_TYPE_MAGIC, Cancellation.class);

				if (SkillController.isFigure(cha, o, skill, true, SkillController.isClan(cha, o))) {
					onBuff(o, skill);
				} else
					// \f1마법이 실패했습니다.
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 280));

			}
		}
	}

	static public void init(MonsterInstance mi, object o, MonsterSkill ms) {
		// 처리
		if (o != null) {
			if (SkillController.isMagic(mi, ms, true)) {
				// 공격당한거 알리기.
				o.toDamage(mi, 0, Lineage.ATTACK_TYPE_MAGIC, Cancellation.class);

				if (SkillController.isFigure(mi, o, ms.getSkill(), false, false))
					onBuff(o, ms.getSkill());
			}
		}
	}

	static public void onBuff(object o, Skill skill) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);

		// [강제] 가속 관련 아이콘 즉시 삭제 패킷 전송
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 0, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS); // 헤이스트/초록물약
			SC_BUFFICON_NOTI.on(pc, 1, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS); // 용기
			SC_BUFFICON_NOTI.on(pc, 2, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS); // 2단가속
			SC_BUFFICON_NOTI.on(pc, 47, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS); // 무빙 악셀레이션
			SC_BUFFICON_NOTI.on(pc, 68, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS); // 그레이터 헤이스트
			SC_BUFFICON_NOTI.on(pc, 70, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS); // 홀리 워크
		}

		// [중요] 모든 가속 버프 강제 삭제 (장비 옵션 체크 무시하고 일단 제거)
		BuffController.remove(o, Haste.class);
		BuffController.remove(o, HastePotionMagic.class); // 초록 물약 확실히 제거
		BuffController.remove(o, GreaterHaste.class);
		BuffController.remove(o, Bravery.class);
		BuffController.remove(o, Wafer.class);
		BuffController.remove(o, HolyWalk.class);
		BuffController.remove(o, movingacceleratic.class); // 무빙 악셀레이션 확실히 제거

		// [핵심] 캐릭터 이동 속도 수치 강제 원상 복구 (0: Normal)
		o.setSpeed(0);
		o.setBrave(false);
		o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 0, 0, 0), true);

		// --- 기타 마법 제거 ---
		BuffController.remove(o, Light.class);
		BuffController.remove(o, Shield.class);
		BuffController.remove(o, CursePoison.class);
		BuffController.remove(o, DecreaseWeight.class);
		BuffController.remove(o, CurseBlind.class);
		BuffController.remove(o, Berserks.class);
		BuffController.remove(o, EnchantDexterity.class);
		BuffController.remove(o, Slow.class);
		BuffController.remove(o, CounterMagic.class);
		BuffController.remove(o, Meditation.class);
		BuffController.remove(o, CurseParalyze.class);
		BuffController.remove(o, EnchantMighty.class);
		BuffController.remove(o, IceLance.class);
		BuffController.remove(o, Disease.class);

		// 변신 제거
		if (BuffController.find(o, SkillDatabase.find(208)) != null) {
			if (!o.isSetPoly)
				BuffController.remove(o, ShapeChange.class);
		}

		BuffController.remove(o, ImmuneToHarm.class);
		BuffController.remove(o, DecayPotion.class);
		BuffController.remove(o, Silence.class);
		BuffController.remove(o, FogOfSleeping.class);
		BuffController.remove(o, InvisiBility.class);
		BuffController.remove(o, ReductionArmor.class);
		BuffController.remove(o, SolidCarriage.class);
		BuffController.remove(o, CounterBarrier.class);
		BuffController.remove(o, GlowingWeapon.class);
		BuffController.remove(o, ShiningShield.class);
		BuffController.remove(o, BraveMental.class);
		BuffController.remove(o, BraveAvatar.class);
		BuffController.remove(o, ResistMagic.class);
		BuffController.remove(o, ClearMind.class);
		BuffController.remove(o, ResistElemental.class);
		BuffController.remove(o, EagleEye.class);
		BuffController.remove(o, AquaProtect.class);
		BuffController.remove(o, PolluteWater.class);
		BuffController.remove(o, StrikerGale.class);
		BuffController.remove(o, EraseMagic.class);
		BuffController.remove(o, BurningWeapon.class);
		BuffController.remove(o, ElementalFire.class);
		BuffController.remove(o, EyeOfStorm.class);
		BuffController.remove(o, NaturesTouch.class);
		BuffController.remove(o, EarthGuardian.class);
		BuffController.remove(o, AdditionalFire.class);
		BuffController.remove(o, WaterLife.class);
		BuffController.remove(o, EarthBind.class);
		BuffController.remove(o, StormShot.class);
		BuffController.remove(o, SoulOfFlame.class);
		BuffController.remove(o, IronSkin.class);
		BuffController.remove(o, CurseGhoul.class);
		BuffController.remove(o, FireWeapon.class);
		BuffController.remove(o, WindShot.class);
		BuffController.remove(o, BlessOfFire.class);
		BuffController.remove(o, Wisdom.class);
		BuffController.remove(o, Blue.class);
		// BuffController.remove(o, Exp_Potion.class);
	}
}