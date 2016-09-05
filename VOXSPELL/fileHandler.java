package VOXSPELL;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This class handles all file-related stuff. It removes words and appends words to the
 * specified files. If needed, it will also clearstats().
 * 
 * @author jacky
 *
 */
public class fileHandler {
	String[] _files = {".mastered.txt",".stats.txt",".failed.txt",".faulted.txt"};
	
	protected void writeToFile(String fileName, String currentWord){
		// this method writes to the specified fileName - it checks if the word exists yet or not
		// if it does, it will then add it. it will also add the word to stats along with whether
		// it was faulted, failed, or mastered.
		try {
			if(wordNotExists(fileName, currentWord)){
				// true set to enable appending to file writer
				BufferedWriter typeOfSuccess = new BufferedWriter(new FileWriter(fileName, true));
				appendingFile(typeOfSuccess, currentWord);
				typeOfSuccess.close();
			}
			BufferedWriter stats = new BufferedWriter(new FileWriter(".stats.txt", true));
			appendingFile(stats, currentWord+": "+fileName);
			stats.close();
		} catch (IOException e1) {
		} 
	}

	private boolean wordNotExists(String fileName, String currentWord) {
		// this method checks if a word exists in the specified filename
		try {
			String word = null;
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
			while((word = bufferedReader.readLine()) != null) {
				if(word.equals(currentWord)){
					bufferedReader.close();
					return false;
				}
			}
			bufferedReader.close();
		} catch (Exception e) {
		}
		return true;
	}

	private void appendingFile(BufferedWriter fileName, String toAdd){
		/*
		 * This method appends to an existing file and adds the string to a new line.
		 */
		try {
			fileName.write(toAdd);
			fileName.newLine();
		} catch (IOException e) {
		}
	}
	
	protected void removingWord(String fileName, String toRemove){
		/*
		 * this method finds a string in a file and removes it from the file - removing the blank space
		 * too. it uses a temporary file to do this.
		 */
		try {
			File tempFile = new File(".TempWordlist.txt");
			File inputFile = new File(fileName);
			
			BufferedWriter fileToWrite = new BufferedWriter(new FileWriter(tempFile, true));
			BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
			String word = null;
			while((word = bufferedReader.readLine()) != null) {
				if(!word.equals(toRemove)){
					appendingFile(fileToWrite, word);
				}
			}
			bufferedReader.close();
			inputFile.delete();
			tempFile.renameTo(inputFile);
		} catch (IOException e) {
		}
	}

	protected void clearStats(){
		/*
		 * reset all the statistics by recreating the files.
		 */
		try {
			for(int i = 0; i<_files.length; i++){
				BufferedWriter out = new BufferedWriter(new FileWriter(_files[i]));
				out.close();
			}
		} catch (IOException e) {
		}
	}

	public List<String> getWordList(String fileName) {
		/*
		 * retrieve the word list associated with a file.
		 */
		String word = null;
		List<String> words = new ArrayList<String>();
		FileReader fileReader;
		try {

			fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while((word = bufferedReader.readLine()) != null) {
				words.add(word);
			}   
			bufferedReader.close();         
		} catch (Exception e){
		}
		return words;
	}
}
