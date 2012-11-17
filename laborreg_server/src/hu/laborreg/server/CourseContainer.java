package hu.laborreg.server;

import java.util.Iterator;
import java.util.Set;

public class CourseContainer {
	
	private Set<Course> courses;
	
	/**
	 * A container class which contains the courses.
	 */
	public CourseContainer()
	{
		//TODO read the existing courses from the DB.
	}
	
	/**
	 * Add course to the courses list.
	 * @param course The needed course
	 */
	public void addCourse(Course course)
	{
		
		try
		{
			if(this.courses.add(course) == false)
			{
				//TODO courses already contain this course. Display error in error window?
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
	 * Remove course to the courses list.
	 * @param course The needed course
	 */
	public void removeCourse(Course course)
	{
		
		try
		{
			if(this.courses.remove(course) == false)
			{
				//TODO courses doesn't contain this course.  Display error in error window?
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
	}
	
	/**
	 * Get the specified course from courses.
	 * @param name The name of the course (must be unique).
	 * @param year The year of the course.
	 * @return The needed course
	 */
	public Course getCourse(String name, int year)
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
		return null;
	}
}
