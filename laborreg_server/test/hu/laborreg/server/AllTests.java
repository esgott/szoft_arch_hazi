package hu.laborreg.server;

import hu.laborreg.server.db.DBConnectionHandlerTest;
import hu.laborreg.server.db.DBInitiatorTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DBConnectionHandlerTest.class, DBInitiatorTest.class })
public class AllTests {

}
