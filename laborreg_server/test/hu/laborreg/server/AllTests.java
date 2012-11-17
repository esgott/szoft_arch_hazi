package hu.laborreg.server;

import hu.laborreg.server.db.DBConnectionHandlerTest;
import hu.laborreg.server.db.DBInitiatorTest;
import hu.laborreg.server.http.HttpFileHandlerTest;
import hu.laborreg.server.http.HttpRequestListenerThreadTest;
import hu.laborreg.server.http.HttpWorkerThreadTest;
import hu.laborreg.server.course.CourseTest;
import hu.laborreg.server.labEvent.LabEventTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DBConnectionHandlerTest.class, DBInitiatorTest.class, HttpWorkerThreadTest.class,
		HttpRequestListenerThreadTest.class, HttpFileHandlerTest.class,  CourseTest.class, LabEventTest.class})
public class AllTests {

	public class HttpFileHandlerTest {

	}

}
