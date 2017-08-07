package ch.sharpsoft.hexapod.simulation.demo;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeSelectionModel;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;

public class Main extends JFrame {
	private static final long serialVersionUID = -4296204662394260962L;

	public static String data;

	public static class AppDesc {
		public Class<? extends ApplicationListener> clazz;
		public String title;

		public int width;
		public int height;
		public String data;

		public AppDesc(String title, int width, int height, Class<? extends ApplicationListener> clazz, String data) {
			this.clazz = clazz;
			this.title = title;
			this.width = width;
			this.height = height;
			this.data = data;
		}

		public AppDesc(String title, int width, int height, Class<? extends ApplicationListener> clazz) {
			this(title, width, height, clazz, null);
		}

		@Override
		public String toString() {
			return title;
		}
	}

	// public final static Object[] apps = { new Object[] { "Bullet: Dynamics",
	// new AppDesc("step 6: kinematic body", 640, 480,
	// ch.sharpsoft.hexapod.simulation.demo.BulletTest.class) } };

	LwjglAWTCanvas currentTest = null;

	public boolean runApp(final AppDesc appDesc) {
		ApplicationListener listener;
		try {
			listener = appDesc.clazz.newInstance();
		} catch (InstantiationException e) {
			return false;
		} catch (IllegalAccessException e) {
			return false;
		}
		data = (appDesc.data == null || appDesc.data.isEmpty()) ? "data" : appDesc.data;

		Container container = getContentPane();
		if (currentTest != null) {
			currentTest.stop();
			container.remove(currentTest.getCanvas());
		}

		currentTest = new LwjglAWTCanvas(listener);
		currentTest.getCanvas().setSize(appDesc.width, appDesc.height);
		container.add(currentTest.getCanvas(), BorderLayout.CENTER);
		pack();
		return true;
	}

	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new Main();
	}

	public Main() throws HeadlessException {
		super("Xoppa Libgdx Tutorials");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = getContentPane();
		JPanel appList = new AppList();
		appList.setSize(250, 600);
		container.add(appList, BorderLayout.LINE_START);
		pack();
		setSize(900, 600);
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

	class AppList extends JPanel {
		private static final long serialVersionUID = 1582559224991888475L;

		public AppList() {
			setLayout(new BorderLayout());

			final JButton button = new JButton("Run Test");

			AppDesc app = new AppDesc("step 6: kinematic body", 640, 480, ch.sharpsoft.hexapod.simulation.demo.BulletTest.class);
			// dispose();
			runApp(app);
		}


		private DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(hierarchy[0]);
			DefaultMutableTreeNode child;
			for (int i = 1; i < hierarchy.length; i++) {
				Object nodeSpecifier = hierarchy[i];
				if (nodeSpecifier instanceof Object[])
					child = processHierarchy((Object[]) nodeSpecifier);
				else
					child = new DefaultMutableTreeNode(nodeSpecifier);
				node.add(child);
			}
			return node;
		}
	}
}