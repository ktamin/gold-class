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

public class MaanBirth extends Magic {

	public MaanBirth(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new MaanBirth(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;
			cha.setBuffMaanBirth(true);
			// 정령 내성+5%
			cha.setDynamicElfResist(cha.getDynamicElfResist() + 0.05);
		}
		
		// [추가] 버프 시작 시 아이콘(95번) 출력
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 95, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}
	

	@Override
	public void toBuffUpdate(object o) {
		// [추가] 버프 시간 갱신 (재접속 하거나 시간이 흐를 때 아이콘 시간 동기화)
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 95, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
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
			cha.setBuffMaanBirth(false);
			// 정령 내성-5%
			cha.setDynamicElfResist(cha.getDynamicElfResist() - 0.05);

			ChattingController.toChatting(cha, "\\fY탄생의 마안 종료", Lineage.CHATTING_MODE_MESSAGE);
		}
		// [추가] 버프 종료 시 아이콘 삭제 (시간을 0으로 보내서 삭제)
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 95, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuff(object o) {
		if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
			ChattingController.toChatting(o, "\\fY탄생의 마안: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(object o, int time) {
		BuffController.append(o, MaanBirth.clone(BuffController.getPool(MaanBirth.class), SkillDatabase.find(619), time));
	}

	static public void init(Character cha, Skill skill) {
		BuffController.remove(cha, MaanWatar.class);
		BuffController.remove(cha, MaanWind.class);
		BuffController.remove(cha, MaanEarth.class);
		BuffController.remove(cha, MaanFire.class);
		BuffController.remove(cha, MaanShape.class);
		BuffController.remove(cha, MaanLife.class);

		cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
		BuffController.append(cha, MaanBirth.clone(BuffController.getPool(MaanBirth.class), skill, skill.getBuffDuration()));
		ChattingController.toChatting(cha, "탄생의 마안: 일정 확률로 물리 회피 상승, 받는 마법 대미지 50% 감소, 정령 내성+5%", Lineage.CHATTING_MODE_MESSAGE);
	}
}
