package ch.sharpsoft.hexapod.simulation;

import java.util.stream.IntStream;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorldType;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBodyFlags;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.Array;

public class BulletTest {
	final static short GROUND_FLAG = 1 << 8;
	final static short OBJECT_FLAG = 1 << 9;
	final static short ALL_FLAG = -1;

	public static void main(String[] args) {
		Bullet.init();
		float box1Mass = 1.0f;
		float box2Mass = 2.0f;

		btBoxShape ground = new btBoxShape(new Vector3(100f, 100f, 1f));
		btBoxShape box1 = new btBoxShape(new Vector3(2.5f, 0.5f, 2.5f));
		btBoxShape box2 = new btBoxShape(new Vector3(2.5f, 0.5f, 2.5f));

		btCollisionObject groundCollisionObj = new btCollisionObject();
		groundCollisionObj.setCollisionShape(box1);
		Vector3 groundPosition = new Vector3(100, 100, 0);
		Quaternion groundRotation = new Quaternion(new Vector3(1, 0, 0), 0.0f);
		groundCollisionObj.setWorldTransform(new Matrix4(groundPosition, groundRotation, new Vector3(1, 1, 1)));
		Vector3 groundLocalInteria = new Vector3(0, 0, 0);
		btRigidBody.btRigidBodyConstructionInfo groundConstructionInfo = new btRigidBody.btRigidBodyConstructionInfo(0, null, ground, groundLocalInteria);
		btRigidBody groundBody = new btRigidBody(groundConstructionInfo);

		btCollisionObject box1CollisionObj = new btCollisionObject();
		box1CollisionObj.setCollisionShape(box1);
		Vector3 position1 = new Vector3(10, 0, 100);
		Quaternion rotation1 = new Quaternion(new Vector3(1, 0, 0), 0.5f);
		box1CollisionObj.setWorldTransform(new Matrix4(position1, rotation1, new Vector3(1, 1, 1)));
		Vector3 box1LocalInteria = new Vector3();
		box1.calculateLocalInertia(box1Mass, box1LocalInteria);
		btRigidBody.btRigidBodyConstructionInfo box1ConstructionInfo = new btRigidBody.btRigidBodyConstructionInfo(box1Mass, null, box1, box1LocalInteria);
		btRigidBody box1Body = new btRigidBody(box1ConstructionInfo);

		btCollisionObject box2CollisionObj = new btCollisionObject();
		box2CollisionObj.setCollisionShape(box2);
		Vector3 position2 = new Vector3(10, 0, 200);
		Quaternion rotation2 = new Quaternion(new Vector3(1, 0, 0), 0.5f);
		box2CollisionObj.setWorldTransform(new Matrix4(position2, rotation2, new Vector3(1, 1, 1)));
		Vector3 box2LocalInteria = new Vector3();
		box2.calculateLocalInertia(box2Mass, box2LocalInteria);
		btRigidBody.btRigidBodyConstructionInfo box2ConstructionInfo = new btRigidBody.btRigidBodyConstructionInfo(box2Mass, null, box2, box2LocalInteria);
		btRigidBody box2Body = new btRigidBody(box2ConstructionInfo);

		btDefaultCollisionConfiguration collisionConfig = new btDefaultCollisionConfiguration();
		btCollisionDispatcher dispatcher = new btCollisionDispatcher(collisionConfig);

		btDbvtBroadphase broadphase = new btDbvtBroadphase();
		btSequentialImpulseConstraintSolver constraintSolver = new btSequentialImpulseConstraintSolver();
		btDiscreteDynamicsWorld dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
		dynamicsWorld.setGravity(new Vector3(0, 0, -10f));

		dynamicsWorld.addRigidBody(groundBody, GROUND_FLAG, ALL_FLAG);
		dynamicsWorld.addRigidBody(box1Body, OBJECT_FLAG, GROUND_FLAG);
		dynamicsWorld.addRigidBody(box2Body, OBJECT_FLAG, GROUND_FLAG);

		IntStream.range(0, 1000).forEach(i -> {
			dynamicsWorld.stepSimulation(0.1f, 10, 1 / 60f);
			groundBody.getWorldTransform().getTranslation(groundPosition);
			box1Body.getWorldTransform().getTranslation(position1);
			box2Body.getWorldTransform().getTranslation(position2);
			System.out.println(i + " (" + position1.x + "," + position1.y + "," + position1.z + ")");
		});
	}
}
