package hu.laborreg.server.computer;

import hu.laborreg.server.Configuration;
import hu.laborreg.server.Constants;
import hu.laborreg.server.exception.WrongIpAddressException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Computer {
	
	private String IpAddress;
	
	private final Configuration configuration;
	
	/**
	 * This class represents the computers in the lab. The lab leader can set the "multiple registration allowed" flag in each Computer.
	 * From these Computers, server accept more than one sign ins from Students (but only from these).
	 * @param IPaddress
	 */
	public Computer(String IpAddress, Configuration config) throws WrongIpAddressException
	{
		configuration = config;
		if(validateIpAddress(IpAddress))
		{
			this.IpAddress = IpAddress;
		}
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
		String[] smallestIpAddressOctets = configuration.getProperty(Configuration.smallestIpAddress).split(Constants.IP_ADDRESS_DELIMITER);
		String[] biggestIpAddressOctets = configuration.getProperty(Configuration.biggestIpAddress).split(Constants.IP_ADDRESS_DELIMITER);
		
		for(int i=0; i<currentIpAddressOctets.length; i++)
		{
			int currentIpAddressOctet = Integer.parseInt(currentIpAddressOctets[i]);
			int smallestIpAddressOctet = Integer.parseInt(smallestIpAddressOctets[i]);
			int biggestIpAddressOctet = Integer.parseInt(biggestIpAddressOctets[i]);
			
			if(currentIpAddressOctet < smallestIpAddressOctet || currentIpAddressOctet > biggestIpAddressOctet)
			{
				retVal=false;
				throw new WrongIpAddressException("Ip address " + IpAddress + " invalid. It should be between " +
												configuration.getProperty(Configuration.smallestIpAddress) + " and " +
												configuration.getProperty(Configuration.biggestIpAddress));
			}
		}
		return retVal;
	}
	
	@Override
	public synchronized boolean equals(Object other) {
		if (this == other) {
			return true;
		} else if (!(other instanceof Computer)) {
			return false;
		}
		Computer computer = (Computer) other;
		return computer.IpAddress.equals(IpAddress);
	}
}
