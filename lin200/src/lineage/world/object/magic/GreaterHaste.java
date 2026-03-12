package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectSpeed;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class GreaterHaste extends Magic {

	public GreaterHaste(Skill skill) {
		super(null, skill);	
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time, boolean restart) {
		if (bi == null)
			bi = new GreaterHaste(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}

	@Override
	public void toBuffStart(object o) {
		o.setSpeed(1);
		o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 0, o.getSpeed(), getTime()), true);
		// \f1갑자기 빠르게 움직입니다.
		o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 184));
		
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			// [중요] 그레이터 헤이스트 시작 시, 혹시 남아있을지 모르는 헤이스트 아이콘(0번)을 강제로 삭제합니다.
			SC_BUFFICON_NOTI.on(pc, 0, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			
			// [추가] 그레이터 헤이스트 아이콘 출력 (68번)
			SC_BUFFICON_NOTI.on(pc, 68, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
		
		// 아이콘 처리는 toBuffUpdate로 위임
		toBuffUpdate(o);
	}

	@Override
	public void toBuffUpdate(object o) {
		o.setSpeed(1);
		o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 0, o.getSpeed(), getTime()), true);
		// \f1다리에 새 힘이 솟습니다.
		o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 183));
		
		if (o instanceof PcInstance) {
			PcInstance pc = (PcInstance) o;
			// 갱신 시에도 0번은 확실히 지움
			SC_BUFFICON_NOTI.on(pc, 0, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			// 68번 유지
			SC_BUFFICON_NOTI.on(pc, 68, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}

	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o) {
		if (o.isWorldDelete())
			return;
		o.setSpeed(0);
		o.toSender(S_ObjectSpeed.clone(BasePacketPooling.getPool(S_ObjectSpeed.class), o, 0, o.getSpeed(), 0), true);
		// \f1느려지는 것을 느낍니다.
		o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 185));
		
		// [추가] 종료 시 아이콘 삭제 (68번)
		if (o instanceof PcInstance) {
			SC_BUFFICON_NOTI.on((PcInstance)o, 68, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
		}
	}
	
	@Override
	public void toBuff(object o) {
		if (getTime() == 1)
			o.speedCheck = System.currentTimeMillis() + 2000;
	}

	static public void init(Character cha, Skill skill, long object_id) {
		// 초기화
		object o = null;
		
		// 타겟 찾기
		if (object_id == cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList(object_id);
		
		// 처리
		if (o != null) {
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
			
			// 시전 딜레이 패킷
			if (cha instanceof PcInstance) {
				SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(700).send((PcInstance) cha);
			}

			if (SkillController.isMagic(cha, skill, true) && SkillController.isFigure(cha, o, skill, false, false)) {
				
				if (o.getSpeed() == 2) {
					// 슬로우 제거.
					BuffController.remove(o, Slow.class);
					return;
				}
				
				// 기존 가속 버프 제거
				BuffController.remove(o, HastePotionMagic.class);
				BuffController.remove(o, Haste.class);
				BuffController.remove(o, GreaterHaste.class);
				
				// 추가적으로 패킷을 한 번 더 보내 확실하게 아이콘 삭제 시도 (init 시점)
				if (o instanceof PcInstance) {
					SC_BUFFICON_NOTI.on((PcInstance)o, 0, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
				}
				
				o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
				
				BuffController.append(o, GreaterHaste.clone(BuffController.getPool(GreaterHaste.class), skill, skill.getBuffDuration(), false));
			}
		}
	}

}