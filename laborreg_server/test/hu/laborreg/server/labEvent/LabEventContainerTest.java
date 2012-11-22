package hu.laborreg.server.labEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;

import hu.laborreg.server.course.Course;
import hu.laborreg.server.db.DBConnectionHandler;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.exception.TimeSetException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LabEventContainerTest {

	@Mock
	DBConnectionHandler mockDbConnectionHandler;

	private LabEventContainer cont;
	private Course c1;
	private Course c2;
	private LabEvent l1;
	private LabEvent l2;
	private LabEvent l3;

	@Before
	public void init() throws ParseException, TimeSetException {
		MockitoAnnotations.initMocks(this);
		cont = new LabEventContainer(mockDbConnectionHandler);

		c1 = new Course("course1", 1999);
		c2 = new Course("course2", 2001);

		l1 = new LabEvent("lab_event_1", c1.getName(), c1.getYear(), "12:00", "14:00");
		l2 = new LabEvent("lab_event_2", c1.getName(), c1.getYear(), "13:00", "16:00");
		l3 = new LabEvent("lab_event_3", c2.getName(), c2.getYear(), "10:00", "12:00");
	}

	@Test
	public void addLabEventToContainerTest() throws UnsupportedOperationException, ClassCastException,
			NullPointerException, IllegalArgumentException, ParseException, TimeSetException,
			ElementAlreadyAddedException {
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
			NullPointerException, IllegalArgumentException, ParseException, TimeSetException,
			ElementAlreadyAddedException, ElementNotFoundException {
		cont.addLabEvent(l1);
		cont.addLabEvent(l2);

		cont.removeLabEvent(l1);

		try {
			cont.removeLabEvent(l1);
		} catch (ElementNotFoundException e) {
			return;
		}

		fail("ElementNotFoundException not thrown.");
	}

	@Test
	public void getLabEventFromContainerTest() throws UnsupportedOperationException, ClassCastException,
			NullPointerException, IllegalArgumentException, ParseException, TimeSetException,
			ElementAlreadyAddedException, ElementNotFoundException {
		cont.addLabEvent(l1);
		cont.addLabEvent(l2);

		assertEquals(l1.getName(), cont.getLabEvent(l1.getName(), l1.getCourseName(), l1.getCourseYear()).getName());
		assertEquals(l2.getName(), cont.getLabEvent(l2.getName(), l2.getCourseName(), l2.getCourseYear()).getName());

		cont.removeLabEvent(l2);

		try {
			cont.getLabEvent(l2.getName(), l2.getCourseName(), l2.getCourseYear());
		} catch (ElementNotFoundException e) {
			return;
		}

		fail("ElementNotFoundException not thrown.");
	}
}
