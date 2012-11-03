package hu.laborreg.server;

import hu.laborreg.server.db.DBConnectionHandlerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DBConnectionHandlerTest.class })
public class AllTests {

}
