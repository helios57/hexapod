package ch.sharpsoft.hexapod.walk;

public interface WalkInput {
	/**
	 * Current Positions of Legs
	 * 
	 * @return [legIndex][x,y,z]
	 */
	double[][] getCurrentPosition();

	/**
	 * Historical Positions of Legs
	 * 
	 * @return [0,-1,-2,...,-positionCount][legIndex][x,y,z]
	 */
	double[][][] getLastPositions(int positionCount);

	/**
	 * @return Desired direction [x,y]
	 */
	double[] getTargetDirection();

	/**
	 * @return Distance to Ground at point [x,y] (lot)
	 */
	double getGroundDist(double[] xy);
}