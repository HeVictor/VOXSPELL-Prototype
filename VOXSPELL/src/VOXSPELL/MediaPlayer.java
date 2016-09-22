package VOXSPELL;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import VOXSPELL.swingworker.VideoWorker;


import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * This class is used to display a media player to play a video existing in the current project directory. It
 * contains the play, pause, rewind, fast-forward, stop, mute and unmute functions.
 * 
 * The media icons used in this media player are credited to Kellen Lester from the Noun Project. Specifically,
 * the play icon, pause icon, fast rewind icon, fast forward icon, stop icon, muted icon and unmuted icon.
 * 
 * @author victor
 *
 */

public class MediaPlayer extends JFrame implements ActionListener, WindowListener, MediaPlayerEventListener{

	private static final String PLAY_IMAGE = ".media/PLAY.png";
	private static final String PAUSE_IMAGE = ".media/PAUSE.png";
	private static final String RWD_IMAGE = ".media/RWD.png";
	private static final String FFW_IMAGE = ".media/FFW.png";
	private static final String STOP_IMAGE = ".media/STOP.png";
	private static final String MUTED_IMAGE = ".media/MUTED.png";
	private static final String UNMUTED_IMAGE = ".media/UNMUTED.png";

	private EmbeddedMediaPlayerComponent _player = new EmbeddedMediaPlayerComponent();
	private String _videoName;

	private JButton btnPlayPause = new JButton();
	private JButton btnMute = new JButton();
	private JButton btnStop = new JButton();
	private JButton btnFFW = new JButton();
	private JButton btnRWD = new JButton();

	private JButton[] _buttons = {btnPlayPause,btnRWD,btnStop,btnFFW,btnMute};
	private JPanel _buttonPane = new JPanel();

	private String[] _iconImageNames = {PAUSE_IMAGE,RWD_IMAGE,STOP_IMAGE,FFW_IMAGE,MUTED_IMAGE,PLAY_IMAGE,UNMUTED_IMAGE};
	private ImageIcon[] _icons = new ImageIcon[7];
	private ImageIcon _pauseIcon;
	private ImageIcon _rwdIcon;
	private ImageIcon _stopIcon;
	private ImageIcon _ffwIcon;
	private ImageIcon _mutedIcon;
	private ImageIcon _playIcon;
	private ImageIcon _unmutedIcon;

	private EmbeddedMediaPlayer rewardVideo = _player.getMediaPlayer();

	private boolean _playSpookyVideo;

	// Code below sampled from FrameDemo.java from Java Tutorials at oracle.com
	public MediaPlayer(String videoName, boolean playSpookyVideo) {
		super();
		_videoName = videoName;
		_playSpookyVideo = playSpookyVideo;
	}

	// Code below semi-sampled from SOFTENG206's VLCJ ACP tutorial. This sets up the main media player GUI.
	public void setupGUI() {
		if (_playSpookyVideo == true) {
			this.setTitle("Spooky Video!");
		} else {
			this.setTitle("Enjoy your Reward!");
		}

		// Sets up the video inside the JFrame.
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(_player, BorderLayout.CENTER);
		this.setContentPane(panel);

		// Adds the buttons in a row to the bottom of the JFrame.
		_buttonPane.setLayout(new GridLayout());
		for (JButton button: _buttons) {
			_buttonPane.add(button);
			button.addActionListener(this);
		}
		this.add(_buttonPane,BorderLayout.SOUTH);

		// Converts the images to icons and stores them in the array.
		int buttonNumber = 0; 
		for (String img: _iconImageNames) {
			ImageIcon smallIcon = convertToIcon(img);
			_icons[buttonNumber] = smallIcon;
			buttonNumber++;
		}

		// Assigns the local fields the icons, and sets up the initial button icons.
		_pauseIcon = _icons[0];
		_rwdIcon = _icons[1];
		_stopIcon = _icons[2];
		_ffwIcon = _icons[3];
		_mutedIcon = _icons[4];
		_playIcon = _icons[5];
		_unmutedIcon = _icons[6];

		btnPlayPause.setIcon(_pauseIcon);
		btnRWD.setIcon(_rwdIcon);
		btnStop.setIcon(_stopIcon);
		btnFFW.setIcon(_ffwIcon);
		btnMute.setIcon(_mutedIcon);

		// Sets size and location of GUI, and makes it visible.
		this.setSize(1000, 563);
		this.setLocationRelativeTo(null); // Centers the JFrame, retrieved from Erick Robertson and JRL from StackOverflow.
		this.setVisible(true);

		// This allows this class to respond to window closing calls.
		this.addWindowListener(this);

		// This allows this class to respond to media player events.
		rewardVideo.addMediaPlayerEventListener(this);

		// Plays the video.


		rewardVideo.playMedia(_videoName);
	}

	// This method converts an BufferedImage to the right-sized icon, specifically media icon images download from Noun Project
	// by Kellen Lester.
	private ImageIcon convertToIcon(String img) {
		BufferedImage iconImg;
		try {
			iconImg = ImageIO.read(new File(img));
			BufferedImage smallerImg = iconImg.getSubimage(195, 164, 315, 275);
			ImageIcon smallIcon = new ImageIcon(smallerImg.getScaledInstance(31,27,Image.SCALE_SMOOTH));

			return smallIcon;
		} catch (IOException e) {
		}
		return null;
	}

	// Respond to button clicks appropriately.
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnPlayPause) {	
			if (rewardVideo.isPlaying()) {
				btnPlayPause.setIcon(_playIcon);
				rewardVideo.pause();
			} else {
				btnPlayPause.setIcon(_pauseIcon);
				rewardVideo.play();
			}
		} else if (e.getSource() == btnFFW) {
			rewardVideo.skip(5000);
		} else if (e.getSource() == btnRWD) {
			rewardVideo.skip(-5000);
		} else if (e.getSource() == btnStop) {
			rewardVideo.setTime(0);
			rewardVideo.pause();
			btnPlayPause.setIcon(_playIcon);
		} else if (e.getSource() == btnMute) {

			if (rewardVideo.isMute()) {
				btnMute.setIcon(_mutedIcon);
			} else {
				btnMute.setIcon(_unmutedIcon);
			}
			rewardVideo.mute();
		}



	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	// This makes sure that only the video terminates, and not the entire spelling application.
	@Override
	public void windowClosing(WindowEvent arg0) {
		rewardVideo.stop();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {	
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}

	@Override
	public void backward(uk.co.caprica.vlcj.player.MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void buffering(uk.co.caprica.vlcj.player.MediaPlayer arg0, float arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void elementaryStreamAdded(uk.co.caprica.vlcj.player.MediaPlayer arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void elementaryStreamDeleted(uk.co.caprica.vlcj.player.MediaPlayer arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void elementaryStreamSelected(uk.co.caprica.vlcj.player.MediaPlayer arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endOfSubItems(uk.co.caprica.vlcj.player.MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void error(uk.co.caprica.vlcj.player.MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

	// This detects that the video has finished playing and will restart and pause the video (like pressing Stop).
	@Override
	public void finished(uk.co.caprica.vlcj.player.MediaPlayer arg0) {
		rewardVideo.stop();
		VideoWorker vw = new VideoWorker(rewardVideo);
		vw.execute();
		btnStop.doClick();
	}

	@Override
	public void forward(uk.co.caprica.vlcj.player.MediaPlayer arg0) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void lengthChanged(uk.co.caprica.vlcj.player.MediaPlayer arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaChanged(uk.co.caprica.vlcj.player.MediaPlayer arg0, libvlc_media_t arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaDurationChanged(uk.co.caprica.vlcj.player.MediaPlayer arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaFreed(uk.co.caprica.vlcj.player.MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaMetaChanged(uk.co.caprica.vlcj.player.MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaParsedChanged(uk.co.caprica.vlcj.player.MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaStateChanged(uk.co.caprica.vlcj.player.MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaSubItemAdded(uk.co.caprica.vlcj.player.MediaPlayer arg0, libvlc_media_t arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaSubItemTreeAdded(uk.co.caprica.vlcj.player.MediaPlayer arg0, libvlc_media_t arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newMedia(uk.co.caprica.vlcj.player.MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void opening(uk.co.caprica.vlcj.player.MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pausableChanged(uk.co.caprica.vlcj.player.MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paused(uk.co.caprica.vlcj.player.MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playing(uk.co.caprica.vlcj.player.MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void positionChanged(uk.co.caprica.vlcj.player.MediaPlayer arg0, float arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void scrambledChanged(uk.co.caprica.vlcj.player.MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void seekableChanged(uk.co.caprica.vlcj.player.MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void snapshotTaken(uk.co.caprica.vlcj.player.MediaPlayer arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopped(uk.co.caprica.vlcj.player.MediaPlayer arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void subItemFinished(uk.co.caprica.vlcj.player.MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void subItemPlayed(uk.co.caprica.vlcj.player.MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void timeChanged(uk.co.caprica.vlcj.player.MediaPlayer arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void titleChanged(uk.co.caprica.vlcj.player.MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void videoOutput(uk.co.caprica.vlcj.player.MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub
	}


}
