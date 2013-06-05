#ifndef TERMONATOR_ICE
#define TERMONATOR_ICE
module utils {
  interface Controller {
    bool heaterOn ();
    bool heaterOff ();
    bool setTemperature (double temperature);
    bool getStatus ();
    double getConsumption ();
  }; 
  interface Boiler {
    bool turnOn();
    bool turnOff();
    bool getStatus();
    bool addController(string secret, int floor, string door, utils::Controller* proxy);
    bool turnOnHeating(string secret, int floor, string door);
    bool turnOffHeating(string secret, int floor, string door);
    void changeTemperature(string secret, int floor, string door, double temperature);
    bool getHeatingStatus(string secret, int floor, string door);
    double getHeatingConsumption(string secret, int floor, string door);
  };
  interface DataBase {

  };
};
#endif
