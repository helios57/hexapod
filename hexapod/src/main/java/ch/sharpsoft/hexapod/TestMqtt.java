package ch.sharpsoft.hexapod;

import java.util.concurrent.locks.LockSupport;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class TestMqtt implements MqttCallback {

	public static void main(String[] args) {
		String topic = "Test/Topic";
		int qos = 2;
		String broker = "tcp://192.168.2.122:1883";
		String clientId = "JavaSample";
		MemoryPersistence persistence = new MemoryPersistence();

		try {
			MqttAsyncClient sampleClient = new MqttAsyncClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			sampleClient.setCallback(new TestMqtt());
			System.out.println("Connecting to broker: " + broker);
			sampleClient.connect(connOpts);
			System.out.println("Connected");
			Thread.sleep(1000);
			sampleClient.subscribe(topic, qos);
			System.out.println("Subscribed");
			int count = 0;
			while (true) {
				MqttMessage mqttMessage = new MqttMessage();
				mqttMessage.setPayload(("TestMessage" + count++).getBytes());
				mqttMessage.setQos(2);
				IMqttDeliveryToken token = sampleClient.publish("TestIn", mqttMessage);
				while (!token.isComplete()) {
					LockSupport.parkNanos(1000_00);
				}
			}
		} catch (Exception me) {
			if (me instanceof MqttException) {
				System.out.println("reason " + ((MqttException) me).getReasonCode());
			}
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
	}

	public void connectionLost(Throwable arg0) {
		System.err.println("connection lost");

	}

	public void deliveryComplete(IMqttDeliveryToken arg0) {
//		System.err.println("delivery complete");
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("topic: " + topic);
		System.out.println("message: " + new String(message.getPayload()));
	}
}
