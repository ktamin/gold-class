package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class Exp_Potion extends Magic {

	public Exp_Potion(Character cha, Skill skill) {
		super(cha, skill); // 부모 클래스에 시전자 정보를 확실히 전달
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Character cha, Skill skill, int time) {
		if (bi == null)
			bi = new Exp_Potion(cha, skill);

		bi.setCharacter(cha);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setBuffExpPotion(true);
		// [아이콘] 시작 시 79번 출력
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 79, getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffUpdate(object o) {
		o.setBuffExpPotion(true);
		// [아이콘] 리스 시 79번 복구 핵심 로직
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 79, getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		o.setBuffExpPotion(false);
		// [아이콘] 종료 시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 79, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		ChattingController.toChatting(o, "\\fY경험치 2배 물약 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	@Override
	public void toBuff(object o) {
		if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
			ChattingController.toChatting(o, "\\fY경험치 2배 물약: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void onBuff(Character cha, Skill skill, int time, boolean restart) {
		// 중복 제거 및 시간 합산 (원본 로직 유지)
		if (!restart)
			time = BuffController.addBuffTime(cha, skill, time);

		if (skill.getCastGfx() > 0)
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()),
					true);

		// BuffController에 등록 (NPE 방지를 위해 cha 전달)
		BuffController.append(cha, Exp_Potion.clone(BuffController.getPool(Exp_Potion.class), cha, skill, time));

		ChattingController.toChatting(cha, "경험치 2배 효과 적용", Lineage.CHATTING_MODE_MESSAGE);
	}
}