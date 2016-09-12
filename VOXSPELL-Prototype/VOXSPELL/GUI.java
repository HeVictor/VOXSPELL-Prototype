package VOXSPELL;

/**
 * This abstract GUI class represents all GUI objects. It defines the mediator constructor
 * which requires the GUI to associate itself with the mediator so that the overall GUI can update
 * itself. It provides an abstract method of creatingGUI() and setModel to enforce that a GUI is
 * associated with this class, and a model is associated with each GUI.
 * 
 * @author jacky
 *
 */

import javax.swing.JPanel;

public abstract class GUI {
	protected GUIMediator mediator;
	public GUI(GUIMediator m){
		mediator = m;
	}
	public abstract JPanel creatingGUI();
	public abstract void setModel(Command cmd);
}
