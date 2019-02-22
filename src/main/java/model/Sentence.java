package model;

import java.util.HashMap;
import java.util.Map;

public class Sentence {

	private String rawSentence;
	private String editedSentence;
	
	private Map<String, Word> words;
	
	public Sentence(String rawSentence) {
		this.rawSentence = rawSentence;
		this.editedSentence = rawSentence; 
		this.words = new HashMap<>();
	}
	
	public String getRawSentence() {
		return rawSentence;
	}

	public void setRawSentence(String rawSentence) {
		this.rawSentence = rawSentence;
	}

	public Map<String, Word> getWords() {
		return words;
	}

	public void setWords(Map<String, Word> words) {
		this.words = words;
	}
	
	public String getEditedSentence() {
		return editedSentence;
	}

	public void setEditedSentence(String editedSentence) {
		this.editedSentence = editedSentence;
	}

	@Override
	public String toString() {
		return this.rawSentence;
	}
	
}
