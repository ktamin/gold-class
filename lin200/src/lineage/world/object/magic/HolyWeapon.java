package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.PcInstance;

public class HolyWeapon extends Magic {

	public HolyWeapon(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill) {
		if (bi == null)
			bi = new HolyWeapon(skill);
		bi.setSkill(skill);
		bi.setTime(skill.getBuffDuration());
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setBuffHolyWeapon(true);

		// [수정] o는 무기(ItemInstance)입니다. 주인을 찾아 아이콘(8번)을 보냅니다.
		if (o instanceof ItemInstance) {
			ItemInstance item = (ItemInstance) o;

			if (item.getCharacter() instanceof PcInstance) {
				PcInstance pc = (PcInstance) item.getCharacter();
				SC_BUFFICON_NOTI.on(pc, 8, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			}
		}

		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		// [추가] 버프 갱신 시 아이콘 유지 (8번)
		if (o instanceof ItemInstance) {
			ItemInstance item = (ItemInstance) o;

			if (item.getCharacter() instanceof PcInstance) {
				PcInstance pc = (PcInstance) item.getCharacter();
				SC_BUFFICON_NOTI.on(pc, 8, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			}
		}
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		o.setBuffHolyWeapon(false);

		if (o instanceof ItemInstance) {
			ItemInstance weapon = (ItemInstance) o;
			if (weapon.getCharacter() != null) {
				// 무기가 보통으로 돌아왔습니다.
				ChattingController.toChatting(weapon.getCharacter(),
						String.format("+%d %s 보통으로 돌아왔습니다.", weapon.getEnLevel(), weapon.getItem().getName()),
						Lineage.CHATTING_MODE_MESSAGE);

				// 종료 멘트 중복 방지 (주석 처리)
				// ChattingController.toChatting(weapon.getCharacter(), "\\fY홀리 웨폰 종료",
				// Lineage.CHATTING_MODE_MESSAGE);

				// [수정] 종료 시 아이콘 삭제 (8번)
				if (weapon.getCharacter() instanceof PcInstance) {
					PcInstance pc = (PcInstance) weapon.getCharacter();
					SC_BUFFICON_NOTI.on(pc, 8, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
				}
			}
		}
	}

	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if(o instanceof ItemInstance && getTime() == Lineage.buff_magic_time_max ||
		// getTime() == Lineage.buff_magic_time_min){
		// ItemInstance weapon = (ItemInstance)o;
		// if(weapon.isEquipped() && weapon.getCharacter()!=null &&
		// !weapon.getCharacter().isWorldDelete()){
		// ChattingController.toChatting(weapon.getCharacter(),"\\fY홀리 웨폰: " + getTime()
		// + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
		// }
		// }
	}

	static public void init(Character cha, Skill skill, int object_id) {
		// 초기화
		object o = null;
		// 타겟 찾기 (자신의 무기 찾기)
		if (object_id == cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList(object_id);
		if (o != null && !Util.isAreaAttack(cha, o)) {
			return; // 벽 뒤면 무조건 정지
		}

		// 처리
		if (o != null && o instanceof Character) {
			if (o.getInventory() != null) {
				// 착용 중인 무기 가져오기
				o = o.getInventory().getSlot(Lineage.SLOT_WEAPON);

				if (o == null) {
					ChattingController.toChatting(cha, "\\fY무기를 착용하고 있지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
					return;
				}

				if (o != null && o instanceof ItemWeaponInstance) {
					cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha,
							Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);

					// 딜레이 패킷 (안전하게 캐스팅)
					if (cha instanceof PcInstance) {
						SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(700).send((PcInstance) cha);
					}

					if (SkillController.isMagic(cha, skill, true))
						onBuff(o, skill);
				}
			}
		}
	}

	static public void onBuff(object o, Skill skill) {
		// 중복 버프 마법 우선순위 적용
		// 인챈트 웨폰
		if (BuffController.find(o, SkillDatabase.find(12)) != null) {
			ChattingController.toChatting(o, "\\fY무기에 인챈트 웨폰 효과가 부여되어 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		// 블레스 웨폰
		if (BuffController.find(o, SkillDatabase.find(48)) != null) {
			ChattingController.toChatting(o, "\\fY무기에 블레스 웨폰 효과가 부여되어 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}

		ItemWeaponInstance weapon = (ItemWeaponInstance) o;

		// 이펙트
		weapon.getCharacter().toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class),
				weapon.getCharacter(), skill.getCastGfx()), true);

		// 버프 등록
		BuffController.append(o, HolyWeapon.clone(BuffController.getPool(HolyWeapon.class), skill));

		ChattingController.toChatting(weapon.getCharacter(),
				String.format("+%d %s 한 순간 파랗게 빛납니다.", weapon.getEnLevel(), weapon.getItem().getName()),
				Lineage.CHATTING_MODE_MESSAGE);

		// 시작 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(weapon.getCharacter(), "홀리 웨폰: 근거리 대미지+1, 근거리
		// 명중+1, 언데드 몬스터 추가 대미지", Lineage.CHATTING_MODE_MESSAGE);
	}
}