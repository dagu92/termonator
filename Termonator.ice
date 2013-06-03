#ifndef TERMONATOR_ICE
#define TERMONATOR_ICE
module utils {
  interface Controlador {
  
  };
  interface Boiler {
    bool turnOn();
    bool turnOff();
    void addController(utils::Controlador* proxy);
  };
};
#endif
