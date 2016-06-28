/*
 * Mqtt.cpp
 *
 *  Created on: 22.06.2016
 *      Author: Helios
 */

#include <iostream>
#include <stdlib.h>
#include <unistd.h>
#include <cstring>
#include "Mqtt.h"

Mqtt::Mqtt() :
		mosquittopp(), buffer(0) {
	mosqpp::lib_init();
	connect("192.168.2.122");
	std::cout << "connected to mqtt 192.168.2.122 \n";
	//loop_start();
}

Mqtt::~Mqtt() {
	//loop_stop();
	mosqpp::lib_cleanup();
	if (buffer) {
		free(buffer);
	}
}

bool Mqtt::send_message(const std::string topic, const std::string message) {
	int ret = publish(NULL, topic.c_str(), strlen(message.c_str()),
			message.c_str(), 0, false);
	usleep(1000);
	return (ret == MOSQ_ERR_SUCCESS);
}

bool Mqtt::send_message(const ch::sharpsoft::hexapod::transfer::IMU *imuData) {
	int size = imuData->ByteSize();
	if (!buffer) {
		buffer = malloc(size * 2);
	}
	imuData->SerializeToArray(buffer, size);
	int ret = publish(NULL, "Sensor/IMU", size, buffer, 0, false);
	usleep(1000);
	return (ret == MOSQ_ERR_SUCCESS);
}
void Mqtt::on_message(const struct mosquitto_message *message) {
	std::cout << message->payload;
	callbacks[std::string(message->topic)](
			static_cast<char*>(message->payload));
}

void Mqtt::on_subscribe(int mid, int qos_count, const int* granted_qos) {
	std::cout << "on_subscribe " << mid << " " << qos_count << " "
			<< granted_qos << "\n";
}

void Mqtt::subscribeTopic(const std::string topic,
		std::function<void(const char*)> callback) {
	subscribe(NULL, topic.c_str(), 0);
	callbacks[topic] = callback;
}
