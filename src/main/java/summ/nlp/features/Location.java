package summ.nlp.features;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.model.Text;
import summ.utils.Pipe;

public class Location implements Pipe<Text> {
	
	private static final Logger log = LogManager.getLogger(Location.class);
	
	/**
	 * Simple sentence location and the relative sentence location.
	 *
	 * Razão entre posição no parágrafo e tamanho da sentença
	 *
	 */
	private Text location(Text text) {
		log.debug("Calculating simple, inverse and relative locations features for each sentence.");
		text.getParagraphs().forEach(p -> {
			int middle = p.getLength() / 2;
			p.getSentences().forEach(s -> {
				
				Double pos = (double)p.getLength() - s.getPos();
				
				Double simpleLocation = pos / p.getLength();
				s.addFeature("simple_location", simpleLocation);
				
				Double inverseLocation = 1 - (pos-1) / p.getLength();
				s.addFeature("inverse_location", inverseLocation); 
				
				Double relativeLocation = s.getPos() <= middle ? pos / p.getLength() 
						:  1 - ((pos - 1) / p.getLength());
				s.addFeature("relative_location", relativeLocation); 
				
			});
		});
		return text;
	}
	
	@Override
	public String toString() {
		return "Location";
	}

	@Override
	public Text process(Text input) {
		return location(input);
	}
	
}
