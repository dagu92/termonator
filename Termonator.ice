#ifndef TERMONATOR_ICE
#define TERMONATOR_ICE
module utils {
  interface Controlador {
  
  };
  interface Boiler {
    bool turnOn();
    bool turnOff();
    void addController(int floor, string door, utils::Controlador* proxy);
    bool turnOnHeating(int floor, string door);
    bool turnOffHeating(int floor, string door);
    void changeTemperature(int floor, string door, int temperature);
  };
};
#endif
