package pkg;

import java.sql.*;

public class Query {

public static void main(String args[]) throws SQLException {
		
		Connection conn = getConnection();
		

		//Query tables 
		
		String[] queryStatements = getQueryStatements();
		
		try (var statement = conn.createStatement()){
					
			ResultSet rs = statement.executeQuery(queryStatements[0]);
			while (rs.next()) {
		        int snum = rs.getInt("snum");
		        int ssn = rs.getInt("ssn");
		        System.out.println(snum + ", " + ssn );
		      }
					
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

	public static String[] getQueryStatements() {
		String q1 = "select snum, ssn from students where name = 'Becky'";
		String statement[] = new String[] {q1};
		return statement;
	}
}
