package ch.sharpsoft.hexapod;

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
		startSegment = new LegSegment(startPoint, startOrientation.multiply(new Vector3(0.1, 0.0, 0.0)));
		segment1 = new LegSegment(startPoint.add(startSegment.getVector()), startOrientation.multiply(new Vector3(5, 0.0, 0.0)));
		segment2 = new LegSegment(segment1.getStartPoint().add(segment1.getVector()), startOrientation.multiply(new Vector3(7, 0.0, 0.0)));
		endSegment = new LegSegment(segment2.getStartPoint().add(segment2.getVector()), startOrientation.multiply(new Vector3(13, 0.0, 0.0)));
		k1 = new Knee(startSegment, segment1, true);
		k2 = new Knee(segment1, segment2, false);
		k3 = new Knee(segment2, endSegment, false);
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