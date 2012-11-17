package hu.laborreg.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import hu.laborreg.server.db.DBConnectionHandler;
import hu.laborreg.server.gui.MainWindow;

public class Main {
	private static DBConnectionHandler dbConnHandler;
	private static Connection connection;
	
	private DataExporter dataExporter = new DataExporter(dbConnHandler);
	private ClientConnectionHandler clientConnectionHandler = new ClientConnectionHandler();
	private CourseContainer courseContainer = new CourseContainer();
	private LabEventContainer labEventContainer = new LabEventContainer();
	private ComputerContainer computercontainer = new ComputerContainer();
	private StudentContainer studentContainer = new StudentContainer();
	
	public static void main(String[] args) {
		Configuration configuration = new Configuration();
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + configuration.getProperty(Configuration.dbFile));
			dbConnHandler = new DBConnectionHandler(connection);
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
