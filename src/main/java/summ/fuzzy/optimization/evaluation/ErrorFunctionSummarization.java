package summ.fuzzy.optimization.evaluation;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sourceforge.jFuzzyLogic.optimization.ErrorFunction;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import summ.fuzzy.FuzzySystem;
import summ.model.Text;
import summ.nlp.evaluation.EvaluationMethod;
import summ.nlp.evaluation.EvaluationResult;
import summ.summarizer.Summarizer;

public class ErrorFunctionSummarization extends ErrorFunction {
	
	private static final Logger log = LogManager.getLogger(ErrorFunctionSummarization.class);
	
	private FuzzySystem fs;
	private List<String> varNames;
	private List<Text> textList;
	private Summarizer summarizer;	
	private EvaluationMethod evaluationMethod;
	
	public ErrorFunctionSummarization(FuzzySystem fs, Summarizer summarizer, List<Text> texts,
			EvaluationMethod evaluationMethod, List<String> varNames) {
		super();
		this.fs = fs;
		this.evaluationMethod = evaluationMethod;
		this.textList = texts;
		this.varNames = varNames;
		this.summarizer = summarizer;
	}
	
	public FuzzySystem getFuzzySystem() {
		return this.fs;
	}
	
	/**
	 *  Evaluate the result and calculate the error.     
	 * 
	 */
    public double evaluate(RuleBlock ruleBlock) {
        double acc = 0.0;
        for (Text text : textList) {
        	Text generatedSummary = this.summarizer.summarizeText(text, this.fs, varNames);
        	EvaluationResult result = this.summarizer.evaluateSummary(evaluationMethod, generatedSummary, text.getReferenceSummary());
        	log.debug(text.getName() + " - " + result.getEvaluationMetricValue());
            log.trace(result);
            acc += result.getEvaluationMetricValue();
		}
        log.debug("Result " + acc / this.textList.size());
        return acc / this.textList.size();
    }
    
} 