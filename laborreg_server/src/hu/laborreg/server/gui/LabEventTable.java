package hu.laborreg.server.gui;

import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.labEvent.LabEvent;
import hu.laborreg.server.labEvent.LabEventContainer;

import java.awt.BorderLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class LabEventTable extends JPanel implements TableInterface {

	private static final long serialVersionUID = 983632928597677866L;
	private JTable table;
	private LabEventTableModel labEventTableModel;
	private final DateFormat formatter = new SimpleDateFormat(LabEventContainer.DATE_FORMAT);
	private final Logger logger = Logger.getLogger(this.getClass().getName());

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
		int rowIndex = table.getSelectedRow();
		labEventTableModel.deleteRow(rowIndex);
	}

	@Override
	public void displayDetails() {
		int rowIndex = table.getSelectedRow();
		try {
			LabEvent labEvent = labEventTableModel.getLabEventAt(rowIndex);
			StringBuilder message = new StringBuilder("Laboresemény név: ");
			message.append(labEvent.getName());
			message.append("\nKurzus név: ");
			message.append(labEvent.getCourseName());
			message.append("\nKurzus év: ");
			message.append(labEvent.getCourseYear());
			message.append("\nKezdés: ");
			message.append(formatter.format(labEvent.getStartTime()));
			message.append("\nVége: ");
			message.append(formatter.format(labEvent.getStopTime()));
			message.append("\nJelentkezett hallgatók: ");
			message.append(labEvent.getSignedInStudentsAsString());
			message.append("\nTöbbszörös jelentkezés engedélyezve: ");
			message.append(labEvent.getRegisteredComputersAsString());
			JOptionPane
					.showMessageDialog(this, message.toString(), "Laboresemény részletek", JOptionPane.PLAIN_MESSAGE);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.info("Failed to display details, probably wrong selection");
		}
	}

	public LabEvent getCurrentElement() {
		int rowIndex = table.getSelectedRow();
		LabEvent labEvent = labEventTableModel.getLabEventAt(rowIndex);
		return labEvent;
	}

}
