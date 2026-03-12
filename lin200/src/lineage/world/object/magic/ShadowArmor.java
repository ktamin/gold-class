package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
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

public class ShadowArmor extends Magic {
	
	public ShadowArmor(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new ShadowArmor(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		if(o instanceof Character){
			Character cha = (Character)o;
			
			// [수정] MR +5 적용 (AC 관련 코드 삭제됨)
			cha.setDynamicMr(cha.getDynamicMr() + 5);
			
			// 스탯 갱신 패킷 전송
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}
		
		// 아이콘 처리는 toBuffUpdate로 위임 (중복 방지)
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o){
		// 버프 갱신 및 아이콘 유지 (18번 - 쉐도우 아머)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 45, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
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
			
		if(o instanceof Character){
			Character cha = (Character)o;
			
			// [수정] MR -5 원상 복구
			cha.setDynamicMr(cha.getDynamicMr() - 5);
			
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 45, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		
		// 종료 멘트 삭제 (주석 처리)
		// ChattingController.toChatting(o, "\\fY쉐도우 아머 종료", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		//	ChattingController.toChatting(o, "\\fY쉐도우 아머: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	static public void init(Character cha, Skill skill){
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		if(SkillController.isMagic(cha, skill, true))
			onBuff(cha, skill);
	}

	static public void init(Character cha, int time){
		BuffController.append(cha, Shield.clone(BuffController.getPool(Shield.class), SkillDatabase.find(13, 2), time));
	}
	
	static public void onBuff(Character cha, Skill skill){
		cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);

		// [중복 방지] 방어 버프류 제거 (기존 로직 유지)
		BuffController.remove(cha, Shield.class);
		BuffController.remove(cha, EarthSkin.class);
		BuffController.remove(cha, EarthGuardian.class);
		BuffController.remove(cha, IronSkin.class);
		
		// 쉐도우 아머 적용
		BuffController.append(cha, ShadowArmor.clone(BuffController.getPool(ShadowArmor.class), skill, skill.getBuffDuration()));
		
		// 시작 멘트 삭제 (주석 처리)
		// ChattingController.toChatting(cha, "\\fY쉐도우 아머 :  MR + 5%", Lineage.CHATTING_MODE_MESSAGE);
	}

}