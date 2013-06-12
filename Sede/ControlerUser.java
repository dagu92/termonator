import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import utils.BoilerPrx;
import utils.FailureIceException;
import utils.InvalidSecretException;
import utils.ItemNotFoundException;

/**
 * @brief Clase que ofrece las funcionalidades del usuario
 */
public class ControlerUser {
	private String _neighbourhood;
	private int _portal;
	private int _floor;
	private String _door;
	private DataBaseI _takeBoilers;
  private ArrayList<Boiler> _boilerList;
  private BoilerPrx _boilerProxy;
  private String _secret;
  private double _setTemperature, _temperatureStatus;
  private boolean _status;
	
  public ControlerUser(DataBaseI dbI) {
  	this._takeBoilers = dbI;
  }

	/**
	 * @brief Función que se utiliza para comprobar el estado de la calefacción, de la casa
	 * del usuario cliente que realizó el login previamente. Aquí el usuario deberá introducir
	 * la secret que ha fijado en su controlador mediante la funcion takeSecret.
	 * Existen dos excepciones dentro de la función una si la secret introducida es incorrecta y otra si no
	 * es posible la conexión con su controlador.
	 * Al final de la función se muestra al usuario el estado de la calefacciçon
	 * @param u_name nombre del usuario que nos servirá para encontrar los datos del controlador del
	 * usuario dentro de la base de datos. Para ello se ejecuta la función takeFromDataBase.
	 */
	public void heaterStatus(String u_name){
		takeFromDataBase(u_name);
    takeBoilerPrx();
    takeSecret();
    try {
     _status = _boilerProxy.getHeatingStatus(_secret, _floor, _door);
    } catch (InvalidSecretException e) {
      System.out.println("INCORRECT secret");
      return;
    } catch (ItemNotFoundException e) {
      System.out.println("Cannot Connect to the House. Put in contact with" +
          "us");
      return;
    } catch (FailureIceException e) {
	    return;
    }
		if(_status == true){
		  try {
        _temperatureStatus = _boilerProxy.getHeatingTemperature(
            _secret, _floor, _door);
      } catch (InvalidSecretException e) {
        System.out.println("INCORRECT secret");
      } catch (ItemNotFoundException e) {
        System.out.println("Cannot Connect to the House. Put in contact with" +
            "us");
      } catch (FailureIceException e) {
	      return;
      }
			
		  System.out.println("");
		  System.out.println("Your heating is ON");
		  System.out.println("Your heating is established on "+_temperatureStatus+
		      "ºC");
			}
			else {
				System.out.println("");
			  System.out.println("Your heating is OFF");
			}
			  

	}
	
	/**
	 * @brief Función que se utiliza para modificar la temperatura de la calefacción, de la casa
	 * del usuario cliente que realizó el login previamente. Aquí el usuario deberá introducir
	 * la secret que ha fijado en su controlador mediante la funcion takeSecret y la nueva temperatura que desea fijar.
	 * Existen dos excepciones dentro de la función una si la secret introducida es incorrecta y otra si no
	 * es posible la conexión con su controlador.
	 * Al final de la función se muestra al usuario la nueva temperatura que se ha fijado.
	 * @param u_name nombre del usuario que nos servirá para encontrar los datos del controlador del
	 * usuario dentro de la base de datos. Para ello se ejecuta la función takeFromDataBase.
	 */
	public void modifyTemperature(String u_name){
	  takeFromDataBase(u_name);
    takeBoilerPrx();
    takeSecret();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Insert the temperature (ºC)");
      try {
        _setTemperature = Double.valueOf(br.readLine());
      } catch (IOException e) {
        System.out.println("ERROR taking input data");
        return;
      } 
    try {
      _boilerProxy.changeTemperature(_secret, _floor, _door, _setTemperature);
    } catch (InvalidSecretException e) {
      System.out.println("INCORRECT secret");
      return;
    } catch (ItemNotFoundException e) {
      System.out.println("Cannot Connect to the House. Put in contact with" +
          "us");
      return;
    } catch (FailureIceException e) {
	    return;
    }
		System.out.println("");
    System.out.println("Your heating temperature is now established on " 
        +_setTemperature+"ºC");
	}
	
	
	/**
	 * @brief Función que se utiliza para encender la calefacción, de la casa
	 * del usuario cliente que realizó el login previamente. Aquí el usuario deberá introducir
	 * la secret que ha fijado en su controlador mediante la funcion takeSecret.
	 * Existen dos excepciones dentro de la función una si la secret introducida es incorrecta y otra si no
	 * es posible la conexión con su controlador.
	 * @param u_name nombre del usuario que nos servirá para encontrar los datos del controlador del
	 * usuario dentro de la base de datos. Para ello se ejecuta la función takeFromDataBase.
	 */
	public void switchON(String u_name){
	  takeFromDataBase(u_name);
    takeBoilerPrx();
    takeSecret();
		System.out.println("");

    try {
      if(! _boilerProxy.turnOnHeating(_secret, _floor, _door)){
        System.out.println("Your heating is already ON");
      }
      else
        System.out.println("Your heating change to ON");
    } catch (InvalidSecretException e) {
      System.out.println("INCORRECT secret");
      return;
    } catch (ItemNotFoundException e) {
      System.out.println("Cannot Connect to the House. Put in contact with" +
          "us");
      return;
    } catch (FailureIceException e) {
	    return;
    }

	}
	
	/**
	 * @brief Función que se utiliza para apagar la calefacción, de la casa
	 * del usuario cliente que realizó el login previamente. Aquí el usuario deberá introducir
	 * la secret que ha fijado en su controlador mediante la funcion takeSecret.
	 * Existen dos excepciones dentro de la función una si la secret introducida es incorrecta y otra si no
	 * es posible la conexión con su controlador.
	 * @param u_name nombre del usuario que nos servirá para encontrar los datos del controlador del
	 * usuario dentro de la base de datos. Para ello se ejecuta la función takeFromDataBase.
	 */
	public void switchOFF(String u_name){
	  takeFromDataBase(u_name);
    takeBoilerPrx();
    takeSecret();
		System.out.println("");

    try {
      if(! _boilerProxy.turnOffHeating(_secret, _floor, _door)){
        System.out.println("Your heating is already OFF");
      }
      else
        System.out.println("Your heating change to OFF");
    } catch (InvalidSecretException e) {
      System.out.println("INCORRECT secret");
      return;
    } catch (ItemNotFoundException e) {
      System.out.println("Cannot Connect to the House. Put in contact with" +
          "us");
      return;
    } catch (FailureIceException e) {
	    return;
    }
	}
	
	/**
	 * @brief Función que se utiliza para coger los datos del controlador de la casa
	 * del usuario cliente que realizó el login previamente. Aquí se realiza una conexión
	 * a la base de datos.
	 * @param u_name nombre del usuario que nos servirá para encontrar los datos del controlador del
	 * usuario dentro de la base de datos. Mediante este parametro realizaremos la query correspondiente
	 * a la base de datos para que todos los datos del controlador de este usuario sean cargados y podamos
	 * establecer la comunicación con el mismo mediante ICE.
	 */
	public void takeFromDataBase(String u_name){
	  
	  Connection conn = null;
    String url = "jdbc:mysql://localhost:3306/";
    String dbName = "Termonator";
    String driver = "com.mysql.jdbc.Driver";
    String userName = "Termonator"; 
    String password = "termonator";
    
    try{
      Class.forName(driver).newInstance();
      conn = DriverManager.getConnection(url+dbName,userName,password);
      try {
          PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM Users WHERE u_name = ?");
          prepStmt.setString(1, u_name);
          ResultSet resultset = prepStmt.executeQuery();
          while(resultset.next()){
            if(resultset.getString("u_name").equals(u_name)){
            _neighbourhood = resultset.getString("Neighbourhood");
            _portal = Integer.valueOf(resultset.getString("Portal"));
            _floor = Integer.valueOf(resultset.getString("Floor"));
            _door = resultset.getString("Door");
          }
        }
      } catch (SQLException e1) { 
      	System.out.println("ERROR executing query");
      	return;
      	}
    }catch(Exception e){
    	
    	System.out.println("ERROR on the DataBase Connection");
    	return;
    }
      try {
        conn.close();
    }catch (SQLException e) {
      System.out.println("ERROR closing DataBase connection");
      return;
    }
	}
	
	/**
	 * @brief Función que se utiliza para coger el proxy del controlador central. 
	 * Se necesita este proxy puesto que el controlador central de la vecindad del usuario será
	 * el encargado de establecer la conexión con el controlador de la vivienda correspondiente.
	 */
	public void takeBoilerPrx (){
	  _boilerList = _takeBoilers.getBoilerList();
	  for(Boiler item: _boilerList){
	    if(item.getStreet().equals(_neighbourhood) && item.getPortal() == _portal){
	      _boilerProxy = item.getBoiler();
	    }
	  }
	}
	
	/**
	 * @brief Función que se utiliza para coger pedir al usuario que introduzca la secret
	 * correspondiente que posteriormente será necesaria para establecer la conexión con el controlador de 
	 * su vivienda.
	 */
	public void takeSecret(){
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	  System.out.println("Insert your Secret");
	  try {
      _secret = br.readLine();
    } catch (IOException e) {
      System.out.println("ERROR taking input data");
      return;
    }
	}
}
