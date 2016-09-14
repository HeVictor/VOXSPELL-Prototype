package VOXSPELL;

import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.SwingWorker;

public class VoiceWorker extends SwingWorker<Void,Void> {
	String _command;
	JButton _btn;
	newGame _ng;
	spellingGUI _GUI;
	String _outputMsg;

	// Now this also takes a String for the purpose of outputting next word on txtField after Festival finishes - Victor
	public VoiceWorker(String command, JButton btn,  newGame ng, spellingGUI GUI, String outputMsg) { // Modified the constructor to take newGame as parameter - Victor
		_command = command;
		_btn = btn;
		_ng = ng;
		_GUI = GUI;
		_outputMsg = outputMsg;
	}

	@Override
	protected Void doInBackground() throws Exception {
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", _command);
		try {
			
			CountDownLatch waitSignal = _ng.getLatch(); // Gets the local Latch from newGame - Victor
			
			waitSignal.await(); // Waits for last Festival call to properly finish if there was one - Victor
			_ng.activateLatch(); // Activate the local latch to one count in newGame - Victor
			Process process = pb.start();
			process.waitFor(); // waiting for the process to finish
		} catch (Exception e) {
		}
		return null;
	}
	
	protected void done(){
		_ng.countDown(); // Counts down the latch, freeing it for the next Festival call - Victor
		_btn.setEnabled(true);
		_GUI.appendTxtField(_outputMsg); // Added this to sync text appending after Festival completes - Victor
		
		
	}

}
