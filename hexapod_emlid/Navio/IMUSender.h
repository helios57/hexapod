/*
 * IMUSender.h
 *
 *  Created on: 26.06.2016
 *      Author: Helios
 */

#ifndef IMUSENDER_H_
#define IMUSENDER_H_

#include "Mqtt.h"
#include "NavioLib/MPU9250.h"

class IMUSender {
private:
	//long long lastSent;
	Mqtt *mqtt;
	MPU9250 *imu;
	ch::sharpsoft::hexapod::transfer::IMU *imuData;
public:
	IMUSender(Mqtt *mqtt);
	virtual ~IMUSender();
	void start();
	void loop();
};

#endif /* IMUSENDER_H_ */
