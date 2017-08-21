
package ch.sharpsoft.hexapod.simulation.evaluate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btHingeConstraint;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.Disposable;

import ch.sharpsoft.hexapod.Hexapod;
import ch.sharpsoft.hexapod.Leg;
import ch.sharpsoft.hexapod.LegSegment;

public class PhysicSimulation implements Disposable {
	private final static short GROUND_FLAG = 1 << 8;
	private final static short OBJECT_FLAG = 1 << 9;

	private final btCollisionConfiguration collisionConfig;
	private final btDispatcher dispatcher;
	private final PhysicSimulationContactListener contactListener;
	private final btBroadphaseInterface broadphase;
	private final btDynamicsWorld dynamicsWorld;
	private final btConstraintSolver constraintSolver;

	private final Hexapod hp = new Hexapod();
	private final Set<Runnable> updateAngles = new HashSet<>();
	private final List<SimulationObject> instances = new ArrayList<>();
	private final Vector3 lastPosition = new Vector3();
	private SimulationObject torso;

	public PhysicSimulation() {
		Bullet.init();
		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		constraintSolver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
		dynamicsWorld.setGravity(new Vector3(0, -9.81f, 0));
		contactListener = new PhysicSimulationContactListener();
		createGround();
		createTorso();
		createLegs();
	}

	private void createLegs() {
		final List<Leg> legs = hp.getLegs();
		legs.forEach(leg -> {
			createLeg(leg);
		});
	}

	private void createLeg(Leg leg) {
		final LegSegment segment1 = leg.getSegment1();
		final Vector3 sizeLeg1 = new Vector3(5f * 0.5f, 1f * 0.5f, 3f * 0.5f);
		final SimulationObject leg1 = new SimulationObject(new btBoxShape(sizeLeg1), 1f * 0.1f);
		final ch.sharpsoft.hexapod.util.Quaternion orientation = segment1.getOrientation();
		final ch.sharpsoft.hexapod.util.Vector3 correction = orientation.multiply(new ch.sharpsoft.hexapod.util.Vector3(sizeLeg1.x, 0, 0));
		final ch.sharpsoft.hexapod.util.Vector3 startPoint = segment1.getStartPoint().add(correction);
		final Quaternion orientation1 = new Quaternion();
		orientation1.set((float) orientation.getX(), (float) orientation.getZ(), (float) orientation.getY(), (float) -orientation.getW());
		leg1.transform.translate(0f, 16f, 0f);
		leg1.transform.translate(toVector3(startPoint));
		leg1.transform.rotate(orientation1);

		finishLeg(leg1);

		final float yaw = (float) leg.getStartYaw();

		final LegSegment segment2 = leg.getSegment2();
		final Vector3 sizeLeg2 = new Vector3(7f * 0.5f, 1f * 0.5f, 3f * 0.5f);
		final SimulationObject leg2 = new SimulationObject(new btBoxShape(sizeLeg2), 1f * 0.1f);
		final ch.sharpsoft.hexapod.util.Quaternion orientation2 = segment2.getOrientation();
		final ch.sharpsoft.hexapod.util.Vector3 correction2 = orientation2.multiply(new ch.sharpsoft.hexapod.util.Vector3(sizeLeg2.x, 0, 0));
		final ch.sharpsoft.hexapod.util.Vector3 startPoint2 = segment2.getStartPoint().add(correction2);
		final Quaternion orientation22 = new Quaternion();
		orientation22.set((float) orientation2.getX(), (float) orientation2.getZ(), (float) orientation2.getY(), (float) -orientation2.getW());
		leg2.transform.translate(0f, 16f, 0f);
		leg2.transform.translate(toVector3(startPoint2));
		leg2.transform.rotate(orientation22);

		finishLeg(leg2);

		final LegSegment segment3 = leg.getEndSegment();
		final Vector3 sizeLeg3 = new Vector3(13f * 0.5f, 1f * 0.5f, 3f * 0.5f);
		final btConvexHullShape leg3Shape = createLeg3ShapePyramid();

		final SimulationObject leg3 = new SimulationObject(leg3Shape, 1f * 0.2f);
		final ch.sharpsoft.hexapod.util.Quaternion orientation3 = segment3.getOrientation();
		final ch.sharpsoft.hexapod.util.Vector3 correction3 = orientation.multiply(new ch.sharpsoft.hexapod.util.Vector3(sizeLeg3.x, 0, 0));
		final ch.sharpsoft.hexapod.util.Vector3 startPoint3 = segment3.getStartPoint().add(correction3);
		final Quaternion orientation33 = new Quaternion();
		orientation33.set((float) orientation3.getX(), (float) orientation3.getZ(), (float) orientation3.getY(), (float) -orientation3.getW());
		leg3.transform.translate(0f, 16f, 0f);
		leg3.transform.translate(toVector3(startPoint3));
		leg3.transform.rotate(orientation33);

		finishLeg(leg3);

		// torso->leg1
		final Vector3 pivotInA = toVector3(startPoint);
		final Vector3 pivotInB = new Vector3(-sizeLeg1.x, 0, 0);
		final Vector3 axisInA = new Vector3(0f, -3f, 0f);
		final Vector3 axisInB = new Vector3(0f, -3f, 0f);
		final btHingeConstraint hinge1 = new btHingeConstraint(torso.body, leg1.body, pivotInA, pivotInB, axisInA, axisInB);

		// leg1 -> leg2
		final Vector3 pivotInA2 = new Vector3(sizeLeg1.x, 0, 0);
		final Vector3 pivotInB2 = new Vector3(-sizeLeg2.x, 0, 0);
		final Vector3 axisInA2 = new Vector3(0f, 0f, 3f);
		final Vector3 axisInB2 = new Vector3(0f, 0f, 3f);
		final btHingeConstraint hinge2 = new btHingeConstraint(leg1.body, leg2.body, pivotInA2, pivotInB2, axisInA2, axisInB2);

		// leg2 -> leg3
		final Vector3 pivotInA3 = new Vector3(sizeLeg2.x, 0, 0);
		final Vector3 pivotInB3 = new Vector3(-sizeLeg3.x, 0, 0);
		final Vector3 axisInA3 = new Vector3(0f, 0f, 3f);
		final Vector3 axisInB3 = new Vector3(0f, 0f, 3f);

		final btHingeConstraint hinge3 = new btHingeConstraint(leg2.body, leg3.body, pivotInA3, pivotInB3, axisInA3, axisInB3);

		Runnable update = () -> {
			final double[] angles = leg.getAngles();
			final float a1 = (float) (yaw + angles[0]);
			final float a2 = (float) (angles[1]);
			final float a3 = (float) (angles[2]) - a2;
			hinge1.setLimit(a1, a1, 0.1f, 1f, 0.1f);
			hinge2.setLimit(a2, a2, 0.1f, 1f, 0.1f);
			hinge3.setLimit(a3, a3, 0.1f, 1f, 0.1f);
		};
		update.run();
		updateAngles.add(update);
		dynamicsWorld.addConstraint(hinge1, true);
		dynamicsWorld.addConstraint(hinge2, true);
		dynamicsWorld.addConstraint(hinge3, true);
	}

	private btConvexHullShape createLeg3ShapePyramid() {
		final btConvexHullShape leg3Shape = new btConvexHullShape();
		leg3Shape.addPoint(new Vector3(-13f * 0.5f, 1f * 0.5f, 3f * 0.5f));
		leg3Shape.addPoint(new Vector3(-13f * 0.5f, -1f * 0.5f, 3f * 0.5f));
		leg3Shape.addPoint(new Vector3(-13f * 0.5f, -1f * 0.5f, 3f * -0.5f));
		leg3Shape.addPoint(new Vector3(-13f * 0.5f, 1f * 0.5f, 3f * -0.5f));
		leg3Shape.addPoint(new Vector3(13f * 0.5f, 0f, 0f));
		return leg3Shape;
	}

	private void finishLeg(final SimulationObject leg) {
		leg.body.proceedToTransform(leg.transform);
		leg.body.setUserValue(instances.size());
		leg.body.setCollisionFlags(leg.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		instances.add(leg);
		dynamicsWorld.addRigidBody(leg.body);
		leg.body.setContactCallbackFlag(OBJECT_FLAG);
		leg.body.setContactCallbackFilter(GROUND_FLAG);
	}

	private void createTorso() {
		torso = new SimulationObject(new btBoxShape(new Vector3(12f, 1f, 6f)), 2f);

		torso.transform.trn(0f, 16f, 0f);
		torso.body.proceedToTransform(torso.transform);
		torso.body.setUserValue(instances.size());
		torso.body.setCollisionFlags(torso.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		instances.add(torso);
		dynamicsWorld.addRigidBody(torso.body);
		torso.body.setContactCallbackFlag(OBJECT_FLAG);
		torso.body.setContactCallbackFilter(GROUND_FLAG);
		final Vector3 mainMiddle = new Vector3();
		torso.transform.getTranslation(mainMiddle);
	}

	private void createGround() {
		final SimulationObject ground = new SimulationObject(new btBoxShape(new Vector3(10000f, 1f, 10000f)), 0f);
		ground.body.setCollisionFlags(ground.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
		instances.add(ground);
		dynamicsWorld.addRigidBody(ground.body);
		ground.body.setContactCallbackFlag(GROUND_FLAG);
		ground.body.setContactCallbackFilter(0);
		ground.body.setActivationState(Collision.DISABLE_DEACTIVATION);
	}

	private Vector3 toVector3(final ch.sharpsoft.hexapod.util.Vector3 startPoint) {
		return new Vector3((float) startPoint.getX(), (float) startPoint.getZ(), (float) startPoint.getY());
	}

	public double[] loop(double dtInSec) {
		dynamicsWorld.stepSimulation((float) dtInSec, 60, (float) dtInSec);
		updateAngles.forEach(Runnable::run);
		instances.get(1).transform.getTranslation(lastPosition);
		return new double[] { lastPosition.x, lastPosition.y, lastPosition.z };
	}

	@Override
	public void dispose() {
		instances.forEach(Disposable::dispose);
		instances.clear();
		dynamicsWorld.dispose();
		constraintSolver.dispose();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfig.dispose();
		contactListener.dispose();
	}
}