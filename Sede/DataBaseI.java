import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Ice.Current;
import utils.BoilerPrx;
import utils._DataBaseDisp;
import java.util.ArrayList;

/**
 * @brief Clase ofrecida al Controlador central mediante Ice para alamcenar ciertos datos.
 */
public class DataBaseI extends _DataBaseDisp {
	
	public ArrayList<Boiler> _boilerList = new ArrayList<Boiler>();
	private int _incidentCont = 0;

	/**
	 * @brief Función que ejecuta el controlador central de una vecindad, cuando este
	 * detecta que falla algo dentro de la vecindad. Está función se ejecuta mediante ICE y guardará
	 * la incidencia en la base de datos del sistema.
	 * @param incident es la descripción de la incidencia que ha ocurrida y esta se guardará en la 
	 * base de datos.
	 */
	public void saveIncident(String incident, Current __current) {
		  /*Ficherop de configuracon. Fichero ptroperties*/
		  Connection conn = null;
		  String url = "jdbc:mysql://localhost:3306/";
		  String dbName = "Termonator";
		  String driver = "com.mysql.jdbc.Driver";
		  String userName = "Termonator"; 
		  String password = "termonator";
		  _incidentCont ++;
		  try{
			  Class.forName(driver).newInstance();
			  conn = DriverManager.getConnection(url+dbName,userName,password);
			 
			  try {
			  	System.out.println("Incident "+incident);
				  PreparedStatement prepStmt = conn.prepareStatement(
				  	    "INSERT INTO Incidents (I_description) VALUES (?)");
				  prepStmt.setString(1, incident);
				  prepStmt.execute();

				} catch (SQLException e1) {
					System.out.println("ERROR executing query");
				}
			  
		  }catch(Exception e){
			  System.out.println("ERROR on the DataBase Connection");
		  }
		  try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("ERROR closing DataBase connection");
		}
	}
	
	/**
	 * @brief Función se ejecuta cuando un nuevo controlador central de una vecindad
	 * se conecta a la red. De este modo se añade a la sede y podremos comunicarnos con el.
	 * La información se guardará en un arrayList BoilerList de tipo Boiler (una clase creada
	 * para almacenar los datos).
	 * @param street se guarda la calle de la vecindad.
	 * @param portal seguarda el numero del portal de la vecindad
	 * @param proxy se guarda el proxy del controlador central de la vecindad. De este modo 
	 * la comunicación mediante ICE será posible.
	 */
	public void addBoilerController(String street, int portal, BoilerPrx proxy, 
	                                Current __current) {
		Boiler testBoiler = new Boiler(street,portal,proxy);
		boolean repeat = false;
		for(Boiler item: _boilerList){
	    if(item.getStreet().equals(street) && item.getPortal() == portal){
	    	repeat = true;
	    	break;
	    }
		}
		if (repeat == false){
			_boilerList.add(testBoiler);
		}

	}
	
	public ArrayList<Boiler> getBoilerList(){
    return _boilerList;
	}
	
	public int getIncidentCont(){
		return _incidentCont;
	}
	
	public void setIncidentCont(int incident){
		_incidentCont = incident;
	}

}
