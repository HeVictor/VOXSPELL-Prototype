package VOXSPELL;

import java.awt.BorderLayout;
import java.awt.Component;

/**
 * This class is the mainMenuGUI JPanel. It has buttons that enables the user to start a new quiz,
 * view stats, clear, review or quit the app.
 * 
 * @author jacky
 */

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import VOXSPELL.spelling.newGame;
import VOXSPELL.stats.clearStats;
import VOXSPELL.stats.newStats;

public class mainMenuGUI extends GUI implements ActionListener{
	private JButton btnNewGame = new JButton("New Game"); 
	private JButton btnReview = new JButton("Review Mistakes"); 
	private JButton btnStats = new JButton("View Stats");
	private JButton btnClear = new JButton("Clear Stats");
	private JButton btnQuit = new JButton("Quit Game"); 
	private JLabel labelSelect = new JLabel("Please select a spelling level: ");
	private JComboBox<String> levelList;

	public mainMenuGUI(GUIMediator mediator){
		super(mediator);
	}

	public JPanel creatingGUI(){
		
		// creating the GUI Jpanel
		JPanel panel = new JPanel(new BorderLayout());
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
		
		String[] levels = new String[11];
		for(int i = 1; i < 12; i++){
			levels[i-1] = "Level "+i;
		}
		levelList = new JComboBox<String>(levels);
		
		levelList.addActionListener(this);
		btnNewGame.addActionListener(this); 
		btnReview.addActionListener(this); 
		btnStats.addActionListener(this); 
		btnClear.addActionListener(this);
		btnQuit.addActionListener(this); 

		buttonPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		buttonPane.add(Box.createRigidArea(new Dimension(0,20)));
		
		buttonPane.add(labelSelect);
		
		levelList.setMaximumSize(new Dimension(200, btnNewGame.getMinimumSize().height));
		buttonPane.add(levelList);
		levelList.setAlignmentX(Component.LEFT_ALIGNMENT);
		buttonPane.add(Box.createRigidArea(new Dimension(0, 10)));
		
		btnNewGame.setMaximumSize(new Dimension(200, btnNewGame.getMinimumSize().height));
		buttonPane.add(btnNewGame);
		buttonPane.add(Box.createRigidArea(new Dimension(0, 10)));

		btnReview.setMaximumSize(new Dimension(200, btnNewGame.getMinimumSize().height));
		buttonPane.add(btnReview); 
		buttonPane.add(Box.createRigidArea(new Dimension(0, 10)));

		btnStats.setMaximumSize(new Dimension(200, btnNewGame.getMinimumSize().height));
		buttonPane.add(btnStats); 
		buttonPane.add(Box.createRigidArea(new Dimension(0, 10)));
		
		btnClear.setMaximumSize(new Dimension(200, btnNewGame.getMinimumSize().height));
		buttonPane.add(btnClear);
		buttonPane.add(Box.createRigidArea(new Dimension(0, 10)));

		btnQuit.setMaximumSize(new Dimension(200, btnNewGame.getMinimumSize().height));
		buttonPane.add(btnQuit);

		try {
			BufferedImage img = ImageIO.read(new File(".media/VOXSPELL_LOGO.png"));
			ImageIcon icon = new ImageIcon(img);
			JLabel label = new JLabel(icon);
			panel.add(label, BorderLayout.WEST);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		panel.add(buttonPane, BorderLayout.CENTER);
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// when a button is clicked, it checks which button is the source and then set the GUI
		// to the associated button, and generate a model to fit that GUI
		Command cmd = null;
		if(e.getSource() == btnNewGame){
			mediator.sendUpdateToGUI("GUI");
			cmd = new newGame(false);
			mediator.setModelOfGUI("GUI", cmd, (String)levelList.getSelectedItem());
		} else if(e.getSource() == btnStats){
			mediator.sendUpdateToGUI("VIEW");
			cmd = new newStats();
			mediator.setModelOfGUI("VIEW", cmd, (String)levelList.getSelectedItem());
		} 
		else if (e.getSource() == btnClear) {
			// a warning dialog is shown to confirm that the user really wants to clear statistics
			int PromptResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear your statistics?", "Confirmation", 
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if(PromptResult == 0){
				cmd = new clearStats();
				mediator.resetSideStats();
			}
		} else if (e.getSource() == btnReview){
			mediator.sendUpdateToGUI("GUI");
			cmd = new newGame(true);
			mediator.setModelOfGUI("GUI", cmd, (String)levelList.getSelectedItem());
		}
		else if (e.getSource() == btnQuit) {
			System.exit(0);
		}
		if(cmd != null){
			cmd.execute();
		}
	}

	public String getSpellingLevel(){
		return (String)levelList.getSelectedItem();
	}
	
	@Override
	public void setModel(Command cmd) {	
		// mainMenu does not need a model associated with it - it merely is used to notify listeners
		// of button clicks - no processing
	}

}
