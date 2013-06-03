import utils.*;
import Ice.Current;
import java.util.ArrayList;

public class Boiler extends _BoilerDisp {

  private boolean status;
  private ArrayList<ControladorPrx> controllerList;

  public Boiler() {
    status = false;
    controllerList = new ArrayList<ControladorPrx>();
  }

  public void addController(int floor, String door, ControladorPrx proxy,
                            Current __current) {
    if(controllerList.contains(proxy) == false) {
      controllerList.add(proxy);
    }
  }

  public boolean turnOff(Current __current) {
    if(status == false) {
      return false;
    } else {
      status = false;
      return true;
    }
  }

  public boolean turnOn(Current __current) {
    if(status == true) {
      return false;
    } else {
      status = true;
      return true;
    }
  }

  public boolean turnOffHeating(int floor, String door, Current __current) {
    for(ControladorPrx item: controllerList) {
      if(item.getFloor() == floor && item.getDoor() == door) {
        if(item.turnOff() == false) {
          return false;
        } else {
          return true;
        }
      }
    }
    return false; //Dbg: Rise an exception??
  }
  
  public boolean turnOnHeating(int floor, String door, Current __current) {
    for(ControladorPrx item: controllerList) {
      if(item.getFloor() == floor && item.getDoor() == door) {
        if(item.turnOn() == false) {
          return false;
        } else {
          return true;
        }
      }
    }
    return false; //Dbg: Rise an exception??
  }

  public void changeTemperature(int floor, String door, int temperature,
                                Current __current) {
    
  }
}
