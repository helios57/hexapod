package ch.sharpsoft.hexapod.simulation.evaluate;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;

public class SimulationObject implements Disposable {
	public final btRigidBody body;
	public final MotionState motionState;
	public final btCollisionShape shape;

	private Vector3 localInertia;
	public final Matrix4 transform;

	public SimulationObject(btCollisionShape shape, float mass) {
		this.shape = shape;
		localInertia = new Vector3();
		if (mass > 0f) {
			shape.calculateLocalInertia(mass, localInertia);
		} else {
			localInertia.set(0, 0, 0);
		}
		motionState = new MotionState();
		transform = new Matrix4();
		motionState.transform = transform;
		btRigidBody.btRigidBodyConstructionInfo btRigidBodyConstructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, motionState, shape, localInertia);
		body = new btRigidBody(btRigidBodyConstructionInfo);
		btRigidBodyConstructionInfo.dispose();
		body.setMotionState(motionState);
	}

	@Override
	public void dispose() {
		body.dispose();
		motionState.dispose();

	}
}