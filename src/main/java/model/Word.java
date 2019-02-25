package model;

import java.util.HashMap;
import java.util.Map;

public class Word implements Comparable<Word> {
	
	private String rawWord;
	private Map<String, Float> features;
	
	public Word(String rawWord) {
		this.rawWord = rawWord;
		this.features = new HashMap<>();
	}
	
	public String getRawWord() {
		return rawWord;
	}

	public void setRawWord(String rawWord) {
		this.rawWord = rawWord;
	}

	public void addFeature(String key, Float value) {
		this.features.put(key, value);
	}
	
	public Float getFeature(String key) {
		if(this.features.containsKey(key)) {
			return this.features.get(key);
		}
		return null;
	}
	
	@Override
	public int compareTo(Word w1) {
		System.out.println("comparing " + w1.getRawWord() + " with " + this.getRawWord());
		return w1.getRawWord().compareTo(this.getRawWord());
	}
	
	@Override
	public String toString() {
		return this.rawWord;
	}
	
}
