package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_BuffEva;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class ExpDropBuff_20 extends Magic {
	public ExpDropBuff_20(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new ExpDropBuff_20(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;		
			pc.setDynamicExp(pc.getDynamicExp() + 0.2);
//			pc.setAddDropItemRate(pc.getAddDropItemRate() + 0.2);
//			pc.setAddDropAdenRate(pc.getAddDropAdenRate() + 1);
			pc.toSender(S_BuffEva.clone(BasePacketPooling.getPool(S_BuffEva.class), pc, getTime()));
			
			// 💡 [아이콘 추가] 시작 시 112번 아이콘 출력
			SC_BUFFICON_NOTI.on(pc, 112, getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffUpdate(object o) {
		// 💡 [아이콘 추가] 리스/텔레포트 시 112번 아이콘 복구 및 시간 유지
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 112, getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
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
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;	
			pc.setDynamicExp(pc.getDynamicExp() - 0.2);
//			pc.setAddDropItemRate(pc.getAddDropItemRate() - 0.2);
//			pc.setAddDropAdenRate(pc.getAddDropAdenRate() - 1);
//			ChattingController.toChatting(o, String.format("\\fY%s 종료", getSkill().getName()), Lineage.CHATTING_MODE_MESSAGE);
			pc.toSender(S_BuffEva.clone(BasePacketPooling.getPool(S_BuffEva.class), pc, 0));
			
			// 💡 [아이콘 추가] 버프 종료 시 112번 아이콘 삭제
			SC_BUFFICON_NOTI.on(pc, 112, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuff(object o) {
		// 종료멘트 출력 주석
//		if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
//			ChattingController.toChatting(o, String.format("\\fY%s: %d초 후 종료", getSkill().getName(), getTime()), Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(object o, int time) {
		BuffController.append(o, ExpDropBuff_20.clone(BuffController.getPool(ExpDropBuff_20.class), SkillDatabase.find(702), time));
	}

	static public void init(Character cha, Skill skill) {
		BuffController.remove(cha, ExpDropBuff_10.class);
		BuffController.remove(cha, ExpDropBuff_50.class);
		
		// 💡 [추가된 코드] 경험치 2배 물약 버프 삭제
		BuffController.remove(cha, Exp_Potion.class);
		
		if (skill.getCastGfx() > 0)
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
		BuffController.append(cha, ExpDropBuff_20.clone(BuffController.getPool(ExpDropBuff_20.class), skill, skill.getBuffDuration()));
//		ChattingController.toChatting(cha, "경험치/드랍/아데나 물약: 경험치/드랍률+20%, 아데나 획득 2배 증가.", Lineage.CHATTING_MODE_MESSAGE);
	}
}
