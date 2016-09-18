package VOXSPELL;

public class currentStats implements Command{
	private int[] levelSuccess;
	private int[] levelAttempt;
	private CurrentStatsGUI _GUI;
	private int _levels;
	
	@Override
	public void execute() {		
	}

	@Override
	public void addGUI(GUI GUI) {
		_GUI = (CurrentStatsGUI) GUI;
		_levels = CurrentStatsGUI.LEVELS;
		levelSuccess = new int[_levels];
		levelAttempt = new int[_levels];
	}

	public void setStats(String level, boolean success){
		if(success){
			levelSuccess[Integer.parseInt(level.split(" ")[1])-1]++;
		}
		levelAttempt[Integer.parseInt(level.split(" ")[1])-1]++;
		_GUI.setLabel(levelSuccess, levelAttempt);
	}
	
	public void resetStats(){
		levelSuccess = new int[_levels];
		levelAttempt = new int[_levels];
	}
	
}
