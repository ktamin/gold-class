package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
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

public class EnchantVenom extends Magic {
	
	public EnchantVenom(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new EnchantVenom(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		// 1. 상태 설정 (공격 시 독 효과는 CalcPC 등에서 이 상태값을 체크하여 발동됨)
		o.setBuffEnchantVenom(true);
		
		// 아이콘 중복 방지를 위해 여기서는 삭제 (toBuffUpdate로 위임)
		
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {	
		// 2. 버프 갱신 및 아이콘 유지 (20번 - 인첸트 베놈 예상)
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			SC_BUFFICON_NOTI.on(pc, 20, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o){
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o){
		// 3. 상태 해제
		o.setBuffEnchantVenom(false);
		
		// 4. 종료 시 아이콘 즉시 삭제
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 20, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}

		// 종료 멘트 중복 방지 (주석 처리)
		// if(o instanceof Character){
		// 	Character cha = (Character) o;
		// 	ChattingController.toChatting(cha, "\\fY인첸트 베놈 효과가 종료되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
		// }		
	}

	static public void init(Character cha, Skill skill) {
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		if(SkillController.isMagic(cha, skill, true))
			onBuff(cha, skill, skill.getBuffDuration());
	}

	/**
	 * 중복코드 방지용
	 * @param cha
	 * @param o
	 * @param skill
	 * @param time
	 */
	static private void onBuff(Character cha, Skill skill, int time){
		if(cha!=null && skill!=null){
			// 버프 등록
			BuffController.append(cha, EnchantVenom.clone(BuffController.getPool(EnchantVenom.class), skill, time));
			
			// 패킷 처리
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			
			// 시작 멘트(필요 시 주석 처리) - 인첸트 베놈은 보통 멘트가 없습니다.
		}
	}

}