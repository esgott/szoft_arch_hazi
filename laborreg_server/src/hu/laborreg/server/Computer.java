package hu.laborreg.server;

public class Computer {
	
	private String IpAddress;
	private boolean isMultipleRegistrationAllowed;
	
	/**
	 * This class represents the computers in the lab. The lab leader can set the "multiple registration allowed" flag in each Computer.
	 * From these Computers, server accept more than one sign ins from Students (but only from these).
	 * @param IPaddress
	 */
	public Computer(String IpAddress)
	{
		this.IpAddress = IpAddress;
		isMultipleRegistrationAllowed = false;
	}
	
	/**
	 * Set the "multiple registration allowed" to true.
	 */
	public void allowMultipleRegistration()
	{
		this.isMultipleRegistrationAllowed = true;
	}
	
	/**
	 * Returns the current value of "multiple registration allowed" flag.
	 * @return The current value of "multiple registration allowed" flag.
	 */
	public boolean isMultipleRegistrationAllowed()
	{
		return this.isMultipleRegistrationAllowed;
	}
	
	/**
	 * Returns the IpAddress of the Computer.
	 * @return The IpAddress of the Computer.
	 */
	public String getIpAddress()
	{
		return this.IpAddress;
	}
}
