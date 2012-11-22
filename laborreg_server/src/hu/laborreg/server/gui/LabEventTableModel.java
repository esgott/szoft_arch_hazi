package hu.laborreg.server.gui;

import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.labEvent.LabEvent;
import hu.laborreg.server.labEvent.LabEventContainer;

import javax.swing.table.AbstractTableModel;

public class LabEventTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private final LabEventContainer labEvents;
	private String[] columnNames;

	public LabEventTableModel(LabEventContainer labEventContainer) {
		labEvents = labEventContainer;
		columnNames = new String[5];
		columnNames[0] = "Név";
		columnNames[1] = "Kurzus";
		columnNames[2] = "Kurzus év";
		columnNames[3] = "Kezdés";
		columnNames[4] = "Vége";
	}

	@Override
	public int getRowCount() {
		return labEvents.getNumberOfLabevents();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		LabEvent labEvent = labEvents.getLabEvent(rowIndex);
		switch (columnIndex) {
		case 0:
			return labEvent.getName();
		case 1:
			return labEvent.getCourseName();
		case 2:
			return labEvent.getCourseYear();
		case 3:
			return labEvent.getStartTime();
		case 4:
			return labEvent.getStopTime();
		}
		return null;
	}

	public void deleteRow(int rowIndex) throws ElementNotFoundException {
		LabEvent labEvent = labEvents.getLabEvent(rowIndex);
		labEvents.removeLabEvent(labEvent);
		fireTableRowsDeleted(rowIndex, rowIndex);
	}

	public LabEvent getLabEventAt(int rowIndex) {
		return labEvents.getLabEvent(rowIndex);
	}

}
