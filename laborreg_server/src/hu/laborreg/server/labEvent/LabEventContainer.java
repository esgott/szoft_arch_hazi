package hu.laborreg.server.labEvent;

import hu.laborreg.server.Configuration;
import hu.laborreg.server.computer.Computer;
import hu.laborreg.server.computer.ComputerContainer;
import hu.laborreg.server.course.Course;
import hu.laborreg.server.course.CourseContainer;
import hu.laborreg.server.db.DBConnectionHandler;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.exception.TimeSetException;
import hu.laborreg.server.exception.WrongIpAddressException;
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
import java.util.List;
import java.util.logging.Logger;

public class LabEventContainer {

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private List<LabEvent> labEvents;
	private final DBConnectionHandler dbConnection;
	private final StudentContainer students;
	private final ComputerContainer computers;
	private final CourseContainer courses;
	private final DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
	private final Configuration configuration;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * A container class which contains the labEvents.
	 */
	public LabEventContainer(DBConnectionHandler dbConnectionHandler, StudentContainer studentContainer,
			ComputerContainer computerContainer, CourseContainer courseContainer, Configuration config) {
		labEvents = new ArrayList<LabEvent>();
		dbConnection = dbConnectionHandler;
		students = studentContainer;
		computers = computerContainer;
		courses = courseContainer;
		configuration = config;
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

				Course c = courses.getCourse(courseName, courseYear);
				c.addLabEvent(labEvent);
			}
		} catch (SQLException e) {
			logger.severe("Failed to init LabEvents from DB: " + e.getMessage());
		} catch (TimeSetException e) {
			logger.warning("Error during date setting: " + e.getMessage());
		} catch (ParseException e) {
			logger.severe("Failed to parse date in DB: " + e.getMessage());
		} catch (ElementAlreadyAddedException | ElementNotFoundException e) {
			logger.warning("Failed to add LabEvent to Course: " + e.getMessage());
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
	public synchronized void addLabEvent(LabEvent labEvent) throws ElementNotFoundException,
			ElementAlreadyAddedException {
		Course c = courses.getCourse(labEvent.getCourseName(), labEvent.getCourseYear());
		if (!labEvents.contains(labEvent)) {
			labEvents.add(labEvent);
		} else {
			throw new ElementAlreadyAddedException("Lab event " + labEvent.getName()
					+ " already added to Lab events list.");
		}
		addToDB(labEvent);
		c.addLabEvent(labEvent);
	}

	private void addToDB(LabEvent labEvent) {
		try {
			String command = "INSERT INTO lab_event (name, part_of_course_name, part_of_course_year, start_time, end_time)"
					+ " VALUES(?, ?, ?, ?, ?)";
			PreparedStatement statement = dbConnection.createPreparedStatement(command);
			statement.setString(1, labEvent.getName());
			statement.setString(2, labEvent.getCourseName());
			statement.setInt(3, labEvent.getCourseYear());
			statement.setString(4, formatter.format(labEvent.getStartTime()));
			statement.setString(5, formatter.format(labEvent.getStopTime()));
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.severe("Failed to add new labEvent to db: " + e.getMessage());
			labEvents.remove(labEvent);
		}
	}

	/**
	 * Remove labEvent to the labEvents list.
	 * 
	 * @param labEvent
	 *            The needed labEvent
	 */
	public synchronized void removeLabEvent(LabEvent labEvent, boolean removeSignInAndMultipleRegistration)
			throws ElementNotFoundException {
		Course c = courses.getCourse(labEvent.getCourseName(), labEvent.getCourseYear());
		c.removeLabEvent(labEvent);
		if (this.labEvents.remove(labEvent) == false) {
			throw new ElementNotFoundException("Lab Event " + labEvent.getName()
					+ " does not found in Lab events list.");
		}
		removeFromDB(labEvent, removeSignInAndMultipleRegistration);
	}

	public synchronized void removeLabEventWithoutDeleteFromCourse(LabEvent labEvent) throws ElementNotFoundException {
		if (this.labEvents.remove(labEvent) == false) {
			throw new ElementNotFoundException("Lab Event " + labEvent.getName()
					+ " does not found in Lab events list.");
		}
		removeFromDB(labEvent, true);
	}

	private void removeFromDB(LabEvent labEvent, boolean removeSignInAndMultipleRegistration) {
		try {
			String command = "DELETE FROM lab_event WHERE name = ?";
			PreparedStatement statement = dbConnection.createPreparedStatement(command);
			statement.setString(1, labEvent.getName());
			statement.executeUpdate();
			if (removeSignInAndMultipleRegistration) {
				removeSignIns(labEvent);
				removeMultipleRegistrations(labEvent);
			}
		} catch (SQLException e) {
			logger.severe("Failed to delete labEvent from DB: " + e.getMessage());
		}
	}

	private void removeSignIns(LabEvent labEvent) throws SQLException {
		String command = "DELETE FROM signed WHERE lab_event_name = ?";
		PreparedStatement statement = dbConnection.createPreparedStatement(command);
		statement.setString(1, labEvent.getName());
		statement.executeUpdate();
	}

	private void removeMultipleRegistrations(LabEvent labEvent) throws SQLException {
		String command = "DELETE FROM multiple_registration_allowed WHERE lab_event_name = ?";
		PreparedStatement statement = dbConnection.createPreparedStatement(command);
		statement.setString(1, labEvent.getName());
		statement.executeUpdate();
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
	public synchronized LabEvent getLabEvent(String name) throws ElementNotFoundException {
		for (LabEvent labEvent : labEvents) {
			if (name.equals(labEvent.getName())) {
				return labEvent;
			}
		}

		throw new ElementNotFoundException("Lab event: " + name + ") does not found in the Lab events list.");
	}

	public synchronized boolean setLabEvent(String name, String oldName, String courseName, int courseYear,
			String[] multipleRegistrations, Date startTime, Date endTime) throws TimeSetException,
			WrongIpAddressException {
		boolean success = true;
		checkIpAddresses(multipleRegistrations);
		LabEvent labEvent = new LabEvent(name, courseName, courseYear, startTime, endTime);
		LabEvent oldLabEvent = null;
		try {
			oldLabEvent = getLabEvent(oldName);
		} catch (ElementNotFoundException e) {
			logger.fine("Failed to delete old element");
		}
		try {
			courses.getCourse(courseName, courseYear);
		} catch (ElementNotFoundException e2) {
			logger.warning("Wrong course given: " + e2.getMessage());
			return false;
		}

		if (!name.equals(oldName) || !courseName.equals(oldLabEvent.getCourseName())
				|| courseYear != oldLabEvent.getCourseYear() || !startTime.equals(oldLabEvent.getStartTime())
				|| !endTime.equals(oldLabEvent.getStopTime())) {
			try {
				if (oldLabEvent != null) {
					removeLabEvent(oldLabEvent, false);
				}
			} catch (ElementNotFoundException e1) {
				logger.fine("Failed to delete old labEvent: " + e1.getMessage());
			}
			try {
				addLabEvent(labEvent);
				if (oldLabEvent != null) {
					updateSignInfo(oldLabEvent, labEvent);
				}
			} catch (ElementAlreadyAddedException | ElementNotFoundException e) {
				logger.warning("Failed to add new element: " + e.getMessage());
				return false;
			}
		}
		try {
			updateMultipleRegistration(name, oldName, multipleRegistrations);
		} catch (SQLException | ElementNotFoundException e) {
			logger.warning("Failed to move multiple registration information to new labEvent: " + e.getMessage());
			success = false;
		}
		return success;
	}

	private void checkIpAddresses(String[] multipleRegistrations) throws WrongIpAddressException {
		for (String ipAddress : multipleRegistrations) {
			new Computer(ipAddress, configuration);
		}

	}

	private void updateSignInfo(LabEvent oldLabEvent, LabEvent labEvent) {
		if (oldLabEvent == null) {
			logger.fine("oldLabEvent was null");
			return;
		}
		try {
			String command = "UPDATE signed SET lab_event_name = ? WHERE lab_event_name = ?";
			PreparedStatement statement = dbConnection.createPreparedStatement(command);
			statement.setString(1, labEvent.getName());
			statement.setString(2, oldLabEvent.getName());
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.severe("Failed to move old sign in info to new lab event: " + e.getMessage());
		}
	}

	private void updateMultipleRegistration(String labEventName, String oldLabEventName, String[] multipleRegistrations)
			throws SQLException, ElementNotFoundException, WrongIpAddressException {
		String command = "DELETE FROM multiple_registration_allowed WHERE lab_event_name = ?";
		PreparedStatement statement = dbConnection.createPreparedStatement(command);
		statement.setString(1, oldLabEventName);
		statement.executeUpdate();
		LabEvent labEvent = getLabEvent(labEventName);
		labEvent.getRegisteredComputers().clear();
		for (String ipAddress : multipleRegistrations) {
			if (ipAddress.equals("")) {
				continue;
			}
			Computer computer = null;
			try {
				computer = computers.getComputer(ipAddress);
			} catch (ElementNotFoundException e) {
				computer = new Computer(ipAddress, configuration);
				try {
					computers.addComputer(computer);
				} catch (ElementAlreadyAddedException e1) {
					logger.warning("Failed to add new computer: " + e.getMessage());
				}
			}
			try {
				addComputerForMultipleRegistration(labEvent, computer);
			} catch (ElementAlreadyAddedException | SQLException e) {
				logger.warning("Failed to allow multiple registration: " + e.getMessage());
			}
		}
	}

	private void addComputerForMultipleRegistration(LabEvent labEvent, Computer computer)
			throws ElementAlreadyAddedException, SQLException {
		labEvent.allowMultipleRegistration(computer);
		String command = "INSERT INTO multiple_registration_allowed (lab_event_name, computer_ip_address) VALUES (?, ?)";
		PreparedStatement statement = dbConnection.createPreparedStatement(command);
		statement.setString(1, labEvent.getName());
		statement.setString(2, computer.getIpAddress());
		statement.executeUpdate();
	}

	public synchronized boolean signInForLabEvent(String labEventName, String neptun, boolean forced)
			throws ElementNotFoundException, ElementAlreadyAddedException, TimeSetException {
		LabEvent labEvent = getLabEvent(labEventName);
		if (labEvent.isSignInAcceptable() == false) {
			logger.warning("Time window is closed now for LabEvent: " + labEventName);
			throw new TimeSetException("Time window is closed to LabEvent: " + labEventName);
		}

		Student student;
		try {
			student = students.getStudent(neptun);
		} catch (ElementNotFoundException e) {
			if (forced) {
				logger.info("Failed to get student from StudentsList. Create a new one.");
				student = new Student(neptun);
				students.addStudent(student);
			} else {
				return false;
			}
		}
		labEvent.signInStudent(student);
		try {
			String command = "INSERT INTO signed (lab_event_name, student_neptun) VALUES (?, ?)";
			PreparedStatement statement = dbConnection.createPreparedStatement(command);
			statement.setString(1, labEvent.getName());
			statement.setString(2, student.getNeptunCode());
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.warning("Failed to record sign in in DB: " + e.getMessage());
			throw new ElementNotFoundException("Failed to record sign in in DB");
		}
		return true;
	}

	public synchronized int getNumberOfLabevents() {
		return labEvents.size();
	}

	public synchronized LabEvent getLabEvent(int index) {
		return labEvents.get(index);
	}
}
