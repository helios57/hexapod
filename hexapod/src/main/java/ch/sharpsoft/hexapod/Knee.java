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

	public void calculateAngle() {
		double[] angles = to.getOrientation().diff(from.getOrientation()).toAngles();
		if (yaw) {
			angle = angles[2];
			if (Math.abs(angles[0]) > 0.001f || Math.abs(angles[1]) > 0.001f) {
				throw new IllegalStateException();
			}
		} else {
			angle = angles[1];
			if (Math.abs(angles[0]) > 0.001f || Math.abs(angles[2]) > 0.001f) {
				throw new IllegalStateException();
			}
		}
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