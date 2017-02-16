package ch.sharpsoft.hexapod.transfer;

public interface ControllerListener {

	void onSpeedChange(int speed);

	void onHeightChange(int height);

	void onMoveChange(int x, int y);
}
