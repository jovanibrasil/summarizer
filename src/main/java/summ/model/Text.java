package summ.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Text {

	private String name;

	private ArrayList<Paragraph> paragraphs;
	private String rawText; 
	
	private HashMap<String, Object> features;

	public Text(String rawText) {
		this.rawText = rawText;
		this.paragraphs = new ArrayList<>();
		this.features = new HashMap<>();
	}
	
	public ArrayList<Paragraph> getParagraphs() {
		return paragraphs;
	}

	public void addParagraph(Paragraph paragraph) {
		this.paragraphs.add(paragraph);
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
	
	public boolean containsSentence(Sentence sentence) {
		for (Paragraph paragraph : paragraphs) {
			for (Sentence s : paragraph.getSentences()) {
				if(s.getRawSentence().equals(sentence.getRawSentence())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Sentence getSentenceById(int id) {
		for (Paragraph paragraph : paragraphs) {
			for(Sentence sentence : paragraph.getSentences()) {
				if(sentence.getId() == id) {
					return sentence;
				}				
			}
		}
		return null;
	}
	
	public int getTotalSentence() {
		int total = 0;
		for (Paragraph paragraph : paragraphs) {
			total += paragraph.getSentences().size();
		}
		return total;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
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
