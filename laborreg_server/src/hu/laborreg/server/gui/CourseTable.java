package hu.laborreg.server.gui;

import hu.laborreg.server.course.Course;
import hu.laborreg.server.course.CourseContainer;
import hu.laborreg.server.exception.ElementNotFoundException;

import java.awt.BorderLayout;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class CourseTable extends JPanel implements TableInterface {

	private static final long serialVersionUID = 1207047791312415309L;
	private final JTable table;
	private final CourseTableModel tableModel;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	public CourseTable(CourseContainer courses) {
		setLayout(new BorderLayout());

		tableModel = new CourseTableModel(courses);
		table = new JTable(tableModel);
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
	}

	@Override
	public void dataChanged() {
		tableModel.fireTableDataChanged();
	}

	@Override
	public void deleteCurrent() throws ElementNotFoundException {
		int rowIndex = table.getSelectedRow();
		tableModel.deleteRow(rowIndex);
	}

	@Override
	public void displayDetails() {
		int rowIndex = table.getSelectedRow();
		try {
			Course course = tableModel.getCourseAt(rowIndex);
			StringBuilder message = new StringBuilder("Kurzus név: :");
			message.append(course.getName());
			message.append("\nKurzus év: ");
			message.append(course.getYear());
			message.append("\nRegisztrált hallgatók: ");
			message.append(course.getRegisteredStudentsAsString());
			message.append("\nLaboresemények: ");
			message.append(course.getLabEventsAsString());
			JOptionPane.showMessageDialog(this, message.toString(), "Kurzus Részletek", JOptionPane.PLAIN_MESSAGE);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.info("Failed to display details, probably wrong selection");
		}
	}

	public Course getCurrentElement() throws ArrayIndexOutOfBoundsException {
		int rowIndex = table.getSelectedRow();
		Course course = tableModel.getCourseAt(rowIndex);
		return course;
	}
}
