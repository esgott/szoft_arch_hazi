package hu.laborreg.server.db;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DBConnectionHandlerTest {
	
	@Mock
	private Connection mockConnection;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void test() {
		DBConnectionHandler dbConnHandler = new DBConnectionHandler(mockConnection);
		fail("Not yet implemented");
	}

}
