package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateDatabase {
public static void main(String args[]) throws SQLException {
		
		Connection conn = getConnection();
		
		// Create database
		String databaseCreate = getCreateDatabaseStatement();
		
		try (var statement = conn.createStatement()){
			statement.execute(databaseCreate);
			System.out.println("Created database");
		}  catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static Connection getConnection() throws SQLException{
		
		Connection conn = null;	
		String userName = "root";
		String password = "root";
		String dbServer = "jdbc:mysql://localhost:3306/";
		conn = DriverManager.getConnection(dbServer,userName,password);
		System.out.println("Connected to the database");
		return conn;
		
	}
	
	public static String getCreateDatabaseStatement() {
		String database = "create database university;" ;
		return database;
		}
}
