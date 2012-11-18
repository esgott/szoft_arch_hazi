package hu.laborreg.server.computer;

import static org.junit.Assert.*;

import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.exception.WrongIpAddressException;

import org.junit.Test;

public class ComputerContainerTest {

	private ComputerContainer cont;
	private Computer c1;
	private Computer c2;
	
	private void init() throws WrongIpAddressException
	{
		cont = new ComputerContainer();
		c1 = new Computer("1.2.3.4");
		c2 = new Computer("234.123.211.123");
	}
	
	@Test
	public void addComputerToContainerTest() throws UnsupportedOperationException, ClassCastException, NullPointerException,
												IllegalArgumentException, ElementAlreadyAddedException, WrongIpAddressException
	{
		init();
		
		cont.addComputer(c1);
		cont.addComputer(c2);
		
		try
		{
			cont.addComputer(c1);
		}
		catch(ElementAlreadyAddedException e)
		{
			return;
		}
		
		fail("ElementAlreadyAddedException not thrown.");
	}
	
	@Test
	public void removeComputerFromContainerTest() throws UnsupportedOperationException, ClassCastException, NullPointerException,
													IllegalArgumentException, ElementAlreadyAddedException, ElementNotFoundException,
													WrongIpAddressException
	{
		init();
		cont.addComputer(c1);
		cont.addComputer(c2);

		cont.removeComputer(c1);
		
		try
		{
			cont.removeComputer(c1);
		}
		catch(ElementNotFoundException e)
		{
			return;
		}
		
		fail("ElementNotFoundException not thrown.");
	}
	
	@Test
	public void getComputerFromContainerTest() throws UnsupportedOperationException, ClassCastException, NullPointerException,
												IllegalArgumentException, ElementAlreadyAddedException, ElementNotFoundException,
												WrongIpAddressException
	{
		init();
		
		cont.addComputer(c1);
		cont.addComputer(c2);
		
		assertEquals(c1.getIpAddress(),cont.getComputer(c1.getIpAddress()).getIpAddress());
		assertEquals(c2.getIpAddress(),cont.getComputer(c2.getIpAddress()).getIpAddress());
		
		cont.removeComputer(c2);
		
		try
		{
			cont.getComputer(c2.getIpAddress());
		}
		catch(ElementNotFoundException e)
		{
			return;
		}
		
		fail("ElementNotFoundException not thrown.");
	}
}
