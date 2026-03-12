package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectSpeed;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class HolyWalk extends Magic {

	public HolyWalk(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new HolyWalk(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		o.setBrave(true);
		
		if(Lineage.server_version > 200)
			o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 1, 1, getTime()), true);
		else
			o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 1, 1, getTime()), true);
			
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		o.setBrave(true);
		
		// 속도 패킷 전송
		if(Lineage.server_version > 200)
			o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 1, 1, getTime()), true);
		else
			o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 1, 1, getTime()), true);
			
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			
			// [중요] 용기/엘븐와퍼 아이콘(2번) 강제 삭제
			// 홀리 워크 사용 시 용기 아이콘이 같이 뜨는 것을 막습니다.
			SC_BUFFICON_NOTI.on(pc, 2, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			
			// [추가] 홀리 워크 아이콘 출력 (70번)
			SC_BUFFICON_NOTI.on(pc, 70, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
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
			
		o.setBrave(false);
		o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 1, 0, 0), true);
		
		// 캔슬레이션 등으로 버프가 종료될 때 아이콘을 확실히 지웁니다.
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			
			// [중요] 홀리 워크 아이콘 삭제 (70번)
			SC_BUFFICON_NOTI.on(pc, 70, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			
			// [중요] 용기 아이콘 삭제 (2번)
			// 혹시라도 남아있을 2번 아이콘도 같이 삭제합니다.
			SC_BUFFICON_NOTI.on(pc, 2, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}
	
	@Override
	public void toBuff(object o) {
		if (getTime() == 1)
			o.speedCheck = System.currentTimeMillis() + 2000;
	}
	
	static public void init(Character cha, Skill skill){
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		if(SkillController.isMagic(cha, skill, true)){
			// 패킷 처리
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			
			// 딜레이 패킷
			if (cha instanceof PcInstance) {
				SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(1100).send((PcInstance) cha);
			}
			
			// 처리.
			if(cha.getSpeed() != 2){
				// [중요] 기존 용기(Bravery) 효과 제거
				// 용기 물약 상태였다면 제거하고 홀리 워크로 덮어씌웁니다.
				BuffController.remove(cha, Bravery.class);
				
				// 슬로우 상태가 아닐경우 버프 적용
				BuffController.append(cha, HolyWalk.clone(BuffController.getPool(HolyWalk.class), skill, skill.getBuffDuration()));
				
			}else{
				// 슬로우 상태일경우 슬로우 제거 (상쇄)
				BuffController.remove(cha, Slow.class);
			}
		}
	}
	
	static public void init(Character cha, int time){
		// 적용
		if (cha.getClassType() == Lineage.LINEAGE_CLASS_WIZARD)
			BuffController.append(cha, HolyWalk.clone(BuffController.getPool(HolyWalk.class), SkillDatabase.find(7, 3), time));
	}
}