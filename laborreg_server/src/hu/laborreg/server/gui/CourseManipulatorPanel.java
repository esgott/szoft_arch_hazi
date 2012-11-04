package hu.laborreg.server.gui;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class CourseManipulatorPanel extends ManipulatorPanel {

	private static final long serialVersionUID = -327589817126666321L;

	private JTextField yearTextField;
	private JTextField nameTextField;

	public CourseManipulatorPanel() {
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

}
