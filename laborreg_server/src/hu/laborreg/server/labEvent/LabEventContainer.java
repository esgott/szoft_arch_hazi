package hu.laborreg.server.labEvent;


import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LabEventContainer {
	
	private Set<LabEvent> labEvents;
	
	/**
	 * A container class which contains the labEvents.
	 */
	public LabEventContainer()
	{
		labEvents = new HashSet<LabEvent>();
		//TODO read the existing labEvents from the DB.
	}
	
	/**
	 * Add labEvent to the labEvents list.
	 * @param labEvent The needed labEvent
	 */
	public void addLabEvent(LabEvent labEvent) throws ElementAlreadyAddedException, UnsupportedOperationException, ClassCastException, NullPointerException, IllegalArgumentException
	{
		if(this.labEvents.add(labEvent) == false)
		{
			throw new ElementAlreadyAddedException("Lab event " + labEvent.getName() + " already added to Lab events list.");
		}
	}
	
	/**
	 * Remove labEvent to the labEvents list.
	 * @param labEvent The needed labEvent
	 */
	public void removeLabEvent(LabEvent labEvent) throws ElementNotFoundException, UnsupportedOperationException, ClassCastException, NullPointerException
	{
		if(this.labEvents.remove(labEvent) == false)
		{
			throw new ElementNotFoundException("Lab Event " + labEvent.getName() + " does not found in Lab events list.");
		}
	}
	
	/**
	 * Get the specified labEvent from labEvents.
	 * @param name The name of the labEvent (must be unique).
	 * @param year The year of the labEvent.
	 * @return The needed labEvent
	 */
	public LabEvent getLabEvent(String name, String courseName, int courseYear)
	{
		Iterator<LabEvent> it = labEvents.iterator();
		
		while(it.hasNext())
		{
			LabEvent retVal = it.next();
			if(retVal.getName().equals(name) && retVal.getCourseName().equals(courseName) && retVal.getCourseYear() == courseYear)
			{
				return retVal;
			}
		}
		return null;
	}
}
