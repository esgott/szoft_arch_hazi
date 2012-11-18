package hu.laborreg.server;

import hu.laborreg.server.db.DBConnectionHandlerTest;
import hu.laborreg.server.db.DBInitiatorTest;
import hu.laborreg.server.http.HttpFileHandlerTest;
import hu.laborreg.server.http.HttpRequestListenerThreadTest;
import hu.laborreg.server.http.HttpWorkerThreadTest;
import hu.laborreg.server.labEvent.LabEventContainerTest;
import hu.laborreg.server.labEvent.LabEventTest;
import hu.laborreg.server.student.StudentContainerTest;
import hu.laborreg.server.student.StudentTest;
import hu.laborreg.server.computer.ComputerContainerTest;
import hu.laborreg.server.computer.ComputerTest;
import hu.laborreg.server.course.CourseContainerTest;
import hu.laborreg.server.course.CourseTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DBConnectionHandlerTest.class, DBInitiatorTest.class, HttpWorkerThreadTest.class,
		HttpRequestListenerThreadTest.class, HttpFileHandlerTest.class,  CourseContainerTest.class, CourseTest.class,
		ComputerContainerTest.class, /*ComputerTest.class, LabEventContainerTest.class, LabEventTest.class,
		StudentContainerTest.class, StudentTest.class*/})
public class AllTests {

	public class HttpFileHandlerTest {

	}

}
