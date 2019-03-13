package summ.nlp.evaluation;

import java.util.HashMap;

import summ.model.Text;

public class Evaluation {

	public Evaluation() {}
	
	public static HashMap<String, HashMap<String, Object>>  evaluate(Text generatedSummary, 
			Text referenceSummary, EvaluationTypes evaluationType) {
		
		switch (evaluationType) {
			case ROUGE:
				return Rouge.evaluate(generatedSummary, referenceSummary);
			case OVERLAP:
				return Overlap.evaluate(generatedSummary, referenceSummary);
			default:
				break;
		}
		return null;
	}
	
}
