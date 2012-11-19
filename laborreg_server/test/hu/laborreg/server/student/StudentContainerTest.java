package hu.laborreg.server.student;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;

public class StudentContainerTest{
	
	private StudentContainer cont;
	private Student s1;
	private Student s2;
	
	private void init()
	{
		cont = new StudentContainer();
		s1 = new Student("abcdef","Bela");
		s2 = new Student("a123bcd","Geza");
	}
	
	@Test
	public void addStudentToContainerTest() throws UnsupportedOperationException, ClassCastException, NullPointerException,
												IllegalArgumentException, ElementAlreadyAddedException
	{
		init();
		
		cont.addStudent(s1);
		cont.addStudent(s2);
		
		try
		{
			cont.addStudent(s1);
		}
		catch(ElementAlreadyAddedException e)
		{
			return;
		}
		
		fail("ElementAlreadyAddedException not thrown.");
	}
	
	@Test
	public void removeStudentFromContainerTest() throws UnsupportedOperationException, ClassCastException, NullPointerException,
													IllegalArgumentException, ElementAlreadyAddedException, ElementNotFoundException
	{
		init();
		
		cont.addStudent(s1);
		cont.addStudent(s2);

		cont.removeStudent(s1);
		
		try
		{
			cont.removeStudent(s1);
		}
		catch(ElementNotFoundException e)
		{
			return;
		}
		
		fail("ElementNotFoundException not thrown.");
	}
	
	@Test
	public void getStduentFromContainerTest() throws UnsupportedOperationException, ClassCastException, NullPointerException,
												IllegalArgumentException, ElementAlreadyAddedException, ElementNotFoundException
	{
		init();
		
		cont.addStudent(s1);
		cont.addStudent(s2);
		
		assertEquals(s1.getName(),cont.getStudent(s1.getNeptunCode()).getName());
		assertEquals(s2.getName(),cont.getStudent(s2.getNeptunCode()).getName());
		
		cont.removeStudent(s2);
		
		try
		{
			cont.getStudent(s2.getNeptunCode());
		}
		catch(ElementNotFoundException e)
		{
			return;
		}
		
		fail("ElementNotFoundException not thrown.");
	}
}
