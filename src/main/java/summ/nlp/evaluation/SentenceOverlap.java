package summ.nlp.evaluation;

import java.util.HashMap;

import summ.model.Sentence;
import summ.model.Text;

public class SentenceOverlap {

	/**
	 * Evaluates similarity between two texts using precision, recall, and f-measure. 	 
	 * 
	 * @param generatedText
	 * @param referenceText 
	 * @return a HashMap with the metrics: precision, recall, f-measure; and with the counters: retrievedSentences, 
	 * relevantSentences, correctSentences
	 */
	public static HashMap<String, Double>  evaluate(Text generatedText, Text referenceText) {
		
		double retrievedSentences = generatedText.getTotalSentence();
		double relevantSentences = referenceText.getTotalSentence();
		double correctSentences = countOverlappedSentences(generatedText, referenceText); 
		
		HashMap<String, Double> result = calculateMetrics(correctSentences, retrievedSentences, relevantSentences);
		result.put("retrievedSentences", (double)retrievedSentences);
		result.put("relevantSentences", (double)relevantSentences);
		result.put("correctSentences", (double)correctSentences);
		return result;
	}
	
	/**
	 * Calculates precision, recall and f-measure.
	 * 
	 * @param relevantSentences is the number of sentences in the reference summary.
	 * @param retrievedSentences is the number of sentences extracted by the summarizer system.
	 * @param correctSentences is the number of retrieved sentences that are relevant and retrieved sentences.
	 * @return a HashMap with the precision, recall, and f-measure values.
	 */
	public static HashMap<String, Double> calculateMetrics(double relevantSentences, double retrievedSentences,  double correctSentences){
		
		double precision = 0.0, recall = 0.0, fMeasure = 0.0;
		
		if(relevantSentences != 0 && retrievedSentences != 0 && correctSentences != 0) {
			// precision = number of correct sentences extracted by the summarizer system / number of retrieved sentences
			precision = correctSentences / retrievedSentences;
			// Recall =  number of correct sentences extracted by the summarizer system / number of relevant sentences					 
			recall = correctSentences / relevantSentences;
			// F = (2 * P * R) / (P + R)
			fMeasure = 2 * ((precision * recall) / (precision + recall));
		}	
		
		HashMap<String, Double> result = new HashMap<>();
		result.put("precision", precision);
		result.put("recall", recall);
		result.put("fMeasure", fMeasure);
		return result;
		
	}
	
	/**
	 * 
	 * Calculates the number overlapped sentences between the texts. 
	 * 
	 * @param generatedText
	 * @param referenceText
	 * @return is a counter of overlaps.
	 */
	public static int countOverlappedSentences(Text generatedText, Text referenceText) {
		int overlap = 0;
		for (Sentence s : generatedText.getSentences()) {			
			if(referenceText.containsSentence(s)) {
				overlap++;
			}
		}
		return overlap;
	}
	
}
