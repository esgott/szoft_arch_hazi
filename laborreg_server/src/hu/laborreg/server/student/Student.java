package hu.laborreg.server.student;

public class Student {
	
	private String neptunCode;
	
	/**
	 * This class represents the students and store their Neptun codes and names. When a student sign in to a LabEvent, the server identify him/her with help of these attributes.
	 * During sign in process, server checks the validity of the Neptun code. If the lab leader doesn't register the student to the course, the student will get an alarm about it.
	 * @param neptunCode The Neptun code of the student.
	 * @param name The name of the student.
	 */
	public Student(String neptunCode)
	{
		this.neptunCode = neptunCode.toUpperCase();
	}
	
	/**
	 * Returns the Neptun code of the student.
	 * @return Neptun code of the student.
	 */
	public String getNeptunCode()
	{
		return this.neptunCode;
	}
}
