package ch.sharpsoft.hexapod;

import org.junit.Assert;
import org.junit.Test;

public class LegTest {

	@Test
	public void testLegAngles() {
		Leg leg1 = new Leg(1, new Vector3(0, 0, 0), 0.1234567);
		leg1.setAngles(Math.PI / 4, Math.PI / 6, -Math.PI / 8);
		Assert.assertEquals(Math.PI / 4, leg1.getK1().getAngle(), 0.01);
		Assert.assertEquals(Math.PI / 6, leg1.getK2().getAngle(), 0.01);
		Assert.assertEquals(-Math.PI / 8, leg1.getK3().getAngle(), 0.01);
	}

	@Test
	public void testLegPositions() {
		Leg leg1 = new Leg(1, new Vector3(0, 0, 0), Math.PI / 2);// 90°
		assertEquals(new Vector3(0, 0, 0), leg1.getStartSegment().getStartPoint());
		assertEquals(new Vector3(0, 0, 0), leg1.getSegment1().getStartPoint());
		assertEquals(new Vector3(0, -5, 0), leg1.getSegment2().getStartPoint());
		assertEquals(new Vector3(0, -12, 0), leg1.getEndSegment().getStartPoint());
		assertEquals(new Vector3(0, -25, 0), leg1.getEndSegment().getEndPoint());
		leg1.setAngles(Math.PI / 4, Math.PI / 4, -Math.PI / 2);// 45° 45° -90°
		assertEquals(new Vector3(0, 0, 0), leg1.getStartSegment().getStartPoint());
		assertEquals(new Vector3(0, 0, 0), leg1.getSegment1().getStartPoint());
		assertEquals(new Vector3(-3.535, -3.535, 0), leg1.getSegment2().getStartPoint());
		assertEquals(new Vector3(-7.0355, -7.0355, -4.949), leg1.getEndSegment().getStartPoint());
		assertEquals(new Vector3(-13.535, -13.535, 4.242), leg1.getEndSegment().getEndPoint());
	}

	@Test
	public void testLegPositions2() {
		Leg leg1 = new Leg(1, new Vector3(0, 0, 0), Math.PI / 2);
		assertEquals(new Vector3(0, 0, 0), leg1.getStartSegment().getStartPoint());
		assertEquals(new Vector3(0, 0, 0), leg1.getSegment1().getStartPoint());
		assertEquals(new Vector3(0, -5, 0), leg1.getSegment2().getStartPoint());
		assertEquals(new Vector3(0, -12, 0), leg1.getEndSegment().getStartPoint());
		assertEquals(new Vector3(0, -25, 0), leg1.getEndSegment().getEndPoint());
		leg1.setAngles(-Math.PI / 4, Math.PI / 4, -Math.PI / 4);
		Assert.assertEquals(-Math.PI / 4, leg1.getK1().getAngle(), 0.01);
		Assert.assertEquals(Math.PI / 4, leg1.getK2().getAngle(), 0.01);
		Assert.assertEquals(-Math.PI / 4, leg1.getK3().getAngle(), 0.01);

		assertEquals(new Vector3(0, 0, 0), leg1.getStartSegment().getStartPoint());
		assertEquals(new Vector3(0, 0, 0), leg1.getSegment1().getStartPoint());
		assertEquals(new Vector3(3.535, -3.535, 0.0), leg1.getSegment2().getStartPoint());
		assertEquals(new Vector3(7.0355, -7.0355, -4.949), leg1.getEndSegment().getStartPoint());
		assertEquals(new Vector3(16.227, -16.227, -4.949), leg1.getEndSegment().getEndPoint());
	}

	@Test
	public void testSetPosition() {
		Leg leg = new Leg(1, new Vector3(0, 0, 0), 0.0);
		Vector3 endpoint = new Vector3(12.0, 0.0, -13.0);
		leg.setEndpoint(endpoint);
		Vector3 calculated = leg.getEndpoint();
		assertEquals(endpoint, calculated);
	}

	@Test
	public void testSetPosition2() {
		Leg leg = new Leg(1, new Vector3(0, 0, 0), Math.PI / 4);
		Vector3 endpoint = new Vector3(12.0, 0.0, -13.0);
		leg.setEndpoint(endpoint);
		Vector3 calculated = leg.getEndpoint();
		assertEquals(endpoint, calculated);
	}

	@Test
	public void testSetPosition3() {
		Leg leg = new Leg(1, new Vector3(12, -6, 0), Math.PI / 4);
		Vector3 endpoint = new Vector3(24.0, 0.0, -12.0);
		leg.setEndpoint(endpoint);
		Vector3 calculated = leg.getEndpoint();
		assertEquals(endpoint, calculated);
	}

	@Test
	public void testSetPosition4() {
		Leg leg = new Leg(1, new Vector3(12, 6, 0), -Math.PI / 4);
		Vector3 endpoint = new Vector3(24.0, 0.0, -12.0);
		leg.setEndpoint(endpoint);
		Vector3 calculated = leg.getEndpoint();
		assertEquals(endpoint, calculated);
	}

	@Test
	public void testSetPosition5() {
		Leg leg = new Leg(1, new Vector3(0, 9, 0), -Math.PI / 2);
		Vector3 endpoint = new Vector3(12.0, 12.0, -12.0);
		leg.setEndpoint(endpoint);
		Vector3 calculated = leg.getEndpoint();
		assertEquals(endpoint, calculated);
	}

	private void assertEquals(Vector3 v1, Vector3 v2) {
		Assert.assertEquals("X", v1.getX(), v2.getX(), 0.2);
		Assert.assertEquals("Y", v1.getY(), v2.getY(), 0.2);
		Assert.assertEquals("Z", v1.getZ(), v2.getZ(), 0.2);
	}
}