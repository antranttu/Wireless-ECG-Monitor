#include <SPI.h>
#include <SoftwareSerial.h>
#include <nRF24L01.h>
#include <RF24.h>

//Define variables to hold incoming ECG data
int ECG_1;
int ECG_2;
int Synchronized;
int average = 0;

RF24 radio(7, 8);

//Define pipes address for the transmitters and receiver
const uint64_t pipes[3] = { 0xF0F0F0F0E1LL, 0xF0F0F0F0E2LL, 0xF0F0F0F0E3LL };

void setup(void) {
  
Serial.begin(57600);

radio.begin();

//Set communicating channel
radio.setChannel(108);

//Data transfer rate
radio.setDataRate(RF24_250KBPS);

//Read the 2 ECG signals from specific pipe addresses
radio.openReadingPipe(1, pipes[1]);
radio.openReadingPipe(2, pipes[2]);

radio.startListening();

}

void loop(void)
{
if ( radio.available() )
{

//Start reading incoming ECG 1 data 
radio.read(&ECG_1, sizeof(ECG_1));

delay(10);

//Start reading incoming ECG 2 data
radio.read(&ECG_2, sizeof(ECG_2));

//Create a "Synchronized variable, add the 2 ECG signals
//to verify the synchronization works
Synchronized = ECG_1;// + ECG_2;

//Send the Synchronized signal to Bluetooth using Serial Data
Serial.print(Synchronized);
Serial.print(",");
Serial.println(average);
}
}
