package hu.laborreg.server.gui;

import hu.laborreg.server.course.Course;
import hu.laborreg.server.course.CourseContainer;
import hu.laborreg.server.exception.ElementNotFoundException;

import javax.swing.table.AbstractTableModel;

public class CourseTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -4496658984873192301L;

	private final CourseContainer courses;
	private String[] columnNames;

	public CourseTableModel(CourseContainer courseContainer) {
		courses = courseContainer;
		columnNames = new String[2];
		columnNames[0] = "Név";
		columnNames[1] = "Év";
	}

	@Override
	public int getRowCount() {
		return courses.getNumberOfCourses();
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
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0) {
			return String.class.getClass();
		} else if (columnIndex == 1) {
			return int.class.getClass();
		} else {
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Course course = courses.getCourse(rowIndex);
		if (columnIndex == 0) {
			return course.getName();
		} else if (columnIndex == 1) {
			return course.getYear();
		} else {
			return null;
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		return;
	}

	public void deleteRow(int rowIndex) throws ElementNotFoundException {
		Course course = courses.getCourse(rowIndex);
		courses.removeCourse(course);
		fireTableRowsDeleted(rowIndex, rowIndex);
	}
	
	public Course getCourseAt(int rowIndex) {
		return courses.getCourse(rowIndex);
	}

}
