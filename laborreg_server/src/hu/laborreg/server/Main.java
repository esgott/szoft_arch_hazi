package hu.laborreg.server;

import hu.laborreg.server.computer.ComputerContainer;
import hu.laborreg.server.course.CourseContainer;
import hu.laborreg.server.db.DBConnectionHandler;
import hu.laborreg.server.gui.MainWindow;
import hu.laborreg.server.handlers.ClientConnectionHandler;
import hu.laborreg.server.handlers.DataExporter;
import hu.laborreg.server.http.HttpFactory;
import hu.laborreg.server.http.HttpRequestListenerThread;
import hu.laborreg.server.labEvent.LabEventContainer;
import hu.laborreg.server.student.StudentContainer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.SwingUtilities;

public class Main {

	private static DBConnectionHandler dbConnHandler;
	private static Connection connection;
	private static Configuration configuration = new Configuration();
	private static Logger logger;

	private DataExporter dataExporter = new DataExporter(dbConnHandler);
	private ClientConnectionHandler clientConnectionHandler = new ClientConnectionHandler();
	private static CourseContainer courseContainer;
	private static LabEventContainer labEventContainer;
	private static ComputerContainer computercontainer;
	private static StudentContainer studentContainer;

	public static void main(String[] args) {
		initializeLogger();
		initializeDB();
		initializeContainers();
		initializeHttpServer();
		displayGUI();
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
			logger.warning("Failed to create logfile");
		}
		logger.info("Laborreg Server started");
	}

	private static void initializeDB() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + configuration.getProperty(Configuration.dbFile));
			dbConnHandler = new DBConnectionHandler(connection);
		} catch (SQLException e) {
			logger.severe("SQLError: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			logger.severe("Failed to load database driver: " + e.getMessage());
		}
	}

	private static void initializeContainers() {
		computercontainer = new ComputerContainer(dbConnHandler);
		studentContainer = new StudentContainer(dbConnHandler);
		courseContainer = new CourseContainer(dbConnHandler, studentContainer);
		labEventContainer = new LabEventContainer(dbConnHandler);
	}

	private static void initializeHttpServer() {
		try {
			int port = Integer.parseInt(configuration.getProperty(Configuration.httpServerPort));
			String docRoot = configuration.getProperty(Configuration.htmlRoot);
			HttpFactory httpFactory = new HttpFactory();
			ClientConnectionHandler clientConnHandler = new ClientConnectionHandler();
			HttpRequestListenerThread httpServer = new HttpRequestListenerThread(port, docRoot, httpFactory,
					clientConnHandler);
			Thread thread = new Thread(httpServer);
			thread.setDaemon(true);
			thread.start();
		} catch (IOException e) {
			logger.severe("Failed to initilaize HTTP server:" + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void displayGUI() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					MainWindow window = new MainWindow(courseContainer, labEventContainer);
					window.display();
				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
