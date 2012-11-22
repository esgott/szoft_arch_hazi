package hu.laborreg.server.gui;

import hu.laborreg.server.labEvent.LabEvent;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class LabEventManipulatorPanel extends ManipulatorPanel {

	private static final long serialVersionUID = 1L;
	private JTextField nameTextField;
	private JTextField courseNameTextField;
	private JTextField courseYearTextField;
	private JTextField startTextField;
	private JTextField endTextField;
	private LabEvent oldLabEvent;

	public LabEventManipulatorPanel() {
		setLayout(new GridLayout(5, 2, 10, 10));

		JLabel nameLabel = new JLabel("Név");
		add(nameLabel);

		nameTextField = new JTextField();
		add(nameTextField);
		nameTextField.setColumns(10);

		JLabel courseNameLabel = new JLabel("Kurzus név:");
		add(courseNameLabel);

		courseNameTextField = new JTextField();
		add(courseNameTextField);
		courseNameTextField.setColumns(10);

		JLabel courseYearLabel = new JLabel("Kurzus év:");
		add(courseYearLabel);

		courseYearTextField = new JTextField();
		add(courseYearTextField);
		courseYearTextField.setColumns(10);

		JLabel startLabel = new JLabel("Kezdés:");
		add(startLabel);

		startTextField = new JTextField();
		add(startTextField);
		startTextField.setColumns(10);

		JLabel endlabel = new JLabel("Befejezés:");
		add(endlabel);

		endTextField = new JTextField();
		add(endTextField);
		endTextField.setColumns(10);

	}

	@Override
	public void clear() {
		nameTextField.setText("");
		courseNameTextField.setText("");
		courseYearTextField.setText("");
		startTextField.setText("");
		endTextField.setText("");
	}
	
	public void setFields(LabEvent labEvent) {
		oldLabEvent = labEvent;
		//TODO set fields
	}

	@Override
	public void commit() {
		// TODO Auto-generated method stub

	}

}
