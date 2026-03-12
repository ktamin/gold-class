package Fx.server.MJTemplate.MJProto;

import Fx.server.MJTemplate.MJProto.Models.SC_CUSTOM_SETTINGS_NOTI;
import lineage.world.object.instance.PcInstance;

public class ClientSetting {
	public static ClientSetting newEmpty(PcInstance owner){
		return new ClientSetting(owner);
	}

	//@SuppressWarnings("unused")	
	private static final int Empty = 0;
	private static final int AttackContinue = 1 << 0;
	private static final int Minimap = 1 << 1;

	private static final int Default = Empty;

	private int mFlags;
	private PcInstance mOwner;
	private ClientSetting(PcInstance owner){
		mFlags = Default;
		mOwner = owner;
	}
	
	private ClientSetting addFlags(int flag){
		mFlags |= flag;
		return this;
	}
	
	private ClientSetting removeFlags(int flag){
		mFlags &= ~(flag);
		return this;
	}
	
	private ClientSetting toggleFlags(int flag){
		return hasFlags(flag) ? removeFlags(flag) : addFlags(flag);
	}

	private boolean hasFlags(int flag){
		return (mFlags & flag) == flag;
	}

	public ClientSetting onAttackContinue(){
		return addFlags(AttackContinue);
	}

	public ClientSetting offAttackContinue(){
		return removeFlags(AttackContinue);
	}
	
	public ClientSetting toggleAttackContinue(){
		return toggleFlags(AttackContinue);
	}

	public ClientSetting onMinimap(){
		return addFlags(Minimap);
	}

	public ClientSetting offMinimap(){
		return removeFlags(Minimap);
	}
	
	public ClientSetting toggleMinimap(){
		return toggleFlags(Minimap);
	}
	
	public void update(){
		SC_CUSTOM_SETTINGS_NOTI.newInstance()
			.setFlags(mFlags)
			.send(mOwner);
	}

	public ClientSetting setFlags(int flags){
		mFlags = flags;
		return this;
	}
	
	public int getFlags(){
		return mFlags;
	}
}
