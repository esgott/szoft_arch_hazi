package hu.laborreg.server.labEvent;

import hu.laborreg.server.computer.Computer;
import hu.laborreg.server.computer.ComputerContainer;
import hu.laborreg.server.db.DBConnectionHandler;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.exception.TimeSetException;
import hu.laborreg.server.student.Student;
import hu.laborreg.server.student.StudentContainer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class LabEventContainer {
	
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private List<LabEvent> labEvents;
	private final DBConnectionHandler dbConnection;
	private final StudentContainer students;
	private final ComputerContainer computers;
	private final DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * A container class which contains the labEvents.
	 */
	public LabEventContainer(DBConnectionHandler dbConnectionHandler, StudentContainer studentContainer,
			ComputerContainer computerContainer) {
		labEvents = new ArrayList<LabEvent>();
		dbConnection = dbConnectionHandler;
		students = studentContainer;
		computers = computerContainer;
		initFromDB();
	}

	private void initFromDB() {
		try {
			PreparedStatement statement = dbConnection.createPreparedStatement("SELECT * FROM lab_event");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				String name = result.getString("name");
				String courseName = result.getString("part_of_course_name");
				int courseYear = result.getInt("part_of_course_year");

				String startTimeText = result.getString("start_time");
				String endTimeText = result.getString("end_time");
				Date startTime = formatter.parse(startTimeText);
				Date endTime = formatter.parse(endTimeText);

				LabEvent labEvent = new LabEvent(name, courseName, courseYear, startTime, endTime);
				boolean success = labEvents.add(labEvent);
				if (!success) {
					logger.warning("LabEvent instance found multiple times in the DB: " + labEvent.getName()
							+ " for course: " + labEvent.getCourseName() + "," + labEvent.getCourseYear());
				}
				fillSignedInStudents(labEvent);
				fillMultipleRegistrations(labEvent);
			}
		} catch (SQLException e) {
			logger.severe("Failed to init LabEvents from DB: " + e.getMessage());
		} catch (TimeSetException e) {
			logger.warning("Error during date setting: " + e.getMessage());
		} catch (ParseException e) {
			logger.severe("Failed to parse date in DB: " + e.getMessage());
		}
	}

	private void fillSignedInStudents(LabEvent labEvent) throws SQLException {
		String command = "SELECT * FROM signed WHERE lab_event_name = ?";
		PreparedStatement statement = dbConnection.createPreparedStatement(command);
		statement.setString(1, labEvent.getName());
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			String neptun = result.getString("student_neptun");
			try {
				Student student = students.getStudent(neptun);
				labEvent.signInStudent(student);
			} catch (ElementNotFoundException e) {
				logger.warning("Student found in signed, but not in students: " + e.getMessage());
			} catch (ElementAlreadyAddedException e) {
				logger.warning("Studnet signed in multiple times: " + e.getMessage());
			}
		}
	}

	private void fillMultipleRegistrations(LabEvent labEvent) throws SQLException {
		String command = "SELECT * FROM multiple_registration_allowed WHERE lab_event_name = ?";
		PreparedStatement statement = dbConnection.createPreparedStatement(command);
		statement.setString(1, labEvent.getName());
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			String ipAddress = result.getString("computer_ip_address");
			try {
				Computer computer = computers.getComputer(ipAddress);
				labEvent.allowMultipleRegistration(computer);
			} catch (ElementNotFoundException e) {
				logger.warning("Computer found in multiple_registration_allowed, but not in computer: "
						+ e.getMessage());
			} catch (ElementAlreadyAddedException e) {
				logger.warning("Multiple registration was set multiple times: " + e.getMessage());
			}
		}
	}

	/**
	 * Add labEvent to the labEvents list.
	 * 
	 * @param labEvent
	 *            The needed labEvent
	 */
	public void addLabEvent(LabEvent labEvent) throws ElementAlreadyAddedException, UnsupportedOperationException,
			ClassCastException, NullPointerException, IllegalArgumentException {
		if (labEvents.contains(labEvent)) {
			labEvents.add(labEvent);
		} else {
			throw new ElementAlreadyAddedException("Lab event " + labEvent.getName()
					+ " already added to Lab events list.");
		}
	}

	/**
	 * Remove labEvent to the labEvents list.
	 * 
	 * @param labEvent
	 *            The needed labEvent
	 */
	public void removeLabEvent(LabEvent labEvent) throws ElementNotFoundException, UnsupportedOperationException,
			ClassCastException, NullPointerException {
		if (this.labEvents.remove(labEvent) == false) {
			throw new ElementNotFoundException("Lab Event " + labEvent.getName()
					+ " does not found in Lab events list.");
		}
	}

	/**
	 * Get the specified labEvent from labEvents.
	 * 
	 * @param name
	 *            The name of the labEvent (must be unique).
	 * @param year
	 *            The year of the labEvent.
	 * @return The needed labEvent
	 * @throws ElementNotFoundException
	 */
	public LabEvent getLabEvent(String name, String courseName, int courseYear) throws ElementNotFoundException {
		Iterator<LabEvent> it = labEvents.iterator();

		while (it.hasNext()) {
			LabEvent retVal = it.next();
			if (retVal.getName().equals(name) && retVal.getCourseName().equals(courseName)
					&& retVal.getCourseYear() == courseYear) {
				return retVal;
			}
		}
		throw new ElementNotFoundException("Lab event: " + name + " with course: " + courseName + "(" + courseYear
				+ ") does not found in the Lab events list.");
	}

	public boolean setLabEvent(String name, String oldName, String courseName, int courseYear,
			String[] multipleRegistrations, Date startTime, Date endTime) {
		return false;
	}

	public int getNumberOfLabevents() {
		return labEvents.size();
	}

	public LabEvent getLabEvent(int index) {
		return labEvents.get(index);
	}
}
