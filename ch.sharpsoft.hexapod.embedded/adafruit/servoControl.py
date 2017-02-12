from __future__ import division
import time
import sys

# Import the PCA9685 module.
import Adafruit_PCA9685

#inport paho mqtt
import paho.mqtt.client as mqtt

# Uncomment to enable debug output.
#import logging
#logging.basicConfig(level=logging.DEBUG)

# Initialise the PCA9685 using the default address (0x40).
pwm = Adafruit_PCA9685.PCA9685()

# Alternatively specify a different address and/or bus:
#pwm = Adafruit_PCA9685.PCA9685(address=0x41, busnum=2)

# Configure min and max servo pulse lengths
servo_min = 150  # Min pulse length out of 4096
servo_max = 550  # Max pulse length out of 4096

# Helper function to make setting a servo pulse width simpler.
def set_servo_pulse(channel, pulse):
    pulse_length = 1000000    # 1,000,000 us per second
    pulse_length //= 60       # 60 Hz
    print('{0}us per period'.format(pulse_length))
    pulse_length //= 4096     # 12 bits of resolution
    print('{0}us per bit'.format(pulse_length))
    pulse *= 1000
    pulse //= pulse_length
    pwm.set_pwm(channel, 0, pulse)

#print('Moving servo on channel 0, press Ctrl-C to quit...')
#while True:
    # Move servo on channel O between extremes.
#    pwm.set_pwm(0, 0, servo_min)
#    time.sleep(1)
#    pwm.set_pwm(0, 0, servo_max)
#    time.sleep(1)
#    pwm.set_pwm(1,0,300)

# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, rc):
    print("Connected with result code "+str(rc))
    client.subscribe("SERVO/OUT")
    # Subscribing in on_connect() means that if we lose the connection and
    # reconnect then subscriptions will be renewed.

# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    print(msg.topic+" "+str(msg.payload))
    try:
        commando = msg.payload.split(":")
        channel = int(commando[0])
        valueToSet = int(commando[1])
        if (channel<0 or channel>16 or valueToSet<servo_min or valueToSet>servo_max):
            return
        pwm.set_pwm(channel,0,valueToSet)
    except (ValueError,IndexError):
        pass

#main
def main_entry_point():
    # Set frequency to 60hz, good for servos.
    pwm.set_pwm_freq(60)
    client = mqtt.Client()
    client.on_connect = on_connect
    client.on_message = on_message
    client.connect("localhost", 1883, 60)
    client.loop_forever()

if __name__ == '__main__':
    main_entry_point()
