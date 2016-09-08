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
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane; 
import javax.swing.JTextArea; 
import javax.swing.JTextField;

public class spellingGUI extends GUI implements ActionListener{
	private JTextField txt = new JTextField("");
	private JTextArea txtOutput = new JTextArea(10, 20);
	private newGame modelController;
	private int count = 0;
	private int iterations = 0;
	private JButton btnEnter = new JButton("Enter"); 
	private JButton btnBack = new JButton("Back");
	private JButton btnStart = new JButton("Start");
	private JComboBox levelList;
	

	public spellingGUI(GUIMediator m) {
		// associating this GUI with a mediator to notify changes.
		super(m);
	}

	public JPanel creatingGUI() {
		String[] levels = new String[11];
		for(int i = 1; i < 12; i++){
			levels[i-1] = "Level "+i;
		}
		levelList = new JComboBox(levels);
		levelList.addActionListener(this);
		JPanel spellingPanel = new JPanel();
		txtOutput.setEditable(false);
		spellingPanel.setLayout(new BorderLayout());
		btnEnter.addActionListener(this); 
		btnBack.addActionListener(this);
		btnStart.addActionListener(this);
		txt.setPreferredSize(new Dimension(200, 40));
		JScrollPane scroll = new JScrollPane(txtOutput);
		
		JPanel firstPanel = new JPanel();
		firstPanel.setLayout(new BorderLayout());
		firstPanel.add(scroll, BorderLayout.PAGE_START);
		firstPanel.add(txt, BorderLayout.WEST); 
		firstPanel.add(btnEnter, BorderLayout.CENTER); 
		firstPanel.add(btnBack, BorderLayout.EAST);
		
		JPanel secondPanel = new JPanel();
		secondPanel.setLayout(new BorderLayout());
		secondPanel.add(btnStart, BorderLayout.LINE_START);
		secondPanel.add(levelList, BorderLayout.CENTER);
		
		spellingPanel.add(firstPanel, BorderLayout.PAGE_START);
		spellingPanel.add(secondPanel, BorderLayout.PAGE_END);
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
		} else if(e.getSource().equals(btnEnter)) {
			if(iterations != 3 || modelController.getWordListSize() > iterations){
				String userInput = txt.getText(); // gets what the user entered into the JTextField
				txt.setText("");
				if(!modelController.isValid(userInput)){
					// sends a warning if any symbols are entered into the field
					JOptionPane.showMessageDialog(null, "Must enter a valid input (no symbols or empty field)!", "Warning!", JOptionPane.WARNING_MESSAGE);
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
		else if(e.getSource().equals(levelList)){
			modelController.setLevel((String)levelList.getSelectedItem());
		}
		else if (e.getSource().equals(btnStart)){
			modelController.generateRandomWord();
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
