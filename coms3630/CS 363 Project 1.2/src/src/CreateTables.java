package src;

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
		String userName = "coms363";
		String password = "password";
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
		String degrees = "CREATE TABLE DEGREES" + 
                "(name VARCHAR(50) NOT NULL," +
				"level VARCHAR(50) NOT NULL," +
                "department_code INTEGER DEFAULT NULL," +
				"PRIMARY KEY ( name, level ), " +
                "FOREIGN KEY (department_code) REFERENCES departments(code) )";
		String courses = "CREATE TABLE COURSES" +
                "(number INTEGER NOT NULL UNIQUE," +
				"name VARCHAR(50) NOT NULL," +
                "description VARCHAR(50) NOT NULL," +
                "credithours INTEGER DEFAULT NULL," +
				"level VARCHAR(20) DEFAULT NULL," +
				"department_code INTEGER DEFAULT NULL," +
                "PRIMARY KEY (number, name)," +
				"FOREIGN KEY (department_code) REFERENCES departments(code) )";
		String register = "CREATE TABLE REGISTER" +
				"( snum INTEGER NOT NULL UNIQUE," +
				"course_number INTEGER DEFAULT NULL," +
				"regtime VARCHAR(20) DEFAULT NULL," +
				"credithours INTEGER DEFAULT NULL," +
				"PRIMARY KEY (snum)," +
				"FOREIGN KEY (snum) REFERENCES students(snum), " +
				"FOREIGN KEY (course_number) REFERENCES courses(number) )";
		String major = "CREATE TABLE MAJOR" +
				"( snum INTEGER NOT NULL UNIQUE," +
				"name VARCHAR(50) NOT NULL," +
				"level VARCHAR(5) NOT NULL," +
				"PRIMARY KEY (snum, name, level)," +
				"FOREIGN KEY (snum) REFERENCES students(snum), " +
				"FOREIGN KEY (name, level) REFERENCES degrees(name, level) ) ";
		
		String minor = "CREATE TABLE MINOR" +
				"( snum INTEGER NOT NULL UNIQUE," +
				"name VARCHAR(50) NOT NULL," +
				"level VARCHAR(5) NOT NULL," +
				"PRIMARY KEY (snum, name, level)," +
				"FOREIGN KEY (snum) REFERENCES students(snum), " +
				"FOREIGN KEY (name, level) REFERENCES degrees(name, level) ) ";
		
		
		String statement[] = new String[] {students, departments, degrees, courses, register, major, minor};
		return statement;
		
	}
}











