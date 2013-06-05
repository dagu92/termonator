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
    bool addController(int floor, string door, utils::Controller* proxy);
    bool turnOnHeating(int floor, string door);
    bool turnOffHeating(int floor, string door);
    void changeTemperature(int floor, string door, double temperature);
    bool getHeatingStatus(int floor, string door);
    double getHeatingConsumption(int floor, string door);
  };
  interface DataBase {

  };
};
#endif
