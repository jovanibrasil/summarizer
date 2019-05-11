package summ.nlp.evaluation;

import org.jfree.util.Log;

public class EvaluationMethodFactory {

	
	public static EvaluationMethod getEvaluationMethod(EvaluationTypes evaluationType) {
		Log.info("Evaluation type: " + evaluationType.name());
		switch (evaluationType) {
			case FMEASURE:
				return new SentenceOverlap(EvaluationTypes.FMEASURE);
			case RECALL:
				return new SentenceOverlap(EvaluationTypes.RECALL);
			case PRECISION:
				return new SentenceOverlap(EvaluationTypes.PRECISION);
			case ROUGE1:
				return new Rouge(EvaluationTypes.ROUGE1);
			case ROUGE2:
				return new Rouge(EvaluationTypes.ROUGE2);
			case ALL:
				return new SentenceOverlap(EvaluationTypes.ALL);
			default:
				return null;
		}
	}
	
}
