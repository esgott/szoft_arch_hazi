package hu.laborreg.server.computer;

import static org.junit.Assert.*;

import org.junit.Test;

import hu.laborreg.server.Constants;
import hu.laborreg.server.exception.WrongIpAddressException;

public class ComputerTest {
	
	private Computer c1;
	private Computer c2;
	
	private void init() throws WrongIpAddressException
	{
		c1 = new Computer(Constants.SMALLEST_VALID_IP_ADDRESS);
		c2 = new Computer(Constants.BIGGEST_VALID_IP_ADDRESS);
	}
	
	@Test
	public void basicAttributesTest() throws WrongIpAddressException
	{
		init();
		
		assertEquals(Constants.SMALLEST_VALID_IP_ADDRESS,c1.getIpAddress());
		assertEquals(Constants.BIGGEST_VALID_IP_ADDRESS,c2.getIpAddress());
		
		c1.allowMultipleRegistration();
		assertEquals(false,c2.isMultipleRegistrationAllowed());
		assertEquals(true, c1.isMultipleRegistrationAllowed());
	}
	
	@Test
	public void invalidFormatOfIpAddress()
	{
		int numberOfThrownExceptions = 0;
		
		try
		{
			c1 = new Computer(Constants.BIGGEST_VALID_IP_ADDRESS);
		}
		catch(WrongIpAddressException e)
		{
			numberOfThrownExceptions++; //valid
		}
		
		try
		{
			c1 = new Computer("aaa");
		}
		catch(WrongIpAddressException e)
		{
			numberOfThrownExceptions++; //invalid
		}
		
		try
		{
			c1 = new Computer("100.101.102.");
		}
		catch(WrongIpAddressException e)
		{
			numberOfThrownExceptions++; //invalid
		}
		
		
		try
		{
			c1 = new Computer("a.b.c.d");
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
		String[] smallestValidIpAddress = Constants.SMALLEST_VALID_IP_ADDRESS.split(Constants.IP_ADDRESS_DELIMITER);
		String[] biggestValidIpAddress = Constants.BIGGEST_VALID_IP_ADDRESS.split(Constants.IP_ADDRESS_DELIMITER);
		String IpAddress;
		
		int numberOfThrownExceptions = 0;
		
		try
		{
			IpAddress = smallestValidIpAddress[0] + "." + smallestValidIpAddress[1] + "." +
					smallestValidIpAddress[2] + "." + smallestValidIpAddress[3];
			c1 = new Computer(IpAddress);
		}
		catch(WrongIpAddressException e)
		{
			numberOfThrownExceptions++; //valid
		}
		
		try
		{
			IpAddress = biggestValidIpAddress[0] + "." + biggestValidIpAddress[1] + "." +
					biggestValidIpAddress[2] + "." + biggestValidIpAddress[3];
			c1 = new Computer(IpAddress);
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
			System.out.println(IpAddress);
			c1 = new Computer(IpAddress);
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
			System.out.println(IpAddress);
			c1 = new Computer(IpAddress);
		}
		catch(WrongIpAddressException e)
		{
			numberOfThrownExceptions++; //invalid
		}
		
		try
		{
			int badOctet = 256;
			IpAddress = smallestValidIpAddress[0] + "." + badOctet + "." + smallestValidIpAddress[2] + "." + smallestValidIpAddress[3];
			System.out.println(IpAddress);
			c1 = new Computer(IpAddress);
		}
		catch(WrongIpAddressException e)
		{
			numberOfThrownExceptions++; //invalid
		}
		
		try
		{
			int badOctet = -1;
			IpAddress = smallestValidIpAddress[0] + "." + badOctet + "." + smallestValidIpAddress[2] + "." + smallestValidIpAddress[3];
			System.out.println(IpAddress);
			c1 = new Computer(IpAddress);
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
