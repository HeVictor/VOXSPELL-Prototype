package VOXSPELL;

import java.util.ArrayList;

public class currentStats implements Command{
	private int[] levelSuccess = new int[11];
	private int[] levelAttempt = new int[11];
	private CurrentStatsGUI _GUI;
	
	@Override
	public void execute() {		
	}

	@Override
	public void addGUI(GUI GUI) {
		_GUI = (CurrentStatsGUI) GUI;
	}

	public void setStats(String level, boolean success){
		if(success){
			levelSuccess[Integer.parseInt(level.split(" ")[1])-1]++;
		}
		levelAttempt[Integer.parseInt(level.split(" ")[1])-1]++;
		_GUI.setLabel(levelSuccess, levelAttempt);
	}
	
}
