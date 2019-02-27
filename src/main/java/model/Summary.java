package model;

import java.util.ArrayList;

public class Summary {

	private ArrayList<Paragraph> paragraphs;
	private String rawSummary;
	
	public Summary(String rawSummary) {
		this.paragraphs = new ArrayList<>();
		this.rawSummary = rawSummary;
	}
	
	public Summary() {
		this.paragraphs = new ArrayList<>();
	}
	
	public void addParagraph(Paragraph p) {
		this.paragraphs.add(p);
	}
	
	public ArrayList<Paragraph> getParagraphs(){
		return this.paragraphs;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		this.paragraphs.forEach(p -> {
			p.getSentences().forEach(s -> {
				sb.append(s.getRawSentence());
				sb.append("\n");
			});
		});
		return sb.toString();
	}
	
}
