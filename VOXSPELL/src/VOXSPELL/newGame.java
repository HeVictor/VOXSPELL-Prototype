package VOXSPELL;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	protected boolean _review;
	private int _iterations = 0;
	private List<Integer> _wordIndex = new ArrayList<Integer>();
	protected String _level = "%Level 1";
	protected int _wordsCorrect = 0;
	private String _voiceSelected = "";
	private int _listSize = 0;
	private static final String LIST_FILE_NAME = "NZCER-spelling-lists.txt";
	public static final int NUM_WORDS_TESTED = 10; // A constant of num words to be teseted, refactored - Victor
	
	private ExecutorService _threadPool;

	public newGame(boolean reviewBoolean){
		/*
		 * This constructor sets the game type of the spelling. If it's a review, then it will
		 * read from the .failed, whereas if its a new game, it reads from wordlist.
		 */
		_review = reviewBoolean;
		
		// This supposedly solves the overlapping by only have one thread at a time. Not sure if this is optimal. - Victor
		_threadPool = Executors.newFixedThreadPool(1);
	}

	public void execute() {
		this._voiceSelected = _GUI.getVoiceField();
		this._wordsCorrect = 0;
		this._iterations = 0;
		this._words.clear();
		this._currentWord = "";
		this._wordIndex.clear();
		/*
		 * This is signature method from the Command interface, and the execute method gets called
		 * Specifically, it is executing the model view so the spellingGUI can begin.
		 */
		if(_review){
			_GUI.setLevelLabel("REVIEW");
			_fileName = ".failed.txt";
			_words = new fileHandler().getWordList(_fileName, null);
		} else {
			_GUI.setLevelLabel(this._level.split(" ")[1]);
			_fileName = LIST_FILE_NAME;
			_words = new fileHandler().getWordList(_fileName, _level);
		}
		// setting the wordList to the required spelling list
		if(_words.size() == 0){
			_GUI.btnStart.setEnabled(false);
			_GUI.setTxtField("No words to revise. \nPress back to return to main menu.");
		} else if (_words.size() >= NUM_WORDS_TESTED){
			this._listSize = NUM_WORDS_TESTED;
		} else {
			this._listSize = _words.size();
		}
		//generateRandomWord(); begin the gui operation by generating a random word
	}

	public void spell(){
		/*
		 * merely sends a string for the process builder to read through text to speech
		 */
		textToSpeech("festival -b '(voice_"+_voiceSelected+")' '(SayText \"Please spell "+_currentWord+"\")'", "");
	}

	// Now this method only generates and returns a random word and sets current word - Victor
	public String generateRandomWord(){
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
			if(_words.size() >= NUM_WORDS_TESTED){
				// makes a call to the view to update the progress of the jprogressbar - jacky
				_GUI.setJProgress(0, NUM_WORDS_TESTED, this._wordsCorrect);
				return "Spell word "+(_iterations+1)+" of "+NUM_WORDS_TESTED+": "; // Changed to cater for user input display - Victor
			} else {
				_GUI.setJProgress(0, _words.size(), this._wordsCorrect);
				return "Spell word "+(_iterations+1)+" of "+_words.size()+": "; // Changed to cater for user input display - Victor
			}
			
		}
		return null;
	}
	
	// This method is an algorithm to generate, append text to output and then speaks the word to the user. - Victor
	public void proceedToNextWord(String condition) {
		
		String festivalMsg = "";
		
		// Changed below a bit to allow syncing of Festival and text output - Victor
		if (condition.equals("mastered") || condition.equals("faulted")) {
			festivalMsg = "Correct!";
		} else if (condition.equals("failed")){
			festivalMsg = "Incorrect!";
		}	
		
		// this is necessary to ensure the next word is not read out after 10 iterations or word.size()
		// is met
		if(_iterations == NUM_WORDS_TESTED || _words.size()-1 < _iterations){
			_GUI.appendTxtField("No more words to cover.");
			textToSpeech("festival -b '(voice_"+_voiceSelected+")' '(SayText \""+festivalMsg+"\")'", "");
		} else {
			
			String nextWord = generateRandomWord();
			
			if (condition.equals("")) { // This is only for the first word of every spelling test session - Victor
				_GUI.appendTxtField(nextWord);
			} else {
				textToSpeech("festival -b '(voice_"+_voiceSelected+")' '(SayText \""+festivalMsg+"\")'", nextWord);
			}
			spell(); // asks -tts to say the word outloud
			
		}
	}

	public void textToSpeech(String command, String msgOutput){
		/*
		 * this function builds a process which is executed within the bash shell
		 */
		// these are disabling the buttons to prevent error-prone states i.e. too many VoiceWorker objects
		// being generated.
		_GUI.btnRelisten.setEnabled(false);
		_GUI.btnEnter.setEnabled(false);
		VoiceWorker voice = new VoiceWorker(command, _GUI.btnRelisten, _GUI.btnEnter, this, _GUI, msgOutput);
		
		_threadPool.submit(voice);
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

	protected String getVoice(){
		return this._voiceSelected;
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
		 * checks the user input is valid and does not contain any non-letters except apostrophes
		 */
		if(userInput.equals("")){
			return false;
		}
		char[] characters = userInput.toCharArray();
		for(char c: characters){
			if(!Character.isLetter(c) && (c != '\'') ){
				return false;
			}
		}
		return true;
	}

	protected void setVoice(String voice){
		this._voiceSelected = voice;
	}
	
	protected void setLevel(String level){
		_level = level;
	}
	
	protected void resetVoice(){
		
	}
	
	// Refactored this into a method - Victor
	private void teachSpelling() {
		// changed below to only do one Festival call to solve overlap - Victor
		textToSpeech("festival -b '(voice_"+_voiceSelected+")' '(SayText \"This is how you spell: "+_currentWord+"\")'","");
		// sends a batch command to festival through the process builder to set the voice, and say the current word - Jacky
		for(char c:_currentWord.toCharArray()){
			textToSpeech("festival -b '(voice_"+_voiceSelected+")' '(SayText \""+c+"\")'","");
			// speaking each letter individual - SayText has an inherent gap between each call.
		}
	}
	
	

	protected void whereToWrite(String condition){
		/*
		 * writes to a file depending on the condition, it will append the word to mastered/faulted/
		 * failed if it does not exist. if review is specified, it will prompt users if they
		 * want to know how to spell a word if it is failed again
		 */
		_iterations++;
		if (condition.equals("mastered")){
			_wordsCorrect++;
			_GUI.setJProgress(0, this._listSize, this._wordsCorrect);
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
				textToSpeech("festival -b '(voice_"+_voiceSelected+")' '(SayText \"Incorrect!\")'",""); // Moved code around - Victor
				if(_GUI.promptUserToRelisten()){
					teachSpelling();
					proceedToNextWord("");
					return;
				}
			}
		}
		
		// Refactored the code previously here inside proceedToNextWord, so that it may say "Correct/Incorrect" for the last word. - Victor
		proceedToNextWord(condition);
		
	}
}