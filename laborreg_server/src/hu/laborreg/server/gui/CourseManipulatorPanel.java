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
	private JLabel neptunLabel;
	private JTextField neptunTextField;
	private Course oldCourse;

	public CourseManipulatorPanel(CourseContainer courseContainer, MainWindow parent) {
		courses = courseContainer;
		mainWindow = parent;
		oldCourse = new Course(" ", 0);

		setLayout(new GridLayout(4, 2, 10, 10));

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

		neptunLabel = new JLabel("Regisztrált NEPTUN\n kódok:");
		add(neptunLabel);

		neptunTextField = new JTextField();
		add(neptunTextField);
		neptunTextField.setColumns(10);

	}

	@Override
	public void clear() {
		yearTextField.setText("");
		nameTextField.setText("");
		neptunTextField.setText("");
	}

	@Override
	public void commit() {
		try {
			String name = nameTextField.getText();
			int year = Integer.parseInt(yearTextField.getText());
			String[] neptuns;
			if (neptunTextField.getText().equals("")) {
				neptuns = new String[0];
			} else {
				neptuns = neptunTextField.getText().split(",");
			}
			for (int i = 0; i < neptuns.length; i++) {
				String trimmedNeptun = neptuns[i].trim(); 
				neptuns[i] = trimmedNeptun;
			}
			boolean success = courses.setData(name, oldCourse.getName(), year, oldCourse.getYear(), neptuns);
			if (!success) {
				throw new Exception("Módosítás nem sikerült");
			}
			mainWindow.dataOfTableChanged();
		} catch (Exception e) {
			String message = "Hozzáadás sikertelen - " + e.getMessage();
			JOptionPane.showMessageDialog(this, message, "Hiba", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void setFields(Course course) {
		oldCourse = course;
		nameTextField.setText(course.getName());
		yearTextField.setText(Integer.toString(course.getYear()));
		neptunTextField.setText(course.getRegisteredStudentsAsString());
	}

}
