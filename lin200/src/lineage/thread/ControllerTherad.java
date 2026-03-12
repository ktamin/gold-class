package lineage.thread;

import lineage.share.Common;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.controller.AgitController;
import lineage.world.controller.AuctionController;
import lineage.world.controller.ColosseumController;
import lineage.world.controller.ElvenforestController;
import lineage.world.controller.FightController;
import lineage.world.controller.KingdomController;
import lineage.world.controller.SpotController;
import lineage.world.controller.TeamBattleController;
import lineage.world.controller.고라스컨트롤러;
import lineage.world.controller.고무컨트롤러;
import lineage.world.controller.그신컨트롤러;
import lineage.world.controller.기감컨트롤러;
import lineage.world.controller.드워프컨트롤러;
import lineage.world.controller.라바던전컨트롤러;
import lineage.world.controller.마족신전컨트롤러;
import lineage.world.controller.보물찾기컨트롤러;
import lineage.world.controller.악마왕의영토컨트롤러;
import lineage.world.controller.얼던컨트롤러;
import lineage.world.controller.오만10층컨트롤러;
import lineage.world.controller.오만1층컨트롤러;
import lineage.world.controller.오만2층컨트롤러;
import lineage.world.controller.오만3층컨트롤러;
import lineage.world.controller.오만4층컨트롤러;
import lineage.world.controller.오만5층컨트롤러;
import lineage.world.controller.오만6층컨트롤러;
import lineage.world.controller.오만7층컨트롤러;
import lineage.world.controller.오만8층컨트롤러;
import lineage.world.controller.오만9층컨트롤러;
import lineage.world.controller.오만정상컨트롤러;
import lineage.world.controller.월드보스컨트롤러;
import lineage.world.controller.정무컨트롤러;
import lineage.world.controller.지옥컨트롤러;
import lineage.world.controller.지하수로컨트롤러;
import lineage.world.controller.칠흑던전3층컨트롤러;
import lineage.world.controller.칠흑던전4층컨트롤러;
import lineage.world.controller.칠흑던전컨트롤러;
import lineage.world.controller.타임이벤트컨트롤러;
import lineage.world.controller.테베라스컨트롤러;
import lineage.world.controller.펭귄사냥컨트롤러;

public class ControllerTherad implements Runnable {
	static private ControllerTherad thread;
	// 쓰레드동작 여부
	static private boolean running;

	static public void init() {
		TimeLine.start("ControllerTherad..");

		thread = new ControllerTherad();
		start();

		TimeLine.end();
	}

	static private void start() {
		running = true;
		Thread t = new Thread(thread);
		t.setName(ControllerTherad.class.toString());
		t.start();
	}

	@Override
	public void run() {
		for (; running;) {
			try {
				long time = System.currentTimeMillis();

				// 팀대전 관리.
				try {
					TeamBattleController.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("팀대전 관리.");
					lineage.share.System.println(e);
				}

				// 성 관리
				try {
					KingdomController.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("성 관리");
					lineage.share.System.println(e);
				}

				// 스팟 쟁탈전 관리.
				try {
					SpotController.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("스팟 쟁탈전 관리.");
					lineage.share.System.println(e);
				}

				// 테베 던전 관리.
				try {
					테베라스컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("테베 던전 관리.");
					lineage.share.System.println(e);
				}

				// 지옥 던전 관리.
				try {
					보물찾기컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("보물상자 찾기 관리.");
					lineage.share.System.println(e);
				}

				// 지옥 던전 관리.
				try {
					지옥컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("지옥 던전 관리.");
					lineage.share.System.println(e);
				}
				// 지하수로 던전 관리.
				try {
					지하수로컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("지하수로 던전 관리.");
					lineage.share.System.println(e);
				}
				// 펭귄사냥컨트롤러 관리.
				try {
					펭귄사냥컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("펭귄사냥터 관리.");
					lineage.share.System.println(e);
				}
				// 마족신전 던전 관리.
				try {
					마족신전컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("마족신전 던전 관리.");
					lineage.share.System.println(e);
				}

				// 라바던전 관리.
				try {
					라바던전컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("라스타바드 던전 관리.");
					lineage.share.System.println(e);
				}

				// 칠흑 던전 관리.
				try {
					칠흑던전컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("칠흑 던전 관리.");
					lineage.share.System.println(e);

				}
				// 칠흑3츷 던전 관리.
				try {
					칠흑던전3층컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("칠흑 던전 3층 관리.");
					lineage.share.System.println(e);

				}

				// 칠흑4층 던전 관리.
				try {
					칠흑던전4층컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("칠흑 던전 4층 관리.");
					lineage.share.System.println(e);

				}
				// 악영 던전 관리.
				try {
					악마왕의영토컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("악마왕의영토 던전 관리.");
					lineage.share.System.println(e);

				}

				// 고무 던전 관리.
				try {
					고무컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("고무 던전 관리.");
					lineage.share.System.println(e);
				}

				// 얼던 관리. 야도란
				try {
					얼던컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("얼던 던전 관리.");
					lineage.share.System.println(e);
				}

				// 정무 관리. 야도란
				try {
					정무컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("정무 던전 관리.");
					lineage.share.System.println(e);
				}

				// 그신 관리. 야도란
				try {
					그신컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("그신 던전 관리.");
					lineage.share.System.println(e);
				}

				// 기감 관리. 야도란
				try {
					기감컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("기감 던전 관리.");
					lineage.share.System.println(e);
				}

				// 기감 관리. 야도란
				try {
					고라스컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("고라스 던전 관리.");
					lineage.share.System.println(e);
				}

				// 기감 관리. 야도란
				try {
					드워프컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("드워프 던전 관리.");
					lineage.share.System.println(e);
				}
				
				//오만1층
				try {
					오만1층컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("오만1층 던전 관리.");
					lineage.share.System.println(e);
				}
				
				//오만2층
				try {
					오만2층컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("오만2층 던전 관리.");
					lineage.share.System.println(e);
				}
				
				//오만3층
				try {
					오만3층컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("오만3층 던전 관리.");
					lineage.share.System.println(e);
				}
				
				//오만4층
				try {
					오만4층컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("오만4층 던전 관리.");
					lineage.share.System.println(e);
				}
				
				//오만5층
				try {
					오만5층컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("오만5층 던전 관리.");
					lineage.share.System.println(e);
				}
				
				//오만6층
				try {
					오만6층컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("오만6층 던전 관리.");
					lineage.share.System.println(e);
				}
				
				//오만7층
				try {
					오만7층컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("오만7층 던전 관리.");
					lineage.share.System.println(e);
				}
				
				//오만8층
				try {
					오만8층컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("오만8층 던전 관리.");
					lineage.share.System.println(e);
				}
				
				//오만9층
				try {
					오만9층컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("오만9층 던전 관리.");
					lineage.share.System.println(e);
				}
				
				//오만10층
				try {
					오만10층컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("오만10층 던전 관리.");
					lineage.share.System.println(e);
				}
				
				//오만정상층
				try {
					오만정상컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("오만정상 던전 관리.");
					lineage.share.System.println(e);
				}

				try {
					월드보스컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("월드보스 던전 관리.");
					lineage.share.System.println(e);
				}
				try {
					타임이벤트컨트롤러.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.println("타임이벤트  관리.");
					lineage.share.System.println(e);
				}
				// 경매 관리
				try {
					if (Lineage.server_version >= 200) {
						AuctionController.toTimer(time);
					}
				} catch (Exception e) {
					lineage.share.System.println("경매 관리");
					lineage.share.System.println(e);
				}

				// 아지트 관리
				try {
					if (Lineage.server_version >= 200) {
						AgitController.toTimer(time);
					}
				} catch (Exception e) {
					lineage.share.System.println("아지트 관리");
					lineage.share.System.println(e);
				}

				// 콜로세움 관리
				// try { if(Lineage.server_version >= 200){ColosseumController.toTimer(time);} }
				// catch (Exception e) {
				// lineage.share.System.println("콜로세움 관리");
				// lineage.share.System.println(e);
				// }

				// 요정숲 관리
				// try { ElvenforestController.toTimer(time); } catch (Exception e) {
				// lineage.share.System.println("요정숲 관리");
				// lineage.share.System.println(e);
				// }

				// 투기장 관리
				// try { FightController.toTimer(time); } catch (Exception e) {
				// lineage.share.System.println("투기장 관리");
				// lineage.share.System.println(e);
				// }

				Thread.sleep(Common.TIMER_SLEEP);
			} catch (Exception e) {
				lineage.share.System.printf("lineage.thread.ControllerTherad.run()\r\n : %s\r\n", e.toString());
			}
		}
	}

	/**
	 * 쓰레드 종료처리 함수.
	 */
	static public void close() {
		running = false;
		thread = null;
	}

}
