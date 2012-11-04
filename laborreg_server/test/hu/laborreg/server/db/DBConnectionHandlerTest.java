package hu.laborreg.server.db;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DBConnectionHandlerTest {

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
	public void test() throws SQLException {
		when(mockConnection.getMetaData()).thenReturn(mockMetaData);
		when(mockMetaData.getTables(null, null, null, new String[] { "TABLE" })).thenReturn(mockResultSet);
		when(mockConnection.createStatement()).thenReturn(mockStatement);

		/* DBConnectionHandler dbConnHandler = */new DBConnectionHandler(mockConnection);
		fail("Not yet implemented");
	}

}
