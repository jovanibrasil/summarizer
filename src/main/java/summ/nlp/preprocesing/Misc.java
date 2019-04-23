package summ.nlp.preprocesing;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.model.Text;
import summ.utils.Pipe;

public class Misc implements Pipe<Text> {
	
	private static final Logger log = LogManager.getLogger(Misc.class);
	private PreProcessingTypes preProcessingType;
	
	public Misc(PreProcessingTypes preProcessingType) {
		this.preProcessingType = preProcessingType;
	}
	
	public Text removePunctuation(Text text) {
		log.info("Removing text punctuation.");
		try {
			text.getParagraphs().forEach( paragraph -> {
				paragraph.getSentences().forEach(sentence -> {
					String editedSentence = sentence.getCurrentValue().replaceAll("\\p{Punct}", "");
					sentence.setCurrentValue(editedSentence);
				});
			});
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return text;
	}
	
	public Text convertToLowerCase(Text text) {
		log.info("Converting text to lower case.");
		try {
			text.getParagraphs().forEach( paragraph -> {
				paragraph.getSentences().forEach(sentence -> {
					String editedSentence = sentence.getCurrentValue().toLowerCase();
					sentence.setCurrentValue(editedSentence);
				});
			});
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return text;
	}

	@Override
	public Text process(Text text) {
		switch (this.preProcessingType) {
			case REMOVE_PUNCTUATION:
				return removePunctuation(text);
			case TO_LOWER_CASE:
				return convertToLowerCase(text);
			default:
				break;
		}
		return text;
	}
	
}
