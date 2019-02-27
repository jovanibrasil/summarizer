package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Paragraph {

	private int pos;
	private ArrayList<Sentence> sentences;
	private String rawParagraph;
	
	private Map<String, Double> features;
	
	public Paragraph(String rawParagraph) {
		this.rawParagraph = rawParagraph;
		this.sentences = new ArrayList<>();
		this.features = new HashMap<String, Double>();
	}
	
	public ArrayList<Sentence> getSentences() {
		return sentences;
	}

	public void setSentences(ArrayList<Sentence> sentences) {
		this.sentences = sentences;
	}
	
	public void addSentence(Sentence sentence) {
		this.sentences.add(sentence);
	}

	public String getRawParagraph() {
		return rawParagraph;
	}

	public void setRawParagraph(String rawParagraph) {
		this.rawParagraph = rawParagraph;
	}
	
	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}
	
	public int getLength() {
		return this.sentences.size();
	}
	
	public void addFeature(String key, Double value) {
		this.features.put(key, value);
	}
	
	public Double getFeature(String key) {
		return this.features.get(key);
	}

	@Override
	public String toString() {
		//return rawParagraph;
		StringBuilder sb = new StringBuilder();
		this.sentences.forEach(s -> {
			sb.append(s);
		});
		return sb.toString();
	}
	
}
