package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.PcInstance;

public class EnchantWeapon extends Magic {
	
	public EnchantWeapon(Skill skill){
		super(null, skill);
	}
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time){
		if(bi == null)
			bi = new EnchantWeapon(skill);
		bi.setSkill(skill);
		bi.setTime(time);
		return bi;
	}
		
	@Override
	public void toBuffStart(object o){
		o.setBuffEnchantWeapon(true);
	
		// [수정] o는 무기(ItemInstance)입니다. 무기의 주인(PcInstance)을 찾아 아이콘을 보냅니다.
		if (o instanceof ItemInstance) {
			ItemInstance item = (ItemInstance) o;
			if (item.getCharacter() instanceof PcInstance) {
				PcInstance pc = (PcInstance) item.getCharacter();
				// 아이콘 18번 출력
				SC_BUFFICON_NOTI.on(pc, 18, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			}
		}
	
		// 아이콘 처리는 toBuffUpdate로 위임 (갱신)
		toBuffUpdate(o);	
	}

	@Override
	public void toBuffUpdate(object o) {	
		// [수정] 버프 갱신 시 아이콘 유지
		if (o instanceof ItemInstance) {
			ItemInstance item = (ItemInstance) o;
			
			if (item.getCharacter() instanceof PcInstance) {
				PcInstance pc = (PcInstance) item.getCharacter();
				SC_BUFFICON_NOTI.on(pc, 18, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
			}
		}
	}

	@Override
	public void toBuffStop(object o){
		toBuffEnd(o);
	}

	@Override
	public void toBuffEnd(object o){
		o.setBuffEnchantWeapon(false);
		
		if(o instanceof ItemInstance){
			ItemInstance weapon = (ItemInstance)o;
			
			if(weapon.getCharacter() != null) {
				// 무기가 보통으로 돌아왔습니다.
				ChattingController.toChatting(weapon.getCharacter(), String.format("+%d %s 보통으로 돌아왔습니다.", weapon.getEnLevel(), weapon.getItem().getName()), Lineage.CHATTING_MODE_MESSAGE);
				
				// 종료 멘트 중복 방지 (주석 처리)
				// ChattingController.toChatting(weapon.getCharacter(), "\\fY인챈트 웨폰 종료", Lineage.CHATTING_MODE_MESSAGE);
				
				// [수정] 종료 시 아이콘 삭제 (주석 해제 및 적용)
				if (weapon.getCharacter() instanceof PcInstance) {
					PcInstance pc = (PcInstance) weapon.getCharacter();
					SC_BUFFICON_NOTI.on(pc, 18, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
				}
			}
		}
	}
	
	@Override
	public void toBuff(object o) {
		// 시간 알림 (필요 시 주석 해제)
		// if(o instanceof ItemInstance && getTime() == Lineage.buff_magic_time_max || getTime() == Lineage.buff_magic_time_min){
		// 	ItemInstance weapon = (ItemInstance)o;
		// 	if(weapon.isEquipped() && weapon.getCharacter()!=null && !weapon.getCharacter().isWorldDelete()){
		// 		ChattingController.toChatting(weapon.getCharacter(),"\\fY인챈트 웨폰: " + getTime() + "초 후 종료", Lineage.CHATTING_MODE_MESSAGE);
		// 	}
		// }
	}

	static public void init(Character cha, Skill skill, int object_id){
		// 타겟 찾기 (인벤토리 내 아이템)
		object o = cha.getInventory().value(object_id);
		
		// 처리
		if(o != null){
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
			
			if (cha instanceof PcInstance) {
				SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(700).send((PcInstance) cha);
			}
			
			if(SkillController.isMagic(cha, skill, true) && o instanceof ItemWeaponInstance)
				onBuff(cha, o, skill, skill.getBuffDuration());
		}
	}

	/**
	 * 중복코드 방지용
	 */
	static public void onBuff(Character cha, object o, Skill skill, int time){
		if(cha!=null && o!=null && skill!=null){
			ItemWeaponInstance weapon = (ItemWeaponInstance) o;
			
			if (weapon.getItem().getName().contains("화살"))
				return;
				
			// 홀리웨폰 삭제 (중복 불가)
			BuffController.remove(o, HolyWeapon.class);
			
			// 블레스 웨폰 (상위 버프 체크)
			if (BuffController.find(o, SkillDatabase.find(48)) != null) {
				ChattingController.toChatting(cha, "\\fY무기에 인첸트 웨폰 효과가 부여되어 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			// 버프 등록
			BuffController.append(o, EnchantWeapon.clone(BuffController.getPool(EnchantWeapon.class), skill, time));		
			
			// 패킷 처리
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, skill.getCastGfx()), true);
			ChattingController.toChatting(cha, String.format("+%d %s 한 순간 파랗게 빛납니다.", weapon.getEnLevel(), weapon.getItem().getName()), Lineage.CHATTING_MODE_MESSAGE);
			
			// 시작 멘트 중복 방지 (주석 처리)
			// ChattingController.toChatting(cha, "인챈트 웨폰: 근거리 대미지+2", Lineage.CHATTING_MODE_MESSAGE);
		}
	}
}