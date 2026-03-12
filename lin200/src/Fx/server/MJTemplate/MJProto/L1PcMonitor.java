package Fx.server.MJTemplate.MJProto;

import lineage.world.World;
import lineage.world.object.instance.PcInstance;

/**
 * L1PcInstance의 정기 처리, 감시 처리등을 실시하기 때문에(위해)의 공통적인 처리를 실장한 추상 클래스
 * 
 * 각 태스크 처리는{@link #run()}는 아니고{@link #execTask(L1PcInstance)}에서 실장한다. PC가 로그아웃 하는 등 해 서버상에 존재하지 않게 되었을 경우, run() 메소드에서는 즉석에서 리턴 한다. 그 경우, 태스크가 정기 실행 스케줄링 되고 있으면(자), 로그아웃 처리등으로 스케줄링을 정지할 필요가 있다. 정지하지 않으면 태스크는 멈추지 않고, 영원히
 * 정기 실행되게 된다. 정기 실행이 아니고 단발 액션의 경우는 그러한 제어는 불요.
 * 
 * L1PcInstance의 참조를 직접 가지는 것은 바람직하지 않다.
 * 
 * @author frefre
 *
 */
public abstract class L1PcMonitor implements Runnable {

	/** 모니터 대상 L1PcInstance의 오브젝트 ID */
	protected int _id;

	/**
	 * 지정된 파라미터로 L1PcInstance에 대한 모니터를 작성한다.
	 * 
	 * @param oId
	 *            {@link L1PcInstance#getId()}로 취득할 수 있는 오브젝트 ID
	 */
	public L1PcMonitor(int oId) {
		_id = oId;
	}

	@Override
	public final void run() {
		PcInstance pc = World.findPc(_id);
		if (pc == null) {
			return;
		}

		if (pc.getClient() == null && !pc.isFishing()) {
			return;
		}

		execTask(pc);
	}

	/**
	 * 태스크 실행시의 처리
	 * 
	 * @param pc
	 *            모니터 대상의 PC
	 */
	public abstract void execTask(PcInstance pc);
}
