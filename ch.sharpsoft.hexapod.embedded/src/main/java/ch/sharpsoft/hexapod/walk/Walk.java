package ch.sharpsoft.hexapod.walk;

import ch.sharpsoft.hexapod.util.Quaternion;
import ch.sharpsoft.hexapod.util.Vector3;

/**
 * Interface for Walk-Algorithms
 */
public interface Walk {
	/**
	 * The direction to Walk, if the length of the Vector is to great it will be
	 * shortened. <br>
	 * Set to new {@link Vector3}(0,0,0) or <code>null</code> to stop
	 * walking.<br>
	 */
	void setDirection(Vector3 direction);

	/**
	 * The rotation to perform. The rotation will be normalized and only x/y
	 * rotation-parts will be taken into account. Set to <code>null</code> to
	 * stop rotation.
	 */
	void setHeadingRelative(Quaternion rotation);

	/**
	 * Perform the next walk action, optionally taking the deltaT into account.
	 */
	void doNextAction();

	/**
	 * Set the Size each action will perform
	 */
	void setActionSize(double cm);

	/**
	 * Set the distance legs will be lifted while walking.
	 */
	void setLegLift(double legLiftinCm);

	/**
	 * Set the height from the ground to the center of the hexapod while
	 * standing still or walking.
	 */
	void setWalkHeight(double heightInCm);
}
