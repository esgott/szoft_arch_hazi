package hu.laborreg.server.student;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import hu.laborreg.server.db.DBConnectionHandler;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StudentContainerTest {

	@Mock
	DBConnectionHandler mockDbConnectionHandler;
	@Mock
	PreparedStatement mockPreparedStatement;
	@Mock
	ResultSet mockResultset;

	private StudentContainer cont;
	private Student s1;
	private Student s2;

	@Before
	public void init() throws SQLException {
		MockitoAnnotations.initMocks(this);

		when(mockDbConnectionHandler.createPreparedStatement(anyString())).thenReturn(mockPreparedStatement);
		when(mockPreparedStatement.executeQuery()).thenReturn(mockResultset);
		when(mockResultset.next()).thenReturn(false);

		cont = new StudentContainer(mockDbConnectionHandler);
		s1 = new Student("abcdef");
		s2 = new Student("a123bcd");
	}

	@Test
	public void addStudentToContainerTest() throws ElementAlreadyAddedException, SQLException {
		cont.addStudent(s1);
		cont.addStudent(s2);

		verify(mockPreparedStatement, times(2)).executeUpdate();

		try {
			cont.addStudent(s1);
		} catch (ElementAlreadyAddedException e) {
			return;
		}

		fail("ElementAlreadyAddedException not thrown.");
	}

	@Test
	public void removeStudentFromContainerTest() throws ElementAlreadyAddedException, ElementNotFoundException,
			SQLException {
		cont.addStudent(s1);
		cont.addStudent(s2);

		cont.removeStudent(s1);

		verify(mockPreparedStatement, times(3)).executeUpdate();

		try {
			cont.removeStudent(s1);
		} catch (ElementNotFoundException e) {
			return;
		}

		fail("ElementNotFoundException not thrown.");
	}

	@Test
	public void getStduentFromContainerTest() throws ElementAlreadyAddedException, ElementNotFoundException,
			SQLException {
		cont.addStudent(s1);
		cont.addStudent(s2);

		try {
			cont.getStudent(s1.getNeptunCode());
			cont.getStudent(s2.getNeptunCode());
		} catch (ElementNotFoundException e) {
			fail("ElementNotFoundException thrown.");
		}

		cont.removeStudent(s2);

		verify(mockPreparedStatement, times(3)).executeUpdate();

		try {
			cont.getStudent(s2.getNeptunCode());
		} catch (ElementNotFoundException e) {
			return;
		}

		fail("ElementNotFoundException not thrown.");
	}
}
