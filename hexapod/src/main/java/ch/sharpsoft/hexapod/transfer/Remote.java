package ch.sharpsoft.hexapod.transfer;

import java.util.Deque;
import java.util.LinkedList;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import ch.sharpsoft.hexapod.transfer.Api.IMU;

public class Remote {
	private final static String TOPIC_IN_IMU = "Sensor/IMU";
	private final static String TOPIC_OUT_SERVO = "Servo/Pos";
	// private final static String BROKER = "tcp://192.168.2.122:1883";
	private final static String BROKER = "vm://localhost:1883";
	private final static String CLIENTID = "JavaController";
	private Topic inImuTopic;
	private Topic outServoTopic;
	private Session session;
	private MessageProducer outServoProducer;
	private MessageConsumer inImuConsumer;

	public Remote() {
	}

	public void init() throws Exception {
		BrokerService bs = new BrokerService();
		bs.setBrokerName("HexapodBroker");
		bs.addConnector("mqtt://0.0.0.0:1883");
		bs.start();
		final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER);
		final javax.jms.Connection connection = connectionFactory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		inImuTopic = session.createTopic(TOPIC_IN_IMU);
		outServoTopic = session.createTopic(TOPIC_OUT_SERVO);
		inImuConsumer = session.createConsumer(inImuTopic);
		inImuConsumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				BytesMessage b = (BytesMessage) message;
				messageArrived(TOPIC_IN_IMU, b);
			}
		});
		outServoProducer = session.createProducer(outServoTopic);
		connection.start();
	}

	public void messageArrived(final String topic, final BytesMessage message) {
		if (TOPIC_IN_IMU.equals(topic)) {
			byte[] buffer = new byte[1024];
			try {
				int len = message.readBytes(buffer);
				IMU imu = IMU.parseFrom(buffer);
				System.out.println(imu);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean sendServoPosition(final String cmd) {
		BytesMessage bs;
		try {
			bs = session.createBytesMessage();
			bs.writeBytes(cmd.getBytes());
			outServoProducer.send(bs);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static void main(String[] args) throws Exception {
		Remote r = new Remote();
		r.init();
		while (true) {
			// #<channel 0-31>P<pulswidth 500-2500>[S<movement speed in uS per
			// second>]...#
			// "#0P1500#1P1500#2P1500#3P1500#4P1500#5P1500#6P1500#7P1500#8P1500#9P1500#10P1500#11P1500#12P1500#13P1500#14P1500#15P1500#16P1500#17P1500#18P1500#19P1500#20P1500#21P1500#22P1500#23P1500#24P1500#25P1500#26P1500#27P1500#28P1500#29P1500#30P1500#31P1500\r\n";
			r.sendServoPosition("blubber");
			System.out.println("blubber");
			Thread.sleep(1000);
		}
	}
}
