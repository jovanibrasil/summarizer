package summ.model;

import java.util.HashMap;
import java.util.Map;

public class Word implements Comparable<Word> {
	
	private Map<String, Double> features;
	private String posTag; // morphological class
	private String rawToken; // (lexeme) the original token text
	private String processedToken;
	
	public Word(String rawWord) {
		this.rawToken = rawWord;
		this.processedToken = rawWord;
		this.features = new HashMap<>();
	}
	
	public String getProcessedToken() {
		return processedToken;
	}

	public void setProcessedToken(String processedToken) {
		this.processedToken = processedToken;
	}
	
	public String getRawWord() {
		return rawToken;
	}

	public void setRawWord(String rawWord) {
		this.rawToken = rawWord;
	}

	public void addFeature(String key, Double value) {
		this.features.put(key, value);
	}
	
	public Double getFeature(String key) {
		if(this.features.containsKey(key)) {
			return this.features.get(key);
		}
		return null;
	}

	public String getPosTag() {
		return posTag;
	}

	public void setPosTag(String posTag) {
		this.posTag = posTag;
	}

	@Override
	public int compareTo(Word w1) {
		System.out.println("comparing " + w1.getRawWord() + " with " + this.getRawWord());
		return w1.getRawWord().compareTo(this.getRawWord());
	}
	
	@Override
	public String toString() {
		return this.rawToken;
	}
	
}
