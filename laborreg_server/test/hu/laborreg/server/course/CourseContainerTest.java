package hu.laborreg.server.course;

import static org.junit.Assert.*;

import hu.laborreg.server.course.Course;
import hu.laborreg.server.course.CourseContainer;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.exception.TimeSetException;

import java.text.ParseException;

import org.junit.Test;

public class CourseContainerTest {

	private CourseContainer cont;
	private Course c1;
	private Course c2;
	
	private void init() throws TimeSetException, ParseException 
	{
		cont = new CourseContainer();
		c1 = new Course("aaa",111);
		c2 = new Course("bbb",222);
	}
	
	@Test
	public void addCourseToContainerTest() throws UnsupportedOperationException, ClassCastException, NullPointerException,
												IllegalArgumentException, TimeSetException, ParseException, ElementAlreadyAddedException
	{
		init();
		
		cont.addCourse(c1);
		cont.addCourse(c2);
		
		try
		{
			cont.addCourse(c1);
		}
		catch(ElementAlreadyAddedException e)
		{
			return;
		}
		
		fail("ElementAlreadyAddedException not thrown.");
	}
	
	@Test
	public void removeCourseFromContainerTest() throws UnsupportedOperationException, ClassCastException, NullPointerException,
													IllegalArgumentException, TimeSetException, ParseException, ElementAlreadyAddedException,
													ElementNotFoundException
	{
		init();
		
		cont.addCourse(c1);
		cont.addCourse(c2);

		cont.removeCourse(c1);
		
		try
		{
			cont.removeCourse(c1);
		}
		catch(ElementNotFoundException e)
		{
			return;
		}
		
		fail("ElementNotFoundException not thrown.");
	}
	
	@Test
	public void getCourseFromContainerTest() throws UnsupportedOperationException, ClassCastException, NullPointerException,
												IllegalArgumentException, TimeSetException, ParseException, 
												ElementAlreadyAddedException, ElementNotFoundException
	{
		init();
		
		cont.addCourse(c1);
		cont.addCourse(c2);
		
		assertEquals(c1,cont.getCourse(c1.getName(), c1.getYear()));
		assertEquals(c2,cont.getCourse(c2.getName(), c2.getYear()));
		
		cont.removeCourse(c2);
		
		try
		{
			cont.getCourse(c2.getName(), c2.getYear());
		}
		catch(ElementNotFoundException e)
		{
			return;
		}
		
		fail("ElementNotFoundException not thrown.");
	}
}