package hu.laborreg.server.course;

import hu.laborreg.server.db.DBConnectionHandler;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.student.Student;
import hu.laborreg.server.student.StudentContainer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class CourseContainer {

	private List<Course> courses;
	private DBConnectionHandler dbConnection;
	private StudentContainer studentContainer;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * A container class which contains the courses.
	 */
	public CourseContainer(DBConnectionHandler dbConnectionHandler, StudentContainer students) {
		courses = new ArrayList<Course>();
		dbConnection = dbConnectionHandler;
		studentContainer = students;
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
				initRegisteredStudents(course);
			}
		} catch (SQLException e) {
			logger.severe("Failed to init Courses from DB: " + e.getMessage());
		}
	}

	private void initRegisteredStudents(Course course) throws SQLException {
		String command = "SELECT * FROM registered WHERE course_year = ? AND course_name = ?";
		PreparedStatement statement = dbConnection.createPreparedStatement(command);
		statement.setInt(1, course.getYear());
		statement.setString(2, course.getName());
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			String neptun = result.getString("student_neptun");
			Student student;
			try {
				student = studentContainer.getStudent(neptun);
				course.registerStudent(student);
			} catch (ElementNotFoundException | ElementAlreadyAddedException e) {
				logger.warning("Failed to register student during init: " + e.getMessage());
			}
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
			removeRegitrations(course);
		} catch (SQLException e) {
			logger.severe("Failed to delete course from DB: " + e.getMessage());
		}
	}

	private void removeRegitrations(Course course) throws SQLException {
		String command = "DELETE FROM registered WHERE course_name = ? AND course_year = ?";
		PreparedStatement deleteStatement = dbConnection.createPreparedStatement(command);
		deleteStatement.setString(1, course.getName());
		deleteStatement.setInt(2, course.getYear());
		deleteStatement.executeUpdate();
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

	/**
	 * Get the course at the specified position
	 * 
	 * @param index
	 *            position in list
	 * @return course
	 */
	public Course getCourse(int index) {
		return courses.get(index);
	}

	/**
	 * Registers a student to a course, and writes the changes to the DB.
	 * 
	 * @param course
	 *            course to register on
	 * @param student
	 *            student to register
	 * @throws ElementAlreadyAddedException
	 *             if the student is already registered on the course
	 */
	public void registerStudentToCourse(Course course, Student student) throws ElementAlreadyAddedException {
		course.registerStudent(student);
		registerInDb(course, student);
	}

	private void registerInDb(Course course, Student student) {
		try {
			String command = "INSERT INTO registered(course_name, course_year, student_neptun) VALUES(?, ?, ?)";
			PreparedStatement statement = dbConnection.createPreparedStatement(command);
			statement.setString(1, course.getName());
			statement.setInt(2, course.getYear());
			statement.setString(3, student.getNeptunCode());
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.severe("Failed to add new registration to db: " + e.getMessage());
			try {
				course.unregisterStudent(student);
			} catch (ElementNotFoundException e1) {
				logger.warning("Failed to unregister student: " + e.getMessage());
			}
		}
	}

	/**
	 * Unregisters a student from a course, and writes the changes to the DB.
	 * 
	 * @param course
	 *            course to unregister from
	 * @param student
	 *            student to unregister
	 * @throws ElementNotFoundException
	 *             if the student was not registered to this course
	 */
	public void unregisterStudentFromCourse(Course course, Student student) throws ElementNotFoundException {
		course.unregisterStudent(student);
		unregisterInDb(course, student);
	}

	private void unregisterInDb(Course course, Student student) {
		try {
			String command = "DELETE FROM registered WHERE course_name = ? AND course_year = ? AND student_neptun = ?";
			PreparedStatement statement = dbConnection.createPreparedStatement(command);
			statement.setString(1, course.getName());
			statement.setInt(2, course.getYear());
			statement.setString(3, student.getNeptunCode());
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.severe("Failed to delete registration from DB: " + e.getMessage());
		}
	}

	public boolean setData(String name, String oldName, int year, int oldYear, String[] neptuns) {
		Course course = new Course(name, year);
		Course oldCourse = new Course(oldName, oldYear);
		boolean success = true;
		if (!course.equals(oldCourse)) {
			try {
				removeCourse(oldCourse);
			} catch (ElementNotFoundException e) {
				logger.fine("Failed to delete old element");
			}
			try {
				addCourse(course);
			} catch (ElementAlreadyAddedException e) {
				logger.warning("Failed to add new Course: " + e.getMessage());
			}
		}
		try {
			updateStudents(course, neptuns);
		} catch (SQLException | ElementNotFoundException e) {
			logger.severe("Failed to update registration in DB: " + e.getMessage());
			success = false;
		}
		return success;
	}

	private void updateStudents(Course course, String[] neptuns) throws SQLException, ElementNotFoundException {
		course = getCourse(course.getName(), course.getYear());
		String command = "DELETE FROM registered WHERE course_name = ? AND course_year = ?";
		PreparedStatement deleteStatement = dbConnection.createPreparedStatement(command);
		deleteStatement.setString(1, course.getName());
		deleteStatement.setInt(2, course.getYear());
		deleteStatement.executeUpdate();
		Set<Student> students = course.getRegisteredStudents();
		students.clear();
		for (String neptun : neptuns) {
			if (neptun.equals("")) {
				continue;
			}
			Student current = null;
			try {
				current = studentContainer.getStudent(neptun);
			} catch (ElementNotFoundException e) {
				current = new Student(neptun);
				try {
					studentContainer.addStudent(current);
				} catch (ElementAlreadyAddedException e1) {
					logger.warning("Failed to add new student " + e1.getMessage());
				}
			}
			try {
				registerStudentToCourse(course, current);
			} catch (ElementAlreadyAddedException e) {
				logger.warning("Failed to register student " + e.getMessage());
			}
		}
	}

}
