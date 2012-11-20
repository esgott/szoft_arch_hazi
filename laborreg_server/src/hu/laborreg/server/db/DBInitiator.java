package hu.laborreg.server.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class DBInitiator {

	private Connection connection;
	private List<String> tables;
	private Map<String, String> commands;
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public DBInitiator(Connection dbConnection) {
		connection = dbConnection;
		commands = new HashMap<String, String>();
		addCommands();
	}

	private void addCommands() {
		commands.put("course",
				"CREATE TABLE course (year INTEGER NOT NULL, name TEXT NOT NULL, PRIMARY KEY (year, name))");
		commands.put("student", "CREATE TABLE student (neptun TEXT PRIMARY KEY, name TEXT)");
		commands.put("computer", "CREATE TABLE computer (ip_address TEXT PRIMARY KEY)");
		commands.put("lab_event", "CREATE TABLE lab_event (name TEXT PRIMARY KEY, part_of_course_name TEXT NOT NULL, "
				+ "part_of_course_year INTEGER NOT NULL, start_time DATETIME NOT NULL, end_time DATETIME NOT NULL, "
				+ "FOREIGN KEY(part_of_course_name) REFERENCES course(name), "
				+ "FOREIGN KEY(part_of_course_year) REFERENCES course(year))");
		commands.put("registered", "CREATE TABLE registered (course_name TEXT NOT NULL, course_year INTEGER NOT NULL, "
				+ "student_neptun TEXT NOT NULL, FOREIGN KEY(course_name) REFERENCES course(name), "
				+ "FOREIGN KEY(course_year) REFERENCES course(year), "
				+ "FOREIGN KEY(student_neptun) REFERENCES student(neptun))");
		commands.put("signed", "CREATE TABLE signed (lab_event_name TEXT NOT NULL, student_neptun TEXT NOT NULL, "
				+ "FOREIGN KEY(lab_event_name) REFERENCES lab_event(name), "
				+ "FOREIGN KEY(student_neptun) REFERENCES student(neptun))");
		commands.put("multiple_registration_allowed", "CREATE TABLE multiple_registration_allowed ("
				+ "lab_event_name TEXT NOT NULL, computer_ip_address TEXT NOT NULL, "
				+ "FOREIGN KEY(lab_event_name) REFERENCES lab_event(name), "
				+ "FOREIGN KEY(computer_ip_address) REFERENCES computer(ip_address))");
	}

	public void initiate() throws SQLException {

		tables = new ArrayList<String>();

		DatabaseMetaData metadata = connection.getMetaData();
		ResultSet result = metadata.getTables(null, null, null, new String[] { "TABLE" });
		while (result.next()) {
			tables.add(result.getString("TABLE_NAME"));
		}

		createTables();
	}

	private void createTables() throws SQLException {
		Set<String> tableNames = commands.keySet();
		for (String tableName : tableNames) {
			createTable(tableName);
		}
	}

	private void createTable(String tableName) throws SQLException {
		if (!tables.contains(tableName)) {
			Statement statement = connection.createStatement();
			String command = commands.get(tableName);
			logger.fine("Creating table " + tableName);
			logger.finer(command);
			statement.executeUpdate(command);
			statement.close();
		}
		logger.fine(tableName + " table already exists");
	}

}
