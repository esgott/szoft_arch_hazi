package hu.laborreg.server.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class DataManipulatorDialog extends JDialog {

	private static final long serialVersionUID = 5541396405124311701L;

	private JPanel contentPanel = new JPanel();
	private JPanel buttonPane = new JPanel();;
	private ManipulatorPanel manipulatorPanel;

	public DataManipulatorDialog(int width, int height, ManipulatorPanel manipulatorPanel) {
		setBounds(100, 100, width, height);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		this.manipulatorPanel = manipulatorPanel;
		contentPanel.add(manipulatorPanel);

		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		addOkButton();
		addCancelButton();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				clear();
			}
		});
	}

	private void addOkButton() {
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearAndHide();
			}
		});
	}

	private void addCancelButton() {
		JButton cancelButton = new JButton("Mégse");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearAndHide();
			}
		});
	}

	public void display(String title) {
		setTitle(title);
		setVisible(true);
	}

	public void clearAndHide() {
		setVisible(false);
		clear();
	}

	private void clear() {
		manipulatorPanel.clear();
	}

}
