package ch.sharpsoft.hexapod.util;

public class Ground {
	private final static double ground = 20.0;

	/**
	 * Distanz (im lot) der gegebenen Potition zum Boden.
	 */
	public double getDistanceToGround(Vector3 position) {
		return ground - position.getZ();
	}
}