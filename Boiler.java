import utils.BoilerPrx;


public class Boiler {
	
	private String _street;
	private int _portal;
	private BoilerPrx _boilerPrx;
	
	public Boiler(String street, int portal, BoilerPrx boilerProxy) {
		this._street = street;
		this._portal = portal;
		this._boilerPrx = boilerProxy;
	}
	public void addStreet(String street){
		this._street = street;
	}
	public void addPortal(int portal){
		this._portal = portal;
	}
	public void addBoiler(BoilerPrx boilerPrx){
		this._boilerPrx = boilerPrx;
	}
	
	public String getStreet(){
		return _street;
	}
	public int getPortal(){
		return _portal;
	}
	public BoilerPrx getBoiler(){
		return _boilerPrx;
	}
	
	

}

