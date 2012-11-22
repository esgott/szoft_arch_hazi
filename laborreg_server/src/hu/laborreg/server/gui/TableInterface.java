package hu.laborreg.server.gui;

import hu.laborreg.server.exception.ElementNotFoundException;

public interface TableInterface {

	public void dataChanged();

	public void deleteCurrent() throws ElementNotFoundException;

	public void displayDetails();

}
