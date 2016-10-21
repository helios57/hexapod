package ch.sharpsoft.hexapod;

import static java.lang.Math.PI;
import java.util.ArrayList;
import java.util.List;

/**
 * <code>
 * 
 *                              +x
 *                              0°
 *      45° = -Pi/4  (12,6)L6--------L1(12,-6)    45° = Pi/4
 *                         |         |
 *                         |         |
 *                         |         |
 *                         |         |
 *                        |           |
 * +y-90° = -Pi/2 (0,9)L5      18       L2(0,-9)    90° = Pi/2 -y
 *                        |           |
 *                         |         |
 *                         |         |
 *                         |         |
 *                         |    12   |
 *   -135° = -3Pi/4 (-12,6)L4--------L3(-12,-6)    135° = 3Pi/4
 *                             Pi°
 *                             -x
 *                        l=24
 * </code>
 */
public class Hexapod {

	private final static double MASS = 2000.0;
	private final List<Leg> legs = new ArrayList<>();
	private final Vector3 position;
	private final Quaternion orientation;

	public Hexapod() {
		orientation = Quaternion.fromEuler(0.0, 0.0, 0.0);
		legs.add(new Leg(1, new Vector3(12, -6, 0), PI / 4.0));
		legs.add(new Leg(2, new Vector3(0, -9, 0), PI / 2.0));
		legs.add(new Leg(3, new Vector3(-12, -6, 0), (3 * PI) / 4.0));
		legs.add(new Leg(4, new Vector3(-12, 6, 0), (-3 * PI) / 4.0));
		legs.add(new Leg(5, new Vector3(0, 9, 0), -PI / 2));
		legs.add(new Leg(6, new Vector3(12, 6, 0), -PI / 4));
		position = new Vector3(0, 0, 0);
		for (final Leg leg : legs) {
			leg.setAngles(0.0, 0.0, 0.0);
		}
	}

	public List<Leg> getLegs() {
		return legs;
	}

	public List<Vector3> getEndpoints() {
		List<Vector3> result = new ArrayList<>();
		legs.forEach(l -> result.add(l.getEndpoint()));
		return result;
	}

	public List<Vector3> setEndpoints(List<Vector3> endpoints) {
		for (int i = 0; i < endpoints.size(); i++) {
			Vector3 vector3 = endpoints.get(i);
			legs.get(i).setEndpoint(vector3);
		}
		return getEndpoints();
	}

	public Vector3 getPosition() {
		return position;
	}

	public Quaternion getOrientation() {
		return orientation;
	}

	@Override
	public String toString() {
		return "Hexapod [legs=" + legs + ", position=" + position + ", orientation=" + orientation + "]";
	}
}