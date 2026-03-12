package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class EnchantDexterity extends Magic {

	public EnchantDexterity(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new EnchantDexterity(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		if(o instanceof Character){
			Character cha = (Character)o;
			// DEX +5
			cha.setDynamicDex( cha.getDynamicDex() + 5 );
			
			// [추가] 스탯 창 즉시 갱신 (정보창 반영)
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}
		
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o){
		// [수정] 구형 패킷(S_BuffDex) 삭제 후 신규 아이콘(6번) 적용
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 6, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
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
			// DEX -5 (원상복구)
			cha.setDynamicDex( cha.getDynamicDex() - 5 );
			
			// [추가] 스탯 창 즉시 갱신
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
			
			// 종료 멘트 중복 방지 (주석 처리)
			// ChattingController.toChatting(cha, "\\fY피지컬 인챈트: DEX 종료", Lineage.CHATTING_MODE_MESSAGE);
			
			// 구형 패킷 삭제
			// if(Lineage.server_version>144) ...
		}
		
		// [추가] 종료 시 아이콘 즉시 삭제 (6번)
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 6, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	ChattingController.toChatting(o, "\\fY피지컬 인챈트 DEX: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	static public void init(Character cha, Skill skill, int object_id){
		// 초기화
		object o = null;
		// 타겟 찾기
		if(object_id == cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList( object_id );
		// 처리
		if(o != null){
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
			
			// [추가] 시전 딜레이 패킷
			if (cha instanceof PcInstance) {
				SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(700).send((PcInstance) cha);
			}
			
			if(SkillController.isMagic(cha, skill, true)){
				if(SkillController.isFigure(cha, o, skill, false, SkillController.isClan(cha, o))){
					onBuff(o, skill);
				}else{
					// \f1마법이 실패했습니다.
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 280));
				}
			}
		}
	}
	
	static public void init(Character cha, int time){
		BuffController.append(cha, EnchantDexterity.clone(BuffController.getPool(EnchantDexterity.class), SkillDatabase.find(4, 1), time));
	}
	
	static public void onBuff(object o, Skill skill){
		o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
		
		// 중복 버프 제거 (드레스 덱스터리티)
		BuffController.remove(o, DressDexterity.class);
		
		BuffController.append(o, EnchantDexterity.clone(BuffController.getPool(EnchantDexterity.class), skill, skill.getBuffDuration()));
		
		// 시작 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "피지컬 인챈트 DEX: DEX+5", Lineage.CHATTING_MODE_MESSAGE);
	}
	
}