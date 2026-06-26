package lineage.world.object.item;

import java.sql.Connection;

import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_LetterRead;
import lineage.world.controller.LetterController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class Letter extends ItemInstance {

	private int uid;
	private String from;
	private String to;
	private String subject;
	private String memo;
	private long date;

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new Letter();
		return item;
	}

	@Override
	public void close() {
		super.close();

		date = uid = 0;
		from = to = subject = memo = null;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	@Override
	public int getLetterUid() {
		return uid;
	}

	@Override
	public void setLetterUid(int uid) {
		this.uid = uid;
	}

	@Override
	public void toWorldJoin(Connection con, PcInstance pc) {
		super.toWorldJoin(con, pc);
		LetterController.read(con, this);
	}

	/*-------운영자 전체 메세지를 사용하기 위해 수정 2026.06.18	
		@Override
		public void toClick(Character cha, ClientBasePacket cbp){
			if(getItem().getInvGfx()==464){
				// 새편지 작성
				cbp.readH();
				String to = cbp.readS();
				String subject = cbp.readSS();
				String memo = cbp.readSS();
	
				if (subject == null || subject.length() < 2)
					subject = "제목 없음";
				
				if (memo == null || memo.length() < 2)
					subject += "   ";
	
				// 수량 하향
				cha.getInventory().count(this, getCount()-1, true);
				// 편지작성한거 처리.
				LetterController.toLetter(cha.getName(), to, subject, memo, 0);
			}else{
				// 읽거나 않읽은 편지
				if(getItem().getInvGfx()==465)
					//읽은편지지로 변경.
					item = ItemDatabase.find("편지지 - 읽은 편지");
				// 편지창 뛰우기.
				cha.toSender(S_LetterRead.clone(BasePacketPooling.getPool(S_LetterRead.class), this));
			}
		}
	*/
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if (getItem().getInvGfx() == 464) {
			// 새편지 작성
			cbp.readH();
			String to = cbp.readS();
			String subject = cbp.readSS();
			String memo = cbp.readSS();

			if (subject == null || subject.length() < 2)
				subject = "제목 없음";

			if (memo == null || memo.length() < 2)
				memo += "   ";

			// 수량 하향 (편지지 소모)
			cha.getInventory().count(this, getCount() - 1, true);

			PcInstance pc = null;
			if (cha instanceof PcInstance) {
				pc = (PcInstance) cha;
			}

			// =========================================================
			// ✨ [기능 추가] 운영자 전용 "전체" 편지 발송 로직 (getGm 적용)
			// =========================================================
			if (pc != null && pc.getGm() > 0 && to.equalsIgnoreCase("전체")) {

				java.sql.Connection con = null;
				java.sql.PreparedStatement st = null;
				java.sql.ResultSet rs = null;
				int sendCount = 0;

				try {
					con = lineage.database.DatabaseConnection.getLineage();

					// 💡 DB에서 모든 캐릭터 이름을 가져옵니다. (컬럼명이 다를 경우 char_name 부분을 수정하세요)
					st = con.prepareStatement("SELECT char_name FROM characters");
					rs = st.executeQuery();

					// DB에 있는 모든 캐릭터에게 편지를 복사해서 발송
					while (rs.next()) {
						String targetName = rs.getString(1);
						LetterController.toLetter(pc.getName(), targetName, subject, memo, 0);
						sendCount++;
					}

					lineage.world.controller.ChattingController.toChatting(pc,
							"전체 유저(" + sendCount + "명)에게 편지를 성공적으로 발송했습니다.",
							lineage.share.Lineage.CHATTING_MODE_MESSAGE);

				} catch (Exception e) {
					lineage.world.controller.ChattingController.toChatting(pc, "전체 편지 발송 중 오류가 발생했습니다.",
							lineage.share.Lineage.CHATTING_MODE_MESSAGE);
					e.printStackTrace();
				} finally {
					// 💡 팩에 따라 close(st, rs)에서 에러가 날 경우 close(st); close(rs); 로 나눠 적어주세요.
					lineage.database.DatabaseConnection.close(st, rs);
					lineage.database.DatabaseConnection.close(con);
				}

			} else {
				// =========================================================
				// 💀 기존 1:1 편지 발송 로직
				// =========================================================
				LetterController.toLetter(cha.getName(), to, subject, memo, 0);
			}

		} else {
			// 읽거나 안 읽은 편지
			if (getItem().getInvGfx() == 465) {
				// 읽은 편지지로 변경.
				item = ItemDatabase.find("편지지 - 읽은 편지");
			}
			// 편지창 띄우기.
			cha.toSender(S_LetterRead.clone(BasePacketPooling.getPool(S_LetterRead.class), this));
		}
	}
}
