package hu.laborreg.server.student;


import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;

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
	public void addStudent(Student student) throws ElementAlreadyAddedException, UnsupportedOperationException, ClassCastException, NullPointerException, IllegalArgumentException
	{
		if(this.students.add(student) == false)
		{
			throw new ElementAlreadyAddedException("Student " + student.getName() + "(" + student.getNeptunCode() + ") already added to Students list.");
		}
	}
	
	/**
	 * Remove Student to the Students list.
	 * @param student The needed Student.
	 */
	public void removeStudent(Student student) throws ElementNotFoundException, UnsupportedOperationException, ClassCastException, NullPointerException
	{
		if(this.students.remove(student) == false)
		{
			throw new ElementNotFoundException("Student " + student.getName() + "(" + student.getNeptunCode() + ") does not found in Students list.");
		}
	}
	
	/**
	 * Get the specified Student from students.
	 * @param neptunCode The Neptun code of the Student (must be unique).
	 * @return The needed Student.
	 */
	public Student getStudent(String neptunCode) throws ElementNotFoundException
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
		throw new ElementNotFoundException("Student: " + neptunCode + "does not found in the Students list.");
	}
}
