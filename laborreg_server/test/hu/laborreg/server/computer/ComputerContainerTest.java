package hu.laborreg.server.computer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import hu.laborreg.server.Constants;
import hu.laborreg.server.db.DBConnectionHandler;
import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.exception.WrongIpAddressException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ComputerContainerTest {
	
	@Mock 
	DBConnectionHandler mockDbConnectionHandler;
	@Mock
	PreparedStatement mockPreparedStatement;
	@Mock
	ResultSet mockResultset;

	private ComputerContainer cont;
	private Computer c1;
	private Computer c2;
	
	@Before
	public void init() throws WrongIpAddressException, SQLException
	{
		MockitoAnnotations.initMocks(this);
		
		when(mockDbConnectionHandler.createPreparedStatement(anyString())).thenReturn(mockPreparedStatement);
		when(mockPreparedStatement.executeQuery()).thenReturn(mockResultset);
		when(mockResultset.next()).thenReturn(false);
		
		cont = new ComputerContainer(mockDbConnectionHandler);
		
		verify(mockPreparedStatement).executeQuery();
		
		c1 = new Computer(Constants.BIGGEST_VALID_IP_ADDRESS);
		c2 = new Computer(Constants.SMALLEST_VALID_IP_ADDRESS);
	}
	
	@Test
	public void addComputerToContainerTest() throws ElementAlreadyAddedException, SQLException
	{
		cont.addComputer(c1);
		cont.addComputer(c2);
		verify(mockPreparedStatement, times(2)).executeUpdate();
		
		
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
	public void removeComputerFromContainerTest() throws ElementAlreadyAddedException, ElementNotFoundException, SQLException
	{
		cont.addComputer(c1);
		cont.addComputer(c2);

		cont.removeComputer(c1);
		
		verify(mockPreparedStatement, times(3)).executeUpdate();
		
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
	public void getComputerFromContainerTest() throws ElementAlreadyAddedException, ElementNotFoundException, SQLException
	{
		cont.addComputer(c1);
		cont.addComputer(c2);
		
		assertEquals(c1.getIpAddress(),cont.getComputer(c1.getIpAddress()).getIpAddress());
		assertEquals(c2.getIpAddress(),cont.getComputer(c2.getIpAddress()).getIpAddress());
		
		cont.removeComputer(c2);
		verify(mockPreparedStatement, times(3)).executeUpdate();
		
		
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
