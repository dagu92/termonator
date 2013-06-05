
import utils._DeviceIDisp;

import Ice.Current;

public class HomeServant extends _DeviceIDisp {

	private Home _home;
	
	public HomeServant () { //falta el prx de Controlador -> ice de Dani
		_home = new Home();
		_home.start();
	}
	
	
	public boolean heaterOn(Current __current) {
		return _home.setHeaterOn();
	}

	public boolean heaterOff(Current __current) {
		return _home.setHeaterOff();
	}

	public boolean setTemperature(double temperature, Current __current) {
		return _home.updateTemperature(temperature);
		
	}

	public boolean getStatus(Current __current) {
		return _home.getStatus();
	}

	public double getConsumption(Current __current) {
		
		return 0;
	}

}
