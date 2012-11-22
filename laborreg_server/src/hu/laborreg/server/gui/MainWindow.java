package hu.laborreg.server.gui;

import hu.laborreg.server.course.CourseContainer;
import hu.laborreg.server.exception.ElementNotFoundException;
import hu.laborreg.server.labEvent.LabEventContainer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainWindow {

	private static final int MINIMUM_WIDTH = 620;
	private static final int MINIMUM_HEIGHT = 200;

	private JFrame frame = new JFrame("Jelenlét regisztráló rendszer");;
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);;
	private JToolBar toolBar = new JToolBar();;
	private JFileChooser fileChooser;
	private DataManipulatorDialog courseManipulatorDialog;
	private CourseManipulatorPanel courseManipulatorPanel;
	private DataManipulatorDialog labEventManipulatorDialog;
	private LabEventManipulatorPanel labEventManipulatorPanel;
	private CourseTable courseTable;
	private LabEventTable labEventTable;
	private TableInterface activetab;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	public MainWindow(CourseContainer courses, LabEventContainer labEventContainer) {
		initialize(courses, labEventContainer);
	}

	private void initialize(CourseContainer courses, LabEventContainer labEventContainer) {
		frame.setBounds(100, 100, MINIMUM_WIDTH, MINIMUM_HEIGHT + 200);
		frame.setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		translateJFileChooser();
		fileChooser = new JFileChooser();

		courseManipulatorPanel = new CourseManipulatorPanel(courses, this);
		courseManipulatorDialog = new DataManipulatorDialog(400, 200, courseManipulatorPanel);
		labEventManipulatorPanel = new LabEventManipulatorPanel();
		labEventManipulatorDialog = new DataManipulatorDialog(300, 250, labEventManipulatorPanel);

		toolBar.setRollover(true);
		toolBar.setFloatable(false);
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		createToolbarButtons();
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);

		createTabs(courses, labEventContainer);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
	}

	private void createToolbarButtons() {
		createButton("Hozzáad", "Új hozzáadása", "plus-circle", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (activetab == courseTable) {
					courseManipulatorDialog.display("Kurzus hozzáadása");
				} else {
					labEventManipulatorDialog.display("Laboresemény hozzáadása");
				}
			}
		});

		createButton("Töröl", "Kijelölt törlése", "minus-circle", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String title = "Törlés megerősítése";
				String message = "Biztosan törölni szeretnéd?";
				int answer = JOptionPane.showConfirmDialog(frame, message, title, JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					try {
						activetab.deleteCurrent();
					} catch (ElementNotFoundException ex) {
						JOptionPane.showMessageDialog(frame, "Törlés sikertelen");
					}
				}
			}
		});

		createButton("Szerkeszt", "Kijelölt szerkesztése", "keyboard-command", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (activetab == courseTable) {
						courseManipulatorPanel.setFields(courseTable.getCurrentElement());
						courseManipulatorDialog.display("Kurzus módosítása");
					} else {
						labEventManipulatorDialog.display("Laboresemény módosítása");
					}
				} catch (ArrayIndexOutOfBoundsException ex) {
					logger.info("Failed to display details, probably wrong selection");
				}
			}
		});

		createButton("Részletek", "Részletes információ megjelenítése", "eye", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				activetab.displayDetails();
			}
		});

		createButton("Frissít", "Frissítés", "arrow-circle-double-135");

		createButton("Exportál", "Adatok exportálása CSV formátumba", "arrow-curve", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser.showDialog(frame, "Exportálás");
			}
		});
	}

	private void createButton(String buttonText, String tooltipText, String iconName, ActionListener listener) {
		String imagePath = "/icons/" + iconName + ".png";
		URL imageURL = getClass().getResource(imagePath);
		ImageIcon image = new ImageIcon(imageURL);
		JButton button = new JButton(buttonText, image);
		button.setToolTipText(tooltipText);
		button.setHorizontalTextPosition(JButton.CENTER);
		button.setVerticalTextPosition(JButton.BOTTOM);
		button.setPreferredSize(new Dimension(93, 53));
		if (listener != null) {
			button.addActionListener(listener);
		}
		toolBar.add(button);
	}

	private void createButton(String buttonText, String tooltipText, String iconName) {
		createButton(buttonText, tooltipText, iconName, null);
	}

	private void createTabs(CourseContainer courses, LabEventContainer labEventContainer) {
		courseTable = new CourseTable(courses);
		activetab = courseTable;
		labEventTable = new LabEventTable(labEventContainer);
		tabbedPane.addTab("Kurzusok", courseTable);
		tabbedPane.addTab("Laboresemények", labEventTable);
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				if (index == 0) {
					activetab = courseTable;
				} else {
					activetab = labEventTable;
				}
			}
		});
	}

	private void translateJFileChooser() {
		UIManager.put("FileChooser.acceptAllFileFilterText", "Összes fájl");
		UIManager.put("FileChooser.lookInLabelText", "Hely");
		UIManager.put("FileChooser.cancelButtonText", "Mégse");
		UIManager.put("FileChooser.cancelButtonToolTipText", "Mégse");
		UIManager.put("FileChooser.openButtonText", "Megnyitás");
		UIManager.put("FileChooser.openButtonToolTipText", "Fájl megnyitása");
		UIManager.put("FileChooser.filesOfTypeLabelText", "Típus");
		UIManager.put("FileChooser.fileNameLabelText", "Fájlnév");
		UIManager.put("FileChooser.listViewButtonToolTipText", "Lista");
		UIManager.put("FileChooser.listViewButtonAccessibleName", "Lista");
		UIManager.put("FileChooser.detailsViewButtonToolTipText", "Részletek");
		UIManager.put("FileChooser.detailsViewButtonAccessibleName", "Részletek");
		UIManager.put("FileChooser.upFolderToolTipText", "Egy szinttel feljebb");
		UIManager.put("FileChooser.upFolderAccessibleName", "Egy szinttel feljebb");
		UIManager.put("FileChooser.homeFolderToolTipText", "Saját mappa");
		UIManager.put("FileChooser.homeFolderAccessibleName", "Saját mappa");
		UIManager.put("FileChooser.fileNameHeaderText", "Név");
		UIManager.put("FileChooser.fileSizeHeaderText", "Méret");
		UIManager.put("FileChooser.fileTypeHeaderText", "Típus");
		UIManager.put("FileChooser.fileDateHeaderText", "Módosítva");
		UIManager.put("FileChooser.fileAttrHeaderText", "Attribútumok");
		UIManager.put("FileChooser.openDialogTitleText", "Megnyitás");
		UIManager.put("FileChooser.readOnly", Boolean.TRUE);
	}

	public void dataOfCoursesChanged() {
		courseTable.dataChanged();
	}

	public void display() {
		frame.setVisible(true);
	}
}
