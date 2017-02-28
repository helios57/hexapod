package ch.sharpsoft.hexapod.walk.independent;

import ch.sharpsoft.hexapod.Leg;
import ch.sharpsoft.hexapod.util.Vector3;

public class WalkLegState {

	private enum State {
		Forward, Up, Down, Backward
	}

	private final Leg leg;
	private Vector3 initialPosition;
	private State state;

	public WalkLegState(Leg leg) {
		this.leg = leg;
		setState(State.Forward);
	}

	public Vector3 getInitialPosition() {
		return initialPosition;
	}

	public void setInitialPosition(Vector3 initialPosition) {
		this.initialPosition = initialPosition;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Leg getLeg() {
		return leg;
	}
}
