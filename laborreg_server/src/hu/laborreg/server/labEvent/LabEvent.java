package hu.laborreg.server.labEvent;

import hu.laborreg.server.computer.Computer;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.TimeSetException;
import hu.laborreg.server.student.Student;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class LabEvent {

	private String name;
	private String courseName;
	private int courseYear;
	private Date startTime;
	private Date stopTime;
	private Set<Student> signedInStudents;
	private Set<Computer> registeredComputers;

	/**
	 * This class represents the lab events. The lab leader enables to create
	 * LabEvents and add them to a Course. The start and the stop time have to
	 * be added at the creation by the lab leader. Students can sing in to this
	 * lab event during this time interval. The lab leader can modify this time
	 * interval before the LabEvent started.
	 * 
	 * @param name
	 *            The name of the LabEvent.
	 * @param courseName
	 *            The name of the course where the LabEvent is added to.
	 * @param startTime
	 *            The start time of the LabEvent in "HH:MM" format.
	 * @param stopTime
	 *            The stop time of the LabEvent in "HH:MM" format.
	 * @throws ParseException
	 * @throws TimeSetException
	 */
	public LabEvent(String name, String courseName, int courseYear, Date startTime, Date stopTime)
			throws TimeSetException {

		this.name = name;
		this.courseName = courseName;
		this.courseYear = courseYear;

		setStartAndStopTime(startTime, stopTime, true);

		signedInStudents = new HashSet<Student>();
		registeredComputers = new HashSet<Computer>();
	}

	/**
	 * Returns the name of the LabEvent.
	 * 
	 * @return The name of the LabEvent.
	 */
	public synchronized String getName() {
		return this.name;
	}

	/**
	 * Return the name of Course which this LabEvent is allocated to.
	 * 
	 * @return The name of the Course which this LabEvent is allocated to.
	 */
	public synchronized String getCourseName() {
		return this.courseName;
	}

	/**
	 * Return the year of Course which this LabEvent is allocated to.
	 * 
	 * @return The year of the Course which this LabEvent is allocated to.
	 */
	public synchronized int getCourseYear() {
		return this.courseYear;
	}

	/**
	 * Returns the start time of the LabEvent.
	 * 
	 * @return The start time of the LabEvent in "HH:MM" format.
	 */
	public synchronized Date getStartTime() {
		return this.startTime;
	}

	/**
	 * Returns the stop time of the LabEvent.
	 * 
	 * @return The stop time of the LabEvent in "HH:MM" format.
	 */
	public synchronized Date getStopTime() {
		return this.stopTime;
	}

	/**
	 * Return the list of already signed in students to this LabEvent.
	 * 
	 * @return The list of already signed in students to this LabEvent.
	 */
	public synchronized Set<Student> getSignedInStudents() {
		return this.signedInStudents;
	}
	
	public synchronized String getSignedInStudentsAsString() {
		StringBuilder result = new StringBuilder();
		for (Student student : signedInStudents) {
			result.append(student.getNeptunCode());
			result.append(", ");
		}
		return result.toString();
	}

	/**
	 * Return the list of the registered computers (which is enabled to have
	 * multiple registration) to this LabEvent.
	 * 
	 * @return The list of the registered computers to this LabEvent.
	 */
	public synchronized Set<Computer> getRegisteredComputers() {
		return this.registeredComputers;
	}
	
	public synchronized String getRegisteredComputersAsString() {
		StringBuilder result = new StringBuilder();
		for (Computer computer : registeredComputers) {
			result.append(computer.getIpAddress());
			result.append(", ");
		}
		return result.toString();
	}

	/**
	 * Sign in Student to the Lab event participants list.
	 * 
	 * @param student
	 *            The Students who wants to sign in.
	 */
	public synchronized void signInStudent(Student student) throws ElementAlreadyAddedException {
		if (this.signedInStudents.add(student) == false) {
			throw new ElementAlreadyAddedException("Student: " + student.getNeptunCode()
					+ " already signed in to this Lab event.");
		}
	}

	/**
	 * Allow multiple registration to the specified Computer. After this more
	 * than one Students can sign in from this computer.
	 * 
	 * @param computer
	 *            The needed Computer.
	 * @return If computer added to the list: COMPUTER_ADDED(0) if computer
	 *         already added to the list: COMPUTER_ALREADY_ADDED(1)
	 */
	public synchronized void allowMultipleRegistration(Computer computer) throws ElementAlreadyAddedException {
		if (this.registeredComputers.add(computer) == false) {
			throw new ElementAlreadyAddedException("Computer: " + computer.getIpAddress() + " already added to list.");
		}
	}

	/**
	 * Set start and stop time of this LabEvent.
	 * 
	 * @param startTime
	 *            Start time of the LabEvent in "HH:MM" format.
	 * @param stopTime
	 *            Stop time of the LabEvent in "HH:MM" format.
	 * @return If the setting correct: TIME_IS_OK(0) If the stop time is later
	 *         than start time: STARTTIME_IS_LATER_THAN_STOPTIME(1) If LabEvent
	 *         is currently ongoing: LABEVENT_IS_ONGOING(2) If LabEvent is
	 *         already finished: LABEVENT_FINISHED(3)
	 */
	public synchronized void setStartAndStopTime(Date startTime, Date stopTime, boolean isCalledFromConstructor) throws TimeSetException {
		if (checkTime(startTime, stopTime, isCalledFromConstructor) == 999) {
			this.startTime = startTime;
			this.stopTime = stopTime;
		}
	}

	/**
	 * Check the given times.
	 * 
	 * @param startTime
	 *            The given start time.
	 * @param stopTime
	 *            The given stop time.
	 */
	private synchronized int checkTime(Date startTime, Date stopTime, boolean isCalledFromConstructor) throws TimeSetException {
		Calendar newStartTime = Calendar.getInstance();
		newStartTime.setTime(startTime);
		Calendar newStopTime = Calendar.getInstance();
		newStopTime.setTime(stopTime);

		if(stopTime.before(startTime)) {
			throw new TimeSetException("Start time: " + newStartTime.get(Calendar.HOUR_OF_DAY) + ":" + newStartTime.get(Calendar.MINUTE) + 
					" is later then stop time: " + newStopTime.get(Calendar.HOUR_OF_DAY) + ":" + newStopTime.get(Calendar.MINUTE));
		}
		return 999;
	}
	
	public boolean isSignInAcceptable()
	{
		Date currentTime = new Date();
		if(currentTime.after(startTime) && currentTime.before(stopTime)) {
			return true;
		}
		return false;
	}
	
	@Override
	public synchronized boolean equals(Object other) {
		if (this == other) {
			return true;
		} else if (!(other instanceof LabEvent)) {
			return false;
		}
		LabEvent labEvent = (LabEvent) other;
		return labEvent.name.equals(name);
	}
}
