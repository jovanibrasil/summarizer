package summ.nlp.preprocesing;

import summ.model.Text;
import summ.utils.Utils;

public class Titles {

	/*
	 * Verify if a sentence is a title. A sentence is a title if it begins with number OR if sentence length is less 
	 * than the average sentence length and the paragraph has only one sentence.
	 * 
	 */
	public static Text identifyTitles(Text text) {
		
		text.getParagraphs().forEach( paragraph -> {
			paragraph.getSentences().forEach(sentence -> {
				String firstWord = sentence.getWords().get(0).getRawWord();
				int maxSentenceLength = 5; // TODO calculate dynamically the sentence length BEFORE the execution of this method
				if(Utils.isNumeric(firstWord) || 
						paragraph.getLength() == 1 && sentence.getLength() < maxSentenceLength) {
					sentence.setTitle(true);
				} else {
					sentence.setTitle(false);
				}
			});
		});
		
		return text;
	}
	
}
