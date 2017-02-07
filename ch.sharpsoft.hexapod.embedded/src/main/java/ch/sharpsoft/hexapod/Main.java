package ch.sharpsoft.hexapod;

import ch.sharpsoft.hexapod.transfer.RemotePaho;
import ch.sharpsoft.hexapod.util.Vector3;

public class Main {

	public static void main(String[] args) {
		final Hexapod hp = new Hexapod();
		final RemotePaho remote = new RemotePaho();
		remote.init();

		remote.setInControlListener((inControlMsg) -> {

		});
		final ServoManager sm = new ServoManager(hp, remote);
		WalkSafe walkSimple = new WalkSafe(hp);
		walkSimple.setDirection(new Vector3(-1, 0, 0));
		while (true) {
			int delay = 100;
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