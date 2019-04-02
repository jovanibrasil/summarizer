package summ.nlp.features;

import summ.model.Text;
import summ.utils.Pipe;

public class Location implements Pipe<Text> {
	/**
	 * Simple sentence location and the relative sentence location.
	 *
	 * Razão entre posição no parágrafo e tamanho da sentença
	 *
	 */
	private Text location(Text text) {
	
		text.getParagraphs().forEach(p -> {
			int middle = p.getLength() / 2;
			p.getSentences().forEach(s -> {
				
				Double pos = (double)p.getLength() - s.getPos();
				
				Double simpleLocation = pos / p.getLength();
				s.addFeature("simple-location", simpleLocation);
				
				Double inverseLocation = 1 - (pos-1) / p.getLength();
				s.addFeature("inverse-location", inverseLocation); 
				
				Double relativeLocation = s.getPos() <= middle ? pos / p.getLength() 
						:  1 - ((pos - 1) / p.getLength());
				s.addFeature("relative-location", relativeLocation); 
				
			});
		});
		return text;
	}

	@Override
	public Text process(Text input) {
		return location(input);
	}
	
}
