package hu.laborreg.server.labEvent;

import hu.laborreg.server.computer.Computer;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.TimeSetException;
import hu.laborreg.server.student.Student;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class LabEvent {

	private String name;
	private String courseName;
	private int courseYear;
	private String startTime;
	private String stopTime;
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
	public LabEvent(String name, String courseName, int courseYear, String startTime, String stopTime)
			throws TimeSetException, ParseException {
		Calendar currentTime = Calendar.getInstance();
		if (currentTime.get(Calendar.HOUR_OF_DAY) < 23) {
			this.startTime = (currentTime.get(Calendar.HOUR_OF_DAY) + 1) + ":" + currentTime.get(Calendar.MINUTE);
			this.stopTime = (currentTime.get(Calendar.HOUR_OF_DAY) + 2) + ":" + currentTime.get(Calendar.MINUTE);
		} else {
			this.startTime = "23:58";
			this.stopTime = "23:59";
		}

		this.name = name;
		this.courseName = courseName;
		this.courseYear = courseYear;

		setStartAndStopTime(startTime, stopTime);

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
	public String getStartTime() {
		return this.startTime;
	}

	/**
	 * Returns the stop time of the LabEvent.
	 * 
	 * @return The stop time of the LabEvent in "HH:MM" format.
	 */
	public String getStopTime() {
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
	public void setStartAndStopTime(String startTime, String stopTime) throws TimeSetException, ParseException {
		if (checkTime(startTime, stopTime) == 999) {
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
	private int checkTime(String startTime, String stopTime) throws TimeSetException, ParseException {
		DateFormat df = new SimpleDateFormat("HH:mm");

		Calendar currentTime = Calendar.getInstance();

		Calendar givenStartTime = Calendar.getInstance();
		Calendar givenStopTime = Calendar.getInstance();
		Calendar storedStartTime = Calendar.getInstance();
		Calendar storedStopTime = Calendar.getInstance();

		givenStartTime.setTime(df.parse(startTime));
		givenStopTime.setTime(df.parse(stopTime));
		storedStartTime.setTime(df.parse(this.startTime));
		storedStopTime.setTime(df.parse(this.stopTime));

		if (givenStartTime.get(Calendar.HOUR_OF_DAY) >= givenStopTime.get(Calendar.HOUR_OF_DAY)) {
			if (givenStartTime.get(Calendar.MINUTE) >= givenStopTime.get(Calendar.MINUTE)) {
				throw new TimeSetException("Start time: " + startTime + " is later then stop time: " + stopTime);
			}
		}
		if (currentTime.get(Calendar.HOUR_OF_DAY) >= storedStartTime.get(Calendar.HOUR_OF_DAY)) {
			if (currentTime.get(Calendar.MINUTE) >= storedStartTime.get(Calendar.MINUTE)) {
				throw new TimeSetException("Set time is not enabled, because the Lab event is currently ongoing.");
			}
		}
		if (currentTime.get(Calendar.HOUR_OF_DAY) >= storedStartTime.get(Calendar.HOUR_OF_DAY)) {
			if (currentTime.get(Calendar.MINUTE) >= storedStartTime.get(Calendar.MINUTE)) {
				throw new TimeSetException("Set time is not enabled, because the Lab event is currently ongoing.");
			}
		}
		if ((currentTime.get(Calendar.HOUR_OF_DAY) >= givenStartTime.get(Calendar.HOUR_OF_DAY))
				&& (currentTime.get(Calendar.HOUR_OF_DAY) <= givenStopTime.get(Calendar.HOUR_OF_DAY))) {
			if ((currentTime.get(Calendar.MINUTE) >= givenStartTime.get(Calendar.MINUTE))
					&& (currentTime.get(Calendar.MINUTE) <= givenStopTime.get(Calendar.MINUTE))) {
				throw new TimeSetException("Set time is not enabled, because the Lab event is currently ongoing.");
			}
		}
		if (currentTime.get(Calendar.HOUR_OF_DAY) >= storedStopTime.get(Calendar.HOUR_OF_DAY)) {
			if (currentTime.get(Calendar.MINUTE) >= storedStopTime.get(Calendar.MINUTE)) {
				throw new TimeSetException("Set time is not enabled, because the Lab event is finished.");
			}
		}
		if (currentTime.get(Calendar.HOUR_OF_DAY) >= givenStopTime.get(Calendar.HOUR_OF_DAY)) {
			if (currentTime.get(Calendar.MINUTE) >= givenStopTime.get(Calendar.MINUTE)) {
				throw new TimeSetException("Set time is not enabled, because the Lab event is finished.");
			}
		}

		return 999;
	}
}
