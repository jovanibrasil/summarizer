package model;

import java.util.ArrayList;

public class Summary {

	private ArrayList<Sentence> sentences;
	
	public Summary() {
		this.sentences = new ArrayList<>();
	}
	
	public void addSentence(Sentence sentence) {
		this.sentences.add(sentence);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		this.sentences.forEach(s -> {
			sb.append(s.getRawSentence());
			sb.append("\n");
		});
		return sb.toString();
	}
	
}
