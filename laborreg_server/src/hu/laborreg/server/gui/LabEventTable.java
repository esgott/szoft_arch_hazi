package hu.laborreg.server.gui;

import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.labEvent.LabEvent;
import hu.laborreg.server.labEvent.LabEventContainer;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class LabEventTable extends JPanel implements TableInterface{

	private static final long serialVersionUID = 983632928597677866L;
	private JTable table;
	private LabEventTableModel labEventTableModel;

	public LabEventTable(LabEventContainer labEventContainer) {
		setLayout(new BorderLayout());
		
		labEventTableModel = new LabEventTableModel(labEventContainer);
		table = new JTable(labEventTableModel);
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
	}

	@Override
	public void dataChanged() {
		labEventTableModel.fireTableDataChanged();
	}

	@Override
	public void deleteCurrent() throws ElementNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayDetails() {
		// TODO Auto-generated method stub
		
	}

	public LabEvent getCurrentElement() {
		int rowIndex = table.getSelectedRow();
		LabEvent labEvent = labEventTableModel.getLabEventAt(rowIndex);
		return labEvent;
	}

}
