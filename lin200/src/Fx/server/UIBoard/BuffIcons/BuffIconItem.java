package Fx.server.UIBoard.BuffIcons;

import java.sql.ResultSet;
import java.sql.SQLException;

class BuffIconItem {
	static BuffIconItem newItem(ResultSet rs) throws SQLException{
		BuffIconItem iconItem = newEmpty()
				.setId(rs.getInt("id"))
				.setIcon(rs.getInt("icon"))
				.setDuration(rs.getInt("duration"))
				.setGroup(rs.getInt("group"))
				.setStartMsg(rs.getInt("startMsg"))
				.setEndMsg(rs.getInt("endMsg"));		
		return iconItem;
	}
	
	static BuffIconItem newEmpty(){
		return new BuffIconItem();
	}
	
	private int mId;
	private int mIcon;
	private int mDuration;
	private int mGroup;
	private int mStartMsg;
	private int mEndMsg;
	
	BuffIconItem setId(int n) {
		mId = n;
		return this;
	}
	BuffIconItem setIcon(int n) {
		mIcon = n;
		return this;
	}
	BuffIconItem setDuration(int n) {
		mDuration = n;
		return this;
	}
	BuffIconItem setGroup(int n) {
		mGroup = n;
		return this;
	}
	BuffIconItem setStartMsg(int n) {
		mStartMsg = n;
		return this;
	}
	BuffIconItem setEndMsg(int n) {
		mEndMsg = n;
		return this;
	}

	int getId() {
		return mId;
	}
	int getIcon() {
		return mIcon;
	}
	int getDuration() {
		return mDuration;
	}
	int getGroup() {
		return mGroup;
	}
	int getStartMsg() {
		return mStartMsg;
	}
	int getEndMsg() {
		return mEndMsg;
	}
}
