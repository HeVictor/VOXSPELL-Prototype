package VOXSPELL;

import javax.swing.JButton;
import javax.swing.SwingWorker;

public class VoiceWorker extends SwingWorker<Void,Void> {
	String _command;
	JButton _btn;
	JButton _btn2;
	newGame _ng;
	spellingGUI _GUI;
	String _outputMsg;
	boolean _btnEnable;

	// Now this also takes a String for the purpose of outputting next word on txtField after Festival finishes - Victor
	public VoiceWorker(String command, JButton btn,  JButton btn2, newGame ng, spellingGUI GUI, String outputMsg, boolean btnEnable) { // Modified the constructor to take newGame as parameter - Victor
		_command = command;
		_btn = btn;
		_btn2 = btn2;
		_ng = ng;
		_GUI = GUI;
		_outputMsg = outputMsg;
		_btnEnable = btnEnable;
	}

	@Override
	protected Void doInBackground() throws Exception {
		ProcessBuilder pb = new ProcessBuilder("bash", "-c",  _command);
		try {
			Process process = pb.start();
			process.waitFor(); // waiting for the process to finish
		} catch (Exception e) {
		}
		return null;
	}
	
	protected void done(){
		
		// This enables the buttons manually as dictated by specific Festival calls. - Victor
		_btn.setEnabled(_btnEnable);
		_btn2.setEnabled(_btnEnable);
		
		_GUI.appendTxtField(_outputMsg); // Added this to sync text appending after Festival completes - Victor
		
	}

}
