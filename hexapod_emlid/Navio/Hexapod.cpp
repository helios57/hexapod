/*
 * Hexapod.cpp
 *
 *  Created on: 26.06.2016
 *      Author: Helios
 */

#include "Hexapod.h"
#include <iostream>
#include <thread>

static Hexapod * hp;
void loopMqtt() {
	while (1) {
		hp->loopMqtt();
	}
}
void loopImu() {
	while (1) {
		hp->loopIMU();
	}
}
int main() {
	hp = new Hexapod();
	hp->init();
	std::thread tMqtt(loopMqtt);
	std::thread tIMU(loopImu);
	tMqtt.join();
	tIMU.join();
}

Hexapod::Hexapod() {
	mqtt = new Mqtt();
	imuSender = new IMUSender(mqtt);
	servoController = new ServoController();
	servosOnline = false;
}

Hexapod::~Hexapod() {
	delete servoController;
	delete imuSender;
	delete mqtt;
}

void Hexapod::init() {
	imuSender->start();
	servosOnline = servoController->init();
	mqtt->subscribeTopic("Servo/Pos", onServoMessageStatic);
	sleep(2);
	std::cout << "Hexapod::init complete \n";
}

void Hexapod::onServoMessage(const char* msg) {
	if (servosOnline) {
		servoController->send(msg, strlen(msg));
	}
}

void Hexapod::onServoMessageStatic(const char* msg) {
	std::cout << msg << "\n";
	hp->onServoMessage(msg);
}

bool Hexapod::loopMqtt() {
	mqtt->loop();
	return true;
}

bool Hexapod::loopIMU() {
	imuSender->loop();
	return true;
}
