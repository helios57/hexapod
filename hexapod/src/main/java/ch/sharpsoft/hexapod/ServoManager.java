package ch.sharpsoft.hexapod;

import java.util.List;
import java.util.stream.IntStream;

import ch.sharpsoft.hexapod.transfer.Remote;

public class ServoManager {

	private static final int SERVOS = 32;
	private final Hexapod hp;
	private final int[] servoState;
	private final int[] calibration;
	private final int[] usedServos = { 2, 3, 4, 7, 8, 9, 11, 12, 13, 27, 28, 29, 23, 24, 25, 18, 19, 20 };
	private final Remote remote;
	private int moveTime = 0;

	public ServoManager(final Hexapod hp, final Remote remote) {
		this.hp = hp;
		this.remote = remote;
		servoState = new int[SERVOS];
		calibration = new int[SERVOS];
		IntStream.range(0, servoState.length).forEach(i -> servoState[i] = 0);
	}

	public void sendState() {
		calibration[24] = 20;
		calibration[25] = 50;
		calibration[28] = -20;
		calibration[29] = -100;

		int[] oldState = new int[SERVOS];
		System.arraycopy(servoState, 0, oldState, 0, SERVOS);
		applyState();
		StringBuilder sb = new StringBuilder();
		for (int i : usedServos) {
			if (oldState[i] != servoState[i] && servoState[i] != 0) {
				sb.append("#").append(i).append("P").append(servoState[i]);
			}
		}
		if (!sb.toString().isEmpty()) {
			sb.append("t");
			sb.append(moveTime);
			sb.append("\r\n");
			System.out.println(sb.toString());
			if (!remote.sendServoPosition(sb.toString())) {
				System.arraycopy(oldState, 0, servoState, 0, SERVOS);
			}
		}
	}

	private void applyState() {
		List<Leg> legs = hp.getLegs();
		Leg leg0 = legs.get(0);
		double[] angles0 = leg0.getAngles();
		servoState[2] = angleToPulse(angles0[0]) + calibration[2];
		servoState[3] = angleToPulse(angles0[1]) + calibration[3];
		servoState[4] = angleToPulse(angles0[2]) + calibration[4];

		Leg leg1 = legs.get(1);
		double[] angles1 = leg1.getAngles();
		servoState[7] = angleToPulse(angles1[0]) + calibration[7];
		servoState[8] = angleToPulse(angles1[1]) + calibration[8];
		servoState[9] = angleToPulse(angles1[2]) + calibration[9];

		Leg leg2 = legs.get(2);
		double[] angles2 = leg2.getAngles();
		servoState[11] = angleToPulse(angles2[0]) + calibration[11];
		servoState[12] = angleToPulse(angles2[1]) + calibration[12];
		servoState[13] = angleToPulse(angles2[2]) + calibration[13];

		Leg leg3 = legs.get(3);
		double[] angles3 = leg3.getAngles();
		servoState[27] = angleToPulse(angles3[0]) + calibration[27];
		servoState[28] = angleToPulse(angles3[1]) + calibration[28];
		servoState[29] = angleToPulse(angles3[2]) + calibration[29];

		Leg leg4 = legs.get(4);
		double[] angles4 = leg4.getAngles();
		servoState[23] = angleToPulse(angles4[0]) + calibration[23];
		servoState[24] = angleToPulse(angles4[1]) + calibration[24];
		servoState[25] = angleToPulse(angles4[2]) + calibration[25];

		Leg leg5 = legs.get(5);
		double[] angles5 = leg5.getAngles();
		servoState[18] = angleToPulse(angles5[0]) + calibration[18];
		servoState[19] = angleToPulse(angles5[1]) + calibration[19];
		servoState[20] = angleToPulse(angles5[2]) + calibration[20];
	}

	private int angleToPulse(final double angleInRadiant) {
		double factor = 1950 / Math.PI;
		int result = 1500 + (int) (angleInRadiant * factor);
		if (result < 500) {
			return 500;
		}
		if (result > 2500) {
			return 2500;
		}
		return result;
	}

	public int getMoveTime() {
		return moveTime;
	}

	public void setMoveTime(int moveTime) {
		this.moveTime = moveTime;
	}
}