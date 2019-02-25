package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Text {

	private ArrayList<Paragraph> paragraphs;
	private String rawText; 
	
	private HashMap<String, Object> features;

	private Sentence title;

	public Text(String rawText) {
		this.rawText = rawText;
		this.paragraphs = new ArrayList<>();
		this.features = new HashMap<>();
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
	
	public void addFeature(String key, Object value) {
		this.features.put(key, value);
	}
	
	public Object getFeature(String key) {
		return this.features.get(key);
	}
	
	public Sentence getTitle() {
		return this.paragraphs.get(0).getSentences().get(0);
	}

//	public void setTitle(Text rawTitle) {
//		this.title =;
//	}
	
	@Override
	public String toString() {
		//return rawText;
		StringBuilder sb = new StringBuilder();
		this.paragraphs.forEach(p -> {
			sb.append(p);
			sb.append("\n\n");
		});
		return sb.toString();
	}
	
}
