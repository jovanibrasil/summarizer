package model;

import java.util.ArrayList;

public class Paragraph {

	private ArrayList<Sentence> sentences;
	private String rawParagraph;
	
	public Paragraph(String rawParagraph) {
		this.rawParagraph = rawParagraph;
		this.sentences = new ArrayList<>();
	}
	
	public ArrayList<Sentence> getSentences() {
		return sentences;
	}

	public void setSentences(ArrayList<Sentence> sentences) {
		this.sentences = sentences;
	}

	public String getRawParagraph() {
		return rawParagraph;
	}

	public void setRawParagraph(String rawParagraph) {
		this.rawParagraph = rawParagraph;
	}
	
	@Override
	public String toString() {
		return rawParagraph;
	}
	
}
