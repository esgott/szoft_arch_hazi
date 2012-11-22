package hu.laborreg.server.labEvent;

import hu.laborreg.server.exception.ElementAlreadyAddedException;
import hu.laborreg.server.exception.ElementNotFoundException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LabEventContainer {

	private List<LabEvent> labEvents;

	/**
	 * A container class which contains the labEvents.
	 */
	public LabEventContainer() {
		labEvents = new ArrayList<LabEvent>();
		// TODO read the existing labEvents from the DB.
	}

	/**
	 * Add labEvent to the labEvents list.
	 * 
	 * @param labEvent
	 *            The needed labEvent
	 */
	public void addLabEvent(LabEvent labEvent) throws ElementAlreadyAddedException, UnsupportedOperationException,
			ClassCastException, NullPointerException, IllegalArgumentException {
		if (labEvents.contains(labEvent)) {
			labEvents.add(labEvent);
		} else {
			throw new ElementAlreadyAddedException("Lab event " + labEvent.getName()
					+ " already added to Lab events list.");
		}
	}

	/**
	 * Remove labEvent to the labEvents list.
	 * 
	 * @param labEvent
	 *            The needed labEvent
	 */
	public void removeLabEvent(LabEvent labEvent) throws ElementNotFoundException, UnsupportedOperationException,
			ClassCastException, NullPointerException {
		if (this.labEvents.remove(labEvent) == false) {
			throw new ElementNotFoundException("Lab Event " + labEvent.getName()
					+ " does not found in Lab events list.");
		}
	}

	/**
	 * Get the specified labEvent from labEvents.
	 * 
	 * @param name
	 *            The name of the labEvent (must be unique).
	 * @param year
	 *            The year of the labEvent.
	 * @return The needed labEvent
	 * @throws ElementNotFoundException
	 */
	public LabEvent getLabEvent(String name, String courseName, int courseYear) throws ElementNotFoundException {
		Iterator<LabEvent> it = labEvents.iterator();

		while (it.hasNext()) {
			LabEvent retVal = it.next();
			if (retVal.getName().equals(name) && retVal.getCourseName().equals(courseName)
					&& retVal.getCourseYear() == courseYear) {
				return retVal;
			}
		}
		throw new ElementNotFoundException("Lab event: " + name + " with course: " + courseName + "(" + courseYear
				+ ") does not found in the Lab events list.");
	}

	public int getNumberOfLabevents() {
		return labEvents.size();
	}

	public LabEvent getLabEvent(int index) {
		return labEvents.get(index);
	}
}
