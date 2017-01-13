package ch.sharpsoft.hexapod;

public class Knee {
	private double angle = 0;
	private final LegSegment from;
	private final LegSegment to;
	private final boolean yaw;

	public Knee(LegSegment from, LegSegment to, boolean yaw) {
		this.from = from;
		this.to = to;
		this.yaw = yaw;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public LegSegment getFrom() {
		return from;
	}

	public LegSegment getTo() {
		return to;
	}

	public boolean isYaw() {
		return yaw;
	}

	@Override
	public String toString() {
		return "Knee [angle=" + angle + ", from=" + from + ", to=" + to + ", yaw=" + yaw + "]";
	}
}