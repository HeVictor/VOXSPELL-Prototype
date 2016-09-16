package VOXSPELL;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class CurrentStatsGUI extends GUI {
	JPanel separatorPanel;
	currentStats model;
	JLabel currentStats = new JLabel("Current Statistics:");
	/*JLabel currentStatsLvl1 = new JLabel("Level 1: ");
	JLabel currentStatsLvl2 = new JLabel("Level 2: ");
	JLabel currentStatsLvl3 = new JLabel("Level 3: ");
	JLabel currentStatsLvl4 = new JLabel("Level 4: ");
	JLabel currentStatsLvl5 = new JLabel("Level 5: ");
	JLabel currentStatsLvl6 = new JLabel("Level 6: ");
	JLabel currentStatsLvl7 = new JLabel("Level 7: ");
	JLabel currentStatsLvl8 = new JLabel("Level 8: ");
	JLabel currentStatsLvl9 = new JLabel("Level 9: ");
	JLabel currentStatsLvl10 = new JLabel("Level 10: ");
	JLabel currentStatsLvl11 = new JLabel("Level 11: ");*/

	public CurrentStatsGUI(GUIMediator m) {
		super(m);
	}

	public JPanel creatingGUI(){

		/*JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
		labelPanel.setPreferredSize(new Dimension(250, 200));
		labelPanel.add(Box.createRigidArea(new Dimension(0, 30)));
		labelPanel.add(currentStats);
		labelPanel.add(currentStatsLvl1);
		labelPanel.add(currentStatsLvl2);
		labelPanel.add(currentStatsLvl3);
		labelPanel.add(currentStatsLvl4);
		labelPanel.add(currentStatsLvl5);
		labelPanel.add(currentStatsLvl6);
		labelPanel.add(currentStatsLvl7);
		labelPanel.add(currentStatsLvl8);
		labelPanel.add(currentStatsLvl9);
		labelPanel.add(currentStatsLvl10);
		labelPanel.add(currentStatsLvl11);*/

		separatorPanel = new JPanel(new BorderLayout(20,0));
		separatorPanel.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.WEST);
		currentStats = new JLabel("<html>Current Statistics<BR>");
		currentStats.setPreferredSize(new Dimension(250, 200));
		separatorPanel.add(currentStats);
		for(int i = 1; i<12; i++){
			currentStats.setText(currentStats.getText()+"Level "+i+": <BR>");
		}
		currentStats.setText(currentStats.getText()+"</html>");
		//separatorPanel.add(labelPanel, BorderLayout.CENTER);
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
		for(int i = 1; i<12; i++){
			if(attempts[i-1] != 0){
				currentStats.setText(currentStats.getText()+"Level "+i+": "+(float)success[i-1]/attempts[i-1]*100+"%<BR>");
			} else {
				currentStats.setText(currentStats.getText()+"Level "+i+": <BR>");
			}
		}
		currentStats.setText(currentStats.getText()+"</html>");
	}

}
