
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.NumberFormatException;

public class Home extends Thread {
    
    private float _roomTemperature = 20;
    private float _consumption = 0;

    private float _temperature;
    private Object _secret;
    private boolean _state; //0 for OFF, 1 for ON

    protected BufferedReader br;

    public Home () {
	_roomTemperature = 20;
	_temperature = 0;
	_secret = new Object();
	_state = false;
	br = new BufferedReader (new InputStreamReader(System.in));
    }

    public void homeDispatcher () {
	
	
    }


    public int getMenuChoice () {
	int userChoice = 0;
	
	printMenu ();
	try {
	    userChoice = Integer.valueOf(br.readLine()).intValue();
	} catch(IOException | NumberFormatException ex) {
	    System.err.println("Error reading number");
	}
	return (isMenuChoiceOk(userChoice))?menuChoice:getMenuChoice();
    }
    
    public void printMenu () {
	System.out.println("1.- Turn on the heater");
	System.out.println("2.- Turn off the heater");
	System.out.println("3.- Change target temperature");
	System.out.println("4.- Update secret password");
	System.out.println("5.- Visualize room temperature");
	System.out.print("Your choice --> ");
    }
    
    public boolean isChoiceOk (int choice) {
	return (choice > 0 && choice < 6)? true: false;
    }

    public boolean setHeaterOn () {
	try {
	    if(!_state) {
		_state = true;
		//start consumption
	    }
	    return true;
	} catch(Exception exception) {
	    System.err.println("Error while setting off the heater");
	    return false;
	}
    }
    
    public boolean setHeaterOff () {
	try {
	    if(_state) {
		_state = false;
		//stop consumption
		//save consumption
	    }
	    return true;
	} catch(Exception exception) {
	    System.err.println("Error while setting off the heater");
	    return false;
	}

    }
    
    

    public boolean getState () {
	return _state;
    }

    
}
