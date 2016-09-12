package VOXSPELL;
/**
 * This class is the model associated with the viewStats GUI. It handles the processing - sorting,
 * and what stats to display. It then tells the viewStatsGUI to update itself
 * 
 * @author jacky
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class newStats implements Command{
	private viewStatsGUI _GUI; // associating itself with its view
	private HashMap<String, int[]> scoresForAllWords = new HashMap<String, int[]>();
	// hash map is used to map its failed, master, and faulted score to the word
	private List<String> _words = new ArrayList<String>();

	@Override
	public void execute() {
		generateMap();
		generateScore();
		sortByAlphabetical();
	}
	
	public void sortByAlphabetical(){
		// This method sorts by alphabetical order when the button is pressed
		Comparator<String> alphabeticalSort = new Comparator<String>(){
			public int compare(String word1, String word2){
				return word1.compareTo(word2);
			}
		};
		Collections.sort(_words,alphabeticalSort);
		printStats();
	}
	
	public void sortByMaster(){
		// This method sorts by best order when the button is pressed
		// creating an anon inner class to override the compare method for the .sort method
		Comparator<String> bestSort = new Comparator<String>(){
			public int compare(String word1, String word2){
				if(scoresForAllWords.get(word1)[0] > scoresForAllWords.get(word2)[0]){
					return -1;
				} else if(scoresForAllWords.get(word1)[0] < scoresForAllWords.get(word2)[0]){
					return 1;
				} else {
					return 0;
				}
			}
		};
		Collections.sort(_words,bestSort);
		printStats();
	}
	
	public void sortByFailed(){
		// This method sorts by the most failed words when the button is pressed
		Comparator<String> worstSort = new Comparator<String>(){
			public int compare(String word1, String word2){
				if(scoresForAllWords.get(word1)[2] > scoresForAllWords.get(word2)[2]){
					return -1;
				} else if(scoresForAllWords.get(word1)[2] < scoresForAllWords.get(word2)[2]){
					return 1;
				} else {
					return 0;
				}
			}
		};
		Collections.sort(_words,worstSort);
		printStats();
	}
	
	public void printStats(){
		// this method updates the GUI to show stats
		String[] fileNames = {"Mastered", "Faulted", "Failed"};
		if(_words.isEmpty()){
			_GUI.showStats("You have yet to attempt any words!");
		} else {
			for(String key:_words){
				_GUI.showStats("Word: "+key+"\n");
				for(int i = 0; i<scoresForAllWords.get(key).length; i++){
					_GUI.showStats(fileNames[i]+": "+scoresForAllWords.get(key)[i]+"\n");
				}
				_GUI.showStats("\n");
			}
		}
	}
	
	private void generateScore() {
		// this method generates the scores associated with each word 
		String[] fileNames = {".mastered.txt", ".faulted.txt", ".failed.txt"};
		List<String> words = new ArrayList<String>();
		words = new fileHandler().getWordList(".stats.txt");
		for(String word: words){
			for(int i = 0; i<fileNames.length; i++){
				for(String key:scoresForAllWords.keySet()){
					// the fileNames indexes are stored mastered, faulted and failed 0, 1, 2 respectively
					if((key+": "+fileNames[i]).equals(word)){
						// if the string matches the output of for example echoes: .mastered.txt then
						// we increment its score location by 1;
						scoresForAllWords.get(key)[i] = scoresForAllWords.get(key)[i]+1;
					}
				}
			}
		}
	}

	@Override
	public void addGUI(GUI GUI) {
		_GUI = (viewStatsGUI) GUI;

	}

	private void generateMap(){
		// generating the keys associated with the values from the three different stats files
		String[] fileNames = {".failed.txt", ".faulted.txt", ".mastered.txt"};
		List<String> words = new ArrayList<String>();
		for(int i = 0; i<fileNames.length; i++){
			words = new fileHandler().getWordList(fileNames[i]);
			for(String word: words){
				// if the word does not exist in the _words list then we map it to an array of {0,0,0}
				// the positions are mastered, faulted and failed
				if(!scoresForAllWords.containsKey(word)){
					int[] scores = {0,0,0};
					scoresForAllWords.put(word, scores);
					_words.add(word);
				}
			}
		}
	}

}
