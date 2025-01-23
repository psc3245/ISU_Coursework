package pkg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Insert {
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
		String userName = "root";
		String password = "Documentary@11";
		String dbServer = "jdbc:mysql://localhost:3306/university";
		conn = DriverManager.getConnection(dbServer,userName,password);
		System.out.println("Connected to the database");
		return conn;
		
	}
	
	public static String[] getInsertStatements() {
		String students = "INSERT INTO students VALUES\n"
				+ "                (1001, 654651234, 'Randy', 'M', '2000/12/01', '301 E Hall', '5152700988', '121 Main', '7083066321'),\n"
				+ "                (1002, 123097834, 'Victor', 'M', '2000/05/06', '270 W Hall', '5151234578', '702 Walnut', '7080366333'),\n"
				+ "                (1003, 978012431, 'John', 'M', '1999/07/08', '201 W Hall', '5154132805', '888 University', '5152012011'),\n"
				+ "                (1004, 746897816, 'Seth', 'M', '1998/12/30', '199 N Hall', '5158891504', '21 Green', '5154132907'),\n"
				+ "                (1005, 186032894, 'Nicole', 'F', '2001/04/01', '178 S Hall', '5158891155', '13 Gray', '5157162071'),\n"
				+ "                (1006, 534218752, 'Becky', 'F', '2001/05/16', '12 N Hall', '5157083698', '189 Clark', '2034367632'),\n"
				+ "                (1007, 432609519, 'Kevin', 'M', '2000/08/12', '75 E Hall', '5157082497', '89 National', '7182340772');";
		String departments = " INSERT INTO departments VALUES\n"
				+ "                (401, 'Computer Science', '5152982801', 'LAS'),\n"
				+ "                (402, 'Mathematics', '5152982802', 'LAS'),\n"
				+ "                (403, 'Chemical Engineering', '5152982803', 'Engineering'),\n"
				+ "                (404, 'Landscape Architect', '5152982804', 'Design');";
		
		String statement[] = new String[] {students, departments};
		return statement;
	}

}
