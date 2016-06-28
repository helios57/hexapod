/*
 * Hexapod.h
 *
 *  Created on: 26.06.2016
 *      Author: Helios
 */

#ifndef HEXAPOD_H_
#define HEXAPOD_H_
#include "Mqtt.h"
#include "IMUSender.h"
#include "ServoController.h"

class Hexapod {
private:
	Mqtt * mqtt;
	IMUSender *imuSender;
	ServoController *servoController;
	bool servosOnline;
public:
	Hexapod();
	virtual ~Hexapod();
	void init();
	void onServoMessage(const char* msg);
	void static onServoMessageStatic(const char* msg);
	bool loopMqtt();
	bool loopIMU();
};

#endif /* HEXAPOD_H_ */
