#ifndef TERMONATOR_ICE
#define TERMONATOR_ICE
module utils {
  exception InvalidSecretException {
    
  };
  interface Controller {
    bool heaterOn (string secret) throws InvalidSecretException;
    bool heaterOff (string secret) throws InvalidSecretException;
    bool setTemperature (string secret, double temperature) throws InvalidSecretException;
    bool getStatus (string secret) throws InvalidSecretException;
    double getConsumption ();
  };	
  interface Boiler {
    bool turnOn();
    bool turnOff();
    bool getStatus();
    bool addController(int floor, string door, utils::Controller* proxy) throws InvalidSecretException;
    bool turnOnHeating(string secret, int floor, string door) throws InvalidSecretException;
    bool turnOffHeating(string secret, int floor, string door) throws InvalidSecretException;
    void changeTemperature(string secret, int floor, string door, double temperature) throws InvalidSecretException;
    bool getHeatingStatus(string secret, int floor, string door) throws InvalidSecretException;
    double getHeatingConsumption(int floor, string door);
  };
  interface DataBase {

  };
};
#endif
