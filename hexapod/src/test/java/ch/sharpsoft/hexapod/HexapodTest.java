package ch.sharpsoft.hexapod;

import java.util.List;

import org.junit.Test;

public class HexapodTest {

	@Test
	public void testSetup() throws Exception {
		Hexapod hp = new Hexapod();
		List<Leg> legs = hp.getLegs();
		Leg leg1 = legs.get(5);
		leg1.setAngles(Math.PI / 4, Math.PI / 6, -Math.PI / 8);
		LegSegment startSegment = leg1.getStartSegment();
		Vector3 vector = startSegment.getVector();
		Vector3 v2 = leg1.getSegment1().getVector();
		double angle = leg1.getK1().getAngle();
		System.out.println(angle);
		System.out.println(v2);
	}
}