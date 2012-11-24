package hu.laborreg.server.labEvent;

import hu.laborreg.server.db.DBConnectionHandler;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.exception.TimeSetException;
import hu.laborreg.server.student.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class LabEventContainer {

	private List<LabEvent> labEvents;
	private final DBConnectionHandler dbConnection;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * A container class which contains the labEvents.
	 */
	public LabEventContainer(DBConnectionHandler dbConnectionHandler) {
		labEvents = new ArrayList<LabEvent>();
		dbConnection = dbConnectionHandler;
//		initFromDB();
	}

	private void initFromDB() throws TimeSetException{
		try {
			PreparedStatement statement = dbConnection.createPreparedStatement("SELECT * FROM lab_event");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				String name = result.getString("name");
				String courseName = result.getString("part_of_course_name");
				int courseYear = result.getInt("part_of_course_year");
				
				Date startTime = result.getTimestamp("start_time");
				Date endTime = result.getTimestamp("end_time");
				
				LabEvent labEvent = new LabEvent(name, courseName, courseYear, startTime, endTime);
				boolean success = labEvents.add(labEvent);
				if (!success) {
					logger.warning("LabEvent instance found multiple times in the DB: " + labEvent.getName() +
							" for course: " + labEvent.getCourseName() + "," +labEvent.getCourseYear());
				}
				//TODO finish connecting to DB
			}
		} catch (SQLException e) {
			logger.severe("Failed to init LabEvents from DB: " + e.getMessage());
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

	public int getNumberOfLabevents() {
		return labEvents.size();
	}

	public LabEvent getLabEvent(int index) {
		return labEvents.get(index);
	}
}
