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

  public void addController(ControladorPrx proxy, Current __current) {
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
}
