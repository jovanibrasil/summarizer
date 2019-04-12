package summ.fuzzy.optimization;

import java.util.ArrayList;
import java.util.HashMap;

import net.sourceforge.jFuzzyLogic.optimization.ErrorFunction;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import summ.fuzzy.FuzzySystem;
import summ.model.Text;
import summ.nlp.evaluation.Evaluation;
import summ.nlp.evaluation.EvaluationResult;
import summ.nlp.evaluation.EvaluationTypes;
import summ.nlp.evaluation.SentenceOverlap;
import summ.summarizer.Summarizer;
import summ.utils.Tuple;

class ErrorFunctionSummarization extends ErrorFunction {
	
	private FuzzySystem fs;
	private Text originalText;
	private Text referenceSummary;
	private int counter = 0;
	private Evaluation evaluation;
	
	public ErrorFunctionSummarization(String fileName, Text originalText, Text referenceSummary) {
		super();
		this.fs = new FuzzySystem(fileName);
		this.referenceSummary = referenceSummary;
		this.originalText = originalText;
	}
	
    public double evaluate(RuleBlock ruleBlock) {
        // Update fuzzy system
        fs.updateSystem(ruleBlock.getFunctionBlock()); 
        // Generate new summary using the new configuration
     	ArrayList<Tuple<Integer, Double>> sentencesInformativity = Summarizer.computeSentencesInformativity(originalText, this.fs);
        Text generatedSummary = Summarizer.generateSummary(originalText, referenceSummary.getTotalSentence(), sentencesInformativity);
        // Evaluate the result and calculate the error
        EvaluationResult result = evaluation.evaluate(generatedSummary, referenceSummary);
        double error = (double)(referenceSummary.getTotalSentence() - result.getMetric("fMeasure")) 
        			/ referenceSummary.getTotalSentence();
        this.counter++;
        System.out.println("Iteratation: " + this.counter + " Error value: " + error + 
        		" Overlap: " + result + " Reference summary size: " + referenceSummary.getTotalSentence());
        return error ;
    }
    
} 