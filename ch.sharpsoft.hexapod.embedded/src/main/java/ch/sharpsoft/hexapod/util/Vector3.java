package ch.sharpsoft.hexapod.util;

public class Vector3 {
	private final double x, y, z;

	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static Vector3 byYaw(double length, double yaw) {
		double px = length * Math.cos(yaw);
		double py = -length * Math.sin(yaw);
		return new Vector3(px, py, 0);
	}

	public static Vector3 byPitch(double length, double pitch) {
		double px = length * Math.cos(pitch);
		double pz = -length * Math.sin(pitch);
		return new Vector3(px, 0, pz);
	}

	public double norm() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public double normSquared() {
		return x * x + y * y + z * z;
	}

	public Vector3 normalize() {
		final double norm = norm();
		return new Vector3((double) (x / norm), (double) (y / norm), (double) (z / norm));
	}

	public Vector3 substract(Vector3 minus) {
		return new Vector3(this.x - minus.x, this.y - minus.y, this.z - minus.z);
	}

	public Vector3 add(Vector3 plus) {
		return new Vector3(this.x + plus.x, this.y + plus.y, this.z + plus.z);
	}

	public Vector3 multiply(double a) {
		return new Vector3(this.x * a, this.y * a, this.z * a);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	@Override
	public String toString() {
		return "V [x=" + String.format("%.4f", x) + ", y=" + String.format("%.4f", y) + ", z=" + String.format("%.4f", z) + ", l=" + String.format("%.4f", norm()) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector3 other = (Vector3) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}
}