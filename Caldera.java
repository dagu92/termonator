import utils.*;
import Ice.Current;
import java.util.ArrayList;

public class Caldera extends _CalderaDisp {

  private boolean status;
  private ArrayList<ControladorPrx> listaControladores;

  public Caldera() {
    status = false;
    listaControladores = new ArrayList<ControladorPrx>();
  }

  public void anadirCaldera(ControladorPrx proxy, Current __current) {
    if(listaControladores.contains(proxy) == false) {
      listaControladores.add(proxy);
    }
  }

  public boolean apagar(Current __current) {
    if(status == false) {
      return false;
    } else {
      status = false;
      return true;
    }
  }

  public boolean encender(Current __current) {
    if(status == true) {
      return false;
    } else {
      status = true;
      return true;
    }
  }
}
