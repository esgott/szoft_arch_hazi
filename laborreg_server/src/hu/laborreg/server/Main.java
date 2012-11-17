package hu.laborreg.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import hu.laborreg.server.db.DBConnectionHandler;
import hu.laborreg.server.gui.MainWindow;

public class Main {
	
	private DataExporter dataExporter = new DataExporter();
	private ClientConnectionHandler clientConnectionHandler = new ClientConnectionHandler();
	private CourseContainer courseContainer = new CourseContainer();
	private LabEventContainer labEventContainer = new LabEventContainer();
	private ComputerContainer computercontainer = new ComputerContainer();
	private StudentContainer studentContainer = new StudentContainer();
	
	public static void main(String[] args) {
		Configuration configuration = new Configuration();
		Connection connection;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + configuration.getProperty(Configuration.dbFile));
			DBConnectionHandler dbConnHandler = new DBConnectionHandler(connection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MainWindow.display();
		
		
	}

}
