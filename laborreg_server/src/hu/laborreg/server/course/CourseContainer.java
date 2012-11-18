package hu.laborreg.server.course;


import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CourseContainer {
	
	private Set<Course> courses;
	
	/**
	 * A container class which contains the courses.
	 */
	public CourseContainer()
	{
		courses = new HashSet<Course>();
		//TODO read the existing courses from the DB.
	}
	
	/**
	 * Add Course to the courses list.
	 * @param course The needed course
	 */
	public void addCourse(Course course) throws ElementAlreadyAddedException, UnsupportedOperationException, ClassCastException, NullPointerException, IllegalArgumentException
	{
		if(this.courses.add(course) == false)
		{
			throw new ElementAlreadyAddedException("Course" + course.getName() + "(" + course.getYear() + ") already added to the Courses list.");
		}
	}
	
	/**
	 * Remove course to the courses list.
	 * @param course The needed course
	 */
	public void removeCourse(Course course) throws ElementNotFoundException, UnsupportedOperationException, ClassCastException, NullPointerException
	{
		if(this.courses.remove(course) == false)
		{
			throw new ElementNotFoundException("Course" + course.getName() + "(" + course.getYear() + ") does not found in the Courses list.");
		}
	}
	
	/**
	 * Get the specified course from courses.
	 * @param name The name of the course (must be unique).
	 * @param year The year of the course.
	 * @return The needed course
	 */
	public Course getCourse(String name, int year) throws ElementNotFoundException
	{
		Iterator<Course> it = courses.iterator();
		
		while(it.hasNext())
		{
			Course retVal = it.next();
			if(retVal.getName().equals(name) && retVal.getYear()==year)
			{
				return retVal;
			}
		}
		throw new ElementNotFoundException("Course" + name + "(" + year + ") does not found in the Courses list.");
	}
}
