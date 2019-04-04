package summ.nlp.features;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import summ.model.Paragraph;
import summ.model.Sentence;
import summ.model.Text;
import summ.utils.Pipe;

public class Length implements Pipe<Text> {
	
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
				s.addFeature("simple-len", (double)s.getEditedSentenceLength() / maxLength);
				s.addFeature("relative-len", relativeLen);
			}
		}
		
		for (Sentence s : text.getSentences()) {
			s.addFeature("relative-len", (double)s.getFeature("relative-len") / maxRelativeLength);	
		}
		return text;	
	}

	@Override
	public Text process(Text text) {
		return this.length(text);
	}
	
}
