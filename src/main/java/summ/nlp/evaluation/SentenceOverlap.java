package summ.nlp.evaluation;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.model.Sentence;
import summ.model.Text;

public class SentenceOverlap implements EvaluationMethod {

	private static final Logger log = LogManager.getLogger(SentenceOverlap.class);
	private EvaluationTypes evaluationType;
	
	public SentenceOverlap(EvaluationTypes evaluationType) {
		this.evaluationType = evaluationType;
	}
	
	/**
	 * Evaluates similarity between two texts using precision, recall, and f-measure. 	 
	 * 
	 * @param generatedText
	 * @param referenceText 
	 * @return an Evaluation object with the metrics: precision, recall, f-measure; and with the counters: retrievedSentences, 
	 * relevantSentences, correctSentences
	 */
	public EvaluationResult evaluate(Text generatedText, Text referenceText) {
		
		double relevantSentences = referenceText.getTotalSentence();
		double retrievedSentences = generatedText.getTotalSentence();
		double correctSentences = countOverlappedSentences(generatedText, referenceText); 
		
		EvaluationResult eval = calculateMetrics(relevantSentences, retrievedSentences, correctSentences);
		eval.setMainEvaluationMetric(this.evaluationType);
		eval.addMetric("retrievedSentences", (double)retrievedSentences);
		eval.addMetric("relevantSentences", (double)relevantSentences);
		eval.addMetric("correctSentences", (double)correctSentences);
		return eval;
	}
	
	/**
	 * Calculates precision, recall and f-measure.
	 * 
	 * @param relevantSentences is the number of sentences in the reference summary.
	 * @param retrievedSentences is the number of sentences extracted by the summarizer system.
	 * @param correctSentences is the number of retrieved sentences that are relevant and retrieved sentences.
	 * @return an Evaluation object with the precision, recall, and f-measure values.
	 */
	public EvaluationResult calculateMetrics(double relevantSentences, double retrievedSentences,  double correctSentences){
		
		double precision = 0.0, recall = 0.0, fMeasure = 0.0;
		
		if(relevantSentences != 0 && retrievedSentences != 0 && correctSentences != 0) {
			// precision = number of correct sentences extracted by the summarizer system / number of retrieved sentences
			precision = correctSentences / retrievedSentences;
			// Recall =  number of correct sentences extracted by the summarizer system / number of relevant sentences					 
			recall = correctSentences / relevantSentences;
			// F = (2 * P * R) / (P + R)
			fMeasure = 2 * ((precision * recall) / (precision + recall));
		}	
		
		EvaluationResult eval = new EvaluationResult();
		eval.setMainEvaluationMetric(this.evaluationType);
		eval.addMetric(EvaluationTypes.PRECISION.name(), precision);
		eval.addMetric(EvaluationTypes.RECALL.name(), recall);
		eval.addMetric(EvaluationTypes.FMEASURE.name(), fMeasure);
		return eval;
		
	}
	
	/**
	 * 
	 * Calculates the number overlapped sentences between the texts. 
	 * 
	 * @param generatedText
	 * @param referenceText
	 * @return is a counter of overlaps.
	 */
	public int countOverlappedSentences(Text generatedText, Text referenceText) {
		int overlap = 0;
		for (Sentence s : generatedText.getSentences()) {			
			if(referenceText.containsSentence(s)) {
				overlap++;
			}
		}
		return overlap;
	}
	
	@Override
	public String toString() {
		return "Sentence overlap " + this.evaluationType.name();
	}
	
}
