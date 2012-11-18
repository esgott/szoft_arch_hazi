package hu.laborreg.server.computer;

import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;

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
	 */
	public void addComputer(Computer computer) throws ElementAlreadyAddedException, ClassCastException, NullPointerException, IllegalArgumentException
	{
		if(this.computers.add(computer) == false)
		{
			throw new ElementAlreadyAddedException("Computer " + computer.getIpAddress() + " is already added to Computers list.");
		}
	}
	
	/**
	 * Remove computer to the computers list.
	 * @param computer The needed computer.
	 */
	public void removeComputer(Computer computer) throws ElementNotFoundException, UnsupportedOperationException, ClassCastException, NullPointerException
	{
		if(this.computers.remove(computer) == false)
		{
			throw new ElementNotFoundException("Computer " + computer.getIpAddress() + " does not found in Computers list.");
		}
	}
	
	/**
	 * Get the specified computer from computers.
	 * @param name The name of the computer (must be unique).
	 * @param year The year of the computer.
	 * @return The needed computer
	 */
	public Computer getComputer(String IpAddress) throws ElementNotFoundException
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
		throw new ElementNotFoundException("Computer " + IpAddress + " does not found in Computers list.");
	}
}