package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class WindShot extends Magic {

	public WindShot(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new WindShot(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;
			o.setBuffWindShot(true);
			// 원거리 명중 +4
			cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + 4);
			
			// [추가] 스탯 갱신 패킷 (정보창에 명중 변화 즉시 반영)
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}
		
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		// [수정] 구형 패킷(S_BuffElf) 및 반복 멘트/이펙트 삭제
		// 신규 아이콘 패킷 전송 (14번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 14, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		
		// 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "\\fY원거리 명중+4", Lineage.CHATTING_MODE_MESSAGE);
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;
			o.setBuffWindShot(false);
			// 명중 원상복구
			cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - 4);
			
			// 스탯 갱신
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 14, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		
		// 종료 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "\\fY윈드 샷 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(Character cha, Skill skill, long object_id) {		
		if (cha != null) {
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
			
			if (SkillController.isMagic(cha, skill, true) && SkillController.isFigure(cha, cha, skill, false, false))
				onBuff(cha, skill);
		}
	}

	static public void onBuff(object o, Skill skill) {
		// 아이오브스톰 적용되어 있을경우 우선순위 적용 (중복 불가)
		if (BuffController.find(o, SkillDatabase.find(126)) != null)
			return;
		// 스톰샷 적용되어 있을경우 우선순위 적용 (중복 불가)
		if (BuffController.find(o, SkillDatabase.find(135)) != null)
			return;
			
		// [이동] 이펙트는 시작할 때 한 번만 출력 (Update에서 이동해옴)
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);

		BuffController.append(o, WindShot.clone(BuffController.getPool(WindShot.class), skill, skill.getBuffDuration()));
	}

}