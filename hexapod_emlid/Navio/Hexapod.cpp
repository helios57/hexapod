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
	std::cout << "main 0 \n";
	hp = new Hexapod();
	std::cout << "main 1 \n";
	hp->init();
	std::cout << "main 2 \n";
	std::thread tMqtt(loopMqtt);
	std::cout << "main 3 \n";
	std::thread tIMU(loopImu);
	tMqtt.join();
	std::cout << "main 4 \n";
	tIMU.join();
}

Hexapod::Hexapod() {
	std::cout << "Hexapod 0 \n";
	mqtt = new Mqtt();
	std::cout << "Hexapod 1 \n";
	imuSender = new IMUSender(mqtt);
	std::cout << "Hexapod 2 \n";
	servoController = new ServoController();
	std::cout << "Hexapod 3 \n";
	servosOnline = false;
}

Hexapod::~Hexapod() {
	delete servoController;
	delete imuSender;
	delete mqtt;
}

void Hexapod::init() {
	imuSender->start();
	std::cout << "Hexapod::init 0 \n";
	servosOnline = servoController->init();
	std::cout << "Hexapod::init 1 \n";
	mqtt->subscribeTopic("Servo/Pos", onServoMessageStatic);
	std::cout << "Hexapod::init 2 \n";
	//sleep(2);
	std::cout << "Hexapod::init 3 \n";
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
