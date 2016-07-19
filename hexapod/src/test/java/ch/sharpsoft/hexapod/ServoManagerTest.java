package ch.sharpsoft.hexapod;

import ch.sharpsoft.hexapod.transfer.RemotePaho;

public class ServoManagerTest {

	public static void main(String[] args) throws Exception {
		Shell s = new Shell();
		Hexapod hp = new Hexapod();
		RemotePaho remote = new RemotePaho();
		remote.init();
		ServoManager sm = new ServoManager(hp, remote);
		double i = 0.0;
		while (true) {
			// hp.getLegs().stream().filter(p->p.getId()%2==0).forEach(action);
			sm.sendState();
			Thread.sleep(100);
		}
	}
}