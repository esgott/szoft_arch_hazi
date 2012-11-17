package hu.laborreg.server.computer;

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
	 * @param computer The needed computer
	 */
	public void addComputer(Computer computer)
	{
		
		try
		{
			if(this.computers.add(computer) == false)
			{
				//TODO computers already contain this computer. Display error in error window?
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
	}
	
	/**
	 * Remove computer to the computers list.
	 * @param computer The needed computer
	 */
	public void removeComputer(Computer computer)
	{
		
		try
		{
			if(this.computers.remove(computer) == false)
			{
				//TODO computers doesn't contain this computer.  Display error in error window?
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