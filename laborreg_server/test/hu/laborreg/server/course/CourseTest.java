package hu.laborreg.server.course;

import static org.junit.Assert.*;

import hu.laborreg.server.course.Course;
import hu.laborreg.server.labEvent.LabEvent;
import hu.laborreg.server.student.Student;

import java.io.IOException;

import org.junit.Test;

public class CourseTest {

	private CourseContainer cont;
	private Course c1;
	private Course c2;
	private LabEvent ev1;
	private LabEvent ev2;
	private Student st1;
	private Student st2;
	private Student st3;
	
	private void setUp()
	{
		cont = new CourseContainer();
		c1 = new Course("aaa",111);
		c2 = new Course("bbb",222);
		ev1 = new LabEvent("lab_1",c1.getName(),"11:00","12:00");
		ev2 = new LabEvent("lab_2",c2.getName(),"12:00","13:00");
		st1 = new Student("xxx","Bela");
		st2 = new Student("yyy","Geza");
		st3 = new Student("zzz", "Sanyi");
	}
	
	@Test
	public void createCourseAndAddLabEventsAndStudentsToIt() throws IOException {
		
		setUp();
		
		assertEquals("aaa",c1.getName());
		assertEquals(111,c1.getYear());
		
		//TODO LabEvent gets' check
		
		assertEquals("yyy", st2.getNeptunCode());
		assertEquals("Geza", st2.getName());
		
		cont.addCourse(c1);
		cont.addCourse(c2);
		cont.removeCourse(c1);
		cont.addCourse(c1);
		cont.addCourse(c1); //TODO check that c1 shouldn't be twice in the container
		
		c1.addLabEvent(ev1);
		c1.addLabEvent(ev2);
		c1.removeLabEvent(ev2);
		
		assertEquals(1, c1.getLabEvents().size());
		c1.addLabEvent(ev2);
		assertEquals(2, c1.getLabEvents().size());
		c1.removeLabEvent(ev2);
		
		c1.registerStudent(st1);
		c1.registerStudent(st2);
		c1.registerStudent(st3);
		
		assertEquals(3, c1.getRegisteredStudents().size());
		c1.registerStudent(st2);
		assertEquals(3, c1.getRegisteredStudents().size());
		
		assertEquals(0, c2.getRegisteredStudents().size());
		c2.registerStudent(st2);
		c2.registerStudent(st3);
		assertEquals(2, c2.getRegisteredStudents().size());
		
		c2.unregisterStudent(st2);
		c2.unregisterStudent(st3);
		assertEquals(0, c2.getRegisteredStudents().size());
		
		c1.unregisterStudent(st1);
		c1.unregisterStudent(st3);
		assertEquals(1, c1.getRegisteredStudents().size());
		c1.unregisterStudent(st2);
		
		c1.removeLabEvent(ev2);
		c1.removeLabEvent(ev1);
		assertEquals(0, c1.getLabEvents().size());
		
		cont.removeCourse(c2);
		cont.removeCourse(c1);
	}
		
		@Test
		public void getDataFromCourseContainerAndCourse() throws IOException {
			
			setUp();
			
			cont.addCourse(c1);
			c1.addLabEvent(ev1);
			c1.registerStudent(st1);
			c1.registerStudent(st2);
			c1.registerStudent(st3);
			
			cont.addCourse(c2);
			c2.addLabEvent(ev2);
			c2.registerStudent(st2);
			c2.registerStudent(st3);

			
			assertEquals(1,cont.getCourse(c1.getName(), c1.getYear()).getLabEvents().size());
			assertEquals(1,cont.getCourse(c2.getName(), c2.getYear()).getLabEvents().size());
			
			assertEquals(3,cont.getCourse(c1.getName(), c1.getYear()).getRegisteredStudents().size());
			assertEquals(2,cont.getCourse(c2.getName(), c2.getYear()).getRegisteredStudents().size());
			
			c2.removeLabEvent(ev2);
			c2.unregisterStudent(st2);
			c2.unregisterStudent(st3);
			
			c1.removeLabEvent(ev1);
			c1.removeLabEvent(ev2);
			c1.unregisterStudent(st1);
			c1.unregisterStudent(st2);
			c1.unregisterStudent(st3);
		}
	}
