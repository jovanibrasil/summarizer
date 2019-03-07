package summ.nlp.evaluation;

import summ.model.Text;

public class Evaluation {

	public Evaluation() {}
	
	public static void evaluate(Text generatedSummary, Text referenceSummary, EvaluationTypes evaluationType) {
		
		switch (evaluationType) {
			case ROUGE:
				Rouge.evaluate(generatedSummary, referenceSummary);
				break;
			case OVERLAP:
				Overlap.evaluate(generatedSummary, referenceSummary);
				break;
			default:
				break;
		}
		
	}
	
}
