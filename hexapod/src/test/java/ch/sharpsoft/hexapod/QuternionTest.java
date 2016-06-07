package ch.sharpsoft.hexapod;

import org.junit.Assert;
import org.junit.Test;

public class QuternionTest {

	@Test
	public void testVector1() throws Exception {
		Quaternion quat = Quaternion.fromEuler(0.0, 0.0, Math.PI / 2);
		Vector3 v = new Vector3(0.0, 1.0, 0.0);
		Vector3 m = quat.multiply(v);
		Assert.assertEquals(1.0, m.getX(), 0.01f);
	}

	@Test
	public void testVector2() throws Exception {
		Quaternion quat = Quaternion.fromEuler(0.0, 0.0, Math.PI / 2);
		Vector3 v = new Vector3(1.0, 0.0, 0.0);
		Vector3 m = quat.multiply(v);
		Assert.assertEquals(-1.0, m.getY(), 0.01f);
	}

	@Test
	public void testVector3() throws Exception {
		Quaternion quat = Quaternion.fromEuler(0.0, 0.0, Math.PI / 2);
		Vector3 v = new Vector3(0.0, 0.0, 1.0);
		Vector3 m = quat.multiply(v);
		Assert.assertEquals(1.0, m.getZ(), 0.01f);
	}

	@Test
	public void testVector4() throws Exception {
		Quaternion quat = Quaternion.fromEuler(0.0, 0.0, -Math.PI / 2);
		Vector3 v = new Vector3(1.0, 0.0, 0.0);
		Vector3 m = quat.multiply(v);
		Assert.assertEquals(1.0, m.getY(), 0.01f);
	}

	@Test
	public void testVector5() throws Exception {
		Quaternion quat = Quaternion.fromEuler(0.0, Math.PI / 2, 0.0);
		Vector3 v = new Vector3(1.0, 0.0, 0.0);
		Vector3 m = quat.multiply(v);
		Assert.assertEquals(-1.0, m.getZ(), 0.01f);
	}

	@Test
	public void testVector6() throws Exception {
		Quaternion quat = Quaternion.fromEuler(Math.PI / 2, 0.0, 0.0);
		Vector3 v = new Vector3(1.0, 0.0, 0.0);
		Vector3 m = quat.multiply(v);
		Assert.assertEquals(1.0, m.getX(), 0.01f);
	}

	@Test
	public void testVector7() throws Exception {
		Quaternion quat = Quaternion.fromEuler(Math.PI / 2, 0.0, 0.0);
		Vector3 v = new Vector3(0.0, 1.0, 0.0);
		Vector3 m = quat.multiply(v);
		Assert.assertEquals(-1.0, m.getZ(), 0.01f);
	}

	@Test
	public void testVector8() throws Exception {
		Quaternion quat = Quaternion.fromEuler(-Math.PI / 2, 0.0, 0.0);
		Vector3 v = new Vector3(0.0, 1.0, 0.0);
		Vector3 m = quat.multiply(v);
		Assert.assertEquals(1.0, m.getZ(), 0.01f);
	}

	@Test
	public void testEulerRoll() throws Exception {
		for (int i = -1000; i < 1000; i++) {
			if (i == 0 || i == 1 || i == -1) {
				continue;
			}
			Quaternion quat = Quaternion.fromEuler(Math.PI / i, 0.0, 0.0);
			Assert.assertEquals(Math.PI / i, quat.toAngles()[0], 0.01f);
		}
	}

	@Test
	public void testEulerPitch() throws Exception {
		for (int i = -1000; i < 1000; i++) {
			if (i == 0 || i == 1 || i == -1) {
				continue;
			}
			Quaternion quat = Quaternion.fromEuler(0.0, Math.PI / i, 0.0);
			Assert.assertEquals(Math.PI / i, quat.toAngles()[1], 0.01f);
		}
	}

	@Test
	public void testEulerYaw() throws Exception {
		for (int i = -1000; i < 1000; i++) {
			if (i == 0 || i == 1 || i == -1) {
				continue;
			}
			Quaternion quat = Quaternion.fromEuler(0.0, 0.0, Math.PI / i);
			Assert.assertEquals(Math.PI / i, quat.toAngles()[2], 0.01f);
		}
	}
}