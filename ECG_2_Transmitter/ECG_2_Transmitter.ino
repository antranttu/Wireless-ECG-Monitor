#include <SPI.h>
#include <SoftwareSerial.h>
#include "nRF24L01.h"
#include "RF24.h"

//Define pin for input ECG signal 1
int analogPin = A0;
unsigned long time_stamp;

//Define a variable to hold ECG signal 1
int ECG_2 = 0;

//RF module parameters definitions
RF24 radio(7, 8);

//Assign addresses to communication links between receiver and transmitters
const uint64_t pipes[3] = { 0xF0F0F0F0E1LL, 0xF0F0F0F0E2LL, 0xF0F0F0F0E3LL };

void setup(void) {
Serial.begin(57600);

//Begin transmitting process
radio.begin();

//Define Communication Channel
radio.setChannel(108);

//Set Data Transfer Rate
radio.setDataRate(RF24_250KBPS);

//Write data to data pipe using the assigned address 
radio.openWritingPipe(pipes[2]);

//Stop listening to write data
radio.stopListening();
}

void loop(void)
{
//Read in ECG signal from the pin defined earlier
ECG_2 = analogRead(analogPin);
time_stamp = millis();

//Start transmitting ECG data using RF
radio.write(&ECG_2, sizeof(ECG_2));
Serial.println(ECG_2);
delay(10);
}
