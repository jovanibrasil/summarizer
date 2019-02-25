package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sentence {

	private int pos;

	private String rawSentence;
	private String editedSentence;
	private Map<String, Object> features; 
	
	private ArrayList<Word> words;
	//private HashMap<String, Word> wordsDic;
	
	private Boolean isTitle;
	
	public Sentence(String rawSentence) {
		
		this.rawSentence = rawSentence;
		this.editedSentence = rawSentence;
		this.features = new HashMap<>();
		
		this.words = new ArrayList<>();
		//this.wordsDic = new HashMap<>();
		this.isTitle = false;
		
	}
	
	public String getRawSentence() {
		return rawSentence;
	}

	public void setRawSentence(String rawSentence) {
		this.rawSentence = rawSentence;
	}

	public ArrayList<Word> getWords() {
		return words;
	}

	public void setWords(ArrayList<Word> words) {
		this.words = words;
	}
	
	//	public void addWords(Word word) {
	//		this.words.add(word);
	//		this.wordsDic.put(word.getRawWord(), word);
	//	}
	//	
	//	public Word Word(String key) {
	//		return this.wordsDic.get(key);
	//	}
	
	public boolean containsWord(Word word) {
		//return this.words.contains(word);
		return this.words.stream().filter(w -> w.getRawWord().equals(word.getRawWord())).findFirst().isPresent();
	}
	
	public String getEditedSentence() {
		return editedSentence;
	}

	public void setEditedSentence(String editedSentence) {
		this.editedSentence = editedSentence;
	}
	
	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}
	
	public void addFeature(String key, Object value) {
		this.features.put(key, value);
	}
	
	public Object getFeature(String key) {
		return this.features.get(key);
	}
	
	public int getEditedSentenceLength() {
		return this.words.size();
	}
	
	public int getLength() {
		return this.words.size();
	}

	public Boolean isTitle() {
		return isTitle;
	}
	
	public void setTitle(Boolean isTitle) {
		this.isTitle =isTitle;
	}
	
	@Override
	public String toString() {
		return this.rawSentence + "\n	" + 
				this.features.toString() + "\n";
	}
	
}
