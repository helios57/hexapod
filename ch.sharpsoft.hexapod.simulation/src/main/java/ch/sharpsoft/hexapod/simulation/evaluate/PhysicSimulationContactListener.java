package ch.sharpsoft.hexapod.simulation.evaluate;

import java.util.List;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;

class PhysicSimulationContactListener extends ContactListener {
	private List<SimulationObject> instances;

	public PhysicSimulationContactListener(List<SimulationObject> instances) {
		this.instances = instances;
	}

	@Override
	public boolean onContactAdded(int userValue0, int partId0, int index0, boolean match0, int userValue1, int partId1,
			int index1, boolean match1) {
		SimulationObject o0 = instances.get(userValue0);
		SimulationObject o1 = instances.get(userValue1);
		if (o0.isHexaPodBody() && o1.isHexaPodBody()) {

		}
		return true;
	}
}