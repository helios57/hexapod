package ch.sharpsoft.hexapod.simulation;

import static org.ode4j.ode.DRotation.dQFromAxisAndAngle;

import org.ode4j.math.DQuaternion;
import org.ode4j.ode.DBody;
import org.ode4j.ode.DMass;
import org.ode4j.ode.DWorld;
import org.ode4j.ode.OdeHelper;

public class OdeTest {
	public static void main(String[] args) {
		DWorld world = OdeHelper.createWorld();
		world.setERP(0.2);
		world.setCFM(1e-6);
		world.setGravity(0, 0, -9.81);

		DMass m = OdeHelper.createMass();
		m.setMass(2.0);

		DBody mainBody = OdeHelper.createBody(world);
		mainBody.setMass(m);
		mainBody.setPosition(0.0, 0.0, 0.0);
		DQuaternion q = new DQuaternion();
//		dQFromAxisAndAngle(q, ax1x, ax1y, ax1z, a1);
		mainBody.setQuaternion(q);
	}
}
