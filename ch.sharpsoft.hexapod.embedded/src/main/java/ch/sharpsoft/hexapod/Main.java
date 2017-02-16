package ch.sharpsoft.hexapod;

import ch.sharpsoft.hexapod.transfer.ControllerListener;
import ch.sharpsoft.hexapod.transfer.RemotePaho;
import ch.sharpsoft.hexapod.util.Vector3;

public class Main {

	private static int delay = 100;

	public static void main(String[] args) {
		final Hexapod hp = new Hexapod();
		final RemotePaho remote = new RemotePaho();
		remote.init();
		final ServoManager sm = new ServoManager(hp, remote);
		WalkSafe walkSimple = new WalkSafe(hp);
		remote.setControllerListener(new ControllerListener() {

			@Override
			public void onSpeedChange(int speed) {
				System.out.println("speed " + speed);
				delay = 100 + (1000 - speed * 10);
			}

			@Override
			public void onMoveChange(int x, int y) {
				System.out.println("move " + x + ";" + y);
				walkSimple.setDirection(new Vector3(x, y, 0));
			}

			@Override
			public void onHeightChange(int height) {
				System.out.println("height " + height);

			}
		});
		remote.setInControlListener((inControlMsg) -> {
		});
		// walkSimple.setDirection(new Vector3(-1, 0, 0));
		while (true) {
			try {
				walkSimple.doNextAction();
				sm.setMoveTime(delay);
				sm.sendState();
				Thread.sleep(delay);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}