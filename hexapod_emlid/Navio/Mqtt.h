/*
 * Mqtt.h
 *
 *  Created on: 22.06.2016
 *      Author: Helios
 */

#ifndef MQTT_H_
#define MQTT_H_
#include <mosquittopp.h>
#include <functional>
#include <string>
#include <map>
#include "transfer/cpp/api.pb.h"

class Mqtt: public mosqpp::mosquittopp {
private:
	std::map<std::string, std::function<void(const char*)>> callbacks;
	//void on_connect(int rc);
	//void on_disconnect(int rc);
//void on_publish(int mid);
	void on_message(const struct mosquitto_message *message);
	void on_subscribe(int mid, int qos_count, const int *granted_qos);
//void on_unsubscribe(int mid);
//void on_log(int level, const char *str);
//void on_error();
	void *buffer;
public:
	Mqtt();
	virtual ~Mqtt();
	bool send_message(const std::string topic, const std::string message);
	bool send_message(const ch::sharpsoft::hexapod::transfer::IMU *imuData);
	void subscribeTopic(const std::string topic,
			std::function<void(const char*)> callback);
};

#endif /* MQTT_H_ */
