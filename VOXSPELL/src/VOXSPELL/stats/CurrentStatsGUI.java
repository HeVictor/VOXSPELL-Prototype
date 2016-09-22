package VOXSPELL.stats;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import VOXSPELL.Command;
import VOXSPELL.GUI;
import VOXSPELL.GUIMediator;

/**
 * This class is the view controller of the stats side panel. It only applies to current sessions, and does not keep track
 * of prior sessions.
 * 
 * @author jacky lo
 *
 */

public class CurrentStatsGUI extends GUI {
	JPanel separatorPanel;
	currentStats model;
	static final int LEVELS = 11;
	JLabel currentStats = new JLabel("Current Statistics:");

	public CurrentStatsGUI(GUIMediator m) {
		// associates this GUI with a mediator
		super(m);
	}

	public JPanel creatingGUI(){

		separatorPanel = new JPanel(new BorderLayout(20,0));
		separatorPanel.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.WEST);
		currentStats = new JLabel("<html>Current Statistics<BR>");
		currentStats.setPreferredSize(new Dimension(250, 200));
		separatorPanel.add(currentStats);
		for(int i = 1; i<=LEVELS; i++){
			currentStats.setText(currentStats.getText()+"Level "+i+": <BR>");
		}
		currentStats.setText(currentStats.getText()+"</html>");
		return separatorPanel;
	}

	@Override
	public void setModel(Command cmd) {
		model = (currentStats) cmd;
	}

	public void updateStats(String level, boolean success){
		model.setStats(level, success);
	}
	
	public void setLabel(int success[], int attempts[]){
		currentStats.setText("<html>Current Statistics<BR>");
		for(int i = 1; i<=LEVELS; i++){
			if(attempts[i-1] != 0){
				currentStats.setText(currentStats.getText()+"Level "+i+": "+(float)success[i-1]/attempts[i-1]*100+"%<BR>");
			} else {
				currentStats.setText(currentStats.getText()+"Level "+i+": <BR>");
			}
		}
		currentStats.setText(currentStats.getText()+"</html>");
	}
	
	public void resetLabel(){
		model.resetStats();
		currentStats.setText("<html>Current Statistics<BR>");
		for(int i = 1; i<=LEVELS; i++){
			currentStats.setText(currentStats.getText()+"Level "+i+": <BR>");
		}
		currentStats.setText(currentStats.getText()+"</html>");
	}

}
