package ch.sharpsoft.hexapod.transfer;

public interface Remote {

	boolean sendServoPosition(String cmd);

}