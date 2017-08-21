package ch.sharpsoft.hexapod.walk.independent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.sharpsoft.hexapod.Hexapod;
import ch.sharpsoft.hexapod.Leg;
import ch.sharpsoft.hexapod.util.Quaternion;
import ch.sharpsoft.hexapod.util.Vector3;
import ch.sharpsoft.hexapod.walk.Walk;
import ch.sharpsoft.hexapod.walk.WalkComplete;

public abstract class WalkSimple2 implements WalkComplete {
	private Vector3 legLift;
	private final Hexapod hp;
	private Vector3 direction;
	private Quaternion rotation;
	private final Set<Leg> up = new HashSet<>();
	private final Set<Leg> back = new HashSet<>();
	private final Map<Leg, Vector3> initialPosition = new HashMap<>();

	public WalkSimple2(final Hexapod hp) {
		this.hp = hp;
		init();
	}

	public void init() {
		legLift = new Vector3(0.0, 0.0, 5);
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

	public void setHeadingRelative(final Quaternion rotation) {
		if (rotation != null) {
			this.rotation = rotation.normalize();
		} else {
			this.rotation = null;
		}
	}

	@Override
	public void doNextAction() {
		if (direction != null) {
			moveInDirection();
		}
	}

	@Override
	public void setLegLift(double legLiftinCm) {
		legLift = new Vector3(0, 0, legLiftinCm);
	}

	@Override
	public void setWalkHeight(double heightInCm) {
		// TODO winkel berechnen
	}

	private void moveInDirection() {
		final List<Leg> legs = hp.getLegs();
		for (final Leg leg : legs) {
			final Vector3 endpoint = leg.getEndpoint();
			Vector3 newEndpoint;
			Vector3 initial = initialPosition.get(leg);

			if (isUpShouldGoBack(leg)) {
				back.add(leg);
				newEndpoint = initial.substract(direction.multiply(2)).add(legLift);
			} else if (isGoingBack(leg)) {
				up.remove(leg);
				back.remove(leg);
				newEndpoint = endpoint.substract(legLift);
			} else {
				newEndpoint = endpoint.add(direction);
				if (newEndpoint.substract(initial).norm() >= 5) {
					if (canGoUp(leg.getId() % 6)) {
						up.add(leg);
						newEndpoint = endpoint.add(legLift);
					}
				}
			}
			leg.setEndpoint(newEndpoint);
		}
	}

	private boolean isGoingBack(final Leg leg) {
		return back.contains(leg);
	}

	private boolean isUpShouldGoBack(final Leg leg) {
		return up.contains(leg) && !isGoingBack(leg);
	}

	private boolean canGoUp(int id) {
		return !up.stream().anyMatch(u -> ((u.getId() - 1 + 6) % 6) == id || ((u.getId() + 1) % 6) == id);
	}

	@Override
	public void setActionSize(double cm) {
		// TODO Auto-generated method stub

	}
}