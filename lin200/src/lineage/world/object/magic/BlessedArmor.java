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
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class BlessedArmor extends Magic {
	
	public BlessedArmor(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new BlessedArmor(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		if(o instanceof ItemInstance){
			ItemInstance item = (ItemInstance)o;
			
			// [수정] AC -3 (방어력 향상)
			// 기존(+3)은 방어력이 나빠지므로 수정했습니다.
			item.setDynamicAc( item.getDynamicAc() + 3 );
			
			// 장비 착용 중이라면 캐릭터 AC도 즉시 갱신
			if(item.isEquipped() && item.getCharacter()!=null){
				// 캐릭터 AC도 -3
				item.getCharacter().setAc(item.getCharacter().getAc() + 3);
				item.getCharacter().toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), item.getCharacter()));
			}
			
			// [수정] 아이템의 주인이 PC일 경우 아이콘 전송 (64번)
			if (item.getCharacter() instanceof PcInstance) {
				PcInstance pc = (PcInstance) item.getCharacter();
				SC_BUFFICON_NOTI.on(pc, 64, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			}
		}
		
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {	
		// [수정] 버프 갱신 시 아이콘 유지 (64번)
		if (o instanceof ItemInstance) {
			ItemInstance item = (ItemInstance)o;
			
			if (item.getCharacter() instanceof PcInstance) {
				PcInstance pc = (PcInstance) item.getCharacter();
				SC_BUFFICON_NOTI.on(pc, 64, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			}
		}
	}

	@Override
	public void toBuffStop(object o){
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o){
		if(o instanceof ItemInstance){
			ItemInstance item = (ItemInstance)o;
			
			// [수정] AC 원상복구 (+3)
			item.setDynamicAc( item.getDynamicAc() - 3 );
			
			if(item.isEquipped() && item.getCharacter()!=null && !item.getCharacter().isWorldDelete()){
				// 캐릭터 AC 원상복구 (+3)
				item.getCharacter().setAc(item.getCharacter().getAc() - 3);
				item.getCharacter().toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), item.getCharacter()));
				
				// 멘트 출력
				ChattingController.toChatting(item.getCharacter(), String.format("+%d %s 보통으로 돌아왔습니다.", item.getEnLevel(), item.getItem().getName()), Lineage.CHATTING_MODE_MESSAGE);
				
				// 종료 멘트 중복 방지 (주석 처리)
				// ChattingController.toChatting(item.getCharacter(), "\\fY블레스드 아머 종료", Lineage.CHATTING_MODE_MESSAGE);
				
				// [수정] 종료 시 아이콘 삭제 (주석 해제 및 64번 적용)
				if (item.getCharacter() instanceof PcInstance) {
					PcInstance pc = (PcInstance) item.getCharacter();
					SC_BUFFICON_NOTI.on(pc, 64, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
				}
			}
		}
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if(o instanceof ItemInstance && getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min){
		// 	ItemInstance item = (ItemInstance)o;
		// 	if(item.isEquipped() && item.getCharacter()!=null && !item.getCharacter().isWorldDelete()){
		// 		ChattingController.toChatting(item.getCharacter(),"\\fY블레스드 아머: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
		// 	}
		// }
	}


	static public void init(Character cha, Skill skill, int object_id){
		// 타겟 찾기 (인벤토리 내 아이템)
		object o = cha.getInventory().value(object_id);
		// 처리
		if(o != null){
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
			
			// 딜레이 패킷 (안전하게 캐스팅)
			if (cha instanceof PcInstance) {
				SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(700).send((PcInstance) cha);
			}
			
			if(SkillController.isMagic(cha, skill, true) && o instanceof ItemInstance)
				onBuff(cha, (ItemInstance)o, skill, skill.getBuffDuration());
		}
	}
	
	/**
	 * 중복코드 방지용.
	 */
	static public void onBuff(Character cha, ItemInstance item, Skill skill, int time){
		if(item!=null && skill!=null && item.getItem().getType2().equalsIgnoreCase("armor")){
			// 버프 등록 (아이템에 버프를 등록함)
			BuffController.append(item, BlessedArmor.clone(BuffController.getPool(BlessedArmor.class), skill, time));
			
			// 패킷 처리
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			ChattingController.toChatting(cha, String.format("+%d %s 한 순간 파랗게 빛납니다.", item.getEnLevel(), item.getItem().getName()), Lineage.CHATTING_MODE_MESSAGE);
			
			// 시작 멘트 중복 방지 (주석 처리)
			// ChattingController.toChatting(cha, "블레스드 아머: AC-3", Lineage.CHATTING_MODE_MESSAGE);
		}
	}
}