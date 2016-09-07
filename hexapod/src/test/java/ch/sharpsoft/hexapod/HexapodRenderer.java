package ch.sharpsoft.hexapod;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class HexapodRenderer {
	Hexapod hp;

	public HexapodRenderer(Hexapod hp) {
		this.hp = hp;
	}

	protected void setup(GL2 gl, int width, int height) {
		final float h = (float) width / (float) height;
		GLU glu = new GLU();

		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glClearColor(0f, 0f, 0f, 0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LEQUAL);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45.0f, h, 1.0, 20.0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	protected void render(GL2 gl2, int width, int height) {
		gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl2.glLoadIdentity();
		gl2.glTranslatef(0f, 0f, 0.0f);

		// draw a triangle filling the window
		gl2.glLoadIdentity();
		for (Leg l : hp.getLegs()) {
			gl2.glBegin(GL.GL_LINE_LOOP);
			gl2.glColor3f(1.0f, 1.0f, 1.0f);
			Vector3 p0 = l.getSegment1().getStartPoint();
			Vector3 p1 = l.getSegment1().getEndPoint();
			Vector3 p2 = l.getSegment2().getEndPoint();
			Vector3 p3 = l.getEndSegment().getEndPoint();
			gl2.glVertex3f(0f, 0f, 0f);
			push(gl2, p0);
			push(gl2, p1);
			push(gl2, p2);
			push(gl2, p3);
			push(gl2, p2);
			push(gl2, p1);
			gl2.glEnd();
		}
	}

	private final static float ratio = 1.0f;

	private void push(GL2 gl2, Vector3 v) {
		gl2.glVertex3f((float) (ratio * v.getX()), (float) (ratio * v.getY()), (float) (ratio * v.getZ()));
	}
}