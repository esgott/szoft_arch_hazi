package hu.laborreg.server.computer;

import hu.laborreg.server.exception.WrongIpAddressException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Computer {
	
	private String IpAddress;
	private boolean isMultipleRegistrationAllowed;
	
	private static final String IpAddressPattern = 
	        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	/**
	 * This class represents the computers in the lab. The lab leader can set the "multiple registration allowed" flag in each Computer.
	 * From these Computers, server accept more than one sign ins from Students (but only from these).
	 * @param IPaddress
	 */
	public Computer(String IpAddress) throws WrongIpAddressException
	{
		isMultipleRegistrationAllowed = false;
		
		if(validateIpAddress(IpAddress))
		{
			this.IpAddress = IpAddress;
		}
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
	
	/**
	 * Validates the given IP address (it should be in [0-255].[0-255].[0-255].[0-255] format.
	 * @param IpAddress The IP address which should be validated. 
	 */
	private boolean validateIpAddress(final String IpAddress) throws WrongIpAddressException
	{          
		boolean retVal = true;
		
		Pattern pattern = Pattern.compile(IpAddressPattern);
		Matcher matcher = pattern.matcher(IpAddress);
		
		if(matcher.matches() == false)
		{
			retVal = false;
			throw new WrongIpAddressException("Ip address " + IpAddress + " is invalid. The valid format is: [0-255].[0-255].[0-255].[0-255]");
		}
		
		return retVal;
	}
}
