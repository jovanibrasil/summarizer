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
			case ROUGE1_P:
				return new Rouge(EvaluationTypes.ROUGE1, EvaluationTypes.PRECISION);
			case ROUGE1_R:
				return new Rouge(EvaluationTypes.ROUGE1, EvaluationTypes.RECALL);
			case ROUGE1_F:
				return new Rouge(EvaluationTypes.ROUGE1, EvaluationTypes.FMEASURE);
			case ALL:
				return new SentenceOverlap(EvaluationTypes.ALL);
			default:
				return null;
		}
	}
	
}
