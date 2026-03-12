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
import lineage.world.object.instance.PcInstance;

public class DressMighty extends Magic {

	public DressMighty(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new DressMighty(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
	
	@Override
	public void toBuffStart(object o){
		if(o instanceof Character){
			Character cha = (Character)o;
			// STR 증가 (DB에 설정된 maxdmg 값만큼, 보통 3)
			cha.setDynamicStr( (int) (cha.getDynamicStr() + skill.getMaxdmg()) );
			
			// 스탯 갱신 패킷 전송 (창에 수치 바로 반영)
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}
		
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o){
		// 버프 갱신 및 아이콘 유지 (43번)
		// 기존 S_BuffStr 패킷은 구형이므로 제거하고 신규 규격 사용
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 43, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
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
			// STR 원상복구
			cha.setDynamicStr( (int) (cha.getDynamicStr() - skill.getMaxdmg()) );
			
			// 스탯 갱신
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		}
		
		// 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 43, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}

		// 종료 멘트 중복 방지 (주석 처리)
		// if(o instanceof Character){
		// 	Character cha = (Character) o;
		// 	ChattingController.toChatting(cha, "\\fY드레스 마이티 효과가 종료되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
		// }	
	}
	
	static public void init(Character cha, Skill skill) {
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		if(SkillController.isMagic(cha, skill, true)) {
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			
			// 인챈트 마이티와 중복 불가 (제거)
			BuffController.remove(cha, EnchantMighty.class);
			
			BuffController.append(cha, DressMighty.clone(BuffController.getPool(DressMighty.class), skill, skill.getBuffDuration()));
			
			// 시작 멘트 중복 방지 (주석 처리)
			// ChattingController.toChatting(cha, "드레스 마이티 :  STR+3", Lineage.CHATTING_MODE_MESSAGE);
		}
	}

}