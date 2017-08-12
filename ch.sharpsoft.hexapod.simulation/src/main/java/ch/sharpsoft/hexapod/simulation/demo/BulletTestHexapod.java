
package ch.sharpsoft.hexapod.simulation.demo;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btHingeConstraint;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.Array;

import ch.sharpsoft.hexapod.Hexapod;
import ch.sharpsoft.hexapod.Leg;
import ch.sharpsoft.hexapod.LegSegment;

/**
 * @see https://xoppa.github.io/blog/using-the-libgdx-3d-physics-bullet-wrapper-part2/
 * @author Xoppa
 */
public class BulletTestHexapod extends JFrame implements ApplicationListener {
	private static final long serialVersionUID = 1L;
	private final static int sizeX = 1048;
	private final static int sizeY = 748;

	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new BulletTestHexapod();
	}

	LwjglAWTCanvas currentTest = null;

	public BulletTestHexapod() throws HeadlessException {
		super("Bullet Test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		currentTest = new LwjglAWTCanvas(this);
		currentTest.getCanvas().setSize(sizeX, sizeY);
		getContentPane().add(currentTest.getCanvas(), BorderLayout.CENTER);
		setSize(sizeX, sizeY);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if (currentTest != null)
					currentTest.exit();
			}
		});
	}

	final static short GROUND_FLAG = 1 << 8;
	final static short OBJECT_FLAG = 1 << 9;
	final static short ALL_FLAG = -1;

	PerspectiveCamera cam;
	CameraInputController camController;
	ModelBatch modelBatch;
	Environment environment;
	Model model;
	Array<SimulationObject> instances;
	float spawnTimer;

	btCollisionConfiguration collisionConfig;
	btDispatcher dispatcher;
	SimulationContactListener contactListener;
	btBroadphaseInterface broadphase;
	btDynamicsWorld dynamicsWorld;
	btConstraintSolver constraintSolver;

	Hexapod hp = new Hexapod();

	@Override
	public void create() {
		Bullet.init();

		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(3f, 7f, 10f);
		cam.lookAt(0, 4f, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		ModelBuilder mb = new ModelBuilder();
		mb.begin();
		mb.node().id = "ground";
		mb.part("ground", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.GREEN))).box(100f, 1f, 100f);

		mb.node().id = "main";
		mb.part("main", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.BLUE))).box(24f, 2f, 12f);

		mb.node().id = "leg0";
		mb.part("leg0", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.BLUE))).box(1f, 1f, 3f);
		mb.node().id = "leg1";
		mb.part("leg1", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.BLUE))).box(5f, 1f, 3f);
		mb.node().id = "leg2";
		mb.part("leg2", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.BLUE))).box(7f, 1f, 3f);
		mb.node().id = "leg3";
		mb.part("leg3", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.BLUE))).box(13f, 1f, 3f);

		mb.node().id = "box";
		mb.part("box", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.BLUE))).box(1f, 1f, 1f);
		mb.node().id = "box2";
		mb.part("box2", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.RED))).box(1f, 1f, 1f);
		model = mb.end();

		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		constraintSolver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
		dynamicsWorld.setGravity(new Vector3(0, -9.81f, 0));
		contactListener = new SimulationContactListener();

		instances = new Array<SimulationObject>();
		createGround();

		SimulationObject main = new SimulationObject(model, "main", new btBoxShape(new Vector3(12f, 1f, 6f)), 1f);
		main.transform.trn(0f, 9f, 0f);
		main.body.proceedToTransform(main.transform);
		main.body.setUserValue(instances.size);
		main.body.setCollisionFlags(
				main.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		instances.add(main);
		dynamicsWorld.addRigidBody(main.body);
		main.body.setContactCallbackFlag(OBJECT_FLAG);
		main.body.setContactCallbackFilter(GROUND_FLAG);
		Vector3 mainMiddle = new Vector3();
		main.transform.getTranslation(mainMiddle);

		List<Leg> legs = hp.getLegs();
		legs.forEach(leg -> {
			LegSegment segment1 = leg.getSegment1();

			Vector3 sizeLeg1 = new Vector3(5f * 0.5f, 1f * 0.5f, 3f * 0.5f);
			SimulationObject leg1 = new SimulationObject(model, "leg1", new btBoxShape(sizeLeg1), 5f * 0.1f);
			ch.sharpsoft.hexapod.util.Quaternion orientation = segment1.getOrientation();
			ch.sharpsoft.hexapod.util.Vector3 correction = orientation
					.multiply(new ch.sharpsoft.hexapod.util.Vector3(sizeLeg1.x, 0, 0));
			ch.sharpsoft.hexapod.util.Vector3 startPoint = segment1.getStartPoint().add(correction);
			Quaternion orientation2 = new Quaternion();
			orientation2.set((float) orientation.getX(), (float) orientation.getZ(), (float) orientation.getY(),
					(float) -orientation.getW());
			leg1.transform.translate(0f, 9f, 0f);
			leg1.transform.translate(toVector3(startPoint));
			leg1.transform.rotate(orientation2);

			leg1.body.proceedToTransform(leg1.transform);
			leg1.body.setUserValue(instances.size);
			leg1.body.setCollisionFlags(
					leg1.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
			instances.add(leg1);

			dynamicsWorld.addRigidBody(leg1.body);
			leg1.body.setContactCallbackFlag(OBJECT_FLAG);
			leg1.body.setContactCallbackFilter(GROUND_FLAG);

			Vector3 pivotInA = toVector3(startPoint);
			Vector3 pivotInB = new Vector3(sizeLeg1.x, 0, 0);
			Vector3 axisInA = new Vector3(0f, -1f, 0f);
			Vector3 axisInB = new Vector3(0f, -1f, 0f);
			btHingeConstraint hinge = new btHingeConstraint(main.body, leg1.body, pivotInA, pivotInB, axisInA, axisInB);
			float yaw = (float) leg.getAngles()[0];// (float)
			// -orientation.toAngles()[2];
			// ((180f / Math.PI) *
			hinge.setLimit(yaw, yaw);
			hinge.enableAngularMotor(true, 1f, 1f);

			dynamicsWorld.addConstraint(hinge, true);

			LegSegment segment2 = leg.getSegment2();
			LegSegment endSegment = leg.getEndSegment();
		});

	}

	private void createGround() {
		SimulationObject ground = new SimulationObject(model, "ground", new btBoxShape(new Vector3(100f, 1f, 100f)),
				0f);
		ground.body.setCollisionFlags(
				ground.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
		instances.add(ground);
		dynamicsWorld.addRigidBody(ground.body);
		ground.body.setContactCallbackFlag(GROUND_FLAG);
		ground.body.setContactCallbackFilter(0);
		ground.body.setActivationState(Collision.DISABLE_DEACTIVATION);
	}

	private Vector3 toVector3(ch.sharpsoft.hexapod.util.Vector3 startPoint) {
		return new Vector3((float) startPoint.getX(), (float) startPoint.getZ(), (float) startPoint.getY());
	}

	@Override
	public void render() {
		final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());
		dynamicsWorld.stepSimulation(delta, 5, 1f / 60f);

		if ((spawnTimer -= delta) < 0) {
			spawnTimer = 1.5f;
		}

		camController.update();

		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();
	}

	@Override
	public void dispose() {
		for (SimulationObject obj : instances)
			obj.dispose();
		instances.clear();

		dynamicsWorld.dispose();
		constraintSolver.dispose();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfig.dispose();

		contactListener.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void resize(int width, int height) {
	}
}