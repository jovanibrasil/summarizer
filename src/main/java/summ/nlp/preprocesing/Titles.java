package summ.nlp.preprocesing;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.model.Text;
import summ.utils.MathUtils;
import summ.utils.Pipe;

public class Titles implements Pipe<Text> {

	private static final Logger log = LogManager.getLogger(Titles.class);
	
	/*
	 * Verify if a sentence is a title. A sentence is a title if it begins with number OR if sentence length is less 
	 * than the average sentence length and the paragraph has only one sentence.
	 * 
	 */
	public static Text identifyTitles(Text text) {
		log.info("Searching and identifying text titles.");
		text.getParagraphs().forEach( paragraph -> {
			paragraph.getSentences().forEach(sentence -> {
				// TODO sentença pode não ter valores após o pre-processamento
				String firstWord = sentence.getFirstWord().getInitialValue();
				int maxSentenceLength = 5; // TODO calculate dynamically the sentence length BEFORE the execution of this method
				if(MathUtils.isNumeric(firstWord) || 
						paragraph.getLength() == 1 && sentence.getLength() < maxSentenceLength) {
					sentence.setTitle(true);
				} else {
					sentence.setTitle(false);
				}
			});
		});
		
		return text;
	}

	@Override
	public Text process(Text text) {
		return identifyTitles(text);
	}
	
}
