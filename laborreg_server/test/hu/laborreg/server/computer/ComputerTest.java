package hu.laborreg.server.computer;

import static org.junit.Assert.*;


import org.junit.Test;

import hu.laborreg.server.Configuration;
import hu.laborreg.server.Constants;
import hu.laborreg.server.exception.WrongIpAddressException;

public class ComputerTest {
	
	private static Configuration configuration = new Configuration(new String[0]);
	
	
	@Test
	public void invalidFormatOfIpAddress()
	{
		int numberOfThrownExceptions = 0;
		
		try
		{
			new Computer(configuration.getProperty(Configuration.biggestIpAddress), configuration);
		}
		catch(WrongIpAddressException e)
		{
			numberOfThrownExceptions++; //valid
		}
		
		try
		{
			new Computer("aaa", configuration);
		}
		catch(WrongIpAddressException e)
		{
			numberOfThrownExceptions++; //invalid
		}
		
		try
		{
			new Computer("100.101.102.", configuration);
		}
		catch(WrongIpAddressException e)
		{
			numberOfThrownExceptions++; //invalid
		}
		
		
		try
		{
			new Computer("a.b.c.d", configuration);
		}
		catch(WrongIpAddressException e)
		{
			numberOfThrownExceptions++; //invalid
		}
		
		if(numberOfThrownExceptions == 3)
		{
			return;
		}
		else
		{
			fail("Bad number of WrongIpAddressExceptions thrown.");
		}	
	}
	
	@Test
	public void invalidValueOfIpAddress()
	{
		String[] smallestValidIpAddress = configuration.getProperty(Configuration.smallestIpAddress).split(Constants.IP_ADDRESS_DELIMITER);
		String[] biggestValidIpAddress = configuration.getProperty(Configuration.biggestIpAddress).split(Constants.IP_ADDRESS_DELIMITER);
		String IpAddress;
		
		int numberOfThrownExceptions = 0;
		
		try
		{
			IpAddress = smallestValidIpAddress[0] + "." + smallestValidIpAddress[1] + "." +
					smallestValidIpAddress[2] + "." + smallestValidIpAddress[3];
			new Computer(IpAddress, configuration);
		}
		catch(WrongIpAddressException e)
		{
			numberOfThrownExceptions++; //valid
		}
		
		try
		{
			IpAddress = biggestValidIpAddress[0] + "." + biggestValidIpAddress[1] + "." +
					biggestValidIpAddress[2] + "." + biggestValidIpAddress[3];
			new Computer(IpAddress, configuration);
		}
		catch(WrongIpAddressException e)
		{
			numberOfThrownExceptions++; //valid
		}
		
		//invalid.valid.valid.valid
		try
		{
			int badOctet = Integer.parseInt(smallestValidIpAddress[0])-1;
			IpAddress = badOctet + "." + smallestValidIpAddress[1] + "." + smallestValidIpAddress[2] + "." + smallestValidIpAddress[3];
			new Computer(IpAddress, configuration);
		}
		catch(WrongIpAddressException e)
		{
			numberOfThrownExceptions++; //invalid
		}
		
		//invalid.valid.valid.valid
		try
		{
			int badOctet = Integer.parseInt(biggestValidIpAddress[0])+1;
			IpAddress = badOctet + "." + biggestValidIpAddress[1] + "." + biggestValidIpAddress[2] + "." + biggestValidIpAddress[3];
			new Computer(IpAddress, configuration);
		}
		catch(WrongIpAddressException e)
		{
			numberOfThrownExceptions++; //invalid
		}
		
		try
		{
			int badOctet = 256;
			IpAddress = smallestValidIpAddress[0] + "." + badOctet + "." + smallestValidIpAddress[2] + "." + smallestValidIpAddress[3];
			new Computer(IpAddress, configuration);
		}
		catch(WrongIpAddressException e)
		{
			numberOfThrownExceptions++; //invalid
		}
		
		try
		{
			int badOctet = -1;
			IpAddress = smallestValidIpAddress[0] + "." + badOctet + "." + smallestValidIpAddress[2] + "." + smallestValidIpAddress[3];
			new Computer(IpAddress, configuration);
		}
		catch(WrongIpAddressException e)
		{
			numberOfThrownExceptions++; //invalid
		}
		
		if(numberOfThrownExceptions == 4)
		{
			return;
		}
		else
		{
			fail("Bad number of WrongIpAddressExceptions thrown.");
		}
	}
}
