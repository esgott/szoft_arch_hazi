package hu.laborreg.server.computer;

import hu.laborreg.server.Configuration;
import hu.laborreg.server.db.DBConnectionHandler;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.exception.WrongIpAddressException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class ComputerContainer {

	private Set<Computer> computers;
	private final DBConnectionHandler dbConnection;
	private final Configuration configuration;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * A container class which contains the computers.
	 */
	public ComputerContainer(DBConnectionHandler dbConnectionhandler, Configuration config) {
		computers = new HashSet<Computer>();
		dbConnection = dbConnectionhandler;
		configuration = config;
		intializeFromDB();
	}

	private void intializeFromDB() {
		try {
			PreparedStatement statement = dbConnection.createPreparedStatement("SELECT * FROM computer");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				String ipAddress = result.getString("ip_address");
				Computer newComputer = new Computer(ipAddress, configuration);
				boolean success = computers.add(newComputer);
				if (!success) {
					logger.warning("Comupter instance found multiple times in the DB: " + ipAddress);
				}
			}
		} catch (SQLException e) {
			logger.severe("Could init Computers from database: " + e.getMessage());
		} catch (WrongIpAddressException e) {
			logger.warning("Can't create Computer instance: " + e.getMessage());
		}
	}

	/**
	 * Add computer to the computers list.
	 * 
	 * @param computer
	 *            The needed computer.
	 */
	public void addComputer(Computer computer) throws ElementAlreadyAddedException {
		if (this.computers.add(computer) == false) {
			throw new ElementAlreadyAddedException("Computer " + computer.getIpAddress()
					+ " is already added to Computers list.");
		}
		addToDB(computer);
	}

	private void addToDB(Computer computer) {
		try {
			String command = "INSERT INTO computer VALUES(?)";
			PreparedStatement statement = dbConnection.createPreparedStatement(command);
			statement.setString(1, computer.getIpAddress());
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.severe("Failed to add new computer to db: " + e.getMessage());
			computers.remove(computer);
		}
	}

	/**
	 * Remove computer to the computers list.
	 * 
	 * @param computer
	 *            The needed computer.
	 */
	public void removeComputer(Computer computer) throws ElementNotFoundException {
		if (this.computers.remove(computer) == false) {
			throw new ElementNotFoundException("Computer " + computer.getIpAddress()
					+ " is not found in Computers list.");
		}
		removeFromDB(computer);
	}

	private void removeFromDB(Computer computer) {
		try {
			String command = "DELETE FROM computer WHERE ip_address = ?";
			PreparedStatement statement = dbConnection.createPreparedStatement(command);
			statement.setString(1, computer.getIpAddress());
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.severe("Failed to delete computer from DB: " + e.getMessage());
		}
	}

	/**
	 * Get the specified computer from computers.
	 * 
	 * @param name
	 *            The name of the computer (must be unique).
	 * @param year
	 *            The year of the computer.
	 * @return The needed computer
	 */
	public Computer getComputer(String ipAddress) throws ElementNotFoundException {
		for (Computer computer : computers) {
			if (computer.getIpAddress().equals(ipAddress)) {
				return computer;
			}
		}
		throw new ElementNotFoundException("Computer " + ipAddress + " is not found in Computers list.");
	}
	
	public Computer getComputerAndAddIfNotFound(String ipAddress) throws WrongIpAddressException{
		for (Computer computer : computers) {
			if (computer.getIpAddress().equals(ipAddress)) {
				return computer;
			}
		}
		Computer computer = new Computer(ipAddress, configuration);
		computers.add(computer);
		addToDB(computer);
		return computer;
	}
}