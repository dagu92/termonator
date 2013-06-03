
public class Control {

	Ice.Communicator homeComm;
	Ice.ObjectAdapter homeObjAdapter;
	Ice.Object homeServant;
	
	public Control () {
		homeComm = Ice.Util.initialize();
		homeObjAdapter = homeComm.createObjectAdapter("");//check how its done with cfg

		homeServant = new HomeServant();
		homeObjAdapter.addWithUUID(homeServant);

	}
	
	public static void main (String[] args) {
		Control control = new Control();
		
	}
	
	
	
	
}
