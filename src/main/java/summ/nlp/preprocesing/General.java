package summ.nlp.preprocesing;

import summ.model.Text;
import summ.utils.Pipe;

public class General implements Pipe<Text> {
	
	private PreProcessingTypes preProcessingType;
	
	public General(PreProcessingTypes preProcessingType) {
		this.preProcessingType = preProcessingType;
	}
	
	public Text removePunctuation(Text text) {
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
		}
		return text;
	}
	
}
