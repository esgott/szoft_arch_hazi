package hu.laborreg.server.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainWindow {

	private static final int MINIMUM_WIDTH = 600;
	private static final int MINIMUM_HEIGHT = 200;

	private JFrame frame = new JFrame("Jelenlét regisztráló rendszer");;
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);;
	private JToolBar toolBar = new JToolBar();;
	private JFileChooser fileChooser;
	private DataManipulatorDialog dataManipulatorDialog;

	public static void display() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public MainWindow() {
		initialize();
	}

	private void initialize() {
		frame.setBounds(100, 100, MINIMUM_WIDTH, MINIMUM_HEIGHT + 200);
		frame.setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		translateJFileChooser();
		fileChooser = new JFileChooser();

		dataManipulatorDialog = new DataManipulatorDialog(300, 100, new CourseManipulatorPanel());

		toolBar.setRollover(true);
		toolBar.setFloatable(false);
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		createToolbarButtons();
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);

		createTabs();
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
	}

	private void createToolbarButtons() {
		createButton("Hozzáad", "Új hozzáadása", "plus-circle", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dataManipulatorDialog.display("Kurzus hozzáadása");
			}
		});

		createButton("Töröl", "Kijelölt törlése", "minus-circle");

		createButton("Szerkeszt", "Kijelölt szerkesztése", "keyboard-command");

		createButton("Részletek", "Részletes információ megjelenítése", "eye", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = "Kurzus neve: kurzus1\nÉv: 1999\nLaboresemények: labor1, labor2\nRegisztrált hallgatók: ABC123, DEF456";
				JOptionPane.showMessageDialog(frame, message, "Részletek", JOptionPane.PLAIN_MESSAGE);
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

	private void createTabs() {
		tabbedPane.addTab("Kurzusok", new CourseTable());
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
		UIManager.put("FileChooser.openDialogTitleText","Megnyitás");
		UIManager.put("FileChooser.readOnly", Boolean.TRUE);
	}
}
