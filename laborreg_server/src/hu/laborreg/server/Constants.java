package hu.laborreg.server;

public abstract class Constants {
	
	//LabEvent.allowMultipleRegistration()
	public static final int TIME_IS_OK = 0;
	public static final int STARTTIME_IS_LATER_THAN_STOPTIME = 1;
	public static final int LABEVENT_IS_ONGOING = 2;
	public static final int LABEVENT_FINISHED = 3;
	
	//LabEvent.setStartAndStopDate()
	public static final int COMPUTER_ADDED = 0;
	public static final int COMPUTER_ALREADY_ADDED = 1;

	//Course.registerStudent()
	//Course.unregisterStudent()
	public static final int STUDENT_REGISTERED = 0;
	public static final int STUDENT_ALREADY_REGISTERED = 1;
	public static final int STUDENT_UNREGISTERED = 0;
	public static final int STUDENT_NOT_FOUND_IN_THE_REGISTERED_STUDENTS_LIST = 1;
	
	//Course.addLabEvent()
	//Course.removeLabEvent()
	public static final int LAB_EVENT_ADDED = 0;
	public static final int LAB_EVENT_ALREADY_ADDED = 1;
	public static final int LAB_EVENT_REMOVED = 0;
	public static final int LAB_EVENT_NOT_FOUND_IN_THE_LAB_EVENTS_LIST = 1;
	
	
	//LabEventContainer.addLabEvent()
	//LabEventConatiner.removeLabEvent()
	//CourseContainer.addCourse()
	//CourseContainer.removeCourse()
	//StudentContainer.addStudent()
	//StudentContainer.removeStudent()
	//ComputerContainer.addComputer()
	//Computercontainer.removeComputer()
	public static final int CONTAINER_OK = 0;
	public static final int CONTAINER_ALREADY_CONTAINS_THIS_ELEMENT = 1;
	public static final int CONTAINER_DOES_NOT_CONTAIN_THIS_ELEMENT = 2;
}
