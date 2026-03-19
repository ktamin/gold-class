package lineage.network.packet.server;

import lineage.bean.database.Item;
import lineage.bean.database.ItemSetoption;
import lineage.database.CharacterMarbleDatabase;
import lineage.database.ItemSetoptionDatabase;
import lineage.network.packet.ServerBasePacket;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.item.Candle;
import lineage.world.object.item.DogCollar;
import lineage.world.object.item.Letter;
import lineage.world.object.item.MagicDoll;
import lineage.world.object.item.weapon.Arrow;

public class S_Inventory extends ServerBasePacket {

	protected void toArmor(ItemInstance item) {
		if (item.getItem().getName().equalsIgnoreCase("절대 반지") || item.getItem().getNameIdNumber() == 431)
			toEtc(item.getItem(), item.getWeight());
		else
			toArmor(item.getItem(), item.getDurability(), item.getEnLevel(), item.getWeight(), item.getDynamicMr(),
					item.getBless(), item.getDynamicStunDefence(), item.getDynamicSp(), item.getDynamicReduction());

	}

	protected void toWeapon(ItemInstance item) {
		if (item.getItem().getType2().equalsIgnoreCase("fishing_rod"))
			toEtc(item.getItem(), item.getWeight());
		else
			toWeapon(item.getItem(), item.getDurability(), item.getEnLevel(), item.getWeight(), item.getBless(),
					item.getDynamicMr(), item.getDynamicStunDefence(), item.getDynamicSp(), item.getDynamicReduction());
	}

	protected void toEtc(ItemInstance item) {
		if (item.getItem().getNameIdNumber() == 1173) {
			DogCollar dc = (DogCollar) item;
			writeC(0x0f); // 15
			writeC(0x19);
			writeH(dc.getPetClassId());
			writeC(0x1a);
			writeH(dc.getPetLevel());
			writeC(0x1f);
			writeH(dc.getPetHp());
			writeC(0x17);
			writeC(item.getItem().getMaterial());
			writeD(item.getWeight());
		} else {
			if (!item.getItem().getType2().equalsIgnoreCase("sword_lack")
					&& !item.getItem().getType2().equalsIgnoreCase("자동 칼질") &&
					!item.getItem().getName().contains("기운을 잃은"))
				toEtc(item.getItem(), item.getWeight());
		}
	}

	protected void toArmor(Item item, int durability, int enlevel, int weight, int dynamic_mr, int bless,
			double dynamic_stun_defence, int dynamic_sp, int dynamic_reduction) {
		ItemSetoption setoption = Lineage.server_version >= 250 ? ItemSetoptionDatabase.find(item.getSetId()) : null;
		writeC(getOptionSize(item, durability, enlevel, setoption, bless, dynamic_mr, dynamic_stun_defence, dynamic_sp,
				dynamic_reduction));
		writeC(19);
		writeC(item.getAc());
		writeC(item.getMaterial());
		if (Lineage.server_version > 300)
			writeC(-1); // Grade
		writeD(weight);

		// AC 0+enlevel
		if (enlevel != 0) {
			int ac = enlevel;

			writeC(0x02);
			writeC(ac);
		}

		int type = item.getRoyal() != 1 ? 0 : 1;
		type += item.getKnight() != 1 ? 0 : 2;
		type += item.getElf() != 1 ? 0 : 4;
		type += item.getWizard() != 1 ? 0 : 8;
		type += item.getDarkElf() != 1 ? 0 : 16;
		type += item.getDragonKnight() != 1 ? 0 : 32;
		type += item.getBlackWizard() != 1 ? 0 : 64;
		writeC(7);
		writeC(type);

		if (item.getAddDmg() != 0 || ((bless == 0 || bless == -128) && item.isAcc())
				|| (item.getType2().equalsIgnoreCase("ring") && enlevel > 4)) {
			int addDmg = item.getAddDmg();

			if ((bless == 0 || bless == -128) && item.isAcc()) {
				addDmg += 1;
			}

			if (item.getType2().equalsIgnoreCase("ring") && enlevel > 4) {
				switch (enlevel) {
					case 5:
						addDmg += 1;
						break;
					case 6:
						addDmg += 2;
						break;
					case 7:
						addDmg += 3;
						break;
					case 8:
						addDmg += 4;
						break;
					case 9:
						addDmg += 5;
						break;
					case 10:
						addDmg += 6;
						break;
				}
			}

			if (addDmg > 0) {
				writeC(6);
				writeC(addDmg);
			}
		}

		if (item.getAddHit() != 0 || ((bless == 0 || bless == -128) && item.isAcc())
				|| (item.getName().equalsIgnoreCase("수호성의 파워 글로브") || item.getName().equalsIgnoreCase("수호성의 활 골무")
						|| item.getName().equalsIgnoreCase("빛나는 마력의 장갑"))) {
			int addHit = item.getAddHit();

			if ((bless == 0 || bless == -128) && item.isAcc()) {
				addHit += 1;
			}

			if (item.getName().equalsIgnoreCase("수호성의 파워 글로브") || item.getName().equalsIgnoreCase("수호성의 활 골무")
					|| item.getName().equalsIgnoreCase("빛나는 마력의 장갑")) {
				switch (enlevel) {

					case 7:
						addHit += 1;
						break;
					case 8:
						addHit += 2;
						break;
					case 9:
						addHit += 3;
						break;
					case 10:
						addHit += 6;
						break;
				}
			}

			if (addHit > 0) {
				writeC(5);
				writeC(addHit);
			}
		}

		if (item.getAddStr() != 0) {
			writeC(8);
			writeC(item.getAddStr());
		}

		if (item.getAddDex() != 0) {
			writeC(9);
			writeC(item.getAddDex());
		}

		if (item.getAddCon() != 0) {
			writeC(10);
			writeC(item.getAddCon());
		}

		if (item.getAddInt() != 0) {
			writeC(12);
			writeC(item.getAddInt());
		}

		if (item.getAddWis() != 0) {
			writeC(11);
			writeC(item.getAddWis());
		}

		if (item.getAddCha() != 0) {
			writeC(13);
			writeC(item.getAddCha());
		}

		if (item.getAddHp() != 0 || ((bless == 0 || bless == -128) && item.getType1().equalsIgnoreCase("armor"))
				|| (item.getType2().equals("necklace") || item.getType2().equalsIgnoreCase("ring")) && enlevel > 0
				|| (item.getType2().equalsIgnoreCase("belt") && enlevel > 0)
				|| (item.getName().equalsIgnoreCase("완력의 부츠") || item.getName().equalsIgnoreCase("민첩의 부츠")
						|| item.getName().equalsIgnoreCase("지식의 부츠"))) {

			int addHp = (bless == 0 || bless == -128) && item.getType1().equalsIgnoreCase("armor") && !item.isAcc()
					? item.getAddHp() + 10
					: item.getAddHp();

			if ((item.getName().equalsIgnoreCase("완력의 부츠") || item.getName().equalsIgnoreCase("민첩의 부츠")
					|| item.getName().equalsIgnoreCase("지식의 부츠"))) {
				switch (enlevel) {
					case 7:
						addHp += 10;
						break;
					case 8:
						addHp += 20;
						break;
					case 9:
						addHp += 30;
						break;
					case 10:
						addHp += 70;
						break;
				}
			}

			if ((item.getType2().equals("necklace") || item.getType2().equalsIgnoreCase("ring")) && enlevel > 0) {
				switch (enlevel) {
					case 1:
						addHp += 10;
						break;
					case 2:
						addHp += 20;
						break;
					case 3:
						addHp += 30;
						break;
					case 4:
						addHp += 40;
						break;
					case 5:
						addHp += 50;
						break;
					case 6:
						addHp += 60;
						break;
					case 7:
						addHp += 70;
						break;
					case 8:
						addHp += 80;
						break;
					case 9:
						addHp += 90;
						break;
					case 10:
						addHp += 100;
						break;
				}
			}

			if (item.getType2().equalsIgnoreCase("belt") && enlevel > 0) {
				switch (enlevel) {
					case 1:
						addHp += 5;
						break;
					case 2:
						addHp += 10;
						break;
					case 3:
						addHp += 15;
						break;
					case 4:
						addHp += 20;
						break;
					case 5:
						addHp += 25;
						break;
					case 6:
						addHp += 30;
						break;
					case 7:
						addHp += 35;
						break;
					case 8:
						addHp += 40;
						break;
					case 9:
						addHp += 45;
						break;
					case 10:
						addHp += 50;
						break;
				}
			}

			if (addHp > 0) {
				writeC(14);
				writeH(addHp);
			}
		}

		if (((item.getAddReduction() != 0 || dynamic_reduction != 0) && !item.getType2().equalsIgnoreCase("belt"))
				|| (item.getName().equalsIgnoreCase("완력의 부츠") || item.getName().equalsIgnoreCase("민첩의 부츠")
						|| item.getName().equalsIgnoreCase("지식의 부츠"))) {

			int reduction = item.getAddReduction() + dynamic_reduction;

			if ((item.getName().equalsIgnoreCase("완력의 부츠") || item.getName().equalsIgnoreCase("민첩의 부츠")
					|| item.getName().equalsIgnoreCase("지식의 부츠")) && enlevel == 9)
				reduction += 1;

			if (item.getName().equalsIgnoreCase("안타라스의 완력") || item.getName().equalsIgnoreCase("안타라스의 마력")
					|| item.getName().equalsIgnoreCase("안타라스의 인내력") || item.getName().equalsIgnoreCase("안타라스의 예지력")) {
				switch (enlevel) {
					case 1:
						reduction += 0;
						break;
					case 2:
						reduction += 0;
						break;
					case 3:
						reduction += 0;
						break;
					case 4:
						reduction += 0;
						break;
					case 5:
						reduction += 1;
						break;
					case 6:
						reduction += 2;
						break;
					case 7:
						reduction += 3;
						break;
					case 8:
						reduction += 4;
						break;
					case 9:
						reduction += 5;
						break;
					case 10:
						reduction += 6;
						break;
				}
			}

			if (reduction > 0) {
				writeC(20);
				writeC(reduction);
			}
		}

		if (item.getName().equalsIgnoreCase("고대 암석의 장갑") ||
				item.getName().equalsIgnoreCase("고대 암석의 부츠") ||
				item.getName().equalsIgnoreCase("고대 암석의 망토")) {

			// [수정] DB(나비켓)에 설정된 기본 PVP 리덕션 값을 가져옵니다.
			int pvpReduction = item.getPvpReduction();

			// 인첸트별 보너스 수치 합산 (+= 사용)
			switch (enlevel) {
				case 5:
					pvpReduction += 5;
					break;
				case 6:
					pvpReduction += 10;
					break;
				case 7:
					pvpReduction += 15;
					break;
				case 8:
					pvpReduction += 20;
					break;
				case 9:
					pvpReduction += 25;
					break;
				case 10:
					pvpReduction += 30;
					break;
			}

			// 합산된 값이 있을 때만 전송 (Opcode 30: PVP 리덕션)
			if (pvpReduction > 0) {
				writeC(30);
				writeC(pvpReduction);
			}
		}

		if (item.getName().equalsIgnoreCase("고대 마물의 장갑") ||
				item.getName().equalsIgnoreCase("고대 마물의 부츠") ||
				item.getName().equalsIgnoreCase("고대 마물의 망토")) {

			// [수정] 0이 아니라, DB에 설정된 기본값을 가져옵니다.
			int pvpDmg = item.getPvpDamage();

			// 인첸트 보너스 추가 (기존 값에 더하기 +=)
			switch (enlevel) {
				case 5:
					pvpDmg += 5;
					break;
				case 6:
					pvpDmg += 10;
					break;
				case 7:
					pvpDmg += 15;
					break;
				case 8:
					pvpDmg += 20;
					break;
				case 9:
					pvpDmg += 25;
					break;
				case 10:
					pvpDmg += 6;
					break;
			}

			// 합산된 결과가 0보다 크면 전송
			if (pvpDmg > 0) {
				writeC(29);
				writeC(pvpDmg);
			}
		}

		if ((item.getStunDefense() != 0 || dynamic_stun_defence != 0)
				&& !item.getType2().equalsIgnoreCase("necklace")) {
			int stunDefence = (int) ((item.getStunDefense() + dynamic_stun_defence) * 100);
			writeC(28);
			writeC(stunDefence);
		}

		// 물약 회복량, 스턴 내성
		if (item.getType2().equalsIgnoreCase("necklace")) {
			int potion = 0;
			int stunResist = (int) (item.getStunDefense() * 100);

			switch (enlevel) {
				case 1:
					potion += 0;
					break;
				case 2:
					potion += 0;
					break;
				case 3:
					potion += 0;
					break;
				case 4:
					potion += 0;
					break;
				case 5:
					potion += 1;
					break;
				case 6:
					potion += 2;
					break;
				case 7:
					potion += 3;
					stunResist += 5;
					break;
				case 8:
					potion += 4;
					stunResist += 10;
					break;
				case 9:
					potion += 5;
					stunResist += 4;
					break;
				case 10:
					potion += 6;
					stunResist += 5;
					break;
			}

			if (potion > 0) {
				writeC(27);
				writeC(potion);
			}

			if (stunResist > 0) {
				writeC(28);
				writeC(stunResist);
			}
		}

		if (item.getAddSp() != 0 || dynamic_sp != 0 || (item.getType2().equalsIgnoreCase("ring") && enlevel > 6)) {
			int addSp = item.getAddSp() + dynamic_sp;

			if (item.getType2().equalsIgnoreCase("ring") && enlevel > 6) {
				switch (enlevel) {
					case 5:
						addSp += 1;
						break;
					case 6:
						addSp += 2;
						break;
					case 7:
						addSp += 3;
						break;
					case 8:
						addSp += 4;
						break;
					case 9:
						addSp += 3;
						break;
					case 10:
						addSp += 4;
						break;
				}
			}

			if (addSp > 0) {
				writeC(17);
				writeC(addSp);
			}
		}

		if (item.getAddMr() != 0 || dynamic_mr != 0 || (item.getType2().equalsIgnoreCase("ring") && enlevel > 5)) {
			int addMr = item.getAddMr() + dynamic_mr;

			if (item.getType2().equals("ring") && enlevel > 5) {
				switch (enlevel) {
					case 6:
						addMr += 0;
						break;
					case 7:
						addMr += 3;
						break;
					case 8:
						addMr += 5;
						break;
					case 9:
						addMr += 7;
						break;
					case 10:
						addMr += 8;
						break;
				}
			}

			if (addMr > 0) {
				writeC(15);
				writeH(addMr);
			}
		}

		// PvP 대미지
		if (item.getType2().equalsIgnoreCase("ring") && enlevel > 6) {
			int pvpDmg = 0;

			switch (enlevel) {
				case 7:
					pvpDmg += 10;
					break;
				case 8:
					pvpDmg += 30;
					break;
				case 10:
					pvpDmg += 40;
					break;
			}
			if (pvpDmg > 0) {
				writeC(29);
				writeC(pvpDmg);
			}
		}

		if (setoption != null && (setoption.isBrave() || setoption.isHaste()))
			writeC(18);

		if (item.getAddMp() != 0 || (item.getType2().equalsIgnoreCase("belt") && enlevel > 0)) {
			int addMp = item.getAddMp();

			if (item.getType2().equalsIgnoreCase("belt") && enlevel > 0) {
				switch (enlevel) {
					case 1:
						addMp += 5;
						break;
					case 2:
						addMp += 10;
						break;
					case 3:
						addMp += 15;
						break;
					case 4:
						addMp += 20;
						break;
					case 5:
						addMp += 25;
						break;
					case 6:
						addMp += 30;
						break;
					case 7:
						addMp += 35;
						break;
					case 8:
						addMp += 40;
						break;
					case 9:
						addMp += 45;
						break;
					case 10:
						addMp += 50;
						break;
				}
			}

			if (addMp > 0) {
				writeC(24);
				writeC(addMp);
			}
		}

		// 벨트
		if (item.getType2().equalsIgnoreCase("belt")) {
			int reduction = item.getAddReduction() + dynamic_reduction;
			int pvpDmgReduction = 0;

			switch (enlevel) {
				case 5:
					reduction += 1;
					break;
				case 6:
					reduction += 2;
					break;
				case 7:
					reduction += 3;
					pvpDmgReduction = 10;
					break;
				case 8:
					reduction += 4;
					pvpDmgReduction = 30;
					break;
				case 9:
					reduction += 5;
					pvpDmgReduction += 4;
					break;
				case 10:
					reduction += 6;
					pvpDmgReduction += 5;
					break;
			}

			if (reduction > 0) {
				writeC(20);
				writeC(reduction);
			}

			if (pvpDmgReduction > 0) {
				writeC(30);
				writeC(pvpDmgReduction);
			}
		}
	}

	// 20 대미지 감소
	// 24 최대 MP
	// 27 물약 회복량(%)
	// 28 스턴 내성
	// 29 PvP 대미지
	// 30 PvP 대미지 감소

	protected void toWeapon(Item item, int durability, int enlevel, int weight, int bless, int dynamic_mr,
			double dynamic_stun_defence, int dynamic_sp, int dynamic_reduction) {
		ItemSetoption setoption = Lineage.server_version >= 250 ? ItemSetoptionDatabase.find(item.getSetId()) : null;
		writeC(getOptionSize(item, durability, enlevel, setoption, bless, enlevel * item.getEnchantMr(),
				enlevel * item.getEnchantStunDefense(), enlevel * item.getEnchantSp(),
				enlevel * item.getEnchantReduction()));
		writeC(0x01);
		writeC(item.getSmallDmg());
		writeC(item.getBigDmg());
		writeC(item.getMaterial());
		writeD(weight);
		if (enlevel != 0) {
			writeC(0x02);
			writeC(enlevel);
		}
		if (durability != 0) {
			writeC(3);
			writeC(durability);
		}

		int type = item.getRoyal() != 1 ? 0 : 1;
		type += item.getKnight() != 1 ? 0 : 2;
		type += item.getElf() != 1 ? 0 : 4;
		type += item.getWizard() != 1 ? 0 : 8;
		type += item.getDarkElf() != 1 ? 0 : 16;
		type += item.getDragonKnight() != 1 ? 0 : 32;
		type += item.getBlackWizard() != 1 ? 0 : 64;
		writeC(7);
		writeC(type);

		if (item.isTohand())
			writeC(4);

		if (item.getAddDmg() != 0 || ((bless == 0 || bless == -128) && item.getType1().equalsIgnoreCase("weapon")
				&& !item.getType2().equalsIgnoreCase("wand"))) {
			int addDmg = bless == 0 && item.getType1().equals("weapon") && !item.getType2().equals("wand")
					? item.getAddDmg() + 2
					: item.getAddDmg();

			writeC(6);
			writeC(addDmg);
		}

		if (item.getAddHit() != 0) {
			writeC(5);
			writeC(item.getAddHit());
		}

		if (item.getAddStr() != 0) {
			writeC(8);
			writeC(item.getAddStr());
		}

		if (item.getAddDex() != 0) {
			writeC(9);
			writeC(item.getAddDex());
		}

		if (item.getAddCon() != 0) {
			writeC(10);
			writeC(item.getAddCon());
		}

		if (item.getAddWis() != 0) {
			writeC(11);
			writeC(item.getAddWis());
		}

		if (item.getAddInt() != 0) {
			writeC(12);
			writeC(item.getAddInt());
		}

		if (item.getAddCha() != 0) {
			writeC(13);
			writeC(item.getAddCha());
		}

		if ((item.getAddSp() != 0 || dynamic_sp != 0) || ((bless == 0 || bless == -128)
				&& item.getType1().equalsIgnoreCase("weapon") && item.getType2().equalsIgnoreCase("wand"))) {
			int sp = bless == 0 && item.getType1().equals("weapon") && item.getType2().equals("wand")
					? item.getAddSp() + 1 + dynamic_sp
					: item.getAddSp() + dynamic_sp;

			writeC(17);
			writeC(sp);
		}

		if (item.getAddHp() != 0) {
			writeC(14);
			writeH(item.getAddHp());
		}

		if (item.getAddMr() != 0) {
			writeC(15);
			writeH(item.getAddMr());
		}

		if (item.getStealMp() != 0) {
			writeC(16);
		}

		if (setoption != null && (setoption.isBrave() || setoption.isHaste()))
			writeC(18);

		if (item.getAddMr() != 0 || dynamic_mr != 0) {
			int addMr = item.getAddMr() + dynamic_mr;

			if (addMr > 0) {
				writeC(15);
				writeH(addMr);
			}
		}

		if (item.getStunDefense() != 0 || dynamic_stun_defence != 0) {
			int stunDefence = (int) ((item.getStunDefense() + dynamic_stun_defence) * 100);
			writeC(28);
			writeC(stunDefence);
		}

		if (item.getAddReduction() != 0 || dynamic_reduction != 0) {
			int reduction = item.getAddReduction() + dynamic_reduction;

			if (reduction > 0) {
				writeC(20);
				writeC(reduction);
			}
		}

	}

	protected void toEtc(Item item, int weight) {
		writeC(0x06);
		writeC(0x17);
		writeC(item.getMaterial());
		writeD(weight);
	}

	protected String getName(ItemInstance item) {
		String name = CharacterMarbleDatabase.getItemName(item);
		if (name != null) {
			return name;
		}

		StringBuffer sb = new StringBuffer();
		if (item.getItem().getNameIdNumber() == 1075 && item.getItem().getInvGfx() != 464) {
			Letter letter = (Letter) item;
			sb.append(letter.getFrom());
			sb.append(" : ");
			sb.append(letter.getSubject());
		} else {
			/*
			 * if(item.getDeleteTime() > 0){
			 * 
			 * // 현재 시간
			 * long time = System.currentTimeMillis() / 1000;
			 * 
			 * // 남은 시간
			 * long 남은시간 = item.getDeleteTime() - time;
			 * 
			 * // 시간
			 * long hour = 남은시간 / 3600;
			 * 남은시간 -= hour * 3600;
			 * // 분
			 * long min = 남은시간 / 60;
			 * 남은시간 -= min * 60;
			 * // 초
			 * long sec = 남은시간 / 1;
			 * 
			 * // 알림
			 * sb.append("[" + (hour > 0 ? hour + "시간 " : "") + (min > 0 ? min + "분 " : "")
			 * + sec + "초] ");
			 * }
			 */
			
			if (item.getDeleteTime() > 0) {
				long currentTime = System.currentTimeMillis() / 1000;
				long 남은시간 = item.getDeleteTime() - currentTime;
				
				if (남은시간 <= 0) {
					sb.append("남은시간[만료됨] ");
				} else {
					// 1. 일(Day) 계산
					long day = 남은시간 / 86400;
					남은시간 %= 86400;
					
					// 2. 시간 계산
					long hour = 남은시간 / 3600;
					남은시간 %= 3600;
					
					// 3. 분/초 계산
					long min = 남은시간 / 60;
					long sec = 남은시간 % 60;
					
					// 4. [사장님 오더 적용] 기간에 따른 스마트 출력
					sb.append("남은시간[");
					
					if (day > 0) {
						// 30일 등 하루 이상 남았을 때 ➔ 남은시간[30일]
						sb.append(day + "일");
					} else if (hour > 0) {
						// 하루 미만일 때 ➔ 남은시간[23시간 50분] (또는 초까지)
						sb.append(hour + "시간 " + (min > 0 ? min + "분" : ""));
					} else {
						// 1시간 미만일 때 ➔ 남은시간[59분 30초]
						sb.append(min + "분 " + sec + "초");
					}
					
					sb.append("] ");
				}
			}
			
			// 봉인 표현
			if (item.isDefinite() && item.getBless() < 0) {
				sb.append("[봉인]");
				sb.append(" ");
			}

			if (item.isDefinite() && (item instanceof ItemWeaponInstance || item instanceof ItemArmorInstance)
					&& !item.getItem().getType2().equalsIgnoreCase("fishing_rod") &&
					!item.getItem().getName().equalsIgnoreCase("절대 반지") && item.getItem().getNameIdNumber() != 431) {
				// 속성 인첸 표현.
				String element_name = null;
				Integer element_en = 0;
				if (item.getEnWind() > 0) {
					element_name = "풍령";
					element_en = item.getEnWind();
				}
				if (item.getEnEarth() > 0) {
					element_name = "지령";
					element_en = item.getEnEarth();
				}
				if (item.getEnWater() > 0) {
					element_name = "수령";
					element_en = item.getEnWater();
				}
				if (item.getEnFire() > 0) {
					element_name = "화령";
					element_en = item.getEnFire();
				}
				if (element_name != null) {
					sb.append(element_name).append(":").append(element_en).append("단");
					sb.append(" ");
				}
				// 인첸 표현.
				if (item.getEnLevel() >= 0) {
					sb.append("+");
				}
				sb.append(item.getEnLevel());
				sb.append(" ");
			}
			sb.append(item.getName());

			if (item.isDefinite() && item.getQuantity() > 0/*
															 * (item instanceof MapleWand || item instanceof PineWand ||
															 * item instanceof EbonyWand)
															 */) {
				sb.append(" (");
				sb.append(item.getQuantity());
				sb.append(")");
			}

			if (item.getCount() > 1) {
				sb.append(" (");
				sb.append(Util.changePrice(item.getCount()));
				sb.append(")");
			}

			if (item.getItem().getNameIdNumber() == 1173) {
				DogCollar dc = (DogCollar) item;
				sb.append(" [Lv.");
				sb.append(dc.getPetLevel());
				sb.append(" ");
				sb.append(dc.getPetName());
				sb.append("]");
			}
			if (item.getInnRoomKey() > 0) {
				sb.append(" #");
				sb.append(item.getInnRoomKey());
			}

			// 착용중인 아이템 표현
			if (item.isEquipped()) {
				if (item instanceof ItemWeaponInstance || item instanceof Arrow) {
					sb.append(" ($9)");
				} else if (item instanceof ItemArmorInstance || item instanceof MagicDoll) {
					sb.append(" ($117)");
				} else if (item instanceof Candle) {
					// 양초, 등잔
					sb.append(" ($10)");
				}
			}
			// if (item.getItem().getSafeEnchant() >= 0 &&
			// item.getItem().getType1().equalsIgnoreCase("weapon") ||
			// item.getItem().getType1().equalsIgnoreCase("armor") ) {
			// writeC(30);
			//
			// writeS("안전인첸 ");

			// sb.append(System.getProperty("line.separator"));
			//
			// sb.append("\\fY[안전인챈 : +" + item.getItem().getSafeEnchant() + "]");
			// sb.append(System.getProperty("line.separator"));
			//
			// sb.append(System.getProperty("line.separator"));
			// sb.append(System.getProperty("line.separator"));
			//

			// }
			// }
		}

		return sb.toString().trim();
	}

	protected int getOptionSize(ItemInstance item) {
		return getOptionSize(item.getItem(), item.getDurability(), item.getEnLevel(), null, item.getBless(),
				item.getDynamicMr(), item.getDynamicStunDefence(), item.getDynamicSp(), item.getDynamicReduction());
	}

	protected int getOptionSize(Item item, int durability, int enlevel, ItemSetoption setoption, int bless,
			int dynamic_mr, double dynamic_stun_defence, int dynamic_sp, int dynamic_reduction) {
		int size = 0;

		if (item.getType1().equalsIgnoreCase("armor")) {
			if (Lineage.server_version > 300)
				size += 10;
			else
				size += 9;

			if (enlevel != 0)
				size += 2;
			if (item.getAddStr() != 0)
				size += 2;
			if (item.getAddDex() != 0)
				size += 2;
			if (item.getAddCon() != 0)
				size += 2;
			if (item.getAddInt() != 0)
				size += 2;
			if (item.getAddCha() != 0)
				size += 2;
			if (item.getAddWis() != 0)
				size += 2;

			if (item.getName().equalsIgnoreCase("완력의 부츠") || item.getName().equalsIgnoreCase("민첩의 부츠")
					|| item.getName().equalsIgnoreCase("지식의 부츠")) {
				if ((bless == 0 || bless == -128) || enlevel > 6)
					size += 3;
				if (enlevel == 9)
					size += 2;

				return size;
			}

			if (item.getName().equalsIgnoreCase("고대 암석의 장갑") ||
					item.getName().equalsIgnoreCase("고대 암석의 부츠") ||
					item.getName().equalsIgnoreCase("고대 암석의 망토")) {

				// [수정] DB값 + 인첸트값 미리 계산해보기
				int tempReduc = item.getPvpReduction();

				// 인첸트 보너스 계산 (switch문 대신 if로 간략화 가능)
				if (enlevel >= 5) {
					tempReduc += (enlevel - 4); // 5강일 때 +1, 6강일 때 +2...
				}

				// 합친 값이 0보다 크면 패킷 사이즈 2 추가
				if (tempReduc > 0) {
					size += 2;
				}
			}

			if (item.getName().equalsIgnoreCase("고대 마물의 장갑") ||
					item.getName().equalsIgnoreCase("고대 마물의 부츠") ||
					item.getName().equalsIgnoreCase("고대 마물의 망토")) {

				// [수정] DB값 + 인첸트 보너스 계산
				int tempPvp = item.getPvpDamage();

				if (enlevel >= 5) {
					tempPvp += (enlevel - 4); // 5강 이상일 때 추가
				}

				// 합산 결과가 0보다 크면 사이즈 추가
				if (tempPvp > 0) {
					size += 2;
				}
			}

			if (item.getType2().equalsIgnoreCase("necklace")) {
				if (item.getAddDmg() != 0 || (bless == 0 || bless == -128))
					size += 2;
				if (item.getAddHit() != 0 || (bless == 0 || bless == -128))
					size += 2;
				if (item.getAddHp() != 0 || enlevel > 0)
					size += 3;
				if (item.getAddMp() != 0)
					size += 2;
				if (item.getAddMr() != 0 || dynamic_mr != 0)
					size += 3;
				if (item.getAddSp() != 0 || dynamic_sp != 0)
					size += 2;
				if (item.getStunDefense() != 0 || dynamic_stun_defence != 0 || enlevel > 6)
					size += 2;
				if (item.getAddReduction() > 0 || dynamic_reduction != 0)
					size += 2;
				if (item.getStunHit() != 0)
					size += 2;

				// 물약 회복량
				if (enlevel > 4)
					size += 2;

				return size;
			}

			if (item.getType2().equalsIgnoreCase("ring")) {
				if (item.getAddDmg() != 0 || (bless == 0 || bless == -128) || enlevel > 4)
					size += 2;
				if (item.getAddHit() != 0 || (bless == 0 || bless == -128))
					size += 2;
				if (item.getAddHp() != 0 || enlevel > 0)
					size += 3;
				if (item.getAddMp() != 0)
					size += 2;
				if ((item.getAddMr() != 0 || dynamic_mr != 0) || enlevel > 5)
					size += 3;
				if (item.getAddSp() != 0 || dynamic_sp != 0 || enlevel > 6)
					size += 2;
				if (item.getStunDefense() != 0 || dynamic_stun_defence != 0)
					size += 2;
				if (item.getAddReduction() > 0 || dynamic_reduction != 0)
					size += 2;

				// PvP 데미지
				if (enlevel > 6)
					size += 2;

				return size;
			}

			if (item.getType2().equalsIgnoreCase("belt")) {
				if (item.getAddDmg() != 0 || (bless == 0 || bless == -128))
					size += 2;
				if (item.getAddHit() != 0 || (bless == 0 || bless == -128))
					size += 2;
				if (item.getAddHp() != 0 || enlevel > 0)
					size += 3;
				if (item.getAddMp() != 0 || enlevel > 0)
					size += 3;
				if (item.getAddMr() != 0 || dynamic_mr != 0)
					size += 3;
				if (item.getAddSp() != 0 || dynamic_sp != 0)
					size += 2;
				if (item.getStunDefense() != 0 || dynamic_stun_defence != 0)
					size += 2;
				if (item.getAddReduction() > 0 || dynamic_reduction != 0 || enlevel > 4)
					size += 2;

				// PvP 리덕션
				if (enlevel > 6)
					size += 2;

				return size;
			}

			if (item.getType2().equalsIgnoreCase("earring")) {
				if (item.getAddDmg() != 0 || (bless == 0 || bless == -128))
					size += 2;
				if (item.getAddHit() != 0 || (bless == 0 || bless == -128))
					size += 2;
				if (item.getAddHp() != 0)
					size += 3;
				if (item.getAddMp() != 0)
					size += 2;
				if (item.getAddMr() != 0)
					size += 3;
				if (item.getAddSp() != 0)
					size += 2;
				if (item.getStunDefense() != 0)
					size += 2;
				if (item.getAddReduction() > 0)
					size += 2;

				return size;
			}
		} else if (item.getType1().equalsIgnoreCase("weapon")) {
			size += 10;
			if (enlevel != 0)
				size += 2;
			if (item.getAddStr() != 0)
				size += 2;
			if (item.getAddDex() != 0)
				size += 2;
			if (item.getAddCon() != 0)
				size += 2;
			if (item.getAddInt() != 0)
				size += 2;
			if (item.getAddCha() != 0)
				size += 2;
			if (item.getAddWis() != 0)
				size += 2;
			if (durability != 0)
				size += 2;
			if (item.isTohand())
				size += 1;
			if (item.getStealMp() != 0)
				size += 1;
		} else {
			return 0;
		}

		if (item.getAddReduction() != 0 || dynamic_reduction != 0)
			size += 2;
		if (item.getStunDefense() != 0 || dynamic_stun_defence != 0)
			size += 2;
		if (item.getAddMp() != 0)
			size += 2;
		if (item.getAddMr() != 0 || dynamic_mr != 0)
			size += 3;
		if (item.getAddSp() != 0 || ((bless == 0 || bless == -128) && item.getType2().equalsIgnoreCase("wand"))
				|| dynamic_sp != 0)
			size += 2;
		if (item.getAddHp() != 0 || (bless == 0 || bless == -128) && item.getType1().equalsIgnoreCase("armor"))
			size += 3;
		if (item.getAddDmg() != 0 || ((bless == 0 || bless == -128) && item.getType1().equalsIgnoreCase("weapon")
				&& !item.getType2().equalsIgnoreCase("wand")))
			size += 2;
		if (item.getAddHit() != 0
				|| ((item.getName().equalsIgnoreCase("수호성의 파워 글로브") || item.getName().equalsIgnoreCase("수호성의 활 골무")
						|| item.getName().equalsIgnoreCase("빛나는 마력의 장갑")) && enlevel > 4))
			size += 2;
		if (setoption != null && (setoption.isBrave() || setoption.isHaste()))
			size += 1;

		return size;
	}

}
