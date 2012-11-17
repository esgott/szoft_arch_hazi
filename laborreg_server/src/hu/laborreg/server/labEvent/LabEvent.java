package hu.laborreg.server.labEvent;

import hu.laborreg.server.computer.Computer;
import hu.laborreg.server.student.Student;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class LabEvent {
	
	public final int TIME_IS_OK = 0;
	public final int STARTTIME_IS_LATER_THAN_STOPTIME = 1;
	public final int LABEVENT_IS_ONGOING = 2;
	public final int LABEVENT_FINISHED = 3;
	
	private String name;
	private String courseName;
	private String startTime;
	private String stopTime;
	private boolean isActive; //TODO Do we need this?
	private Set<Student> signedInStudents;
	private Set<Computer> registeredComputers;
	
	
	/**
	 * This class represents the lab events. The lab leader enables to create LabEvents and add them to a Course.
	 * The start and the stop time have to be added at the creation by the lab leader.
	 * Students can sing in to this lab event during this time interval.
	 * The lab leader can modify this time interval before the LabEvent started.
	 * @param name The name of the LabEvent.
	 * @param courseName The name of the course where the LabEvent is added to.
	 * @param startTime The start time of the LabEvent in "HH:MM" format.
	 * @param stopTime The stop time of the LabEvent in "HH:MM" format.
	 */
	public LabEvent(String name, String courseName, String startTime, String stopTime)
	{
		this.name = name;
		this.courseName = courseName;
		this.startTime = startTime;
		this.stopTime = stopTime;
		
		signedInStudents = new HashSet<Student>();
		registeredComputers = new HashSet<Computer>();
	}

	/**
	 * Returns the name of the LabEvent.
	 * @return The name of the LabEvent.
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Return the name of Course which this LabEvent is allocated to.
	 * @return The name of the Course which this LabEvent is allocated to.
	 */
	public String getCourseName()
	{
		return this.courseName;
	}
	
	/**
	 * Returns the start time of the LabEvent.
	 * @return The start time of the LabEvent in "HH:MM" format.
	 */
	public String getStartTime()
	{
		return this.startTime;
	}
	
	/**
	 * Returns the stop time of the LabEvent.
	 * @return The stop time of the LabEvent in "HH:MM" format.
	 */
	public String getStopTime()
	{
		return this.stopTime;
	}
	
	/**
	 * Return the list of already signed in students to this LabEvent. 
	 * @return The list of already signed in students to this LabEvent.
	 */
	public Set<Student> getSignedInStudents()
	{
		return this.signedInStudents;
	}
	
	/**
	 * Return the list of the registered computers (which is enabled to have multiple registration) to this LabEvent.
	 * @return The list of the registered computers to this LabEvent.
	 */
	public Set<Computer> getRegisteredComputers()
	{
		return this.registeredComputers;
	}
	
	/**
	 * Allow multiple registration to the specified Computer. After this more than one Students can sign in from this computer.
	 * @param computer The needed Computer.
	 */
	public void allowMultipleRegistration(Computer computer)
	{
		try
		{
			if(this.registeredComputers.add(computer) == false)
			{
				//TODO  registeredComputers already contain this computer. Display error in error window?
			}
		}
		catch(UnsupportedOperationException e)
		{
			e.printStackTrace();
		}
		catch(ClassCastException e)
		{
			e.printStackTrace();
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Set start and stop time of this LabEvent.
	 * @param startTime Start time of the LabEvent in "HH:MM" format.
	 * @param stopTime Stop time of the LabEvent in "HH:MM" format.
	 * @return If the setting correct:	TIME_IS_OK(0)
	 *			If the stop time is later than start time: STARTTIME_IS_LATER_THAN_STOPTIME(1)
	 *			If LabEvent is currently ongoing: LABEVENT_IS_ONGOING(2)
	 *			If LabEvent is already finished: LABEVENT_FINISHED(3)
	 */
	public int setStartAndStopTime(String startTime, String stopTime)
	{
		int retVal = checkTime(startTime,stopTime);
		if(retVal == 0)
		{
			this.startTime = startTime;
			this.stopTime = stopTime;
		}
		
		return retVal;
	}
	
	/**
	 * Check the given times.
	 * @param startTime The given start time. 
	 * @param stopTime The given stop time.
	 * @return If the setting correct:	TIME_IS_OK(0)
	 *			If the stop time is later than start time: STARTTIME_IS_LATER_THAN_STOPTIME(1)
	 *			If LabEvent is currently ongoing: LABEVENT_IS_ONGOING(2)
	 *			If LabEvent is already finished: LABEVENT_FINISHED(3)
	 */
	public int checkTime(String startTime, String stopTime)
	{
		DateFormat df = new SimpleDateFormat("HH:mm");

		Calendar currentTime = Calendar.getInstance();
		Calendar givenStartTime = Calendar.getInstance();
		Calendar givenStopTime = Calendar.getInstance();
		Calendar storedStartTime = Calendar.getInstance();
		Calendar storedStopTime = Calendar.getInstance();
		
		try
		{
			givenStartTime.setTime(df.parse(startTime));
			givenStopTime.setTime(df.parse(stopTime));
			storedStartTime.setTime(df.parse(this.startTime));
			storedStopTime.setTime(df.parse(this.stopTime));
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		
		if(givenStartTime.get(Calendar.HOUR_OF_DAY) >= givenStopTime.get(Calendar.HOUR_OF_DAY))
		{
			if(givenStartTime.get(Calendar.MINUTE) >= givenStopTime.get(Calendar.MINUTE))
			{
				return STARTTIME_IS_LATER_THAN_STOPTIME;
			}
		}
		if(currentTime.get(Calendar.HOUR_OF_DAY) >= storedStartTime.get(Calendar.HOUR_OF_DAY))
		{
			if(currentTime.get(Calendar.MINUTE) >= storedStartTime.get(Calendar.MINUTE))
			{
				return LABEVENT_IS_ONGOING;
			}
		}
		if(currentTime.get(Calendar.HOUR_OF_DAY) >= storedStopTime.get(Calendar.HOUR_OF_DAY))
		{
			if(currentTime.get(Calendar.MINUTE) >= storedStopTime.get(Calendar.MINUTE))
			{
				return LABEVENT_FINISHED;
			}
		}
		
		return TIME_IS_OK;
	}
}
