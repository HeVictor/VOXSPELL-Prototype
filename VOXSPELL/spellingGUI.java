package VOXSPELL;

/**
 * this class is the JPanel GUI for the spelling, particularly review and new game.
 * it is user driven and all processing is associated with its newGame model.
 * 
 * @author jacky
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane; 
import javax.swing.JTextArea; 
import javax.swing.JTextField;

public class spellingGUI extends GUI implements ActionListener{

	public spellingGUI(GUIMediator m) {
		// associating this GUI with a mediator to notify changes.
		super(m);
	}

	private JTextField txt = new JTextField("");
	private JTextArea txtOutput = new JTextArea(10, 20);
	private newGame modelController;
	private int count = 0;
	private int iterations = 0;
	private JButton btnEnter = new JButton("Enter"); 
	private JButton btnBack = new JButton("Back");

	public JPanel creatingGUI() {
		JPanel spellingPanel = new JPanel();
		txtOutput.setEditable(false);
		spellingPanel.setLayout(new BorderLayout());
		btnEnter.addActionListener(this); 
		btnBack.addActionListener(this);
		txt.setPreferredSize(new Dimension(200, 40));
		JScrollPane scroll = new JScrollPane(txtOutput); 
		spellingPanel.add(scroll, BorderLayout.PAGE_START);
		spellingPanel.add(txt, BorderLayout.LINE_START); 
		spellingPanel.add(btnEnter, BorderLayout.CENTER); 
		spellingPanel.add(btnBack, BorderLayout.LINE_END);
		return spellingPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnBack)){
			// prompting user to confirm if they want to end this game.
			int PromptResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to leave this session?", "Confirmation", 
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if(PromptResult == 0){
				iterations  = 0;
				txtOutput.setText("");
				mediator.sendUpdateToGUI("MAIN"); // sends them back to the main menu GUI
			}
		} else {
			if(iterations != 3 || modelController.getWordListSize() > iterations){
				String userInput = txt.getText(); // gets what the user entered into the JTextField
				txt.setText("");
				if(!modelController.isValid(userInput)){
					// sends a warning if any symbols are entered into the field
					JOptionPane.showMessageDialog(null, "Please enter letters!", "Warning!", JOptionPane.WARNING_MESSAGE);
				} else if(modelController.getWordListSize() > 0){
					// this is the 'mastered' branch, it will notify the model to do appropriate processing
					if(modelController.isCorrect(userInput) && count == 0){
						modelController.textToSpeech("echo \"Correct!\" | festival --tts");
						txtOutput.append("Correct!\n");
						iterations++;
						modelController.whereToWrite("mastered");
					} else if(modelController.isCorrect(userInput)){
						// this is the faulted branch - specifically if count > 0, then it means they've had another try
						modelController.textToSpeech("echo \"Correct!\" | festival --tts");
						txtOutput.append("Correct!\n");
						iterations++;
						modelController.whereToWrite("faulted");
					} else if(count == 0){
						// this is if they've failed the word the first try
						modelController.textToSpeech("echo \"Incorrect, try once more: "+modelController.getCurrentWord()+"\" | festival --tts");
						txtOutput.append("Incorrect, try once more!\n");
						count++;
					}
					else {
						// this is if they've failed the word two times in a row
						modelController.textToSpeech("echo \"Incorrect!\" | festival --tts");
						txtOutput.append("Incorrect!\n");
						iterations++;
						modelController.whereToWrite("failed");
					}
					// reset the iterations, and text field, and send them back to the MAIN gui.
					if(iterations == 3 || modelController.getWordListSize() < iterations){
						iterations  = 0;
						txtOutput.setText("");
						mediator.sendUpdateToGUI("MAIN");
					}
				}
			}
		}
	}

	public void resetSpelling(){
		count = 0;
	}
	
	protected boolean promptUserToRelisten(){
		// this will prompt the user if they want to relisten to the word.
		int PromptResult = JOptionPane.showConfirmDialog(null, "Would you like to listen to the spelling of the word?", "Listen to Spelling",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if(PromptResult == 0){
			return true;
		} else {
			return false;
		}
	}
	
	protected void setTxtField(String txtToSet){
		txtOutput.setText(txtToSet);
	}
	
	protected void appendTxtField(String txtToAppend){
		txtOutput.append(txtToAppend);
	}

	public void setModel(Command cmd) {
		modelController = (newGame) cmd;		
	}
}
