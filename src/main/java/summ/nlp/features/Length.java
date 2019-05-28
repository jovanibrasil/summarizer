package summ.nlp.features;

import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.model.Sentence;
import summ.model.Text;
import summ.utils.Pipe;

public class Length implements Pipe<Text> {

	private static final Logger log = LogManager.getLogger(Length.class);

	/**
	 * Sentence length
	 * 
	 * Simple length is the sentence length divided by the length of the longest
	 * sentence of the text.
	 * 
	 * Calcula a feature comprimento da sentença.
	 *
	 * len = ln(tla - | (tla - tl) / tal |)
	 *
	 * Cada parágrafo é quebrado em dois e analisado individualmente.
	 *
	 * tla - média da quantidade de termos das sentenças tl - quantidade de termos
	 * de uma sentença tal - desvio padrão
	 * 
	 */
	private Text length(Text text) {

		int maxLength = 0;

		// get a list of sentences without titles
		List<Sentence> validSentences = text.getValidSentences();
		int middle = validSentences.size() / 2;
		DescriptiveStatistics ds1 = new DescriptiveStatistics();
		DescriptiveStatistics ds2 = new DescriptiveStatistics();

		log.debug("Calculating sentences mean length and standard deviation.");
		for (Sentence s : validSentences) {
			maxLength = s.getEditedSentenceLength() > maxLength ? s.getEditedSentenceLength() : maxLength;
			if (s.getId() < middle) {
				ds1.addValue(s.getEditedSentenceLength());
			} else {
				ds2.addValue(s.getEditedSentenceLength());
			}
		}

		text.addFeature("tla1", ds1.getMean());
		text.addFeature("tla2", ds2.getMean());
		text.addFeature("tal1", ds1.getStandardDeviation());
		text.addFeature("tal2", ds2.getStandardDeviation());
		
		log.debug("Calculating simple and relative length features for each sentence.");
		double relativeLen = 0.0;
		
		for (Sentence s : text.getSentences()) {
			s.addFeature("len", s.getLength());
			s.addFeature("simple_len", (double) s.getEditedSentenceLength() / maxLength);
			s.addFeature("relative_len", 0.0);
		}
		
		int idx = 0;
		double tla, tal;
		
		double maxRelativeLen = 0.0;
		
		for(Sentence s : text.getValidSentences()) {
			int tl = s.getEditedSentenceLength();
			tla = idx < middle ? (double)text.getFeature("tla1") : (double)text.getFeature("tla2");
			tal = idx < middle ? (double)text.getFeature("tal1") : (double)text.getFeature("tal2");
			relativeLen = Math.log(tla - Math.abs((tla - tl) / tal));
			
 			if (relativeLen > maxRelativeLen) maxRelativeLen = relativeLen;
 			
			s.addFeature("relative_len", relativeLen);			
			idx++;
		}
		for(Sentence s : text.getValidSentences()) {
			s.addFeature("relative_len", (double) s.getFeature("relative_len") / maxRelativeLen);
		}
		return text;
	}

	@Override
	public String toString() {
		return "Length";
	}

	@Override
	public Text process(Text text) {
		log.debug("Calculating length features for " + text.getName());
		return this.length(text);
	}

}
