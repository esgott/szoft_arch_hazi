package hu.laborreg.server.labEvent;


import hu.laborreg.server.Constants;

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
	 * @return If the container does not contain this LabEvent yet: CONTAINER_OK(0)
	 * 			If the container already contains this LabEvent: CONTAINER_ALREADY_CONSISTS_THIS_ELEMENT(1)
	 */
	public int addLabEvent(LabEvent labEvent)
	{
		try
		{
			if(this.labEvents.add(labEvent) == false)
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
	 * Remove labEvent to the labEvents list.
	 * @param labEvent The needed labEvent
	 * @return If the remove was successful: CONAINER_OK(0)
	 * 			If the container does not contain this LabEvent: CONTAINER_DOES_NOT_CONTAIN_THIS_ELEMENT(1)
	 */
	public int removeLabEvent(LabEvent labEvent)
	{
		
		try
		{
			if(this.labEvents.remove(labEvent) == false)
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
