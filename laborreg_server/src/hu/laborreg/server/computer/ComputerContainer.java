package hu.laborreg.server.computer;

import hu.laborreg.server.Constants;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ComputerContainer {
	
	private Set<Computer> computers;
	
	/**
	 * A container class which contains the computers.
	 */
	public ComputerContainer()
	{
		computers = new HashSet<Computer>();
		//TODO read the existing computers from the DB.
	}
	
	/**
	 * Add computer to the computers list.
	 * @param computer The needed computer.
	 * @return If the container does not contain this Computer yet: CONTAINER_OK(0)
	 * 			If the container already contains this Computer: CONTAINER_ALREADY_CONTAINS_THIS_ELEMENT(1)
	 */
	public int addComputer(Computer computer)
	{
		
		try
		{
			if(this.computers.add(computer) == false)
			{
				return Constants.CONTAINER_ALREADY_CONTAINS_THIS_ELEMENT;
			}
		}
		catch(UnsupportedOperationException e)
		{
			e.printStackTrace();
		}
		catch(ClassCastException e)
		{
			e.printStackTrace();
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		
		return Constants.CONTAINER_OK;
	}
	
	/**
	 * Remove computer to the computers list.
	 * @param computer The needed computer.
	 * @return If the remove was successful: CONAINER_OK(0)
	 * 			If the container does not contain this Computer: CONTAINER_DOES_NOT_CONTAIN_THIS_ELEMENT(1)
	 */
	public int removeComputer(Computer computer)
	{
		
		try
		{
			if(this.computers.remove(computer) == false)
			{
				return Constants.CONTAINER_DOES_NOT_CONTAIN_THIS_ELEMENT;
			}
		}
		catch(UnsupportedOperationException e)
		{
			e.printStackTrace();
		}
		catch(ClassCastException e)
		{
			e.printStackTrace();
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		
		return Constants.CONTAINER_OK;
	}
	
	/**
	 * Get the specified computer from computers.
	 * @param name The name of the computer (must be unique).
	 * @param year The year of the computer.
	 * @return The needed computer
	 */
	public Computer getComputer(String IpAddress)
	{
		Iterator<Computer> it = computers.iterator();
		
		while(it.hasNext())
		{
			Computer retVal = it.next();
			if(retVal.getIpAddress().equals(IpAddress))
			{
				return retVal;
			}
		}
		return null;
	}
}