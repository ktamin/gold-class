package Fx.server.UIBoard;

import java.util.Calendar;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import Fx.server.MJTemplate.MJProto.TimeListener.TimeListener;
import Fx.server.MJTemplate.MJProto.TimeListener.gametime.BaseTime;
import Fx.server.MJTemplate.MJProto.TimeListener.gametime.RealTimeClock;
import lineage.network.LineageClient;
import lineage.world.object.instance.PcInstance;

public class UIBoardService implements TimeListener{

	public static final int CharacterNameLength = 14;
	public static final int IntroductionLength = 32;
	public static final int BuffIconProvider = 0;

	
	private static UIBoardService mService = null;
	public static UIBoardService service(){
		if(mService == null){
			mService = new UIBoardService();
		}
		return mService;
	}

	private final BlockingQueue<IUIBoardServiceConsumer> mQueue;
	private UIBoardProvider[] mProviders;
	private UIBoardService(){
		mProviders = new UIBoardProvider[]{
			new Fx.server.UIBoard.BuffIcons.BuffIconsProvider(),
		};
		mQueue = new LinkedBlockingQueue<>();
		mQueue.offer(new IUIBoardServiceConsumer(){
			@Override
			public void accept() {
				long previous = System.currentTimeMillis();
				try{
					for(UIBoardProvider provider : mProviders){
						provider.onStart();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				System.out.println("[UIBoard System Load Completed] " + (System.currentTimeMillis() - previous) + "ms");
			}
		});
		new Thread(new UIBoardConsumer()).start();
		
		RealTimeClock.getInstance().addListener(this, Calendar.MINUTE);
	}
	
	public void onEnterSelectCharacter(final LineageClient clnt){
		mQueue.offer(new IUIBoardServiceConsumer(){
			@Override
			public void accept() {
				for(UIBoardProvider provider : mProviders){
					provider.onEnterSelectCharacter(clnt);	
				}
			}
		});
	}
	
	public void onEnterWorld(final PcInstance pc){
		mQueue.offer(new IUIBoardServiceConsumer(){
			@Override
			public void accept() {
				for(UIBoardProvider provider : mProviders){
					provider.onEnterWorld(pc);
				}
			}
		});
	}

	public void onLeaveWorld(PcInstance pc){
		final String accountName = pc.getClient().getAccountId();
		final String characterName = pc.getName();
		final int characterId = (int) pc.getObjectId();
		mQueue.offer(new IUIBoardServiceConsumer(){
			@Override
			public void accept() {
				for(UIBoardProvider provider : mProviders){
					provider.onLeaveWorld(accountName, characterName, characterId);
				}
			}
		});
	}

	public void onRequest(final int provider, final PcInstance pc, final long version, final Object arg){
		mQueue.offer(new IUIBoardServiceConsumer(){
			@Override
			public void accept() {
				mProviders[provider].onRequest(pc, version, arg);
			}
		});
	}

	public void onRequest(final int provider, final PcInstance pc, final Object arg){
		onRequest(provider, pc, 0, arg);
	}

	public void onReload(final int provider, final PcInstance pc, final Object arg) {
		mQueue.offer(new IUIBoardServiceConsumer(){
			@Override
			public void accept() {
				mProviders[provider].onReload(pc, arg);
			}
		});
	}
	
	
	@Override
	public void onMonthChanged(BaseTime time) {	
	}
	
	@Override
	public void onDayChanged(BaseTime time) {	
	}

	@Override
	public void onHourChanged(BaseTime time) {
	}
	
	@Override
	public void onMinuteChanged(BaseTime time) {
		final int minute = time.get(Calendar.MINUTE);
		mQueue.offer(new IUIBoardServiceConsumer(){
			@Override
			public void accept() {
				for(UIBoardProvider provider : mProviders){
					provider.onMinuteChanged(minute);
				}
			}
		});
	}
	
	@Override
	public void onSecondChanged(BaseTime time) {	
	}
	

	private class UIBoardConsumer implements Runnable{
		@Override
		public void run() {
			IUIBoardServiceConsumer consumer = null;
			try {
				while((consumer = mQueue.take()) != null){
					consumer.accept();
				}
			} catch (InterruptedException e) {
			}
		}
	}
	
	interface IUIBoardServiceConsumer{
		void accept();
	}
}
