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
		 void addController(utils::Controller* proxy);
  };
};

#endif
