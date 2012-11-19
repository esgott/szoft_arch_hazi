package hu.laborreg.server.labEvent;

import static org.junit.Assert.*;
import hu.laborreg.server.Constants;
import hu.laborreg.server.computer.Computer;
import hu.laborreg.server.course.Course;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.TimeSetException;
import hu.laborreg.server.exception.WrongIpAddressException;
import hu.laborreg.server.student.Student;

import java.text.ParseException;
import java.util.Calendar;

import org.junit.Test;

public class LabEventTest
{
	private Course c1;
	private Course c2;
	private Computer comp1;
	private Computer comp2;
	private Student s1;
	private Student s2;
	private LabEvent l1;
	private LabEvent l2;
	
	private void init() throws TimeSetException, ParseException, WrongIpAddressException
	{
		c1 = new Course("course1",1999);
		c2 = new Course("course2",2001);
		
		comp1 = new Computer(Constants.SMALLEST_VALID_IP_ADDRESS);
		comp2 = new Computer(Constants.BIGGEST_VALID_IP_ADDRESS);
		
		s1 = new Student("abcdef","Bela");
		s2 = new Student("xyz","Geza");
		
		Calendar currentTime = Calendar.getInstance();
		
		l1 = new LabEvent("lab_event_1",c1.getName(),c1.getYear(),
				currentTime.get(Calendar.HOUR_OF_DAY)+":"+(currentTime.get(Calendar.MINUTE)+1),
				currentTime.get(Calendar.HOUR_OF_DAY)+":"+(currentTime.get(Calendar.MINUTE)+3));
		l2 = new LabEvent("lab_event_2",c1.getName(),c1.getYear(),
				currentTime.get(Calendar.HOUR_OF_DAY)+":"+(currentTime.get(Calendar.MINUTE)+2),
				currentTime.get(Calendar.HOUR_OF_DAY)+":"+(currentTime.get(Calendar.MINUTE)+4));
	}
	
	@Test
	public void basicAttributeTest() throws TimeSetException, ParseException, WrongIpAddressException
	{
		init();
		
		Calendar currentTime = Calendar.getInstance();
		
		l1 = new LabEvent("lab_event_1",c1.getName(),c1.getYear(),
				currentTime.get(Calendar.HOUR_OF_DAY)+":"+(currentTime.get(Calendar.MINUTE)+1),
				currentTime.get(Calendar.HOUR_OF_DAY)+":"+(currentTime.get(Calendar.MINUTE)+3));
		l2 = new LabEvent("lab_event_2",c2.getName(),c2.getYear(),
				currentTime.get(Calendar.HOUR_OF_DAY)+":"+(currentTime.get(Calendar.MINUTE)+2),
				currentTime.get(Calendar.HOUR_OF_DAY)+":"+(currentTime.get(Calendar.MINUTE)+4));
		
		assertEquals("lab_event_1",l1.getName());
		assertEquals(c1.getName(),l1.getCourseName());
		assertEquals(c2.getYear(),l2.getCourseYear());
		assertEquals(currentTime.get(Calendar.HOUR_OF_DAY)+":"+(currentTime.get(Calendar.MINUTE)+4),l2.getStopTime());
		assertEquals(currentTime.get(Calendar.HOUR_OF_DAY)+":"+(currentTime.get(Calendar.MINUTE)+1),l1.getStartTime());
	}
	
	@Test
	public void createLabEventWithWrongStartAndStopDateTest() throws ParseException, TimeSetException, WrongIpAddressException
	{
		init();
		Calendar currentTime = Calendar.getInstance();
		
		int numberOfThrownExceptions = 0;
		
		try
		{
			l1 = new LabEvent("lab_event_1",c1.getName(),c1.getYear(),
					(currentTime.get(Calendar.HOUR_OF_DAY)+1)+":00",(currentTime.get(Calendar.HOUR_OF_DAY)+1)+":30"); //OK
		}
		catch (TimeSetException e)
		{
			numberOfThrownExceptions++;
		}
	
		try
		{
			l1 = new LabEvent("lab_event_1",c1.getName(),c1.getYear(),"12:aa","13:00"); //NOK
		}
		catch(ParseException e)
		{
			numberOfThrownExceptions++;
		}
		
		try
		{
			l1 = new LabEvent("lab_event_1",c1.getName(),c1.getYear(),"12:05","12:04"); //NOK
		}
		catch(TimeSetException e)
		{
			numberOfThrownExceptions++;
		}
		
		try
		{
			//NOK
			l1 = new LabEvent("lab_event_1",c1.getName(),c1.getYear(),
								currentTime.get(Calendar.HOUR_OF_DAY) + ":" + (currentTime.get(Calendar.MINUTE)-1),
								currentTime.get(Calendar.HOUR_OF_DAY) + ":" + (currentTime.get(Calendar.MINUTE)+1));
		}
		catch(TimeSetException e)
		{
			numberOfThrownExceptions++;
		}
	
		try
		{
			//NOK
			l1 = new LabEvent("lab_event_1",c1.getName(),c1.getYear(),
								currentTime.get(Calendar.HOUR_OF_DAY) + ":" + (currentTime.get(Calendar.MINUTE)-3),
								currentTime.get(Calendar.HOUR_OF_DAY) + ":" + (currentTime.get(Calendar.MINUTE)-1));	
		}
		catch(TimeSetException e)
		{
			numberOfThrownExceptions++;
		}
		
		if(numberOfThrownExceptions == 4)
		{
			return;
		}
		else
		{
			fail("Bad number of TimeSetExceptions or ParseExceptions thrown.");
		}
	}
	
	@Test
	public void setStartAndStopTimeTest() throws ParseException, TimeSetException, WrongIpAddressException
	{
		init();
		Calendar currentTime = Calendar.getInstance();
		
		try
		{
			l1.setStartAndStopTime(currentTime.get(Calendar.HOUR_OF_DAY)+":"+(currentTime.get(Calendar.MINUTE)-1),
									currentTime.get(Calendar.HOUR_OF_DAY)+":"+(currentTime.get(Calendar.MINUTE)+1));
		}
		catch (TimeSetException e)
		{
			return;
		}
	
		fail("TimeSetException not thrown.");
	}
	
	@Test
	public void allowMultipleRegistartionsTest() throws ParseException, TimeSetException, WrongIpAddressException, ElementAlreadyAddedException
	{
		init();

		assertEquals(0,l1.getRegisteredComputers().size());
		assertEquals(0,l1.getRegisteredComputers().size());
		
		l1.allowMultipleRegistration(comp1);
		l1.allowMultipleRegistration(comp2);
		l2.allowMultipleRegistration(comp2);
		
		assertEquals(2,l1.getRegisteredComputers().size());
		assertEquals(1,l2.getRegisteredComputers().size());

		try
		{
			l1.allowMultipleRegistration(comp1);
		}
		catch(ElementAlreadyAddedException e)
		{
			return;
		}
		
		fail("ElementAlreadyAddedException not thrown.");
	}
	
	public void signInStudentTest() throws ParseException, TimeSetException, WrongIpAddressException, ElementAlreadyAddedException
	{
		init();

		assertEquals(0,l1.getSignedInStudents().size());
		assertEquals(0,l2.getSignedInStudents().size());
		
		l1.signInStudent(s1);
		l1.signInStudent(s2);
		l2.signInStudent(s2);
		
		assertEquals(2,l1.getSignedInStudents().size());
		assertEquals(1,l2.getSignedInStudents().size());

		try
		{
			l1.signInStudent(s2);
		}
		catch(ElementAlreadyAddedException e)
		{
			return;
		}
		
		fail("ElementAlreadyAddedException not thrown.");
	}
}
