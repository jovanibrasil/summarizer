package summ.nlp.evaluation;

import summ.model.Text;

public interface EvaluationMethod {
	public EvaluationResult evaluate(Text generatedText, Text referenceText, String outputPath);
}
