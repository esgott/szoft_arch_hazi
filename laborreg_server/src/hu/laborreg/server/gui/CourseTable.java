package hu.laborreg.server.gui;

import hu.laborreg.server.course.CourseContainer;
import hu.laborreg.server.exception.ElementNotFoundException;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class CourseTable extends JPanel {

	private static final long serialVersionUID = 1207047791312415309L;
	private final JTable table;
	private final CourseTableModel tableModel;

	public CourseTable(CourseContainer courses) {
		setLayout(new BorderLayout());

		tableModel = new CourseTableModel(courses);
		table = new JTable(tableModel);
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
	}

	public void dataChanged() {
		tableModel.fireTableDataChanged();
	}

	public void deleteCurrent() throws ElementNotFoundException {
		int rowIndex = table.getSelectedRow();
		tableModel.deleteRow(rowIndex);
	}

}
