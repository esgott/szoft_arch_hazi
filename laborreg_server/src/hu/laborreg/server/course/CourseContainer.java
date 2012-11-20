package hu.laborreg.server.course;

import hu.laborreg.server.db.DBConnectionHandler;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CourseContainer {

	private List<Course> courses;
	private DBConnectionHandler dbConnection;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * A container class which contains the courses.
	 */
	public CourseContainer(DBConnectionHandler dbConnectionHandler) {
		courses = new ArrayList<Course>();
		dbConnection = dbConnectionHandler;
		initFromDB();
	}

	private void initFromDB() {
		try {
			PreparedStatement statement = dbConnection.createPreparedStatement("SELECT * FROM course");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				String name = result.getString("name");
				int year = result.getInt("year");
				Course course = new Course(name, year);
				if (!courses.contains(course)) {
					courses.add(course);
				} else {
					logger.warning("Multiple instances in Course table: " + name + " " + year);
				}
			}
		} catch (SQLException e) {
			logger.severe("Failed to init Courses from DB: " + e.getMessage());
		}
	}

	/**
	 * Add Course to the courses list.
	 * 
	 * @param course
	 *            The needed course
	 */
	public void addCourse(Course course) throws ElementAlreadyAddedException {
		if (!courses.contains(course)) {
			courses.add(course);
		} else {
			throw new ElementAlreadyAddedException("Course" + course.getName() + "(" + course.getYear()
					+ ") already added to the Courses list.");
		}
		addToDB(course);
	}

	private void addToDB(Course course) {
		try {
			String command = "INSERT INTO course(year, name) VALUES(?, ?)";
			PreparedStatement statement = dbConnection.createPreparedStatement(command);
			statement.setInt(1, course.getYear());
			statement.setString(2, course.getName());
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.severe("Failed to add new course to db: " + e.getMessage());
			courses.remove(course);
		}
	}

	/**
	 * Remove course to the courses list.
	 * 
	 * @param course
	 *            The needed course
	 */
	public void removeCourse(Course course) throws ElementNotFoundException {
		if (this.courses.remove(course) == false) {
			throw new ElementNotFoundException("Course" + course.getName() + "(" + course.getYear()
					+ ") does not found in the Courses list.");
		}
		removeFromDB(course);
	}

	private void removeFromDB(Course course) {
		try {
			String command = "DELETE FROM course WHERE name = ? AND year = ?";
			PreparedStatement statement = dbConnection.createPreparedStatement(command);
			statement.setString(1, course.getName());
			statement.setInt(2, course.getYear());
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.severe("Failed to delete course from DB: " + e.getMessage());
		}
	}

	/**
	 * Get the specified course from courses.
	 * 
	 * @param name
	 *            The name of the course (must be unique).
	 * @param year
	 *            The year of the course.
	 * @return The needed course
	 */
	public Course getCourse(String name, int year) throws ElementNotFoundException {
		for (Course course : courses) {
			if (course.getName().equals(name) && course.getYear() == year) {
				return course;
			}
		}
		throw new ElementNotFoundException("Course: " + name + "(" + year + ") does not found in the Courses list.");
	}

	/**
	 * Get the number of elements in this container
	 * 
	 * @return Number of elements
	 */
	public int getNumberOfCourses() {
		return courses.size();
	}

	public Course getCourse(int index) {
		return courses.get(index);
	}
}
