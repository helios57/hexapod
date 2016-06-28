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
	milliseconds ms = duration_cast<milliseconds>(
			system_clock::now().time_since_epoch());
	imu->getMotion9(imuData);
	imuData->set_timestamp(ms.count());
	mqtt->send_message(imuData);
}
