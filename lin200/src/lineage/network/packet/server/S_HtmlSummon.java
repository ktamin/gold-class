package lineage.network.packet.server;

import lineage.bean.database.Exp;
import lineage.database.ExpDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.share.Lineage;
import lineage.world.object.instance.PetInstance;
import lineage.world.object.instance.SummonInstance;

public class S_HtmlSummon extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, SummonInstance si){
		if(bp == null)
			bp = new S_HtmlSummon(si);
		else
			((S_HtmlSummon)bp).clone(si);
		return bp;
	}
	
	public S_HtmlSummon(SummonInstance si){
		clone(si);
	}
	
	public void clone(SummonInstance si){
		clear();
		
		// 초기화
		String type = "moncom";
		Integer count = 9;
		String s_mode = "$471";
		String f_mode = "0";
		String exp = "792";
		String lawful = null;
		switch(si.getSummonMode()){
			case AggressiveMode:
				s_mode = "$469";	// 공격 태세
				break;
			case DefensiveMode:
				s_mode = "$470";	// 방어 태세
				break;
			case Deploy:
				s_mode = "$476";	// 산개
				break;
			case Alert:
				s_mode = "$472";	// 경계
				break;
			case ItemPickUp:
				s_mode = "$613";	// 수집
				break;
			case Rest:
				s_mode = "$471";	// 휴식
				break;
		    default:
		        // 기본 동작 정의
		        s_mode = "기본 모드";  // 또는 다른 기본 설정
		        break;
		}
		if(si instanceof PetInstance){
			PetInstance pi = (PetInstance)si;
			type = "anicom";
			count = 10;
			switch(pi.getFoodMode()){
				case Veryhungry:
					f_mode = "$608";
					break;
				case Littlehungry:
					f_mode = "$609";
					break;
				case NeitherHungryNorFull:
					f_mode = "$610";
					break;
				case LittleFull:
					f_mode = "$611";
					break;
				case VeryFull:
					f_mode = "$612";
					break;
			}
			Exp e = ExpDatabase.find( pi.getLevel() );
			double a = e.getBonus() - e.getExp();
			double b = pi.getExp() - a;
			exp = String.valueOf((int)((b/e.getExp())*100));
			lawful = String.valueOf(pi.getLawful()-Lineage.NEUTRAL);
		}
		
		// 처리.
		writeC(Opcodes.S_OPCODE_SHOWHTML);			// 
		writeD(si.getObjectId());					// 
		writeS(type);								// 
		if(Lineage.server_version > 144)
			writeC(0x00);							// ?
		writeH(count);								// 문자열 갯수
		writeS(s_mode);								// 
		writeS(String.valueOf(si.getNowHp()));		// 현재 hp
		writeS(String.valueOf(si.getTotalHp()));	// 최대 hp
		writeS(String.valueOf(si.getNowMp()));		// 현재 mp
		writeS(String.valueOf(si.getTotalMp()));	// 최대 mp
		writeS(String.valueOf(si.getLevel()));		// 레벨
		writeS(si.getName());						// 
		writeS(f_mode);								// 
		writeS(exp); 								// 790, 792, 경험치
		writeS(lawful); 							// 라우풀
		
	}
}
