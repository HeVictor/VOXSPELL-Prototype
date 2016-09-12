package VOXSPELL;

/**
 * This class is the mainMenuGUI JPanel. It has buttons that enables the user to start a new quiz,
 * view stats, clear, review or quit the app.
 * 
 * @author jacky
 */

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class mainMenuGUI extends GUI implements ActionListener{
	private JButton btnNewGame = new JButton("New Game"); 
	private JButton btnReview = new JButton("Review Mistakes"); 
	private JButton btnStats = new JButton("View Stats");
	private JButton btnClear = new JButton("Clear Stats"); 
	private JButton btnQuit = new JButton("Quit Game"); 
	
	public mainMenuGUI(GUIMediator mediator){
		super(mediator);
	}
	
	public JPanel creatingGUI(){
		// creating the GUI Jpanel
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
		btnNewGame.addActionListener(this); 
		btnReview.addActionListener(this); 
		btnStats.addActionListener(this); 
		btnQuit.addActionListener(this); 
		btnClear.addActionListener(this); 
		buttonPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		btnNewGame.setMaximumSize(new Dimension(200, btnNewGame.getMinimumSize().height));
		buttonPane.add(btnNewGame);
		btnReview.setMaximumSize(new Dimension(200, btnNewGame.getMinimumSize().height));
		buttonPane.add(btnReview); 
		btnStats.setMaximumSize(new Dimension(200, btnNewGame.getMinimumSize().height));
		buttonPane.add(btnStats); 
		btnClear.setMaximumSize(new Dimension(200, btnNewGame.getMinimumSize().height));
		buttonPane.add(btnClear);
		btnQuit.setMaximumSize(new Dimension(200, btnNewGame.getMinimumSize().height));
		buttonPane.add(btnQuit);
		return buttonPane;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// when a button is clicked, it checks which button is the source and then set the GUI
		// to the associated button, and generate a model to fit that GUI
		Command cmd = null;
		if(e.getSource() == btnNewGame){
			mediator.sendUpdateToGUI("GUI");
			cmd = new newGame(false);
			mediator.setModelOfGUI("GUI", cmd);
		} else if(e.getSource() == btnStats){
			mediator.sendUpdateToGUI("VIEW");
			cmd = new newStats();
			mediator.setModelOfGUI("VIEW", cmd);
		} else if (e.getSource() == btnClear) {
			// a warning dialog is shown to confirm that the user really wants to clear statistics
			int PromptResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear your statistics?", "Confirmation", 
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if(PromptResult == 0){
				cmd = new clearStats();
			}
		} else if (e.getSource() == btnReview){
			mediator.sendUpdateToGUI("GUI");
			cmd = new newGame(true);
			mediator.setModelOfGUI("GUI", cmd);
		}
		else if (e.getSource() == btnQuit) {
			System.exit(0);
		}
		if(cmd != null){
			cmd.execute();
		}
	}

	@Override
	public void setModel(Command cmd) {	
		// mainMenu does not need a model associated with it - it merely is used to notify listeners
		// of button clicks - no processing
	}

}
