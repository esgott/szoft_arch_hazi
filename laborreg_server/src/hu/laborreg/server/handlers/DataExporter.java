package hu.laborreg.server.handlers;

import hu.laborreg.server.db.DBConnectionHandler;
import hu.laborreg.server.labEvent.LabEvent;

public class DataExporter {
	
	private DBConnectionHandler dbConnHandler;
	
	/**
	 * Enables to save the LabEvents' data to a hard drive. It reads the data of the LabEvents (after it is finished) from the database, then save the data to the specified path in CSV format.
	 * @param dbConnHandler Handler of the database connection.
	 */
	public DataExporter(DBConnectionHandler dbConnHandler)
	{
		this.dbConnHandler = dbConnHandler;
	}
	
	/**
	 * Export Lab events' data to a CSV file
	 * @param labEvent The needed labEvent.
	 * @param filePath The path of the needed output CSV file.
	*/
	public void exportLabEventData(LabEvent labEvent, String filePath)
	{
		//TODO get LabEvent data from the DB and export it to CSV
	}

}
