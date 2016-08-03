package ch.sharpsoft.hexapod;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
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

	@Test
	public void testSetEndpointDefault() throws Exception {
		Hexapod hp = new Hexapod();
		List<Leg> legs = hp.getLegs();
		SecureRandom r = new SecureRandom();
		for (Leg leg : legs) {
			Vector3 endpoint = leg.getEndpoint();
			leg.setEndpoint(endpoint);
			assertEquals(endpoint, leg.getEndpoint());
		}
	}

	@Test
	public void testSetEndpointRandom() throws Exception {
		Hexapod hp = new Hexapod();
		List<Leg> legs = hp.getLegs();
		SecureRandom r = new SecureRandom();
		for (Leg leg : legs) {
			for (int i = 0; i < 10000; i++) {
				leg.setAngles(r.nextDouble(), r.nextDouble(), r.nextDouble());
				Vector3 endpoint = leg.getEndpoint();
				try {
					leg.setEndpoint(endpoint);
					assertEquals(endpoint, leg.getEndpoint());
				} catch (IllegalArgumentException e) {
					// Ignore
				}
			}
		}
	}

	private void assertEquals(Vector3 v1, Vector3 v2) {
		Assert.assertEquals("X", v1.getX(), v2.getX(), 0.02);
		Assert.assertEquals("Y", v1.getY(), v2.getY(), 0.02);
		Assert.assertEquals("Z", v1.getZ(), v2.getZ(), 0.02);
	}
}