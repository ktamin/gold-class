package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class SolidCarriage extends Magic {

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new SolidCarriage(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	public SolidCarriage(Skill skill) {
		super(null, skill);
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;
			cha.setDynamicEr(cha.getDynamicEr() + 15);
		}
		
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {	
		// 버프 갱신 및 아이콘 유지 (23번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 23, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
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
			cha.setDynamicEr(cha.getDynamicEr() - 15);
		}
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 23, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		
		// 종료 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(cha, "\\fY솔리드 캐리지 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	@Override
	public void toBuff(object o) {
		// 시간 알림 멘트 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	ChattingController.toChatting(o, "\\fY솔리드 캐리지: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	/**
	 * 
	 * @param cha
	 * @param skill
	 */
	static public void init(Character cha, Skill skill) {
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		// 방패 또는 가더 착용 체크
		if ((cha.getInventory().getSlot(Lineage.SLOT_SHIELD) != null || cha.getInventory().getSlot(Lineage.SLOT_GUARDER) != null) && SkillController.isMagic(cha, skill, true)) {
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			BuffController.append(cha, SolidCarriage.clone(BuffController.getPool(SolidCarriage.class), skill, skill.getBuffDuration()));
			
			// 시작 멘트 중복 방지 (주석 처리)
			// ChattingController.toChatting(cha, "솔리드 캐리지: ER(원거리 회피)+15, 방패 장착 해제시 종료", Lineage.CHATTING_MODE_MESSAGE);
		} else {
			// 실패 멘트는 유지 (이건 시스템 메시지가 아니라 에러 메시지이므로)
			if (cha.getInventory().getSlot(Lineage.SLOT_SHIELD) == null && cha.getInventory().getSlot(Lineage.SLOT_GUARDER) == null)
				ChattingController.toChatting(cha, "\\fY방패 또는 가더를 착용해야 사용가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
		}

	}

}