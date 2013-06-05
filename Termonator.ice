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
    double getConsumption (string secret) throws InvalidSecretException;
  };	
  interface Boiler {
    bool turnOn();
    bool turnOff();
    void addController(int floor, string door, utils::Controller* proxy);
    bool turnOnHeating(int floor, string door);
    bool turnOffHeating(int floor, string door);
    void changeTemperature(int floor, string door, int temperature);

  };
};

#endif
