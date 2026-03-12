package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class ArmorBreak extends Magic {

	public ArmorBreak(Skill skill) {
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
		if (bi == null)
			bi = new ArmorBreak(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		if (o instanceof Character) {
			Character cha = (Character) o;
			cha.setBuffArmorBreak(true);
		}
		
		// 아이콘 처리는 toBuffUpdate로 위임 (상대방 화면에 출력됨)
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {
		// 버프 갱신 및 아이콘 유지 (53번)
		// 여기서 o는 마법에 걸린 '상대방'입니다. 따라서 상대방 화면에 아이콘이 뜹니다.
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 53, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
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
			cha.setBuffArmorBreak(false);
		}
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 53, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	static public void init(Character cha, Skill skill, int object_id) {

		object o = null;

		if (object_id == cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList(object_id);

		if (o != null) {

			if (SkillController.isMagic(cha, skill, true)) {
				// 상대방에게 마법 성공 확률 체크
				if (SkillController.isFigure(cha, o, skill, true, false)) {
					onBuff(o, skill, skill.getBuffDuration());
					return;
				}else {
					// 실패 메시지는 시전한 사람(cha)에게 보임
					ChattingController.toChatting(cha, "마법이 실패하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
				}
			}
		}
	}

	static public void onBuff(object o, Skill skill, int time) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		BuffController.append(o, ArmorBreak.clone(BuffController.getPool(ArmorBreak.class), skill, time));
		
		// 성공 시 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "일정시간동안 입는 피해가 크게 증가합니다.", Lineage.CHATTING_MODE_MESSAGE);
	}

}