package VOXSPELL;

/**
 * This is a mediator class - it enables GUI JPanels to interact with each other without knowing their
 * existence. It receives messages by listening to events from its registered GUIs. It then sends 
 * messages to the GUI JFrame and updates it.
 * 
 * @author jacky
 */

import java.util.ArrayList;
import java.util.List;

public class GUIMediator {
	private List<GUI> _GUIList = new ArrayList<GUI>(); //storing all associated GUIs
	private GUICardLayout _mainGUI; // the GUI frame we want to update

	public void addMainGUI(GUICardLayout main){
		_mainGUI = main;
	}

	public void addGUI(GUI userGUI){
		_GUIList.add(userGUI);
	}

	public void sendUpdateToGUI(String e){
		_mainGUI.changeLayout(e);
	}

	public void setModelOfGUI(String e, Command cmd, String level){
		// associating a GUI with its model
		for(GUI gui:_GUIList){
			if(gui instanceof spellingGUI && e.equals("GUI")){
				gui.setModel(cmd);
				cmd.addGUI(gui);
				newGame test = (newGame) cmd;
				test.setLevel("%"+level);
			} else if(gui instanceof viewStatsGUI && e.equals("VIEW")){
				gui.setModel(cmd);
				cmd.addGUI(gui);
			}
		}
	}
	
	public void updateSideStats(String level, boolean success){
		for(GUI gui: _GUIList){
			if(gui instanceof CurrentStatsGUI){
				CurrentStatsGUI sidePanel = (CurrentStatsGUI) gui;
				sidePanel.updateStats(level, success);
			}
		}
	}
}
