package Fx.server.UIBoard.BuffIcons;

import Fx.server.MJTemplate.MJProto.MJEProtoMessages;
import Fx.server.MJTemplate.MJProto.ProtoOutputStream;
import Fx.server.MJTemplate.MJProto.Models.SC_BUFFDATA_NOTI;
import Fx.server.UIBoard.UIBoardProvider;
import lineage.network.LineageClient;
import lineage.world.World;
import lineage.world.object.instance.PcInstance;

public class BuffIconsProvider extends UIBoardProvider{

	private ProtoOutputStream mBuffDatas;
	public BuffIconsProvider(){
		mBuffDatas = null;
	}
	
	private ProtoOutputStream createBuffData(){
		SC_BUFFDATA_NOTI buffdataNoti = SC_BUFFDATA_NOTI.newInstance();
		BuffIconsDao.getBuffIconItems().forEach(item -> {
			buffdataNoti.addBuffData(item.getIcon(), item.getDuration(), item.getGroup(), item.getStartMsg(), item.getEndMsg()); });
		return buffdataNoti.writeTo(MJEProtoMessages.SC_BUFFDATA_NOTI);
	}
	
	private void onLoad(){
		mBuffDatas = createBuffData();
	}
	
	@Override
	public void onStart() {
		onLoad();
	}

	@Override
	public void onMinuteChanged(int minute) {
	}

	@Override
	public void onEnterSelectCharacter(LineageClient clnt) {
		clnt.sendProto(mBuffDatas, false);
	}

	@Override
	public void onEnterWorld(PcInstance pc) {
	}

	@Override
	public void onLeaveWorld(String accountName, String characterName, int characterId) {
	}

	@Override
	public void onRequest(PcInstance pc, long version, Object arg) {
	}

	@Override
	public void onReload(PcInstance pc, Object arg) {
		onLoad();
		for(PcInstance other : World.getPcList()){
			other.sendProto(mBuffDatas, false);
		}
	}

}
