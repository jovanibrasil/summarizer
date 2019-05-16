package summ.fuzzy.optimization.evaluation;

import java.util.List;

import org.apache.commons.math3.linear.RealVector;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.fuzzy.FuzzySystem;
import summ.model.Text;
import summ.nlp.evaluation.EvaluationMethod;
import summ.nlp.evaluation.EvaluationResult;
import summ.summarizer.Summarizer;

public class ErrorFunctionSummarization implements ErrorFunction {
	
	private static final Logger log = LogManager.getLogger(ErrorFunctionSummarization.class);
	
	private List<String> varNames;
	private List<Text> textList;
	private Summarizer summarizer;	
	private EvaluationMethod evaluationMethod;
	
	public ErrorFunctionSummarization(Summarizer summarizer, List<Text> texts,
			EvaluationMethod evaluationMethod, List<String> varNames) {
		super();
		this.evaluationMethod = evaluationMethod;
		this.textList = texts;
		this.varNames = varNames;
		this.summarizer = summarizer;
	}
	
	public double functionsEvaluation(RealVector genes) {
		
//		double a = term.getParameter(0);
//		double max, min;
	
		for (int i = 0; i < genes.getDimension(); i+=9) {
			
			//i+2 + i+0 	<	 i+5 - i-3
			//i+5 + i+3 	<	 i+8 - i-3
			
			
			
//			// Testa se o valor da parâmetro c é válido através da comparação com o centro dos outros termos
//			if(term.getTermName().equals("baixo")) {
//				max = genes.getEntry(6) variable.getLinguisticTerm("medio").getParameter(2);
//				if(value + a < max) return true; // menor que o primeiro
//				return false;
//			} if(term.getTermName().equals("medio")) {
//				min = variable.getLinguisticTerm("baixo").getParameter(2);
//				max = variable.getLinguisticTerm("alto").getParameter(2);
//				if(value - a > min && value + a < max) return true;
//				return false;
//			} else {
//				max = variable.getLinguisticTerm("medio").getParameter(2);
//				if(value - a > max) return true; // maior que o ultimo
//				return false;
//			}
			
		}
		
		return 0.0;
	}
	
	/**
	 *  Evaluate the result and calculate the error.     
	 * 
	 */
    public double evaluate(FuzzySystem fuzzySystem) {
        double acc = 0.0;
        for (Text text : textList) {
        	Text generatedSummary = this.summarizer.summarizeText(text, fuzzySystem, varNames);
        	EvaluationResult result = this.summarizer.evaluateSummary(evaluationMethod, generatedSummary, text.getReferenceSummary());
        	log.debug(text.getName() + " - " + result.getEvaluationMetricValue());
            log.trace(result);
            acc += result.getEvaluationMetricValue();
		}
        log.debug("Result " + acc / this.textList.size());
        return acc / this.textList.size();
    }
    
} 