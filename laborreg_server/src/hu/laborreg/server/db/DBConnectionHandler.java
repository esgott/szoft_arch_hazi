package hu.laborreg.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DBConnectionHandler {

	private Connection connection;
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public DBConnectionHandler(Connection dbConnection) throws SQLException {
		connection = dbConnection;
		connection.setAutoCommit(true);

		prepareDB();
	}

	private void prepareDB() {
		try {
			DBInitiator initiator = new DBInitiator(connection);
			initiator.initiate();
		} catch (SQLException e) {
			logger.severe("Error while initializing database: " + e.getMessage());
		}
	}

	public PreparedStatement createPreparedStatement(String command) throws SQLException {
		logger.fine("New prepared statement created: " + command);
		return connection.prepareStatement(command);
	}

}
