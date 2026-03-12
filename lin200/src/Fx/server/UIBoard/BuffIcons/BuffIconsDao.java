package Fx.server.UIBoard.BuffIcons;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Fx.server.MJTemplate.MJProto.FullSelectorHandler;
import Fx.server.MJTemplate.MJProto.Selector;

class BuffIconsDao {

	static List<BuffIconItem> getBuffIconItems() {
		final List<BuffIconItem> items = new ArrayList<>();
		Selector.exec("select * from fx_buff_icon order by id asc", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					items.add(BuffIconItem.newItem(rs));
				}
			}
		});
		return items;
	}
}
