package ch.sharpsoft.hexapod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WalkSafe {
	private static final Vector3 UP = new Vector3(0.0, 0.0, 1);
	private final Hexapod hp;
	private Vector3 direction;
	private final Set<Leg> grp1 = new HashSet<>();
	private final Set<Leg> grp2 = new HashSet<>();
	private final Map<Leg, Vector3> initialPosition = new HashMap<>();
	private int state = 0;

	public WalkSafe(final Hexapod hp) {
		this.hp = hp;
		init();
	}

	public void init() {
		hp.getLegs().forEach(l -> l.setAngles(0.0, 0.4, 1.3));
		hp.getLegs().forEach(l -> initialPosition.put(l, l.getEndpoint()));
		for (Leg l : hp.getLegs()) {
			if (l.getId() % 2 == 0) {
				grp1.add(l);
			} else {
				grp2.add(l);
			}
		}
	}

	public void setDirection(final Vector3 direction) {
		if (direction != null) {
			this.direction = direction.normalize().multiply(2.0);
		} else {
			this.direction = null;
		}
	}

	public void doNextAction() {
		if (direction != null) {
			moveInDirection();
		}
	}

	private void moveInDirection() {
		state = (1 + state) % 6;
		switch (state) {
		case 0:
			up(grp1);
			forward(grp1);
			forward(grp2);
			break;
		case 1:
			back(grp1);
			forward(grp2);
			break;
		case 2:
			down(grp1);
			forward(grp1);
			forward(grp2);
			break;
		case 3:
			up(grp2);
			forward(grp1);
			forward(grp2);
			break;
		case 4:
			back(grp2);
			forward(grp1);
			break;
		case 5:
			down(grp2);
			forward(grp1);
			forward(grp2);
			break;
		default:
			break;
		}
	}

	private void up(Set<Leg> grp) {
		grp.forEach(leg -> leg.setEndpoint(leg.getEndpoint().add(UP)));
	}

	private void down(Set<Leg> grp) {
		grp.forEach(leg -> leg.setEndpoint(leg.getEndpoint().substract(UP)));
	}

	private void back(Set<Leg> grp) {
		for (Leg leg : grp) {
			Vector3 initial = initialPosition.get(leg);
			leg.setEndpoint(initial.substract(direction.multiply(2)).add(UP));
		}
	}

	private void forward(Set<Leg> grp) {
		grp.forEach(leg -> leg.setEndpoint(leg.getEndpoint().add(direction)));
	}
}