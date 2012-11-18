package hu.laborreg.server.student;

import hu.laborreg.server.Constants;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class StudentContainer {
	
	private Set<Student> students;
	
	/**
	 * A container class which contains the Students.
	 */
	public StudentContainer()
	{
		students = new HashSet<Student>();
		//TODO read the existing students from the DB.
	}
	
	/**
	 * Add Student to the students list.
	 * @param student The needed Student.
	 * @return If the container does not contain this Student yet: CONTAINER_OK(0)
	 * 			If the container already contains this Student: CONTAINER_ALREADY_CONTAINS_THIS_ELEMENT(1)
	 */
	public int addStudent(Student student)
	{
		
		try
		{
			if(this.students.add(student) == false)
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
	 * Remove Student to the Students list.
	 * @param student The needed Student.
	 * @return If the remove was successful: CONAINER_OK(0)
	 * 			If the container does not contain this Student: CONTAINER_DOES_NOT_CONTAIN_THIS_ELEMENT(1)
	 */
	public int removeStudent(Student student)
	{
		
		try
		{
			if(this.students.remove(student) == false)
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
		
		return Constants.CONTAINER_OK;
	}
	
	/**
	 * Get the specified Student from students.
	 * @param neptunCode The Neptun code of the Student (must be unique).
	 * @return The needed Student.
	 */
	public Student getStudent(String neptunCode)
	{
		Iterator<Student> it = students.iterator();
		
		while(it.hasNext())
		{
			Student retVal = it.next();
			if(retVal.getNeptunCode().equals(neptunCode))
			{
				return retVal;
			}
		}
		return null;
	}
}
