package ch.sharpsoft.hexapod.transfer;

import org.apache.activemq.broker.BrokerService;

public class MqttBroker {

	public MqttBroker() {
	}

	public void init() throws Exception {
		BrokerService bs = new BrokerService();
		bs.setBrokerName("HexapodBroker");
		bs.addConnector("mqtt://0.0.0.0:1883");
		bs.start();
	}

	public static void main(String[] args) throws Exception {
		MqttBroker r = new MqttBroker();
		r.init();
	}
}
