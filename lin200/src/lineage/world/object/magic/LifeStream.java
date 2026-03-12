package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.PcInstance;

public class LifeStream extends Magic {
	
	private BackgroundInstance lifeStream;
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new LifeStream(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
	
	public LifeStream(Skill skill){
		super(null, skill);
		
		lifeStream = new lineage.world.object.npc.background.LifeStream();
		lifeStream.setGfx(2231);
		lifeStream.setLight(6);
	}
		
	@Override
	public void toBuffStart(object o){
		lifeStream.setObjectId(ServerDatabase.nextEtcObjId());
		lifeStream.toTeleport(o.getX(), o.getY(), o.getMap(), false);
		
		// 시전 멘트 중복 방지 (주석 처리)
		// ChattingController.toChatting(o, "라이프 스트림: 일정 범위 안의 캐릭터 HP회복량이 증가", Lineage.CHATTING_MODE_MESSAGE);
		
		// toBuffUpdate는 NPC 위치 고정이므로 호출하지 않거나, 필요 시 추가
	}

	@Override
	public void toBuff(object o) {
		// 1. 범위(2셀) 내에 진입한 대상 처리
		for(object oo : lifeStream.getInsideList()){
			// 살아있고, 캐릭터이며, 거리가 2칸 이내이고, 아직 리스트에 없는 경우
			if(!oo.isDead() && oo instanceof Character && Util.isDistance(lifeStream, oo, 2) && !lifeStream.isContainsList(oo)){
				Character cha = (Character) oo;
				lifeStream.appendList(oo);
				
				// HP 틱 회복량 증가 (+10)
				cha.setDynamicTicHp(cha.getDynamicTicHp() + 10);
				
				// [추가] 범위 내 대상에게 아이콘 출력 (71번)
				if (cha instanceof PcInstance) {
					SC_BUFFICON_NOTI.on((PcInstance)cha, 71, getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
				}
				
				// 진입 멘트 (주석 처리)
				// ChattingController.toChatting(cha, "라이프 스트림: HP의 회복량 증가", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
		
		// 2. 범위를 벗어난 대상 처리
		for (object oo : lifeStream.getList()) {
			// 거리가 멀어졌거나 죽은 경우
			if (!Util.isDistance(lifeStream, oo, 2) || oo.isDead()) {
				Character cha = (Character) oo;
				lifeStream.removeList(oo);
				
				// HP 틱 회복량 원상복구 (-10)
				cha.setDynamicTicHp(cha.getDynamicTicHp() - 10);
				
				// [추가] 범위 이탈 시 아이콘 삭제 (71번)
				if (cha instanceof PcInstance) {
					SC_BUFFICON_NOTI.on((PcInstance)cha, 71, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
				}
				
				// 이탈 멘트 (주석 처리)
				// ChattingController.toChatting(cha, "\\fY라이프 스트림 종료", Lineage.CHATTING_MODE_MESSAGE);
			}
		}
		
		// 시간 알림 (필요 시 주석 해제)
		// if (getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min)
		// 	ChattingController.toChatting(o, "\\fY라이프 스트림: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
	}

	@Override
	public void toBuffUpdate(object o) {
		// 라이프 스트림 오브젝트 위치 갱신 (보통 고정이지만 필요 시 유지)
		if(lifeStream != null)
			lifeStream.toTeleport(o.getX(), o.getY(), o.getMap(), false);
	}

	@Override
	public void toBuffEnd(object o){
		// 3. 마법 종료 시 현재 범위 내에 있던 모든 대상 처리
		for (object oo : lifeStream.getList()) {
			Character cha = (Character) oo;
			// 리스트 제거 로직은 루프 끝나고 clearList로 처리하므로 여기선 스탯만 복구
			
			// HP 틱 회복량 원상복구 (-10)
			cha.setDynamicTicHp(cha.getDynamicTicHp() - 10);
			
			// [추가] 종료 시 아이콘 삭제 (71번)
			if (cha instanceof PcInstance) {
				SC_BUFFICON_NOTI.on((PcInstance)cha, 71, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			}
			
			// 종료 멘트 (주석 처리)
			// ChattingController.toChatting(cha, "\\fY라이프 스트림 종료", Lineage.CHATTING_MODE_MESSAGE);
		}
		
		lifeStream.clearList(true);
		World.remove(lifeStream);
	}
	
	@Override
	public void toBuffStop(object o) {
		toBuffEnd(o);
	}
	
	static public void init(Character cha, Skill skill){
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
		
		// [추가] 시전 딜레이 패킷 (안전하게 캐스팅)
		if (cha instanceof PcInstance) {
			SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(1100).send((PcInstance) cha);
		}
		
		if(SkillController.isMagic(cha, skill, true)){
			// 버프 등록 (시전자에게 등록되어 매 틱마다 주변을 체크함)
			BuffController.append(cha, LifeStream.clone(BuffController.getPool(LifeStream.class), skill, skill.getBuffDuration()));
		}
	}
	
}