package ch.sharpsoft.hexapod;

import edu.emory.mathcs.backport.java.util.Arrays;

public class Quaternion {

	private final double w, x, y, z;

	public Quaternion(double[] q) {
		this.w = q[0];
		this.x = q[1];
		this.y = q[2];
		this.z = q[3];
	}

	public Quaternion(double w, double x, double y, double z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double norm() {
		return Math.sqrt(w * w + x * x + y * y + z * z);
	}

	public Quaternion normalize() {
		final double norm = norm();
		return new Quaternion((double) (w / norm), (double) (x / norm), (double) (y / norm), (double) (z / norm));
	}

	public Quaternion conjugate() {
		return new Quaternion(w, -x, -y, -z);
	}

	public Quaternion plus(Quaternion b) {
		return new Quaternion(w + b.w, x + b.x, y + b.y, z + b.z);
	}

	public Quaternion diff(Quaternion b) {
		return multiply(b.conjugate());
	}

	// return a new Quaternion whose value is (this * b)
	public Quaternion multiply(Quaternion b) {
		double w0 = w * b.w - x * b.x - y * b.y - z * b.z;
		double x0 = w * b.x + x * b.w + y * b.z - z * b.y;
		double y0 = w * b.y - x * b.z + y * b.w + z * b.x;
		double z0 = w * b.z + x * b.y - y * b.x + z * b.w;
		return new Quaternion(w0, x0, y0, z0);
	}

	public Vector3 multiply(final Vector3 vec) {
		Quaternion vecQuat = new Quaternion(0.0f, vec.getX(), vec.getY(), vec.getZ());
		Quaternion resQuat = multiply(vecQuat).multiply(conjugate());
		return (new Vector3(resQuat.x, resQuat.y, resQuat.z));
	}

	public static Quaternion fromAxis(Vector3 v, double angle) {
		Vector3 vn = v.normalize();

		double sinAngle = (double) Math.sin(angle / 2);

		double x = (vn.getX() * sinAngle);
		double y = (vn.getY() * sinAngle);
		double z = (vn.getZ() * sinAngle);
		double w = (double) Math.cos(angle / 2);
		return new Quaternion(w, x, y, z);
	}

	/**
	 * 
	 * @param roll
	 *            in radiants
	 * @param pitch
	 *            in radiants
	 * @param yaw
	 *            in radiants
	 * @return
	 */
	public static Quaternion fromEuler(double roll, double pitch, double yaw) {
		// Basically we create 3 Quaternions, one for pitch, one for yaw, one
		// for roll
		// and multiply those together.
		// the calculation below does the same, just shorter

		double p = (double) (pitch / 2.0);
		double y = (double) (-yaw / 2.0);
		double r = (double) (-roll / 2.0);

		double sinp = (double) Math.sin(p);
		double siny = (double) Math.sin(y);
		double sinr = (double) Math.sin(r);
		double cosp = (double) Math.cos(p);
		double cosy = (double) Math.cos(y);
		double cosr = (double) Math.cos(r);

		double _x = sinr * cosp * cosy - cosr * sinp * siny;
		double _y = cosr * sinp * cosy + sinr * cosp * siny;
		double _z = cosr * cosp * siny - sinr * sinp * cosy;
		double _w = cosr * cosp * cosy + sinr * sinp * siny;

		return new Quaternion(_w, _x, _y, _z).normalize();
	}

	public double getW() {
		return w;
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

	public double[] getDoubleArray() {
		return new double[] { w, x, y, z };
	}

	public double[] getDoubleArrayXYZW() {
		return new double[] { x, y, z, w };
	}

	/**
	 * <code>toAngles</code> returns this quaternion converted to Euler rotation
	 * angles (roll,pitch,yaw).<br/>
	 * 
	 * @see <a href=
	 *      "http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm">
	 *      http://www.euclideanspace.com/maths/geometry/rotations/conversions/
	 *      quaternionToEuler/index.htm</a>
	 * 
	 * @return the float[](roll,pitch,yaw) in which the angles are stored.
	 */
	public double[] toAngles() {
		double[] result = new double[3];
		double sqw = w * w;
		double sqx = x * x;
		double sqy = y * y;
		double sqz = z * z;
		// if normalized is one, otherwise is correction factor
		double unit = sqx + sqy + sqz + sqw;
		double test = x * y + z * w;
		if (test > 0.499 * unit) { // singularity at north pole
			result[1] = 2 * Math.atan2(x, w);
			result[2] = -Math.PI / 2;
			result[0] = 0;
		} else if (test < -0.499 * unit) { // singularity at south pole
			result[1] = -2 * Math.atan2(x, w);
			result[2] = Math.PI / 2;
			result[0] = 0;
		} else {
			result[1] = Math.atan2(2 * y * w - 2 * x * z, sqx - sqy - sqz + sqw);
			result[2] = -Math.asin(2 * test / unit);
			result[0] = -Math.atan2(2 * x * w - 2 * y * z, -sqx + sqy - sqz + sqw);
		}
		return result;
	}

	@Override
	public String toString() {
		return "[roll,pitch,yaw]= " + Arrays.toString(toAngles()) + "  Q [w=" + String.format("%.4f", w) + ", x="
				+ String.format("%.4f", x) + ", y=" + String.format("%.4f", y) + ", z=" + String.format("%.4f", z)
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(w);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Quaternion other = (Quaternion) obj;
		if (Double.doubleToLongBits(w) != Double.doubleToLongBits(other.w))
			return false;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}
}