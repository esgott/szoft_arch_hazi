package hu.laborreg.server.db;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionHandler {

	private Connection connection;

	public DBConnectionHandler(Connection dbConnection) {
		connection = dbConnection;

		prepareDB();
	}

	private void prepareDB() {
		try {
			DBInitiator initiator = new DBInitiator(connection);
			initiator.initiate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
