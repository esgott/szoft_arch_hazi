package hu.laborreg.server.labEvent;

import hu.laborreg.server.computer.Computer;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.TimeSetException;
import hu.laborreg.server.student.Student;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.text.ParseException;

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
	public String getName() {
		return this.name;
	}

	/**
	 * Return the name of Course which this LabEvent is allocated to.
	 * 
	 * @return The name of the Course which this LabEvent is allocated to.
	 */
	public String getCourseName() {
		return this.courseName;
	}

	/**
	 * Return the year of Course which this LabEvent is allocated to.
	 * 
	 * @return The year of the Course which this LabEvent is allocated to.
	 */
	public int getCourseYear() {
		return this.courseYear;
	}

	/**
	 * Returns the start time of the LabEvent.
	 * 
	 * @return The start time of the LabEvent in "HH:MM" format.
	 */
	public Date getStartTime() {
		return this.startTime;
	}

	/**
	 * Returns the stop time of the LabEvent.
	 * 
	 * @return The stop time of the LabEvent in "HH:MM" format.
	 */
	public Date getStopTime() {
		return this.stopTime;
	}

	/**
	 * Return the list of already signed in students to this LabEvent.
	 * 
	 * @return The list of already signed in students to this LabEvent.
	 */
	public Set<Student> getSignedInStudents() {
		return this.signedInStudents;
	}

	/**
	 * Return the list of the registered computers (which is enabled to have
	 * multiple registration) to this LabEvent.
	 * 
	 * @return The list of the registered computers to this LabEvent.
	 */
	public Set<Computer> getRegisteredComputers() {
		return this.registeredComputers;
	}

	/**
	 * Sign in Student to the Lab event participants list.
	 * 
	 * @param student
	 *            The Students who wants to sign in.
	 */
	public void signInStudent(Student student) throws ElementAlreadyAddedException, UnsupportedClassVersionError,
			ClassCastException, NullPointerException, IllegalArgumentException {
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
	public void allowMultipleRegistration(Computer computer) throws ElementAlreadyAddedException,
			UnsupportedClassVersionError, ClassCastException, NullPointerException, IllegalArgumentException {
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
	public void setStartAndStopTime(Date startTime, Date stopTime, boolean isCalledFromConstructor) throws TimeSetException {
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
	private int checkTime(Date startTime, Date stopTime, boolean isCalledFromConstructor) throws TimeSetException {

		Date currentTime = Calendar.getInstance().getTime();

		Calendar newStartTime = Calendar.getInstance();
		newStartTime.setTime(startTime);
		Calendar newStopTime = Calendar.getInstance();
		newStopTime.setTime(stopTime);

		if(stopTime.before(startTime)) {
			throw new TimeSetException("Start time: " + newStartTime.get(Calendar.HOUR_OF_DAY) + ":" + newStartTime.get(Calendar.MINUTE) + 
					" is later then stop time: " + newStopTime.get(Calendar.HOUR_OF_DAY) + ":" + newStopTime.get(Calendar.MINUTE));
		}

		if(currentTime.after(startTime) && currentTime.before(stopTime)) {
			throw new TimeSetException("Set time is not enabled, because the Lab event is currently ongoing or the start time of of the lab event is in the past.");
		}
		
		if(currentTime.after(stopTime)) {
			throw new TimeSetException("Set time is not enabled, because the Lab event is finished or the stop time of the lab event is in the past.");
		}
		
		if(isCalledFromConstructor == false)
		{
			if(currentTime.after(this.startTime)) {
				throw new TimeSetException("Set time is not enabled, because the Lab event is currently ongoing.");
			}
			
			if(currentTime.after(this.stopTime)){
				throw new TimeSetException("Set time is not enabled, because the Lab event is finished.");
			}
		}
		return 999;
	}
}
