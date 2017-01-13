package ch.sharpsoft.hexapod.util;

public class Quaternion {

	public final double x;
	public final double y;
	public final double z;
	public final double w;

	public Quaternion(final double[] q) {
		this(q[0], q[1], q[2], q[3]);
	}

	public Quaternion(final double x, final double y, final double z, final double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * @return the length squared of the quaternion
	 */
	public double lengthSquared() {
		return x * x + y * y + z * z + w * w;
	}

	public double norm() {
		return Math.sqrt(lengthSquared());
	}

	public Quaternion normalize() {
		final double norm = norm();
		return new Quaternion((x / norm), (y / norm), (z / norm), (w / norm));
	}

	public Quaternion conjugate() {
		return new Quaternion(-x, -y, -z, w);
	}

	public Quaternion plus(final Quaternion b) {
		return new Quaternion(x + b.x, y + b.y, z + b.z, w + b.w);
	}

	// return a new Quaternion whose value is the inverse of this
	public Quaternion inverse() {
		final double d = lengthSquared();
		return new Quaternion(-x / d, -y / d, -z / d, w / d).normalize();
	}

	/**
	 * Multiplies quaternion by the inverse of quaternion right.
	 */
	public Quaternion mulInverse(final Quaternion right) {
		double n = right.lengthSquared();
		// zero-div may occur.
		n = (n == 0.0 ? n : 1 / n);
		return new Quaternion(//
				(x * right.w - w * right.x - y * right.z + z * right.y) * n, //
				(y * right.w - w * right.y - z * right.x + x * right.z) * n, //
				(z * right.w - w * right.z - x * right.y + y * right.x) * n, //
				(w * right.w + x * right.x + y * right.y + z * right.z) * n);
	}

	// return a new Quaternion whose value is (this * b)
	public Quaternion multiply(final Quaternion q) {
		return new Quaternion(//
				(w * q.x) + (x * q.w) + (y * q.z) - (z * q.y), //
				(w * q.y) + (y * q.w) + (z * q.x) - (x * q.z), //
				(w * q.z) + (z * q.w) + (x * q.y) - (y * q.x), //
				(w * q.w) - (x * q.x) - (y * q.y) - (z * q.z));
	}

	public Vector3 multiply(final Vector3 vec) {
		final Quaternion vecQuat = new Quaternion(vec.getX(), vec.getY(), vec.getZ(), 0.0f);
		final Quaternion normalize = normalize();
		final Quaternion resQuat = normalize.multiply(vecQuat).multiply(normalize.conjugate());
		return (new Vector3(resQuat.x, resQuat.y, resQuat.z));
	}

	public Vector3 getDirectionX(double length) {
		double vx = 1 - 2 * y * y - 2 * z * z;
		double vy = 2 * x * y + 2 * z * w;
		double vz = 2 * x * z - 2 * y * w;
		return new Vector3(vx, vy, vz).multiply(length);
	}

	public static Quaternion fromAxis(final Vector3 v, final double angle) {
		final Vector3 vn = v.normalize();

		final double sinAngle = Math.sin(angle / 2);

		final double x = (vn.getX() * sinAngle);
		final double y = (vn.getY() * sinAngle);
		final double z = (vn.getZ() * sinAngle);
		final double w = Math.cos(angle / 2);
		return new Quaternion(x, y, z, w);
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
	public static Quaternion fromEuler(final double roll, final double pitch, final double yaw) {
		// Basically we create 3 Quaternions, one for pitch, one for yaw, one
		// for roll
		// and multiply those together.
		// the calculation below does the same, just shorter

		final double p = (pitch / 2.0);
		final double y = (-yaw / 2.0);
		final double r = (-roll / 2.0);

		final double sinp = Math.sin(p);
		final double siny = Math.sin(y);
		final double sinr = Math.sin(r);
		final double cosp = Math.cos(p);
		final double cosy = Math.cos(y);
		final double cosr = Math.cos(r);

		final double _x = sinr * cosp * cosy - cosr * sinp * siny;
		final double _y = cosr * sinp * cosy + sinr * cosp * siny;
		final double _z = cosr * cosp * siny - sinr * sinp * cosy;
		final double _w = cosr * cosp * cosy + sinr * sinp * siny;

		return new Quaternion(_x, _y, _z, _w).normalize();
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
		final double[] result = new double[3];
		final double sqw = w * w;
		final double sqx = x * x;
		final double sqy = y * y;
		final double sqz = z * z;
		// if normalized is one, otherwise is correction factor
		final double unit = sqx + sqy + sqz + sqw;
		final double test = x * y + z * w;
		if (test > 0.499 * unit) { // singularity at north pole
			result[0] = 0;
			result[1] = 2 * Math.atan2(x, w);
			result[2] = -Math.PI / 2;
		} else if (test < -0.499 * unit) { // singularity at south pole
			result[0] = 0;
			result[1] = -2 * Math.atan2(x, w);
			result[2] = Math.PI / 2;
		} else {
			result[0] = -Math.atan2(2 * x * w - 2 * y * z, -sqx + sqy - sqz + sqw);
			result[1] = Math.atan2(2 * y * w - 2 * x * z, sqx - sqy - sqz + sqw);
			result[2] = -Math.asin(2 * test / unit);
		}
		return result;
	}

	public final double getRotationX() {
		return Math.atan2((2.0 * x * w) - (2.0 * y * z), 1.0 - 2.0 * (x * x) - 2.0 * (z * z));
	}

	public final double getRotationY() {
		return Math.atan2((2.0 * y * w) - (2.0 * x * z), 1.0 - (2.0 * y * y) - (2.0 * z * z));
	}

	public final double getRotationZ() {
		return Math.asin((2.0 * x * y) + (2.0 * z * w));
	}

	@Override
	public String toString() {
		double[] euler = toAngles();
		return "[roll,pitch,yaw]= [" + String.format("%.4f", euler[0]) + "," + String.format("%.4f", euler[1]) + "," + String.format("%.4f", euler[2]) + "] Q [w=" + String.format("%.4f", w) + ", x=" + String.format("%.4f", x) + ", y="
				+ String.format("%.4f", y) + ", z=" + String.format("%.4f", z) + "]";
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
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Quaternion other = (Quaternion) obj;
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