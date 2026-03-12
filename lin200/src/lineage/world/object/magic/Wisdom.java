package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterSpMr;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class Wisdom extends Magic {

	public Wisdom(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new Wisdom(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;
			cha.setBuffWisdom(true);
			// SP+2, MP틱+2
			cha.setDynamicSp(cha.getDynamicSp() + 2);
			cha.setDynamicTicMp(cha.getDynamicTicMp() + 2);
			
			// 스탯 갱신 패킷 (정보창 갱신)
			cha.toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), cha));
		}

		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	public void toBuffUpdate(object o) {
		// 버프 갱신 및 아이콘 유지 (3번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 3, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
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
			cha.setBuffWisdom(false);
			// 스탯 원상복구
			cha.setDynamicSp(cha.getDynamicSp() - 2);
			cha.setDynamicTicMp(cha.getDynamicTicMp() - 2);
			
			// 스탯 갱신
			cha.toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), cha));
			
			// 종료 멘트 중복 방지 (주석 처리)
			// ChattingController.toChatting(cha, "\\fY지혜의 물약 종료", Lineage.CHATTING_MODE_MESSAGE);
		}
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 3, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	ChattingController.toChatting(o, "\\fY지혜의 물약: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(Character cha, int time) {
		// 기존 버프 제거 (중첩 방지)
		BuffController.remove(cha, Wisdom.class);
		// 지혜 적용
		BuffController.append(cha, Wisdom.clone(BuffController.getPool(Wisdom.class), SkillDatabase.find(203), time));
		
		// 시작 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(cha, "지혜의 물약: SP+2, MP 회복+2", Lineage.CHATTING_MODE_MESSAGE);
	}
}