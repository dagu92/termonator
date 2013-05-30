module Home {
       interface DeviceI {
       		 bool heaterOn ();
		 bool heaterOff ();
		 bool setTemperature (float temperature);
		 bool getState ();
		 float getConsumption ();
       };	
};