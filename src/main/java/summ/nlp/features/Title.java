package summ.nlp.features;

import java.util.Map;

import summ.model.Sentence;
import summ.model.Text;
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
		
		var wrapper = new Object() { int wordCount = 0; Double summation = 0.0; Double maxSummation = 1.0; };
		
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				
				wrapper.wordCount = 0; 
				wrapper.summation = 0.0;
			
				if(!s.isTitle()) {
				
					Map<String, Double> tfIsf = (Map<String, Double>) text.getFeature("tf-isf");
					s.getWords().forEach(w -> {
						if(title.containsWord(w)) {
							wrapper.wordCount++;
							Double weight = tfIsf.get(w.getRawWord());
							wrapper.summation += Math.pow(weight, 2);
						}
					});
					
					if(wrapper.summation > wrapper.maxSummation) {
						wrapper.maxSummation = wrapper.summation;
					}
				
				}
			
				s.addFeature("title-words-counter", wrapper.wordCount);
				s.addFeature("title-words-relative", wrapper.summation);
				
			});
		});
		
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				s.addFeature("title-words-relative", (Double)s.getFeature("title-words-relative") / wrapper.maxSummation);
			});
		});
		return text;	
	}

	@Override
	public Text process(Text text) {
		return titleWords(text);
	}
	
}
