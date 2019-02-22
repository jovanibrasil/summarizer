package model;

import java.util.ArrayList;

public class Text {

	private ArrayList<Paragraph> paragraphs;
	private String rawText; 
	
	public Text(String rawText) {
		this.rawText = rawText;
		paragraphs = new ArrayList<>();
	}
	
	public ArrayList<Paragraph> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(ArrayList<Paragraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}
	
	@Override
	public String toString() {
		return rawText;
	}
	
}
