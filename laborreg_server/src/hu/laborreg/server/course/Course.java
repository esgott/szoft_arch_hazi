package hu.laborreg.server.course;

import hu.laborreg.server.Constants;
import hu.laborreg.server.labEvent.LabEvent;
import hu.laborreg.server.student.Student;

import java.util.HashSet;
import java.util.Set;

public class Course {
	
	private String name;
	private int year;
	private Set<Student> registeredStudents;
	private Set<LabEvent> labEvents;
	
	/**
	 * This class represents the courses. The Lab leader can create and delete Courses, add and remove LabEvents to and from Courses, add and remove Students to and from Courses.
	 * If the Lab leader doesn't add a Student to the Course Student list, then this Student will get an alarm when he or she sign in to the LabEvent (which allocated to the Course). 
	 * @param name Name of the course.
	 * @param year Year of the course.
	 */
	public Course(String name, int year)
	{
		this.name = name;
		this.year = year;
		
		registeredStudents = new HashSet<Student>();
		labEvents = new HashSet<LabEvent>();
	}
	
	/**
	 * Returns the name of the course.
	 * @return The name of the course.
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Returns the year of the course.
	 * @return The year of the course.
	 */
	public int getYear()
	{
		return this.year;
	}
	
	/**
	 * Returns the registered students of this Course.
	 * @return The registered students.
	 */
	public Set<Student> getRegisteredStudents()
	{
		return this.registeredStudents;
	}
	
	/**
	 * Returns the existing lab events of this Course
	 * @return The existing lab events.
	 */
	public Set<LabEvent> getLabEvents()
	{
		return this.labEvents;
	}
	
	/**
	 * Register Student to this Course.
	 * @param student The Student who wants to be registered to this Course. 
	 * @return If the Student registered to the Course: STUDENT_REGISTERED(0)
	 * 			If the Student already registered to the Course: STUDENT_ALREADY_REGISTERED(1)
	 */
	public int registerStudent(Student student)
	{
		try
		{
			if(registeredStudents.add(student) == false)
			{
				return Constants.STUDENT_ALREADY_REGISTERED;
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
		
		return Constants.STUDENT_REGISTERED;
	}
	
	/**
	 * Delete Student from this Course.
	 * @param student The Student who wants to be deleted from this Course.
	 * @return If the Student removed successfully from the Course: STUDENT_UNREGISTERED(0)
	 * 			If the Student doesn't found in the Student list of the Course: STUDENT_NOT_FOUND_IN_THE_REGISTERED_STUDENTS_LIST(1)
	 */
	public int unregisterStudent(Student student)
	{
		try
		{
			if(registeredStudents.remove(student) == false)
			{
				return Constants.STUDENT_NOT_FOUND_IN_THE_REGISTERED_STUDENTS_LIST;
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
		
		return Constants.STUDENT_UNREGISTERED;
	}
	
	/**
	 * Add lab event to this course.
	 * @param labEvent The lab event which want to be added to this Course.
	 * @return If the LabEvent added to the Course: LAB_EVENT_ADDED(0)
	 * 			If the LabEvent already added to the Course: LAB_EVENT_ADDED(1)
	 */
	public int addLabEvent(LabEvent labEvent)
	{
		try
		{
			if(labEvents.add(labEvent) == false)
			{
				return Constants.LAB_EVENT_ALREADY_ADDED;
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
		
		return Constants.LAB_EVENT_ADDED;
	}
	
	/**
	 * Remove lab event from this Course.
	 * @param labEvent The lab event which want to be removed from this Course.
	 * @return If the LabEvent removed successfully from the Course: LAB_EVENT_REMOVED(0)
	 * 			If the LabEvent doesn't found in the LabEvent list of the Course: LAB_EVENT_NOT_FOUND_IN_THE_LAB_EVENTS_LIST(1)
	 */
	public int removeLabEvent(LabEvent labEvent)
	{
		try
		{
			if(labEvents.remove(labEvent) == false)
			{
				return Constants.LAB_EVENT_NOT_FOUND_IN_THE_LAB_EVENTS_LIST;
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
		
		return Constants.LAB_EVENT_REMOVED;
	}
}
