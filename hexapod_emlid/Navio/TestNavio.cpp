#include <termios.h>
#include <iostream>
#include <stdlib.h>
#include <unistd.h>
#include <mosquittopp.h>
#include "NavioLib/MPU9250.h"
#include "NavioLib/MS5611.h"
#include "NavioLib/Util.h"
#include <functional>
#include <string>
#include "Mqtt.h"
using namespace std;

void callback(std::string msg) {
	std::cout << msg << endl;
}
int main11() {
	if (check_apm()) {
		return 1;
	}
	//-------------------------------------------------------------------------
	MPU9250 imu;
	imu.initialize();
	MS5611 barometer;
	barometer.initialize();

	float ax, ay, az, gx, gy, gz, mx, my, mz;

	//-------------------------------------------------------------------------

	while (1) {
		imu.getMotion9(&ax, &ay, &az, &gx, &gy, &gz, &mx, &my, &mz);
		printf("Acc: %+7.3f %+7.3f %+7.3f  ", ax, ay, az);
		printf("Gyr: %+8.3f %+8.3f %+8.3f  ", gx, gy, gz);
		printf("Mag: %+7.3f %+7.3f %+7.3f\n", mx, my, mz);
		barometer.refreshPressure();
		usleep(10000); // Waiting for pressure data ready
		barometer.readPressure();

		barometer.refreshTemperature();
		usleep(10000); // Waiting for temperature data ready
		barometer.readTemperature();

		barometer.calculatePressureAndTemperature();

		printf("Temperature(C): %f Pressure(millibar): %f\n",
				barometer.getTemperature(), barometer.getPressure());
		usleep(5000);
		break;
	}
	Mqtt client;
	client.subscribeTopic("TestIn", &callback);
	while (1) {
		usleep(10000);
		client.send_message("Test/Topic", "TestMsg");
	}

	struct termios servos_tio;
	memset(&servos_tio, 0, sizeof(servos_tio));
	servos_tio.c_iflag = 0;
	servos_tio.c_oflag = 0;
	servos_tio.c_cflag = CS8 | CREAD | CLOCAL;
	servos_tio.c_lflag = 0;
	servos_tio.c_cc[VMIN] = 1;
	servos_tio.c_cc[VTIME] = 5;
	int servos_tty_fd = open("/dev/ttyUSB0", O_RDWR | O_NONBLOCK);

	if (servos_tty_fd <= 0) {
		printf("open(\"/dev/ttyUSB0\", O_RDWR | O_NONBLOCK)==0");
		close(servos_tty_fd);
		return 1;
	}
	if (cfsetospeed(&servos_tio, B115200) != 0) { // 115200 baud = B115200
		printf("cfsetospeed(&tio, B115200)!= 0");
		close(servos_tty_fd);
		return 1;
	}
	if (cfsetispeed(&servos_tio, B115200) != 0) { // 115200 baud = B115200
		printf("cfsetispeed(&tio, B115200)!= 0");
		close(servos_tty_fd);
		return 1;
	}
	if (tcsetattr(servos_tty_fd, TCSANOW, &servos_tio) != 0) {
		printf("tcsetattr(tty_fd, TCSANOW, &tio)!= 0");
		close(servos_tty_fd);
		return 1;
	}
	unsigned char servo_char;
	//#<channel 0-31>P<pulswidth 500-2500>[S<movement speed in uS per second>]...#
	char cmd[] =
			"#0P1500#1P1500#2P1500#3P1500#4P1500#5P1500#6P1500#7P1500#8P1500#9P1500#10P1500#11P1500#12P1500#13P1500#14P1500#15P1500#16P1500#17P1500#18P1500#19P1500#20P1500#21P1500#22P1500#23P1500#24P1500#25P1500#26P1500#27P1500#28P1500#29P1500#30P1500#31P1500\r\n";
	sleep(2);
	while (read(servos_tty_fd, &servo_char, 1) > 0 && servo_char != 0) {
		write(STDOUT_FILENO, &servo_char, 1); // if new data is available on the serial port, print it out
	}
	write(servos_tty_fd, cmd, sizeof(cmd));
	sleep(2);
	while (read(servos_tty_fd, &servo_char, 1) > 0 && servo_char != 0) {
		write(STDOUT_FILENO, &servo_char, 1); // if new data is available on the serial port, print it out
	}
}
