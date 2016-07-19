/*
 * IMUSender.cpp
 *
 *  Created on: 26.06.2016
 *      Author: Helios
 */

#include "IMUSender.h"

#include <iostream>
#include <stdlib.h>
#include <unistd.h>
#include "NavioLib/Util.h"
#include <string>
#include <chrono>
#include <thread>

using namespace std::chrono;

IMUSender::IMUSender(Mqtt *mqtt) :
		mqtt(mqtt) {
	imuData = new ch::sharpsoft::hexapod::transfer::IMU();
	imu = new MPU9250();
}

IMUSender::~IMUSender() {
}

void IMUSender::start() {
	imu->initialize();
}
void IMUSender::loop() {
	milliseconds now = duration_cast<milliseconds>(
			system_clock::now().time_since_epoch());
	long long nowAsMillis = now.count();
	//if (lastSent + 9 < nowAsMillis) {
	imu->getMotion9(imuData);
	imuData->set_timestamp(nowAsMillis);
	mqtt->send_message(imuData);
	//lastSent = nowAsMillis;
	//} else {
	//	long long int toSleep = lastSent + 9 - 2 - nowAsMillis;
	//	if (toSleep > 0) {
	//		std::this_thread::sleep_for(std::chrono::milliseconds(toSleep));
	//	}
//}
}
