package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Fx.server.MJTemplate.MJProto.Models.SC_TOAST_NOTI;
import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.npc.무기제작사.CreateItem;

public class 전설장비제작사 extends object {

    public class CreateItem {
        public String itemName;
        public boolean isCheckBless;
        public int bless;
        public boolean isCheckEnchant;
        public int enchant;
        public int count;

        public CreateItem(String itemName, boolean isCheckBless, int bless, boolean isCheckEnchant, int enchant,
                int count) {
            this.itemName = itemName;
            this.isCheckBless = isCheckBless;
            this.bless = bless;
            this.isCheckEnchant = isCheckEnchant;
            this.enchant = enchant;
            this.count = count;
        }
    }

    @Override
    public void toTalk(PcInstance pc, ClientBasePacket cbp) {
        pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "armorcreate6"));
    }

    @Override
    public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp) {
        if (pc.getInventory() != null) {
            List<CreateItem> createList = new ArrayList<CreateItem>();
            List<CreateItem> createList2 = new ArrayList<CreateItem>();
            List<ItemInstance> itemList = new ArrayList<ItemInstance>();

            if (action.equalsIgnoreCase("진명황의 집행검")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 500000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "진명황의 집행검", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("진명황의 집행검1")) {
                createList.add(new CreateItem("나이트발드의 양손검", true, 0, true, 10, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "진명황의 집행검", 1, 2, 1, 100);

            } else if (action.equalsIgnoreCase("진명황의 집행검2")) {
                createList.add(new CreateItem("나이트발드의 양손검", true, 0, true, 9, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 2));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 400000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "진명황의 집행검", 1, 1, 1, 100);

            } else if (action.equalsIgnoreCase("사신의 검")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 500000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "사신의 검", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("사신의 검1")) {
                createList.add(new CreateItem("포르세의 검", true, 0, true, 10, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "사신의 검", 1, 2, 1, 100);

            } else if (action.equalsIgnoreCase("사신의 검2")) {
                createList.add(new CreateItem("포르세의 검", true, 0, true, 9, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 400000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "사신의 검", 1, 1, 1, 100);

            } else if (action.equalsIgnoreCase("가이아의 격노")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 500000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "가이아의 격노", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("가이아의 격노1")) {
                createList.add(new CreateItem("악몽의 장궁", true, 0, true, 10, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "가이아의 격노", 1, 2, 1, 100);

            } else if (action.equalsIgnoreCase("가이아의 격노2")) {
                createList.add(new CreateItem("악몽의 장궁", true, 0, true, 9, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 400000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "가이아의 격노", 1, 1, 1, 100);

            } else if (action.equalsIgnoreCase("붉은 그림자의 이도류")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 500000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "붉은 그림자의 이도류", 1, 2, 1, 100);

            } else if (action.equalsIgnoreCase("붉은 그림자의 이도류1")) {
                createList.add(new CreateItem("포효의 이도류", true, 0, true, 10, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "붉은 그림자의 이도류", 1, 1, 1, 100);

            } else if (action.equalsIgnoreCase("붉은 그림자의 이도류2")) {
                createList.add(new CreateItem("포효의 이도류", true, 0, true, 9, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 400000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "붉은 그림자의 이도류", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("수정 결정체 지팡이")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 500000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "수정 결정체 지팡이", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("수정 결정체 지팡이1")) {
                createList.add(new CreateItem("제로스의 지팡이", true, 0, true, 10, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "수정 결정체 지팡이", 1, 2, 1, 100);

            } else if (action.equalsIgnoreCase("수정 결정체 지팡이2")) {
                createList.add(new CreateItem("제로스의 지팡이", true, 0, true, 9, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 400000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "수정 결정체 지팡이", 1, 1, 1, 100);

            } else if (action.equalsIgnoreCase("마법서 (미티어 스트라이크)")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "마법서 (미티어 스트라이크)", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("기술서 (카운터 배리어)")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 500000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "기술서 (카운터 배리어)", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("마법서 (브레이브 멘탈)")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 500000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "마법서 (브레이브 멘탈)", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("마법서 (디스인티그레이트)")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 500000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "마법서 (디스인티그레이트)", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("아머 브레이크")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 500000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "아머 브레이크", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("정령의 수정 (스트라이커 게일)")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "정령의 수정 (스트라이커 게일)", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("정령의 수정 (폴루트 워터)")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "정령의 수정 (폴루트 워터)", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("정령의 수정 (소울 오브 프레임)")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "정령의 수정 (소울 오브 프레임)", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("정령의 수정 (어스 바인드)")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 300000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "정령의 수정 (어스 바인드)", 1, 0, 1, 100);
                // -------------------------------------------------------------------------

            } else if (action.equalsIgnoreCase("대마법사의 모자")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "대마법사의 모자", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("머미로드의 왕관")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "머미로드의 왕관", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("지휘관의 투구")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "지휘관의 투구", 1, 0, 1, 100);
                // ---------------------------------------------------------------------------
            } else if (action.equalsIgnoreCase("화룡의 티셔츠")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "화룡의 티셔츠", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("풍룡의 티셔츠")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "풍룡의 티셔츠", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("수룡의 티셔츠")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "수룡의 티셔츠", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("지룡의 티셔츠")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "지룡의 티셔츠", 1, 0, 1, 100);
                // --------------------------------------------------------------------

            } else if (action.equalsIgnoreCase("발라카스의 완력")) {
                createList.add(new CreateItem("멸마의 판금 갑옷", false, 1, true, 0, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "발라카스의 완력", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("발라카스의 인내력")) {
                createList.add(new CreateItem("멸마의 가죽 갑옷", false, 1, true, 0, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "발라카스의 인내력", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("발라카스의 예지력")) {
                createList.add(new CreateItem("멸마의 비늘 갑옷", false, 1, true, 0, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "발라카스의 예지력", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("발라카스의 마력")) {
                createList.add(new CreateItem("멸마의 로브", false, 1, true, 0, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "발라카스의 마력", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("안타라스의 완력")) {
                createList.add(new CreateItem("멸마의 판금 갑옷", false, 1, true, 0, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "안타라스의 완력", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("안타라스의 예지력")) {
                createList.add(new CreateItem("멸마의 비늘 갑옷", false, 1, true, 0, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "안타라스의 예지력", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("안타라스의 인내력")) {
                createList.add(new CreateItem("멸마의 가죽 갑옷", false, 1, true, 0, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "안타라스의 인내력", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("안타라스의 마력")) {
                createList.add(new CreateItem("멸마의 로브", false, 1, true, 0, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "안타라스의 마력", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("파푸리온의 완력")) {
                createList.add(new CreateItem("멸마의 판금 갑옷", false, 1, true, 0, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "파푸리온의 완력", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("파푸리온의 예지력")) {
                createList.add(new CreateItem("멸마의 비늘 갑옷", false, 1, true, 0, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "파푸리온의 예지력", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("파푸리온의 인내력")) {
                createList.add(new CreateItem("멸마의 가죽 갑옷", false, 1, true, 0, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "파푸리온의 인내력", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("파푸리온의 마력")) {
                createList.add(new CreateItem("멸마의 로브", false, 1, true, 0, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "파푸리온의 마력", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("린드비오르의 완력")) {
                createList.add(new CreateItem("멸마의 판금 갑옷", false, 1, true, 0, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "린드비오르의 완력", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("린드비오르의 예지력")) {
                createList.add(new CreateItem("멸마의 비늘 갑옷", false, 1, true, 0, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "린드비오르의 예지력", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("린드비오르의 인내력")) {
                createList.add(new CreateItem("멸마의 가죽 갑옷", false, 1, true, 0, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "린드비오르의 인내력", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("린드비오르의 마력")) {
                createList.add(new CreateItem("멸마의 로브", false, 1, true, 0, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "린드비오르의 마력", 1, 0, 1, 100);
                // ----------------------------------------------------------------------------

            } else if (action.equalsIgnoreCase("뱀파이어의 망토")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "뱀파이어의 망토", 1, 0, 1, 100);
                // ------------------------------------------------------------------------------

            } else if (action.equalsIgnoreCase("격분의 장갑")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "격분의 장갑", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("아이리스의 장갑")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "아이리스의 장갑", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("대마법사의 장갑")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "대마법사의 장갑", 1, 0, 1, 100);
                // --------------------------------------------------------------------------------

            } else if (action.equalsIgnoreCase("나이트발드의 부츠")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "나이트발드의 부츠", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("아이리스의 부츠")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "아이리스의 부츠", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("뱀파이어의 부츠")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "뱀파이어의 부츠", 1, 0, 1, 100);
                // --------------------------------------------------------------------------------

            } else if (action.equalsIgnoreCase("쿠거의 가더")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "쿠거의 가더", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("우그누스의 가더")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "우그누스의 가더", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("시어의 심안")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "시어의 심안", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("반역자의 방패")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 150000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "반역자의 방패", 1, 0, 1, 100);
                // --------------------------------------------------------------------------------

            } else if (action.equalsIgnoreCase("신성한 완력의 반지")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 완력의 반지", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 민첩의 반지")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 민첩의 반지", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 지식의 반지")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 지식의 반지", 1, 0, 1, 100);
                // --------------------------------------------------------------------------------
                // 0반지

            } else if (action.equalsIgnoreCase("신성한 완력의 반지1")) {
                createList.add(new CreateItem("완력의 반지", true, 0, true, 8, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 완력의 반지", 1, 7, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 민첩의 반지")) {
                createList.add(new CreateItem("민첩의 반지", true, 0, true, 8, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 민첩의 반지", 1, 7, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 지식의 반지")) {
                createList.add(new CreateItem("지식의 반지", true, 0, true, 8, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 지식의 반지", 1, 7, 1, 100);
                // --------------------------------------------------------------------------------
                // 1반지

            } else if (action.equalsIgnoreCase("신성한 완력의 반지1")) {
                createList.add(new CreateItem("완력의 반지", true, 0, true, 7, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 완력의 반지", 1, 6, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 민첩의 반지")) {
                createList.add(new CreateItem("민첩의 반지", true, 0, true, 7, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 민첩의 반지", 1, 6, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 지식의 반지")) {
                createList.add(new CreateItem("지식의 반지", true, 0, true, 7, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 지식의 반지", 1, 6, 1, 100);
                // --------------------------------------------------------------------------------
                // 2반지
            } else if (action.equalsIgnoreCase("신성한 완력의 목걸이")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 완력의 목걸이", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 민첩의 목걸이")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 민첩의 목걸이", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 지식의 목걸이")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 지식의 목걸이", 1, 0, 1, 100);
                // --------------------------------------------------------------------------------
                // 0목걸이
            } else if (action.equalsIgnoreCase("신성한 완력의 목걸이1")) {
                createList.add(new CreateItem("완력의 목걸이", true, 0, true, 8, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 완력의 목걸이", 1, 7, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 민첩의 목걸이1")) {
                createList.add(new CreateItem("민첩의 목걸이", true, 0, true, 8, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 민첩의 목걸이", 1, 7, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 지식의 목걸이1")) {
                createList.add(new CreateItem("민첩의 목걸이", true, 0, true, 8, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 지식의 목걸이", 1, 7, 1, 100);
                // --------------------------------------------------------------------------------
                // 1목걸이
            } else if (action.equalsIgnoreCase("신성한 완력의 목걸이2")) {
                createList.add(new CreateItem("완력의 목걸이", true, 0, true, 7, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 완력의 목걸이", 1, 6, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 민첩의 목걸이2")) {
                createList.add(new CreateItem("민첩의 목걸이", true, 0, true, 7, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 민첩의 목걸이", 1, 6, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 지식의 목걸이2")) {
                createList.add(new CreateItem("민첩의 목걸이", true, 0, true, 7, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 지식의 목걸이", 1, 6, 1, 100);
                // --------------------------------------------------------------------------------
                // 2목걸이
            } else if (action.equalsIgnoreCase("신성한 완력의 벨트")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 완력의 벨트", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 민첩의 벨트")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 민첩의 벨트", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 지식의 벨트")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 지식의 벨트", 1, 0, 1, 100);
                // --------------------------------------------------------------------------------0벨트
            } else if (action.equalsIgnoreCase("신성한 완력의 벨트1")) {
                createList.add(new CreateItem("완력의 벨트", true, 0, true, 8, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 완력의 벨트", 1, 7, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 민첩의 벨트1")) {
                createList.add(new CreateItem("민첩의 벨트", true, 0, true, 8, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 민첩의 벨트", 1, 7, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 지식의 벨트1")) {
                createList.add(new CreateItem("지식의 벨트", true, 0, true, 8, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 지식의 벨트", 1, 7, 1, 100);
                // --------------------------------------------------------------------------------1벨트
            } else if (action.equalsIgnoreCase("신성한 완력의 벨트2")) {
                createList.add(new CreateItem("완력의 벨트", true, 0, true, 7, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, false, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 완력의 벨트", 1, 6, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 민첩의 벨트2")) {
                createList.add(new CreateItem("민첩의 벨트", true, 0, true, 7, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 민첩의 벨트", 1, 6, 1, 100);

            } else if (action.equalsIgnoreCase("신성한 지식의 벨트2")) {
                createList.add(new CreateItem("지식의 벨트", true, 0, true, 7, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "신성한 지식의 벨트", 1, 6, 1, 100);
                // --------------------------------------------------------------------------------2벨트
            } else if (action.equalsIgnoreCase("화령의 귀걸이")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "화령의 귀걸이", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("풍령의 귀걸이")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "풍령의 귀걸이", 1, 0, 1, 100);

            } else if (action.equalsIgnoreCase("수령의 귀걸이")) {
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "수령의 귀걸이", 1, 0, 1, 100);
                // ----------------------------------------------------------------------------0귀걸이
            } else if (action.equalsIgnoreCase("화령의 귀걸이1")) {
                createList.add(new CreateItem("노예의 귀걸이", true, 0, true, 8, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "화령의 귀걸이", 1, 7, 1, 100);

            } else if (action.equalsIgnoreCase("풍령의 귀걸이1")) {
                createList.add(new CreateItem("노예의 귀걸이", true, 0, true, 8, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "풍령의 귀걸이", 1, 7, 1, 100);

            } else if (action.equalsIgnoreCase("수령의 귀걸이1")) {
                createList.add(new CreateItem("노예의 귀걸이", true, 0, true, 8, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "수령의 귀걸이", 1, 7, 1, 100);
                // ----------------------------------------------------------------------------1귀걸이
            } else if (action.equalsIgnoreCase("화령의 귀걸이2")) {
                createList.add(new CreateItem("노예의 귀걸이", true, 0, true, 7, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "화령의 귀걸이", 1, 6, 1, 100);

            } else if (action.equalsIgnoreCase("풍령의 귀걸이2")) {
                createList.add(new CreateItem("노예의 귀걸이", true, 0, true, 7, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "풍령의 귀걸이", 1, 6, 1, 100);

            } else if (action.equalsIgnoreCase("수령의 귀걸이2")) {
                createList.add(new CreateItem("노예의 귀걸이", true, 0, true, 7, 1));
                createList.add(new CreateItem("전설 제작 비법서", false, 1, true, 0, 1));
                createList.add(new CreateItem("신비한 날개깃털", false, 1, false, 0, 200000));
                if (!checkItem(pc, createList, itemList))
                    return;

                createItem(pc, createList, itemList, "수령의 귀걸이", 1, 6, 1, 100);
                // ----------------------------------------------------------------------------2귀걸이

            }
        }
    }

    public boolean checkItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList) {
        boolean allItemsAvailable = true;
        StringBuilder missingItems = new StringBuilder("부족한 재료: ");

        if (createList != null) {
            for (CreateItem list : createList) {
                boolean found = false;
                for (ItemInstance i : pc.getInventory().getList()) {
                    if (i.getItem() != null &&
                            i.getItem().getName().equalsIgnoreCase(list.itemName) &&
                            i.getCount() >= list.count &&
                            !i.isEquipped()) {

                        // 인첸트 확인
                        if (list.isCheckEnchant && i.getEnLevel() != list.enchant) {
                            continue;
                        }
                        // 축복 확인
                        if (list.isCheckBless && i.getBless() != list.bless) {
                            continue;
                        }

                        itemList.add(i);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    allItemsAvailable = false;
                    String prefix = "";
                    if (list.isCheckEnchant && list.enchant > 0)
                        prefix += "+" + list.enchant + " ";
                    if (list.isCheckBless && list.bless == 0)
                        prefix += "축복받은 ";

                    missingItems.append(String.format("%s%s(%,d) ", prefix, list.itemName, list.count));
                }
            }
        }

        if (!allItemsAvailable) {
            ChattingController.toChatting(pc, missingItems.toString(), Lineage.CHATTING_MODE_MESSAGE);
            itemList.clear(); // 실패했으니 담아둔 리스트 초기화
        }

        return allItemsAvailable;
    }

    // ▼▼▼ [핵심] createItem 메서드 구현 (재료 차감 및 아이템 생성) ▼▼▼
    public void createItem(PcInstance pc, List<CreateItem> createList, List<ItemInstance> itemList,
            String createItemName, int bless, int enchant, int count, int successRate) {
        Random random = new Random();
        boolean isSuccess = random.nextInt(100) < successRate;

        // 재료 차감 (itemList에는 이미 검증된 아이템들이 들어있음)
        for (CreateItem list : createList) {
            for (ItemInstance item : itemList) {
                if (item != null && item.getItem() != null
                        && list.itemName.equalsIgnoreCase(item.getItem().getName())) {
                    // 사장님 서버 방식인 count() 메서드 사용
                    pc.getInventory().count(item, item.getCount() - list.count, true);
                    break;
                }
            }
        }

        if (!isSuccess) {
            ChattingController.toChatting(pc, String.format("'%s' 제작에 실패하였습니다. 재료가 차감되었습니다.", createItemName),
                    Lineage.CHATTING_MODE_MESSAGE);
            return;
        }

        // 아이템 지급
        Item i = ItemDatabase.find(createItemName);
        if (i != null) {
            // 겹치는 아이템(주문서 등) 처리
            if (i.isPiles()) {
                ItemInstance temp = pc.getInventory().find(i.getName(), bless, i.isPiles());
                if (temp != null) {
                    pc.getInventory().count(temp, temp.getCount() + count, true);
                } else {
                    ItemInstance newItem = ItemDatabase.newInstance(i);
                    newItem.setObjectId(ServerDatabase.nextItemObjId());
                    newItem.setBless(bless);
                    newItem.setEnLevel(enchant);
                    newItem.setCount(count);
                    newItem.setDefinite(true);
                    pc.getInventory().append(newItem, true);
                }
            } else {
                // 장비류 처리 (count 만큼 반복 생성하거나 1개 생성)
                ItemInstance newItem = ItemDatabase.newInstance(i);
                newItem.setObjectId(ServerDatabase.nextItemObjId());
                newItem.setBless(bless);
                newItem.setEnLevel(enchant);
                newItem.setCount(count);
                newItem.setDefinite(true);
                pc.getInventory().append(newItem, true);
            }
            ChattingController.toChatting(pc, String.format("'%s' 제작에 성공하였습니다!", createItemName),
                    Lineage.CHATTING_MODE_MESSAGE);
        }
    }
}