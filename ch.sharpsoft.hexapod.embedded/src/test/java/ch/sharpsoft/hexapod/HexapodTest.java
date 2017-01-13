package ch.sharpsoft.hexapod;

import static org.junit.Assert.*;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import ch.sharpsoft.hexapod.util.Vector3;

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

	@Test
	public void testSetEndpointZ() throws Exception {
		Hexapod hp = new Hexapod();
		hp.getLegs().forEach(l -> l.setAngles(0.0, 0.7, 0.5));
		List<Vector3> endpoints = hp.getEndpoints();
		endpoints.forEach(System.out::println);
		List<Vector3> newEndpoints = endpoints.stream().map(e -> //
		//
		new Vector3(e.getX(), e.getY(), e.getZ() + 1.0f)//
		//
		).collect(Collectors.toList());
		hp.setEndpoints(newEndpoints);
		endpoints = hp.getEndpoints();
		System.out.println();
		endpoints.forEach(System.out::println);
	}

	@Test
	public void testSetEndpointY() throws Exception {
		Hexapod hp = new Hexapod();
		hp.getLegs().forEach(l -> l.setAngles(0.0, 0.7, 0.5));
		List<Vector3> endpoints = hp.getEndpoints();
		endpoints.forEach(System.out::println);
		List<Vector3> newEndpoints = endpoints.stream().map(e -> //
		//
		new Vector3(e.getX(), e.getY() + 1f, e.getZ())//
		//
		).collect(Collectors.toList());
		hp.setEndpoints(newEndpoints);
		endpoints = hp.getEndpoints();
		System.out.println();
		endpoints.forEach(System.out::println);
	}

	@Test
	public void testSetEndpointX() throws Exception {
		Hexapod hp = new Hexapod();
		hp.getLegs().forEach(l -> l.setAngles(0.0, 0.7, 0.5));
		List<Vector3> endpoints = hp.getEndpoints();
		endpoints.forEach(System.out::println);
		List<Vector3> newEndpoints = endpoints.stream().map(e -> //
		//
		new Vector3(e.getX() + 1f, e.getY(), e.getZ())//
		//
		).collect(Collectors.toList());
		hp.setEndpoints(newEndpoints);
		endpoints = hp.getEndpoints();
		System.out.println();
		endpoints.forEach(System.out::println);
	}

	private void assertEquals(Vector3 v1, Vector3 v2) {
		Assert.assertEquals("X", v1.getX(), v2.getX(), 0.03);
		Assert.assertEquals("Y", v1.getY(), v2.getY(), 0.03);
		Assert.assertEquals("Z", v1.getZ(), v2.getZ(), 0.03);
	}
}