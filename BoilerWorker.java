import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import utils.BoilerPrx;
/*1 --> See consumption
 * 2 --> Switch On
 * 3 --> ShutDown
 */
import utils.ItemNotFoundException;

public class BoilerWorker {
  
  private DataBaseI _takeBoilers;
  private ArrayList<Boiler> _BoilerList;
	private int _portal, _floor;
	private String _street, _door;
  public BoilerWorker(DataBaseI dbI) {
  	this._takeBoilers=dbI;
  }
  public BoilerWorker(DataBaseI dbI, int portal, int floor, String street, String door) {
  	this._takeBoilers=dbI;
  	this._floor = floor;
  	this._door = door;
  	this._portal = portal;
  	this._street = street;
  }

	/**
	 * @brief Función en la que se piden los datos de la vecindad en la que el trabajador
	 * desea realizar una de las opciones disponibles. Una vez introducidos los datos se ejecuta
	 * la función testNeighbourhood que comprueba que los datos son correctos.
	 * @param function especifica la función que se ejecutara despues de introducir los 
	 * datos de la vecindad. Los valores pueden ser los siguientes:
	 * function = 1 --> ver consumo de una vivienda
	 * function = 2 --> encender caldera de la vecindad
	 * function = 3 --> apagar caldera de la vecindad 
	 * @param u_name 
	 */
	public void insertNeighbourhood(int function){
	  _street = "";
	  _portal = 0;
	  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Insert the street name of the neighbourhood");
		try {
      _street = br.readLine();
      System.out.println("Insert the portal of neighbourhood");
      _portal = Integer.valueOf(br.readLine());
    } catch (IOException e) {
      System.out.println("ERROR Inserting DATA");
    }
		if(!testNeighbourhood(function)){
		  System.out.println("Invalid Street or Portal");
		}
		
	}
	
	/**
	 * @brief Función encargada de comprobar que los datos que se han introducido
	 * existen o son correctos. Si los datos son correctos se procederá a la conexión 
	 * con el controlador central de la vecindad. 
	 * @param street especifica el nombre de la calle de la vecindad, necesario para encontrar
	 * el proxy del controlador central
	 * @param portal especifica el numero de portal de la vecindad, necesario para encontrar el 
	 * proxy del contralador.
	 * @param function especifica la función que se ejecutara despues de comprobar que los datos
	 * existen en nuestro sistema. Los valores pueden ser los siguientes:
	 * function = 1 --> ver consumo de una vivienda
	 * function = 2 --> encender caldera de la vecindad
	 * function = 3 --> apagar caldera de la vecindad 
	 * @param u_name 
	 */
	public boolean testNeighbourhood(int function){
	  _BoilerList = _takeBoilers.getBoilerList();
	  for(Boiler item: _BoilerList){
	    if(item.getStreet().equals(_street) && item.getPortal() == _portal){
	      connectToController(item.getBoiler(), function);
	      return true;
	    }
	  }
    return false;
	}
	
	/**
	 * @brief Función que se ejecutará tras comprobar que los datos introducidos son
	 * correctos. Aquí hay un switch case donde se ejecutarán las diferentes funciones 
	 * dependiendo del valor del parametro function.
	 * @param function especifica la función que se ejecutara. Los valores pueden ser los siguientes:
	 * function = 1 --> ver consumo de una vivienda- Se ejectua la función connectToHome y se le pasa
	 * el proxy del controlador central de la vecindad.
	 * function = 2 --> encender caldera de la vecindad. Se ejecuta la función turnOn mediante ICE.
	 * function = 3 --> apagar caldera de la vecindad. Se ejecuta la función switchOff mediante ICE.
	 * @param u_name 
	 */
	public void connectToController(BoilerPrx boilerPrx, int function){
	  switch(function){
	    case 1: connectToHome(boilerPrx);
	      break;
	    case 2:
	    	if(!boilerPrx.turnOn()) System.out.println("ERROR Switching ON");
	            else System.out.println("Now Boiler is ON");
	      break;
	    case 3: if(!boilerPrx.turnOff()) System.out.println("ERROR Switching OFF");
              else System.out.println("Now Boiler is OFF");
	      break;
	  }
	}
	
	/**
	 * @brief Función en la que se piden los datos de la casa a la que se desea conectar
	 * para comprobar el consumo. Una vez introducidos los datos se ejecutará la función
	 * getHeatingCOnsumption mediante ICE en la que se mandarán los datos de la casa
	 * que se han introducidos para que el controlador central de la vecindad pueda realizar
	 * la conexión a la misma.
	 * @param boilerPrx es el proxy del controlador central de la vecindad en la que queremos
	 * comprobar el consumo de una casa concreta. Este parametro es necesario para establecer
	 * la conexión mediante ICE.
	 */
	public void connectToHome(BoilerPrx boilerPrx){
	  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	  _floor = 0;
	  _door = "";
	  double consumption = 0;
	    
	  System.out.println("Insert the floor of the house");
    try {
      _floor = Integer.valueOf(br.readLine());
      System.out.println("Insert the door of the floor (Ex. 'B'");
      _door = br.readLine();
    } catch (IOException e) {
      System.out.println("ERROR Inserting DATA");
    }
    try {
    	System.out.println("Antes de pedir");
     consumption = boilerPrx.getHeatingConsumption(_floor, _door);
   	System.out.println("Despues de pedir");

     String u_name = "";
     u_name = getUsername(u_name);
     saveConsumption(consumption, u_name);
    } catch (ItemNotFoundException e) {
      System.out.println("The house that you're consulting doesn't exist or" +
      		"the house is now unvailable");
      		
    }
   }
	
	public void saveConsumption(double consumption, String u_name){
		Connection conn = null;
		boolean exist = false;
	  String url = "jdbc:mysql://localhost:3306/";
	  String dbName = "Termonator";
	  String driver = "com.mysql.jdbc.Driver";
	  String userName = "Termonator"; 
	  String password = "termonator";
	  try{
		  Class.forName(driver).newInstance();
		  conn = DriverManager.getConnection(url+dbName,userName,password);
		  try {
			  PreparedStatement prepStmt = conn.prepareStatement(
			  	    "UPDATE Users SET Consumption = ? WHERE u_name = ?");
			  prepStmt.setDouble(1, consumption);
			  prepStmt.setString(2, u_name);
		  	prepStmt.executeUpdate();	
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
	
	public String getUsername(String u_name){
		
		  Connection conn = null;
		  String url = "jdbc:mysql://localhost:3306/";
		  String dbName = "Termonator";
		  String driver = "com.mysql.jdbc.Driver";
		  String userName = "Termonator"; 
		  String password = "termonator";
		  try{
			  Class.forName(driver).newInstance();
			  conn = DriverManager.getConnection(url+dbName,userName,password);
			  PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM Users WHERE Neighbourhood = ? " +
			  		"AND Portal = ? AND Floor = ? AND Door = ?");		
			  prepStmt.setString(1,_street);
			  prepStmt.setInt(2, _portal);
			  prepStmt.setInt(3,_floor);
			  prepStmt.setString(4,_door);
			  try {
				  	ResultSet resultset = prepStmt.executeQuery();
				  	while(resultset.next()){
				  		
				  				u_name = resultset.getString("u_name");
				  				return u_name;
							
					}
						
					
				} catch (SQLException e1) { System.out.println("ERROR executing query");}
		  }catch(Exception e){System.out.println("ERROR on the DataBase Connection");}
		  try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("ERROR closing DataBase connection");
		}
		return u_name;
	}
}
