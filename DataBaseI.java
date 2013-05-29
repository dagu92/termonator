import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Ice.Current;
import utils._DataBaseDisp;


public class DataBaseI extends _DataBaseDisp {

	@Override
	public void SaveIncident(String incident, Current __current) {
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
					Statement statement = conn.createStatement();
					ResultSet resultset = statement.executeQuery("INSERT INTO Incidents (I_description) VALUES ('"+incident+"')");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			  
		  }catch(Exception e){
			  System.out.println("Error en el Almacenamiento de la Base de Datos");
		  }
	}

}
