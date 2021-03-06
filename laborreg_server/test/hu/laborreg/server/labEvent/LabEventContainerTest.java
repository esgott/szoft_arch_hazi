package hu.laborreg.server.labEvent;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import hu.laborreg.server.Configuration;
import hu.laborreg.server.computer.ComputerContainer;
import hu.laborreg.server.course.Course;
import hu.laborreg.server.course.CourseContainer;
import hu.laborreg.server.db.DBConnectionHandler;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.exception.TimeSetException;
import hu.laborreg.server.student.StudentContainer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LabEventContainerTest {

	@Mock
	DBConnectionHandler mockDbConnectionHandler;
	@Mock
	PreparedStatement mockPreparedStatement;
	@Mock
	StudentContainer mockStudentContainer;
	@Mock
	ComputerContainer computerContainer;
	@Mock
	CourseContainer mockCourseContainer;
	@Mock
	ResultSet mockResultset;
	@Mock
	Configuration mockConfiguration;
	
	private LabEventContainer cont;
	private Course c1;
	private LabEvent l1;
	private LabEvent l2;

	@Before
	public void init() throws TimeSetException, SQLException {
		MockitoAnnotations.initMocks(this);
		
		when(mockDbConnectionHandler.createPreparedStatement(anyString())).thenReturn(mockPreparedStatement);
		when(mockPreparedStatement.executeQuery()).thenReturn(mockResultset);
		when(mockResultset.next()).thenReturn(false);
		
		cont = new LabEventContainer(mockDbConnectionHandler, mockStudentContainer, computerContainer, mockCourseContainer, mockConfiguration);
		c1 = new Course("course1", 1999);

		Calendar startTime = Calendar.getInstance();
		startTime.set(Calendar.MINUTE,startTime.get(Calendar.MINUTE)+1);
		Calendar stopTime = Calendar.getInstance();
		stopTime.set(Calendar.MINUTE,stopTime.get(Calendar.MINUTE)+2);
		
		l1 = new LabEvent("lab_event_1", c1.getName(), c1.getYear(), startTime.getTime(), stopTime.getTime());
		l2 = new LabEvent("lab_event_2", c1.getName(), c1.getYear(), startTime.getTime(), stopTime.getTime());
	}

	@Test
	public void addLabEventToContainerTest() throws UnsupportedOperationException, ClassCastException,
			NullPointerException, IllegalArgumentException, TimeSetException,
			ElementAlreadyAddedException, SQLException, ElementNotFoundException {
		
		cont.addLabEvent(l1);
		cont.addLabEvent(l2);
		
		try {
			cont.addLabEvent(l1);
		} catch (ElementAlreadyAddedException e) {
			return;
		}

		fail("ElementAlreadyAddedException not thrown.");
	}

	@Test
	public void removeLabEventFromContainerTest() throws UnsupportedOperationException, ClassCastException,
			NullPointerException, IllegalArgumentException, TimeSetException,
			ElementAlreadyAddedException, ElementNotFoundException {

		cont.addLabEvent(l1);
		cont.removeLabEvent(l1,true);

		try {
			cont.removeLabEvent(l1, true);
		} catch (ElementNotFoundException e) {
			return;
		}

		fail("ElementNotFoundException not thrown.");
	}

	@Test
	public void getLabEventFromContainerTest() throws UnsupportedOperationException, ClassCastException,
			NullPointerException, IllegalArgumentException, TimeSetException,
			ElementAlreadyAddedException, ElementNotFoundException {

		cont.addLabEvent(l1);
		cont.addLabEvent(l2);
		
		assertEquals(l1.getName(), cont.getLabEvent(l1.getName()).getName());
		assertEquals(l2.getName(), cont.getLabEvent(l2.getName()).getName());

		cont.removeLabEvent(l2,true);
		
		cont.getLabEvent(l1.getName());
		
		try {
			cont.getLabEvent(l2.getName());
		} catch (ElementNotFoundException e) {
			return;
		}

		fail("ElementNotFoundException not thrown.");
	}
}
