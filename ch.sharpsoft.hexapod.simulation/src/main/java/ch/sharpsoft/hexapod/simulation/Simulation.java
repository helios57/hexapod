package ch.sharpsoft.hexapod.simulation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

import ch.sharpsoft.hexapod.Hexapod;
import ch.sharpsoft.hexapod.Leg;
import ch.sharpsoft.hexapod.WalkSafe;
import ch.sharpsoft.hexapod.util.Ground;
import ch.sharpsoft.hexapod.util.Quaternion;
import ch.sharpsoft.hexapod.util.Vector3;

public class Simulation {
	private final Ground ground = new Ground();
	private final double g = 9.81; // erdanziehungskraft in m/s
	private final double m = 2000;// masse in gramm

	/**
	 * Mittelpunkt des Hexapod in global
	 */
	private Vector3 globalPosition = new Vector3(0, 0, 0);
	private Vector3 globalSpeed = new Vector3(0, 0, 0);

	private Quaternion globalOrietation = Quaternion.fromEuler(0, 0, 0);
	private Quaternion globalOrietationSpeed = Quaternion.fromEuler(0, 0, 0);

	private long simulationTime = 0;// in ms

	private Hexapod hp_soll;
	private Hexapod hp_ist;
	private long ms_soll_to_ist = 0;
	private double[][] deltaAnglePerMs = new double[6][3];
	private WalkSafe ws;

	public Simulation() {
		hp_soll = new Hexapod();
		hp_ist = new Hexapod();
		new HexapodRenderer(hp_soll);
		new HexapodRenderer(hp_ist);
		ws = new WalkSafe(hp_soll);
		ws.setDirection(new Vector3(1, 0, 0));
	}

	public static void main(String[] args) {
		new Simulation().simulate();
	}

	private void simulate() {
		while (true) {
			hexapodAction();
			while (ms_soll_to_ist > 0) {
				calculate(1);
				// System.out.println("soll " + hp_soll);
				// System.out.println("ist " + hp_ist);
			}
			LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));
			System.out.println(simulationTime);
		}
	}

	private void calculate(long dt_ms) {
		simulationTime += dt_ms;
		ms_soll_to_ist--;
		List<Leg> legsIst = hp_ist.getLegs();
		for (int i = 0; i < deltaAnglePerMs.length; i++) {
			Leg leg = legsIst.get(i);
			double[] legIst = leg.getAngles();
			for (int j = 0; j < 3; j++) {
				legIst[j] = legIst[j] + deltaAnglePerMs[i][j];
			}
			Vector3 endpointBefore = leg.getEndpoint();
			leg.setAngles(legIst[0], legIst[1], legIst[2]);
			Vector3 endpointAfter = leg.getEndpoint();
			
		}
	}

	private void hexapodAction() {
		ms_soll_to_ist = 100;
		ws.doNextAction();
		List<Leg> legsSoll = hp_soll.getLegs();
		List<Leg> legsIst = hp_ist.getLegs();
		for (int i = 0; i < deltaAnglePerMs.length; i++) {
			double[] legIst = legsIst.get(i).getAngles();
			double[] legSoll = legsSoll.get(i).getAngles();
			for (int j = 0; j < 3; j++) {
				deltaAnglePerMs[i][j] = (legSoll[j] - legIst[j]) / ms_soll_to_ist;
			}
		}
	}

	public void run() {
		List<Vector3> lastEndpoints = getEndpoints();
		Double ground = lastEndpoints.stream().map(e -> e.getZ()).collect(Collectors.averagingDouble(d -> d)) + 0.5f;
		System.out.println("ground " + ground);
		List<Vector3> groundVectors = new ArrayList<>(6);
		for (int i = 0; i < 1000; i++) {
			groundVectors.clear();

			ws.doNextAction();
			List<Vector3> currentEndpoints = getEndpoints();
			for (int j = 0; j < currentEndpoints.size(); j++) {
				Vector3 last = lastEndpoints.get(j);
				Vector3 current = currentEndpoints.get(j);
				if (last.getZ() < ground && current.getZ() < ground) {
					groundVectors.add(current.substract(last));
				}
			}

			for (Vector3 gv : groundVectors) {
				System.out.println(gv);
			}

			lastEndpoints = currentEndpoints;
		}
	}

	private List<Vector3> getEndpoints() {
		return hp_soll.getLegs().stream().map(l -> l.getEndpoint()).collect(Collectors.toList());
	}
}