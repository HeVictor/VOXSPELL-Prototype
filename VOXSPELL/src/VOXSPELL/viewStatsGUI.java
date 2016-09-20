package VOXSPELL;

/**
 * This class is associated with the viewStats button - the JPanel GUI. its model is the newStats
 * class. it merely handles if a sort is pressed, it notifies its model and updates.
 * @author jacky
 * 
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
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
	private JLabel _statsLabel = new JLabel("Overall Statistics");
	private JTextArea _txtAccuracy = new JTextArea(10,20);

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
		scroll.setPreferredSize(new Dimension(280, 250));
		
		JScrollPane accuracyScroll = new JScrollPane(_txtAccuracy);
		accuracyScroll.setPreferredSize(new Dimension(265, 250));
		
		JPanel buttonPanel = new JPanel();
		spellingPanel.add(scroll, BorderLayout.WEST);
		spellingPanel.add(accuracyScroll, BorderLayout.EAST);
		
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(_btnAlpha); 
		buttonPanel.add(_btnBest); 
		buttonPanel.add(_btnWorst); 
		
		_btnAlpha.setMaximumSize(new Dimension(140, _btnAlpha.getMinimumSize().height));
		_btnBest.setMaximumSize(new Dimension(55, _btnAlpha.getMinimumSize().height));
		_btnWorst.setMaximumSize(new Dimension(70, _btnAlpha.getMinimumSize().height));
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		_btnBack.setPreferredSize(new Dimension(20, 20));
		topPanel.add(_btnBack);
		topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		topPanel.add(_statsLabel);
		topPanel.add(Box.createRigidArea(new Dimension(160, 0)));
		topPanel.add(new JLabel("Overall Accuracy Rate"));
		
		spellingPanel.add(buttonPanel, BorderLayout.SOUTH);
		spellingPanel.add(topPanel,  BorderLayout.NORTH);
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
			_txtOutput.setText("");
			_txtAccuracy.setText("");
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

	public void showAccuracy(String statForLevel){
		this._txtAccuracy.append(statForLevel);
	}
	
}
