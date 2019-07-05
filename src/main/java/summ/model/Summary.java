package summ.model;

import java.util.ArrayList;

public class Summary {

	private ArrayList<Paragraph> paragraphs;
	
	public Summary(String rawSummary) {
		this.paragraphs = new ArrayList<>();
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
				sb.append(s.getInitialValue());
				sb.append("\n");
			});
		});
		return sb.toString();
	}
	
}
