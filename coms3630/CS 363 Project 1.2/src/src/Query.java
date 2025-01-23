package src;

import java.sql.*;

public class Query {

	public static void main(String args[]) throws SQLException {

		Connection conn = getConnection();


		//Query tables 

		String[] queryStatements = getQueryStatements();

		try (var statement = conn.createStatement()){

			for (int i = 0; i < queryStatements.length; i++) {
				ResultSet rs = statement.executeQuery(queryStatements[i]);
				while (rs.next()) {
					if (i == 0) {
						int snum = rs.getInt("snum");
						int ssn = rs.getInt("ssn");
						System.out.println(snum + ", " + ssn);
					}
					else if (i == 1) {
						while (rs.next()) {
							String name = rs.getNString("name");
							String level = rs.getNString("level");
							System.out.println(name + ", " + level);
						}
					}
					else if (i == 2) {
						String name = rs.getNString("name");
						System.out.println(name);
					}
					else if (i == 3) {
						String name = rs.getNString("name");
						String level = rs.getNString("level");
						System.out.println(name + ", " + level);
					}
					else if (i == 4) {
						String name = rs.getNString("name");
						System.out.println(name);
					}
					else {
						// error, manually entered values so size should never be > 5
						break;
					}
				}

			}

		}


		catch (SQLException e) {
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
		String q1 = "select snum, ssn from students where name = 'Becky'";
		String q2 = "select name, level from major where snum in (select snum from students where ssn = 123097834);";
		String q3 = "select distinct c.name from courses c, departments d where c.department_code = d.code and d.name = 'Computer Science';";
		String q4 = "select distinct g.name, g.level from degrees g, departments d where g.department_code = d.code and d.name = 'Computer Science'";
		String q5 = "select distinct s.name from students s, minor m where s.snum = m.snum;";
		String statement[] = new String[] {q1, q2, q3, q4, q5};
		return statement;
	}
}