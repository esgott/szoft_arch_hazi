package hu.laborreg.server.student;

import hu.laborreg.server.db.DBConnectionHandler;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

public class StudentContainer {

	private Set<Student> students;
	private final DBConnectionHandler dbConnection;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * A container class which contains the Students.
	 */
	public StudentContainer(DBConnectionHandler dbConectionHandler) {
		students = new HashSet<Student>();
		dbConnection = dbConectionHandler;
		initFromDB();
	}

	private void initFromDB() {
		try {
			PreparedStatement statement = dbConnection.createPreparedStatement("SELECT * FROM student");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				String neptun = result.getString("neptun");
				Student student = new Student(neptun);
				boolean success = students.add(student);
				if (!success) {
					logger.warning("Student instance found multiple times in the DB: " + neptun);
				}
			}
		} catch (SQLException e) {
			logger.severe("Could init Students from database: " + e.getMessage());
		}
	}

	/**
	 * Add Student to the students list.
	 * 
	 * @param student
	 *            The needed Student.
	 */
	public void addStudent(Student student) throws ElementAlreadyAddedException {
		if (this.students.add(student) == false) {
			throw new ElementAlreadyAddedException("Student " + student.getNeptunCode()
					+ " already added to Students list.");
		}

		addToDB(student);
	}

	private void addToDB(Student student) {
		try {
			String command = "INSERT INTO student(neptun) VALUES(?)";
			PreparedStatement statement = dbConnection.createPreparedStatement(command);
			statement.setString(1, student.getNeptunCode());
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.severe("Failed to add new student to db: " + e.getMessage());
			students.remove(student);
		}
	}

	/**
	 * Remove Student to the Students list.
	 * 
	 * @param student
	 *            The needed Student.
	 */
	public void removeStudent(Student student) throws ElementNotFoundException, UnsupportedOperationException,
			ClassCastException, NullPointerException {
		if (this.students.remove(student) == false) {
			throw new ElementNotFoundException("Student " + student.getNeptunCode()
					+ " does not found in Students list.");
		}
		removeFromDB(student);
	}

	private void removeFromDB(Student student) {
		try {
			String command = "DELETE FROM student WHERE neptun = ?";
			PreparedStatement statement = dbConnection.createPreparedStatement(command);
			statement.setString(1, student.getNeptunCode());
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.severe("Failed to delete student from DB: " + e.getMessage());
		}
	}

	/**
	 * Get the specified Student from students.
	 * 
	 * @param neptunCode
	 *            The Neptun code of the Student (must be unique).
	 * @return The needed Student.
	 */
	public Student getStudent(String neptunCode) throws ElementNotFoundException {
		for (Student student : students) {
			if (neptunCode.equalsIgnoreCase(student.getNeptunCode()))
				return student;
		}
		throw new ElementNotFoundException("Student: " + neptunCode + "does not found in the Students list.");
	}
}
