package hu.laborreg.server.student;

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
	 */
	public void addStudent(Student student)
	{
		
		try
		{
			if(this.students.add(student) == false)
			{
				//TODO students already contain this student. Display error in error window?
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
	 * Remove Student to the Students list.
	 * @param student The needed Student.
	 */
	public void removeStudent(Student student)
	{
		
		try
		{
			if(this.students.remove(student) == false)
			{
				//TODO students doesn't contain this student.  Display error in error window?
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
