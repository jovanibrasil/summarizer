package summ.nlp.features;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.model.Sentence;
import summ.model.Text;
import summ.utils.Pipe;

public class Location implements Pipe<Text> {
	
	private static final Logger log = LogManager.getLogger(Location.class);
	
	public double relativeLocation(int blocksLength, double pos) {
		int block1Size = blocksLength == 1 ? 1 : blocksLength / 2;
		int block2Size = blocksLength == 1 ? 0 : blocksLength - block1Size;
		return pos <= block1Size ? 1 - (pos - 1) / block1Size
				: (pos - block1Size) / block2Size;
	}
	
	/**
	 * Simple sentence location and the relative sentence location.
	 *
	 * Razão entre posição no parágrafo e tamanho da sentença
	 *
	 */
	public Text location(Text text) {
		log.debug("Calculating simple, inverse and relative locations features for each sentence.");
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				double simpleLocation = (double)s.getPos() / p.getLength();
				s.addFeature("simple_location", simpleLocation);
				double inverseLocation = 1 - ((p.getLength() - s.getPos())-1) / p.getLength();
				s.addFeature("inverse_location", inverseLocation); 
				s.addFeature("relative_location", 0.0); // initialize relative location
			});
		});
		List<Sentence> sentences = text.getValidSentences();
		int index = 1;
		for (Sentence sentence : sentences) {
			double relativeLocation = relativeLocation(sentences.size(), index++);		
			sentence.addFeature("relative_location", relativeLocation);	
		}
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
