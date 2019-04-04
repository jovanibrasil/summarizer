package summ.nlp.features;

import java.util.Map;

import summ.model.Sentence;
import summ.model.Text;
import summ.model.Word;
import summ.utils.Pipe;

public class Title implements Pipe<Text> {
	
	/**
	 * Title words
	 * 
	 * This method ignores any title.
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
					Map<String, Double> tfIsf = (Map<String, Double>) text.getFeature("tf-isf");
					for (Word w : s.getWords()) {
						if(title.containsWord(w)) {
							wordCount++;
							Double weight = tfIsf.get(w.getProcessedToken());
							summation += Math.pow(weight, 2);
						}
					}
					
					if(summation > maxSummation) {
						maxSummation = summation;
					}
				
				}
				s.addFeature("title-words-counter", wordCount);
				s.addFeature("title-words-relative", summation);
		};
		
		for (Sentence s : text.getSentences()) {
			s.addFeature("title-words-relative", (Double)s.getFeature("title-words-relative") / maxSummation);
		}
		return text;	
	}

	@Override
	public Text process(Text text) {
		return titleWords(text);
	}
	
}
