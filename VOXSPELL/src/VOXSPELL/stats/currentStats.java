package VOXSPELL.stats;

import VOXSPELL.Command;
import VOXSPELL.GUI;

public class currentStats implements Command{
	/**
	 * This class keeps track of the state of the side panel. 
	 * 
	 * @author: jacky
	 * 
	 */
	private int[] levelSuccess;
	private int[] levelAttempt;
	private CurrentStatsGUI _GUI;
	private int _levels;
	
	@Override
	public void execute() {		
	}

	@Override
	public void addGUI(GUI GUI) {
		// associates the model with a GUI
		_GUI = (CurrentStatsGUI) GUI;
		_levels = CurrentStatsGUI.LEVELS;
		levelSuccess = new int[_levels]; // instantiate an array of the same size as the number of levels
		levelAttempt = new int[_levels];
	}

	public void setStats(String level, boolean success){
		if(success){
			levelSuccess[Integer.parseInt(level.split(" ")[1])-1]++; // if a word is mastered, we increment that level's success rate
		}
		levelAttempt[Integer.parseInt(level.split(" ")[1])-1]++; // this is to keep track total number of attempts
		_GUI.setLabel(levelSuccess, levelAttempt); // notifies the GUI to update its view
	}
	
	public void resetStats(){
		levelSuccess = new int[_levels];
		levelAttempt = new int[_levels];
	}
	
}
