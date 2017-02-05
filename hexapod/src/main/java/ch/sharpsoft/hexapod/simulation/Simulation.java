package ch.sharpsoft.hexapod.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.sharpsoft.hexapod.Hexapod;
import ch.sharpsoft.hexapod.Vector3;
import ch.sharpsoft.hexapod.WalkSafe;

public class Simulation {
	private Hexapod hp;
	private WalkSafe ws;

	public Simulation() {
		hp = new Hexapod();
		ws = new WalkSafe(hp);
		ws.setDirection(new Vector3(1, 0, 0));
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
		return hp.getLegs().stream().map(l -> l.getEndpoint()).collect(Collectors.toList());
	}

	public static void main(String[] args) {
		new Simulation().run();
	}
}