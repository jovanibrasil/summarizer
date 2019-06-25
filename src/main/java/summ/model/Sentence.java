package summ.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import summ.utils.Tuple;

public class Sentence implements Comparable<Sentence> {

	private int id;
	private int pos; // relative position to paragraph  	

	private String initialValue;
	private String currentValue;
	private Map<String, Object> features; 
	
	private List<Word> words;
	private Boolean isTitle;
	private Double score;
	
	public int wordCounter;
	
	public HashSet<String> wordList;
	
	public Sentence(String initialValue) {
		this.initialValue = initialValue;
		this.currentValue = initialValue;
		this.features = new HashMap<>();
		this.words = new ArrayList<>();
		this.isTitle = false;
		this.wordCounter = 0;
		

		this.wordList = new HashSet<String>();
		
	}
	
	public void addWord(Word word) {
		//this.words.put(word.getRawWord(), word);
		this.words.add(word);
	}
	
	public Word getWord(String rawWord) {
		for (Word word : this.words) {
			if(word.getInitialValue().equals(rawWord)) {
				return word;
			}
		}
		return null;
	}
	
	public void removeWord(Word word) {
		this.words.remove(word);
	}
	
	public String getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(String initialValue) {
		this.initialValue = initialValue;
	}

	public List<Word> getWords() {
		return words;
	}
	
	public String[] getRawWords() {
		String words[] = new String[this.words.size()];
		int index = 0;
		for (Word word : this.words) {
			words[index++] = word.getInitialValue(); 
		}
		return words;
	}
	
	public boolean containsWord(Word word) {
		return this.words.stream().filter(w -> w.getInitialValue().equals(word.getInitialValue())).findFirst().isPresent();
	}
	
	public String getCurrentValue() {
		return currentValue;
	}
	
	public Word getFirstWord() {
		if(this.words.size() > 0) {
			return this.words.get(0);
		}
		return null;
	}

	public void setCurrentValue(String editedSentence) {
		this.currentValue = editedSentence;
	}
	
	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}
	
	public void addFeature(String key, Object value) {
		this.features.put(key, value);
	}
	
	public Object getFeature(String key) {
		return this.features.get(key);
	}

	public Map<String, Double> getFeatures(List<String> selectedFeatures) {
		Map<String, Double> features = new HashMap<>();
		for (String feature : selectedFeatures) {
			features.put(feature, (Double)this.features.get(feature));
		}
		return features;
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
	
	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	@Override
	public int compareTo(Sentence s) {
		if(s.getId() > this.getId()) {
			return -1;
		}else if(s.getId() < this.getId()) {
			return 1;
		}else {
			return 0;
		}
	}
	
	public String toString(List<String> features) {
		StringBuilder sb = new StringBuilder();
		
		features.forEach(featureName -> {
			sb.append(String.format("{ %s: %s }", featureName,
					this.features.get(featureName)).toString());
		});
		
		return this.initialValue + "\n\t" + 
				sb.toString() + "\n";
	}
	
	@Override
	public String toString() {
		return this.initialValue + "\n\t" + 
				"ID: " + this.id + "\n\t" +
				this.words + "\n\t" +
				this.features.toString() + "\n\t" +
				"Score: " + this.getScore() + "\n";
	}

	public String getWordsToStringWithFeatures() {
		StringBuilder sb = new StringBuilder();
		for (Word w : this.getWords()) {
			sb.append(w.toString() + " (tf-isf=" + w.getFeature("tf_isf") + ") " );
		}
		return sb.toString();
	}
	
}
