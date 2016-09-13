package VOXSPELL;

import javax.swing.JButton;
import javax.swing.SwingWorker;

public class VoiceWorker extends SwingWorker {
	String _command;
	JButton _btn;

	public VoiceWorker(String command, JButton btn) {
		_command = command;
		_btn = btn;
	}

	@Override
	protected Object doInBackground() throws Exception {
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", _command);
		try {
			Process process = pb.start();
			process.waitFor(); // waiting for the process to finish
		} catch (Exception e) {
		}
		return null;
	}
	
	protected void done(){
		_btn.setEnabled(true);
	}

}
