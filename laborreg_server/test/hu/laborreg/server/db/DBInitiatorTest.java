package hu.laborreg.server.db;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DBInitiatorTest {

	@Mock
	private Connection mockConnection;
	@Mock
	private DatabaseMetaData mockMetaData;
	@Mock
	private ResultSet mockResultSet;
	@Mock
	private Statement mockStatement;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void tablesCreatedWhenNoTablesPresent() throws SQLException {
		DBInitiator initiator = new DBInitiator(mockConnection);

		when(mockConnection.getMetaData()).thenReturn(mockMetaData);
		when(mockMetaData.getTables(null, null, null, new String[] { "TABLE" })).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(false);
		when(mockConnection.createStatement()).thenReturn(mockStatement);

		initiator.initiate();

		verify(mockStatement, times(7)).executeUpdate(anyString());
		verify(mockStatement, times(7)).close();
	}

	@Test
	public void tablesCreatedWhenOnlyOtherTablesPresent() throws SQLException {
		DBInitiator initiator = new DBInitiator(mockConnection);

		when(mockConnection.getMetaData()).thenReturn(mockMetaData);
		when(mockMetaData.getTables(null, null, null, new String[] { "TABLE" })).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(true, true, true, false);
		when(mockResultSet.getString("TABLE_NAME")).thenReturn("abcd", "efgh", "cica");
		when(mockConnection.createStatement()).thenReturn(mockStatement);

		initiator.initiate();

		verify(mockStatement, times(7)).executeUpdate(anyString());
		verify(mockStatement, times(7)).close();
	}

	@Test
	public void courseTableNotCreatedWhenItIsPresent() throws SQLException {
		DBInitiator initiator = new DBInitiator(mockConnection);

		when(mockConnection.getMetaData()).thenReturn(mockMetaData);
		when(mockMetaData.getTables(null, null, null, new String[] { "TABLE" })).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(true, true, true, false);
		when(mockResultSet.getString("TABLE_NAME")).thenReturn("abcd", "course", "cica");
		when(mockConnection.createStatement()).thenReturn(mockStatement);

		initiator.initiate();

		verify(mockStatement, times(6)).executeUpdate(anyString());
		verify(mockStatement, times(6)).close();
		verifyNoMoreInteractions(mockStatement);
	}

	@Test
	public void noTablesCreatedWhenAllArePresent() throws SQLException {
		DBInitiator initiator = new DBInitiator(mockConnection);

		when(mockConnection.getMetaData()).thenReturn(mockMetaData);
		when(mockMetaData.getTables(null, null, null, new String[] { "TABLE" })).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(true, true, true, false);
		when(mockResultSet.getString("TABLE_NAME")).thenReturn("abcd", "course", "cica",
				"multiple_registration_allowed", "signed", "registered", "lab_event", "computer", "student");
		when(mockConnection.createStatement()).thenReturn(mockStatement);

		initiator.initiate();

		verify(mockStatement, times(6)).executeUpdate(anyString());
		verify(mockStatement, times(6)).close();
		verifyNoMoreInteractions(mockStatement);
	}

}
