package ch.sharpsoft.hexapod.transfer;

import java.util.Deque;
import java.util.LinkedList;

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
	private final static String TOPIC_OUT_SERVO = "Servo/Pos";
	// private final static String BROKER = "tcp://127.0.0.1:1883";
	private final static String BROKER = "tcp://192.168.2.122:1883";
	private final static String CLIENTID = "JavaController";
	private final MemoryPersistence persistence = new MemoryPersistence();
	private final Deque<IMqttDeliveryToken> openMsgs = new LinkedList<>();
	private MqttAsyncClient client;

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void messageArrived(final String topic, final MqttMessage message) throws Exception {
		if (TOPIC_IN_IMU.equals(topic)) {
			IMU imu = IMU.parseFrom(message.getPayload());
			// System.out.println(imu);
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

	public static void main(String[] args) throws MqttException, InterruptedException {
		RemotePaho r = new RemotePaho();
		r.init();
		int count = 0;
		while (true) {
			System.out.println(count++ + " " + r.sendServoPosition(
					"#0P1600#1P1600#2P1600#3P1600#4P1600#5P1600#6P1600#7P1600#8P1600#9P1600#10P1600#11P1600#12P1600#13P1600#14P1600#15P1600#16P1600#17P1600#18P1600#19P1600#20P1600#21P1600#22P1600#23P1600#24P1600#25P1600#26P1600#27P1600#28P1600#29P1600#30P1600#31P1600\r\n"));
			Thread.sleep(100);
		}
	}
}
