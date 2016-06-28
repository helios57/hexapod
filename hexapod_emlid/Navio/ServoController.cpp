/*
 * ServoController.cpp
 *
 *  Created on: 26.06.2016
 *      Author: Helios
 */

#include "ServoController.h"
#include <iostream>
#include <unistd.h>
#include <cstring>
#include <fcntl.h>
#include <sys/stat.h>

ServoController::ServoController() {
	servos_tty_fd = 0;
}

ServoController::~ServoController() {
	close(servos_tty_fd);
}

bool ServoController::init() {
	std::cout << "ServoController::init \n";
	const char* servoPort = "/dev/ttyUSB0";
	struct stat buffer;
	if (stat(servoPort, &buffer) != 0) {
		std::cout << servoPort;
		std::cout << " not available \n";
		return false;
	}
	memset(&servos_tio, 0, sizeof(servos_tio));
	servos_tio.c_iflag = 0;
	servos_tio.c_oflag = 0;
	servos_tio.c_cflag = CS8 | CREAD | CLOCAL;
	servos_tio.c_lflag = 0;
	servos_tio.c_cc[VMIN] = 1;
	servos_tio.c_cc[VTIME] = 5;
	servos_tty_fd = open(servoPort, O_RDWR | O_NONBLOCK);

	std::cout << "servos_tty_fd opended for /dev/ttyUSB0";
	if (servos_tty_fd <= 0) {
		std::cout << "open(\"/dev/ttyUSB0\", O_RDWR | O_NONBLOCK)==0\n";
		close(servos_tty_fd);
		return false;
	}
	if (cfsetospeed(&servos_tio, B115200) != 0) { // 115200 baud = B115200
		std::cout << "cfsetospeed(&tio, B115200)!= 0\n";
		close(servos_tty_fd);
		return false;
	}
	if (cfsetispeed(&servos_tio, B115200) != 0) { // 115200 baud = B115200
		std::cout << "cfsetispeed(&tio, B115200)!= 0\n";
		close(servos_tty_fd);
		return false;
	}
	if (tcsetattr(servos_tty_fd, TCSANOW, &servos_tio) != 0) {
		std::cout << "tcsetattr(tty_fd, TCSANOW, &tio)!= 0\n";
		close(servos_tty_fd);
		return false;
	}
	std::cout << "ServoController::init complete\n";
	return true;
}
/*
 * 	//#<channel 0-31>P<pulswidth 500-2500>[S<movement speed in uS per second>]...#
 char cmd[] =
 "#0P1500#1P1500#2P1500#3P1500#4P1500#5P1500#6P1500#7P1500#8P1500#9P1500#10P1500#11P1500#12P1500#13P1500#14P1500#15P1500#16P1500#17P1500#18P1500#19P1500#20P1500#21P1500#22P1500#23P1500#24P1500#25P1500#26P1500#27P1500#28P1500#29P1500#30P1500#31P1500\r\n";
 sleep(2);
 write(servos_tty_fd, cmd, sizeof(cmd));
 */
void ServoController::send(const char* buffer, size_t size) {
	if (servos_tty_fd) {
		write(servos_tty_fd, buffer, size);
	} else {
		std::cout << "servos offline: ";
		std::cout << buffer;
		std::cout << "\n";
	}
}
