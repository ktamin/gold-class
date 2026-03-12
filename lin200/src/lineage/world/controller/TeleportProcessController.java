package lineage.world.controller;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectLock;
import lineage.world.object.object;

public class TeleportProcessController implements Runnable {
	static class TeleportProcessArgs{		
		public object sender;
		public object receiver;
		public long expiration;
		private TeleportProcessArgs(object sender, object receiver, long expiration) {
			this.sender = sender;
			this.receiver = receiver;
			this.expiration = expiration;
		}
		
	}
	
	private static final TeleportProcessController mController = new TeleportProcessController();
	private static CopyOnWriteArrayList<TeleportProcessArgs> mTeleportProcessList = new CopyOnWriteArrayList<>();	
	private boolean mRun;
	private TeleportProcessController() {
		mRun = false;
	}
	
	public static void append(object sender, object receiver, long expiration) {
		TeleportProcessArgs args = new TeleportProcessArgs(sender, receiver, System.currentTimeMillis() + expiration);
		mTeleportProcessList.add(args);
	}

	@Override
	public void run() {
		while(mRun) {
			try {
				toTimer(System.currentTimeMillis());
			}catch(Exception e) {
				lineage.share.System.println("TeleportProcessController");
				lineage.share.System.println(e);
			}	
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void stop() {
		mController.mRun = false;
	}
	
	public static void execute() {
		if(mController.mRun) {
			return;
		}
		mController.mRun = true;
		new Thread(mController).start();
	}
	
	public static void toTimer(long time) {
		ArrayList<TeleportProcessArgs> waitArgs = new ArrayList<>(32);
		try {
			for(TeleportProcessArgs args : mTeleportProcessList) {
				if(args.expiration > time) {
					continue;
				}
				waitArgs.add(args);
				args.receiver.toSender(S_ObjectLock.clone(BasePacketPooling.getPool(S_ObjectLock.class), 40, args.sender.getObjectId()));
			}
		}finally {
			mTeleportProcessList.removeAll(waitArgs);
		}
	}
}
