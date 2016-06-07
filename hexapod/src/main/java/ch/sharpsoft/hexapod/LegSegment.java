package ch.sharpsoft.hexapod;


public class LegSegment {

	private Vector3 startPoint;
	private Vector3 vector;

	public LegSegment(Vector3 startPoint, Vector3 vector) {
		this.setStartPoint(startPoint);
		this.setVector(vector);
	}

	public Vector3 getVector() {
		return vector;
	}

	public void setVector(Vector3 vector) {
		this.vector = vector;
	}

	public Vector3 getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Vector3 startPoint) {
		this.startPoint = startPoint;
	}

	@Override
	public String toString() {
		return "LegSegment [startPoint=" + startPoint + ", vector=" + vector + "]";
	}
}