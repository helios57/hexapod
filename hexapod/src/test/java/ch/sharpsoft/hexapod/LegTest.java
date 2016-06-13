package ch.sharpsoft.hexapod;

import org.junit.Assert;
import org.junit.Test;

public class LegTest {

	@Test
	public void testLegAngles() {
		Leg leg1 = new Leg(1, new Vector3(0, 0, 0), Quaternion.fromEuler(0.0, 0.0, 0.1234567));
		leg1.setAngles(Math.PI / 4, Math.PI / 6, -Math.PI / 8);
		leg1.getK1().calculateAngle();
		leg1.getK2().calculateAngle();
		leg1.getK3().calculateAngle();
		Assert.assertEquals(Math.PI / 4, leg1.getK1().getAngle(), 0.01);
		Assert.assertEquals(Math.PI / 6, leg1.getK2().getAngle(), 0.01);
		Assert.assertEquals(-Math.PI / 8, leg1.getK3().getAngle(), 0.01);
	}

	@Test
	public void testLegPositions() {
		Leg leg1 = new Leg(1, new Vector3(0, 0, 0), Quaternion.fromEuler(0.0, 0.0, Math.PI / 2));
		assertEquals(new Vector3(0, 0, 0), leg1.getStartSegment().getStartPoint());
		assertEquals(new Vector3(0, 0, 0), leg1.getSegment1().getStartPoint());
		assertEquals(new Vector3(0, -5, 0), leg1.getSegment2().getStartPoint());
		assertEquals(new Vector3(0, -12, 0), leg1.getEndSegment().getStartPoint());
		assertEquals(new Vector3(0, -25, 0), leg1.getEndSegment().getEndPoint());
		leg1.setAngles(Math.PI / 4, Math.PI / 4, -Math.PI / 2);
		assertEquals(new Vector3(0, 0, 0), leg1.getStartSegment().getStartPoint());
		assertEquals(new Vector3(0, 0, 0), leg1.getSegment1().getStartPoint());
		assertEquals(new Vector3(-3.535, -3.535, 0), leg1.getSegment2().getStartPoint());
		assertEquals(new Vector3(-7.0355, -8.485, 3.5), leg1.getEndSegment().getStartPoint());
		assertEquals(new Vector3(-13.535, -17.677, -3), leg1.getEndSegment().getEndPoint());
	}

	@Test
	public void testLegPositions2() {
		Leg leg1 = new Leg(1, new Vector3(0, 0, 0), Quaternion.fromEuler(0.0, 0.0, Math.PI / 2));
		assertEquals(new Vector3(0, 0, 0), leg1.getStartSegment().getStartPoint());
		assertEquals(new Vector3(0, 0, 0), leg1.getSegment1().getStartPoint());
		assertEquals(new Vector3(0, -5, 0), leg1.getSegment2().getStartPoint());
		assertEquals(new Vector3(0, -12, 0), leg1.getEndSegment().getStartPoint());
		assertEquals(new Vector3(0, -25, 0), leg1.getEndSegment().getEndPoint());
		leg1.setAngles(0.0f, Math.PI / 4, -Math.PI / 2);
		leg1.getK1().calculateAngle();
		leg1.getK2().calculateAngle();
		leg1.getK3().calculateAngle();
		Assert.assertEquals(0.0f, leg1.getK1().getAngle(), 0.01);
		Assert.assertEquals(Math.PI / 4, leg1.getK2().getAngle(), 0.01);
		Assert.assertEquals(-Math.PI / 2, leg1.getK3().getAngle(), 0.01);

		assertEquals(new Vector3(0, 0, 0), leg1.getStartSegment().getStartPoint());
		assertEquals(new Vector3(0, 0, 0), leg1.getSegment1().getStartPoint());
		assertEquals(new Vector3(0, -5, 0), leg1.getSegment2().getStartPoint());
		assertEquals(new Vector3(0, -12, 0), leg1.getEndSegment().getStartPoint());
		assertEquals(new Vector3(0, -25, 0), leg1.getEndSegment().getEndPoint());
	}

	private void assertEquals(Vector3 v1, Vector3 v2) {
		Assert.assertEquals("X", v1.getX(), v2.getX(), 0.001);
		Assert.assertEquals("Y", v1.getY(), v2.getY(), 0.001);
		Assert.assertEquals("Z", v1.getZ(), v2.getZ(), 0.001);
	}
}