package hu.laborreg.server.db;

import java.sql.Connection;

public class DBConnectionHandler {
	
	private Connection connection;
	
	public DBConnectionHandler(Connection dbConnection) {
		connection = dbConnection;
	}

}
