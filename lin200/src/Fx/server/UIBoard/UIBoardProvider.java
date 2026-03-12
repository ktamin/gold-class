package Fx.server.UIBoard;

import lineage.network.LineageClient;
import lineage.world.object.instance.PcInstance;

public abstract class UIBoardProvider {
	public abstract void onStart();
	public abstract void onMinuteChanged(final int minute);
	public abstract void onEnterSelectCharacter(final LineageClient clnt);	
	public abstract void onEnterWorld(final PcInstance pc);
	public abstract void onLeaveWorld(final String accountName, String characterName, int characterId);
	public abstract void onRequest(final PcInstance pc, final long version, final Object arg);
	public abstract void onReload(final PcInstance pc, final Object arg);
}
