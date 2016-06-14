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

	public Leg(int id, Vector3 startPoint, Quaternion startOrientation) {
		this.id = id;
		this.startOrientation = startOrientation;
		this.startPoint = startPoint;
		startSegment = new LegSegment(startPoint, 0.0, startOrientation);
		segment1 = new LegSegment(startPoint.add(startSegment.getVector()), 5, startOrientation);
		segment2 = new LegSegment(segment1.getStartPoint().add(segment1.getVector()), 7, startOrientation);
		endSegment = new LegSegment(segment2.getStartPoint().add(segment2.getVector()), 13, startOrientation);
		k1 = new Knee(startSegment, segment1, true);
		k2 = new Knee(segment1, segment2, false);
		k3 = new Knee(segment2, endSegment, false);
	}

	public void setAngles(double k1, double k2, double k3) {
		this.k1.setAngle(k1);
		this.k2.setAngle(k2);
		this.k3.setAngle(k3);
		Quaternion o1 = startOrientation.multiply(Quaternion.fromEuler(0.0, 0.0, k1));
		Quaternion o2 = o1.multiply(Quaternion.fromEuler(0.0, k2, 0.0));
		Quaternion o3 = o2.multiply(Quaternion.fromEuler(0.0, k3, 0.0));
		segment1.setOrientation(o1);
		segment2.setOrientation(o2);
		endSegment.setOrientation(o3);
		segment2.setStartPoint(segment1.getEndPoint());
		endSegment.setStartPoint(segment2.getEndPoint());
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