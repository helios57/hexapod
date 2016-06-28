/*
 * ServoController.h
 *
 *  Created on: 26.06.2016
 *      Author: Helios
 */

#ifndef SERVOCONTROLLER_H_
#define SERVOCONTROLLER_H_
#include <termios.h>
#include <stdlib.h>

class ServoController {
private:
	struct termios servos_tio;
	int servos_tty_fd;
public:
	ServoController();
	virtual ~ServoController();
	bool init();
	void send(const char * buffer, size_t size);
};

#endif /* SERVOCONTROLLER_H_ */
