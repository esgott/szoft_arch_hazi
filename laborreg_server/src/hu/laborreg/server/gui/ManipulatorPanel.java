package hu.laborreg.server.gui;

import javax.swing.JPanel;

public abstract class ManipulatorPanel extends JPanel {

	private static final long serialVersionUID = -3518867518110992128L;
	
	public abstract void clear();
	
	public abstract boolean commit();

}
