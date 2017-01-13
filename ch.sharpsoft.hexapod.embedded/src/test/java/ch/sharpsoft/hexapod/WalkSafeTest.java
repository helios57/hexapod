package ch.sharpsoft.hexapod;

import org.junit.Test;

import ch.sharpsoft.hexapod.util.Vector3;

public class WalkSafeTest {
	@Test
	public void testName() throws Exception {
		Hexapod hp = new Hexapod();
		WalkSafe ws = new WalkSafe(hp);
		ws.setDirection(new Vector3(1, 0, 0));
		for (int i = 0; i < 10; i++) {
			System.out.println();
			hp.getLegs().stream().map(l -> l.getEndpoint()).forEach(System.out::println);
			ws.doNextAction();
		}
	}
}
