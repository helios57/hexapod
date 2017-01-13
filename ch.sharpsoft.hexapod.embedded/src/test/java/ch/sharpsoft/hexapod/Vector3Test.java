package ch.sharpsoft.hexapod;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.sharpsoft.hexapod.util.Vector3;

public class Vector3Test {

	@Test
	public void testVectorYaw1() throws Exception {
		Vector3 result = Vector3.byYaw(10, 0.0);
		assertEquals(10.0, result.getX(), 0.01f);
	}

	@Test
	public void testVectorYaw2() throws Exception {
		Vector3 result = Vector3.byYaw(10, Math.PI / 2);
		assertEquals(-10.0, result.getY(), 0.01f);
	}

	@Test
	public void testVectorYaw3() throws Exception {
		Vector3 result = Vector3.byYaw(10, Math.PI / 4);
		assertEquals(7.07, result.getX(), 0.01f);
		assertEquals(-7.07, result.getY(), 0.01f);
	}

	@Test
	public void testVectorPitch1() throws Exception {
		Vector3 result = Vector3.byPitch(10, 0.0);
		assertEquals(10.0, result.getX(), 0.01f);
	}

	@Test
	public void testVectorPitch2() throws Exception {
		Vector3 result = Vector3.byPitch(10, Math.PI / 2);
		assertEquals(-10.0, result.getZ(), 0.01f);
	}

	@Test
	public void testVectorPitch3() throws Exception {
		Vector3 result = Vector3.byPitch(10, Math.PI / 4);
		assertEquals(7.07, result.getX(), 0.01f);
		assertEquals(-7.07, result.getZ(), 0.01f);
	}

}