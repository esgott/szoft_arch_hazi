package hu.laborreg.server.course;

import static org.junit.Assert.*;

import hu.laborreg.server.course.Course;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.exception.TimeSetException;
import hu.laborreg.server.labEvent.LabEvent;
import hu.laborreg.server.student.Student;

import java.text.ParseException;
import java.util.Calendar;

import org.junit.Test;

public class CourseTest {

	private Course c1;
	private Course c2;
	private LabEvent ev1;
	private LabEvent ev2;
	private Student st1;
	private Student st2;
	
	private void init() throws TimeSetException, ParseException 
	{
		c1 = new Course("aaa",111);
		c2 = new Course("bbb",222);

		st1 = new Student("xxx","Bela");
		st2 = new Student("yyy","Geza");
		
		Calendar currentTime = Calendar.getInstance();
		
		ev1 = new LabEvent("lab_event_1",c1.getName(),c1.getYear(),
				currentTime.get(Calendar.HOUR_OF_DAY)+":"+(currentTime.get(Calendar.MINUTE)+1),
				currentTime.get(Calendar.HOUR_OF_DAY)+":"+(currentTime.get(Calendar.MINUTE)+3));
		ev2 = new LabEvent("lab_event_2",c1.getName(),c1.getYear(),
				currentTime.get(Calendar.HOUR_OF_DAY)+":"+(currentTime.get(Calendar.MINUTE)+2),
				currentTime.get(Calendar.HOUR_OF_DAY)+":"+(currentTime.get(Calendar.MINUTE)+4));
	}
	
	@Test
	public void basicAttributesTest() throws TimeSetException, ParseException
	{
		init();
		assertEquals("aaa",c1.getName());
		assertEquals("bbb",c2.getName());
		assertEquals(111,c1.getYear());
		assertEquals(222,c2.getYear());
	}
	
	@Test
	public void registerAndUnregisterStudentTest() throws TimeSetException, ParseException, UnsupportedOperationException,
														ClassCastException, NullPointerException, IllegalArgumentException,
														ElementAlreadyAddedException, ElementNotFoundException
	{
		init();

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
	public void addAndRemoveLabEventTest() throws TimeSetException, ParseException, UnsupportedOperationException,
												ClassCastException, NullPointerException, IllegalArgumentException,
												ElementAlreadyAddedException, ElementNotFoundException
	{
		init();

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
