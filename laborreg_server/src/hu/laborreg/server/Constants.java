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

	
	//LabEventContainer.addLabEvent()
	//LabEventConatiner.removeLabEvent()
	//TODO CourseContainer.addCourse()
	//TODO CourseContainer.removeCourse()
	//TODO StudentContainer.addStudent()
	//TODO StudentContainer.removeStudent()
	//TODO ComputerContainer.addComputer()
	//TODO Computercontainer.removeComputer()
	//TODO Course.registerStudent()
	//TODO Course.unregisterStudent()
	//TODO Course.addLabEvent()
	//TODO Course.removeLabEvent()
	public static final int CONTAINER_OK = 0;
	public static final int CONTAINER_ALREADY_CONSISTS_THIS_ELEMENT = 1;
	public static final int CONTAINER_DOES_NOT_CONTAIN_THIS_ELEMENT = 2;
}
