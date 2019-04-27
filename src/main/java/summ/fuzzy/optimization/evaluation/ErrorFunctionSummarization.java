package summ.fuzzy.optimization.evaluation;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sourceforge.jFuzzyLogic.optimization.ErrorFunction;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import summ.fuzzy.FuzzySystem;
import summ.model.Text;
import summ.nlp.evaluation.Evaluation;
import summ.nlp.evaluation.EvaluationResult;
import summ.summarizer.Summarizer;
import summ.utils.Tuple;

public class ErrorFunctionSummarization extends ErrorFunction {
	
	private static final Logger log = LogManager.getLogger(ErrorFunctionSummarization.class);
	
	private FuzzySystem fs;
	private Evaluation evaluation;
	private List<String> varNames;
	private List<Text> originalTexts;
	private List<Text> referenceSummaries;
	private String metricName = "fMeasure";
		
	public ErrorFunctionSummarization(FuzzySystem fs, List<Text> texts, List<Text> refSummaries, 
			Evaluation evaluation, List<String> varNames) {
		super();
		this.fs = fs;
		this.referenceSummaries = refSummaries;
		this.originalTexts = texts;
		this.evaluation = evaluation;
		this.varNames = varNames;
		log.info("Initializing text summarization evaluation function ...");
		log.info("Metric name: " + this.metricName + " Reference vector size: " + this.referenceSummaries.size());
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
     	for (int i = 0; i < this.originalTexts.size(); i++) {
     		Text originalText = this.originalTexts.get(i);
			Text referenceSummary = this.referenceSummaries.get(i);
     		ArrayList<Tuple<Integer>> sentencesInformativity = Summarizer.computeSentencesInformativity(originalText, this.fs, this.varNames);
         	Text generatedSummary = Summarizer.generateSummary(originalText, referenceSummary.getTotalSentence(), sentencesInformativity);
            EvaluationResult result = evaluation.evaluate(generatedSummary, referenceSummary);
            log.debug(result);
            acc += result.getMetric(this.metricName);
		}
        
        double error = acc / this.referenceSummaries.size();

		//      log.info(result);
		//      System.out.println("Iteratation: " + this.counter + " Error value: " + error + 
		//        		" Overlap: " + result + " Reference summary size: " + referenceSummary.getTotalSentence());
		//      System.out.println(error);
		//      System.out.println(fs);
		        
        return error ;
    }
    
} 