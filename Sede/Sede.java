import java.sql.*;

import java.util.Scanner;

/**
 * @brief Clase principal de la aplicación en la que se realiza el login y dependiendo del 
 * mismo se carga un menú u otro.
 */
public class Sede {
	
	private static DataBaseI _dbI;

	/**
	 * @author David Gutierrez
	 * @brief Función principal del programa site server en la que se inicializa el 
	 * object adapter y se activa. También se carga el Login.
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Ice.Communicator ic = Ice.Util.initialize(args);
		Ice.ObjectAdapter adapter = ic.createObjectAdapter("ServerAdapter");
		_dbI = new DataBaseI();
		adapter.add(_dbI,ic.stringToIdentity("DataBase"));
		adapter.activate();
		while (true){
			login();
		}
	}
	/**
	 * @brief FUnción en la que se realiza el login en la aplicación. 
	 * Primero se le pide el usuario y contraseña al usurio. Estos datos se comprueban 
	 * en la base de datos mediante la función CheckLogin y dependiende de las credenciales
	 * se devolvera un log_result. 
	 * log_result = -1 --> login incorrecto
	 * log_result = 0 --> login como usuario cliente
	 * log_result = 1 --> login como trabajador de la sede.
	 */
	public static void login(){
		String uName ="", uPass="";
		int logResult = -2;
		Scanner teclado = new Scanner(System.in);
		while(logResult < 0){
		System.out.println("");
		System.out.println("Welcome to Termonator 1.0");
		System.out.println("Login");
		System.out.print("User: ");
		uName = teclado.next();
		System.out.println("");
		System.out.print("Password: ");
		uPass = teclado.next();
		System.out.println("");
		logResult = CheckLogin(uName, uPass, logResult);
		if (logResult == -1) System.out.println("Invalid Username or Password");
		}
		
		if (logResult ==1) printMenuWorker(uName);
		else PrintMenuUser(uName);
		
	}
	
	/**
	 * @brief Función que comprueba el las credenciales en la base de datos
	 * @param uName nombre de usuario introducido
	 * @param uPass contraseña que se ha introducido
	 * @return devuelve log_result que determinará el resultado del login.
	 * log_result = -1 --> login incorrecto
	 * log_result = 0 --> login como usuario cliente
	 * log_result = 1 --> login como trabajador de la sede.
	 */
	public static int CheckLogin(String uName, String uPass, int logResult){
		
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
				  	PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM Users WHERE u_name = ? AND u_pass = ?");
				  	prepStmt.setString(1, uName);
				  	prepStmt.setString(2, uPass);
				  	ResultSet resultset = prepStmt.executeQuery();
				  	while(resultset.next()){
					if(resultset.getString("u_name").equals(uName) && resultset.getString("u_pass").equals(uPass)){
							logResult = Integer.parseInt(resultset.getString("Worker_User"));
							return logResult;
						}	
					}
				} catch (SQLException e1) { System.out.println("ERROR executing query");}
		  }catch(Exception e){System.out.println("ERROR on the DataBase Connection");}
		  try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("ERROR closing DataBase connection");
		}
		  logResult = -1;
		  return logResult;
	}
	
	/**
	 * @brief Función que muestra en pantalla el menu diaponible para el usuario cliente.
	 * Esta función solo se cargará si log_result es igual a 0. Una vez se muestra
	 * el menú se ejecutará el mismo, mediate la función ExecuteMenuUser.
	 * @param uName nombre de usuario introducido que se utiliza para mostrar en el mensaje 
	 * de bienvenida.
	 * 
	 */
	public static void PrintMenuUser(String uName){
		Scanner teclado = new Scanner(System.in);
		int option = -1;
		while(option != 0){
		System.out.println("");
		System.out.println("**********************************************" +
				"**********************");
		System.out.println("Welcome "+uName+" to Termonator 1.0");
		System.out.println("1.- See heater Status");
		System.out.println("2.- Modify Heater Temperature");
		System.out.println("3.- Switch ON your heater");
		System.out.println("4.- Switch OFF your heater");
		System.out.println("0.- Exit");
		System.out.println("**********************************************" +
				"**********************");
		System.out.print("Your Option: ");
		option = teclado.nextInt();
		executeMenuUser(option,uName);
		}
	}
	
	/**
	 * @brief Función que ejecuta el menu del usuario. Es decir, aquí se llamará
	 * a las funciones necesarias dependiendo de la elección del usuario en el menú.
	 * @param option opción introducida por el usuario.
	 * @param uName nombre del usuario que nos servirá para las funciones a las que
	 * se llame.
	 */
	public static void executeMenuUser(int option, String uName){
		ControlerUser heater = new ControlerUser(_dbI);
		switch(option){
		case 1: heater.heaterStatus(uName);
			break;
		case 2: heater.modifyTemperature(uName);
			break;
		case 3: heater.switchON(uName);
			break;
		case 4: heater.switchOFF(uName);
			break;
		case 0:
			break;
		default: 
			System.out.println("Incorrect Option. Try again.");
			break;
		}
	}
	
	/**
	 * @brief Función que muestra en pantalla el menu diaponible para el usuario trabajador.
	 * Esta función solo se cargará si log_result es igual a 1. Una vez se muestra
	 * el menú se ejecutará el mismo, mediante la función ExecuteMenuWorker.
	 * @param uName nombre de usuario introducido que se utiliza para mostrar en el mensaje 
	 * de bienvenida.
	 * 
	 */
	public static void printMenuWorker(String uName){
		Scanner teclado = new Scanner(System.in);
		int option =-1;
		while (option != 0){
		System.out.println("");
		System.out.println("*************************************************" +
				"*******************");
		System.out.println("Welcome "+uName+" to Termonator 1.0");
		System.out.println("1.- See Consumption of a concrete Home");
		System.out.println("2.- Switch ON a boiler of a specific neighbourhood");
		System.out.println("3.- Switch OFF a boiler of a specific neighbourhood");
		System.out.println("0.- Exit");
		System.out.println("*************************************************" +
				"*******************");
		System.out.print("Your Option: ");
		option = teclado.nextInt();
		executeMenuWorker(option);
		}
	}
	
	/**
	 * @brief Función que ejecuta el menu del trabajador. Es decir, aquí se llamará
	 * a las funciones necesarias dependiendo de la elección del trabajdor en el menú.
	 * @param option opción introducida por el trabajdor.
	 * @param uName nombre del trabajador que nos servirá para las funciones a las que
	 * se llame
	 */
	public static void executeMenuWorker(int option){
		BoilerWorker boiler = new BoilerWorker(_dbI);
		int incidents;
		incidents = _dbI.getIncidentCont();
			System.out.println("New "+incidents+" Incidents");
			_dbI.setIncidentCont(incidents);
		int seeConsumption = 1, switchON = 2, switchOFF = 3;
		
		switch(option){
		case 1: boiler.insertNeighbourhood(seeConsumption);
			break;
		case 2: boiler.insertNeighbourhood(switchON);
			break;
		case 3: boiler.insertNeighbourhood(switchOFF);
			break;
		case 0: System.out.println("Bye");
			break;
		default: 
			System.out.println("Incorrect Option. Try again.");
			break;
		}
	}
	

}
