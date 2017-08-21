package ch.sharpsoft.hexapod.walk;

/**
 * Interface for Walk-Algorithms
 */
public interface Walk {
	/**
	 * Perform the next walk action
	 * 
	 * @return Array of legs with Array of endPoints [legIndex(0-5)][x,y,z]
	 */
	double[][] getNextPositions(WalkInput state);
}