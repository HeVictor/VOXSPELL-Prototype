package VOXSPELL;

/**
 * this class is the JPanel GUI for the spelling, particularly review and new game.
 * it is user driven and all processing is associated with its newGame model.
 * 
 * @author jacky
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
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
	private JButton btnRelisten = new JButton("Relisten");
	private JComboBox<String> festivalSelect;
	

	public spellingGUI(GUIMediator m) {
		// associating this GUI with a mediator to notify changes.
		super(m);
	}

	public JPanel creatingGUI() {

		JPanel spellingPanel = new JPanel();
		
		txtOutput.setEditable(false);
		spellingPanel.setLayout(new BorderLayout());
		btnEnter.addActionListener(this); 
		btnBack.addActionListener(this);
		btnRelisten.addActionListener(this);
		txt.setPreferredSize(new Dimension(200, 40));
		JScrollPane scroll = new JScrollPane(txtOutput);
		scroll.setPreferredSize(new Dimension(300, 200));
		
		JPanel firstPanel = new JPanel();
		firstPanel.setLayout(new BorderLayout());
		firstPanel.add(scroll);
		
		JPanel secondPanel = new JPanel();
		secondPanel.setLayout(new BoxLayout(secondPanel, BoxLayout.Y_AXIS));
		String[] levels = new String[11];
		for(int i = 1; i < 12; i++){
			levels[i-1] = "Level "+i;
		}
		festivalSelect = new JComboBox<String>(levels);
		festivalSelect.setMaximumSize(new Dimension(200, btnRelisten.getMinimumSize().height));
		btnRelisten.setMaximumSize(new Dimension(200, btnRelisten.getMinimumSize().height));
		btnRelisten.setAlignmentX(Component.CENTER_ALIGNMENT);
		secondPanel.add(festivalSelect);
		secondPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		secondPanel.add(btnRelisten);
		secondPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		secondPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		JLabel correctLabel = new JLabel("Words Correct");
		correctLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		secondPanel.add(correctLabel);
		
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("");
        secondPanel.add(progressBar);
		
		JPanel thirdPanel = new JPanel();
		thirdPanel.setLayout(new BorderLayout());
		thirdPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		thirdPanel.add(txt, BorderLayout.CENTER);
		thirdPanel.add(btnEnter, BorderLayout.EAST);
		
		JPanel fourthPanel = new JPanel();
		fourthPanel.setLayout(new BoxLayout(fourthPanel, BoxLayout.X_AXIS));
		btnBack.setPreferredSize(new Dimension(20, 20));
		fourthPanel.add(btnBack);
		
		spellingPanel.add(firstPanel, BorderLayout.LINE_START);
		spellingPanel.add(secondPanel, BorderLayout.EAST);
		spellingPanel.add(thirdPanel, BorderLayout.SOUTH);
		
		spellingPanel.add(fourthPanel, BorderLayout.NORTH);
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
			if(iterations != 10 || modelController.getWordListSize() > iterations){
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
					if(iterations == 10 || modelController.getWordListSize() < iterations){
						iterations  = 0;
						txtOutput.setText("");
						mediator.sendUpdateToGUI("MAIN");
					}
				}
			}
		} else if (e.getSource() == btnRelisten){
			modelController.spell();
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
