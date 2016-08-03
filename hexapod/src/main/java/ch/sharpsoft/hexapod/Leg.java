package ch.sharpsoft.hexapod;

import java.util.Arrays;

/**
 * <code>
 *    S
 *  --O
 *     \
 *      \ 5
 *       \
 *        \
 *         O
 *         |
 *         |
 *         | 7
 *         |
 *         O
 *         |
 *         |
 *         | 13
 *         |
 *         |
 *         E       
 * </code>
 */
public class Leg {
	private final int id;
	private final Vector3 startPoint;
	private final Quaternion startOrientation;
	private final LegSegment startSegment;
	private final LegSegment segment1;
	private final LegSegment segment2;
	private final LegSegment endSegment;
	private final Knee k1;
	private final Knee k2;
	private final Knee k3;
	private final double startYaw;

	public Leg(final int id, final Vector3 startPoint, final double startYaw) {
		this.id = id;
		this.startYaw = startYaw;
		this.startOrientation = Quaternion.fromEuler(0.0, 0.0, startYaw);
		this.startPoint = startPoint;
		startSegment = new LegSegment(startPoint, 0.0, startOrientation);
		segment1 = new LegSegment(startPoint.add(startSegment.getVector()), 5, startOrientation);
		segment2 = new LegSegment(segment1.getStartPoint().add(segment1.getVector()), 7, startOrientation);
		endSegment = new LegSegment(segment2.getStartPoint().add(segment2.getVector()), 13, startOrientation);
		k1 = new Knee(startSegment, segment1, true);
		k2 = new Knee(segment1, segment2, false);
		k3 = new Knee(segment2, endSegment, false);
	}

	public void setAngles(final double k1, final double k2, final double k3) {
		this.k1.setAngle(k1);
		this.k2.setAngle(k2);
		this.k3.setAngle(k3);
		final Quaternion o1 = startOrientation.multiply(Quaternion.fromEuler(0.0, 0.0, k1));
		final Quaternion o2 = o1.multiply(Quaternion.fromEuler(0.0, k2, 0.0));
		final Quaternion o3 = o2.multiply(Quaternion.fromEuler(0.0, k3, 0.0));
		segment1.setOrientation(o1);
		segment2.setOrientation(o2);
		endSegment.setOrientation(o3);
		segment2.setStartPoint(segment1.getEndPoint());
		endSegment.setStartPoint(segment2.getEndPoint());
	}

	public double[] getAngles() {
		return new double[] { k1.getAngle(), k2.getAngle(), k3.getAngle() };
	}

	public void setEndpoint(final Vector3 vector3) {
		final Vector3 legframe = vector3.substract(startPoint);
		final double alpha = atan2(-legframe.getY(), legframe.getX()) - startYaw;
		k1.setAngle(alpha);
		final Quaternion o1 = startOrientation.multiply(Quaternion.fromEuler(0.0, 0.0, alpha));
		segment1.setOrientation(o1);
		final Vector3 rest = o1.conjugate().multiply(vector3.substract(segment1.getEndPoint()));
		if (rest.norm() > segment2.getLength() + endSegment.getLength()) {
			throw new IllegalArgumentException("Legs too short for this point " + vector3);
		}
		final double r0 = getSegment2().getLength();
		final double r1 = getEndSegment().getLength();
		final double phi = atan2(Math.sqrt(1 - pow2((pow2(rest.getX()) + pow2(rest.getZ()) - pow2(r0) - pow2(r1)) / (2 * r0 * r1))), (pow2(rest.getX()) + pow2(rest.getZ()) - pow2(r0) - pow2(r1)) / (2 * r0 * r1));
		final double beta = atan2(-rest.getZ(), rest.getX()) - atan2(r1 * Math.sin(phi), r0 + r1 * Math.cos(phi));
		k2.setAngle(beta);
		final Quaternion o2 = o1.multiply(Quaternion.fromEuler(0.0, beta, 0.0));
		segment2.setOrientation(o2);
		segment2.setStartPoint(segment1.getEndPoint());
		k3.setAngle(phi);
		final Quaternion o3 = o2.multiply(Quaternion.fromEuler(0.0, phi, 0.0));
		endSegment.setOrientation(o3);
		endSegment.setStartPoint(segment2.getEndPoint());
	}

	private double pow2(double x) {
		return x * x;
	}

	private double atan2(double x, double y) {
		double angle = Math.atan2(x, y);
		if (angle < -Math.PI + 0.01) {
			return 0;
		}
		if (angle > Math.PI - 0.01) {
			return 0;
		}
		return angle;
	}

	public Vector3 getEndpoint() {
		return getEndSegment().getEndPoint();
	}

	public int getId() {
		return id;
	}

	public Vector3 getStartPoint() {
		return startPoint;
	}

	public Quaternion getStartOrientation() {
		return startOrientation;
	}

	public LegSegment getStartSegment() {
		return startSegment;
	}

	public LegSegment getSegment1() {
		return segment1;
	}

	public LegSegment getSegment2() {
		return segment2;
	}

	public LegSegment getEndSegment() {
		return endSegment;
	}

	public Knee getK1() {
		return k1;
	}

	public Knee getK2() {
		return k2;
	}

	public Knee getK3() {
		return k3;
	}

	@Override
	public String toString() {
		return "\nLeg [id=" + id + ", startPoint=" + startPoint + ", startOrientation=" + startOrientation + ", startSegment=" + startSegment + ", segment1=" + segment1 + ", segment2=" + segment2 + ", endSegment=" + endSegment + ", k1="
				+ k1 + ", k2=" + k2 + ", k3=" + k3 + "]";
	}
}