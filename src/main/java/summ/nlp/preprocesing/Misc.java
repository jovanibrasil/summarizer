package summ.nlp.preprocesing;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.model.Sentence;
import summ.model.Text;
import summ.model.Word;
import summ.utils.Pipe;

public class Misc implements Pipe<Text> {

	private static final Logger log = LogManager.getLogger(Misc.class);
	private PreProcessingTypes preProcessingType;

	public Misc(PreProcessingTypes preProcessingType) {
		this.preProcessingType = preProcessingType;
	}

	public String removePunctuation(String sentence) {
		log.debug("Removing text punctuation.");
		return sentence.replaceAll("\\p{Punct}", "").replace("º", "");
	}

	public String removeNumbers(String sentence) {
		log.debug("Removing numbers.");
		return sentence.replaceAll("[0-9.]","");
	}
	
	public String convertToLowerCase(String sentence) {
		log.debug("Converting text to lower case.");
		return sentence.toLowerCase();
	}
	
	private String removeMoney(String currentValue) {
		log.debug("Removing money");
		throw new UnsupportedOperationException();
	}

	private String removeDates(String currentValue) {
		log.debug("Removing dates");
		throw new UnsupportedOperationException();
	}
	
	private String removeEmptyWords(Sentence s) {
		log.debug("Removing empty words.");
		List<Word> words = new ArrayList<Word>();
		s.getWords().forEach(w -> { if(!w.getCurrentValue().isEmpty()) { words.add(w); }  });
		s.setWords(words);
		return s.getCurrentValue();
	}

	@Override
	public String toString() {
		return this.preProcessingType.toString();
	}

	@Override
	public Text process(Text text) {
		try {
			for (Sentence sentence : text.getSentences()) {
				String editedSentence = "";
				switch (this.preProcessingType) {
					case REMOVE_PUNCTUATION:
						editedSentence = removePunctuation(sentence.getCurrentValue());
						break;
					case TO_LOWER_CASE:
						editedSentence = convertToLowerCase(sentence.getCurrentValue());
						break;
					case REMOVE_NUMBERS:
						editedSentence = removeNumbers(sentence.getCurrentValue());
						break;
					case REMOVE_DATES:
						editedSentence = removeDates(sentence.getCurrentValue());
						break;
					case REMOVE_MONEY:
						editedSentence = removeMoney(sentence.getCurrentValue());
						break;
					case REMOVE_EMPTY_WORDS:
						editedSentence = removeEmptyWords(sentence);
						break;
					default:
						break;
				}
				
				sentence.setCurrentValue(editedSentence);	
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return text;
	}

}
