package ch.sharpsoft.hexapod.transfer;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Consumer;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import ch.sharpsoft.hexapod.transfer.Api.IMU;

public class RemotePaho implements MqttCallback, Remote {
	private final static String TOPIC_IN_IMU = "Sensor/IMU";
	private final static String TOPIC_IN_CONTROL = "CONTROLLER/#";
	private final static String TOPIC_OUT_SERVO = "Servo/Pos";
	private final static String BROKER = "tcp://192.168.2.84:1883";
	// private final static String BROKER = "tcp://127.0.0.1:1883";
	// private final static String BROKER = "tcp://192.168.2.122:1883";
	// private final static String BROKER = "tcp://192.168.2.167:1883";
	private final static String CLIENTID = "JavaController";
	private final MemoryPersistence persistence = new MemoryPersistence();
	private final Deque<IMqttDeliveryToken> openMsgs = new LinkedList<>();
	private MqttAsyncClient client;
	private Consumer<String> inControlListener;
	private ControllerListener controllerListener;

	public RemotePaho() {
	}

	public void init() {
		try {
			client = new MqttAsyncClient(BROKER, CLIENTID, persistence);
			final MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			client.connect(connOpts);
			client.setCallback(this);
			Thread.sleep(1000);
			client.subscribe(TOPIC_IN_IMU, 0);
			client.subscribe(TOPIC_IN_CONTROL, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setInControlListener(Consumer<String> inControlListener) {
		this.inControlListener = inControlListener;

	}

	public void setControllerListener(ControllerListener controllerListener) {
		this.controllerListener = controllerListener;
	}

	public void messageArrived(final String topic, final MqttMessage message) throws Exception {
		if (TOPIC_IN_IMU.equals(topic)) {
			IMU imu = IMU.parseFrom(message.getPayload());
		}
		if ("CONTROLLER/SPEED".equals(topic)) {
			String speed = new String(message.getPayload());
			try {
				Integer s = Integer.valueOf(speed);
				if (controllerListener != null) {
					controllerListener.onSpeedChange(s);
				}
			} catch (Exception e) {
			}
		}
		if ("CONTROLLER/HEIGHT".equals(topic)) {
			String height = new String(message.getPayload());
			try {
				Integer h = Integer.valueOf(height);
				if (controllerListener != null) {
					controllerListener.onHeightChange(h);
				}
			} catch (Exception e) {
			}
		}
		if ("CONTROLLER/MOVE".equals(topic)) {
			String move = new String(message.getPayload());
			try {
				String[] split = move.split(";");
				int x = Integer.valueOf(split[0]).intValue();
				int y = Integer.valueOf(split[1]).intValue();
				if (controllerListener != null) {
					controllerListener.onMoveChange(x, y);
				}
			} catch (Exception e) {
			}
		}
	}

	@Override
	public boolean sendServoPosition(final String cmd) {
		synchronized (openMsgs) {
			if (openMsgs.size() > 65000) {
				return false;
			}
			final MqttMessage msg = new MqttMessage();
			msg.setPayload(cmd.getBytes());
			msg.setQos(0);
			IMqttDeliveryToken token;
			try {
				token = client.publish(TOPIC_OUT_SERVO, msg);
				openMsgs.addLast(token);
			} catch (MqttException e) {
				e.printStackTrace();
			}
			return true;
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		throw new RuntimeException(cause);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		synchronized (openMsgs) {
			openMsgs.remove(token);
		}
	}
}