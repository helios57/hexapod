package ch.sharpsoft.hexapod;

public class LegSegment {

	private Vector3 startPoint;
	private Quaternion orientation;
	private final double length;

	public LegSegment(Vector3 startPoint, double length, Quaternion orientation) {
		this.startPoint = startPoint;
		this.length = length;
		this.orientation = orientation;
	}

	public Vector3 getVector() {
		return orientation.multiply(new Vector3(length, 0, 0));
	}

	public Vector3 getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Vector3 startPoint) {
		this.startPoint = startPoint;
	}

	public Vector3 getEndPoint() {
		return startPoint.add(getVector());
	}

	public Quaternion getOrientation() {
		return orientation;
	}

	public void setOrientation(Quaternion orientation) {
		this.orientation = orientation;
	}

	@Override
	public String toString() {
		return "LegSegment [startPoint=" + startPoint + ", length=" + length + ", orientation=" + orientation + "]";
	}
}