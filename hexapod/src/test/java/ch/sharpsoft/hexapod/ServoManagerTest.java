package ch.sharpsoft.hexapod;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.sharpsoft.hexapod.transfer.RemotePaho;

public class ServoManagerTest {

	public static void main(String[] args) throws Exception {
		Hexapod hp = new Hexapod();
		RemotePaho remote = new RemotePaho();
		remote.init();
		ServoManager sm = new ServoManager(hp, remote);

		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(0, 0, 1024, 1024);
		for (final Leg l : hp.getLegs()) {
			JButton jb = new JButton();
			jb.setText("Leg " + l.getId());
			JPanel mainframe = new JPanel();
			mainframe.add(jb);
			JTextField link = new JTextField(50);
			mainframe.add(link);
			window.getContentPane().add(mainframe);
		}
		window.pack();
		window.setVisible(true);

		double i = 0.0;
		while (true) {
			// hp.getLegs().stream().filter(p->p.getId()%2==0).forEach(action);
			sm.sendState();
			Thread.sleep(100);
		}
	}
}