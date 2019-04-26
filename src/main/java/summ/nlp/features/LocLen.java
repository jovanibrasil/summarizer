package summ.nlp.features;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.model.Sentence;
import summ.model.Text;
import summ.utils.Pipe;

public class LocLen implements Pipe<Text> {

	private static final Logger log = LogManager.getLogger(LocLen.class);
	
	/**
	 * Correlation between location and length. 
	 * 
	 */
	public Text locLen(Text text) {
		log.info("Calculating LOC-LEN (location and length correlation) feature for each sentence of " + text.getName());
		double maxLocLen = 0.0;
		for (Sentence s : text.getSentences()) {
			Double loc = (Double)s.getFeature("relative_location");
			Double len = (Double)s.getFeature("relative_len");
			Double locLen = (-0.084 + (0.08 * len) + (2.344 * loc));
			s.addFeature("loc_len", locLen);
			maxLocLen = locLen > maxLocLen ? locLen : maxLocLen;	
		}
		for (Sentence s : text.getSentences()) {
			s.addFeature("loc_len", (double)s.getFeature("loc_len") / maxLocLen);
		}
		return text;		
	}

	@Override
	public Text process(Text text) {
		return this.locLen(text);
	}
	
}
