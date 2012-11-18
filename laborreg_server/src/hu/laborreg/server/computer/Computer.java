package hu.laborreg.server.computer;

import hu.laborreg.server.Constants;
import hu.laborreg.server.exception.WrongIpAddressException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Computer {
	
	private String IpAddress;
	private boolean isMultipleRegistrationAllowed;
	
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
	 * Validates the given IP address (it should be in [0-255].[0-255].[0-255].[0-255] format,
	 * and should be in the valid IP address range (which is valid for Lab computers).
	 * @param IpAddress The IP address which should be validated. 
	 * @return true if IpAddress is valid, false if IpAddress is invalid. 	
	 */
	private boolean validateIpAddress(final String IpAddress) throws WrongIpAddressException
	{          
		boolean retVal = true;
		
		Pattern pattern = Pattern.compile(Constants.VALID_IP_ADDRESS_PATTERN);
		Matcher matcher = pattern.matcher(IpAddress);
		
		if(matcher.matches() == false)
		{
			retVal = false;
			throw new WrongIpAddressException("Ip address " + IpAddress + " is invalid. The valid format is: [0-255].[0-255].[0-255].[0-255]");
		}
			
		String[] currentIpAddressOctets = IpAddress.split(Constants.IP_ADDRESS_DELIMITER);
		String[] smallestIpAddressOctets = Constants.SMALLEST_VALID_IP_ADDRESS.split(Constants.IP_ADDRESS_DELIMITER);
		String[] biggestIpAddressOctets = Constants.BIGGEST_VALID_IP_ADDRESS.split(Constants.IP_ADDRESS_DELIMITER);
		
		for(int i=0; i<currentIpAddressOctets.length; i++)
		{
			int currentIpAddressOctet = Integer.parseInt(currentIpAddressOctets[i]);
			int smallestIpAddressOctet = Integer.parseInt(smallestIpAddressOctets[i]);
			int biggestIpAddressOctet = Integer.parseInt(biggestIpAddressOctets[i]);
			
			if(currentIpAddressOctet < smallestIpAddressOctet || currentIpAddressOctet > biggestIpAddressOctet)
			{
				retVal=false;
				throw new WrongIpAddressException("Ip address " + IpAddress + " invalid. It should be between " + Constants.SMALLEST_VALID_IP_ADDRESS +
													" and " + Constants.BIGGEST_VALID_IP_ADDRESS);
			}
		}
		return retVal;
	}
}
