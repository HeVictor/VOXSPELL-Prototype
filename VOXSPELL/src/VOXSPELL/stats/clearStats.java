package VOXSPELL.stats;

import VOXSPELL.Command;
import VOXSPELL.GUI;
import VOXSPELL.fileHandler;

/**
 * This class merely handles the clearing of stats when executed from the clear jbutton.
 * 
 * @author Jacky Lo
 *
 */

public class clearStats implements Command{

	@Override
	public void execute() {
		/*
		 * this method clearly calls the fileHandler to clear all the stats associated in files
		 */
		new fileHandler().clearStats();
	}

	@Override
	public void addGUI(GUI GUI) {	
	}

}
