package VOXSPELL;

/**
 * This class is associated with the viewStats button - the JPanel GUI. its model is the newStats
 * class. it merely handles if a sort is pressed, it notifies its model and updates.
 * @author jacky
 * 
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class viewStatsGUI extends GUI implements ActionListener {

	public viewStatsGUI(GUIMediator m) {
		super(m);
	}

	private JTextArea _txtOutput = new JTextArea(10, 20);
	private newStats _modelController;
	private JButton _btnAlpha = new JButton("Alphabetical");
	private JButton _btnBest = new JButton("Best");
	private JButton _btnWorst = new JButton("Worst");
	private JButton _btnBack = new JButton("Back");

	@Override
	public JPanel creatingGUI() {
		JPanel spellingPanel = new JPanel();
		_txtOutput.setEditable(false);
		spellingPanel.setLayout(new BorderLayout());
		_btnBest.addActionListener(this); 
		_btnWorst.addActionListener(this); 
		_btnAlpha.addActionListener(this); 
		_btnBack.addActionListener(this);
		JScrollPane scroll = new JScrollPane(_txtOutput);
		JPanel buttonPanel = new JPanel();
		spellingPanel.add(scroll, BorderLayout.PAGE_START);
		
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(_btnAlpha); 
		buttonPanel.add(_btnBest); 
		buttonPanel.add(_btnWorst); 
		buttonPanel.add(_btnBack);
		
		spellingPanel.add(buttonPanel, BorderLayout.LINE_START);
		return spellingPanel;
	}

	@Override
	public void setModel(Command cmd) {
		// setting the model, and resetting the textField
		_txtOutput.setText("");
		_modelController = (newStats) cmd;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == _btnBack){
			mediator.sendUpdateToGUI("MAIN");
		} else if (e.getSource() == _btnAlpha){
			_txtOutput.setText("");
			_modelController.sortByAlphabetical();
		} else if (e.getSource() == _btnBest){
			_txtOutput.setText("");
			_modelController.sortByMaster();
		} else if (e.getSource() == _btnWorst){
			_txtOutput.setText("");
			_modelController.sortByFailed();
		}
		
	}
	
	public void showStats(String statForWord){
		// this method appends to the textfield
		_txtOutput.append(statForWord);
	}

}
