package src;

import java.sql.*;

public class DropTables {

	public static void main(String args[]) throws SQLException {

		Connection conn = getConnection();


		//Query tables 

		String[] queryStatements = getQueryStatements();

		try (var statement = conn.createStatement()){

			for (int i = 0; i < queryStatements.length; i++) {		
				int rowsAffected = statement.executeUpdate(queryStatements[i]);
				System.out.println("Table Deleted");
			}
			System.out.println("All Tables Deleted");

		}  catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException{

		Connection conn = null;	
		String userName = "coms363";
		String password = "password";
		String dbServer = "jdbc:mysql://localhost:3306/university";
		conn = DriverManager.getConnection(dbServer,userName,password);
		System.out.println("Connected to the database");
		return conn;

	}  

	public static String[] getQueryStatements() {
		String q1 = "set sql_safe_updates = 0;";
		String q2 = "drop table if exists major;";
		String q3 = "drop table if exists minor";
		String q4 = "drop table if exists degrees;";
		String q5 = "drop table if exists register;";
		String q6 = "drop table if exists courses;";
		String q7 = "drop table if exists students;";
		String q8 = "drop table if exists departments;";
		String q9 = "set sql_safe_updates = 1;";
		String statement[] = new String[] {q1, q2, q3, q4, q5, q6, q7, q8, q9};
		return statement;
	}
}

