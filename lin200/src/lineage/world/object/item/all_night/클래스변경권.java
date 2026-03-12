package lineage.world.object.item.all_night;

import java.util.ArrayList;
import java.util.List;

import lineage.database.CharactersDatabase;
import lineage.network.LineageServer;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Disconnect;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.CharacterController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class 클래스변경권 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new 클래스변경권();
		return item;
	}
	
	public String getClassName(String action) {
		if (action.equalsIgnoreCase("0")) {
			return "(남) 군주";
		} else if (action.equalsIgnoreCase("1")) {
			return "(여) 군주";
		} else if (action.equalsIgnoreCase("2")) {
			return "(남) 기사";
		} else if (action.equalsIgnoreCase("3")) {
			return "(여) 기사";
		} else if (action.equalsIgnoreCase("4")) {
			return "(남) 요정";
		} else if (action.equalsIgnoreCase("5")) {
			return "(여) 요정";
		} else if (action.equalsIgnoreCase("6")) {
			return "(남) 마법사";
		} else if (action.equalsIgnoreCase("7")) {
			return "(여) 마법사";
		} else if (action.equalsIgnoreCase("8")) {
			return "(남) 다크엘프";
		} else if (action.equalsIgnoreCase("9")) {
			return "(여) 다크엘프";	
		}
		
		return null;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (cha.getLevelUpStat() > 0 || cha.getResetBaseStat() > 0 || cha.getResetLevelStat() > 0) {
			ChattingController.toChatting(cha, "스탯 포인트를 모두 사용한 후 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		List<String> list = new ArrayList<String>();

		list.add(getItem().getName());
		list.add(getClassName("0"));
		list.add(getClassName("1"));
		list.add(getClassName("2"));
		list.add(getClassName("3"));
		list.add(getClassName("4"));
		list.add(getClassName("5"));
		list.add(getClassName("6"));
		list.add(getClassName("7"));
		list.add(getClassName("8"));
		list.add(getClassName("9"));

		cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "classChange", null, list));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
		String classType = getClassName(action);
		int classId = 0;

		if (classType == null) {
			return;
		}
		
		switch (action) {
		case "0":
		case "1":
			classId = Lineage.LINEAGE_CLASS_ROYAL;
			break;
		case "2":
		case "3":
			classId = Lineage.LINEAGE_CLASS_KNIGHT;
			break;
		case "4":
		case "5":
			classId = Lineage.LINEAGE_CLASS_ELF;
			break;
		case "6":
		case "7":
			classId = Lineage.LINEAGE_CLASS_WIZARD;
			break;
		case "8":
		case "9":
			classId = Lineage.LINEAGE_CLASS_DARKELF;
			break;	
		}
	
		if (classType != null) {
			if (pc.getClassType() == classId) {
				pc.setClassChangeScroll(null);
				pc.setClassChangeType(null);
				ChattingController.toChatting(pc, "동일한 클래스로 변경할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}

			pc.setClassChangeScroll(this);
			pc.setClassChangeType(action);
			String msg = String.format("'%s'로 클래스를 변경하시겠습니까? 게임이 종료됩니다.", classType);
			pc.toSender(S_MessageYesNo.clone(BasePacketPooling.getPool(S_MessageYesNo.class), 783, msg));
		}
	}

	public void toAsk(PcInstance pc, boolean yes) {
		if (yes && this != null && pc != null && pc.getInventory() != null && !pc.isDead() && !pc.isLock() && !pc.isFishing()) {
			BuffController.removeDead(pc);
			
			String classChaneType = pc.getClassChangeType();
			int classType = 0;
			int classGfx = 0;
			int sex = 0;
			long objId = pc.getObjectId();
			
			if (classChaneType.equalsIgnoreCase("0")) {
				classType = Lineage.LINEAGE_CLASS_ROYAL;
				classGfx = Lineage.royal_male_gfx;
				sex = 0;
			} else if (classChaneType.equalsIgnoreCase("1")) {
				classType = Lineage.LINEAGE_CLASS_ROYAL;
				classGfx = Lineage.royal_female_gfx;
				sex = 1;
			} else if (classChaneType.equalsIgnoreCase("2")) {
				classType = Lineage.LINEAGE_CLASS_KNIGHT;
				classGfx = Lineage.knight_male_gfx;
				sex = 0;
			} else if (classChaneType.equalsIgnoreCase("3")) {
				classType = Lineage.LINEAGE_CLASS_KNIGHT;
				classGfx = Lineage.knight_female_gfx;
				sex = 1;
			} else if (classChaneType.equalsIgnoreCase("4")) {
				classType = Lineage.LINEAGE_CLASS_ELF;
				classGfx = Lineage.elf_male_gfx;
				sex = 0;
			} else if (classChaneType.equalsIgnoreCase("5")) {
				classType = Lineage.LINEAGE_CLASS_ELF;
				classGfx = Lineage.elf_female_gfx;
				sex = 1;
			} else if (classChaneType.equalsIgnoreCase("6")) {
				classType = Lineage.LINEAGE_CLASS_WIZARD;
				classGfx = Lineage.wizard_male_gfx;
				sex = 0;
			} else if (classChaneType.equalsIgnoreCase("7")) {
				classType = Lineage.LINEAGE_CLASS_WIZARD;
				classGfx = Lineage.wizard_female_gfx;
				sex = 1;
			} else if (classChaneType.equalsIgnoreCase("8")) {
				classType = Lineage.LINEAGE_CLASS_DARKELF;
				classGfx = Lineage.darkelf_male_gfx;
				sex = 0;
			} else if (classChaneType.equalsIgnoreCase("9")) {
				classType = Lineage.LINEAGE_CLASS_DARKELF;
				classGfx = Lineage.darkelf_female_gfx;
				sex = 1;	
			}

				
			for (ItemInstance i : pc.getInventory().getList()) {
				if (i != null && i.isEquipped()) {
					i.toClick(pc, null);
				}
			}
			
			ItemInstance item1 = pc.getInventory().find("바운스 어택", 0, 1);
			ItemInstance item2 = pc.getInventory().find("세인트 이뮨 투함", 0, 1);
			ItemInstance item3 = pc.getInventory().find("트리플 애로우(부스트)", 0, 1);
			ItemInstance item4 = pc.getInventory().find("임페리얼 아머", 0, 1);
			
			ItemInstance item5 = pc.getInventory().find("포스스턴", 0, 1);
			ItemInstance item6 = pc.getInventory().find("엘리멘탈샷", 0, 1);
			ItemInstance item7 = pc.getInventory().find("디스(에이션트)", 0, 1);
			ItemInstance item8 = pc.getInventory().find("엘리멘탈샷", 0, 1);
			
			if(item1 != null ){
				
				pc.getInventory().count(item1, 0, true);
				//ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("달러"));
				//ii.setCount(150000);
				//pc.toGiveItem(null, ii, ii.getCount());

			}
			if(item2 != null ){
				
				pc.getInventory().count(item2, 0, true);
				//ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("달러"));
				//ii.setCount(150000);
				//pc.toGiveItem(null, ii, ii.getCount());

			}
			if(item3 != null ){
				
				pc.getInventory().count(item3, 0, true);
				//ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("달러"));
				//ii.setCount(150000);
				//pc.toGiveItem(null, ii, ii.getCount());

			}
			if(item4 != null ){
				
				pc.getInventory().count(item4, 0, true);
				//ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("달러"));
				//ii.setCount(150000);
				//pc.toGiveItem(null, ii, ii.getCount());

			}
			
			if(item5 != null ){
				
				pc.getInventory().count(item5, 0, true);
				//ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("달러"));
				//ii.setCount(300000);
				//pc.toGiveItem(null, ii, ii.getCount());

			}
			if(item6 != null ){
				
				pc.getInventory().count(item6, 0, true);
				//ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("달러"));
				//ii.setCount(300000);
				//pc.toGiveItem(null, ii, ii.getCount());

			}
			if(item7 != null ){
				
				pc.getInventory().count(item7, 0, true);
				//ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("달러"));
				//ii.setCount(300000);
				//pc.toGiveItem(null, ii, ii.getCount());

			}
			if(item8 != null ){
				
				pc.getInventory().count(item8, 0, true);
				//ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("달러"));
				//ii.setCount(300000);
				//pc.toGiveItem(null, ii, ii.getCount());

			}
			pc.getInventory().count(this, getCount() - 1, true);

			CharacterController.toResetStat(pc, classType);
			
			// 사용자 강제종료 시키기.
			pc.toSender(S_Disconnect.clone(BasePacketPooling.getPool(S_Disconnect.class), 0x0A));
			LineageServer.close(pc.getClient());

			CharactersDatabase.classChange(objId, sex, classType, classGfx);		
		}
	}
}
