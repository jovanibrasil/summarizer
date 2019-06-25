package summ.nlp.evaluation;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.model.Sentence;
import summ.model.Text;

public class SimpleRouge implements EvaluationMethod {

	private static final Logger log = LogManager.getLogger(SimpleRouge.class);
	@SuppressWarnings("unused")
	private EvaluationTypes rougeType; // ROUGE-1, ROUGE-2, ROUGE-L ...
	private EvaluationTypes evaluationType;
	
	public SimpleRouge(EvaluationTypes rougeType, EvaluationTypes evaluationType) {
		this.rougeType = rougeType;
		this.evaluationType = evaluationType;
	}	
	
	
	@Override
	public String toString() {
		return "Rouge";
	}

	@Override
	public EvaluationResult evaluate(Text generatedText, Text referenceText, String outputPath) {
		log.debug("Rouge evaluation ...");
//		long startTime = System.nanoTime();
		
		EvaluationResult eval = new EvaluationResult();
		eval.setMainEvaluationMetric(this.evaluationType);

		double overlappCounter = 0;
		double gSummWordCounter = 0;
		for (Sentence s : generatedText.getSentences()) {
			for (String word : s.wordList) {
				if(referenceText.wordSet.contains(word)) overlappCounter +=1;
				gSummWordCounter += 1;
			}
		}
		
		double recall = overlappCounter / referenceText.wordCounter;
		double precision = overlappCounter / gSummWordCounter;
		double fmeasure = (2 * precision * recall) / (precision+recall);
		log.debug("1-gram hit: " + overlappCounter + " 1-gram model count: " + referenceText.wordCounter +
				" 1-gram peer count: " + gSummWordCounter);
		log.debug("ROUGE-1-R: " + recall + " ROUGE-1-P: " + precision + " ROUGE-1-F: " + fmeasure);
		
		eval.addMetric("RECALL", recall);
		eval.addMetric("PRECISION", precision);
		eval.addMetric("FMEASURE", fmeasure);
		
//		long timeElapsed = (System.nanoTime() - startTime) / 1000000;
//		log.info("ROUGE evaluation time (s) : " + timeElapsed);
//		log.info(eval);
		
		//eval.setMainEvaluationMetric(this.evaluationType);
		return eval;
	}
	
}
