package summ.nlp.evaluation;

public class EvaluationMethodFactory {

	public enum EvaluationMethodType {
		OVERLAP, OVERLAP_P, OVERLAP_R, OVERLAP_F, ROUGE1, ROUGE2, NONE
	}
	
	public static EvaluationMethod getEvaluationMethod(EvaluationMethodType evaluationType) {
		switch (evaluationType) {
			case OVERLAP_F:
				return new SentenceOverlap();
			case OVERLAP_R:
				return new SentenceOverlap();
			case OVERLAP_P:
				return new SentenceOverlap();
			case OVERLAP:
				return new SentenceOverlap();
			default:
				return new SentenceOverlap();
		}
	}
	
}
