package summ.nlp.features;

import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.model.Sentence;
import summ.model.Text;
import summ.model.Word;
import summ.utils.Pipe;

public class Title implements Pipe<Text> {
	
	private static final Logger log = LogManager.getLogger(Title.class);
	
	/**
	 * Compute the feature title similarity for each sentence. The value of this
	 * feature is the sum of all words tf-isf that occurs in the title and in the sentence.   
	 * 
	 * @param text
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Text titleWords(Text text) {
		
		Sentence title = text.getTitle();
		int wordCount = 0; Double summation = 0.0; Double maxSummation = 1.0;
		
		for (Sentence s : text.getSentences()) {
				wordCount = 0; 
				summation = 0.0;
				if(!s.isTitle()) {
					Map<String, Double> tfIsf = (Map<String, Double>) text.getFeature("tf_isf");
					for (Word w : s.getWords()) {
						if(title.containsWord(w)) {
							wordCount++;
							Double weight = tfIsf.get(w.getCurrentValue());
							summation += Math.pow(weight, 2);
						}
					}
					if(summation > maxSummation) {
						maxSummation = summation;
					}
				}
				s.addFeature("title_words_counter", wordCount);
				s.addFeature("title_words_relative", summation);
		};
		
		for (Sentence s : text.getSentences()) {
			s.addFeature("title_words_relative", (Double)s.getFeature("title_words_relative") / maxSummation);
		}
		return text;	
	}
	
	@Override
	public String toString() {
		return "Title";
	}

	@Override
	public Text process(Text text) {
		return titleWords(text);
	}
	
}
