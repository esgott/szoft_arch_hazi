package hu.laborreg.server;

import hu.laborreg.server.db.DBConnectionHandler;
import hu.laborreg.server.gui.MainWindow;
import hu.laborreg.server.http.HttpServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {

	private static DBConnectionHandler dbConnHandler;
	private static Connection connection;
	private static Configuration configuration = new Configuration();
	private static Logger logger;

	private DataExporter dataExporter = new DataExporter(dbConnHandler);
	private ClientConnectionHandler clientConnectionHandler = new ClientConnectionHandler();
	private CourseContainer courseContainer = new CourseContainer();
	private LabEventContainer labEventContainer = new LabEventContainer();
	private ComputerContainer computercontainer = new ComputerContainer();
	private StudentContainer studentContainer = new StudentContainer();

	public static void main(String[] args) {
		initializeLogger();
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

		try {
			int port = Integer.parseInt(configuration.getProperty(Configuration.httpServerPort));
			ServerSocket server = new ServerSocket(port, 10);
			logger.info("HTTP server started on port " + Integer.toString(port));
			for (;;) {
				Socket connected = server.accept();
				String clientHtml = configuration.getProperty(Configuration.clientHtmlPage);
				String errorHtml = configuration.getProperty(Configuration.errorHtmlPage);
				HttpServer httpServer = new HttpServer(connected, clientHtml, errorHtml);
				Thread thread = new Thread(httpServer);
				thread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void initializeLogger() {
		logger = Logger.getLogger(Main.class.getPackage().getName());
		logger.setLevel(Level.ALL);
		try {
			FileHandler fileHandler = new FileHandler(configuration.getProperty(Configuration.logFile));
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
		} catch (Exception e) {
			System.out.println("Failed to create logfile");
		}
		logger.info("Laborreg Server started");
	}

}
