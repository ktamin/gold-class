package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.MonsterSkill;
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
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public class DecayPotion extends Magic {

	public DecayPotion(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new DecayPotion(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		// 중복 실행 방지
		if (o.isBuffDecayPotion())
			return;

		o.setBuffDecayPotion(true);
		
		// [아이콘] 74번 출력
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 74, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		
		// [주석 처리] 시작 멘트 차단
		// ChattingController.toChatting(o, "디케이 포션: 회복용 물약 사용 불가", Lineage.CHATTING_MODE_MESSAGE);
	}

	@Override
	public void toBuffUpdate(object o) {
		// 업데이트(리스타트 등) 시 아이콘 유지
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 74, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o){
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o){
		o.setBuffDecayPotion(false);
		
		// [아이콘] 종료 시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance) o, 74, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		
		// [주석 처리] 종료 멘트 차단
		// ChattingController.toChatting(o, "\\fY디케이 포션 종료", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	static public void init(Character cha, Skill skill, int object_id){
		object o = (object_id == cha.getObjectId()) ? cha : cha.findInsideList(object_id);
		
		if(o != null){
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
			
			if(SkillController.isMagic(cha, skill, true)) {
				if(SkillController.isFigure(cha, o, skill, true, false)) {
					// 기존 버프 제거 후 다시 추가 (시간 갱신)
					BuffController.remove(o, DecayPotion.class);
					
					o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
					BuffController.append(o, DecayPotion.clone(BuffController.getPool(DecayPotion.class), skill, skill.getBuffDuration()));
				}
				Detection.onBuff(cha);
			}
		}
	}
	
	static public void init(MonsterInstance mi, object o, MonsterSkill ms){					
		if(o != null){
			if(SkillController.isMagic(mi, ms, true)) {
				// 이미 걸려있다면 무시 (도배 방지)
				if (o.isBuffDecayPotion())
					return;

				if(SkillController.isFigure(mi, o, ms.getSkill(), false, false)) {
					o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 7781), true);
					BuffController.append(o, DecayPotion.clone(BuffController.getPool(DecayPotion.class), ms.getSkill(), ms.getSkill().getBuffDuration()));
				}		
			}
		}
	}
}