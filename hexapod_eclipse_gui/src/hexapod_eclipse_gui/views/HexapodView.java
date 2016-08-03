package hexapod_eclipse_gui.views;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import ch.sharpsoft.hexapod.Hexapod;
import ch.sharpsoft.hexapod.Leg;
import ch.sharpsoft.hexapod.ServoManager;
import ch.sharpsoft.hexapod.transfer.RemotePaho;


public class HexapodView extends ViewPart {

	public static final String ID = "hexapod_eclipse_gui.views.HexapodView";
	private Hexapod hp;
	private ServoManager sm;

	@Override
	public void createPartControl(Composite parent) {
		hp = new Hexapod();
		RemotePaho remote = new RemotePaho();
		remote.init();
		sm = new ServoManager(hp, remote);
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(parent);
		hp.getLegs().forEach(l -> addLeg(l, parent));
	}

	private void addLeg(Leg l, Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("Leg " + l.getId());
		Text text = new Text(parent, SWT.NONE);
		text.setText(string);
	}

	@Override
	public void setFocus() {
	}
}