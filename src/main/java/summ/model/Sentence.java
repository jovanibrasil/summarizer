package summ.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sentence {

	private int id;
	private int pos; // relative position to paragraph  	

	private String rawSentence;
	private String editedSentence;
	private Map<String, Object> features; 
	
	private ArrayList<Word> words;
	//private Map<String, Word> words;
	
	private Boolean isTitle;
	
	public Sentence(String rawSentence) {
		
		this.rawSentence = rawSentence;
		this.editedSentence = rawSentence;
		this.features = new HashMap<>();
		
		this.words = new ArrayList<>();
		//this.words = new HashMap<>();
		this.isTitle = false;
		
	}
	
	public void addWord(Word word) {
		//this.words.put(word.getRawWord(), word);
		this.words.add(word);
	}
	
	public void removeWord(String rawWord) {
		this.words.remove(rawWord);
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
	
	public String[] getRawWords() {
		String words[] = new String[this.words.size()];
		int index = 0;
		for (Word word : this.words) {
			words[index++] = word.getRawWord(); 
		}
		return words;
	}
	
	public boolean containsWord(Word word) {
		return this.words.stream().filter(w -> w.getRawWord().equals(word.getRawWord())).findFirst().isPresent();
		//return this.words.containsKey(word.getRawWord());
	}
	
	public String getEditedSentence() {
		return editedSentence;
	}
	
	public Word getFirstWord() {
		return this.words.get(0);
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
	
	public Map<String, Object> getFeatures() {
		return this.features;
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
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String toString(List<String> features) {
		StringBuilder sb = new StringBuilder();
		
		features.forEach(featureName -> {
			sb.append(String.format("{ %s: %s }", featureName,
					this.features.get(featureName)).toString());
		});
		
		return this.rawSentence + "\n\t" + 
				sb.toString() + "\n";
	}
	
	@Override
	public String toString() {
		return this.rawSentence + "\n\t" + 
				this.words + "\n\t" +
				this.features.toString() + "\n";
	}
	
}
