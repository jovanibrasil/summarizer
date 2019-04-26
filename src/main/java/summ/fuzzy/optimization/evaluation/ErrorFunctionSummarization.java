package summ.fuzzy.optimization.evaluation;

import java.util.ArrayList;

import net.sourceforge.jFuzzyLogic.optimization.ErrorFunction;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import summ.fuzzy.FuzzySystem;
import summ.model.Text;
import summ.nlp.evaluation.Evaluation;
import summ.nlp.evaluation.EvaluationResult;
import summ.summarizer.Summarizer;
import summ.utils.Tuple;

public class ErrorFunctionSummarization extends ErrorFunction {
	
	private FuzzySystem fs;
	private Text originalText;
	private Text referenceSummary;
	private Evaluation evaluation;
	
	public ErrorFunctionSummarization(String fileName, Text originalText, Text referenceSummary, Evaluation evaluation) {
		super();
		this.fs = new FuzzySystem(fileName);
		this.referenceSummary = referenceSummary;
		this.originalText = originalText;
		this.evaluation = evaluation;
	}
	
	public FuzzySystem getFuzzySystem() {
		return this.fs;
	}
	
    public double evaluate(RuleBlock ruleBlock) {
        
        // Generate new summary using the new configuration
     	ArrayList<Tuple<Integer>> sentencesInformativity = Summarizer.computeSentencesInformativity(originalText, this.fs);
        Text generatedSummary = Summarizer.generateSummary(originalText, referenceSummary.getTotalSentence(), sentencesInformativity);
        // Evaluate the result and calculate the error
        EvaluationResult result = evaluation.evaluate(generatedSummary, referenceSummary);
        double error = result.getMetric("fMeasure");
//        System.out.println("Iteratation: " + this.counter + " Error value: " + error + 
//        		" Overlap: " + result + " Reference summary size: " + referenceSummary.getTotalSentence());
//        System.out.println(error);
//        System.out.println(fs);
        return error ;
    }
    
} 