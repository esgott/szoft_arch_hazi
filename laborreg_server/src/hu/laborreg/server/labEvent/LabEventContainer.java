package hu.laborreg.server.labEvent;


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
	public void addLabEvent(LabEvent labEvent)
	{
		
		try
		{
			if(this.labEvents.add(labEvent) == false)
			{
				//TODO labEvents already contain this labEvent. Display error in error window?
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
	 * Remove labEvent to the labEvents list.
	 * @param labEvent The needed labEvent
	 */
	public void removeLabEvent(LabEvent labEvent)
	{
		
		try
		{
			if(this.labEvents.remove(labEvent) == false)
			{
				//TODO labEvents doesn't contain this labEvent.  Display error in error window?
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
	 * Get the specified labEvent from labEvents.
	 * @param name The name of the labEvent (must be unique).
	 * @param year The year of the labEvent.
	 * @return The needed labEvent
	 */
	public LabEvent getLabEvent(String name, int year)
	{
		Iterator<LabEvent> it = labEvents.iterator();
		
		while(it.hasNext())
		{
			LabEvent retVal = it.next();
			if(retVal.getName().equals(name) && retVal.getCourseName().equals(name))
			{
				return retVal;
			}
		}
		return null;
	}
}
