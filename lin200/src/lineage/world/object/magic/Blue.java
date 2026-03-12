package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class Blue extends Magic {

	public Blue(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new Blue(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;
			cha.setBuffBluePotion(true);
			// 아이콘(22번) 표시
			if (o instanceof PcInstance) {
				PcInstance pc = (PcInstance) o;
				SC_BUFFICON_NOTI.on(pc, 34, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			}
		//	cha.toSender(S_Ext_BuffTime.clone(BasePacketPooling.getPool(S_Ext_BuffTime.class), S_Ext_BuffTime.BUFFID_1889, getTime()));
		}
		toBuffUpdate(o);
		
	}
	
	public void toBuffUpdate(object o) {	
		// 버프 갱신 시 아이콘 유지
				if (o instanceof PcInstance) {
					PcInstance pc = (PcInstance) o;
					SC_BUFFICON_NOTI.on(pc, 34, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
				}
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;
			cha.setBuffBluePotion(false);
			ChattingController.toChatting(cha, "\\fY파란 물약 종료", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

	@Override
	public void toBuff(object o) {
		if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
			ChattingController.toChatting(o, "\\fY파란 물약: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	static public void init(Character cha, int time) {
		// ✅ 기존 블루 포션 버프 제거 (중첩 방지)
		BuffController.remove(cha, Blue.class);

		// ✅ 버프 새로 적용 (시간 덮어쓰기)
		Skill skill = SkillDatabase.find(204); // 204는 BluePotion 스킬 번호
		BuffController.append(cha, Blue.clone(BuffController.getPool(Blue.class), skill, time));

		// ✅ 안내 메시지 출력
		ChattingController.toChatting(cha, "파란 물약: MP 회복량 증가", Lineage.CHATTING_MODE_MESSAGE);
	}
}
