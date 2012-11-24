package hu.laborreg.server.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import au.com.bytecode.opencsv.CSVWriter;

import hu.laborreg.server.course.Course;
import hu.laborreg.server.db.DBConnectionHandler;
import hu.laborreg.server.labEvent.LabEvent;

public class DataExporter {
	
	private DBConnectionHandler dbConnHandler;
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	
	/**
	 * Enables to save the LabEvents' data to a hard drive. It reads the data of the LabEvents (after it is finished) from the database, then save the data to the specified path in CSV format.
	 * @param dbConnHandler Handler of the database connection.
	 */
	public DataExporter(DBConnectionHandler dbConnHandler)
	{
		this.dbConnHandler = dbConnHandler;
	}

	public void exportData(File file)
	{	
		try{
			CSVWriter writer = new CSVWriter(new FileWriter(file.getPath()), ',');
			ResultSet result;
			
			try {
				PreparedStatement statement = dbConnHandler.createPreparedStatement("SELECT * FROM lab_event");
				result = statement.executeQuery();
				writer.writeAll(result,true);
			} catch (SQLException e) {
				logger.severe("Failed to read LabEvents from DB: " + e.getMessage());
			}	
			writer.close();
			
		}catch (IOException e)
		{
			logger.severe("Failed to write LabEvents to the CSV file: " + e.getMessage());
		}
	}

}
