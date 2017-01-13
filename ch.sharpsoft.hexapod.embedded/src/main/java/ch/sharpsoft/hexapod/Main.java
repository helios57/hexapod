package ch.sharpsoft.hexapod;

import java.net.UnknownHostException;

import ch.sharpsoft.hexapod.frontend.WebSocketServer;

public class Main {

	public static void main(String[] args) throws UnknownHostException {
		WebSocketServer webSocketServer = new WebSocketServer();
		webSocketServer.run();
	}
}