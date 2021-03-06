package hu.laborreg.server.course;

import static org.junit.Assert.*;

import hu.laborreg.server.course.Course;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.exception.TimeSetException;
import hu.laborreg.server.labEvent.LabEvent;
import hu.laborreg.server.student.Student;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class CourseTest {

	private Course c1;
	private Course c2;
	private LabEvent ev1;
	private LabEvent ev2;
	private Student st1;
	private Student st2;
	
	@Before
	public void init() throws TimeSetException 
	{
		c1 = new Course("aaa",111);
		c2 = new Course("bbb",222);

		st1 = new Student("xxx");
		st2 = new Student("yyy");

		Calendar startTime = Calendar.getInstance();
		startTime.set(Calendar.MINUTE,startTime.get(Calendar.MINUTE)+1);
		Calendar stopTime = Calendar.getInstance();
		stopTime.set(Calendar.MINUTE,stopTime.get(Calendar.MINUTE)+2);
		
		ev1 = new LabEvent("lab_event_1",c1.getName(),c1.getYear(),startTime.getTime(),stopTime.getTime());
		ev2 = new LabEvent("lab_event_2",c1.getName(),c1.getYear(),startTime.getTime(),stopTime.getTime());
	}
	
	@Test
	public void basicAttributesTest() throws TimeSetException
	{
		assertEquals("aaa",c1.getName());
		assertEquals("bbb",c2.getName());
		assertEquals(111,c1.getYear());
		assertEquals(222,c2.getYear());
	}
	
	@Test
	public void registerAndUnregisterStudentTest() throws TimeSetException, UnsupportedOperationException,
														ClassCastException, NullPointerException, IllegalArgumentException,
														ElementAlreadyAddedException, ElementNotFoundException
	{
		int numberOfThrownExceptions = 0;
		
		assertEquals(0,c1.getRegisteredStudents().size());
		assertEquals(0,c2.getRegisteredStudents().size());
		
		c1.registerStudent(st1);
		c1.registerStudent(st2);
		c2.registerStudent(st2);
		
		assertEquals(2,c1.getRegisteredStudents().size());
		assertEquals(1,c2.getRegisteredStudents().size());

		c1.unregisterStudent(st1);
		
		assertEquals(1,c1.getRegisteredStudents().size());
		assertEquals(1,c2.getRegisteredStudents().size());
		
		try
		{
			c1.unregisterStudent(st1);
		}
		catch(ElementNotFoundException e)
		{
			numberOfThrownExceptions++;
		}
		
		try
		{
			c2.unregisterStudent(st1);
		}
		catch(ElementNotFoundException e)
		{
			numberOfThrownExceptions++;
		}
		
		try
		{
			c1.registerStudent(st2);
		}
		catch(ElementAlreadyAddedException e)
		{
			numberOfThrownExceptions++;
		}
		
		if(numberOfThrownExceptions == 3)
		{
			return;
		}
		else
		{
			fail("Wrong number of ElementAlreadyAddedException or ElementNotFoundException.");
		}
	}
	
	@Test
	public void addAndRemoveLabEventTest() throws TimeSetException, UnsupportedOperationException,
												ClassCastException, NullPointerException, IllegalArgumentException,
												ElementAlreadyAddedException, ElementNotFoundException
	{
		int numberOfThrownExceptions = 0;
		
		assertEquals(0,c1.getLabEvents().size());
		assertEquals(0,c2.getLabEvents().size());
		
		c1.addLabEvent(ev1);
		c1.addLabEvent(ev2);
		c2.addLabEvent(ev2);
		
		assertEquals(2,c1.getLabEvents().size());
		assertEquals(1,c2.getLabEvents().size());

		c1.removeLabEvent(ev1);
		
		assertEquals(1,c1.getLabEvents().size());
		assertEquals(1,c2.getLabEvents().size());
		
		try
		{
			c1.removeLabEvent(ev1);
		}
		catch(ElementNotFoundException e)
		{
			numberOfThrownExceptions++;
		}
		
		try
		{
			c2.removeLabEvent(ev1);
		}
		catch(ElementNotFoundException e)
		{
			numberOfThrownExceptions++;
		}
		
		try
		{
			c1.addLabEvent(ev2);
		}
		catch(ElementAlreadyAddedException e)
		{
			numberOfThrownExceptions++;
		}
		
		if(numberOfThrownExceptions == 3)
		{
			return;
		}
		else
		{
			fail("Wrong number of ElementAlreadyAddedException or ElementNotFoundException.");
		}
	}
}
