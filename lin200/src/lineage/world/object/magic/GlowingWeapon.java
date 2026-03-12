package lineage.world.object.magic;

import java.util.ArrayList;
import java.util.List;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.bean.lineage.Clan;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.ClanController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class GlowingWeapon extends Magic {

	public GlowingWeapon(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new GlowingWeapon(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;
			cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 5);
			cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() + 5);
			cha.setDynamicAddHit(cha.getDynamicAddHit() + 5);
			cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() + 5);
			cha.setBuffGlowingAura(true);

			// 아이콘 중복 방지를 위해 여기서는 삭제 (toBuffUpdate로 위임)
		}
		
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {	
		// 버프 갱신 및 아이콘 유지 (15번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 15, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
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
			cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 5);
			cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow() - 5);
			cha.setDynamicAddHit(cha.getDynamicAddHit() - 5);
			cha.setDynamicAddHitBow(cha.getDynamicAddHitBow() - 5);
			cha.setBuffGlowingAura(false);
		}
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 15, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		
		// 종료 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "\\fY글로잉 웨폰 종료", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 멘트 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	ChattingController.toChatting(o, "\\fY글로잉 웨폰: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(Character cha, Skill skill) {
		// 패킷
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		// 시전가능 확인
		if (SkillController.isMagic(cha, skill, true) && cha instanceof PcInstance) {
			// 초기화
			PcInstance royal = (PcInstance) cha;
			List<object> list_temp = new ArrayList<object>();
			list_temp.add(royal);
			// 혈맹원 추출.
			Clan c = ClanController.find(royal);
			if (c != null) {
				for (PcInstance pc : c.getList()) {
					if (!list_temp.contains(pc))
						list_temp.add(pc);
				}
			}
			// 처리.
			for (object o : list_temp)
				onBuff(o, skill);
		}
	}
	static public void init2(Character cha, Skill skill) {
		// 시전가능 확인
		if (SkillController.isMagic(cha, skill, true) && cha instanceof PcInstance) {
			// 초기화
			PcInstance royal = (PcInstance) cha;
			List<object> list_temp = new ArrayList<object>();
			list_temp.add(royal);
			// 혈맹원 추출.
			Clan c = ClanController.find(royal);
			if (c != null) {
				for (PcInstance pc : c.getList()) {
					if (!list_temp.contains(pc))
						list_temp.add(pc);
				}
			}
			// 처리.
			for (object o : list_temp)
				onBuff(o, skill);
		}
	}
	/**
	 * 중복코드 방지용.
	 * 
	 * @param cha
	 * @param pc
	 * @param skill
	 */
	static public void onBuff(object o, Skill skill) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		BuffController.append(o, GlowingWeapon.clone(BuffController.getPool(GlowingWeapon.class), skill, skill.getBuffDuration()));
		
		// 시작 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "글로잉 웨폰: 추가 대미지+5, 추가 명중+5", Lineage.CHATTING_MODE_MESSAGE);
	}

}