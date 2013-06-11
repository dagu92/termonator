import java.sql.*;

import java.util.Scanner;


public class Sede {

	private static DataBaseI dbI;

	/**
	 * @author David Gutierrez
	 * @brief Función principal del programa site server en la que se inicializa el 
	 * object adapter y se activa. También se carga el Login.
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Ice.Communicator ic = Ice.Util.initialize(args);
		Ice.ObjectAdapter adapter = ic.createObjectAdapter("ServerAdapter");
		dbI = new DataBaseI();
		adapter.add(dbI,ic.stringToIdentity("DataBase"));
		adapter.activate();
		while (true){
			Login();
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
	public static void Login(){
		String u_name ="", u_pass="";
		int log_result = -2;
		Scanner teclado = new Scanner(System.in);
		while(log_result < 0){
		System.out.println("");
		System.out.println("Welcome to Termonator 1.0");
		System.out.println("Login");
		System.out.print("User: ");
		u_name = teclado.next();
		System.out.println("");
		System.out.print("Password: ");
		u_pass = teclado.next();
		System.out.println("");
		log_result = CheckLogin(u_name, u_pass, log_result);
		if (log_result == -1) System.out.println("Invalid Username or Password");
		}
		
		if (log_result ==1) PrintMenuWorker(u_name);
		else PrintMenuUser(u_name);
		
	}
	
	/**
	 * @brief Función que comprueba el las credenciales en la base de datos
	 * @param u_name nombre de usuario introducido
	 * @param u_pass contraseña que se ha introducido
	 * @return devuelve log_result que determinará el resultado del login.
	 * log_result = -1 --> login incorrecto
	 * log_result = 0 --> login como usuario cliente
	 * log_result = 1 --> login como trabajador de la sede.
	 */
	public static int CheckLogin(String u_name, String u_pass, int log_result){
		
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
				  	prepStmt.setString(1, u_name);
				  	prepStmt.setString(2, u_pass);
				  	ResultSet resultset = prepStmt.executeQuery();
				  	while(resultset.next()){
					if(resultset.getString("u_name").equals(u_name) && resultset.getString("u_pass").equals(u_pass)){
							log_result = Integer.parseInt(resultset.getString("Worker_User"));
							return log_result;
						}	
					}
				} catch (SQLException e1) { System.out.println("ERROR executing query");}
		  }catch(Exception e){System.out.println("ERROR on the DataBase Connection");}
		  try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("ERROR closing DataBase connection");
		}
		  log_result = -1;
		  return log_result;
	}
	
	/**
	 * @brief Función que muestra en pantalla el menu diaponible para el usuario cliente.
	 * Esta función solo se cargará si log_result es igual a 0. Una vez se muestra
	 * el menú se ejecutará el mismo, mediate la función ExecuteMenuUser.
	 * @param u_name nombre de usuario introducido que se utiliza para mostrar en el mensaje 
	 * de bienvenida.
	 * 
	 */
	public static void PrintMenuUser(String u_name){
		Scanner teclado = new Scanner(System.in);
		int option = -1;
		while(option != 0){
		System.out.println("");
		System.out.println("**********************************************" +
				"**********************");
		System.out.println("Welcome "+u_name+" to Termonator 1.0");
		System.out.println("1.- See heater Status");
		System.out.println("2.- Modify Heater Temperature");
		System.out.println("3.- Switch ON your heater");
		System.out.println("4.- Switch OFF your heater");
		System.out.println("0.- Exit");
		System.out.println("**********************************************" +
				"**********************");
		System.out.print("Your Option: ");
		option = teclado.nextInt();
		ExecuteMenuUser(option,u_name);
		}
	}
	
	/**
	 * @brief Función que ejecuta el menu del usuario. Es decir, aquí se llamará
	 * a las funciones necesarias dependiendo de la elección del usuario en el menú.
	 * @param option opción introducida por el usuario.
	 * @param u_name nombre del usuario que nos servirá para las funciones a las que
	 * se llame.
	 */
	public static void ExecuteMenuUser(int option, String u_name){
		ControlerUser heater = new ControlerUser(dbI);
		switch(option){
		case 1: heater.HeaterStatus(u_name);
			break;
		case 2: heater.ModifyTemperature(u_name);
			break;
		case 3: heater.SwitchON(u_name);
			break;
		case 4: heater.SwitchOFF(u_name);
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
	 * @param u_name nombre de usuario introducido que se utiliza para mostrar en el mensaje 
	 * de bienvenida.
	 * 
	 */
	public static void PrintMenuWorker(String u_name){
		Scanner teclado = new Scanner(System.in);
		int option =-1;
		while (option != 0){
		System.out.println("");
		System.out.println("*************************************************" +
				"*******************");
		System.out.println("Welcome "+u_name+" to Termonator 1.0");
		System.out.println("1.- See Consumption of a concrete Home");
		System.out.println("2.- Switch ON a boiler of a specific neighbourhood");
		System.out.println("3.- Switch OFF a boiler of a specific neighbourhood");
		System.out.println("0.- Exit");
		System.out.println("*************************************************" +
				"*******************");
		System.out.print("Your Option: ");
		option = teclado.nextInt();
		ExecuteMenuWorker(option, u_name);
		}
	}
	
	/**
	 * @brief Función que ejecuta el menu del trabajador. Es decir, aquí se llamará
	 * a las funciones necesarias dependiendo de la elección del trabajdor en el menú.
	 * @param option opción introducida por el trabajdor.
	 * @param u_name 
	 * @param u_name nombre del trabajador que nos servirá para las funciones a las que
	 * se llame
	 */
	public static void ExecuteMenuWorker(int option, String u_name){
		BoilerWorker boiler = new BoilerWorker(dbI);
		int incidents;
		incidents = dbI.getIncidentCont();
		if(incidents > 0){
			System.out.println("New "+incidents+" Incidents");
			dbI.setIncidentCont(incidents);
		}
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
