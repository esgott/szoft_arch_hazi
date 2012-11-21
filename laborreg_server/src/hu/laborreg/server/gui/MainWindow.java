package hu.laborreg.server.gui;

import hu.laborreg.server.course.CourseContainer;
import hu.laborreg.server.exception.ElementNotFoundException;

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
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;

public class MainWindow {

	private static final int MINIMUM_WIDTH = 620;
	private static final int MINIMUM_HEIGHT = 200;

	private JFrame frame = new JFrame("Jelenlét regisztráló rendszer");;
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);;
	private JToolBar toolBar = new JToolBar();;
	private JFileChooser fileChooser;
	private DataManipulatorDialog dataManipulatorDialog;
	private CourseManipulatorPanel courseManipulatorPanel;
	private CourseTable courseTable;
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	public MainWindow(CourseContainer courses) {
		initialize(courses);
	}

	private void initialize(CourseContainer courses) {
		frame.setBounds(100, 100, MINIMUM_WIDTH, MINIMUM_HEIGHT + 200);
		frame.setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		translateJFileChooser();
		fileChooser = new JFileChooser();

		courseManipulatorPanel = new CourseManipulatorPanel(courses, this);
		dataManipulatorDialog = new DataManipulatorDialog(400, 200, courseManipulatorPanel);

		toolBar.setRollover(true);
		toolBar.setFloatable(false);
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		createToolbarButtons();
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);

		createTabs(courses);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
	}

	private void createToolbarButtons() {
		createButton("Hozzáad", "Új hozzáadása", "plus-circle", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dataManipulatorDialog.display("Kurzus hozzáadása");
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
						courseTable.deleteCurrent();
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
					courseManipulatorPanel.setFields(courseTable.getCurrentElement());
					dataManipulatorDialog.display("Kurzus módosítása");
				} catch (ArrayIndexOutOfBoundsException ex) {
					logger.info("Failed to display details, probably wrong selection");
				}
			}
		});

		createButton("Részletek", "Részletes információ megjelenítése", "eye", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				courseTable.displayDetails();
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

	private void createTabs(CourseContainer courses) {
		courseTable = new CourseTable(courses);
		tabbedPane.addTab("Kurzusok", courseTable);
		tabbedPane.addTab("Laboresemények", new JPanel());
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
