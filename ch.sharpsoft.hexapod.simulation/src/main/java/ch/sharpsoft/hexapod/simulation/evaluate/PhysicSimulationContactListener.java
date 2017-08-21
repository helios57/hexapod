package ch.sharpsoft.hexapod.simulation.evaluate;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;

class PhysicSimulationContactListener extends ContactListener {

	@Override
	public boolean onContactAdded(int userValue0, int partId0, int index0, boolean match0, int userValue1, int partId1, int index1, boolean match1) {
//		if (match0)
//			((ColorAttribute) instances.get(userValue0).materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.RED);
//		if (match1)
//			((ColorAttribute) instances.get(userValue1).materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.RED);
		return true;
	}

	@Override
	public void onContactEnded(btCollisionObject colObj0, boolean match0, btCollisionObject colObj1, boolean match1) {
//		if (match0) {
//			((ColorAttribute) instances.get(colObj0.getUserValue()).materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.BLUE);
//		}
//		if (match1) {
//			((ColorAttribute) instances.get(colObj1.getUserValue()).materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.BLUE);
//		}
	}
}