package summ.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import summ.nlp.evaluation.EvaluationResult;
import summ.utils.Tuple;

public class Text {

	private String name;

	private ArrayList<Paragraph> paragraphs;
	private String rawText; 
	private HashMap<String, Object> features;
	private EvaluationResult evaluationResult;
	
	private String fullTextPath;
	private Text referenceSummary;
	
	public int wordCounter;
	
	public Text(String rawText) {
		this.rawText = rawText;
		this.paragraphs = new ArrayList<>();
		this.features = new HashMap<>();
		this.wordCounter = 0;
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
	
	public HashMap<String, Object> getFeatures(){
		return this.features;
	}
	
	public Sentence getTitle() {
		return this.paragraphs.get(0).getSentences().get(0);
	}
	
	public boolean containsSentence(Sentence sentence) {
		for (Paragraph paragraph : paragraphs) {
			for (Sentence s : paragraph.getSentences()) {
				if(s.getInitialValue().equals(sentence.getInitialValue())) {
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
	
	
	public Sentence[] getSentences(){
		Sentence[] sentences = new Sentence[this.getTotalSentence()];
		int i = 0;
		for (Paragraph p : this.paragraphs) {
			for (Sentence s: p.getSentences()) {
				sentences[i++] = s;
			}
		}
		return sentences;
	}
	
	public List<String> getStringSentences(){
		List<String> sentences = new ArrayList<String>();
		for (Paragraph p : this.paragraphs) {
			for (Sentence s: p.getSentences()) {
				sentences.add(s.getInitialValue());
			}
		}
		return sentences;
	}
	
	public Map<Integer, Sentence> getSentencesMap() {
		Map<Integer, Sentence> sentences = new HashMap<Integer, Sentence>();
		for (Paragraph p : this.paragraphs) {
			for (Sentence s: p.getSentences()) {
				sentences.put(s.getId(), s);
			}
		}
		return sentences;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getFullTextPath() {
		return fullTextPath;
	}

	public void setFullTextPath(String fullTextPath) {
		this.fullTextPath = fullTextPath;
	}
	
	public EvaluationResult getEvaluationResult() {
		return evaluationResult;
	}

	public void setEvaluationResult(EvaluationResult evaluationResult) {
		this.evaluationResult = evaluationResult;
	}

	@Override
	public String toString() {
		//return rawText;
		StringBuilder sb = new StringBuilder();
		sb.append("Text content\n");
		this.paragraphs.forEach(p -> {
			sb.append(p);
			sb.append("\n\n");
		});
		return sb.toString();
	}

	public Text getReferenceSummary() {
		return this.referenceSummary;
	}
	
	public void setReferenceSummary(Text referenceSummary) {
		this.referenceSummary = referenceSummary;
	}

	public List<Sentence> getValidSentences() {
		List<Sentence> validSentences = new ArrayList<Sentence>();
		for (Paragraph paragraph : this.paragraphs) {
			for (Sentence sentence : paragraph.getSentences()) {
				if(!sentence.isTitle()) validSentences.add(sentence);
			}
		}
		return validSentences;
	}
	
}
