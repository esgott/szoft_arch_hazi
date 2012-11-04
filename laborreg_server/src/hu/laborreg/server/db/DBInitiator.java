package hu.laborreg.server.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBInitiator {

	private Connection connection;
	private List<String> tables;

	public DBInitiator(Connection dbConnection) {
		connection = dbConnection;
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
		if (!tables.contains("course")) {
			createCourseTable();
		}
	}

	private void createCourseTable() throws SQLException {
		Statement statement = connection.createStatement();
		statement.executeUpdate("CREATE TABLE course (year INTEGER NOT NULL, name TEXT NOT NULL, PRIMARY KEY (year, name))");
		statement.close();
	}

}
