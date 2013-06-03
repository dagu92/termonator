module utils {
       interface DeviceI {
		 bool heaterOn ();
		 bool heaterOff ();
		 bool setTemperature (double temperature);
		 bool getStatus ();
		 double getConsumption ();
       };	
       interface Boiler {
       
       };
};