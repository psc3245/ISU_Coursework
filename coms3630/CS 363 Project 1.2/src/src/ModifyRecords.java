package src;

import java.sql.*;

public class ModifyRecords {

	public static void main(String args[]) throws SQLException {

		Connection conn = getConnection();


		//Query tables 

		String[] queryStatements = getQueryStatements();

		try (var statement = conn.createStatement()){

			for (int i = 0; i < queryStatements.length; i++) {		
				int rowsAffected = statement.executeUpdate(queryStatements[i]);
				if (i == 1 || i == 2 || i == 3) {
					System.out.println(rowsAffected + " row(s) updated");
				}
			}
			System.out.println("Records Modified.");

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
		String q2 = "update students set name = 'Scott' where ssn = 746897816;";
		String q3 = "update major set name = 'Computer Science', level = 'MS' where snum in (select snum from students where ssn = 746897816);";
		String q4 = "delete from register where regtime = 'Spring2021';";
		String q5 = "set sql_safe_updates = 1;";
		String statement[] = new String[] {q1, q2, q3, q4, q5};
		return statement;
	}
}
