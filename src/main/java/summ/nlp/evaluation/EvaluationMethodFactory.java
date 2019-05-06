package summ.nlp.evaluation;

import org.jfree.util.Log;

public class EvaluationMethodFactory {

	public enum EvaluationMethodType {
		ALL, PRECISION, RECALL, FMEASURE, ROUGE1, ROUGE2, NONE
	}
	
	public static EvaluationMethod getEvaluationMethod(EvaluationMethodType evaluationType) {
		Log.info("Evaluation type: " + evaluationType.name());
		switch (evaluationType) {
			case FMEASURE:
				return new SentenceOverlap(EvaluationMethodType.FMEASURE);
			case RECALL:
				return new SentenceOverlap(EvaluationMethodType.RECALL);
			case PRECISION:
				return new SentenceOverlap(EvaluationMethodType.PRECISION);
			case ALL:
				return new SentenceOverlap(EvaluationMethodType.ALL);
			default:
				return new SentenceOverlap(EvaluationMethodType.ALL);
		}
	}
	
}
