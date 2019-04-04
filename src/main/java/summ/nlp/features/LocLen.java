package summ.nlp.features;

import summ.model.Sentence;
import summ.model.Text;
import summ.utils.Pipe;

public class LocLen implements Pipe<Text> {

	/**
	 * Correlation between location and length. 
	 * 
	 */
	public Text locLen(Text text) {
		
		double maxLocLen = 0.0;
		for (Sentence s : text.getSentences()) {
			Double loc = (Double)s.getFeature("relative-location");
			Double len = (Double)s.getFeature("relative-len");
			Double locLen = (-0.084 + (0.08 * len) + (2.344 * loc));
			s.addFeature("loc-len", locLen);
			maxLocLen = locLen > maxLocLen ? locLen : maxLocLen;	
		}
		for (Sentence s : text.getSentences()) {
			s.addFeature("loc-len", (double)s.getFeature("loc-len") / maxLocLen);
		}
		return text;		
	}

	@Override
	public Text process(Text text) {
		return this.locLen(text);
	}
	
}
