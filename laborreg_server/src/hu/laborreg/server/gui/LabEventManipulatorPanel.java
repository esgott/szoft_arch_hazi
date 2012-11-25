package hu.laborreg.server.gui;

import hu.laborreg.server.labEvent.LabEvent;
import hu.laborreg.server.labEvent.LabEventContainer;

import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class LabEventManipulatorPanel extends ManipulatorPanel {

	private static final long serialVersionUID = 1L;
	private JTextField nameTextField;
	private JTextField courseNameTextField;
	private JTextField courseYearTextField;
	private JTextField startTextField;
	private JTextField endTextField;
	private JLabel multiplaRegLabel;
	private JTextField multipleRegTextField;
	private String oldLabEventName;
	private final LabEventContainer labEvents;
	private final MainWindow mainWindow;
	private final DateFormat formatter = new SimpleDateFormat(LabEventContainer.DATE_FORMAT);

	public LabEventManipulatorPanel(LabEventContainer labEventContainer, MainWindow parent) {
		setLayout(new GridLayout(6, 2, 10, 10));

		JLabel nameLabel = new JLabel("Név");
		add(nameLabel);

		nameTextField = new JTextField();
		add(nameTextField);
		nameTextField.setColumns(10);
		nameTextField.setToolTipText("A laboresemény neve");;

		JLabel courseNameLabel = new JLabel("Kurzus név:");
		add(courseNameLabel);

		courseNameTextField = new JTextField();
		add(courseNameTextField);
		courseNameTextField.setColumns(10);
		courseNameTextField.setToolTipText("A kurzus neve");

		JLabel courseYearLabel = new JLabel("Kurzus év:");
		add(courseYearLabel);

		courseYearTextField = new JTextField();
		add(courseYearTextField);
		courseYearTextField.setColumns(10);
		courseYearTextField.setToolTipText("A kurzus éve");

		JLabel startLabel = new JLabel("Kezdés:");
		add(startLabel);

		startTextField = new JTextField();
		add(startTextField);
		startTextField.setColumns(10);
		startTextField.setToolTipText("Érvényes dátumformátum: 2012-01-15 12:00:00");

		JLabel endlabel = new JLabel("Befejezés:");
		add(endlabel);

		endTextField = new JTextField();
		add(endTextField);
		endTextField.setColumns(10);
		endTextField.setToolTipText("Érvényes dátumformátum: 2012-01-15 12:00:00");

		multiplaRegLabel = new JLabel("Többszörös jelentkezés");
		add(multiplaRegLabel);

		multipleRegTextField = new JTextField();
		add(multipleRegTextField);
		multipleRegTextField.setColumns(10);
		multipleRegTextField.setToolTipText("IP-címek vesszővel elválasztott listája");

		labEvents = labEventContainer;
		mainWindow = parent;
		oldLabEventName = " ";
	}

	@Override
	public void clear() {
		nameTextField.setText("");
		courseNameTextField.setText("");
		courseYearTextField.setText("");
		startTextField.setText("");
		endTextField.setText("");
		multipleRegTextField.setText("");
		oldLabEventName = " ";
	}

	public void setFields(LabEvent labEvent) {
		oldLabEventName = labEvent.getName();
		nameTextField.setText(labEvent.getName());
		courseNameTextField.setText(labEvent.getCourseName());
		String year = Integer.toString(labEvent.getCourseYear());
		courseYearTextField.setText(year);
		Date startTime = labEvent.getStartTime();
		startTextField.setText(formatter.format(startTime));
		Date endTime = labEvent.getStopTime();
		endTextField.setText(formatter.format(endTime));
		multipleRegTextField.setText(labEvent.getRegisteredComputersAsString());
	}

	@Override
	public boolean commit() {
		try {
			String name = nameTextField.getText();
			String courseName = courseNameTextField.getText();
			int courseYear = Integer.parseInt(courseYearTextField.getText());
			Date startTime = formatter.parse(startTextField.getText());
			Date endTime = formatter.parse(endTextField.getText());
			String[] ipAddresses = null;
			if (multipleRegTextField.getText().equals("")) {
				ipAddresses = new String[0];
			} else {
				ipAddresses = multipleRegTextField.getText().split(",");
			}
			for (int i = 0; i < ipAddresses.length; i++) {
				ipAddresses[i] = ipAddresses[i].trim();
			}
			boolean success = labEvents.setLabEvent(name, oldLabEventName, courseName, courseYear, ipAddresses,
					startTime, endTime);
			if (!success) {
				throw new Exception("Módosítás vagy hozzáadás nem sikerült");
			}
			mainWindow.dataOfTableChanged();
			return success;
		} catch (Exception e) {
			String message = "Módosítás vagy hozzáadás sikertelen - " + e.getMessage();
			JOptionPane.showMessageDialog(this, message, "Hiba", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

}
