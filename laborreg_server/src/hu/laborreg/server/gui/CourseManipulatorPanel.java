package hu.laborreg.server.gui;

import hu.laborreg.server.course.Course;
import hu.laborreg.server.course.CourseContainer;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class CourseManipulatorPanel extends ManipulatorPanel {

	private static final long serialVersionUID = -327589817126666321L;

	private JTextField yearTextField;
	private JTextField nameTextField;
	private final CourseContainer courses;
	private final MainWindow mainWindow;

	public CourseManipulatorPanel(CourseContainer courseContainer, MainWindow parent) {
		courses = courseContainer;
		mainWindow = parent;

		setLayout(new GridLayout(2, 2, 10, 10));

		JLabel yearLabel = new JLabel("Év:");
		add(yearLabel);
		yearTextField = new JTextField();
		add(yearTextField);
		yearTextField.setColumns(10);

		JLabel nameLabel = new JLabel("Név:");
		add(nameLabel);
		nameTextField = new JTextField();
		add(nameTextField);
		nameTextField.setColumns(10);

	}

	@Override
	public void clear() {
		yearTextField.setText("");
		nameTextField.setText("");
	}

	@Override
	public void commit() {
		try {
			String name = nameTextField.getText();
			int year = Integer.parseInt(yearTextField.getText());
			Course course = new Course(name, year);
			courses.addCourse(course);
			mainWindow.dataOfCoursesChanged();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Hozzáadás sikertelen", JOptionPane.ERROR_MESSAGE);
		}
	}

}
