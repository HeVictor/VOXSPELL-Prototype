package VOXSPELL;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the model for the spellingGUI. It handles the data processing and sends an
 * update message to the associated GUI to change its view.
 * 
 * @author Jacky Lo
 *
 */

public class newGame implements Command{
	private String _fileName;
	private List<String> _words = new ArrayList<String>();
	private spellingGUI _GUI;
	private String _currentWord = "";
	private boolean _review;
	private int _iterations = 0;
	private List<Integer> _wordIndex = new ArrayList<Integer>();

	public newGame(boolean reviewBoolean){
		/*
		 * This constructor sets the game type of the spelling. If it's a review, then it will
		 * read from the .failed, whereas if its a new game, it reads from wordlist.
		 */
		_review = reviewBoolean;
	}

	public void execute() {
		/*
		 * This is signature method from the Command interface, and the execute method gets called
		 * Specifically, it is executing the model view so the spellingGUI can begin.
		 */
		if(_review){
			_fileName = ".failed.txt";
		} else {
			_fileName = "wordlist";
		}
		_words = new fileHandler().getWordList(_fileName); // setting the wordList to the required spelling list
		if(_words.size() == 0){
			_GUI.setTxtField("No words to revise. \nPress back to return to main menu.");
		}
		generateRandomWord(); //begin the gui operation by generating a random word
	}

	public void spell(){
		/*
		 * merely sends a string for the process builder to read through text to speech
		 */
		textToSpeech("echo \"Please spell "+_currentWord+"... "+_currentWord+"\" | festival --tts");
	}

	public void generateRandomWord(){
		/*
		 * this method generates random words from the wordList obtained - it cannot repeat
		 * previous words within the same session
		 */
		if(_words.size() > 0){
			int randomWord = (int) Math.ceil(Math.random()*_words.size()-1);
			while(_wordIndex.contains(randomWord)){ // this checks that the word has not been previously assessed
				randomWord = (int) Math.ceil(Math.random()*_words.size()-1);
			}
			_wordIndex.add(randomWord);
			_currentWord = _words.get(randomWord);
			_GUI.resetSpelling();
			if(_words.size() > 3){
				_GUI.appendTxtField("Spell word: "+(_iterations+1)+" of 3\n");
			} else {
				_GUI.appendTxtField("Spell word: "+(_iterations+1)+" of "+_words.size()+"\n");
			}
			spell(); // asks -tts to say the word outloud
		}
	}

	public void textToSpeech(String command){
		/*
		 * this function builds a process which is executed within the bash shell
		 */
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
		try {
			Process process = pb.start();
			process.waitFor(); // waiting for the process to finish
		} catch (Exception e) {
		}
	}

	public String getCurrentWord(){
		return _currentWord;
	}

	public boolean isCorrect(String answer){
		/*
		 * this function merely checks that the user answer is equal to the current word being assessed
		 */
		if(answer.toLowerCase().equals(_currentWord.toLowerCase())){
			return true;
		} else {
			return false;
		}
	}

	protected int getWordListSize(){
		return _words.size();
	}

	protected void writeWordToFile(String fileName){
		/*
		 * this method calls the fileHandler and appends to the file thats specified with the word
		 */
		new fileHandler().writeToFile(fileName, _currentWord);
	}

	protected void removeWordFromFile(String fileName){
		/*
		 * this method calls the fileHandler and remove the word from the specified file
		 */
		new fileHandler().removingWord(fileName, _currentWord);
	}

	public void addGUI(GUI viewGUI){
		/*
		 * associates this model with a spellingGUI
		 */
		_GUI = (spellingGUI)viewGUI;
	}

	public boolean isValid(String userInput) {
		/*
		 * checks the user input is valid and does not contain any non-letters
		 */
		char[] characters = userInput.toCharArray();
		for(char c: characters){
			if(!Character.isLetter(c)){
				return false;
			}
		}
		return true;
	}

	protected void whereToWrite(String condition){
		/*
		 * writes to a file depending on the condition, it will append the word to mastered/faulted/
		 * failed if it does not exist. if review is specified, it will prompt users if they
		 * want to know how to spell a word if it is failed again
		 */
		_iterations++;
		if (condition.equals("mastered")){
			writeWordToFile(".mastered.txt");
			if(_review){
				removeWordFromFile(".failed.txt");
			}
		} else if (condition.equals("faulted")){
			if(_review){
				removeWordFromFile(".failed.txt");
			}
			writeWordToFile(".faulted.txt");
		} else if(condition.equals("failed")){
			writeWordToFile(".failed.txt");
			if(_review){
				if(_GUI.promptUserToRelisten()){
					textToSpeech("echo \"This is how you spell: "+_currentWord+"... \" | festival --tts");
					for(char c:_currentWord.toCharArray()){
						textToSpeech("echo \""+c+"... \" | festival --tts");
					}
				}
			}
		}
		// this is necessary to ensure the next word is not read out after 3 iterations or word.size()
		// is met
		if(_iterations == 3 || _words.size()-1 < _iterations){
			_GUI.setTxtField("No more words to cover.");
		} else {
			generateRandomWord();
		}
	}
}