/*
1회에 1000아데나
마법촉매 부족 - alchemy11
마법 촉매를 추출해내려면 매개체가 되는 속성석과 재료비가 필요합니다. 준비가 되시면 다시 와 주십시오. 
검은혈혼 부족 - alchemy12
검은 혈흔과 분리에 필요한 비용을 가지고 오셔야 속성석을 분리해드릴 수가 있습니다. 
 */

package lineage.world.object.npc.craft;

import lineage.bean.database.Npc;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.object.instance.CraftInstance;
import lineage.world.object.instance.PcInstance;

public class Alchemist extends CraftInstance {

	public Alchemist(Npc npc){
		super(npc);
	}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "alchemy1"));
	}
	
}


/*switch(text1.charAt(0)){
	case 'a':	// 마프르의 유산 1개		마력의 돌 20개
		alchemy(cha, "$3064-1", "355 20");
		break;
	case 'b':	// 마프르의 유산 10개
		alchemy(cha, "$3064-10", "355 200");
		break;
	case 'c':	// 마프르의 유산 50개
		alchemy(cha, "$3064-50", "355 1000");
		break;
	case 'd':	// 마프르의 유산 1개		정령옥 40개
		alchemy(cha, "$3064-1", "322 40");
		break;
	case 'e':	// 마프르의 유산 10개
		alchemy(cha, "$3064-10", "322 400");
		break;
	case 'f':	// 마프르의 유산 50개
		alchemy(cha, "$3064-50", "322 2000");
		break;
	case 'g':	// 마프르의 유산 1개		흑마석 10개
		alchemy(cha, "$3064-1", "392 10");
		break;
	case 'h':	// 마프르의 유산 10개
		alchemy(cha, "$3064-10", "392 100");
		break;
	case 'i':	// 마프르의 유산 50개
		alchemy(cha, "$3064-50", "392 500");
		break;
	case 'j':	// 사이하의 유산 1개		마력의 돌 25개
		alchemy(cha, "$3067-1", "355 25");
		break;
	case 'k':	// 사이하의 유산 10개
		alchemy(cha, "$3067-10", "355 250");
		break;
	case 'l':	// 사이하의 유산 50개
		alchemy(cha, "$3067-50", "355 1250");
		break;
	case 'm':	// 사이하의 유산 1개		정령옥 60개
		alchemy(cha, "$3067-1", "322 60");
		break;
	case 'n':	// 사이하의 유산 10개
		alchemy(cha, "$3067-10", "322 600");
		break;
	case 'o':	// 사이하의 유산 50개
		alchemy(cha, "$3067-50", "322 3000");
		break;
	case 'p':	// 사이하의 유산 1개		흑마석 20개
		alchemy(cha, "$3067-1", "392 20");
		break;
	case 'q':	// 사이하의 유산 10개
		alchemy(cha, "$3067-10", "392 200");
		break;
	case 'r':	// 사이하의 유산 50개
		alchemy(cha, "$3067-50", "392 1000");
		break;
	case 's':	// 에바의 유산 1개		마력의 돌 30개
		alchemy(cha, "$3066-1", "355 30");
		break;
	case 't':	// 에바의 유산 10개
		alchemy(cha, "$3066-10", "355 300");
		break;
	case 'u':	// 에바의 유산 50개
		alchemy(cha, "$3066-50", "355 1500");
		break;
	case 'v':	// 에바의 유산 1개		정령옥 50개
		alchemy(cha, "$3066-1", "322 50");
		break;
	case 'w':	// 에바의 유산 10개
		alchemy(cha, "$3066-10", "322 500");
		break;
	case 'x':	// 에바의 유산 50개
		alchemy(cha, "$3066-50", "322 2500");
		break;
	case 'y':	// 에바의 유산 1개		흑마석 20개
		alchemy(cha, "$3066-1", "392 20");
		break;
	case 'z':	// 에바의 유산 10개
		alchemy(cha, "$3066-10", "392 200");
		break;
	case 'A':	// 에바의 유산 50개
		alchemy(cha, "$3066-50", "392 1000");
		break;
	case 'B':	// 파아그리오의 유산 1개		마력의 돌 25개
		alchemy(cha, "$3065-1", "355 25");
		break;
	case 'C':	// 파아그리오의 유산 10개
		alchemy(cha, "$3065-10", "355 250");
		break;
	case 'D':	// 파아그리오의 유산 50개
		alchemy(cha, "$3065-50", "355 1250");
		break;
	case 'E':	// 파아그리오의 유산 1개		정령옥 30개
		alchemy(cha, "$3065-1", "322 30");
		break;
	case 'F':	// 파아그리오의 유산 10개
		alchemy(cha, "$3065-10", "322 300");
		break;
	case 'G':	// 파아그리오의 유산 50개
		alchemy(cha, "$3065-50", "322 1500");
		break;
	case 'H':	// 파아그리오의 유산 1개		흑마석 10개
		alchemy(cha, "$3065-1", "392 10");
		break;
	case 'I':	// 파아그리오의 유산 10개
		alchemy(cha, "$3065-10", "392 100");
		break;
	case 'J':	// 파아그리오의 유산 50개
		alchemy(cha, "$3065-50", "392 500");
		break;
	case 'K':	// 검은 혈흔에서 4종류의 속성석을 분리 1개
		alchemy(cha, 1);
		break;
	case 'L':	// 검은 혈흔에서 4종류의 속성석을 분리 10개
		alchemy(cha, 10);
		break;
	case 'M':	// 검은 혈흔에서 4종류의 속성석을 분리 50개
		alchemy(cha, 50);
		break;
}*/