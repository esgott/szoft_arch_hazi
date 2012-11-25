package hu.laborreg.server.labEvent;

import static org.junit.Assert.*;
import hu.laborreg.server.Configuration;
import hu.laborreg.server.computer.Computer;
import hu.laborreg.server.course.Course;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.TimeSetException;
import hu.laborreg.server.exception.WrongIpAddressException;
import hu.laborreg.server.student.Student;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class LabEventTest {
	private Course c1;
	private Computer comp1;
	private Computer comp2;
	private Student s1;
	private Student s2;
	private LabEvent l1;
	private LabEvent l2;

	private Configuration configuration = new Configuration(new String[0]);

	@Before
	public void init() throws TimeSetException, WrongIpAddressException {
		c1 = new Course("course1", 1999);

		comp1 = new Computer(configuration.getProperty(Configuration.smallestIpAddress), configuration);
		comp2 = new Computer(configuration.getProperty(Configuration.biggestIpAddress), configuration);

		s1 = new Student("abcdef");
		s2 = new Student("xyz");

		Calendar startTime = Calendar.getInstance();
		startTime.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE) + 1);
		Calendar startTime2 = Calendar.getInstance();
		startTime2.set(Calendar.MINUTE, startTime2.get(Calendar.MINUTE) + 2);
		Calendar stopTime = Calendar.getInstance();
		stopTime.set(Calendar.MINUTE, stopTime.get(Calendar.MINUTE) + 3);
		Calendar stopTime2 = Calendar.getInstance();
		stopTime2.set(Calendar.MINUTE, stopTime2.get(Calendar.MINUTE) + 4);

		l1 = new LabEvent("lab_event_1", c1.getName(), c1.getYear(), startTime.getTime(), stopTime.getTime());
		l2 = new LabEvent("lab_event_2", c1.getName(), c1.getYear(), startTime2.getTime(), stopTime2.getTime());
	}

	@Test
	public void basicAttributeTest() throws TimeSetException, WrongIpAddressException {
		Calendar startTime = Calendar.getInstance();
		startTime.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE) + 1);
		Calendar stopTime = Calendar.getInstance();
		stopTime.set(Calendar.MINUTE, stopTime.get(Calendar.MINUTE) + 4);

		assertEquals("lab_event_1", l1.getName());
		assertEquals(c1.getName(), l1.getCourseName());
		assertEquals(c1.getYear(), l1.getCourseYear());

		Calendar retVal = Calendar.getInstance();
		retVal.setTime(l1.getStartTime());
		assertEquals(startTime.get(Calendar.HOUR_OF_DAY), retVal.get(Calendar.HOUR_OF_DAY));
		assertEquals(startTime.get(Calendar.MINUTE), retVal.get(Calendar.MINUTE));

		retVal.setTime(l2.getStopTime());
		assertEquals(stopTime.get(Calendar.HOUR_OF_DAY), retVal.get(Calendar.HOUR_OF_DAY));
		assertEquals(stopTime.get(Calendar.MINUTE), retVal.get(Calendar.MINUTE));
	}

	@Test
	public void createLabEventWithWrongStartAndStopDateTest() throws TimeSetException, WrongIpAddressException {
		Calendar currentTime = Calendar.getInstance();
		Calendar startTime = Calendar.getInstance();
		Calendar stopTime = Calendar.getInstance();

		int numberOfThrownExceptions = 0;

		try {
			startTime.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE) + 1);
			stopTime.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE) + 2);

			l1 = new LabEvent("lab_event_1", c1.getName(), c1.getYear(), startTime.getTime(), stopTime.getTime()); // OK
		} catch (TimeSetException e) {
			numberOfThrownExceptions++;
		}

		try {
			startTime.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE) + 2);
			stopTime.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE) + 1);

			l1 = new LabEvent("lab_event_1", c1.getName(), c1.getYear(), startTime.getTime(), stopTime.getTime()); // NOK
		} catch (TimeSetException e) {
			numberOfThrownExceptions++;
		}

		try {
			startTime.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE) - 1);
			stopTime.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE) + 1);

			l1 = new LabEvent("lab_event_1", c1.getName(), c1.getYear(), startTime.getTime(), stopTime.getTime()); // OK
		} catch (TimeSetException e) {
			numberOfThrownExceptions++;
		}

		try {
			startTime.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE) - 2);
			stopTime.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE) - 1);

			l1 = new LabEvent("lab_event_1", c1.getName(), c1.getYear(), startTime.getTime(), stopTime.getTime()); // OK
		} catch (TimeSetException e) {
			numberOfThrownExceptions++;
		}

		if (numberOfThrownExceptions == 1) {
			return;
		} else {
			fail("Bad number of TimeSetExceptions thrown.");
		}
	}

	@Test
	public void setStartAndStopTimeTest() throws TimeSetException, WrongIpAddressException {
		Calendar currentTime = Calendar.getInstance();
		Calendar startTime = Calendar.getInstance();
		Calendar stopTime = Calendar.getInstance();

		startTime.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE) + 2);
		stopTime.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE) + 4);

		l1.setStartAndStopTime(startTime.getTime(), stopTime.getTime(), false);
	}

	@Test
	public void allowMultipleRegistartionsTest() throws TimeSetException, WrongIpAddressException,
			ElementAlreadyAddedException {
		assertEquals(0, l1.getRegisteredComputers().size());
		assertEquals(0, l1.getRegisteredComputers().size());

		l1.allowMultipleRegistration(comp1);
		l1.allowMultipleRegistration(comp2);
		l2.allowMultipleRegistration(comp2);

		assertEquals(2, l1.getRegisteredComputers().size());
		assertEquals(1, l2.getRegisteredComputers().size());

		try {
			l1.allowMultipleRegistration(comp1);
		} catch (ElementAlreadyAddedException e) {
			return;
		}

		fail("ElementAlreadyAddedException not thrown.");
	}

	public void signInStudentTest() throws TimeSetException, WrongIpAddressException, ElementAlreadyAddedException {
		Computer computer = new Computer("192.168.0.1", configuration);

		assertEquals(0, l1.getSignedInStudents().size());
		assertEquals(0, l2.getSignedInStudents().size());

		l1.signInStudent(s1, computer);
		l1.signInStudent(s2, computer);
		l2.signInStudent(s2, computer);

		assertEquals(2, l1.getSignedInStudents().size());
		assertEquals(1, l2.getSignedInStudents().size());

		try {
			l1.signInStudent(s2, computer);
		} catch (ElementAlreadyAddedException e) {
			return;
		}

		fail("ElementAlreadyAddedException not thrown.");
	}
}
