package ch.sharpsoft.hexapod;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.sharpsoft.hexapod.util.Quaternion;
import ch.sharpsoft.hexapod.util.Vector3;

public class QuternionTest {

	@Test
	public void testVector1() throws Exception {
		Quaternion quat = Quaternion.fromEuler(0.0, 0.0, Math.PI / 2);
		Vector3 v = new Vector3(0.0, 1.0, 0.0);
		Vector3 m = quat.multiply(v);
		assertEquals(1.0, m.getX(), 0.01f);
	}

	@Test
	public void testVector2() throws Exception {
		Quaternion quat = Quaternion.fromEuler(0.0, 0.0, Math.PI / 2);
		Vector3 v = new Vector3(1.0, 0.0, 0.0);
		Vector3 m = quat.multiply(v);
		assertEquals(-1.0, m.getY(), 0.01f);
	}

	@Test
	public void testVector3() throws Exception {
		Quaternion quat = Quaternion.fromEuler(0.0, 0.0, Math.PI / 2);
		Vector3 v = new Vector3(0.0, 0.0, 1.0);
		Vector3 m = quat.multiply(v);
		assertEquals(1.0, m.getZ(), 0.01f);
	}

	@Test
	public void testVector4() throws Exception {
		Quaternion quat = Quaternion.fromEuler(0.0, 0.0, -Math.PI / 2);
		Vector3 v = new Vector3(1.0, 0.0, 0.0);
		Vector3 m = quat.multiply(v);
		assertEquals(1.0, m.getY(), 0.01f);
	}

	@Test
	public void testVector5() throws Exception {
		Quaternion quat = Quaternion.fromEuler(0.0, Math.PI / 2, 0.0);
		Vector3 v = new Vector3(1.0, 0.0, 0.0);
		Vector3 m = quat.multiply(v);
		assertEquals(-1.0, m.getZ(), 0.01f);
	}

	@Test
	public void testVector6() throws Exception {
		Quaternion quat = Quaternion.fromEuler(Math.PI / 2, 0.0, 0.0);
		Vector3 v = new Vector3(1.0, 0.0, 0.0);
		Vector3 m = quat.multiply(v);
		assertEquals(1.0, m.getX(), 0.01f);
	}

	@Test
	public void testVector7() throws Exception {
		Quaternion quat = Quaternion.fromEuler(Math.PI / 2, 0.0, 0.0);
		Vector3 v = new Vector3(0.0, 1.0, 0.0);
		Vector3 m = quat.multiply(v);
		assertEquals(-1.0, m.getZ(), 0.01f);
	}

	@Test
	public void testVector8() throws Exception {
		Quaternion quat = Quaternion.fromEuler(-Math.PI / 2, 0.0, 0.0);
		Vector3 v = new Vector3(0.0, 1.0, 0.0);
		Vector3 m = quat.multiply(v);
		assertEquals(1.0, m.getZ(), 0.01f);
	}

	@Test
	public void testVector9() throws Exception {
		Quaternion quat = Quaternion.fromEuler(0.0, -Math.PI / 4, 0.0);
		Vector3 v = new Vector3(5.0, 0.0, 0.0);
		Vector3 m = quat.multiply(v);
		assertEquals(3.5355, m.getZ(), 0.01f);
		assertEquals(3.5355, m.getX(), 0.01f);
	}

	@Test
	public void testEulerRoll() throws Exception {
		for (int i = -1000; i < 1000; i++) {
			if (i == 0 || i == 1 || i == -1) {
				continue;
			}
			Quaternion quat = Quaternion.fromEuler(Math.PI / i, 0.0, 0.0);
			assertEquals(Math.PI / i, quat.toAngles()[0], 0.01f);
		}
	}

	@Test
	public void testEulerPitch() throws Exception {
		for (int i = -1000; i < 1000; i++) {
			if (i == 0 || i == 1 || i == -1) {
				continue;
			}
			Quaternion quat = Quaternion.fromEuler(0.0, Math.PI / i, 0.0);
			assertEquals(Math.PI / i, quat.toAngles()[1], 0.01f);
		}
	}

	@Test
	public void testEulerYaw() throws Exception {
		for (int i = -1000; i < 1000; i++) {
			if (i == 0 || i == 1 || i == -1) {
				continue;
			}
			Quaternion quat = Quaternion.fromEuler(0.0, 0.0, Math.PI / i);
			assertEquals(Math.PI / i, quat.toAngles()[2], 0.01f);
		}
	}

	@Test
	public void testDiffRoll() throws Exception {
		Quaternion quat1 = Quaternion.fromEuler(0.57, 0.0, 0.0);
		Quaternion quat2 = Quaternion.fromEuler(0.923, 0.0, 0.0);
		Quaternion diff = quat1.mulInverse(quat2);
		double diffAngle = diff.toAngles()[0];
		assertEquals((0.57) - (0.923), diffAngle, 0.01f);
	}

	@Test
	public void testDiffPitch() throws Exception {
		Quaternion quat1 = Quaternion.fromEuler(0.0, 0.57, 0.0);
		Quaternion quat2 = Quaternion.fromEuler(0.0, 0.923, 0.0);
		Quaternion diff = quat1.mulInverse(quat2);
		double diffAngle = diff.toAngles()[1];
		assertEquals((0.57) - (0.923), diffAngle, 0.01f);
	}

	@Test
	public void testDiffYaw() throws Exception {
		Quaternion quat1 = Quaternion.fromEuler(0.0, 0.0, 0.57);
		Quaternion quat2 = Quaternion.fromEuler(0.0, 0.0, 0.923);
		Quaternion diff = quat1.mulInverse(quat2);
		double diffAngle = diff.toAngles()[2];
		assertEquals((0.57) - (0.923), diffAngle, 0.01f);
	}

	@Test
	public void testMulInverse() throws Exception {
		Quaternion o1 = Quaternion.fromEuler(0.1, 0.0, 0.0);
		Quaternion o2 = Quaternion.fromEuler(0.0, 0.2, 0.0);
		Quaternion o3 = Quaternion.fromEuler(0.0, 0.0, 0.3);

		assertEquals(0.1, o1.toAngles()[0], 0.001);
		assertEquals(0.2, o2.toAngles()[1], 0.001);
		assertEquals(0.3, o3.toAngles()[2], 0.001);

		Quaternion o12 = o1.multiply(o2);
		Quaternion o13 = o1.multiply(o3);
		Quaternion o23 = o2.multiply(o3);
		Quaternion o32 = o3.multiply(o2);

		o1 = o12.mulInverse(o2);
		o2 = o23.mulInverse(o3);
		o3 = o32.mulInverse(o2);

		assertEquals(0.1, o1.toAngles()[0], 0.001);
		assertEquals(0.2, o2.toAngles()[1], 0.001);
		assertEquals(0.3, o3.toAngles()[2], 0.001);
	}

	@Test
	public void testMulInverse2() throws Exception {
		Quaternion o1 = Quaternion.fromEuler(0.1, 0.0, 0.0);
		Quaternion o2 = Quaternion.fromEuler(0.0, 0.2, 0.0);
		Quaternion o3 = Quaternion.fromEuler(0.0, 0.0, 0.3);
		Quaternion o4 = Quaternion.fromEuler(0.0, 1.1, 0.0);

		Quaternion o12 = o1.multiply(o2);
		Quaternion o123 = o12.multiply(o3);
		Quaternion o1234 = o123.multiply(o4);

		o123 = o1234.mulInverse(o4);
		o12 = o123.mulInverse(o3);
		o1 = o12.mulInverse(o2);

		assertEquals(0.1, o1.toAngles()[0], 0.001);
	}

	@Test
	public void testMulInverse3() throws Exception {
		Quaternion o1 = Quaternion.fromEuler(0.1, 0.0, 0.0);
		Quaternion o2 = Quaternion.fromEuler(0.0, 0.2, 0.0);
		Quaternion o12 = o1.multiply(o2);
		o2 = o12.mulInverse(o1);
		assertEquals(0.2, o2.toAngles()[1], 0.001);
	}

	@Test
	public void testMulInverse4() throws Exception {
		Quaternion o1 = Quaternion.fromEuler(0.1, 0.0, 0.0);
		Quaternion o2 = Quaternion.fromEuler(0.0, 0.2, 0.0);
		Quaternion o3 = Quaternion.fromEuler(0.0, 0.0, 0.3);

		Quaternion o12 = o1.multiply(o2);
		Quaternion o123 = o12.multiply(o3);

		o3 = o123.mulInverse(o12);

		assertEquals(0.3, o3.toAngles()[2], 0.01);
	}

	@Test
	public void testGetDirectionX1() throws Exception {
		Quaternion o1 = Quaternion.fromEuler(0.0, 0.0, 0.0);
		Vector3 result = o1.getDirectionX(10);
		assertEquals(10.0, result.getX(), 0.01);
	}

	@Test
	public void testGetDirectionX2() throws Exception {
		Quaternion o1 = Quaternion.fromEuler(0.0, 0.0, Math.PI / 2);
		Vector3 result = o1.getDirectionX(10);
		assertEquals(-10.0, result.getY(), 0.01);
	}

	@Test
	public void testGetDirectionX3() throws Exception {
		Quaternion o1 = Quaternion.fromEuler(0.0, 0.0, -Math.PI / 2);
		Vector3 result = o1.getDirectionX(10);
		assertEquals(10.0, result.getY(), 0.01);
	}

	@Test
	public void testGetDirectionX4() throws Exception {
		Quaternion o1 = Quaternion.fromEuler(0.0, Math.PI / 2, 0.0);
		Vector3 result = o1.getDirectionX(10);
		assertEquals(-10.0, result.getZ(), 0.01);
	}
}