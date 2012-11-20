package hu.laborreg.server.course;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hu.laborreg.server.course.Course;
import hu.laborreg.server.course.CourseContainer;
import hu.laborreg.server.db.DBConnectionHandler;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.exception.TimeSetException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CourseContainerTest {

	@Mock
	DBConnectionHandler mockDbConnectionHandler;
	@Mock
	PreparedStatement mockPreparedStatement;
	@Mock
	ResultSet mockResultset;

	private CourseContainer cont;
	private Course c1;
	private Course c2;

	@Before
	public void init() throws TimeSetException, SQLException {
		MockitoAnnotations.initMocks(this);

		when(mockDbConnectionHandler.createPreparedStatement(anyString())).thenReturn(mockPreparedStatement);
		when(mockPreparedStatement.executeQuery()).thenReturn(mockResultset);
		when(mockResultset.next()).thenReturn(false);

		cont = new CourseContainer(mockDbConnectionHandler);

		verify(mockPreparedStatement).executeQuery();

		c1 = new Course("aaa", 111);
		c2 = new Course("bbb", 222);
	}

	@Test
	public void addCourseToContainerTest() throws ElementAlreadyAddedException, SQLException {
		cont.addCourse(c1);
		cont.addCourse(c2);
		verify(mockPreparedStatement, times(2)).executeUpdate();

		try {
			cont.addCourse(c1);
		} catch (ElementAlreadyAddedException e) {
			return;
		}

		fail("ElementAlreadyAddedException not thrown.");
	}

	@Test
	public void removeCourseFromContainerTest() throws ElementAlreadyAddedException, ElementNotFoundException,
			SQLException {
		cont.addCourse(c1);
		cont.addCourse(c2);

		cont.removeCourse(c1);

		verify(mockPreparedStatement, times(3)).executeUpdate();

		try {
			cont.removeCourse(c1);
		} catch (ElementNotFoundException e) {
			return;
		}

		fail("ElementNotFoundException not thrown.");
	}

	@Test
	public void getCourseFromContainerTest() throws ElementAlreadyAddedException, ElementNotFoundException,
			SQLException {
		cont.addCourse(c1);
		cont.addCourse(c2);

		assertEquals(c1, cont.getCourse(c1.getName(), c1.getYear()));
		assertEquals(c2, cont.getCourse(c2.getName(), c2.getYear()));

		cont.removeCourse(c2);

		verify(mockPreparedStatement, times(3)).executeUpdate();

		try {
			cont.getCourse(c2.getName(), c2.getYear());
		} catch (ElementNotFoundException e) {
			return;
		}

		fail("ElementNotFoundException not thrown.");
	}
}