package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
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
import lineage.world.object.instance.PcInstance;

public class DecreaseWeight extends Magic {
	
	public DecreaseWeight(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new DecreaseWeight(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
	
	@Override
	public void toBuffStart(object o){
		o.setBuffDecreaseWeight(true);
		
		// 무게 정보 갱신을 위해 스탯 패킷 전송
		if(o instanceof Character)
			o.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), (Character)o));
			
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {
		// [추가] 버프 갱신 및 아이콘 유지 (63번)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 63, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o){
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o){
		if(o.isWorldDelete())
			return;
			
		o.setBuffDecreaseWeight(false);
		
		if(o instanceof Character) {
			// [수정] 중복되던 패킷 하나로 통합 및 멘트 정리
			o.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), (Character)o));
			// ChattingController.toChatting(o, "\\fY디크리즈 웨이트 종료", Lineage.CHATTING_MODE_MESSAGE);
		}
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 63, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}
	
	static public void init(Character cha, Skill skill){
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		// [수정] 안전하게 형변환 검사 후 딜레이 패킷 전송
		if (cha instanceof PcInstance) {
			SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(700).send((PcInstance) cha);
		}
		
		if(SkillController.isMagic(cha, skill, true))
			onBuff(cha, skill);
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	ChattingController.toChatting(o, "\\fY디크리즈 웨이트: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	/**
	 * 중복코드 방지용
	 *  : 마법주문서 (디크리즈 웨이트) 에서도 사용중.
	 * @param cha
	 * @param skill
	 */
	static public void onBuff(Character cha, Skill skill){
		cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
		BuffController.append(cha, DecreaseWeight.clone(BuffController.getPool(DecreaseWeight.class), skill, skill.getBuffDuration()));
		
		// 시작 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(cha, "디크리즈 웨이트: 최대무게+180", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	/*
	 * 운영자 올버프에서 사용중
	 * */
	static public void onBuff(object o, Skill skill) {
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		
		BuffController.append(o, DecreaseWeight.clone(BuffController.getPool(DecreaseWeight.class), skill, skill.getBuffDuration()));
	}
	
}