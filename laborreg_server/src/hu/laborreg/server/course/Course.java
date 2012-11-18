package hu.laborreg.server.course;


import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;
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
	 */
	public void registerStudent(Student student) throws ElementAlreadyAddedException, UnsupportedOperationException, ClassCastException, NullPointerException, IllegalArgumentException
	{
		if(registeredStudents.add(student) == false)
		{
			throw new ElementAlreadyAddedException("Student " + student.getName() + "(" + student.getNeptunCode() + ") already registered to this course.");
		}
	}
	
	/**
	 * Delete Student from this Course.
	 * @param student The Student who wants to be deleted from this Course.
	 */
	public void unregisterStudent(Student student) throws ElementNotFoundException, UnsupportedOperationException, ClassCastException, NullPointerException
	{
		if(registeredStudents.remove(student) == false)
		{
			throw new ElementNotFoundException("Student " + student.getName() + "(" + student.getNeptunCode() + ") not found in Course's Student list.");
		}
	}
	
	/**
	 * Add lab event to this course.
	 * @param labEvent The lab event which want to be added to this Course.
	 */
	public void addLabEvent(LabEvent labEvent) throws ElementAlreadyAddedException, UnsupportedOperationException, ClassCastException, NullPointerException, IllegalArgumentException
	{
		if(labEvents.add(labEvent) == false)
		{
			throw new ElementAlreadyAddedException("Lab event " + labEvent.getName() + " already added to this Course.");
		}

	}
	
	/**
	 * Remove lab event from this Course.
	 * @param labEvent The lab event which want to be removed from this Course.
	 */
	public void removeLabEvent(LabEvent labEvent) throws ElementNotFoundException, UnsupportedClassVersionError, ClassCastException, NullPointerException
	{
		if(labEvents.remove(labEvent) == false)
		{
			throw new ElementNotFoundException("Lab event" + labEvent.getName() + " not found in Course's Lab Event list.");
		} 
	}
}