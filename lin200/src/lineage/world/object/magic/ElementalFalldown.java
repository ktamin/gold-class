package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class ElementalFalldown extends Magic {

	static synchronized public BuffInterface clone(BuffInterface bi, Character cha, Skill skill, int time) {
		if (bi == null)
			bi = new ElementalFalldown(skill);
		// 여기서 setCharacter는 시전자를 저장함 (시전자의 속성을 알아야 하므로)
		bi.setCharacter(cha);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	public ElementalFalldown(Skill skill) {
		super(null, skill);
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;

			// 시전자(getCharacter)의 속성에 따라 상대방(cha)의 저항력을 깎음
			// [수정] 복사 붙여넣기 오류 수정 (Fire 저항을 가져오던 것들 수정)
			// [수정] 수치를 -50으로 고정 (skill.getMaxdmg() 대신 50 사용)
			switch (getCharacter().getAttribute()) {
				case Lineage.ELEMENT_EARTH:
					cha.setDynamicEarthress(cha.getDynamicEarthress() - 50);
					break;
				case Lineage.ELEMENT_FIRE:
					cha.setDynamicFireress(cha.getDynamicFireress() - 50);
					break;
				case Lineage.ELEMENT_WIND:
					// 기존 코드 오류: getDynamicFireress() -> getDynamicWindress()로 수정
					cha.setDynamicWindress(cha.getDynamicWindress() - 50);
					break;
				case Lineage.ELEMENT_WATER:
					// 기존 코드 오류: getDynamicFireress() -> getDynamicWaterress()로 수정
					cha.setDynamicWaterress(cha.getDynamicWaterress() - 50);
					break;
			}
			// 스탯 갱신 (상대방 화면)
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}

		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		// 버프 갱신 및 아이콘 유지 (55번)
		// o는 디버프 걸린 상대방입니다.
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 55, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
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

		if (o instanceof Character) {
			Character cha = (Character) o;

			// [수정] 원상 복구 로직 (오타 수정 및 +50 적용)
			switch (getCharacter().getAttribute()) {
				case Lineage.ELEMENT_EARTH:
					cha.setDynamicEarthress(cha.getDynamicEarthress() + 50);
					break;
				case Lineage.ELEMENT_FIRE:
					cha.setDynamicFireress(cha.getDynamicFireress() + 50);
					break;
				case Lineage.ELEMENT_WIND:
					cha.setDynamicWindress(cha.getDynamicWindress() + 50);
					break;
				case Lineage.ELEMENT_WATER:
					cha.setDynamicWaterress(cha.getDynamicWaterress() + 50);
					break;
			}
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}

		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 55, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	/**
	 * * @param cha
	 * 
	 * @param skill
	 * @param object_id
	 */
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

			// 성공 확률 체크 (isFigure)
			if (SkillController.isMagic(cha, skill, true) && SkillController.isFigure(cha, o, skill, false, false)) {
				// 버프 등록 (여기서 cha는 시전자입니다. clone 메서드로 넘겨서 시전자 속성을 저장합니다.)
				BuffController.append(o, ElementalFalldown.clone(BuffController.getPool(ElementalFalldown.class), cha,
						skill, skill.getBuffDuration()));
				// 이펙트 처리
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()),
						true);
			}
		}
	}

}