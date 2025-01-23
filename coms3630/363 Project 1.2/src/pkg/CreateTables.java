package pkg;

import java.sql.*;

public class CreateTables {

	public static void main(String args[]) throws SQLException {
		
		Connection conn = getConnection();
		
		//Create tables
		String[] createStatements = getCreateStatements();
		
		try (var statement = conn.createStatement()){
			conn.setAutoCommit(false);
			for (String stmt: createStatements) {
				statement.addBatch(stmt);
			}
			statement.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		    System.out.println("Created tables");
		}  catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static Connection getConnection() throws SQLException{
		
		Connection conn = null;	
		String userName = "root";
		String password = "Documentary@11";
		String dbServer = "jdbc:mysql://localhost:3306/university";
		conn = DriverManager.getConnection(dbServer,userName,password);
		System.out.println("Connected to the database");
		return conn;
		
	}
	
	public static String[] getCreateStatements() {
		String students = "CREATE TABLE STUDENTS " +
                "(snum INTEGER DEFAULT NULL UNIQUE, " +
                " ssn INTEGER NOT NULL, " +
                " name VARCHAR(10) DEFAULT NULL, " +
                " gender VARCHAR(1) DEFAULT NULL, " +
                " dob DATETIME DEFAULT NULL," +
                " c_addr VARCHAR(20) DEFAULT NULL, " +
                " c_phone VARCHAR(10) DEFAULT NULL, " +
                " p_addr VARCHAR(20) DEFAULT NULL, " +
                " p_phone VARCHAR(10) DEFAULT NULL, " +	                   
                " PRIMARY KEY ( ssn ))" ;
		String departments = "CREATE TABLE DEPARTMENTS " +
                "(code INTEGER NOT NULL, " +	                   
                " name VARCHAR(50) DEFAULT NULL UNIQUE, " +
                " phone VARCHAR(10) DEFAULT NULL, " +	                 
                " college VARCHAR(20) DEFAULT NULL, " +	                   	                   
                " PRIMARY KEY ( code ))" ;
		
		String statement[] = new String[] {students, departments};
		return statement;
		
	}
}
