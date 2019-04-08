package summ.model;

import java.util.HashMap;
import java.util.Map;

public class Word implements Comparable<Word> {
	
	private Map<String, Double> features;
	private String posTag; // morphological class
	private String initialValue; // (lexeme) the original token text
	private String currentValue;
	
	public Word(String initialValue) {
		this.initialValue = initialValue;
		this.currentValue = initialValue;
		this.features = new HashMap<>();
	}
	
	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}
	
	public String getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(String initialValue) {
		this.initialValue = initialValue;
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
		return w1.getInitialValue().compareTo(this.getInitialValue());
	}
	
	@Override
	public String toString() {
		return this.initialValue;
	}
	
}
