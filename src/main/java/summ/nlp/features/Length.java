package summ.nlp.features;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.model.Paragraph;
import summ.model.Sentence;
import summ.model.Text;
import summ.utils.Pipe;

public class Length implements Pipe<Text> {
	
	private static final Logger log = LogManager.getLogger(Length.class);
	
	/**
	 * Sentence length
	 * 
	 * Simple length is the sentence length divided by the length of the 
	 * longest sentence of the text.
	 * 
	 * Calcula a feature comprimento da sentença.
	 *
     *  len = ln(tla - | (tla - tl) / tal |)
	 *
     * Cada parágrafo é quebrado em dois e analisado individualmente.
	 *
     *  tla - média da quantidade de termos das sentenças
     *  tl - quantidade de termos de uma sentença
     *  tal - desvio padrão
	 * 
	 */
	private Text length(Text text) {
		
		int maxLength = 0; double maxRelativeLength = 0.0; 
		
		log.info("Calculating sentences mean length and standard deviation.");
		for (Paragraph p : text.getParagraphs()) {
			int middle = p.getLength() / 2;
			
			DescriptiveStatistics ds1 = new DescriptiveStatistics();
			DescriptiveStatistics ds2 = new DescriptiveStatistics();
			
			for (Sentence s : text.getSentences()) {
				maxLength = s.getEditedSentenceLength() > maxLength
						? s.getEditedSentenceLength() : maxLength;	
				if(s.getPos() <= middle) {
					ds1.addValue(s.getEditedSentenceLength());
				} else {
					ds2.addValue(s.getEditedSentenceLength());
				}		
			}
			
			p.addFeature("tla1", ds1.getMean());
			p.addFeature("tla2", ds2.getMean());
			p.addFeature("tal1", ds1.getStandardDeviation());
			p.addFeature("tal2", ds2.getStandardDeviation());
			
		}
		
		log.info("Calculating simple and relative length features for each sentence.");
		for (Paragraph p : text.getParagraphs()) {
			for (Sentence s : p.getSentences()) {
				int middle = p.getLength() / 2;
				
				double tla, tal;
				int tl = s.getEditedSentenceLength();
				
				if(s.getPos() <= middle) {
					tla = p.getFeature("tla1"); tal = p.getFeature("tal1");
				} else {
					tla = p.getFeature("tla2"); tal = p.getFeature("tal2");
				}
				
				tal = tal == 0 ? tla : tal;
				tal = tal == 0 ? 1 : tal;
				
				double relativeLen = Math.log(tla - Math.abs((tla - tl) / tal));
				
				if(relativeLen > maxRelativeLength) {
					maxRelativeLength = relativeLen;
				}
				
				s.addFeature("len", s.getLength());
				s.addFeature("simple_len", (double)s.getEditedSentenceLength() / maxLength);
				s.addFeature("relative_len", relativeLen);
			}
		}
		
		for (Sentence s : text.getSentences()) {
			s.addFeature("relative_len", (double)s.getFeature("relative_len") / maxRelativeLength);	
		}
		return text;	
	}

	@Override
	public String toString() {
		return "Length";
	}
	
	@Override
	public Text process(Text text) {
		log.info("Calculating length features for " + text.getName());
		return this.length(text);
	}
	
}
