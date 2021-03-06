package ch.sharpsoft.hexapod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.sharpsoft.hexapod.util.Quaternion;
import ch.sharpsoft.hexapod.util.Vector3;

public class WalkSimple {
	private static final Vector3 UP = new Vector3(0.0, 0.0, 5);
	private final Hexapod hp;
	private Vector3 direction;
	private Quaternion rotation;
	private final Set<Leg> up = new HashSet<>();
	private final Set<Leg> back = new HashSet<>();
	private final Map<Leg, Vector3> initialPosition = new HashMap<>();

	public WalkSimple(final Hexapod hp) {
		this.hp = hp;
		init();
	}

	public void init() {
		hp.getLegs().forEach(l -> l.setAngles(0.0, 0.4, 1.3));
		hp.getLegs().forEach(l -> initialPosition.put(l, l.getEndpoint()));
	}

	public void setDirection(final Vector3 direction) {
		if (direction != null) {
			this.direction = direction.normalize();
		} else {
			this.direction = null;
		}
	}

	public void setRotation(final Quaternion rotation) {
		if (rotation != null) {
			this.rotation = rotation.normalize();
		} else {
			this.rotation = null;
		}
	}

	public void doNextAction() {
		if (direction != null) {
			moveInDirection();
		}
	}

	private void moveInDirection() {
		final List<Leg> legs = hp.getLegs();
		for (final Leg leg : legs) {
			final Vector3 endpoint = leg.getEndpoint();
			Vector3 newEndpoint;
			Vector3 initial = initialPosition.get(leg);
			if (isUpShouldGoBack(leg)) {
				back.add(leg);
				newEndpoint = initial.substract(direction.multiply(2)).add(UP);
			} else if (isBackCanGoDown(leg)) {
				up.remove(leg);
				back.remove(leg);
				newEndpoint = endpoint.substract(UP);
			} else {
				newEndpoint = endpoint.add(direction.multiply(0.1));
				if (newEndpoint.substract(initial).norm() >= 5) {
					if (canGoUp(leg.getId() % 6)) {
						up.add(leg);
						newEndpoint = endpoint.add(UP);
					}
				}
			}
			leg.setEndpoint(newEndpoint);
		}
	}

	private boolean isBackCanGoDown(final Leg leg) {
		return back.contains(leg);
	}

	private boolean isUpShouldGoBack(final Leg leg) {
		return up.contains(leg) && !isBackCanGoDown(leg);
	}

	private boolean canGoUp(int id) {
		return !up.stream().anyMatch(u -> ((u.getId() - 1 + 6) % 6) == id || ((u.getId() + 1) % 6) == id);
	}
}