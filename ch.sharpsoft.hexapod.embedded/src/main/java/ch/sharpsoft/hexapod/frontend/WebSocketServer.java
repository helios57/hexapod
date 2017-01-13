package ch.sharpsoft.hexapod.frontend;

import java.net.UnknownHostException;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

public class WebSocketServer extends org.java_websocket.server.WebSocketServer {

	public WebSocketServer() throws UnknownHostException {
	}
	
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println(conn + "" + handshake);
		conn.send("Hi From Server");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println(conn + "" + code + " " + reason + " " + reason + " " + remote);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println(conn + "" + message);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		System.out.println(conn + "" + ex);
	}
}