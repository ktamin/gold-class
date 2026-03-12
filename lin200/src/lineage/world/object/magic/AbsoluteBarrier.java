package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
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

public class AbsoluteBarrier extends Magic {

	public AbsoluteBarrier(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new AbsoluteBarrier(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setBuffAbsoluteBarrier(true);
		
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {
		// [추가] 버프 갱신 및 아이콘 유지 (77번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 77, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o){
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o){
		o.setBuffAbsoluteBarrier(false);
		
		// 앱솔루트 배리어 해제 이펙트 (14539)
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 14539), true);
		
		// 종료 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "\\fY앱솔루트 배리어 종료", Lineage.CHATTING_MODE_MESSAGE);
		
		// [추가] 종료 시 아이콘 삭제 (77번)
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 77, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}
	
	static public void init(Character cha, Skill skill){
		if (cha.getMap() == Lineage.teamBattleMap || cha.getMap() == Lineage.BattleRoyalMap) {
			ChattingController.toChatting(cha, "이곳에선 해당 스킬을 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		// 모션 처리
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		// [수정] 안전하게 형변환 검사 후 딜레이 패킷 전송
		if (cha instanceof PcInstance) {
			SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(8600).send((PcInstance) cha);
		}
		
		if(SkillController.isMagic(cha, skill, true)){
			// 시전 이펙트
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			// 버프 적용
			BuffController.append(cha, AbsoluteBarrier.clone(BuffController.getPool(AbsoluteBarrier.class), skill, skill.getBuffDuration()));
		}
	}
	
	static public void onBuff(object o, Skill skill, int time){
		if (o.getMap() == Lineage.teamBattleMap || o.getMap() == Lineage.BattleRoyalMap) {
			ChattingController.toChatting(o, "이곳에선 해당 스킬을 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		BuffController.append(o, AbsoluteBarrier.clone(BuffController.getPool(AbsoluteBarrier.class), skill, time));
	}
	
}