package ch.sharpsoft.hexapod.simulation.demo;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	private final static int sizeX = 2048;
	private final static int sizeY = 1548;

	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new Main();
	}

	LwjglAWTCanvas currentTest = null;

	public Main() throws HeadlessException {
		super("Bullet Test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ApplicationListener listener = new ch.sharpsoft.hexapod.simulation.demo.BulletSimulation();
		currentTest = new LwjglAWTCanvas(listener);
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
}