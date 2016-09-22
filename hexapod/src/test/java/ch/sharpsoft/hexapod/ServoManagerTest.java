package ch.sharpsoft.hexapod;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import ch.sharpsoft.hexapod.transfer.RemotePaho;

public class ServoManagerTest {

	private static List<Runnable> reloads = new ArrayList<Runnable>();

	public static void main(final String[] args) throws Exception {
		final Hexapod hp = new Hexapod();
		final RemotePaho remote = new RemotePaho();
		remote.init();
		final ServoManager sm = new ServoManager(hp, remote);
		createUI(hp);
		new HexapodRenderer(hp);
		while (true) {
			sm.sendState();
			Thread.sleep(100);
		}
	}

	private static void createUI(final Hexapod hp) {
		final Font f = new Font(Font.SANS_SERIF, 1, 40);
		final JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(0, 0, 1024, 1024);
		final JPanel mainframe = new JPanel();
		mainframe.setLayout(new GridLayout(12, 1));
		for (final Leg l : hp.getLegs()) {
			final JPanel line = new JPanel();
			final JButton angles = new JButton();
			angles.setText("Angles Leg " + l.getId());
			angles.setFont(f);
			line.add(angles);

			final JTextField kee0 = new JTextField(15);
			kee0.setFont(f);
			line.add(kee0);

			final JTextField kee1 = new JTextField(15);
			kee1.setFont(f);
			line.add(kee1);

			final JTextField kee2 = new JTextField(15);
			kee2.setFont(f);
			line.add(kee2);

			final JButton endpoint = new JButton();
			endpoint.setText("EndPos " + l.getId());
			endpoint.setFont(f);
			line.add(endpoint);

			final JTextField endposX = new JTextField(15);
			endposX.setFont(f);
			line.add(endposX);

			final JTextField endposY = new JTextField(15);
			endposY.setFont(f);
			line.add(endposY);

			final JTextField endposZ = new JTextField(15);
			endposZ.setFont(f);
			line.add(endposZ);

			Runnable r = new Runnable() {

				@Override
				public void run() {
					kee0.setText(l.getAngles()[0] + "");
					kee1.setText(l.getAngles()[1] + "");
					kee2.setText(l.getAngles()[2] + "");
					endposX.setText(l.getEndpoint().getX() + "");
					endposY.setText(l.getEndpoint().getY() + "");
					endposZ.setText(l.getEndpoint().getZ() + "");
				}
			};
			r.run();
			reloads.add(r);

			angles.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Double a0 = Double.valueOf(kee0.getText());
					Double a1 = Double.valueOf(kee1.getText());
					Double a2 = Double.valueOf(kee2.getText());
					l.setAngles(a0, a1, a2);
					reload();
				}
			});

			endpoint.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Double x = Double.valueOf(endposX.getText());
					Double y = Double.valueOf(endposY.getText());
					Double z = Double.valueOf(endposZ.getText());
					l.setEndpoint(new Vector3(x, y, z));
					reload();
				}
			});

			mainframe.add(line);
		}

		{
			final JPanel line = new JPanel();
			final JButton angles = new JButton();
			angles.setText("Odd ");
			angles.setFont(f);
			line.add(angles);

			final JTextField kee0 = new JTextField(15);
			kee0.setFont(f);
			line.add(kee0);

			final JTextField kee1 = new JTextField(15);
			kee1.setFont(f);
			line.add(kee1);

			final JTextField kee2 = new JTextField(15);
			kee2.setFont(f);
			line.add(kee2);

			final JButton endpoint = new JButton();
			endpoint.setText("Move ");
			endpoint.setFont(f);
			line.add(endpoint);

			final JTextField moveX = new JTextField(15);
			moveX.setFont(f);
			line.add(moveX);

			final JTextField moveY = new JTextField(15);
			moveY.setFont(f);
			line.add(moveY);

			final JTextField moveZ = new JTextField(15);
			moveZ.setFont(f);
			line.add(moveZ);

			Runnable r = new Runnable() {

				@Override
				public void run() {
					kee0.setText("0");
					kee1.setText("0");
					kee2.setText("0");
					moveX.setText("0");
					moveY.setText("0");
					moveZ.setText("0");
				}
			};
			r.run();
			// reloads.add(r);

			angles.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Double a0 = Double.valueOf(kee0.getText());
					Double a1 = Double.valueOf(kee1.getText());
					Double a2 = Double.valueOf(kee2.getText());
					hp.getLegs().stream().filter(l -> l.getId() % 2 == 1).forEach(l -> l.setAngles(a0, a1, a2));
					reload();
				}
			});

			endpoint.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Double x = Double.valueOf(moveX.getText());
					Double y = Double.valueOf(moveY.getText());
					Double z = Double.valueOf(moveZ.getText());
					hp.getLegs().stream().filter(l -> l.getId() % 2 == 1).forEach(l -> {
						Vector3 old = l.getEndpoint();
						old.add(new Vector3(x, y, z));
						l.setEndpoint(old);
					});
					reload();
				}
			});

			mainframe.add(line);
		}

		{
			final JPanel line = new JPanel();
			final JButton angles = new JButton();
			angles.setText("Even ");
			angles.setFont(f);
			line.add(angles);

			final JTextField kee0 = new JTextField(15);
			kee0.setFont(f);
			line.add(kee0);

			final JTextField kee1 = new JTextField(15);
			kee1.setFont(f);
			line.add(kee1);

			final JTextField kee2 = new JTextField(15);
			kee2.setFont(f);
			line.add(kee2);

			final JButton endpoint = new JButton();
			endpoint.setText("Move ");
			endpoint.setFont(f);
			line.add(endpoint);

			final JTextField moveX = new JTextField(15);
			moveX.setFont(f);
			line.add(moveX);

			final JTextField moveY = new JTextField(15);
			moveY.setFont(f);
			line.add(moveY);

			final JTextField moveZ = new JTextField(15);
			moveZ.setFont(f);
			line.add(moveZ);

			Runnable r = new Runnable() {

				@Override
				public void run() {
					kee0.setText("0");
					kee1.setText("0");
					kee2.setText("0");
					moveX.setText("0");
					moveY.setText("0");
					moveZ.setText("0");
				}
			};
			r.run();
			// reloads.add(r);

			angles.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Double a0 = Double.valueOf(kee0.getText());
					Double a1 = Double.valueOf(kee1.getText());
					Double a2 = Double.valueOf(kee2.getText());
					hp.getLegs().stream().filter(l -> l.getId() % 2 == 0).forEach(l -> l.setAngles(a0, a1, a2));
					reload();
				}
			});

			endpoint.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Double x = Double.valueOf(moveX.getText());
					Double y = Double.valueOf(moveY.getText());
					Double z = Double.valueOf(moveZ.getText());
					hp.getLegs().stream().filter(l -> l.getId() % 2 == 0).forEach(l -> {
						Vector3 old = l.getEndpoint();
						old.add(new Vector3(x, y, z));
						l.setEndpoint(old);
					});
					reload();
				}
			});

			mainframe.add(line);
		}

		{
			final JPanel line = new JPanel();
			final JButton angles = new JButton();
			angles.setText("All ");
			angles.setFont(f);
			line.add(angles);

			final JTextField kee0 = new JTextField(15);
			kee0.setFont(f);
			line.add(kee0);

			final JTextField kee1 = new JTextField(15);
			kee1.setFont(f);
			line.add(kee1);

			final JTextField kee2 = new JTextField(15);
			kee2.setFont(f);
			line.add(kee2);

			final JButton endpoint = new JButton();
			endpoint.setText("Move ");
			endpoint.setFont(f);
			line.add(endpoint);

			final JTextField moveX = new JTextField(15);
			moveX.setFont(f);
			line.add(moveX);

			final JTextField moveY = new JTextField(15);
			moveY.setFont(f);
			line.add(moveY);

			final JTextField moveZ = new JTextField(15);
			moveZ.setFont(f);
			line.add(moveZ);

			Runnable r = new Runnable() {

				@Override
				public void run() {
					kee0.setText("0");
					kee1.setText("0");
					kee2.setText("0");
					moveX.setText("0");
					moveY.setText("0");
					moveZ.setText("0");
				}
			};
			r.run();
			// reloads.add(r);

			angles.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Double a0 = Double.valueOf(kee0.getText());
					Double a1 = Double.valueOf(kee1.getText());
					Double a2 = Double.valueOf(kee2.getText());
					hp.getLegs().forEach(l -> l.setAngles(a0, a1, a2));
					reload();
				}
			});

			endpoint.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Double x = Double.valueOf(moveX.getText());
					Double y = Double.valueOf(moveY.getText());
					Double z = Double.valueOf(moveZ.getText());
					hp.getLegs().forEach(l -> {
						Vector3 old = l.getEndpoint();
						Vector3 n = old.add(new Vector3(x, y, z));
						try {
							l.setEndpoint(n);
						} catch (IllegalArgumentException ex) {

						}
					});
					reload();
				}
			});

			mainframe.add(line);
		}

		window.getContentPane().add(mainframe);
		window.pack();
		window.setVisible(true);
	}

	public static void reload() {
		reloads.forEach(r -> r.run());
	}
}