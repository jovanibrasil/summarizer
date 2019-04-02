package summ.nlp.features;

import summ.model.Text;
import summ.utils.Pipe;

public class LocLen implements Pipe<Text> {

	/**
	 * Correlation between location and length. 
	 * 
	 */
	public Text locLen(Text text) {
		
		var wrapper = new Object() { Double maxLocLen = 0.0; };
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				Double loc = (Double)s.getFeature("relative-location");
				Double len = (Double)s.getFeature("relative-len");
				Double locLen = (-0.084 + (0.08 * len) + (2.344 * loc));
				s.addFeature("loc-len", locLen);
				wrapper.maxLocLen = locLen > wrapper.maxLocLen 
						? locLen : wrapper.maxLocLen;
			});
		});
		
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				s.addFeature("loc-len", (Double)s.getFeature("loc-len") / wrapper.maxLocLen);
			});
		});
		return text;		
	}

	@Override
	public Text process(Text text) {
		return this.locLen(text);
	}
	
}
