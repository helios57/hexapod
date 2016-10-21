package ch.sharpsoft.hexapod;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;

public class HexapodRenderer {
	Hexapod hp;

	public HexapodRenderer(Hexapod hp) {
		this.hp = hp;
		open();
	}

	/**
	 * Each box has 8 Points around it's vector. <code>
	 *        (+z)
	 *         . b1(+y)    . b5
	 * (-y)b0 '        b4 ' 
	 *        |           |
	 *        s - - - - ->e
	 *        |           |
	 *     b2 .        b6 .
	 *         ' b3        ' b7
	 *       (-z)
	 *
	 * </code>
	 */

	private void render(GL2 gl2) {
		for (Leg l : hp.getLegs()) {
			for (List<Vector3> box : l.getBoundingBoxes()) {
				pushBox(gl2, box);
			}
		}
	}

	private void pushBox(GL2 gl2, List<Vector3> box1) {
		pushIndex(gl2, box1, 2, 3, 0, 1, 4, 5, 6, 7, 2, 3);
		pushIndex(gl2, box1, 1, 3, 5, 7);
		pushIndex(gl2, box1, 6, 4, 2, 0);
	}

	private void pushIndex(GL2 gl2, List<Vector3> list, int... index) {
		gl2.glBegin(GL.GL_TRIANGLE_STRIP);
		gl2.glColor3f(0.7f, 0.7f, 1.0f);
		for (int i : index) {
			push(gl2, list.get(i));
		}
		gl2.glEnd();
	}

	private void push(GL2 gl2, Vector3 v) {
		gl2.glVertex3f((float) (v.getX()), (float) (v.getY()), (float) (v.getZ()));
	}

	private void open() {
		GLProfile glprofile = GLProfile.getDefault();
		GLCapabilities glcapabilities = new GLCapabilities(glprofile);
		final GLCanvas glcanvas = new GLCanvas(glcapabilities);

		glcanvas.addGLEventListener(new GLEventListener() {

			@Override
			public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {
				GL2 gl = glautodrawable.getGL().getGL2();
				GLU glu = new GLU();
				if (height <= 0) // no divide by zero
					height = 1;
				// keep ratio
				final float h = (float) width / (float) height;
				gl.glViewport(0, 0, width, height);
				gl.glMatrixMode(GL2.GL_PROJECTION);
				gl.glLoadIdentity();
				glu.gluPerspective(100.0f, h, 1.0, 150.0);
				gl.glMatrixMode(GL2.GL_MODELVIEW);
				gl.glLoadIdentity();
			}

			@Override
			public void init(GLAutoDrawable glautodrawable) {
				GL2 gl = glautodrawable.getGL().getGL2();
				// Set backgroundcolor and shading mode
				gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
				gl.glShadeModel(GL2.GL_FLAT);
				// give me some light
				float ambient[] = { 1.0f, 1.0f, 1.0f, 1.0f };
				float diffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };
				float specular[] = { 0.2f, 0.2f, 0.2f, 1.0f };
				float position[] = { 20.0f, 30.0f, 20.0f, 0.0f };
				gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
				gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
				gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position, 0);
				// and some green material
				float amb[] = { 0.1f, 0.1f, 0.1f, 1.0f };
				float diff[] = { 0.1f, 0.1f, 0.1f, 1.0f };
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, amb, 0);
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diff, 0);
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);

				gl.glEnable(GL2.GL_LIGHTING);
				gl.glEnable(GL2.GL_LIGHT0);
				gl.glEnable(GL2.GL_DEPTH_TEST);
				gl.glEnable(GL2.GL_NORMALIZE);
			}

			@Override
			public void dispose(GLAutoDrawable glautodrawable) {
			}

			@Override
			public void display(GLAutoDrawable glautodrawable) {
				GL2 gl = glautodrawable.getGL().getGL2();
				GLU glu = new GLU(); // needed for lookat
				// Clear the drawing area
				gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
				// Reset the current matrix to the "identity"
				gl.glLoadIdentity();
				glu.gluLookAt(40, 0, 40, // eye pos
						0, 0, 0, // look at
						0, 0, 1); // up

				// gl.glRotatef(50.0f, 0.0f, 0.0f, 1.0f);
				// gl.glRotatef(40.0f, 1.0f, 0.0f, 0.0f);
				render(glautodrawable.getGL().getGL2());
				gl.glFlush();
			}
		});

		final Animator animator = new Animator(glcanvas);
		final JFrame jframe = new JFrame("Hexapod GLCanvas");
		jframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowevent) {
				animator.stop();
				jframe.dispose();
				System.exit(0);
			}
		});
		animator.start();
		jframe.getContentPane().add(glcanvas, BorderLayout.CENTER);
		jframe.setSize(1024, 1024);
		jframe.setVisible(true);
	}
}