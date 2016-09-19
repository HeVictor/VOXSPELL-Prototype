package VOXSPELL;

import javax.swing.SwingWorker;


/**
 * This class is a SwingWorker that creates a filtered video through FFMPEG filters, as a secret
 * additional reward for the user to discover.
 * @author victor
 *
 */
public class FFMPEGWorker extends SwingWorker<Void,Void>{
	
	String _videoName;
	
	public FFMPEGWorker(String videoName) {
		_videoName = videoName;
	}

	@Override
	protected Void doInBackground() throws Exception {
		
		// Inverting colours filtering retrieved from http://randombio.com/linuxsetup141.html
		// Adjusting video and audio speed retrieved from https://trac.ffmpeg.org/wiki/How%20to%20speed%20up%20/%20slow%20down%20a%20video
		// Specifically, the video is manipulated to be halved the speed and the colours inverted, to create a "spooky" effect.
		String command = "ffmpeg -y -i big_buck_bunny_1_minute.avi -filter_complex \"[0:v]setpts=2*PTS,lutrgb=r=negval:g=negval:b=negval[v];[0:a]atempo=0.5[a]\" -map \"[v]\" -map \"[a]\" .SPOOKY.avi";
		
		ProcessBuilder builder = new ProcessBuilder("bash","-c",command);
		
		Process process = builder.start();
		process.waitFor();
		
		return null;
	}
	
	

}
