package hu.laborreg.server.course;


import hu.laborreg.server.Constants;

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
	 * @return If the container does not contain this Course yet: CONTAINER_OK(0)
	 * 			If the container already contains this Course: CONTAINER_ALREADY_CONTAINS_THIS_ELEMENT(1)
	 */
	public int addCourse(Course course)
	{
		
		try
		{
			if(this.courses.add(course) == false)
			{
				return Constants.CONTAINER_ALREADY_CONTAINS_THIS_ELEMENT;
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
		
		return Constants.CONTAINER_OK;
	}
	
	/**
	 * Remove course to the courses list.
	 * @param course The needed course
	 * @return If the remove was successful: CONAINER_OK(0)
	 * 			If the container does not contain this Course: CONTAINER_DOES_NOT_CONTAIN_THIS_ELEMENT(1)
	 */
	public int removeCourse(Course course)
	{
		
		try
		{
			if(this.courses.remove(course) == false)
			{
				return Constants.CONTAINER_DOES_NOT_CONTAIN_THIS_ELEMENT;
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
		
		return Constants.CONTAINER_OK;
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
