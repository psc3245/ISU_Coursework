package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class InsertRecords {
public static void main(String args[]) throws SQLException {
		
		Connection conn = getConnection();
		//Insert into tables
		String[] insertStatements = getInsertStatements();
		
		try (var statement = conn.createStatement()){
			conn.setAutoCommit(false);
			for (String stmt: insertStatements) {
			statement.addBatch(stmt);
		    }
		    statement.executeBatch();
		    conn.commit();
			conn.setAutoCommit(true);
			System.out.println("Inserted into tables");
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
	
	public static String[] getInsertStatements() {
		String students = "INSERT INTO students VALUES\n"
				+ "(1001, 654651234, 'Randy', 'M', '2000/12/01', '301 E Hall', '5152700988', '121 Main', '7083066321'),\n"
				+ "(1002, 123097834, 'Victor', 'M', '2000/05/06', '270 W Hall', '5151234578', '702 Walnut', '7080366333'),\n"
				+ "(1003, 978012431, 'John', 'M', '1999/07/08', '201 W Hall', '5154132805', '888 University', '5152012011'),\n"
				+ "(1004, 746897816, 'Seth', 'M', '1998/12/30', '199 N Hall', '5158891504', '21 Green', '5154132907'),\n"
				+ "(1005, 186032894, 'Nicole', 'F', '2001/04/01', '178 S Hall', '5158891155', '13 Gray', '5157162071'),\n"
				+ "(1006, 534218752, 'Becky', 'F', '2001/05/16', '12 N Hall', '5157083698', '189 Clark', '2034367632'),\n"
				+ "(1007, 432609519, 'Kevin', 'M', '2000/08/12', '75 E Hall', '5157082497', '89 National', '7182340772');";
		String departments = " INSERT INTO departments VALUES\n"
				+ "(401, 'Computer Science', '5152982801', 'LAS'),\n"
				+ "(402, 'Mathematics', '5152982802', 'LAS'),\n"
				+ "(403, 'Chemical Engineering', '5152982803', 'Engineering'),\n"
				+ "(404, 'Landscape Architect', '5152982804', 'Design');";
		String degrees = " INSERT INTO degrees VALUES \n"
				+ "('Computer Science', 'BS', 401),\n"
				+ "('Software Engineering', 'BS', 401),\n"
				+ "('Computer Science', 'MS', 401),\n"
				+ "('Computer Science', 'PhD', 401),\n"
				+ "('Applied Mathematics', 'MS', 402),\n"
				+ "('Chemical Engineering', 'BS', 403),\n"
				+ "('Landscape Architect', 'BS', 404) \n";
		String major = "INSERT INTO major VALUES \n"
				+ "(1001, 'Computer Science', 'BS'), \n"
				+ "(1002, 'Software Engineering', 'BS'), \n"
				+ "(1003, 'Chemical Engineering', 'BS'), \n"
				+ "(1004, 'Landscape Architect', 'BS'), \n"
				+ "(1005, 'Computer Science', 'MS'), \n"
				+ "(1006, 'Applied Mathematics', 'MS'), \n"
				+ "(1007, 'Computer Science', 'PhD') \n";
		String minor = "INSERT INTO minor VALUES \n"
				+ "(1007, 'Applied Mathematics', 'MS'),\n"
				+ "(1005, 'Applied Mathematics', 'MS'),\n"
				+ "(1001, 'Software Engineering', 'BS') \n";
		String courses = "INSERT INTO courses VALUES \n"
				+ "(113, 'Spreadsheet', 'Microsoft Excel and Access', 3, 'Undergraduate', 401), \n"
				+ "(311, 'Algorithm', 'Design and Analysis', 3, 'Undergraduate', 401), \n"
				+ "(531, 'Theory of Computation', 'Theorem and Probability', 3, 'Graduate', 401), \n"
				+ "(363, 'Database', 'Design Principal', 3, 'Undergraduate', 401), \n"
				+ "(412, 'Water Management', 'Water Management', 3, 'Undergraduate', 404), \n"
				+ "(228, 'Special Topics', 'Interesting Topics about CE', 3, 'Undergraduate', 404), \n"
				+ "(101, 'Calculus', 'Limit and Derivative', 3, 'Undergraduate', 403) \n";
		String register = "INSERT INTO register VALUES \n"
				+ "(1001, 363, 'Fall2020', 3), \n"
				+ "(1002, 311, 'Fall2020', 4), \n"
				+ "(1003, 228, 'Fall2020', 4), \n"
				+ "(1004, 363, 'Spring2021', 3), \n"
				+ "(1005, 531, 'Spring2021', 4), \n"
				+ "(1006, 363, 'Fall2020', 3), \n"
				+ "(1007, 531, 'Spring2021', 4) \n"
				+ "";
		
		String statement[] = new String[] {students, departments, degrees, courses, register, major, minor};
		return statement;
	}

}
