package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.PcInstance;

public class ShadowFang extends Magic {

	public ShadowFang(Skill skill){
		super(null, skill);
	}

	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new ShadowFang(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o){
		// 상태 체크 (중복 방지 로직은 유지하되, 아이콘 갱신은 흘러가도록)
		if (!o.isBuffShadowFang()) {
			o.setBuffShadowFang(true);
			
			if (o instanceof Character) {
				Character cha = (Character) o;
				cha.setDynamicAddDmg(cha.getDynamicAddDmg() + 5);
				cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
			}
		}

		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		// 버프 갱신 및 아이콘 유지 (51번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 51, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o){
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o){
		if (!o.isBuffShadowFang())
			return;

		o.setBuffShadowFang(false);

		if (o.isWorldDelete())
			return;

		if (o instanceof Character) {
			Character cha = (Character) o;
			cha.setDynamicAddDmg(cha.getDynamicAddDmg() - 5);
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 51, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		
		// 종료 멘트 삭제 (주석 처리)
		// ChattingController.toChatting(o, "\\fY쉐도우 팽 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	// [참고] 아이템에 사용하는 경우 (기존 코드 유지)
	static public void init(Character cha, Skill skill, int object_id){
		object o = cha.getInventory().value(object_id);
		if(o != null){
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);

			if(SkillController.isMagic(cha, skill, true) && o instanceof ItemWeaponInstance){
				BuffController.append(o, ShadowFang.clone(BuffController.getPool(ShadowFang.class), skill, skill.getBuffDuration()));
				cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			}
		}
	}

	// 캐릭터에게 사용하는 경우
	static public void init(Character cha, Skill skill){	
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);

		if (SkillController.isMagic(cha, skill, true)){
			BuffController.append(cha, ShadowFang.clone(BuffController.getPool(ShadowFang.class), skill, skill.getBuffDuration()));
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			
			// 시작 멘트 삭제 (주석 처리)
			// ChattingController.toChatting(cha, "쉐도우 팽: 근거리 대미지 +5", Lineage.CHATTING_MODE_MESSAGE);
		}
	}
}