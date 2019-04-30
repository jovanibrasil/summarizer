package summ.fuzzy.optimization.evaluation;

import java.nio.file.Path;
import java.util.ArrayList;
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
import summ.utils.Tuple;

public class ErrorFunctionSummarization extends ErrorFunction {
	
	private static final Logger log = LogManager.getLogger(ErrorFunctionSummarization.class);
	
	private FuzzySystem fs;
	//private EvaluationMethod evaluation;
	private List<String> varNames;
	private List<Text> textList;
	private String metricName = "fMeasure";
	private Summarizer summarizer;
		
	public ErrorFunctionSummarization(FuzzySystem fs, Summarizer summarizer, List<Text> texts, EvaluationMethod evaluation, List<String> varNames) {
		super();
		this.fs = fs;
		this.textList = texts;
		//this.evaluation = evaluation;
		this.varNames = varNames;
		this.summarizer = summarizer;
		log.info("Initializing text summarization evaluation function ...");
		log.info("Metric name: " + this.metricName + " Reference vector size: " + this.textList.size());
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
        	
        	Text generatedSummary = this.summarizer.summarizeText(text, varNames);
        	EvaluationResult result = this.summarizer.evaluateSummary(generatedSummary, text.getReferenceSummary());
        	
            log.debug(result);
            acc += result.getMetric(this.metricName);
		}
        return acc / this.textList.size();
    }
    
} 