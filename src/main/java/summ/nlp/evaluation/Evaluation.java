package summ.nlp.evaluation;

import summ.model.Text;

public interface Evaluation {
	public EvaluationResult evaluate(Text generatedText, Text referenceText);
}
